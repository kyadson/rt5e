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
package org.lazaro.rt5e.logic.login;

import org.jboss.netty.channel.ChannelFutureListener;
import org.lazaro.rt5e.Context;
import org.lazaro.rt5e.logic.player.Player;
import org.lazaro.rt5e.login.LSException;
import org.lazaro.rt5e.network.PacketBuilder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Lazaro
 */
public class LoginWorker implements Runnable {
    private BlockingQueue<LoginRequest> loginQueue = new LinkedBlockingQueue<LoginRequest>();

    public void run() {
        while (Context.isRunning()) {
            try {
                LoginRequest login = loginQueue.take();
                Player player = new Player(login.getConnection());
                player.setName(login.getName());
                player.setPassword(login.getPassword());
                player.setLoginOpcode(login.getLoginOpcode());
                LoginResponse response;
                try {
                    response = Context.getWorld().getSession().loadPlayer(player);
                } catch (LSException e) {
                    response = LoginResponse.LOGIN_SERVER_OFFLINE;
                }
                if (response == LoginResponse.LOGIN) {
                    switch (login.getLoginOpcode()) {
                        case 16:
                        case 18:
                            player.getActions().sendLoginResponse(response.getResponseCode());
                            player.getActions().sendLogin();
                            Context.getWorld().register(player);
                            break;
                        case 19:
                            player.getActions().sendLobbyResponse(response.getResponseCode());
                            Context.getWorld().register(player);
                            break;
                    }
                } else {
                    System.out.println("Refused login [" + response + ", " + login + "]");
                    login.getConnection().write(new PacketBuilder().putByte(response.getResponseCode()).toPacket()).addListener(ChannelFutureListener.CLOSE);
                }
            } catch (InterruptedException e) {
            }
        }
    }

    public void dispatchLogin(LoginRequest login) {
        loginQueue.offer(login);
    }
}
