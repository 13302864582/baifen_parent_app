package com.daxiong.fun.function.homework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.function.homework.adapter.HomeWorkReadOnlyPageAdapter;
import com.daxiong.fun.function.homework.model.StuPublishHomeWorkPageModel;
import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.MyViewPager;

import java.util.ArrayList;
import java.util.List;

public class HomeWorkReadOnlyActivity extends BaseActivity {

	private ArrayList<StuPublishHomeWorkPageModel> mHomeWorkPageModelList;
	private MyViewPager mMyViewPager;
	private List<HomeWorkReadOnlyDetailItemFragment> itemList = new ArrayList<HomeWorkReadOnlyDetailItemFragment>();
	private HomeWorkReadOnlyPageAdapter mHomeWorkReadOnlyPageAdapter;
	private ArrayList<View> dotLists;
	private LinearLayout dots_ll;
	private int currentSelectedIndex;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_homework_readonly);

		initView();

		findViewById(R.id.back_layout).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		Intent i = getIntent();
		if (null != i) {
			mHomeWorkPageModelList = (ArrayList<StuPublishHomeWorkPageModel>) i.getSerializableExtra("pagerlist");
			if (null != mHomeWorkPageModelList) {
				for (StuPublishHomeWorkPageModel m : mHomeWorkPageModelList) {
					HomeWorkReadOnlyDetailItemFragment f = new HomeWorkReadOnlyDetailItemFragment(m.getImgpath());
					itemList.add(f);
				}
			}
			mHomeWorkReadOnlyPageAdapter = new HomeWorkReadOnlyPageAdapter(getSupportFragmentManager(), itemList);
			mMyViewPager.setAdapter(mHomeWorkReadOnlyPageAdapter);

			currentSelectedIndex = i.getIntExtra("position", 0);
			initDot(mHomeWorkPageModelList.size(), currentSelectedIndex);

			selectDot(currentSelectedIndex);
			mMyViewPager.setCurrentItem(currentSelectedIndex, true);
		} else {
			ToastUtils.show("Error");
			finish();
		}
	}

	public void initView() {
		mMyViewPager = (MyViewPager) findViewById(R.id.homework_viewpager);
		mMyViewPager.setOffscreenPageLimit(8);
		mMyViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int postion) {
				currentSelectedIndex = postion;
				selectDot(postion);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		dots_ll = (LinearLayout) findViewById(R.id.dots_ll);
	}

	@Override
	public void onResume() {
		super.onResume();
		GlobalVariable.mViewPager = mMyViewPager;
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
