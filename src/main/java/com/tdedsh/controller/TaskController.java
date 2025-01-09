package com.tdedsh.controller;

import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;

public class TaskController {
    public static void createTask(Context ctx) {
        ctx.result("Create task");
    }

    public static void getSingleTask(Context ctx) {
        String taskId = ctx.pathParam("taskId");
        ctx.result("Get task with ID: " + taskId);
    }

    public static void getAllTasksFiltered(Context ctx) {
        ctx.result("Get all tasks");
    }

    public static void updateTask(Context ctx) {
        String taskId = ctx.pathParam("taskId");
        ctx.result("Update task with ID: " + taskId);
    }

    public static void removeTask(Context ctx) {
        String taskId = ctx.pathParam("taskId");
        ctx.result("Remove task with ID: " + taskId);
    }
}
