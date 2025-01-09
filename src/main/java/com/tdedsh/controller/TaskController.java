package com.tdedsh.controller;

import com.tdedsh.generated.enums.TasksStatus;
import com.tdedsh.generated.tables.records.TasksRecord;
import io.javalin.http.Context;
import org.jooq.DSLContext;

import static com.tdedsh.generated.tables.Tasks.TASKS;

public class TaskController {
    private static DSLContext db; // Assume this is initialized elsewhere (e.g., in a DatabaseConfig class)

    // Inject the DSLContext (dependency injection)
    public static void setDb(DSLContext db) {
        TaskController.db = db;
    }

    // Get all tasks (filtered by status if provided)
    public static void getAllTasksFiltered(Context ctx) {
        String status = ctx.queryParam("status"); // Optional query parameter
        var query = db.selectFrom(TASKS);
        if (status != null) {
            query.where(TASKS.STATUS.eq(TasksStatus.valueOf(status)));
        }
        var tasks = query.fetchInto(TasksRecord.class);
        ctx.json(tasks);
    }

    // Get a single task by ID
    public static void getSingleTask(Context ctx) {
        int taskId = Integer.parseInt(ctx.pathParam("taskId"));
        var task = db.selectFrom(TASKS)
                .where(TASKS.ID.eq(taskId))
                .fetchOneInto(TasksRecord.class);
        if (task != null) {
            ctx.json(task);
        } else {
            ctx.status(404).result("Task not found");
        }
    }

    // Create a new task
    public static void createTask(Context ctx) {
        var task = ctx.bodyAsClass(TasksRecord.class);
        db.insertInto(TASKS)
                .set(task)
                .execute();
        ctx.status(201).result("Task created");
    }

    // Update an existing task
    public static void updateTask(Context ctx) {
        int taskId = Integer.parseInt(ctx.pathParam("taskId"));
        var task = ctx.bodyAsClass(TasksRecord.class);
        db.update(TASKS)
                .set(task)
                .where(TASKS.ID.eq(taskId))
                .execute();
        ctx.result("Task updated");
    }

    // Delete a task
    public static void removeTask(Context ctx) {
        int taskId = Integer.parseInt(ctx.pathParam("taskId"));
        db.deleteFrom(TASKS)
                .where(TASKS.ID.eq(taskId))
                .execute();
        ctx.result("Task deleted");
    }
}
