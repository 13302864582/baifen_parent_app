package com.daxiong.fun.function.account.setting;

import android.os.Bundle;
import android.view.View;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;

/**
 * 关于大熊作业
 */

public class AboutAppActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_about_app);
        setWelearnTitle(R.string.about_us);
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }


    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initListener() {
        super.initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
