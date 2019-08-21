package com.daxiong.fun.function.homework.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.daxiong.fun.function.homework.model.StuPublishHomeWorkPageModel;
import com.daxiong.fun.function.study.StuHomeWorkCheckDetailActivity;
import com.daxiong.fun.function.study.StuHomeWorkCheckDetailItemFragment;

import java.util.ArrayList;

public class StuHomeWorkDetailAdapter extends FragmentStatePagerAdapter {

	private ArrayList<StuPublishHomeWorkPageModel> mHomeWorkPageModelList;
	private int type;
	private boolean isShow=false;
	private StuHomeWorkCheckDetailActivity stuHomeWorkCheckDetailActivity;
	private final SparseArray<StuHomeWorkCheckDetailItemFragment> mFragmentRef = new SparseArray<StuHomeWorkCheckDetailItemFragment>();
	private int stuid;

	public void setAllPageData(ArrayList<StuPublishHomeWorkPageModel> homeWorkPageModelList) {
		this.mHomeWorkPageModelList = homeWorkPageModelList;
		for (int i = 0; i < mHomeWorkPageModelList.size(); i++) {
			StuHomeWorkCheckDetailItemFragment fragment = getFragment(i);
			if (fragment != null) {
				
				fragment.showPageData(homeWorkPageModelList.get(i).getCheckpointlist(),i,type,isShow,stuid);
			}
		}
	}

	public void setPageData(int position, StuPublishHomeWorkPageModel pageModel) {
		mHomeWorkPageModelList.set(position, pageModel);
		StuHomeWorkCheckDetailItemFragment fragment = getFragment(position);
		if (fragment != null) {
			fragment.showPageData(pageModel.getCheckpointlist(),0,type,isShow,stuid);
		}
	}

	public StuHomeWorkDetailAdapter(FragmentManager fm, ArrayList<StuPublishHomeWorkPageModel> homeWorkPageModelList, StuHomeWorkCheckDetailActivity stuHomeWorkCheckDetailActivity,int type,boolean isShow,int stuid) {
		super(fm);
		this.mHomeWorkPageModelList = homeWorkPageModelList;
		this.stuHomeWorkCheckDetailActivity = stuHomeWorkCheckDetailActivity;
		this.type = type;
		this.isShow = isShow;
		this.stuid=stuid;
	}

	@Override
	public Fragment getItem(int position) {
		StuHomeWorkCheckDetailItemFragment fragment = StuHomeWorkCheckDetailItemFragment
				.newInstance(mHomeWorkPageModelList.get(position),stuHomeWorkCheckDetailActivity);
		mFragmentRef.put(position, fragment);
		return fragment;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
		mFragmentRef.remove(position);
	}

	public StuHomeWorkCheckDetailItemFragment getFragment(int pos) {
		StuHomeWorkCheckDetailItemFragment fragment = null;
		try {
			fragment = mFragmentRef.get(pos);
		} catch (Exception e) {
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return mHomeWorkPageModelList == null ? 0 : mHomeWorkPageModelList.size();
	}
	
	public void showPoint(int position,int flag){
	    StuHomeWorkCheckDetailItemFragment fragment = getFragment(position);
	    fragment.showPoint(flag);
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