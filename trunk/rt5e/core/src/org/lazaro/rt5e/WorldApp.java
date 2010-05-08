package org.lazaro.rt5e;

import org.lazaro.rt5e.logic.World;
import org.lazaro.rt5e.utility.Configuration;
import org.lazaro.rt5e.utility.Logger;
import org.lazaro.rt5e.utility.NativeConsole;

/**
 * @author Lazaro
 */
public class WorldApp {
    private static boolean active = false;

    public static boolean isActive() {
        return active;
    }

    public static World getWorld() {
        return world;
    }

    private static World world = null;

    public static void main(String[] args) {
        active = true;

        NativeConsole.setHeader("RT5E [World Server]");
        Logger.setupLogging();
        Logger.printInfo();

        System.out.println("Starting world server...");
        Logger.incrementIndentationTab();

        try {
            Context.setConfiguration(new Configuration(Constants.LOGIN_SERVER_CONFIG));
            System.out.println("Loaded settings");

            world = new World(1);
            world.start();
            System.out.println("Loaded world!");

        } catch (Throwable e) {
            e.printStackTrace();
        }

        Logger.resetIndentation();
        System.out.println("DONE!");
    }
}
