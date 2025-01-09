package com.tdedsh.controller;

import io.javalin.http.Context;

public class UserController {
    public static void getAll(Context ctx) {
        ctx.result("Get all users");
    }

    public static void getOne(Context ctx) {
        String userId = ctx.pathParam("userId");
        ctx.result("Get user with ID: " + userId);
    }

    public static void create(Context ctx) {
        ctx.result("Create a user");
    }

    public static void update(Context ctx) {
        String userId = ctx.pathParam("userId");
        ctx.result("Update user with ID: " + userId);
    }

    public static void delete(Context ctx) {
        String userId = ctx.pathParam("userId");
        ctx.result("Delete user with ID: " + userId);
    }
}
