
package com.daxiong.fun.function.agent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.model.UserInfoModel;

import java.util.List;

/**
 * 此类的描述：发题Adapter
 * 
 * @author: Sky
 * @最后修改人： Sky
 * @最后修改日期:2015-7-27 下午4:58:12
 * @version: 2.0
 */
public class PublisherAdapter extends BaseAdapter {

    private Context context;

    private List<UserInfoModel> publisherList;

    private LayoutInflater inflater;

    public PublisherAdapter(Context context, List<UserInfoModel> userList) {
        super();
        this.context = context;
        this.publisherList = userList;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return publisherList.size();
    }

    @Override
    public Object getItem(int position) {
        return publisherList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null) {
            holder=new ViewHolder();   
            convertView=inflater.inflate(R.layout.publisher_item, null);
            holder.tv_xuehao=(TextView)convertView.findViewById(R.id.tv_xuehao);
            holder.tv_name=(TextView)convertView.findViewById(R.id.tv_name);
            holder.tv_grade=(TextView)convertView.findViewById(R.id.tv_grade);
            convertView.setTag(holder);
               
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        UserInfoModel userinfo=(UserInfoModel)getItem(position);
        holder.tv_xuehao.setText(userinfo.getUserid()+"");
        holder.tv_name.setText(userinfo.getName());
        holder.tv_grade.setText(userinfo.getGrade());
        return convertView;
    }
    
    class ViewHolder{
        TextView tv_xuehao;
        TextView tv_name;
        TextView  tv_grade;
    }

}
