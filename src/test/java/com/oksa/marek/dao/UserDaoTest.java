package com.oksa.marek.dao;

import com.oksa.marek.config.DatabaseConfig;
import com.oksa.marek.model.User;
import liquibase.Contexts;
import liquibase.LabelExpression;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDaoTest {
    private UserDao userDao;
    private Connection connection;

    @BeforeAll
    void setupDb() {
        DatabaseConfig.configure();
        connection = DatabaseConfig.getConnection();
        userDao = new UserDao(connection);
    }

    @AfterEach
    void resetDB() {
        try {
           var liquibase = DatabaseConfig.getLiquibase(connection);
            liquibase.dropAll();
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetAllUsers_shouldReturnAllUsers() {
        var numberOfUsers = (int) Math.floor(Math.random() * 10) + 1;
        insertUsers(numberOfUsers);

        var allUsers = userDao.getAllUsers();
        assertFalse(allUsers.isEmpty());

        for (var user : allUsers) {
            System.out.println(user);
        }

        assertEquals(allUsers.size(), numberOfUsers);
    }

    @Test
    void testInsertUser_shouldInsertAndReturnUser() {
        var user = new User("Test user");
        var insertedUser = userDao.insertUser(user);

        assertNotNull(insertedUser);
        assertEquals(user.getUuid(), insertedUser.getUuid());
        assertEquals(user.getName(), insertedUser.getName());
        assertNotNull(insertedUser.getId());
    }

    @Test
    void testGetUserByUUID_shouldReturnCorrectUser() {
        var user = new User("Test user");
        userDao.insertUser(user);

        var foundUser = userDao.getUserByUUID(user.getUuid());

        assertNotNull(foundUser);

        assertEquals(user.getUuid(), foundUser.getUuid());
        assertEquals(user.getName(), foundUser.getName());
        assertNotNull(foundUser.getId());
    }

    @Test
    void testDeleteAllUsers_shouldCreateAndDeleteAllUsers() {
        var numberOfUsers = (int) Math.floor(Math.random() * 10) + 1;
        insertUsers(numberOfUsers);

        var allUsersBefore = userDao.getAllUsers();
        assertFalse(allUsersBefore.isEmpty());
        assertEquals(allUsersBefore.size(), numberOfUsers);

        userDao.deleteAllUsers();

        var allUsersAfter = userDao.getAllUsers();
        assertTrue(allUsersAfter.isEmpty());
    }

    private void insertUsers(int amount) {
        for (int i = 0; i < amount; i++) {
            var userToInsert = new User("User " + i);
            userDao.insertUser(userToInsert);
        }
    }
}
