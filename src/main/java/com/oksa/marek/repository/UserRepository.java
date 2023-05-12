package com.oksa.marek.repository;

import com.oksa.marek.model.User;

import java.util.Collection;
import java.util.UUID;

/**
 * An interface containing all User-related data operations
 */
public interface UserRepository {
    Collection<User> getAllUsers();
    User getUserByUUID(UUID uuid);
    User insertUser(User user);
    int deleteAllUsers();
}
