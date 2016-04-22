package com.erban.common.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.erban.common.runtime.ApplicationDelegate;

public class KNetUtils {

    private static final String TAG = "KNetUtils";

    // 判断网络是否存在
    public static boolean isNetworkAvailable() {
        Context context = ApplicationDelegate.getApplication();
        if (context == null) {
            return false;
        }

        boolean ret = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            try {
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isAvailable()) {
                    ret = true;
                }
            } catch (Exception e) {
            }
        }
        return ret;
    }

    /**
     * 判断Wifi是否可用
     *
     * @return true:可用 false:不可用
     */
    public static boolean isWiFiActive() {
        Context context = ApplicationDelegate.getApplication();
        if (context == null) {
            return false;
        }

        boolean ret = false;
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = null;
        try {
            wifiInfo = mWifiManager.getConnectionInfo();
            int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
            if (mWifiManager.isWifiEnabled() && ipAddress != 0) {
                ret = true;
            }
        } catch (Exception e) {
        }

        return ret;
    }


    // 获取当前网络连接类型
    public static int getNetworkState() {
        Context context = ApplicationDelegate.getApplication();
        if (context == null) {
            return -1;
        }

        int ret = -1;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        // 平板可能没有移动通讯模块
        // infoMobile == null device:
        // LENOVO IdeaTab S6000-F
        // asus Nexus 7
        // samsung GT-N8010
        if (null == manager) {
            return -1;
        }

        NetworkInfo infoMobile = null;
        try {
            infoMobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        } catch (Exception e) {
            // maybe have no permission :
            // android.permission.ACCESS_NETWORK_STATE
            infoMobile = null;
        }
        if (infoMobile != null) {
            State mobile = infoMobile.getState();
            if (mobile == State.CONNECTED || mobile == State.CONNECTING)
                return ConnectivityManager.TYPE_MOBILE;
        }
        NetworkInfo infoWifi = null;
        try {
            infoWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        } catch (SecurityException e) {
            // maybe have no permission :
            // android.permission.ACCESS_NETWORK_STATE
            infoWifi = null;
        }
        if (infoWifi != null) {
            State wifi = infoWifi.getState();
            if (wifi == State.CONNECTED || wifi == State.CONNECTING)
                return ConnectivityManager.TYPE_WIFI;
        }
        return -1;
    }

    //    public static String getNetworkName(Context context){
    //        String netWorkName = null;
    //        int networkState = getNetworkState(context);
    //        switch (networkState) {
    //            case ConnectivityManager.TYPE_MOBILE:
    //                netWorkName = UserLogConstants.NETWORK_MOBILE;
    //                break;
    //            case ConnectivityManager.TYPE_WIFI:
    //                netWorkName = UserLogConstants.NETWORK_WIFI;
    //                break;
    //            default:
    //                netWorkName = UserLogConstants.NETWORK_NONE;
    //                break;
    //        }
    //        return netWorkName;
    //    }

}
