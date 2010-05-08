package org.lazaro.rt5e.logic.mask;

/**
 * @author Lazaro
 */
public class Animation implements Mask {
    private int delay;
    private int id;

    /**
     * Creates an animation with no delay.
     *
     * @param id The animation id.
     */
    public Animation(int id) {
        this(id, 0);
    }

    /**
     * Creates an animation with the specified delay.
     *
     * @param id    The animation id.
     * @param delay The delay.
     */
    public Animation(int id, int delay) {
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
