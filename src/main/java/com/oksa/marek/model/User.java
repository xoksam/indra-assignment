package com.oksa.marek.model;

import java.util.Objects;
import java.util.UUID;


/**
 * A data model representing the user
 */
public class User {
    private final Long id;
    private final UUID uuid;
    private final String name;

    public User(String username) {
        this(null, UUID.randomUUID(), username);
    }

    public User(Long id, UUID uuid, String name) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && uuid.equals(user.uuid) && name.equals(user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, name);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", name='" + name + '\'' +
                '}';
    }

    // I would've used some persistence framework to handle these things
    // But the assignment said not to use Spring and that using only an embedded DB is sufficient
    // So this is the solution I came up with
    public static class DbColumns {
        private DbColumns() {
            // No instances
        }

        public static final String id = "USER_ID";
        public static final String uuid = "USER_UUID";
        public static final String name = "USER_NAME";
    }

}
