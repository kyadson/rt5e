package org.lazaro.rt5e.network;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;

import java.io.ObjectInputStream;
import java.util.Arrays;

/**
 * @author Lazaro
 */
public class Packet {
    public static enum Type {
        FIXED, VAR_BYTE, VAR_SHORT
    }

    public static final int[] BIT_MASK = new int[32];

    static {
        for (int i = 0; i < BIT_MASK.length; i++) {
            BIT_MASK[i] = (1 << i) - 1;
        }
    }

    private int bitPosition = 0;

    protected ChannelBuffer buffer;
    protected int length;
    protected int opcode;
    protected Type type;

    public Packet(int opcode, int length, Type type, ChannelBuffer buffer) {
        this.opcode = opcode;
        this.length = length;
        this.type = type;
        this.buffer = buffer;
    }

    public byte get() {
        return buffer.readByte();
    }

    public void get(byte[] packed) {
        buffer.readBytes(packed);

    }

    public byte getA() {
        return (byte) (getUnsigned() - 128);
    }

    public int getBits(int numBits) {
        int bytePos = bitPosition >> 3;
        int bitOffset = 8 - (bitPosition & 7);
        int value = 0;
        bitPosition += numBits;

        int pos = (bitPosition + 7) / 8;
        buffer.readerIndex(pos);

        for (; numBits > bitOffset; bitOffset = 8) {
            value += (buffer.getByte(bytePos++) & BIT_MASK[bitOffset]) << numBits
                    - bitOffset;
            numBits -= bitOffset;
        }
        if (numBits == bitOffset)
            value += buffer.getByte(bytePos) & BIT_MASK[bitOffset];
        else
            value += buffer.getByte(bytePos) >> bitOffset - numBits
                    & BIT_MASK[numBits];
        return value;
    }

    /**
     * Gets the underlying Netty <code>ChannelBuffer</code> that stores this message's
     * data.
     *
     * @return The data.
     */
    public ChannelBuffer getBuffer() {
        return buffer;
    }

    /**
     * Gets the data in this message in bytes.
     *
     * @return The data.
     */
    public byte[] getBytes() {
        byte[] array = new byte[getLength()];
        buffer.getBytes(0, array);
        return array;
    }

    public byte getC() {
        return (byte) (-getUnsigned());
    }

    public int getInt() {
        return buffer.readInt();
    }

    public int getInt1() {
        return (getUnsigned() << 8) | getUnsigned() | (getUnsigned() << 24)
                | (getUnsigned() << 16);
    }

    public int getInt2() {
        return (getUnsigned() << 16) | (getUnsigned() << 24) | getUnsigned()
                | (getUnsigned() << 8);
    }

    public int getLEInt() {
        return getUnsigned() | (getUnsigned() << 8) | (getUnsigned() << 16)
                | (getUnsigned() << 24);
    }

    public int getLength() {
        return length;
    }

    public short getLEShort() {
        int i = (getUnsigned() + (getUnsigned() << 8));
        if (i > 32767) {
            i -= 0x10000;
        }
        return (short) i;
    }

    public short getLEShortA() {
        int i = (getUnsignedA() + (getUnsigned() << 8));
        if (i > 32767) {
            i -= 0x10000;
        }
        return (short) i;
    }

    public long getLong() {
        return buffer.readLong();
    }

    public Object getObject() {
        try {
            ObjectInputStream stream = new ObjectInputStream(new ChannelBufferInputStream(buffer));
            Object object = stream.readObject();
            stream.close();
            return object;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getOpcode() {
        return opcode;
    }

    public String getRS2String() {
        StringBuffer string = new StringBuffer();

        int i;
        while ((i = get() & 0xff) != 10) {
            string.append((char) i);
        }
        return string.toString();
    }

    public byte getS() {
        return (byte) (128 - getUnsigned());
    }

    public short getShort() {
        return buffer.readShort();
    }

    public short getShortA() {
        int i = ((getUnsigned() << 8) + getUnsignedA());
        if (i > 32767) {
            i -= 0x10000;
        }
        return (short) i;
    }

    public String getString() {
        StringBuffer string = new StringBuffer();

        int i;
        while ((i = get() & 0xff) != 0) {
            string.append((char) i);
        }
        return string.toString();
    }

    public Type getType() {
        return type;
    }

    public int getUnsigned() {
        return buffer.readByte() & 0xff;
    }

    public int getUnsignedA() {
        return getA() & 0xff;
    }

    public int getUnsignedC() {
        return getC() & 0xff;
    }

    public int getUnsignedLEShort() {
        return getLEShort() & 0xffff;
    }

    public int getUnsignedLEShortA() {
        return getLEShortA() & 0xffff;
    }

    public int getUnsignedS() {
        return getS() & 0xff;
    }

    public int getUnsignedShort() {
        return buffer.readShort() & 0xffff;
    }

    public int getUnsignedShortA() {
        return getShortA() & 0xffff;
    }

    public boolean isRaw() {
        return opcode == -1;
    }

    public String toString() {
        byte[] data = new byte[length > 6 ? 6 : length];
        buffer.getBytes(0, data);
        String dataPreview = Arrays.toString(data);
        if (length > 6) {
            dataPreview = dataPreview.substring(0, dataPreview.length() - 1) + ", ...]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("opcode=").append(opcode).append(", length=").append(getLength()).append(", data=").append(dataPreview);
        return sb.toString();
    }
}
