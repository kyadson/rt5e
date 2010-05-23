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

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.lazaro.rt5e.logic.player.Player;
import org.lazaro.rt5e.network.Connection;
import org.lazaro.rt5e.network.ConnectionMap;
import org.lazaro.rt5e.network.Packet;

/**
 * @author Lazaro
 */
@ChannelPipelineCoverage("all")
public class PacketDispatcher extends SimpleChannelHandler {
    private static final PacketDispatcher instance = new PacketDispatcher();

    public static PacketDispatcher getInstance() {
        return instance;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        if (e.getMessage() instanceof Packet) {
            Connection conn = ConnectionMap.forChannel(ctx.getChannel());
            Packet packet = (Packet) e.getMessage();

            if (conn.getAttachment() instanceof Player) {
                Player player = (Player) conn.getAttachment();
                if (!player.isPacketReceived(packet.getOpcode())) {
                    player.getPacketQueue().offer(packet);
                    player.setPacketReceived(packet.getOpcode(), true);
                }
            }
        }
    }
}
