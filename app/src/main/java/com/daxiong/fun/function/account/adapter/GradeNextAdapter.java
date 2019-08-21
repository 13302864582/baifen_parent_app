package com.daxiong.fun.function.account.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.function.account.model.BigGradeModel;

import java.util.List;

/**
 * 年级选择next
 */

public class GradeNextAdapter extends BaseAdapter {

    private Context context;

    private List<BigGradeModel.SubGradeModel> gradeModelList;


    public GradeNextAdapter(Context context, List<BigGradeModel.SubGradeModel> gradeModelList) {
        this.gradeModelList = gradeModelList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return gradeModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return gradeModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.grade_next_layout, null);
            TextView tv_gradename = (TextView) convertView.findViewById(R.id.tv_grade);
            ImageView iv_choice = (ImageView) convertView.findViewById(R.id.iv_arrow);
            View view_line = (View) convertView.findViewById(R.id.view_line);
            holder = new ViewHolder(tv_gradename, iv_choice, view_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BigGradeModel.SubGradeModel item = (BigGradeModel.SubGradeModel) getItem(position);
        holder.tv_gradename.setText(item.getGrade_name());
        if (item.getChecked() == 1) {
            holder.imageview.setVisibility(View.VISIBLE);
        } else {
            holder.imageview.setVisibility(View.GONE);
        }
        if (position == gradeModelList.size() - 1) {
            holder.view_line.setVisibility(View.GONE);
        } else {
            holder.view_line.setVisibility(View.VISIBLE);
        }


        return convertView;
    }

    final class ViewHolder {
        TextView tv_gradename;
        ImageView imageview;
        View view_line;

        public ViewHolder(TextView tv_gradename, ImageView imageview, View view_line) {
            this.tv_gradename = tv_gradename;
            this.imageview = imageview;
            this.view_line = view_line;
        }
    }
}
