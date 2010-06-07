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
public class Constants {
    public static class Equipment {
        public static final int EQUIPMENT_SIZE = 14;

        public static final int AMULET_SLOT = 2;
        public static final int ARROWS_SLOT = 13;
        public static final int BOOTS_SLOT = 10;
        public static final int BOTTOMS_SLOT = 7;
        public static final int CAPE_SLOT = 1;
        public static final int CHEST_SLOT = 4;
        public static final int GLOVES_SLOT = 9;
        public static final int HELM_SLOT = 0;
        public static final int RING_SLOT = 12;
        public static final int SHIELD_SLOT = 5;
        public static final int WEAPON_SLOT = 3;
    }

    /**
     * The RS2 cache that the server uses.
     */
    public static final String CACHE_DIRECTORY = "./data/cache/";

    /**
     * The client's revision/version.
     */
    public static final int CLIENT_REVISION = 597;

    public static final String GAME_NAME = "RuneScape";

    /**
     * The JS5 protocol opcode.
     */
    public static final int JS5_PROTOCOL_OPCODE = 15;

    public static final String LOBBY_SERVER_CONFIG = "./data/lobby_server.ini";

    /**
     * Configuration file location for the login server.
     */
    public static final String LOGIN_SERVER_CONFIG = "./data/login_server.ini";

    public static final String MAP_XTEA_DIR = "./data/mapxtea/";
    public static final String MAP_XTEA_FILE = "./data/mapxtea.dat";

    public static final int[] MESSAGE_LENGTHS = new int[256];

    public static final String PACKET_HANDLER_CONFIG = "./data/packet_handlers.ini";
    public static final String PACKET_HANDLER_PACKAGE = "org.lazaro.rt5e.network.protocol.world.handler";

    /**
     * The maximum amount of players allowed online at any given time on one
     * world..
     */
    public static final int PLAYER_CAP = 2047;

    /**
     * The server's internal user name used for security purposes.
     */
    public static final String ROOT_USER_NAME = "root";

    /**
     * The RS2 protocol opcode.
     */
    public static final int RS2_PROTOCOL_OPCODE = 14;

    /**
     * How often the world updater runs in milliseconds.
     * <p/>
     * Suggested rate is 600ms.
     */
    public static final int UPDATE_INTERVAL = 600;

    /**
     * Configuration file location for the world server.
     */
    public static final String WORLD_SERVER_CONFIG = "./data/world_server.ini";

    static {
        for (int i = 0; i < 256; i++) {
            MESSAGE_LENGTHS[i] = -3;
        }
        MESSAGE_LENGTHS[43] = 0; // world list req

        MESSAGE_LENGTHS[66] = 6; // screen set
        MESSAGE_LENGTHS[34] = 0; // rendered map
        MESSAGE_LENGTHS[4] = -1; // ??
        MESSAGE_LENGTHS[5] = 4; // camera values?
        MESSAGE_LENGTHS[8] = 2; // interface related value?
        MESSAGE_LENGTHS[22] = 2; // world ping?
        MESSAGE_LENGTHS[26] = 4; // camera moved
        MESSAGE_LENGTHS[57] = 1; // window focus
        MESSAGE_LENGTHS[37] = 6; // mouse clicked
        MESSAGE_LENGTHS[26] = 4; // player typed
        MESSAGE_LENGTHS[38] = 5; // normal walk
        MESSAGE_LENGTHS[58] = 18; // minimap walk
    }

    /**
     * If you are required to register an account before you login
     * or create an account automatically like most servers.
     */
    public static final boolean REGISTER_ACCOUNTS = false;

    public static final int MAX_LOCAL_PLAYERS = 2046;

    /**
     * How often packets should be handled.
     */
    public static final int PACKET_HANDLING_INTERVAL = 600;

    /**
     * The amount of SQL connections that should be pooled on startup.
     */
    public static final int SQL_CONNECTION_POOL_COUNT = 5;

    /**
     * XML file with a list of object aliases to simplify XML input/output and serialization.
     */
    public static final String XSTREAM_ALIASES = "./data/aliases.xml";

    public static final String ITEM_DEFINITIONS = "./data/item_definitions.xml";
}
