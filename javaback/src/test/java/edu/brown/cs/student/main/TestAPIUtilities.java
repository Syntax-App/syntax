package edu.brown.cs.student.main;

import edu.brown.cs.student.main.mocks.MockJSON;
import edu.brown.cs.student.main.mocks.MockWeatherUtils;
import edu.brown.cs.student.main.server.Response;
import edu.brown.cs.student.main.server.csvHandlers.LoadHandler;
import edu.brown.cs.student.main.server.csvHandlers.SearchHandler;
import edu.brown.cs.student.main.server.csvHandlers.ViewHandler;
import edu.brown.cs.student.main.server.weather.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Backend testing suite
 */
public class TestAPIUtilities {

    // ensures that a JSON string is extracted properly from a Reader
    @Test
    public void testStringFromReader() throws IOException {
        // create a mock Reader object, which simulates the connection's InputReader.
        WeatherUtils weatherUtils = new WeatherUtils();
        StringReader stringReader = new StringReader(MockJSON.mockProvidenceForecast);
        String jsonNoFormat = MockJSON.mockProvidenceForecast.replace("\n", "");
        assertEquals(jsonNoFormat, weatherUtils.readerToString(stringReader));
    }

    // tests that the NWS endpoint URL is formed properly given geo coordinates.
    @Test
    public void testEndpointURLFromCoords() {
        WeatherUtils weatherUtils = new WeatherUtils();
        GeoCoord coords = new GeoCoord(10.4, -77.3);
        assertEquals("https://api.weather.gov/points/10.4,-77.3", weatherUtils.getGridEndpointURL(coords));
    }

    // given a set of coordinates with many decimal places, tests that the NWS URL
    // is formed properly, by rounding these coords to the second decimal place.
    @Test
    public void testRoundEndpointURLFromCoords() {
        WeatherUtils weatherUtils = new WeatherUtils();
        GeoCoord coords = new GeoCoord(10.4192938174662, -77.3103975527194);
        assertEquals("https://api.weather.gov/points/10.42,-77.31", weatherUtils.getGridEndpointURL(coords));
    }

    // Tests that the forecast URL returned by the API is parsed properly.
    @Test
    public void testFromJsonForecastURL() throws IOException {
        WeatherUtils weatherUtils = new WeatherUtils();
        Response.GridResponse gridResponse = weatherUtils.fromJson(Response.GridResponse.class, MockJSON.mockProvidenceURLs);
        assertEquals("https://api.weather.gov/gridpoints/BOX/64,63/forecast", gridResponse.forecastURL().url());
    }

    // tests that the data returned by the call to the forecast URL is parsed properly.
    @Test
    public void testFromJsonForecast() throws IOException {
        WeatherUtils weatherUtils = new WeatherUtils();
        Response.ForecastResponse forecastResponse = weatherUtils.fromJson(Response.ForecastResponse.class, MockJSON.mockProvidenceForecast);
        assertEquals(14, forecastResponse.forecastPeriods().forecasts().size());
        assertEquals(51, forecastResponse.forecastPeriods().forecasts().get(0).temp());
        assertEquals("F", forecastResponse.forecastPeriods().forecasts().get(0).unit());
        assertEquals(31, forecastResponse.forecastPeriods().forecasts().get(1).temp());
        assertEquals("F", forecastResponse.forecastPeriods().forecasts().get(1).unit());

    }

    // Tests that the WeatherResponse instance returned by the getWeatherResponse helper does
    // return a WeatherResponse with the appropriate values.
    @Test
    public void testGetWeatherResponse() throws IOException {
        WeatherUtils weatherUtils = new WeatherUtils();
        Response.ForecastResponse forecastResponse = weatherUtils.fromJson(Response.ForecastResponse.class, MockJSON.mockProvidenceForecast);
        String mockTime = "2023-03-02T19:53:16+00:00";
        WeatherResponse response = weatherUtils.getWeatherResponse(mockTime, forecastResponse);
        assertEquals(51, response.temp());
        assertEquals("F", response.unit());
        assertEquals(mockTime, response.timestamp());
    }

    // tests that serializing a WeatherSuccess from a WeatherResponse into a response JSON.
    @Test
    public void testWeatherSuccessSerialize() throws IOException {
        String mockTime = "2023-03-02T19:53:16+00:00";
        MockWeatherUtils mockWeatherUtils = new MockWeatherUtils();
        Response.ForecastResponse forecastResponse = mockWeatherUtils.fromJson(Response.ForecastResponse.class, MockJSON.mockProvidenceForecast);
        WeatherResponse response = mockWeatherUtils.getWeatherResponse(mockTime, forecastResponse);

        WeatherHandler weatherHandler = new WeatherHandler();
        assertEquals(MockJSON.mockServerExpectedNewYorkCity, weatherHandler.getSerializedSuccess(40.73, -73.94, response));
    }

    // tests that serializing a WeatherFailure into an error JSON response is done properly.
    @Test
    public void testWeatherFailureSerialize() {
        WeatherHandler weatherHandler = new WeatherHandler();
        String serializedFailure = weatherHandler.getSerializedFailure("error_bad_json", "Some error message!");
        assertEquals(MockJSON.mockExpectedWeatherFailure, serializedFailure);
    }


    // tests that forecasts are cached automatically by the WeatherProxy.
    @Test
    public void testWeatherProxyCacheIdentical() {
        MockWeatherUtils mockWeatherUtils = new MockWeatherUtils();
        WeatherProxy weatherProxy = new WeatherProxy(mockWeatherUtils, CacheParams.PROXIMITY_RADIUS, CacheParams.CACHE_MAX_SIZE, CacheParams.CACHE_EXPIRE_AFTER, TimeUnit.HOURS);
        GeoCoord nyCoords = new GeoCoord(40.73, -73.94);
        weatherProxy.getForecast(nyCoords);
        assertTrue(weatherProxy.isCachedExact(nyCoords));

        WeatherResponse res = weatherProxy.getForecast(nyCoords);
        assertEquals(55, res.temp());
        assertEquals("F", res.unit());
    }

    // tests that, when attempting to get a forecast for coordinates close to
    // a cached set of coordinates, the WeatherProxy does return the cached set.
    @Test
    public void testWeatherProxyCacheClose() {
        MockWeatherUtils mockWeatherUtils = new MockWeatherUtils();
        WeatherProxy weatherProxy = new WeatherProxy(mockWeatherUtils, CacheParams.PROXIMITY_RADIUS, CacheParams.CACHE_MAX_SIZE, CacheParams.CACHE_EXPIRE_AFTER, TimeUnit.HOURS);
        GeoCoord nyCoords = new GeoCoord(40.73, -73.94);
        weatherProxy.getForecast(nyCoords);

        GeoCoord closeNyCoords = new GeoCoord(40.60, -73.80);
        assertFalse(weatherProxy.isCachedExact(closeNyCoords));
        assertEquals(nyCoords, weatherProxy.getClosestKey(closeNyCoords));

        WeatherResponse res = weatherProxy.getForecast(closeNyCoords);
        assertEquals(55, res.temp());
        assertEquals("F", res.unit());
    }

    // makes sure that we get new data is it's been more than the time of expiry.
    // would make use of the WeatherProxy's second constructor.
    @Test
    public void testWeatherProxyExpiry() throws InterruptedException {
        MockWeatherUtils mockWeatherUtils = new MockWeatherUtils();
        WeatherProxy weatherProxy = new WeatherProxy(mockWeatherUtils, CacheParams.PROXIMITY_RADIUS, CacheParams.CACHE_MAX_SIZE, 1, TimeUnit.SECONDS);
        GeoCoord nyCoords = new GeoCoord(40.73, -73.94);
        weatherProxy.getForecast(nyCoords);
        assertTrue(weatherProxy.isCachedExact(nyCoords));
        TimeUnit.SECONDS.sleep(1);
        assertFalse(weatherProxy.isCachedExact(nyCoords));
    }

    // ensures that, when a set of coordinates that wasn't cached
    // is given, the exact same coordinates are returned.
    @Test
    public void testGetClosestNotFound() {
        MockWeatherUtils mockWeatherUtils = new MockWeatherUtils();
        WeatherProxy weatherProxy = new WeatherProxy(mockWeatherUtils, CacheParams.PROXIMITY_RADIUS, CacheParams.CACHE_MAX_SIZE, CacheParams.CACHE_EXPIRE_AFTER, TimeUnit.HOURS);
        GeoCoord nyCoords = new GeoCoord(40.73493012, -73.94394812);
        assertEquals(nyCoords, weatherProxy.getClosestKey(nyCoords));
    }

    // testing the LoadSuccessResponse's serialize method.
    @Test
    public void testLoadSuccessSerialize() {
        LoadHandler.LoadSuccessResponse loadSuccessResponse = new LoadHandler.LoadSuccessResponse("success", "test_filepath");
        assertEquals("{\"result\":\"success\",\"filepath\":\"test_filepath\"}", loadSuccessResponse.serialize());
    }

    // testing the LoadFailureResponse's serialize method.
    @Test
    public void testLoadFailureSerialize() {
        LoadHandler.LoadFailureResponse loadFailureResponse = new LoadHandler.LoadFailureResponse("error_bad_json", "some error message!");
        assertEquals("{\"result\":\"error_bad_json\",\"error_message\":\"some error message!\"}", loadFailureResponse.serialize());
    }

    // testing the SearchSuccessResponse's serialize method.
    @Test
    public void testSearchSuccessSerialize() {
        ArrayList<String> innerRow = new ArrayList<>();
        innerRow.add("Hello!");
        innerRow.add("Hi!");
        List<List<String>> fakeData = new ArrayList<>();
        fakeData.add(innerRow);
        SearchHandler.SearchSuccessResponse searchSuccessResponse = new SearchHandler.SearchSuccessResponse("success", "some_value", "some_column", fakeData);
        assertEquals("{\"result\":\"success\",\"value\":\"some_value\",\"col\":\"some_column\",\"data\":[[\"Hello!\",\"Hi!\"]]}", searchSuccessResponse.serialize());
    }

    // testing the SearchFailureResponse's serialize method.
    @Test
    public void testSearchFailureSerialize() {
        SearchHandler.SearchFailureResponse searchFailureResponse = new SearchHandler.SearchFailureResponse("error_bad_json", "some error message!", "some_value", "some_column");
        assertEquals("{\"result\":\"error_bad_json\",\"error_message\":\"some error message!\",\"value\":\"some_value\",\"col\":\"some_column\"}", searchFailureResponse.serialize());
    }

    // testing the ViewSuccessResponse's serialize method.
    @Test
    public void testViewSuccessSerialize() {
        ArrayList<String> innerRow = new ArrayList<>();
        innerRow.add("Hello!");
        innerRow.add("Hi!");
        List<List<String>> fakeData = new ArrayList<>();
        fakeData.add(innerRow);
        ViewHandler.ViewSuccessResponse viewSuccessResponse = new ViewHandler.ViewSuccessResponse("success", fakeData);
        assertEquals("{\"result\":\"success\",\"data\":[[\"Hello!\",\"Hi!\"]]}", viewSuccessResponse.serialize());
    }

    // testing the ViewFailureResponse's serialize method.
    @Test
    public void testViewFailureSerialize() {
        ViewHandler.ViewFailureResponse viewFailureResponse = new ViewHandler.ViewFailureResponse("error_bad_json", "some error message!");
        assertEquals("{\"result\":\"error_bad_json\",\"error_message\":\"some error message!\"}", viewFailureResponse.serialize());
    }

}
