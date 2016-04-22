package com.pay.chip.easypay.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.erban.pulltorefresh.PullToRefreshListView;
import com.erban.pulltorefresh.internal.LoadingLayout;
import com.pay.chip.easypay.R;

public class EBPullToRefreshListView extends PullToRefreshListView {
    public EBPullToRefreshListView(Context context) {
        super(context);
    }

    public EBPullToRefreshListView(Context context,
                                   AttributeSet attrs) {
        super(context, attrs);
    }

    public EBPullToRefreshListView(Context context,
                                   Mode mode) {
        super(context, mode);
    }

    public EBPullToRefreshListView(Context context,
                                   Mode mode,
                                   AnimationStyle style) {
        super(context, mode, style);
    }



    @Override
    protected void handleStyledAttributes(TypedArray a) {
//        super.handleStyledAttributes(a);

        mListViewExtrasEnabled = a.getBoolean(
                com.erban.pulltorefresh.R.styleable.PullToRefresh_ptrListViewExtrasEnabled, true);

        if (mListViewExtrasEnabled) {
            final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);

            // Create Loading Views ready for use later
            FrameLayout frame = new FrameLayout(getContext());
            mHeaderLoadingView = createLoadingLayout(getContext(), Mode.PULL_FROM_START, a);
            mHeaderLoadingView.setVisibility(View.GONE);
            frame.addView(mHeaderLoadingView, lp);
            mRefreshableView.addHeaderView(frame, null, false);
            mRefreshableView.setOverScrollMode(OVER_SCROLL_NEVER);

            mLvFooterLoadingFrame = new FrameLayout(getContext());
            mFooterLoadingView = createLoadingLayout(getContext(), Mode.PULL_FROM_END, a);
            mFooterLoadingView.setVisibility(View.GONE);
//            mFooterLoadingView.setVisibility(View.VISIBLE);
            mLvFooterLoadingFrame.addView(mFooterLoadingView, lp);

            /**
             * If the value for Scrolling While Refreshing hasn't been
             * explicitly set via XML, enable Scrolling While Refreshing.
             */
            if (!a.hasValue(
                    com.erban.pulltorefresh.R.styleable.PullToRefresh_ptrScrollingWhileRefreshingEnabled)) {
                setScrollingWhileRefreshingEnabled(true);
            }
        }
    }

    @Override
    protected void updateUIForMode() {
        setShowIndicator(false);

        super.updateUIForMode();
    }

    @Override
    protected LoadingLayout createLoadingLayout(Context context,
                                                Mode mode,
                                                TypedArray attrs) {
        LoadingLayout layout = new EBPullRefreshLoadingLayout(context, mode, Orientation.VERTICAL,
                                                          attrs);
        layout.setVisibility(View.INVISIBLE);
        return layout;
    }

    public void showListEnd(){
        mFooterLoadingView.setVisibility(View.VISIBLE);
        mFooterLoadingView.getmHeaderProgress().setVisibility(View.GONE);
        mFooterLoadingView.getmHeaderImage().setVisibility(View.GONE);
        mFooterLoadingView.getmHeaderText().setText(getResources().getText(R.string.no_more_data));
        mFooterLoadingView.getmHeaderText().setTextColor(getResources().getColor(R.color.company_grey));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, 50);
        layoutParams.gravity = Gravity.CENTER;
        mFooterLoadingView.getmHeaderText().setLayoutParams(layoutParams);

    }
}
