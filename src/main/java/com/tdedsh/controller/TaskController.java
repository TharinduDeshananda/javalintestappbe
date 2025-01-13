package com.tdedsh.controller;

import com.tdedsh.dto.CustomException;
import com.tdedsh.dto.CustomResponse;
import com.tdedsh.dto.TaskDto;
import com.tdedsh.dto.mapper.TaskMapper;
import com.tdedsh.generated.enums.TasksStatus;
import com.tdedsh.generated.tables.Users;
import com.tdedsh.generated.tables.records.TasksRecord;
import com.tdedsh.generated.tables.records.UsersRecord;
import io.javalin.http.Context;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.tdedsh.generated.Tables.USERS;
import static com.tdedsh.generated.tables.Tasks.TASKS;

public class TaskController {

    private static final Logger log = LoggerFactory.getLogger(TaskController.class);
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
        var tasks = query.fetchInto(TasksRecord.class).stream().map(TaskMapper::toTaskDTO).toList();
        ctx.json(new CustomResponse(200,tasks,"Success"));
    }

    // Get a single task by ID
    public static void getSingleTask(Context ctx) {
        int taskId = Integer.parseInt(ctx.pathParam("taskId"));
        var task = db.selectFrom(TASKS)
                .where(TASKS.ID.eq(taskId))
                .fetchOneInto(TasksRecord.class);
        if (task != null) {
            ctx.json(new CustomResponse(200,TaskMapper.toTaskDTO(task),"Success"));
        } else {
            ctx.status(404).json(new CustomResponse(404,null,"Task not found"));
        }
    }

    // Create a new task
    public static void createTask(Context ctx) {
        log.info("method createTask start");
        //get authenticated user
        var userEmail = (String)ctx.attribute("userEmail");
        if(userEmail==null|| userEmail.isBlank())throw new CustomException(401,"Unauthorized");
        var user = db.selectFrom(Users.USERS)
                .where(Users.USERS.EMAIL.eq(userEmail))
                .fetchOneInto(UsersRecord.class);
        if(user==null)throw new CustomException(401,"Unauthorized");


        var task = ctx.bodyAsClass(TaskDto.class);

        //set user id
        task.setUserId(user.getId());

        db.insertInto(TASKS)
                .set(TaskMapper.toTasksRecord(task))
                .execute();
        ctx.status(200).json(new CustomResponse(200,null,"Task created"));
    }

    // Update an existing task
    public static void updateTask(Context ctx) {
//        int taskId = Integer.parseInt(ctx.pathParam("taskId"));
        var task = ctx.bodyAsClass(TaskDto.class);
        int taskId = task.getId();
        if(taskId<=0)throw new CustomException(404,"Task not found");
        db.update(TASKS)
                .set(TaskMapper.toTasksRecord(task))
                .where(TASKS.ID.eq(taskId))
                .execute();
        ctx.status(200).json(new CustomResponse(200,null,"Task updated"));
    }

    // Delete a task
    public static void removeTask(Context ctx) {
        int taskId = Integer.parseInt(ctx.pathParam("taskId"));
        db.deleteFrom(TASKS)
                .where(TASKS.ID.eq(taskId))
                .execute();
        ctx.status(200).json(new CustomResponse(200,null,"Task removed"));
    }
}
