
package com.daxiong.fun.function.account.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.manager.UpdateManagerForDialog;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.util.WeLearnFileUtil;
import com.daxiong.fun.view.SegmentedControl;
import com.daxiong.fun.view.SegmentedControl.OnSegmentChangedListener;
import com.daxiong.fun.view.popwindow.LogoutPopWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 此类的描述：系统设置
 *
 * @author: sky @最后修改人： sky
 * @最后修改日期:2015年8月7日 下午4:39:28
 */
public class SystemSettingActivity extends BaseActivity implements OnClickListener {
    public static final String TAG = SystemSettingActivity.class.getSimpleName();

    private SegmentedControl notifySegmentedControl = null;

    private SegmentedControl vibrateSegmentedControl = null;

    private boolean isLasted;
    private TextView tv_cache,tv_show_cash;

    private UpdateManagerForDialog mUpdateManager;
    private final static int SCAN_OK = 1;
    private final static int DELETE_OK = 2;
    private static final int SCAN_NONE=3;
    private long fileLength = 0;
    private List<String> cachelist;
    private Button btn_logout;

    private RelativeLayout rl_msg_setting;

    private  LogoutPopWindow  popWindow;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCAN_OK:
                    if (fileLength != 0) {
                        double length = (double) fileLength / (1024 * 1024);
                        tv_show_cash.setText(String.format("%.2f", length) + "MB");
                    }else{
                        tv_show_cash.setText(0+"MB");
                    }
                    break;
                case DELETE_OK:
                    tv_cache.setText("清除缓存");
                    closeDialog();
                    ToastUtils.show("缓存清除成功");
                    break;
                case SCAN_NONE:
                    tv_show_cash.setText(0+ "MB");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.fragment_system_setting);
        initView();
        initListener();
        scanCache();
    }

    @Override
    public void initView() {
        super.initView();
        setWelearnTitle(R.string.settings);
        rl_msg_setting = (RelativeLayout) this.findViewById(R.id.rl_msg_setting);
        btn_logout = (Button) this.findViewById(R.id.btn_logout);
        tv_cache = (TextView) findViewById(R.id.tv_cache);
        tv_show_cash= (TextView) this.findViewById(R.id.tv_show_cash);
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

        installListeners();
    }

    public void initListener() {
        findViewById(R.id.back_layout).setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        rl_msg_setting.setOnClickListener(this);

    }

    private void scanCache() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                cachelist = new ArrayList<String>();
                String absolutePath = WeLearnFileUtil.getImgMsgFile().getAbsolutePath() + File.separator;
                File file = null;
                String[] list = WeLearnFileUtil.getImgMsgFile().list();
                if (list == null){
                    mHandler.sendEmptyMessage(SCAN_NONE);
                    return;
                }

                for (String path : list) {
                    if (!path.contains(".")) {
                        String newPath = absolutePath + path;
                        cachelist.add(newPath);
                        file = new File(newPath);
                        fileLength += file.length();
                    }
                }
                mHandler.sendEmptyMessage(SCAN_OK);
            }
        }).start();

    }


    private void installListeners() {
        // find view object with id and add listener on it.
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

        findViewById(R.id.rl_about_app).setOnClickListener(this);
        findViewById(R.id.rl_clear_cache).setOnClickListener(this);

        findViewById(R.id.user_respone_btn).setOnClickListener(this);

        // 暂时作为免打扰设置入口
        findViewById(R.id.rl_not_distur_setting).setOnClickListener(this);

        findViewById(R.id.check_update).setOnClickListener(this);
        View updateTips = findViewById(R.id.tips_update_iv_setting);
        int latestVersion = MySharePerfenceUtil.getInstance().getLatestVersion();
        if (latestVersion > MyApplication.versionCode) {
            isLasted = false;
            updateTips.setVisibility(View.VISIBLE);
        } else {
            updateTips.setVisibility(View.INVISIBLE);
            isLasted = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // StatService.onEventStart(mActivity, "systemSetting", "");
        MobclickAgent.onEventBegin(this, "systemSetting");
    }

    @Override
    public void onPause() {
        super.onPause();
        // StatService.onEventEnd(mActivity, "systemSetting", "");
        MobclickAgent.onEventEnd(this, "systemSetting");
        // WelearnHandler.getInstance().removeMessage(MsgDef.MSG_DEF_LOGOUT);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout://返回
                finish();
                break;

            case R.id.rl_msg_setting://声音开关设置
                Intent it = new Intent(this, MessageSettingActivity.class);
                startActivity(it);
                break;
            case R.id.rl_not_distur_setting://免打扰设置
                IntentManager.goToDoNotDisturbActivity(SystemSettingActivity.this, null, false);
                break;

            // 清除缓存
            case R.id.rl_clear_cache://清除缓存
                showDialog("正在清除缓存");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                  for (String path : cachelist) {
//
//                      File file = new File(path);
//                      if (file.exists()) {
//                          file.delete();
//                      }
//                  }
//                  mHandler.sendEmptyMessage(DELETE_OK);
                        mHandler.sendEmptyMessageDelayed(DELETE_OK, 2000);
                    }
                }).start();
                break;

            case R.id.rl_about_app://关于
                MobclickAgent.onEvent(SystemSettingActivity.this, "About");
                IntentManager.goToAbout(SystemSettingActivity.this);
                break;

            case R.id.btn_logout://退出登录
                MobclickAgent.onEvent(this, "LoginOut");
                MobclickAgent.onEvent(this, "logout");

                 popWindow =new LogoutPopWindow(this, new LogoutPopWindow.ILogoutListener() {
                    @Override
                    public void doSure() {
                        doLogout();
                    }

                    @Override
                    public void doCancle() {
                        popWindow.dismiss();

                    }
                });
                if (!SystemSettingActivity.this.isFinishing()){
                    //显示窗口  //设置layout在PopupWindow中显示的位置
                    popWindow.showAtLocation(this.findViewById(R.id.layout_container), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                }

                break;
            case R.id.user_respone_btn://用户反馈
                MobclickAgent.onEvent(SystemSettingActivity.this, "FreeBack");
                IntentManager.goToUserRequest(SystemSettingActivity.this);
                break;

            case R.id.check_update://检查更新
                if (isLasted) {
                    ToastUtils.show("没有发现最新版本");
                } else {
                    if (mUpdateManager == null) {
                        if (!SystemSettingActivity.this.isFinishing()) {
                            mUpdateManager = new UpdateManagerForDialog(SystemSettingActivity.this);
                        }
                    }
                    if (mUpdateManager != null) {
                        mUpdateManager.cloesNoticeDialog();
                        mUpdateManager.showNoticeDialog(false);
                    }
                }
                break;


        }
    }

    /**
     * 注销
     */
    private void doLogout() {
        showDialog(getString(R.string.text_logging_out));
        OkHttpHelper.post(this, "user", "logout", null, new HttpListener() {
            @Override
            public void onSuccess(int code, String dataJson, String errMsg) {
                cleanUseInfo();
            }

            @Override
            public void onFail(int HttpCode, String errMsg) {
                cleanUseInfo();
            }
        });
    }

    /**
     * 清空用户数据
     */
    private void cleanUseInfo() {
        closeDialog();
        IntentManager.stopWService(this);
        DBHelper.getInstance().getWeLearnDB().deleteCurrentUserInfo();
        MyApplication.mQQAuth.logout(MyApplication.getContext());
        MySharePerfenceUtil.getInstance().setWelearnTokenId("");
        MySharePerfenceUtil.getInstance().setTokenId("");
        MySharePerfenceUtil.getInstance().setIsChoicGream(false);
        MySharePerfenceUtil.getInstance().setGoLoginType(-1);
        // WeLearnSpUtil.getInstance().setGradeId(0);
        // WeLearnSpUtil.getInstance().setOpenId("");
        MySharePerfenceUtil.getInstance().setPhoneNum("");
        MySharePerfenceUtil.getInstance().setAccessToken("");
        if (GlobalVariable.mainActivity != null) {
            GlobalVariable.mainActivity.finish();
        }

        IntentManager.goToPhoneLoginActivity(this, null, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popWindow!=null){
            popWindow.dismiss();
            popWindow=null;
        }
        GlobalVariable.centerActivity = null;
    }

}
