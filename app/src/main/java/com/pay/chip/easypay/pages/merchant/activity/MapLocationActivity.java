package com.pay.chip.easypay.pages.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.pay.chip.easypay.R;
import com.pay.chip.easypay.pages.main.model.LocationData;
import com.pay.chip.easypay.pages.merchant.adapter.PoiAddressListAdapter;
import com.pay.chip.easypay.pages.merchant.event.LocationChangeEvent;
import com.pay.chip.easypay.pages.merchant.model.PoiItemModel;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MapLocationActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mPoiSearchView;
    private LinearLayout mMapPanel;
    private MapView mBMapView;
    private Button locateCurrentBtn;
    private Button discountSure;
    private ListView mAddressListView;
    private ListView mPoiSearchResultsView;

    // 纪录待返回给调用Activity的位置数据
    private LocationData mLocationData;
    // 纪录定位到的"我的位置"
    private BDLocation mSelfLocation;

    // Poi搜索相关
    private PoiSearch mPoiSearch;


    // 地理编码/反编码相关
    private GeoCoder mGeoCoder;

    // 地图相关
    private BaiduMap mMapController;

    /* 可拖拽的maker */
    private Marker mMarker;
    // 定位相关
    private LocationClient mLocationClient;
    private MyLocationListenner mLocationListener = new MyLocationListenner();
    private boolean mIsFirstLocate = true;     // 是否首次定位
    private double mLat;
    private double mLng;

    public static void startMapLocation(Context context){
        Intent intent = new Intent(context, MapLocationActivity.class);
        Bundle b = new Bundle();
        intent.putExtras(b);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location);

        initView();
        mLocationData = new LocationData();

        configurePoiSearchFeature();

        configureMapFeature();
    }


    private void initView() {
        mPoiSearchView = (EditText) findViewById(R.id.poi_search_view);
        mMapPanel = (LinearLayout) findViewById(R.id.map_panel);
        mBMapView = (MapView) findViewById(R.id.bmap_view);
        locateCurrentBtn = (Button) findViewById(R.id.locate_current_btn);
        locateCurrentBtn.setOnClickListener(this);
        mAddressListView = (ListView) findViewById(R.id.address_listview);
        mPoiSearchResultsView = (ListView) findViewById(R.id.poi_search_results);
        discountSure = (Button) findViewById(R.id.discountSure);
        discountSure.setOnClickListener(this);
    }


    private void configurePoiSearchFeature() {
        mPoiSearchResultsView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        mPoiSearch = PoiSearch.newInstance();

        mPoiSearchView.clearFocus();

        mPoiSearchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mMapPanel.setVisibility(View.GONE);
                    mPoiSearchResultsView.setVisibility(View.VISIBLE);
                } else {
                    mMapPanel.setVisibility(View.VISIBLE);
                    mPoiSearchResultsView.setVisibility(View.GONE);
                }
            }
        });

        mPoiSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newText = s.toString();

                /**
                 * POI检索的优先级
                 * 1. 如果反编码出了城市，则在该城市内搜索
                 * 2. 如果只有经纬度，则在该经纬度附近内搜索
                 * 3. 上述两者皆无，则在北京市搜索，实测可以搜索出全国大部分数据
                 */
                if (mLocationData != null && !TextUtils.isEmpty(mLocationData.city)) {
                    mPoiSearch.searchInCity(new PoiCitySearchOption().keyword(newText).city(mLocationData.city).pageCapacity(20));
                } else if (mLocationData != null && mSelfLocation != null) {
                    LatLng centerLatLng = new LatLng(mSelfLocation.getLatitude(), mSelfLocation.getLongitude());
                    mPoiSearch.searchNearby(new PoiNearbySearchOption().keyword(newText).location(centerLatLng).radius(50000).pageCapacity(20));
                } else {
                    mPoiSearch.searchInCity(new PoiCitySearchOption().keyword(newText).city("北京").pageCapacity(20));
                }

            }
        });

        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult result) {
                if (result == null
                        || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    Toast.makeText(MapLocationActivity.this, "未找到该地点，请确认输入！", Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                    List<PoiItemModel> addressList = new ArrayList<>();

                    List<PoiInfo> poiList =  result.getAllPoi();
                    for (PoiInfo poi : poiList) {
                        addressList.add(new PoiItemModel(poi.name, poi.address, poi.location.latitude, poi.location.longitude));
                    }

                    PoiAddressListAdapter adapter = new PoiAddressListAdapter(MapLocationActivity.this, R.layout.cell_poi_address, addressList);
                    mPoiSearchResultsView.setAdapter(adapter);
                    mPoiSearchResultsView.setItemChecked(0, true);
                    mPoiSearchResultsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ListView listView = (ListView)parent;
                            listView.setItemChecked(position, true);

                            mPoiSearchView.clearFocus();
                            InputMethodManager imm = (InputMethodManager)getSystemService(MapLocationActivity.this.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mPoiSearchView.getWindowToken(), 0);

                            PoiItemModel itemModel = (PoiItemModel)listView.getItemAtPosition(position);
                            setSelectedPostion(itemModel.latitude, itemModel.longitude, true, true);
                            mLat = itemModel.latitude;
                            mLng = itemModel.longitude;
                        }
                    });

                    return;
                }

                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
                    // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
                    String strInfo = "在";
                    for (CityInfo cityInfo : result.getSuggestCityList()) {
                        strInfo += cityInfo.city;
                        strInfo += ",";
                    }
                    strInfo += "找到结果";
//                    Toast.makeText(GetLocationPage.this, strInfo, Toast.LENGTH_LONG)
//                            .show();
                    Toast.makeText(MapLocationActivity.this, "未找到该地点，请确认输入！", Toast.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }
        });
    }


    private void configureMapFeature() {
        mAddressListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // 初始化Geo搜索模块，注册事件监听
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(MapLocationActivity.this, "抱歉，未能找到地址信息", Toast.LENGTH_LONG)
                            .show();
                    return;
                }


                ReverseGeoCodeResult.AddressComponent addressDetail = result.getAddressDetail();
//                mLocationData.province = addressDetail.province;
                mLocationData.city = addressDetail.city;
//                mLocationData.area = addressDetail.district;

                List<PoiItemModel> addressList = new ArrayList<>();
                PoiItemModel address = new PoiItemModel("[位置]", result.getAddress(), result.getLocation().latitude, result.getLocation().longitude);
                addressList.add(address);

                List<PoiInfo> poiList =  result.getPoiList();
                if (poiList != null) {
                    for (PoiInfo poi : poiList) {
                        addressList.add(new PoiItemModel(poi.name, poi.address, poi.location.latitude, poi.location.longitude));
                    }
                }

                PoiAddressListAdapter adapter = new PoiAddressListAdapter(MapLocationActivity.this, R.layout.cell_poi_address, addressList);
                mAddressListView.setAdapter(adapter);
                mAddressListView.setItemChecked(0, true);
                mAddressListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ListView listView = (ListView)parent;
                        listView.setItemChecked(position, true);
                        PoiItemModel itemModel = (PoiItemModel)listView.getItemAtPosition(position);
                        setSelectedPostion(itemModel.latitude, itemModel.longitude, true, false);;
                        mLat = itemModel.latitude;
                        mLng = itemModel.longitude;
                    }
                });

            }
        });

        mBMapView.showZoomControls(false);
        mMapController = mBMapView.getMap();
        mMapController.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                MapStatus mapStatus = mMapController.getMapStatus();
                LatLng center =  mapStatus.bound.getCenter();
                setSelectedPostion(center.latitude, center.longitude, false, true);
            }
        });

        // 开启定位图层
        mMapController.setMyLocationEnabled(true);
        // 自定义maker图标
        BitmapDescriptor currentMarkerBmp = BitmapDescriptorFactory.fromResource(R.drawable.icon_map_current_location);
        mMapController.setMyLocationConfigeration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, currentMarkerBmp));

        // 初始化定位设施
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(mLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);    // 打开gps
        option.setCoorType("bd09ll");   // 设置坐标类型
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }



  /*  @OnClick(R.id.locate_current_btn)
    void backToCurrentPosition() {
        // 回到"我的位置"
        if (mSelfLocation != null) {
            setSelectedPostion(mSelfLocation.getLatitude(), mSelfLocation.getLongitude(), true, true);
        }
    }*/

    @Override
    protected void onPause() {
        mBMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mBMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 销毁Poi搜索设置
        mPoiSearch.destroy();
        // 销毁地理编码/反编码设施
        mGeoCoder.destroy();
        // 退出时销毁定位设施
        mLocationClient.stop();
        // 关闭定位图层
        mMapController.setMyLocationEnabled(false);
        mBMapView.onDestroy();
        mBMapView = null;
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.locate_current_btn:
                if (mSelfLocation != null) {
                    setSelectedPostion(mSelfLocation.getLatitude(), mSelfLocation.getLongitude(), true, true);
                }
                break;
            case R.id.discountSure:
                EventBus.getDefault().post(new LocationChangeEvent(mLat,mLng));
                finish();
                break;
        }
    }


    private class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            // 接收到位置变化
            // map view销毁后不再处理新接收的位置
            if (location == null || mBMapView == null)
                return;
            // 根据定位到的经纬度信息, 创建"我的位置"
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();


            mSelfLocation = location;
            mMapController.setMyLocationData(locData);

            if (mIsFirstLocate) {
                mIsFirstLocate = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());

                // 基于当前位置，创建一个标记中心位置的maker
                BitmapDescriptor markerBmp = BitmapDescriptorFactory.fromResource(R.drawable.icon_map_drag_locate);
                OverlayOptions overlayOptions = new MarkerOptions().position(ll).icon(markerBmp).zIndex(0);
                mMarker = (Marker) (mMapController.addOverlay(overlayOptions));

                setSelectedPostion(location.getLatitude(), location.getLongitude(), true, true);
            }
        }
    }

    private void setSelectedPostion(double latitude, double longitude, boolean triggerMarkerMove, boolean triggerReverseGeo) {
        LatLng center =  new LatLng(latitude, longitude);

        if (mMarker != null) {
            mMarker.setPosition(center);

            if (triggerMarkerMove) {
                // 将maker移动到视野内
                LatLng ll = new LatLng(latitude, longitude);
                float maxLevel = mMapController.getMaxZoomLevel();  // 19.0 最小比例尺
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, maxLevel - 2);
                mMapController.animateMapStatus(u);
            }
        }

//        mLocationData.latitude = center.latitude;
//        mLocationData.longitude = center.longitude;

        if (triggerReverseGeo) {
            // 反Geo搜索
            mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(center));
        }
    }

}
