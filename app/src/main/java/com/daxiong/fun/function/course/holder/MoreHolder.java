package com.daxiong.fun.function.course.holder;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.function.course.SearchCourseActivity;

public class MoreHolder extends BaseHolder<Integer> implements OnClickListener{
	
	public static final int HAS_MORE = 0;
	public static final int NO_MORE = 1;
	public static final int ERROR = 2;
	
	private RelativeLayout mLoading, mError;
	
	private SearchCourseActivity activity;

	public MoreHolder(SearchCourseActivity searchCourseActivity) {
		activity = searchCourseActivity;
	}

	@Override
	public View initView() {
		View view = View.inflate(MyApplication.getContext(), R.layout.list_more_loading, null);
		mLoading = (RelativeLayout) view.findViewById(R.id.rl_more_loading);
		mError = (RelativeLayout) view.findViewById(R.id.rl_more_error);
		mError.setOnClickListener(this);
		return view;
	}

	@Override
	public void refreshView() {
		Integer data = getData();
		mLoading.setVisibility(data == HAS_MORE ? View.VISIBLE : View.GONE);
		mError.setVisibility(data == ERROR ? View.VISIBLE : View.GONE);
	}

	@Override
	public void onClick(View v) {
		loadMore();
	}
	
	@Override
	public View getRootView() {
		if (getData() == HAS_MORE) {
			loadMore();
		}
		return super.getRootView();
	}

	/**加载更多数据*/
	private void loadMore() {
		activity.loadMore();
	}
}
