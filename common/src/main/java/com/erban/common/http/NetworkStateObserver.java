package com.erban.common.http;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class NetworkStateObserver extends BroadcastReceiver {
    private static final String TAG = "NetworkStateObserver";
    private static final String ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    private static Integer[] sObject = new Integer[0];
    private static NetworkStateObserver mInstance = null;
    private static List<NetworkStateListener> sObservers = new ArrayList<NetworkStateListener>();

    /**
     * callback interface
     */
    public static interface NetworkStateListener {
        public void onNetworkChanged(NetworkInfo info);
    }


    @Override
    public void onReceive(Context context,
                          Intent intent) {
        NetworkInfo networkInfo = getNetworkInfo(context);

        notify(networkInfo);
    }

    /**
     * register network state callback
     *
     * @param observer
     */
    public static void registerNetworkObserver(NetworkStateListener observer) {
        if (observer != null) {
            synchronized (sObject) {
                if (sObservers.contains(observer))
                    return;
                sObservers.add(observer);
            }
        }
    }

    /**
     * unregister network state callback
     *
     * @param observer
     */
    public static void unRegisterNetworkObserver(NetworkStateListener observer) {
        if (observer != null) {
            synchronized (sObject) {
                sObservers.remove(observer);
            }
        }
    }

    /**
     * notify network state change.
     *
     * @param info
     */
    private void notify(final NetworkInfo info) {
        synchronized (sObject) {
            for (NetworkStateListener observer : sObservers) {
                if (observer != null) {
                    observer.onNetworkChanged(info);
                }
            }
        }
    }

    /**
     * 将NetworkStateObserver注册到Context。
     *
     * @date 2015.2.13
     */
    public static void registerConnectivityReceiver(Context context) {
        if (context == null) {
            return;
        }
        if (mInstance == null) {
            IntentFilter filter = new IntentFilter(ACTION);
            mInstance = new NetworkStateObserver();
            context.registerReceiver(mInstance, filter);
        }
    }

    /**
     * 将NetworkStateObserver从Context注销。
     *
     * @date 2015.2.13
     */
    public static void unregisterConnectivityReceiver(Context context) {
        if (context == null) {
            return;
        }

        if (mInstance != null) {
            context.unregisterReceiver(mInstance);
            mInstance = null;
        }
    }

    /**
     * 获取网络状态信息。
     *
     * @date 2015.2.13
     */
    public static NetworkInfo getNetworkInfo(Context context) {
        if (context == null) {
            return null;
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    /**
     * 是否处于wifi连接状态。
     *
     * @date 2015.2.13
     */
    public static boolean isOnWifi(Context context) {
        return isOnWifi(getNetworkInfo(context));
    }

    /**
     * 是否处于wifi连接状态。
     *
     * @date 2015.2.13
     */
    public static boolean isOnWifi(NetworkInfo info) {
        if (info == null)
            return false;
        switch (info.getType()) {
            case ConnectivityManager.TYPE_MOBILE:
            case ConnectivityManager.TYPE_MOBILE_DUN:
            case ConnectivityManager.TYPE_MOBILE_MMS:
            case ConnectivityManager.TYPE_MOBILE_SUPL:
            case ConnectivityManager.TYPE_MOBILE_HIPRI:
            case ConnectivityManager.TYPE_WIMAX: // separate case for this?
                return false;
            case ConnectivityManager.TYPE_WIFI:
            case ConnectivityManager.TYPE_ETHERNET:
            case ConnectivityManager.TYPE_BLUETOOTH:
                return true;
            default:
                return false;
        }
    }


    /**
     * 是否是wifi网络
     *
     * @date 2015.2.13
     */
    public static boolean isWifiNetworkAvailable(Context context) {
        if (context == null)
            return false;

        ConnectivityManager conmgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conmgr == null) {
            return false;
        }

        NetworkInfo activeNetInfo = null;
        try {
            activeNetInfo = conmgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        } catch (SecurityException e) {
            // maybe have no permission :
            // android.permission.ACCESS_NETWORK_STATE
            activeNetInfo = null;
        } catch (Exception e) {
            // maybe NullPointerException :
            //at android.os.Parcel.readException(Parcel.java:1431)
            //at android.os.Parcel.readException(Parcel.java:1379)
            //at android.net.IConnectivityManager$Stub$Proxy.getNetworkInfo(IConnectivityManager.java:734)
            //at android.net.ConnectivityManager.getNetworkInfo(ConnectivityManager.java:478)
            activeNetInfo = null;
        }

        if (activeNetInfo == null) {
            return false;
        }
        State wifi = activeNetInfo.getState();

        if (wifi == State.CONNECTED)
            return true;
        return false;
    }


    /**
     * 判断网络是否存在
     *
     * @date 2015.2.13
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                try {
                    if (cm.getActiveNetworkInfo() != null) {
                        if (cm.getActiveNetworkInfo().isAvailable()) {
                            return true;
                        }
                    }
                } catch (Exception e) {
                    return false;
                }

            }
            return false;
        }
        return false;
    }


    /**
     * 获取当前网络连接类型
     *
     * @date 2015.2.13
     */
    public static int getNetworkState(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        // 平板可能没有移动通讯模块
        // infoMobile == null device:
        // LENOVO IdeaTab S6000-F
        // asus Nexus 7
        // samsung GT-N8010
        if (null == manager)
            return -1;

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


    /**
     * 获取当前连接wifi的SSID，如果没有连接返回为链接字符串，获取失败则返回null。
     *
     * @date 2015.2.13
     */
    public static String getCurrentSSID(Context context) {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null && networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ssid = connectionInfo.getSSID();
            }
        }
        return ssid;
    }

    public static boolean isMobileNetwork(Context context) {
        return getNetworkState(context) == ConnectivityManager.TYPE_MOBILE;
    }

}
