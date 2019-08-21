package com.daxiong.fun.function.account.vip;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;

/**
 * 待支付adapter
 *
 * @author Administrator
 */
public class MyOrderListActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {


    private DaizhifuFragment daizhifuFragment;

    private YizhifuFragment yizhifuFragment;

    private FragmentManager fm;

    private RadioGroup radioGroup;

    private RadioButton radio_daizhifu;

    private RadioButton radio_yizhifu;

    private int index = 0;

    private RelativeLayout back_layout;
    private RelativeLayout layout_next_step;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.order_list_activity);
        initView();
        initListener();
        onCheckedChanged(radioGroup, radio_daizhifu.getId());

    }

    @Override
    public void initView() {
        super.initView();
        fm = getSupportFragmentManager();
        back_layout = (RelativeLayout)findViewById(R.id.back_layout);
        layout_next_step = (RelativeLayout)this.findViewById(R.id.next_setp_layout);
        this.radioGroup = (RadioGroup)findViewById(R.id.radio_group);
        radio_daizhifu = (RadioButton)findViewById(R.id.radio_message);
        radio_yizhifu = (RadioButton)findViewById(R.id.radio_friend);
        radio_daizhifu.setText("待支付");
        radio_yizhifu.setText("已支付");
        layout_next_step.setVisibility(View.GONE);
    }

    @Override
    public void initListener() {
        super.initListener();
        radioGroup.setOnCheckedChangeListener(this);
        back_layout.setOnClickListener(this);
        layout_next_step.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_layout:// 返回
                Intent intent=new Intent("com.action.updateuser");
                sendBroadcast(intent);
                finish();
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        index = radio_daizhifu.getId() == checkedId ? 0 : 1;
        setTabSelection(index);
    }

    public void setTabSelection(int index) {
        // 每次先清除上次的选中状态
        clearSelection();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:// 消息tab
//                radio_daizhifu.setBackgroundResource(R.drawable.tab_left_checked);
//                radio_yizhifu.setBackgroundResource(R.drawable.tab_right_normal);
                radio_daizhifu.setTextColor(Color.parseColor("#ffffff"));
                radio_yizhifu.setTextColor(Color.parseColor("#ff6666"));
                if (daizhifuFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    daizhifuFragment = new DaizhifuFragment();
                    transaction.add(R.id.layout_container, daizhifuFragment);
                } else {
                    transaction.show(daizhifuFragment);
                }
                break;
            case 1:// 好友tab
            default:
//                radio_yizhifu.setBackgroundResource(R.drawable.tab_right_checked);
//                radio_daizhifu.setBackgroundResource(R.drawable.tab_left_normal);
                radio_yizhifu.setTextColor(Color.parseColor("#ffffff"));
                radio_daizhifu.setTextColor(Color.parseColor("#ff6666"));
                if (yizhifuFragment == null) {
                    // 如果ContactsFragment为空，则创建一个并添加到界面上
                    yizhifuFragment = new YizhifuFragment();
                    transaction.add(R.id.layout_container, yizhifuFragment);
                } else {
                    transaction.show(yizhifuFragment);
                }
                break;
        }
        transaction.commit();

    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
//        radio_daizhifu.setBackgroundResource(R.drawable.tab_left_normal);
//        radio_yizhifu.setBackgroundResource(R.drawable.tab_right_normal);
        radio_daizhifu.setTextColor(Color.parseColor("#00bdbd"));
        radio_yizhifu.setTextColor(Color.parseColor("#00bdbd"));

    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (daizhifuFragment != null) {
            transaction.hide(daizhifuFragment);
        }

        if (yizhifuFragment != null) {
            transaction.hide(yizhifuFragment);
        }

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        // super.onSaveInstanceState(outState);
        // //将这一行注释掉，阻止activity保存fragment的状态
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (daizhifuFragment != null && fragment instanceof DaizhifuFragment) {
            daizhifuFragment =(DaizhifuFragment) fragment;
        }else if (yizhifuFragment != null && fragment instanceof YizhifuFragment) {
            yizhifuFragment =(YizhifuFragment) fragment;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
