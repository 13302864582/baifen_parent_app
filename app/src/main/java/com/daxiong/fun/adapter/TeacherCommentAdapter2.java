package com.daxiong.fun.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.daxiong.fun.R;
import com.daxiong.fun.base.ImageLoader;
import com.daxiong.fun.model.CommentModel;
import com.daxiong.fun.util.DateUtil;

public class TeacherCommentAdapter2 extends BaseAdapter {

	private Context context;
	private ArrayList<CommentModel> data;
	private int avatarSize;

	public TeacherCommentAdapter2(Context context, ArrayList<CommentModel> data) {
		this.context = context;
		this.data = data;
		avatarSize = context.getResources().getDimensionPixelSize(R.dimen.msg_list_avatar_size);
		notifyDataSetChanged();
	}

	public void setData(ArrayList<CommentModel> data) {
		this.data = data;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (null != data) {
			return data.size();
		}
		return 0;
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
			convertView = View.inflate(context, R.layout.item_comment_item2, null);
			holder.cHeadIv = (NetworkImageView) convertView.findViewById(R.id.comm_user_avatar);
			holder.cNameTv = (TextView) convertView.findViewById(R.id.comm_user_name);
			holder.cTimeTv = (TextView) convertView.findViewById(R.id.comm_time);
			holder.cContentTv = (TextView) convertView.findViewById(R.id.comm_content);
			holder.cStarRb = (RatingBar) convertView.findViewById(R.id.comm_start_rb);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CommentModel item = data.get(position);

		ImageLoader.getInstance().loadImageWithDefaultAvatar(item.getAvatar(), holder.cHeadIv, avatarSize,
				avatarSize / 10);
		
		String timeStr = DateUtil.getCommTime(Long.parseLong(item.getTime()));
		if(TextUtils.isEmpty(timeStr) || "0".equals(timeStr)){
			timeStr = "";
		}
		
		holder.cNameTv.setText(item.getStudname());
		holder.cTimeTv.setText(timeStr);
		holder.cContentTv.setText(item.getContent());
		holder.cStarRb.setProgress(item.getStar());
		holder.cStarRb.setEnabled(false);

		return convertView;
	}

	class ViewHolder {
		NetworkImageView cHeadIv;
		TextView cNameTv, cTimeTv, cContentTv;
		RatingBar cStarRb;
	}

}
