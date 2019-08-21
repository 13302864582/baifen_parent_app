
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
public class MessageSettingActivity extends BaseActivity implements OnClickListener {
    
    private SegmentedControl notifySegmentedControl = null;

    private SegmentedControl vibrateSegmentedControl = null;


    private ToggleButton tg_voice_btn;
    private ToggleButton tg_vibrate_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();

    }

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.message_setting_activity);
        setWelearnTitle(R.string.message_settings);
       
        notifySegmentedControl = (SegmentedControl) findViewById(R.id.allowMsgNotify);
        notifySegmentedControl.setStyle(SegmentedControl.SEGMENT);
        notifySegmentedControl.newButton(getResources().getString(R.string.system_setting_close), 0);
        notifySegmentedControl.newButton(getResources().getString(R.string.system_setting_open), 1);
        if (MySharePerfenceUtil.getInstance().getMsgNotifyFlag()) {
            notifySegmentedControl.setSelectedIndex(1);
        } else {
            notifySegmentedControl.setSelectedIndex(0);
        }

        vibrateSegmentedControl = (SegmentedControl) findViewById(R.id.allowMsgVibrate);
        vibrateSegmentedControl.setStyle(SegmentedControl.SEGMENT);
        vibrateSegmentedControl.newButton(getResources().getString(R.string.system_setting_close), 0);
        vibrateSegmentedControl.newButton(getResources().getString(R.string.system_setting_open), 1);
        if (MySharePerfenceUtil.getInstance().getMsgNotifyVibrate()) {
            vibrateSegmentedControl.setSelectedIndex(1);
        } else {
            vibrateSegmentedControl.setSelectedIndex(0);
        }


        tg_voice_btn= (ToggleButton) this.findViewById(R.id.tg_voice_btn);
        if (MySharePerfenceUtil.getInstance().getMsgNotifyFlag()) {
            tg_voice_btn.setChecked(true);
        } else {
            tg_voice_btn.setChecked(false);
        }
        tg_vibrate_btn= (ToggleButton) this.findViewById(R.id.tg_vibrate_btn);
        if (MySharePerfenceUtil.getInstance().getMsgNotifyVibrate()) {
            tg_vibrate_btn.setChecked(true);
        } else {
            tg_vibrate_btn.setChecked(false);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        findViewById(R.id.back_layout).setOnClickListener(this);
        
        notifySegmentedControl.setOnSegmentChangedListener(new OnSegmentChangedListener() {
            @Override
            public void onSegmentChanged(int index) {
                notifySegmentedControl.setSelectedIndex(index);
                if (index == 1) {
                    MySharePerfenceUtil.getInstance().setMsgNotifyFlag(true);
                } else {
                    MySharePerfenceUtil.getInstance().setMsgNotifyFlag(false);
                }
            }
        });

        vibrateSegmentedControl.setOnSegmentChangedListener(new OnSegmentChangedListener() {
            @Override
            public void onSegmentChanged(int index) {
                vibrateSegmentedControl.setSelectedIndex(index);
                if (index == 1) {
                    MySharePerfenceUtil.getInstance().setMsgNotifyVibrate(true);
                } else {
                    MySharePerfenceUtil.getInstance().setMsgNotifyVibrate(false);
                }
            }
        });

        tg_voice_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    tg_voice_btn.setChecked(true);
                    MySharePerfenceUtil.getInstance().setMsgNotifyFlag(true);

                } else {
                    tg_voice_btn.setChecked(false);
                    MySharePerfenceUtil.getInstance().setMsgNotifyFlag(false);

                }
            }
        });
        tg_vibrate_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    tg_vibrate_btn.setChecked(true);
                    MySharePerfenceUtil.getInstance().setMsgNotifyVibrate(true);
                } else {
                    tg_vibrate_btn.setChecked(false);
                    MySharePerfenceUtil.getInstance().setMsgNotifyVibrate(false);

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
