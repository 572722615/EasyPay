package com.pay.chip.easypay.pages.merchant.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.baidu.mapapi.SDKInitializer;
import com.erban.pulltorefresh.PullToRefreshBase;
import com.pay.chip.easypay.R;
import com.pay.chip.easypay.pages.merchant.activity.MapLocationActivity;
import com.pay.chip.easypay.pages.merchant.adapter.MerchantAdapter;
import com.pay.chip.easypay.util.EBPullToRefreshListView;


public class MerchantFragment extends Fragment implements View.OnClickListener{
    private View mRootView = null;
    private Button find;
    private EBPullToRefreshListView pullToRefreshLV;

    private PullToRefreshBase.Mode lastMode;
    protected MerchantAdapter merchantAdapter;
    private Handler mHandler = new Handler();
    public ListView mListView;

    public MerchantFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
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
        find = (Button) mRootView.findViewById(R.id.find);
        find.setOnClickListener(this);

        return mRootView;
    }

    private void initPullToRefresh() {
//        pullToRefreshLV = (EBPullToRefreshListView) mRootView.findViewById(R.id.pullToRefreshLV);
        /*pullToRefreshLV.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        lastMode = pullToRefreshLV.getMode();


        mListView = pullToRefreshLV.getRefreshableView();
//        mListView.setSelector(getResources().getDrawable(R.drawable.transparent));
//        mListView.setSelector(R.drawable.transparent);
        mListView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

//        merchantAdapter = new MyRecordAdapter(this,balance,cardId);

        mListView.setAdapter(merchantAdapter);
        pullToRefreshLV.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                requestData(EBConstant.REQUEST_TYPE.REQUEST_PULL_DOWN);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                requestData(EBConstant.REQUEST_TYPE.REQUEST_PULL_UP);
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
                requestData(EBConstant.REQUEST_TYPE.REQUEST_PULL_UP);
            }
        });*/
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
}
