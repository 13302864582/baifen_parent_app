package com.daxiong.fun.function.cram;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.function.course.adapter.WBaseAdapter;
import com.daxiong.fun.function.course.holder.BaseHolder;
import com.daxiong.fun.function.cram.holder.GoodsNotesHolder;
import com.daxiong.fun.function.cram.holder.GoodsNotesModel;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.StirngUtil;
import com.daxiong.fun.view.XListView;
import com.daxiong.fun.view.XListView.IXListViewListener;
import com.daxiong.fun.view.XListViewFooter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 精品讲义
 */
public class GoodsNotesActivity extends BaseActivity implements OnClickListener, OnItemClickListener, HttpListener, IXListViewListener {

	private static final String TAG = null;
	private List<GoodsNotesModel> mData = new ArrayList<GoodsNotesModel>();
	private GoodsNotesAdapter mAdapter;
	private int orgid;
	private int pageindex = 1;
	private final int pagecount = 10;
	private String orgname;
	private XListView xListView;
	private boolean hasMore;
	private boolean isRefresh;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_good_notes);
		setWelearnTitle(StirngUtil.format(R.string.good_notes, ""));

		findViewById(R.id.back_layout).setOnClickListener(this);

		xListView = (XListView) findViewById(R.id.good_notes_lv);
		mAdapter = new GoodsNotesAdapter(mData);
		xListView.setAdapter(mAdapter);
		xListView.setOnItemClickListener(this);
		xListView.setXListViewListener(this);
		xListView.setPullLoadEnable(true); //可以是加载更多
		xListView.getFooterView().hide();
		xListView.setPullRefreshEnable(true); //可以是刷新
		
		Intent intent = getIntent();
		if(intent != null){
			orgid = intent.getIntExtra("orgid", -1);
			orgname = intent.getStringExtra("orgname");
			setWelearnTitle(StirngUtil.format(R.string.good_notes, orgname));
		}
		
		requestData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
			finish();
			break;
		}
	}
	
	private class GoodsNotesAdapter extends WBaseAdapter<GoodsNotesModel>{

		public GoodsNotesAdapter(List<GoodsNotesModel> data) {
			super(data);
		}

		@Override
		public BaseHolder<GoodsNotesModel> createHolder() {
			return new GoodsNotesHolder();
		}
	}

	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		position -= xListView.getHeaderViewsCount();
		// TODO (已完成)讲义Item 点击事件
		Bundle data = new Bundle();
		data.putInt("courseid", mData.get(position).getCourseid());
		data.putBoolean("isSVIP", true);
		IntentManager.goToCourseDetailsActivity(this, data);
	}
	
	private void requestData() {
		/*
		    "orgid": 辅导机构ID
            "pageindex": 第几页,
            "pagecount": 页显示条数,
		 */
		JSONObject json = new JSONObject();
		try {
			json.put("orgid", orgid);
			json.put("pageindex", pageindex);
			json.put("pagecount", pagecount);
		} catch (JSONException e1) {
			LogUtils.e(TAG, "Json： ", e1);
		}
		OkHttpHelper.post(this, "org","excourses",  json, this);
	}

	
	
	@Override
	public void onRefresh() {
		// 下拉刷新
		pageindex = 1;
		requestData();
		hasMore = false;
		isRefresh = true;
		xListView.getFooterView().setState(XListViewFooter.STATE_OTHER, "");
		xListView.getFooterView().hide();
	}

	@Override
	public void onLoadMore() {
		// 加载更多
		isRefresh = false;
		if (hasMore) {
			// 在一次加载完成时, pageindex++;
			requestData();
			hasMore = false;
		} else {
			xListView.getFooterView().setState(XListViewFooter.STATE_NOMORE, "");
			xListView.getFooterView().hide();
			onStopLoad();
		}
	}
	
	/**加载完成*/
	@SuppressLint("SimpleDateFormat")
	private void onStopLoad() {
		xListView.stopRefresh();
		xListView.stopLoadMore();
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String time = sdf.format(new Date());
		xListView.setRefreshTime(time);
	}

	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (!TextUtils.isEmpty(dataJson)) {
			List<GoodsNotesModel> listData = null;
			try {
				listData = new Gson().fromJson(dataJson, new TypeToken<List<GoodsNotesModel>>() {}.getType());
			} catch (Exception e) {
				LogUtils.e(TAG, "数据请求失败！", e);
			}
			
			if (listData != null) {
				pageindex++;
				if (isRefresh) { // 重新加载， 清空数据
					mData.clear();
				}
				
				mData.addAll(listData);
				mAdapter.notifyDataSetChanged();
				onStopLoad();
				if (listData.size() >= pagecount) {
					hasMore = true; //还可能有更多数据
					xListView.getFooterView().show();
				}else{
					xListView.getFooterView().hide();
				}
			}
		}
	
		
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {
		
		
	}
}
