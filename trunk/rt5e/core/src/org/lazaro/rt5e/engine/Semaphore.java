package org.lazaro.rt5e.engine;

/**
 * @author Lazaro
 */
public class Semaphore {
    private Object lock = new Object();
    private Permit currentPermit = null;

    public Permit acquirePermit() throws InterruptedException {
        return acquirePermit(0);
    }

    public Permit acquirePermit(long timeout) throws InterruptedException {
        synchronized (lock) {
            if (currentPermit != null) {
                lock.wait(timeout);
            }
            return currentPermit = new Permit();
        }
    }

    public Permit acquireForcedPermit() throws InterruptedException {
        return acquireForcedPermit(0);
    }

    public Permit acquireForcedPermit(long timeout) throws InterruptedException {
        synchronized (lock) {
            if (currentPermit != null) {
                currentPermit.cancelPermit();
            }
            return acquirePermit(timeout);
        }
    }

    public void releasePermit(Permit permit) {
        synchronized (lock) {
            permit.cancelPermit();
            if (permit == currentPermit) {
                currentPermit = null;
                lock.notify();
            }
        }
    }
}
