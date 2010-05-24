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
package org.lazaro.rt5e.login;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.lazaro.rt5e.Context;
import org.lazaro.rt5e.LoginApp;
import org.lazaro.rt5e.network.Connection;
import org.lazaro.rt5e.network.ConnectionMap;
import org.lazaro.rt5e.network.Packet;
import org.lazaro.rt5e.network.PacketBuilder;
import org.lazaro.rt5e.utility.StreamUtilities;

/**
 * @author Lazaro
 */
public class LSDecoder extends FrameDecoder {
    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        Connection conn = ConnectionMap.forChannel(channel);
        if (conn.getAttachment() != null) {
            WorldSession world = (WorldSession) conn.getAttachment();

            if (buffer.readableBytes() >= 2) {
                buffer.markReaderIndex();

                int opcode = buffer.readByte() & 0xff;
                int size = buffer.readByte() & 0xff;
                if (buffer.readableBytes() >= size) {
                    ChannelBuffer data = ChannelBuffers.buffer(size);
                    buffer.readBytes(data, 0, size);
                    data.setIndex(0, size);
                    return new Packet(opcode, size, null, data);
                } else {
                    buffer.resetReaderIndex();
                }
                return null;
            }
        } else {
            if (buffer.readable()) {
                buffer.markReaderIndex();

                int size = buffer.readByte() & 0xff;
                if (buffer.readableBytes() >= size) {
                    String password = StreamUtilities.getString(buffer);
                    if (!password.equals(Context.getConfiguration().getString("LOGIN_SERVER_PASS"))) {
                        conn.write(new PacketBuilder().putByte(2).toPacket());
                        channel.close();
                        return null;
                    }
                    WorldSession world = new WorldSession(conn);
                    conn.setAttachment(world);
                    world.setLobbyWorld((buffer.readByte() & 0xff) == 1);
                    world.setId(buffer.readShort());
                    if (world.isLobbyWorld() ? LoginApp.getLobbyWorlds().containsKey(world.getId()) : LoginApp.getGameWorlds().containsKey(world.getId())) {
                        conn.write(new PacketBuilder().putByte(3).toPacket());
                        channel.close();
                        return null;
                    }

                    if (world.isLobbyWorld())
                        LoginApp.getLobbyWorlds().put(world.getId(), world);
                    else
                        LoginApp.getGameWorlds().put(world.getId(), world);

                    System.out.println("Registered world [type=" + (world.isLobbyWorld() ? "lobby" : "normal") + ", id=" + world.getId() + "]");

                    conn.write(new PacketBuilder().putByte(1).toPacket());
                } else {
                    buffer.resetReaderIndex();
                }
                return null;
            }
        }
        return null;
    }
}
