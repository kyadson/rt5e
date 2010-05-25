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

/**
 * @author Lazaro
 */
public enum LoginResponse {
    ALREADY_ONLINE(5), BANNED(4), CLIENT_UPDATED(6), CURRENTLY_UPDATING(14), ERROR(
            13), INVALID_DETAILS(3), LOGIN(2), LOGIN_LIMIT_EXCEEDED(9), LOGIN_SERVER_OFFLINE(
            8), WORLD_FULL(7);

    public static LoginResponse valueFor(int responseCode) {
        switch (responseCode) {
            case 2:
                return LOGIN;
            case 3:
                return INVALID_DETAILS;
            case 4:
                return BANNED;
            case 5:
                return ALREADY_ONLINE;
            case 6:
                return CLIENT_UPDATED;
            case 7:
                return WORLD_FULL;
            case 8:
                return LOGIN_SERVER_OFFLINE;
            case 9:
                return LOGIN_LIMIT_EXCEEDED;
            case 13:
                return ERROR;
            case 14:
                return CURRENTLY_UPDATING;
        }
        return null;
    }

    private int responseCode;

    private LoginResponse() {
        this(-1);
    }

    private LoginResponse(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public String toString() {
        return "response=" + super.toString().replace("_", " ").toLowerCase();
    }
}
