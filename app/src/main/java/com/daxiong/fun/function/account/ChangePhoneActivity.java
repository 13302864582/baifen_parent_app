package com.daxiong.fun.function.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.dialog.CustomModifyPhoneDialog;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.model.PhoneLoginModel;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.edittext.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.daxiong.fun.R.id.back_layout;
import static com.daxiong.fun.R.id.btn_next;

public class ChangePhoneActivity extends BaseActivity {

    private static final String TAG = ChangePhoneActivity.class.getSimpleName();
    @Bind(R.id.back_iv)
    ImageView backIv;
    @Bind(R.id.back_layout)
    RelativeLayout backLayout;

    @Bind(R.id.next_setp_layout)
    RelativeLayout nextSetpLayout;

    @Bind(R.id.et_phone_no)
    ClearEditText etPhoneNo;
    @Bind(R.id.btn_next)
    Button btnNext;
    private PhoneLoginModel plm;

    private String phone = "";

    private boolean flag1;


    private Handler mHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_phone_activity);
        ButterKnife.bind(this);
        initView();
        initListener();

    }

    @Override
    public void initView() {
        super.initView();
        setWelearnTitle("更改手机");
        Intent intentx = getIntent();
        phone = intentx.getStringExtra("phone");
        //etPhoneNo.setText(phone);

    }

    @Override
    public void initListener() {
        super.initListener();
        backLayout.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        etPhoneNo.addTextChangedListener(new TextWatcher() {
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
//                if (flag1) {
//                    btnNext.setTextColor(getResources().getColorStateList(R.color.colorffffff));
//                } else {
//                    btnNext.setTextColor(getResources().getColorStateList(R.color.color80ffffff));
//                }

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case back_layout:// 返回
                finish();
                break;
            case btn_next:// 更换手机操作
                String et_phone_str = etPhoneNo.getText().toString().trim();
                if (TextUtils.isEmpty(et_phone_str)
                        || !et_phone_str.matches("1[3|5|4|7|8|][0-9]{9}")) {
                    ToastUtils.show("请输入正确的手机号码");
                    return;
                }
                MySharePerfenceUtil.getInstance().setPhoneNum(et_phone_str);
                showDialogForValidate();
                break;


        }
    }

    private void showDialogForValidate() {
        final CustomModifyPhoneDialog midifyPhoneDialog = new CustomModifyPhoneDialog(this, "确认手机号码", "我们将发送验证码到这个号码" + phone, "取消", "好");
        midifyPhoneDialog.show();
        midifyPhoneDialog.setClicklistener(new CustomModifyPhoneDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                midifyPhoneDialog.dismiss();
                sendValidateCode();
                Intent intentxx = new Intent(ChangePhoneActivity.this, ChangePhoneNextActivity.class);
                intentxx.putExtra("phone", etPhoneNo.getText().toString().trim());
                startActivity(intentxx);
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
        String et_phone_str = etPhoneNo.getText().toString().trim();
        String telRegex = "[1][34578]\\d{9}";// "[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。

        if (TextUtils.isEmpty(et_phone_str)) {
            ToastUtils.show("请输入正确的手机号码");
            return;
        }
        if (!et_phone_str.matches(telRegex)) {
            ToastUtils.show("请输入正确的手机号码");
            return;
        }
        try {
            JSONObject jobj = new JSONObject();
            jobj.put("tel", et_phone_str);
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

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String tel = data.getStringExtra("tel");
            data.putExtra("tel", tel);
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
