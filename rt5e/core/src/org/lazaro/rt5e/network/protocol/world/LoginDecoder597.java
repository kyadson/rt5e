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
package org.lazaro.rt5e.network.protocol.world;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.lazaro.rt5e.Constants;
import org.lazaro.rt5e.Context;
import org.lazaro.rt5e.LobbyApp;
import org.lazaro.rt5e.WorldApp;
import org.lazaro.rt5e.logic.login.LoginRequest;
import org.lazaro.rt5e.logic.login.LoginResponse;
import org.lazaro.rt5e.network.Connection;
import org.lazaro.rt5e.network.ConnectionMap;
import org.lazaro.rt5e.network.PacketBuilder;
import org.lazaro.rt5e.network.StandardPacketDecoder;
import org.lazaro.rt5e.utility.BufferUtilities;
import org.lazaro.rt5e.utility.NameUtilities;

/**
 * @author Lazaro
 */
public class LoginDecoder597 extends FrameDecoder {
    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        Connection conn = ConnectionMap.forChannel(channel);
        if (buffer.readableBytes() >= 3) {
            buffer.markReaderIndex();

            int loginOpcode = buffer.readByte() & 0xff;
            int loginBlockLength = buffer.readShort() & 0xffff;

            LoginResponse response = LoginResponse.LOGIN;
            LoginRequest login = null;

            loginBlock:
            do {
                if (buffer.readableBytes() >= loginBlockLength) {
                    int clientRevision = buffer.readInt();
                    if (clientRevision != Constants.CLIENT_REVISION) {
                        response = LoginResponse.CLIENT_UPDATED;
                        break loginBlock;
                    }
                    int displayMode, rsaHeaderKey;
                    long clientSessionKey, serverSessionKey;
                    String name, password;
                    switch (loginOpcode) {
                        case 16:
                        case 18:
                            if (WorldApp.isActive()) {
                                buffer.readByte();
                                displayMode = buffer.readByte();
                                int width = buffer.readShort();
                                int height = buffer.readShort();
                                buffer.readByte();
                                for (int i = 0; i < 24; i++) {
                                    buffer.readByte(); // related to cache
                                }
                                BufferUtilities.getString(buffer); // settings
                                buffer.readInt();
                                int settingsBlockLength = buffer.readByte();
                                buffer.skipBytes(settingsBlockLength);
                                buffer.readShort();
                                buffer.readShort();
                                for (int i = 0; i < 30; i++) {
                                    buffer.readInt(); // cache crcs
                                }
                                buffer.readShort(); // RSA block length
                                rsaHeaderKey = buffer.readByte();
                                if (rsaHeaderKey != 10) {
                                    response = LoginResponse.ERROR;
                                    break loginBlock;
                                }

                                clientSessionKey = buffer.readLong();
                                serverSessionKey = buffer.readLong();
                                conn.setClientSessionKey(clientSessionKey);
                                conn.setServerSessionKey(serverSessionKey);

                                name = NameUtilities.longToString(buffer.readLong());
                                password = BufferUtilities.getString(buffer);

                                login = new LoginRequest(conn, name, password, loginOpcode);
                            } else {
                                response = LoginResponse.ERROR;
                                break loginBlock;
                            }
                            break;
                        case 19:
                            if (LobbyApp.isActive()) {
                                buffer.readByte();
                                displayMode = buffer.readByte();
                                for (int i = 0; i < 24; i++) {
                                    buffer.readByte(); // related to cache
                                }
                                BufferUtilities.getString(buffer); // settings
                                buffer.readInt();
                                for (int i = 0; i < 30; i++) {
                                    buffer.readInt(); // cache crcs
                                }

                                buffer.readShort(); // RSA block length
                                rsaHeaderKey = buffer.readByte();
                                if (rsaHeaderKey != 10) {
                                    response = LoginResponse.ERROR;
                                    break loginBlock;
                                }

                                clientSessionKey = buffer.readLong();
                                serverSessionKey = buffer.readLong();
                                conn.setClientSessionKey(clientSessionKey);
                                conn.setServerSessionKey(serverSessionKey);

                                name = NameUtilities.longToString(buffer.readLong());
                                password = BufferUtilities.getString(buffer);

                                login = new LoginRequest(conn, name, password, loginOpcode);
                            } else {
                                response = LoginResponse.ERROR;
                                break loginBlock;
                            }
                            break;
                        default:
                            response = LoginResponse.ERROR;
                            break loginBlock;
                    }
                } else {
                    buffer.resetReaderIndex();
                    return null;
                }
            } while (false);

            if (response == LoginResponse.LOGIN) {
                channel.getPipeline().replace("decoder", "decoder", new StandardPacketDecoder());
                channel.getPipeline().addLast("dispatcher", PacketDispatcher.getInstance());

                Context.getLoginWorker().dispatchLogin(login);
                System.out.println("Dispatched login [" + login + "]");
            } else {
                channel.write(new PacketBuilder().putByte(response.getResponseCode()).toPacket()).addListener(ChannelFutureListener.CLOSE);
            }
        }
        return null;
    }
}
