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

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.lazaro.rt5e.engine.Engine;
import org.lazaro.rt5e.io.MapXTEA;
import org.lazaro.rt5e.io.cache.Cache;
import org.lazaro.rt5e.logic.World;
import org.lazaro.rt5e.logic.login.LoginWorker;
import org.lazaro.rt5e.network.StandardPacketEncoder;
import org.lazaro.rt5e.network.protocol.HandshakeDecoder;
import org.lazaro.rt5e.network.protocol.world.ConnectionHandler;
import org.lazaro.rt5e.network.protocol.world.PacketHandlerWorker;
import org.lazaro.rt5e.utility.*;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Lazaro
 */
public class WorldApp {
    public static boolean isActive() {
        return active;
    }

    private static boolean active = false;

    private static ExecutorService bossExecutor = Executors.newCachedThreadPool();
    private static ExecutorService workerExecutor = Executors.newCachedThreadPool();

    public static MapXTEA getMapXTEA() {
        return mapXTEA;
    }

    private static MapXTEA mapXTEA = null;

    private static void startupNetworking() throws Throwable {
        ChannelFactory factory = new NioServerSocketChannelFactory(
                bossExecutor, workerExecutor);
        ConnectionHandler handler = new ConnectionHandler();

        ServerBootstrap bootstrap = new ServerBootstrap(factory);
        ChannelPipeline pipeline = bootstrap.getPipeline();
        pipeline.addLast("monitor", BandwidthMonitor.getInstance());
        pipeline.addLast("handler", handler);
        pipeline.addLast("encoder", new StandardPacketEncoder());
        pipeline.addLast("decoder", new HandshakeDecoder());

        bootstrap.setOption("child.tcpNoDelay", false);
        bootstrap.setOption("child.keepAlive", true);

        bootstrap.bind(new InetSocketAddress(Context.getConfiguration().getInt("WORLD_SERVER_PORT_OFFSET") + Context.getConfiguration().getInt("WORLD_ID")));
    }

    public static void main(String[] args) {
        active = true;

        NativeConsole.setHeader("RT5E [World Server]");
        Logger.setupLogging();
        Logger.printInfo();

        System.out.println("Starting world server...");
        Logger.incrementIndentationTab();

        try {
            Context.setConfiguration(new Configuration(Constants.WORLD_SERVER_CONFIG));
            System.out.println("Loaded settings");

            Context.setCache(new Cache(Constants.CACHE_DIRECTORY));
            System.out.println("Loaded cache");

            mapXTEA = new MapXTEA();

            Context.setWorld(new World(Context.getConfiguration().getInt("WORLD_ID")));
            Context.getWorld().start();
            System.out.println("Loaded world");

            Context.setLoginWorker(new LoginWorker());
            new Thread(Context.getLoginWorker()).start();
            System.out.println("Loaded login worker(s)");

            PacketHandlerWorker.loadPacketHandlers();

            Engine.getInstance().getCoreExecutor().scheduleAtFixedRate(Context.getWorld(), 0, 600, TimeUnit.MILLISECONDS);
            Engine.getInstance().getCoreExecutor().scheduleAtFixedRate(new PacketHandlerWorker(), 300, 600, TimeUnit.MILLISECONDS);

            Engine.getInstance().submitMiscEvent(new Monitor());
            Engine.getInstance().start();
            System.out.println("Started engine");

            startupNetworking();
            System.out.println("Bound port : " + (Context.getConfiguration().getInt("WORLD_SERVER_PORT_OFFSET") + Context.getConfiguration().getInt("WORLD_ID")));

            ProcessPriority.setProcessPriority();
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(ExitCodes.UNKNOWN_ERROR);
        }

        Logger.resetIndentation();
        System.out.println("DONE!");
        System.out.println();
    }
}
