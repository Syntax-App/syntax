package edu.brown.cs.student.main.server.weather;

import java.util.concurrent.TimeUnit;

/**
 * A constants class for caching parameters. Developer users can change these constants to adjust caching requirements.
 */
public class CacheParams {
    // the max distance between two coordinates to cache response
    public static final double PROXIMITY_RADIUS = 0.3;
    // the max number of caches
    public static final int CACHE_MAX_SIZE = 10;
    // the expiration time of caches
    public static final int CACHE_EXPIRE_AFTER = 2;
    // time unit for expiration time
    public static final TimeUnit CACHE_EXPIRE_AFTER_TIMEUNIT = TimeUnit.HOURS;
}
