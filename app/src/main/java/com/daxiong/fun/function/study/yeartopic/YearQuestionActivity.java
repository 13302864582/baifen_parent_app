
package com.daxiong.fun.function.study.yeartopic;

import android.content.Intent;
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
import com.daxiong.fun.constant.EventConstant;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.function.study.yeartopic.adapter.AnswerListAdapter;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.model.AnswerListItemGsonModel;
import com.daxiong.fun.util.MySharePerfenceUtil;
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

/**
 * 此类的描述：历年考题
 * 
 * @author: sky
 */
public class YearQuestionActivity extends BaseActivity
		implements OnClickListener, IXListViewListener, HttpListener, OnScrollListener {

	private TextView nextStepTV;

	private RelativeLayout nextStepLayout;

	private XListView mAnswerListView;

	private AnswerListAdapter mAdapter;

	private LinearLayout mTopBarContainer;

	private TextView mTopbarLeftTab;

	private TextView mTopbarRightTab;

	private View mCustomView;

	public static final String KEY_IS_QPAD = "is_qpad";
	private static final int PAGE_COUNT = 10;

	private ArrayList<AnswerListItemGsonModel> currentList;

	private int pageIndex = 1;

	private boolean isRefresh = true;

	private boolean isQuery = false;

	private String mGrades;

	private int target_role_id = 0;

	private int target_user_id = 0;

	private boolean hasMore = true;

	private int subjectGroupId;

	private int chapterGroupId;

	private int knowPointGroupId;

	private int gradeid;

	private int q_type;

	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			onLoadFinish();
		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.fragment_answer_list);
		initView();
		initListener();
		refreshUI();
		loadData();

	}

	@Override
	public void initView() {
		super.initView();
		this.target_role_id = MySharePerfenceUtil.getInstance().getUserRoleId();
		this.target_user_id = MySharePerfenceUtil.getInstance().getUserId();

		mAdapter = new AnswerListAdapter(this);

		mAnswerListView = (XListView) findViewById(R.id.answer_list);
		mTopBarContainer = (LinearLayout) findViewById(R.id.top_bar_container);
		mTopbarLeftTab = (TextView) findViewById(R.id.tab_text_collection);
		mTopbarRightTab = (TextView) findViewById(R.id.tab_text_questions);

		TextView titleTV = (TextView) findViewById(R.id.title);
		titleTV.setText(R.string.text_excellent_selction);

		nextStepLayout = (RelativeLayout) findViewById(R.id.next_setp_layout);
		nextStepTV = (TextView) findViewById(R.id.next_step_btn);
		nextStepTV.setVisibility(View.VISIBLE);
		nextStepTV.setText(R.string.text_knowledge_point);
		nextStepTV.setBackgroundResource(R.drawable.publish_btn_selector);
	}

	@Override
	public void initListener() {
		super.initListener();
		findViewById(R.id.back_layout).setOnClickListener(this);
		nextStepLayout.setOnClickListener(this);
		mAnswerListView.setAdapter(mAdapter);
		mAnswerListView.setPullLoadEnable(true);
		mAnswerListView.setPullRefreshEnable(true);
		mAnswerListView.setXListViewListener(this);
		mTopbarLeftTab.setOnClickListener(this);
		mTopbarRightTab.setOnClickListener(this);
		if (currentList == null) {
			currentList = new ArrayList<AnswerListItemGsonModel>();
		}
	}

	private void loadData() {
		JSONObject data = new JSONObject();
		try {
			data.put("q_type", q_type);// #0代表一题多解 1代表收藏
			data.put("page", pageIndex);
			data.put("pagenum", PAGE_COUNT);
			data.put("grade", gradeid);
			data.put("subject", subjectGroupId);
			data.put("chapterid", chapterGroupId);
			data.put("pointid", knowPointGroupId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpHelper.post(this, "question", "library", data, this);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onEventBegin(this, "OneQuestionMoreAnswers");

	}

	private void refreshUI() {
		mTopbarLeftTab.setText(getString(R.string.text_one_qus_more_ans));
		mTopbarLeftTab.setTag(GlobalContant.ONE_QUS_MORE_ANS);

		mTopbarRightTab.setText(getString(R.string.text_my_collect));
		mTopbarRightTab.setTag(GlobalContant.COLLECT_OF_MINE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1002:
				if (data != null) {
					int type = data.getIntExtra("type", 0);
					q_type = 0;
					pageIndex = 1;
					refreshUI();
					isRefresh = true;
					switch (type) {
					case 1:
						String keyword = data.getStringExtra("keyword");
						JSONObject dataJson = new JSONObject();
						try {
							dataJson.put("page", pageIndex);
							dataJson.put("pagenum", PAGE_COUNT);
							dataJson.put("keyword", keyword);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						OkHttpHelper.post(this, "question", "librarysearch", dataJson, this);

						break;
					case 2:
						gradeid = data.getIntExtra("gradeid", 0);
						subjectGroupId = data.getIntExtra("subjectGroupId", 0);
						chapterGroupId = data.getIntExtra("chapterGroupId", 0);
						knowPointGroupId = data.getIntExtra("knowPointGroupId", 0);

						loadData();
						break;

					default:
						break;
					}
				}
				break;

			default:
				break;
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
			MobclickAgent.onEvent(this, EventConstant.CUSTOM_EVENT_FILTER);
			// IntentManager.goToTargetFilterActivity(this);
			Intent intent = new Intent(this, SearchKnowledgeActivity.class);
			startActivity(intent);
			GlobalVariable.oneQueMoreAnswActivity = this;
			break;
		case R.id.tab_text_collection:
			mTopbarLeftTab.setTextColor(getResources().getColor(R.color.tabbar_normal));
			mTopbarRightTab.setTextColor(getResources().getColor(R.color.tabbar_pressed));
			int tagLeft = Integer.parseInt(view.getTag().toString());
			if (tagLeft == GlobalContant.ONE_QUS_MORE_ANS) {// 一题多解
				q_type = GlobalContant.MSG_TYPE_SEND;//
				scrollToRefresh();
			}
			break;
		case R.id.tab_text_questions:
			MobclickAgent.onEvent(this, "collection");
			mTopbarRightTab.setTextColor(getResources().getColor(R.color.tabbar_normal));
			mTopbarLeftTab.setTextColor(getResources().getColor(R.color.tabbar_pressed));
			int tagRight = Integer.parseInt(view.getTag().toString());
			if (tagRight == GlobalContant.COLLECT_OF_MINE) {// 我的收藏
				q_type = GlobalContant.ONE_QUES_MORE_ANS;// 我的收藏
				scrollToRefresh();
			}
			break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onEventEnd(this, "OneQuestionMoreAnswers");
	}

	

	public void onLoadFinish() {
		mAnswerListView.stopRefresh();
		mAnswerListView.stopLoadMore();
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String time = sdf.format(new Date());
		mAnswerListView.setRefreshTime(time);
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
		if (mAnswerListView != null) {
			mAnswerListView.showHeaderRefreshing();
			mAnswerListView.setSelection(0);
			isRefresh = true;
		}
		pageIndex = 1;
		loadData();
	}

	@Override
	public void onRefresh() {
		pageIndex = 1;
		hasMore = true;
		isRefresh = true;
		loadData();
		mAnswerListView.getFooterView().setState(XListViewFooter.STATE_OTHER, "");
	}

	@Override
	public void onLoadMore() {
		isRefresh = false;
		if (hasMore) {
			loadData();
		} else {
			mAnswerListView.getFooterView().setState(XListViewFooter.STATE_NOMORE, "");
			onLoadFinish();
		}
	}

	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (TextUtils.isEmpty(dataJson)) {
			hasMore = false;
		} else {
			Gson gson = new Gson();
			ArrayList<AnswerListItemGsonModel> answerList = null;
			try {
				answerList = gson.fromJson(dataJson, new TypeToken<ArrayList<AnswerListItemGsonModel>>() {
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
			mAdapter.setGoTag(2);// 2表示从历年真题点进去的
		}
		onLoadFinish();
	
		
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {
		// TODO Auto-generated method stub
		
	}

}
