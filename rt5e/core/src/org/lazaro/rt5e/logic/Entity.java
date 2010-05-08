package org.lazaro.rt5e.logic;

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

    private Packet cachedMaskBlock = null;
    private boolean dead = false;

    protected final Direction direction = new Direction();

    private final List<Player> localPlayers = new ArrayList<Player>(255);

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
}