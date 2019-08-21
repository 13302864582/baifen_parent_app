package com.daxiong.fun.function.homework.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.model.SubjectModel;
import com.daxiong.fun.util.DensityUtil;

import java.util.List;

public class PublishHwSubjectAdapter extends BaseAdapter {

    private Context context;
    private List<SubjectModel> list;

    public PublishHwSubjectAdapter(Context context, List<SubjectModel> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.view_radio_button2,
                    null);
            holder.tv_subject = (TextView) convertView.findViewById(R.id.tv_subject);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SubjectModel item = (SubjectModel) getItem(position);
        holder.tv_subject.setText(item.getSubject_name());
        holder.tv_subject.setId(item.getSubjectid());
        holder.tv_subject.setTag(item);
		holder.tv_subject.setBackgroundResource(R.drawable.publish_choosesubject_btn_normal);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.tv_subject.getLayoutParams();
        int screenWidth=DensityUtil.getScreenWidth();
        lp.width=screenWidth/3-DensityUtil.dip2px(context,15*2);
//        lp.height=75;
//        holder.tv_subject.setPadding(30, 30, 30, 30);
        lp.setMargins(8,8,8,8);
        holder.tv_subject.setGravity(Gravity.CENTER);
        holder.tv_subject.setLayoutParams(lp);

        if (item.getChecked() == 1) {
            holder.tv_subject.setTextColor(context.getResources().getColor(android.R.color.white));
            //holder.rb.setBackgroundColor(context.getResources().getColor(R.color.grade_text_selected));
            holder.tv_subject.setBackgroundResource(R.drawable.publish_choosesubject_btn_pressed);
        } else {
            holder.tv_subject.setTextColor(context.getResources().getColor(R.color.grade_normal));
            //holder.rb.setBackgroundColor(context.getResources().getColor(android.R.color.white));
            holder.tv_subject.setBackgroundResource(R.drawable.publish_choosesubject_btn_normal);
        }

        return convertView;

    }

    final class ViewHolder {
        private TextView tv_subject;
    }

}
