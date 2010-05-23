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

import org.lazaro.rt5e.Constants;
import org.lazaro.rt5e.Context;
import org.lazaro.rt5e.logic.player.Player;
import org.lazaro.rt5e.network.Packet;
import org.lazaro.rt5e.utility.Configuration;

import java.io.IOException;

/**
 * @author Lazaro
 */
public class PacketHandlerWorker implements Runnable {
    public static void loadPacketHandlers() {
        try {
            int count = 0;

            Configuration config = new Configuration(Constants.PACKET_HANDLER_CONFIG);
            String[] handlerNames = config.getStringArray("packet_handler");
            for (int i = 0; i < handlerNames.length; i++) {
                if (handlerNames[i] != null) {
                    try {
                        Class<?> handlerClass = Class.forName(handlerNames[i]);
                        PacketHandler handler = (PacketHandler) handlerClass.newInstance();
                        PacketHandler.PACKET_HANDLER[i] = handler;
                        count++;
                    } catch (ClassNotFoundException e) {
                        System.err.println("Could not find packet handler [opcode=" + i + ", name=" + handlerNames[i] + "]");
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        System.err.println("Error loading packet handler [opcode=" + i + ", name=" + handlerNames[i] + "]");
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        System.err.println("Error loading message handler [opcode=" + i + ", name=" + handlerNames[i] + "]");
                        e.printStackTrace();
                    }
                }
            }

            System.out.println("Loaded " + count + " packet handlers");
        } catch (IOException e) {
            System.err.println("Error loading packet handlers!");
            e.printStackTrace();
        }
    }

    public void run() {
        for (Player player : Context.getWorld().getGlobalPlayers()) {
            if (player.getConnection().isConnected() && player.getConnection().isOpen()) {
                for (Packet packet = player.getPacketQueue().poll(); packet != null; packet = player.getPacketQueue().poll()) {
                    PacketHandler handler = PacketHandler.PACKET_HANDLER[packet.getOpcode()];
                    if (handler != null) {
                        handler.handle(player, packet);
                    } else {
                        System.err.println("Unhandled packet [player=" + player.getName() + ", opcode=" + packet.getOpcode() + "]");
                    }
                    player.setPacketReceived(packet.getOpcode(), false);
                }
            }
        }
    }
}
