package org.lazaro.rt5e.logic.login;

/**
 * @author Lazaro
 */
public enum LoginResponse {
    ALREADY_ONLINE(5), BANNED(4), CLIENT_UPDATED(6), CURRENTLY_UPDATING(14), ERROR(13), INVALID_DETAILS(3), LOGIN(2), LOGIN_LIMIT_EXCEEDED(9), LOGIN_SERVER_OFFLINE(8), WORLD_FULL(7);

    public static LoginResponse forIntegerCode(int responseCode) {
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
}
