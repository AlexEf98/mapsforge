package org.mapsforge.core.util;

/**
 * Created by ruinalef on 10/6/15.
 */
public class MercatorProjection implements MapProjection {

    /**
     * The circumference of the earth at the equator in meters.
     */
    private static final double EARTH_CIRCUMFERENCE = 40075016.686;

    private double lon0;
    private double y0;
    private double kx0;
    private double ky0;
    private double kx;
    private double ky;
    private double width;
    private double height;

    public MercatorProjection(MapConfiguration config, int tileWidth, int tileHeight, byte zoom) {
        width = tileWidth * config.getTileXCount();
        height = tileHeight * config.getTileYCount();
        lon0 = config.getMapLeftLon();
        double lat0 = config.getMapUpperLat();
        y0 = Math.log(Math.tan(lat0 * Math.PI / 360 + Math.PI / 4));
        kx = kx0 = width / config.widthInDegrees();
        ky = ky0 = height * config.getMapWidth() * 180 / Math.PI / config.getMapHeight() / config.widthInDegrees();
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
        return ky * (y0 - Math.log(Math.tan(latitude * Math.PI / 360 + Math.PI / 4)));
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
        return 360 * Math.atan(Math.exp(y0 - pixelY/ky)) / Math.PI - 90;
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
