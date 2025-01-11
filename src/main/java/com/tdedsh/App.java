package com.tdedsh;

import com.tdedsh.controller.AuthController;
import com.tdedsh.controller.TaskController;
import com.tdedsh.controller.UserController;
import com.tdedsh.util.AuthMiddleware;
import io.javalin.Javalin;
import org.jooq.DSLContext;

import static io.javalin.apibuilder.ApiBuilder.before;

public class App {
    public static void main(String[] args) {

        // Initialize the database connection
        DSLContext db = DatabaseConfig.create();

        // Inject the DSLContext into controllers
        UserController.setDb(db);
        TaskController.setDb(db);
        AuthController.setDb(db);
        var app = Javalin.create(config -> {
            config.useVirtualThreads = true;
            config.http.asyncTimeout = 10_000L;
            // Configure routes using apiBuilder
            config.router.apiBuilder(App::addRoutes);
        });

        app.start(8080);
    }

    private static void addRoutes() {

        var authMiddleware = new AuthMiddleware();
        before("/tasks",authMiddleware);
        before("/users",authMiddleware);

        // Add user routes
        var userRoutes = Routes.userRoutes();
        var taskRoutes  = Routes.taskRoutes();
        var authRoutes = Routes.authRoutes();


        authRoutes.addEndpoints();
        userRoutes.addEndpoints();
        taskRoutes.addEndpoints();


    }
}
