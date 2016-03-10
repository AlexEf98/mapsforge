package org.mapsforge.core.util;

/**
 * Created by ruinalef on 10/9/15.
 */
public interface MapProjectionProvider {
    MapProjection getProjection(byte zoom);
}
