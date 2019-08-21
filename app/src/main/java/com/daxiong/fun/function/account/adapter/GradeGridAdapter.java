package com.daxiong.fun.function.account.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daxiong.fun.R;

import java.util.List;

public class GradeGridAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;


    public GradeGridAdapter(Context context, List<String> list) {
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
            convertView = View.inflate(context, R.layout.choose_grade_adapter_item, null);
            holder = new ViewHolder();
            holder.tv_grade = (TextView) convertView.findViewById(R.id.tv_grade);
            holder.iv_arrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
            holder.view_line = (View) convertView.findViewById(R.id.view_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_grade.setText((String) getItem(position));
        if (position == list.size() -1) {
            holder.view_line.setVisibility(View.GONE);
        }else{
            holder.view_line.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_grade;
        ImageView iv_arrow;
        View view_line;
    }

}
