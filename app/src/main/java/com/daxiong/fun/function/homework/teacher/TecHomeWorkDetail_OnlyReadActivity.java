package com.daxiong.fun.function.homework.teacher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.function.AnswerListItemView;
import com.daxiong.fun.function.homework.adapter.TecHomeWorkDetail_OnlyReadAdapter;
import com.daxiong.fun.function.homework.model.StuPublishHomeWorkPageModel;
import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.view.MyViewPager;

import java.util.ArrayList;

public class TecHomeWorkDetail_OnlyReadActivity extends BaseActivity {
	private int currentPosition;
	private ArrayList<View> dotLists;
	private LinearLayout dots_ll;
	public static final String HOMEWROKDETAILPAGERLIST = "pagerlist";
	private ArrayList<StuPublishHomeWorkPageModel> mHomeWorkPageModelList;

	@Override
	@SuppressLint("InlinedApi")
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		View view = View.inflate(this, R.layout.fragment_question_detail, null);
		setContentView(view);
		setWelearnTitle(R.string.homework_detail_title_text);

		findViewById(R.id.back_layout).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		dots_ll = (LinearLayout) view.findViewById(R.id.dots_ll);
		MyViewPager mViewPager = (MyViewPager) view.findViewById(R.id.detail_pager);
		findViewById(R.id.homework_asker_info_container).setVisibility(View.VISIBLE);
		// mViewPager.setOffscreenPageLimit(8);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int postion) {
				currentPosition = postion;
				selectDot(postion);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		Intent intent = getIntent();
		if (intent != null) {
			currentPosition = intent.getIntExtra(AnswerListItemView.EXTRA_POSITION, -1);
			mHomeWorkPageModelList = (ArrayList<StuPublishHomeWorkPageModel>) intent
					.getSerializableExtra(HOMEWROKDETAILPAGERLIST);
			initDot(mHomeWorkPageModelList.size(), currentPosition);
		}
		mViewPager.setAdapter(new TecHomeWorkDetail_OnlyReadAdapter(mHomeWorkPageModelList, this));
		mViewPager.setCurrentItem(currentPosition, false);// 加上fasle表示切换时不出现平滑效果
	}

	// 初始化点
	private void initDot(int size, int defalutPosition) {
		dotLists = new ArrayList<View>();
		dots_ll.removeAllViews();
		for (int i = 0; i < size; i++) {
			// 设置点的宽高
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(this, 6),
					DensityUtil.dip2px(this, 6));
			// 设置点的间距
			params.setMargins(7, 0, 7, 0);
			// 初始化点的对象
			View m = new View(this);
			// 把点的宽高设置到view里面
			m.setLayoutParams(params);
			if (i == defalutPosition) {
				// 默认情况下，首先会调用第一个点。就必须展示选中的点
				m.setBackgroundResource(R.drawable.dot_focus);
			} else {
				// 其他的点都是默认的。
				m.setBackgroundResource(R.drawable.dot_normal);
			}
			// 把所有的点装载进集合
			dotLists.add(m);
			// 现在的点进入到了布局里面
			dots_ll.addView(m);
		}
	}

	private void selectDot(int postion) {
		for (View dot : dotLists) {
			dot.setBackgroundResource(R.drawable.dot_normal);
		}
		dotLists.get(postion).setBackgroundResource(R.drawable.dot_focus);
	}
}
