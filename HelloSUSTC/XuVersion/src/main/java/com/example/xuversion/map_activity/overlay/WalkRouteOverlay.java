package com.example.xuversion.map_activity.overlay;


import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkStep;

import java.util.List;

public class WalkRouteOverlay extends RouteOverlay {

    private PolylineOptions mPolylineOptions;

    private BitmapDescriptor walkStationDescriptor= null;

    private WalkPath walkPath;
	/**
	 * Create a walking route layer with this constructor
	 * @param amap map object
	 * @param path A plan for walking route planning. See the path query package of the search service module for details.
	 * @param start start point
	 * @param end end point
	 */
	public WalkRouteOverlay(AMap amap, WalkPath path,
                            LatLonPoint start, LatLonPoint end) {
		this.mAMap = amap;
		this.walkPath = path;
		startPoint = AMapServicesUtil.convertToLatLng(start);
		endPoint = AMapServicesUtil.convertToLatLng(end);
	}
	/**
	 * Add a walking route to the map
	 */
    public void addToMap() {

        initPolylineOptions();
        try {
            List<WalkStep> walkPaths = walkPath.getSteps();
            mPolylineOptions.add(startPoint);
            for (int i = 0; i < walkPaths.size(); i++) {
                WalkStep walkStep = walkPaths.get(i);
                LatLng latLng = AMapServicesUtil.convertToLatLng(walkStep
                        .getPolyline().get(0));
                
				addWalkStationMarkers(walkStep, latLng);
                addWalkPolyLines(walkStep);
               
            }
            mPolylineOptions.add(endPoint);
            addStartAndEndMarker();

            showPolyline();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

	private void checkDistanceToNextStep(WalkStep walkStep,
			WalkStep walkStep1) {
		LatLonPoint lastPoint = getLastWalkPoint(walkStep);
		LatLonPoint nextFirstPoint = getFirstWalkPoint(walkStep1);
		if (!(lastPoint.equals(nextFirstPoint))) {
			addWalkPolyLine(lastPoint, nextFirstPoint);
		}
	}

	private LatLonPoint getLastWalkPoint(WalkStep walkStep) {
		return walkStep.getPolyline().get(walkStep.getPolyline().size() - 1);
	}

	private LatLonPoint getFirstWalkPoint(WalkStep walkStep) {
		return walkStep.getPolyline().get(0);
	}

    private void addWalkPolyLine(LatLonPoint pointFrom, LatLonPoint pointTo) {
        addWalkPolyLine(AMapServicesUtil.convertToLatLng(pointFrom), AMapServicesUtil.convertToLatLng(pointTo));
    }

    private void addWalkPolyLine(LatLng latLngFrom, LatLng latLngTo) {
        mPolylineOptions.add(latLngFrom, latLngTo);
    }

    private void addWalkPolyLines(WalkStep walkStep) {
        mPolylineOptions.addAll(AMapServicesUtil.convertArrList(walkStep.getPolyline()));
    }

    private void addWalkStationMarkers(WalkStep walkStep, LatLng position) {
        addStationMarker(new MarkerOptions()
                .position(position)
                .title("\u65B9\u5411:" + walkStep.getAction()
                        + "\n\u9053\u8DEF:" + walkStep.getRoad())
                .snippet(walkStep.getInstruction()).visible(nodeIconVisible)
                .anchor(0.5f, 0.5f).icon(walkStationDescriptor));
    }

    private void initPolylineOptions() {

        if(walkStationDescriptor == null) {
            walkStationDescriptor = getWalkBitmapDescriptor();
        }

        mPolylineOptions = null;

        mPolylineOptions = new PolylineOptions();
        mPolylineOptions.color(getWalkColor()).width(getRouteWidth());
    }


    private void showPolyline() {
        addPolyLine(mPolylineOptions);
    }
}
