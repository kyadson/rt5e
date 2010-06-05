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
package org.lazaro.rt5e.logic.item;

import org.lazaro.rt5e.Context;
import org.lazaro.rt5e.logic.player.Player;

/**
 * @author Lazaro
 */
public class AppearanceListener implements ItemCollectionListener {
    public void event(Player player, ItemCollection itemList, EventType type) {
        if (type == EventType.REFRESH) {
            player.setCachedAppearanceBlock(Context.getWorld().getPlayerUpdater().doApperanceBlock(player));
            player.getMasks().setAppearance(true);
        }
    }

    public void refresh(Player player, ItemCollection itemList) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void refresh(Player player, ItemCollection itemList, int index) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
