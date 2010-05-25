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

import org.lazaro.rt5e.engine.event.Event;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Lazaro
 */
public class Engine {
    private final static Engine instance = new Engine();

    private static final double THREAD_MULTIPLIER = 1.0;

    public static Engine getInstance() {
        return instance;
    }

    private Executor asyncExecutor = Executors.newSingleThreadExecutor();

    private Executor blockingExecutor = Executors.newFixedThreadPool(8);

    private ScheduledExecutorService coreExecutor = Executors
            .newSingleThreadScheduledExecutor();

    private List<Event> logicEvents = new LinkedList<Event>();
    private List<Event> miscEvents = new LinkedList<Event>();
    private ScheduledExecutorService parallelExecutor = Executors
            .newScheduledThreadPool((int) ((Runtime.getRuntime()
                    .availableProcessors() == 1 ? 2 : Runtime.getRuntime()
                    .availableProcessors()) * THREAD_MULTIPLIER));
    private boolean running = false;
    private Semaphore semaphore = new Semaphore();

    private Engine() {
    }

    public void execute(Runnable r) {
        asyncExecutor.execute(r);
    }

    public void executeBlocking(Runnable r) {
        blockingExecutor.execute(r);
    }

    private void executeEvent(final Event event) {
        coreExecutor.schedule(new Runnable() {
            public void run() {
                event.updateLastRun();
                try {
                    event.run();
                } catch (Throwable e) {
                    System.err.println("Error executing event [" + event + "]");
                    e.printStackTrace();
                }

                if (event.isRunning()) {
                    executeEvent(event);
                } else {
                    synchronized (logicEvents) {
                        logicEvents.remove(event);
                    }
                }
            }
        }, event.getTimeRemaining(), TimeUnit.MILLISECONDS);
    }

    private void executeMiscEvent(final Event event) {
        parallelExecutor.schedule(new Runnable() {
            public void run() {
                event.updateLastRun();
                try {
                    event.run();
                } catch (Throwable e) {
                    System.err.println("Error executing event [" + event + "]");
                    e.printStackTrace();
                }

                if (event.isRunning()) {
                    executeMiscEvent(event);
                } else {
                    synchronized (miscEvents) {
                        miscEvents.remove(event);
                    }
                }
            }
        }, event.getTimeRemaining(), TimeUnit.MILLISECONDS);
    }

    public void executeParallel(Runnable r) {
        parallelExecutor.execute(r);
    }

    public Executor getAsyncExecutor() {
        return asyncExecutor;
    }

    public Executor getBlockingExecutor() {
        return blockingExecutor;
    }

    public ScheduledExecutorService getCoreExecutor() {
        return coreExecutor;
    }

    public ScheduledExecutorService getParallelExecutor() {
        return parallelExecutor;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public void start() {
        running = true;
        synchronized (logicEvents) {
            for (Event event : logicEvents) {
                executeEvent(event);
            }
        }
        synchronized (miscEvents) {
            for (Event event : miscEvents) {
                executeMiscEvent(event);
            }
        }
    }

    public void submitEvent(Event event) {
        synchronized (logicEvents) {
            logicEvents.add(event);
        }
        if (running) {
            executeEvent(event);
        }
    }

    public void submitMiscEvent(Event event) {
        synchronized (miscEvents) {
            miscEvents.add(event);
        }
        if (running) {
            executeMiscEvent(event);
        }
    }
}
