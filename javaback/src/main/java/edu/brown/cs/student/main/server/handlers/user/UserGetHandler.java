package edu.brown.cs.student.main.server.handlers.user;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import edu.brown.cs.student.main.server.SerializeHelper;
import edu.brown.cs.student.main.server.States;
import java.util.List;
import java.util.concurrent.ExecutionException;
import spark.Request;
import spark.Response;
import spark.Route;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class UserGetHandler implements Route {
    private final Firestore db;

    /**
     * LoadHandler constructor.
     *
     * @param states -  a class that keeps track of shared variables.
     */
    public UserGetHandler(States states) {
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
        // get params
        ApiFuture<QuerySnapshot> querySnapshot = null;

        String email = request.queryParams("email");
        CollectionReference users = db.collection("users");
        Query query = users.whereEqualTo("email", email);
        querySnapshot = query.get();

        // if there are no users with the given email, return a failure response
        String res = this.checkEmpty(querySnapshot.get().getDocuments());
        if (res != null) {
            return res;
        }

        // otherwise, return a success res w the user data
        response.status(200);
        return this.getSerializedSuccess("success", querySnapshot.get().getDocuments().get(0).getData());
    }

    public <T> String checkEmpty(List<T> docs) {
        if (docs.isEmpty()) {
            return this.getSerializedFailure("error", "user with given email does not exist!");
        }
        return null;
    }

    public String getSerializedSuccess(String status, Map<String, Object> data) {
        return new GetUserSuccessResponse(status, data).serialize();
    }

    public String getSerializedFailure(String status, String errorMessage) {
        return new GetUserFailureResponse(status, errorMessage).serialize();
    }

    /**
     * Success response for loading. Serializes the result ("success") and the filepath of file loaded.
     */
    public record GetUserSuccessResponse(String status, Map<String, Object> userData) {

        /**
         * @return this response, serialized as Json
         */
        public String serialize() {
            LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
            responseMap.put("status", "success");

            HashMap<String, Map<String, Object>> dataMap = new HashMap<>();
            dataMap.put("user", this.userData);
            responseMap.put("data", dataMap);
            return SerializeHelper.helpSerialize(responseMap);
        }
    }


    /**
     * Failure response for loading. Serializes the error type and the error message.
     */
    public record GetUserFailureResponse(String status, String error_message) {

        /**
         * @return this response, serialized as Json
         */
        public String serialize() {
            LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
            responseMap.put("status", "error");
            responseMap.put("error_message", this.error_message);
            return SerializeHelper.helpSerialize(responseMap);
        }
    }
}
