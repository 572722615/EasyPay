package com.pay.chip.easypay.util;

/**
 * Created by DavidLee on 2015/9/14.
 */
public class EBConstant {
    public final static String KEY = "18fnssnfl0efk89jrf348";
    public final static String KEY_SSID = "com.erban.KEY_SSID";
    public final static String KEY_BSSID = "com.erban.KEY_BSSID";
    public final static String KEY_NO_WIFI_SHOW_BACK_BTN = "com.erban.KEY_NO_WIFI_SHOW_BACK_BTN";
    public final static String KEY_MERCHANT_ID = "com.erban.KEY_MERCHANT_ID";
    public final static String KEY_LAT = "com.erban.KEY_LAT";
    public final static String KEY_LON = "com.erban.KEY_LON";
    public final static String KEY_MERCHANT_URL = "com.erban.KEY_MERCHANT_URL";
    public final static String KEY_MERCHANT_NAME = "com.erban.KEY_MERCHANT_NAME";
    public final static String KEY_DISCOUNT_ID = "com.erban.KEY_DISCOUNT_ID";
    public final static String KEY_MERCHANT_PHONE = "com.erban.KEY_MERCHANT_PHONE";
    public final static String KEY_SHARE_OBJECT = "com.erban.KEY_MERCHANT_SHARE";
    public final static String KEY_MERCHANT_MERCHANTLINKEDWIFIRESP = "com.erban.KEY_MERCHANT_MERCHANTLINKEDWIFIRESP";
    public final static String KEY_MERCHANT_DETAILVIEWPAGERDATA = "com.erban.KEY_MERCHANT_DETAILVIEWPAGERDATA";
    public final static String KEY_MERCHANT_CLICKED_POSTION = "com.erban.KEY_MERCHANT_CLICKED_POSTION";
    public final static String KEY_WIFI_RESPONSENEARWIFI = "com.erban.KEY_WIFI_RESPONSENEARWIFI";
    public final static String KEY_USER_SELECT_PIC = "com.erban.KEY_USER_SELECT_PIC";
    public final static String KEY_USER_TWO_CODE = "com.erban.KEY_USER_TWO_CODE";
    public final static String KEY_PAY_TWO_CODE = "com.erban.KEY_PAY_TWO_CODE";
    public final static String KEY_USER_ID = "com.erban.KEY_USER_ID";
    public final static String KEY_WIFI_COUNT = "log_wifi";
    public final static String KEY_MAIN_ACITVITY_FLAG = "com.erban.KEY_MAIN_ACITVITY_FLAG";
    //跳转地址
    public final static String HEAD_URL1 = "wifi://";
    public final static String HEAD_URL2 = "wifidd://";
    public final static String URL_MERCHANT_HEAD1 = HEAD_URL1 + "action=";
    public final static String URL_MERCHANT_HEAD2 = HEAD_URL2 + "action=";
    public final static String URL_MERCHANT_GET_DISCOUNT = "getDiscount"; //领取优惠
    public final static String URL_MERCHANT_SHOW_MAP = "showMap"; //显示地图
    public final static String URL_MERCHANT_DIAL = "dial"; //打电话
    public final static String URL_MERCHANT_JUMP2MERCHANT = "showMerchant"; //跳转到商家
    public final static String KEY_APP_CHANNEL_ID = "com.erban.beauty.KEY_APP_CHANNEL_ID";

    public final static String BAIDU_DEFAULE_LOCATION = "4.9E-324";
    public final static String BAIDU_DEFAULE_LOCATION_HEAD = "4.9E";
    public final static String PERMISSION_ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    public final static String PERMISSION_ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";
    public final static String PERMISSION_ACCESS_LOCATION_EXTRA_COMMANDS = "android.permission.ACCESS_LOCATION_EXTRA_COMMANDS";
    //QQ key
    public final static String QQAPPID = "1105204813";
    public final static String QQAPPKEY = "AiwBkmHQVDaPmAzb";
    //微信 key
    public final static String WEIXINID = "wxba0f2d4ad3978e56";
    public final static String WEIXINSECRET = "7813dd75b255f7e280d25e0dc45934b5";

    //支付宝
    // 商户PID
    public static final String PARTNER = "2088002198355793";
    // 商户收款账号
    public static final String SELLER = "xiang.wu@calli-tech.com";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "";

    //分享下载url
    public final static String SHARE_DOWNLOAD_URL = "http://www.510wifi.com/weixin_download_client.html?channel=";

    //注册登录
    public final static int MAX_REG_LEN = 18;
    public final static int MIN_REG_LEN = 6;

    //循环播放时间k
    public final static int ROUND_PLAY_INTERVAL = 3500;

    //图片显示比例
    public static final String CROP_RATE_SET = "com.erbang.CROP_RATE_SET";
    public static final double CROP_RATE_SET_RECOMMEND = /*2 / 3*/0.667;
    public static final double CROP_RATE_SET_BRAND = /*5 / 8*/0.625;
    public static final double CROP_RATE_SET_DISCOUNT = /*2 / 3*/0.667;
    public static final double CROP_RATE_SET_ORDER = /*9 / 16*/0.5625;
    public static final double CROP_RATE_SET_GUIDE_TOP = 0.84;

    //用户协议URL
    public final static String URL_USER_PROTOCOL = "http://admin.510wifi.com/static/ykyhxy.html";

    public static final int MERCHANT_VIEW_EMPTY_STATE_NETWORK_ERROR = 1;
    public static final int MERCHANT_VIEW_EMPTY_STATE_ALL_NOTHING = 2;

    public static final String CLASSIFY_UNUSED = "nouse";
    public static final String CLASSIFY_OVERDUE = "expire";
    public static final String CLASSIFY_USED = "used";

    public static final String ALL_CITY = "1";

    public static final String CLASSIFY_MY_CARD = "cardList";
    public static final String CLASSIFY_TAB_DRAW_CARD = "myGets";
    //点单
    public static final String ORDER_TAG_IS_HOT = "1";
    public static final String ORDER_TAG_HOT_NAME = "热销";
    public static final String ORDER_TAG_HOT_ID = "-99999";
    public static final String KEY_REMARK = "com.erban.KEY_REMARK";
    public static final String KEY_ORDER_DETAIL_SHOW_TOP_VIEW = "com.erban.KEY_ORDER_DETAIL_SHOW_TOP_VIEW";
    public static final String KEY_PAGE_FROM_PAGE = "com.erban.KEY_PAGE_FROM_PAGE";
    public static final String KEY_ORDER_DETAIL_ID = "com.erban.KEY_ORDER_DETAIL_ID";
    public final static String KEY_PUSH_EXTRA_ORDER_ID = "orderId";
    public final static String KEY_CARD_ID = "com.erban.KEY_CARD_ID";
    public final static String KEY_CARD_BALANCE = "com.erban.KEY_CARD_BALANCE";
    public final static String KEY_CARD_FIRM = "com.erban.KEY_CARD_FIRM";
    public final static String KEY_CARD_DISCOUNT_B = "com.erban.KEY_CARD_DISCOUNT_B";
    public final static String KEY_CARD_DISCOUNT_S = "com.erban.KEY_CARD_DISCOUNT_S";
    public final static String KEY_CARD_TYPENAME = "com.erban.KEY_CARD_TYPENAME";
    public final static String KEY_CARD_CARDBG = "com.erban.KEY_CARD_BG";
    public final static String KEY_CARD_ADDRESS = "com.erban.KEY_MERCHANT_ADDRESS";
    public final static String KEY_CARD_MOBILE = "com.erban.KEY_CARD_MOBILE";
    public final static String KEY_CARD_SHARE_LIST = "com.erban.KEY_SHARE_LIST";
    public final static String KEY_PUSH_EXTRA_DESK_NUM = "seatNum";
    public final static String WIFI_VERIFY_PARAM = "&mobilekey=wifidiandian";
    public final static int CALL_WAITER_PAGE_MERCHANT_OFFICIAL = 1;
    public final static int CALL_WAITER_PAGE_MERCHANT_LINKED = 2;
    public final static int CALL_WAITER_PAGE_ORDER_DETAIL = 3;

    public final static long CALL_WAITER_INTERVAL = 10 * 60 * 1000;
    public final static int NOTIC_MERCHANT_OFFICIAL = 4;


    public final static int NOTIC_MERCHANT_LINKED = 5;
    public static final int ORDER_DETAIL_FROM_PAGE_NORMAL = 0;

    public static final int ORDER_DETAIL_FROM_PAGE_PUSH = 1;
    //是否是专属Wifi 0是 2不是
    public final static String EXCLUSIVE = "0";
    public final static String NOT_EXCLUSIVE = "2";
    public final static String SEARCH_TYPE_MERCHANT = "merchant";
    public final static String SEARCH_TYPE_DISCOUNT = "discount";
    public final static String KEY_SEARCH_TYPE = "com.erban.KEY_SEARCH_TYPE";

    public final static String DISCOUNT_URL_TAIL = "client/h5/detail?id=";
    //是否有菜单会员 1有 0没有
    public final static String HAS_NO_MENU= "0";

    public final static String HAS_NO_MEMBER= "0";
    public final static int DISCOUNT_TAG_1= 1;
    public final static int DISCOUNT_TAG_2= 2;
    public final static int DISCOUNT_TAG_3= 3;

    public static enum REQUEST_TYPE {
        REQUEST_REFRESH,
        REQUEST_PULL_DOWN,
        REQUEST_PULL_UP,
    }

    public enum UserFlag {
        USER_LOGIN,
        USER_REGISTER,
        USER_FORGET,
        MY_PERFER,
        MY_ORDER,
        ORDER_DETAIL,
        MY_COLLECT,
        MY_MEMBER,
        MY_NEW,
        USER_OPTION,
        VERSION_UPDATE,
        ABOUT_US,
        USE_HELP,
        UserFlag,
        USER_PROTOCOL,
        USER_INFO,
        NEW_DETAIL,
        MEMBER_DETAIL,
        SHARE_US,
        MY_SHARE,
        SHARE_WIFI
    }

    public enum UpdateFlag {
        USER_HEAD,
        USER_NAME,
        USER_NICK,
        USER_SEX,
        USER_BIRTH,
        USER_PLACE,
        USER_PLACE_CITY,
        USER_PLACE_AREA,
        USER_PHONE,
        USER_QQ
    }
}
