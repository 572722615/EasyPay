package com.pay.chip.easypay.pages.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;
import com.erban.pulltorefresh.PullToRefreshBase;
import com.pay.chip.easypay.R;
import com.pay.chip.easypay.pages.merchant.adapter.GoodAdapter;
import com.pay.chip.easypay.pages.merchant.event.GoodEvent;
import com.pay.chip.easypay.pages.merchant.model.GoodModel;
import com.pay.chip.easypay.util.Constant;
import com.pay.chip.easypay.util.EBPullToRefreshListView;
import com.pay.chip.easypay.util.HttpProcessManager;
import com.pay.chip.easypay.util.VolleyManager;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class GoodListActivity extends AppCompatActivity implements View.OnClickListener {
    private EBPullToRefreshListView pullToRefreshLV;

    private PullToRefreshBase.Mode lastMode;
    protected GoodAdapter goodAdapter;
    private Handler mHandler = new Handler();
    public ListView mListView;
    private String id;

    private TextView reg_head;
    private FrameLayout user_back;

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

        user_back = (FrameLayout) findViewById(R.id.leftFL);
        user_back.setOnClickListener(this);
        reg_head = (TextView) findViewById(R.id.titleTV);
        reg_head.setText(getResources().getString(R.string.good_list));

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
        goodAdapter = new GoodAdapter(GoodListActivity.this);

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


        }
    }

    public void onEventMainThread(final GoodEvent event) {

        pullToRefreshLV.onRefreshComplete();
        if (event == null || event.data == null) {
            return;
        }

        goodAdapter.setData((ArrayList<GoodModel.DataEntity>) event.data);
    }


}
