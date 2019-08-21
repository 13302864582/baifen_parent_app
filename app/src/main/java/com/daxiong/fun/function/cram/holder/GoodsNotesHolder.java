package com.daxiong.fun.function.cram.holder;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.base.ImageLoader;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.function.course.holder.BaseHolder;
import com.daxiong.fun.manager.IntentManager;

public class GoodsNotesHolder extends BaseHolder<GoodsNotesModel> implements OnClickListener {

	private NetworkImageView itme_nv_icon;
	private TextView itme_tv_name;
	private TextView itme_tv_groupname;
	private TextView itme_tv_grade;
	private TextView itme_tv_subject;
	private TextView itme_tv_intro;
	private int avatarSize;

	@Override
	public View initView() {
		avatarSize = MyApplication.getContext().getResources().getDimensionPixelSize(R.dimen.itme_nv_icon);
		View view = View.inflate(MyApplication.getContext(), R.layout.item_goods_notes, null);
		itme_nv_icon = (NetworkImageView) view.findViewById(R.id.itme_nv_icon);
		itme_tv_name = (TextView) view.findViewById(R.id.itme_tv_name);
		itme_tv_groupname = (TextView) view.findViewById(R.id.itme_tv_groupname);
		itme_tv_grade = (TextView) view.findViewById(R.id.itme_tv_grade);
		itme_tv_subject = (TextView) view.findViewById(R.id.itme_tv_subject);
		itme_tv_intro = (TextView) view.findViewById(R.id.itme_tv_intro);
		
		itme_nv_icon.setOnClickListener(this);
		return view;
	}

	@Override
	public void refreshView() {
		GoodsNotesModel data = getData();
		ImageLoader.getInstance().loadImage(data.getTeacheravatar(), itme_nv_icon, R.drawable.ic_default_avatar, avatarSize,
				avatarSize / 10);
		itme_tv_name.setText(data.getTeachername());
		itme_tv_groupname.setText(data.getOrgname());
		itme_tv_grade.setText(data.getGrade());
		itme_tv_subject.setText(data.getSubject());
		itme_tv_intro.setText(data.getCoursename());
	}

	@Override
	public void onClick(View v) {
		// TODO (已完成)讲义 头像点击事件
		switch (v.getId()) {
		case R.id.itme_nv_icon:
			BaseActivity activity = BaseActivity.getForegroundActivity();
			if (activity != null){
				IntentManager.gotoPersonalPage(activity, getData().getTeacherid(), GlobalContant.ROLE_ID_COLLEAGE);
			}
			break;
		}
	}
}
