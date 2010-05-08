package org.lazaro.rt5e.utility;

/**
 * @author Lazaro
 */
public class Benchmark {
    private long startTime = 0;
    private long endTime = 0;

    public void start() {
        startTime = System.nanoTime();
    }

    public void end() {
        endTime = System.nanoTime();
    }

    public int elapsed() {
        return (int) ((endTime - startTime) / 1000000);
    }

    public void reset() {
        startTime = endTime = 0;
    }
}
