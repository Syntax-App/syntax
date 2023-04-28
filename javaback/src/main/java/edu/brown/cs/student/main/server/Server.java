package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.server.csvHandlers.*;
import edu.brown.cs.student.main.server.handlers.race.StartHandler;
import edu.brown.cs.student.main.server.handlers.user.UserCreateHandler;
import edu.brown.cs.student.main.server.handlers.user.UserGetHandler;
import edu.brown.cs.student.main.server.weather.WeatherHandler;
import spark.Spark;

import static spark.Spark.*;


/**
 * Top-level class for this Server.
 *
 * Initializes the Server's four endpoints - loadcsv, viewcsv, searchcsv, and weather.
 * The first three endpoints have shared states, which hold the CSV data and its header.
 */
public class Server {
    public static void main(String[] args) {
        Spark.port(4000);

//        after((request, response) -> {
//            response.header("Access-Control-Allow-Origin", "*");
//            response.header("Access-Control-Allow-Methods", "*");
//            response.header("Access-Control-Allow-Headers","*");
//        });

        options("/*",
                (request, response) -> {

                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }

                    return "OK";
                });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
        });

        // initializing an instance which will hold shared states.
        States states = new States();

        // Setting up the handlers for each endpoint.
        Spark.get("loadcsv", new LoadHandler(states));
        Spark.get("viewcsv", new ViewHandler(states));
        Spark.get("searchcsv", new SearchHandler(states));
        Spark.get("weather", new WeatherHandler());

        // User routes!
        Spark.get("user/get", new UserGetHandler(states));
        Spark.post("user/create", new UserCreateHandler(states));

        // Race routes!
        Spark.get("race/start", new StartHandler(states));

        Spark.init();
        Spark.awaitInitialization();
        System.out.println("Server started at http://localhost:" + Spark.port());
    }
}
