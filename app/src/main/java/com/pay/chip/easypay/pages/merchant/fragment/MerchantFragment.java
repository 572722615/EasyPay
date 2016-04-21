package com.pay.chip.easypay.pages.merchant.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.baidu.mapapi.SDKInitializer;
import com.pay.chip.easypay.R;
import com.pay.chip.easypay.pages.merchant.activity.MapLocationActivity;


public class MerchantFragment extends Fragment implements View.OnClickListener{
    private View mRootView = null;
    private Button find;


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
        find = (Button) mRootView.findViewById(R.id.find);
        find.setOnClickListener(this);

        return mRootView;
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
