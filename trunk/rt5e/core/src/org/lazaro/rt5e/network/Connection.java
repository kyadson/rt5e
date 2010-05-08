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
import org.jboss.netty.channel.ChannelFuture;
import org.lazaro.rt5e.utility.crypto.ISAACCipher;

/**
 * @author Lazaro
 */
public class Connection {
    private Channel channel = null;

    private ISAACCipher incommingISAACCipher = null;

    public Connection(Channel channel) {
        this.channel = channel;
    }

    public ISAACCipher getIncommingISAACCipher() {
        return incommingISAACCipher;
    }

    public void setIncommingISAACCipher(ISAACCipher incommingISAACCipher) {
        this.incommingISAACCipher = incommingISAACCipher;
    }

    public ISAACCipher getOutgoingISAACCipher() {
        return outgoingISAACCipher;
    }

    public void setOutgoingISAACCipher(ISAACCipher outgoingISAACCipher) {
        this.outgoingISAACCipher = outgoingISAACCipher;
    }

    private ISAACCipher outgoingISAACCipher = null;

    public Object getAttachment() {
        return attachment;
    }

    public void setAttachment(Object attachment) {
        this.attachment = attachment;
    }

    private Object attachment = null;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public ChannelFuture close() {
        return channel.close();
    }

    public boolean isConnected() {
        return channel.isConnected();
    }

    public boolean isOpen() {
        return channel.isOpen();
    }


    public ChannelFuture write(Packet packet) {
        return channel.write(packet);
    }
}
