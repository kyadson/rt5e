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
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.lazaro.rt5e.Context;
import org.lazaro.rt5e.engine.Engine;
import org.lazaro.rt5e.io.cache.RS2File;
import org.lazaro.rt5e.network.Packet;
import org.lazaro.rt5e.network.PacketBuilder;

import java.nio.ByteBuffer;
import java.util.LinkedList;

/**
 * @author Lazaro
 */
public class JS5Decoder extends FrameDecoder {
    @Override
    protected Object decode(ChannelHandlerContext ctx, final Channel channel,
                            ChannelBuffer buffer) throws Exception {
        final LinkedList<int[]> requests = new LinkedList<int[]>();
        while (buffer.readableBytes() >= 4) {
            final int opcode = buffer.readByte() & 0xff;
            final int index = buffer.readByte() & 0xff;
            final int file = buffer.readShort() & 0xffff;
            switch (opcode) {
                case 0:
                    requests.add(new int[]{index, file});
                    break;
                case 1:
                    Engine.getInstance().executeBlocking(new Runnable() {
                        public void run() {
                            channel.write(prepareFilePacket(1, Context.getCache()
                                    .getFile(index, file)));
                        }
                    });
            }
        }
        if (!requests.isEmpty()) {
            Engine.getInstance().executeBlocking(new Runnable() {
                public void run() {
                    while (requests.size() > 0) {
                        int[] req = requests.removeFirst();
                        channel.write(prepareFilePacket(0, Context.getCache()
                                .getFile(req[0], req[1])));
                    }
                }
            });
        }
        return null;
    }

    private Packet prepareFilePacket(int opcode, RS2File file) {
        int compression = file.getCompression();
        int length = file.getLength();

        int attributes = compression;
        if (opcode == 0) {
            attributes |= 0x80;
        }

        PacketBuilder pb = new PacketBuilder();
        pb.putByte(file.getParentId()).putShort(file.getId()).putByte(
                attributes).putInt(length);

        ByteBuffer data = file.getData();
        int blockOffset = 8;
        for (int offset = 0; offset < data.limit(); offset++) {
            if (blockOffset == 512) {
                pb.putByte(255);
                blockOffset = 1;
            }
            pb.put(data.get());
            blockOffset++;
        }
        data.rewind();

        return pb.toPacket();
    }
}
