package com.tdedsh.util;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthMiddleware implements Handler {
    private static final Logger log = LoggerFactory.getLogger(AuthMiddleware.class);
    @Override
    public void handle(Context ctx) {
        log.info("Authmiddleware check");
        String token = ctx.cookie("jwt");

        if (token != null) {
            log.info("Token found");
            try {
                Claims claims = TokenUtil.validateToken(token);
                ctx.attribute("userEmail", claims.getSubject()); // Attach user email to the context
            } catch (Exception e) {
                ctx.status(401).json("Unauthorized");
            }
        } else {
            log.info("Token not found");
            ctx.status(401).json("Unauthorized");
        }
    }
}
