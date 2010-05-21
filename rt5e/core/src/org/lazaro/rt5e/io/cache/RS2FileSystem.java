/**
 * Copyright (C) 2010 Lazaro Brito
 *
 * This file is part of RT5E.
 *
 * RT5E is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RT5E is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RT5E.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.lazaro.rt5e.io.cache;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.zip.CRC32;

/**
 * @author Lazaro, Defyboy
 */
public class RS2FileSystem {
    private Cache cache;
    private RandomAccessFile cacheFile;
    private RS2FileDescriptorTable descriptorTable = null;
    private int id;
    private RandomAccessFile indexFile;

    public RS2FileSystem(int id, RandomAccessFile cacheFile,
                         RandomAccessFile indexFile, Cache cache) {
        this.id = id;
        this.cacheFile = cacheFile;
        this.indexFile = indexFile;
        this.cache = cache;
    }

    public RandomAccessFile getCacheFile() {
        return cacheFile;
    }

    public RS2FileDescriptorTable getDescriptorTable() {
        return descriptorTable;
    }

    public RS2File getFile(int fileId) {
        try {
            ByteBuffer index = indexFile.getChannel().map(
                    FileChannel.MapMode.READ_ONLY, 6 * fileId, 6);
            int length = ((index.get() & 0xFF) << 16) | ((index.get() & 0xFF) << 8) | (index.get() & 0xFF);
            int position = ((index.get() & 0xFF) << 16) | ((index.get() & 0xFF) << 8) | (index.get() & 0xFF);


            if (length == 0) {
                return null;
            }

            ByteBuffer buffer = ByteBuffer.allocate(length);
            int remaining = length;
            int offset = 0;

            while (remaining > 0) {
                int amount = remaining;
                if (amount > 512) {
                    amount = 512;
                }

                ByteBuffer block = cacheFile.getChannel().map(
                        FileChannel.MapMode.READ_ONLY, position * 520, amount + 8);

                int nextFileId = block.getShort() & 0xffff;
                int currentOffset = block.getShort() & 0xffff;
                position = ((block.get() & 0xFF) << 16) | ((block.get() & 0xFF) << 8) | (block.get() & 0xFF);
                int nextFileSystemId = block.get() & 0xff;

                /* Header checks */
                if (currentOffset != offset) {
                    throw new IOException("Invalid offset read!");
                }

                if (position < 0) {
                    throw new IOException("Unexpected block position!");
                }

                byte[] data = new byte[amount];
                block.get(data, 0, amount);
                buffer.put(data);

                remaining -= amount;

                /* Checks for next block details */
                if (nextFileId != fileId) {
                    throw new IOException("Invalid next file id read! [cache=" + id + ", file=" + fileId + ", next file id=" + nextFileId + "]");
                }

                if (nextFileSystemId != id) {
                    throw new IOException(
                            "Invalid next file system id read! [cache=" + id + ", file=" + fileId + ", next cache=" + nextFileSystemId + "]");
                }
                offset++;
            }
            buffer.flip();

            int compression = buffer.get() & 0xff;
            int fileLength = buffer.getInt();
            try {
                byte[] file = new byte[compression != RS2File.COMPRESSION_NONE ? fileLength + 4
                        : fileLength];
                buffer.get(file);
                ByteBuffer fileBuffer = ByteBuffer.wrap(file);
                return new RS2File(id, fileId, compression, fileLength, fileBuffer);

            } catch (Exception e) {

                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Deprecated
    public boolean setFile(int fileId, int compression, byte[] data, int decompressedLength) {
        try {
            boolean fileExists = getFile(fileId) != null;
            byte[] readBuffer = new byte[520];
            int curSectorId;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(baos);
            out.writeByte(compression);
            out.writeInt(data.length);
            if (compression != 0) {
                out.writeInt(decompressedLength);
            }
            out.write(data);
            out.flush();
            byte[] containerData = baos.toByteArray();
            int fileLength = containerData.length;


            if (fileExists) {
                indexFile.seek(fileId * 6);
                int readThisCycle;
                for (int i1 = 0; i1 < 6; i1 += readThisCycle) // Read the previous file index
                {
                    readThisCycle = indexFile.read(readBuffer, i1, 6 - i1);
                    if (readThisCycle == -1) {
                        return false;
                    }
                }

                int lastFileSize = ((readBuffer[0] & 0xff) << 16) + ((readBuffer[1] & 0xff) << 8) + (readBuffer[2] & 0xff);
                curSectorId = ((readBuffer[3] & 0xff) << 16) + ((readBuffer[4] & 0xff) << 8) + (readBuffer[5] & 0xff);

                if ((fileLength / 520) > (lastFileSize / 520)) {
                    //if (reportBoundsError) {
                    //updateFile(fileId, fileBuffer);
                    //throw new IOException("New file sector count extends previous file bounds. Cache must be rebuilt");
                    //}
                    curSectorId = (int) ((cacheFile.length() + 519L) / 520L);
                    if (curSectorId == 0) {
                        curSectorId = 1;
                    }
                }
                if (curSectorId <= 0 || (long) curSectorId > cacheFile.length() / 520L) {
                    //throw new IOException("Sector extends cache bounds!");
                }
            } else {
                curSectorId = (int) ((cacheFile.length() + 519L) / 520L);
                if (curSectorId == 0) {
                    curSectorId = 1;
                }
            }
            readBuffer[0] = (byte) (fileLength >> 16);
            readBuffer[1] = (byte) (fileLength >> 8);
            readBuffer[2] = (byte) fileLength;
            readBuffer[3] = (byte) (curSectorId >> 16);
            readBuffer[4] = (byte) (curSectorId >> 8);
            readBuffer[5] = (byte) curSectorId;
            indexFile.seek(fileId * 6);
            indexFile.write(readBuffer, 0, 6);
            int dataWrittenSoFar = 0;
            for (int expectedFilePartitionNo = 0; dataWrittenSoFar < fileLength; expectedFilePartitionNo++) {
                int nextSectorId = 0;
                if (fileExists) {
                    cacheFile.seek(curSectorId * 520);
                    int j2;
                    int l2;
                    for (j2 = 0; j2 < 8; j2 += l2) {
                        l2 = cacheFile.read(readBuffer, j2, 8 - j2);
                        if (l2 == -1) {
                            break;
                        }
                    }

                    if (j2 == 8) {
                        int sectorFileNumber = ((readBuffer[0] & 0xff) << 8) + (readBuffer[1] & 0xff);
                        int prevFilePartitionNo = ((readBuffer[2] & 0xff) << 8) + (readBuffer[3] & 0xff);
                        nextSectorId = ((readBuffer[4] & 0xff) << 16) + ((readBuffer[5] & 0xff) << 8) + (readBuffer[6] & 0xff);
                        int prevCacheNo = readBuffer[7] & 0xff;
                        if (sectorFileNumber != fileId) {
                            throw new IOException("Sector file number didn't match expected file number");
                        } else if (prevFilePartitionNo != expectedFilePartitionNo) {
                            throw new IOException("Sector file part number didn't match expected file part number");
                        } else if (prevCacheNo != id) {
                            throw new IOException("Sector cache number didn't match expected cache number");
                        }
                        if (nextSectorId < 0 || (long) nextSectorId > cacheFile.length() / 520L) {
                            throw new IOException("Sector extends cache bounds!");
                        }
                    }
                }
                if (nextSectorId == 0) {
                    fileExists = false;
                    nextSectorId = (int) ((cacheFile.length() + 519L) / 520L);
                    if (nextSectorId == 0) {
                        nextSectorId++;
                    }
                    if (nextSectorId == curSectorId) {
                        nextSectorId++;
                    }
                }
                if (fileLength - dataWrittenSoFar <= 512) {
                    nextSectorId = 0;
                }
                readBuffer[0] = (byte) (fileId >> 8);
                readBuffer[1] = (byte) fileId;
                readBuffer[2] = (byte) (expectedFilePartitionNo >> 8);
                readBuffer[3] = (byte) expectedFilePartitionNo;
                readBuffer[4] = (byte) (nextSectorId >> 16);
                readBuffer[5] = (byte) (nextSectorId >> 8);
                readBuffer[6] = (byte) nextSectorId;
                readBuffer[7] = (byte) id;
                cacheFile.seek(curSectorId * 520);
                cacheFile.write(readBuffer, 0, 8);
                int amountOfDataWrittenThisCycle = fileLength - dataWrittenSoFar;
                if (amountOfDataWrittenThisCycle > 512) {
                    amountOfDataWrittenThisCycle = 512;
                }
                cacheFile.write(containerData, dataWrittenSoFar, amountOfDataWrittenThisCycle);
                dataWrittenSoFar += amountOfDataWrittenThisCycle;
                curSectorId = nextSectorId;
            }
            if (id != 255) {
                CRC32 crc32 = new CRC32();
                crc32.update(containerData);

                if (!fileExists) {
                }
                RS2FileDescriptor descriptor = descriptorTable.getEntries()[fileId];
                descriptor.setExists(true);
                if (descriptor.getCRC() != 0) {
                    descriptor.setCRC((int) crc32.getValue());
                }
                if (descriptor.getRevision() != 0) {
                    descriptor.setRevision(descriptor.getRevision() + 1);
                }

                cache.getDescriptorTableFile().setFile(id, 0, descriptorTable.toByteArray(), 0);
            }


            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public RS2File getFileForName(String name) {
        if (descriptorTable.isTitled()) {
            int nameHash = RS2FileDescriptorTable.hashName(name);
            RS2FileDescriptor fileDescriptor = descriptorTable.getDescriptorMap().get(nameHash);
            if (fileDescriptor != null) {
                return getFile(fileDescriptor.getId());
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public RandomAccessFile getIndexFile() {
        return indexFile;
    }

    public int getLength() {
        try {
            return (int) (indexFile.length() / 6);
        } catch (IOException e) {
        }
        return 0;
    }

    public void setDescriptorTable(RS2FileDescriptorTable descriptorTable) {
        this.descriptorTable = descriptorTable;
    }
}
