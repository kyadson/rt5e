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
package org.lazaro.rt5e.network.protocol;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.lazaro.rt5e.Constants;
import org.lazaro.rt5e.network.Connection;
import org.lazaro.rt5e.network.ConnectionMap;
import org.lazaro.rt5e.network.PacketBuilder;
import org.lazaro.rt5e.network.protocol.world.LoginDecoder597;

/**
 * @author Lazaro
 */
public class HandshakeDecoder extends FrameDecoder {
    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel,
                            ChannelBuffer buffer) throws Exception {
        Connection conn = ConnectionMap.forChannel(channel);
        if (buffer.readableBytes() >= 1) {
            buffer.markReaderIndex();

            int opcode = buffer.readByte() & 0xff;
            switch (opcode) {
                case Constants.RS2_PROTOCOL_OPCODE:
                    if (buffer.readableBytes() >= 1) {
                        int nameHash = buffer.readByte() & 0xff;

                        conn
                                .setServerSessionKey(((long) (Math.random() * 99999999D) << 32)
                                        + (long) (Math.random() * 99999999D));

                        channel.write(new PacketBuilder().putByte(0).putLong(
                                conn.getServerSessionKey()).toPacket());
                        channel.getPipeline().replace("decoder", "decoder",
                                new LoginDecoder597());
                    } else {
                        buffer.resetReaderIndex();
                        return null;
                    }
                    break;
                case Constants.JS5_PROTOCOL_OPCODE:
                    if (buffer.readableBytes() >= 4) {
                        int revision = buffer.readInt();
                        if (revision != Constants.CLIENT_REVISION) {
                            channel
                                    .write(
                                            new PacketBuilder().putByte(6)
                                                    .toPacket()).addListener(
                                    ChannelFutureListener.CLOSE);
                        } else {
                            channel
                                    .write(new PacketBuilder().putByte(0)
                                            .toPacket());
                            channel.getPipeline().replace("decoder", "decoder",
                                    new JS5Decoder());
                        }
                    } else {
                        buffer.resetReaderIndex();
                        return null;
                    }
                    break;
            }
        }
        return null;
    }
}
