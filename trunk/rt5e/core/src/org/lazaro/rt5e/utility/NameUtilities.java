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

/**
 * @author Lazaro
 */
public class NameUtilities {
    private static final char[] VALID_CHARS = {'_', 'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
            's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9'};

    public static String formatNameForDisplay(String string) {
        final char ac[] = string.replace("_", " ").toCharArray();
        for (int j = 0; j < ac.length; j++)
            if (ac[j] == '_') {
                ac[j] = ' ';
                if ((j + 1 < ac.length) && (ac[j + 1] >= 'a')
                        && (ac[j + 1] <= 'z')) {
                    ac[j + 1] = (char) ((ac[j + 1] + 65) - 97);
                }
            }
        if ((ac[0] >= 'a') && (ac[0] <= 'z')) {
            ac[0] = (char) ((ac[0] + 65) - 97);
        }
        return new String(ac);
    }

    public static String formatNameForProtocol(String string) {
        return string.toLowerCase().replace(" ", "_");
    }

    public static String longToString(long l) {
        if (l <= 0L || l >= 0x5b5b57f8a98a5dd1L) {
            return null;
        }
        if (l % 37L == 0L) {
            return null;
        }
        int i = 0;
        char ac[] = new char[12];
        while (l != 0L) {
            long l1 = l;
            l /= 37L;
            ac[11 - i++] = VALID_CHARS[(int) (l1 - l * 37L)];
        }
        return new String(ac, 12 - i, i);
    }

    public static long stringToLong(String string) {
        long l = 0L;
        for (int i = 0; i < string.length() && i < 12; i++) {
            char c = string.charAt(i);
            l *= 37L;
            if (c >= 'A' && c <= 'Z')
                l += (1 + c) - 65;
            else if (c >= 'a' && c <= 'z')
                l += (1 + c) - 97;
            else if (c >= '0' && c <= '9')
                l += (27 + c) - 48;
        }
        while (l % 37L == 0L && l != 0L)
            l /= 37L;
        return l;
    }

    private NameUtilities() {
    }
}
