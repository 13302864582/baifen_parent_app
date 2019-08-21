
package com.daxiong.fun.function.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.daxiong.fun.R;
import com.daxiong.fun.api.UserAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.model.PhoneLoginModel;
import com.daxiong.fun.util.LocationUtils;
import com.daxiong.fun.util.LocationUtils.LocationChangedListener;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MD5Util;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.edittext.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 忘记密码next
 *
 * @author sky
 */
public class ForgetPasswordNextActivity extends BaseActivity implements OnClickListener, LocationChangedListener {

    private static final String TAG = ForgetPasswordNextActivity.class.getSimpleName();

    private RelativeLayout back_layout;
    private ClearEditText et_pwd, et_newpwd;
    private Button submit;
    private String pwdStr, pwdNewStr, province, city;
    private ImageView iv_show_pwd;
    private boolean isHidden = false;
    private PhoneLoginModel plm;
    private LocationUtils mLocationUtils;
    private UserAPI userApi;
    String phoneNo = "";
    String validateCode = "";

    private boolean flag1 = false;
    private boolean flag2 = false;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_pwd_next_activity);
        initView();
        initListener();
        getExtraData();

    }

    private void getExtraData() {
        Intent xIntent = getIntent();
        if (null != xIntent) {
            phoneNo = xIntent.getStringExtra("phoneno");
            validateCode = xIntent.getStringExtra("validatecode");
        }

    }

    @Override
    public void initView() {
        super.initView();
        back_layout = (RelativeLayout) findViewById(R.id.back_layout);
        et_pwd = (ClearEditText) findViewById(R.id.et_pwd);
        et_newpwd = (ClearEditText) findViewById(R.id.et_newpwd);
        iv_show_pwd = (ImageView) this.findViewById(R.id.iv_show_pwd);
        submit = (Button) findViewById(R.id.submit_reg_btn_phone_register);
        setWelearnTitle(getResources().getString(R.string.text_reset_title));
        userApi = new UserAPI();
        mLocationUtils = LocationUtils.getInstance(this);

    }

    @Override
    public void initListener() {
        super.initListener();
        back_layout.setOnClickListener(this);
        submit.setOnClickListener(this);
        iv_show_pwd.setOnClickListener(this);

        et_pwd.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    flag1 = true;
                } else {
                    flag1 = false;
                }
                setLoginButton();
            }
        });

        et_newpwd.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    flag2 = true;
                } else {
                    flag2 = false;
                }
                setLoginButton();
            }
        });


    }


    public void setLoginButton() {
        if (flag1 && flag2) {
            submit.setBackgroundResource(R.drawable.login_btn_checked);
            submit.setTextColor(Color.parseColor("#ffffffff"));
        } else {
            submit.setBackgroundResource(R.drawable.login_btn_normal);
            submit.setTextColor(Color.parseColor("#80ffffff"));
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        // mLocationUtils.startListen(this);
        //mLocationUtils.startBDLocation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout://返回操作
                finish();
                break;
            case R.id.submit_reg_btn_phone_register://忘记密码操作
                pwdStr = et_pwd.getText().toString().trim();
                pwdNewStr = et_newpwd.getText().toString().trim();
                MySharePerfenceUtil.getInstance().setPhoneNum(phoneNo);

                if (TextUtils.isEmpty(validateCode)) {
                    ToastUtils.show("请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(pwdStr) || pwdStr.length() < 6 || pwdStr.length() > 14) {
                    ToastUtils.show("请输入6位以上14位以下密码");
                    return;
                }
                if (!pwdStr.equals(pwdNewStr)){
                    ToastUtils.show("两次输入密码不相同,请输入正确的密码");
                    return;
                }
                doResetPwd(phoneNo, pwdNewStr, validateCode);
                break;

            case R.id.iv_show_pwd://显示密码或者隐藏密码
                if (isHidden) {
                    //设置EditText文本为可见的
                    iv_show_pwd.setBackgroundResource(R.drawable.icon_clos_eye);
                    et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //设置EditText文本为隐藏的
                    iv_show_pwd.setBackgroundResource(R.drawable.icon_open_eye);
                    et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                isHidden = !isHidden;
                et_pwd.postInvalidate();
                //切换后将EditText光标置于末尾
                CharSequence charSequence = et_pwd.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
                break;
        }
    }

    /**
     * 重置密码
     *
     * @param tel
     * @param pwd
     * @param code
     */
    private void doResetPwd(final String tel, String pwd, String code) {
        try {
            String module = "user";
            String func = "bindmobile";

            JSONObject jobj = new JSONObject();
            jobj.put("tel", tel);
            jobj.put("password", MD5Util.getMD5String(pwd));
            jobj.put("code", code);
            showDialog("请稍候...");
            OkHttpHelper.post(this, "guest", "resetpassword", jobj, new HttpListener() {
                @Override
                public void onSuccess(int code, String dataJson, String errMsg) {
                    closeDialog();
                    if (code == 0) {
                        ToastUtils.show(R.string.text_reset_password_success);
                        Intent intent=new Intent();
                        setResult(RESULT_OK,intent);
                        finish();
                    } else {
                        if (!TextUtils.isEmpty(errMsg)) {
                            ToastUtils.show(errMsg);
                        }
                    }
                }

                @Override
                public void onFail(int HttpCode, String errMsg) {
                    closeDialog();
                    if (TextUtils.isEmpty(errMsg)) {
                        ToastUtils.show(errMsg);
                    }

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


    @Override
    public void onPause() {
        super.onPause();
        // mLocationUtils.stopListen();
        mLocationUtils.stopBDLocation();
    }


    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationUtils.stopBDLocation();
    }
}
