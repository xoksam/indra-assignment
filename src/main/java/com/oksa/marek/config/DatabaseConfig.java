package com.oksa.marek.config;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;


/**
 * A configuration class, configuring Database, Liquibase and all other DB-related things
 */
public class DatabaseConfig {
    public static final String PROPERTIES_FILENAME = "db.properties";
    private static final Set<String> REQUIRED_PROPERTIES = Set.of(
            "liquibase.changelogPath",
            "db.jdbc.url",
            "db.username",
            "db.password"
    );

    private static final Properties properties = getDatabaseProperties();

    private DatabaseConfig() {
        // No instances
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    properties.getProperty("db.jdbc.url"),
                    properties.getProperty("db.username"),
                    properties.getProperty("db.password")
            );
        } catch (SQLException e) {
            throw new RuntimeException("An error occurred while creating DB connection!", e);
        }
    }

    public static void configure() {
        try {
            var connection = getConnection();
            var liquibase = getLiquibase(connection);
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while configuring the DB!", e);
        }
    }

    public static Liquibase getLiquibase(Connection connection) throws DatabaseException {
        var database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        return new Liquibase(
                properties.getProperty("liquibase.changelogPath"),
                new ClassLoaderResourceAccessor(),
                database
        );
    }

    private static Properties getDatabaseProperties() {
        var properties = new Properties();

        try (var input = DatabaseConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILENAME)) {
            if (input == null) {
                throw new IOException("Unable to find " + PROPERTIES_FILENAME + " file");
            }

            properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("There was an error while loading " + PROPERTIES_FILENAME + " file!", ex);
        }

        if (!properties.stringPropertyNames().containsAll(REQUIRED_PROPERTIES)) {
            throw new IllegalStateException("Property file doesn't contain all required properties! " + REQUIRED_PROPERTIES);
        }

        return properties;
    }

}
