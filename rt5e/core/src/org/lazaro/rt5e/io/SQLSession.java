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
package org.lazaro.rt5e.io;

import org.lazaro.rt5e.utility.Configuration;
import org.lazaro.rt5e.utility.Pooled;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Lazaro
 */
public class SQLSession implements Pooled {
    private Connection connection;

    public void init(Configuration configuration)
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

    public void recycle() {
    }
}
