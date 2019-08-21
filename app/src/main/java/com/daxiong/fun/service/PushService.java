
package com.daxiong.fun.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.TextUtils;

import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.dispatch.WelearnHandler;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MessageUtils;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;

import de.tavendo.autobahn.WebSocketConnectionHandler;

public class PushService extends IntentService {

    private static final String TAG = PushService.class.getSimpleName();

    private static WelearnWebSocketConnectionHandler webSocketConnectionHandler;

    // 维持心跳的AlarmManager
    private AlarmManager mAlarmManager;

    private Intent mAlarmIntent;

    private int mCurrentReConnCount;

    private static final int MAX_RECONNECT_COUNT = 10000;

    // 心跳消息间隔时间，暂设为5分钟(api 19后不依赖于次设置的时间)
    // see http://developer.android.com/reference/android/app/AlarmManager.html
    public static final int HEART_BEAT_TIME = 1000 * 60 * 5;

    private static final int SEND_SESSION_DELAY = 5 * 1000;

    private static final int MSG_SEND_SESSION_TO_SERVER = 0x1;

    // 自定义action
    private static final String ACTION_ALARM_HEART_BEAT = "ACTION_ALARM_HEART_BEAT";

    public static final String ACTION_EXIT_WEBSOCKET_SERVICE = "action_exit_websocket_service";

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_SEND_SESSION_TO_SERVER:
                    MessageUtils.sendSessionToServer();
                    sendEmptyMessageDelayed(MSG_SEND_SESSION_TO_SERVER, SEND_SESSION_DELAY);
                    break;
            }
        }
    };

    public PushService() {
        super(Thread.currentThread().getName());
    }

    public PushService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (null == webSocketConnectionHandler) {
            webSocketConnectionHandler = new WelearnWebSocketConnectionHandler();
        }
        mAlarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        mAlarmIntent = new Intent(this, PushService.class);
        mAlarmIntent.setAction(ACTION_ALARM_HEART_BEAT);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LogUtils.i(TAG, "---onHandleIntent---");
        if (intent != null) {
            String action = intent.getAction();

            if (ACTION_EXIT_WEBSOCKET_SERVICE.equals(action)) {// 停止服务action
                LogUtils.i(TAG, "---onHandleIntent stop 1 ---");
                MyApplication.mNetworkUtil.disConnect();
                stopSelf();
                return;
            } else if (ACTION_ALARM_HEART_BEAT.equals(action)) {// 心跳action
                if (!MyApplication.mNetworkUtil.isSocketConnected()) {
                    MessageUtils.sendSessionToServer();
                }
                MyApplication.mNetworkUtil.sendPingMessage("ping".getBytes());
            }
        }

        if (isUserLogout()) {
            LogUtils.i(TAG, "---onHandleIntent stop 2 ---");
            stopSelf();
        } else {
            // LogUtils.i(TAG, "---onHandleIntent connectionServer ---");
            MyApplication.mNetworkUtil.connectionServer(AppConfig.PYTHON_URL,
                    webSocketConnectionHandler);
        }
    }

    /**
     * 设置心跳Alarm
     * 
     * @param millis
     * @return
     */
    private void setHeartBeatAlarm(int millis) {
        // LogUtils.d(TAG, "setHeartBeatAlarm millis=" + millis);
        if (millis > 0) {
            int requestCode = 0;
            PendingIntent pendIntent = PendingIntent.getService(getApplicationContext(),
                    requestCode, mAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            mAlarmManager.cancel(pendIntent);

            int triggerTime = (int)(SystemClock.elapsedRealtime() + millis);
            mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, millis,
                    pendIntent);
        }
    }

    private class WelearnWebSocketConnectionHandler extends WebSocketConnectionHandler {

        @Override
        public void onOpen() {// 连接成功
            LogUtils.i(TAG, "---onOpen---");
            PushService.this.mCurrentReConnCount = 0;
            MyApplication.mNetworkUtil.sendPingMessage("ping".getBytes());

            
            mHandler.obtainMessage(MSG_SEND_SESSION_TO_SERVER).sendToTarget();
        }

        @Override
        public void onTextMessage(final String payload) {
            // LogUtils.i(TAG, payload);

            int code = JsonUtil.getInt(payload, "code", -1);
            if (code == 5) {
                return;
            }

            int type = JsonUtil.getInt(payload, "type", 0);
            switch (type) {
                case 2:// 服务器收到Session后的回包
                    int subtype = JsonUtil.getInt(payload, "subtype", 1);
                    switch (subtype) {
                        case 8:
                            LogUtils.i(TAG, "---received backpack---");
                            mHandler.removeMessages(MSG_SEND_SESSION_TO_SERVER);
                            return;
                    }
            }

            WelearnHandler.getInstance().onCmdHandle(payload);
        }

        @Override
        public void onPongMessage(byte[] payload) {
            // LogUtils.e(TAG, "pong: " + new String(payload));
            setHeartBeatAlarm(HEART_BEAT_TIME);
        }

        @Override
        public void onClose(int code, String reason) {
            LogUtils.e(TAG, "---onClose---");
            mHandler.removeMessages(MSG_SEND_SESSION_TO_SERVER);

            if (AppConfig.IS_DEBUG) {
                LogUtils.e(TAG, code + ":" + reason);
                ToastUtils.show(reason);
            }

            // 用户退出登录（用户信息为空），不需要重连WebSocket
            if (isUserLogout()) {
                return;
            }

            if (MyApplication.mNetworkUtil.isInternetConnected(PushService.this)) {
                mCurrentReConnCount++;
                // LogUtils.i(TAG, "长连接异常,发重连消息!当前重连次数: " +
                // mCurrentReConnTimes);
              
                    new Handler(getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MyApplication.mNetworkUtil.processNoNetworkConn();
                        }
                    }, 5000);
               
            } else {
                LogUtils.e(TAG, getString(R.string.text_check_network));
            }
        }
    }

    /**
     * 根据重连尝试次数，计算需要延时等待的时间
     * 
     * @param retryTimes
     * @return
     */
    private int getDelayedTime(int retryTimes) {
        // 简单返回离散值，后续可优化为一公式计算
        switch (retryTimes) {
            case 1:
                return 10000; // 10s
            case 2:
                return 20000; // 20s
            case 3:
                return 40000; // 40s
            case 4:
                return 60000; // 1分钟
            case 5:
                return 90000; // 1.5分钟
            case 6:
                return 180000; // 3分钟
            case 7:
                return 300000; // 5分钟
        }
        return 300000;
    }

    // 判断用户是否登出
    private boolean isUserLogout() {
        String tokenId = MySharePerfenceUtil.getInstance().getWelearnTokenId();
        UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
        // 用户退出登录（用户信息为空），不需要重连WebSocket
        if (TextUtils.isEmpty(tokenId) || null == uInfo) {
            return true;
        }
        return false;
    }
}
