package com.pay.chip.easypay.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


/**
 * 有关版本升级工具
 */
public class VersionUtil {

    //版本码
    public static int getVersionCode(Context context) {
        //  PackageInfo packageInfo;
       /* try {
            packageInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }*/
        int appVersionCode = 1;
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String name = context.getPackageName();
            appVersionCode = info.versionCode;   //版本名
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return appVersionCode;
    }

    //当前版本号
    public static String getVersionName(Context context) {
        String appVersionName = "";
        PackageManager manager = context.getPackageManager();

        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            appVersionName = info.versionName;   //版本名

        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return appVersionName;
    }

 /*   //更新版本
    public static void updateVersion(RespVersionModel respVersionModel, Activity activity) {
        if (respVersionModel != null) {

            FragmentManager transaction = activity.getFragmentManager();
            UpdateDialog dialog = UpdateDialog.newInstance(respVersionModel);
            dialog.setStyle(R.style.Dialog_No_Border, 0);
            dialog.show(transaction, "ConfirmShareDialog");

        }

    }*/
}
