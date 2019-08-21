
package com.daxiong.fun.function.account;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.google.gson.Gson;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.adapter.GuidePageAdapter;
import com.daxiong.fun.adapter.GuidePageAdapter.OnViewClickListener;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.EventConstant;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.QQLoginModel;
import com.daxiong.fun.model.QQUserInfo;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LocationUtils;
import com.daxiong.fun.util.LocationUtils.LocationChangedListener;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MediaUtil;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.PhoneUtils;
import com.daxiong.fun.util.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.daxiong.fun.R.id.iv_voice;
import static com.daxiong.fun.R.id.layout_record;

/**
 * 引导页面
 *
 * @author: sky
 */
public class GuideActivity extends BaseActivity
        implements OnViewClickListener, HttpListener, LocationChangedListener {
    private static final String TAG = GuideActivity.class.getSimpleName();

    // public static String URL = "http://api.map.baidu.com/geocoder/v2/?"
    // + "ak=bhxSDTX4vE7DoZxCBMsYamaK"
    // +
    // "&mcode=AC:16:DC:65:B6:B8:65:92:F0:74:5E:DF:42:0D:73:C9:5E:61:69:41;com.welearn.welearn"
    // + "&output=json" + "&coordtype=wgs84ll" + "&location=";


    private UserInfo mInfo = null;

    private ViewPager mViewPager;

    private GuidePageAdapter vpAdapter;

    private List<View> views;

    private QQLoginModel mQQLoginModel;

    private JSONObject data;

    public static final int REQUEST_CODE_BINDING_PHONE = 1;

    private String province, city;

    private LocationUtils mLocationUtils;

    private static final int QQ_LOGIN_CODE = 121;// qq登录

    private LinearLayout dots_ll;

    private ArrayList<View> dotLists;

    private int currentPosition = 0;

    private int lastIndex = 3;

    /**
     * 是否可以跳转
     */
    private boolean canJump;

    private List<TextView> tvNameList;
    private List<TextView> tvStudyList;
    private List<TextView> tvLocationList;

    private List<RelativeLayout> layoutRecordList;
    private List<ImageView> ivVoiceList;


    private ImageView iv_guide_logo;
    private Button phoneLogin;
    private RelativeLayout layout_record0, layout_record1, layout_record2;
    private String openid;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalContant.CLOSEDIALOG:
                    if (isShowDialog) {
                        closeDialog();
                        isShowDialog = false;
                        // ToastUtils.show(R.string.text_connection_timeout);
                    }
                    break;
                case QQ_LOGIN_CODE:
                    mInfo = new UserInfo(MyApplication.getContext(),
                            MyApplication.mQQAuth.getQQToken());
                    mInfo.getUserInfo(new BaseUiListener() {

                        @Override
                        protected void doComplete(final JSONObject values) {
                            // ToastUtils.show(mActivity, values.toString());
                            // Log.e("登录回包:", values.toString());
                            data = new JSONObject();
                            // LogInActivity.this.values = values;

                            mQQLoginModel = new QQLoginModel();
                            QQUserInfo qinfo = new QQUserInfo();
                            try {
                                qinfo.setNick_name(values.getString("nickname"));
                                qinfo.setVip(values.getString("vip"));
                                qinfo.setLevel(values.getString("level"));
                                qinfo.setProvince(values.getString("province"));
                                qinfo.setGender(values.getString("gender"));
                                qinfo.setFigureurl_qq_40(values.getString("figureurl_qq_1"));
                                qinfo.setFigureurl_qq_100(values.getString("figureurl_qq_2"));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }

                            mQQLoginModel.setProvince(province);
                            mQQLoginModel.setCity(city);

                            mQQLoginModel.setOpenid(MySharePerfenceUtil.getInstance().getTokenId());
                            mQQLoginModel.setUserinfo(qinfo);
                            mQQLoginModel.setOpenid(openid);

                            try {
                                data.put("openid", openid);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // Log.e("QQ回包:", values.toString());
                            GlobalVariable.loginActivity = GuideActivity.this;
                            // GlobalVariable.loginFragment =
                            // LogInFragment.this;

                            mQQLoginModel.setIemi(PhoneUtils.getInstance().getIemi());
                            mQQLoginModel.setSolekey(PhoneUtils.getInstance().getDeviceUUID());

                            try {
                                OkHttpHelper.post(GuideActivity.this, "user", "login",
                                        new JSONObject(new Gson().toJson(mQQLoginModel)),
                                        GuideActivity.this);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    break;
            }
        }
    };


    @SuppressLint("InflateParams")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);
        initView();
        initListener();
    }

    @Override
    public void initView() {
        super.initView();


        if (GlobalVariable.tempList != null) {
            GlobalVariable.tempList.add(this);
        }
        mViewPager = (ViewPager) findViewById(R.id.guide_viewpager);
        dots_ll = (LinearLayout) this.findViewById(R.id.dots_ll);
        dots_ll.setVisibility(View.VISIBLE);

        LayoutInflater inflater = LayoutInflater.from(this);
        View page0 = inflater.inflate(R.layout.view_login_guide_layout, null);
        page0.setBackgroundResource(R.drawable.login_guide_0);
        TextView tvName0 = (TextView) page0.findViewById(R.id.tv_name);
        TextView tvStudy0 = (TextView) page0.findViewById(R.id.tv_study);
        TextView tvLocation0 = (TextView) page0.findViewById(R.id.tv_location);

        layout_record0 = (RelativeLayout) page0.findViewById(layout_record);
        ImageView iv_voice0 = (ImageView) page0.findViewById(iv_voice);
        TextView tv_voice_seconds0= (TextView) page0.findViewById(R.id.tv_voice_seconds);
        tv_voice_seconds0.setText("22'");

        View page1 = inflater.inflate(R.layout.view_login_guide_layout, null);
        page1.setBackgroundResource(R.drawable.login_guide_1);
        TextView tvName1 = (TextView) page1.findViewById(R.id.tv_name);
        TextView tvStudy1 = (TextView) page1.findViewById(R.id.tv_study);
        TextView tvLocation1 = (TextView) page1.findViewById(R.id.tv_location);
        layout_record1 = (RelativeLayout) page1.findViewById(layout_record);
        ImageView iv_voice1 = (ImageView) page1.findViewById(iv_voice);
        TextView tv_voice_seconds1= (TextView) page1.findViewById(R.id.tv_voice_seconds);
        tv_voice_seconds1.setText("18'");

        View page2 = inflater.inflate(R.layout.view_login_guide_layout, null);
        page2.setBackgroundResource(R.drawable.login_guide_2);
        TextView tvName2 = (TextView) page2.findViewById(R.id.tv_name);
        TextView tvStudy2 = (TextView) page2.findViewById(R.id.tv_study);
        TextView tvLocation2 = (TextView) page2.findViewById(R.id.tv_location);

        layout_record2 = (RelativeLayout) page2.findViewById(layout_record);
        ImageView iv_voice2 = (ImageView) page2.findViewById(iv_voice);
        TextView tv_voice_seconds2= (TextView) page2.findViewById(R.id.tv_voice_seconds);
        tv_voice_seconds2.setText("17'");

        View page3 = inflater.inflate(R.layout.view_login_guide_layout_last, null);
        //page3.setBackgroundResource(R.drawable.login_guide_2);
        TextView tvName3 = (TextView) page3.findViewById(R.id.tv_name);
        TextView tvStudy3 = (TextView) page3.findViewById(R.id.tv_study);
        TextView tvLocation3 = (TextView) page3.findViewById(R.id.tv_location);
        iv_guide_logo = (ImageView) page3.findViewById(R.id.iv_guide_logo);
        phoneLogin = (Button) page3.findViewById(R.id.phone_loginorreg_bt);


        tvNameList = new ArrayList<TextView>();
        tvStudyList = new ArrayList<TextView>();
        tvLocationList = new ArrayList<TextView>();
        layoutRecordList = new ArrayList<>();
        ivVoiceList = new ArrayList<>();
        tvNameList.add(tvName0);
        tvNameList.add(tvName1);
        tvNameList.add(tvName2);
        tvNameList.add(tvName3);

        tvStudyList.add(tvStudy0);
        tvStudyList.add(tvStudy1);
        tvStudyList.add(tvStudy2);
        tvStudyList.add(tvStudy3);


        tvLocationList.add(tvLocation0);
        tvLocationList.add(tvLocation1);
        tvLocationList.add(tvLocation2);
        tvLocationList.add(tvLocation3);


        layoutRecordList.add(layout_record0);
        layoutRecordList.add(layout_record1);
        layoutRecordList.add(layout_record2);


        ivVoiceList.add(iv_voice0);
        ivVoiceList.add(iv_voice1);
        ivVoiceList.add(iv_voice2);

        //mLocationUtils = LocationUtils.getInstance(this);

        View page4 = inflater.inflate(R.layout.view_login_guide_layout, null);
        page4.setBackgroundResource(R.drawable.login_guide_2);
        views = new ArrayList<View>();
        views.add(page0);
        views.add(page1);
        views.add(page2);
        views.add(page3);
        // views.add(page4);


        vpAdapter = new GuidePageAdapter(this, views, GuidePageAdapter.GUIDE_TYPE_LOGIN, this);
        mViewPager.setAdapter(vpAdapter);
        //mViewPager.setOffscreenPageLimit(2);
        initDot(4, currentPosition);

       /* boolean isShow = MySharePerfenceUtil.getInstance().isShowLoginGuide();
        if (isShow) {
            MySharePerfenceUtil.getInstance().setShowLoginGuideFalse();
        } else {
            IntentManager.goToPhoneLoginActivity(this, null, true);
            // vp.setCurrentItem(views.size() - 1, false);
        }*/


    }

    @Override
    public void initListener() {
        super.initListener();
        layout_record0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVoice(0);
            }
        });

        layout_record1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVoice(1);
            }
        });


        layout_record2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVoice(2);
            }
        });


        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int postion) {
                currentPosition = postion;
                selectDot(postion);

                switch (postion) {
                    case 0:
                        MediaUtil.getInstance(false).stopPlay();
                        tvNameList.get(0).setText("心心");
                        tvStudyList.get(0).setText("学号309069  四年级");
                        tvLocationList.get(0).setText("江苏 无锡");
                        break;
                    case 1:
                        MediaUtil.getInstance(false).stopPlay();
                        tvNameList.get(1).setText("张女士");
                        tvStudyList.get(1).setText("学号328422  四年级");
                        tvLocationList.get(1).setText("上海 陆家嘴");
                        break;
                    case 2:
                        MediaUtil.getInstance(false).stopPlay();
                        tvNameList.get(2).setText("郭女士");
                        tvStudyList.get(2).setText("学号245357  初二");
                        tvLocationList.get(2).setText("陕西 安康");
                        break;
                    case 3:
                        MediaUtil.getInstance(false).stopPlay();
                        iv_guide_logo.setVisibility(View.VISIBLE);
                        Animation ivGuideLogo = AnimationUtils.loadAnimation(GuideActivity.this, R.anim.guide_push_bottom_in);
                        iv_guide_logo.startAnimation(ivGuideLogo);
                        ivGuideLogo.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                phoneLogin.setVisibility(View.VISIBLE);
                                ObjectAnimator.ofFloat(phoneLogin, "alpha", 0f, 1f).setDuration(1500).start();
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        break;
                }

            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                // if (position==3&&arg1>0.1&&arg2>director) {
                // IntentManager.goToPhoneLoginActivity(GuideActivity.this,
                // null, true);
                // }
                // director=arg2;
                Log.e(TAG, "onPageScrolled position:" + position);
                // 当最后一页往后划动的时候触发该事件
                if (position == lastIndex && positionOffset == 0 && positionOffsetPixels == 0) {
                    if (canJump) {
                        IntentManager.goToPhoneLoginActivity(GuideActivity.this, null, true);
                        // 事件执行一次后进行重置,避免事件多次触发
                        canJump = false;
                        MySharePerfenceUtil.getInstance().setShowLoginGuideFalse();
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e(TAG, "onPageScrollStateChanged state:" + state);
                // 判断是否是划动状态且是最后一页
                if (state == ViewPager.SCROLL_STATE_DRAGGING && currentPosition == lastIndex) {
                    canJump = true;
                } else {
                    canJump = false;
                }
            }
        });

    }

    // 初始化点
    private void initDot(int size, int defalutPosition) {
        dotLists = new ArrayList<View>();
        dots_ll.removeAllViews();
        for (int i = 0; i < size; i++) {
            // 设置点的宽高
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    DensityUtil.dip2px(this, 6), DensityUtil.dip2px(this, 6));
            // 设置点的间距
            params.setMargins(7, 0, 7, 0);
            // 初始化点的对象
            View m = new View(this);
            // 把点的宽高设置到view里面
            m.setLayoutParams(params);
            if (i == defalutPosition) {
                // 默认情况下，首先会调用第一个点。就必须展示选中的点
                m.setBackgroundResource(R.drawable.guide_dot_checked);
            } else {
                // 其他的点都是默认的。
                m.setBackgroundResource(R.drawable.guide_dot_normal);
            }
            // 把所有的点装载进集合
            dotLists.add(m);
            // 现在的点进入到了布局里面
            dots_ll.addView(m);
        }
    }

    private void selectDot(int postion) {
        for (View dot : dotLists) {
            dot.setBackgroundResource(R.drawable.guide_dot_normal);
        }
        if (postion == dotLists.size() - 1) {
            for (View dot : dotLists) {
                dot.setBackgroundResource(0);
            }
        } else {
            phoneLogin.setVisibility(View.GONE);
            dotLists.get(postion).setBackgroundResource(R.drawable.guide_dot_checked);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        // mLocationUtils.startListen(this);
        // mLocationUtils.startBDLocation();
    }


    @Override
    public void onSubViewClick(View v) {
        if (System.currentTimeMillis() - clickTime <= 500) {
            return;
        }
        clickTime = System.currentTimeMillis();

        switch (v.getId()) {
            case R.id.login_bt:
                if (MyApplication.mQQAuth.isSessionValid()) {
                    // mHandler.sendEmptyMessage(121);
                    // showDialog(getString(R.string.text_connecting_please_wait));
                    // isShowDialog = true;
                    // mHandler.sendEmptyMessageDelayed(GlobalContant.CLOSEDIALOG,
                    // 10000);
                    // GlobalVariable.loginActivity = LogInActivity.this;
                    // // GlobalVariable.loginFragment = LogInFragment.this;
                    MyApplication.mQQAuth.reAuth(this, "all", listener);
                } else {
                    // WApplication.mTencent.loginWithOEM(mActivity, "all",
                    // listener,
                    // "10000144",
                    // "10000144", "xxxx");
                    MyApplication.mTencent.login(this, "all", listener);
                }
                break;
            case R.id.phone_loginorreg_bt:
                GlobalVariable.guideActivity = this;
                MySharePerfenceUtil.getInstance().setShowLoginGuideFalse();
                IntentManager.goToPhoneLoginActivity(this, null, true);
                break;
        }
    }


    @Override
    public void onSuccess(int code, String dataJson, String errMsg) {

        try {
            JSONObject jobj = new JSONObject(dataJson);
            int roleid = jobj.getInt("roleid");
            if (roleid != GlobalContant.ROLE_ID_PARENTS) {
                closeDialogHelp();
                ToastUtils.show(R.string.you_are_not_a_parents);
            } else {

                UserInfoModel uInfo = new Gson().fromJson(dataJson, UserInfoModel.class);
                JSONArray dreamuniv2 = JsonUtil.getJSONArray(dataJson, "dreamuniv2", null);
                if (dreamuniv2 != null) {
                    for (int i = 0; i < dreamuniv2.length(); i++) {
                        JSONObject univ = dreamuniv2.getJSONObject(i);
                        if (univ != null) {
                            String name = JsonUtil.getString(univ, "name", "");
                            switch (i) {
                                case 0:
                                    MySharePerfenceUtil.getInstance().setGreamSchool1(name);
                                    break;
                                case 1:
                                    MySharePerfenceUtil.getInstance().setGreamSchool2(name);
                                    break;
                                case 2:
                                    MySharePerfenceUtil.getInstance().setGreamSchool3(name);
                                    break;
                            }
                        }
                    }
                }

                DBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);
                MySharePerfenceUtil.getInstance().saveQQLoginInfo(mQQLoginModel.getOpenid());
                MySharePerfenceUtil.getInstance().setUserRoleId(uInfo.getRoleid());

                String tokenId = jobj.getString("tokenid");
                MySharePerfenceUtil.getInstance().setWelearnTokenId(tokenId);

                // 打开websocket连接
                IntentManager.startWService(MyApplication.getContext());

                String tel = jobj.getString("tel");
                // 判断是否绑定手机号，如果没有绑定，则跳转绑定手机号页面
                if (TextUtils.isEmpty(tel)) {
                    Intent i = new Intent(GuideActivity.this, PhoneRegisterActivity.class);
                    i.putExtra(PhoneRegisterActivity.DO_TAG, PhoneRegisterActivity.DO_BIND);
                    i.putExtra("binding_from_login", true);
                    startActivityForResult(i, REQUEST_CODE_BINDING_PHONE);
                } else {
                    IntentManager.goToMainView(this);
                    // loginOldServer();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFail(int HttpCode, String errMsg) {
        if (AppConfig.IS_DEBUG) {
            ToastUtils.show("fail = " + HttpCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_BINDING_PHONE) {
                openDialogHelp();
                // loginOldServer();
                IntentManager.goToMainView(this);
            }
        }
        closeDialog();
    }

    @Override
    public void onLocationChanged(BDLocation location, String province, String city) {
        LogUtils.d(TAG, "p=" + province + ",c=" + city);
        if (!TextUtils.isEmpty(province) && !TextUtils.isEmpty(city)) {
            this.province = province;
            this.city = city;
            // mLocationUtils.stopListen();
            //mLocationUtils.stopBDLocation();
        }
    }

    @Override
    public void onClick(View v) {


    }


    private void showVoice(final int position) {
        // 显示语音
        final RelativeLayout layout_record = layoutRecordList.get(position);
        final ImageView iv_voice = ivVoiceList.get(position);

        layout_record.setVisibility(View.VISIBLE);
        iv_voice.setImageResource(R.drawable.ic_play2);
        MobclickAgent.onEvent(GuideActivity.this, EventConstant.CUSTOM_EVENT_PLAY_AUDIO);

        iv_voice.setImageResource(R.drawable.play_animation);
        AnimationDrawable mAnimationDrawable = (AnimationDrawable) iv_voice.getDrawable();
        MyApplication.animationDrawables.add(mAnimationDrawable);
        MyApplication.anmimationPlayViews.add(iv_voice);
        /*
		 * WeLearnMediaUtil.getInstance(false) .stopPlay();
		*/
        String currVoicePath = "";
        switch (position) {
            case 0:
                currVoicePath = "guide_sound0.amr";
                break;
            case 1:
                currVoicePath = "guide_sound1.amr";
                break;
            case 2:
                currVoicePath = "guide_sound2.amr";
                break;
        }
        MediaUtil.getInstance(false).playAssetsVoice(currVoicePath, mAnimationDrawable,
                new MediaUtil.ResetImageSourceCallback() {

                    @Override
                    public void reset() {
                        iv_voice.setImageResource(R.drawable.ic_play2);
                    }

                    @Override
                    public void playAnimation() {
                    }

                    @Override
                    public void beforePlay() {
                        MediaUtil.getInstance(false).resetAnimationPlay(iv_voice);
                    }
                }, null);


    }


    public abstract class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            doComplete((JSONObject) response);
        }

        protected abstract void doComplete(JSONObject values);

        @Override
        public void onError(UiError e) {
        }

        @Override
        public void onCancel() {
        }
    }

    IUiListener listener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) { //
            openid = JsonUtil.getString(values, "openid", "");
            mHandler.sendEmptyMessage(QQ_LOGIN_CODE);
            showDialog(getString(R.string.text_connecting_please_wait));
            isShowDialog = true;
            mHandler.sendEmptyMessageDelayed(GlobalContant.CLOSEDIALOG, 10000);
            GlobalVariable.loginActivity = GuideActivity.this;
            // GlobalVariable.loginFragment = LogInFragment.this;
        }
    };


    @Override
    public void onPause() {
        super.onPause();
        // mLocationUtils.stopListen();
        //mLocationUtils.stopBDLocation();
        MediaUtil.getInstance(false).stopPlay();
    }

    public void closeDialogHelp() {
        super.closeDialogHelp();
        mHandler.removeMessages(GlobalContant.CLOSEDIALOG);
    }

    public void openDialogHelp() {
        showDialog(getString(R.string.text_connecting_please_wait));
        isShowDialog = true;
        mHandler.sendEmptyMessageDelayed(GlobalContant.CLOSEDIALOG, 7000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isShowDialog = false;
        mHandler.removeMessages(GlobalContant.CLOSEDIALOG);
        MediaUtil.getInstance(false).stopPlay();
    }


}
