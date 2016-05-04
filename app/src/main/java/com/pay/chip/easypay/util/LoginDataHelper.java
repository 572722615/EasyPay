package com.pay.chip.easypay.util;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.pay.chip.easypay.application.PayApplication;
import com.pay.chip.easypay.pages.main.model.LocationData;
import com.pay.chip.easypay.pages.merchant.model.ShoppingCartData;
import com.pay.chip.easypay.pages.person.event.LoginOutEvent;
import com.pay.chip.easypay.pages.person.model.LoginUserInfo;

import de.greenrobot.event.EventBus;

public class LoginDataHelper {

    private static LoginDataHelper sInstance = null;
    private LocationClient mLocationClient;
    private BDLocation bDLocation;
    private LocationData locationData;
    private LoginUserInfo loginUserInfo;
    private MyLocationListener mMyLocationListener;
    private ShoppingCartData shoppingCartData;

    private void LoginDataHelper() {
    }

    public static LoginDataHelper getInstance() {
        if (null == sInstance) {
            synchronized (LoginDataHelper.class) {
                if (null == sInstance) {
                    sInstance = new LoginDataHelper();
                }
            }
        }
        return sInstance;
    }
    //百度S
    // DK start
    public void initBaiduSDK(Context context) {
        SDKInitializer.initialize(context);
        if (mLocationClient != null) {
            mLocationClient.stop();
            mLocationClient = null;
        }
        mLocationClient = new LocationClient(context);
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，
        option.setIsNeedAddress(false);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    public void doLogout(Activity activity) {
        LoginOutEvent loginOutEvent = new LoginOutEvent(0,null);
        EventBus.getDefault().post(loginOutEvent);
        //清除所有与用户相关缓存
        LoginDataHelper.getInstance().clearLoginUserInfo();
    }


    public void setShoppingCartData(ShoppingCartData shoppingCartData) {
        this.shoppingCartData = shoppingCartData;
    }

    public ShoppingCartData getShoppingCartData() {
        return shoppingCartData;
    }

    public class MyLocationListener implements BDLocationListener {
        private LocationData data;

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            bDLocation = location;
            if (data == null) {
                data = new LocationData();
            }
            data.latitude = String.valueOf(location.getLatitude());
            data.longitude = String.valueOf(location.getLongitude());
            if (LoginDataHelper.getInstance().getLocationData() != null) {
                data.city = LoginDataHelper.getInstance().getLocationData().city;
            }
            if (!TextUtils.isEmpty(location.getCity())) {
                data.city = location.getCity();
            }
            LoginDataHelper.getInstance().setLocationData(data);

            if (mLocationClient != null) {
                mLocationClient.stop();
            }
//            EventBus.getDefault().post(new OnLocateEvent(bDLocation));
        }
    }
    public synchronized void setLocationData(LocationData locationData) {
        if (locationData != null) {
        }
        String data = LocationData.toJsonString(locationData);
        ServiceConfigManager.getInstance(PayApplication.getAppContext()).setLocationData(data);
        this.locationData = null;
        this.locationData = getLocationData();
    }
    public synchronized LocationData getLocationData() {
        if (null == locationData) {
            String data = ServiceConfigManager.getInstance(
                    PayApplication.getAppContext()).getLocationData();
            if (!TextUtils.isEmpty(data)) {
                locationData = LocationData.getFromJson(data);
            }
        }
        return locationData;
    }

    public synchronized LoginUserInfo getLoginUserInfo() {
        if (null == loginUserInfo) {
            String data = ServiceConfigManager.getInstance(
                    PayApplication.getAppContext()).getLoginUserInfo();
            if (!TextUtils.isEmpty(data)) {
                loginUserInfo = LoginUserInfo.getFromJson(data);
            }
        }
        return loginUserInfo;
    }

    public boolean isLogin() {
        LoginUserInfo info = getLoginUserInfo();
        if (info == null) {
            return false;
        }
        return true;
    }

    public synchronized void setLoginUserInfo(String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        ServiceConfigManager.getInstance(PayApplication.getAppContext()).setLoginUserInfo(data);
        loginUserInfo = null;
        loginUserInfo = getLoginUserInfo();
    }

    public synchronized void clearLoginUserInfo() {

        ServiceConfigManager.getInstance(PayApplication.getAppContext()).setLoginUserInfo("");
        loginUserInfo = null;
        loginUserInfo = getLoginUserInfo();
    }

}
