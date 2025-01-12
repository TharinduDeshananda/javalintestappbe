package com.tdedsh;

import com.tdedsh.controller.AuthController;
import com.tdedsh.controller.TaskController;
import com.tdedsh.controller.UserController;
import static io.javalin.apibuilder.ApiBuilder.*;
import io.javalin.apibuilder.EndpointGroup;

public class Routes {
    public static EndpointGroup userRoutes() {
        return () -> path("/users", () -> {
            get(UserController::getAll);
            post(UserController::create);
            path("/{userId}", () -> {
                get(UserController::getOne);
                patch(UserController::update);
                delete(UserController::delete);
            });
        });
    }

    public static EndpointGroup taskRoutes() {
        return () -> path("/tasks", () -> {
            get(TaskController::getAllTasksFiltered);
            post(TaskController::createTask);
            patch(TaskController::updateTask);
            path("/{taskId}", () -> {
                get(TaskController::getSingleTask);
                delete(TaskController::removeTask); // Fixed: Use delete instead of get for removeTask
            });
        });
    }
    public static EndpointGroup authRoutes() {
        return () -> path("/auth", () -> {
            post("/new", AuthController::create);
            post("/login", AuthController::loginUser);
            post("/logout",AuthController::logoutUser);
        });
    }

}
