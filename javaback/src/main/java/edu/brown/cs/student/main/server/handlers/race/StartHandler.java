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
import edu.brown.cs.student.main.server.types.User;
import edu.brown.cs.student.main.server.utils.JSONUtils;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
            // get email param
            String email = request.queryParams("email");
            String lang = request.queryParams("lang");

            JSONUtils jsonUtils = new JSONUtils();
            File snippetsFile = new File(
                "src/main/java/edu/brown/cs/student/main/algo/snippets/JavaSnippets.json");
            Reader reader = new FileReader(snippetsFile);
            String snippetsString = jsonUtils.readerToString(reader);

            SnippetsJSON json = jsonUtils.fromJson(SnippetsJSON.class, snippetsString);

            // if user is not logged in
            if (email == null) {
                Graph graph = new Graph();
                int randID = graph.getAvailableIDs()
                    .get(new Random().nextInt(graph.getAvailableIDs().size()));
                return new StartSuccessResponse("success", json.array()[randID].text(),
                    "code explanation here").serialize();
            }

            // get user with the email
            CollectionReference users = this.db.collection("users");
            Query query = users.whereEqualTo("email", email);
            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            User currUser = querySnapshot.get().getDocuments().get(0).toObject(User.class);

            // get exp stat
            double userExperience = currUser.getStats().getExp();

//            double userExperience = 2.0;
//            String email = "daniel_liu2@brown.edu";

            int snippetId;

            if (this.snippetStack.containsKey(email)) {
                if (this.snippetStack.get(email).size() > 1) {
                    snippetId = this.snippetStack.get(email).pop();
                } else {
                    snippetId = this.snippetStack.get(email).pop();
                    Graph graph = new Graph();
                    graph.constructGraph(userExperience);
                    List<Integer> snippetIDs = graph.findPath(graph.getHead());
                    LinkedList<Integer> linkedSnippetIDs = new LinkedList<>(snippetIDs);
                    this.snippetStack.put(email, linkedSnippetIDs);

//                    if (!this.snippetStack.isEmpty()) {
//
//                    } else {
//                        LinkedList<Integer> linkedSnippetIDs = new LinkedList<>(snippetIDs);
//                        this.snippetStack.put(email, linkedSnippetIDs);
//                    }
//                    Graph graph = new Graph();
//                    System.out.println("hello");
//                    graph.constructGraph(userExperience);
//                    System.out.println("hellllloooo");
//                    List<Integer> snippetIDs = graph.findPath(graph.getHead());
//
//                    System.out.println("hello!");
//
//                    LinkedList<Integer> linkedSnippetIDs = new LinkedList<>(snippetIDs);
//                    snippetId = linkedSnippetIDs.pop();
//                    this.snippetStack.put(email, linkedSnippetIDs);
                }

            } else {
                Graph graph = new Graph();
                graph.constructGraph(userExperience);
                List<Integer> snippetIDs = graph.findPath(graph.getHead());
                LinkedList<Integer> linkedSnippetIDs = new LinkedList<>(snippetIDs);
                snippetId = linkedSnippetIDs.pop();
                this.snippetStack.put(email, linkedSnippetIDs);
            }

            String snippetContent = json.array()[snippetId].text();

//            String url = "https://api.openai.com/v1/chat/completions";
//            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setRequestProperty("Authorization", "Bearer INSERT KEY HERE");
//            Map<String, Object> data = jsonUtils.getParsedJSON(reader);
//            data.put("model", "gpt-3.5-turbo");
//            data.put("prompt", "Explain this code snippet: \n" + snippetContent);
//            data.put("max_tokens", 100);
//            data.put("temperature", 1.0);
//
//            connection.setDoOutput(true);
//            connection.getOutputStream().write(data.toString().getBytes());
//            String explanation = new BufferedReader(new InputStreamReader(connection.getInputStream())).lines()
//                .reduce((a, b) -> a + b).get();
            String explanation = "code explanation goes here";

            //String snippet = Files.readString(Path.of("src/main/java/edu/brown/cs/student/main/syntax-algo/ReactFlightClient.txt"));
            return new StartSuccessResponse("success", snippetContent, explanation).serialize();
//        } catch (Exception e) {
//            return new StartFailureResponse("error", e.getMessage()).serialize();
//        }
        } catch (FileNotFoundException | ExecutionException | InterruptedException e) {
            return new StartFailureResponse("error", "Snippet file not found!").serialize();
        } catch (IOException e) {
            return new StartFailureResponse("error", "Could not open snippet file").serialize();
        } catch (IndexOutOfBoundsException e) {
            return new StartFailureResponse("error", "User not found").serialize();
        }

    }


    /**
     * Success response for loading. Serializes the result ("success") and the filepath of file loaded.
     */
    public record StartSuccessResponse(String status, String snippet, String explanation) {
        /**
         * @return this response, serialized as Json
         */
        public String serialize() {
            LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
            responseMap.put("status", "success");
            HashMap<String, String> dataMap = new HashMap<>();
            dataMap.put("snippet", this.snippet);
            dataMap.put("explanation", this.explanation);
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
            responseMap.put("error_message", this.error_message());
            return SerializeHelper.helpSerialize(responseMap);
        }
    }

}
