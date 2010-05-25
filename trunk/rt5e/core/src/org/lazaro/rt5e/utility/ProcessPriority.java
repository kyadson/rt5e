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
import org.lazaro.rt5e.utility.win32.W32API;

/**
 * @author Lazaro
 */
public class ProcessPriority {
    private static final int WIN32_PROCESS_PRIORITY_CLASS = Kernel32.REALTIME_PRIORITY_CLASS;

    public static void setProcessPriority() {
        if (Platform.isWindows()) {
            W32API.HANDLE currentProcess = Kernel32.INSTANCE
                    .GetCurrentProcess();
            if (Kernel32.INSTANCE.SetPriorityClass(currentProcess,
                    WIN32_PROCESS_PRIORITY_CLASS)) {
                System.out.println("Set process to "
                        + win32PriorityName(WIN32_PROCESS_PRIORITY_CLASS)
                        + " priority mode");
            }
        } else if (Platform.isLinux()) {
            // TODO Write low level code for Linux
        }
    }

    private static String win32PriorityName(int priorityClass) {
        switch (priorityClass) {
            case Kernel32.ABOVE_NORMAL_PRIORITY_CLASS:
                return "above normal";
            case Kernel32.BELOW_NORMAL_PRIORITY_CLASS:
                return "below normal";
            case Kernel32.HIGH_PRIORITY_CLASS:
                return "high";
            case Kernel32.IDLE_PRIORITY_CLASS:
                return "idle";
            case Kernel32.REALTIME_PRIORITY_CLASS:
                return "realtime";
        }
        return "unknown";
    }
}
