package com.daxiong.fun.function.account.vip;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.function.account.model.BuyDetailModel;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.view.XListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * vip购买详情
 */

public class BuyDetailActivity extends BaseActivity implements XListView.IXListViewListener {


    @Bind(R.id.back_layout)
    RelativeLayout backLayout;
    @Bind(R.id.next_setp_layout)
    RelativeLayout nextSetpLayout;
    @Bind(R.id.listview)
    XListView listview;
    @Bind(R.id.empty_view)
    LinearLayout emptyView;
    @Bind(R.id.view_line)
    View view_line;



    private MyAdapter mAdapter;
    private List<BuyDetailModel> list;
    private int pageIndex = 0;
    private int pageSize = 5;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.buy_detail_activity);
        ButterKnife.bind(this);
        initView();
        initListener();
        initDate();
    }


    @Override
    public void initView() {
        super.initView();
        list = new ArrayList<BuyDetailModel>();
        mAdapter = new MyAdapter(this, list);
        listview.setAdapter(mAdapter);
    }


    private void initDate() {
        showDialog("请稍等...");
        try {
            JSONObject json = new JSONObject();
            json.put("page", pageIndex);
            json.put("count", pageSize);
            OkHttpHelper.post(this, "parents", "buyviplist", json, new OkHttpHelper.HttpListener() {
                @Override
                public void onSuccess(int code, String dataJson, String errMsg) {
                    List<BuyDetailModel> buyDetailModels = JSON.parseArray(dataJson, BuyDetailModel.class);


                    if (buyDetailModels.size() < 5) {
                        listview.setPullLoadEnable(false);
                    } else {
                        listview.setPullLoadEnable(true);
                    }
                    list.addAll(buyDetailModels);
                    if (list != null && list.size() > 0) {
                        emptyView.setVisibility(View.GONE);
                        view_line.setVisibility(View.VISIBLE);
                        mAdapter.notifyDataSetChanged();

                    } else {
                        emptyView.setVisibility(View.VISIBLE);
                        view_line.setVisibility(View.GONE);
                        listview.setEmptyView(emptyView);
                    }

                }

                @Override
                public void onFail(int HttpCode, String errMsg) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void initListener() {
        super.initListener();
        backLayout.setOnClickListener(this);
        listview.setXListViewListener(this);
        listview.setPullRefreshEnable(true);
        listview.setPullLoadEnable(true);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_layout:
                finish();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    static class MyAdapter extends BaseAdapter {

        private Context context;
        private List<BuyDetailModel> list;


        public MyAdapter(Context context, List<BuyDetailModel> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.buy_detail_item_layout, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();

            }
            BuyDetailModel item = (BuyDetailModel) getItem(position);
            holder.tvTitle.setText(item.getContent());
            holder.tvDate.setText(item.getStart_time() + "至" + item.getEnd_time());
            holder.tvPrice.setText("￥" + item.getPrice() + "");
            if (position==list.size()-1){
                holder.tv_xian.setVisibility(View.GONE);
            }else{
                holder.tv_xian.setVisibility(View.VISIBLE);
            }
            return convertView;
        }


        static class ViewHolder {
            @Bind(R.id.tv_title)
            TextView tvTitle;
            @Bind(R.id.tv_date)
            TextView tvDate;
            @Bind(R.id.tv_price)
            TextView tvPrice;
            @Bind(R.id.tv_xian)
            View tv_xian;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

