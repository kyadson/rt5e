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

import org.lazaro.rt5e.LobbyApp;
import org.lazaro.rt5e.WorldApp;
import org.lazaro.rt5e.engine.event.Event;

/**
 * @author Lazaro
 */
public class Monitor extends Event {
    private String moduleName;

    public Monitor() {
        super(1000);

        if(WorldApp.isActive()) {
            moduleName = "World Server";
        } else if(LobbyApp.isActive()) {
            moduleName = "Lobby Server";
        } else {
            moduleName = "Login Server";
        }
    }

    public void run() {
        NativeConsole.setHeader("RT5E [" + moduleName + "] [bandwidth=(" + bandwidthInfo() + ")]");
        BandwidthMonitor.getInstance().reset();
    }

    private String bandwidthInfo() {
        long in = BandwidthMonitor.getInstance().getBytesReceived();
        long out = BandwidthMonitor.getInstance().getBytesSent();
        String inMeasure = "B";
        String outMeasure = "B";
        if(in > 1024) {
            in /= 1024;
            inMeasure = "kB";
            if(in > 1024) {
                in /= 1024;
                inMeasure = "mB";
            }
        }
        if(out > 1024) {
            out /= 1024;
            outMeasure = "kB";
            if(out > 1024) {
                out /= 1024;
                outMeasure = "mB";
            }
        }
        return "in: " + in + " " + inMeasure + "/s, out: " + out + " " + outMeasure + "/s";
    }
}
