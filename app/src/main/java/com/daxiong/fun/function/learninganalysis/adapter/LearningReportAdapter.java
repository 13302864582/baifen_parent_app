
package com.daxiong.fun.function.learninganalysis.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.function.learninganalysis.model.XueqingBigModel;
import com.daxiong.fun.util.DateUtil;

import java.util.List;

/**
 * 学情报告adapter
 *
 * @author: sky
 */
public class LearningReportAdapter extends BaseAdapter {

    private Context context;

    private List<XueqingBigModel.ReportDetailBen> list;

    public LearningReportAdapter(Context context, List<XueqingBigModel.ReportDetailBen> list) {
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
            convertView = View.inflate(context, R.layout.learning_report_item2, null);
            holder.iv_isread = (ImageView) convertView.findViewById(R.id.iv_isread);
            holder.tv_context = (TextView) convertView.findViewById(R.id.tv_context);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
      final XueqingBigModel.ReportDetailBen item = list.get(position);
        if (item.getIsread() == 0) {
            holder.iv_isread.setImageResource(R.drawable.learning_situation_report_unread_btn_normal);
        } else {
            holder.iv_isread.setImageResource(R.drawable.learning_situation_report_read_btn_normal);
        }
        holder.tv_context.setText(item.getContent());
        holder.tv_time.setText(DateUtil.getDisplayTime2(item.getTime()));

        return convertView;
    }

    final class ViewHolder {

        private TextView tv_time;
        private TextView tv_context;
        private ImageView iv_isread;

    }

}
