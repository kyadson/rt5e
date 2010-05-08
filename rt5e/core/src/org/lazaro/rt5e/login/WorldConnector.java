package org.lazaro.rt5e.login;

import org.lazaro.rt5e.Context;
import org.lazaro.rt5e.WorldApp;
import org.lazaro.rt5e.network.Packet;
import org.lazaro.rt5e.network.PacketBuilder;

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
    private Map<String, Runnable> futureQueue = new HashMap<String, Runnable>();

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
    }
}
