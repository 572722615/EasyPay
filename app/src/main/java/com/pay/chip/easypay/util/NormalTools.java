package com.pay.chip.easypay.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;


import com.pay.chip.easypay.application.PayApplication;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class NormalTools {
    private static SimpleDateFormat dataFormat;

    public static String getDistance_KM(float distance_m) {
        if (distance_m <= 1000) {
            int dis = Math.round(distance_m);
            return String.valueOf(dis) + "m";
        }
        distance_m = distance_m / 1000;
        BigDecimal bd = new BigDecimal(String.valueOf(distance_m));
        bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
//        LoginLog.log("bd2= " +bd);
        return String.valueOf(bd) + "km";
    }

    public static String getSpeed_KB_MB(long distance_m) {
        String disStr = String.valueOf(distance_m);
        if (distance_m <= 1000) {
            return disStr + "KB/s";
        }
        double disM = distance_m / 1000.00;
        disStr = String.valueOf(disM);
        BigDecimal bd = new BigDecimal(disStr);
        bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
        return String.valueOf(bd) + "MB/s";
    }


    public static boolean isMiui() {

        String s = Build.DISPLAY;
        if (s != null) {
            if (s.toUpperCase().contains("MIUI")) {
                return true;
            }
        }

        s = Build.MODEL; // 灏忕背
        if (s != null) {
            if (s.contains("MI-ONE")) {
                return true;
            }
        }

        s = Build.DEVICE;
        if (s != null) {
            if (s.contains("mione")) {
                return true;
            }
        }

        s = Build.MANUFACTURER;
        if (s != null) {
            if (s.equalsIgnoreCase("Xiaomi")) {
                return true;
            }
        }

        s = Build.PRODUCT;
        if (s != null) {
            if (s.contains("mione")) {
                return true;
            }
        }

        return false;
    }

    public static int getPercent(int num1,
                                 int num2) {
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();

        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);

        String str = numberFormat.format((float) num1 / (float) num2 * 100);
        String result = "1";
        int index = str.indexOf(".");
        if (index == -1) {
            result = str;
        } else {
            result = str.substring(0, str.indexOf("."));
        }
        System.out.println("num1和num2的百分比为:" + result);

        return Integer.parseInt(result);
    }

    /**
     * 分享
     *
     * @param context
     * @param title        标题
     * @param content      内容
     * @param imageContent 图片
     */
    public static void shareMsg(Context context,
                                String title,
                                String content,
                                File imageContent) {
        Intent intent = new Intent(Intent.ACTION_SEND);// 该Intent设置为发送给支持ACTION_SEND的Activity
        intent.setType("text/plain");
        if (null != imageContent) {
            intent.setType("image/png");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageContent));
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        /**
         * createChooser方法接受Intent做参数，也同时接纳了Intent里面要求的filter（ACTION_SEND)
         * 只有支持ACTION_SEND的Activity才会被列入可选列表
         */
        context.startActivity(Intent.createChooser(intent, "选择分享项"));
    }


    /**
     * 当前日期加减n天后的日期，返回String   (yyyy-MM-dd)
     *
     * @param n
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String nDaysAftertodayYMD(int n) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.DAY_OF_MONTH, -n);
        return df.format(rightNow.getTime());
    }

    /**
     * 当前日期加减n天后的日期，返回String   (yyyy-mm-dd)
     *
     * @param n
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String nDaysAftertoday(int n) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm ss:ss");
        Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.DAY_OF_MONTH, -n);
        return df.format(rightNow.getTime());
    }

    /**
     * 判断字符串是否为空 true-空 false-非空
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        if (null == s || "".equals(s)) {
            return true;
        }
        return false;
    }

    /**
     * 判断文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean isFileIsExists(String path) {
        try {

            File f = new File(path);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }

    /**
     * 按正方形裁切图片
     */
    public static Bitmap ImageCrop(Bitmap bitmap) {

        if (bitmap == null) {
            return null;
        }

        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长

        int retX = w > h ? (w - h) / 2 : 0;// 基于原图，取正方形左上角x坐标
        int retY = w > h ? 0 : (h - w) / 2;

        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null,
                false);
        if (bitmap != null && !bitmap.equals(bmp)
                && !bitmap.isRecycled()) {
            //            bitmap.recycle();
            //            bitmap = null;
        }

        return bmp;
    }

    /**
     * Save Bitmap to a file.保存图片到SD卡。
     */
    public static void saveBitmapToFile(Context context,
                                        Bitmap bitmap,
                                        String _file)
            throws IOException {
        if (NormalTools.hasSdcard()) {
            BufferedOutputStream os = null;
            try {
                File file = new File(_file);
                // String _filePath_file.replace(File.separatorChar +
                // file.getName(), "");
                int end = _file.lastIndexOf(File.separator);
                String _filePath = _file.substring(0, end);
                File filePath = new File(_filePath);
                if (!filePath.exists()) {
                    filePath.mkdirs();
                }
                file.createNewFile();
                os = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        Log.e("ERROR", e.getMessage(), e);
                    }
                }
            }
        } else {
            Toast.makeText(context, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
        }
    }



    /**
     * 获取Android id
     *
     * @param context
     * @return
     */
    public static String getAndrodId(Context context) {
        ContentResolver resolver = context.getContentResolver();
        String androidId = Secure.getString(resolver, Secure.ANDROID_ID);
        System.out.println("Android ID=======" + androidId);
        return androidId;
    }

    /**
     * 获取屏幕分辨率
     *
     * @param context
     * @return
     */
    public static String getDisplay(Context context) {

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        String display = width + "*" + height;
        System.out.println(display);
        return display;
    }

    /**
     * 十六进制字符串
     *
     * @return
     */
    public static String getS() {
        String str = randomString(8);
        String hex = toHexString(str);
        return hex;
    }

    /**
     * 随机生成8位字符串
     *
     * @param length
     * @return
     */
    public static final String randomString(int length) {
        Random randGen = null;
        char[] numbersAndLetters = null;
        if (length < 1) {
            return null;
        }
        if (randGen == null) {
            randGen = new Random();
            numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz"
                    + "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
            // numbersAndLetters =
            // ("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
        }
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
            // randBuffer[i] = numbersAndLetters[randGen.nextInt(35)];
        }
        return new String(randBuffer);
    }


    /**
     * 将生成的字符串转化为十六进制
     *
     * @param s
     * @return
     */
    public static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    /**
     * 字符串转换为十六进制字符
     *
     * @param str
     * @return
     */
    public static String stringToHex(String str) {
        if (str == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        char[] keyChars = str.toCharArray();
        for (int i = 0; i < keyChars.length; i++) {
            builder.append(Integer.toHexString(keyChars[i]));
        }
        return builder.toString();
    }


    public static byte[] file2Byte(String filePath) {
        if (filePath == null) {
            return null;
        }
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return null;
            }
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * @param plainText 明文
     * @return 32位密文
     */
    public static String encryption(String plainText) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }


    /**
     * 得到当天日期的下一天
     *
     * @return
     */
    public static Date getNextData() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date nextDate = calendar.getTime();
        return nextDate;
    }

    public static String getAuthorityFromPermission(Context context,
                                                    String permission) {
        if (permission == null)
            return null;
        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(
                PackageManager.GET_PROVIDERS);
        if (packs != null) {
            for (PackageInfo pack : packs) {
                ProviderInfo[] providers = pack.providers;
                if (providers != null) {
                    for (ProviderInfo provider : providers) {
                        System.out.println(
                                "readPermission = " + provider.readPermission + ";authority =" + provider.authority);
                        if (permission.equals(provider.readPermission)) {
                            return provider.authority;
                        }
                        if (permission.equals(provider.writePermission)) {
                            return provider.authority;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取IMEI及MAC Address
     *
     * @param context
     * @return
     */
    public static String getIMEIAndMacAddress(Context context) {

        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(
                Context.TELEPHONY_SERVICE);
        String imei = TelephonyMgr.getDeviceId();

        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String walnMAC = wm.getConnectionInfo().getMacAddress();


        System.out.println("did ==" + imei + walnMAC);

        return imei + walnMAC;

    }

    /**
     * 获取MAC Address
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {

        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wm.getConnectionInfo();
        if (wifiInfo == null) {
            return null;
        }
        String walnMAC = wifiInfo.getMacAddress();
        return walnMAC;
    }

    /**
     * 获取IMEI
     *
     * @param context
     * @return
     */
    //    public static String getIMEI(Context context) {
    //
    //        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(
    //                Context.TELEPHONY_SERVICE);
    //        String imei = TelephonyMgr.getDeviceId();
    //        System.out.println("imei ==" + imei);
    //
    //        return imei;
    //
    //    }


    /**
     * 获取当前版本号 如： 1
     *
     * @param context
     * @return
     */
    public static int getLocalVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        int v = 1;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            v = info.versionCode;

        } catch (NameNotFoundException e) {

            e.printStackTrace();
        }
        return v;
    }

    /**
     * 获取版本号 如:1.0
     *
     * @param context
     * @return
     */
    public static String getLocalVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        String v = "1.0";
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            v = info.versionName;

        } catch (NameNotFoundException e) {

            e.printStackTrace();
        }
        return v;
    }

    /**
     * 获取当前app的应用名字
     */
    public static String getApplicationName(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) packageManager
                .getApplicationLabel(applicationInfo);
        return applicationName;
    }


    /**
     * 判断高德地图app是否已经安装
     */
    public static boolean getAppIn(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(
                    "com.autonavi.minimap", 0);
        } catch (NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        // 本手机没有安装高德地图app
        if (packageInfo != null) {
            return true;
        }
        // 本手机成功安装有高德地图app
        else {
            return false;
        }
    }

    /**
     * 如果距离小于1000 不保留小数点  如果大于1000 就除以1000  保留一位小数点 如果小数点为0 就不保留
     *
     * @param distance
     * @return
     */
    public static String showDistance(double distance) {

        String strDistance;

        if (distance < 1000) {
            strDistance = String.valueOf(distance);
            if (strDistance != null && !strDistance.equals("")) {
                strDistance = strDistance.substring(0, strDistance.indexOf("."));

            }
        } else {
            double geolist = distance / 1000;

            strDistance = String.valueOf(geolist);
            if (strDistance != null && !strDistance.equals("")) {
                strDistance = strDistance.substring(0, strDistance.indexOf(".") + 2);

                String p = strDistance.substring(strDistance.indexOf(".") + 1,
                        strDistance.length());

                int i = Integer.valueOf(p);
                if (i == 0) {
                    p = strDistance.substring(0, strDistance.indexOf("."));
                    strDistance = p;
                }
            }
        }

        return strDistance;
    }

    /**
     * 得到当前包名
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        PackageManager pm = context.getPackageManager();
        String pkg = null;
        PackageInfo info;
        try {
            info = pm.getPackageInfo(context.getPackageName(), 0);
            if (info != null) {
                pkg = info.packageName;
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return pkg;
    }

    /**
     * 检查是否存在SDCard
     *
     * @return
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取app路径
     * @return
     */
    public static String getAppFilePath(Context context) {
        String iconDir = context.getFilesDir().getAbsolutePath() + File.separatorChar + "usericon" + File.separatorChar;
        File f = new File(iconDir);
        if (!f.exists() || !f.isDirectory()) {
            f.mkdirs();
        }
        return iconDir;
    }


    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
            return sdDir.toString();
        }
        return null;

    }

    // 得到系统安装的所有用户程序
    public static List<PackageInfo> getUserPkgInfoList() {
        PackageManager pm = PayApplication.getAppContext().getPackageManager();
        List<PackageInfo> packageInfoList = null;
        try {
            packageInfoList = pm.getInstalledPackages(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == packageInfoList) {
            return null;
        }
        List<PackageInfo> userAppPkgInfoList = new ArrayList<PackageInfo>();

        for (PackageInfo pkgInfo : packageInfoList) {
            ApplicationInfo appInfo = pkgInfo.applicationInfo;
            if (null == appInfo || (appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0
                    || (appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                continue;
            } else {
                userAppPkgInfoList.add(pkgInfo);
            }
        }
        return userAppPkgInfoList;
    }


    private static final int FACEBOOK_INSTALLED = 1;
    private static final int FACEBOOK_LITE_INSTALLED = 2;
    private static final int ALL_INSTALLED = 3;
    private static final int ALL_NOT_INSTALLED = 4;
    public static final String APP_FACEBOOK = "com.facebook.katana";
    public static final String APP_FACEBOOK_LITE = "com.facebook.lite";

    /**
     * 判断Facebook 或者 Facebook Lite app是否已经安装
     */
    public static int checkFacebookInstalled(Context context) {
        if (context == null || context.getPackageManager() == null) {
            return -1;
        }
        PackageInfo packageInfo;
        int res = 0;
        boolean facebookInstalled;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(
                    APP_FACEBOOK, 0);
        } catch (NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo != null) {
            facebookInstalled = true;
        } else {
            facebookInstalled = false;
        }

        boolean facebook_lite_installed;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(
                    APP_FACEBOOK_LITE, 0);
        } catch (NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo != null) {
            facebook_lite_installed = true;
        } else {
            facebook_lite_installed = false;
        }

        if (facebookInstalled && !facebook_lite_installed) {
            res = FACEBOOK_INSTALLED;
        } else if (!facebookInstalled && facebook_lite_installed) {
            res = FACEBOOK_LITE_INSTALLED;
        } else if (facebookInstalled && facebook_lite_installed) {
            res = ALL_INSTALLED;
        } else {
            res = ALL_NOT_INSTALLED;
        }
        return res;
    }

    public static String getNetworkType() {
        Context context = PayApplication.getAppContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return "1"; //not connected
        }
        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            return "5";
        }
        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return "2";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return "3";
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return "4";
                default:
                    return "0";
            }
        }
        return "0";
    }

}
