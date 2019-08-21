
package com.daxiong.fun.function.account;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
 * 以前的注册界面  废弃掉了
 * @author Administrator
 *
 */
public class PhoneRegisterActivity extends BaseActivity
        implements OnClickListener, LocationChangedListener {

    private static final String TAG = PhoneRegisterActivity.class.getSimpleName();

    public static final String DO_TAG = "do_tag";

    public static final int DO_REGISTER = 1;// 注册

    public static final int DO_BIND = 2;// 绑定

    public static final int DO_RESET = 3;// 找回密码
    
    public static final int DO_FORGET_PASS=4;//忘记密码

    private RelativeLayout bindingInfoLayout;

    private EditText num_et;

    private EditText psw_et;

    private EditText verCode_et;

    private Button get_ver, submit;

    private TextView bindingInfoTV, skipBinding;

    private ImageView deletePhoneIV;

    private int toDo = DO_REGISTER;

    private boolean bindingFromLogin;

    private String num, psw, province, city;

    private LocationUtils mLocationUtils;

    private UserAPI userApi;

    private RelativeLayout back_layout;

    private String isMust = "";

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalContant.LOOPMSG:
                    Message msg1 = Message.obtain();
                    msg1.what = GlobalContant.LOOPMSG;
                    int time = (Integer)msg.obj - 1;
                    msg1.obj = time;
                    int de = 0;
                    if (time >= de) {
                        get_ver.setText(time + "");
                        mHandler.sendMessageDelayed(msg1, 1000);
                    } else {
                        get_ver.setText("重新发送");
                        get_ver.setClickable(true);
                        get_ver.setBackgroundResource(R.drawable.bg_get_verification_btn_selector);
                    }
                    break;
            }
        }
    };

    private PhoneLoginModel plm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_phone_register);
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
                submit.setText(R.string.text_regist_confirm);
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
            case  DO_FORGET_PASS://忘记密码
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
        back_layout = (RelativeLayout)findViewById(R.id.back_layout);

        bindingInfoLayout = (RelativeLayout)findViewById(R.id.binding_info_layout);
        bindingInfoTV = (TextView)findViewById(R.id.binding_info_textview);
        skipBinding = (TextView)findViewById(R.id.skip_binding);
        num_et = (EditText)findViewById(R.id.phone_num_et_phone_register);
        psw_et = (EditText)findViewById(R.id.phone_psw_et_phone_register);
        verCode_et = (EditText)findViewById(R.id.ver_code_et_phone_register);
        get_ver = (Button)findViewById(R.id.get_verificationcode_btn_phone_register);
        submit = (Button)findViewById(R.id.submit_reg_btn_phone_register);
        deletePhoneIV = (ImageView)findViewById(R.id.delete_phone_iv);
        userApi = new UserAPI();
        mLocationUtils = LocationUtils.getInstance(this);

    }

    @Override
    public void initListener() {
        super.initListener();
        back_layout.setOnClickListener(this);
        skipBinding.setOnClickListener(this);
        get_ver.setOnClickListener(this);
        submit.setOnClickListener(this);
        deletePhoneIV.setOnClickListener(this);

        num_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    deletePhoneIV.setVisibility(View.VISIBLE);
                } else {
                    deletePhoneIV.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        String phoneNum = MySharePerfenceUtil.getInstance().getPhoneNum();
        num_et.setText(phoneNum);
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
            case R.id.get_verificationcode_btn_phone_register:
                MobclickAgent.onEvent(this,"GetSecurityCode");
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
                        public void onFail(int HttpCode,String errMsg) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                get_ver.setClickable(false);
                get_ver.setBackgroundResource(R.drawable.ic_get_verificate_bt_pressed);
                Message msg = Message.obtain();
                msg.what = GlobalContant.LOOPMSG;
                msg.obj = 60;
                get_ver.setText("60");
                mHandler.sendMessageDelayed(msg, 1000);
                break;
            case R.id.submit_reg_btn_phone_register:// 注册操作
                num = num_et.getText().toString().trim();
                psw = psw_et.getText().toString().trim();
                String code = verCode_et.getText().toString().trim();
                if (TextUtils.isEmpty(num) || !num.matches("1[3|5|4|7|8|][0-9]{9}")) {
                    ToastUtils.show("请输入正确的手机号码");
                    return;
                }
                MySharePerfenceUtil.getInstance().setPhoneNum(num);
                if (TextUtils.isEmpty(psw) || psw.length() < 6||psw.length() > 14) {
                    ToastUtils.show("请输入6位以上14位以下密码");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    ToastUtils.show("请输入验证码");
                    return;
                }
                bindOrRegisterOrReset(num, psw, code);
                break;
            case R.id.delete_phone_iv:
                if (null != num_et) {
                    num_et.setText("");
                }
                break;
            case R.id.skip_binding:
                setResult(RESULT_OK);
                finish();
                break;
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
                if (TextUtils.isEmpty(province)) {
                    province="广东省";
                }
                if (TextUtils.isEmpty(city)) {
                    city="深圳市";
                }
                jobj.put("province", province);
                jobj.put("city", city);
            } else if (toDo == DO_BIND) {
                module = "user";
                func = "bindmobile";
            } else if (toDo == DO_RESET|toDo==DO_FORGET_PASS) {
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
                        } else if (toDo == DO_RESET|toDo==DO_FORGET_PASS) {
                            ToastUtils.show(R.string.text_reset_password_success);
                            finish();
                        }
                       
                    } else {
                        if (!TextUtils.isEmpty(errMsg)) {
                            ToastUtils.show(errMsg);
                            if (errMsg.contains("已绑定")) {
                                verCode_et.setText("");
                                get_ver.setText("重新发送");
                                get_ver.setClickable(true);
                                get_ver.setBackgroundResource(
                                        R.drawable.bg_get_verification_btn_selector);
                            }
                        } else {
                        }
                    }
                }

                @Override
                public void onFail(int HttpCode,String errMsg) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(BDLocation location,String province, String city) {
        LogUtils.d(TAG, "p=" + province + ",c=" + city);
        if (!TextUtils.isEmpty(province) && !TextUtils.isEmpty(city)) {
            this.province = province;
            this.city = city;
            // mLocationUtils.stopListen();
            mLocationUtils.stopBDLocation();
        }
    }

    public void execLogin() {

        num = num_et.getText().toString().trim();
        psw = psw_et.getText().toString().trim();
        if (TextUtils.isEmpty(num)/* || !num.matches("1[3|5|7|8|][0-9]{9}") */) {
            ToastUtils.show(R.string.text_input_right_phone_num);
            return;
        }
        MySharePerfenceUtil.getInstance().setPhoneNum(num);
        if (TextUtils.isEmpty(psw)) {
            ToastUtils.show(R.string.text_input_password);
            return;
        }
        showDialog(getString(R.string.text_logining));
//      mHandler.sendEmptyMessageDelayed(GlobalContant.CLOSEDIALOG, 35000);

        plm = new PhoneLoginModel();
        plm.setCount(num);
        plm.setPassword(MD5Util.getMD5String(psw)); 
      
        userApi.execLogin(requestQueue, plm, this, RequestConstant.REGISTER_LOGIN_CODE);

    }

    @Override
    public void onPause() {
        super.onPause();
        // mLocationUtils.stopListen();
        mLocationUtils.stopBDLocation();
    }

    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        int flag = ((Integer)param[0]).intValue();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        isShowDialog = false;
        closeDialog();
    }

}
