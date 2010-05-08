package org.lazaro.rt5e.logic.mask;

import org.lazaro.rt5e.logic.Node;

/**
 * @author Lazaro
 */
public class Hit implements Mask {
    public static final int NO_DAMAGE = 0, NORMAL_DAMAGE = 1,
            POISON_DAMAGE = 2, DESEASE_DAMAGE = 3;

    private int damage;
    private Node from;
    private int type;

    public Hit(Node from, int damage) {
        this.from = from;
        this.damage = damage;
        this.type = damage > 0 ? NORMAL_DAMAGE : NO_DAMAGE;
    }

    public Hit(Node from, int damage, int type) {
        this.from = from;
        this.damage = damage;
        this.type = type;
    }

    /**
     * @return the damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * @return the <code>Node</code> the damage was from.
     */
    public Node getFrom() {
        return from;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }
}
