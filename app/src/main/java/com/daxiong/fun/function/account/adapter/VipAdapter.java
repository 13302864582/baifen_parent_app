package com.daxiong.fun.function.account.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.function.account.model.VipModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Sky on 2016/7/5 0005.
 */
public class VipAdapter extends BaseAdapter {


    private Context context;

    private List<VipModel.BuyVipInfosBean> list;


    public VipAdapter(Context context, List<VipModel.BuyVipInfosBean> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder  holder=null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.vip_index_item, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder= (ViewHolder) convertView.getTag();

        }
        VipModel.BuyVipInfosBean item= (VipModel.BuyVipInfosBean) getItem(position);

        holder.rl_vip_item_bg.setBackgroundResource(R.drawable.bg_vip_normal);
        holder.ivTag.setVisibility(View.GONE);

        //是否推荐
        if (item.getIs_recommend()==1){
            holder.rl_vip_item_bg.setBackgroundResource(R.drawable.bg_vip_jian);
            holder.ivTag.setVisibility(View.VISIBLE);
            holder.ivTag.setBackgroundResource(R.drawable.icon_jian);
        }

        //是否优惠
        if (item.getIs_preferential()==1){
            holder.rl_vip_item_bg.setBackgroundResource(R.drawable.bg_vip_hui);
            holder.ivTag.setVisibility(View.VISIBLE);
            holder.ivTag.setBackgroundResource(R.drawable.icon_hui);
        }

        holder.tvTitle.setText(item.getContent());
        holder.tvPrice.setText("¥"+item.getMoney()+"");

        holder.btnDoorder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listener.doOrder(position);
            }
        });
        holder.tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.doDetail(position);
            }
        });
        holder.rl_vip_item_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.doDetail(position);
            }
        });

        return convertView;
    }




    class ViewHolder {
         @Bind(R.id.rl_vip_item_bg)
         LinearLayout rl_vip_item_bg;
        @Bind(R.id.iv_tag)
        ImageView ivTag;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_detail)
         TextView tvDetail;
        @Bind(R.id.tv_price)
        TextView tvPrice;
        @Bind(R.id.btn_doorder)
        Button btnDoorder;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    IClickListerner  listener;

    public void setOnButtonClickListener(IClickListerner  listener){
        this.listener=listener;
    }

    public  static interface  IClickListerner{
        //下订单
        void doOrder(int position);
        //详情
        void  doDetail(int position);
    }
}
