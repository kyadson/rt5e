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

import org.jboss.netty.buffer.ChannelBuffers;
import org.lazaro.rt5e.Constants;
import org.lazaro.rt5e.Context;
import org.lazaro.rt5e.WorldApp;
import org.lazaro.rt5e.logic.login.LoginResponse;
import org.lazaro.rt5e.logic.map.Tile;
import org.lazaro.rt5e.logic.player.Player;
import org.lazaro.rt5e.logic.player.PlayerDefinition;
import org.lazaro.rt5e.network.Packet;
import org.lazaro.rt5e.network.PacketBuilder;
import org.lazaro.rt5e.utility.BufferUtilities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lazaro
 */
public class WorldConnector implements Runnable {
    private Object lock = new Object();
    private DataInputStream input = null;
    private DataOutputStream output = null;
    private Socket socket = null;
    private Thread thread;
    private Map<String, Packet> packetQueue = new HashMap<String, Packet>();

    public WorldConnector() throws IOException {
        connect();

        thread = new Thread(this);
        thread.start();
    }

    private void connect() throws IOException {
        socket = new Socket(Context.getConfiguration().getString(
                "LOGIN_SERVER_ADDR"), Context.getConfiguration()
                .getInt("LOGIN_SERVER_PORT"));
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
        authenticate();
    }

    private void authenticate() throws IOException {
        PacketBuilder pb = new PacketBuilder();
        pb.putString(Context.getConfiguration().getString(
                "LOGIN_SERVER_PASS"));
        pb.putByte(WorldApp.isActive() ? 0 : 1).putShort(WorldApp.getWorld().getId());
        Packet p = pb.toPacket();
        output.write(p.getLength());
        output.write(p.getBytes());
        output.flush();

        int resp = input.read() & 0xff;
        if (resp != 1) {
            throw new IOException("Invalid response code [" + resp + "] recieved!");
        }
    }

    public void run() {
        while (true) {
            try {
                int opcode = input.read() & 0xff;
                int size = input.read() & 0xff;
                String userName = BufferUtilities.getString(input);
                byte[] data = new byte[size];
                if (size > 0) {
                    input.read(data);
                }
                synchronized (packetQueue) {
                    packetQueue.put(userName, new Packet(opcode, size, null, ChannelBuffers.wrappedBuffer(data)));
                    packetQueue.notifyAll();
                }
            } catch (IOException e) {
                try {
                    output.close();
                    input.close();
                    socket.close();
                } catch (IOException e2) {
                } finally {
                    synchronized (packetQueue) {
                        packetQueue.notifyAll();
                    }
                }
                System.err.println("Login server disconnected! Attempting reconnection.");
                while (true) {
                    try {
                        connect();
                        Thread.sleep(5000);
                    } catch (IOException e2) {
                        continue;
                    } catch (InterruptedException e2) {
                        continue;
                    }
                    break;
                }
            }
        }
    }

    private Packet waitForPacket(String userName) throws LSException {
        synchronized (packetQueue) {
            while (true) {
                if (socket != null && socket.isConnected()
                        && !socket.isClosed()) {
                    try {
                        packetQueue.wait();
                    } catch (InterruptedException e) {
                        continue;
                    }
                    Packet packet = packetQueue.remove(userName);
                    if (packet != null) {
                        return packet;
                    }
                } else {
                    throw new LSException("Login server disconnected!");
                }
            }
        }
    }

    private void sendPacket(Packet packet) throws LSException {
        synchronized (lock) {
            try {
                if (!packet.isRaw()) {
                    output.write(packet.getOpcode());
                    output.write(packet.getLength());
                }
                output.write(packet.getBytes());
                output.flush();
            } catch (IOException e) {
                throw new LSException("Unable to send message to login server!", e);
            }
        }
    }

    public LoginResponse loadPlayer(Player player) throws LSException {
        PacketBuilder pb = new PacketBuilder(2);
        pb.putString(player.getName());
        pb.putByte(player.getLoginOpcode());
        pb.putString(player.getPassword());
        sendPacket(pb.toPacket());
        Packet packet = waitForPacket(player.getName());
        LoginResponse result = LoginResponse.valueFor(packet.getUnsigned());
        if (result == LoginResponse.LOGIN) {
            PlayerDefinition def = (PlayerDefinition) packet.getObject();
            player.setLocation(Tile.locate(def.getCoordX(), def.getCoordY(), def
                    .getCoordZ()));
        }
        return result;
    }


    public void savePlayer(Player player) throws LSException {
        PacketBuilder pb = new PacketBuilder(2);
        pb.putString(player.getName()).putByte(player.getLoginOpcode());
        PlayerDefinition def = new PlayerDefinition();
        def.setCoordX(player.getLocation().getX());
        def.setCoordY(player.getLocation().getY());
        def.setCoordZ(player.getLocation().getZ());
        pb.putObject(def);
        sendPacket(pb.toPacket());
    }

    public byte[] worldListData() throws LSException {
        PacketBuilder smf = new PacketBuilder(3);
        smf.putString(Constants.ROOT_USER_NAME);
        sendPacket(smf.toPacket());
        Packet message = waitForPacket(Constants.ROOT_USER_NAME);
        return message.getBytes();
    }
}
