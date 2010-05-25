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
package org.lazaro.rt5e.logic;

import org.lazaro.rt5e.logic.map.Part;
import org.lazaro.rt5e.logic.map.Tile;

/**
 * @author Lazaro
 */
public class Locatable extends Node {
    private Tile location = null;

    private Part part = null;

    public Tile getLocation() {
        return location;
    }

    public Part getPart() {
        return part;
    }

    public void setLocation(Tile location) {
        /*
           * int x = location.getPartX(), y = location.getPartY();
           *
           * if (part == null || part.getX() != x || part.getY() != y) { if (part
           * != null) { part.remove(this); } part =
           * World.getInstance().getMap().getPart(x, y); part.add(this); }
           */

        this.location = location;
    }
}
