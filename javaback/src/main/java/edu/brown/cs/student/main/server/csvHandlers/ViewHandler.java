package edu.brown.cs.student.main.server.csvHandlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
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
 * Handler class for viewing a CSV file.
 */
public class ViewHandler implements Route {
    private States states;

    /**
     * ViewHandler constructor.
     * @param states -  a class that keeps track of shared variables.
     */
    public ViewHandler(States states) {
        this.states = states;
    }

    /**
     * If file is loaded, simply returns the active file as a JSON.
     * @param request the request to handle
     * @param response used to modify properties of the response
     * @return response content
     * @throws Exception part of interface
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        if (!this.states.getActiveFileContent().isEmpty()) {
            return new ViewSuccessResponse("success", this.states.getActiveFileContent()).serialize();
        } else {
            return new ViewFailureResponse("error_datasource", "Please load a file before viewing").serialize();
        }
    }

    /**
     * Success response for viewing. Serializes the csv data.
     * @param result success result message
     * @param data csv data displayed
     */
    public record ViewSuccessResponse(String result, List<List<String>> data) {
        /**
         * @return this response, serialized as Json
         */
        public String serialize() {
            LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
            responseMap.put("result", result);
            responseMap.put("data", new ArrayList<>(data)); // defensive copy
            return SerializeHelper.helpSerialize(responseMap);
        }
    }

    /**
     * Failure response for viewing. Serializes the error type and the error message.
     * @param result error type
     * @param error_message error message to display
     */
    public record ViewFailureResponse(String result, String error_message) {

        /**
         * @return this response, serialized as Json
         */
        public String serialize() {
            LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
            responseMap.put("result", result);
            responseMap.put("error_message", error_message);
            return SerializeHelper.helpSerialize(responseMap);
        }
    }

}
