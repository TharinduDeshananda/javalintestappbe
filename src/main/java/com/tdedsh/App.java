package com.tdedsh;

import com.tdedsh.controller.TaskController;
import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.*;
import com.tdedsh.controller.UserController;
import io.javalin.apibuilder.ApiBuilder;
import org.jooq.DSLContext;

public class App {
    public static void main(String[] args) {

        // Initialize the database connection
        DSLContext db = DatabaseConfig.create();

        // Inject the DSLContext into controllers
        UserController.setDb(db);
        TaskController.setDb(db);

        var app = Javalin.create(config -> {
            config.useVirtualThreads = true;
            config.http.asyncTimeout = 10_000L;
            // Configure routes using apiBuilder
            config.router.apiBuilder(App::addRoutes);
        }).start(8080);

    }

    private static void addRoutes() {
        // Add user routes
        var userRoutes = Routes.userRoutes();
        var taskRoutes  = Routes.taskRoutes();
        userRoutes.addEndpoints();
        taskRoutes.addEndpoints();
    }
}
