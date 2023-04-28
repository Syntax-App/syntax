package edu.brown.cs.student.main.server.csvHandlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.csv.Searcher;
import edu.brown.cs.student.main.server.SerializeHelper;
import edu.brown.cs.student.main.server.States;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Handler class for searching a CSV file.
 */
public class SearchHandler implements Route {
    States states;

    /**
     * SearchHandler constructor.
     * @param states -  a class that keeps track of shared variables.
     */
    public SearchHandler(States states) {
        this.states = states;
    }

    /**
     * Uses value and col params to search through the active file if loaded.
     * @param request - the request to handle
     * @param response - used to modify properties of the response
     * @return response - content
     * @throws Exception - part of interface
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        // get params
        String value = request.queryParams("value");
        String col = request.queryParams("col");

        // check if file has been loaded
        if (this.states.getActiveFileContent().isEmpty()) {
            return new SearchFailureResponse("error_bad_request", "Please load a file before searching.", value, col).serialize();
        }

        // check that there's a search value inputted
        if (value == null) {
            return new SearchFailureResponse("error_bad_request", "Please input the search value as a parameter.", value, col).serialize();
        }

        // search and return the result
        try {
            Searcher searcher = new Searcher(this.states.getActiveFileContent(), value, this.states.getActiveFileHeader(), col);
            return new SearchSuccessResponse("success", value, col, searcher.search()).serialize();

        } catch (IllegalArgumentException e) {
            // handle IllegalArgument in search.
            return new SearchFailureResponse("error_bad_request", e.getMessage(), value, col).serialize();
        }
    }

    /**
     * Success response for searching. Serializes the following values.
     * @param result - success result message
     * @param value - search value
     * @param col - column to search in
     * @param data - returned search result
     */
    public record SearchSuccessResponse(String result, String value, String col, List<List<String>> data) {

        /**
         * @return this response, serialized as Json
         */
        public String serialize() {
            LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
            responseMap.put("result", result);
            responseMap.put("value", value);
            responseMap.put("col", col);
            responseMap.put("data", new ArrayList<>(data)); // defensive copy
            return SerializeHelper.helpSerialize(responseMap);
        }
    }

    /**
     * Failure response for searching. Serializes the error type and the error message.
     * @param result error type
     * @param error_message error message to display
     */
    public record SearchFailureResponse(String result, String error_message, String value, String col) {
        /**
         * @return this response, serialized as Json
         */
        public String serialize() {
            LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
            responseMap.put("result", result);
            responseMap.put("error_message", error_message);
            responseMap.put("value", value);
            responseMap.put("col", col);
            return SerializeHelper.helpSerialize(responseMap);
        }
    }

}
