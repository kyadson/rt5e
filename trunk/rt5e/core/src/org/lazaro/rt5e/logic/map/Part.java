package org.lazaro.rt5e.logic.map;

import org.lazaro.rt5e.logic.Locatable;
import org.lazaro.rt5e.logic.player.Player;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Lazaro
 */
public class Part {
    private List<Player> players;
    private int x, y;

    public Part(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void add(Locatable locatable) {
        if (locatable instanceof Player) {
            getPlayers().add((Player) locatable);
        }
    }

    public boolean containsPlayers() {
        return players != null && players.size() > 0;
    }

    public List<Player> getPlayers() {
        if (players == null) {
            players = new LinkedList<Player>();
        }
        return players;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void remove(Locatable locatable) {
        if (locatable instanceof Player) {
            getPlayers().remove(locatable);
        }
    }
}
