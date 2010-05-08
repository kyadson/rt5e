package org.lazaro.rt5e.engine;

/**
 * @author Lazaro
 */
public class Permit {
    private boolean valid = true;

    public boolean isValid() {
        return valid;
    }

    public void cancelPermit() {
        valid = false;
    }
}
