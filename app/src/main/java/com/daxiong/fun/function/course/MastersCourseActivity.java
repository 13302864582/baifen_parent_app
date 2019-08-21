package com.daxiong.fun.function.course;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.function.course.fragment.FinderCourseFragment;
import com.daxiong.fun.function.course.fragment.MyCourseFragment;
import com.daxiong.fun.manager.IntentManager;

/**
 * 微课辅导  
 * sky
 */
public class MastersCourseActivity extends BaseActivity implements OnClickListener {

	public static final int CHOOSE_MY_COURSE = 0;
	public static final int CHOOSE_TEACHER_COURSE = 1;
	
	public static final String FRAGEMT_1 = "MyCourseFragment";
	public static final String FRAGEMT_2 = "FinderCourseFragment";

	/** 我的课程 */
	private TextView tv_mycourse;
	/** 发现课程 */
	private TextView tv_teachercourse;
	private ImageView search_img;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_masters_course);
		setWelearnTitle(R.string.masters_course);

		View title = findViewById(R.id.master_title);
		title.findViewById(R.id.back_layout).setOnClickListener(this);

		search_img = (ImageView) title.findViewById(R.id.next_step_img);
		search_img.setImageResource(R.drawable.search_white_selector);
		search_img.setClickable(true);
		search_img.setOnClickListener(this);

		tv_mycourse = (TextView) findViewById(R.id.master_tv_mycourse);
		tv_teachercourse = (TextView) findViewById(R.id.master_tv_teachercourse);

		tv_mycourse.setOnClickListener(this);
		tv_teachercourse.setOnClickListener(this);

		tv_mycourse.performClick();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent != null) {
			int choose = intent.getIntExtra("choose", -1);
			if (choose == CHOOSE_TEACHER_COURSE) {
				tv_teachercourse.performClick();
			} else {
				tv_mycourse.performClick();
			}
		}
	}

	@Override
	public void onClick(View v) {
		FragmentTransaction ft = null;
		switch (v.getId()) {
		case R.id.master_tv_mycourse: // 我的课程
			tv_mycourse.setTextColor(getResources().getColor(R.color.master_tab_gotfocus));
			tv_teachercourse.setTextColor(getResources().getColor(R.color.master_tab_losefocus));
			search_img.setVisibility(View.GONE);

			ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.master_fl_content, new MyCourseFragment(), FRAGEMT_1);
			ft.commit();
			break;
		case R.id.master_tv_teachercourse: // 发现课程
			tv_teachercourse.setTextColor(getResources().getColor(R.color.master_tab_gotfocus));
			tv_mycourse.setTextColor(getResources().getColor(R.color.master_tab_losefocus));
			search_img.setVisibility(View.VISIBLE);

			ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.master_fl_content, new FinderCourseFragment(), FRAGEMT_2);
			ft.commit();
			break;
		case R.id.back_layout:
			finish();
			break;
		case R.id.next_step_img: // 打开搜索
			IntentManager.goToSearchCourse(MastersCourseActivity.this);
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO (已完成) 更新 发现新课中的购买状态
		FinderCourseFragment fragmet = (FinderCourseFragment) getSupportFragmentManager().findFragmentByTag(FRAGEMT_2);
		if(fragmet != null){
			fragmet.onActivityResult(requestCode, resultCode, data);
		}
	}
}
