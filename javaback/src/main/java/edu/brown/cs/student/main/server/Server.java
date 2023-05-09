package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.server.handlers.race.EndHandler;
import edu.brown.cs.student.main.server.handlers.race.StartHandler;
import edu.brown.cs.student.main.server.handlers.user.UserCreateHandler;
import edu.brown.cs.student.main.server.handlers.user.UserGetHandler;
import edu.brown.cs.student.main.server.handlers.user.UserRankHandler;
import spark.Spark;

import static spark.Spark.*;


/**
 * Top-level class for this Server.
 *
 * Initializes the Server's endpoints.
 * The first three endpoints have shared states, which hold the CSV data and its header.
 */
public class Server {
    public static void main(String[] args) {
        Spark.port(4000);

        // credit: https://stackoverflow.com/questions/29114667/cross-origin-communication-between-nginx-and-spark-java
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

        // User routes!
        Spark.get("user/get", new UserGetHandler(states));
        Spark.post("user/create", new UserCreateHandler(states));
        Spark.get("user/ranking", new UserRankHandler(states));

        // Race routes!
        Spark.get("race/start", new StartHandler(states));
        Spark.post("race/end", new EndHandler(states));

        Spark.init();
        Spark.awaitInitialization();
        System.out.println("Server started at http://localhost:" + Spark.port());
    }
}
