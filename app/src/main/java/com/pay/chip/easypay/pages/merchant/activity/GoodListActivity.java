package com.pay.chip.easypay.pages.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;
import com.erban.pulltorefresh.PullToRefreshBase;
import com.pay.chip.easypay.R;
import com.pay.chip.easypay.pages.merchant.adapter.GoodAdapter;
import com.pay.chip.easypay.pages.merchant.adapter.ShoppingCarAdapter;
import com.pay.chip.easypay.pages.merchant.event.GoodEvent;
import com.pay.chip.easypay.pages.merchant.event.UpdateMenuListEvent;
import com.pay.chip.easypay.pages.merchant.model.GoodModel;
import com.pay.chip.easypay.pages.merchant.model.ShoppingCartData;
import com.pay.chip.easypay.util.Constant;
import com.pay.chip.easypay.util.CustomToast;
import com.pay.chip.easypay.util.EBPullToRefreshListView;
import com.pay.chip.easypay.util.HttpProcessManager;
import com.pay.chip.easypay.util.ListViewForScrollView;
import com.pay.chip.easypay.util.LoginDataHelper;
import com.pay.chip.easypay.util.VolleyManager;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class GoodListActivity extends AppCompatActivity implements View.OnClickListener , GoodAdapter.OrderMenuAdapterClickListener{
    private EBPullToRefreshListView pullToRefreshLV;

    private PullToRefreshBase.Mode lastMode;
    protected GoodAdapter goodAdapter;
    private Handler mHandler = new Handler();
    public ListView mListView;
    private RelativeLayout bottomRL;
    private RelativeLayout car_title_layout;
    private RelativeLayout selectedOrderRL;
    private TextView countTV, totalMoneyTV;
    private TextView selectedOrderTitle;
    private ShoppingCarAdapter selectedAdapter;

    private ListViewForScrollView selectedOrderLV;

    private Button okBtn;


    private ViewGroup anim_mask_layout;//动画层
    private final int ANIM_LEN = 350;
    private final int ANIM_LEN_2 = ANIM_LEN / 2;
    private final int WHAT_START_ANIM = 1;
    private MyHandler myHandler = new MyHandler();

    private String id;

    private TextView reg_head;
    private FrameLayout user_back,shoppingCarFL;    ;

    public static void startGoodListActivity(Context context,String id) {
        Intent intent = new Intent(context, GoodListActivity.class);
        Bundle b = new Bundle();
        b.putString(Constant.ID, id);
        intent.putExtras(b);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_list);
        EventBus.getDefault().register(this);
        perseIntent();
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {

        bottomRL = (RelativeLayout) findViewById(R.id.bottomRL);

        user_back = (FrameLayout) findViewById(R.id.leftFL);
        user_back.setOnClickListener(this);
        reg_head = (TextView) findViewById(R.id.titleTV);
        reg_head.setText(getResources().getString(R.string.good_list));

        countTV = (TextView) bottomRL.findViewById(R.id.countTV);
        totalMoneyTV = (TextView) bottomRL.findViewById(R.id.totalMoneyTV);
        okBtn = (Button) bottomRL.findViewById(R.id.okBtn);
        shoppingCarFL = (FrameLayout) bottomRL.findViewById(R.id.shoppingCarFL);

        selectedOrderLV = (ListViewForScrollView) findViewById(R.id.selectedOrderLV);
        selectedAdapter = new ShoppingCarAdapter(this);
        selectedOrderLV.setAdapter(selectedAdapter);

        okBtn.setOnClickListener(this);
        shoppingCarFL.setOnClickListener(this);
        selectedOrderTitle = (TextView) findViewById(R.id.selectedOrderTitle);
        car_title_layout = (RelativeLayout) findViewById(R.id.car_title_layout);

        initPullToRefresh();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                firstRequestData(true);
            }
        }, 1000);
    }



    private void initPullToRefresh() {
        pullToRefreshLV = (EBPullToRefreshListView) findViewById(R.id.pullToRefreshLV);
        pullToRefreshLV.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        lastMode = pullToRefreshLV.getMode();
        goodAdapter = new GoodAdapter(GoodListActivity.this,this);


        selectedOrderRL = (RelativeLayout) findViewById(R.id.selectedOrderRL);
        selectedOrderRL.setOnClickListener(this);

        mListView = pullToRefreshLV.getRefreshableView();
        mListView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);


        mListView.setAdapter(goodAdapter);
        pullToRefreshLV.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                request();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                request();
            }

            @Override
            public void onTouchDownEvent(final MotionEvent event, final int position, final boolean isNewItem) {
            }
        });

        pullToRefreshLV.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (pullToRefreshLV.getMode() != PullToRefreshBase.Mode.BOTH) {
                    return;
                }
//                requestData(EBConstant.REQUEST_TYPE.REQUEST_PULL_UP);
                request();
            }
        });
    }

    private void request() {
        StringRequest request =  HttpProcessManager.getInstance().findGoods(Constant.HOST_GOODS_FIND, id);
        VolleyManager.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void perseIntent() {
        Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null) {
            return;
        }

        Bundle b = intent.getExtras();
        id = b.getString(Constant.ID);

    }

    public void firstRequestData(boolean needRefresh) {
        if (needRefresh) {
            doRefresh();
        }
    }

    private void doRefresh() {
        if (goodAdapter != null && goodAdapter.isEmpty()) {
            pullToRefreshLV.setRefreshing();
        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {


            case R.id.leftFL:
                finish();
                break;
            case R.id.selectedOrderRL:
                selectedOrderRL.setVisibility(View.INVISIBLE);
                break;
            case R.id.shoppingCarFL:
                if (selectedOrderRL.getVisibility() != View.VISIBLE) {
                    ShoppingCartData shoppingCartData = LoginDataHelper.getInstance().getShoppingCartData();
                    if (shoppingCartData == null || shoppingCartData.data == null || shoppingCartData.data.isEmpty()) {
                        CustomToast.showToast(getString(R.string.title_order_empty_tip));
                        return;
                    }
                    selectedOrderRL.setVisibility(View.VISIBLE);
                    selectedAdapter.setData(shoppingCartData.data, shoppingCartData.totalCount, shoppingCartData.totalMoney);
                } else {
                    selectedOrderRL.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.okBtn:
                ShoppingCartData shoppingCartData = LoginDataHelper.getInstance().getShoppingCartData();
                if (shoppingCartData == null || shoppingCartData.data == null || shoppingCartData.data.isEmpty()) {
                    CustomToast.showToast(getString(R.string.title_order_empty_tip));
                    return;
                }
                SubmitOrderActivity.startSubmitOrderActivity(GoodListActivity.this,shoppingCartData.data.get(0).getM_id());
                break;

        }
    }

    public void onEventMainThread(final GoodEvent event) {

        pullToRefreshLV.onRefreshComplete();
        if (event == null || event.data == null) {
            return;
        }

        goodAdapter.setData((ArrayList<GoodModel.DataEntity>) event.data,0,0);
    }

    public void onEventMainThread(UpdateMenuListEvent event) {
       ShoppingCartData shoppingCartData = LoginDataHelper.getInstance().getShoppingCartData();
        if (shoppingCartData == null) {
            return;
        }
        GoodModel.DataEntity itemData = event.itemData;
        if (itemData == null) {
            return;
        }
        goodAdapter.updateData(itemData, shoppingCartData.totalCount, shoppingCartData.totalMoney);
        if (shoppingCartData.totalCount > 0) {
            countTV.setText(shoppingCartData.totalCount + "");
            countTV.setVisibility(View.VISIBLE);
            okBtn.setVisibility(View.VISIBLE);
            totalMoneyTV.setText("共¥" + shoppingCartData.totalMoney);
            selectedOrderTitle.setText("(共" + shoppingCartData.totalCount + "件商品)");
            car_title_layout.setVisibility(View.VISIBLE);
        } else {
            countTV.setVisibility(View.INVISIBLE);
            okBtn.setVisibility(View.INVISIBLE);
            totalMoneyTV.setText(getResources().getString(R.string.shop_car_empty));
            car_title_layout.setVisibility(View.GONE);
        }
    }

    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    private View addViewToAnimLayout(final ViewGroup vg, final View view,
                                     int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }

    private void setAnim(final View v, int[] start_location) {
        anim_mask_layout = null;
        anim_mask_layout = createAnimLayout();
        anim_mask_layout.addView(v);//把动画小球添加到动画层
        final View view = addViewToAnimLayout(anim_mask_layout, v, start_location);
        int[] end_location = new int[2];// 这是用来存储动画结束位置的X、Y坐标
        shoppingCarFL.getLocationInWindow(end_location);// shopCart是那个购物车
        // 计算位移
        int endX = 0 - start_location[0] + 40;// 动画位移的X坐标
        int endY = end_location[1] - start_location[1];// 动画位移的y坐标
        TranslateAnimation translateAnimationX = new TranslateAnimation(0, endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0, 0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(ANIM_LEN);// 动画的执行时间
        view.startAnimation(set);
        // 动画监听事件
        set.setAnimationListener(new Animation.AnimationListener() {
            // 动画的开始
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
            }
        });
    }

    private void startShopAnimation() {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new ScaleAnimation(1.0f, 1.4f, 1.0f, 1.4f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
        animationSet.addAnimation(new AlphaAnimation(1.0f, 1.0f));
        animationSet.setDuration(ANIM_LEN_2);
        animationSet.setInterpolator(new AccelerateInterpolator());
        animationSet.setFillAfter(true);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(new ScaleAnimation(1.4f, 1.0f, 1.4f,
                        1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f));
                animationSet.addAnimation(new AlphaAnimation(1.0f, 1.0f));
                animationSet.setDuration(ANIM_LEN_2);
                animationSet.setInterpolator(new DecelerateInterpolator());
                animationSet.setFillAfter(false);
                shoppingCarFL.startAnimation(animationSet);
            }
        });
        shoppingCarFL.startAnimation(animationSet);
    }


    @Override
    public void onAddMenuBtnClicked(View view) {
        int[] start_location = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
        view.getLocationInWindow(start_location);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
        ImageView buyImg = new ImageView(GoodListActivity.this);// buyImg是动画的图片，我的是一个小球（R.drawable.sign）
        buyImg.setImageResource(R.drawable.order_anim_dot);// 设置buyImg的图片
        setAnim(buyImg, start_location);// 开始执行动画
//        LoginLog.log("onAddMenuBtnClicked");
        myHandler.sendEmptyMessageDelayed(WHAT_START_ANIM, ANIM_LEN);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case WHAT_START_ANIM:
//                    LoginLog.log("handleMessage");
                    startShopAnimation();
                    break;
            }

        }
    }
}
