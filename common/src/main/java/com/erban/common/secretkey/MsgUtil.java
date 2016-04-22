
package com.erban.common.secretkey;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 泡泡指令系统相关通用工具
 *
 * @author Administrator
 */
public class MsgUtil {
//    public static final String DATEFORMAT5 = "yyyyMMddHHmmss";

    /**
     * @param address
     * @param params
     * @param proxy
     * @param timeOut 单位ms
     * @return
     */
    public static String httpGet(String address, String params, Proxy proxy,
                                 int timeOut) {

        try {
            URL url;
            if (TextUtils.isEmpty(params)) {
                url = new URL(address);
            } else {
                url = new URL(address + "?" + params);
                System.out.println("key url==" + address + "?" + params);
            }
            HttpURLConnection httpConn;
            if (proxy == null) {
                httpConn = (HttpURLConnection) url.openConnection();
            } else {
                httpConn = (HttpURLConnection) url.openConnection(proxy);
            }

            httpConn.setDoOutput(false);// 使用 URL 连接进行输出
            httpConn.setDoInput(true);// 使用 URL 连接进行输入
            httpConn.setUseCaches(false);// 忽略缓存

            httpConn.setReadTimeout(timeOut);
            httpConn.setConnectTimeout(timeOut);
            httpConn.setRequestMethod("GET");// 设置URL请求方法

            httpConn.connect();
            int responseCode = httpConn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuffer sb = new StringBuffer();
                String readLine;
                BufferedReader responseReader;
                responseReader = new BufferedReader(new InputStreamReader(
                        httpConn.getInputStream(), "UTF-8"), 10 * 1024);
                while (!TextUtils.isEmpty(readLine = responseReader.readLine())) {
                    sb.append(readLine).append("\n");
                }
                responseReader.close();
                httpConn.disconnect();
                return sb.toString();
            } else {
            }

        } catch (Exception e) {
        }

        return null;

    }

//    /**
//     * 组装请求的数据
//     *
//     * @throws MessageException
//     */
//    public static String getRequestParms(Context context, RequestArgs args,
//            String msgType, final HashMap<String, String> extraParams) {
//
//        StringBuffer params = new StringBuffer();
//
//        if (!Constants.TYPE_DEFAULT.equalsIgnoreCase(msgType)) {
//            params.append(Constants.MSG_TYPE).append("=").append(msgType);
//            params.append("&");
//        }
//
//        if (extraParams != null && !extraParams.isEmpty()) {
//            Set<String> keys = extraParams.keySet();
//            Iterator<String> it = keys.iterator();
//            while (it.hasNext()) {
//                String key = it.next();
//                params.append(key).append("=").append(extraParams.get(key));
//                params.append("&");
//            }
//        }
//
//        params.append(Constants.PRODUCT).append("=").append(args.mProduct);
//        params.append("&");
//        params.append(Constants.APP_VER).append("=")
//                .append(getSoftwareVersionCode(context));
//        params.append("&");
//        params.append(Constants.OS_VER).append("=")
//                .append(android.os.Build.VERSION.SDK_INT);
//
//        String imei = args.mImei;
//        if (TextUtils.isEmpty(imei)) {
//            imei = MsgUtil.getIMEI(context);
//        }
//        if (!TextUtils.isEmpty(imei)) {
//            params.append("&").append(Constants.IMEI).append("=").append(imei);
//        }
//
//        params.append("&");
//        params.append(Constants.TUNNEL).append("=").append(args.mChannel);
//
//        String userId = UserHelper.readUserId(context);
//        if (TextUtils.isEmpty(userId)) {
//            return null;
//        }
//        params.append("&");
//        params.append(Constants.DID).append("=").append(userId);
//        params.append("&");
//        params.append(Constants.OEM).append("=").append(args.mOEM);
//
//        String recentId = MsgSharedPref.getInstanse(context)
//                .getRecentCmdId();
//        if (!TextUtils.isEmpty(recentId)) {
//            params.append("&");
//            params.append(Constants.RECENT_ID).append("=").append(recentId);
//        }
//        try {
//            String device = URLEncoder.encode(android.os.Build.MODEL, "UTF-8");
//            params.append("&");
//            params.append(Constants.DEVICE).append("=").append(device);
//        } catch (UnsupportedEncodingException e) {
//        }
//        for (int i = 0; i < params.length(); i++) {
//            if (' ' == params.charAt(i)) {
//                params.setCharAt(i, '+');
//            }
//        }
//
//        byte[] secretParams = null;
//        try {
//            secretParams = OpenSSLDecryptor.encrypt(null, params.toString()
//                    .getBytes("utf-8"), Constants.SECRET_KEY);
//        } catch (Exception e) {
//        }
//        return Base64Util.encode(secretParams);
//    }

//    public static void getPackageProperties(Properties properties,
//            JSONArray array) {
//        final int length = array.length();
//        JSONObject jsonObject;
//        for (int i = 0; i < length; i++) {
//            try {
//                jsonObject = array.getJSONObject(i);
//                if (jsonObject != null) {
//                    properties.put(jsonObject.get(JsonKeys.NAME),
//                            jsonObject.get(JsonKeys.VALUE));
//                }
//            } catch (JSONException e) {
//            }
//        }
//
//    }

//    public static JSONObject getJSONCmd(JSONArray array, String type)
//            throws JSONException {
//
//        int length = array.length();
//        JSONObject jsonObject;
//
//        for (int i = 0; i < length; i++) {
//
//            jsonObject = array.getJSONObject(i);
//            if (jsonObject != null && !TextUtils.isEmpty(type)
//                    && type.equals(jsonObject.get(JsonKeys.TYPE))) {
//                return jsonObject;
//            }
//        }
//
//        return null;
//
//    }

//    public static void save2Properties(Context context, JSONObject jsonObject,
//            int type) throws JSONException, FileNotFoundException {
//    }
//
//    public static boolean verifyRequestArgs(RequestArgs args)
//            throws MessageException {
//        if (TextUtils.isEmpty(args.mPid)) {
//            throw new MessageException(ErrCode.ERR_ARG_NO_PID);
//        }
//
//        if (TextUtils.isEmpty(args.mProduct)) {
//            throw new MessageException(ErrCode.ERR_ARG_NO_PRODUCT);
//        }
//        return true;
//    }

//    public static String parse2String(String[][] data) {
//        StringBuilder sb = new StringBuilder();
//        for (String[] item : data) {
//            if (item == null || item.length != 2 || item[0] == null
//                    || item[1] == null)
//                continue;
//            sb.append(item[0]).append('=').append(item[1]).append('&');
//        }
//        int size = sb.length();
//        sb.delete(size - 1, size);
//        size = sb.length();
//        for (int i = 0; i < size; i++) {
//            if (sb.charAt(i) == ' ')
//                sb.setCharAt(i, '+');
//        }
//        return sb.toString();
//    }

//    public static String getIMEI(Context context) {
//        TelephonyManager tm = (TelephonyManager) context
//                .getSystemService(Context.TELEPHONY_SERVICE);
//        if (tm == null) {
//            return null;
//        } else {
//            return tm.getDeviceId();
//        }
//    }

    public static String getWifiMac(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

//    public static String getLocalIpAddress() {
//        try {
//            Enumeration<NetworkInterface> en = NetworkInterface
//                    .getNetworkInterfaces();
//            if (en != null) {
//                while (en.hasMoreElements()) {
//                    NetworkInterface intf = en.nextElement();
//                    Enumeration<InetAddress> enumIpAddr = intf
//                            .getInetAddresses();
//                    if (enumIpAddr != null) {
//                        while (enumIpAddr.hasMoreElements()) {
//                            InetAddress inetAddress = enumIpAddr.nextElement();
//                            if (!inetAddress.isLoopbackAddress()) {
//                                String ip = inetAddress.getHostAddress()
//                                        .toString();
//                                return filterIpForUrl(ip);
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            KLog.e("getLocalIpAddress:", ex.toString());
//        }
//        return null;
//    }

//    /**
//     * 过滤ip中的网卡名字
//     *
//     * @param ip
//     * @return
//     */
//    public static String filterIpForUrl(String ip) {
//        if (!TextUtils.isEmpty(ip)) {
//            if (ip.contains("%")) {
//                ip = ip.substring(0, ip.indexOf("%"));
//            }
//        }
//        return ip;
//    }

//    public static String getDisplay(Context context) {
//        // 得到设备分辨率
//        DisplayMetrics dm = new DisplayMetrics();
//        dm = context.getApplicationContext().getResources().getDisplayMetrics();
//        return dm.widthPixels + "*" + dm.heightPixels;
//    }

//    public static boolean isThisAppInRom(Context context) {
//        final Context contextac = context.getApplicationContext();
//
//        ComponentName cn = new ComponentName(context, contextac.getClass());
//        try {
//            PackageInfo info = context.getPackageManager().getPackageInfo(
//                    cn.getPackageName(), 0);
//            return ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
//        } catch (Exception e) {
//            return false;
//        }
//    }

//    public static void loadFromFile(Properties p, File file) {
//        FileInputStream fis = null;
//        try {
//            fis = new FileInputStream(file);
//            p.load(fis);
//        } catch (Exception e) {
//            // nothing
//        } finally {
//            if (fis != null) {
//                try {
//                    fis.close();
//                } catch (IOException ioe) {
//                    // ignore
//                }
//            }
//        }
//    }

//    /**
//     * 文件下载工具
//     *
//     * @param httpUrl 请求地址
//     * @param localPath 保存到本地路径
//     * @param localFileName 文件名
//     * @return
//     */
//    public static boolean downFile(String httpUrl, String localPath,
//            String localFileName) {
//        if (TextUtils.isEmpty(httpUrl) || TextUtils.isEmpty(localPath)
//                || TextUtils.isEmpty(localFileName)) {
//            return false;
//        }
//        mkDirs(localPath);
//        File file = new File(localPath + localFileName);
//        try {
//            URL url = new URL(httpUrl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            InputStream input = conn.getInputStream();
//
//            FileOutputStream out = new FileOutputStream(file);
//            byte[] b = new byte[1024];
//            int read;
//            while ((read = input.read(b)) != -1) {
//                out.write(b, 0, read);
//            }
//            input.close();
//            out.close();
//            if (conn.getResponseCode() == 200) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            if (file.exists()) {
//                file.delete();
//            }
//            return false;
//        }
//    }

//    public static boolean mkDirs(String path) {
//        if (TextUtils.isEmpty(path)) {
//            return false;
//        }
//        File file = new File(path);
//        if (file != null && !file.exists()) {
//            return file.mkdirs();
//        }
//        return true;
//    }

//    public static String getVersionName(Context context) {
//        if (context == null)
//            return null;
//
//        ComponentName cn = new ComponentName(context, context.getClass());
//        try {
//            PackageInfo info = context.getPackageManager().getPackageInfo(
//                    cn.getPackageName(), 0);
//            return info.versionName;
//        } catch (Exception e) {
//            return null;
//        }
//    }

//    public static String getVersionNameCode(Context context) {
//        if (context == null)
//            return null;
//
//        ComponentName cn = new ComponentName(context, context.getClass());
//        try {
//            PackageInfo info = context.getPackageManager().getPackageInfo(
//                    cn.getPackageName(), 0);
//
//            return info.versionName + "(" + info.versionCode + ")";
//        } catch (Exception e) {
//            return null;
//        }
//    }

//    public static int getVersionCode(Context context) {
//        if (context == null)
//            return -1;
//        ComponentName cn = new ComponentName(context, context.getClass());
//        try {
//            PackageInfo info = context.getPackageManager().getPackageInfo(
//                    cn.getPackageName(), 0);
//            return info.versionCode;
//        } catch (Exception e) {
//            return -1;
//        }
//    }

    // 判断网络是否存在
//    public static boolean IsNetworkAvailable(Context context) {
//        if (context != null) {
//            ConnectivityManager cm = (ConnectivityManager) context
//                    .getSystemService(Context.CONNECTIVITY_SERVICE);
//            if (cm != null) {
//                try {
//                    if (cm.getActiveNetworkInfo() != null) {
//                        if (cm.getActiveNetworkInfo().isAvailable()) {
//                            return true;
//                        }
//                    }
//                } catch (Exception e) {
//                    return false;
//                }
//
//            }
//            return false;
//        }
//        return false;
//    }

//    public static boolean IsWifiNetworkAvailable(Context context) {
//        if (context == null)
//            return false;
//
//        ConnectivityManager conmgr = (ConnectivityManager) context
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (conmgr == null) {
//            return false;
//        }
//
//        NetworkInfo activeNetInfo = null;
//        try {
//            activeNetInfo = conmgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        } catch (SecurityException e) {
//            // maybe have no permission :
//            // android.permission.ACCESS_NETWORK_STATE
//            activeNetInfo = null;
//        }
//        if (activeNetInfo == null) {
//            return false;
//        }
//        State wifi = activeNetInfo.getState();
//
//        if (wifi == State.CONNECTED)
//            return true;
//        return false;
//    }

//    public static int getNetworkType(Context cx) {
//        int type = ConnectivityManager.TYPE_WIFI;
//        if (cx == null) {
//            return type;
//        }
//
//        ConnectivityManager connectivityManager = (ConnectivityManager) cx
//                .getSystemService(Context.CONNECTIVITY_SERVICE);// 获取系统的连接服务
//
//        if (connectivityManager == null) {
//            return type;
//        }
//
//        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();// 获取网络的连接情况
//        if (activeNetInfo == null) {
//            return type;
//        }
//
//        return activeNetInfo.getType();
//    }

    public static int getSoftwareVersionCode(Context context) {
        if (context == null)
            return -1;
        ComponentName cn = new ComponentName(context, context.getClass());
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    cn.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            return -1;
        }
    }

//    public static int getSDKVersion() {
//        return android.os.Build.VERSION.SDK_INT;
//    }

//    public static float getDisplayDensityDpi(Context context) {
//        // 得到设备分辨率
//        DisplayMetrics dm = new DisplayMetrics();
//        dm = context.getApplicationContext().getResources().getDisplayMetrics();
//        return dm.densityDpi;
//    }

//    public static String getAbsDbFileDirPath(Context context) {
//        return context.getFilesDir().getAbsolutePath();
//    }

    /**
     * @param pattern yyyyMMdd/yyyy-MM-dd HH:mm:ss
     * @return
     */
//    public static String getNowFormatDate(String pattern) {
//        SimpleDateFormat df = new SimpleDateFormat(pattern);
//        return df.format(new Date());
//    }

    /***
     * Create properties from the file.
     *
     * @param fileName properties file name to load
     * @exception IOException
     */
//    public static Properties createFromFile(String fileName) {
//        return createFromFile(new File(fileName));
//    }

    /***
     * Create properties from the file.
     *
     * @param file properties file to load
     * @exception IOException
     */
//    public static Properties createFromFile(File file) {
//        Properties prop = new Properties();
//        loadFromFile(prop, file);
//        return prop;
//    }

    /***
     * Loads properties from the file. Properties are appended to the existing
     * properties object.
     *
     * @param p properties to fill in
     * @param fileName properties file name to load
     * @exception IOException
     */
//    public static void loadFromFile(Properties p, String fileName)
//            throws IOException {
//        loadFromFile(p, new File(fileName));
//    }

//    public static boolean checkApkInstallByIntent(Context context,
//            String packageName) {
//
//        if (TextUtils.isEmpty(packageName)) {
//            return false;
//        }
//        PackageManager packageManager = context.getPackageManager();
//
//        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
//        if (intent == null)
//            return false;
//        else {
//            List<ResolveInfo> list = packageManager.queryIntentActivities(
//                    intent, PackageManager.MATCH_DEFAULT_ONLY);
//            return list.size() > 0;
//        }
//    }

    /**
     * @param input
     * @return
     */
//    public static String md5(byte[] input) {
//        byte[] result = null;
//        try {
//            MessageDigest md5 = MessageDigest.getInstance("MD5");
//            result = md5.digest(input);
//        } catch (NoSuchAlgorithmException ex) {
//            result = new byte[16];
//        }
//        return byte2String(result);
//    }
    public static String md5(String input) {
        byte[] result = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            result = md5.digest(input.getBytes());
        } catch (NoSuchAlgorithmException ex) {
            result = new byte[16];
        }
        return byte2String(result);
    }

    public static String byte2String(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%1$02x", 0xff & b));
        }
        return sb.toString();
    }

    public static String HexToInterger(String hex) {
        StringBuffer str = new StringBuffer();
        if (hex.length() % 2 == 0) {
            for (int i = 0; i < (hex.length() / 2); i++) {
                str.append(Integer.parseInt(hex.substring(i, i + 1), 16));
            }
        }
        return str.toString();
    }
}
