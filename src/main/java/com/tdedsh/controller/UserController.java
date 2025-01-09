package com.tdedsh.controller;

import com.tdedsh.generated.tables.records.UsersRecord;
import io.javalin.http.Context;
import org.jooq.DSLContext;

import static com.tdedsh.generated.tables.Users.USERS;

public class UserController {
    private static DSLContext db; // Assume this is initialized elsewhere (e.g., in a DatabaseConfig class)

    // Inject the DSLContext (dependency injection)
    public static void setDb(DSLContext db) {
        UserController.db = db;
    }

    // Get all users
    public static void getAll(Context ctx) {
        var users = db.selectFrom(USERS).fetchInto(UsersRecord.class);
        ctx.json(users);
    }

    // Get a single user by ID
    public static void getOne(Context ctx) {
        int userId = Integer.parseInt(ctx.pathParam("userId"));
        var user = db.selectFrom(USERS)
                .where(USERS.ID.eq(userId))
                .fetchOneInto(UsersRecord.class);
        if (user != null) {
            ctx.json(user);
        } else {
            ctx.status(404).result("User not found");
        }
    }

    // Create a new user
    public static void create(Context ctx) {
        var user = ctx.bodyAsClass(UsersRecord.class);
        db.insertInto(USERS)
                .set(user)
                .execute();
        ctx.status(201).result("User created");
    }

    // Update an existing user
    public static void update(Context ctx) {
        int userId = Integer.parseInt(ctx.pathParam("userId"));
        var user = ctx.bodyAsClass(UsersRecord.class);
        db.update(USERS)
                .set(user)
                .where(USERS.ID.eq(userId))
                .execute();
        ctx.result("User updated");
    }

    // Delete a user
    public static void delete(Context ctx) {
        int userId = Integer.parseInt(ctx.pathParam("userId"));
        db.deleteFrom(USERS)
                .where(USERS.ID.eq(userId))
                .execute();
        ctx.result("User deleted");
    }
}
