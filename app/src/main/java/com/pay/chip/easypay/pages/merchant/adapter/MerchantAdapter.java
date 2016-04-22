package com.pay.chip.easypay.pages.merchant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by DavidLee on 2016/3/9.
 */
public class MerchantAdapter extends BaseAdapter {

    private Context context;
    private final String balance;
    private final String cardId;
//    private ArrayList<RecordData.RecordListData> dataList = new ArrayList<>();
//    private RecordData.RecordListData firstList = new RecordData.RecordListData();
    private String year;
    private String month;
    private String day;
    private String events = "";
    boolean isFirst = true;
    boolean isLast = true;

    public MerchantAdapter(Context context, String balance, String cardId) {
        this.context = context;
        this.balance = balance;
        this.cardId = cardId;

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }




  /*  public void setData(ArrayList<RecordData.RecordListData> data) {
        if (data == null) {
            return;
        }
        dataList = new ArrayList<>();
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    public void addToTail(ArrayList<RecordData.RecordListData> data) {
        if (data == null) {
            return;
        }
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public RecordData.RecordListData getItem(int position) {
        return dataList.get(position);
    }
*/
  /*  @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.merchant_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.viewTop = (ImageView) convertView.findViewById(R.id.view_top);
            viewHolder.record_layout = (RelativeLayout) convertView.findViewById(R.id.record_layout);
            viewHolder.showTime = (TextView) convertView.findViewById(R.id.show_time);
            viewHolder.showTimeYear = (TextView) convertView.findViewById(R.id.show_time_year);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.viewBottom = (ImageView) convertView.findViewById(R.id.view_bottom);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.title_cost = (TextView) convertView.findViewById(R.id.title_cost);
            viewHolder.title_content = (TextView) convertView.findViewById(R.id.title_content);
            viewHolder.share_btn = (Button) convertView.findViewById(R.id.share_friend_btn);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        final RecordData.RecordListData data = dataList.get(position);


        //去掉头尾的线，更换中间的图标,第一条数据手动添加，其他数据从接口调
        int pad = DeviceUtils.dip2px(context, 10);
        if (position == 0 && isFirst) {
            isFirst = false;
            viewHolder.title_cost.setText(balance + "元");
            viewHolder.viewTop.setVisibility(View.INVISIBLE);
            viewHolder.image.setImageResource(R.drawable.balance_list_icon);
            viewHolder.title.setText(R.string.balance_t);
            viewHolder.record_layout.setPadding(pad, 0, 0, 0);
            viewHolder.title_content.setVisibility(View.GONE);
            viewHolder.viewBottom.setVisibility(View.VISIBLE);
            viewHolder.share_btn.setVisibility(View.VISIBLE);

            viewHolder.showTimeYear.setText(year);
            viewHolder.showTime.setText(month + "月" + day + "日");


            isFirst = true;
        } else if ((position == dataList.size() - 1) && isLast) {
            isLast = false;
            viewHolder.record_layout.setPadding(pad, pad, 0, 0);
            viewHolder.title.setText(R.string.pay);
            viewHolder.viewBottom.setVisibility(View.INVISIBLE);
            viewHolder.viewTop.setVisibility(View.VISIBLE);
            viewHolder.image.setImageResource(R.drawable.cost_pay);
            viewHolder.title_content.setVisibility(View.VISIBLE);
            viewHolder.share_btn.setVisibility(View.GONE);

            fillAdapterContent(viewHolder, data);

            isLast = true;
        } else {
            viewHolder.record_layout.setPadding(pad, pad, 0, 0);
            viewHolder.viewBottom.setVisibility(View.VISIBLE);
            viewHolder.viewTop.setVisibility(View.VISIBLE);
            viewHolder.image.setImageResource(R.drawable.cost_pay);
            viewHolder.title.setText(R.string.pay);
            viewHolder.title_content.setVisibility(View.VISIBLE);
            viewHolder.share_btn.setVisibility(View.GONE);

            fillAdapterContent(viewHolder, data);
        }

       *//* if(position==0){
            isFirst = false;

        }*//*

        if (dataList.size() == 1) {
            viewHolder.record_layout.setPadding(pad, 0, 0, 0);
            viewHolder.viewBottom.setVisibility(View.GONE);
            viewHolder.title_content.setVisibility(View.GONE);
        }

        viewHolder.share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareCardActivity.startShareCardActivity(context, cardId, balance);
            }
        });


        return convertView;
    }

    private void fillAdapterContent(ViewHolder viewHolder, RecordData.RecordListData data) {
        if(data!=null&&data.operateTime!=null){
            year = data.operateTime.substring(0, 4);
            viewHolder.showTimeYear.setText(year);
            month = data.operateTime.substring(5, 7);
            day = data.operateTime.substring(8, 10);
            month = Integer.parseInt(month) + "";
            viewHolder.showTime.setText(month + "月" + day + "日");
        }


        String employee = "";
        for (RecordData.RecordListData.ItemData eventslist : data.itemlist) {
            if(eventslist.employee==null||eventslist.employee.isEmpty()){
                employee = "\n";
            }else{
                employee = "，由" + eventslist.employee + "完成\n";
            }

            events += eventslist.item + eventslist.count + "次"+employee;
        }
        viewHolder.title_content.setText(events);
        events = "";
        viewHolder.title_cost.setText(data.totalpay + "元");
    }

    private static class ViewHolder {


        public ImageView viewTop;
        public TextView showTime;
        public TextView showTimeYear;
        public ImageView image;
        public ImageView viewBottom;
        public TextView title;
        public TextView title_content;
        public TextView title_cost;
        public RelativeLayout record_layout;
        public Button share_btn;
    }
*/
}
