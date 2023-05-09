package edu.brown.cs.student.main.server.handlers.race;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import edu.brown.cs.student.main.algo.graph.Graph;
import edu.brown.cs.student.main.algo.snippets.GPTProxyCache;
import edu.brown.cs.student.main.algo.snippets.GPTRequester;
import edu.brown.cs.student.main.algo.snippets.Snippets.SnippetsJSON;
import edu.brown.cs.student.main.server.SerializeHelper;
import edu.brown.cs.student.main.server.States;
import edu.brown.cs.student.main.server.types.User;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Handler class for race/start endpoint
 */
public class StartHandler implements Route {
    private Firestore db;
    private Map<String, Map<String, LinkedList<Integer>>> snippetStack;
    private GPTProxyCache cache;
    private SnippetsJSON json;

    /**
     * StartHandler constructor.
     * @param states -  a class that keeps track of shared variables.
     */
    public StartHandler(States states) {
        this.db = states.getDb();
        this.snippetStack = states.getSnippetStacks();

        GPTRequester gptRequester = new GPTRequester();
        // create cache to store a max of 100 GPT explanations with 24-hour expiration time
        this.cache = new GPTProxyCache(gptRequester, 100, 24, TimeUnit.HOURS);
    }

    /**
     * Uses email and language params to respond with a snippet and its explanation
     *
     * @param request  the request to handle
     * @param response used to modify properties of the response
     * @return response content
     */
    @Override
    public Object handle(Request request, Response response) {
        if (request.headers("Referer") == null) {
            return new StartFailureResponse("error", "Unauthorized").serialize();
        }
        if (!request.headers("Referer").equals("https://syntax-front.vercel.app/") && !request.headers("Host").equals("localhost:4000")) {
            return new StartFailureResponse("error", "Unauthorized").serialize();
        }
        // this is where we call our algorithm!
        try {
            // get email and language params
            String email = request.queryParams("email");
            String lang = request.queryParams("lang");

            // if user is not logged in
            if (email == null) {
                // if no language, use default graph
                if (lang == null) lang = "";
                // create new graph
                Graph graph = new Graph(lang);
                this.json = graph.getJson();
                // choose random snippet since no personalization is possible without user data
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
            if (lang == null) lang = "";
            int snippetId;

            // if user has done races before
            if (this.snippetStack.containsKey(email)) {
                // if user chooses new lang
                if (!this.snippetStack.get(email).containsKey(lang)) {
                    Graph graph = new Graph(lang);
                    this.json = graph.getJson();
                    graph.constructGraph(userExperience);
                    List<Integer> snippetIDs = graph.findPath(graph.getHead());
                    LinkedList<Integer> linkedSnippetIDs = new LinkedList<>(snippetIDs);
                    snippetId = linkedSnippetIDs.pop();
                    // create new snippet path for new lang
                    this.snippetStack.get(email).put(lang, linkedSnippetIDs);
                } else {
                    // if there's more than one snippet left for the user,
                    // pop it off the stack and use it
                    if (this.snippetStack.get(email).get(lang).size() > 1) {
                        Graph graph = new Graph(lang);
                        this.json = graph.getJson();
                        snippetId = this.snippetStack.get(email).get(lang).pop();
                    } else {
                        // if there's one snippet left, pop the last snippet
                        snippetId = this.snippetStack.get(email).get(lang).pop();
                        // construct new graph and find new path
                        Graph graph = new Graph(lang);
                        this.json = graph.getJson();
                        graph.constructGraph(userExperience);
                        List<Integer> snippetIDs = graph.findPath(graph.getHead());
                        LinkedList<Integer> linkedSnippetIDs = new LinkedList<>(snippetIDs);
                        // refresh stack with new snippet order
                        this.snippetStack.get(email).put(lang, linkedSnippetIDs);
                    }
                }
            } else {
                // if it's the user's first time ever racing
                // construct a new graph and find a new path
                Graph graph = new Graph(lang);
                this.json = graph.getJson();
                graph.constructGraph(userExperience);
                List<Integer> snippetIDs = graph.findPath(graph.getHead());
                LinkedList<Integer> linkedSnippetIDs = new LinkedList<>(snippetIDs);
                // pop a snippet off the path for use
                snippetId = linkedSnippetIDs.pop();
                // set stack with snippets
                Map<String, LinkedList<Integer>> langMap = new HashMap<>();
                langMap.put(lang, linkedSnippetIDs);
                this.snippetStack.put(email, langMap);
            }
            // get snippet text and explanation
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
     * Success response for starting a race. Serializes the result ("success"), the snippet text,
     * and the snippet explanation
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
     * Failure response for starting a race. Serializes the error type and the error message.
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
