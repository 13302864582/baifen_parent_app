package com.daxiong.fun.function.question;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.function.AnswerListItemView;
import com.daxiong.fun.function.question.OneQuestionMoreAnswersDetailItemFragment.OnTipsShowListener;
import com.daxiong.fun.function.question.adapter.QADetailAdapter;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.MyViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QADetailActivity extends BaseActivity implements OnClickListener, OnPageChangeListener, OnTipsShowListener, HttpListener {
	private TextView nextStepTV;
	private RelativeLayout nextStepLayout;

	private LinearLayout dots_ll;
	// 点的集合
	private List<View> dotLists;

	private TextView title;

	// 换成自定义ViewPager
	private MyViewPager mViewPager;
	private QADetailAdapter mAdapter;
	private int currentPosition;
	private boolean mIsQpad;
	private int mCount;
	private boolean[] isShowPoint = { true, true, true, true };

	public int currentPositionState = ONFIRST;
	public final static int ONFIRST = 1;
	public final static int ONLAST = 2;

	@Override
	@SuppressLint("InlinedApi")
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.fragment_question_detail);
		// mViewPager1 = new RollViewPager(mActivity, null, null);
		dots_ll = (LinearLayout) findViewById(R.id.dots_ll);
		mViewPager = (MyViewPager) findViewById(R.id.detail_pager);
		
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setOnPageChangeListener(this);

		mAdapter = new QADetailAdapter(getSupportFragmentManager());

		currentPosition = getIntent().getIntExtra(AnswerListItemView.EXTRA_POSITION, 0);
		mIsQpad = getIntent().getBooleanExtra(AnswerListItemView.EXTRA_ISQPAD, false);

		title = (TextView)findViewById(R.id.title);

		nextStepLayout = (RelativeLayout) findViewById(R.id.next_setp_layout);
		nextStepTV = (TextView)findViewById(R.id.next_step_btn);
		nextStepTV.setVisibility(View.VISIBLE);
		nextStepLayout.setOnClickListener(this);

		showTitle(currentPosition);

		long questionId = getIntent().getLongExtra(AnswerListItemView.EXTRA_QUESTION_ID, 0);
		
		JSONObject data = new JSONObject();
		try {
			data.put("qid", questionId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpHelper.post(this, "parents", "questiongetone",data, this);

		findViewById(R.id.back_layout).setOnClickListener(this);
		
	}
	

	@Override
	public void onResume() {
		super.onResume();
//		GlobalVariable.mViewPager = mViewPager;
//		GlobalVariable.mOneQuestionMoreAnswersDetailFragment = this;
	}
	private void showTitle(int position) {
		if (0 == position) {
			title.setText(R.string.text_student_question);
			nextStepTV.setVisibility(View.GONE);
		} else {
			nextStepTV.setVisibility(View.VISIBLE);
			title.setText(R.string.text_teacher_answer);
			if (isShowPoint[position]) {
				nextStepTV.setText(R.string.text_hide_tips);
			} else {
				nextStepTV.setText(R.string.text_show_tips);
			}
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_layout:
			finish();
			break;

		case R.id.next_setp_layout:
			if (mAdapter == null) {
				return;
			}
			OneQuestionMoreAnswersDetailItemFragment mFragment = mAdapter.getFragment(currentPosition);
			
			if (null == mFragment) {
				return;
			}
			isShowPoint[currentPosition] = !isShowPoint[currentPosition];
			
			boolean isShow = isShowPoint[currentPosition];
			
			mFragment.showTips(isShow);

			if (null != nextStepTV) {
				nextStepTV.setText(isShow ? R.string.text_hide_tips : R.string.text_show_tips);
			}
			break;
		}
	}

	
	
	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (!TextUtils.isEmpty(dataJson)) {
			mAdapter.setData(dataJson, mIsQpad);

			JSONArray answerArray = JsonUtil.getJSONArray(dataJson, "answer", new JSONArray());
			mCount = answerArray.length() + 1;
			if (mCount != 0) {
				initDot(mCount, currentPosition);
			}
			if (mViewPager.getAdapter() == null) {
				mViewPager.setAdapter(mAdapter);
			}

			for (int i = 0; i < mAdapter.getCount(); i++) {
				if (null != mAdapter.getFragment(i)) {
					mAdapter.getFragment(i).setOnTipsShowListener(this);
				}
			}
		} else {
			ToastUtils.show(R.string.text_toast_svr_error);
		}
	
		
	}


	@Override
	public void onFail(int HttpCode,String errMsg) {
		
		
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
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int position) {
		if (position == 0) {
			currentPositionState = ONFIRST;
		} else if (position == mCount - 1) {
			currentPositionState = ONLAST;
		}
		currentPosition = position;
		showTitle(position);
		if (mCount != 0) {
			initDot(mCount, currentPosition);
		}
	}
	
	@Override
	public void onTipsShow(boolean isShow) {
		isShowPoint[currentPosition] = isShow;
		if (null != nextStepTV) {
			nextStepTV.setText(isShow ? R.string.text_hide_tips : R.string.text_show_tips);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GlobalContant.APPEND_ASK_REQUEST_CODE 
				|| requestCode == GlobalContant.APPEND_ANSWER_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				finish();
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		if (GlobalVariable.mRefuseAnswerPopWindow != null) {
			GlobalVariable.mRefuseAnswerPopWindow.dismiss();
		}else {
			super.onBackPressed();
		}
	}






}
