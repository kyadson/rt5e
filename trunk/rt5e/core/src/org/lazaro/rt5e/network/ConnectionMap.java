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
package org.lazaro.rt5e.network;

import org.jboss.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lazaro
 */
public class ConnectionMap {
    private static Map<Channel, Connection> connectionMap = new HashMap<Channel, Connection>();

    public static Connection forChannel(Channel channel) {
        return connectionMap.get(channel);
    }

    public static void register(Connection connection) {
        connectionMap.put(connection.getChannel(), connection);
    }

    public static void release(Channel channel) {
        connectionMap.remove(channel);
    }

    public static void release(Connection connection) {
        connectionMap.remove(connection.getChannel());
    }
}
