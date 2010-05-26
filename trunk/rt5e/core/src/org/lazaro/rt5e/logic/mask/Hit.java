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
