package com.daxiong.fun.function.course.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.daxiong.fun.R;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.function.course.CourseDetailsActivity;
import com.daxiong.fun.function.course.adapter.WBaseAdapter;
import com.daxiong.fun.function.course.holder.BaseHolder;
import com.daxiong.fun.function.course.holder.FinderCourseListItemHolder;
import com.daxiong.fun.function.course.model.CourseListItemModel;
import com.daxiong.fun.function.course.view.LoadingPageView.LoadResult;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.LogUtils;
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
 * 名师课程-发现课程
 */
public class FinderCourseFragment extends BaseFragment implements OnItemClickListener, IXListViewListener, HttpListener{
	private final String TAG = FinderCourseFragment.class.getSimpleName();

	/**是否需要自动刷新*/
	public static boolean isNeedRefresh = true;
	private List<CourseListItemModel> mData = new ArrayList<CourseListItemModel>();
	private FinderCourseAdapter adapter;
	
	private int gradeid = 0;
	private int pageindex = 1;
	private final int pagecount = 10;
	private boolean hasMore;
	private boolean isRefresh;

	private XListView xListView;
	
	private int[] mBuyCourseIds;
	
	private static final int MSG_REQUESTDATA = 0x11;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_REQUESTDATA:
				requestData();
				break;
			}
		};
	};
	/**当前点击的Item*/
	private CourseListItemModel mCurrentItem;
	
	@Override
	protected View createLoadedView() {
		View view = View.inflate(getActivity(), R.layout.view_xlistview, null);
		xListView = (XListView) view.findViewById(R.id.xlistview);
		adapter = new FinderCourseAdapter(mData);
		xListView.setBackgroundColor(getResources().getColor(R.color.master_lv_bg));
		xListView.setDivider(null);
		xListView.setAdapter(adapter);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(true);
		xListView.getFooterView().hide();
		xListView.setOnItemClickListener(this);
		xListView.setXListViewListener(this);
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if(mData.size() <= 0){
			isNeedRefresh = false;
			show(LoadResult.LOADING);
			
			onRefresh(); // 确保Id请求成功再去请求数据 requestData();
		}else if(isNeedRefresh){
			isNeedRefresh = false;
			
			onRefresh();
		}
	}

	private class FinderCourseAdapter extends WBaseAdapter<CourseListItemModel>{

		public FinderCourseAdapter(List<CourseListItemModel> data) {
			super(data);
		}

		@Override
		public BaseHolder<CourseListItemModel> createHolder() {
			return new FinderCourseListItemHolder();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		position -= xListView.getHeaderViewsCount();
		mCurrentItem = mData.get(position);
		
		Bundle bundle = new Bundle();
		bundle.putInt("courseid", mCurrentItem.getCourseid());
		IntentManager.goToCourseDetailsActivityForResult(getActivity(), bundle, 0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data != null){
			int type = data.getIntExtra("type", -1);
			mCurrentItem.setBuy(type == CourseDetailsActivity.TYPE_YET_BAY ? true : false);
			adapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onRefresh() {
		// 下拉刷新
		isRefresh = true;
		pageindex = 1;
		requestBuyCourseId();
		hasMore = false; 
		xListView.getFooterView().setState(XListViewFooter.STATE_OTHER, "");
		xListView.getFooterView().hide();
	}
	
	@Override
	public void onLoadMore() {
		isRefresh = false;
		if (hasMore) {
			// 在一次加载完成时, pageindex++;
			requestData();
			hasMore = false; //这一次拉取数据完成再进行下一次
		} else {
			xListView.getFooterView().setState(XListViewFooter.STATE_NOMORE, "");
			xListView.getFooterView().hide();
			onLoadFinish();
		}
	}
	
	/**加载完成*/
	@SuppressLint("SimpleDateFormat")
	private void onLoadFinish() {
		xListView.stopRefresh();
		xListView.stopLoadMore();
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String time = sdf.format(new Date());
		xListView.setRefreshTime(time);
	}

	
	
	/**
	 * 更改ListView中课程购买状态
	 * @param listData
	 */
	private void changeListBuyState(ArrayList<CourseListItemModel> listData) {
		if(mBuyCourseIds != null){
			for (CourseListItemModel courseListItemModel : listData) {
				int courseid = courseListItemModel.getCourseid();
				for (int buyId : mBuyCourseIds) {
					if(courseid == buyId){
						courseListItemModel.setBuy(true);
						break;
					}
				}
			}
		}
	}

	/**
	 * 请求数据
	 */
	private void requestData() {
		if(gradeid == 0){
			UserInfoModel userInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
			if(userInfo != null){
				gradeid  = userInfo.getGradeid();
			}
		}
		JSONObject json = new JSONObject();
		try {
			json.put("gradeid", gradeid);
			json.put("pageindex", pageindex);
			json.put("pagecount", pagecount);
		} catch (JSONException e1) {
			LogUtils.e(TAG, "Json： ", e1);
		}
		OkHttpHelper.post(getActivity(),"course", "market", json, this);
	}

	/**
	 *  查询我购买的课程id清单
	 */
	private void requestBuyCourseId() {
		OkHttpHelper.post(getActivity(),"course", "courseidlist", null, new HttpListener() {
			
			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				try {
					mBuyCourseIds = new Gson().fromJson(dataJson, new TypeToken<int[]>() {}.getType());
				} catch (Exception e) {
				}
				mHandler.sendEmptyMessage(MSG_REQUESTDATA);				
			}
			
			@Override
			public void onFail(int HttpCode,String errMsg) {				
				
			}
		});
	}

	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (!TextUtils.isEmpty(dataJson)) {
			ArrayList<CourseListItemModel> listData =  null;
			try {
				listData = new Gson().fromJson(dataJson, new TypeToken<ArrayList<CourseListItemModel>>() {}.getType());
			} catch (Exception e) {
				LogUtils.e(TAG, "数据请求失败！", e);
			}
			if (listData != null) {
				pageindex++;
				show(LoadResult.SUCCEED);
				if (isRefresh) { // 属于下拉刷新, 清空数据
					mData.clear();
				}
				
				changeListBuyState(listData);
				
				mData.addAll(listData);
				adapter.notifyDataSetChanged();
				onLoadFinish();
				if (listData.size() >= pagecount) {
					hasMore = true; //还可能有更多数据
					xListView.getFooterView().show();
				}else{
					xListView.getFooterView().hide();
				}
			}
			
			if (mData.size() == 0) {
				show(LoadResult.EMPTY);
			}
		}
	
		
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {		
		
	}
}
