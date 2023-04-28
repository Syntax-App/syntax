package edu.brown.cs.student.main.server.handlers.user;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import edu.brown.cs.student.main.server.SerializeHelper;
import edu.brown.cs.student.main.server.States;
import spark.Request;
import spark.Response;
import spark.Route;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class UserGetHandler implements Route {
    private Firestore db;

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
        String email = request.queryParams("email");
        CollectionReference users = db.collection("users");
        Query query = users.whereEqualTo("email", email);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        if (querySnapshot.get().getDocuments().isEmpty()) {
            return new GetUserFailureResponse("error", "User with given email does not exist!").serialize();
        }

        response.status(200);
        return new GetUserSuccessResponse("success", querySnapshot.get().getDocuments().get(0).getData()).serialize();


//        String filepath = request.queryParams("filepath");
//        String hasHeader = request.queryParams("hasHeader");
//
//        // check that hasHeader is a valid boolean
//        if (hasHeader != null && !hasHeader.equals("true") && !hasHeader.equals("false")) {
//            return new UserCreateHandler.LoadFailureResponse("error_bad_request", "Please enter a valid boolean (true/false) for <hasHeader> parameter.").serialize();
//        }
//
//        // check that filepath was inputted
//        if (filepath == null) {
//            return new UserCreateHandler.LoadFailureResponse("error_bad_request", "Please enter a <filepath> parameter to request.").serialize();
//        }
//
//        try {
//            // instantiate parser and parse the given csv file
//            CreatorFromRow<List<String>> rowCreator = new ListCreator();
//            Parser<List<String>> parser = new Parser<>(new FileReader("data/" + filepath + ".csv"), rowCreator, Boolean.parseBoolean(hasHeader));
//
//            // set the states variables
//            this.states.setActiveFileContent(parser.getContent());
//            this.states.setActiveFileHeader(parser.getHeader());
//            return new UserCreateHandler.LoadSuccessResponse(filepath).serialize();
//
//        } catch (FileNotFoundException e) {
//            // handle FileNotFound
//            return new UserCreateHandler.LoadFailureResponse("error_datasource", "The given file was not found! Please load a valid file.").serialize();
//        }
    }


    /**
     * Success response for loading. Serializes the result ("success") and the filepath of file loaded.
     *
     * @param result   success result message
     * @param filepath filepath of file loaded
     */
    public record GetUserSuccessResponse(String status, Map<String, Object> userData) {

        /**
         * @return this response, serialized as Json
         */
        public String serialize() {
            LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
            responseMap.put("status", "success");

            HashMap<String, Map<String, Object>> dataMap = new HashMap<>();
            dataMap.put("user", userData);
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
    public record GetUserFailureResponse(String status, String error_message) {

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
