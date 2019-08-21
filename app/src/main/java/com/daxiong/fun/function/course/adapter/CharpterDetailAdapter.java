package com.daxiong.fun.function.course.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.daxiong.fun.function.course.fragment.CharpterDetailItemFragment;
import com.daxiong.fun.function.course.model.CoursePageModel;

import java.util.ArrayList;

public class CharpterDetailAdapter extends FragmentStatePagerAdapter {

	private ArrayList<CoursePageModel> mPageModelList;
	private final SparseArray<CharpterDetailItemFragment> mFragmentRef = new SparseArray<CharpterDetailItemFragment>();

//	public void setAllPageData(ArrayList<CoursePageModel> pageModelList) {
//	this.mPageModelList = pageModelList;
//	for (int i = 0; i < pageModelList.size(); i++) {
//		CharpterDetailItemFragment fragment = getFragment(i);
//		if (fragment != null) {
//			fragment.showPageData(homeWorkPageModelList.get(i).getCheckpointlist());
//		}
//	}
//}
//
//public void setPageData(int position, StuPublishHomeWorkPageModel pageModel) {
//	mHomeWorkPageModelList.set(position, pageModel);
//	StuHomeWorkCheckDetailItemFragment fragment = getFragment(position);
//	if (fragment != null) {
//		fragment.showPageData(pageModel.getCheckpointlist());
//	}
//}

	public CharpterDetailAdapter(FragmentManager fm, ArrayList<CoursePageModel> pageModelList) {
		super(fm);
		this.mPageModelList = pageModelList;
	}

	@Override
	public Fragment getItem(int position) {
		CharpterDetailItemFragment fragment = CharpterDetailItemFragment.newInstance(mPageModelList.get(position));
		mFragmentRef.put(position, fragment);
		return fragment;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
		mFragmentRef.remove(position);
	}

	public CharpterDetailItemFragment getFragment(int pos) {
		CharpterDetailItemFragment fragment = null;
		if (pos < mFragmentRef.size()) {
			fragment = mFragmentRef.get(pos);
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return mPageModelList == null ? 0 : mPageModelList.size();
	}

	public String getPageData(int position){
		String pointlist = "";
		CharpterDetailItemFragment fragment = getFragment(position);
		if (fragment != null) {
			pointlist = fragment.getPointList();
		}
		return pointlist;
	}
	
	public void refreshCurrentPage(int position){
		CharpterDetailItemFragment fragment = getFragment(position);
		if (fragment != null) {
			fragment.loadData();
		}
	}
}

// public class StuHomeWorkDetailAdapter extends PagerAdapter {
// private ArrayList<StuPublishHomeWorkPageModel> mHomeWorkPageModelList;
// private SingleFragmentActivity mActivity;
// private AddPointCommonView mAddPointView;
//
// // private final SparseArray<AddPointCommonView> mFragmentRef = new
// // SparseArray<AddPointCommonView>();
//
// public StuHomeWorkDetailAdapter(ArrayList<StuPublishHomeWorkPageModel>
// mHomeWorkPageModelList,
// SingleFragmentActivity context) {
// super();
// this.mActivity = context;
// this.mHomeWorkPageModelList = mHomeWorkPageModelList;
// }
//
// public void setData(StuPublishHomeWorkPageModel pageModel, int position) {
// // this.mHomeWorkPageModelList.remove(position);
// // mHomeWorkPageModelList.add(position, pageModel);
// mHomeWorkPageModelList.set(position, pageModel);
// notifyDataSetChanged();
// }
//
// @Override
// public int getCount() {
// return mHomeWorkPageModelList == null ? 0 : mHomeWorkPageModelList.size();
// }
//
// @Override
// public boolean isViewFromObject(View arg0, Object arg1) {
// return arg0 == arg1;
// }
//
// @Override
// public void destroyItem(ViewGroup container, int position, Object object) {
// container.removeView(view)
// }
//
// @Override
// public View instantiateItem(ViewGroup container, int position) {
// View view = View.inflate(mActivity,
// R.layout.homework_check_detail_pager_item, null);
// mAddPointView = (AddPointCommonView)
// view.findViewById(R.id.add_point_common_tec_detail);
// if (position < mHomeWorkPageModelList.size()) {
// StuPublishHomeWorkPageModel pageModel = mHomeWorkPageModelList.get(position);
// mAddPointView.showData(pageModel, false, mActivity);
// }
// container.addView(view);
// return view;
// }
//
// }