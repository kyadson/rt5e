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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.zip.CRC32;

/**
 * @author Lazaro, Defyboy
 */
public class Cache {
    private RandomAccessFile dataFile = null;
    private RS2File cacheHash = null;
    private RS2FileSystem[] fileSystems = new RS2FileSystem[32];
    private RS2FileSystem descriptorTableFile = null;

    public Cache(File directory) throws IOException {
        this(directory, false);
    }

    public Cache(File directory, boolean debug) throws IOException {
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                if (file.getName().equals("main_file_cache.dat2")) {
                    dataFile = new RandomAccessFile(file, "rw");
                    if (debug) {
                        System.out.println("Loaded cache file : " + file.getName());
                    }
                } else if (file.getName().startsWith("main_file_cache.idx")) {
                    int id = Integer.parseInt(file.getName().substring(
                            file.getName().indexOf(".idx") + 4,
                            file.getName().length()));
                    if (id != 255) {
                        fileSystems[id] = new RS2FileSystem(id, dataFile,
                                new RandomAccessFile(file, "rw"), this);
                        if (debug) {
                            System.out.println("Loaded file system file : " + file.getName());
                        }
                    } else {
                        descriptorTableFile = new RS2FileSystem(id, dataFile,
                                new RandomAccessFile(file, "rw"), this);
                        if (debug) {
                            System.out.println("Loaded refrence table file : " + file.getName());
                        }
                    }
                }
            }
        }

        for (int i = 0; i < descriptorTableFile.getLength(); i++) {
            fileSystems[i].setDescriptorTable(new RS2FileDescriptorTable(
                    descriptorTableFile.getFile(i)));
        }
        if (debug) {
            System.out.println("Loaded descriptor file tables.");
        }

        ByteBuffer buffer = ByteBuffer.allocate(descriptorTableFile.getLength() * 8);
        CRC32 crc = new CRC32();
        for (int i = 0; i < descriptorTableFile.getLength(); i++) {
            RS2File file = descriptorTableFile.getFile(i);
            crc.update(ByteBuffer.allocate(5).put((byte) file.getCompression()).putInt(file.getLength()).array());
            crc.update(file.getBytes());
            buffer.putInt((int) crc.getValue());
            buffer.putInt(fileSystems[i].getDescriptorTable().getRevision());
            crc.reset();
        }
        buffer.flip();
        cacheHash = new RS2File(255, 255, 0, descriptorTableFile.getLength() * 8, buffer);

        if (debug) {
            System.out.println("Calculated cache checksums \n" + Arrays.toString(cacheHash.getBytes()));
        }
    }

    public Cache(String directory) throws IOException {
        this(new File(directory), false);
    }

    public Cache(String directory, boolean debug) throws IOException {
        this(new File(directory), debug);
    }

    public RandomAccessFile getDataFile() {
        return dataFile;
    }

    public RS2FileSystem getDescriptorTableFile() {
        return descriptorTableFile;
    }

    public synchronized RS2File getFile(int fileSystemId, int fileId) {
        if (fileSystemId == 255 && fileId == 255) {
            return cacheHash;
        }

        RS2FileSystem fileSystem = getFileSystem(fileSystemId);
        if (fileSystem != null) {
            return fileSystem.getFile(fileId);
        }

        return null;
    }

    public RS2FileSystem getFileSystem(int id) {
        if (id != 255) {
            return fileSystems[id];
        } else {
            return descriptorTableFile;
        }
    }

    public RS2FileSystem[] getFileSystems() {
        return fileSystems;
    }
}
