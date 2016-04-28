package com.pay.chip.easypay.pages.merchant.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.toolbox.StringRequest;
import com.baidu.mapapi.SDKInitializer;
import com.erban.pulltorefresh.PullToRefreshBase;
import com.pay.chip.easypay.R;
import com.pay.chip.easypay.pages.merchant.activity.MapLocationActivity;
import com.pay.chip.easypay.pages.merchant.adapter.MerchantAdapter;
import com.pay.chip.easypay.pages.merchant.event.LocationChangeEvent;
import com.pay.chip.easypay.pages.merchant.event.MerchantEvent;
import com.pay.chip.easypay.pages.merchant.model.MerchantModel;
import com.pay.chip.easypay.util.Constant;
import com.pay.chip.easypay.util.EBPullToRefreshListView;
import com.pay.chip.easypay.util.HttpProcessManager;
import com.pay.chip.easypay.util.VolleyManager;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;


public class MerchantFragment extends Fragment implements View.OnClickListener{
    private View mRootView = null;
    private Button find;
    private EBPullToRefreshListView pullToRefreshLV;

    private PullToRefreshBase.Mode lastMode;
    protected MerchantAdapter merchantAdapter;
    private Handler mHandler = new Handler();
    public ListView mListView;

    private String lat = "0.0";
    private String lng = "0.0";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        initPullToRefresh();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                firstRequestData(true);
            }
        }, 1000);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SDKInitializer.initialize(getActivity().getApplicationContext());
        mRootView = inflater.inflate(R.layout.fragment_merchant, container, false);
        initView();
        find = (Button) mRootView.findViewById(R.id.find);
        find.setOnClickListener(this);

        return mRootView;
    }

    private void initPullToRefresh() {
       pullToRefreshLV = (EBPullToRefreshListView) mRootView.findViewById(R.id.pullToRefreshLV);
        pullToRefreshLV.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        lastMode = pullToRefreshLV.getMode();
        merchantAdapter = new MerchantAdapter(getActivity());

        mListView = pullToRefreshLV.getRefreshableView();
//        mListView.setSelector(getResources().getDrawable(R.drawable.transparent));
//        mListView.setSelector(R.drawable.transparent);
        mListView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

//        merchantAdapter = new MyRecordAdapter(this,balance,cardId);

        mListView.setAdapter(merchantAdapter);
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
        StringRequest request =  HttpProcessManager.getInstance().findMerchant(Constant.HOST_MERCHANT_FIND,lat,lng);
        VolleyManager.getInstance(getActivity().getApplicationContext()).addToRequestQueue(request);
    }


    public void firstRequestData(boolean needRefresh) {
        if (needRefresh) {
            doRefresh();
        }
    }

    private void doRefresh() {
        if (merchantAdapter != null && merchantAdapter.isEmpty()) {
            pullToRefreshLV.setRefreshing();
        }
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.find:
                MapLocationActivity.startMapLocation(getActivity());
                break;
        }
    }

    public void onEventMainThread(MerchantEvent event) {
        pullToRefreshLV.onRefreshComplete();
        if (event == null||event.data==null) {
            return;
        }

        merchantAdapter.setData((ArrayList<MerchantModel.DataEntity>) event.data);
    }

    public void onEventMainThread(final LocationChangeEvent event) {

        if(event==null){
            return;
        }
        lat = event.latitude+"";
        lng = event.longitude+"";

        mHandler.postDelayed(new Runnable() {
            public void run() {
//                pullToRefreshLV.setRefreshing();
                StringRequest request =  HttpProcessManager.getInstance().findMerchant(Constant.HOST_MERCHANT_FIND,lat,lng);
                VolleyManager.getInstance(getActivity().getApplicationContext()).addToRequestQueue(request);
            }
        }, 1000);

    }


}
