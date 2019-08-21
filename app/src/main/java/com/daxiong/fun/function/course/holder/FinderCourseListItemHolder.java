package com.daxiong.fun.function.course.holder;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.base.ImageLoader;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.function.course.model.CourseListItemModel;
import com.daxiong.fun.manager.IntentManager;

public class FinderCourseListItemHolder extends BaseHolder<CourseListItemModel> implements OnClickListener{

	private NetworkImageView nv_head_icon;
	private TextView tv_name;
	private TextView tv_introduce;
	private TextView tv_class;
	private TextView tv_subject;
	private TextView tv_course;
	private TextView tv_state;
	
	private int avatarSize;
	private int teacherid;

	@Override
	public View initView() {
		avatarSize = MyApplication.getContext().getResources().getDimensionPixelSize(R.dimen.master_list_icon_size);
		
		View view = View.inflate(MyApplication.getContext(), R.layout.master_item_view, null);
		nv_head_icon = (NetworkImageView) view.findViewById(R.id.master_item_iv_head_icon);
		tv_name = (TextView) view.findViewById(R.id.master_item_tv_stuname);
		tv_introduce = (TextView) view.findViewById(R.id.master_item_tv_introduce);
		tv_class = (TextView) view.findViewById(R.id.master_item_tv_class);
		tv_subject = (TextView) view.findViewById(R.id.master_item_tv_subject);
		tv_course = (TextView) view.findViewById(R.id.master_item_tv_course);
		tv_state = (TextView) view.findViewById(R.id.master_item_tv_state);
		
		nv_head_icon.setOnClickListener(this);
		return view;
	}

	@Override
	public void refreshView() {
		CourseListItemModel data = getData();
		
		String url = data.getTeacheravatar();
		ImageLoader.getInstance().loadImage(url, nv_head_icon, R.drawable.ic_default_avatar, avatarSize,
				avatarSize / 10);
		
		teacherid = data.getTeacherid();
		
		tv_name.setText(data.getTeachername());
		tv_introduce.setText(data.getContent());
		tv_class.setText(data.getGrade());
		tv_subject.setText(data.getSubject());
		tv_course.setText(data.getCoursename());
		tv_state.setVisibility(data.isBuy() ? View.VISIBLE : View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.master_item_iv_head_icon:
			BaseActivity activity = BaseActivity.getForegroundActivity();
			if (activity != null && teacherid != 0){
				IntentManager.gotoPersonalPage(activity, teacherid, GlobalContant.ROLE_ID_COLLEAGE);
			}
			break;
		}
	}
}
