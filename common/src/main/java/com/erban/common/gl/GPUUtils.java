package com.erban.common.gl;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;

import com.erban.common.common.BaseUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 放一些opengl相关的公用静态方法
 *
 * @author lb
 */
public class GPUUtils {

    private static GPUUtils _instance;
    private String _gpuModel = "", _gpuVendor;
    private boolean bSupportFilter = true;
    private List<String> GPU_CLOUD_WHITE_LIST;

    // 可使用pixelbuffer的白名单
    // VideoCore IV HW芯片对opengl es 2.0 支持的不好，使用pixelbuffer导致崩溃，程序过滤了这个芯片
    // 但对有些android 4.0 以上的手机，这个芯片可以正常使用，所以这里使用一个白名单打开，适用于Video和Filter
    // Galaxy Fame:S6810,6812
    // Galaxy Pocket:SS5310,S5312
    // SII plus : GT-I9105
    private static final List<String> GPU_WHITE_LIST = Arrays
            .asList("GT-S5301",
                    "GT-I9082",
                    "GT-S7392",
                    "GT-S6810",
                    "GT-S6812",
                    "GT-S5310",
                    "GT-S5312",
                    "GT-I9105",
                    "GT-B5330",
                    "GT-S5303",
                    "GT-I9060",
                    "GT-S7270",
                    "GT-S7272",
                    "GT-I9150",
                    "GT-I9152",
                    "GT-S7580");

    // //OpenGL 黑名单，这些手机直接不支持
    private static final List<String> GPU_BLACK_LIST = Arrays
            .asList(new String[]{
                    //"GT-S5302"
            });

    private static final List<String> CUSTOM_CAMERA_BLACK_LIST = Arrays
            .asList(new String[]{
            });

    private GPUUtils() {

    }

    public static GPUUtils getInstance() {
        if (_instance == null) {
            _instance = new GPUUtils();
        }
        return _instance;
    }

    public void setCloudWhiteList(List<String> cloudlist) {
        if (cloudlist == null)
            return;
        GPU_CLOUD_WHITE_LIST = new ArrayList<String>(cloudlist);
    }

    public void setSupportFilter(boolean bSupportFilter) {
        this.bSupportFilter = bSupportFilter;
    }

    public boolean getSupportFilter() {
        return bSupportFilter;
    }

    public boolean isVersionSupport() {
        // 有些2.1-update1系统APIlevel
        // 可能判断失败，需要进行字符串判断排除2.1系统
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO && (!(Build.VERSION.RELEASE.startsWith("2.1")));
    }

    public boolean isNeedGPUTest(Context context) {
        SharedPreferences preference = context.getSharedPreferences("GPUINFO",
                Context.MODE_PRIVATE);
        String _gpuModel = preference.getString("GPU_MODEL", "");
        if (TextUtils.isEmpty(_gpuModel)) {
            return true;
        } else {
            return false;
        }
    }

    // 支持滤镜
    // 判断滤镜是否支持要两个条件
    // 1. supportFilter 是否支持滤镜
    // 2. 如果支持，调用isSaveByScreenCapture判断用截屏方式保存，还是pixelbuffer保存
    public boolean supportFilters(final Context context) {
        boolean bHardware = isVersionSupport()
                && BaseUtils.supportsOpenGLES2(context);
        if (!bHardware)
            return false;
        String phoneModel = android.os.Build.MODEL;
        return !BaseUtils.inList(GPU_BLACK_LIST, phoneModel);
    }

    // Galaxy Y - GT-S5360
    // Galaxy Young - GT-S5363
    // Galaxy Y Duos - GT-S6102
    // Galaxy Y pro duos (GT-B5512)
    // Galaxy Ace (GT-S5830)
    // Galaxy Ace (GT-S5830C)
    // Galaxy Ace (GT-S5830i)
    // Galaxy Ace (GT-S5830M)
    // Galaxy Ace Duo (GT-S6802)
    // Galaxy Ace Duos (GT-S6802B)
    public boolean isSaveByScreenCapture() {
        if (!TextUtils.isEmpty(_gpuModel)) {
            // vendor = Broadcom生产的芯片，做滤镜会崩溃
            // http://en.wikipedia.org/wiki/VideoCore
            if (_gpuModel.equalsIgnoreCase("VideoCore IV HW")
                    || _gpuModel.contains("VideoCore IV")
                    || _gpuModel.equalsIgnoreCase("N/A")) {

                String phoneModel = android.os.Build.MODEL;

                List<String> whiteList = GPU_CLOUD_WHITE_LIST;
                if (whiteList == null || whiteList.size() == 0) {
                    whiteList = GPU_WHITE_LIST;
                }
                // 可以用pixelbuffer保存
                if (BaseUtils.inList(whiteList, phoneModel)) {
                    return false;
                }
                String device = android.os.Build.DEVICE;
                if (BaseUtils.inList(whiteList, device)) {
                    return false;
                }

                return true;
            }
        }

        if (phoneModel("GT-S5360") || phoneModel("GT-S5363")
                || phoneModel("GT-S5367") || phoneModel("GT-S6102")
                || phoneModel("GT-B5512") || phoneModel("GT-S5830")
                || phoneModel("GT-S6802") || phoneModel("GT-S7272")
                || phoneModel("LG-P509") || phoneModel("GT-S5300")) {
            return true;
        }

        return false;
    }

    // 支持视频，两个判断条件
    //1. supportFilters，是否支持openGL
    //2. isGPUCoreSupport，是否支持视频
    public boolean isGPUCoreSupport(Context context) {
        SharedPreferences preference = context.getSharedPreferences("GPUINFO",
                Context.MODE_PRIVATE);
        String _gpuModel = preference.getString("GPU_MODEL", "");
        // vendor = Broadcom生产的芯片，做滤镜会崩溃
        // http://en.wikipedia.org/wiki/VideoCore
        if (_gpuModel.equalsIgnoreCase("VideoCore IV HW")
                || _gpuModel.contains("VideoCore IV")) {
            String phoneModel = android.os.Build.MODEL;

            List<String> whiteList = GPU_CLOUD_WHITE_LIST;
            if (whiteList == null || whiteList.size() == 0) {
                whiteList = GPU_WHITE_LIST;
            }

            if (BaseUtils.inList(whiteList, phoneModel)) {
                return true;
            }
            String device = android.os.Build.DEVICE;
            if (BaseUtils.inList(whiteList, device)) {
                return true;
            }

            return false;
        }
        return true;
    }


//	public void testGPUChip(Handler handler, final Context context,
//			final ViewGroup view) {
//		if (view != null) {
//			GPUTestView gpuTest = new GPUTestView(context);
//			// {
//			// public void surfaceDestroyed(SurfaceHolder holder) {
//			// super.surfaceDestroyed(holder);
//			// }
//			// };
//			gpuTest.setId(0x10);
//			gpuTest.setLayoutParams(new ViewGroup.LayoutParams(1, 1));
//			gpuTest.setVisibility(View.VISIBLE);
//			view.addView(gpuTest);
//
//			gpuTest.startTest(handler);
//		}
//	}

    public void setGPUModel(Context context, String chipModel) {
        _gpuModel = chipModel;

        SharedPreferences preference = context.getSharedPreferences("GPUINFO",
                Context.MODE_PRIVATE);
        preference.edit().putString("GPU_MODEL", chipModel).apply();
    }

    public String getGPUModel(Context context) {
        if (TextUtils.isEmpty(_gpuModel)) {
            SharedPreferences preference = context.getSharedPreferences(
                    "GPUINFO", Context.MODE_PRIVATE);
            _gpuModel = preference.getString("GPU_MODEL", "");
        }
        return _gpuModel;
    }

    public boolean phoneModel(final String model) {
        String phoneModel = android.os.Build.MODEL;
        String device = android.os.Build.DEVICE;

        boolean is = phoneModel.startsWith(model) || device.startsWith(model);
        return is;
    }

    /**
     * 是否支持人脸美化
     * 需要支持opengl，pixelbuffer，android3.0以上
     *
     * @param context
     * @return
     */
    public boolean isRetouchSupport(Context context) {
        if (supportFilters(context) && !isSaveByScreenCapture()
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return true;
        } else
            return false;
    }

    private boolean isCustomCameraListSupport() {
        String phoneModel = android.os.Build.MODEL;
        if (BaseUtils.inList(CUSTOM_CAMERA_BLACK_LIST, phoneModel)) {
            return false;
        }
        return true;
    }

    public boolean isCustomCameraSupport(Context context) {
        if (BaseUtils.hasIceCreamSandwich() && isCustomCameraListSupport() && supportFilters(context)) {
            return true;
        } else {
            return false;
        }
    }
}
