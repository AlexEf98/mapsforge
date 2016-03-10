package org.mapsforge.core.util;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Tile;

/**
 * Created by ruinalef on 10/6/15.
 */
public interface MapModel extends MapProjectionProvider {
    public static final int MAX_ZOOM = 21;

    int getTileWidth();
    int getTileHeight();
    int tileXCount(int zoom);
    int tileYCount(int zoom);
    double tilePixelX(int tileX);
    double tilePixelY(int tileY);
    /**
     * Converts a latitude coordinate (in degrees) to a tile Y number at a certain zoom level.
     *
     * @param latitude
     *            the latitude coordinate that should be converted.
     * @param zoomLevel
     *            the zoom level at which the coordinate should be converted.
     * @return the tile Y number of the latitude value.
     */
    int latitudeToTileY(double latitude, byte zoomLevel);

    /**
     * Converts a longitude coordinate (in degrees) to the tile X number at a certain zoom level.
     *
     * @param longitude
     *            the longitude coordinate that should be converted.
     * @param zoomLevel
     *            the zoom level at which the coordinate should be converted.
     * @return the tile X number of the longitude value.
     */
    int longitudeToTileX(double longitude, byte zoomLevel);

    /**
     * Converts a tile X number at a certain zoom level to a longitude coordinate.
     *
     * @param tileX
     *            the tile X number that should be converted.
     * @param zoomLevel
     *            the zoom level at which the number should be converted.
     * @return the longitude value of the tile X number.
     */
    double tileXToLongitude(long tileX, byte zoomLevel);

    /**
     * Converts a tile Y number at a certain zoom level to a latitude coordinate.
     *
     * @param tileY
     *            the tile Y number that should be converted.
     * @param zoomLevel
     *            the zoom level at which the number should be converted.
     * @return the latitude value of the tile Y number.
     */
    double tileYToLatitude(long tileY, byte zoomLevel);

    BoundingBox getTileBoundingBox(Tile tile);
    boolean supportsTile(Tile tile);
}
