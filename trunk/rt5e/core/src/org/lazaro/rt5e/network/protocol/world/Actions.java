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
package org.lazaro.rt5e.network.protocol.world;

/**
 * @author Lazaro
 */
public interface Actions {
    public static enum DisplayMode {
        FIXED(1), FULL_SCREEN(3), RESIZABLE(2);

        public static DisplayMode forValue(int value) {
            switch (value) {
                case 1:
                    return FIXED;
                case 2:
                    return RESIZABLE;
                case 3:
                    return FULL_SCREEN;
            }
            return null;
        }

        private int value;

        private DisplayMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public Actions sendAccessMask(int set1, int set2, int interfaceId1,
                                  int childId1, int interfaceId2, int childId2);

    public Actions sendFixedScreen();

    public Actions sendInterface(int id, int window, int location,
                                 boolean walkable);

    // public Actions sendItems(int interfaceId, ItemCollection items, boolean
    // positionBoolean); // TODO

    public Actions sendLobbyResponse(int response);

    public Actions sendLogin();

    public Actions sendLoginResponse(int response);

    public Actions sendLogout();

    public Actions sendMapRegion();

    public Actions sendMessage(String message);

    public Actions sendMessage(String message, String messageExtension, int req);

    public Actions sendResizableScreen();

    public Actions sendRunningEnergy();

    public Actions sendSkill(int skillId);

    public Actions sendSkills();

    public Actions sendTab(int location, int id);

    public Actions sendWindow(int window);

    public Actions sendWorldList();

    public Actions switchToFixedScreen();

    public Actions switchToResizableScreen();

    public Actions switchWindows(int windowFrom, int windowPosFrom,
                                 int windowTo, int windowPosTo);
}
