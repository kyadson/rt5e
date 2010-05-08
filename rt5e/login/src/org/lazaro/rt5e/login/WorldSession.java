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

    public boolean isLobbyWorld() {
        return lobbyWorld;
    }

    public void setLobbyWorld(boolean lobbyWorld) {
        this.lobbyWorld = lobbyWorld;
    }

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

    public void setId(int id) {
        this.id = id;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }
}
