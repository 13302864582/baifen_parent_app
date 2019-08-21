package com.daxiong.fun.function.account.setting;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.view.SegmentedControl;
import com.daxiong.fun.view.SegmentedControl.OnSegmentChangedListener;

/**
 * 此类的描述：免打扰设置
 *
 * @author: sky @最后修改人： sky
 * @最后修改日期:2015年8月7日 下午4:34:01
 */
public class DoNotDisturbActivity extends BaseActivity implements OnClickListener {


    private ToggleButton tg_shangkeshijian_btn;
    private ToggleButton tg_xiuxishijian_btn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // mActionBar.setTitle("免打扰设置");

        setContentView(R.layout.fragment_donotdistur);

        setWelearnTitle(R.string.dnd_settings);

        findViewById(R.id.back_layout).setOnClickListener(this);

        final SegmentedControl day_sc = (SegmentedControl) findViewById(R.id.day_time_segmentedcontrol_donotdis);
        day_sc.setStyle(SegmentedControl.SEGMENT);
        day_sc.newButton(getResources().getString(R.string.system_setting_close), 0);
        day_sc.newButton(getResources().getString(R.string.system_setting_open), 1);
        if (MySharePerfenceUtil.getInstance().getDayNotDis()) {
            day_sc.setSelectedIndex(1);
        } else {
            day_sc.setSelectedIndex(0);
        }
        day_sc.setOnSegmentChangedListener(new OnSegmentChangedListener() {
            @Override
            public void onSegmentChanged(int index) {
                day_sc.setSelectedIndex(index);
                if (index == 1) {
                    MySharePerfenceUtil.getInstance().setDayNotDis(true);
                } else {
                    MySharePerfenceUtil.getInstance().setDayNotDis(false);
                }
            }
        });

        final SegmentedControl night_sc = (SegmentedControl) findViewById(R.id.night_time_segmentedcontrol_donotdis);
        night_sc.setStyle(SegmentedControl.SEGMENT);
        night_sc.newButton(getResources().getString(R.string.system_setting_close), 0);
        night_sc.newButton(getResources().getString(R.string.system_setting_open), 1);
        if (MySharePerfenceUtil.getInstance().getNightNotDis()) {
            night_sc.setSelectedIndex(1);
        } else {
            night_sc.setSelectedIndex(0);
        }
        night_sc.setOnSegmentChangedListener(new OnSegmentChangedListener() {
            @Override
            public void onSegmentChanged(int index) {
                night_sc.setSelectedIndex(index);
                if (index == 1) {
                    MySharePerfenceUtil.getInstance().setNightNotDis(true);
                } else {
                    MySharePerfenceUtil.getInstance().setNightNotDis(false);
                }
            }
        });


        /**************************************************/
        tg_shangkeshijian_btn = (ToggleButton) this.findViewById(R.id.tg_shangkeshijian_btn);
        if (MySharePerfenceUtil.getInstance().getDayNotDis()) {
            tg_shangkeshijian_btn.setChecked(true);
        } else {
            tg_shangkeshijian_btn.setChecked(false);
        }
        tg_shangkeshijian_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tg_shangkeshijian_btn.setChecked(true);
                    MySharePerfenceUtil.getInstance().setNightNotDis(true);
                } else {
                    tg_shangkeshijian_btn.setChecked(false);
                    MySharePerfenceUtil.getInstance().setNightNotDis(false);
                }
            }
        });

        tg_xiuxishijian_btn = (ToggleButton) this.findViewById(R.id.tg_xiuxishijian_btn);
        if (MySharePerfenceUtil.getInstance().getNightNotDis()) {
            tg_xiuxishijian_btn.setChecked(true);
        } else {
            tg_xiuxishijian_btn.setChecked(false);
        }
        tg_xiuxishijian_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tg_xiuxishijian_btn.setChecked(true);
                    MySharePerfenceUtil.getInstance().setNightNotDis(true);
                } else {
                    tg_xiuxishijian_btn.setChecked(false);
                    MySharePerfenceUtil.getInstance().setNightNotDis(false);
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                finish();
                break;
        }
    }
}
