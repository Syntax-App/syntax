package edu.brown.cs.student.main.server.handlers.user;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import edu.brown.cs.student.main.server.SerializeHelper;
import edu.brown.cs.student.main.server.States;
import edu.brown.cs.student.main.server.types.UserTypes.User;
import edu.brown.cs.student.main.server.types.UserTypes.UserStats;
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
       // ApiFuture<WriteResult> future = db.collection("cities").document("LA").set(stats);
        CollectionReference users = db.collection("users");
        String reqBody = request.body();
        //Map<String, Object> bodyParams = this.getBodyParams(reqBody);
        JSONUtils jsonUtils = new JSONUtils();
        User temp_user = jsonUtils.fromJson(User.class, reqBody);
        User user = createUserRecord(temp_user.name(), temp_user.email(), temp_user.pic());

        Query query = users.whereEqualTo("email", user.email());
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        if (!querySnapshot.get().getDocuments().isEmpty()) {
            return new CreateFailureResponse("error", "User with given email already exists!", querySnapshot.get().getDocuments().get(0).getData()).serialize();
        }

        users.add(user);
        return new CreateSuccessResponse("success", user).serialize();
    }

    public User createUserRecord(String name, String email, String pic) {
        UserStats userStats = new UserStats(0,0,0,0,0,0);
        User user = new User(UUID.randomUUID().toString(), name, email, pic, userStats);
        return user;
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

    public record CreateSuccessResponse(String status, User data) {
        /**
         * @return this response, serialized as Json
         */
        public String serialize() {
            LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
            responseMap.put("status", "success");
            HashMap<String, User> dataMap = new HashMap<>();
            dataMap.put("user", data);
            responseMap.put("data", dataMap);
            return SerializeHelper.helpSerialize(responseMap);
        }
    }

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
