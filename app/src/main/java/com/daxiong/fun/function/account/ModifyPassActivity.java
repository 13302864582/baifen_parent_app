
package com.daxiong.fun.function.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.R;
import com.daxiong.fun.api.UserAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.model.PhoneLoginModel;
import com.daxiong.fun.util.CheckValidateUtils;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.edittext.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 修改密码activity
 * 
 * @author: sky
 */
public class ModifyPassActivity extends BaseActivity {
    private static final String TAG = "ModifyPassActivity";

    public static final String DO_TAG = "do_tag";

    //public static final int DO_REGISTER = 1;// 注册

    public static final int DO_BIND = 2;// 绑定

    public static final int DO_RESET = 3;// 找回密码

    public static final int DO_FORGET_PASS = 4;// 忘记密码

    private RelativeLayout bindingInfoLayout;

    private ClearEditText et_phone_no;
    private EditText et_validate_code;

    private EditText psw_et;

    private Button btn_validate_code, btn_next;






    private String num, psw, province, city;



    private UserAPI userApi;

    private RelativeLayout back_layout;



    private ImageView iv_back;


    private boolean isHidden = false;
    private PhoneLoginModel plm;

    private boolean flag1 = false;
    private boolean flag2 = false;

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
                        btn_validate_code.setText(time + "s后重新发送");
                        mHandler.sendMessageDelayed(msg1, 1000);
                    } else {
                        btn_validate_code.setText("重新发送");
                        btn_validate_code.setClickable(true);
//					    get_ver.setBackgroundResource(R.drawable.bg_get_verification_btn_selector);
                    }
                    break;
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_forget_password);
        initView();
        initListener();
    }



    @Override
    public void initView() {
        super.initView();
        back_layout = (RelativeLayout) findViewById(R.id.back_layout);
        bindingInfoLayout = (RelativeLayout) findViewById(R.id.binding_info_layout);
        et_phone_no = (ClearEditText) findViewById(R.id.et_phone_no);
        et_validate_code = (EditText) findViewById(R.id.et_validate_code);
        btn_validate_code = (Button) findViewById(R.id.btn_validate_code);
        btn_next = (Button) findViewById(R.id.btn_next);
        setWelearnTitle(getResources().getString(R.string.modify_pass));
        userApi = new UserAPI();



    }

    @Override
    public void initListener() {
        super.initListener();
        back_layout.setOnClickListener(this);
        btn_validate_code.setOnClickListener(this);
        btn_next.setOnClickListener(this);


        et_phone_no.addTextChangedListener(new TextWatcher() {

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
//                setLoginButton();
            }
        });


        et_validate_code.addTextChangedListener(new TextWatcher() {

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
              //  setLoginButton();
            }
        });

    }

    public void setLoginButton() {
        if (flag1 && flag2) {
            btn_next.setBackgroundResource(R.drawable.login_btn_checked);
            btn_next.setTextColor(Color.parseColor("#ffffffff"));
        } else {
            btn_next.setBackgroundResource(R.drawable.login_btn_normal);
            btn_next.setTextColor(Color.parseColor("#80ffffff"));
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        String phoneNum = MySharePerfenceUtil.getInstance().getPhoneNum();
        //检测手机号
        if (CheckValidateUtils.isMobileNO(phoneNum)) {
            et_phone_no.setText(phoneNum);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout://返回
                // setResult(RESULT_OK);
                finish();
                break;
            case R.id.btn_validate_code://获取验证码
                MobclickAgent.onEvent(this, "GetSecurityCode");
                getValidateCode();
                break;
            case R.id.btn_next://下一步
                doNext();

                break;
//		case R.id.skip_binding:
//			setResult(RESULT_OK);
//			finish();
//			break;

        }
    }


    /**
     * 获取验证码
     */
    private void getValidateCode() {
        if (System.currentTimeMillis() - clickTime < 500) {
            return;
        }
        clickTime = System.currentTimeMillis();
        num = et_phone_no.getText().toString().trim();
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
        }

        btn_validate_code.setClickable(false);
        // get_ver.setBackgroundResource(R.drawable.ic_get_verificate_bt_pressed);
        Message msg = Message.obtain();
        msg.what = GlobalContant.LOOPMSG;
        msg.obj = 60;
        btn_validate_code.setText("60s后重新发送");
        mHandler.sendMessageDelayed(msg, 1000);
    }


    /**
     * 下一步操作
     */
    public void doNext() {
        String et_phonenoStr = et_phone_no.getText().toString().trim();
        String et_validateStr = et_validate_code.getText().toString().trim();
        if (TextUtils.isEmpty(et_phonenoStr)) {
            ToastUtils.show("手机号码不能为空");
            return;
        }
        if (TextUtils.isEmpty(et_validateStr)) {
            ToastUtils.show("验证码不能为空");
            return;
        }
        Intent nextIntent = new Intent(this, ForgetPasswordNextActivity.class);
        nextIntent.putExtra("phoneno", et_phonenoStr);
        nextIntent.putExtra("validatecode", et_validateStr);
        startActivity(nextIntent);
    }





    @Override
    public void onPause() {
        super.onPause();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isShowDialog = false;
        closeDialog();
    }

}
