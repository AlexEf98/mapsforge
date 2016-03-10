package org.mapsforge.core.util;

/**
 * Created by ruinalef on 10/8/15.
 */
public class MapModelFactory {

    private MapConfiguration mapConfiguration;

    public MapModelFactory(MapConfiguration mapConfiguration) {
        this.mapConfiguration = mapConfiguration;
    }

    /*public MapModelFactory() {
        MapConfiguration.Builder builder = new MapConfiguration.Builder();*/
        /*builder.setMapWidth(256)
               .setMapHeight(256)
               .setTileXCount(1)
               .setTileYCount(1)
               .setMapUpperLat(85.05112877980659)
               .setMapBottomLat(-85.05112877980659)
               .setMapLeftLon(-180)
               .setMapRightLon(180)
               .setProjection(MapConfiguration.MERCATOR_PROJECTION);*/
        /*builder.setMapWidth(256)
                .setMapHeight(256)
                .setTileXCount(2)
                .setTileYCount(1)
                .setMapUpperLat(90)
                .setMapBottomLat(-90)
                .setMapLeftLon(-180)
                .setMapRightLon(180)
                .setProjection(MapConfiguration.GEO_PROJECTION);*/

        /*builder.setMapWidth(1280)
                .setMapHeight(720)
                .setTileXCount(4)
                .setTileYCount(2)
                .setMapUpperLat(50)
                .setMapBottomLat(20.17162)
                .setMapLeftLon(-128)
                .setMapRightLon(-64)
                .setProjection(MapConfiguration.GEO_PROJECTION);

        mapConfiguration = builder.build();
    }*/

    public MapModel createModel() {
        return new MapBaseModel(mapConfiguration);
    }

    public MapModel createModel(int tileSize) {
        return new MapBaseModel(mapConfiguration, tileSize);
    }
}
