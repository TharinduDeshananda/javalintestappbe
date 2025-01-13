package com.tdedsh.util;

import com.tdedsh.dto.CustomResponse;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.servlet.JavalinServletContext;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthMiddleware implements Handler {
    private static final Logger log = LoggerFactory.getLogger(AuthMiddleware.class);

    @Override
    public void handle(Context ctx) {
        log.info("AuthMiddleware check");
        String token = ctx.cookie("jwt");

        if (token != null) {
            log.info("Token found");
            try {
                Claims claims = TokenUtil.validateToken(token);
                ctx.attribute("userEmail", claims.getSubject()); // Attach user email to the context
                ctx.attribute("userId",claims.get("userId"));
            } catch (Exception e) {
                log.warn("Invalid token", e);
                ctx.status(401).json(new CustomResponse(401,null,"Unauthorized"));
                ((JavalinServletContext)ctx).getTasks().clear(); // skip any remaining tasks for this request
                return; // Terminate the request
            }
        } else {
            log.info("Token not found");
            ctx.status(401).json(new CustomResponse(401,null,"Unauthorized"));
            ((JavalinServletContext)ctx).getTasks().clear();
            return; // Terminate the request
        }
    }
}

