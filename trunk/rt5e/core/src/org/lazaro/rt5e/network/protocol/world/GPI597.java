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
package org.lazaro.rt5e.network.protocol.world;

import org.lazaro.rt5e.Constants;
import org.lazaro.rt5e.logic.Entity;
import org.lazaro.rt5e.logic.item.Item;
import org.lazaro.rt5e.logic.mask.Mask;
import org.lazaro.rt5e.logic.player.Appearance;
import org.lazaro.rt5e.logic.player.Player;
import org.lazaro.rt5e.logic.utility.PlayerUpdater;
import org.lazaro.rt5e.network.Packet;
import org.lazaro.rt5e.network.PacketBuilder;

/**
 * @author Lazaro
 */
public class GPI597 implements PlayerUpdater {
    public Packet doApperanceBlock(Player player) {
        PacketBuilder appearanceBlock = new PacketBuilder();

        Appearance app = player.getAppearance();
        appearanceBlock.put((byte) 0); // TODO hash byte for a bunch of different values
        appearanceBlock.putByte(0).putByte(0).putByte(0);
        if (!app.isNPC()) {
            for (int i = 0; i < 4; i++) {
                Item item = player.getEquipment().get(i);
                if (item != null) {
                    appearanceBlock.putShort(32768 + item.getDefinition().getXlateId());
                } else {
                    appearanceBlock.putByte(0);
                }
            }
            Item chestItem = player.getEquipment().get(Constants.Equipment.CHEST_SLOT);
            if (chestItem != null) {
                appearanceBlock.putShort(32768 + chestItem.getDefinition().getXlateId());
            } else {
                appearanceBlock.putShort(0x100 + app.getLook(2));
            }
            Item shieldItem = player.getEquipment().get(Constants.Equipment.SHIELD_SLOT);
            if (shieldItem != null) {
                appearanceBlock.putShort(32768 + shieldItem.getDefinition().getXlateId());
            } else {
                appearanceBlock.putByte(0);
            }
            if (chestItem != null && chestItem.getDefinition().isFullBody()) {
                appearanceBlock.putByte(0);
            } else {
                appearanceBlock.putShort(0x100 + app.getLook(3));
            }
            Item bottomItem = player.getEquipment().get(Constants.Equipment.BOTTOMS_SLOT);
            if (bottomItem != null) {
                appearanceBlock.putShort(32768 + bottomItem.getDefinition().getXlateId());
            } else {
                appearanceBlock.putShort(0x100 + app.getLook(5));
            }
            Item helmItem = player.getEquipment().get(Constants.Equipment.HELM_SLOT);
            if (helmItem != null
                    && (helmItem.getDefinition().isHat() || helmItem
                    .getDefinition().isMask())) {
                appearanceBlock.putByte(0);
            } else {
                appearanceBlock.putShort(0x100 + app.getLook(0));
            }
            Item glovesItem = player.getEquipment().get(Constants.Equipment.GLOVES_SLOT);
            if (glovesItem != null) {
                appearanceBlock.putShort(32768 + glovesItem.getDefinition().getXlateId());
            } else {
                appearanceBlock.putShort(0x100 + app.getLook(4));
            }
            Item bootsItem = player.getEquipment().get(Constants.Equipment.BOOTS_SLOT);
            if (bootsItem != null) {
                appearanceBlock.putShort(32768 + bootsItem.getDefinition().getXlateId());
            } else {
                appearanceBlock.putShort(0x100 + app.getLook(6));
            }
            if (helmItem != null && helmItem.getDefinition().isMask()) {
                appearanceBlock.putByte(0);
            } else {
                appearanceBlock.putShort(0x100 + app.getLook(1));
            }
        } else {
            appearanceBlock.putShort(-1);
            appearanceBlock.putShort(app.getNPCType());
            appearanceBlock.putByte(0);
        }
        for (int color : app.getColor()) {
            appearanceBlock.put((byte) color);
        }
        appearanceBlock.putShort(1426);
        appearanceBlock.putString(player.getName());

        appearanceBlock.put((byte) 3); // combat level

        appearanceBlock.putShort(0);
        appearanceBlock.putByte(0);

        return appearanceBlock.toPacket();
    }

    private void doAppearance(Player player, PacketBuilder pb) {
        Packet appearanceBlock = player.getCachedAppearanceBlock();

        pb.putC((byte) (appearanceBlock.getLength() & 0xFF));
        byte[] payload = appearanceBlock.getBytes();
        for (int i = payload.length - 1; i >= 0; i--) {
            pb.put((byte) (-128 + payload[i]));
        }
    }

    private void masks(Player player, PacketBuilder pb, boolean forceAppearance, boolean noChat) {
        if (!forceAppearance && !noChat) {
            pb.put(player.getCachedMaskBlock().getBytes());
        } else {
            pb.put(doMaskBlock(player, forceAppearance, noChat).getBytes());
        }
    }

    /**
     * For updating a player.
     *
     * @param owner  The player we are updating.
     * @param player A local player.
     * @param pb     The message factory to write with.
     * @return If the NSN0 block was written.
     */
    private boolean nsn0(Player owner, Player player, PacketBuilder pb, int skip) {
        if (player.isTeleporting()) {
            writeSkip(skip, pb);
            updateLocalPlayer(owner, player, 3, false, pb);
        } else if (player.getDirection().getDirection() != -1 && !player.getDirection().isFirstDirection()) {
            writeSkip(skip, pb);
            if (player.isRunning() || player.getWaypointQueue().isRunning()) {
                updateLocalPlayer(owner, player, 2, false, pb);
            } else {
                updateLocalPlayer(owner, player, 1, false, pb);
            }
        } else {
            if (player.getMasks().requiresUpdate()) {
                writeSkip(skip, pb);
                updateLocalPlayer(owner, player, 0, false, pb);
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * For updating movement directions after the first step.
     *
     * @param owner  The player we are updating.
     * @param player A local player.
     * @param pb     The message factory to write with.
     * @return If the NSN1 block was written.
     */
    private boolean nsn1(Player owner, Player player, PacketBuilder pb, int skip) {
        if (owner.isTeleporting() || player.isTeleporting()) {
            return false;
        }
        if (player.getDirection().getDirection() != -1 && player.getDirection().isFirstDirection()) {
            writeSkip(skip, pb);
            if (player.isRunning() || player.getWaypointQueue().isRunning()) {
                updateLocalPlayer(owner, player, 2, false, pb);
            } else {
                updateLocalPlayer(owner, player, 1, false, pb);
            }
            return true;
        }
        return false;
    }

    public Packet doMaskBlock(Entity entity) {
        return doMaskBlock((Player) entity, false, false);
    }

    private Packet doMaskBlock(Player player, boolean forceAppearance, boolean noChat) {
        PacketBuilder pb = new PacketBuilder();

        int mask = 0;
        if (player.getMasks().requiresUpdate(Mask.MaskType.APPEARANCE) || forceAppearance) {
            mask |= 0x4;
        }

        if (mask >= 0x100) {
            mask |= 0x1;
            if (mask >= 0x10000) {
                mask |= 0x400;
                pb.putTriByte(mask);
            } else {
                pb.putShort(mask);
            }
        } else {
            pb.putByte(mask);
        }

        if (player.getMasks().requiresUpdate(Mask.MaskType.APPEARANCE) || forceAppearance) {
            doAppearance(player, pb);
        }

        return pb.toPacket();
    }

    public void update(Player player) {
        if (player.isMapRegionChanged()) {
            player.getActions().sendMapRegion();
        }
        PacketBuilder pb = new PacketBuilder(48, Packet.Type.VAR_SHORT);

        /**
         * NSN0 - (first step)?, teleport, etc.
         */
        pb.recalculateBitPosition();
        int skip = -1;
        //if((player.getGPIFlag(player.getIndex()) & 0x1) == 0) {
        if (!nsn0(player, player, pb, skip)) {
            //player.setGPIFlag(player.getIndex(), (byte) (player.getGPIFlag(0) | 2));
            skip++;
        } else {
            skip = -1;
        }
        //}
        if (skip > -1) {
            writeSkip(skip, pb);
        }

        /**
         * NSN1 - walking/running (steps other than first)?
         */
        pb.recalculateBitPosition();
        skip = -1;
        //if((player.getGPIFlag(player.getIndex()) & 0x1) != 0) {
        // if(!nsn1(player, player, pb, skip)) {
        //player.setGPIFlag(0, (byte) (player.getGPIFlag(player.getIndex()) | 2));
        skip++;
        //} else {
        //    skip = -1;
        //}
        //}
        //if(skip > -1) {
        //    writeSkip(skip, pb);
        //}

        /**
         * NSN2 - (appending new players)?
         */
        pb.recalculateBitPosition();
        /*boolean[] newPlayers = new boolean[2047];
        skip = -1;
        for (int index=1; index < 2047; index++) {
            Player other = Context.getWorld().getGlobalPlayers().get(index);
            if(other != null && !player.equals(other) && other.isOnline() && !player.getLocalPlayers().contains(other) && !other.isTeleporting()) {
                if(skip > -1) {
                    writeSkip(skip, pb);
                    skip = -1;
                }
                addLocalPlayer(player, other, pb);
                newPlayers[index] = true;
            } else {
                skip++;
            }
        }
        if(skip > -1) {
            writeSkip(skip - player.getLocalPlayers().size(), pb);
        }*/

        /**
         * NSN3 - (updating non-global players)?
         */
        //if((player.getGPIFlag(player.getIndex()) & 0x1) == 0) {
        pb.recalculateBitPosition();
        writeSkip(2045, pb);
        //}

        /**
         * Shift flags
         */
        for (int index = 1; index < 2048; index++) {
            player.setGPIFlag(index, (byte) (player.getGPIFlag(index) >> 1));
        }

        /**
         * Masks
         */
        pb.recalculateBitPosition();
        if (player.getMasks().requiresUpdate()) {
            masks(player, pb, false, false);
        }
        for (Player local : player.getLocalPlayers()) {
            //if(local.getMasks().requiresUpdate() || newPlayers[local.getIndex()]) {
            // masks(local, pb, newPlayers[local.getIndex()], false);
            // }
        }

        player.getConnection().write(pb.toPacket());
    }

    private void updateLocalPlayer(Player owner, Player player, int stage, boolean newPlayer, PacketBuilder pb) {
        pb.putBits(1, 1);
        pb.putBits(1, player.getMasks().requiresUpdate() ? 1 : 0);
        pb.putBits(2, stage);
        switch (stage) {
            case 0:
                pb.putBits(1, 0);
                break;
            case 1:
                pb.putBits(3, player.getDirection().getDirection());
                break;
            case 2:
                pb.putBits(4, player.getDirection().getDirection());
                break;
            case 3:
                if (newPlayer) {
                    int x = player.getLocation().getX() - owner.getLocation().getX();
                    int y = player.getLocation().getY() - owner.getLocation().getY();
                    if (x < 0) x += 32;
                    if (y < 0) y += 32;
                    pb.putBits(1, 0);
                    pb.putBits(12, player.getLocation().getZ() << 10 | x << 5 | y & 0x1f);
                } else {
                    pb.putBits(1, 1);
                    pb.putBits(30, player.getLocation().getX() << 14 | player.getLocation().getY() & 0x3fff | player.getLocation().getZ() << 28);
                }
                break;
        }
    }

    private void addLocalPlayer(Player owner, Player player, PacketBuilder pb) {
        owner.getLocalPlayers().add(player);
        updateGlobalPlayer(owner, player, 0, true, pb);
    }

    private void updateGlobalPlayer(Player owner, Player player, int stage, boolean addToRegion, PacketBuilder pb) {
        pb.putBits(1, 1);
        pb.putBits(2, stage);
        switch (stage) {
            case 0:
                int x = player.getLocation().getX() - owner.getLocation().getX();
                int y = player.getLocation().getY() - owner.getLocation().getY();
                if (x < 0) x += 32;
                if (y < 0) y += 32;
                pb.putBits(1, 0);
                pb.putBits(6, x);
                pb.putBits(6, y);
                pb.putBits(1, addToRegion ? 1 : 0);
                break;
            case 1:
                // player.getOutStream().writeBits(2, values[0]);
                break;
            case 2:
                // player.getOutStream().writeBits(5, values[0]);
                break;
            case 3:
                // player.getOutStream().writeBits(18, values[0]);
                break;
        }
    }

    /**
     * Writes an amount of loops to skip.
     *
     * @param skip The amount of loops to skip.
     * @param pb   The message factory to write with.
     */
    private void writeSkip(int skip, PacketBuilder pb) {
        if (skip != -1) {
            int type = 0;
            if (skip != 0) {
                if (skip < 32) {
                    type = 1;
                } else if (skip < 256) {
                    type = 2;
                } else if (skip < 2048) {
                    type = 3;
                } else {
                    throw new IllegalArgumentException("Skip count cannot be over 2047!");
                }
            }
            pb.putBits(1, 0);
            pb.putBits(2, type);
            switch (type) {
                case 1:
                    pb.putBits(5, skip);
                    break;
                case 2:
                    pb.putBits(8, skip);
                    break;
                case 3:
                    pb.putBits(11, skip);
                    break;
            }
        }
    }
}
