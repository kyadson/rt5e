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
package org.lazaro.rt5e.logic.player;

/**
 * @author Lazaro
 */
public class Appearance {
    private int[] color = new int[5];
    private int[] look = new int[7];
    private int npcType = -1;
    private int pkIcon = -1;
    private int prayerIcon = -1;

    public Appearance() {
        look[1] = 10;
        look[2] = 18;
        look[3] = 26;
        look[4] = 33;
        look[5] = 36;
        look[6] = 42;
    }

    public int getArms() {
        return look[3];
    }

    public int getBeard() {
        return look[1];
    }

    public int[] getColor() {
        return color;
    }

    public int getColor(int id) {
        return color[id];
    }

    public int getFeet() {
        return look[6];
    }

    public int getFeetColor() {
        return color[4];
    }

    public int getHairColor() {
        return color[1];
    }

    public int getHands() {
        return look[4];
    }

    public int getHead() {
        return look[0];
    }

    public int getLegs() {
        return look[5];
    }

    public int getLegsColor() {
        return color[3];
    }

    public int[] getLook() {
        return look;
    }

    public int getLook(int id) {
        return look[id];
    }

    public int getNPCType() {
        return npcType;
    }

    public int getPKIcon() {
        return pkIcon;
    }

    public int getPrayerIcon() {
        return prayerIcon;
    }

    public int getSkinColor() {
        return color[0];
    }

    public int getTorso() {
        return look[2];
    }

    public int getTorsoColor() {
        return color[2];
    }

    public boolean isNPC() {
        return npcType != -1;
    }

    public void setColor(int[] color) {
        this.color = color;
    }

    public void setLook(int[] look) {
        this.look = look;
    }

    public void setPKIcon(int pkIcon) {
        this.pkIcon = pkIcon;
    }

    public void setPrayerIcon(int prayerIcon) {
        this.prayerIcon = prayerIcon;
    }

    public void toNPC(int npcType) {
        this.npcType = npcType;
    }

    public void toPlayer() {
        npcType = -1;
    }
}
