package com.daxiong.fun.function.myfudaoquan.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daxiong.fun.R;
import com.daxiong.fun.api.HomeListAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.base.BaseFragment;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.model.FudaoquanModel;

import java.util.List;

/**
 * 索券
 *
 * @author: sky
 */
public class ExpireFudaoquanAdapter extends BaseAdapter {

    private BaseActivity context;
    private BaseFragment context2;
    private HomeListAPI homeListAPI;
    private List<FudaoquanModel> list;

    public ExpireFudaoquanAdapter(BaseActivity context,BaseFragment context2,HomeListAPI homeListAPI, List<FudaoquanModel> list) {
        super();
        this.context = context;
        this.context2 = context2;
        this.homeListAPI = homeListAPI;
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
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.expire_fudaoquan_layout, null);

            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.v1 = convertView.findViewById(R.id.v1);
            holder.tv_zhangshu = (TextView) convertView.findViewById(R.id.tv_zhangshu);
            holder.tv_dizhi = (TextView) convertView.findViewById(R.id.tv_dizhi);
            holder.iv_show = (ImageView) convertView.findViewById(R.id.iv_show);
            holder.bt_suoquan = (TextView) convertView.findViewById(R.id.bt_suoquan);
            holder.line_top=convertView.findViewById(R.id.line_top);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final FudaoquanModel fudaoquanModel = list.get(position);
        holder.tv_name.setText(fudaoquanModel.getOrgname());
        holder.tv_dizhi.setText(fudaoquanModel.getAddress());
        holder.tv_zhangshu.setText(fudaoquanModel.getCouponleft()+"张 每次索取自动赠送"+fudaoquanModel.getSendcoupon()+"张");
        Glide.with(context).load(fudaoquanModel.getAvatar()).asBitmap().centerCrop()
              .placeholder(R.drawable.load_img)
                .into(holder.iv_show );
        holder.bt_suoquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeListAPI.orgsendcoupon(context2.requestQueue, context2,fudaoquanModel.getOrgid(), RequestConstant.GETFUDAOQUAN_HW_CODE);
            }
        });

        if (position==0){
            holder.line_top.setVisibility(View.VISIBLE);
        }else{
            holder.line_top.setVisibility(View.GONE);
        }
        if(list.size()==position+1){
            holder.v1.setVisibility(View.VISIBLE);
        }else{
            holder.v1.setVisibility(View.GONE);
        }
        return convertView;
    }

    final class ViewHolder {

        private ImageView iv_show;
        private View v1;

        private View line_top;


        private TextView tv_name;

        private TextView tv_zhangshu;

        private TextView tv_dizhi;

        private TextView bt_suoquan;


    }

}
