package org.lazaro.rt5e.network;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.lazaro.rt5e.network.encryption.ISAACCipher;

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
