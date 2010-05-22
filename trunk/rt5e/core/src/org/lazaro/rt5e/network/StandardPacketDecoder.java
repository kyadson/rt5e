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
package org.lazaro.rt5e.network;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.lazaro.rt5e.Constants;

/**
 * @author Lazaro
 */
public class StandardPacketDecoder extends FrameDecoder {
    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel,
                            ChannelBuffer buffer) throws Exception {
        Connection conn = ConnectionMap.forChannel(channel);
        if (buffer.readableBytes() >= 1) {
            buffer.markReaderIndex();
            int opcode = buffer.readByte() & 0xff;
            if (conn.getIncommingISAACCipher() != null) {
                opcode = (opcode - conn.getIncommingISAACCipher().nextInt()) & 0xff;
            }
            int length = Constants.MESSAGE_LENGTHS[opcode];
            if (length == -1) {
                if (buffer.readableBytes() >= 1) {
                    length = buffer.readByte() & 0xff;
                } else {
                    buffer.resetReaderIndex();
                    return null;
                }
            }
            if (length < 0) {
                System.err.println("Invalid message length, guessing '" + buffer.readableBytes() + "' instead [opcode=" + opcode + ", length=" + length + "]");
                length = buffer.readableBytes();
            }
            if (buffer.readableBytes() >= length) {
                ChannelBuffer data = ChannelBuffers.buffer(length);
                buffer.readBytes(data, 0, length);
                data.setIndex(0, length);
                return new Packet(opcode, length, null, data);
            } else {
                buffer.resetReaderIndex();
                return null;
            }
        }
        return null;
    }
}
