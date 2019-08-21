package com.daxiong.fun.function.question;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.function.question.adapter.QAHallListAdapter;
import com.daxiong.fun.function.question.model.AnswerListItemModel;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.XListView;
import com.daxiong.fun.view.XListView.IXListViewListener;
import com.daxiong.fun.view.XListViewFooter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QAHallActivity extends BaseActivity implements OnScrollListener, IXListViewListener,
		OnClickListener, HttpListener {

	private static final int PAGE_COUNT = 10;
	private ArrayList<AnswerListItemModel> currentList;
	private TextView nextStepTV;
	private RelativeLayout nextStepLayout;
	private XListView mAnswerList;
	private QAHallListAdapter mAdapter;
	private LinearLayout mTopBarContainer;
	private int pageIndex = 1;
	private boolean isRefresh = true;
	private boolean hasMore = true;
	@SuppressLint("HandlerLeak")
	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			onLoadFinish();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_answer_list);
		setWelearnTitle(R.string.text_qa_hall);

		findViewById(R.id.back_layout).setOnClickListener(this);

		nextStepLayout = (RelativeLayout) findViewById(R.id.next_setp_layout);
		nextStepTV = (TextView) findViewById(R.id.next_step_btn);
		nextStepTV.setVisibility(View.VISIBLE);
		nextStepTV.setText(R.string.text_ask_title);
		nextStepLayout.setOnClickListener(this);

		mAdapter = new QAHallListAdapter(this);

		mAnswerList = (XListView) findViewById(R.id.answer_list);
		mTopBarContainer = (LinearLayout) findViewById(R.id.top_bar_container);
		mTopBarContainer.setVisibility(View.GONE);

		mAnswerList.setAdapter(mAdapter);
		mAnswerList.setPullLoadEnable(true);
		mAnswerList.setPullRefreshEnable(true);
		mAnswerList.setXListViewListener(this);
		if (currentList == null) {
			currentList = new ArrayList<AnswerListItemModel>();
		}
		loadData();
	}

	private void loadData() {
		JSONObject data = new JSONObject();
		try {
			data.put("packtype", 0);// 0代表广场， 1代表发完悬赏问答之后跳转到广场， 2表示我的问集， 3表示我的答集
			data.put("pageindex", pageIndex);
			data.put("pagecount", PAGE_COUNT);
//			data.put("gradegroup", gradegroup);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpHelper.post(this, "question", "getall",  data, this);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onEventBegin(this, "QAHall");
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onEventEnd(this, "QAHall");

	}

	
	
	
	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (TextUtils.isEmpty(dataJson)) {
			hasMore = false;
		} else {
			Gson gson = new Gson();
			ArrayList<AnswerListItemModel> answerList = null;
			try {
				answerList = gson.fromJson(dataJson, new TypeToken<ArrayList<AnswerListItemModel>>() {
				}.getType());
			} catch (Exception e) {
			}

			pageIndex++;
			if (isRefresh) {
				currentList.clear();
			}
			if (answerList != null && !answerList.isEmpty()) {
				if (answerList.size() < PAGE_COUNT) {
					hasMore = false;
				}
				currentList.addAll(answerList);
			}
			if (currentList.size() == 0) {
				ToastUtils.show(R.string.text_no_question);
			} else if (currentList.size() < 10) {
				ToastUtils.show(getString(R.string.text_question_just_have, currentList.size()));
			}
			mAdapter.setData(currentList);
		}
		onLoadFinish();
	
		
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {
		
		
	}

	@SuppressLint("SimpleDateFormat")
	public void onLoadFinish() {
		mAnswerList.stopRefresh();
		mAnswerList.stopLoadMore();
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String time = sdf.format(new Date());
		mAnswerList.setRefreshTime(time);
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		mAdapter.setScrolling(true);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {
			mAdapter.setScrolling(false);
		}
	}

	public void scrollToRefresh() {
		if (mAnswerList != null) {
			mAnswerList.showHeaderRefreshing();
			mAnswerList.setSelection(0);
			isRefresh = true;
		}
	}

	@Override
	public void onRefresh() {
		pageIndex = 1;
		hasMore = true;
		isRefresh = true;
		loadData();
		mAnswerList.getFooterView().setState(XListViewFooter.STATE_OTHER, "");
	}

	@Override
	public void onLoadMore() {
		isRefresh = false;
		if (hasMore) {
			loadData();
		} else {
			mAnswerList.getFooterView().setState(XListViewFooter.STATE_NOMORE, "");
			onLoadFinish();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (null != mAdapter) {
			mAdapter.setData(null);
		}
		if (currentList != null) {
			currentList.clear();
		}
		mAnswerList.setXListViewListener(null);
		pageIndex = 1;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
			finish();
			break;
		case R.id.next_setp_layout:
			GlobalVariable.QAHallActivity = this;
			IntentManager.goToPayAnswerAskView(this, false);
			break;
		default:
			break;
		}
	}

	

}
