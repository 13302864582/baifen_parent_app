
package com.daxiong.fun;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.iflytek.cloud.Setting;
import com.iflytek.cloud.SpeechUtility;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.connect.auth.QQAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.daxiong.fun.constant.WxConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.db.WelearnDBUtil;
import com.daxiong.fun.exception.CrashHandlerException;
import com.daxiong.fun.http.VolleyRequestQueue;
import com.daxiong.fun.model.ExplainPoint;
import com.daxiong.fun.model.FudaoquanModel;
import com.daxiong.fun.util.NetworkUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * application 类
 * 
 * @author: sky
 */
public class MyApplication extends Application {
    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    private static Handler mMainThreadHandler;

    private static MyApplication mInstance;

    private static Context mContext;

    public static NetworkUtils mNetworkUtil;

    public static LinkedHashSet<String> readyReqQueue;

    public static boolean isChange = false;// 维护抢题页面是否需要答题

    public static boolean isInChatMsgView = false;// 是否在聊天页面

    public static int notifiFromUser = 0;// 是否有消息通知 , 0则没有

    public static LinkedHashSet<ExplainPoint> coordinateAnswerIconSet;

    public static Map<Double, Integer> time2CmdMap;
    public static ArrayList<Integer> checklist=new ArrayList<Integer>();
    public static ArrayList<View> checkviewlist=new ArrayList<View>();
    public static ArrayList<Runnable> runnablelists=new ArrayList<Runnable>();

    public static List<AnimationDrawable> animationDrawables;

    public static List<ImageView> anmimationPlayViews;

    public static int currentUserId;

    public static float lastmessagetimestamp;// 最后一条消息的时间戳

    public static AudioManager audioManager;

    public static IWXAPI api;

    public static QQAuth mQQAuth;

    public static Tencent mTencent;

    public static int versionCode;// 版本号

    public static String versionName;// 版本名

    public static String umeng_channel;

    public static FudaoquanModel fudaoquanmodel;// 临时变量

    public static FudaoquanModel getFudaoquanmodel() {
        return fudaoquanmodel;
    }

    public static void setFudaoquanmodel(FudaoquanModel fudaoquanmodel) {
        MyApplication.fudaoquanmodel = fudaoquanmodel;
    }

    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    public static Context getContext() {
        return mContext;
    }

    public static String getChannelValue() {
        return umeng_channel;
    }
    
    private WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();

    public WindowManager.LayoutParams getWindowParams() {
        return windowParams;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        mInstance = this;
        mContext = getApplicationContext();
        umeng_channel = getMetaValue("UMENG_CHANNEL");
        mMainThreadHandler = new Handler();
        // 初始化数据库
        DBHelper.getInstance().getWeLearnDB();
        // 初始化网络请求队列
        VolleyRequestQueue.init(mContext);
        // 初始化对象
        initObject();
        initException();
        initWx();
        loadVersion();
        initData();
       
    }

    private void initObject() {
        coordinateAnswerIconSet = new LinkedHashSet<ExplainPoint>();
        animationDrawables = new ArrayList<AnimationDrawable>();
        anmimationPlayViews = new ArrayList<ImageView>();
        readyReqQueue = new LinkedHashSet<String>();
        mNetworkUtil = NetworkUtils.getInstance();
        time2CmdMap = new ConcurrentHashMap<Double, Integer>();

        // add by milo 2014.09.23
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMicrophoneMute(false);
        audioManager.setSpeakerphoneOn(true);
        audioManager.setMode(AudioManager.STREAM_MUSIC);

        // 监听网络变化广播
        LocalBroadcastManager.getInstance(mContext)
                .sendBroadcast(new Intent("com.welearn.reveiver.startconn"));
    }

    // 扑捉异常
    private void initException() {
        // CrashHandler crashHandler=new CrashHandler(mContext);
        // Thread.setDefaultUncaughtExceptionHandler(crashHandler);
        CrashHandlerException crashHandler = new CrashHandlerException(this);
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
    }

    // 初始化腾讯api
    private void initWx() {
        mTencent = Tencent.createInstance(WxConstant.APP_ID_QQ, mContext);
        mQQAuth = QQAuth.createInstance(WxConstant.APP_ID_QQ, mContext);
        api = WXAPIFactory.createWXAPI(mContext, WxConstant.APP_ID_WW, true);
        api.registerApp(WxConstant.APP_ID_WW);

        // 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误
        SpeechUtility.createUtility(MyApplication.this, "appid=" + getString(R.string.app_id));
        // 以下语句用于设置日志开关（默认开启），设置成false时关闭语音云SDK日志打印
        Setting.setShowLog(true);

    }

    /**
     * 加载版本信息
     * 
     * @author: sky void
     */
    private void loadVersion() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取清单文件张Meta-Data的Value值
     */
    public static String getMetaValue(String metaKey) {
        String value = "";
        if (mContext != null && metaKey != null) {
            try {
                String packageName = mContext.getPackageName();
                PackageManager packageManager = mContext.getPackageManager();
                ApplicationInfo aiApplicationInfo = packageManager.getApplicationInfo(packageName,
                        PackageManager.GET_META_DATA);

                if (null != aiApplicationInfo) {
                    Bundle metaData = aiApplicationInfo.metaData;
                    if (null != metaData) {
                        value = metaData.getString(metaKey, "");
                        if (TextUtils.isEmpty(value)) {
                            value = metaData.getInt(metaKey, 0) + "";
                        }
                    }
                }

            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        return value;
    }

    /**
     * 初始化静态数据
     */
    private void initData() {
        // if (DBHelper.getInstance().getWeLearnDB().getSubjectCount() <= 0) {
        // WelearnDBUtil.loadDefaultGradeDB();
        // WelearnDBUtil.loadDefaultSubjectDB();
        // }

        System.out.println("subject-->" + DBHelper.getInstance().getWeLearnDB().getSubjectCount());
        if (DBHelper.getInstance().getWeLearnDB().getSubjectCount() <= 0) {
            WelearnDBUtil.loadDefaultGradeDB();
            WelearnDBUtil.loadDefaultSubjectDB();
        } else if (DBHelper.getInstance().getWeLearnDB().getSubjectCount() > 0) {
            int yuwenCount = DBHelper.getInstance().getWeLearnDB().querySubjectWithYuwen();
            if (yuwenCount == 0 || yuwenCount > 1) {
                DBHelper.getInstance().getWeLearnDB().delSubjectAndGradeTable();
                WelearnDBUtil.loadDefaultGradeDB();
                WelearnDBUtil.loadDefaultSubjectDB();
            }

        }
    }

}
