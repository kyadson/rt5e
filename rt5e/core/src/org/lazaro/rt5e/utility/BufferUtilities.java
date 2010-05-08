package org.lazaro.rt5e.utility;

import org.jboss.netty.buffer.ChannelBuffer;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Lazaro
 */
public class BufferUtilities {
    public static String getRS2String(ChannelBuffer buffer) {
        StringBuffer string = new StringBuffer();

        int i;
        while ((i = buffer.readByte() & 0xff) != 10) {
            string.append((char) i);
        }
        return string.toString();
    }

    public static String getRS2String(InputStream in) throws IOException {
        StringBuffer string = new StringBuffer();

        int i;
        while ((i = in.read()) != 10 && i != -1) {
            string.append((char) i);
        }
        return string.toString();
    }

    public static String getString(ChannelBuffer buffer) {
        StringBuffer string = new StringBuffer();

        int i;
        while ((i = buffer.readByte() & 0xff) != 0) {
            string.append((char) i);
        }
        return string.toString();
    }

    public static String getString(InputStream in) throws IOException {
        int count = 0;
        StringBuffer string = new StringBuffer();

        int i;
        while ((i = in.read()) != 0 && i != -1) {
            string.append((char) i);
        }
        return string.toString();
    }
}
