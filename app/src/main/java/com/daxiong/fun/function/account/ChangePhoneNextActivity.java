package com.daxiong.fun.function.account;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.daxiong.fun.R;
import com.daxiong.fun.api.UserAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.model.PhoneLoginModel;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.CheckValidateUtils;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.edittext.ClearEditText;


/**
 * Created by Sky on 2016/7/1 0001.
 */

public class ChangePhoneNextActivity extends BaseActivity {
    private static final String TAG = ChangePhoneActivity.class.getSimpleName();

    private RelativeLayout back_layout;

    private ClearEditText et_phone;

    private ClearEditText et_validate_code;

    private Button btn_submit;

    private UserAPI userApi;

    private String extra_phoneno_str;

    private String et_validatecode_str;



    private PhoneLoginModel plm;

    private boolean flag1=false,flag2=true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_phone_next_activity);
        initView();
        initListener();

    }

    @Override
    public void initView() {
        super.initView();
        back_layout = (RelativeLayout)findViewById(R.id.back_layout);
        et_phone = (ClearEditText)findViewById(R.id.et_phone);
        et_validate_code = (ClearEditText)findViewById(R.id.et_validate_code);
        btn_submit = (Button)findViewById(R.id.btn_submit);
        setWelearnTitle("更改手机");
        userApi = new UserAPI();

        Intent intent = getIntent();
        extra_phoneno_str = intent.getStringExtra("phone");

        et_phone.setText(extra_phoneno_str);
    }

    @Override
    public void initListener() {
        super.initListener();
        back_layout.setOnClickListener(this);
        btn_submit.setOnClickListener(this);


        et_phone.addTextChangedListener(new TextWatcher() {
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

                setLoginButton();

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
//        String phoneNum = MySharePerfenceUtil.getInstance().getPhoneNum();
//        et_phone.setText(phoneNum);

    }

    public void setLoginButton() {
        if (flag1 && flag2) {
            btn_submit.setBackgroundResource(R.drawable.login_btn_checked);
            btn_submit.setTextColor(Color.parseColor("#ffffffff"));
        } else {
            btn_submit.setBackgroundResource(R.drawable.login_btn_normal);
            btn_submit.setTextColor(Color.parseColor("#80ffffff"));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:// 返回
                // setResult(RESULT_OK);
                finish();
                break;
            case R.id.btn_submit:// 更换手机操作
                String et_validatecode_str = et_validate_code.getText().toString().trim();

                if (TextUtils.isEmpty(extra_phoneno_str)
                        || !CheckValidateUtils.isMobileNO(extra_phoneno_str)) {
                    ToastUtils.show("请输入正确的手机号码");
                    return;
                }
                if (TextUtils.isEmpty(et_validatecode_str)) {
                    ToastUtils.show("请输入验证码");
                    return;
                }
                UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
                PhoneLoginModel model=MySharePerfenceUtil.getInstance().getPhoneLoginInfo();
                userApi.modifyTel(requestQueue, extra_phoneno_str, et_validatecode_str, model.getPassword(),
                        this, RequestConstant.CHANGE_PHONE_CODE);
                break;


        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        int flag = ((Integer)param[0]).intValue();
        switch (flag) {
            case RequestConstant.CHANGE_PHONE_CODE:// 更换手机
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        try {
                            closeDialog();
                            String dataJson = JsonUtil.getString(datas, "Data", "");
                            ToastUtils.show(R.string.text_binding_success);
                            Intent intent = new Intent();
                            intent.putExtra("tel", et_phone.getText().toString().trim());
                            setResult(RESULT_OK, intent);
                            finish();

                        } catch (Exception e) {
                            e.printStackTrace();
                            closeDialog();
                            if (!TextUtils.isEmpty(msg)) {
                                ToastUtils.show(msg);
                            }
                        }
                    } else {
                        closeDialog();
                        if (!TextUtils.isEmpty(msg)) {
                            ToastUtils.show(msg);
                        }
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
