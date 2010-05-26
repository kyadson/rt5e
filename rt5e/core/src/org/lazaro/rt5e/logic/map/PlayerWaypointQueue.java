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

import org.lazaro.rt5e.logic.player.Player;

/**
 * @author Lazaro
 */
public class PlayerWaypointQueue extends WaypointQueue {
    private Player player;

    public PlayerWaypointQueue(Player player) {
        super();
        this.player = player;
    }

    @Override
    protected int next() {
        /**
         * Check if we are walking.
         */
        if (moving()) {
            /**
             * Get the first waypoint from the queue.
             */
            Tile nextPoint = waypoints.peek();

            /**
             * Check if we are already on this tile.
             */
            if (player.getLocation().equals(nextPoint)) {
                /**
                 * Remove the current waypoint and get the next one.
                 */
                waypoints.remove();
                nextPoint = waypoints.peek();
            }

            /**
             * Check if there is a next step.
             */
            if (nextPoint == null) {
                /**
                 * Stop walking.
                 */
                return -1;
            }

            /**
             * Calculate the direction of the next waypoint.
             */
            int direction = Direction.walkingDirectionFor(player.getLocation(),
                    nextPoint);

            /**
             * We want to move.
             */
            if (direction != -1) {
                /**
                 * Calculate the next tile.
                 */
                Tile next = player.getLocation().translate(
                        Direction.DIRECTION_DELTA_X[direction],
                        Direction.DIRECTION_DELTA_Y[direction], 0);

                /**
                 * Check if the map region is going to change.
                 */
                if (player.getMapRegion().differentMap(next)) {
                    /**
                     * Flag the map changing.
                     */
                    player.setMapRegionChanged(true);
                }

                /**
                 * Set the tile and return the direction.
                 */
                player.setLocation(next);
                return direction;
            }
        }
        return -1;
    }

    @Override
    public void process() {
        if (player.getTeleportDestination() != null) { // Check if we are teleporting.
            Tile oldLocation = player.getLocation(); // Save the old location.
            player.setLocation(player.getTeleportDestination()); // Set the new location.
            player.setTeleportDestination(null); // Reset the teleport variables.
            player.setTeleporting(true); // Flag the teleport.
            player.setMapRegionChanged(oldLocation.differentMap(player.getLocation())); // Flag if the map has changed.
            reset(); // Reset the waypoint queue.
        } else if (waypoints.size() > 0) { // Check if we are moving.
            int direction = next(); // Calculate the first direction, for walking.
            int secondDirection = -1;
            if (running || player.isRunning()) { // Check if we are running.
                if (player.getRunningEnergy() > 0) { // Check if the player has enough energy to run.
                    secondDirection = next(); // Calculate the second direction, for running.
                    player.setRunningEnergy(player.getRunningEnergy() - 1); // Decrease energy.
                    player.getActions().sendRunningEnergy();
                } else {
                    running = false; // Stop running this queue.
                    player.setRunning(false); // Stop the player from running.
                    // TODO Toggle running off client-sided.
                }
            }
            if (player.isMapRegionChanged()) {
                direction = secondDirection = -1; // Stop moving via player update.
            }
            player.getDirection().setDirections(direction); // Set the directions.
        }
        if (coordinateFuture != null) { // Check if there is a coordinate future.
            if (!coordinateFuture.update()) { // Update the coordinate future, and check if we should remove it.
                coordinateFuture = null; // Remove the coordinate future.
            }
        }
    }

    public void reset() {
        super.reset();

        player.getDirection().setFirstDirection(true);
    }
}
