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
package org.lazaro.rt5e.engine.event;

/**
 * @author Lazaro
 */
public abstract class Event implements Runnable {
    private int delay;
    private long lastRun;
    protected boolean running;

    public Event(int delay) {
        this.delay = delay;

        lastRun = System.currentTimeMillis();
        running = true;
    }

    public int getDelay() {
        return delay;
    }

    public long getLastRun() {
        return lastRun;
    }

    public int getTimeRemaining() {
        return delay - ((int) (System.currentTimeMillis() - lastRun));
    }

    public boolean isRunning() {
        return running;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void updateLastRun() {
        lastRun = System.currentTimeMillis();
    }
}
