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

import org.lazaro.rt5e.utility.bzip2.CBZip2InputStream;
import org.lazaro.rt5e.utility.crypto.XTEA;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;

/**
 * @author Lazaro, Defyboy
 */
public class RS2File {
    public static final int COMPRESSION_NONE = 0, COMPRESSION_GZIP = 2,
            COMPRESSION_BZIP2 = 1;
    private int compression;
    private ByteBuffer data;
    private final int id;
    private int length;
    private final int parentId;

    public RS2File(int parentId, int id, int compression, int length,
                   ByteBuffer data) {
        this.parentId = parentId;
        this.id = id;
        this.compression = compression;
        this.length = length;
        this.data = data;
    }

    /**
     * Deciphers the XTEA encryption, if any, and unpacks the file.
     *
     * @param key The XTEA 128 bit key, if any.
     * @throws IOException
     */
    public void extract(int[] key) throws IOException {
        if (key != null && key[0] != 0 && key[1] != 0 && key[2] != 0
                && key[3] != 0) {
            new XTEA(key).decrypt(data);
        }
        int decompressedLength = length;
        if (compression != COMPRESSION_NONE) {
            decompressedLength = data.getInt();
        }
        byte[] raw = new byte[length];
        data.get(raw);

        byte[] ext;
        switch (compression) {
            case COMPRESSION_GZIP:
                ext = new byte[decompressedLength];
                new GZIPInputStream(new ByteArrayInputStream(raw)).read(ext);
                data = ByteBuffer.wrap(ext);
                break;
            case COMPRESSION_BZIP2:
                ext = new byte[decompressedLength];
                try {
                    new CBZip2InputStream(new ByteArrayInputStream(raw)).read(ext);
                } catch (Exception e) {
                    throw new IOException("Not in BZIP2 format");
                }
                data = ByteBuffer.wrap(ext);
                break;
        }

        data.rewind();
    }

    /**
     * Checks if the underlying <code>IoBuffer</code> is backed by an array. If
     * it has an array it will return that. If no array is available is will
     * create one based on the <code>IoBuffer</code>.
     * <p/>
     * Note that this method might modify the underlying <code>IoBuffer</code>'s
     * position.
     *
     * @return The byte array that makes up this file.
     */
    public byte[] getBytes() {
        if (data.hasArray()) {
            return data.array();
        } else {
            byte[] array = new byte[length];
            data.get(array);
            data.rewind();
            return array;
        }
    }

    public int getCompression() {
        return compression;
    }

    /**
     * Gets the underlying <code>ByteBuffer</code>.
     * <p/>
     * Remember to rewind the buffer when you're done reading it!
     *
     * @return The <code>ByteBuffer</code>.
     */
    public ByteBuffer getData() {
        return data;
    }

    public int getId() {
        return id;
    }

    public int getLength() {
        return length;
    }

    public int getParentId() {
        return parentId;
    }
}
