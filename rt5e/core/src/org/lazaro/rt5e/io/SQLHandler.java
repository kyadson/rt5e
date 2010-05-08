package org.lazaro.rt5e.io;

import org.lazaro.rt5e.utility.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Lazaro
 */
public class SQLHandler {
    private Connection connection;

    public SQLHandler(Configuration configuration)
            throws InstantiationException, IllegalAccessException,
            ClassNotFoundException, SQLException {
        // Set up the properties
        Properties properties = new Properties();
        properties.put("user", configuration.getString("SQL_USER"));
        properties.put("password", configuration.getString("SQL_PASS"));
        properties.put("autoReconnect", configuration
                .getString("SQL_RECONNECT"));
        properties.put("maxReconnects", configuration
                .getString("SQL_MAX_RECONNECTS"));

        // Load the SQL driver
        Class.forName(configuration.getString("SQL_DRIVER")).newInstance();

        // Connect to the server
        connection = DriverManager.getConnection(configuration
                .getString("SQL_HOST"), properties);

        // Open the database
        connection.createStatement().executeQuery("use rt5;");
    }

    public Connection getConnection() {
        return connection;
    }
}
