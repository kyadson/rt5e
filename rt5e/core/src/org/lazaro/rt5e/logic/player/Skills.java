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

import org.lazaro.rt5e.logic.utility.Status;

/**
 * @author Lazaro
 */
public class Skills implements Status {
    public static final int SKILL_COUNT = 24;
    private byte[] currentLevels = new byte[SKILL_COUNT]; // The current levels
    private double[] experiences = new double[SKILL_COUNT]; // Experience
    private byte[] levels = new byte[SKILL_COUNT]; // The levels for experience
    private Player player;

    public Skills(Player player) {
        this.player = player;

        /* We can remove this when player saving is done */
        for (int i = 0; i < SKILL_COUNT; i++) {
            levels[i] = currentLevels[i] = 1;
        }
    }

    /**
     * Adds experience to the specified skill.
     *
     * @param skillId The skill to add experience to.
     * @param amount  The amount of experience.
     */
    public void addExp(int skillId, double amount) {
        if (skillId >= SKILL_COUNT || skillId < 0) {
            return;
        }
        byte oldLevel = levels[skillId]; // Save the old level
        experiences[skillId] += amount; // Add the experience
        byte newLevel = levels[skillId] = levelForExp(skillId); // Calculate the new level
        if (oldLevel < newLevel) { // The level changed.
            if (skillId == 3) {
                updateHp(newLevel - oldLevel); // Update the HP.
            } else {
                currentLevels[skillId] += (newLevel - oldLevel); // Add up the levels.
            }
            // TODO Level up animations, etc.
        }
        player.getActions().sendSkill(skillId);
    }

    /**
     * Gets the current level of a skill.
     * <p/>
     * Note: This is not the level based on experience!
     *
     * @param skillId The skill id.
     * @return The current level.
     */
    public byte getCurrentLevel(int skillId) {
        return currentLevels[skillId];
    }

    /**
     * Gets the experience for the skill specified.
     *
     * @param skillId The skill id.
     * @return The experience.
     */
    public double getExperience(int skillId) {
        return experiences[skillId];
    }

    /**
     * Gets the actual level based on the experience.
     *
     * @param skillId The skill id.
     * @return The current level.
     */
    public byte getLevel(int skillId) {
        return levels[skillId];
    }

    /**
     * Calculates the actual level for the amount of experience.
     *
     * @param skillId The skill id to calculate for.
     * @return The actual level based on the experience.
     */
    private byte levelForExp(int skillId) {
        int exp = (int) experiences[skillId];
        int points = 0;
        int output = 0;
        for (byte lvl = 1; lvl < 100; lvl++) {
            points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
            output = (int) Math.floor(points / 4);
            if (exp < output) {
                return lvl;
            }
        }
        return 99;
    }

    /**
     * For player loading ONLY!
     *
     * @param skillId The skill id.
     * @param level   The level.
     */
    public void setCurrentLevel(int skillId, byte level) {
        currentLevels[skillId] = level;
    }

    /**
     * For player loading ONLY!
     *
     * @param skillId    The skill id.
     * @param experience The experience.
     */
    public void setExperience(int skillId, double experience) {
        experiences[skillId] = experience;
    }

    /**
     * For player loading ONLY!
     *
     * @param skillId The skill id.
     * @param level   The level.
     */
    public void setLevel(int skillId, byte level) {
        levels[skillId] = level;
    }

    public void update() {
        for (int i = 0; i < SKILL_COUNT; i++) {
            player.getActions().sendSkill(i);
        }
    }

    /**
     * Updates the client with the new HP.
     *
     * @param diffHp The difference in HP.
     */
    public void updateHp(int diffHp) {
        currentLevels[3] += diffHp;
        if (currentLevels[3] > levels[3]) {
            currentLevels[3] = levels[3];
        } else if (currentLevels[3] <= 0) {
            currentLevels[3] = 0;
            player.setDead(true);
            player.getWaypointQueue().reset();
            // TODO Schedule death event.
        }
        player.getActions().sendSkill(3);
    }
}
