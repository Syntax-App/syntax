package edu.brown.cs.student.main.server.weather;

import java.lang.Math;

/**
 * A record representing coordinates. Has a getDistance() method to calculate distance between 2 coordinates.
 */
public record GeoCoord (double lat, double lon) {

    /**
     * Calculates the distance between two GeoCoord instances.
     * @param geoCoord - the GeoCoord instance to calculate distance with
     * @return - the distance
     */
    public double getDistance(GeoCoord geoCoord) {
        return Math.sqrt(Math.pow((geoCoord.lat() - this.lat),2) + Math.pow((geoCoord.lon() - this.lon),2));
    }
}
