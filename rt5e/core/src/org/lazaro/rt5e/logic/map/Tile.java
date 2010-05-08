package org.lazaro.rt5e.logic.map;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lazaro
 */
public class Tile {
    private static Map<Integer, Tile> tileMap = new HashMap<Integer, Tile>();

    public static double distanceFormula(int x, int y, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2));
    }

    public static Tile locate(int x, int y, int z) {
        /* This implementation is more memory efficient, sacrificing a little CPU */
        int tileHash = z << 30 | x << 15 | y;

        Tile tile = tileMap.get(tileHash);
        if (tile == null) {
            tile = new Tile(x, y, z);
            tileMap.put(tileHash, tile);
        }
        return tile;

        /* This implementation is faster, but is not very memory efficient, and it requires more GC operations. */
        // return new Tile(x, y, z);
    }

    private final int x, y, z;

    public Tile(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean differentMap(Tile tile) {
        return distanceFormula(getPartX(), getPartY(), tile.getPartX(),
                tile.getPartY()) >= 4;
    }

    public int distance(Tile tile) {
        return (int) distanceFormula(x, y, tile.x, tile.y);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Tile) {
            Tile tile = (Tile) object;
            return x == tile.x && y == tile.y;
        }
        return false;
    }

    public int getLocalX() {
        return getLocalX(this);
    }

    public int getLocalX(Tile base) {
        return x - ((base.getPartX() - 6) << 3);
    }

    public int getLocalY() {
        return getLocalY(this);
    }

    public int getLocalY(Tile base) {
        return y - ((base.getPartY() - 6) << 3);
    }

    public int getPartX() {
        return x >> 3;
    }

    public int getPartY() {
        return y >> 3;
    }

    public int getRegionX() {
        return x / 64;
    }

    public int getRegionY() {
        return y / 64;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public int hashCode() {
        return z << 30 | x << 15 | y;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("x=").append(x).append(", y=")
                .append(y).toString();
    }

    public Tile translate(int diffX, int diffY, int diffZ) {
        return Tile.locate(x + diffX, y + diffY, z + diffZ);
    }

    public boolean withinRange(Tile t) {
        return withinRange(t, 15);
    }

    public boolean withinRange(Tile t, int distance) {
        return t.z == z && distance(t) <= distance;
    }
}
