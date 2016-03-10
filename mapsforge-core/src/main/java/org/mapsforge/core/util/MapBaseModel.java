package org.mapsforge.core.util;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Tile;

/**
 * Created by ruinalef on 10/7/15.
 */
public class MapBaseModel implements MapModel {

    private MapConfiguration mConfig;
    private MapProjection[] mProjection;
    private int tileWidth;
    private int tileHeight;

    public MapBaseModel(MapConfiguration config) {
        this(config, config.getMapWidth() / config.getTileXCount());
    }

    public MapBaseModel(MapConfiguration config, int tileSize) {
        mConfig = config;
        tileWidth = tileSize;
        double tileWidth0 = config.getMapWidth() / config.getTileXCount();
        double tileHeight0 = config.getMapHeight() / config.getTileYCount();
        double r = (double)tileHeight0 / tileWidth0;
        tileHeight = (int) Math.round(tileSize * r);
        mProjection = new MapProjection[MAX_ZOOM];
        for(byte i=0; i< MAX_ZOOM; i++) {
            if (MapConfiguration.MERCATOR_PROJECTION.equals(config.getProjection())) {
                mProjection[i] = new MercatorProjection(config, tileWidth, tileHeight, i);
            } else {
                mProjection[i] = new GeoProjection(config, tileWidth, tileHeight, i);
            }
        }
    }

    @Override
    public int getTileWidth() {
        return tileWidth;
    }

    @Override
    public int getTileHeight() {
        return tileHeight;
    }

    @Override
    public MapProjection getProjection(byte zoom) {
        return mProjection[zoom];
    }

    @Override
    public int tileXCount(int zoom) {
        return mConfig.getTileXCount() * (1<<zoom);
    }

    @Override
    public int tileYCount(int zoom) {
        return mConfig.getTileYCount() * (1<<zoom);
    }

    @Override
    public double tilePixelX(int tileX) {
        return tileWidth * tileX;
    }

    @Override
    public double tilePixelY(int tileY) {
        return tileHeight * tileY;
    }

    @Override
    public int latitudeToTileY(double latitude, byte zoomLevel) {
        return pixelYToTileY(getProjection(zoomLevel).latitudeToPixelY(latitude), zoomLevel);
    }

    @Override
    public int longitudeToTileX(double longitude, byte zoomLevel) {
        return pixelXToTileX(getProjection(zoomLevel).longitudeToPixelX(longitude), zoomLevel);
    }

    @Override
    public double tileXToLongitude(long tileX, byte zoomLevel) {
        return getProjection(zoomLevel).pixelXToLongitude(tilePixelX((int) tileX));
    }

    @Override
    public double tileYToLatitude(long tileY, byte zoomLevel) {
        return getProjection(zoomLevel).pixelYToLatitude(tilePixelY((int) tileY));
    }

    @Override
    public BoundingBox getTileBoundingBox(Tile tile) {
        MapProjection projection = getProjection(tile.zoomLevel);
        double tilePixelX = tilePixelX(tile.tileX);
        double tilePixelY = tilePixelY(tile.tileY);
        double minLatitude = projection.pixelYToLatitude(tilePixelY + tileHeight);
        double minLongitude = projection.pixelXToLongitude(tilePixelX);
        double maxLatitude = projection.pixelYToLatitude(tilePixelY);
        double maxLongitude = projection.pixelXToLongitude(tilePixelX + tileWidth);
        if (maxLongitude == -180) {
            // fix for dateline crossing, where the right tile starts at -180 and causes an invalid bbox
            maxLongitude = 180;
        }
        return new BoundingBox(minLatitude, minLongitude, maxLatitude, maxLongitude);

    }

    @Override
    public boolean supportsTile(Tile tile) {
        return tile.tileX < tileXCount(tile.zoomLevel) && tile.tileY < tileYCount(tile.zoomLevel);
    }

    /**
     * Converts a pixel Y coordinate to the tile Y number.
     *
     * @param pixelY
     *            the pixel Y coordinate that should be converted.
     * @param zoomLevel
     *            the zoom level at which the coordinate should be converted.
     * @return the tile Y number.
     */
    private int pixelYToTileY(double pixelY, byte zoomLevel) {
        return (int) Math.min(Math.max(pixelY / tileHeight, 0), tileYCount(zoomLevel) - 1);
    }

    /**
     * Converts a pixel X coordinate to the tile X number.
     *
     * @param pixelX
     *            the pixel X coordinate that should be converted.
     * @param zoomLevel
     *            the zoom level at which the coordinate should be converted.
     * @return the tile X number.
     */
    private int pixelXToTileX(double pixelX, byte zoomLevel) {
        return (int) Math.min(Math.max(pixelX / tileWidth, 0), tileXCount(zoomLevel) - 1);
    }
}
