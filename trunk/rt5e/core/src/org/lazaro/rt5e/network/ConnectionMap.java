package org.lazaro.rt5e.network;

import org.jboss.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lazaro
 */
public class ConnectionMap {
    private static Map<Channel, Connection> connectionMap = new HashMap<Channel, Connection>();

    public static void register(Connection connection) {
        connectionMap.put(connection.getChannel(), connection);
    }

    public static void release(Channel channel) {
        connectionMap.remove(channel);
    }

    public static void release(Connection connection) {
        connectionMap.remove(connection.getChannel());
    }

    public static Connection forChannel(Channel channel) {
        return connectionMap.get(channel);
    }
}
