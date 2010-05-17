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
package org.lazaro.rt5e;

/**
 * @author Lazaro
 */
public interface Constants {
    /**
     * The maximum amount of players allowed online at any given time on one world..
     */
    public static final int PLAYER_CAP = 2047;

    /**
     * Configuration file location for the login server.
     */
    public static final String LOGIN_SERVER_CONFIG = "./data/login_server.ini";

    /**
     * Configuration file location for the world server.
     */
    public static final String WORLD_SERVER_CONFIG = "./data/world_server.ini";

    /**
     * How often the world updater runs in milliseconds.
     * <p/>
     * Suggested rate is 600ms.
     */
    public static final int UPDATE_INTERVAL = 600;

    /**
     * The server's internal user name used for security purposes.
     */
    public static final String ROOT_USER_NAME = "root";

    /**
     * The RS2 cache that the server uses.
     */
    public static final String CACHE_DIRECTORY = "./data/cache/";

    /**
     * The JS5 protocol opcode.
     */
    public static final int JS5_PROTOCOL_OPCODE = 15;

    /**
     * The client's revision/version.
     */
    public static final int CLIENT_REVISION = 525;
}
