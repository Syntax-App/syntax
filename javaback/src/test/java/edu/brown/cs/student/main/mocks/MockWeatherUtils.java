package edu.brown.cs.student.main.mocks;

import edu.brown.cs.student.main.server.Response;
import edu.brown.cs.student.main.server.weather.GeoCoord;
import edu.brown.cs.student.main.server.weather.WeatherResponse;
import edu.brown.cs.student.main.server.weather.WeatherUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * A child class of WeatherUtils, used solely for testing.
 * This class overrides the getForecast method to result our mock NYC data.
 */
public class MockWeatherUtils extends WeatherUtils {

    /**
     * Gets the forecast (WeatherResponse) for our mock NYC data.
     * @param coords - GeoCoord for which to retrieve forecast
     * @return - a WeatherResponse object for the Mock NYC data.
     * @throws IOException
     */
    @Override
    public WeatherResponse getForecast(GeoCoord coords) throws IOException {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(new java.util.Date());
        Response.ForecastResponse forecastResponse = this.fromJson(Response.ForecastResponse.class, MockJSON.mockNewYorkCityForecast);
        return getWeatherResponse(timestamp, forecastResponse);
    }
}
