/*
 * Copyright 2010, 2011, 2012, 2013 mapsforge.org
 * Copyright 2014 Ludwig M Brinckmann
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.mapsforge.map.layer.queue;

import java.util.Collection;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.model.Tile;
import org.mapsforge.core.util.MapModel;
import org.mapsforge.core.util.MapProjection;

final class QueueItemScheduler {
	static final double PENALTY_PER_ZOOM_LEVEL = 10;

	static <T extends Job> void schedule(Collection<QueueItem<T>> queueItems, MapPosition mapPosition, MapModel mapModel) {
		for (QueueItem<T> queueItem : queueItems) {
			queueItem.setPriority(calculatePriority(queueItem.object.tile, mapPosition, mapModel));
		}
	}

	private static double calculatePriority(Tile tile, MapPosition mapPosition, MapModel mapModel) {
		double tileLatitude = mapModel.tileYToLatitude(tile.tileY, tile.zoomLevel);
		double tileLongitude = mapModel.tileXToLongitude(tile.tileX, tile.zoomLevel);

		MapProjection projection = mapModel.getProjection(mapPosition.zoomLevel);
		double tilePixelX = projection.longitudeToPixelX(tileLongitude)
				+ mapModel.getTileWidth() / 2;
		double tilePixelY = projection.latitudeToPixelY(tileLatitude)
				+ mapModel.getTileHeight() / 2;

		LatLong latLong = mapPosition.latLong;
		double mapPixelX = projection.longitudeToPixelX(latLong.longitude);
		double mapPixelY = projection.latitudeToPixelY(latLong.latitude);

		double diffPixel = Math.hypot(tilePixelX - mapPixelX, tilePixelY - mapPixelY);
		int diffZoom = Math.abs(tile.zoomLevel - mapPosition.zoomLevel);

		int tileSize = (mapModel.getTileWidth() + mapModel.getTileHeight()) / 2;
		return diffPixel + PENALTY_PER_ZOOM_LEVEL * tileSize * diffZoom;
	}

	private QueueItemScheduler() {
		throw new IllegalStateException();
	}
}
