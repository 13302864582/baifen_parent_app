package com.daxiong.fun.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daxiong.fun.R;
import com.daxiong.fun.model.Teacher_info;
import com.daxiong.fun.view.CropCircleTransformation;

import java.util.List;

public class TeacherCommentAdapter extends BaseAdapter {

	private Context context;
	private List<Teacher_info.Comments> data;


	public TeacherCommentAdapter(Context context, List<Teacher_info.Comments> data) {
		this.context = context;
		this.data = data;

	}



	@Override
	public int getCount() {

		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		if (null != data) {
			data.get(arg0);
		}
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		final ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_comment_item, null);
			holder.cHeadIv = (ImageView) convertView.findViewById(R.id.comm_user_avatar);
			holder.cNameTv = (TextView) convertView.findViewById(R.id.comm_user_name);
			holder.cTimeTv = (TextView) convertView.findViewById(R.id.comm_time);
			holder.cContentTv = (TextView) convertView.findViewById(R.id.comm_content);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Teacher_info.Comments item = data.get(position);
		Glide.with(context).load(item.getAvatar())
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.bitmapTransform(new CropCircleTransformation(context))
				.placeholder(R.drawable.default_icon_circle_avatar).into(holder.cHeadIv);
		holder.cNameTv.setText("学号："+item.getStudid());
		holder.cTimeTv.setText(item.getTime());
		holder.cContentTv.setText(item.getContent());
		return convertView;
	}

	class ViewHolder {
		ImageView cHeadIv;
		TextView cNameTv, cTimeTv, cContentTv;

	}

}
