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

import org.lazaro.rt5e.Context;

/**
 * @author Lazaro
 */
public class ItemDefinition {
    public static ItemDefinition forType(int type) {
        try {
            ItemDefinition def = Context.getWorld().getItemDefinitions().get(
                    type);
            if (def == null) {
                def = prepareDefinition(type);
                Context.getWorld().getItemDefinitions().add(type, def);
            }
            return def;
        } catch (IndexOutOfBoundsException e) {
        }
        return prepareDefinition(type);
    }

    public static ItemDefinition prepareDefinition(int type) {
        return new ItemDefinition("Item #" + type, type, -1, "It's an item!",
                false, false, 1, 1, 1, new int[14], -1, -1);
    }

    private final int bodyType;
    private final int[] bonus;
    private final int equipmentSlot;
    private final String examine;
    private final int maximumPrice;
    private final int minimumPrice;
    private final String name;
    private final int normalPrice;
    private final boolean noted;
    private final boolean stackable;
    private final int type;
    private final int xlateId;

    public ItemDefinition(String name, int type, int xlateId, String examine,
                          boolean stackable, boolean noted, int minimumPrice,
                          int normalPrice, int maximumPrice, int[] bonus, int equipmentSlot,
                          int bodyType) {
        this.name = name;
        this.type = type;
        this.minimumPrice = minimumPrice;
        this.normalPrice = normalPrice;
        this.maximumPrice = maximumPrice;
        this.examine = examine;
        this.bonus = bonus;
        this.stackable = stackable;
        this.noted = noted;
        this.xlateId = xlateId;
        this.equipmentSlot = equipmentSlot;
        this.bodyType = bodyType;
    }

    public int getBodyType() {
        return bodyType;
    }

    public int[] getBonus() {
        return bonus;
    }

    public int getEquipmentSlot() {
        return equipmentSlot;
    }

    public String getExamine() {
        return examine;
    }

    public int getMaximumPrice() {
        return maximumPrice;
    }

    public int getMinimumPrice() {
        return minimumPrice;
    }

    public String getName() {
        return name;
    }

    public int getNormalPrice() {
        return normalPrice;
    }

    public int getType() {
        return type;
    }

    public int getXlateId() {
        return xlateId;
    }

    /**
     * If true, the item can be equipped.
     *
     * @return If the item can be equipped.
     */
    public boolean isEquipable() {
        return equipmentSlot != -1;
    }

    /**
     * The item covers the player's arms.
     *
     * @return If the item covers the player's arms.
     */
    public boolean isFullBody() {
        return bodyType == 0;
    }

    /**
     * The item covers the player's head but not beard.
     *
     * @return If the item covers the player's head but not beard.
     */
    public boolean isHat() {
        return bodyType == 1;
    }

    /**
     * The item covers the player's entire head.
     *
     * @return If the item covers the player's entire head.
     */
    public boolean isMask() {
        return bodyType == 2;
    }

    public boolean isNoted() {
        return noted;
    }

    public boolean isStackable() {
        return stackable;
    }
}
