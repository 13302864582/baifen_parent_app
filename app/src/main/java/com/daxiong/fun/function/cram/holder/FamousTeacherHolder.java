package com.daxiong.fun.function.cram.holder;

import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.base.ImageLoader;
import com.daxiong.fun.function.course.holder.BaseHolder;

public class FamousTeacherHolder extends BaseHolder<FamousTeacherModel>{

	private NetworkImageView nv_icon;
	private TextView tv_name;
	private TextView tv_groupname;
	private TextView tv_school;
	private int avatarSize;

	@Override
	public View initView() {
		avatarSize = MyApplication.getContext().getResources().getDimensionPixelSize(R.dimen.itme_nv_icon);
		
		View view = View.inflate(MyApplication.getContext(), R.layout.item_teacher_famous, null);
		nv_icon = (NetworkImageView) view.findViewById(R.id.itme_nv_icon);
		tv_name = (TextView) view.findViewById(R.id.itme_tv_name);
		tv_groupname = (TextView) view.findViewById(R.id.itme_tv_groupname);
		tv_school = (TextView) view.findViewById(R.id.itme_tv_school);
		
		return view;
	}

	@Override
	public void refreshView() {
		FamousTeacherModel data = getData();
		String url = data.getAvatar();
		ImageLoader.getInstance().loadImage(url, nv_icon, R.drawable.ic_default_avatar, avatarSize,
				avatarSize / 10);
		tv_name.setText(data.getName());
		tv_groupname.setText(data.getOrgname());
		tv_school.setText(data.getMajor());
	}
}
