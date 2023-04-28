package edu.brown.cs.student.main.mocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockJSON {

    public static final String mockRequestBody = "{name: Daniel Liu, email: daniel_liu2@brown.edu, pic: google.com}";

    public static final List<String> mockDocs = Arrays.asList("{uuid: 123, name: Daniel Liu, email: daniel_liu2@brown.edu, pic: google.com, stats: {highlpm: 0, highacc: 0, avgacc: 0, avglpm: 0, numraces: 0}}");

    // Mock "/points" result for Providence coordinates.
    public static final String mockProvidenceURLs = "{\n" +
            "    \"@context\": [\n" +
            "        \"https://geojson.org/geojson-ld/geojson-context.jsonld\",\n" +
            "        {\n" +
            "            \"@version\": \"1.1\",\n" +
            "            \"wx\": \"https://api.weather.gov/ontology#\",\n" +
            "            \"s\": \"https://schema.org/\",\n" +
            "            \"geo\": \"http://www.opengis.net/ont/geosparql#\",\n" +
            "            \"unit\": \"http://codes.wmo.int/common/unit/\",\n" +
            "            \"@vocab\": \"https://api.weather.gov/ontology#\",\n" +
            "            \"geometry\": {\n" +
            "                \"@id\": \"s:GeoCoordinates\",\n" +
            "                \"@type\": \"geo:wktLiteral\"\n" +
            "            },\n" +
            "            \"city\": \"s:addressLocality\",\n" +
            "            \"state\": \"s:addressRegion\",\n" +
            "            \"distance\": {\n" +
            "                \"@id\": \"s:Distance\",\n" +
            "                \"@type\": \"s:QuantitativeValue\"\n" +
            "            },\n" +
            "            \"bearing\": {\n" +
            "                \"@type\": \"s:QuantitativeValue\"\n" +
            "            },\n" +
            "            \"value\": {\n" +
            "                \"@id\": \"s:value\"\n" +
            "            },\n" +
            "            \"unitCode\": {\n" +
            "                \"@id\": \"s:unitCode\",\n" +
            "                \"@type\": \"@id\"\n" +
            "            },\n" +
            "            \"forecastOffice\": {\n" +
            "                \"@type\": \"@id\"\n" +
            "            },\n" +
            "            \"forecastGridData\": {\n" +
            "                \"@type\": \"@id\"\n" +
            "            },\n" +
            "            \"publicZone\": {\n" +
            "                \"@type\": \"@id\"\n" +
            "            },\n" +
            "            \"county\": {\n" +
            "                \"@type\": \"@id\"\n" +
            "            }\n" +
            "        }\n" +
            "    ],\n" +
            "    \"id\": \"https://api.weather.gov/points/41.82,-71.41\",\n" +
            "    \"type\": \"Feature\",\n" +
            "    \"geometry\": {\n" +
            "        \"type\": \"Point\",\n" +
            "        \"coordinates\": [\n" +
            "            -71.409999999999997,\n" +
            "            41.82\n" +
            "        ]\n" +
            "    },\n" +
            "    \"properties\": {\n" +
            "        \"@id\": \"https://api.weather.gov/points/41.82,-71.41\",\n" +
            "        \"@type\": \"wx:Point\",\n" +
            "        \"cwa\": \"BOX\",\n" +
            "        \"forecastOffice\": \"https://api.weather.gov/offices/BOX\",\n" +
            "        \"gridId\": \"BOX\",\n" +
            "        \"gridX\": 64,\n" +
            "        \"gridY\": 63,\n" +
            "        \"forecast\": \"https://api.weather.gov/gridpoints/BOX/64,63/forecast\",\n" +
            "        \"forecastHourly\": \"https://api.weather.gov/gridpoints/BOX/64,63/forecast/hourly\",\n" +
            "        \"forecastGridData\": \"https://api.weather.gov/gridpoints/BOX/64,63\",\n" +
            "        \"observationStations\": \"https://api.weather.gov/gridpoints/BOX/64,63/stations\",\n" +
            "        \"relativeLocation\": {\n" +
            "            \"type\": \"Feature\",\n" +
            "            \"geometry\": {\n" +
            "                \"type\": \"Point\",\n" +
            "                \"coordinates\": [\n" +
            "                    -71.418784000000002,\n" +
            "                    41.823056000000001\n" +
            "                ]\n" +
            "            },\n" +
            "            \"properties\": {\n" +
            "                \"city\": \"Providence\",\n" +
            "                \"state\": \"RI\",\n" +
            "                \"distance\": {\n" +
            "                    \"unitCode\": \"wmoUnit:m\",\n" +
            "                    \"value\": 803.30308961539004\n" +
            "                },\n" +
            "                \"bearing\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degree_(angle)\",\n" +
            "                    \"value\": 115\n" +
            "                }\n" +
            "            }\n" +
            "        },\n" +
            "        \"forecastZone\": \"https://api.weather.gov/zones/forecast/RIZ002\",\n" +
            "        \"county\": \"https://api.weather.gov/zones/county/RIC007\",\n" +
            "        \"fireWeatherZone\": \"https://api.weather.gov/zones/fire/RIZ002\",\n" +
            "        \"timeZone\": \"America/New_York\",\n" +
            "        \"radarStation\": \"KBOX\"\n" +
            "    }\n" +
            "}";


    // Mock result for Providence forecast.
    public static final String mockProvidenceForecast = "{\n" +
            "    \"@context\": [\n" +
            "        \"https://geojson.org/geojson-ld/geojson-context.jsonld\",\n" +
            "        {\n" +
            "            \"@version\": \"1.1\",\n" +
            "            \"wx\": \"https://api.weather.gov/ontology#\",\n" +
            "            \"geo\": \"http://www.opengis.net/ont/geosparql#\",\n" +
            "            \"unit\": \"http://codes.wmo.int/common/unit/\",\n" +
            "            \"@vocab\": \"https://api.weather.gov/ontology#\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"type\": \"Feature\",\n" +
            "    \"geometry\": {\n" +
            "        \"type\": \"Polygon\",\n" +
            "        \"coordinates\": [\n" +
            "            [\n" +
            "                [\n" +
            "                    -71.388849699999994,\n" +
            "                    41.839879600000003\n" +
            "                ],\n" +
            "                [\n" +
            "                    -71.393920399999999,\n" +
            "                    41.8184021\n" +
            "                ],\n" +
            "                [\n" +
            "                    -71.365088499999999,\n" +
            "                    41.814619999999998\n" +
            "                ],\n" +
            "                [\n" +
            "                    -71.360011900000003,\n" +
            "                    41.836097099999996\n" +
            "                ],\n" +
            "                [\n" +
            "                    -71.388849699999994,\n" +
            "                    41.839879600000003\n" +
            "                ]\n" +
            "            ]\n" +
            "        ]\n" +
            "    },\n" +
            "    \"properties\": {\n" +
            "        \"updated\": \"2023-03-02T19:53:16+00:00\",\n" +
            "        \"units\": \"us\",\n" +
            "        \"forecastGenerator\": \"BaselineForecastGenerator\",\n" +
            "        \"generatedAt\": \"2023-03-02T21:22:12+00:00\",\n" +
            "        \"updateTime\": \"2023-03-02T19:53:16+00:00\",\n" +
            "        \"validTimes\": \"2023-03-02T13:00:00+00:00/P8DT6H\",\n" +
            "        \"elevation\": {\n" +
            "            \"unitCode\": \"wmoUnit:m\",\n" +
            "            \"value\": 3.048\n" +
            "        },\n" +
            "        \"periods\": [\n" +
            "            {\n" +
            "                \"number\": 1,\n" +
            "                \"name\": \"This Afternoon\",\n" +
            "                \"startTime\": \"2023-03-02T16:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-02T18:00:00-05:00\",\n" +
            "                \"isDaytime\": true,\n" +
            "                \"temperature\": 51,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": \"falling\",\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": null\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": 3.8888888888888888\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 74\n" +
            "                },\n" +
            "                \"windSpeed\": \"13 mph\",\n" +
            "                \"windDirection\": \"NW\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/day/sct?size=medium\",\n" +
            "                \"shortForecast\": \"Mostly Sunny\",\n" +
            "                \"detailedForecast\": \"Mostly sunny. High near 51, with temperatures falling to around 45 in the afternoon. Northwest wind around 13 mph, with gusts as high as 23 mph.\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"number\": 2,\n" +
            "                \"name\": \"Tonight\",\n" +
            "                \"startTime\": \"2023-03-02T18:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-03T06:00:00-05:00\",\n" +
            "                \"isDaytime\": false,\n" +
            "                \"temperature\": 31,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": null,\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": null\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": 1.1111111111111112\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 76\n" +
            "                },\n" +
            "                \"windSpeed\": \"7 to 13 mph\",\n" +
            "                \"windDirection\": \"NW\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/night/few?size=medium\",\n" +
            "                \"shortForecast\": \"Mostly Clear\",\n" +
            "                \"detailedForecast\": \"Mostly clear, with a low around 31. Northwest wind 7 to 13 mph.\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"number\": 3,\n" +
            "                \"name\": \"Friday\",\n" +
            "                \"startTime\": \"2023-03-03T06:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-03T18:00:00-05:00\",\n" +
            "                \"isDaytime\": true,\n" +
            "                \"temperature\": 42,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": null,\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": null\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": -3.8888888888888888\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 72\n" +
            "                },\n" +
            "                \"windSpeed\": \"6 mph\",\n" +
            "                \"windDirection\": \"N\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/day/sct?size=medium\",\n" +
            "                \"shortForecast\": \"Mostly Sunny\",\n" +
            "                \"detailedForecast\": \"Mostly sunny, with a high near 42. North wind around 6 mph.\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"number\": 4,\n" +
            "                \"name\": \"Friday Night\",\n" +
            "                \"startTime\": \"2023-03-03T18:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-04T06:00:00-05:00\",\n" +
            "                \"isDaytime\": false,\n" +
            "                \"temperature\": 32,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": null,\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 100\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": 0.55555555555555558\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 96\n" +
            "                },\n" +
            "                \"windSpeed\": \"6 to 20 mph\",\n" +
            "                \"windDirection\": \"E\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/night/snow,70/snow,100?size=medium\",\n" +
            "                \"shortForecast\": \"Light Snow\",\n" +
            "                \"detailedForecast\": \"Snow between 8pm and 4am, then rain and snow. Cloudy, with a low around 32. East wind 6 to 20 mph. Chance of precipitation is 100%. New snow accumulation of 2 to 4 inches possible.\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"number\": 5,\n" +
            "                \"name\": \"Saturday\",\n" +
            "                \"startTime\": \"2023-03-04T06:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-04T18:00:00-05:00\",\n" +
            "                \"isDaytime\": true,\n" +
            "                \"temperature\": 39,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": null,\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 100\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": 2.7777777777777777\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 96\n" +
            "                },\n" +
            "                \"windSpeed\": \"18 to 22 mph\",\n" +
            "                \"windDirection\": \"NE\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/day/snow,100/snow,70?size=medium\",\n" +
            "                \"shortForecast\": \"Rain And Snow\",\n" +
            "                \"detailedForecast\": \"Rain and snow. Cloudy, with a high near 39. Northeast wind 18 to 22 mph, with gusts as high as 33 mph. Chance of precipitation is 100%. New snow accumulation of less than half an inch possible.\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"number\": 6,\n" +
            "                \"name\": \"Saturday Night\",\n" +
            "                \"startTime\": \"2023-03-04T18:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-05T06:00:00-05:00\",\n" +
            "                \"isDaytime\": false,\n" +
            "                \"temperature\": 26,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": null,\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 30\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": 0\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 99\n" +
            "                },\n" +
            "                \"windSpeed\": \"7 to 17 mph\",\n" +
            "                \"windDirection\": \"N\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/night/snow,30?size=medium\",\n" +
            "                \"shortForecast\": \"Chance Light Snow\",\n" +
            "                \"detailedForecast\": \"A chance of snow. Mostly cloudy, with a low around 26. North wind 7 to 17 mph, with gusts as high as 28 mph. Chance of precipitation is 30%.\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"number\": 7,\n" +
            "                \"name\": \"Sunday\",\n" +
            "                \"startTime\": \"2023-03-05T06:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-05T18:00:00-05:00\",\n" +
            "                \"isDaytime\": true,\n" +
            "                \"temperature\": 43,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": null,\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": null\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": 0\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 100\n" +
            "                },\n" +
            "                \"windSpeed\": \"8 to 16 mph\",\n" +
            "                \"windDirection\": \"NW\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/day/snow/bkn?size=medium\",\n" +
            "                \"shortForecast\": \"Slight Chance Light Snow then Partly Sunny\",\n" +
            "                \"detailedForecast\": \"A slight chance of snow before 8am. Partly sunny, with a high near 43. Northwest wind 8 to 16 mph.\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"number\": 8,\n" +
            "                \"name\": \"Sunday Night\",\n" +
            "                \"startTime\": \"2023-03-05T18:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-06T06:00:00-05:00\",\n" +
            "                \"isDaytime\": false,\n" +
            "                \"temperature\": 29,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": null,\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": null\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": -0.55555555555555558\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 87\n" +
            "                },\n" +
            "                \"windSpeed\": \"16 mph\",\n" +
            "                \"windDirection\": \"NW\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/night/few?size=medium\",\n" +
            "                \"shortForecast\": \"Mostly Clear\",\n" +
            "                \"detailedForecast\": \"Mostly clear, with a low around 29. Northwest wind around 16 mph.\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"number\": 9,\n" +
            "                \"name\": \"Monday\",\n" +
            "                \"startTime\": \"2023-03-06T06:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-06T18:00:00-05:00\",\n" +
            "                \"isDaytime\": true,\n" +
            "                \"temperature\": 47,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": null,\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": null\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": -1.6666666666666667\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 88\n" +
            "                },\n" +
            "                \"windSpeed\": \"20 mph\",\n" +
            "                \"windDirection\": \"NW\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/day/few?size=medium\",\n" +
            "                \"shortForecast\": \"Sunny\",\n" +
            "                \"detailedForecast\": \"Sunny, with a high near 47. Northwest wind around 20 mph.\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"number\": 10,\n" +
            "                \"name\": \"Monday Night\",\n" +
            "                \"startTime\": \"2023-03-06T18:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-07T06:00:00-05:00\",\n" +
            "                \"isDaytime\": false,\n" +
            "                \"temperature\": 30,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": null,\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": null\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": -2.2222222222222223\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 82\n" +
            "                },\n" +
            "                \"windSpeed\": \"12 to 17 mph\",\n" +
            "                \"windDirection\": \"NW\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/night/sct/snow?size=medium\",\n" +
            "                \"shortForecast\": \"Partly Cloudy then Slight Chance Light Snow\",\n" +
            "                \"detailedForecast\": \"A slight chance of snow after 3am. Partly cloudy, with a low around 30. Northwest wind 12 to 17 mph.\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"number\": 11,\n" +
            "                \"name\": \"Tuesday\",\n" +
            "                \"startTime\": \"2023-03-07T06:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-07T18:00:00-05:00\",\n" +
            "                \"isDaytime\": true,\n" +
            "                \"temperature\": 44,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": null,\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 30\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": -0.55555555555555558\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 81\n" +
            "                },\n" +
            "                \"windSpeed\": \"13 mph\",\n" +
            "                \"windDirection\": \"NW\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/day/snow,30/rain,30?size=medium\",\n" +
            "                \"shortForecast\": \"Slight Chance Light Snow then Chance Light Rain\",\n" +
            "                \"detailedForecast\": \"A slight chance of snow before 7am, then a chance of rain. Partly sunny, with a high near 44. Northwest wind around 13 mph. Chance of precipitation is 30%.\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"number\": 12,\n" +
            "                \"name\": \"Tuesday Night\",\n" +
            "                \"startTime\": \"2023-03-07T18:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-08T06:00:00-05:00\",\n" +
            "                \"isDaytime\": false,\n" +
            "                \"temperature\": 29,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": null,\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": null\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": -1.1111111111111112\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 82\n" +
            "                },\n" +
            "                \"windSpeed\": \"14 mph\",\n" +
            "                \"windDirection\": \"NW\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/night/bkn?size=medium\",\n" +
            "                \"shortForecast\": \"Mostly Cloudy\",\n" +
            "                \"detailedForecast\": \"Mostly cloudy, with a low around 29. Northwest wind around 14 mph.\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"number\": 13,\n" +
            "                \"name\": \"Wednesday\",\n" +
            "                \"startTime\": \"2023-03-08T06:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-08T18:00:00-05:00\",\n" +
            "                \"isDaytime\": true,\n" +
            "                \"temperature\": 44,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": null,\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": null\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": -0.55555555555555558\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 81\n" +
            "                },\n" +
            "                \"windSpeed\": \"16 mph\",\n" +
            "                \"windDirection\": \"NW\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/day/sct?size=medium\",\n" +
            "                \"shortForecast\": \"Mostly Sunny\",\n" +
            "                \"detailedForecast\": \"Mostly sunny, with a high near 44. Northwest wind around 16 mph.\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"number\": 14,\n" +
            "                \"name\": \"Wednesday Night\",\n" +
            "                \"startTime\": \"2023-03-08T18:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-09T06:00:00-05:00\",\n" +
            "                \"isDaytime\": false,\n" +
            "                \"temperature\": 30,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": null,\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": null\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": -1.1111111111111112\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 86\n" +
            "                },\n" +
            "                \"windSpeed\": \"12 to 15 mph\",\n" +
            "                \"windDirection\": \"NW\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/night/bkn?size=medium\",\n" +
            "                \"shortForecast\": \"Mostly Cloudy\",\n" +
            "                \"detailedForecast\": \"Mostly cloudy, with a low around 30. Northwest wind 12 to 15 mph.\"\n" +
            "            }\n" +
            "        ]\n" +
            "    }\n" +
            "}";

    // Mock result for New York City forecast.
    public static final String mockNewYorkCityForecast = "{\n" +
            "    \"@context\": [\n" +
            "        \"https://geojson.org/geojson-ld/geojson-context.jsonld\",\n" +
            "        {\n" +
            "            \"@version\": \"1.1\",\n" +
            "            \"wx\": \"https://api.weather.gov/ontology#\",\n" +
            "            \"geo\": \"http://www.opengis.net/ont/geosparql#\",\n" +
            "            \"unit\": \"http://codes.wmo.int/common/unit/\",\n" +
            "            \"@vocab\": \"https://api.weather.gov/ontology#\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"type\": \"Feature\",\n" +
            "    \"geometry\": {\n" +
            "        \"type\": \"Polygon\",\n" +
            "        \"coordinates\": [\n" +
            "            [\n" +
            "                [\n" +
            "                    -73.963401300000001,\n" +
            "                    40.741954800000002\n" +
            "                ],\n" +
            "                [\n" +
            "                    -73.967876000000004,\n" +
            "                    40.720275700000002\n" +
            "                ],\n" +
            "                [\n" +
            "                    -73.939279400000004,\n" +
            "                    40.716883700000004\n" +
            "                ],\n" +
            "                [\n" +
            "                    -73.934798900000004,\n" +
            "                    40.7385625\n" +
            "                ],\n" +
            "                [\n" +
            "                    -73.963401300000001,\n" +
            "                    40.741954800000002\n" +
            "                ]\n" +
            "            ]\n" +
            "        ]\n" +
            "    },\n" +
            "    \"properties\": {\n" +
            "        \"updated\": \"2023-03-02T20:47:09+00:00\",\n" +
            "        \"units\": \"us\",\n" +
            "        \"forecastGenerator\": \"BaselineForecastGenerator\",\n" +
            "        \"generatedAt\": \"2023-03-02T21:13:56+00:00\",\n" +
            "        \"updateTime\": \"2023-03-02T20:47:09+00:00\",\n" +
            "        \"validTimes\": \"2023-03-02T14:00:00+00:00/P7DT23H\",\n" +
            "        \"elevation\": {\n" +
            "            \"unitCode\": \"wmoUnit:m\",\n" +
            "            \"value\": 3.048\n" +
            "        },\n" +
            "        \"periods\": [\n" +
            "            {\n" +
            "                \"number\": 1,\n" +
            "                \"name\": \"This Afternoon\",\n" +
            "                \"startTime\": \"2023-03-02T16:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-02T18:00:00-05:00\",\n" +
            "                \"isDaytime\": true,\n" +
            "                \"temperature\": 55,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": null,\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": null\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": 2.2222222222222223\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 49\n" +
            "                },\n" +
            "                \"windSpeed\": \"16 mph\",\n" +
            "                \"windDirection\": \"NW\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/day/bkn?size=medium\",\n" +
            "                \"shortForecast\": \"Partly Sunny\",\n" +
            "                \"detailedForecast\": \"Partly sunny, with a high near 55. Northwest wind around 16 mph, with gusts as high as 26 mph.\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"number\": 2,\n" +
            "                \"name\": \"Tonight\",\n" +
            "                \"startTime\": \"2023-03-02T18:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-03T06:00:00-05:00\",\n" +
            "                \"isDaytime\": false,\n" +
            "                \"temperature\": 34,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": null,\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": null\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": 1.1111111111111112\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 75\n" +
            "                },\n" +
            "                \"windSpeed\": \"6 to 16 mph\",\n" +
            "                \"windDirection\": \"NW\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/night/sct?size=medium\",\n" +
            "                \"shortForecast\": \"Partly Cloudy\",\n" +
            "                \"detailedForecast\": \"Partly cloudy, with a low around 34. Northwest wind 6 to 16 mph, with gusts as high as 26 mph.\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"number\": 3,\n" +
            "                \"name\": \"Friday\",\n" +
            "                \"startTime\": \"2023-03-03T06:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-03T18:00:00-05:00\",\n" +
            "                \"isDaytime\": true,\n" +
            "                \"temperature\": 44,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": \"falling\",\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 50\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": -2.7777777777777777\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 75\n" +
            "                },\n" +
            "                \"windSpeed\": \"6 to 12 mph\",\n" +
            "                \"windDirection\": \"NE\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/day/bkn/sleet,50?size=medium\",\n" +
            "                \"shortForecast\": \"Mostly Cloudy then Chance Sleet\",\n" +
            "                \"detailedForecast\": \"A slight chance of rain between 1pm and 4pm, then a chance of sleet and a chance of rain and snow. Mostly cloudy. High near 44, with temperatures falling to around 42 in the afternoon. Northeast wind 6 to 12 mph. Chance of precipitation is 50%.\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"number\": 4,\n" +
            "                \"name\": \"Friday Night\",\n" +
            "                \"startTime\": \"2023-03-03T18:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-04T06:00:00-05:00\",\n" +
            "                \"isDaytime\": false,\n" +
            "                \"temperature\": 41,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": \"rising\",\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 100\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": 5\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 93\n" +
            "                },\n" +
            "                \"windSpeed\": \"13 to 23 mph\",\n" +
            "                \"windDirection\": \"E\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/night/sleet,100/rain_showers,100?size=medium\",\n" +
            "                \"shortForecast\": \"Chance Sleet then Rain Showers\",\n" +
            "                \"detailedForecast\": \"A chance of sleet and rain and snow before 10pm, then rain showers between 10pm and 4am, then rain. Cloudy. Low around 41, with temperatures rising to around 43 overnight. East wind 13 to 23 mph, with gusts as high as 35 mph. Chance of precipitation is 100%. New rainfall amounts between 1 and 2 inches possible.\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"number\": 5,\n" +
            "                \"name\": \"Saturday\",\n" +
            "                \"startTime\": \"2023-03-04T06:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-04T18:00:00-05:00\",\n" +
            "                \"isDaytime\": true,\n" +
            "                \"temperature\": 46,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": \"falling\",\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 100\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": 4.4444444444444446\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 89\n" +
            "                },\n" +
            "                \"windSpeed\": \"12 to 16 mph\",\n" +
            "                \"windDirection\": \"N\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/day/rain,100/rain,50?size=medium\",\n" +
            "                \"shortForecast\": \"Light Rain\",\n" +
            "                \"detailedForecast\": \"Rain. Mostly cloudy. High near 46, with temperatures falling to around 43 in the afternoon. North wind 12 to 16 mph, with gusts as high as 26 mph. Chance of precipitation is 100%. New rainfall amounts between a half and three quarters of an inch possible.\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"number\": 6,\n" +
            "                \"name\": \"Saturday Night\",\n" +
            "                \"startTime\": \"2023-03-04T18:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-05T06:00:00-05:00\",\n" +
            "                \"isDaytime\": false,\n" +
            "                \"temperature\": 36,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": null,\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": null\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": 1.1111111111111112\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 76\n" +
            "                },\n" +
            "                \"windSpeed\": \"12 mph\",\n" +
            "                \"windDirection\": \"NW\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/night/rain/snow?size=medium\",\n" +
            "                \"shortForecast\": \"Slight Chance Light Rain then Slight Chance Snow Showers\",\n" +
            "                \"detailedForecast\": \"A slight chance of rain before 7pm, then a slight chance of rain showers between 1am and 4am, then a slight chance of snow showers. Mostly cloudy, with a low around 36.\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"number\": 7,\n" +
            "                \"name\": \"Sunday\",\n" +
            "                \"startTime\": \"2023-03-05T06:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-05T18:00:00-05:00\",\n" +
            "                \"isDaytime\": true,\n" +
            "                \"temperature\": 49,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": null,\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": null\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": 0\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 75\n" +
            "                },\n" +
            "                \"windSpeed\": \"10 to 18 mph\",\n" +
            "                \"windDirection\": \"NW\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/day/snow/sct?size=medium\",\n" +
            "                \"shortForecast\": \"Slight Chance Snow Showers then Mostly Sunny\",\n" +
            "                \"detailedForecast\": \"A slight chance of snow showers before 7am. Mostly sunny, with a high near 49.\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"number\": 8,\n" +
            "                \"name\": \"Sunday Night\",\n" +
            "                \"startTime\": \"2023-03-05T18:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-06T06:00:00-05:00\",\n" +
            "                \"isDaytime\": false,\n" +
            "                \"temperature\": 35,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": null,\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": null\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": -1.1111111111111112\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 72\n" +
            "                },\n" +
            "                \"windSpeed\": \"12 to 18 mph\",\n" +
            "                \"windDirection\": \"NW\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/night/few?size=medium\",\n" +
            "                \"shortForecast\": \"Mostly Clear\",\n" +
            "                \"detailedForecast\": \"Mostly clear, with a low around 35.\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"number\": 9,\n" +
            "                \"name\": \"Monday\",\n" +
            "                \"startTime\": \"2023-03-06T06:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-06T18:00:00-05:00\",\n" +
            "                \"isDaytime\": true,\n" +
            "                \"temperature\": 50,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": null,\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": null\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": -2.2222222222222223\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 72\n" +
            "                },\n" +
            "                \"windSpeed\": \"15 mph\",\n" +
            "                \"windDirection\": \"NW\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/day/few?size=medium\",\n" +
            "                \"shortForecast\": \"Sunny\",\n" +
            "                \"detailedForecast\": \"Sunny, with a high near 50.\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"number\": 10,\n" +
            "                \"name\": \"Monday Night\",\n" +
            "                \"startTime\": \"2023-03-06T18:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-07T06:00:00-05:00\",\n" +
            "                \"isDaytime\": false,\n" +
            "                \"temperature\": 38,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": null,\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 30\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": -0.55555555555555558\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 73\n" +
            "                },\n" +
            "                \"windSpeed\": \"10 to 14 mph\",\n" +
            "                \"windDirection\": \"NW\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/night/sct/rain_showers,30?size=medium\",\n" +
            "                \"shortForecast\": \"Partly Cloudy then Chance Rain Showers\",\n" +
            "                \"detailedForecast\": \"A chance of rain showers after 1am. Partly cloudy, with a low around 38. Chance of precipitation is 30%.\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"number\": 11,\n" +
            "                \"name\": \"Tuesday\",\n" +
            "                \"startTime\": \"2023-03-07T06:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-07T18:00:00-05:00\",\n" +
            "                \"isDaytime\": true,\n" +
            "                \"temperature\": 52,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": null,\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 30\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": 0.55555555555555558\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 76\n" +
            "                },\n" +
            "                \"windSpeed\": \"9 to 17 mph\",\n" +
            "                \"windDirection\": \"NW\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/day/rain_showers,30/rain_showers?size=medium\",\n" +
            "                \"shortForecast\": \"Chance Rain Showers\",\n" +
            "                \"detailedForecast\": \"A chance of rain showers before 1pm. Partly sunny, with a high near 52. Chance of precipitation is 30%.\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"number\": 12,\n" +
            "                \"name\": \"Tuesday Night\",\n" +
            "                \"startTime\": \"2023-03-07T18:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-08T06:00:00-05:00\",\n" +
            "                \"isDaytime\": false,\n" +
            "                \"temperature\": 35,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": null,\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": null\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": -1.6666666666666667\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 64\n" +
            "                },\n" +
            "                \"windSpeed\": \"17 mph\",\n" +
            "                \"windDirection\": \"NW\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/night/sct?size=medium\",\n" +
            "                \"shortForecast\": \"Partly Cloudy\",\n" +
            "                \"detailedForecast\": \"Partly cloudy, with a low around 35.\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"number\": 13,\n" +
            "                \"name\": \"Wednesday\",\n" +
            "                \"startTime\": \"2023-03-08T06:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-08T18:00:00-05:00\",\n" +
            "                \"isDaytime\": true,\n" +
            "                \"temperature\": 47,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": null,\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": null\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": -3.3333333333333335\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 61\n" +
            "                },\n" +
            "                \"windSpeed\": \"18 mph\",\n" +
            "                \"windDirection\": \"NW\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/day/sct?size=medium\",\n" +
            "                \"shortForecast\": \"Mostly Sunny\",\n" +
            "                \"detailedForecast\": \"Mostly sunny, with a high near 47.\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"number\": 14,\n" +
            "                \"name\": \"Wednesday Night\",\n" +
            "                \"startTime\": \"2023-03-08T18:00:00-05:00\",\n" +
            "                \"endTime\": \"2023-03-09T06:00:00-05:00\",\n" +
            "                \"isDaytime\": false,\n" +
            "                \"temperature\": 33,\n" +
            "                \"temperatureUnit\": \"F\",\n" +
            "                \"temperatureTrend\": null,\n" +
            "                \"probabilityOfPrecipitation\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": null\n" +
            "                },\n" +
            "                \"dewpoint\": {\n" +
            "                    \"unitCode\": \"wmoUnit:degC\",\n" +
            "                    \"value\": -3.8888888888888888\n" +
            "                },\n" +
            "                \"relativeHumidity\": {\n" +
            "                    \"unitCode\": \"wmoUnit:percent\",\n" +
            "                    \"value\": 69\n" +
            "                },\n" +
            "                \"windSpeed\": \"14 to 17 mph\",\n" +
            "                \"windDirection\": \"NW\",\n" +
            "                \"icon\": \"https://api.weather.gov/icons/land/night/sct?size=medium\",\n" +
            "                \"shortForecast\": \"Partly Cloudy\",\n" +
            "                \"detailedForecast\": \"Partly cloudy, with a low around 33.\"\n" +
            "            }\n" +
            "        ]\n" +
            "    }\n" +
            "}";

    // Expected result from our Server, for the mock NYC data.
    public static final String mockServerExpectedNewYorkCity = "{\"result\":\"success\",\"lat\":40.73,\"lon\":-73.94,\"temperature\":51,\"unit\":\"F\",\"timestamp\":\"2023-03-02T19:53:16+00:00\"}";

    // Expected failure result from our Server, for the mock NYC data.
    public static final String mockExpectedWeatherFailure = "{\"result\":\"error_bad_json\",\"error_message\":\"Some error message!\"}";
}
