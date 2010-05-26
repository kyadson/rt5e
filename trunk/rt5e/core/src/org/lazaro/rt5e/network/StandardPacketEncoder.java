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
import org.jboss.netty.channel.*;

/**
 * @author Lazaro
 */
@ChannelPipelineCoverage("all")
public class StandardPacketEncoder extends SimpleChannelHandler {
    @Override
    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) {
        if (e.getMessage() instanceof Packet) {
            Connection conn = ConnectionMap.forChannel(ctx.getChannel());
            Packet packet = (Packet) e.getMessage();

            if (!packet.isRaw()) {
                ChannelBuffer buffer = ChannelBuffers.buffer(3);

                int opcode = packet.getOpcode();
                if (conn.getOutgoingISAACCipher() != null) {
                    opcode = (opcode + conn.getOutgoingISAACCipher().nextInt()) & 0xff;
                }
                buffer.writeByte((byte) opcode);
                if (packet.getType() == Packet.Type.VAR_BYTE) {
                    buffer.writeByte((byte) packet.getLength());
                } else if (packet.getType() == Packet.Type.VAR_SHORT) {
                    buffer.writeShort((short) packet.getLength());
                }
                Channels.write(ctx, Channels.future(ctx.getChannel()), buffer);
            }

            Channels.write(ctx, e.getFuture(), packet.getBuffer());
        }
    }
}