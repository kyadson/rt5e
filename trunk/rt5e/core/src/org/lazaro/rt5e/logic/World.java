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
package org.lazaro.rt5e.logic;

import org.lazaro.rt5e.Constants;
import org.lazaro.rt5e.Context;
import org.lazaro.rt5e.engine.Engine;
import org.lazaro.rt5e.logic.item.ItemDefinition;
import org.lazaro.rt5e.logic.player.Player;
import org.lazaro.rt5e.logic.utility.NodeCollection;
import org.lazaro.rt5e.logic.utility.PlayerUpdater;
import org.lazaro.rt5e.login.LSException;
import org.lazaro.rt5e.login.WorldConnector;
import org.lazaro.rt5e.network.protocol.world.GPI597;
import org.lazaro.rt5e.utility.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author Lazaro
 */
public class World implements Runnable {
    private NodeCollection<Player> globalPlayers = new NodeCollection<Player>(2046);
    private final int id;
    private boolean running = true;
    private WorldConnector session;
    private List<ItemDefinition> itemDefinitions = new ArrayList<ItemDefinition>();
    private PlayerUpdater playerUpdater;

    public World(int id) {
        this.id = id;

        playerUpdater = new GPI597();
    }

    public NodeCollection<Player> getGlobalPlayers() {
        return globalPlayers;
    }

    public int getId() {
        return id;
    }

    public WorldConnector getSession() {
        return session;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean register(Player player) {
        boolean success = globalPlayers.add(player);
        if (success) {
            System.out.println("Registered player [" + player + "]");
        }
        return success;
    }

    public void remove(final Player player) {
        boolean success = globalPlayers.remove(player);
        if (success) {
            Engine.getInstance().executeBlocking(new Runnable() {
                public void run() {
                    try {
                        session.savePlayer(player);
                    } catch (LSException e) {
                        System.err.println("Error saving player [" + player + "]");
                        e.printStackTrace();
                    }
                }
            });
        }
        System.out.println("Removed player [" + player + "]");
    }

    public void run() {
        synchronized (globalPlayers) {
            final CountDownLatch processLatch = new CountDownLatch(globalPlayers.size());
            for (final Player player : globalPlayers) {
                if (player.isOnline() && !player.isOnLogin()) {
                    Engine.getInstance().executeParallel(
                            new Runnable() {
                                public void run() {
                                    try {
                                        player.process();
                                        player.setCachedMaskBlock(playerUpdater.doMaskBlock(player));
                                        processLatch.countDown();
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                } else {
                    processLatch.countDown();
                }
            }
            try {
                processLatch.await();
            } catch (InterruptedException e) {
                return;
            }
            final CountDownLatch updateLatch = new CountDownLatch(globalPlayers.size());
            for (final Player player : globalPlayers) {
                if (player.isOnline() && !player.isOnLogin()) {
                    Engine.getInstance().executeParallel(
                            new Runnable() {
                                public void run() {
                                    try {
                                        if (!player.getConnection().getChannel().isConnected()) {
                                            player.getConnection().destroy();
                                        } else {
                                            playerUpdater.update(player);
                                        }
                                        updateLatch.countDown();
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                } else {
                    updateLatch.countDown();
                }
            }
            try {
                updateLatch.await();
            } catch (InterruptedException e) {
                return;
            }
            final CountDownLatch resetLatch = new CountDownLatch(globalPlayers.size());
            for (final Player player : globalPlayers) {
                if (player.isOnline() && !player.isOnLogin()) {
                    Engine.getInstance().executeParallel(
                            new Runnable() {
                                public void run() {
                                    try {
                                        player.reset();
                                        resetLatch.countDown();
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                } else {
                    resetLatch.countDown();
                }
            }
            try {
                resetLatch.await();
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public List<ItemDefinition> getItemDefinitions() {
        return itemDefinitions;
    }

    private void loadItemDefinitions() throws Exception {
        Map<Integer, ItemDefinition> itemDefinitionMap = (Map<Integer, ItemDefinition>) Context.getXStreamSession().readObject(Constants.ITEM_DEFINITIONS + ".gz");
        if (itemDefinitionMap == null) {
            itemDefinitionMap = (Map<Integer, ItemDefinition>) Context.getXStreamSession().readObject(Constants.ITEM_DEFINITIONS);
            Context.getXStreamSession().writeObject(itemDefinitionMap, Constants.ITEM_DEFINITIONS + ".gz");
        }

        // Convert the map to an array list for a performance boost.
        int maxType = 0;
        for (int type : itemDefinitionMap.keySet()) {
            if (type > maxType) {
                maxType = type;
            }
        }
        for (int type = 0; type <= maxType; type++) {
            ItemDefinition def = itemDefinitionMap.get(type);
            if (def == null) {
                def = ItemDefinition.prepareDefinition(type);
            }
            itemDefinitions.add(type, def);
        }
        System.out.println("Loaded " + itemDefinitions.size() + " item definitions");
    }

    public void start() throws Exception {
        System.out.println("Loading world...");
        Logger.incrementIndentationTab();

        session = new WorldConnector();

        loadItemDefinitions();

        Logger.decrementIndentationTab();
        System.out.println("Loaded world");
    }

    public PlayerUpdater getPlayerUpdater() {
        return playerUpdater;
    }
}
