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
package org.lazaro.rt5e.network;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.lazaro.rt5e.utility.TextUtilities;

import java.io.ObjectOutputStream;

/**
 * @author Lazaro
 */
public class PacketBuilder {
    private int bitPosition = 0;
    private ChannelBuffer buffer;
    private int opcode;
    private Packet.Type type;

    public PacketBuilder() {
        this(-1);
    }

    public PacketBuilder(int opcode) {
        this(opcode, Packet.Type.FIXED);
    }

    public PacketBuilder(int opcode, Packet.Type type) {
        this.opcode = opcode;
        this.type = type;

        this.buffer = ChannelBuffers.buffer(128);
    }

    public int getPosition() {
        return buffer.writerIndex();
    }

    public PacketBuilder put(byte b) {
        buffer.writeByte(b);
        return this;
    }

    public PacketBuilder put(byte[] bytes) {
        buffer.writeBytes(bytes);
        return this;
    }

    public PacketBuilder put(byte[] bytes, int offset, int length) {
        buffer.writeBytes(bytes, offset, length);
        return this;
    }

    public PacketBuilder putA(byte b) {
        buffer.writeByte((byte) ((b & 0xff) + 128));
        return this;
    }

    public PacketBuilder putA(byte[] src) {
        putA(src, 0, src.length);
        return this;
    }

    public PacketBuilder putA(byte[] src, int offset, int length) {
        for (int i = 0; i < length; i++) {
            putA(src[i]);
        }
        return this;
    }

    public PacketBuilder putBackwards(byte[] src) {
        putBackwards(src, 0, src.length);
        return this;
    }

    public PacketBuilder putBackwards(byte[] src, int offset, int length) {
        for (int i = length - 1; i >= offset; i--) {
            put(src[i]);
        }
        return this;
    }

    public PacketBuilder putBits(int numBits, int value) {
        /* Prepare for adding bits */
        int bytePos = bitPosition >> 3;
        int bitOffset = 8 - (bitPosition & 7);
        bitPosition += numBits;
        int pos = (bitPosition + 7) / 8;
        while (pos + 1 > buffer.capacity()) {
            buffer.writeByte((byte) 0); // Netty does not provide any other way of resizing the buffer.
        }
        buffer.writerIndex(pos);

        /* Write the bits */
        byte b;
        for (; numBits > bitOffset; bitOffset = 8) {
            b = buffer.getByte(bytePos);
            buffer.setByte(bytePos, (byte) (b & ~Packet.BIT_MASK[bitOffset]));
            buffer.setByte(bytePos, (byte) (b | (value >> (numBits - bitOffset)) & Packet.BIT_MASK[bitOffset]));
            bytePos++;
            numBits -= bitOffset;
        }
        b = buffer.getByte(bytePos);
        if (numBits == bitOffset) {
            buffer.setByte(bytePos, (byte) (b & ~Packet.BIT_MASK[bitOffset]));
            buffer.setByte(bytePos, (byte) (b | value & Packet.BIT_MASK[bitOffset]));
        } else {
            buffer.setByte(bytePos, (byte) (b & ~(Packet.BIT_MASK[numBits] << (bitOffset - numBits))));
            buffer.setByte(bytePos, (byte) (b | (value & Packet.BIT_MASK[numBits]) << (bitOffset - numBits)));
        }
        return this;
    }

    public PacketBuilder putByte(int i) {
        buffer.writeByte((byte) i);
        return this;
    }

    public PacketBuilder putByteA(int i) {
        buffer.writeByte((byte) (i + 128));
        return this;
    }

    public PacketBuilder putByteC(int i) {
        buffer.writeByte((byte) -i);
        return this;
    }

    public PacketBuilder putByteS(int i) {
        buffer.writeByte((byte) (128 - i));
        return this;
    }

    public PacketBuilder putC(byte b) {
        buffer.writeByte((byte) -(b & 0xff));
        return this;
    }

    public PacketBuilder putGJString2(String string) {
        byte[] packed = new byte[TextUtilities.calculateGJString2Length(string)];
        int length = TextUtilities.packGJString2(0, packed, string);
        putByte(0).put(packed, 0, length).putByte(0);
        return this;
    }

    public PacketBuilder putInt(int i) {
        buffer.writeInt(i);
        return this;
    }

    public PacketBuilder putInt1(int i) {
        buffer.writeByte((byte) (i >> 8));
        buffer.writeByte((byte) i);
        buffer.writeByte((byte) (i >> 24));
        buffer.writeByte((byte) (i >> 16));
        return this;
    }

    public PacketBuilder putInt2(int i) {
        buffer.writeByte((byte) (i >> 16));
        buffer.writeByte((byte) (i >> 24));
        buffer.writeByte((byte) i);
        buffer.writeByte((byte) (i >> 8));
        return this;
    }

    public PacketBuilder putLEInt(int i) {
        buffer.writeByte((byte) i);
        buffer.writeByte((byte) (i >> 8));
        buffer.writeByte((byte) (i >> 16));
        buffer.writeByte((byte) (i >> 24));
        return this;
    }

    public PacketBuilder putLEShort(int i) {
        buffer.writeByte((byte) i);
        buffer.writeByte((byte) (i >> 8));
        return this;
    }

    public PacketBuilder putLEShortA(int i) {
        buffer.writeByte((byte) (i + 128));
        buffer.writeByte((byte) (i >> 8));
        return this;
    }

    public PacketBuilder putLong(long l) {
        buffer.writeLong(l);
        return this;
    }

    public PacketBuilder putObject(Object object) {
        try {
            ObjectOutputStream stream = new ObjectOutputStream(new ChannelBufferOutputStream(buffer));
            stream.writeObject(object);
            stream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public PacketBuilder putRS2String(String string) {
        put(string.getBytes()).putByte(10);
        return this;
    }

    public PacketBuilder putS(byte b) {
        buffer.writeByte((byte) (128 - (b & 0xff)));
        return this;
    }

    public PacketBuilder putS(byte[] src) {
        putS(src, 0, src.length);
        return this;
    }

    public PacketBuilder putS(byte[] src, int offset, int length) {
        for (int i = 0; i < length; i++) {
            putS(src[i]);
        }
        return this;
    }

    public PacketBuilder putShort(int i) {
        buffer.writeShort((short) i);
        return this;
    }

    public PacketBuilder putShortA(int i) {
        buffer.writeByte((byte) (i >> 8));
        buffer.writeByte((byte) (i + 128));
        return this;
    }

    public PacketBuilder putSmart(int i) {
        if (i > 128) {
            putByte(255).putShort(i);
        } else {
            putByte(i);
        }
        return this;
    }

    public PacketBuilder putString(String string) {
        put(string.getBytes()).putByte(0);
        return this;
    }

    public PacketBuilder putTriByte(int i) {
        buffer.writeByte((byte) (i >> 16));
        buffer.writeByte((byte) (i >> 8));
        buffer.writeByte((byte) i);
        return this;
    }

    public PacketBuilder putVariable(int i_23_) {
        if ((~0x7f & i_23_) != 0) {
            if ((i_23_ & ~0x3fff) != 0) {
                if ((i_23_ & ~0x1fffff) != 0) {
                    if ((~0xfffffff & i_23_) != 0)
                        putByte(i_23_ >>> 28 | 0x80);
                    putByte((0x101e39d8 | i_23_) >>> 21);
                }
                putByte(i_23_ >>> 14 | 0x80);
            }
            putByte(i_23_ >>> 7 | 0x80);
        }
        putByte(i_23_ & 0x7f);
        return this;
    }

    /**
     * This is to be called if you are adding bits when the offset is not 0.
     *
     * @return
     */
    public PacketBuilder recalculateBitPosition() {
        bitPosition = buffer.writerIndex() * 8;
        return this;
    }

    public Packet toPacket() {
        return new Packet(opcode, buffer.writerIndex(), type, buffer);
    }
}
