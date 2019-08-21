
package com.daxiong.fun.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.api.WeLearnApi;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.http.volley.VolleyRequestClientAPI;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.okhttp.OkHttpUtils;
import com.daxiong.fun.okhttp.builder.PostFormBuilder;
import com.daxiong.fun.okhttp.callback.StringCallback;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.NetworkUtils;
import com.daxiong.fun.util.ToastUtils;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Request;

/**
 * 此类的描述： http请求封装类
 *
 * @author: Sky @最后修改人： Sky
 * @最后修改日期:2016年5月13日 下午6:05:42
 */
public class OkHttpHelper {

    public static final String COOKIE_KEY = "Cookie";

    public static final String SET_COOKIE_KEY = "Set-Cookie";

    public static final String WELEARN_SESSION_ID_KEY = "WeLearnSessionID";

    /**
     * post请求
     *
     * @param module
     * @param func
     * @param data
     * @param lisener
     */
    public static void post(Context context, String module, String func, JSONObject data, HttpListener lisener) {
        String url = AppConfig.GO_URL + module + "/" + func;
        Log.d("fzhttp",url);
        postWithUrl(context, url, data, lisener);
    }


    /**
     * 微信支付
     * @param context
     * @param module
     * @param func
     * @param data
     * @param lisener
     */
    public static void postWx(Context context, String module, String func, JSONObject data, HttpListener lisener) {
        String url = AppConfig.GET_WXPAY + module + "/" + func;
        postWithUrl(context, url, data, lisener);
    }

    private static void postWithUrl(Context context, String url, JSONObject data, HttpListener lisener) {
        if (!NetworkUtils.getInstance().isInternetConnected(context)) {
            if (context != null) {
                ((BaseActivity) context).closeDialog();
            }
            ToastUtils.show(MyApplication.getContext().getResources().getString(R.string.text_check_network));
            return;
        }

        PostFormBuilder builder = OkHttpUtils.post().url(url);
        int ver = MyApplication.versionCode;
        builder.addParams("ver", ver + "");
        builder.addParams("appname", VolleyRequestClientAPI.APP_NAME);
        String sourcechan = MyApplication.getChannelValue();
        if (TextUtils.isEmpty(sourcechan)) {
            sourcechan = "welearn";
        }
        builder.addParams("sourcechan", sourcechan);
        builder.addParams("tokenid", MySharePerfenceUtil.getInstance().getWelearnTokenId());

        String dataStr = null;
        if (null != data) {
            dataStr = data.toString();
        } else {
            dataStr = "{}";
        }
        builder.addParams("data", dataStr);
        Log.d("fzlog","ver:"+ver+",appname:"+VolleyRequestClientAPI.APP_NAME+",sourcechan:"+sourcechan+",tokenid:"+MySharePerfenceUtil.getInstance().getWelearnTokenId()+",data:"+dataStr);
        MyStringCallback myStringCallback = new MyStringCallback(context, lisener, url, data);
        builder.build().connTimeOut(10000).writeTimeOut(10000).readTimeOut(8000).execute(myStringCallback);

    }


    //////////////////////////////
    ///////////////////////////////////////////

    /**
     * 此类的描述：请求回调
     *
     * @author: Sky @最后修改人： Sky
     * @最后修改日期:2016年5月13日 上午10:37:25
     */
    static class MyStringCallback extends StringCallback {
        private final Context context;
        private HttpListener listener;
        private String url;
        private JSONObject data;
        private BaseActivity activity;

        public MyStringCallback(Context context, HttpListener listener, String url, JSONObject data) {
            super();
            this.context = context;
            this.listener = listener;
            this.url = url;
            this.data = data;
            this.activity = (BaseActivity) context;
        }

        @Override
        public void onBefore(Request request) {
            super.onBefore(request);
            if (activity.mProgressDialog == null) {
                activity.showDialog("加载中...");
            } else {
                if (!activity.mProgressDialog.isShowing()) {
                    activity.showDialog("加载中...");
                }
            }
        }

        @Override
        public void onAfter() {
            super.onAfter();
            if (null != activity) {
                activity.closeDialog();
            }

        }

        @Override
        public void onError(Call call, Exception e) {
            // ToastUtils.show("onError:" + e.getMessage());
            // if (null != activity) {
            // activity.closeDialog();
            // }
            String errorMsg = "";
            if (e != null && !TextUtils.isEmpty(e.getMessage())) {
                errorMsg = e.getMessage();
            } else {
                errorMsg = e.getClass().getSimpleName();
            }
            if (AppConfig.IS_DEBUG) {
                ToastUtils.show("onError:" + errorMsg);
            } else {
                ToastUtils.show("网络异常");
            }
            listener.onFail(-1, errorMsg);

        }

        @Override
        public void onResponse(String response) {
            // if (null != activity) {
            // activity.closeDialog();
            // }
            if (null != listener) {
                if (!TextUtils.isEmpty(response)) {
                    int code = JsonUtil.getInt(response, "Code", -1);
                    String msg = JsonUtil.getString(response, "Msg", "");
                    final String responseStr = JsonUtil.getString(response, "Data", "");
                    switch (code) {
                        case 0:
                            listener.onSuccess(code, responseStr, msg);
                            break;
                        case 2:
                            //doReLogin(context, activity);
                            if (msg.contains("用户未登")){
                                ToastUtils.show("登录过期请重新登录");
                                IntentManager.goToPhoneLoginActivity(activity,null,true);
                            }
                            break;
                        default:
                            ToastUtils.show(msg);
                            if (null != listener) {
                                listener.onFail(code, msg);
                            }
                            break;
                    }

                } else {
                    // listener.onFail(-1,msg);
                    ToastUtils.show("服务器返回异常");
                }
            }

        }

        @Override
        public void inProgress(float progress) {
            // Log.e(TAG, "inProgress:" + progress);
            // mProgressBar.setProgress((int) (100 * progress));
        }


    }

    /**
     * 此方法描述的是：重登录操作
     *
     * @author: Sky @最后修改人： Sky
     * @最后修改日期:2016年5月13日 下午2:38:39 doReLogin void
     */
    public static void doReLogin(final Context context, final BaseActivity activity) {

        WeLearnApi.relogin(context, new HttpListener() {

            @Override
            public void onSuccess(int code, String dataJson, String errMsg) {
                if (code == 0) {
                    UserInfoModel uInfo = null;
                    try {
                        uInfo = new Gson().fromJson(dataJson, UserInfoModel.class);
                        if (uInfo != null) {
                            DBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);
                        }
                        // 打开websocket连接
                        IntentManager.startWService(MyApplication.getContext());
//						postWithUrl(context, url, data, listener);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
//               if (null != listener) {
//                   listener.onSuccess(code, dataJson, errMsg);
//                }
                    if (code == 1) { // 跳转登录页面
                        MyApplication.mNetworkUtil.disConnect();
                        if (null != context) {
                            IntentManager.goToPhoneLoginActivity(activity, null, true);
                        }
                    }
                }
            }

            @Override
            public void onFail(int HttpCode, String errMsg) {

            }
        });

    }

    // 网络回调结束
    //////////////////////////////

    public interface HttpListener {
        void onSuccess(int code, String dataJson, String errMsg);

        void onFail(int HttpCode, String errMsg);
    }
}
