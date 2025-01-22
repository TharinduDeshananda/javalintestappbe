package com.tdedsh.controller;

import com.tdedsh.dto.*;
import com.tdedsh.dto.mapper.UserMapper;
import com.tdedsh.generated.tables.records.UsersRecord;
import com.tdedsh.util.TokenUtil;
import io.javalin.http.Context;
import io.javalin.http.Cookie;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.tdedsh.generated.tables.Users.USERS;

public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static DSLContext db; // Assume this is initialized elsewhere (e.g., in a DatabaseConfig class)
    // Inject the DSLContext (dependency injection)
    public static void setDb(DSLContext db) {
        AuthController.db = db;
    }
    public static void loginUser(Context ctx) {
        // Validate request body
        LoginDto loginDto = ctx.bodyAsClass(LoginDto.class);
        if (loginDto.getUserName() == null || loginDto.getUserName().isBlank()) {
            throw new CustomException(400, "User name is required");
        }
        if (loginDto.getPassword() == null || loginDto.getPassword().isBlank()) {
            throw new CustomException(400, "Password is required");
        }

        // Fetch user from database
        var user = db.selectFrom(USERS)
                .where(USERS.EMAIL.eq(loginDto.getUserName()))
                .fetchOneInto(UsersRecord.class);
        if (user == null) {
            throw new CustomException(404, "User not found");
        }

        // Validate password
        if (!user.getPasswordHash().equals(loginDto.getPassword())) {
            throw new CustomException(400, "Password is not correct");
        }

        // Generate JWT token
        String token = TokenUtil.generateToken(user.getId(),loginDto.getUserName());

        // Set cookie with SameSite, Secure, and HttpOnly flags
        String cookieHeader = String.format(
                "jwt=%s; Path=/; Max-Age=86400; HttpOnly; SameSite=None; Secure",
                token
        );
        ctx.header("Set-Cookie", cookieHeader);

        // Set CORS headers
        ctx.header("Access-Control-Allow-Origin", "http://localhost:5173");
        ctx.header("Access-Control-Allow-Credentials", "true");

        // Return success response
        ctx.json(new CustomResponse(200, new UserDto(user.getId(),user.getUsername(),user.getEmail(),null), "Login success"));
    }

    public static void logoutUser(Context ctx){
        ctx.removeCookie("jwt");
        ctx.status(200).json(new CustomResponse(200,null,"Logged out"));
    }

    public static void create(Context ctx) {
        try {
            log.info("Auth user create start");
            var user = ctx.bodyAsClass(CreateUserDto.class);
            UsersRecord usersRecord = UserMapper.createDtoToUserRecord(user);
            db.insertInto(USERS)
                    .set(usersRecord)
                    .execute();
            ctx.status(200).json(new CustomResponse(200,null,"User account created"));
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    public static UserDto getAuthenticatedUser(Context ctx){
        String userEmail = (String)ctx.attribute("userEmail");
        if(userEmail==null||userEmail.isBlank())return null;

        UsersRecord usersRecord = db.selectFrom(USERS).where(USERS.EMAIL.eq(userEmail)).fetchOneInto(UsersRecord.class);
        if(usersRecord==null)return null;
        return UserMapper.toUserDTO(usersRecord);
    }

    public static String isAuthenticated(Context ctx){
        return ctx.attribute("userEmail");
    }

    public static int getAuthernticatedUserId(Context ctx){
        return Optional.ofNullable((Integer)ctx.attribute("userId")).orElse(0);
    }

}
