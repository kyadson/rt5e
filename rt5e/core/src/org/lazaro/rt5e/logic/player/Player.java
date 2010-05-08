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
