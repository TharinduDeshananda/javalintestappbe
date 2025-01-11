package com.tdedsh.controller;

import com.tdedsh.dto.CustomResponse;
import com.tdedsh.dto.LoginDto;
import com.tdedsh.generated.tables.records.UsersRecord;
import com.tdedsh.util.TokenUtil;
import io.javalin.http.Context;
import io.javalin.http.Cookie;
import org.jooq.DSLContext;

import static com.tdedsh.generated.tables.Users.USERS;

public class AuthController {
    private static DSLContext db; // Assume this is initialized elsewhere (e.g., in a DatabaseConfig class)
    // Inject the DSLContext (dependency injection)
    public static void setDb(DSLContext db) {
        AuthController.db = db;
    }
    public static void loginUser(Context ctx) {
        LoginDto loginDto = ctx.bodyAsClass(LoginDto.class);
        if(loginDto.getUserName()==null||loginDto.getUserName().isBlank())throw new RuntimeException("User name is required");
        if(loginDto.getPassword()==null||loginDto.getPassword().isBlank())throw new RuntimeException("Password is required");
        var user = db.selectFrom(USERS)
                .where(USERS.EMAIL.eq(loginDto.getUserName()))
                .fetchOneInto(UsersRecord.class);
        if(user==null)throw new RuntimeException("User not found");
        if (!user.getPasswordHash().equals(loginDto.getPassword())) throw new RuntimeException("Password is not correct");
        String token = TokenUtil.generateToken(loginDto.getUserName());
        ctx.cookie(new Cookie("jwt",token,"/*",86400,false,1,true)); // 86400 seconds = 24 hours
        ctx.json(new CustomResponse(200,null,"Login success"));
    }

    public static void logoutUser(Context ctx){
        ctx.removeCookie("jwt");
        ctx.status(200).json("Logged out");
    }

}
