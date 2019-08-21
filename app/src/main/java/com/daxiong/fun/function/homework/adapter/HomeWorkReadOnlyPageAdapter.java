package com.daxiong.fun.function.homework.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.daxiong.fun.function.homework.HomeWorkReadOnlyDetailItemFragment;

import java.util.List;

public class HomeWorkReadOnlyPageAdapter extends FragmentPagerAdapter {
	
	private List<HomeWorkReadOnlyDetailItemFragment> itemList;

	public HomeWorkReadOnlyPageAdapter(FragmentManager fm, List<HomeWorkReadOnlyDetailItemFragment> itemList) {
		super(fm);
		this.itemList = itemList;
	}

	@Override
	public int getCount() {
		if (itemList != null) {
			return itemList.size();
		}
		return 0;
	}

	@Override
	public Fragment getItem(int arg0) {
		return itemList.get(arg0);
	}


}
