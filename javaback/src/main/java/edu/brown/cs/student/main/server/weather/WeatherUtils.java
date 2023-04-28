package edu.brown.cs.student.main.server.weather;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

import edu.brown.cs.student.main.server.Response.*;

/**
 * WeatherUtils class to facilitate the Weather API utilities.
 */
public class WeatherUtils {

    /**
     * WeatherUtils constructor.
     */
    public WeatherUtils() {}


    /**
     * Calls NWS API to retrieve forecast given a GeoCoord.
     * @param coords - GeoCoord for which to retrieve forecast
     * @return - the forecast as WeatherResponse instance
     * @throws IOException - if an error occurs while making the API call
     */
    public WeatherResponse getForecast(GeoCoord coords) throws IOException {
        String gridEndpoint = this.getGridEndpointURL(coords);

        // get grids of given latitude/longitude
        GridResponse gridResponse = this.callAPI(gridEndpoint, GridResponse.class);
        String url = gridResponse.forecastURL().url();

        // get forecast for the given grids
        String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(new java.util.Date());
        ForecastResponse forecastResponse = this.callAPI(url, ForecastResponse.class);

        return this.getWeatherResponse(timestamp, forecastResponse);
    }

    /**
     * Helper method to construct the endpoint URL for the Grid API.
     * *public for testing purposes*
     * @param coords - GeoCoord ot get construct endpoint for
     * @return - url string
     */
    public String getGridEndpointURL(GeoCoord coords) {

        double inputLat = coords.lat();
        double inputLon = coords.lon();

        // rounding to two decimal places
        double lat = Math.round(inputLat * 100) / 100.0;
        double lon = Math.round(inputLon * 100) / 100.0;

        return "https://api.weather.gov/points/" + lat + "," + lon;
    }

    /**
     * Returns the WeatherResponse instance for a given ForecastResponse to be wrapped, and adds a timestamp.
     * *public for testing purposes*
     * @param timestamp - the time at which the forecast was retrieved from NWS API
     * @param forecastResponse - ForecastResponse instance to create a WeatherResponse for
     * @return - new WeatherResponse instance created
     */
    public WeatherResponse getWeatherResponse(String timestamp, ForecastResponse forecastResponse) {
        Forecast forecast = forecastResponse.forecastPeriods().forecasts().get(0);
        return new WeatherResponse(forecast.temp(), forecast.unit(), timestamp);
    }

    /**
     * Makes an API call to the given URL and converts the response to the specified class using moshi.
     * *public for testing purposes*
     * @param url - the URL of the API endpoint to call
     * @param classType - the class type to convert the API response to
     * @param <T> - the type of the resulting object
     * @return - the resulting object of type T
     * @throws IOException - if an error occurs while making the API call
     */
    public <T> T callAPI(String url, Type classType) throws IOException {
        URL endpoint = new URL(url);
        HttpURLConnection clientConnection = (HttpURLConnection) endpoint.openConnection();

        clientConnection.connect();
        int status = clientConnection.getResponseCode();

        switch (status) {
            case 404 -> throw new IOException("The entered coordinates were not valid. Please try again.");
            case 500 -> throw new IOException("An unexpected error occurred while connecting to NWS server.");
        }

        InputStreamReader resultReader = new InputStreamReader(clientConnection.getInputStream());
        String jsonAsString = this.readerToString(resultReader);

        return this.fromJson(classType, jsonAsString);
    }

    /**
     * Converts a Reader object to a String.
     * *public for testing purposes*
     * @param reader - the Reader object to convert
     * @return - the String representation of the Reader object
     * @throws IOException - if an error occurs while converting the Reader object to a String
     */
    public String readerToString(Reader reader) throws IOException {
        BufferedReader br = new BufferedReader(reader);
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            stringBuilder.append(line);
        }
        br.close();

        return stringBuilder.toString();
    }

    /**
     * Helper to convert a String to the specified class.
     * *public for testing purposes*
     * @param classType - class type to convert into
     * @param jsonString - string to be converted
     * @param <T> - class to convert into
     * @return - converted JSON
     * @throws IOException - if Moshi throws an error
     */
    public <T> T fromJson(Type classType, String jsonString) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<T> adapter = moshi.adapter(classType);
        return adapter.fromJson(jsonString);
    }
}
