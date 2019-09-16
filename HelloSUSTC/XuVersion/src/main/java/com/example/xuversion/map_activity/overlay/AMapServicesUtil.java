package com.example.xuversion.map_activity.overlay;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import java.util.ArrayList;
import java.util.List;

class AMapServicesUtil {
    /**
     * Convert a latLonPoint object to Latitute and Longitute
     * @param latLonPoint the LatLonPoint object
     * @return the LatLng object of latitude and longitude
     */
	public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
		return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
	}

    /**
     * Convert a list of LatLonPoint obejct to a list of LatLng object
     * @param shapes the array that need to transfer
     * @return the transferred result
     */
	public static ArrayList<LatLng> convertArrList(List<LatLonPoint> shapes) {
		ArrayList<LatLng> lineShapes = new ArrayList<LatLng>();
		for (LatLonPoint point : shapes) {
			LatLng latLngTemp = AMapServicesUtil.convertToLatLng(point);
			lineShapes.add(latLngTemp);
		}
		return lineShapes;
	}

}
