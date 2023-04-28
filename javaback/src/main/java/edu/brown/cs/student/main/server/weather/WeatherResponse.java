package edu.brown.cs.student.main.server.weather;

/**
 * Record to wrap ForecastResponse from NWS API. Used to add the timestamp.
 * @param temp - temperature in response
 * @param unit - unit of temp
 * @param timestamp - time at which the forecast was retrieved from NWS
 */
public record WeatherResponse(int temp, String unit, String timestamp) {}
