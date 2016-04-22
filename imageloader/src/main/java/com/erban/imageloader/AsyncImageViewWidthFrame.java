package com.erban.imageloader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;


public class AsyncImageViewWidthFrame extends AsyncImageView {

    private Rect mFrameRect;
    private Paint mPaint;

    public AsyncImageViewWidthFrame(Context context,
                                    AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#19000000"));
        mPaint.setStyle(Style.STROKE);
        mPaint.setStrokeWidth(1);
        mFrameRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawFrame(canvas);

    }

    private void drawFrame(Canvas canvas) {
        mFrameRect.top = 1;
        mFrameRect.left = 1;
        mFrameRect.right = getMeasuredWidth() - 1;
        mFrameRect.bottom = getMeasuredHeight() - 1;
        canvas.drawRect(mFrameRect, mPaint);
    }

    public void setFrameColor(int frameColor) {
        mPaint.setColor(frameColor);
    }

    public void setFrameWidth(float frameWidth) {
        mPaint.setStrokeWidth(frameWidth);
    }
}
