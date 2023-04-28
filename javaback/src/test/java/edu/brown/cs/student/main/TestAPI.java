package edu.brown.cs.student.main;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.csv.creators.CreatorFromRow;
import edu.brown.cs.student.main.csv.creators.ListCreator;
import edu.brown.cs.student.main.csv.Parser;
import edu.brown.cs.student.main.csv.Searcher;
import edu.brown.cs.student.main.server.States;
import edu.brown.cs.student.main.server.csvHandlers.LoadHandler;
import edu.brown.cs.student.main.server.csvHandlers.SearchHandler;
import edu.brown.cs.student.main.server.csvHandlers.ViewHandler;
import edu.brown.cs.student.main.server.weather.WeatherHandler;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration testing suite.
 */
public class TestAPI {

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

    // tests the API response to a /loadcsv query without a filepath.
    @Test
    public void testAPILoadNoFile() throws IOException {
        HttpURLConnection clientConnection = tryRequest("loadcsv");
        assertEquals(200, clientConnection.getResponseCode());
        LoadHandler.LoadFailureResponse response = getResponse(clientConnection, LoadHandler.LoadFailureResponse.class);

        assertNotNull(response);
        assertEquals("error_bad_request", response.result());
        assertEquals("Please enter a <filepath> parameter to request.", response.error_message());

        clientConnection.disconnect();
    }

    // tests the API response to a /loadcsv query with a nonexistent file.
    @Test
    public void testAPILoadInvalidFile() throws IOException {
        HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=nonexistent");
        assertEquals(200, clientConnection.getResponseCode());
        LoadHandler.LoadFailureResponse response = getResponse(clientConnection, LoadHandler.LoadFailureResponse.class);

        assertNotNull(response);
        assertEquals("error_datasource", response.result());
        assertEquals("The given file was not found! Please load a valid file.", response.error_message());

        clientConnection.disconnect();
    }

    // tests the API response to a /loadcsv query with an invalid value for hasHeader.
    @Test
    public void testAPILoadInvalidHasHeader() throws IOException {
        HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=persons/names-with-header&&hasHeader=yuh");
        assertEquals(200, clientConnection.getResponseCode());
        LoadHandler.LoadFailureResponse response = getResponse(clientConnection, LoadHandler.LoadFailureResponse.class);

        assertNotNull(response);
        assertEquals("error_bad_request", response.result());
        assertEquals("Please enter a valid boolean (true/false) for <hasHeader> parameter.", response.error_message());

        clientConnection.disconnect();
    }

    // tests the API response to a /loadcsv query with an empty CSV file.
    @Test
    public void testAPILoadEmptyFile() throws IOException {
        HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=empty");
        assertEquals(200, clientConnection.getResponseCode());
        LoadHandler.LoadSuccessResponse response = getResponse(clientConnection, LoadHandler.LoadSuccessResponse.class);

        assertNotNull(response);
        assertEquals("success", response.result());
        assertEquals("empty", response.filepath());

        CreatorFromRow<List<String>> rowCreator = new ListCreator();
        Parser<List<String>> parser = new Parser<>(new FileReader("data/empty.csv"), rowCreator, false);
        assertEquals(parser.getContent(), this.states.getActiveFileContent());
        assertEquals(parser.getHeader(), this.states.getActiveFileHeader());

        clientConnection.disconnect();
    }

    // tests the API response to a /loadcsv query with a valid file.
    @Test
    public void testAPILoadFile() throws IOException {
        HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=persons/names-with-header");
        assertEquals(200, clientConnection.getResponseCode());
        LoadHandler.LoadSuccessResponse response = getResponse(clientConnection, LoadHandler.LoadSuccessResponse.class);

        assertNotNull(response);
        assertEquals("success", response.result());
        assertEquals("persons/names-with-header", response.filepath());

        CreatorFromRow<List<String>> rowCreator = new ListCreator();
        Parser<List<String>> parser = new Parser<>(new FileReader("data/persons/names-with-header.csv"), rowCreator, false);
        assertEquals(parser.getContent(), this.states.getActiveFileContent());
        assertEquals(parser.getHeader(), this.states.getActiveFileHeader());

        clientConnection.disconnect();
    }

    // tests the API response to a /loadcsv query with a true header parameter.
    @Test
    public void testAPILoadWithHeader() throws IOException {
        HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=persons/names-with-header&&hasHeader=true");
        assertEquals(200, clientConnection.getResponseCode());
        LoadHandler.LoadSuccessResponse response = getResponse(clientConnection, LoadHandler.LoadSuccessResponse.class);

        assertNotNull(response);
        assertEquals("success", response.result());
        assertEquals("persons/names-with-header", response.filepath());

        CreatorFromRow<List<String>> rowCreator = new ListCreator();
        Parser<List<String>> parser = new Parser<>(new FileReader("data/persons/names-with-header.csv"), rowCreator, true);
        assertEquals(parser.getContent(), this.states.getActiveFileContent());
        assertEquals(parser.getHeader(), this.states.getActiveFileHeader());

        clientConnection.disconnect();
    }

    // tests the API response to a /view query when no file was loaded.
    @Test
    public void testAPIViewNoFile() throws IOException {
        HttpURLConnection clientConnection = tryRequest("viewcsv");
        assertEquals(200, clientConnection.getResponseCode());
        ViewHandler.ViewFailureResponse response = getResponse(clientConnection, ViewHandler.ViewFailureResponse.class);

        assertNotNull(response);
        assertEquals("error_datasource", response.result());
        assertEquals("Please load a file before viewing", response.error_message());

        clientConnection.disconnect();
    }

    // tests the API response to a /view query when a file was loaded.
    @Test
    public void testAPIViewFile() throws IOException {
        HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=persons/names-with-header&&hasHeader=true");
        assertEquals(200, clientConnection.getResponseCode());

        clientConnection = tryRequest("viewcsv");
        assertEquals(200, clientConnection.getResponseCode());
        ViewHandler.ViewSuccessResponse response = getResponse(clientConnection, ViewHandler.ViewSuccessResponse.class);

        CreatorFromRow<List<String>> rowCreator = new ListCreator();
        Parser<List<String>> parser = new Parser<>(new FileReader("data/persons/names-with-header.csv"), rowCreator, true);

        assertNotNull(response);
        assertEquals("success", response.result());
        assertEquals(parser.getContent(), response.data());

        clientConnection.disconnect();
    }

    // tests the API response to a /search query when no file was loaded.
    @Test
    public void testAPISearchNoFile() throws IOException {
        HttpURLConnection clientConnection = tryRequest("searchcsv?value=ben&&col=name");
        assertEquals(200, clientConnection.getResponseCode());
        SearchHandler.SearchFailureResponse response = getResponse(clientConnection, SearchHandler.SearchFailureResponse.class);

        assertNotNull(response);
        assertEquals("error_bad_request", response.result());
        assertEquals("Please load a file before searching.", response.error_message());

        clientConnection.disconnect();
    }

    // tests the API response to a /search query with a value not in the CSV.
    @Test
    public void testAPISearchNoValue() throws IOException {
        HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=persons/names-with-header&&hasHeader=true");
        assertEquals(200, clientConnection.getResponseCode());

        clientConnection = tryRequest("searchcsv?col=name");
        assertEquals(200, clientConnection.getResponseCode());
        SearchHandler.SearchFailureResponse response = getResponse(clientConnection, SearchHandler.SearchFailureResponse.class);

        assertNotNull(response);
        assertEquals("error_bad_request", response.result());
        assertEquals("Please input the search value as a parameter.", response.error_message());

        clientConnection.disconnect();
    }

    // tests the API response to a /search query with a column out of range.
    @Test
    public void testAPISearchOutOfRange() throws IOException {
        HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=persons/names-with-header&&hasHeader=true");
        assertEquals(200, clientConnection.getResponseCode());

        clientConnection = tryRequest("searchcsv?value=ben&&col=100");
        assertEquals(200, clientConnection.getResponseCode());
        SearchHandler.SearchFailureResponse response = getResponse(clientConnection, SearchHandler.SearchFailureResponse.class);

        assertNotNull(response);
        assertEquals("error_bad_request", response.result());

        clientConnection.disconnect();
    }

    // tests the API response to a /search query with an invalid column value.
    @Test
    public void testAPISearchInvalidColHeader() throws IOException {
        HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=persons/names-with-header&&hasHeader=true");
        assertEquals(200, clientConnection.getResponseCode());

        clientConnection = tryRequest("searchcsv?value=ben&&col=invalid");
        assertEquals(200, clientConnection.getResponseCode());
        SearchHandler.SearchFailureResponse response = getResponse(clientConnection, SearchHandler.SearchFailureResponse.class);

        assertNotNull(response);
        assertEquals("error_bad_request", response.result());

        clientConnection.disconnect();
    }

    // tests the API response to a /search query in a file without a header, with a String column
    // identifier.
    @Test
    public void testAPISearchNoHeader() throws IOException {
        HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=persons/names-no-header&&hasHeader=false");
        assertEquals(200, clientConnection.getResponseCode());

        clientConnection = tryRequest("searchcsv?value=ben&&col=name");
        assertEquals(200, clientConnection.getResponseCode());
        SearchHandler.SearchFailureResponse response = getResponse(clientConnection, SearchHandler.SearchFailureResponse.class);

        assertNotNull(response);
        assertEquals("error_bad_request", response.result());

        clientConnection.disconnect();
    }

    // tests the API response for a /search query in a regular file, with valid value and column identifier.
    @Test
    public void testAPISearchFile() throws IOException {
        HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=persons/names-with-header&&hasHeader=true");
        assertEquals(200, clientConnection.getResponseCode());

        clientConnection = tryRequest("searchcsv?value=ben&&col=name");
        assertEquals(200, clientConnection.getResponseCode());
        SearchHandler.SearchSuccessResponse response = getResponse(clientConnection, SearchHandler.SearchSuccessResponse.class);

        Searcher searcher = new Searcher(this.states.getActiveFileContent(), "ben", this.states.getActiveFileHeader(),"name");

        assertNotNull(response);
        assertEquals("success", response.result());
        assertEquals("ben", response.value());
        assertEquals("name", response.col());
        assertEquals(searcher.search(), response.data());

        clientConnection.disconnect();
    }

    // tests the API response for a query to the weather/ endpoint, with Providence's geo coordinates.
    @Test
    public void testAPIWeatherProv() throws IOException {
        HttpURLConnection clientConnection = tryRequest("weather?lat=41.8268&lon=-71.4029");
        assertEquals(200, clientConnection.getResponseCode());
        WeatherHandler.WeatherSuccessResponse response = getResponse(clientConnection, WeatherHandler.WeatherSuccessResponse.class);

        assertNotNull(response);
        assertEquals("success", response.result());

        clientConnection.disconnect();
    }

    // tests the API response for a query to a weather/ endpoint, with double coordinates with a very
    // large number of decimal places -- ensures that values are rounded.
    @Test
    public void testAPIWeatherLongCoords() throws IOException {
        HttpURLConnection clientConnection = tryRequest("weather?lat=41.82683284239819824838175&lon=-71.4029123721899462876419807");
        assertEquals(200, clientConnection.getResponseCode());
        WeatherHandler.WeatherSuccessResponse response = getResponse(clientConnection, WeatherHandler.WeatherSuccessResponse.class);

        assertNotNull(response);
        assertEquals("success", response.result());

        clientConnection.disconnect();
    }

    // tests the API response for a query to the weather/ endpoint, with invalid coordinates.
    @Test
    public void testAPIWeatherInvalidCoords() throws IOException {
        HttpURLConnection clientConnection = tryRequest("weather?lat=1000&lon=-1000");
        assertEquals(200, clientConnection.getResponseCode());
        WeatherHandler.WeatherFailureResponse response = getResponse(clientConnection, WeatherHandler.WeatherFailureResponse.class);

        assertNotNull(response);
        assertEquals("error_datasource", response.result());
        assertEquals("Server returned HTTP response code: 400 for URL: https://api.weather.gov/points/1000.0,-1000.0", response.error_message());

        clientConnection.disconnect();
    }

    // tests the API response for a query with a longitude, but without a latitude value.
    @Test
    public void testAPIWeatherNoLat() throws IOException {
        HttpURLConnection clientConnection = tryRequest("weather?lon=-71.4029");
        assertEquals(200, clientConnection.getResponseCode());
        WeatherHandler.WeatherFailureResponse response = getResponse(clientConnection, WeatherHandler.WeatherFailureResponse.class);

        assertNotNull(response);
        System.out.println(response);
        assertEquals("error_bad_request", response.result());
        assertEquals("Please input both <lat> and <lon> parameters to request weather.", response.error_message());

        clientConnection.disconnect();
    }

    // tests the API response for a query with a latitude, but without a longitude value.
    @Test
    public void testAPIWeatherNoLon() throws IOException {
        HttpURLConnection clientConnection = tryRequest("weather?lat=41.8268");
        assertEquals(200, clientConnection.getResponseCode());
        WeatherHandler.WeatherFailureResponse response = getResponse(clientConnection, WeatherHandler.WeatherFailureResponse.class);

        assertNotNull(response);
        assertEquals("error_bad_request", response.result());
        assertEquals("Please input both <lat> and <lon> parameters to request weather.", response.error_message());

        clientConnection.disconnect();
    }

    // tests the API response for non-numeric longitude and latitude values.
    @Test
    public void testAPIWeatherNotNumbers() throws IOException {
        HttpURLConnection clientConnection = tryRequest("weather?lat=yuh&lon=yuh");
        assertEquals(200, clientConnection.getResponseCode());
        WeatherHandler.WeatherFailureResponse response = getResponse(clientConnection, WeatherHandler.WeatherFailureResponse.class);

        assertNotNull(response);
        assertEquals("error_bad_request", response.result());
        assertEquals("Input not a number. Please try again.", response.error_message());

        clientConnection.disconnect();
    }

}
