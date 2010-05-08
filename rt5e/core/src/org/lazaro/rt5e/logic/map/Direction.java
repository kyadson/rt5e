package org.lazaro.rt5e.logic.map;

/**
 * @author Lazaro
 */
public class Direction {
    public static final byte[] DIRECTION_DELTA_X = new byte[]{1, 2, 4, 7, 6, 5, 3, 0};

    public static final byte[] DIRECTION_DELTA_Y = new byte[]{1, 0, 1, -1, 1, -1, 0, 1};

    public static final byte[] DIRECTION_XLATE = new byte[]{-1, -1, -1, 0, 0, 1, 1, 1};

    /**
     * Generates the direction in which to turn.
     *
     * @param lastTile The last waypoint.
     * @param nextTile The next waypoint.
     * @return The direction in which to turn.
     */
    public static int walkingDirectionFor(Tile lastTile, Tile nextTile) {
        int directionX = nextTile.getX() - lastTile.getX();
        int directionY = nextTile.getY() - lastTile.getY();
        if (directionX < 0) {
            if (directionY < 0)
                return 0;
            else if (directionY > 0)
                return 5;
            else
                return 3;
        } else if (directionX > 0) {
            if (directionY < 0)
                return 2;
            else if (directionY > 0)
                return 7;
            else
                return 4;
        } else {
            if (directionY < 0)
                return 1;
            else if (directionY > 0)
                return 6;
            else
                return -1;
        }
    }

    private int direction = -1;

    private boolean firstDirection = false;

    /**
     * Returns -1 if the player isn't moving.
     *
     * @return The next tile's direction.
     */
    public int getDirection() {
        return direction;
    }

    public boolean isFirstDirection() {
        return firstDirection;
    }

    public void reset() {
        direction = -1;
        firstDirection = false;
    }

    /**
     * Sets the directions.
     *
     * @param direction The direction
     */
    public void setDirections(int direction) {
        this.direction = direction;
    }

    public void setFirstDirection(boolean firstDirection) {
        this.firstDirection = firstDirection;
    }
}
