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
        /* int x = location.getPartX(), y = location.getPartY();

        if (part == null || part.getX() != x || part.getY() != y) {
            if (part != null) {
                part.remove(this);
            }
            part = World.getInstance().getMap().getPart(x, y);
            part.add(this);
        } */

        this.location = location;
    }
}
