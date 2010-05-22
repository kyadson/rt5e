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

import org.lazaro.rt5e.io.cache.Cache;
import org.lazaro.rt5e.logic.World;
import org.lazaro.rt5e.logic.login.LoginWorker;
import org.lazaro.rt5e.utility.Configuration;

/**
 * A simple static class used for storing variables used by multiple modules.
 *
 * @author Lazaro
 */
public class Context {
    public static Configuration getConfiguration() {
        return configuration;
    }

    public static void setConfiguration(Configuration configuration) {
        Context.configuration = configuration;
    }

    private static Configuration configuration = null;

    public static Cache getCache() {
        return cache;
    }

    public static void setCache(Cache cache) {
        Context.cache = cache;
    }

    private static Cache cache = null;

    public static boolean isRunning() {
        return running;
    }

    public static void setRunning(boolean running) {
        Context.running = running;
    }

    public static World getWorld() {
        return world;
    }

    public static void setWorld(World world) {
        Context.world = world;
    }

    public static LoginWorker getLoginWorker() {
        return loginWorker;
    }

    public static void setLoginWorker(LoginWorker loginWorker) {
        Context.loginWorker = loginWorker;
    }

    private static LoginWorker loginWorker = null;

    private static boolean running = true;
    private static World world = null;
}
