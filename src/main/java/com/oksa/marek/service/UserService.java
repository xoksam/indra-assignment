package com.oksa.marek.service;

import com.oksa.marek.dao.UserDao;
import com.oksa.marek.model.User;

/**
 * A simple service providing all needed User-related operations
 */
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User addUser(String name) {
        // Only for debugging/demonstration purposes
        System.out.println("Adding user '" + name + "'");
        var user = new User(name);
        return userDao.insertUser(user);
    }

    public void printAllUsers() {
        var users = userDao.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("Users are empty");
        }
        users.forEach(System.out::println);
    }

    public int deleteAllUsers() {
        // Only for debugging/demonstration purposes
        System.out.println("Deleting all users");
        return userDao.deleteAllUsers();
    }

}
