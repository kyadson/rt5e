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
package org.lazaro.rt5e.engine;

/**
 * @author Lazaro
 */
public class Semaphore {
    private Permit currentPermit = null;
    private Object lock = new Object();

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
