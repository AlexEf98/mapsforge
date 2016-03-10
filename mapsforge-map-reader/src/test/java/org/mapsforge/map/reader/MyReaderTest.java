package org.mapsforge.map.reader;

import org.junit.Test;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.reader.header.MapFileInfo;

/**
 * Created by ruinalef on 8/11/15.
 */
public class MyReaderTest {
    private static final String MAP_PATH="/home/ruinalef/osm/data/atlanta_road.map";

    @Test
    public void readerTest() {
        MapFile mapFile = new MapFile(MAP_PATH);
        MapFileInfo mapFileInfo = mapFile.getMapFileInfo();

        byte zoomLevel = 18;
        int tileX = MercatorProjection.longitudeToTileX(-84.330987, zoomLevel);
        int tileY = MercatorProjection.latitudeToTileY(33.741012, zoomLevel);
        Tile tile = new Tile(tileX, tileY, zoomLevel, 256);

        MapReadResult mapReadResult = mapFile.readMapData(tile);

        mapFile.close();
    }
}
