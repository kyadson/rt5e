package org.lazaro.rt5e.logic.mask;

import org.lazaro.rt5e.logic.Entity;
import org.lazaro.rt5e.logic.player.Player;

/**
 * @author Lazaro
 */
public class FaceDirection implements Mask {
    private int id;

    public FaceDirection(Entity e) {
        this(e instanceof Player ? ((Player) e).getIndex() + 0x5000 : e.getIndex());
    }

    public FaceDirection(int id) {
        this.id = id;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
}
