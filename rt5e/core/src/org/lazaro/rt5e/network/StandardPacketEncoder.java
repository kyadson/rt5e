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