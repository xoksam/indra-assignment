package com.oksa.marek.dao;

import com.oksa.marek.mapper.UserMapper;
import com.oksa.marek.model.User;
import com.oksa.marek.repository.UserRepository;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.oksa.marek.model.User.DbColumns;

/**
 * Data access object for Users
 * Ideally I would've used some persistence framework to handle the data layer...
 */
public class UserDao implements UserRepository {
    private static final String TABLE_NAME = "SUSERS";
    private static final String SELECT_USER_QUERY = String.format("SELECT %s,%s,%s FROM %s",
            DbColumns.id,
            DbColumns.uuid,
            DbColumns.name,
            TABLE_NAME
    );

    private final Connection connection;
    private final UserMapper mapper;

    public UserDao(Connection connection) {
        this.connection = connection;
        mapper = new UserMapper();
    }

    @Override
    public List<User> getAllUsers() {
        try (var statement = connection.createStatement()) {
            var rs = statement.executeQuery(SELECT_USER_QUERY);
            var result = new ArrayList<User>();

            while (rs.next()) {
                result.add(mapper.resultSetToUser(rs));
            }

            return result;
        } catch (Exception ex) {
            throw new RuntimeException("An error occurred while querying data!", ex);
        }
    }

    @Override
    public User getUserByUUID(UUID uuid) {
        try (var statement = connection.createStatement()) {
            var query = String.format("%s WHERE %s = '%s'", SELECT_USER_QUERY, DbColumns.uuid, uuid);
            var rs = statement.executeQuery(query);
            User result = null;

            if (rs.first()) {
                result = mapper.resultSetToUser(rs);
            }

            return result;
        } catch (Exception ex) {
            throw new RuntimeException("An error occurred while querying data!", ex);
        }
    }

    @Override
    public User insertUser(User user) {
        try (var statement = connection.createStatement()) {
            var insert = String.format("INSERT INTO %s(%s, %s) VALUES('%s', '%s')",
                    TABLE_NAME,
                    DbColumns.uuid,
                    DbColumns.name,
                    user.getUuid(),
                    user.getName()
            );

            statement.executeUpdate(insert);

            return getUserByUUID(user.getUuid());
        } catch (Exception ex) {
            throw new RuntimeException("An error occurred while inserting data!", ex);
        }
    }

    @Override
    public int deleteAllUsers() {
        try (var statement = connection.createStatement()) {
            var delete = String.format("DELETE FROM %s WHERE 1=1",
                    TABLE_NAME
            );

            return statement.executeUpdate(delete);
        } catch (Exception ex) {
            throw new RuntimeException("An error occurred while deleting data!", ex);
        }
    }
}
