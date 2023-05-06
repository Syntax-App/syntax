package edu.brown.cs.student.main.server.handlers.race;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import edu.brown.cs.student.main.algo.graph.Graph;
import edu.brown.cs.student.main.algo.snippets.GPTProxyCache;
import edu.brown.cs.student.main.algo.snippets.Snippets.Snippet;
import edu.brown.cs.student.main.algo.snippets.Snippets.SnippetsJSON;
import edu.brown.cs.student.main.server.SerializeHelper;
import edu.brown.cs.student.main.server.States;
import edu.brown.cs.student.main.server.config.APIKeys;
import edu.brown.cs.student.main.server.types.User;
import edu.brown.cs.student.main.server.utils.JSONUtils;
import io.reactivex.Flowable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
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
    private final StringBuilder completionString;
    private GPTProxyCache cache;
    private SnippetsJSON json;

    /**
     * LoadHandler constructor.
     *
     * @param states -  a class that keeps track of shared variables.
     */
    public StartHandler(States states) {
        this.db = states.getDb();
        this.snippetStack = states.getSnippetStacks();
//        try {
//            JSONUtils jsonUtils = new JSONUtils();
//            File snippetsFile = new File(
//                "src/main/java/edu/brown/cs/student/main/algo/snippets/JavaSnippets.json");
//            Reader reader = new FileReader(snippetsFile);
//            String snippetsString = jsonUtils.readerToString(reader);
//
//            this.json = jsonUtils.fromJson(SnippetsJSON.class, snippetsString);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }

        this.completionString = new StringBuilder();
        this.cache = new GPTProxyCache(100, 60, TimeUnit.MINUTES, this.json, this.completionString);
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

            // if user is not logged in
            if (email == null) {
                if (lang == null) lang = "";
                Graph graph = new Graph(lang);
                this.json = graph.getJson();
                int randID = graph.getAvailableIDs()
                    .get(new Random().nextInt(graph.getAvailableIDs().size()));
                String snippet = this.json.array()[randID].text();

                String explanation = this.cache.getExplanation(snippet);
                return new StartSuccessResponse("success", snippet,
                    explanation).serialize();
            }

            // get user with the email
            CollectionReference users = this.db.collection("users");
            Query query = users.whereEqualTo("email", email);
            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            User currUser = querySnapshot.get().getDocuments().get(0).toObject(User.class);

            // get exp stat
            double userExperience = currUser.getStats().getExp();
            int snippetId;

            if (this.snippetStack.containsKey(email)) {
                if (this.snippetStack.get(email).size() > 1) {
                    snippetId = this.snippetStack.get(email).pop();
                } else {
                    snippetId = this.snippetStack.get(email).pop();
                    Graph graph = new Graph(lang);
                    graph.constructGraph(userExperience);
                    List<Integer> snippetIDs = graph.findPath(graph.getHead());
                    LinkedList<Integer> linkedSnippetIDs = new LinkedList<>(snippetIDs);
                    this.snippetStack.put(email, linkedSnippetIDs);
                }

            } else {
                Graph graph = new Graph(lang);
                graph.constructGraph(userExperience);
                List<Integer> snippetIDs = graph.findPath(graph.getHead());
                LinkedList<Integer> linkedSnippetIDs = new LinkedList<>(snippetIDs);
                snippetId = linkedSnippetIDs.pop();
                this.snippetStack.put(email, linkedSnippetIDs);
            }

            String snippetContent = this.json.array()[snippetId].text();
            String explanation = this.cache.getExplanation(snippetContent);


            return new StartSuccessResponse("success", snippetContent, explanation).serialize();
        } catch (ExecutionException | InterruptedException e) {
            return new StartFailureResponse("error", "Snippet file not found!").serialize();
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
