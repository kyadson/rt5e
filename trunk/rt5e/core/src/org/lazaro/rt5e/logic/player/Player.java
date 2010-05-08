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
package org.lazaro.rt5e.logic.player;

import org.lazaro.rt5e.logic.Entity;

/**
 * @author Lazaro
 */
public class Player extends Entity {
    private String name = null;

    public int getLoginOpcode() {
        return loginOpcode;
    }

    public void setLoginOpcode(int loginOpcode) {
        this.loginOpcode = loginOpcode;
    }

    private int loginOpcode = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getNameHash() {
        return nameHash;
    }

    public void setNameHash(long nameHash) {
        this.nameHash = nameHash;
    }

    private String password = null;
    private long nameHash = 0;


    @Override
    protected void _process() {
    }

    @Override
    protected void _reset() {
    }
}
