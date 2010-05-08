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
package org.lazaro.rt5e.logic.mask;

/**
 * @author Lazaro
 */
public class Graphics implements Mask {
    private int delay;
    private int id;

    /**
     * Creates a graphics mask with no delay.
     *
     * @param id The graphics id.
     */
    public Graphics(int id) {
        this.id = id;
        this.delay = 0;
    }

    /**
     * Creates a graphics mask with the specified delay.
     *
     * @param id    The graphics id.
     * @param delay The delay.
     */
    public Graphics(int id, int delay) {
        this.id = id;
        this.delay = delay;
    }

    /**
     * @return the delay
     */
    public int getDelay() {
        return delay;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
}
