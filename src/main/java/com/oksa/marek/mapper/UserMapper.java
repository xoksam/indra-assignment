package com.oksa.marek.mapper;

import com.oksa.marek.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * A simple mapper that maps other objects to a User object and vice versa
 */
public class UserMapper {

    public User resultSetToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong(User.DbColumns.id),
                UUID.fromString(rs.getString(User.DbColumns.uuid)),
                rs.getString(User.DbColumns.name)
        );
    }
}
