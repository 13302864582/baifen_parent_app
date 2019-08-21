
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
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.google.gson.Gson;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.api.UserAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.dialog.CustomModifyPhoneDialog;
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
import com.daxiong.fun.view.edittext.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 家长版 注册activity
 *
 * @author Administrator
 */
public class RegisterActivity extends BaseActivity
        implements OnClickListener, LocationChangedListener {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private RelativeLayout back_layout;

    private RelativeLayout bindingInfoLayout;

    private ClearEditText num_et, psw_et, confrim_pwd_et;

    private EditText et_invite_code;

    private Button submit;

    private TextView bindingInfoTV, skipBinding;

    private int toDo = DO_REGISTER;

    private boolean bindingFromLogin;

    private LocationUtils mLocationUtils;

    private UserAPI userApi;


    private String phoneNoStr, pwdStr, confirmPwdStr, inviteCodeStr, provinceStr, cityStr;

    private String mLocation;

    private double mLatitude, mLongitude;

    public static final String DO_TAG = "do_tag";

    public static final int DO_REGISTER = 1;// 注册

    public static final int DO_BIND = 2;// 绑定

    public static final int DO_RESET = 3;// 找回密码

    public static final int DO_FORGET_PASS = 4;//忘记密码

    private String isMust = "";


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            /*    case GlobalContant.LOOPMSG:
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
                    break;*/
            }
        }
    };

    private PhoneLoginModel plm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_phone_regist);
        registerFinishMySelf();
        initView();
        initListener();
        getExtraData();
    }

    private void getExtraData() {
        Intent i = getIntent();
        if (null != i) {
            toDo = i.getIntExtra(DO_TAG, DO_REGISTER);
            bindingFromLogin = i.getBooleanExtra("binding_from_login", false);
            isMust = i.getStringExtra("isMust");
        }

        switch (toDo) {
            case DO_REGISTER:
                bindingInfoLayout.setVisibility(View.GONE);
                setWelearnTitle(R.string.register_text_register);
                submit.setText(R.string.text_next_step);
                break;
            case DO_BIND:
                bindingInfoLayout.setVisibility(View.VISIBLE);
                psw_et.setHint(R.string.text_bind_password_hint);
                if (bindingFromLogin) {
                    bindingInfoTV.setText(R.string.text_binding_info);
                    skipBinding.setVisibility(View.GONE);
                } else {
                    bindingInfoTV.setText(R.string.text_binding_info_first);
                    skipBinding.setVisibility(View.GONE);
                }
                setWelearnTitle(R.string.text_binding_phone);
                submit.setText(R.string.text_binding);
                if (!TextUtils.isEmpty(isMust) && "isMust".equals(isMust)) {
                    back_layout.setVisibility(View.GONE);
                } else {
                    back_layout.setVisibility(View.VISIBLE);
                }
                break;
            case DO_RESET:
                bindingInfoLayout.setVisibility(View.GONE);
                setWelearnTitle(R.string.text_reset_title);
                submit.setText(R.string.text_reset);
                psw_et.setHint(R.string.text_reset_password_hint);
                break;
            case DO_FORGET_PASS://忘记密码
                bindingInfoLayout.setVisibility(View.GONE);
                setWelearnTitle("忘记密码");
                submit.setText(R.string.text_reset);
                psw_et.setHint(R.string.text_reset_password_hint);
                break;
        }

    }

    @Override
    public void initView() {
        super.initView();
        back_layout = (RelativeLayout) findViewById(R.id.back_layout);
        bindingInfoLayout = (RelativeLayout) findViewById(R.id.binding_info_layout);
        bindingInfoTV = (TextView) findViewById(R.id.binding_info_textview);
        skipBinding = (TextView) findViewById(R.id.skip_binding);
        num_et = (ClearEditText) findViewById(R.id.et_phone_no);
        psw_et = (ClearEditText) findViewById(R.id.et_pwd);
        confrim_pwd_et = (ClearEditText) findViewById(R.id.et_confrim_pwd);
        et_invite_code = (EditText) findViewById(R.id.et_invite_code);
        submit = (Button) findViewById(R.id.submit_reg_btn_phone_register);
        setWelearnTitle(getResources().getString(R.string.register_str));
        userApi = new UserAPI();
        mLocationUtils = LocationUtils.getInstance(this);


    }

    @Override
    public void initListener() {
        super.initListener();
        back_layout.setOnClickListener(this);
        skipBinding.setOnClickListener(this);
        submit.setOnClickListener(this);
//        num_et.addTextChangedListener(myEditTextListener);
//        psw_et.addTextChangedListener(myEditTextListener);
//        confrim_pwd_et.addTextChangedListener(myEditTextListener);
//        verCode_et.addTextChangedListener(myEditTextListener);
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
            phoneNoStr = num_et.getText().toString().trim();
            pwdStr = psw_et.getText().toString().trim();
            confirmPwdStr = confrim_pwd_et.getText().toString().trim();
            String code = et_invite_code.getText().toString().trim();
            if (!TextUtils.isEmpty(phoneNoStr) && !TextUtils.isEmpty(pwdStr) && !TextUtils.isEmpty(confirmPwdStr) && !TextUtils.isEmpty(code) && pwdStr.equals(confirmPwdStr)) {
                submit.setBackgroundResource(R.drawable.login_btn_checked);
                submit.setTextColor(Color.parseColor("#ffffffff"));
            } else {
                submit.setBackgroundResource(R.drawable.login_btn_normal);
                submit.setTextColor(Color.parseColor("#80ffffff"));
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        String phoneNum = MySharePerfenceUtil.getInstance().getPhoneNum();
        //num_et.setText(phoneNum);
        // mLocationUtils.startListen(this);
        mLocationUtils.startBDLocation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                // setResult(RESULT_OK);
                finish();
                break;
      /*      case R.id.btn_validate_code:
                MobclickAgent.onEvent(this, "GetSecurityCode");
                if (System.currentTimeMillis() - clickTime < 500) {
                    return;
                }
                clickTime = System.currentTimeMillis();
                num = num_et.getText().toString().trim();
                String telRegex = "[1][34578]\\d{9}";// "[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
                // if (TextUtils.isEmpty(num) ||
                // !num.matches("1[3|4|5|7|8|][0-9]{9}")) {
                // ToastUtils.show("请输入正确的手机号码");
                // return;
                // }
                if (TextUtils.isEmpty(num)) {
                    ToastUtils.show("请输入正确的手机号码");
                    return;
                }
                if (!num.matches(telRegex)) {
                    ToastUtils.show("请输入正确的手机号码");
                    return;
                }
                MySharePerfenceUtil.getInstance().setPhoneNum(num);

                try {
                    JSONObject jobj = new JSONObject();
                    jobj.put("tel", num);
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
                break;*/
            case R.id.submit_reg_btn_phone_register:// 注册操作
                phoneNoStr = num_et.getText().toString().trim();
                pwdStr = psw_et.getText().toString().trim();
                confirmPwdStr = confrim_pwd_et.getText().toString().trim();
                inviteCodeStr = et_invite_code.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNoStr) || !phoneNoStr.matches("1[3|5|4|7|8|][0-9]{9}")) {
                    ToastUtils.show("请输入正确的手机号码");
                    return;
                }
                MySharePerfenceUtil.getInstance().setPhoneNum(phoneNoStr);
                if (TextUtils.isEmpty(pwdStr) || pwdStr.length() < 6 || pwdStr.length() > 14) {
                    ToastUtils.show("请输入6位以上14位以下密码");
                    return;
                }

                if (!confirmPwdStr.equals(pwdStr)) {
                    ToastUtils.show("两次输入的密码不相同");
                    return;
                }
                //bindOrRegisterOrReset(num, confirm_psw, code);
                //showDialogForValidate();
                goNextActivity(phoneNoStr, confirmPwdStr, inviteCodeStr);
                break;

            case R.id.skip_binding:
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    /**
     * 显示发送验证码的dialog
     */
    private void showDialogForValidate() {
        final CustomModifyPhoneDialog midifyPhoneDialog = new CustomModifyPhoneDialog(this, "确认手机号码", "我们将发送验证码到这个号码" + phoneNoStr, "取消", "好");
        midifyPhoneDialog.show();
        midifyPhoneDialog.setClicklistener(new CustomModifyPhoneDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                midifyPhoneDialog.dismiss();
                sendValidateCode();

            }

            @Override
            public void doCancel() {
                midifyPhoneDialog.dismiss();
            }


        });
    }

    public void sendValidateCode() {

        if (System.currentTimeMillis() - clickTime < 500) {
            return;
        }
        clickTime = System.currentTimeMillis();

        try {
            JSONObject jobj = new JSONObject();
            jobj.put("tel", phoneNoStr);
            OkHttpHelper.post(this, "guest", "sendsecuritycode", jobj, new OkHttpHelper.HttpListener() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * 去到下一个界面actiivty
     */
    private void goNextActivity(final String tel, final String pwd, final String inviteCode) {

        try {
            int inviteCodeInt = TextUtils.isEmpty(inviteCode) ? 0 : Integer.parseInt(inviteCode);
            JSONObject json = new JSONObject();
            json.put("tel", tel);
            json.put("code", inviteCodeInt);
            json.put("province", provinceStr);
            json.put("city", cityStr);
            OkHttpHelper.post(this, "guest", "checkinvitationcode", json, new HttpListener() {
                @Override
                public void onSuccess(int code, String dataJson, String errMsg) {
                    int orgid = JsonUtil.getInt(dataJson, "orgid", 0);
                    Intent goNextIntent = new Intent(RegisterActivity.this, RegisterNextActivity.class);
                    goNextIntent.putExtra("tel", tel);
                    goNextIntent.putExtra("pwd", pwd);
                    goNextIntent.putExtra("province", provinceStr);
                    goNextIntent.putExtra("city", cityStr);
                    goNextIntent.putExtra("orgid", orgid);
                    goNextIntent.putExtra("latitude",mLatitude);
                    goNextIntent.putExtra("longitude",mLongitude);
                    goNextIntent.putExtra("location",mLocation);
                    startActivity(goNextIntent);
                }

                @Override
                public void onFail(int HttpCode, String errMsg) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void bindOrRegisterOrReset(final String tel, String pwd, String code) {
        try {
            String module = "user";
            String func = "bindmobile";
            JSONObject jobj = new JSONObject();
            jobj.put("tel", tel);
            jobj.put("password", MD5Util.getMD5String(pwd));
            jobj.put("code", code);
            if (toDo == DO_REGISTER) {
                module = "guest";
                func = "register";
                jobj.put("roleid", GlobalContant.ROLE_ID_PARENTS);
                jobj.put("solekey", PhoneUtils.getInstance().getDeviceUUID());
                if (TextUtils.isEmpty(provinceStr)) {
                    provinceStr = "广东省";
                }
                if (TextUtils.isEmpty(cityStr)) {
                    cityStr = "深圳市";
                }
                jobj.put("province", provinceStr);
                jobj.put("city", cityStr);
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
//                                verCode_et.setText("");
//                                get_ver.setText("重新发送");
//                                get_ver.setClickable(true);
//                                get_ver.setTextColor(getResources().getColor(R.color.color8d8d8d));
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



    public void execLogin() {

        phoneNoStr = num_et.getText().toString().trim();
        pwdStr = psw_et.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNoStr)/* || !num.matches("1[3|5|7|8|][0-9]{9}") */) {
            ToastUtils.show(R.string.text_input_right_phone_num);
            return;
        }
        MySharePerfenceUtil.getInstance().setPhoneNum(phoneNoStr);
        if (TextUtils.isEmpty(pwdStr)) {
            ToastUtils.show(R.string.text_input_password);
            return;
        }
        showDialog(getString(R.string.text_logining));
//      mHandler.sendEmptyMessageDelayed(GlobalContant.CLOSEDIALOG, 35000);

        plm = new PhoneLoginModel();
        plm.setCount(phoneNoStr);
        plm.setPassword(MD5Util.getMD5String(pwdStr));


        userApi.execLogin(requestQueue, plm, this, RequestConstant.REGISTER_LOGIN_CODE);

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
                RegisterActivity.this.finish();
            }

        }

    };

    @Override
    public void onLocationChanged(BDLocation bdLocation, String province, String city) {
        LogUtils.d(TAG, "p=" + province + ",c=" + city);
        if (bdLocation!=null&&!TextUtils.isEmpty(province) && !TextUtils.isEmpty(city)) {
            this.mLatitude = bdLocation.getLatitude();
            this.mLongitude = bdLocation.getLongitude();
            this.mLocation = bdLocation.getAddrStr();

            this.provinceStr = province;
            this.cityStr = city;
            // mLocationUtils.stopListen();
        }
        mLocationUtils.stopBDLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        // mLocationUtils.stopListen();
        mLocationUtils.stopBDLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationUtils.stopBDLocation();
        isShowDialog = false;
        closeDialog();
        unregisterReceiver(finishReceiver);
    }

}
