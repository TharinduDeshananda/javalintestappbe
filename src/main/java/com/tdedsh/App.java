package com.tdedsh;

import com.tdedsh.controller.AuthController;
import com.tdedsh.controller.TaskController;
import com.tdedsh.controller.UserController;
import com.tdedsh.util.AuthMiddleware;
import io.javalin.Javalin;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.javalin.apibuilder.ApiBuilder.before;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

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
            // CORS config
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> {
                    it.allowHost("http://localhost:5173");
                    it.allowHost("localhost:5173");
                    it.allowCredentials = true; // Allow credentials
//                    it.allowedHeaders = List.of("Content-Type", "Authorization"); // Allowed headers
//                    it.allowedMethods = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"); // Allowed methods
                });
            });
            // Configure routes using apiBuilder
            config.router.apiBuilder(App::addRoutes);
        });
        ExceptionMapper.mapExceptions(app);

        log.info("Starting server in 8080");
        app.start(8080);
    }

    private static void addRoutes() {

        var authMiddleware = new AuthMiddleware();
        before("/tasks", authMiddleware);
        before("/users", authMiddleware);

        // Add user routes
        var userRoutes = Routes.userRoutes();
        var taskRoutes = Routes.taskRoutes();
        var authRoutes = Routes.authRoutes();


        authRoutes.addEndpoints();
        userRoutes.addEndpoints();
        taskRoutes.addEndpoints();


    }
}
