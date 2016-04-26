package com.pay.chip.easypay.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import com.pay.chip.easypay.application.PayApplication;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by Li Xiaoqian on 2015/6/14.
 */
public class SelectIconHelper {
    public final static int MAX_VIEW_SIZE = 1024;
    private String[] items = new String[]{"图库", "拍照"};
    public static final int SELECT_PIC_CODE = 3;
    public static final int SELECT_CAMERA_CODE = 4;
    public static final String IMAGE_FILE_PATH = PayApplication.getInstance().getUserIconDir() + "pics" + File.separatorChar + "faceImage.jpg";
    public static final String IMAGE_FILE_PATH_FACEBOOK = PayApplication.getInstance().getUserIconDir() + "pics" + File.separatorChar + "faceImage_facebook.jpg";
    public static final String IMAGE_PATH_PATH = PayApplication.getInstance().getUserIconDir() + "pics" + File.separatorChar;

    private static SelectIconHelper instance = null;

    private SelectIconHelper() {
        File f = new File(IMAGE_PATH_PATH);
        if (f != null && !f.exists()) {
            f.mkdirs();
        }
        f = new File(IMAGE_FILE_PATH);
        if (f != null && f.exists()) {
            f.delete();
        }
    }

    public void initFacebookIcon() {
        File f = new File(IMAGE_FILE_PATH_FACEBOOK);
        if (f != null && f.exists()) {
            f.delete();
        }
    }

    public static SelectIconHelper getInstance() {
        if (instance == null) {
            synchronized (SelectIconHelper.class) {
                if (instance == null) {
                    instance = new SelectIconHelper();
                }
            }
        }
        return instance;
    }

    public void showSettingFaceDialog(Fragment fragment) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        fragment.startActivityForResult(intent, SELECT_PIC_CODE);
    }

    public void showSettingFaceDialog(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        activity.startActivityForResult(intent, SELECT_PIC_CODE);
    }

    public void showCameraDialog(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getCameraTempUri());
        activity.startActivityForResult(intent, SELECT_CAMERA_CODE);
    }

    public Uri getCameraTempUri(){
        return Uri.fromFile(new File(
                Environment.getExternalStorageDirectory(), "user_icon_temp.jpg"));
    }

    /**
     * 保存裁剪之后的图片数据
     */
    public boolean setImageToView(Intent data,
                                  final Activity activity,
                                  AsyncCircleImageView user_head_icon) throws IOException {
        if (data == null || data.getExtras() == null) {
            return false;
        }

        Uri croppedUri = data.getExtras().getParcelable(MediaStore.EXTRA_OUTPUT);
        if (croppedUri == null) {
            return false;
        }

        final Bitmap photo = getBitMap(activity, croppedUri, MAX_VIEW_SIZE);
        if (photo == null) {
            return false;
        }

        user_head_icon.setImageBitmap(photo);

        try {
            NormalTools.saveBitmapToFile(activity, photo, IMAGE_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

  /*  public void startPhotoCrop(Context context,
                               Uri uri) {
        if (context == null || uri == null) {
            return;
        }
        Intent intent = new Intent(context, CropImageActivity.class);
        intent.setData(uri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(IMAGE_FILE_PATH)));
        context.startActivity(intent);
    }*/

    public static Bitmap getBitMap(Context context,
                                   Uri uri,
                                   int maxSize) {

        if (maxSize <= 0) {
            maxSize = MAX_VIEW_SIZE;
        }

        Bitmap bitmap = null;
        try {
            if (uri.toString().length() <= uri.getScheme().length() + ":///".length()) {
                return null;
            }

            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            ParcelFileDescriptor des = context.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = des.getFileDescriptor();
            BitmapFactory.decodeFileDescriptor(fileDescriptor, null, opt);
            BitmapFactory.Options opt2 = getSampledOption(opt, maxSize);
            bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, opt2);
            des.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private static BitmapFactory.Options getSampledOption(BitmapFactory.Options options,
                                                          int maxSize) {
        int scale = 1;
        if (options.outHeight > maxSize || options.outWidth > maxSize) {
            scale = (int) Math.pow(2, (int) Math.round(Math.log(
                    maxSize / (double) Math.max(options.outHeight, options.outWidth)) / Math.log(
                    0.5)));
        }
        BitmapFactory.Options retOpt = new BitmapFactory.Options();
        retOpt.inSampleSize = scale;

        return retOpt;
    }
}

