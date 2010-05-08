package org.lazaro.rt5e.logic;

import org.lazaro.rt5e.Constants;
import org.lazaro.rt5e.engine.Engine;
import org.lazaro.rt5e.engine.Permit;
import org.lazaro.rt5e.engine.Semaphore;
import org.lazaro.rt5e.login.WorldConnector;
import org.lazaro.rt5e.utility.Benchmark;

/**
 * @author Lazaro
 */
public class World implements Runnable {
    public int getId() {
        return id;
    }

    private final int id;

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    private boolean running = true;
    private Thread thread;

    private WorldConnector session;

    public World(int id) {
        this.id = id;
    }

    public void start() throws Exception {
        session = new WorldConnector();

        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        Benchmark bm = new Benchmark();
        Semaphore semaphore = Engine.getInstance().getSemaphore();
        while (running) {
            bm.start();
            Permit permit = null;
            try {
                permit = semaphore.acquireForcedPermit();
            } catch (InterruptedException e) {
                break;
            }
            try {
            } catch (Throwable e) {
                System.err.println("World error");
                e.printStackTrace();
            }
            semaphore.releasePermit(permit);
            bm.end();
            try {
                Thread.sleep(Constants.UPDATE_INTERVAL - bm.elapsed());
            } catch (InterruptedException e) {
                break;
            }
        }
        System.out.print("World shutting down... ");
        // TODO execute shutdown code here
        System.out.println("DONE!");
    }
}
