package org.mapsforge.core.util;

/**
 * Created by ruinalef on 10/12/15.
 */
public class GeoProjection implements MapProjection {
    /**
     * The circumference of the earth at the equator in meters.
     */
    private static final double EARTH_CIRCUMFERENCE = 40075016.686;

    private double lon0;
    private double lat0;
    private double kx0;
    private double ky0;
    private double kx;
    private double ky;
    private double width;
    private double height;

    public GeoProjection(MapConfiguration config, int tileWidth, int tileHeight, byte zoom) {
        width = tileWidth * config.getTileXCount();
        height = tileHeight * config.getTileYCount();
        lon0 = config.getMapLeftLon();
        lat0 = config.getMapUpperLat();
        kx = kx0 = width / config.widthInDegrees();
        ky = ky0 = height / config.heightInDegrees();
        setZoom(zoom);
    }

    private void setZoom(byte zoom) {
        double scale = Math.pow(2, zoom);
        kx = kx0 * scale;
        ky = ky0 * scale;
        width *= scale;
        height *= scale;
    }

    @Override
    public double getMapWidth() {
        return width;
    }

    @Override
    public double getMapHeight() {
        return height;
    }

    @Override
    public double latitudeToPixelY(double latitude) {
        return ky * (lat0 - latitude);
    }

    @Override
    public double longitudeToPixelX(double longitude) {
        return kx * (longitude - lon0);
    }

    @Override
    public double pixelXToLongitude(double pixelX) {
        return lon0 + pixelX/kx;
    }

    @Override
    public double pixelYToLatitude(double pixelY) {
        return lat0 - pixelY / ky;
    }

    @Override
    public double calculateGroundResolution(double latitude) {
        return Math.cos(latitude * (Math.PI / 180)) * EARTH_CIRCUMFERENCE / 360 / kx;
    }

    @Override
    public double metersToPixels(float meters, double latitude) {
        return  meters / calculateGroundResolution(latitude);
    }
}
