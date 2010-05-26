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

/**
 * @author Lazaro, Defyboy
 */
public class RS2FileDescriptor {
    public static class SubRS2FileDescriptor {
        private int nameHash = 0;
        private int offset = 0;

        public int getNameHash() {
            return nameHash;
        }

        public int getOffset() {
            return offset;
        }

        public void setNameHash(int nameHash) {
            this.nameHash = nameHash;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }
    }

    private int crc = 0;
    private boolean exists = false;
    private int id;
    private int nameHash = 0;
    private int revision = 0;
    private SubRS2FileDescriptor[] subFiles = null;

    public RS2FileDescriptor(int id) {
        this.id = id;
    }

    public boolean exists() {
        return exists;
    }

    public int getCRC() {
        return crc;
    }

    public int getId() {
        return id;
    }

    public int getNameHash() {
        return nameHash;
    }

    public int getRevision() {
        return revision;
    }

    public SubRS2FileDescriptor[] getSubFiles() {
        return subFiles;
    }

    public void setCRC(int crc) {
        this.crc = crc;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public void setNameHash(int nameHash) {
        this.nameHash = nameHash;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public void setSubFiles(SubRS2FileDescriptor[] subFiles) {
        this.subFiles = subFiles;
    }
}
