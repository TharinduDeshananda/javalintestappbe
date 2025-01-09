package com.tdedsh;

import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.*;
import com.tdedsh.controller.UserController;
public class App {
    public static void main(String[] args) {
        var app = Javalin.create(config -> {
            config.useVirtualThreads = true;
            config.http.asyncTimeout = 10_000L;
//            config.staticFiles.add("/public");
//            config.staticFiles.enableWebjars();
            config.router.apiBuilder(() -> {
                path("/users", () -> {
                    get(UserController::getAll);
                    post(UserController::create);
                    path("/{userId}", () -> {
                        get(UserController::getOne);
                        patch(UserController::update);
                        delete(UserController::delete);
                    });
                });
            });
        }).start(8080);
    }
}
