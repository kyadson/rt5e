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
package org.lazaro.rt5e.utility;

import org.jboss.netty.buffer.ChannelBuffer;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Lazaro
 */
public class StreamUtilities {
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
