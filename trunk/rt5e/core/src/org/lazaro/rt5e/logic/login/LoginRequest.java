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
package org.lazaro.rt5e.logic.login;

import org.lazaro.rt5e.network.Connection;

/**
 * @author Lazaro
 */
public class LoginRequest {
    private String name;
    private String password;

    public Connection getConnection() {
        return connection;
    }

    private Connection connection;

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }


    public int getLoginOpcode() {
        return loginOpcode;
    }


    private int loginOpcode;

    public LoginRequest(Connection connection, String name, String password, int loginOpcode) {
        this.connection = connection;
        this.name = name;
        this.password = password;
        this.loginOpcode = loginOpcode;
    }

    public String toString() {
        return "name=" + name + ", password=" + password.replaceAll(".", "*") + ", opcode=" + loginOpcode;
    }
}
