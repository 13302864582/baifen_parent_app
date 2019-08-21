
package com.daxiong.fun;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.EventConstant;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.function.homework.view.GrabRedPackageDialog;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.AppUtils;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.SharePerfenceUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 此类的描述： 闪屏页面
 *
 * @author: sky @最后修改人： sky
 * @最后修改日期:2015-7-31 上午10:09:15
 */
public class SplashActivity extends BaseActivity {

    protected static final String TAG = SplashActivity.class.getSimpleName();

    private ImageView welcomeImageView;

    private LinearLayout layout_extra;

    private ImageView iv_avatar;

    private TextView tv_date;

    private TextView tv_txt;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case GlobalContant.SPLASH:
                    boolean isLogin = false;
                    UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
                    int type = MySharePerfenceUtil.getInstance().getGoLoginType();
                    if (null != uInfo && type != -1) {
                        isLogin = true;
                    } else {
                        isLogin = false;
                    }

                    if (isLogin) {
                        IntentManager.goToMainView(SplashActivity.this);
                    } else {
                        boolean isShow = MySharePerfenceUtil.getInstance().isShowLoginGuide();
                        if (isShow) {
                            IntentManager.goToGuideActivity(SplashActivity.this);
                        } else {
                            IntentManager.goToPhoneLoginActivity(SplashActivity.this, null, true);
                        }
                    }
                    break;
            }
        }

        ;
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.updateOnlineConfig(MyApplication.getContext());
        AppUtils.clickevent("open_app", this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_splash);
        initView();
        initListener();


    }


    @Override
    public void initView() {
        super.initView();
        welcomeImageView = (ImageView) findViewById(R.id.welcome_img);
        layout_extra = (LinearLayout) this.findViewById(R.id.layout_extra);
        iv_avatar = (ImageView) this.findViewById(R.id.iv_avatar);
        tv_date = (TextView) this.findViewById(R.id.tv_date);
        tv_txt = (TextView) this.findViewById(R.id.tv_txt);

        // new HomeListAPI().getSystemTime(requestQueue, this,
        // RequestConstant.GET_HOME_SYSTEM_TIME_CODE);


      /*  long current = System.currentTimeMillis();
        long er_7 = DateUtil.getMillis2Data("2016-02-07 00:00:00:000");
        long er_15 = DateUtil.getMillis2Data("2016-02-15 23:59:59:000");
        long er_22 = DateUtil.getMillis2Data("2016-02-22 00:00:00:000");
        long er_22x = DateUtil.getMillis2Data("2016-02-22 23:59:59:000");

        if (current > er_7 && current < er_15) {// 如果日期为除夕到情人节显示猴年图片
            Glide.with(this).load(R.drawable.splash_hounian).into(welcomeImageView);
        } else if (current > er_22 && current < er_22x) {// 如果日期为元宵节显示元宵节图片
            Glide.with(this).load(R.drawable.splash_yuanxiaojie).into(welcomeImageView);
        } else {
            String filePath = WeLearnFileUtil.getWelcomeImagePath();
            File file = new File(filePath);
            if (file.exists()) {
                Bitmap bm = null;
                int latestVersion = SharePerfenceUtil.getInt("versionCode", -1);
                if (latestVersion == -1 || latestVersion < MyApplication.versionCode) {
                    file.delete();
                    if (AppConfig.IS_DEBUG) {
                        Toast.makeText(this, "删除文件", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Options op = new Options();
                    op.outHeight = 1920;
                    op.outWidth = 1080;
                    bm = BitmapFactory.decodeFile(file.getAbsolutePath(), op);
                }

                if (null != bm) {
                    if (welcomeImageView != null) {
                        welcomeImageView.setImageBitmap(bm);
                        layout_extra.setVisibility(View.GONE);
                    }
                } else {
                    if (AppConfig.IS_DEBUG) {
                        Toast.makeText(this, "文件存在，bitmap为null", Toast.LENGTH_SHORT).show();
                    }
                    MySharePerfenceUtil.getInstance().setWelcomeImageUrl("");

                    TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f);
                    mShowAction.setDuration(2000);
                    layout_extra.setVisibility(View.VISIBLE);
                    Glide.with(this).load(R.drawable.default_icon_circle_avatar).diskCacheStrategy(DiskCacheStrategy.ALL).bitmapTransform(new CropCircleTransformation(this))
                            .into(iv_avatar);
                    tv_date.setAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
                    tv_txt.setAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
                    long errortime = SharePerfenceUtil.getLong("errortime", 0);
//					tv_date.setText(DateUtil.time2Ymd(System.currentTimeMillis() - errortime) + ","
//							+ DateUtil.getDayOfWeekToday());
                    tv_date.setText("");
                    tv_txt.setText("");

                }
            } else {
                MySharePerfenceUtil.getInstance().setWelcomeImageUrl("");

                TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
                        0.0f);
                mShowAction.setDuration(2000);
                layout_extra.setVisibility(View.VISIBLE);
                Glide.with(this).load(R.mipmap.ic_launcher).bitmapTransform(new CropCircleTransformation(this))
                        .into(iv_avatar);
                tv_date.setAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
                tv_txt.setAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
                long errortime = SharePerfenceUtil.getLong("errortime", 0);
                tv_date.setText(
                        DateUtil.time2Ymd(System.currentTimeMillis() - errortime) + "," + DateUtil.getDayOfWeekToday());
                tv_date.setText("");
                tv_txt.setText("");
            }

        }

        if (!isExist()) {
            write();
        }

        */

        SharePerfenceUtil.putInt("versionCode", MyApplication.versionCode);

        Message msg = mHandler.obtainMessage();
        msg.what = GlobalContant.SPLASH;
        int delayed = 1500;
        boolean isNewUser = MySharePerfenceUtil.getInstance().IsNewUser();
        if (isNewUser) {
            delayed = 3000;
        } else {
            delayed = 3000;
        }
        mHandler.sendMessageDelayed(msg, delayed);




    }


    @Override
    public void initListener() {
        super.initListener();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onEventBegin(this, EventConstant.CUSTOM_EVENT_SPLASH);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onEventEnd(this, EventConstant.CUSTOM_EVENT_SPLASH);
    }

    @Override
    public void onClick(View v) {

    }

    private void write() {
        InputStream inputStream;
        try {
            inputStream = getResources().getAssets().open(GrabRedPackageDialog.VOICENAME);
            FileOutputStream fileOutputStream = new FileOutputStream(GrabRedPackageDialog.mAudioPath);
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, count);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isExist() {
        File file = new File(GrabRedPackageDialog.mAudioPath);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        int flag = ((Integer) param[0]).intValue();
        switch (flag) {

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
//		case -1:
//			Message msg = mhandler.obtainMessage();
//			msg.what = GlobalContant.SPLASH;
//			int delayed = 1500;
//			boolean isNewUser = MySharePerfenceUtil.getInstance().IsNewUser();
//			if (isNewUser) {
//				delayed = 2000;
//			} else {
//				delayed = 1500;
//			}
//
//			mhandler.sendMessageDelayed(msg, delayed);
//			break;
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);

    }
}
