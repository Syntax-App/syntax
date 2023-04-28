package edu.brown.cs.student.main.server.weather;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.UncheckedExecutionException;

/**
 * WeatherProxy class that acts as an intermediary b/w WeatherHandler and WeatherUtils for caching.
 */
public class WeatherProxy {
    private final LoadingCache<GeoCoord, WeatherResponse> cache;
    private final double proximityRadius;

    /**
     * WeatherProxy constructor. Allows for specification of the cache's max size, and how long
     * the data is kept in the cache.
     * @param proximityRadius - max distance b/w coords to cach response
     * @param weatherUtils - WeatherUtils to be wrapped
     * @param cacheMaxSize - max number of cached data
     * @param expireAfter - the expiration time for cached data
     * @param expireAfterUnit - expiration time unit
     */
    public WeatherProxy(WeatherUtils weatherUtils, double proximityRadius, int cacheMaxSize, int expireAfter, TimeUnit expireAfterUnit) {
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(cacheMaxSize)
                .expireAfterWrite(expireAfter, expireAfterUnit)
                .recordStats()
                .build(
                        new CacheLoader<GeoCoord, WeatherResponse>() {
                            @Override
                            public WeatherResponse load(GeoCoord coords) throws IOException {
                                // If this isn't yet present in the cache, load it:
                                return weatherUtils.getForecast(coords);
                            }
                        });
        this.proximityRadius = proximityRadius;
    }

    /**
     * Returns the closest key (GeoCoord) to the new query if distance b/w them is less than specified radius. Otherwise
     * returns the GeoCoord inputted. Public for testing purposes.
     * @param coords - GeoCoord to look for the closest key for
     * @return - closest kv pair's key in the cache or the passed-in coords if none is found
     */
    public GeoCoord getClosestKey(GeoCoord coords) {
        Map<GeoCoord, WeatherResponse> cacheMap = this.cache.asMap();
        for (GeoCoord key : cacheMap.keySet()) {
            if (coords.getDistance(key) <= this.proximityRadius) {
                return key;
            }
        }
        return coords;
    }

    /**
     * Gets the forecast of a given GeoCoord.
     * @param coords - GeoCoord to get forecast for
     * @return - the WeatherResponse for the given GeoCoord
     * @throws UncheckedExecutionException - if getUnchecked inner call to load() throws any exceptions
     */
    public WeatherResponse getForecast(GeoCoord coords) throws UncheckedExecutionException {
        GeoCoord cachedCoords = this.getClosestKey(coords);
        WeatherResponse result = this.cache.getUnchecked(cachedCoords);
        System.out.println(this.cache.stats());
        return result;
    }

    /**
     * Method to check weather the cache contains given key. *for testing purposes*
     * @param coord - GeoCoord key to check for
     * @return - true if in cache, false otherwise
     */
    public boolean isCachedExact(GeoCoord coord) {
        return this.cache.asMap().containsKey(coord);
    }
}
