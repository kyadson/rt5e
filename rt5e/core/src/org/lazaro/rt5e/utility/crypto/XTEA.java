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
package org.lazaro.rt5e.utility.crypto;

import java.nio.ByteBuffer;

/**
 * @author Lazaro
 */
public class XTEA {
    private static final int DELTA = 0x9E3779B9;
    /* RuneScape setup. */
    private static final int NUM_ROUNDS = 32;
    private final int[] key;

    public XTEA(int[] key) {
        this.key = key;
    }

    private void decipher(int[] block) {
        long sum = (long) DELTA * NUM_ROUNDS;
        for (int i = 0; i < NUM_ROUNDS; i++) {
            block[1] -= (((block[0] << 4) ^ (block[0] >> 5)) + block[0])
                    ^ (sum + key[((int) ((sum >> 11) & 3))]);
            sum -= DELTA;
            block[0] -= (((block[1] << 4) ^ (block[1] >> 5)) + block[0])
                    ^ (sum + key[((int) (sum & 3))]);
        }
    }

    public void decrypt(ByteBuffer buffer) {
        int[] block = new int[2];
        for (int i = buffer.position(); i + 8 <= buffer.limit(); i += 8) {
            block[0] = buffer.getInt(i);
            block[1] = buffer.getInt(i + 4);
            decipher(block);
            buffer.putInt(i, block[0]).putInt(i + 4, block[1]);
        }
        buffer.rewind();
    }

    private void encipher(int[] block) {
        long sum = 0;
        for (int i = 0; i < NUM_ROUNDS; i++) {
            block[0] += (((block[1] << 4) ^ (block[1] >> 5)) + block[1])
                    ^ (sum + key[((int) (sum & 3))]);
            sum += DELTA;
            block[1] += (((block[0] << 4) ^ (block[0] >> 5)) + block[0])
                    ^ (sum + key[((int) ((sum >> 11) & 3))]);
        }
    }

    public void encrypt(ByteBuffer buffer) {
        int[] block = new int[2];
        for (int i = 0; i + 8 < buffer.limit(); i += 8) {
            block[0] = buffer.getInt(i);
            block[1] = buffer.getInt(i + 4);
            encipher(block);
            buffer.putInt(i, block[0]).putInt(i + 4, block[1]);
        }
        buffer.rewind();
    }
}
