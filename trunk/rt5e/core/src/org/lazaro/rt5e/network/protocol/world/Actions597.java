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
import org.lazaro.rt5e.engine.event.impl.WorldListUpdater;
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

    public Actions sendAccessMask(int set1, int set2, int interfaceId1,
                                  int childId1, int interfaceId2, int childId2) {
        return this; // To change body of implemented methods use File |
        // Settings | File Templates.
    }

    public Actions sendFixedScreen() {
        sendWindow(548);
        sendTab(159, 748); // HP bar
        sendTab(161, 749); // Prayer bar
        sendTab(162, 750); // Energy bar
        sendTab(164, 747); // Summoning bar
        sendTab(45, 751);
        sendTab(171, 752);
        sendTab(14, 754);
        sendTab(12, 745);
        sendTab(181, 884);
        sendTab(182, 320);
        sendTab(183, 190);
        sendTab(184, 259);
        sendTab(185, 149);
        sendTab(186, 387);
        sendTab(187, 271);
        sendTab(188, 192);
        sendTab(189, 891);
        sendTab(190, 550);
        sendTab(191, 551);
        sendTab(192, 589);
        sendTab(193, 261);
        sendTab(194, 464);
        sendTab(195, 187);
        sendTab(196, 34);
        sendTab(199, 182);
        sendInterface(137, 752, 9, true);
        return this;
    }

    public Actions sendInterface(int id, int window, int location,
                                 boolean walkable) {
        PacketBuilder pb = new PacketBuilder(101);
        pb.putLEShort(0).putInt(window << 16 | location).putByteS(walkable ? 1 : 0).putLEShortA(id);
        player.getConnection().write(pb.toPacket());
        return this;
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
            pb.putByte(responseBlockMessage.getLength()).put(
                    responseBlockMessage.getBytes());

            player.getConnection().write(pb.toPacket());
        } else {
            player.getConnection().write(pb.toPacket()).addListener(
                    ChannelFutureListener.CLOSE);
        }
        return this;
    }

    public Actions sendLogin() {
        sendMapRegion();

        player.onLogin();
        return this;
    }

    public Actions sendLoginResponse(int response) {
        PacketBuilder pb = new PacketBuilder();
        pb.putByte(response);

        if (response == LoginResponse.LOGIN.getResponseCode()) {
            PacketBuilder responseBlock = new PacketBuilder();

            responseBlock.putByte(player.getRights().getValue()).putByte(0)
                    .putByte(0).putByte(0).putByte(0).putByte(0).putShort(
                    player.getIndex()).putByte(1).putTriByte(0)
                    .putByte(1);

            Packet responseBlockPacket = responseBlock.toPacket();
            pb.putByte(responseBlockPacket.getLength()).put(
                    responseBlockPacket.getBytes());

            player.getConnection().write(pb.toPacket());
        } else {
            player.getConnection().write(pb.toPacket()).addListener(
                    ChannelFutureListener.CLOSE);
        }
        return this;
    }

    public Actions sendLogout() {
        return this; // To change body of implemented methods use File |
        // Settings | File Templates.
    }

    public Actions sendMapRegion() {
        PacketBuilder pb = new PacketBuilder(95, Packet.Type.VAR_SHORT);

        if (player.isOnLogin()) {
            pb.putBits(30, player.getLocation().getX() << 14
                    | player.getLocation().getY() & 0x3fff
                    | player.getLocation().getZ() << 28);
            for (int i = 1; i < 2048; i++) {
                if (i != player.getIndex()) {
                    pb.putBits(18, 0);
                }
            }
        }

        pb.putShortA(player.getLocation().getPartX()).putByteS(
                player.getLocation().getZ()).putShort(
                player.getLocation().getPartY()).putByte(1);

        boolean forceRegion = (player.getLocation().getPartX() / 8 == 48 || player
                .getLocation().getPartX() / 8 == 49)
                && player.getLocation().getPartY() / 8 == 48;
        if (player.getLocation().getPartX() / 8 == 48
                && player.getLocation().getPartY() / 8 == 148) {
            forceRegion = true;
        }
        for (int xCalc = (player.getLocation().getPartX() - 6) / 8; xCalc <= (player
                .getLocation().getPartX() + 6) / 8; xCalc++) {
            for (int yCalc = (player.getLocation().getPartY() - 6) / 8; yCalc <= (player
                    .getLocation().getPartY() + 6) / 8; yCalc++) {
                int region = yCalc + (xCalc << 8);
                int[] key = WorldApp.getMapXTEA().getKey(region);
                if (forceRegion
                        && (yCalc == 49 || yCalc == 149 || yCalc == 147
                        || xCalc == 50 || xCalc == 49 && yCalc == 47)) {
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
        return this; // To change body of implemented methods use File |
        // Settings | File Templates.
    }

    public Actions sendMessage(String message, String messageExtension, int req) {
        return this; // To change body of implemented methods use File |
        // Settings | File Templates.
    }

    public Actions sendResizableScreen() {
        sendWindow(746);
        sendTab(170, 748); // HP bar
        sendTab(171, 749); // Prayer bar
        sendTab(172, 750); // Energy bar
        sendTab(173, 747); // Summoning bar
        sendTab(11, 745);
        sendTab(15, 751);
        sendTab(18, 752);
        sendTab(19, 754);
        sendTab(34, 884);
        sendTab(35, 320);
        sendTab(36, 190);
        sendTab(37, 259);
        sendTab(38, 149);
        sendTab(39, 387);
        sendTab(40, 271);
        sendTab(41, 192);
        sendTab(42, 891);
        sendTab(43, 550);
        sendTab(44, 551);
        sendTab(45, 589);
        sendTab(46, 261);
        sendTab(47, 464);
        sendTab(48, 187);
        sendTab(49, 34);
        sendTab(52, 182);
        sendInterface(137, 752, 9, true);
        return this;
    }

    public Actions sendSkill(int skillId) {
        return this; // To change body of implemented methods use File |
        // Settings | File Templates.
    }

    public Actions sendTab(int location, int id) {
        sendInterface(id, player.getDisplayMode() == DisplayMode.FIXED ? 548 : 746, location, true);
        return this;
    }

    public Actions sendWindow(int window) {
        PacketBuilder smf = new PacketBuilder(103);
        smf.putLEShortA(0).putByteS(0).putShort(window);
        player.getConnection().write(smf.toPacket());
        return this;
    }

    public Actions sendWorldList() {
        if (WorldListUpdater.getInstance().getCachedWorldList() != null) {
            PacketBuilder pb = new PacketBuilder(110, Packet.Type.VAR_SHORT);
            pb.put(WorldListUpdater.getInstance().getCachedWorldList());
            player.getConnection().write(pb.toPacket());
        }
        return this;
    }

    public Actions switchToFixedScreen() {
        sendFixedScreen();
        return this;
    }

    public Actions switchToResizableScreen() {
        sendResizableScreen();
        return this;
    }

    public Actions switchWindows(int windowFrom, int windowPosFrom,
                                 int windowTo, int windowPosTo) {
        return this;
    }
}
