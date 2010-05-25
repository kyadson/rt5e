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
package org.lazaro.rt5e.login;

import org.lazaro.rt5e.network.Connection;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Lazaro
 */
public class WorldSession {
    private Connection connection;
    private int id = -1;

    private boolean lobbyWorld = false;

    private List<String> players = new LinkedList<String>();

    public WorldSession(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public int getId() {
        return id;
    }

    public List<String> getPlayers() {
        return players;
    }

    public boolean isLobbyWorld() {
        return lobbyWorld;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLobbyWorld(boolean lobbyWorld) {
        this.lobbyWorld = lobbyWorld;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }
}
