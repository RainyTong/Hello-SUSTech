package com.example.xuversion.map_activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.GroundOverlayOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.xuversion.MyApplication;
import com.example.xuversion.R;
import com.example.xuversion.RealmHelper;
import com.example.xuversion.building_detail_activity.BuildingDetailActivity;
import com.example.xuversion.map_activity.overlay.WalkRouteOverlay;
import com.example.xuversion.model.Building;
import com.example.xuversion.search_activity.SearchActivity;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class MapActivity extends AppCompatActivity implements AMap.InfoWindowAdapter, AMap.OnMapClickListener, AMap.OnMarkerClickListener, LocationSource, AMapLocationListener, RouteSearch.OnRouteSearchListener {
    @BindView(R.id.maingo)
    LinearLayout maingo;

    private MapView mapView;
    private AMap aMap;
    private RouteSearch routeSearch;
    private WalkRouteResult walkRouteResult;
    private WalkRouteOverlay walkRouteOverlay;
    private AMapLocationClient mLocationClient;//定位发起端
    private AMapLocationClientOption mLocationOption;//定位参数
    private OnLocationChangedListener mListener;//定位监听器
    private boolean isFirstLoc = true;//标识，用于判断是否只显示一次定位信息和用户重新定位
    public double current_longtitude = 0;
    public double current_latitude = 0;
    public Marker oldMarker;
    public ArrayList<Marker> markerArrayList;

    private Realm realm;
    private RealmHelper realmHelper;
    private static final String GETBUILDING = "getAll";

    private static final int REQUEST_SEARCH = 2;
    private String clickBuilding;


    /**
     * bind onclick method
     * @param v the view that need to be bound
     */
    @OnClick(R.id.maingo)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.maingo:
                Intent it = new Intent(MapActivity.this,SearchActivity.class);
                startActivityForResult(it,REQUEST_SEARCH);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);//设置对应的XML布局文件
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            clickBuilding = bundle.getString("result").replace("\n","");
            Log.e("get",clickBuilding);
        }

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        realm = Realm.getDefaultInstance();
        realmHelper = new RealmHelper(realm);
        initialMap();
        setOncamerachangeListener();
        initLoc();//开始定位
    }

    private void getBuilding() {
        String url = MyApplication.getUrl()+"/building/getAll";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {//s为请求返回的字符串数据
                        try{
                            JSONArray jsonArray = new JSONArray(s);
                            realmHelper.addBuildingByJSONArray(jsonArray);
                            //更新marker
                            addMarker(realmHelper.queryAllBuilding());
                            MyApplication.getInstance().setGetALLBuilding(true);
                            showToast("add marker success");
                        }catch (Exception e){
                            showToast(e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        showToast(volleyError.toString());
                    }
                });
        request.setRetryPolicy(new DefaultRetryPolicy(8000,0,0f));
        request.setTag(GETBUILDING);
        MyApplication.getInstance().getHttpQueues().add(request);
    }



    private void initialMap() {
        aMap = mapView.getMap();
        aMap.setMaxZoomLevel(20);
        //把视角移向南科大
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.60000, 113.999930), 16));
        //叠加地图顶层图层
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(new LatLng(22.609000, 114.005760))
                .include(new LatLng(22.591897, 113.994548)).build();
        aMap.addGroundOverlay(new GroundOverlayOptions()
                .anchor(0.5f, 0.5f)
                .transparency(0.1f)
                .zIndex(0)
                .image(BitmapDescriptorFactory
                        .fromResource(R.drawable.school))
                .positionFromBounds(bounds));

        aMap.showMapText(false);
        markerArrayList = new ArrayList<>();
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(2000);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        myLocationStyle.radiusFillColor(android.R.color.transparent);
        myLocationStyle.strokeColor(android.R.color.transparent);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setOnMarkerClickListener(this);
        aMap.setOnMapClickListener(this);
        aMap.setInfoWindowAdapter(this);

        UiSettings settings = aMap.getUiSettings();
        settings.setMyLocationButtonEnabled(false);
        settings.setZoomControlsEnabled(false);
        settings.setLogoBottomMargin(-100);
        //设置定位监听
        aMap.setLocationSource(this);
        // 是否可触发定位并显示定位层
        aMap.setMyLocationEnabled(true);
        if(MyApplication.getInstance().isGetALLBuilding()){
            addMarker(realmHelper.queryAllBuilding());
        }
        else {
            getBuilding();
        }
    }

    private void actionSearch(String content){
        if(content == null){
            return;
        }
        Log.e("now",content.equals("Library")+"");
        for(Marker i:markerArrayList){
            if(i.getTitle()!=null && i.getTitle().equals(content)){
                Log.e("hi",content);
                oldMarker=i;
                LatLng position = oldMarker.getPosition();
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(position.latitude, position.longitude), 18));
            }
            else {
                i.hideInfoWindow();
            }
        }
        if(oldMarker != null){
            oldMarker.showInfoWindow();
        }
    }



    //添加marker
    private void addMarker(List<Building> buildings) {
        MarkerOptions options = new MarkerOptions();
        LatLng latLng;
        for (Building i : buildings) {
            latLng = new LatLng(i.getLatitude(), i.getLongitude());
            options.position(latLng);
            options.title(i.getName());
            options.snippet(i.getDescription());

            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.life));
            Marker marker = aMap.addMarker(options);
            marker.setAlpha(0);
            markerArrayList.add(marker);
        }
        actionSearch(clickBuilding);
    }


    /**
     * get the information window
     *
     * @param marker Marker object
     * @return the information view
     */
    @Override
    public View getInfoWindow(Marker marker) {
        View view = LayoutInflater.from(this).inflate(R.layout.ui_infowindow, null);
        LinearLayout navigation = (LinearLayout) view.findViewById(R.id.navigation_LL);
        LinearLayout check = (LinearLayout) view.findViewById(R.id.call_LL);
        TextView nameTV = (TextView) view.findViewById(R.id.agent_name);
        TextView addrTV = (TextView) view.findViewById(R.id.agent_addr);
        String snippet = marker.getSnippet();
        final String agentName = marker.getTitle();
        nameTV.setText(agentName);
        addrTV.setText("描述：" + snippet);
        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchRouteToMarker(current_latitude, current_longtitude, oldMarker.getPosition().latitude, oldMarker.getPosition().longitude);

            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("buildingName", agentName);
                Intent it = new Intent(MapActivity.this, BuildingDetailActivity.class);
                it.putExtras(bundle);
                startActivity(it);
            }
        });
        return view;
    }

    /**
     * Get Infor Content
     *
     * @param marker the Marker object
     * @return
     */
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    /**
     * set listener
     */
    public void setOncamerachangeListener() {
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                float x = aMap.getCameraPosition().zoom;
                if (x >= 17.9) {
                    for (Marker i : markerArrayList) {
                        i.setAlpha(1);
                    }
                } else {
                    for (Marker i : markerArrayList) {
                        i.setAlpha(0);
                        if (i.getTitle() == null) {
                            i.setAlpha(1);
                        }
                        if (oldMarker != null) {
                            if (oldMarker.isInfoWindowShown()) {
                                oldMarker.setAlpha(1);
                            }
                        }
                    }

                }
            }
        });
    }

    //定位
    private void initLoc() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

    }

    /**
     * Search the path to marker
     *
     * @param s_lat  s
     * @param s_long e
     * @param e_lat  e
     * @param e_long e
     */
    public void searchRouteToMarker(double s_lat, double s_long, double e_lat, double e_long) {
        walkRouteSearch(s_lat, s_long, e_lat, e_long);

    }

    /**
     * The location changes function
     *
     * @param amapLocation AMapLocation object
     */
    //定位回调函数
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null && mListener != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();
                mListener.onLocationChanged(amapLocation);
                //获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                current_latitude = amapLocation.getLatitude();
                //获取纬度
                current_longtitude = amapLocation.getLongitude();
                //获取经度
                amapLocation.getAccuracy();
                //获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                amapLocation.getAddress();
                if (isFirstLoc) {
                    isFirstLoc = false;
                }
            } else {
                if (isFirstLoc) {
                    Toast.makeText(getApplicationContext(), "定位失败，请打开GPS权限", Toast.LENGTH_LONG).show();
                    askForPermission("您需要开启位置权限才可以正常使用地图！");
                    isFirstLoc = false;
                }
            }
        }
    }

    private void askForPermission(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setNegativeButton("残忍拒绝", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("快速设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName())); // 根据包名打开对应的设置界面
                startActivity(intent);
            }
        });
        builder.create().show();
    }

    /**
     * @param StartLat start latitude
     * @param StartLong start longitude
     * @param EndLat end latitude
     * @param EndLong end longitude
     */
    public void walkRouteSearch(double StartLat, double StartLong, double EndLat, double EndLong) {
        LatLonPoint startLngLat = new LatLonPoint(StartLat, StartLong);
        LatLonPoint endLngLat = new LatLonPoint(EndLat, EndLong);
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startLngLat, endLngLat);
        RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WalkDefault);
        routeSearch.calculateWalkRouteAsyn(query);
    }

    /**
     * @param result the WalkRouteResult
     * @param errorCode the error code
     */
    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {

        if (result != null && result.getPaths() != null && result.getPaths().size() > 0) {
            walkRouteResult = result;
            WalkPath walkPath = walkRouteResult.getPaths().get(0);
            if (walkRouteOverlay != null) {
                walkRouteOverlay.removeFromMap();
            }

            walkRouteOverlay = new WalkRouteOverlay(aMap, walkPath, walkRouteResult
                    .getStartPos(), walkRouteResult.getTargetPos());
            walkRouteOverlay.setNodeIconVisibility(false);
            walkRouteOverlay.addToMap();
            walkRouteOverlay.zoomToSpan();
        }
    }

    /**
     * Just for implementing
     * @param busRouteResult bus route result
     * @param i i
     */
    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
    }

    /**
     * Just for implementing
     *
     * @param rideRouteResult rideRouteResult
     * @param i i
     */
    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
    }

    /**
     * Just for implementing
     *
     * @param driveRouteResult driveRouteResult
     * @param i i
     */
    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
    }

    /**
     * activate
     *
     * @param listener the OnLocationChangedListener
     */
    //激活定位
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
    }

    /**
     * stop
     */
    //停止定位
    @Override
    public void deactivate() {
        mListener = null;
    }

    /**
     * OnClick function
     *
     * @param latLng LatLng object
     */
    @Override
    public void onMapClick(LatLng latLng) { //点击地图上没marker 的地方，隐藏inforwindow
        if (oldMarker != null) {
            oldMarker.hideInfoWindow();
            if (aMap.getCameraPosition().zoom < 18) {
                oldMarker.setAlpha(0);
            }
        }
    }

    @Override
    protected  void onActivityResult(int requestCode,int resultCode ,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == REQUEST_SEARCH && resultCode == RESULT_OK){
            clickBuilding = data.getStringExtra("clickbuilding");
            actionSearch(clickBuilding);
        }
    }

    /**
     * On Click function
     *
     * @param marker the Marker object
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {

        if (marker.getTitle() != null && !marker.getTitle().equals("起点") && !marker.getTitle().equals("终点")) {
            oldMarker = marker;
            marker.showInfoWindow();
        }
        return true;
    }

    private void showToast(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        realm.close();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getInstance().getHttpQueues().cancelAll(GETBUILDING);
    }

}
