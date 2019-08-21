
package com.daxiong.fun.function.myfudaoquan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;

import com.daxiong.fun.function.myfudaoquan.fragment.QuanFragment;

/**
 * 我的辅导券
 * 
 * @author: sky
 */
public class MyFudaoquanActivity extends BaseActivity  {

	private FragmentManager fm;

	private int type =1;
	private QuanFragment quanFragment;


	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.my_fudaotuan_activity);
		Intent intent = getIntent();
		if (intent != null) {
			type = intent.getIntExtra("type", 1);

		}
		initView();



	}



	public void initView() {



		fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		quanFragment = QuanFragment.newInstance(type);
		ft.add(R.id.layout_listview_container, quanFragment);
		ft.commit();
	}






}
