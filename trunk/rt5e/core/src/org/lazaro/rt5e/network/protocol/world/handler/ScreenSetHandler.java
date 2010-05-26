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
package org.lazaro.rt5e.network.protocol.world.handler;

import org.lazaro.rt5e.logic.player.Player;
import org.lazaro.rt5e.network.Packet;
import org.lazaro.rt5e.network.protocol.world.Actions;
import org.lazaro.rt5e.network.protocol.world.PacketHandler;

/**
 * @author Lazaro
 */
public class ScreenSetHandler implements PacketHandler {
    public void handle(Player player, Packet packet) {
        int displayMode = packet.getUnsigned();
        int width = packet.getShort();
        int height = packet.getShort();

        player.setDisplayMode(Actions.DisplayMode.forValue(displayMode));

        if (player.isOnLogin()) {
            switch (player.getDisplayMode()) {
                case FIXED:
                    player.getActions().sendFixedScreen();
                    break;
                case RESIZABLE:
                case FULL_SCREEN:
                    player.getActions().sendResizableScreen();
                    break;
            }
            player.getActions().sendAccessMask(
                    65535,
                    65535,
                    player.getDisplayMode() == Actions.DisplayMode.FIXED ? 548
                            : 746,
                    player.getDisplayMode() == Actions.DisplayMode.FIXED ? 81
                            : 120, 0, 2).sendAccessMask(65535, 65535, 884, 11,
                    0, 2).sendAccessMask(65535, 65535, 884, 12, 0, 2)
                    .sendAccessMask(65535, 65535, 884, 13, 0, 2)
                    .sendAccessMask(65535, 65535, 884, 14, 0, 2);
            player.setOnLogin(false);
        } else {
            switch (player.getDisplayMode()) {
                case FIXED:
                    player.getActions().switchToFixedScreen();
                    break;
                case RESIZABLE:
                case FULL_SCREEN:
                    player.getActions().switchToResizableScreen();
                    break;
            }
        }
    }
}
