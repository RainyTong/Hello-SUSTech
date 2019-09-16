package com.example.xuversion.map_activity.overlay;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.example.xuversion.R;


import java.util.ArrayList;
import java.util.List;


public class RouteOverlay {
	protected List<Marker> stationMarkers = new ArrayList<Marker>();
	protected List<Polyline> allPolyLines = new ArrayList<Polyline>();
	protected Marker startMarker;
	protected Marker endMarker;
	protected LatLng startPoint;
	protected LatLng endPoint;
	protected AMap mAMap;

	private Bitmap startBit, endBit, busBit, walkBit, driveBit;
	protected boolean nodeIconVisible = true;


	/**
	 * remove all Marker from BusRouteOverlay
	 */
	public void removeFromMap() {
		if (startMarker != null) {
			startMarker.remove();

		}
		if (endMarker != null) {
			endMarker.remove();
		}
		for (Marker marker : stationMarkers) {
			marker.remove();
		}
		for (Polyline line : allPolyLines) {
			line.remove();
		}
		destroyBit();
	}

	private void destroyBit() {
		if (startBit != null) {
			startBit.recycle();
			startBit = null;
		}
		if (endBit != null) {
			endBit.recycle();
			endBit = null;
		}
		if (busBit != null) {
			busBit.recycle();
			busBit = null;
		}
		if (walkBit != null) {
			walkBit.recycle();
			walkBit = null;
		}
		if (driveBit != null) {
			driveBit.recycle();
			driveBit = null;
		}
	}

	/**
	 * StartBitmapDescriptor getter
	 * @return StartBitmapDescriptor
	 */
	protected BitmapDescriptor getStartBitmapDescriptor() {
		return BitmapDescriptorFactory.fromResource(R.drawable.amap_start);
	}

    /**
     * EndBitmapDescriptor getter
     * @return EndBitmapDescriptor
     */
	protected BitmapDescriptor getEndBitmapDescriptor() {
		return BitmapDescriptorFactory.fromResource(R.drawable.amap_end);
	}

    /**
     * WalkBitmapDescriptor getter
     * @return WalkBitmapDescriptor
     */
	protected BitmapDescriptor getWalkBitmapDescriptor() {
		return BitmapDescriptorFactory.fromResource(R.drawable.life);
	}

    /**
     *  add start and end marker
     */
	protected void addStartAndEndMarker() {
		startMarker = mAMap.addMarker((new MarkerOptions())
				.position(startPoint).icon(getStartBitmapDescriptor())
				.title("\u8D77\u70B9"));

		endMarker = mAMap.addMarker((new MarkerOptions()).position(endPoint)
				.icon(getEndBitmapDescriptor()).title("\u7EC8\u70B9"));

	}
	/**
	 * Zoom the Span to current point
	 */
	public void zoomToSpan() {
		if (startPoint != null) {
			if (mAMap == null) {
				return;
			}
			try {
				LatLngBounds bounds = getLatLngBounds();
				mAMap.animateCamera(CameraUpdateFactory
						.newLatLngBounds(bounds, 100));
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

    /**
     * get latitude and longitude bound
     * @return Latitude and longitude bound
     */
	protected LatLngBounds getLatLngBounds() {
		LatLngBounds.Builder b = LatLngBounds.builder();
		b.include(new LatLng(startPoint.latitude, startPoint.longitude));
		b.include(new LatLng(endPoint.latitude, endPoint.longitude));
		return b.build();
	}
	/**
	 * Link node icon control display interface
	 * @param visible true is visiable, false if invisible
	 */
	public void setNodeIconVisibility(boolean visible) {
		try {
			nodeIconVisible = visible;
			if (this.stationMarkers != null && this.stationMarkers.size() > 0) {
				for (int i = 0; i < this.stationMarkers.size(); i++) {
					this.stationMarkers.get(i).setVisible(visible);
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

    /**
     * add station marker
     * @param options MarkerOptions
     */
	protected void addStationMarker(MarkerOptions options) {
		if(options == null) {
			return;
		}
		Marker marker = mAMap.addMarker(options);
		if(marker != null) {
			stationMarkers.add(marker);
		}
		
	}

    /**
     * add poly line
     * @param options PolylineOptions
     */
	protected void addPolyLine(PolylineOptions options) {
		if(options == null) {
			return;
		}
		Polyline polyline = mAMap.addPolyline(options);
		if(polyline != null) {
			allPolyLines.add(polyline);
		}
	}

    /**
     * RouteWidth getter
     * @return RouteWidth
     */
	protected float getRouteWidth() {
		return 12f;
	}

    /**
     * WalkColor getter
     * @return WalkColor
     */
	protected int getWalkColor() {
		return Color.parseColor("#696969");
	}


}
