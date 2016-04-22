package com.pay.chip.easypay.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.pay.chip.easypay.R;


public class LoginProgressBar extends FrameLayout {

    private Context mContext = null;
    private FrameLayout mFrameLayout = null;
    private ImageView mProgressView1 = null;
    private ImageView mProgressView2 = null;
    private float mProgress = 0;
    private float mSecondaryProgress;

    private int mProgressWidthMin = 0;
    private int mProgressWidthMinScond = 2;
    private int mProgressWidth;
    private int mSecondaryProgressWidth;
    private boolean mNeedLayout = true;

    public LoginProgressBar(Context context,
                            AttributeSet attrs,
                            int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView();
    }

    public LoginProgressBar(Context context,
                            AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public LoginProgressBar(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public void setProgressWidthMin(int mProgressWidthMin) {
        this.mProgressWidthMin = mProgressWidthMin;
    }

    public void setProgressWidthMinSecond(int mProgressWidthMin) {
        this.mProgressWidthMinScond = mProgressWidthMin;
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.progress_bar, this);
        mFrameLayout = (FrameLayout) findViewById(R.id.cm_progress_bar_layout);
        mProgressView1 = (ImageView) findViewById(R.id.progress_bar_1);
        mProgressView2 = (ImageView) findViewById(R.id.progress_bar_2);
        mProgressWidthMin = DeviceUtils.dip2px(mContext, 8);
    }

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float progress) {
        if (progress != mProgress && progress >= 0 && progress <= 100) {
            this.mProgress = progress;
            layoutProgress1();
            requestLayout();
        }
    }

    public float getSecondaryProgress() {
        return mSecondaryProgress;
    }

    public void setSecondaryProgress(float secondaryProgress) {
        if (secondaryProgress != mSecondaryProgress && secondaryProgress >= 0
                && secondaryProgress <= 100) {
            this.mSecondaryProgress = secondaryProgress;
            layoutProgress2();
            requestLayout();
        }
    }

    public boolean isPrepareOk() {
        int width = mFrameLayout.getWidth();
        return width > 0 ? true : false;
    }

    public void setProgressBarHeight(int newHeight) {
        ViewGroup.LayoutParams params = mFrameLayout.getLayoutParams();
        params.height = newHeight;
        mFrameLayout.setLayoutParams(params);

        params = mProgressView1.getLayoutParams();
        params.height = newHeight;
        mProgressView1.setLayoutParams(params);

        params = mProgressView2.getLayoutParams();
        params.height = newHeight;
        mProgressView2.setLayoutParams(params);

        requestLayout();
    }

    public int getProgressWidth() {
        return mProgressWidth;
    }

    public int getSecondaryProgressWidth() {
        return mSecondaryProgressWidth;
    }

    /**
     * 获取两进度条差值块的中点到起点距离，用于设置Arrow的位置
     *
     * @return
     */
    public int getDiffCenterValue() {
        return mSecondaryProgressWidth
                + ((mProgressWidth - mSecondaryProgressWidth) / 2);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mNeedLayout) {
            layoutProgress2();
            layoutProgress1();
            mNeedLayout = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void layoutProgress1() {
        if (mProgressView1 == null) {
            return;
        }

        ViewGroup.LayoutParams params = mProgressView1.getLayoutParams();
        mProgressWidth = (int) (mProgress / 100.f * mFrameLayout.getWidth());
        if (mProgress > 0 && mProgressWidth < mSecondaryProgressWidth + mProgressWidthMinScond) {
            mProgressWidth = mSecondaryProgressWidth + mProgressWidthMinScond;
        }

        params.width = mProgressWidth;
        mProgressView1.setLayoutParams(params);
    }

    private void layoutProgress2() {
        if (mProgressView2 == null) {
            return;
        }
        ViewGroup.LayoutParams params = mProgressView2.getLayoutParams();
        mSecondaryProgressWidth = (int) (mSecondaryProgress / 100.f * mFrameLayout
                .getWidth());
        if (mSecondaryProgress > 0) {
            mSecondaryProgressWidth = Math.max(mProgressWidthMin, mSecondaryProgressWidth);
        }
        params.width = mSecondaryProgressWidth;
        mProgressView2.setLayoutParams(params);
    }

    public void setProgressLayoutBg(int resId) {
        mFrameLayout.setBackgroundResource(resId);
    }


    public void setProgressBg(int resId) {
        mProgressView1.setBackgroundResource(resId);
    }

    public void setSecondaryProgressBg(int resId) {
        mProgressView2.setBackgroundResource(resId);
    }
}
