package org.lazaro.rt5e.network.world;

import org.jboss.netty.channel.*;
import org.lazaro.rt5e.network.Connection;
import org.lazaro.rt5e.network.ConnectionMap;

/**
 * @author Lazaro
 */
@ChannelPipelineCoverage("all")
public class ConnectionHandler extends SimpleChannelHandler {

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        Channel ch = ctx.getChannel();

        ConnectionMap.register(new Connection(ch));

        System.out.println("Channel connected <" + ch.getRemoteAddress().toString()
                + ">");
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx,
                                    ChannelStateEvent e) {
        Channel ch = ctx.getChannel();

        ConnectionMap.release(ch);

        System.out.println("Channel disconnected <" + ch.getRemoteAddress().toString()
                + ">");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        e.getCause().printStackTrace();

        Channel ch = e.getChannel();
        ch.close();
    }
}