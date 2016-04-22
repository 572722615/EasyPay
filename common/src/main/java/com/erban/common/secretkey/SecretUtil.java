
package com.erban.common.secretkey;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


@SuppressWarnings("JavadocReference")
public class SecretUtil {
//    private static final String SECRET_URL = "http://andmlbf.tj.ijinshan.com/start/?";
//    private static final String POST_URL = "http://andmlbf.tj.ijinshan.com/data/?";
//    private static final String HOST = "10.33.20.15";
//    private static final String ALGORITHM_DES = "DES/ECB/NoPadding";
    /**
     * test server
     */
//    public static final boolean TEST_SERVER_DEBUG = false;

//    public static String getUUID() {
//        String uuid = UUID.randomUUID().toString();
//        if (uuid != null && uuid.length() >= 8) {
//            return uuid.substring(0, 8);
//        }
//        return uuid;
//    }

    /**
     * 字符串转换为十六进制字符
     *
     * @param str
     * @return
     */
//    public static String stringToHex(String str) {
//        if (str == null) {
//            return null;
//        }
//
//        StringBuilder builder = new StringBuilder();
//        char[] keyChars = str.toCharArray();
//        for (int i = 0; i < keyChars.length; i++) {
//            builder.append(Integer.toHexString(keyChars[i]));
//        }
//        return builder.toString();
//    }

    /**
     * @param key
     * @param pid 产品id
     * @return
     */
//    public static SecretObject getSecretKey(Context context,
//                                            String key,
//                                            String pid) {
//        try {
//            String android_id = AppEnvUtils.getAndroidID(context);
//            String hexKey = stringToHex(key);
//            String did = UserHelper.readUserId(context);
//            //            String pid ="10000";//每个项目与后台协商定义
//            StringBuilder urlParamBuilder = new StringBuilder();
//            urlParamBuilder.append(SECRET_URL).append("s=");
//            urlParamBuilder.append(hexKey);
//
//            urlParamBuilder.append("&ver=").append(AppEnvUtils.getVersionName(context));
//            urlParamBuilder.append("&pid=").append(pid);
//            urlParamBuilder.append("&did=").append(did);
//            urlParamBuilder.append("&type=").append(1);
//            urlParamBuilder.append("&android_id=").append(android_id);
//
//            HttpGet httpGet = new HttpGet(urlParamBuilder.toString());
//
//            if (TEST_SERVER_DEBUG) {
//                HttpHost httpHost = new HttpHost(HOST, 80);
//                BasicHttpParams params = new BasicHttpParams();
//                params.setParameter(ConnRouteParams.DEFAULT_PROXY, httpHost);
//                httpGet.setParams(params);
//            }
//
//            HttpResponse response = new DefaultHttpClient().execute(httpGet);
//            if (response.getStatusLine().getStatusCode() != 200) {
//                return null;
//            }
//
//            HttpEntity entity = response.getEntity();
//            InputStream is = entity.getContent();
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//
//            byte[] buffer = new byte[2048];
//            int length = -1;
//            while ((length = is.read(buffer)) > 0) {
//                bos.write(buffer, 0, length);
//            }
//            buffer = null;
//
//            byte[] data = Base64.decode(bos.toByteArray(), Base64.DEFAULT);
//            int dataLen = getDataLength(data, 1);
//
//            if (dataLen >= data.length - 4) {
//                byte[] secretData = new byte[dataLen];
//                System.arraycopy(data, 4, secretData, 0, dataLen);
//                data = null;
//
//                byte[] decodeData = decodeDES(key, secretData);
//                String resultStr = new String(decodeData);
//
//                String[] resultArr = resultStr.split("\\|");
//                if (resultArr.length >= 3) {
//                    SecretObject obj = new SecretObject();
//                    obj.mSecretKey = resultArr[0];
//                    obj.mSecretId = resultArr[2];
//                    return obj;
//                }
//            }
//        } catch (Exception e) {
//        }
//        return null;
//    }

    /**
     * @param data
     * @param pid             产品id
     * @param channel         渠道号
     * @param install_channel 安装渠道号
     * @return
     */
//    public static boolean postSecretData(Context context,
//                                         byte[] data,
//                                         String pid,
//                                         String channel,
//                                         String install_channel) {
//        try {
//            String key = SecretUtil.getUUID();
//            SecretObject obj = SecretUtil.getSecretKey(context, key, pid);
//            String sdid = UserHelper.readUserId(context);
//            String android_id = AppEnvUtils.getAndroidID(context);
//            //            String channel ="10000";//渠道号
//            //            String install_channel ="10000";
//            String packageName = AppEnvUtils.getAndroidPkg(context);
//            String model = "";
//            try {
//                model = URLEncoder.encode(android.os.Build.MODEL, "utf-8");
//            } catch (Exception e) {
//            }
//            String brand = "";
//            try {
//                brand = URLEncoder.encode(android.os.Build.BRAND, "utf-8");
//            } catch (Exception e) {
//            }
//            String uptime = "";
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            uptime = sdf.format(new Date());
//            uptime = URLEncoder.encode(uptime);
//
//            StringBuilder urlParamBuilder = new StringBuilder();
//            urlParamBuilder.append(POST_URL);
//            urlParamBuilder.append("version=").append(AppEnvUtils.getVersionCode(context));
//            urlParamBuilder.append("&channel=").append(channel/*AppEnvUtils.getChannel(context)*/);
//            urlParamBuilder.append("&cn=").append(channel/*AppEnvUtils.getChannel(context)*/);
//            urlParamBuilder.append("&install_channel=").append(install_channel/*AppEnvUtils.getInstallChannel()*/);
//            urlParamBuilder.append("&did=").append(sdid);
//            urlParamBuilder.append("&type=").append(1);
//            urlParamBuilder.append("&sdk=").append(AppEnvUtils.getApiLevel() + "");
//            urlParamBuilder.append("&vga=").append(AppEnvUtils.getScreenWidth(context)).append('_')
//                           .append(AppEnvUtils.getScreenHeight(context));
//            urlParamBuilder.append("&dpi=").append(AppEnvUtils.getDensityDpi(context) + "");
//            urlParamBuilder.append("&device=").append(android.os.Build.DEVICE);
//            urlParamBuilder.append("&cpu1=").append(android.os.Build.CPU_ABI);
//            urlParamBuilder.append("&cpu2=").append(android.os.Build.CPU_ABI2);
//            urlParamBuilder.append("&uid=").append(AppEnvUtils.getUid(context, packageName) + "");
//            urlParamBuilder.append("&release=").append(android.os.Build.VERSION.RELEASE);
//            urlParamBuilder.append("&cores=").append(AppEnvUtils.getNumCoresStr());
//            urlParamBuilder.append("&model=").append(model);
//            urlParamBuilder.append("&android_id=").append(android_id);
//            urlParamBuilder.append("&imei=").append(AppEnvUtils.getIMEI(context));
//            urlParamBuilder.append("&app=").append("cheetah_fast");
//
//            urlParamBuilder.append("&cl=").append(AppEnvUtils.getLanguage());
//            urlParamBuilder.append("&mac=").append(AppEnvUtils.getMacAddress(context));
//            urlParamBuilder.append("&brand=").append(brand);
//            urlParamBuilder.append("&serial=").append(AppEnvUtils.getSerial());
//            urlParamBuilder.append("&root=").append(AppEnvUtils.getRootAhth() ? "1" : "0");
//            //            urlParamBuilder.append("&uuid=").append(AppEnvUtils.getUUID(context) + "");
//            urlParamBuilder.append("&prodid=").append("1");//1为国内版，2为国际版
//            urlParamBuilder.append("&mcc=").append(AppEnvUtils.getMCC(context));
//            urlParamBuilder.append("&uptime=").append(uptime);
//
//            int padDataLength = data.length + (8 - data.length % 8);
//            byte[] padData = new byte[padDataLength];
//            System.arraycopy(data, 0, padData, 0, data.length);
//
//            byte[] secretData = encodeDES(obj.mSecretKey, padData);
//
//            ByteArrayOutputStream zipSecretStream = new ByteArrayOutputStream();
//            GZIPOutputStream gzipStream = new GZIPOutputStream(zipSecretStream);
//            gzipStream.write(secretData);
//            gzipStream.flush();
//            gzipStream.close();
//
//            byte[] zipSecretData = zipSecretStream.toByteArray();
//
//            ByteArrayOutputStream postData = new ByteArrayOutputStream();
//            DataOutputStream dos = new DataOutputStream(postData);
//            dos.write('K');
//            dos.write('S');
//            dos.write('I');
//            dos.write('D');
//            dos.write('1');
//            dos.write('.');
//            dos.write('0');
//            dos.write('0');
//            dos.write(obj.mSecretId.getBytes());// SID
//            dos.write(padDigit(String.valueOf(zipSecretData.length)).getBytes());
//            dos.write(zipSecretData);
//
//            HttpEntity postEntity = new ByteArrayEntity(postData.toByteArray());
//            HttpPost httpPost = new HttpPost(urlParamBuilder.toString());
//
//            if (TEST_SERVER_DEBUG) {
//                BasicHttpParams params = new BasicHttpParams();
//                HttpHost httpHost = new HttpHost(HOST, 80);
//                params.setParameter(ConnRouteParams.DEFAULT_PROXY, httpHost);
//                httpPost.setParams(params);
//            }
//
//            httpPost.setEntity(postEntity);
//            HttpResponse response = new DefaultHttpClient().execute(httpPost);
//            if (response.getStatusLine().getStatusCode() == 200) {
//                return true;
//            }
//        } catch (Exception e) {
//        }
//
//        return false;
//    }

//    private static String padDigit(String str) {
//        if (str == null) {
//            return null;
//        }
//        String f = "%0" + (8 - str.length()) + "d" + str;
//        return String.format(f, 0);
//    }
//
//    private static int getDataLength(byte[] data,
//                                     int startIndex) {
//        if (data != null && startIndex + 3 < data.length) {
//            StringBuilder builder = new StringBuilder();
//
//            for (int i = 0; i < 3; i++) {
//                builder.append((char) data[startIndex + i]);
//            }
//            return Integer.parseInt(builder.toString());
//        }
//
//        return -1;
//    }

    /**
     * DES算法，加密
     *
     * @param data 待加密字符串
     * @param key  加密私钥，长度不能够小于8位
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @throws CryptException 异常
     */
//    public static byte[] encodeDES(String key,
//                                   byte[] data) throws Exception {
//        DESKeySpec dks = new DESKeySpec(key.getBytes());
//        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//        // key的长度不能够小于8位字节
//        Key secretKey = keyFactory.generateSecret(dks);
//        Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
//        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//        return cipher.doFinal(data);
//    }

    /**
     * DES算法，解密
     *
     * @param data 待解密字符串
     * @param key  解密私钥，长度不能够小于8位
     * @return 解密后的字节数组
     * @throws Exception 异常
     */
//    public static byte[] decodeDES(String key,
//                                   byte[] data) throws Exception {
//        DESKeySpec dks = new DESKeySpec(key.getBytes());
//        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//        // key的长度不能够小于8位字节
//        Key secretKey = keyFactory.generateSecret(dks);
//        Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
//        cipher.init(Cipher.DECRYPT_MODE, secretKey);
//
//        return cipher.doFinal(data);
//    }

//    public static class SecretObject {
//        public String mSecretKey;
//        public String mSecretId;
//    }

    private static String ALGORITHM_AES = "AES";

    /**
     * AES 加密（128位密钥）
     *
     * @param plainText
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encodeAES(byte[] plainText,
                                   String key) throws Exception {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        secureRandom.setSeed(key.getBytes());
        KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM_AES);
        kgen.init(128, secureRandom);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(enCodeFormat, ALGORITHM_AES);
        Cipher cipher = Cipher.getInstance(ALGORITHM_AES);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] result = cipher.doFinal(plainText);
        return result;
    }

    /**
     * AES 解密（128位密钥）
     *
     * @param cipherText
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decodeAES(byte[] cipherText,
                                   String key) throws Exception {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        secureRandom.setSeed(key.getBytes());
        KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM_AES);
        kgen.init(128, secureRandom);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(enCodeFormat, ALGORITHM_AES);
        Cipher cipher = Cipher.getInstance(ALGORITHM_AES);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] result = cipher.doFinal(cipherText);
        return result;
    }
}
