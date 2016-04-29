package com.pay.chip.easypay.pages.discount.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.StringRequest;
import com.pay.chip.easypay.R;
import com.pay.chip.easypay.pages.discount.adapter.DiscountItemRecyclerViewAdapter;
import com.pay.chip.easypay.pages.merchant.event.DiscountEvent;
import com.pay.chip.easypay.util.Constant;
import com.pay.chip.easypay.util.HttpProcessManager;
import com.pay.chip.easypay.util.LoadMoreRecyclerView;
import com.pay.chip.easypay.util.VolleyManager;

import de.greenrobot.event.EventBus;


public class DiscountFragment extends Fragment {


    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private DiscountItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    private LoadMoreRecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int page = 0;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        recyclerView = (LoadMoreRecyclerView) view.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
      /*  view.findViewById(R.id.mode_switch_btn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (1 == mColumnCount) {
                    mColumnCount = 2;
                    ((TextView) v).setText(R.string.list_mode_stagger);
                    myItemRecyclerViewAdapter.switchMode(true);
                    recyclerView.switchLayoutManager(new StaggeredGridLayoutManager(mColumnCount, StaggeredGridLayoutManager.VERTICAL));
                } else {
                    mColumnCount = 1;
                    ((TextView) v).setText(R.string.list_mode_list);
                    myItemRecyclerViewAdapter.switchMode(false);
                    recyclerView.switchLayoutManager(new LinearLayoutManager(getActivity()));
                }
            }
        });*/
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                page = 0;
//                myItemRecyclerViewAdapter.setData();
                recyclerView.setAutoLoadMoreEnable(false);
                myItemRecyclerViewAdapter.notifyDataSetChanged();

            }
        });
      /*  if (1 >= mColumnCount) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(mColumnCount, StaggeredGridLayoutManager.VERTICAL));
        }*/
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        myItemRecyclerViewAdapter = new DiscountItemRecyclerViewAdapter(getActivity());
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myItemRecyclerViewAdapter);
        recyclerView.setAutoLoadMoreEnable(true);
        recyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        myItemRecyclerViewAdapter.notifyDataSetChanged();

//                        myItemRecyclerViewAdapter.addDatas(DummyContent.generyData(++page));
                        recyclerView.notifyMoreFinish(false);
                    }
                }, 1000);
            }
        });
        myItemRecyclerViewAdapter.notifyDataSetChanged();
        getDiscount();
        return view;
    }


    public void getDiscount(){

        StringRequest request =   HttpProcessManager.getInstance().getDicount(Constant.HOST_GET_DISCOUNT);
        VolleyManager.getInstance(getActivity().getApplicationContext()).addToRequestQueue(request);
    }

    public void onEventMainThread(DiscountEvent event) {

        if(event==null){
            return;
        }
        myItemRecyclerViewAdapter.setData(event.data);


    }


}
