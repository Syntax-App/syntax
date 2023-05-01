package edu.brown.cs.student.main.server.handlers.user;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import edu.brown.cs.student.main.server.SerializeHelper;
import edu.brown.cs.student.main.server.States;
import edu.brown.cs.student.main.server.utils.JSONUtils;
import java.io.IOException;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.*;

/**
 * Handler class for loading a CSV file.
 */
public class UserCreateHandler implements Route {
    private Firestore db;

    /**
     * LoadHandler constructor.
     *
     * @param states -  a class that keeps track of shared variables.
     */
    public UserCreateHandler(States states) {
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
    public Object handle(Request request, Response response) throws Exception {
        CollectionReference users = db.collection("users");
        String reqBody = request.body();
        Map<String, Object> bodyParams = this.getBodyParams(reqBody);

        Query query = users.whereEqualTo("email", bodyParams.get("email"));
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        if (!querySnapshot.get().getDocuments().isEmpty()) {
            return new CreateFailureResponse("error", "User with given email already exists!", querySnapshot.get().getDocuments().get(0).getData()).serialize();
        }

//        HashMap<String, Object> userObject = new HashMap<>();
//        userObject.put("uuid", UUID.randomUUID().toString());
//        userObject.put("name", bodyParams.get("name"));
//        userObject.put("email", bodyParams.get("email"));
//        userObject.put("pic", bodyParams.get("pic"));
//
//        HashMap<String, Integer> stats = new HashMap<>();
//        stats.put("highlpm", 0);
//        stats.put("highacc", 0);
//        stats.put("avgacc", 0);
//        stats.put("avglpm", 0);
//        stats.put("numraces", 0);
//        userObject.put("stats", stats);
        Map<String, Object> userObject = this.createUserObject(bodyParams);

        users.add(userObject);

        return new CreateSuccessResponse("success", userObject).serialize();
    }

    public Map<String, Object> createUserObject(Map<String, Object> bodyParams) {
        HashMap<String, Object> userObject = new HashMap<>();
        userObject.put("uuid", UUID.randomUUID().toString());
        userObject.put("name", bodyParams.get("name"));
        userObject.put("email", bodyParams.get("email"));
        userObject.put("pic", bodyParams.get("pic"));

        HashMap<String, Integer> stats = new HashMap<>();
        stats.put("highlpm", 0);
        stats.put("highacc", 0);
        stats.put("avgacc", 0);
        stats.put("avglpm", 0);
        stats.put("numraces", 0);
        stats.put("exp", 0);
        userObject.put("stats", stats);

        return userObject;
    }

    public Map<String, Object> getBodyParams(String reqBody) throws IOException {
        JSONUtils jsonUtils = new JSONUtils();
        return jsonUtils.fromJson(reqBody);
    }

    /**
     * Success response for loading. Serializes the result ("success") and the filepath of file loaded.
     *
     * @param result   success result message
     * @param filepath filepath of file loaded
     */
    public record CreateSuccessResponse(String status, Map<String, Object> data) {
        /**
         * @return this response, serialized as Json
         */
        public String serialize() {
            LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
            responseMap.put("status", "success");
            HashMap<String, Map<String, Object>> dataMap = new HashMap<>();
            dataMap.put("user", data);
            responseMap.put("data", dataMap);
            return SerializeHelper.helpSerialize(responseMap);
        }
    }


    /**
     * Failure response for loading. Serializes the error type and the error message.
     *
     * @param result        error type
     * @param error_message error message to display
     */
    public record CreateFailureResponse(String status, String error_message, Map<String, Object> data) {

        /**
         * @return this response, serialized as Json
         */
        public String serialize() {
            LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
            responseMap.put("status", "error");
            responseMap.put("error_message", error_message());
            HashMap<String, Map<String, Object>> dataMap = new HashMap<>();
            dataMap.put("user", data);
            responseMap.put("data", dataMap);
            return SerializeHelper.helpSerialize(responseMap);
        }
    }

}
