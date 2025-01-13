package com.tdedsh;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tdedsh.controller.AuthController;
import com.tdedsh.controller.TaskController;
import com.tdedsh.controller.UserController;
import com.tdedsh.util.AuthMiddleware;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.servlet.JavalinServletContext;
import io.javalin.json.JavalinJackson;
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

            //object mapper config
            configObjectMapper(config);

            // CORS config
            configureCors(config);


            // Configure routes using apiBuilder
            config.router.apiBuilder(App::addRoutes);
        });

        //map exceptions
        ExceptionMapper.mapExceptions(app);

        log.info("Starting server in 8080");
        app.start(8080);
    }


    //configure routes
    private static void addRoutes() {

        // Add a global before handler to set CORS headers
        before(ctx -> {
            ctx.header("Access-Control-Allow-Origin", "http://localhost:5173");
            ctx.header("Access-Control-Allow-Credentials", "true");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");

            // Handle preflight requests
            if (ctx.method().name().equals("OPTIONS")) {
                ctx.status(200).result("OK");
                ((JavalinServletContext) ctx).getTasks().clear();
            }
        });

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

    //configure cors
    private static void configureCors(JavalinConfig config) {
        config.bundledPlugins.enableCors(cors -> {
            cors.addRule(it -> {
                it.allowHost("http://localhost:5173");
                it.allowHost("localhost:5173");
                it.allowCredentials = true; // Allow credentials
//                    it.allowedHeaders = List.of("Content-Type", "Authorization"); // Allowed headers
//                    it.allowedMethods = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"); // Allowed methods

            });
        });
    }

    // This specifies how object mapping should work
    private static void configObjectMapper(JavalinConfig config) {
        config.jsonMapper(new JavalinJackson().updateMapper(mapper -> {
            // java date time api
            mapper.registerModule(new JavaTimeModule());

            // Disable writing dates as timestamps (serialize as strings instead)
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            // Ignore unknown properties in JSON
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // Allow null values for primitive fields (e.g., int, boolean)
            mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

            // Exclude null fields during serialization
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }));
    }
}
