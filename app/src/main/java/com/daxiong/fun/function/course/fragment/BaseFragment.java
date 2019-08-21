package com.daxiong.fun.function.course.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daxiong.fun.MyApplication;
import com.daxiong.fun.function.course.view.LoadingPageView;
import com.daxiong.fun.function.course.view.LoadingPageView.LoadResult;
import com.daxiong.fun.util.ViewUtils;

import java.util.List;

public abstract class BaseFragment extends Fragment{
	
	protected LoadingPageView mContentView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mContentView == null) {
			
			//为null时，创建一个
			mContentView = new LoadingPageView(MyApplication.getContext()) {

				@Override
				public View createLoadedView() {
					return BaseFragment.this.createLoadedView();
				}
				
				@Override
				public View createEmptyView() {
					View emptyView = BaseFragment.this.createEmptyView();
					if (emptyView != null) {
						return emptyView;
					}
					return super.createEmptyView();
				}
				
				@Override
				public View createErrorView() {
					View errorView = BaseFragment.this.createErrorView();
					if (errorView != null) {
						return errorView;
					}
					return super.createErrorView();
				}
			};
		} else {
			
			//不为null时，需要把自身从父布局中移除，因为ViewPager会再次添加
			ViewUtils.removeSelfFromParent(mContentView);
		}
		return mContentView;
	}

	/** 加载数据出错 */
	protected View createErrorView() {
		return null;
	}

	/** 数据为空显示View */
	protected View createEmptyView() {
		return null;
	}

	/** 根据状态显示页面 */
	public void show(LoadResult result) {
		if (mContentView != null) {
			mContentView.show(result);
		}
	}

	/** 数据加载成功显示的View  */
	protected abstract View createLoadedView();
	
	/** 数据校验 */
	public LoadResult check(Object obj) {
		if (obj == null) {
			return LoadResult.ERROR;
		}
		if (obj instanceof List) {
			List<?> list = (List<?>) obj;
			if (list.size() == 0) {
				return LoadResult.EMPTY;
			}
		}
		return LoadResult.SUCCEED;
	}
}
