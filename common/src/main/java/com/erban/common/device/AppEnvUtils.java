package com.erban.common.device;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.erban.common.http.KNetUtils;
import com.erban.common.runtime.ApplicationDelegate;
import com.erban.common.util.KLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


public class AppEnvUtils {
    /**
     * @brief Set to true to enable debug mode.
     */
    public final static boolean DEBUG = true;
    private static final String LOGTAG = "AppEnvUtils";
    /**
     * Gets total RAM return -1 if error
     */
    private static final String MEM_TOTAL = "MemTotal:";
    private static final String MEM_UNIT = " kB";
    private static final String TAG = AppEnvUtils.class.getSimpleName();
    private static String sMCC;
    private static String sAndroidID;
    private static String sVersionCode;
    private static String sVersionName;
    private static String sIMEI;
    private static Locale mPhoneLocale = null;


    public static String getNetworkClass(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return ""; //not connected
        }
        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            return "WIFI";
        }
        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return "2G";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return "4G";
                default:
                    return "";
            }
        }
        return "";
    }

    /**
     * @param context
     * @return the imei number
     * @brief get the imei number
     */
    public static String getIMEI(Context context) {
        if (sIMEI == null) {
            try {
                TelephonyManager tm = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                sIMEI = tm.getDeviceId();
            } catch (Exception e) {
            }
        }
        return sIMEI;
    }

    /**
     * 获取原始 MAC 地址，以 ":" 分隔
     *
     * @param context
     * @return
     */
    public static String getRawMacAddress(Context context) {
        String macAddress = null;
        try {
            WifiManager wifiMgr = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = (null == wifiMgr ? null : wifiMgr
                    .getConnectionInfo());
            if (null != info) {
                macAddress = info.getMacAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (macAddress == null) {
            macAddress = "";
        }
        return macAddress;
    }

    public static boolean isMemoryOk(final Context cxt) {
        return getAvailabeMemoryM(cxt) > 256;
    }

    private static long getAvailabeMemoryM(final Context cxt) {
        MemoryInfo mi = new MemoryInfo();
        ActivityManager activityManager = (ActivityManager) cxt
                .getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        return mi.availMem / 1024 / 1024;
    }

    /**
     * 获取手机mac地址<br/>
     * 错误返回12个0
     */
    public static String getMacAddress(Context context) {
        // 获取mac地址：
        String macAddress = getRawMacAddress(context);
        return macAddress.replace(":", "");
    }

    public static String getLanguage() {
        return Locale.getDefault().getLanguage();// 语言
    }

    public static Locale GetPhoneLocale() {
        if (mPhoneLocale == null) {
            return ApplicationDelegate.getApplication().getResources().getConfiguration().locale;
        }
        return mPhoneLocale;
    }

    @SuppressLint("NewApi")
    public static String getSerial() {
        String serial = "";
        try {
            serial = Build.SERIAL;
        } catch (Exception e) {
        }
        return serial;
    }

    public static boolean getRootAhth() {
        try {
            File su = new File("/system/bin/su");
            if (su.exists()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getMCC(Context applicationCtx) {
        if (sMCC != null) {
            return sMCC;
        }
        TelephonyManager telManager = (TelephonyManager) applicationCtx.getSystemService(
                Context.TELEPHONY_SERVICE);
        try {
            String mcc_mnc = telManager.getSimOperator();
            if (mcc_mnc != null && mcc_mnc.length() >= 3) {
                sMCC = mcc_mnc.substring(0, 3);
            }
        } catch (Exception e) {
            sMCC = "NULL";
        }
        return sMCC;
    }

    public static String getAndroidID(Context applicationCtx) {
        if (sAndroidID != null) {
            return sAndroidID;
        }
        sAndroidID = Settings.System.getString(applicationCtx.getContentResolver(),
                Settings.System.ANDROID_ID);
        return sAndroidID;
    }

    /**
     * @param applicationCtx
     * @return string of version name
     * @brief get version name, for example : 1.0.0
     */
    public static String getVersionName(Context applicationCtx) {
        if (sVersionName != null) {
            return sVersionName;
        }
        initVersionInfo(applicationCtx);
        return sVersionName;
    }

    /**
     * @param applicationCtx
     * @return integer of version code
     * @brief get version code, for example : 10000100
     */
    public static String getVersionCode(Context applicationCtx) {
        if (sVersionCode != null) {
            return sVersionCode;
        }
        initVersionInfo(applicationCtx);
        return sVersionCode;
    }

    public static String getPhoneModel() {
        return Build.MODEL;
    }

    public static String getEncodedBrand() {
        String brand = "";
        try {
            brand = URLEncoder.encode(Build.BRAND, "UTF-8");
        } catch (Exception e) {
        }

        return brand;
    }

    public static String getEncodedPhoneModel() {
        String model = "";
        try {
            model = URLEncoder.encode(Build.MODEL, "UTF-8");
        } catch (Exception e) {
        }

        return model;
    }

    public static int getApiLevel() {
        return Build.VERSION.SDK_INT;
    }

    //将形如30110001(3-01-1-0001)版本号格式化为3.1.1.1
    public static String translateDecimal(int version) {
        return String.format(Locale.US, "%d.%d.%d.%d",
                version / 10000000,
                version / 100000 % 100,
                version / 10000 % 10,
                version % 10000
        );
    }

    private static synchronized void initVersionInfo(Context applicationCtx) {
        if (sVersionCode != null) {
            return;
        }
        try {
            PackageInfo info = applicationCtx.getPackageManager()
                    .getPackageInfo(applicationCtx.getPackageName(), 0);

            sVersionCode = translateDecimal(info.versionCode);
            sVersionName = info.versionName;
        } catch (NameNotFoundException e) {
            KLog.e(LOGTAG, "Package is not found: " + applicationCtx.getPackageName());
        }
    }

    public static String getNumCoresStr() {
        int c = getNumCores();
        String r = "" + c;
        return r;
    }

    /**
     * wifi、3G、4G、2G
     *
     * @return
     */
    public static String getNetWorkStr(Context context) {
        String network = "";

        int nNet = KNetUtils.getNetworkState();
        if (nNet == ConnectivityManager.TYPE_MOBILE) {
            network = "2G";
        } else if (nNet == ConnectivityManager.TYPE_WIFI) {
            network = "wifi";
        }
        return network;
    }

    private static long getTotalRAM() {
        RandomAccessFile reader = null;
        String load = null;
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
        }

        if (TextUtils.isEmpty(load)) {
            return -1;
        }

        int off = load.indexOf(MEM_TOTAL);
        if (off == -1) {
            KLog.e("CHECK", "Can't get memory total:" + load);
            return -1;
        }

        off = off + MEM_TOTAL.length();

        int end = load.indexOf(MEM_UNIT);
        if (end == -1) {
            KLog.e("CHECK", "Can't get memory total:" + load);
            return -1;
        }

        String num = load.substring(off, end).trim();
        KLog.d("CHECK", "RAM:" + num);
        long size = Long.valueOf(num);
        return size;
    }

    /**
     * Gets the number of cores available in this device, across all processors.
     * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu"
     *
     * @return The number of cores, or 1 if failed to get result
     */
    private static int getNumCores() {
        // Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                // Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            // Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            // Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            // Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            // Default to return 1 core
            return 1;
        }
    }

    public static boolean isLowEndModels() {
        if (getNumCores() >= 2 && getTotalRAM() > 1024000) {
            return false;
        }
        return true;
    }


    /**
     * xgs 判断当前是不是锁屏状态。
     * *
     */
    public final static boolean isScreenLocked(Context c) {
        try {
            KeyguardManager mKeyguardManager = (KeyguardManager) c
                    .getSystemService(Context.KEYGUARD_SERVICE);
            return mKeyguardManager.inKeyguardRestrictedInputMode();
        } catch (Exception e) {
            KLog.d(TAG, "isScreenLocked error:" + e.getMessage());
        }
        return false;
    }

    public final static String getBuildVersion() {
        return Build.VERSION.RELEASE;
    }

    public final static boolean isOSVersion5() {
        String osversion = Build.VERSION.RELEASE;
        if (osversion.indexOf("5.0") > -1) {
            return true;
        }
        return false;
    }

    public final static String getUUid(Context applicationCtx) {
        if (sAndroidID != null) {
            return sAndroidID;
        }

        sAndroidID = Settings.System.getString(applicationCtx.getContentResolver(),
                Settings.System.ANDROID_ID);
        return sAndroidID;
    }
//
//    public final static String getIdent(Context context) {
//        return UserHelper.readUserId(context);
//    }

    public static String getResolution(Context ctx) {
        String s = getWindowWidth(ctx) + " x " + getWindowHeight(ctx);
        return s;
    }

    public static int getWindowWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getWindowHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static float getFileCacheCity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 获取手机内存大小
     *
     * @return
     */
    public static String getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        String ret = "";
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");
            localBufferedReader.close();

            long initial_memory = Integer.valueOf(
                    arrayOfString[1]).intValue() / 1024;// 获得系统总内存，单位是KB，除以1024转换为M
            ret = String.valueOf(initial_memory);
        } catch (Exception e) {
            ret = "";
        }
        return ret;
    }

    /**
     * 获取当前应用包名
     *
     * @param context
     * @return
     */
    public static String getAndroidPkg(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String pkg = null;
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            pkg = packInfo.packageName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return pkg;
    }

    public static int getUid(Context context,
                             String packageName) {
        int uid = 0;
        if (context != null) {
            ActivityManager am = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            try {
                List<ActivityManager.RunningAppProcessInfo> infoList = am.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo apps : infoList) {
                    if (apps.processName.equals(packageName)) {
                        uid = apps.uid;
                        break;
                    }
                }
            } catch (Exception e) {
                /*- maybe ----exception stack trace----
                java.lang.IndexOutOfBoundsException: Invalid index 16, size is 0
                at java.util.ArrayList.throwIndexOutOfBoundsException(ArrayList.java:251)
                at java.util.ArrayList.get(ArrayList.java:304)
                at android.app.AppGlobals.getFilteredProcessList(AppGlobals.java:137)
                at android.app.ActivityManager.getRunningAppProcesses(ActivityManager.java:1600)
                */
            }
        }
        return uid;
    }

    /**
     * @return
     * @note get densityDpi 屏幕密度DPI（120 / 160 / 240）
     */
    public static int getDensityDpi(Context context) {
        //		DisplayMetrics metric = new DisplayMetrics();
        //		activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        //		return metric.densityDpi;
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    /**
     * 获取当前设备的屏幕高度（不包含虚拟导航栏的高度）
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        if (windowManager == null) {
            return 0;
        }

        Display display = windowManager.getDefaultDisplay();
        if (display == null) {
            return 0;
        }

        DisplayMetrics dMetrics = new DisplayMetrics();
        display.getMetrics(dMetrics);
        int screenHeight = Math.max(dMetrics.widthPixels, dMetrics.heightPixels);
        return screenHeight;
    }

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        if (windowManager == null) {
            return 0;
        }

        Display display = windowManager.getDefaultDisplay();
        if (display == null) {
            return 0;
        }

        DisplayMetrics dMetrics = new DisplayMetrics();
        display.getMetrics(dMetrics);
        //        int screenHeight = Math.max(dMetrics.widthPixels, dMetrics.heightPixels);
        //        return screenHeight;
        return dMetrics.widthPixels > dMetrics.heightPixels ? dMetrics.heightPixels : dMetrics.widthPixels;
    }

    /**
     * 注意，这个函数有时候会返回null。建议使用Env.getExternalStorageDirectoryx()
     */
    public static File getExternalFilesRootDir() {
        try {
            return ApplicationDelegate.getApplication().getExternalFilesDir(null);
        } catch (NullPointerException e) {
            return null;
        } catch (SecurityException e) {
            //fix http://trace.cm.ijinshan.com/index/dump?version=&date=20140708&thever=0&dumpkey=2769283760&field=%E6%97%A0&field_content=2769283760
            return null;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }

    }
}
