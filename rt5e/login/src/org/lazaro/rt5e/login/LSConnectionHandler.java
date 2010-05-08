package org.lazaro.rt5e.login;

import org.jboss.netty.channel.*;
import org.lazaro.rt5e.LoginApp;
import org.lazaro.rt5e.network.Connection;
import org.lazaro.rt5e.network.ConnectionMap;

/**
 * @author Lazaro
 */
@ChannelPipelineCoverage("all")
public class LSConnectionHandler extends SimpleChannelHandler {

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        Channel ch = ctx.getChannel();

        ConnectionMap.register(new Connection(ch));
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx,
                                    ChannelStateEvent e) {
        Channel ch = ctx.getChannel();
        Connection conn = ConnectionMap.forChannel(ch);
        
        WorldSession world  = (WorldSession) conn.getAttachment();

        if (world.isLobbyWorld())
            LoginApp.getLobbyWorlds().remove(world.getId());
        else
            LoginApp.getGameWorlds().remove(world.getId());

        System.out.println("Un-registered world [type=" + (world.isLobbyWorld() ? "lobby" : "normal") + ", id=" + world.getId() + "]");

        ConnectionMap.release(ch);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        // e.getCause().printStackTrace();

        Channel ch = e.getChannel();
        ch.close();
    }
}