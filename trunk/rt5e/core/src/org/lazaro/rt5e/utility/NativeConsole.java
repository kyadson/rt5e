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