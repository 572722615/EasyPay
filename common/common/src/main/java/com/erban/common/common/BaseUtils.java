package com.erban.common.common;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.zip.CRC32;

public class BaseUtils {
    private static int currentVersionCode = -1;
    private static String currentVersionName = null;

    public static boolean hasIceCreamSandwich() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasHoneycombMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2;
    }

    public static boolean hasJellyBean() {
        //return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
        return Build.VERSION.SDK_INT >= 16;
    }

    public static boolean hasEclair() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR;
    }

    public static boolean isTablet(Context context) {
        int screenLayoutMask = context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        return (screenLayoutMask == 4)// Configuration.SCREENLAYOUT_SIZE_XLARGE = 4
                || (screenLayoutMask == Configuration.SCREENLAYOUT_SIZE_LARGE);
    }

    public static boolean isAppInstalled(Context context, String name) {
        if (context == null || name == null || name.isEmpty())
            return false;
        PackageManager pm = context.getPackageManager();
        try {
            int status = pm.getApplicationEnabledSetting(name);
            return status == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT || status == PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
        } catch (IllegalArgumentException e) {
        } catch (NullPointerException e) { // fix https://app.crittercism.com/developers/crash-details/1aaaa0f8def86aba780eb93005b06f66233ea33924565a7a2e9e8e4a
        }
        return false;
    }

    public static String getCpuFeatures() {
        FileReader fr = null;
        BufferedReader localBufferedReader = null;
        StringBuilder sb = new StringBuilder();
        try {
            fr = new FileReader("/proc/cpuinfo");
            localBufferedReader = new BufferedReader(fr, 8192);
            String s;
            while ((s = localBufferedReader.readLine()) != null) {
                s = s.trim();
                if (s.startsWith("Features")) {
                    String[] arrayOfString = s.split("\\s+");
                    if (arrayOfString.length >= 2) {
                        for (int i = 2; i < arrayOfString.length; i++) {
                            sb.append(arrayOfString[i]);
                            sb.append(" ");
                        }
                    }
                }
            }
        } catch (Throwable e) {
        } finally {
            if (localBufferedReader != null)
                try {
                    localBufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (fr != null)
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return sb.toString();
    }

    /**
     * 检查CPU是否有指定的属性，默认算作有
     *
     * @param feature
     * @return
     */
    public static boolean cpuHasFeature(String feature) {
        FileReader fr = null;
        BufferedReader localBufferedReader = null;
        try {
            fr = new FileReader("/proc/cpuinfo");
            localBufferedReader = new BufferedReader(fr, 8192);
            String s;
            while ((s = localBufferedReader.readLine()) != null) {
                s = s.trim();
                if (s.startsWith("Features")) {
                    String[] arrayOfString = s.split("\\s+");
                    if (arrayOfString.length >= 2) {
                        boolean hasFeatrue = false;
                        for (int i = 2; i < arrayOfString.length; i++) {
                            if (arrayOfString[i] != null && arrayOfString[i].trim().equalsIgnoreCase(feature)) {
                                hasFeatrue = true;
                                break;
                            }
                        }
                        return hasFeatrue;
                    }
                }
            }
        } catch (Throwable e) {
        } finally {
            if (localBufferedReader != null)
                try {
                    localBufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (fr != null)
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return true;
    }

    /**
     * Checks if OpenGL ES 2.0 is supported on the current device.
     *
     * @param context the context
     * @return true, if successful
     */
    public static boolean supportsOpenGLES2(final Context context) {
        final ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();
        return configurationInfo.reqGlEsVersion >= 0x20000;
    }

    public static boolean inList(Iterable<String> list, String model) {
        if (model == null || list == null)
            return false;
        // model = model.toUpperCase(Locale.ENGLISH);
        for (String blankModel : list) {
            if (model.startsWith(blankModel))
                return true;
        }
        return false;
    }

    public static boolean showDialogFragment(FragmentManager fragmentManager, DialogFragment dialogFragmet, String tag) {
        if (fragmentManager == null || fragmentManager.isDestroyed()) return false;
        try {
            // 用官方推荐的方法：
            FragmentTransaction ft = fragmentManager.beginTransaction();
            Fragment prev = fragmentManager.findFragmentByTag(tag);
            if (prev != null) {
                ft.remove(prev);
            }
//			ft.addToBackStack(null);

            // Create and show the dialog.
            dialogFragmet.show(ft, tag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getDayOfMonth() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 两个日期，是不是同一自然天
     *
     * @param time1
     * @param time2
     * @return
     */
    public static boolean isSameDay(long time1, long time2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(time1);
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(time2);
        return c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR) && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
    }

    public static Drawable createShapeDrawable(int color, int cornerRadius) {
        PaintDrawable drawable = new PaintDrawable(color);
        // drawable.setPadding(padding, padding, padding, padding);
        drawable.setCornerRadius(cornerRadius);
        return drawable;
    }

    public static Drawable createShapeDrawable(int colorNormal, int colorPressed, int cornerRadius) {
        Drawable normal = createShapeDrawable(colorNormal, cornerRadius);
        if (colorPressed == colorNormal)
            return normal;
        Drawable press = createShapeDrawable(colorPressed, cornerRadius);
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, press);
        drawable.addState(new int[]{}, normal);
        return drawable;
    }

    /**
     * 获取String的CRC值
     *
     * @param localData
     * @return
     */
    public static long getStringCRC(String localData) {
        if (localData == null)
            return 0;
        CRC32 crc = new CRC32();
        crc.update(localData.getBytes());
        return crc.getValue();
    }

    /**
     * 数组拼接
     *
     * @param first
     * @param second
     * @return
     */
    public static <T> T[] arrayConcat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    /**
     * 设置TextView的上下左右drawable，并catch OOM
     *
     * @param tv
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return 是否设置成功
     */
    public static boolean setTextCompoundDrawableWithCatchOOM(TextView tv, int left, int top, int right, int bottom) {
        if (tv == null)
            return false;
        try {
            tv.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
            return true;
        } catch (OutOfMemoryError e) {
            return false;
        }
    }

    /**
     * 设置ImageView的drawable，并catch OOM
     *
     * @param imageView
     * @param resID
     */
    public static void setImageResourceWithCatchOOM(ImageView imageView, int resID) {
        // 20141023 从com.roidapp.photogrid.cloud.ShareFinish移到这里来
        if (imageView == null)
            return;
        try {
            imageView.setImageResource(resID);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            imageView.setImageResource(0);
            imageView.setImageBitmap(null);
        }
    }

    public static int getAppVersionCode(Context context) {
        if (currentVersionCode == -1) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                currentVersionCode = packageInfo.versionCode;
                currentVersionName = packageInfo.versionName;
            } catch (NameNotFoundException e) {
                // should never happen
                // throw new RuntimeException("Could not get package name: " + e);
                return -1;
            }
        }
        return currentVersionCode;
    }

    public static String getAppVersionName(Context context) {
        if (currentVersionName == null) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                currentVersionCode = packageInfo.versionCode;
                currentVersionName = packageInfo.versionName;
            } catch (NameNotFoundException e) {
                // should never happen
                // throw new RuntimeException("Could not get package name: " + e);
                return null;
            }
        }
        return currentVersionName;
    }

    public static int[] randomSort(int[] seed) {
        int len = seed.length;
        int[] result = new int[len];
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            int r = random.nextInt(len - i);
            result[i] = seed[r];
            seed[r] = seed[len - 1 - i];
        }
        return result;
    }

    //文本转码
    public static String toDBC(String str) {
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }

        }
        return new String(c);
    }

    // 替换、过滤特殊字符
    public static String StringFilter(String str) throws PatternSyntaxException {
        str=str.replaceAll("【","[").replaceAll("】","]").replaceAll("！","!");//替换中文标号
        String regEx="[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}
