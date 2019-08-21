package com.daxiong.fun.function.cram;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.function.course.adapter.WBaseAdapter;
import com.daxiong.fun.function.course.holder.BaseHolder;
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

/**
 * 我的补习班
 */
public class MyCramSchoolActivity extends BaseActivity implements OnClickListener, HttpListener, OnItemClickListener, IXListViewListener{
	
	private static final String TAG = MyCramSchoolActivity.class.getSimpleName();
	
	private int pageindex = 1;
	private final int pagecount = 999;
	
	private XListView xListView;
	private List<EduModel> mData = new ArrayList<EduModel>();
	private MyCramSchool mAdapter;

	private boolean isRefresh;

	private boolean hasMore;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_mycramschool);
		setWelearnTitle(R.string.menu_cramschool);
		
		findViewById(R.id.back_layout).setOnClickListener(this);
		
		xListView = (XListView) findViewById(R.id.mycramschool_lv);
		mAdapter = new MyCramSchool(mData);
		xListView.setAdapter(mAdapter);
		xListView.setOnItemClickListener(this);
		xListView.setXListViewListener(this);
		xListView.setPullLoadEnable(false); //可以是加载更多
		xListView.getFooterView().hide();
		xListView.setPullRefreshEnable(true); //可以是刷新
		
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
	
	private void requestData() {
		/*
		  "type":0   // 0我的补习班（包括关注）  1不包括
          "pageindex": 第几页,
          "pagecount": 页显示条数,
		 */
		JSONObject json = new JSONObject();
		try {
			json.put("type", 1);
			json.put("pageindex", pageindex);
			json.put("pagecount", pagecount);
		} catch (JSONException e1) {
			LogUtils.e(TAG, "Json： ", e1);
		}
		OkHttpHelper.post(this, "org","myorgs", json, this);
	}
	
	private class MyCramSchool extends WBaseAdapter<EduModel>{

		public MyCramSchool(List<EduModel> data) {
			super(data);
		}

		@Override
		public BaseHolder<EduModel> createHolder() {
			return new EduHolder();
		}
	}


	
	
	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (!TextUtils.isEmpty(dataJson)) {
			ArrayList<EduModel> listData =  null;
			try {
//			    listData = new Gson().fromJson(dataJson, new TypeToken<ArrayList<EduModel>>() {}.getType());
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
			
			if (listData != null && !listData.isEmpty()) {
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
			}else {
				if (pageindex == 1) {
					MySharePerfenceUtil.getInstance().setNotOrgVip();
				}
			}
		}else {
			if (pageindex == 1) {
				MySharePerfenceUtil.getInstance().setNotOrgVip();
			}
		}
	
		
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {
		
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		position -= xListView.getHeaderViewsCount();
		//TODO (已完成)我的补习班Item点击事件
		Bundle data = new Bundle();
		data.putInt("type", CramSchoolDetailsActivity.TYPE_CRAMSHCOOL);
		data.putInt("orgid", mData.get(position).getOrgid());
		IntentManager.goToCramSchoolDetailsActivity(this, data);
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

	
}
