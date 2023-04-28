package edu.brown.cs.student.main.server;

import com.squareup.moshi.Json;
import java.util.List;

/**
 * This class includes multiple record definitions, used to parse JSONs
 * obtained from the NWS API.
 */

public class Response {
    // record for the "properties" field of a NWS API "/points" response.
    // used to extract the URL to obtain forecast information.
    public record GridResponse(
            @Json(name = "properties") ForecastURL forecastURL
    ){}

    // properties.forecast data, which has the URL to obtain forecast information.
    public record ForecastURL(
            @Json(name = "forecast") String url
    ){}

    // record for the "properties" field of a NWS API forecast response.
    // used to extract forecast periods, and thus forecast data.
    public record ForecastResponse(
            @Json(name = "properties") ForecastPeriods forecastPeriods
    ){}

    // properties.periods data, which has a list of weather forecasts.
    public record ForecastPeriods(
            @Json(name = "periods") List<Forecast> forecasts
    ){}

    // properties.period.temperature, which holds the temperature value and unit.
    public record Forecast(
            @Json(name = "temperature") int temp, @Json(name = "temperatureUnit") String unit
    ) {}
}
