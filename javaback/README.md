# Sprint 3: Server


## Project Details
cs logins: `hmasamur`, `abenjell` <br />
Total estimated time: 12 hours <br />
[Github Repo](https://github.com/cs0320-s2023/sprint-3-abenjell-hmasamur)  <br />

## Overview
Our server project consists of one main class Server, 4 different handlers for the various commands, and additional
classes/records to facilitate the handling of `loadcsv`, `searchcsv`, `viewcsv`, and `weather`. The handlers each have
a `handle()` method which handles the commands and return a `SuccessResponse` or `FailureResponse` accordingly.

The `States` class stores shared variables between these different handlers, and the `Response` class contains records
that correspond to particular sections of the JSON response returned from the NWS Weather API.

The `weather` directory also contains additional classes and records, which are all used to handle the `weather` command
and facilitate the caching of responses from NWS. The details of these classes/records are outlined in Design Choices.

### File Structure
```
sprint-3-abenjell-hmasamur/
├── data/
│   ├── persons/
│   │   ├── names-invalid.csv
│   │   ├── names-no-header.csv
│   │   ├── names-part-invalid.csv
│   │   └── names-with-header.csv
│   ├── stars/
│   │   ├── star-part-invalid.csv
│   │   ├── stardata.csv
│   │   └── ten-star.csv
│   └── empty.csv
├── src/
│   ├── main/
│   │   └── java/
│   │       └── edu.brown.cs.student.main/
│   │           ├── csv/
│   │           │   ├── creators/
│   │           │   │   ├── CreatorFromRow.java
│   │           │   │   ├── FactoryFailureException.java
│   │           │   │   ├── ListCreator.java
│   │           │   │   ├── PersonCreator.java
│   │           │   │   └── StarCreator.java
│   │           │   ├── objectTypes/
│   │           │   │   ├── Person.java
│   │           │   │   └── Star.java
│   │           │   ├── Main.java
│   │           │   ├── Parser.java
│   │           │   └── Searcher.java
│   │           └── server/
│   │               ├── csvHandlers/
│   │               │   ├── LoadHandler.java
│   │               │   ├── SearchHandler.java
│   │               │   └── ViewHandler.java
│   │               ├── weather/
│   │               │   ├── CacheParams.java
│   │               │   ├── GeoCoord.java
│   │               │   ├── WeatherHandler.java
│   │               │   ├── WeatherProxy.java
│   │               │   ├── WeatherResponse.java
│   │               │   └── WeatherUtils.java
│   │               ├── Response.java
│   │               ├── Server.java
│   │               └── States.java
│   └── test/
│       └── java/
│           └── edu.brown.cs.student.main/
│               ├── mocks/
│               │   ├── MockJSON.java
│               │   └── MockWeatherUtils.java
│               ├── TestAPI.java
│               ├── TestAPIUtilities.java
│               ├── TestParser.java
│               └── TestSearcher.java
├── .gitignore
├── index.html
├── pom.xml
└── README.md
```

## Design Choices

- For the CSV functionality of our API, we have created a class called `States`,
  and instantiated one common instance that we passed in to the CSV related handlers.
  This class keeps track of the "current" loaded CSV file, as well as its header if it exists.

- We have created a `SerializeHelper` class, with a static method `helpSerialize` which, given a HashMap,
takes care of serializing it into a JSON formatted `String`.

- Given that handling weather requests required multiple sub-steps, 
we created multiple specialized classes, most of which are under the `weather/` folder. 
Here's how they work together:

  - `WeatherUtils` houses essential helper methods for getting and parsing data from
    the NWS API. Its methods are called in the `WeatherHandler`.
  - `WeatherProxy` wraps a `WeatherUtils` class and adds on its functionality
    to support caching. It makes use of the Google Guava library.
  - `WeatherHandler` inherits from a standard Moshi handler and takes care of connecting
    all the functionality together. Using the methods in the `WeatherUtils` and `WeatherProxy` (caching),
    it fetches the API, parses the data, and returns it in a JSON form using Moshi's `serialize` method.
  - `CacheParams` include default parameters for the caching system - the cache's max size and how long data
    stays in the cache before expiry. Changing these parameters is explained in more details in the "How To" section
    below.
  - `GeoCoord` is a record representing a geographical coordinate (latitude, longitude). Instances of
    this record are used as keys to fetch the data stored in the cache. We have chosen to use a record specifically
    because it allowed us to store both coordinates into one structure, and had the `.equals` and `.hashCode` method
    automatically implemented for us.
  - `WeatherResponse` is a record meant to hold our Server's final Weather API responses, i.e.,
    a temperature, the unit for the temperature, and the timestamp for the request.
  - `Response`, housed outside the `weather/` folder, contains multiple classes
    useful for parsing the different JSON data returned by the NWS API into queryable
    records.
- For backend testing, we have created two classes for the sole purpose of mocking:
  `MockJSON` and `MockWeatherUtils`:
  - `MockJSON` contains multiple mock responses from the NWS API/expected responses from our
    server. The data is used extensively in backend tests (i.e., in the `TestAPIUtilities` file).
  - `MockWeatherUtils` extends the `WeatherUtils` class and overrides the usual `getForecast` method
    to provide mock data. This is mostly used to test caching.

## Testing

### Integration Tests
- loadcsv
  - loading with no filepath param
  - loading a nonexistent file
  - loading a file with invalid hasHeader
  - loading an empty file
  - loading without hasHeader param
  - loading with hasHeader = true
- viewcsv
  - viewing before loading a file
  - viewing with file loaded
- searchcsv
  - searching when file isn't loaded
  - searching with no specified search value
  - searching when column is out of range
  - searching with nonexistent column header
  - searching with specified column when there's no header in file
  - searching a loaded file with header
- weather
  - getting weather for Providence coords
  - getting weather with double coordinates with many digits
  - getting weather with invalid coords
  - getting weather without <lat> param
  - getting weather without <lon> param
  - getting weather with coords that are not numbers

### Unit Tests
- converting Reader objects (from the API call)
  to Strings properly.
- given a set of coordinates, forming the NWS URL to call
  properly.
- given a set of coordinates with many decimal places, forming the
  NWS URL by rounding these coords to the second decimal place.
- extracting the URL with forecast data from the initial NWS API call.
- parsing the forecast data into a queryable `ForecastResponse` record.
- getting a final `WeatherResponse` instance from a `ForecastResponse`,
  with valid temperature, unit, and timestamp.
- serializing a `WeatherSuccess` from a `WeatherResponse` into a response JSON.
- serializing a `WeatherFailure` into an error JSON response.
- automatic caching by the `WeatherProxy` when attempting to get a forecast.
- getting a forecast for coordinates close to previously cached coordinates -
  we ensured that the forecast returned was the previously cached forecast.
- expiration of the cache after the delay given, as a parameter, to
  the `WeatherProxy`.
- getting a forecast for uncached coordinates - we ensured that
  the `WeatherProxy` does indeed pass in the exact coordinates to
  the `WeatherUtils`'s getForecast.


## Errors/Bugs
There are no known bugs.

## How to...
### Run Tests
We have written two test files:
- `TestAPI.java` includes all Integration tests.
- `TestAPIUtilities.java` includes all Backend unit tests.

These are traditional JUnit tests and can be run using the Play button in IntelliJ!

### Run the Program
The program can be run by clicking the play button in the `Server.java` file,
or by compiling it and running it with the `java` Shell command.

### Change Cache Parameters
We provide any developer using our Server with the ability to tweak parameters
for the caching functionality. We provide a `CacheParams` class, which contains parameters directly used by
the WeatherHandler for caching:
- `PROXIMITY_RADIUS` is the parameter which determines the criteria
  for proximity between two geo coordinates. Specifically, it is the maximum
  Pythagorean distance tolerated between two latitude/longitude coordinates to
  consider the points "close enough" to return the same forecast.
  In other words, it determines the sphere around a specific geo
  coordinate which forecast would be considered identical.
- `CACHE_MAX_SIZE` determines the maximum number of entries in the cache.
- `CACHE_EXPIRE_AFTER` and `CACHE_EXPIRE_AFTER_TIMEUNIT` determine how long
  an entry is cached, and thus considered to be a valid forecast.