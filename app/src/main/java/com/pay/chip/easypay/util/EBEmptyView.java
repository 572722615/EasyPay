package com.pay.chip.easypay.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pay.chip.easypay.R;


public class EBEmptyView extends RelativeLayout implements OnClickListener {

    private TextView titleTV;
    private ImageView eb_empty_icon;
    private TextView reloadTV;
    private OnRetryListener mOnRetryListener;
    public static final int EMPTY_STATUS_LOADING = 1;
    public static final int EMPTY_STATUS_SHOW_NETWORK_ERROR = 2;
    public static final int EMPTY_STATUS_SHOW_NO_DATA = 3;
    public static final int EMPTY_STATUS_SHOW_NO_NETWORK = 4;

    public EBEmptyView(Context context,
                       AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.eb_empty_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleTV = (TextView) findViewById(R.id.titleTV);
        eb_empty_icon = (ImageView) findViewById(R.id.eb_empty_icon);
        reloadTV = (TextView) findViewById(R.id.reloadTV);
        reloadTV.setOnClickListener(this);
    }

    public void setStatus(int status,String title){
        switch (status){
            case EMPTY_STATUS_LOADING:
                titleTV.setVisibility(INVISIBLE);
                reloadTV.setVisibility(INVISIBLE);
                eb_empty_icon.setImageResource(R.drawable.discount_loading_logo);
                break;
            case EMPTY_STATUS_SHOW_NETWORK_ERROR:
                titleTV.setVisibility(VISIBLE);
                titleTV.setText(getResources().getString(R.string.empty_title_no_network));
                reloadTV.setVisibility(VISIBLE);
                eb_empty_icon.setImageResource(R.drawable.empty_network_error);
                break;
            case EMPTY_STATUS_SHOW_NO_DATA:
                titleTV.setVisibility(VISIBLE);
                titleTV.setText(title);
                reloadTV.setVisibility(INVISIBLE);
                eb_empty_icon.setImageResource(R.drawable.empty_no_data);
                break;
            case EMPTY_STATUS_SHOW_NO_NETWORK:
                titleTV.setVisibility(VISIBLE);
                titleTV.setText(getResources().getString(R.string.no_net_content));
                reloadTV.setVisibility(INVISIBLE);
                eb_empty_icon.setImageResource(R.drawable.empty_network_error);
                break;
        }
    }

    public void setStatus(int status, String title, int noDataIcon) {
        switch (status) {
            case EMPTY_STATUS_LOADING:
                titleTV.setVisibility(INVISIBLE);
                reloadTV.setVisibility(INVISIBLE);
                eb_empty_icon.setImageResource(R.drawable.discount_loading_logo);
                break;
            case EMPTY_STATUS_SHOW_NETWORK_ERROR:
                titleTV.setVisibility(VISIBLE);
                titleTV.setText(getResources().getString(R.string.no_net_content));
                reloadTV.setVisibility(VISIBLE);
                eb_empty_icon.setImageResource(R.drawable.empty_network_error);
                break;
            case EMPTY_STATUS_SHOW_NO_DATA:
                titleTV.setVisibility(VISIBLE);
                titleTV.setText(title);
                reloadTV.setVisibility(INVISIBLE);
                eb_empty_icon.setImageResource(noDataIcon);
                break;
            case EMPTY_STATUS_SHOW_NO_NETWORK:
                titleTV.setVisibility(VISIBLE);
                titleTV.setText(getResources().getString(R.string.no_net_content));
                reloadTV.setVisibility(INVISIBLE);
                eb_empty_icon.setImageResource(R.drawable.empty_network_error);
                break;
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.reloadTV:
                if (mOnRetryListener != null) {
                    mOnRetryListener.onRetryClick();
                }
                break;
            default:
                break;
        }
    }



    public void setOnRetryListener(OnRetryListener onRetryListener) {
        this.mOnRetryListener = onRetryListener;
    }

    public interface OnRetryListener {
        void onRetryClick();
    }

}
