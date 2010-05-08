package org.lazaro.rt5e;

import org.lazaro.rt5e.utility.Configuration;

/**
 * @author Lazaro
 */
public class Context {
    public static Configuration getConfiguration() {
        return configuration;
    }

    public static void setConfiguration(Configuration configuration) {
        Context.configuration = configuration;
    }

    private static Configuration configuration = null;
}
