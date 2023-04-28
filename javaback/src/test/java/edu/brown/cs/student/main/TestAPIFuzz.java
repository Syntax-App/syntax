package edu.brown.cs.student.main;

import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.csv.Parser;
import edu.brown.cs.student.main.csv.Searcher;
import edu.brown.cs.student.main.csv.creators.CreatorFromRow;
import edu.brown.cs.student.main.csv.creators.ListCreator;
import edu.brown.cs.student.main.server.States;
import edu.brown.cs.student.main.server.csvHandlers.LoadHandler;
import edu.brown.cs.student.main.server.csvHandlers.SearchHandler;
import edu.brown.cs.student.main.server.csvHandlers.ViewHandler;
import edu.brown.cs.student.main.server.weather.GeoCoord;
import edu.brown.cs.student.main.server.weather.WeatherHandler;
import edu.brown.cs.student.main.server.weather.WeatherUtils;
import okio.Buffer;
import okio.BufferedSource;
import org.eclipse.jetty.util.IO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration testing suite.
 */
public class TestAPIFuzz {

    private double rLat;
    private double rLon;

    @BeforeAll
    public static void setup_before_everything() {
        Spark.port(0);
        Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
    }

    // initializes shared test states.
    final States states = new States();

    /**
     * Starts the Spark server on port 0, for testing purposes.
     */
    @BeforeEach
    public void setup() {
        this.states.resetAll();

        // Setting up the handler for the GET /order endpoint
        Spark.get("loadcsv", new LoadHandler(this.states));
        Spark.get("viewcsv", new ViewHandler(this.states));
        Spark.get("searchcsv", new SearchHandler(this.states));
        Spark.get("weather", new WeatherHandler());
        Spark.init();
        Spark.awaitInitialization();
    }

    /**
     * Gracefully stop the Spark server.
     */
    @AfterEach
    public void teardown() {
        // Gracefully stop Spark listening on both endpoints
        Spark.unmap("loadcsv");
        Spark.unmap("viewcsv");
        Spark.unmap("searchcsv");
        Spark.unmap("weather");
        Spark.awaitStop(); // don't proceed until the server is stopped
    }

    /**
     * Helper to start a connection to a specific API endpoint/params
     * @param apiCall the call string, including endpoint
     *                (NOTE: this would be better if it had more structure!)
     * @return the connection for the given URL, just after connecting
     * @throws IOException if the connection fails for some reason
     */
    static private HttpURLConnection tryRequest(String apiCall) throws IOException {
        // Configure the connection (but don't actually send the request yet)
        URL requestURL = new URL("http://localhost:"+Spark.port()+"/"+apiCall);
        HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();

        clientConnection.connect();
        return clientConnection;
    }

    /**
     * Helper method to parse a response return by our Server itself in a custom
     * given format.
     * @param clientConnection - connection to the API (ourselves)
     * @param customClass - format in which we want to parse the JSON data in.
     * @param <T> - generic return type
     * @return - instance of CustomClass, with parsed data
     * @throws IOException - potential exception thrown by Moshi's fromJson.
     */
    static private <T> T getResponse(HttpURLConnection clientConnection, Class<T> customClass) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        return moshi.adapter(customClass).fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    }

    /**
     * Helper method to parse a response from a BufferedSource into a
     * given format.
     * @param bs - input BufferedSource
     * @param customClass - format in which we want to parse the JSON data in.
     * @param <T> - generic return type
     * @return - instance of CustomClass, with parsed data
     * @throws IOException - potential exception thrown by Moshi's fromJson.
     */
    static private <T> T getResponse(BufferedSource bs, Class<T> customClass) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        return moshi.adapter(customClass).fromJson(bs);
    }

    // Generates random coordinates inside the US.
    // The coordinates only span a square surface and not the entire country's territory.
    // This is because it was hard to precisely determine the span of valid US coordinates.
    private HttpURLConnection makeGoodRandomWeatherRequest() throws IOException {
        this.rLat = 33.03 + new Random().nextDouble() * (42.06 - 33.03);
        this.rLon = -105.92  + new Random().nextDouble() * (-82.82 + 105.92);
        GeoCoord rGeoCoord = new GeoCoord(this.rLat, this.rLon);
        HttpURLConnection clientConnection = tryRequest("weather?lat=" + rGeoCoord.lat() + "&lon=" + rGeoCoord.lon());
        return clientConnection;
    }

    // Generates random coordinates outside the US.
    // Similarly, the coordinates only span a square surface outside the US.
    private HttpURLConnection makeBadRandomWeatherRequest() throws IOException {
        this.rLat = -37.44 + new Random().nextDouble() * (42.68 + 37.44);
        this.rLon = -31.19  + new Random().nextDouble() * (36.66 + 31.19);
        GeoCoord rGeoCoord = new GeoCoord(this.rLat, this.rLon);
        HttpURLConnection clientConnection = tryRequest("weather?lat=" + rGeoCoord.lat() + "&lon=" + rGeoCoord.lon());
        return clientConnection;
    }

    /**
     * Main fuzz testing method for our Weather endpoint.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testFuzzWeather() throws IOException, InterruptedException {
        for (int i = 0; i < 1; i++) {
            HttpURLConnection connection = this.makeGoodRandomWeatherRequest();
            Buffer buf1 = new Buffer().readFrom(connection.getInputStream());
            Buffer buf2 = buf1.clone();

            // through testing, we noticed that the NWS API sometimes return a 500 error
            // although the coordinates are valid, so we have considered the case where
            // this error happens unexpectedly.
            try {
                WeatherHandler.WeatherSuccessResponse r = getResponse(buf1, WeatherHandler.WeatherSuccessResponse.class);
                assertEquals("success", r.result());
                assertEquals("F", r.unit());
                assertTrue(r.temperature() > -150 && r.temperature() < 200);
                System.out.println("FUZZ TESTING: VALID FORECAST FOR " + this.rLat + "," + this.rLon);
            } catch (JsonDataException e) {
                WeatherHandler.WeatherFailureResponse r = getResponse(buf2, WeatherHandler.WeatherFailureResponse.class);
                assertEquals("error_datasource", r.result());
                assertEquals("An unexpected error occurred while connecting to NWS server.", r.error_message());
                System.err.println("FUZZ TESTING: 500 Error from NWS API FOR " + this.rLat + "," + this.rLon);
            }
        }

        // all requests should return a "error_datasource" error with the invalid coordinates error message.
        for (int i = 0; i < 1; i++) {
            HttpURLConnection connection = this.makeBadRandomWeatherRequest();
            assertNotEquals(500, connection.getResponseCode());
            WeatherHandler.WeatherFailureResponse r = getResponse(connection, WeatherHandler.WeatherFailureResponse.class);
            assertEquals("error_datasource", r.result());
            assertEquals("The entered coordinates were not valid. Please try again.", r.error_message());
            System.out.println("FUZZ TESTING: VALID ERROR FOR " + this.rLat + "," + this.rLon);
        }
    }

}
