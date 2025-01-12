package com.tdedsh.controller;

import com.tdedsh.dto.CustomResponse;
import com.tdedsh.dto.UserDto;
import com.tdedsh.dto.mapper.UserMapper;
import com.tdedsh.generated.tables.records.UsersRecord;
import io.javalin.http.Context;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static com.tdedsh.generated.tables.Users.USERS;

public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static DSLContext db; // Assume this is initialized elsewhere (e.g., in a DatabaseConfig class)

    // Inject the DSLContext (dependency injection)
    public static void setDb(DSLContext db) {
        UserController.db = db;
    }

    // Get all users
    public static void getAll(Context ctx) {
        log.info("getAll called");
        var users = db.selectFrom(USERS).fetchInto(UsersRecord.class);
        ctx.json(new CustomResponse(200,users.stream().map(UserMapper::toUserDTO).toList(),"Success"));
    }

    // Get a single user by ID
    public static void getOne(Context ctx) {
        int userId = Integer.parseInt(ctx.pathParam("userId"));
        var user = db.selectFrom(USERS)
                .where(USERS.ID.eq(userId))
                .fetchOneInto(UsersRecord.class);
        if (user != null) {
            ctx.json(new CustomResponse(200,UserMapper.toUserDTO(user),"Success"));
        } else {
            ctx.status(404).json(new CustomResponse(404,null,"User not found"));
        }
    }

    // Create a new user
    public static void create(Context ctx) {
        log.info("user create start");
        var user = ctx.bodyAsClass(UserDto.class);
        UsersRecord usersRecord = UserMapper.toUsersRecord(user);
        db.insertInto(USERS)
                .set(usersRecord)
                .execute();
        ctx.status(200).json(new CustomResponse(200,null,"User created"));
    }

    // Update an existing user
    public static void update(Context ctx) {
        int userId = Integer.parseInt(ctx.pathParam("userId"));
        var user = ctx.bodyAsClass(UserDto.class);
        UsersRecord usersRecord = UserMapper.toUsersRecord(user);
        db.update(USERS)
                .set(usersRecord)
                .where(USERS.ID.eq(userId))
                .execute();
        ctx.json(new CustomResponse(200,null,"User updated"));;
    }

    // Delete a user
    public static void delete(Context ctx) {
        int userId = Integer.parseInt(ctx.pathParam("userId"));
        db.deleteFrom(USERS)
                .where(USERS.ID.eq(userId))
                .execute();
        ctx.json(new CustomResponse(200,null,"User deleted"));;
    }
}
