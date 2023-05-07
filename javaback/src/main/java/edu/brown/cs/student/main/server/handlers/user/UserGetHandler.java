package edu.brown.cs.student.main.server.handlers.user;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import edu.brown.cs.student.main.server.SerializeHelper;
import edu.brown.cs.student.main.server.States;
import java.util.List;
import spark.Request;
import spark.Response;
import spark.Route;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class represents the handler of the user/get endpoint
 */
public class UserGetHandler implements Route {
    private final Firestore db;

    /**
     * UserGetHandler constructor.
     * @param states -  a class that keeps track of shared variables.
     */
    public UserGetHandler(States states) {
        this.db = states.getDb();
    }

    /**
     * Retrieves user data from Firestore database
     *
     * @param request  the request to handle
     * @param response used to modify properties of the response
     * @return response content
     * @throws Exception part of interface
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        // get user based on email
        ApiFuture<QuerySnapshot> querySnapshot;
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

    /**
     * Helper method to check if a user with specified email exists
     */
    public <T> String checkEmpty(List<T> docs) {
        if (docs.isEmpty()) {
            return this.getSerializedFailure("error", "user with given email does not exist!");
        }
        return null;
    }

    /**
     * Helper method to serialize success response
     */
    public String getSerializedSuccess(String status, Map<String, Object> data) {
        return new GetUserSuccessResponse(status, data).serialize();
    }

    /**
     * Helper method to serialize failure response
     */
    public String getSerializedFailure(String status, String errorMessage) {
        return new GetUserFailureResponse(status, errorMessage).serialize();
    }

    /**
     * Success response for getting user data. Serializes the result ("success") and the user data.
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
     * Failure response for getting user data. Serializes the error type and the error message.
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
