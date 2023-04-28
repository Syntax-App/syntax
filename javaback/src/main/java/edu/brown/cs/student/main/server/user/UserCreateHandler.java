package edu.brown.cs.student.main.server.user;

import edu.brown.cs.student.main.csv.Parser;
import edu.brown.cs.student.main.csv.creators.CreatorFromRow;
import edu.brown.cs.student.main.csv.creators.ListCreator;
import edu.brown.cs.student.main.server.SerializeHelper;
import edu.brown.cs.student.main.server.States;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Handler class for loading a CSV file.
 */
public class UserCreateHandler implements Route {
    private States states;

    /**
     * LoadHandler constructor.
     * @param states -  a class that keeps track of shared variables.
     */
    public UserCreateHandler(States states) {
        this.states = states;
    }

    /**
     * Uses filepath and hasHeader params to parse a CSV and set the active file variables.
     * @param request the request to handle
     * @param response used to modify properties of the response
     * @return response content
     * @throws Exception part of interface
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        // get params
        String filepath = request.queryParams("filepath");
        String hasHeader = request.queryParams("hasHeader");
        request.body().

        // check that hasHeader is a valid boolean
        if (hasHeader != null && !hasHeader.equals("true") && !hasHeader.equals("false")) {
            return new LoadFailureResponse("error_bad_request", "Please enter a valid boolean (true/false) for <hasHeader> parameter.").serialize();
        }

        // check that filepath was inputted
        if (filepath == null) {
            return new LoadFailureResponse("error_bad_request", "Please enter a <filepath> parameter to request.").serialize();
        }

        try {
            // instantiate parser and parse the given csv file
            CreatorFromRow<List<String>> rowCreator = new ListCreator();
            Parser<List<String>> parser = new Parser<>(new FileReader("data/" + filepath + ".csv"), rowCreator, Boolean.parseBoolean(hasHeader));

            // set the states variables
            this.states.setActiveFileContent(parser.getContent());
            this.states.setActiveFileHeader(parser.getHeader());
            return new LoadSuccessResponse(filepath).serialize();

        } catch (FileNotFoundException e) {
            // handle FileNotFound
            return new LoadFailureResponse("error_datasource", "The given file was not found! Please load a valid file.").serialize();
        }
    }


    /**
     * Success response for loading. Serializes the result ("success") and the filepath of file loaded.
     * @param result success result message
     * @param filepath filepath of file loaded
     */
    public record LoadSuccessResponse(String result, String filepath) {
        public LoadSuccessResponse(String filepath) {
            this("success", filepath);
        }
        /**
         * @return this response, serialized as Json
         */
        public String serialize() {
            LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
            responseMap.put("result", result);
            responseMap.put("filepath", filepath);
            return SerializeHelper.helpSerialize(responseMap);
        }
    }


    /**
     * Failure response for loading. Serializes the error type and the error message.
     * @param result error type
     * @param error_message error message to display
     */
    public record LoadFailureResponse(String result, String error_message) {

        /**
         * @return this response, serialized as Json
         */
        public String serialize() {
            LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
            responseMap.put("result", result);
            responseMap.put("error_message", error_message());
            return SerializeHelper.helpSerialize(responseMap);
        }
    }

}
