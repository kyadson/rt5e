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

import com.sun.jna.Platform;
import org.lazaro.rt5e.utility.win32.Kernel32;

/**
 * @author Lazaro
 */
public class NativeConsole {
    public static final int COLOR_BLACK = 0, COLOR_BLUE = 1, COLOR_GREEN = 2,
            COLOR_AQUA = 3, COLOR_RED = 4, COLOR_PURPLE = 5, COLOR_YELLOW = 6,
            COLOR_WHITE = 7, COLOR_GRAY = 8, COLOR_LIGHT_BLUE = 9,
            COLOR_LIGHT_GREEN = 0xa, COLOR_LIGHT_AQUA = 0xb,
            COLOR_LIGHT_RED = 0xc, COLOR_LIGHT_PURPLE = 0xd,
            COLOR_LIGHT_YELLOW = 0xe, COLOR_BRIGHT_WHITE = 0xf;

    private static int currentBackground = COLOR_BLACK;
    private static int currentForeground = COLOR_GRAY;
    private static String currentHeader = null;
    private static boolean supported = Platform.isWindows();

    public static boolean isSupported() {
        return supported;
    }

    public static void setColor(int foreground, int background) {
        if (supported && (currentForeground != foreground || currentBackground != background)) {
            Kernel32.INSTANCE.SetConsoleTextAttribute(Kernel32.INSTANCE
                    .GetStdHandle(Kernel32.STD_OUTPUT_HANDLE),
                    (short) (foreground | background * 16));

            currentForeground = foreground;
            currentBackground = background;
        }
    }

    public static void setHeader(String header) {
        if (supported && (currentHeader == null || !currentHeader.equals(header))) {
            currentHeader = header;
            Kernel32.INSTANCE.SetConsoleTitle(header);
        }
    }
}