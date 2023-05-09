package edu.brown.cs.student.main.server.handlers.user;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import edu.brown.cs.student.main.server.SerializeHelper;
import edu.brown.cs.student.main.server.States;
import edu.brown.cs.student.main.server.types.User;
import spark.Request;
import spark.Response;
import spark.Route;
import java.util.*;

/**
 * This class represents the
 */
public class UserRankHandler implements Route {
    private final Firestore db;

    /**
     * UserRankHandler constructor.
     *
     * @param states - a class that keeps track of shared variables.
     */
    public UserRankHandler(States states) {
        this.db = states.getDb();
    }

    /**
     * Ranks users based on stored data
     * @param request  the request to handle
     * @param response used to modify properties of the response
     * @return response content
     */
    @Override
    public Object handle(Request request, Response response) {
        if (request.headers("Referer") == null) {
            return this.getSerializedFailure("Unauthorized");
        }
        if (!request.headers("Referer").equals("https://syntax-front.vercel.app/") && !request.headers("Host").equals("localhost:4000")) {
            return this.getSerializedFailure("Unauthorized");
        }
        try {
            List<User> userList = new ArrayList<>();

            // retrieve all users
            ApiFuture<QuerySnapshot> future = this.db.collection("users").get();
            List<QueryDocumentSnapshot> users = future.get().getDocuments();

            // case where there are no users returned
            if (users.isEmpty()) {
                return new RankFailureResponse("error", "There are no users in the database!").serialize();
            }

            // convert all users to User class
            for (QueryDocumentSnapshot userDoc : users) {
                User user = userDoc.toObject(User.class);
                userList.add(user);
            }

            // sort users based on their highlpm
            userList.sort((u1, u2) -> Double.compare(
                    u2.getStats().getHighlpm()*u2.getStats().getAvgacc(),
                    u1.getStats().getHighlpm()*u1.getStats().getAvgacc()));

            return this.getSerializedSuccess(userList);
        } catch (Exception e) {
            return this.getSerializedFailure(e.getMessage());
        }
    }

    public String getSerializedSuccess(List<User> users) {
        HashMap<String, List<User>> dataMap = new HashMap<>();
        dataMap.put("ranking", users);
        return new RankSuccessResponse("success", dataMap).serialize();
    }

    public String getSerializedFailure(String errorMessage) {
        return new RankFailureResponse("error", errorMessage).serialize();
    }

    public record RankSuccessResponse(String status, Map<String, List<User>> data) {
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

    public record RankFailureResponse(String status, String error_message) {

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