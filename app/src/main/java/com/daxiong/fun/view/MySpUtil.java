
package com.daxiong.fun.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.config.AppConfig;

public class MySpUtil {

    // private static final String PAY_HOST =
    // "http://pay.test.welearn.com:9301/";
    private static final String PAY_HOST = "http://218.244.151.195:9301/";

    private SharedPreferences mSp;

    private Editor mEditor;

    private static final String SP_NAME = "debugSPV";

    private MySpUtil() {
        mSp = MyApplication.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    private static class MySpUtilHolder {
        private static final MySpUtil INSANCE = new MySpUtil();
    }

    public static MySpUtil getInstance() {
        return MySpUtilHolder.INSANCE;
    }

    public void setUPDATEURL(String UPDATEURL) {
        mEditor = mSp.edit();
        if (!TextUtils.isEmpty(UPDATEURL)) {
            mEditor.putString("UPDATEURL", UPDATEURL);
        }
        mEditor.apply();
    }

    public String getUPDATEURL() {
        return mSp.getString("UPDATEURL", "http://218.244.151.195/app_version.php?os=2");
    }

    public void setWSURI(String WSURI) {
        mEditor = mSp.edit();
        if (!TextUtils.isEmpty(WSURI)) {
            mEditor.putString("WSURI", WSURI);
        }
        mEditor.apply();
    }

    public String getWSURI() {
        return mSp.getString("WSURI", "ws://172.16.1.13:9001/ws");
        // return mSp.getString("WSURI", "ws://218.244.151.195:9001/ws");
    }

    public void setHTTPURI(String HTTPURI) {
        mEditor = mSp.edit();
        if (!TextUtils.isEmpty(HTTPURI)) {
            mEditor.putString("HTTPURI", HTTPURI);
        }
        mEditor.apply();
    }

    public String getHTTPURI() {
        return mSp.getString("HTTPURI", "http://192.168.1.100:7001/http/mail");
        // return mSp.getString("HTTPURI",
        // "http://218.244.151.195:9001/http/mail");
    }

    public void setALIURL(String ALIURL) {
        mEditor = mSp.edit();
        if (!TextUtils.isEmpty(ALIURL)) {
            mEditor.putString("ALIURL", ALIURL);
        }
        mEditor.apply();
    }

    public String getALIURL() {
        return mSp.getString("ALIURL", PAY_HOST + "welearn/alipay/");
    }

    public void setYEEURL(String YEEURL) {
        mEditor = mSp.edit();
        if (!TextUtils.isEmpty(YEEURL)) {
            mEditor.putString("YEEURL", YEEURL);
        }
        mEditor.apply();
    }

    public String getYEEURL() {
        return mSp.getString("YEEURL", PAY_HOST + "welearn/yeepay/");
    }

    public void setCARDURL(String CARDURL) {
        mEditor = mSp.edit();
        if (!TextUtils.isEmpty(CARDURL)) {
            mEditor.putString("CARDURL", CARDURL);
        }
        mEditor.apply();
    }

    public String getCARDURL() {
        return mSp.getString("CARDURL", PAY_HOST + "welearn/card_yeepay/");
    }

    public void setAnswerData(long answer_id, String answerData) {
        mEditor = mSp.edit();
        mEditor.putLong("answer_id", answer_id);
        mEditor.putString("answerData", answerData);
        mEditor.apply();
    }

    public String getAnswerData(long answer_id) {
        boolean flag = mSp.getLong("answer_id", 0) == answer_id;
        if (flag) {
            return mSp.getString("answerData", "");
        } else {
            return "";
        }

    }

    // public void setWSURL_HTTP(String WSURL_HTTP) {
    // mEditor = mSp.edit();
    // if (!TextUtils.isEmpty(WSURL_HTTP)) {
    // mEditor.putString("WSURL_HTTP", WSURL_HTTP);
    // }
    // mEditor.apply();
    // }
    //
    // public String getWSURL_HTTP() {
    // return mSp.getString("WSURL_HTTP", "http://172.16.1.20:82/api/");
    //// return mSp.getString("WSURL_HTTP", "http://218.244.151.195:82/api/");
    // }
    //
    //
    // public String getPayConfig() {
    // return mSp.getString("WSURL_HTTP",
    // "http://172.16.1.13:9301/welearn/payment/configure");
    //// return mSp.getString("WSURL_HTTP",
    // "http://218.244.151.195:9301/welearn/payment/configure");
    // }

    public void setGOTP(String GOIP) {
        mEditor = mSp.edit();
        if (!TextUtils.isEmpty(GOIP)) {
            mEditor.putString("GOIP", GOIP);
        }
        mEditor.apply();
    }

    public void setPYTHONTP(String PYTHONTP) {
        mEditor = mSp.edit();
        if (!TextUtils.isEmpty(PYTHONTP)) {
            mEditor.putString("PYTHONTP", PYTHONTP);
        }
        mEditor.apply();
    }

    public String getGOTP() {
        if (AppConfig.IS_ONLINE) {//线上测试
            return mSp.getString("GOIP",
                    MyApplication.getContext().getResources().getString(R.string.goip_120_text));
        } else {//测试
            return mSp.getString("GOIP",
                    MyApplication.getContext().getResources().getString(R.string.goip_172_text));
        }
    }

    public String getPYTHONTP() {
        if (AppConfig.IS_ONLINE) {//线上测试
            return mSp.getString("PYTHONTP",
                    MyApplication.getContext().getResources().getString(R.string.pyip_120_text));
        } else {//测试
            return mSp.getString("PYTHONTP",
                    MyApplication.getContext().getResources().getString(R.string.pyip_172_text));
        }
    }

}
