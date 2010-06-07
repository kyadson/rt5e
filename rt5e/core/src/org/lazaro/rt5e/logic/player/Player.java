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

import org.lazaro.rt5e.Constants;
import org.lazaro.rt5e.Context;
import org.lazaro.rt5e.logic.Entity;
import org.lazaro.rt5e.logic.item.AppearanceListener;
import org.lazaro.rt5e.logic.item.ItemCollection;
import org.lazaro.rt5e.logic.map.PlayerWaypointQueue;
import org.lazaro.rt5e.logic.map.Tile;
import org.lazaro.rt5e.logic.map.WaypointQueue;
import org.lazaro.rt5e.network.Connection;
import org.lazaro.rt5e.network.Packet;
import org.lazaro.rt5e.network.protocol.world.Actions;
import org.lazaro.rt5e.network.protocol.world.Actions597;
import org.lazaro.rt5e.utility.Destroyed;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @author Lazaro
 */
public class Player extends Entity implements Destroyed {
    public void destroy() {
        if (!destroyed) {
            destroyed = true;

            Context.getWorld().remove(this);
        }
    }

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

    private boolean destroyed = false;
    private Actions actions;
    private Connection connection;
    private Actions.DisplayMode displayMode = null;
    private Gender gender = Gender.MALE;
    private int loginOpcode = 0;
    private Tile mapRegion = null;
    private boolean mapRegionChanged = false;
    private String name = null;
    private long nameHash = 0;
    private boolean onLogin = true;
    private Queue<Packet> packetQueue = new ArrayDeque();
    private boolean[] packetReceived = new boolean[256];
    private String password = null;
    private Rights rights = Rights.PLAYER;
    private boolean running = false;
    private int runningEnergy = 100;
    private WaypointQueue waypointQueue;
    private Skills skills;
    private Appearance appearance = new Appearance();
    private Packet cachedAppearanceBlock = null;
    private ItemCollection equipment;
    private byte[] gpiFlag = new byte[2048];
    private boolean online = true;
    private String protocolName = null;

    public Player(Connection connection) {
        this.connection = connection;

        actions = new Actions597(this);
        waypointQueue = new PlayerWaypointQueue(this);
        skills = new Skills(this);
        equipment = new ItemCollection(this, Constants.Equipment.EQUIPMENT_SIZE);
    }

    @Override
    protected void _process() {
    }

    @Override
    protected void _reset() {
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public Actions getActions() {
        return actions;
    }

    public Connection getConnection() {
        return connection;
    }

    public Actions.DisplayMode getDisplayMode() {
        return displayMode;
    }

    public Gender getGender() {
        return gender;
    }

    public int getLoginOpcode() {
        return loginOpcode;
    }

    public Tile getMapRegion() {
        return mapRegion;
    }

    public String getName() {
        return name;
    }

    public long getNameHash() {
        return nameHash;
    }

    public Queue<Packet> getPacketQueue() {
        return packetQueue;
    }

    public String getPassword() {
        return password;
    }

    public Rights getRights() {
        return rights;
    }

    public boolean isMapRegionChanged() {
        return mapRegionChanged;
    }

    public boolean isOnLogin() {
        return onLogin;
    }

    public boolean isPacketReceived(int opcode) {
        return packetReceived[opcode];
    }

    public void onLogin() {
        equipment.addListener(new AppearanceListener());
        equipment.refresh();

        actions.sendMessage("Welcome to " + Constants.GAME_NAME + ".");

        masks.setAppearance(true);
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setDisplayMode(Actions.DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setLoginOpcode(int loginOpcode) {
        this.loginOpcode = loginOpcode;
    }

    public void setMapRegion(Tile mapRegion) {
        this.mapRegion = mapRegion;
    }

    public void setMapRegionChanged(boolean mapRegionChanged) {
        this.mapRegionChanged = mapRegionChanged;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNameHash(long nameHash) {
        this.nameHash = nameHash;
    }

    public void setOnLogin(boolean onLogin) {
        this.onLogin = onLogin;
    }

    public void setPacketReceived(int opcode, boolean value) {
        packetReceived[opcode] = value;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRights(Rights rights) {
        this.rights = rights;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public int getRunningEnergy() {
        return runningEnergy;
    }

    public void setRunningEnergy(int runningEnergy) {
        this.runningEnergy = runningEnergy;
    }

    public WaypointQueue getWaypointQueue() {
        return waypointQueue;
    }

    public Skills getSkills() {
        return skills;
    }

    public Appearance getAppearance() {
        return appearance;
    }

    public Packet getCachedAppearanceBlock() {
        return cachedAppearanceBlock;
    }

    public void setCachedAppearanceBlock(Packet cachedAppearanceBlock) {
        this.cachedAppearanceBlock = cachedAppearanceBlock;
    }

    public ItemCollection getEquipment() {
        return equipment;
    }

    public byte getGPIFlag(int index) {
        return gpiFlag[index];
    }

    public void setGPIFlag(int index, byte val) {
        gpiFlag[index] = val;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    @Override
    public String toString() {
        return "name=" + protocolName + ", password=" + password.replaceAll(".", "*")
                + ", index=" + getIndex();
    }
}
