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
