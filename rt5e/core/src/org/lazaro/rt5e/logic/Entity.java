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

import org.lazaro.rt5e.Constants;
import org.lazaro.rt5e.logic.map.Direction;
import org.lazaro.rt5e.logic.map.Tile;
import org.lazaro.rt5e.logic.mask.Masks;
import org.lazaro.rt5e.logic.player.Player;
import org.lazaro.rt5e.network.Packet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lazaro
 */
public abstract class Entity extends Locatable {
    public static final Tile SPAWN = Tile.locate(3222, 3222, 0);
    private Packet cachedAppearanceBlock = null;
    private Packet cachedMaskBlock = null;
    private boolean dead = false;
    protected final Direction direction = new Direction();
    private final List<Player> localPlayers = new ArrayList<Player>(Constants.MAX_LOCAL_PLAYERS);
    protected final Masks masks = new Masks();
    private Tile teleportDestination = null;
    private boolean teleporting = false;

    protected abstract void _process();

    protected abstract void _reset();

    public Packet getCachedMaskBlock() {
        return cachedMaskBlock;
    }

    public Direction getDirection() {
        return direction;
    }

    public List<Player> getLocalPlayers() {
        return localPlayers;
    }

    public Masks getMasks() {
        return masks;
    }

    public Tile getTeleportDestination() {
        return teleportDestination;
    }

    public boolean hasCachedUpdateBlock() {
        return cachedMaskBlock != null;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isTeleporting() {
        return teleporting;
    }

    public void process() {
        _process();
    }

    public void reset() {
        masks.reset();
        direction.reset();
        cachedMaskBlock = null;
        teleporting = false;

        _reset();
    }

    public void setCachedMaskBlock(Packet cachedMaskBlock) {
        this.cachedMaskBlock = cachedMaskBlock;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void setTeleportDestination(Tile teleportDestination) {
        this.teleportDestination = teleportDestination;
    }

    public void setTeleporting(boolean teleporting) {
        this.teleporting = teleporting;
    }

    public Packet getCachedAppearanceBlock() {
        return cachedAppearanceBlock;
    }

    public void setCachedAppearanceBlock(Packet cachedAppearanceBlock) {
        this.cachedAppearanceBlock = cachedAppearanceBlock;
    }

    public boolean hasCachedAppearanceBlock() {
        return cachedAppearanceBlock != null;
    }
}
