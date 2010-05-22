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

import org.jboss.netty.channel.ChannelFutureListener;
import org.lazaro.rt5e.WorldApp;
import org.lazaro.rt5e.logic.login.LoginResponse;
import org.lazaro.rt5e.logic.player.Player;
import org.lazaro.rt5e.network.Packet;
import org.lazaro.rt5e.network.PacketBuilder;

/**
 * @author Lazaro
 */
public class Actions597 implements Actions {
    private Player player;

    public Actions597(Player player) {
        this.player = player;
    }

    public Actions sendAccessMask(int set1, int set2, int interfaceId1, int childId1, int interfaceId2, int childId2) {
        return this;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Actions sendFixedScreen() {
        sendWindow(548);
        return this;
    }

    public Actions sendInterface(int id, int window, int location, boolean walkable) {
        return this;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Actions sendLobbyResponse(int response) {
        PacketBuilder pb = new PacketBuilder();
        pb.putByte(response);

        if (response == LoginResponse.LOGIN.getResponseCode()) {
            PacketBuilder responseBlock = new PacketBuilder();
            responseBlock.putByte(1);
            responseBlock.putByte(1);
            responseBlock.putByte(1);
            responseBlock.putByte(1);
            responseBlock.putByte(1);
            responseBlock.putShort(30); // member days left

            responseBlock.putShort(0);
            responseBlock.putShort(0);
            responseBlock.putShort(0);
            responseBlock.putInt(0);

            responseBlock.putByte(0);
            responseBlock.putShort(0);
            responseBlock.putShort(0);

            responseBlock.putByte(0);
            responseBlock.putGJString2(player.getName());
            responseBlock.putShort(1); // current world id
            responseBlock.putGJString2("127.0.0.1"); // TODO

            Packet responseBlockMessage = responseBlock.toPacket();
            pb.putByte(responseBlockMessage.getLength()).put(responseBlockMessage.getBytes());

            player.getConnection().write(pb.toPacket());
        } else {
            player.getConnection().write(pb.toPacket()).addListener(ChannelFutureListener.CLOSE);
        }
        return this;
    }

    public Actions sendLogin() {
        sendMapRegion();

        sendFixedScreen();
        player.setOnLogin(true);
        return this;
    }

    public Actions sendLoginResponse(int response) {
        PacketBuilder pb = new PacketBuilder();
        pb.putByte(response);

        if (response == LoginResponse.LOGIN.getResponseCode()) {
            PacketBuilder responseBlock = new PacketBuilder();
            responseBlock.putByte(player.getRights().getValue()).putByte(0).putByte(0).putByte(0).putByte(0).putByte(0).putShort(player.getIndex()).putByte(1).putByte(1);

            Packet responseBlockMessage = responseBlock.toPacket();
            pb.putByte(responseBlockMessage.getLength()).put(responseBlockMessage.getBytes());

            player.getConnection().write(pb.toPacket());
        } else {
            player.getConnection().write(pb.toPacket()).addListener(ChannelFutureListener.CLOSE);
        }
        return this;
    }

    public Actions sendLogout() {
        return this;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Actions sendMapRegion() {
        PacketBuilder pb = new PacketBuilder(95, Packet.Type.VAR_SHORT);

        if (player.isOnLogin()) {
            pb.putBits(30, player.getLocation().getX() << 14 | player.getLocation().getY() & 0x3fff | player.getLocation().getZ() << 28);
            for (int i = 1; i < 2048; i++) {
                if (i != player.getIndex()) {
                    pb.putBits(18, 0);
                }
            }
        }

        pb.putShortA(player.getLocation().getPartX()).putByteS(player.getLocation().getZ()).putShort(player.getLocation().getPartY()).putByte(1);

        boolean forceRegion = (player.getLocation().getPartX() / 8 == 48 || player.getLocation().getPartX() / 8 == 49) && player.getLocation().getPartY() / 8 == 48;
        if (player.getLocation().getPartX() / 8 == 48 && player.getLocation().getPartY() / 8 == 148) {
            forceRegion = true;
        }
        for (int xCalc = (player.getLocation().getPartX() - 6) / 8; xCalc <= (player.getLocation().getPartX() + 6) / 8; xCalc++) {
            for (int yCalc = (player.getLocation().getPartY() - 6) / 8; yCalc <= (player.getLocation().getPartY() + 6) / 8; yCalc++) {
                int region = yCalc + (xCalc << 8);
                int[] key = WorldApp.getMapXTEA().getKey(region);
                if (forceRegion && (yCalc == 49 || yCalc == 149 || yCalc == 147 || xCalc == 50 || xCalc == 49 && yCalc == 47)) {
                    for (int i = 0; i < 4; i++) {
                        pb.putInt(key[i]);
                    }
                } else {
                    for (int i = 0; i < 4; i++) {
                        pb.putInt(key[i]);
                    }
                }
            }
        }
        player.getConnection().write(pb.toPacket());

        player.setMapRegion(player.getLocation());
        return this;
    }

    public Actions sendMessage(String message) {
        return this;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Actions sendMessage(String message, String messageExtension, int req) {
        return this;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Actions sendResizableScreen() {
        return this;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Actions sendSkill(int skillId) {
        return this;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Actions sendTab(int location, int id) {
        return this;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Actions sendWindow(int window) {
        PacketBuilder smf = new PacketBuilder(103);
        smf.putLEShortA(0).putByteS(0).putShort(window);
        player.getConnection().write(smf.toPacket());
        return this;
    }

    public Actions sendWorldList() {
        return this;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Actions switchToFixedScreen() {
        return this;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Actions switchToResizableScreen() {
        return this;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Actions switchWindows(int windowFrom, int windowPosFrom, int windowTo, int windowPosTo) {
        return this;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
