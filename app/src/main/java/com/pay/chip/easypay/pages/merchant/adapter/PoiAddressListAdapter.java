package com.pay.chip.easypay.pages.merchant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pay.chip.easypay.R;
import com.pay.chip.easypay.pages.merchant.model.PoiItemModel;

import java.util.List;

public class PoiAddressListAdapter extends ArrayAdapter<PoiItemModel> {
    private Context mContext;
    private int resourceId;

    public PoiAddressListAdapter(Context context, int textViewResourceId, List<PoiItemModel> addressList) {
        super(context, textViewResourceId, addressList);
        this.resourceId = textViewResourceId;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        RelativeLayout cellView;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            cellView = (RelativeLayout)inflater.inflate(resourceId, null);
        } else {
            cellView = (RelativeLayout)convertView;
        }

        TextView mainAddrTextView =  (TextView)cellView.findViewById(R.id.main_addr);
        TextView secondAddrTextView = (TextView)cellView.findViewById(R.id.second_addr);
        ImageView selectedIconView = (ImageView)cellView.findViewById(R.id.selected_icon);

        PoiItemModel poiItem = getItem(position);
        mainAddrTextView.setText(poiItem.mainAddress);
        secondAddrTextView.setText(poiItem.secondAddress);

        ListView listView = (ListView)parent;
        if(listView.getCheckedItemPosition() == position) {
            selectedIconView.setVisibility(View.VISIBLE);
        } else {
            selectedIconView.setVisibility(View.GONE);
        }


        return cellView;
    }
}
