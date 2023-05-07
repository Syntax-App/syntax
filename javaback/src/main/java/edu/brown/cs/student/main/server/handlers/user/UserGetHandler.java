package edu.brown.cs.student.main.server.handlers.user;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import edu.brown.cs.student.main.server.SerializeHelper;
import edu.brown.cs.student.main.server.States;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import edu.brown.cs.student.main.server.types.User;
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
    public Object handle(Request request, Response response) {
        try {
            // get user based on email
            ApiFuture<QuerySnapshot> querySnapshot;
            String email = request.queryParams("email");

            if (email == null) {
                return this.getSerializedFailure("No email parameter was provided!");
            }

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
            return this.getSerializedSuccess(querySnapshot.get().getDocuments().get(0).toObject(User.class));
        } catch (ExecutionException | InterruptedException e) {
            return this.getSerializedFailure("Error while communicating with Firestore: " + e.getMessage());
        }
    }

    /**
     * Helper method to check if a user with specified email exists
     */
    public <T> String checkEmpty(List<T> docs) {
        if (docs.isEmpty()) {
            return this.getSerializedFailure("User with given email does not exist!");
        }
        return null;
    }

    /**
     * Helper method to serialize success response
     */
    public String getSerializedSuccess(User user) {
        Map<String, User> dataMap = new HashMap<>();
        dataMap.put("user", user);
        return new GetUserSuccessResponse("success", dataMap).serialize();
    }

    /**
     * Helper method to serialize failure response
     */
    public String getSerializedFailure(String errorMessage) {
        return new GetUserFailureResponse("error", errorMessage).serialize();
    }

    /**
     * Success response for getting user data. Serializes the result ("success") and the user data.
     */
    public record GetUserSuccessResponse(String status, Map<String, User> data) {

        /**
         * @return this response, serialized as Json
         */
        public String serialize() {
            LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
            responseMap.put("status", "success");
            responseMap.put("data", data);
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
