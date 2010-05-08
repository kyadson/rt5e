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

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Lazaro
 */
public class Engine {
    private static final double THREAD_MULTIPLIER = 1.0;

    private final static Engine instance = new Engine();

    public static Engine getInstance() {
        return instance;
    }

    private Engine() {
    }

    private Semaphore semaphore = new Semaphore();
    private Executor asyncExecutor = Executors.newSingleThreadExecutor();
    private Executor parallelExecutor = Executors.newFixedThreadPool((int) ((Runtime.getRuntime().availableProcessors() == 1 ? 2 : Runtime.getRuntime().availableProcessors()) * THREAD_MULTIPLIER));
    private Executor blockingExecutor = Executors.newFixedThreadPool(2);

    public Executor getAsyncExecutor() {
        return asyncExecutor;
    }

    public Executor getParallelExecutor() {
        return parallelExecutor;
    }

    public Executor getBlockingExecutor() {
        return blockingExecutor;
    }

    public void execute(Runnable r) {
        asyncExecutor.execute(r);
    }

    public void executeParallel(Runnable r) {
        parallelExecutor.execute(r);
    }

    public void executeBlocking(Runnable r) {
        blockingExecutor.execute(r);
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }
}
