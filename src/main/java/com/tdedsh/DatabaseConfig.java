package com.tdedsh;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConfig {
    public static DSLContext create() {
        try {
            // Replace with your MySQL connection details
            String url = "jdbc:mysql://localhost:3306/taskdb";
            String user = "root";
            String password = "mysql";

            // Create a connection
            Connection connection = DriverManager.getConnection(url, user, password);

            // Create a JOOQ DSLContext
            return DSL.using(connection, SQLDialect.MYSQL);
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to the database", e);
        }
    }
}
