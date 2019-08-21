
package com.daxiong.fun.function.account;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.api.UserAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.PhoneLoginModel;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LocationUtils;
import com.daxiong.fun.util.LocationUtils.LocationChangedListener;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MD5Util;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.PhoneUtils;
import com.daxiong.fun.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 家长版 注册下一步activity
 *
 * @author Administrator
 */
public class RegisterNextActivity extends BaseActivity
        implements OnClickListener, LocationChangedListener {

    private static final String TAG = RegisterNextActivity.class.getSimpleName();


    private EditText et_validate_code;
    private Button get_ver, btn_submit;

    private LocationUtils mLocationUtils;
    private UserAPI userApi;
    private RelativeLayout back_layout;

    public static final String DO_TAG = "do_tag";

    private int toDo = DO_REGISTER;

    public static final int DO_REGISTER = 1;// 注册

    public static final int DO_BIND = 2;// 绑定

    public static final int DO_RESET = 3;// 找回密码

    public static final int DO_FORGET_PASS = 4;//忘记密码

    private String phoneNo, pwd, province, city;
    private String mLocation;
    private double mLatitude, mLongitude;

    private PhoneLoginModel plm;

    private int orgid=0;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalContant.LOOPMSG:
                    Message msg1 = Message.obtain();
                    msg1.what = GlobalContant.LOOPMSG;
                    int time = (Integer) msg.obj - 1;
                    msg1.obj = time;
                    int de = 0;
                    if (time >= de) {
                        get_ver.setText(time + "s后重新发送");
                        get_ver.setTextColor(getResources().getColor(R.color.color8d8d8d));
                        mHandler.sendMessageDelayed(msg1, 1000);
                    } else {
                        get_ver.setText("重新发送");
                        get_ver.setTextColor(getResources().getColor(R.color.color8d8d8d));
                        get_ver.setClickable(true);
                        //  get_ver.setBackgroundResource(R.drawable.bg_get_verification_btn_selector);
                    }
                    break;
            }
        }
    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_regist_next_activity);
        registerFinishMySelf();
        initView();
        initListener();
        getExtraData();
        onClick(get_ver);
    }

    private void getExtraData() {
        Intent getExtraIntent = getIntent();
        if (null != getExtraIntent) {
            phoneNo = getExtraIntent.getStringExtra("tel");
            pwd = getExtraIntent.getStringExtra("pwd");
            province=getExtraIntent.getStringExtra("province");
            city=getExtraIntent.getStringExtra("city");
            orgid=getExtraIntent.getIntExtra("orgid",0);
            mLatitude=getExtraIntent.getDoubleExtra("latitude",0.0d);
            mLongitude=getExtraIntent.getDoubleExtra("longitude",0.0d);
            mLocation=getExtraIntent.getStringExtra("location");

        }


    }

    @Override
    public void initView() {
        super.initView();
        back_layout = (RelativeLayout) findViewById(R.id.back_layout);
        et_validate_code = (EditText) findViewById(R.id.et_validate_code);
        get_ver = (Button) findViewById(R.id.btn_validate_code);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        setWelearnTitle(R.string.register_text_register);
        userApi = new UserAPI();
        mLocationUtils = LocationUtils.getInstance(this);


    }

    @Override
    public void initListener() {
        super.initListener();
        back_layout.setOnClickListener(this);
        get_ver.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
//      verCode_et.addTextChangedListener(myEditTextListener);
    }

    TextWatcher myEditTextListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            String code = et_validate_code.getText().toString().trim();
            if (!TextUtils.isEmpty(phoneNo) && !TextUtils.isEmpty(pwd)) {
                btn_submit.setBackgroundResource(R.drawable.login_btn_checked);
                btn_submit.setTextColor(Color.parseColor("#ffffffff"));
            } else {
                btn_submit.setBackgroundResource(R.drawable.login_btn_normal);
                btn_submit.setTextColor(Color.parseColor("#80ffffff"));
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        String phoneNum = MySharePerfenceUtil.getInstance().getPhoneNum();
        // mLocationUtils.startListen(this);
        //mLocationUtils.startBDLocation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.btn_validate_code:
                MobclickAgent.onEvent(this, "GetSecurityCode");
                if (System.currentTimeMillis() - clickTime < 500) {
                    return;
                }
                clickTime = System.currentTimeMillis();

                try {
                    JSONObject jobj = new JSONObject();
                    jobj.put("tel", phoneNo);
                    OkHttpHelper.post(this, "guest", "sendsecuritycode", jobj, new HttpListener() {
                        @Override
                        public void onSuccess(int code, String dataJson, String errMsg) {
                            if (code == 0) {
                                ToastUtils.show(R.string.text_get_verification_code_success);
                            } else {
                                if (!TextUtils.isEmpty(errMsg)) {
                                    ToastUtils.show(errMsg);
                                } else {
                                    ToastUtils.show(R.string.text_get_verification_code_fail);
                                }
                            }
                        }

                        @Override
                        public void onFail(int HttpCode, String errMsg) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                get_ver.setClickable(false);
                //get_ver.setBackgroundResource(R.drawable.ic_get_verificate_bt_pressed);
                Message msg = Message.obtain();
                msg.what = GlobalContant.LOOPMSG;
                msg.obj = 60;
                get_ver.setText("60s后重新发送");
                get_ver.setTextColor(getResources().getColor(R.color.color8d8d8d));
                mHandler.sendMessageDelayed(msg, 1000);
                break;
            case R.id.btn_submit:// 注册操作
                String validateCode = et_validate_code.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNo) || !phoneNo.matches("1[3|5|4|7|8|][0-9]{9}")) {
                    ToastUtils.show("请输入正确的手机号码");
                    return;
                }
                MySharePerfenceUtil.getInstance().setPhoneNum(phoneNo);
                if (TextUtils.isEmpty(pwd) || pwd.length() < 6 || pwd.length() > 14) {
                    ToastUtils.show("请输入6位以上14位以下密码");
                    return;
                }

                if (TextUtils.isEmpty(validateCode)) {
                    ToastUtils.show("请输入验证码");
                    return;
                }
                bindOrRegisterOrReset(phoneNo, pwd, orgid,validateCode);
                break;

            case R.id.skip_binding:
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    private void bindOrRegisterOrReset(final String tel, String pwd, int   orgid, String validateCode) {
        try {
            String module = "user";
            String func = "bindmobile";
            JSONObject jobj = new JSONObject();
            jobj.put("tel", tel);
            jobj.put("password", MD5Util.getMD5String(pwd));
            jobj.put("code", validateCode);
            jobj.put("orgid", orgid);

            if (toDo == DO_REGISTER) {
                module = "guest";
                func = "register";
                jobj.put("roleid", GlobalContant.ROLE_ID_PARENTS);
                jobj.put("solekey", PhoneUtils.getInstance().getDeviceUUID());
                if (TextUtils.isEmpty(province)) {
                    province = "广东省";
                }
                if (TextUtils.isEmpty(city)) {
                    city = "深圳市";
                }
                jobj.put("province", province);
                jobj.put("city", city);
            } else if (toDo == DO_BIND) {
                module = "user";
                func = "bindmobile";
            } else if (toDo == DO_RESET | toDo == DO_FORGET_PASS) {
                module = "guest";
                func = "resetpassword";
            }
            OkHttpHelper.post(this, module, func, jobj, new HttpListener() {
                @Override
                public void onSuccess(int code, String dataJson, String errMsg) {
                    if (code == 0) {
                        if (toDo == DO_BIND) {
                            uMengEvent("bindingphone");
                            ToastUtils.show(R.string.text_binding_success);
                            Intent intent = new Intent();
                            intent.putExtra("tel", tel);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else if (toDo == DO_REGISTER) {
                            ToastUtils.show(R.string.text_register_success);
                            // 执行登录操作
                            execLogin();
                        } else if (toDo == DO_RESET | toDo == DO_FORGET_PASS) {
                            ToastUtils.show(R.string.text_reset_password_success);
                            finish();
                        }

                    } else {
                        if (!TextUtils.isEmpty(errMsg)) {
                            ToastUtils.show(errMsg);
                            if (errMsg.contains("已绑定")) {
                                et_validate_code.setText("");
                                get_ver.setText("重新发送");
                                get_ver.setClickable(true);
                                get_ver.setTextColor(getResources().getColor(R.color.color8d8d8d));
//                                get_ver.setBackgroundResource(
//                                        R.drawable.bg_get_verification_btn_selector);
                            }
                        } else {
                        }
                    }
                }

                @Override
                public void onFail(int HttpCode, String errMsg) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(BDLocation location, String province, String city) {
        LogUtils.d(TAG, "p=" + province + ",c=" + city);
        if (!TextUtils.isEmpty(province) && !TextUtils.isEmpty(city)) {
            this.province = province;
            this.city = city;
            // mLocationUtils.stopListen();
            mLocationUtils.stopBDLocation();
        }
    }

    public void execLogin() {
        if (TextUtils.isEmpty(phoneNo)/* || !num.matches("1[3|5|7|8|][0-9]{9}") */) {
            ToastUtils.show(R.string.text_input_right_phone_num);
            return;
        }
        MySharePerfenceUtil.getInstance().setPhoneNum(phoneNo);
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.show(R.string.text_input_password);
            return;
        }
        showDialog(getString(R.string.text_logining));
//      mHandler.sendEmptyMessageDelayed(GlobalContant.CLOSEDIALOG, 35000);

        plm = new PhoneLoginModel();
        plm.setCount(phoneNo);
        plm.setPassword(MD5Util.getMD5String(pwd));
        plm.setLatitude(mLatitude);
        plm.setLongitude(mLongitude);
        plm.setLocation(mLocation);

        userApi.execLogin(requestQueue, plm, this, RequestConstant.REGISTER_LOGIN_CODE);

    }

    @Override
    public void onPause() {
        super.onPause();
        // mLocationUtils.stopListen();
       // mLocationUtils.stopBDLocation();
    }

    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        int flag = ((Integer) param[0]).intValue();
        switch (flag) {
            case RequestConstant.REGISTER_LOGIN_CODE:// 执行登录
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        try {
                            String dataJson = JsonUtil.getString(datas, "Data", "");
                            closeDialog();
                            if (!TextUtils.isEmpty(dataJson)) {
                                JSONObject jobj = new JSONObject(dataJson);
                                String tokenId = jobj.getString("tokenid");
                                int roleid = jobj.getInt("roleid");
                                if (roleid != GlobalContant.ROLE_ID_PARENTS) {
                                    closeDialogHelp();
                                    ToastUtils.show(R.string.you_are_not_a_parents);
                                } else {
                                    UserInfoModel uInfo = new Gson().fromJson(dataJson,
                                            UserInfoModel.class);

                                    DBHelper.getInstance().getWeLearnDB()
                                            .insertOrUpdatetUserInfo(uInfo);
                                    MySharePerfenceUtil.getInstance().savePhoneLoginInfo(plm);
                                    MySharePerfenceUtil.getInstance()
                                            .setUserRoleId(uInfo.getRoleid());

                                    MySharePerfenceUtil.getInstance().setWelearnTokenId(tokenId);

                                    if (GlobalVariable.loginActivity != null) {
                                        GlobalVariable.loginActivity.finish();
                                        GlobalVariable.loginActivity = null;
                                    }

                                    // 打开websocket连接
                                    IntentManager.startWService(MyApplication.getContext());
                                    MySharePerfenceUtil.getInstance().setWelcomeDialog(1);
                                    IntentManager.goToMainView(this);

                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            closeDialog();
//                            if (!TextUtils.isEmpty(msg)) {
//                                ToastUtils.show(msg);
//                            }
                        }
                    } else {
                        closeDialog();
//                        if (!TextUtils.isEmpty(msg)) {
//                            ToastUtils.show(msg);
//                        }
                    }

                }
                break;

        }

    }


    public void registerFinishMySelf() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.action.finish_myself");
        registerReceiver(finishReceiver, filter);
    }

    BroadcastReceiver finishReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("com.action.finish_myself".equals(action)) {
                RegisterNextActivity.this.finish();
            }

        }

    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        isShowDialog = false;
        closeDialog();
        unregisterReceiver(finishReceiver);
    }

}
