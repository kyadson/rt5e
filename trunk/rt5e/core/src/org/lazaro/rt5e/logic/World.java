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

import org.lazaro.rt5e.logic.player.Player;
import org.lazaro.rt5e.logic.utility.NodeCollection;
import org.lazaro.rt5e.login.WorldConnector;

/**
 * @author Lazaro
 */
public class World implements Runnable {
    public int getId() {
        return id;
    }

    private final int id;

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    private boolean running = true;

    public WorldConnector getSession() {
        return session;
    }

    private WorldConnector session;

    private NodeCollection<Player> globalPlayers = new NodeCollection<Player>();

    public boolean register(Player player) {
        boolean success = globalPlayers.add(player);
        if (success) {
            System.out.println("Registered player [" + player + "]");
        }
        return success;
    }

    public void remove(Player player) {
        globalPlayers.remove(player);
        System.out.println("Removed player [" + player + "]");
    }

    public World(int id) {
        this.id = id;
    }

    public void start() throws Exception {
        session = new WorldConnector();
    }

    public void run() {
    }
}
