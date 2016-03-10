package org.mapsforge.core.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by ruinalef on 10/8/15.
 */
public class MercatorProjectionImplTest {
    @Test
    public void pixelYToLatitudeTest() {
        MapConfiguration mapConfiguration = new MapConfiguration.Builder().build();
        MapProjection projection = new MercatorProjection(mapConfiguration, 256, 256, (byte) 0);
        double lat = projection.pixelYToLatitude(0);
        Assert.assertEquals(85.05112877980659, lat, 0);
        lat = projection.pixelYToLatitude(128);
        Assert.assertEquals(0, lat, 0.000000001);
    }

    @Test
    public void latitudeToPixelYTest() {
        MapConfiguration mapConfiguration = new MapConfiguration.Builder().build();
        MapProjection projection = new MercatorProjection(mapConfiguration, 256, 256, (byte) 0);
        double y = projection.latitudeToPixelY(85.05112877980659);
        Assert.assertEquals(0, y, 0);
        y = projection.latitudeToPixelY(0);
        Assert.assertEquals(128, y, 0.000000001);
    }

    @Test
    public void maxLatTest() {
        MapConfiguration config = new MapConfiguration.Builder().build();
        double kx= config.widthInPixels() / config.widthInDegrees();
        double ky= 180 * kx / Math.PI;
        double maxLat = 360 * Math.atan(Math.exp(128/ky)) / Math.PI - 90;
        Assert.assertEquals(85.05112877980659, maxLat, 0);
        double y = ky * Math.log(Math.tan(maxLat * Math.PI / 360 + Math.PI / 4));
        Assert.assertEquals(128, y, 0.000000001);
    }

    @Test
    public void v2vMapTest() {
        MapConfiguration.Builder builder = new MapConfiguration.Builder();
        builder.setMapWidth(1513)
               .setMapHeight(1953)
               .setMapUpperLat(39.5)
               .setMapBottomLat(39)
               .setMapLeftLon(-77.5)
               .setMapRightLon(-77);
        MapConfiguration mapConfiguration = builder.build();
        MapProjection projection = new MercatorProjection(mapConfiguration, 1513, 1953, (byte) 0);
        double topY = projection.latitudeToPixelY(39.5);
        Assert.assertEquals(0, topY, 0);
        double bottomY = projection.latitudeToPixelY(39);
        Assert.assertEquals(1953, bottomY, 0.99);
        double left34X = projection.longitudeToPixelX(-77.125);
        double right34X = projection.longitudeToPixelX(-77);
        Assert.assertEquals(1513, right34X, 0.99);
        double top34Y = projection.longitudeToPixelX(39);
        Assert.assertEquals(1513, right34X, 0.99);
    }

    @Test
    public void v2vMapTest2() {
        MapConfiguration.Builder builder = new MapConfiguration.Builder();
        builder.setMapWidth(1513)
                .setMapHeight(1941)
                .setMapUpperLat(39)
                .setMapBottomLat(38.5)
                .setMapLeftLon(-77.5)
                .setMapRightLon(-77);
        MapConfiguration mapConfiguration = builder.build();
        MapProjection projection = new MercatorProjection(mapConfiguration, 1513, 1941, (byte) 0);
        double topY = projection.latitudeToPixelY(39);
        Assert.assertEquals(0, topY, 0);
        double bottomY = projection.latitudeToPixelY(38.5);
        Assert.assertEquals(1941, bottomY, 0.99);
    }

    @Test
    public void v2vMapTest5_34() {
        MapConfiguration.Builder builder = new MapConfiguration.Builder();
        builder.setMapWidth(1513)
                .setMapHeight(1947)
                .setMapUpperLat(39.5)
                .setMapBottomLat(38.5)
                .setMapLeftLon(-77.5)
                .setMapRightLon(-76.5);
        MapConfiguration mapConfiguration = builder.build();
        MapProjection projection = new MercatorProjection(mapConfiguration, 1513, 1947, (byte) 5);
        double topY = projection.latitudeToPixelY(39.5);
        Assert.assertEquals(0, topY, 0);
        double mY = projection.latitudeToPixelY(39);
        System.out.println("test5_34 lat=39->Y=" + mY);
        double mX = projection.longitudeToPixelX(-77);
        System.out.println("test5_34 lon=-77->X=" + mX);

    }

    @Test
    public void v2vMapTest6_34() {
        MapConfiguration.Builder builder = new MapConfiguration.Builder();
        builder.setMapWidth(1513)
                .setMapHeight(1947)
                .setMapUpperLat(39.5)
                .setMapBottomLat(38.5)
                .setMapLeftLon(-77.5)
                .setMapRightLon(-76.5);
        MapConfiguration mapConfiguration = builder.build();
        MapProjection projection = new MercatorProjection(mapConfiguration, 1513, 1947, (byte) 6);
        double topY = projection.latitudeToPixelY(39.5);
        Assert.assertEquals(0, topY, 0);
        double mY = projection.latitudeToPixelY(38.9375);
        System.out.println("test6_34 lat=38.9375->Y=" + mY);
        double mX = projection.longitudeToPixelX(-77);
        System.out.println("test6_34 lon=-77->X=" + mX);

    }
}
