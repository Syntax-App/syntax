package edu.brown.cs.student.main.server.handlers.user;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import edu.brown.cs.student.main.server.SerializeHelper;
import edu.brown.cs.student.main.server.States;
import edu.brown.cs.student.main.server.types.User;
import edu.brown.cs.student.main.server.types.UserStats;
import spark.Request;
import spark.Response;
import spark.Route;
import java.util.*;

public class UserRankHandler implements Route {
    private final Firestore db;

    /**
     * LoadHandler constructor.
     *
     * @param states -  a class that keeps track of shared variables.
     */
    public UserRankHandler(States states) {
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
        try {
            List<User> userList = new ArrayList<>();

            // retrieve all users
            ApiFuture<QuerySnapshot> future = this.db.collection("users").get();
            List<QueryDocumentSnapshot> users = future.get().getDocuments();

            // case where there are no users returned
            if (users.isEmpty()) {
                return new RankFailureResponse("error", "there are no users in the database").serialize();
            }

            // convert all users to User class
            for (QueryDocumentSnapshot userDoc : users) {
                User user = userDoc.toObject(User.class);
                userList.add(user);
            }

            // sort users based on their highlpm
            userList.sort((u1, u2) -> Double.compare(u2.getStats().getHighlpm(), u1.getStats().getHighlpm()));

            return new RankSuccessResponse("success", userList).serialize();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return new RankFailureResponse("error", "YUH").serialize();
    }

    public record RankSuccessResponse(String status, List<User> users) {
        /**
         * @return this response, serialized as Json
         */
        public String serialize() {
            LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
            responseMap.put("status", "success");
            HashMap<String, List<User>> dataMap = new HashMap<>();
            dataMap.put("ranking", this.users);
            responseMap.put("data", dataMap);
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