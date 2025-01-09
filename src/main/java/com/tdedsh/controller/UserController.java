package com.tdedsh.controller;

import io.javalin.http.Context;

public class UserController {
    public static void getAll(Context ctx) {
        ctx.result("Get All ok");
    }

    public static void create(Context ctx) {
        ctx.result("Create is ok");
    }

    public static void getOne(Context ctx) {
        ctx.result("getOne is ok");
    }

    public static void update(Context ctx) {
        ctx.result("update is ok");
    }

    public static void delete(Context ctx) {
        ctx.result("delete is ok");
    }
}
