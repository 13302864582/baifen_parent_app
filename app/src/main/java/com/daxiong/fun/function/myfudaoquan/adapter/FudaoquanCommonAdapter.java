
package com.daxiong.fun.function.myfudaoquan.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.model.FudaoquanModel;

import java.util.List;

public class FudaoquanCommonAdapter extends BaseAdapter {

    private Context context;
    private List<FudaoquanModel> list;
    private boolean falg = false;

    public void setFalg(boolean falg) {
        this.falg = falg;
    }

    public FudaoquanCommonAdapter(Context context, List<FudaoquanModel> list) {
        super();
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
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.fudaoquan_common_listview_item, null);

            holder.iv = (ImageView) convertView.findViewById(R.id.iv);

            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_laizi = (TextView) convertView.findViewById(R.id.tv_laizi);
            holder.tv_zhangshu = (TextView) convertView.findViewById(R.id.tv_zhangshu);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final FudaoquanModel fudaoquanModel = list.get(position);
        if (fudaoquanModel.getType() == 1) {
            holder.tv_name.setText("难\n题\n券");
            if(falg){
                holder.iv.setImageResource(R.drawable.ticket_expire_bg);
            }else {

                holder.iv.setImageResource(R.drawable.ticket_problem_bg);
            }
        } else {
            if(falg){
                holder.iv.setImageResource(R.drawable.ticket_expire_bg);
            }else {
                holder.iv.setImageResource(R.drawable.ticket_task_bg);
            }
            holder.tv_name.setText("作\n业\n券");
        }
        holder.tv_time.setText(fudaoquanModel.getExpireDate());
         String orgname = fudaoquanModel.getOrgname();
        if(TextUtils.isEmpty(orgname)){
            orgname="大熊作业";
        }
        holder.tv_laizi.setText("来自“"+orgname+"“赠送");
        holder.tv_zhangshu.setText(fudaoquanModel.getCount()+"");
        return convertView;
    }

    final class ViewHolder {


        private ImageView iv;
        private TextView tv_name, tv_time, tv_laizi, tv_zhangshu;


    }


}
