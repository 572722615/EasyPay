package com.pay.chip.easypay.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 处理程序设置
 */

public class ServiceConfigManager {

    private String mstrSharedPreferenceName = null;
    private SharedPreferences mshardPreferences = null;
    private static Context context = null;

    //用户账号相关
    private static final String LOGIN_USER_INFO = "com.erban.LOGIN_USER_INFO";
    private static final String BAIDU_MAP_INFO = "com.erban.BAIDU_MAP_INFO";
    private static final String SPECIAL_WIFI_INFO = "com.erban.SPECIAL_WIFI_INFO";
    private static final String LINKED_SPECIAL_WIFI = "com.erban.LINKED_SPECIAL_WIFI";
    private static final String MERCHANT_NO_WIFI_DATA = "com.erban.MERCHANT_NO_WIFI_DATA";
    private static final String WIFI_LINKED_DATA = "com.erban.WIFI_LINKED_DATA";
    private static final String COLLECT_INFO = "com.erban.COLLECT_INFO";
    private static final String PERFER_UNUSED = "com.erban.PERFER_UNUSED";
    private static final String PERFER_USED = "com.erban.PERFER_USED";
    private static final String PERFER_OUT_TIME = "com.erban.PERFER_OUT_TIME";
    private static final String USER_INFORMATION = "com.erban.USER_INFORMATION";
    private static final String USER_ORDER = "com.erban.USER_ORDER";
    private static final String WIFI_DATA_LIST = "com.erban.WIFI_DATA_LIST";
    private static final String VERSION_INFO = "com.erban.VERSION_INFO";
    private static final String MEMBER_INFO = "com.erban.MEMBER_INFO";
    private static final String JPUSH_INFO_TAG = "com.erban.JPUSH_INFO_TAG";
    private static final String JPUSH_INFO_ALIAS = "com.erban.JPUSH_INFO_ALIAS";
    private static final String GUIDE_STATE = "com.erban.GUIDE_STATE";
    private static final String CALL_WAITER_TIME = "com.erban.CALL_WAITER_TIME";
    private static final String WIFI_MAP_RED_DOT = "com.erban.WIFI_MAP_RED_DOT";
    private static final String NEW_MESSAGE_LIST = "com.erban.NEW_MESSAGE_LIST";
    private static final String SEARCH_HISTORY_DATA = "com.erban.SEARCH_HISTORY_DATA";
    private static final String MERCHANT_LIST_RED_DOT = "com.erban.MERCHANT_LIST_RED_DOT";

    private static class InnerConfigManager {
        private static final ServiceConfigManager instance = new ServiceConfigManager(context);
    }

    private ServiceConfigManager(Context context) {
        mstrSharedPreferenceName = new String(context.getPackageName() + "_preferences");
        mshardPreferences = context.getSharedPreferences(mstrSharedPreferenceName,
                Context.MODE_MULTI_PROCESS);
    }

    public static ServiceConfigManager getInstance(Context context) {
        ServiceConfigManager.context = context;
        ServiceConfigManager cm = InnerConfigManager.instance;
        return cm;
    }



    //保存商家列表入口小红点
    public void setMerchantListRedDotState(boolean data) {
        setBooleanValue(MERCHANT_LIST_RED_DOT, data);
    }

    public boolean getMerchantListRedDotState() {
        return getBooleanValue(MERCHANT_LIST_RED_DOT, false);
    }

    //保存wifi地图小红点
    public void setWiFiRedDotState(boolean data) {
        setBooleanValue(WIFI_MAP_RED_DOT, data);
    }

    public boolean getWiFiRedDotState() {
        return getBooleanValue(WIFI_MAP_RED_DOT, false);
    }

    //保存引导页
    public void setGuideState(boolean data) {
        setBooleanValue(GUIDE_STATE, data);
    }

    public boolean getGuideState() {
        return getBooleanValue(GUIDE_STATE, false);
    }

    //搜索历史数据
    public void setSearchHistoryData(String data) {
        setStringValue(SEARCH_HISTORY_DATA, data);
    }

    public String getSearchHistoryData() {
        return getStringValue(SEARCH_HISTORY_DATA, "");
    }

    //个人中心消息
    public void setNewMessageList(String data) {
        setStringValue(NEW_MESSAGE_LIST, data);
    }

    public String getNewMessageList() {
        return getStringValue(NEW_MESSAGE_LIST, "");
    }

    //连接上的WiFi数据
    public void setWiFiDataList(String data) {
        setStringValue(WIFI_DATA_LIST, data);
    }

    public String getWiFiDataList() {
        return getStringValue(WIFI_DATA_LIST, "");
    }

    //连接上的WiFi数据
    public void setLinkedWiFi(String data) {
        setStringValue(WIFI_LINKED_DATA, data);
    }

    public String getLinkedWiFi() {
        return getStringValue(WIFI_LINKED_DATA, "");
    }

    //没有连接上专属WiFi时的缓存商家列表数据
    public void setMerchantNoWiFiResp(String data) {
        setStringValue(MERCHANT_NO_WIFI_DATA, data);
    }

    public String getMerchantNoWiFiResp() {
        return getStringValue(MERCHANT_NO_WIFI_DATA, "");
    }

    //version数据缓存列表
    public void setVersionInfoResp(String data) {
        setStringValue(VERSION_INFO, data);
    }

    public String getVersionInfoResp() {
        return getStringValue(VERSION_INFO, "");
    }

    //collect数据缓存列表
    public void setCollectInfoResp(String data) {
        setStringValue(COLLECT_INFO, data);
    }

    public String getCollectInfoResp() {
        return getStringValue(COLLECT_INFO, "");
    }

    //member数据缓存列表
    public void setMemberInfoResp(String data) {
        setStringValue(MEMBER_INFO, data);
    }

    public String getMemberInfoResp() {
        return getStringValue(MEMBER_INFO, "");
    }

    //perferUnsed数据缓存列表
    public void setPerferUnusedInfoResp(String data) {
        setStringValue(PERFER_UNUSED, data);
    }

    public String getPerferUnusedInfoResp() {
        return getStringValue(PERFER_UNUSED, "");
    }

    //PerferUsed数据缓存列表
    public void setPerferUsedInfoResp(String data) {
        setStringValue(PERFER_USED, data);
    }

    public String getPerferUsedInfoResp() {
        return getStringValue(PERFER_USED, "");
    }

    //PerferOutTime数据缓存列表
    public void setPerferOutTimeInfoResp(String data) {
        setStringValue(PERFER_OUT_TIME, data);
    }

    public String getPerferOutTimeInfoResp() {
        return getStringValue(PERFER_OUT_TIME, "");
    }

    //Order数据缓存列表
    public void setOrderData(String data) {
        setStringValue(USER_ORDER, data);
    }

    public String getOrderData() {
        return getStringValue(USER_ORDER, "");
    }

    //JPush数据缓存列表
    public void setJPushTag(boolean data) {
        setBooleanValue(JPUSH_INFO_TAG, data);
    }

    public boolean getJPushTag() {
        return getBooleanValue(JPUSH_INFO_TAG, false);
    }

    public void setJPushAlias(boolean data) {
        setBooleanValue(JPUSH_INFO_ALIAS, data);
    }

    public boolean getJPushAlias() {
        return getBooleanValue(JPUSH_INFO_ALIAS, false);
    }


    //UserInformation数据缓存列表
    public void setInformationResp(String data) {
        setStringValue(USER_INFORMATION, data);
    }

    public String getInformationResp() {
        return getStringValue(USER_INFORMATION, "");
    }

    //连接上的专属WiFi
    public void setLinkedSpecialWiFi(String data) {
        setStringValue(LINKED_SPECIAL_WIFI, data);
    }

    public String getLinkedSpecialWiFi() {
        return getStringValue(LINKED_SPECIAL_WIFI, "");
    }

    //专属WiFi列表
    public void setSpecialWiFiData(String data) {
        setStringValue(SPECIAL_WIFI_INFO, data);
    }

    public String getSpecialWiFiData() {
        return getStringValue(SPECIAL_WIFI_INFO, "");
    }

    //百度地图信息
    public void setLocationData(String data) {
        setStringValue(BAIDU_MAP_INFO, data);
    }

    public String getLocationData() {
        return getStringValue(BAIDU_MAP_INFO, "");
    }

    //登录服务器后返回的用户数据
    public void setLoginUserInfo(String data) {
        setStringValue(LOGIN_USER_INFO, data);
    }

    public String getLoginUserInfo() {
        return getStringValue(LOGIN_USER_INFO, "");
    }

    public void setIntValue(String key,
                            int value) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putInt(key, value);
        SharePreferenceUtil.applyToEditor(editor);
    }

    public int getIntValue(String key,
                           int defValue) {
        return getSharedPreference().getInt(key, defValue);
    }

    private SharedPreferences getSharedPreference() {
        return mshardPreferences;
    }


    public long getLongValue(String key,
                             long defValue) {
        return getSharedPreference().getLong(key, defValue);
    }

    public void setLongValue(String key,
                             long value) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putLong(key, value);
        SharePreferenceUtil.applyToEditor(editor);
    }

    public String getStringValue(String key,
                                 String defValue) {
        return getSharedPreference().getString(key, defValue);
    }

    public void setStringValue(String key,
                               String value) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void setBooleanValue(String key,
                                boolean value) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putBoolean(key, value);
        SharePreferenceUtil.applyToEditor(editor);
    }

    public boolean getBooleanValue(String key,
                                   boolean defValue) {
        return getSharedPreference().getBoolean(key, defValue);
    }
}
