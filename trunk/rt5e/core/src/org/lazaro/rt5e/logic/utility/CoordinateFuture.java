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
package org.lazaro.rt5e.logic.utility;

import org.lazaro.rt5e.logic.Entity;
import org.lazaro.rt5e.logic.map.Tile;

/**
 * @author Lazaro
 */
public class CoordinateFuture {
    private Tile destination;
    private Entity entity;
    private Runnable future;
    private int maximumTries;
    private int tries;

    public CoordinateFuture(Entity entity, Tile destination, Runnable future) {
        this.entity = entity;
        this.destination = destination;
        this.future = future;

        maximumTries = entity.getLocation().distance(destination) + 1;
    }

    public boolean update() {
        if (entity.isTeleporting()) {
            return false; // The entity has teleported, stop the
            // event.
        } else if (entity.getLocation().equals(destination)) {
            future.run(); // The entity has reached the destination,
            // execute
            // future.
            return false; // Stop the event.
        } else if (tries++ >= maximumTries) {
            return false; // Tried too many times, stop the event.
        }
        return true; // Continue trying.
    }
}
