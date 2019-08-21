package com.daxiong.fun.function.cram.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.daxiong.fun.R;
import com.daxiong.fun.function.course.adapter.WBaseAdapter;
import com.daxiong.fun.function.course.fragment.BaseFragment;
import com.daxiong.fun.function.course.holder.BaseHolder;
import com.daxiong.fun.function.course.view.LoadingPageView.LoadResult;
import com.daxiong.fun.function.cram.CramSchoolDetailsActivity;
import com.daxiong.fun.function.cram.holder.EduHolder;
import com.daxiong.fun.function.cram.holder.EduModel;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.view.XListView;
import com.daxiong.fun.view.XListView.IXListViewListener;
import com.daxiong.fun.view.XListViewFooter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EduFragment extends BaseFragment implements OnItemClickListener, HttpListener, IXListViewListener{
	
	/**是否需要自动刷新*/
	public static boolean isNeedRefresh = true; 
	private static final String TAG = EduFragment.class.getSimpleName();
	
	private int pageindex = 1;
	private final int pagecount = 999;

	private EduAdapter mAdapter;
	private List<EduModel> mData = new ArrayList<EduModel>();

	private XListView xListView;

	private boolean isRefresh; //是否下拉刷新
	private boolean hasMore; //是否还有更多数据

	@Override
	public void onStart() {
		super.onStart();
		if(mData.size() > 0){
			if(isNeedRefresh){
				isNeedRefresh = false;
				onRefresh();
			}
		}else{
			isNeedRefresh = false;
			show(LoadResult.LOADING);
			onRefresh();
		}
	}

	@Override
	protected View createLoadedView() {
		View view = View.inflate(getActivity(), R.layout.view_xlistview, null);
		xListView = (XListView) view.findViewById(R.id.xlistview);
		mAdapter = new EduAdapter(mData);
		xListView.setAdapter(mAdapter);
		xListView.setOnItemClickListener(this);
		xListView.setXListViewListener(this);
		xListView.setPullLoadEnable(false); //可以是加载更多
		xListView.getFooterView().hide();
		xListView.setPullRefreshEnable(true); //可以是刷新
		return view;
	}
	
	@Override
	protected View createEmptyView() {
		View view = View.inflate(getActivity(), R.layout.fragment_edu_empty, null);
		view.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		return view;
	}
	
	private class EduAdapter extends WBaseAdapter<EduModel>{

		public EduAdapter(List<EduModel> data) {
			super(data);
		}

		@Override
		public BaseHolder<EduModel> createHolder() {
			return new EduHolder();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		position -= xListView.getHeaderViewsCount();
		//TODO (已完成)机构Item点击事件
		EduModel eduModel = mData.get(position);
		int type = eduModel.getRelationtype();
		int orgid = eduModel.getOrgid();
		if(EduModel.TYPE_VIP == type){
			type = CramSchoolDetailsActivity.TYPE_CRAMSHCOOL;
		}else{
			type = CramSchoolDetailsActivity.TYPE_EDU;
		}
		Bundle bundle = new Bundle();
		bundle.putInt("orgid", orgid);
		bundle.putInt("type", type);
		IntentManager.goToCramSchoolDetailsActivity(getActivity(), bundle);
	}
	
	private void requestData() {
		/*
		  "type":0   // 0我的补习班（包括关注）  1不包括
          "pageindex": 第几页,
          "pagecount": 页显示条数,
		 */
		JSONObject json = new JSONObject();
		try {
			json.put("type", 0);
			json.put("pageindex", pageindex);
			json.put("pagecount", pagecount);
		} catch (JSONException e1) {
			LogUtils.e(TAG, "Json： ", e1);
		}
		OkHttpHelper.post(getActivity(), "org","myorgs", json, this);
	}

	
	
	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (!TextUtils.isEmpty(dataJson)) {
			ArrayList<EduModel> listData =  null;
			try {
//				listData = new Gson().fromJson(dataJson, new TypeToken<ArrayList<EduModel>>() {}.getType());
			    JSONObject json=new JSONObject(dataJson);
                JSONArray array=json.getJSONArray("orglist");
                EduModel model=null;
                listData=new ArrayList<EduModel>();
                for (int i = 0; i < array.length(); i++) {
                    model=new EduModel();
                    JSONObject each=array.getJSONObject(i);
                    model.setOrgid(each.optInt("orgid"));
                    model.setLogo(each.optString("logo"));
                    model.setOrgname(each.optString("orgname"));
                    model.setRelationtype(each.optInt("relationtype"));
                    listData.add(model);
                    
                }
			} catch (Exception e) {
				LogUtils.e(TAG, "数据请求失败！", e);
			}
			
			if (listData != null) {
				pageindex++;
				show(LoadResult.SUCCEED);
				if (isRefresh) { // 属于下拉刷新, 清空数据
					mData.clear();
				}
				
				mData.addAll(listData);
				
				MySharePerfenceUtil.getInstance().setNotOrgVip();
				for (EduModel eduModel : mData) {
					int relationtype = eduModel.getRelationtype();
					if (relationtype == EduModel.TYPE_VIP) {
						MySharePerfenceUtil.getInstance().setOrgVip();
						break;
					}
				}
				
				mAdapter.notifyDataSetChanged();
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

	@Override
	public void onRefresh() {
		// 下拉刷新
		isRefresh = true;
		pageindex = 1;
		requestData();
		hasMore = false; 
		XListViewFooter footerView = xListView.getFooterView();
		if(footerView!=null){
			
			footerView.setState(XListViewFooter.STATE_OTHER, "");
			footerView.hide();
		}
	}

	@Override
	public void onLoadMore() {
		// 加载更多
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

	
}
