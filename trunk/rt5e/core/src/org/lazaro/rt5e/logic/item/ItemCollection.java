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

import org.lazaro.rt5e.logic.player.Player;
import org.lazaro.rt5e.logic.utility.NodeCollection;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Lazaro
 */
public class ItemCollection extends NodeCollection<Item> {
    private static enum Stack {
        FORCE_STACK, STANDARD
    }

    private List<ItemCollectionListener> listeners;
    private Player player;
    private Stack stack;

    public ItemCollection(Player player, int size) {
        this(player, size, Stack.STANDARD);
    }

    public ItemCollection(Player player, int size, Stack stack) {
        super(0, size);
        this.player = player;
        this.stack = stack;
        listeners = new LinkedList<ItemCollectionListener>();
        super.setIndexOptimization(false);
    }

    @Override
    public boolean add(Item item) {
        return add(item, nextIndex());
    }

    @Override
    public boolean add(Item item, int index) {
        if (stack == Stack.STANDARD && !item.getDefinition().isStackable()) {
            if (item.getAmount() > 1) {
                if ((capacity() - size()) >= item.getAmount()) {
                    int amount = item.getAmount();
                    while (amount > 0) {
                        if (super.add(new Item(item.getType(), 1), nextIndex())) { // the
                            // index
                            // specified
                            // is
                            // voided.
                            refresh(item.getIndex());
                            amount--;
                        }
                    }
                    return true;
                }
            } else {
                if (super.add(item, index)) {
                    refresh(item.getIndex());
                    return true;
                }
            }
        } else {
            Item otherItem = getForType(item.getType());
            if (otherItem == null) {
                if (super.add(item, index)) {
                    refresh(item.getIndex());
                    return true;
                }
            } else {
                otherItem.setAmount(otherItem.getAmount() + item.getAmount());
                refresh(otherItem.getIndex());
                return true;
            }
        }
        event(ItemCollectionListener.EventType.FULL);
        return false;
    }

    /**
     * Adds an <code>ItemListListener</code> object onto a list of other
     * listeners to be called when the list is modified.
     * <p/>
     * Note: That the listeners do not have a specific order in which they are
     * called.
     *
     * @param listener The listener to add.
     */
    public void addListener(ItemCollectionListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    public void event(ItemCollectionListener.EventType type) {
        synchronized (listeners) {
            for (ItemCollectionListener listener : listeners) {
                listener.event(player, this, type);
            }
        }
    }

    public Item getForType(int type) {
        for (Item otherItem : this) {
            if (otherItem.getType() == type) {
                return otherItem;
            }
        }
        return null;
    }

    /**
     * Calls all the item listeners to refresh everything.
     */
    public void refresh() {
        synchronized (listeners) {
            for (ItemCollectionListener listener : listeners) {
                listener.refresh(player, this);
                listener.event(player, this, ItemCollectionListener.EventType.REFRESH);
            }
        }
    }

    /**
     * Calls all the item listeners to refresh a certain index.
     *
     * @param index The index to refresh.
     */
    private void refresh(int index) {
        synchronized (listeners) {
            for (ItemCollectionListener listener : listeners) {
                listener.refresh(player, this, index);
                listener.event(player, this, ItemCollectionListener.EventType.REFRESH);
            }
        }
    }

    public int remaining() {
        return capacity() - size();
    }

    @Override
    public Item remove(int index) {
        Item item = super.remove(index);
        refresh(index);
        return item;
    }

    @Override
    public boolean remove(Item item) {
        Item otherItem = getForType(item.getType());
        if (otherItem != null) {
            if (otherItem.getAmount() > item.getAmount()) {
                otherItem.setAmount(otherItem.getAmount() - item.getAmount());
                refresh(otherItem.getIndex());
                return true;
            } else {
                super.remove(otherItem.getIndex());
                refresh(otherItem.getIndex());
                return true;
            }
        }
        return false;
    }
}
