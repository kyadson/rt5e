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
package org.lazaro.rt5e.logic.item;

import org.lazaro.rt5e.logic.Node;

/**
 * @author Lazaro
 */
public class Item extends Node {
    protected int amount;
    protected final int type;

    public Item(int type) {
        this(type, 1);
    }

    public Item(int type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    /**
     * Note: This method only compares the type of item! Not the amount!
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Item) {
            return ((Item) object).type == type;
        }
        return false;
    }

    public int getAmount() {
        return amount;
    }

    public ItemDefinition getDefinition() {
        return ItemDefinition.forType(type);
    }

    public int getType() {
        return type;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}