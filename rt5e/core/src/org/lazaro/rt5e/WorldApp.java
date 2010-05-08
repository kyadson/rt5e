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
package org.lazaro.rt5e;

import org.lazaro.rt5e.logic.World;
import org.lazaro.rt5e.utility.Configuration;
import org.lazaro.rt5e.utility.Logger;
import org.lazaro.rt5e.utility.NativeConsole;

/**
 * @author Lazaro
 */
public class WorldApp {
    private static boolean active = false;

    public static boolean isActive() {
        return active;
    }

    public static World getWorld() {
        return world;
    }

    private static World world = null;

    public static void main(String[] args) {
        active = true;

        NativeConsole.setHeader("RT5E [World Server]");
        Logger.setupLogging();
        Logger.printInfo();

        System.out.println("Starting world server...");
        Logger.incrementIndentationTab();

        try {
            Context.setConfiguration(new Configuration(Constants.LOGIN_SERVER_CONFIG));
            System.out.println("Loaded settings");

            world = new World(1);
            world.start();
            System.out.println("Loaded world!");

        } catch (Throwable e) {
            e.printStackTrace();
        }

        Logger.resetIndentation();
        System.out.println("DONE!");
    }
}
