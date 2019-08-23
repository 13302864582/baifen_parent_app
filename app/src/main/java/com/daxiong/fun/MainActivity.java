
package com.daxiong.fun;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.api.HomeListAPI;
import com.daxiong.fun.api.MainAPI;
import com.daxiong.fun.api.UserAPI;
import com.daxiong.fun.api.WeLearnApi;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.dialog.CustomWelcomeDialog;
import com.daxiong.fun.function.account.WoFragment;
import com.daxiong.fun.function.homepage.HomeFragment;
import com.daxiong.fun.function.homepage.OneFragment;
import com.daxiong.fun.function.homework.PublishHwActivity;
import com.daxiong.fun.function.learninganalysis.AnalysisFragment;
import com.daxiong.fun.function.myfudaoquan.fragment.QuanFragment;
import com.daxiong.fun.function.question.PublishQuestionActivity;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.manager.UpdateManagerForDialog;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.SharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.util.WeLearnFileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.daxiong.fun.R.id.item_question;

/**
 * 此类的描述： 主页面
 *
 * @author: sky @最后修改人： sky
 * @最后修改日期:2015-7-31 上午10:08:47
 */
public class MainActivity extends BaseActivity implements OnClickListener {
    public static final String TAG = MainActivity.class.getSimpleName();

    private RelativeLayout rl;

    private ImageView btn_function;

    //fragment
    private FragmentManager fragmentManager;

    private FragmentTransaction transaction;

    private FrameLayout layout_home, layout_analysis, layout_quan, layout_wo;

    private ImageView iv_home, iv_analysis, iv_quan, iv_wo;

    private TextView tv_home, tv_analysis, tv_quan, tv_wo;

    private List<Fragment> fragments;

    private List<FrameLayout> linearLayouts;

    private List<ImageView> imageViews;

    private List<TextView> textViews;

    private HomeFragment homeFragment;

    private AnalysisFragment analysisFragment;

    private QuanFragment quanFragment;

    private OneFragment oneFragment;

    private WoFragment woFragment;


    private UpdateManagerForDialog mUpdateManager;


    //    private ImageView firstUseTipIV;
    private ImageView iv_shang, iv_zhong, iv_xia;

    private UserAPI userApi;


    public static boolean isShowPoint;
    CustomWelcomeDialog tipDialog;
    // 按两次退出标志
    private boolean isDoubleClickExist;

    private static final int CHECK_HZ = 10;

    public static final int CHECK_REDPACKAGE = 110;

    public static final int REQUEST_CODE_BIND = 0;

    private int switchCalendarTag = 1;

    private FrameLayout main_container;

    private RelativeLayout layout_new_guide_page;

    private RelativeLayout layout_mengban;

    private LinearLayout btnQuestion;

    private LinearLayout btnHomework;

    private UserInfoModel uInfo;

    private boolean isFirst = false;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case CHECK_REDPACKAGE:
                    checkUpdate();
                    break;

            }

        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        GlobalVariable.mainActivity = this;
        initObject();
        initView();
        new HomeListAPI().getSystemTime(requestQueue, this, RequestConstant.GET_HOME_SYSTEM_TIME_CODE);

        initListener();
        initFragment();
        // checkWelcomeImage();
    }


    public void initObject() {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        fragments = new ArrayList<Fragment>();
        imageViews = new ArrayList<ImageView>();
        textViews = new ArrayList<TextView>();
        linearLayouts = new ArrayList<FrameLayout>();
    }

    public void initView() {

        userApi = new UserAPI();
        main_container = (FrameLayout)this.findViewById(R.id.main_container);
        rl = (RelativeLayout) findViewById(R.id.rl);


        layout_mengban = (RelativeLayout) this.findViewById(R.id.layout_mengban);
        btnQuestion = (LinearLayout) this.findViewById(R.id.item_question);
        btnHomework = (LinearLayout) this.findViewById(R.id.item_homework);
        layout_new_guide_page = (RelativeLayout) this.findViewById(R.id.layout_new_guide_page);

       /* firstUseTipIV = (ImageView) findViewById(R.id.tips_first_use);
        if (MySharePerfenceUtil.getInstance().isShowFirstUseTip()) {
            firstUseTipIV.setVisibility(View.VISIBLE);
            MySharePerfenceUtil.getInstance().setFirstUseFalse();
        } else {
            firstUseTipIV.setVisibility(View.GONE);
        }*/

        btn_function = (ImageView) this.findViewById(R.id.btn_function);
        iv_shang = (ImageView) this.findViewById(R.id.iv_shang);
        iv_zhong = (ImageView) this.findViewById(R.id.iv_zhong);
        iv_xia = (ImageView) this.findViewById(R.id.iv_xia);

        layout_home = (FrameLayout) this.findViewById(R.id.layout_home);
        layout_analysis = (FrameLayout) this.findViewById(R.id.layout_analysis);
        layout_quan = (FrameLayout) this.findViewById(R.id.layout_quan);
        layout_wo = (FrameLayout) this.findViewById(R.id.layout_wo);

        // 将控件添加到集合中,便于使用
        linearLayouts.add(layout_home);
        linearLayouts.add(layout_analysis);
        linearLayouts.add(layout_quan);
        linearLayouts.add(layout_wo);

        iv_home = (ImageView) this.findViewById(R.id.iv_home);
        iv_analysis = (ImageView) this.findViewById(R.id.iv_analysis);
        iv_quan = (ImageView) this.findViewById(R.id.iv_quan);
        iv_wo = (ImageView) this.findViewById(R.id.iv_wo);

        // 将控件添加到集合中,便于使用
        imageViews.add(iv_home);
        imageViews.add(iv_analysis);
        imageViews.add(iv_quan);
        imageViews.add(iv_wo);

        tv_home = (TextView) this.findViewById(R.id.tv_home);
        tv_analysis = (TextView) this.findViewById(R.id.tv_analysis);
        tv_quan = (TextView) this.findViewById(R.id.tv_quan);
        tv_wo = (TextView) this.findViewById(R.id.tv_wo);

        // 将控件添加到集合中,便于使用
        textViews.add(tv_home);
        textViews.add(tv_analysis);
        textViews.add(tv_quan);
        textViews.add(tv_wo);
//        boolean isNewUser = MySharePerfenceUtil.getInstance().IsNewUser();
//        if (isNewUser) {
//            layout_new_guide_page.setVisibility(View.VISIBLE);
//        } else {
//            layout_new_guide_page.setVisibility(View.GONE);
//            requestWelcomeDialog();
//        }

        //ObjectAnimator.ofFloat(btn_function,"rotation",0f,180f).setDuration(10000).start();
        // Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);//加载动画资源文件
        //btn_function.startAnimation(shake); //给组件播放动画效果

    }

    public void initListener() {
//        backLayout.setOnClickListener(this);

//        titleLayout.setOnClickListener(this);
        btn_function.setOnClickListener(this);
        layout_home.setOnClickListener(this);
        layout_analysis.setOnClickListener(this);
        layout_quan.setOnClickListener(this);
        layout_wo.setOnClickListener(this);
        layout_mengban.setOnClickListener(this);
        btnQuestion.setOnClickListener(this);
        btnHomework.setOnClickListener(this);
        iv_shang.setOnClickListener(this);
        iv_zhong.setOnClickListener(this);
        iv_xia.setOnClickListener(this);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.e(TAG, "onRestart");

    }

    /**
     * 此方法描述的是：初始化Fragment
     *
     * @author: sky @最后修改人： sky
     * @最后修改日期:2015-7-31 下午6:20:06 initFragment void
     */
    private void initFragment() {
        uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
        homeFragment = new HomeFragment();

        analysisFragment = new AnalysisFragment();

        quanFragment = QuanFragment.newInstance(0);

        woFragment = new WoFragment();

        fragments.add(homeFragment);


        fragments.add(analysisFragment);
        fragments.add(quanFragment);
        fragments.add(woFragment);

        // 添加活动fragment
        transaction.add(R.id.main_container, homeFragment, "homeFragment");


        transaction.add(R.id.main_container, analysisFragment, "analysisFragment");
        transaction.add(R.id.main_container, quanFragment, "quanFragment");
        transaction.add(R.id.main_container, woFragment, "woFragment");

        if (null != uInfo && uInfo.getSixteacher() == 1) {
            if (uInfo.getTabbarswitch() == 1) {
                findViewById(R.id.menu_tab).setVisibility(View.VISIBLE);
            } else if (uInfo.getTabbarswitch() == 2) {
                findViewById(R.id.menu_tab).setVisibility(View.GONE);
            }

            oneFragment = new OneFragment();
            fragments.add(oneFragment);
            transaction.add(R.id.main_container, oneFragment, "oneFragment");
            switchCalendarTag = 2;
        }
        setWelearnTitle("家庭作业");
        showFragment(0, transaction);
        // onClick(layout_home);
        // calendarFragment.setFloatView(floatView);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.e(TAG, "onResume");
        WeLearnApi.checkUpdate();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());

        IntentManager.startWService(this);


        uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();


        Intent intent = getIntent();

        if (null != uInfo && uInfo.getSixteacher() == 1 && !isFirst) {
            isFirst = true;
            if (uInfo.getTabbarswitch() == 1) {
                findViewById(R.id.menu_tab).setVisibility(View.VISIBLE);
            } else if (uInfo.getTabbarswitch() == 2) {
                findViewById(R.id.menu_tab).setVisibility(View.GONE);
            }
            if (!fragments.get(4).isHidden()) {// 如果当前页面没有隐藏
                return;
            }
            FragmentTransaction tr = fragmentManager.beginTransaction();

            showFragment(4, tr);
            setTextAndBackgroundColor(0);
            setImage(0);

        } else {
            findViewById(R.id.menu_tab).setVisibility(View.VISIBLE);
            String stringExtra = intent.getStringExtra("layout");
            if ("layout_analysis".equals(stringExtra)) {
                onClick(layout_analysis);

            } else if ("layout_home".equals(stringExtra)) {
                switchCalendarTag = 1;
                onClick(layout_home);
            }
            intent.putExtra("layout", "");
            setIntent(intent);
        }


    }


    @Override
    public void onPause() {
        super.onPause();
        LogUtils.e(TAG, "onPause");
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());

    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.e(TAG, "onStop");
    }

    /**
     * 暂时把检查更新也放进来
     */
    private void checkUpdate() {
        if (mUpdateManager == null) {
            if (!isFinishing()) {
                mUpdateManager = new UpdateManagerForDialog(this);
            }
        }
        mUpdateManager.checkUpdateInfo();
        mHandler.sendEmptyMessageDelayed(CHECK_REDPACKAGE, CHECK_HZ * 60 * 1000);
    }

    private void checkWelcomeImage() {
        userApi.getWelcomeImage(requestQueue, this, RequestConstant.GET_SPLASH_IMAGE_CODE);
    }

    @Override
    public void onClick(View v) {
//        nextLayout.setVisibility(View.VISIBLE);
        switch (v.getId()) {
            case R.id.iv_shang:
                iv_shang.setVisibility(View.INVISIBLE);
                iv_zhong.setVisibility(View.VISIBLE);
                MySharePerfenceUtil.getInstance().setIsNewUser(false);
                break;
            case R.id.iv_zhong:
                iv_zhong.setVisibility(View.INVISIBLE);
                iv_xia.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_xia:
                requestWelcomeDialog();
                MySharePerfenceUtil.getInstance().setWelcomeDialog(-1);
                layout_new_guide_page.setVisibility(View.GONE);
                break;
            case R.id.back_layout:// 左边按钮
//                firstUseTipIV.setVisibility(View.GONE);
                MySharePerfenceUtil.getInstance().setFirstUseFalse();
                break;

            case R.id.layout_home:// 导航(首页)
//                backLayout.setVisibility(View.GONE);
//                nextLayout.setVisibility(View.VISIBLE);
                layout_mengban.setVisibility(View.GONE);
                // removeFloatView();

                setWelearnTitle("作业");
                if (switchCalendarTag == 2) {
                    if (!fragments.get(4).isHidden()) {// 如果当前页面没有隐藏
                        return;
                    }
                    showFragment(4, fragmentManager.beginTransaction());
                    setTextAndBackgroundColor(0);
                    setImage(0);
                    return;
                }
                if (!fragments.get(0).isHidden()) {// 如果当前页面没有隐藏
                    return;
                }
                showFragment(0, fragmentManager.beginTransaction());
                setTextAndBackgroundColor(0);
                setImage(0);
                switchCalendarTag = 1;
                break;

            case R.id.layout_analysis:// 导航(学情)
                MobclickAgent.onEvent(this, "Open_Learn");
                layout_mengban.setVisibility(View.GONE);
                if (!fragments.get(1).isHidden()) {// 如果当前页面没有隐藏
                    return;
                }
                showFragment(1, fragmentManager.beginTransaction());
                setTextAndBackgroundColor(1);
                setImage(1);
                break;

            case R.id.layout_quan:// 导航(券)
                MobclickAgent.onEvent(this, "Open_GrowUp");
                layout_mengban.setVisibility(View.GONE);
                if (!fragments.get(2).isHidden()) {// 如果当前页面没有隐藏
                    return;
                }
                showFragment(2, fragmentManager.beginTransaction());
                setTextAndBackgroundColor(2);
                setImage(2);
                break;

            case R.id.layout_wo:// 导航(我)
                MobclickAgent.onEvent(this, "Open_Me");
                layout_mengban.setVisibility(View.GONE);
                if (!fragments.get(3).isHidden()) {// 如果当前页面没有隐藏
                    return;
                }
                showFragment(3, fragmentManager.beginTransaction());
                setTextAndBackgroundColor(3);
                setImage(3);
                break;
//		case R.id.iv_switch_calendar:// 日历模式(首页)
//			nextLayout.setVisibility(View.VISIBLE);
//			layout_head.setVisibility(View.VISIBLE);
//			if (!fragments.get(4).isHidden()) {// 如果当前页面没有隐藏
//				return;
//			}
//			FragmentTransaction tr = fragmentManager.beginTransaction();
//			// tr.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//			// tr.setCustomAnimations(R.animator.fragment_rotate_enter,
//			// R.animator.fragment_rotate_exit,
//			// R.animator.fragment_rotate_pop_enter,
//			// R.animator.fragment_rotate_pop_exit);
//
//			applyRotation(true, 0, 90);
//
//			showFragment(4, tr);
//			setTextAndBackgroundColor(0);
//			setImage(0);
//
//			break;

            case R.id.layout_back_time_zhou:// 时间轴模式(首页)
                switchCalendarTag = 1;
                if (!fragments.get(0).isHidden()) {// 如果当前页面没有隐藏
                    return;
                }
                FragmentTransaction tr2 = fragmentManager.beginTransaction();
                tr2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                showFragment(0, tr2);
                setTextAndBackgroundColor(0);
                setImage(0);
                break;
            case R.id.layout_mengban:
                //layout_mengban.setVisibility(View.GONE);
            case R.id.btn_function:// 中间的按钮
                if (layout_mengban.getVisibility() == View.GONE) {

                    //中间的按钮顺时针旋转45度
                    ObjectAnimator.ofFloat(btn_function, "rotation", 0f, 135f).start();
                    layout_mengban.setVisibility(View.VISIBLE);
                    ObjectAnimator.ofFloat(btnQuestion, "translationY", 240f, 45f).setDuration(300).start();
                    ObjectAnimator.ofFloat(btnHomework, "translationY", 240f, 45f).setDuration(500).start();

                    //btnQuestion.startAnimation(AnimationUtils.loadAnimation(this, R.anim.popshow));
                    //btnHomework.startAnimation(AnimationUtils.loadAnimation(this, R.anim.popshow));
                } else {

                    ObjectAnimator btnQuestionAnimation = ObjectAnimator.ofFloat(btnQuestion, "translationY", 45f, 300f).setDuration(300);
                    btnQuestionAnimation.start();
                    ObjectAnimator.ofFloat(btnHomework, "translationY", 45f, 300f).setDuration(300).start();
                    btnQuestionAnimation.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            //中间的按钮逆时针旋转45度，恢复初始状态
                            ObjectAnimator.ofFloat(btn_function, "rotation", -135f, 0f).start();
                            layout_mengban.setVisibility(View.GONE);
                        }
                    });


                }
                break;
            case item_question:// 难题答疑
                ObjectAnimator btnQuestionAnimation = ObjectAnimator.ofFloat(btnQuestion, "translationY", 45f, 300f).setDuration(300);
                btnQuestionAnimation.start();
                ObjectAnimator.ofFloat(btnHomework, "translationY", 45f, 300f).setDuration(300).start();
                btnQuestionAnimation.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        //中间的按钮逆时针旋转45度，恢复初始状态
                        ObjectAnimator.ofFloat(btn_function, "rotation", -180 - 45f, 0f).start();
                        layout_mengban.setVisibility(View.GONE);

                        MobclickAgent.onEvent(MainActivity.this, "Open_Question");
                        Intent questionIntent = new Intent(MainActivity.this, PublishQuestionActivity.class);
                        startActivity(questionIntent);
                        layout_mengban.setVisibility(View.GONE);

                    }
                });

                break;
            case R.id.item_homework:// 作业检查
                ObjectAnimator btnHwAnimation = ObjectAnimator.ofFloat(btnQuestion, "translationY", 45f, 300f).setDuration(300);
                btnHwAnimation.start();
                ObjectAnimator.ofFloat(btnHomework, "translationY", 45f, 300f).setDuration(300).start();
                btnHwAnimation.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        //中间的按钮逆时针旋转45度，恢复初始状态
                        ObjectAnimator.ofFloat(btn_function, "rotation", -180 - 45f, 0f).start();
                        layout_mengban.setVisibility(View.GONE);


                        MobclickAgent.onEvent(MainActivity.this, "Open_Homework");
                        Intent hwintent = new Intent(MainActivity.this, PublishHwActivity.class);
                        startActivity(hwintent);
                        layout_mengban.setVisibility(View.GONE);

                    }
                });


                break;


        }
    }


    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        int flag = ((Integer) param[0]).intValue();
        switch (flag) {
            case RequestConstant.GET_SPLASH_IMAGE_CODE:// 请求引导页
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        try {
                            String dataJson = JsonUtil.getString(datas, "Data", "");
                            if (!TextUtils.isEmpty(dataJson)) {
                                String splashurl = JsonUtil.getString(dataJson, "splashurl", "");
                                if (TextUtils.isEmpty(splashurl)) {
                                    WeLearnFileUtil.deleteWelcomeImage();
                                } else {
                                    if (!splashurl.equals(MySharePerfenceUtil.getInstance().getWelcomeImageUrl())) {
                                        MySharePerfenceUtil.getInstance().setWelcomeImageUrl(splashurl);
                                        new DownloadImageTask().execute(splashurl);
                                    }
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        ToastUtils.show(msg);
                    }

                }
                break;
            case RequestConstant.GET_HOME_SYSTEM_TIME_CODE:
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    if (code == 0) {
                        long longtime = JsonUtil.getLong(datas, "Data", -1);
                        if (longtime != -1) {
                            long time = System.currentTimeMillis() - (longtime + 200);
                            SharePerfenceUtil.putLong("errortime", time);
                        }
                    }

                }

                break;
            case RequestConstant.MAIN_ACTIVITY_TIP:// 主界面提示框
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        try {
                            String dataJson = JsonUtil.getString(datas, "Data", "");
                            if (!TextUtils.isEmpty(dataJson)) {
                                String title = JsonUtil.getString(dataJson, "title", "");
                                String content = JsonUtil.getString(dataJson, "content", "");
                                // content="<html><head><title></title></head><body><p>"+"新注册的用户，大熊作业都会免费赠送您
                                // <font size=21 color='#ff7200'> 2 </font>
                                // 张辅导券，<font size=21 color='#ff7200'> 1 </font>
                                // 张作业检查，<font size=21 color='#ff7200'> 1 </font>
                                // 张难题解答，<font size=21 color='#ff7200'> 7 </font>
                                // 天之内使用有效哦！"+"</p></body></html>";
                                // content = "新注册的用户，大熊作业都会免费赠送您 <font size=21
                                // color='#ff7200'> 2 </font> 张辅导券，<font size=21
                                // color='#ff7200'> 1 </font> 张作业检查，<font size=21
                                // color='#ff7200'> 1 </font> 张难题解答，<font size=21
                                // color='#ff7200'> 7 </font> 天之内使用有效哦！";

                                tipDialog = new CustomWelcomeDialog(MainActivity.this, title, content, "以后再说", "去发布作业");
                                final Button positiveBtn = tipDialog.getPositiveButton();
                                final Button negativeBtn = tipDialog.getNegativeButton();
                                tipDialog.setOnNegativeListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        tipDialog.dismiss();

                                    }
                                });

                                tipDialog.setOnPositiveListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        onClickFunction();
                                        tipDialog.dismiss();
                                    }


                                });
                                tipDialog.show();
                                MySharePerfenceUtil.getInstance().setWelcomeDialog(-1);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        ToastUtils.show(msg);
                    }

                }
                break;
        }

    }

    public void onClickFunction() {
        MainActivity.this.onClick(btn_function);
    }

    class DownloadImageTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... urls) {
            int result = -1;
            String urlStr = urls[0];
            // url =
            // "http://www.iyi8.com/uploadfile/2014/0706/20140706112235583.jpg";
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5 * 1000);
                conn.setRequestMethod("GET");
                InputStream inStream = conn.getInputStream();
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String welcomeImgPath = WeLearnFileUtil.getWelcomeImagePath();
                    File wFile = new File(welcomeImgPath);
                    wFile.deleteOnExit();

                    File tFile = new File(WeLearnFileUtil.getWelcomeImagePath() + ".tmp");
                    tFile.deleteOnExit();
                    FileOutputStream fos = null;
                    try {
                        InputStream is = inStream;
                        fos = new FileOutputStream(tFile);
                        byte[] buffer = new byte[1024];
                        int size = 0;
                        int len = -1;
                        while ((len = is.read(buffer)) != -1) {
                            size += len;
                            fos.write(buffer, 0, len);
                        }
                        fos.flush();
                        tFile.renameTo(wFile);
                        result = 0;
                        LogUtils.d(TAG, "下载文件size=" + size);
                    } catch (IOException e) {
                        e.printStackTrace();
                        result = -1;
                    } catch (Exception e) {
                        e.printStackTrace();
                        result = -1;
                    } finally {
                        try {
                            if (null != fos) {
                                fos.close();
                            }
                        } catch (Exception e2) {
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        // @Override
        // protected void onPostExecute(Integer result) {
        // // Toast.makeText(WApplication.getContext(), "下载结果=" + result,
        // // Toast.LENGTH_SHORT).show();
        // LogUtils.d(TAG, "下载结果=" + result);
        // if (result == 0) {
        // WeLearnSpUtil.getInstance().setWelcomeImageUrl(welcome_url);
        // } else {
        // WeLearnSpUtil.getInstance().setWelcomeImageTime(0L);
        // }
        // super.onPostExecute(result);
        // }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {

            // if (mUpdateManager == null) {
            // if (!isFinishing()) {
            // mUpdateManager = new UpdateManagerForDialog(this);
            // }
            // }
            // if (mUpdateManager != null && (System.currentTimeMillis() -
            // checkTime) > 600000) {
            // if (Config.DEBUG) {
            // ToastUtils.show("去服务器检查新包");
            // }
            // mUpdateManager.checkUpdateInfo();
            // checkTime = System.currentTimeMillis();
            //
            // }

            // studyFragment.onRefresh();
        }
    }

    // 控制隐藏或显示fragment
    private void showFragment(int index, FragmentTransaction ftc) {
        if (null != fragments) {
            for (int a = 0; a < fragments.size(); a++) {
                if (index != a) {
                    ftc.hide(fragments.get(a));
                } else {
                    ftc.show(fragments.get(a));
                }
            }
            ftc.commit();
        }
    }

    // 设置底部button背景和字体的颜色
    private void setTextAndBackgroundColor(int index) {
        if (null != linearLayouts && null != imageViews && null != textViews) {
            for (int a = 0; a < linearLayouts.size(); a++) {
                if (index == a) {
                    // linearLayouts.get(a).setBackgroundResource(R.drawable.sport_toolbar_select_bg);
                    textViews.get(a).setTextColor(Color.parseColor("#57be6a"));
                } else {
                    // linearLayouts.get(a).setBackgroundResource(R.drawable.sport_toolbar_bg);
                    textViews.get(a).setTextColor(Color.parseColor("#1F1F1F"));
                }
            }
        }
    }

    // 设置底部image的图片
    private void setImage(int index) {
        if (null != imageViews) {
            switch (index) {
                case 0:
                    imageViews.get(0).setImageResource(R.mipmap.homework_icon_pre);
                    imageViews.get(1).setImageResource(R.mipmap.trend_icon);
                    imageViews.get(2).setImageResource(R.mipmap.growup_icon);
                    imageViews.get(3).setImageResource(R.mipmap.home_icon);
                    break;
                case 1:
                    imageViews.get(1).setImageResource(R.mipmap.trend_icon_pre);
                    imageViews.get(0).setImageResource(R.mipmap.homework_icon);
                    imageViews.get(2).setImageResource(R.mipmap.growup_icon);
                    imageViews.get(3).setImageResource(R.mipmap.home_icon);
                    break;
                case 2:
                    imageViews.get(2).setImageResource(R.mipmap.growup_icon_pre);
                    imageViews.get(0).setImageResource(R.mipmap.homework_icon);
                    imageViews.get(1).setImageResource(R.mipmap.trend_icon);
                    imageViews.get(3).setImageResource(R.mipmap.home_icon);
                    break;
                case 3:
                    imageViews.get(3).setImageResource(R.mipmap.home_icon_pre);
                    imageViews.get(0).setImageResource(R.mipmap.homework_icon);
                    imageViews.get(1).setImageResource(R.mipmap.trend_icon);
                    imageViews.get(2).setImageResource(R.mipmap.growup_icon);
                    break;

                default:
                    break;
            }
        }
    }


    public void requestWelcomeDialog() {
        if (MySharePerfenceUtil.getInstance().getWelcomeDialog() == 1) {// 第一次注册进来弹框
            MainAPI mainApi = new MainAPI();
            mainApi.getMainTip(requestQueue, this, RequestConstant.MAIN_ACTIVITY_TIP);
        }
        MySharePerfenceUtil.getInstance().setWelcomeDialog(-1);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (isDoubleClickExist) {
                isDoubleClickExist = false;
                finish();
                // System.exit(0);
                // android.os.Process.killProcess(android.os.Process.myPid());
            } else {
                isDoubleClickExist = true;
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        isDoubleClickExist = false;
                    }
                }, 3000);
                ToastUtils.show(R.string.text_toast_quit);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 修改信息后更新界面
        if (resultCode == RESULT_OK) {
            if (woFragment != null) {
                // woFragment.updateLeftUI();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        // super.onSaveInstanceState(outState);
        // //将这一行注释掉，阻止activity保存fragment的状态
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalVariable.mainActivity = null;

    }

}
