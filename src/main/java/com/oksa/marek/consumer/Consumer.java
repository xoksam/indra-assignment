package com.oksa.marek.consumer;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Consumer that consumes and executes Runnables
 */
public class Consumer {
    private static final long IDLE_TIME_MILLIS = 5 * 1000L;
    private final Logger logger = Logger.getLogger(Consumer.class.getName());
    private final BlockingQueue<Runnable> sharedQueue;
    private boolean run = true;
    private long startIdleTime = 0;

    public Consumer(BlockingQueue<Runnable> sharedQueue) {
        this.sharedQueue = sharedQueue;
    }

    public void stop() {
        logger.log(Level.INFO, "Stopping consumer");
        run = false;
    }

    public void consume() {
        while (run) {
            try {
                // Initially I wanted to implement a Poison Pill
                if (IDLE_TIME_MILLIS > (System.currentTimeMillis() - startIdleTime)) {
                    stop();
                    return;
                }

                if (sharedQueue.isEmpty()) {
                    startIdleTime = System.currentTimeMillis();
                    continue;
                }

                startIdleTime = 0;
                sharedQueue.take().run();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error while consuming", e);
            }
        }
    }
}
