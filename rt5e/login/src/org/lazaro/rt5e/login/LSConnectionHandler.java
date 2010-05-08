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

        WorldSession world = (WorldSession) conn.getAttachment();

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