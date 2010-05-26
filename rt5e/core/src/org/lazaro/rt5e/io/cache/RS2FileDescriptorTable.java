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
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lazaro, Defyboy
 */
public class RS2FileDescriptorTable {
    public static int hashName(String s) {
        int r = 0;
        for (int i = 0; i < s.length(); i++) {
            r = s.charAt(i) + ((r << 5) - r);
        }
        return r;
    }

    private Map<Integer, RS2FileDescriptor> descriptorMap = null;
    private RS2FileDescriptor[] entries;
    private RS2File infoFile;
    private int revision;
    private boolean titled;
    private int version;

    public RS2FileDescriptorTable(RS2File infoFile) throws IOException {
        this.infoFile = infoFile;

        infoFile.extract(null);
        ByteBuffer buffer = infoFile.getData();
        version = buffer.get() & 0xff;
        if (version == 6) {
            revision = buffer.getInt();
        }
        titled = (buffer.get() & 0xff) == 1;
        int count = buffer.getShort() & 0xffff;
        int[] spacing = new int[count];
        int entryCount = 0;
        for (int i = 0; i < count; i++) {
            spacing[i] = entryCount += buffer.getShort() & 0xffff;
        }
        entryCount++;
        entries = new RS2FileDescriptor[entryCount];
        for (int i = 0; i < entryCount; i++) {
            entries[i] = new RS2FileDescriptor(i);
        }
        if (titled) {
            descriptorMap = new HashMap<Integer, RS2FileDescriptor>();
            for (int i = 0; i < count; i++) {
                RS2FileDescriptor entry = entries[spacing[i]];
                entry.setNameHash(buffer.getInt());
                descriptorMap.put(entry.getNameHash(), entry);
            }
        }
        for (int i = 0; i < count; i++) {
            entries[spacing[i]].setExists(true);
            entries[spacing[i]].setCRC(buffer.getInt());
        }
        for (int i = 0; i < count; i++) {
            entries[spacing[i]].setRevision(buffer.getInt());
        }
        for (int i = 0; i < count; i++) {
            RS2FileDescriptor entry = entries[spacing[i]];
            RS2FileDescriptor.SubRS2FileDescriptor[] subEntries = new RS2FileDescriptor.SubRS2FileDescriptor[buffer
                    .getShort() & 0xffff];
            for (int j = 0; j < subEntries.length; j++) {
                subEntries[j] = new RS2FileDescriptor.SubRS2FileDescriptor();
            }
            entry.setSubFiles(subEntries);
        }
        for (int i = 0; i < count; i++) {
            RS2FileDescriptor entry = entries[spacing[i]];
            for (int j = 0; j < entry.getSubFiles().length; j++) {
                entry.getSubFiles()[j].setOffset(buffer.getShort() & 0xffff);
            }
        }
        if (titled) {
            for (int i = 0; i < count; i++) {
                RS2FileDescriptor entry = entries[spacing[i]];
                for (int j = 0; j < entry.getSubFiles().length; j++) {
                    entries[spacing[i]].getSubFiles()[j].setNameHash(buffer
                            .getInt());
                }
            }
        }
    }

    public Map<Integer, RS2FileDescriptor> getDescriptorMap() {
        return descriptorMap;
    }

    public RS2FileDescriptor[] getEntries() {
        return entries;
    }

    public RS2File getFile() {
        return infoFile;
    }

    public int getRevision() {
        return revision;
    }

    public int getVersion() {
        return version;
    }

    public boolean isTitled() {
        return titled;
    }

    public byte[] toByteArray() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream stream = new DataOutputStream(baos);
            stream.writeByte(version);
            if (version == 6) {
                stream.writeInt(revision);
            }
            stream.writeByte(titled ? 1 : 0);
            int count = 0;
            for (int i = 0; i < entries.length; i++) {
                if (entries[i].exists()) {
                    count++;
                }
            }
            int[] spacing = new int[count];
            stream.writeShort(count);
            int lastEntryOffset = 0;
            int spacingOffset = 0;
            for (int i = 0; i < entries.length; i++) {
                if (entries[i].exists()) {
                    spacing[spacingOffset++] = i;
                    stream.writeShort(i - lastEntryOffset);
                    lastEntryOffset = i;
                }
            }
            if (titled) {
                for (int i = 0; i < count; i++) {
                    stream.writeInt(entries[spacing[i]].getNameHash());
                }
            }
            for (int i = 0; i < count; i++) {
                stream.writeInt(entries[spacing[i]].getCRC());
            }
            for (int i = 0; i < count; i++) {
                stream.writeInt(entries[spacing[i]].getRevision());
            }
            for (int i = 0; i < count; i++) {
                stream.writeShort(entries[spacing[i]].getSubFiles().length);
            }
            for (int i = 0; i < count; i++) {
                RS2FileDescriptor entry = entries[spacing[i]];
                for (int i2 = 0; i2 < entry.getSubFiles().length; i2++) {
                    stream.writeShort(entry.getSubFiles()[i2].getOffset());
                }
            }
            if (titled) {
                for (int i = 0; i < count; i++) {
                    RS2FileDescriptor entry = entries[spacing[i]];
                    for (int i2 = 0; i2 < entry.getSubFiles().length; i2++) {
                        stream.writeInt(entry.getSubFiles()[i2].getNameHash());
                    }
                }
            }
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
