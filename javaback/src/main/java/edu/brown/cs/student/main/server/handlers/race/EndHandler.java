package edu.brown.cs.student.main.server.handlers.race;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import edu.brown.cs.student.main.server.SerializeHelper;
import edu.brown.cs.student.main.server.States;
import edu.brown.cs.student.main.server.handlers.user.UserGetHandler.GetUserFailureResponse;
import edu.brown.cs.student.main.server.types.NewStats;
import edu.brown.cs.student.main.server.types.User;
import edu.brown.cs.student.main.server.types.UserStats;
import edu.brown.cs.student.main.server.utils.JSONUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;
import spark.Request;
import spark.Response;
import spark.Route;

public class EndHandler implements Route {
    private final Firestore db;

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
        CollectionReference users = this.db.collection("users");
        String reqBody = request.body();
        JSONUtils jsonUtils = new JSONUtils();

        NewStats new_stats = jsonUtils.fromJson(NewStats.class, reqBody);

        Query query = users.whereEqualTo("email", new_stats.email());
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        if (querySnapshot.get().getDocuments().isEmpty()) {
            return new EndFailureResponse("error", "User with given email does not exist!").serialize();
        }

        User currUser = querySnapshot.get().getDocuments().get(0).toObject(User.class);

        UserStats curr_stats = currUser.getStats();
        int curr_highlpm = curr_stats.getHighlpm();
        double curr_highacc = curr_stats.getHighacc();

        // update if a new stat is higher
        if (new_stats.recentlpm() > curr_highlpm) {
            curr_highlpm = new_stats.recentlpm();
        }
        if (new_stats.recentacc() > curr_highacc) {
            curr_highacc = new_stats.recentacc();
        }

        // calculate new averages
        int new_numraces = curr_stats.getNumraces() + 1;
        double new_avglpm = (curr_stats.getAvglpm() * curr_stats.getNumraces() + new_stats.recentlpm()) / new_numraces;
        double new_avgacc = (curr_stats.getAvgacc() * curr_stats.getNumraces() + new_stats.recentacc()) / new_numraces;
        double new_exp = curr_stats.getExp() + .1;
        if (curr_stats.getExp() >= 10) new_exp = 10;

        UserStats updatedStats = new UserStats(curr_highlpm, curr_highacc, new_numraces, new_avglpm, new_avgacc, new_exp);

        // get document id
        DocumentReference docRef = users.document(querySnapshot.get().getDocuments().get(0).getId());

        // update user at id
        docRef.update("stats", updatedStats);

        return new EndSuccessResponse("success", updatedStats).serialize();
    }

    /**
     * Success response for loading. Serializes the result ("success") and the filepath of file loaded.
     */
    public record EndSuccessResponse(String status, UserStats stats) {
        /**
         * @return this response, serialized as Json
         */
        public String serialize() {
            LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
            responseMap.put("status", "success");
            HashMap<String, UserStats> dataMap = new HashMap<>();
            dataMap.put("updated_stats", this.stats);
            responseMap.put("data", dataMap);
            return SerializeHelper.helpSerialize(responseMap);
        }
    }


    /**
     * Failure response for loading. Serializes the error type and the error message.
     */
    public record EndFailureResponse(String status, String error_message) {

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
