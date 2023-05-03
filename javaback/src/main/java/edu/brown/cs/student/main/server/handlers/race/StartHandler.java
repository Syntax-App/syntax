package edu.brown.cs.student.main.server.handlers.race;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import edu.brown.cs.student.main.algo.graph.Graph;
import edu.brown.cs.student.main.algo.snippets.Snippets.SnippetsJSON;
import edu.brown.cs.student.main.server.SerializeHelper;
import edu.brown.cs.student.main.server.States;
import edu.brown.cs.student.main.server.handlers.user.UserGetHandler;
import edu.brown.cs.student.main.server.utils.JSONUtils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Handler class for loading a CSV file.
 */
public class StartHandler implements Route {
    private Firestore db;
    private Map<String, LinkedList<Integer>> snippetStack;

    /**
     * LoadHandler constructor.
     *
     * @param states -  a class that keeps track of shared variables.
     */
    public StartHandler(States states) {
        this.db = states.getDb();
        this.snippetStack = states.getSnippetStacks();
    }

    /**
     * Uses filepath and hasHeader params to parse a CSV and set the active file variables.
     *
     * @param request  the request to handle
     * @param response used to modify properties of the response
     * @return response content
     * @throws Exception part of interface
     */
    @Override
    public Object handle(Request request, Response response) {
        // this is where we will call our algorithm!
        try {
            //String email = request.queryParams("email");
//            CollectionReference users = db.collection("users");
//            Query query = users.whereEqualTo("email", email);
//            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            //double userExperience =  querySnapshot.get().getDocuments().get(0).getData().get("exp");

            double userExperience = 2.0;
            String email = "daniel_liu2@brown.edu";
            int snippetId;

            if (this.snippetStack.containsKey(email)) {
                if (!snippetStack.isEmpty()) {
                    snippetId = this.snippetStack.get(email).pop();
                } else {
                    Graph graph = new Graph();
                    System.out.println("hello");
                    graph.constructGraph(userExperience);
                    System.out.println("hellllloooo");
                    List<Integer> snippetIDs = graph.findPath(graph.getHead());

                    System.out.println("hello!");

                    LinkedList<Integer> linkedSnippetIDs = new LinkedList<>(snippetIDs);
                    snippetId = linkedSnippetIDs.pop();
                    this.snippetStack.put(email, linkedSnippetIDs);
                }

            } else {
                Graph graph = new Graph();
                System.out.println("hello");
                graph.constructGraph(userExperience);
                System.out.println("hellllloooo");
                List<Integer> snippetIDs = graph.findPath(graph.getHead());

                System.out.println("hello!");

                LinkedList<Integer> linkedSnippetIDs = new LinkedList<>(snippetIDs);
                snippetId = linkedSnippetIDs.pop();
                this.snippetStack.put(email, linkedSnippetIDs);
            }

            JSONUtils jsonUtils = new JSONUtils();
            File snippetsFile = new File("src/main/java/edu/brown/cs/student/main/algo/snippets/JavaSnippets.json");
            Reader reader = new FileReader(snippetsFile);
            String snippetsString = jsonUtils.readerToString(reader);
            System.out.println(snippetsString);

            SnippetsJSON json = jsonUtils.fromJson(SnippetsJSON.class, snippetsString);

            String snippetContent = json.array()[snippetId].text();

            //String snippet = Files.readString(Path.of("src/main/java/edu/brown/cs/student/main/syntax-algo/ReactFlightClient.txt"));
            return new StartSuccessResponse("success", snippetContent).serialize();
        } catch (Exception e) {
            return new StartFailureResponse("error", e.getMessage()).serialize();
        }
//        } catch (FileNotFoundException e) {
//            return new StartFailureResponse("error", "Snippet file not found!").serialize();
//        } catch (IOException e) {
//            return new StartFailureResponse("error", "Could not open snippet file").serialize();
//        }
//        } catch (ExecutionException e) {
//            return new StartFailureResponse("error", "Snippet file not found!").serialize();
//        } catch (InterruptedException e) {
//            return new StartFailureResponse("error", "Snippet file not found!").serialize();
//        }

    }


    /**
     * Success response for loading. Serializes the result ("success") and the filepath of file loaded.
     */
    public record StartSuccessResponse(String status, String snippet) {
        /**
         * @return this response, serialized as Json
         */
        public String serialize() {
            LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
            responseMap.put("status", "success");
            HashMap<String, String> dataMap = new HashMap<>();
            dataMap.put("snippet", snippet);
            responseMap.put("data", dataMap);
            return SerializeHelper.helpSerialize(responseMap);
        }
    }


    /**
     * Failure response for loading. Serializes the error type and the error message.
     */
    public record StartFailureResponse(String status, String error_message) {

        /**
         * @return this response, serialized as Json
         */
        public String serialize() {
            LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
            responseMap.put("status", "error");
            responseMap.put("error_message", error_message());
            return SerializeHelper.helpSerialize(responseMap);
        }
    }

}
