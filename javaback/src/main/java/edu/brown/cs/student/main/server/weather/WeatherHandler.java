/**
 * Handler class for the soup ordering API endpoint.
 *
 * This endpoint is similar to the endpoint(s) you'll need to create for Sprint 2. It takes a basic GET request with
 * no Json body, and returns a Json object in reply. The responses are more complex, but this should serve as a reference.
 *
 */
package edu.brown.cs.student.main.server.weather;

import com.google.common.cache.Cache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.squareup.moshi.*;
import edu.brown.cs.student.main.server.SerializeHelper;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Handler class for fetching weather data.
 */
public class WeatherHandler implements Route {
    WeatherUtils weatherUtils;
    WeatherProxy weatherProxy;

    /**
     * WeatherHandler constructor.
     */
    public WeatherHandler() {
        this.weatherUtils = new WeatherUtils();
        this.weatherProxy = new WeatherProxy(this.weatherUtils, CacheParams.PROXIMITY_RADIUS, CacheParams.CACHE_MAX_SIZE, CacheParams.CACHE_EXPIRE_AFTER, CacheParams.CACHE_EXPIRE_AFTER_TIMEUNIT);
    }

    /**
     * Uses lat and lon params to query the NWS Weather API for weather data.
     * @param request the request to handle
     * @param response used to modify properties of the response
     * @return response content
     * @throws Exception part of interface
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        // get params
        String latParam = request.queryParams("lat");
        String lonParam = request.queryParams("lon");
        double lat;
        double lon;

        // check that there are lat & lon inputted
        if (latParam == null || lonParam == null) {
            return this.getSerializedFailure("error_bad_request", "Please input both <lat> and <lon> parameters to request weather.");
        }

        // check that lat & lon params are valid doubles
        try {
            lat = Double.parseDouble(latParam);
            lon = Double.parseDouble(lonParam);
        } catch (NumberFormatException e) {
            return this.getSerializedFailure("error_bad_request", "Input not a number. Please try again.");
        }

        // query NWS API via weatherProxy and return result
        try {
            GeoCoord coords = new GeoCoord(lat, lon);
            WeatherResponse forecast = this.weatherProxy.getForecast(coords);
            return this.getSerializedSuccess(lat, lon, forecast);
        } catch (UncheckedExecutionException e) {
            return this.getSerializedFailure("error_datasource", e.getCause().getMessage());
        }
    }

    /**
     * Helper method to get the success response.
     * @param lat - latitude param inputted
     * @param lon - longitude param inputted
     * @param response - WeatherResponse containing info to return
     * @return - a WeatherSuccessResponse (serialized response)
     */
    public String getSerializedSuccess(double lat, double lon, WeatherResponse response) {
        int temp = response.temp();
        String unit = response.unit();
        String timestamp = response.timestamp();
        return new WeatherSuccessResponse("success", lat, lon, temp, unit, timestamp).serialize();
    }

    public String getSerializedFailure(String errorType, String errorMessage) {
        return new WeatherFailureResponse(errorType, errorMessage).serialize();
    }

    /**
     * Success response for weather. Serializes the result ("success"), the input params, the forecast, and the time at
     * which the NWS response was retrieved.
     * @param result success result message
     * @param lat - latitude param inputted
     * @param lon - longitude param inputted
     * @param temperature - temperature retrieved
     * @param unit - unit of temp
     * @param timestamp - time at which the forecast was retrieved from NWS API
     */
    public record WeatherSuccessResponse(String result, double lat, double lon, int temperature, String unit, String timestamp) {

        /**
         * @return this response, serialized as Json
         */
        String serialize() {
            Map<String, Object> responseMap = new LinkedHashMap<>();
            responseMap.put("result", "success");
            responseMap.put("lat", lat);
            responseMap.put("lon", lon);
            responseMap.put("temperature", temperature);
            responseMap.put("unit", unit);
            responseMap.put("timestamp", timestamp);
            return SerializeHelper.helpSerialize(responseMap);
        }
    }

    /**
     * Failure response for weather. Serializes the error type and the error message.
     * @param result error type
     * @param error_message error message to display
     */
    public record WeatherFailureResponse(String result, String error_message) {
        /**
         * @return this response, serialized as Json
         */
        String serialize() {
            Map<String, Object> responseMap = new LinkedHashMap<>();
            responseMap.put("result", result);
            responseMap.put("error_message", error_message);
            return SerializeHelper.helpSerialize(responseMap);
        }
    }

}

