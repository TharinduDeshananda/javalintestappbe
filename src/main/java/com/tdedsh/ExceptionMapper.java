package com.tdedsh;

import com.tdedsh.dto.CustomException;
import com.tdedsh.dto.CustomResponse;
import io.javalin.Javalin;
import org.jooq.exception.IntegrityConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionMapper {
    private static final Logger log = LoggerFactory.getLogger(ExceptionMapper.class);
    public static void mapExceptions(Javalin app){

        app.exception(CustomException.class,(e, ctx)->{
            log.error(e.toString());
            ctx.status(e.getStatus()).json(new CustomResponse(e.getStatus(),null,e.getMessage()));
        });

        app.exception(IntegrityConstraintViolationException.class,(e, ctx)->{
            log.error(e.toString());
            ctx.status(500).json(new CustomResponse(400,null,mapErrorToMessage(e)));
        });

        app.exception(Exception.class,(e,ctx)->{
            log.error(e.toString());
            ctx.status(500).json(new CustomResponse(500,null,"Internal Server Error"));
        });

    }

    // Utility function to map exception details to a user-friendly message
    private static String mapErrorToMessage(Throwable e) {
        String message = e.getMessage();
        if (message.contains("Duplicate entry")) {
            return "A duplicate value was found. Please ensure unique values for the specified fields.";
        } else if (message.contains("foreign key constraint")) {
            return "Invalid reference. Please ensure the related data exists.";
        } else if (message.contains("cannot be null")) {
            return "A required field is missing. Please provide all necessary information.";
        }
        // Generic message for unrecognized cases
        return "A database constraint was violated. Please ensure the provided data is valid.";
    }
}
