package edu.brown.cs.student.main.server.handlers.race;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.remoteconfig.Parameter;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.SerializeHelper;
import edu.brown.cs.student.main.server.States;
import edu.brown.cs.student.main.server.handlers.race.StartHandler.StartFailureResponse;
import edu.brown.cs.student.main.server.handlers.race.StartHandler.StartSuccessResponse;
import edu.brown.cs.student.main.server.handlers.user.UserGetHandler.GetUserFailureResponse;
import edu.brown.cs.student.main.server.utils.JSONUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import spark.Request;
import spark.Response;
import spark.Route;

public class EndHandler implements Route {
    private Firestore db;

    /**
     * LoadHandler constructor.
     *
     * @param states -  a class that keeps track of shared variables.
     */
    public EndHandler(States states) {
        this.db = states.getDb();
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
    public Object handle(Request request, Response response)
        throws ExecutionException, InterruptedException, IOException {
        //String email = request.queryParams("email");
        CollectionReference users = db.collection("users");
        String reqBody = request.body();
        JSONUtils jsonUtils = new JSONUtils();

//        Type statsType = Types.newParameterizedType(Map.class, String.class, Integer.class);
//        Type bodyType = Types.newParameterizedType(Map.class, String.class, Object.class);
        Map<String, Object> bodyParams = jsonUtils.fromJson(reqBody);
        String email = (String) bodyParams.get("email"); // TODO - change this with actual Moshi stuff
        Map<String, Integer> runStats = (Map<String, Integer>) bodyParams.get("run_stats");

        Query query = users.whereEqualTo("email", email);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        if (querySnapshot.get().getDocuments().isEmpty()) {
            return new GetUserFailureResponse("error", "User with given email does not exist!").serialize();
        }

        Map<String, Object> userObject = querySnapshot.get().getDocuments().get(0).getData();
        Map<String, Integer> userStats = (Map<String, Integer>) userObject.get("stats");

        if (runStats.get("lpm") > userStats.get("highlpm")) {
            userStats.put("highlpm", runStats.get("lpm"));
        }
        if (runStats.get("acc") > userStats.get("highacc")) {
            userStats.put("highacc", runStats.get("acc"));
        }

        return new GetUserFailureResponse("error", "Not yet implemented!");
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
