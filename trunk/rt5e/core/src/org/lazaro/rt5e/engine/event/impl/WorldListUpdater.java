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
package org.lazaro.rt5e.engine.event.impl;

import org.lazaro.rt5e.Context;
import org.lazaro.rt5e.engine.event.Event;
import org.lazaro.rt5e.login.LSException;

/**
 * @author Lazaro
 */
public class WorldListUpdater extends Event {
    private static WorldListUpdater instance = new WorldListUpdater();

    public static WorldListUpdater getInstance() {
        return instance;
    }

    private byte[] cachedWorldList = null;

    public WorldListUpdater() {
        super(1000);
    }

    public byte[] getCachedWorldList() {
        return cachedWorldList;
    }

    public void run() {
        try {
            cachedWorldList = Context.getWorld().getSession().worldListData();
        } catch (LSException e) {
            cachedWorldList = null;
            e.printStackTrace();
        }
    }
}
