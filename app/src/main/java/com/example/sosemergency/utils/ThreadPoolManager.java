package com.example.sosemergency.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ThreadPoolManager provides a simple utility for managing a thread pool for background tasks.
 */
public class ThreadPoolManager {

    // Retrieve the number of available processor cores
    private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    // Configure the thread pool settings
    private static final int KEEP_ALIVE_TIME = 1;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    private static final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();

    // Create a fixed-size thread pool with the specified settings
    private static final Executor threadPoolExecutor = new ThreadPoolExecutor(
            NUMBER_OF_CORES,
            NUMBER_OF_CORES,
            KEEP_ALIVE_TIME,
            KEEP_ALIVE_TIME_UNIT,
            workQueue
    );

    /**
     * Execute a task on the background thread pool.
     *
     * @param task The task to be executed.
     */
    public static void execute(Runnable task) {
        threadPoolExecutor.execute(task);
    }

    /**
     * Execute a database query synchronously on a background thread.
     *
     * @param databaseQuery The database query to be executed.
     * @param <T>           The type of the result.
     * @return The result of the database query.
     * @throws Exception If an error occurs during the query execution.
     */
    public static <T> T executeDatabaseQuerySync(final Callable<T> databaseQuery) throws Exception {
        // Here we're using FutureTask to run the Callable on a background thread
        FutureTask<T> futureTask = new FutureTask<>(databaseQuery);
        threadPoolExecutor.execute(futureTask);

        try {
            // We block and wait for the result
            return futureTask.get();
        } catch (InterruptedException | ExecutionException e) {
            // Re-throwing exceptions as needed
            throw new Exception("Error executing database query", e);
        }
    }

    /**
     * Shutdown the thread pool when the application is closing.
     * Note: This method should be called when the application is shutting down to release resources.
     *
     * N.B: Pattern matching expression available just in JDK >= 17
     * (threadPoolExecutor instanceof ThreadPoolExecutor tpe)
     */
    public static void shutdown() {
        if (threadPoolExecutor instanceof ThreadPoolExecutor tpe) {
            // Shutdown() method to initiate an orderly shutdown of the thread pool
            tpe.shutdown();
        }
    }
}


