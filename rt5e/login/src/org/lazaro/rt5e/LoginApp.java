package org.lazaro.rt5e;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.lazaro.rt5e.io.SQLHandler;
import org.lazaro.rt5e.login.LSConnectionHandler;
import org.lazaro.rt5e.login.LSDecoder;
import org.lazaro.rt5e.login.LSMessageHandler;
import org.lazaro.rt5e.login.WorldSession;
import org.lazaro.rt5e.network.StandardPacketEncoder;
import org.lazaro.rt5e.utility.Configuration;
import org.lazaro.rt5e.utility.Logger;
import org.lazaro.rt5e.utility.NativeConsole;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Lazaro
 */
public class LoginApp {
    private static ExecutorService bossExecutor = Executors.newCachedThreadPool();
    private static ExecutorService workerExecutor = Executors.newCachedThreadPool();
    private static Map<Integer, WorldSession> gameWorlds = new HashMap<Integer, WorldSession>();
    private static Map<Integer, WorldSession> lobbyWorlds = new HashMap<Integer, WorldSession>();
    private static Map<String, WorldSession> players = new HashMap<String, WorldSession>();
    private static SQLHandler sqlHandler = null;

    public static Map<Integer, WorldSession> getGameWorlds() {
        return gameWorlds;
    }

    public static Map<Integer, WorldSession> getLobbyWorlds() {
        return lobbyWorlds;
    }

    public static Map<String, WorldSession> getPlayers() {
        return players;
    }

    public static SQLHandler getSQLHandler() {
        return sqlHandler;
    }

    private static void startupNetworking() throws Throwable {
        ChannelFactory factory = new NioServerSocketChannelFactory(
                bossExecutor, workerExecutor);
        LSConnectionHandler handler = new LSConnectionHandler();

        ServerBootstrap bootstrap = new ServerBootstrap(factory);
        ChannelPipeline pipeline = bootstrap.getPipeline();
        pipeline.addLast("handler", handler);
        pipeline.addLast("encoder", new StandardPacketEncoder());
        pipeline.addLast("decoder", new LSDecoder());
        pipeline.addLast("message_handler", new LSMessageHandler());

        bootstrap.setOption("child.tcpNoDelay", false);
        bootstrap.setOption("child.keepAlive", true);

        bootstrap.bind(new InetSocketAddress(Context.getConfiguration().getInt("LOGIN_SERVER_PORT")));
    }

    public static void main(String[] args) {
        try {
            NativeConsole.setHeader("RT5E [Login Server]");
            Logger.setupLogging();
            Logger.printInfo();

            System.out.println("Starting login server...");
            Logger.incrementIndentationTab();


            Context.setConfiguration(new Configuration(Constants.LOGIN_SERVER_CONFIG));
            System.out.println("Loaded settings");

            sqlHandler = new SQLHandler(Context.getConfiguration());
            System.out.println("Connected to SQL database");

            startupNetworking();
            System.out.println("Bound port : " + Context.getConfiguration().getInt("LOGIN_SERVER_PORT"));

            
            Logger.resetIndentation();
            System.out.println("DONE!");
        } catch (Throwable e) {
            System.err.println("Failed to start server");
            e.printStackTrace();
        }
    }
}
