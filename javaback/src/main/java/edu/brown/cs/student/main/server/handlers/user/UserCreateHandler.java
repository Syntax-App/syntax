package edu.brown.cs.student.main.server.handlers.user;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.squareup.moshi.JsonEncodingException;
import edu.brown.cs.student.main.server.SerializeHelper;
import edu.brown.cs.student.main.server.States;
import edu.brown.cs.student.main.server.handlers.race.StartHandler;
import edu.brown.cs.student.main.server.types.User;
import edu.brown.cs.student.main.server.types.UserStats;
import edu.brown.cs.student.main.server.utils.JSONUtils;
import java.io.IOException;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Handler class for user/create endpoint
 */
public class UserCreateHandler implements Route {
    private final Firestore db;

    /**
     * UserCreateHandler constructor.
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
    public Object handle(Request request, Response response) {
        try {
            CollectionReference users = this.db.collection("users");
            String reqBody = request.body();
            JSONUtils jsonUtils = new JSONUtils();
            User user = jsonUtils.fromJson(User.class, reqBody);

            if (user.getEmail() == null || user.getName() == null || user.getPic() == null) {
                return this.getSerializedFailure("One or more fields were not provided!", new HashMap<>());
            }


            Query query = users.whereEqualTo("email", user.getEmail());
            ApiFuture<QuerySnapshot> querySnapshot = query.get();

            if (!querySnapshot.get().getDocuments().isEmpty()) {
                return this.getSerializedFailure("User with given email already exists!", querySnapshot.get().getDocuments().get(0).getData());
            }

            // initialize new stats and ID
            user.setStats(new UserStats(0,0,0,0,0,0));
            user.setUuid(UUID.randomUUID().toString());

            // add user to database
            users.add(user);

            return this.getSerializedSuccess(user);
        } catch (ExecutionException | InterruptedException e) {
            return this.getSerializedFailure("Error while communicating with Firestore: " + e.getMessage(), new HashMap<>());
        } catch (IOException e) {
            return this.getSerializedFailure("One or more fields were not provided!", new HashMap<>());
        }

    }

    public Map<String, Object> getBodyParams(String reqBody) throws IOException {
        JSONUtils jsonUtils = new JSONUtils();
        return jsonUtils.fromJson(reqBody);
    }

    public String getSerializedSuccess(User user) {
        Map<String, User> data = new HashMap<>();
        data.put("user", user);
        return new CreateSuccessResponse("success", data).serialize();
    }

    public String getSerializedFailure(String error_message, Map<String, Object> user) {
        Map<String, Map<String, Object>> data = new HashMap<>();
        data.put("user", user);
        return new CreateFailureResponse("error", error_message, user).serialize();
    }

    public record CreateSuccessResponse(String status, Map<String, User> data) {
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

    public record CreateFailureResponse(String status, String error_message, Map<String, Object> data) {

        /**
         * @return this response, serialized as Json
         */
        public String serialize() {
            LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
            responseMap.put("status", "error");
            responseMap.put("error_message", this.error_message());
            responseMap.put("data", data);
            return SerializeHelper.helpSerialize(responseMap);
        }
    }

}
