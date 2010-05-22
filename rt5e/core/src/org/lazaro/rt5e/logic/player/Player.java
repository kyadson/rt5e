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

import org.lazaro.rt5e.logic.Entity;
import org.lazaro.rt5e.logic.map.Tile;
import org.lazaro.rt5e.network.Connection;
import org.lazaro.rt5e.network.protocol.world.Actions;
import org.lazaro.rt5e.network.protocol.world.Actions597;

/**
 * @author Lazaro
 */
public class Player extends Entity {
    public static enum Gender {
        FEMALE(1), MALE(0);

        private int value;

        private Gender(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static enum Rights {
        ADMINISTRATOR(2), MODERATOR(1), PLAYER(0);

        private int value;

        private Rights(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private String name = null;

    public boolean isOnLogin() {
        return onLogin;
    }

    public void setOnLogin(boolean onLogin) {
        this.onLogin = onLogin;
    }

    private boolean onLogin = true;

    public Rights getRights() {
        return rights;
    }

    public void setRights(Rights rights) {
        this.rights = rights;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    private Rights rights = Rights.PLAYER;
    private Gender gender = Gender.MALE;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    private Connection connection;

    public Player(Connection connection) {
        this.connection = connection;

        actions = new Actions597(this);
    }

    public int getLoginOpcode() {
        return loginOpcode;
    }

    public void setLoginOpcode(int loginOpcode) {
        this.loginOpcode = loginOpcode;
    }

    private int loginOpcode = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getNameHash() {
        return nameHash;
    }

    public void setNameHash(long nameHash) {
        this.nameHash = nameHash;
    }

    private String password = null;
    private long nameHash = 0;


    private Actions.DisplayMode displayMode = null;
    private Tile mapRegion = null;

    public boolean isMapRegionChanged() {
        return mapRegionChanged;
    }

    public void setMapRegionChanged(boolean mapRegionChanged) {
        this.mapRegionChanged = mapRegionChanged;
    }

    public Tile getMapRegion() {
        return mapRegion;
    }

    public void setMapRegion(Tile mapRegion) {
        this.mapRegion = mapRegion;
    }

    public Actions.DisplayMode getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(Actions.DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    private boolean mapRegionChanged = false;

    public Actions getActions() {
        return actions;
    }

    private Actions actions;


    @Override
    protected void _process() {
    }

    @Override
    protected void _reset() {
    }

    public void onLogin() {
    }

    public String toString() {
        return "name=" + name + ", password=" + password.replaceAll(".", "*") + ", index=" + getIndex();
    }
}
