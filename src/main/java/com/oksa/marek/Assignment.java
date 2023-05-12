package com.oksa.marek;

import com.oksa.marek.config.DatabaseConfig;
import com.oksa.marek.consumer.Consumer;
import com.oksa.marek.dao.UserDao;
import com.oksa.marek.producer.Producer;
import com.oksa.marek.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Assignment {

    public static void main(String[] args) throws InterruptedException {
        DatabaseConfig.configure();
        var connection = DatabaseConfig.getConnection();
        var userService = new UserService(new UserDao(connection));

        BlockingQueue<Runnable> sharedQueue = new LinkedBlockingQueue<>();
        Collection<Runnable> commands = List.of(
                () -> userService.addUser("Robert"),
                () -> userService.addUser("Martin"),
                () -> {
                    System.out.println("All users before:");
                    userService.printAllUsers();
                },
                userService::deleteAllUsers,
                () -> {
                    System.out.println("All users after:");
                    userService.printAllUsers();
                }
        );

        var producerThread = new Thread(() -> {
            var producer = new Producer(sharedQueue, commands);
            producer.produce();
        });

        var consumerThread = new Thread(() -> {
            var consumer = new Consumer(sharedQueue);
            consumer.consume();
        });

        producerThread.start();
        producerThread.join();

        consumerThread.start();
        consumerThread.join();
    }

}
