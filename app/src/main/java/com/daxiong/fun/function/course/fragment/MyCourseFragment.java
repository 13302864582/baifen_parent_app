package com.daxiong.fun.function.course.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.daxiong.fun.R;
import com.daxiong.fun.function.course.adapter.WBaseAdapter;
import com.daxiong.fun.function.course.holder.BaseHolder;
import com.daxiong.fun.function.course.holder.MyCourseListItemHolder;
import com.daxiong.fun.function.course.model.CourseListItemModel;
import com.daxiong.fun.function.course.view.LoadingPageView.LoadResult;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 名师课程-我的课程
 *
 */
public class MyCourseFragment extends BaseFragment implements OnItemClickListener, HttpListener{
	private final String TAG = MyCourseFragment.class.getSimpleName();

	/**是否需要自动刷新*/
	public static boolean isNeedRefresh = true;
	private List<CourseListItemModel> mData = new ArrayList<CourseListItemModel>();
	private MyCourseAdapter adapter;

	@Override
	protected View createLoadedView() {
		ListView listView = new ListView(getActivity());
		adapter = new MyCourseAdapter(mData);
		listView.setBackgroundColor(getResources().getColor(R.color.master_lv_bg));
		listView.setDivider(null);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		return listView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if(mData.size() <= 0){ // 第一次加载
			isNeedRefresh = false;
			show(LoadResult.LOADING);
			requestData();
		}else if(isNeedRefresh){
			isNeedRefresh = false;
			requestData();
		}
	}
	
	public void requestData() {
		OkHttpHelper.post(getActivity(),"course","mycourse", null, this);
	}

	private class MyCourseAdapter extends WBaseAdapter<CourseListItemModel>{

		public MyCourseAdapter(List<CourseListItemModel> data) {
			super(data);
		}

		@Override
		public BaseHolder<CourseListItemModel> createHolder() {
			return new MyCourseListItemHolder();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		SharedPreferences sp = getActivity().getSharedPreferences("reddot", Context.MODE_PRIVATE);
		CourseListItemModel data = mData.get(position);
		sp.edit().putInt(data.getCourseid() + "", data.getCharptercount()).commit();
		
		Bundle bundle = new Bundle();
		bundle.putInt("courseid", data.getCourseid());
		IntentManager.goToCourseDetailsActivity(getActivity(), bundle);
	}

	

	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (!TextUtils.isEmpty(dataJson)) {
			try {
				ArrayList<CourseListItemModel> listData = new Gson().fromJson(dataJson, new TypeToken<ArrayList<CourseListItemModel>>() {}.getType());
				if(listData != null){
					mData.clear();
					mData.addAll(listData);
					adapter.notifyDataSetChanged();
				}
				show(check(mData));
			} catch (Exception e) {
				LogUtils.e(TAG, "数据请求失败！", e);
			}
		}
	
		
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {
		
		
	}
}
