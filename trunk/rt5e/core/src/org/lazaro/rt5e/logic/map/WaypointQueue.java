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
package org.lazaro.rt5e.logic.map;

import org.lazaro.rt5e.logic.utility.CoordinateFuture;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @author Lazaro
 */
public abstract class WaypointQueue {
    public static final int MAX_SIZE = 32;
    protected boolean running = false;
    protected CoordinateFuture coordinateFuture = null;
    protected Queue<Tile> waypoints = new ArrayDeque<Tile>();

    public boolean add(Tile tile) {
        if (waypoints.size() >= MAX_SIZE) {
            return false;
        }
        synchronized (waypoints) {
            waypoints.offer(tile);
            return true;
        }
    }

    public CoordinateFuture getCoordinateFuture() {
        return coordinateFuture;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean moving() {
        return waypoints.size() > 0;
    }

    protected abstract int next();

    public abstract void process();

    public void reset() {
        waypoints.clear();
        running = false;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
