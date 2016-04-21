package com.pay.chip.easypay.pages.merchant.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.pay.chip.easypay.R;


public class MerchantFragment extends Fragment {
    private View mRootView = null;
    private MapView mBaiduMap;

    public MerchantFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext());
        mRootView = inflater.inflate(R.layout.fragment_merchant, container, false);
        mBaiduMap = (MapView) mRootView.findViewById(R.id.bmapView);

        return mRootView;
    }



}
