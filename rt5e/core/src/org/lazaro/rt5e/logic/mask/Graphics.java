package org.lazaro.rt5e.logic.mask;

/**
 * @author Lazaro
 */
public class Graphics implements Mask {
    private int delay;
    private int id;

    /**
     * Creates a graphics mask with no delay.
     *
     * @param id The graphics id.
     */
    public Graphics(int id) {
        this.id = id;
        this.delay = 0;
    }

    /**
     * Creates a graphics mask with the specified delay.
     *
     * @param id    The graphics id.
     * @param delay The delay.
     */
    public Graphics(int id, int delay) {
        this.id = id;
        this.delay = delay;
    }

    /**
     * @return the delay
     */
    public int getDelay() {
        return delay;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
}
