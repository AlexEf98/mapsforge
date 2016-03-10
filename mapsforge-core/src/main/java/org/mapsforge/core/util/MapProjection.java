package org.mapsforge.core.util;

/**
 * Created by ruinalef on 10/6/15.
 */
public interface MapProjection {
    double getMapWidth();
    double getMapHeight();
    /**
     * Converts a latitude coordinate (in degrees) to a pixel Y coordinate at a certain zoom level.
     *
     * @param latitude
     *            the latitude coordinate that should be converted.
     * @return the pixel Y coordinate of the latitude value.
     */
    double latitudeToPixelY(double latitude);

    /**
     * Converts a longitude coordinate (in degrees) to a pixel X coordinate at a certain zoom level.
     *
     * @param longitude
     *            the longitude coordinate that should be converted.
     * @return the pixel X coordinate of the longitude value.
     */
    double longitudeToPixelX(double longitude);

    /**
     * Converts a pixel X coordinate at a certain zoom level to a longitude coordinate.
     *
     * @param pixelX
     *            the pixel X coordinate that should be converted.
     * @return the longitude value of the pixel X coordinate.
     * @throws IllegalArgumentException
     *             if the given pixelX coordinate is invalid.
     */

    double pixelXToLongitude(double pixelX);

    /**
     * Converts a pixel Y coordinate at a certain zoom level to a latitude coordinate.
     *
     * @param pixelY
     *            the pixel Y coordinate that should be converted.
     * @return the latitude value of the pixel Y coordinate.
     * @throws IllegalArgumentException
     *             if the given pixelY coordinate is invalid.
     */
    double pixelYToLatitude(double pixelY);

    /**
     * Calculates the distance on the ground that is represented by a single pixel on the map.
     *
     * @param latitude
     *            the latitude coordinate at which the resolution should be calculated.
     * @return the ground resolution at the given latitude and zoom level.
     */
    double calculateGroundResolution(double latitude);

    /**
     * Converts meters to pixels at latitude for zoom-level.
     *
     * @param meters
     *            the meters to convert
     * @param latitude
     *            the latitude for the conversion.
     * @return pixels that represent the meters at the given zoom-level and latitude.
     */
    double metersToPixels(float meters, double latitude);
}
