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
