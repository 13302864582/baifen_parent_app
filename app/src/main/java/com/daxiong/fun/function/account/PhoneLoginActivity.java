package com.daxiong.fun.function.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.constant.ResponseCmdConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.PhoneLoginModel;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.DebugActvity;
import com.daxiong.fun.util.LocationUtils;
import com.daxiong.fun.util.MD5Util;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.AndroidBug5497Workaround;
import com.daxiong.fun.view.edittext.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;


/**
 * 登录Activity
 *
 * @author: sky
 */

public class PhoneLoginActivity extends BaseActivity implements OnClickListener, HttpListener,LocationUtils.LocationChangedListener {

    public static final String TAG = PhoneLoginActivity.class.getSimpleName();


    private Button nonNum;

    private ClearEditText et_phoneno;

    private ClearEditText et_pwd;

    private Button login_btn;

    private ImageView deletePhoneIV, deletePswIV;

    private String num, psw;

    private PhoneLoginModel plm;

    public static final int REQUEST_CODE_BIND = 0;

    private LinearLayout layout_del_phone_num, layout_del_phone_psw;

    private TextView btn_modify_ip, btn_service_tel;
    private boolean flag1 = false;
    private boolean flag2 = false;
    private LocationUtils mLocationUtils;
    private String mLocation;
    private double mLatitude, mLongitude;





    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalContant.CLOSEDIALOG:
                    if (isShowDialog) {
                        isShowDialog = false;
                        closeDialog();
                        ToastUtils.show(R.string.text_login_timeout);
                    }
                    break;

                // case GlobalContant.TOAST_IN_THREAD:
                // break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_phone_login);
        ButterKnife.bind(this);
        initView();
        initListener();

    }

    @Override
    public void initView() {
        super.initView();
        AndroidBug5497Workaround.assistActivity(this);
        mLocationUtils = LocationUtils.getInstance(this);
        findViewById(R.id.back_layout).setVisibility(View.GONE);
        findViewById(R.id.back_layout).setOnClickListener(this);
        setWelearnTitle(R.string.text_login);
        et_phoneno = (ClearEditText) findViewById(R.id.et_phoneno);
        et_phoneno.addTextChangedListener(new TextWatcher() {
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

                //setLoginButton();

            }
        });


        et_pwd = (ClearEditText) findViewById(R.id.et_pwd);
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
                    flag2 = true;
                } else {
                    flag2 = false;
                }

                //setLoginButton();
            }
        });
        login_btn = (Button) findViewById(R.id.phone_login_btn_phonelogin);
        nonNum = (Button) findViewById(R.id.non_num_tv_phonelogin);
        login_btn.setOnClickListener(this);
        nonNum.setOnClickListener(this);
        TextView losPsw = (TextView) findViewById(R.id.los_psw_tv_phonelogin);
        losPsw.setOnClickListener(this);
        SpannableString spStr = new SpannableString(getResources().getString(R.string.login_forget_pwd));

        // 设置字体前景色
        spStr.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.login_forget_pwd_textcolor)), 0, spStr.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色

        // 设置字体背景色
        // spStr.setSpan(new BackgroundColorSpan(Color.parseColor("#57be6a")),
        // 0, spStr.length(),
        // Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置背景色

        spStr.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false); // 设置下划线
            }

            @Override
            public void onClick(View widget) {
                Intent i = new Intent(PhoneLoginActivity.this, ForgetPasswordActivity.class);
                i.putExtra(ForgetPasswordActivity.DO_TAG, ForgetPasswordActivity.DO_RESET);
                startActivity(i);
            }
        }, 0, 0, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        losPsw.setHighlightColor(Color.TRANSPARENT); // 设置点击后的颜色为透明，否则会一直出现高亮
        losPsw.append(spStr);
        losPsw.setMovementMethod(LinkMovementMethod.getInstance());// 开始响应点击事件

        btn_modify_ip = (TextView) this.findViewById(R.id.btn_modify_ip);
        btn_service_tel = (TextView) this.findViewById(R.id.btn_service_tel);

        if (AppConfig.IS_DEBUG) {
            btn_modify_ip.setVisibility(View.VISIBLE);
            btn_service_tel.setVisibility(View.GONE);
        } else {
            btn_service_tel.setVisibility(View.VISIBLE);
            btn_modify_ip.setVisibility(View.GONE);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        btn_modify_ip.setOnClickListener(this);
        btn_service_tel.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        mLocationUtils.startBDLocation();
        String phoneNum = MySharePerfenceUtil.getInstance().getPhoneNum();
        et_phoneno.setText(phoneNum);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout://返回操作
                finish();
                break;
            case R.id.phone_login_btn_phonelogin://登录操作
                MobclickAgent.onEvent(this, "TelLogin");
                if (System.currentTimeMillis() - clickTime < 500) {
                    return;
                }
                clickTime = System.currentTimeMillis();
                num = et_phoneno.getText().toString().trim();
                psw = et_pwd.getText().toString().trim();
                if (TextUtils
                        .isEmpty(num)/* || !num.matches("1[3|5|7|8|][0-9]{9}") */) {
                    ToastUtils.show(R.string.text_input_right_phone_num);
                    return;
                }
                MySharePerfenceUtil.getInstance().setPhoneNum(num);
                if (TextUtils.isEmpty(psw)) {
                    ToastUtils.show(R.string.text_input_password);
                    return;
                }
                showDialog(getString(R.string.text_logining));
                isShowDialog = true;
                // mHandler.sendEmptyMessageDelayed(GlobalContant.CLOSEDIALOG,
                // 35000);

                loginNewServer();

                break;
            case R.id.non_num_tv_phonelogin://注册操作
                MobclickAgent.onEvent(this, "TelRegister");
                IntentManager.goToPhoneRegActivity(this, null, false);
                break;
            case R.id.los_psw_tv_phonelogin:// 忘记密码
                MobclickAgent.onEvent(this, "ResetPassword");
                Intent i = new Intent(PhoneLoginActivity.this, ForgetPasswordActivity.class);
                i.putExtra(ForgetPasswordActivity.DO_TAG, ForgetPasswordActivity.DO_FORGET_PASS);
                startActivity(i);
                break;
            case R.id.btn_modify_ip:
                Intent intent = new Intent(this, DebugActvity.class);
                startActivity(intent);
                break;

        }
    }


    /**
     * 登陆新服务器
     */
    private void loginNewServer() {
        plm = new PhoneLoginModel();
        plm.setCount(num);
        plm.setPassword(MD5Util.getMD5String(psw));
        plm.setLatitude(mLatitude);
        plm.setLongitude(mLongitude);
        plm.setLocation(mLocation);

        // plm.setProvice(province);
        // plm.setCity(city);

        try {
            OkHttpHelper.post(this, "user", "tellogin", new JSONObject(new Gson().toJson(plm)), this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(int code, String dataJson, String errMsg) {
        // ToastUtils.show(R.string.text_connection_timeout);
        Log.e("PhoneLoginActivity-->", Thread.currentThread().getName());
        if (code == ResponseCmdConstant.CODE_RETURN_OK) {
            try {
                JSONObject jobj = new JSONObject(dataJson);
                String tokenId = jobj.getString("tokenid");
                int roleid = jobj.getInt("roleid");
                // if (roleid != GlobalContant.ROLE_ID_PARENTS) {
                // closeDialogHelp();
                // ToastUtils.show(R.string.you_are_not_a_parents);
                // } else {

                UserInfoModel uInfo = new Gson().fromJson(dataJson, UserInfoModel.class);

                DBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);
                MySharePerfenceUtil.getInstance().savePhoneLoginInfo(plm);
                MySharePerfenceUtil.getInstance().setUserRoleId(uInfo.getRoleid());

                MySharePerfenceUtil.getInstance().setWelearnTokenId(tokenId);

                if (GlobalVariable.loginActivity != null) {
                    GlobalVariable.loginActivity.finish();
                    GlobalVariable.loginActivity = null;
                }

                // 打开websocket连接
                IntentManager.startWService(MyApplication.getContext());

                // IntentManager.goToMainView(this);
                // finish();

                String tel = jobj.getString("tel");
                // 判断是否绑定手机号，如果没有绑定，则跳转绑定手机号页面
                if (TextUtils.isEmpty(tel)) {
                    Intent i = new Intent(PhoneLoginActivity.this, RegisterActivity.class);
                    i.putExtra(RegisterActivity.DO_TAG, RegisterActivity.DO_BIND);
                    i.putExtra("isMust", "isMust");
                    i.putExtra("binding_from_login", true);
                    startActivityForResult(i, REQUEST_CODE_BIND);

                } else {
                    IntentManager.goToMainView(this);
                }
                finish();

                // }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            closeDialog();
            if (!TextUtils.isEmpty(errMsg)) {
                ToastUtils.show(errMsg);
            } else {
                ToastUtils.show(R.string.server_error);
            }
        }
    }

    @Override
    public void onFail(int HttpCode, String errMsg) {
        closeDialog();
        if (!TextUtils.isEmpty(errMsg)) {
            ToastUtils.show(errMsg);
        } else {
            ToastUtils.show(R.string.server_error);
//			ToastUtils.show(R.string.text_connection_timeout);
        }

    }


    public void setLoginButton() {
        if (flag1 && flag2) {
            login_btn.setBackgroundResource(R.drawable.login_btn_checked);
            login_btn.setTextColor(Color.parseColor("#ffffffff"));
        } else {
            login_btn.setBackgroundResource(R.drawable.login_btn_normal);
            login_btn.setTextColor(Color.parseColor("#80ffffff"));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onLocationChanged(BDLocation bdLocation, String province, String city) {
        if (bdLocation != null) {
            this.mLatitude = bdLocation.getLatitude();
            this.mLongitude =  bdLocation.getLongitude();
            this.mLocation = bdLocation.getAddrStr();
            // mLocationUtils.stopListen();
        }
        mLocationUtils.stopBDLocation();
    }

    @Override
    public void onPause() {
        MySharePerfenceUtil.getInstance().setPhoneNum(et_phoneno.getText().toString().trim());
        mLocationUtils.stopBDLocation();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationUtils.stopBDLocation();
        isShowDialog = false;
        closeDialog();
        if (mHandler != null) {
            mHandler.removeMessages(GlobalContant.CLOSEDIALOG);
        }
    }



}
