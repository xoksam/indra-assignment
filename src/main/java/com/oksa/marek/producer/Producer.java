package com.oksa.marek.producer;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * A Producer that produces Runnables
 */
public class Producer {

    private final Logger logger = Logger.getLogger(Producer.class.getName());
    private final BlockingQueue<Runnable> sharedQueue;
    private final Collection<Runnable> commands;

    public Producer(BlockingQueue<Runnable> sharedQueue, Collection<Runnable> commands) {
        this.sharedQueue = sharedQueue;
        this.commands = commands;
    }

    public void produce() {
        try {
            for (Runnable command : commands) {
                sharedQueue.put(command);
            }
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Error while producing", e);
        }
    }

}
