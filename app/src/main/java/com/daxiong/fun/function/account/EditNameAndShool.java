package com.daxiong.fun.function.account;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.http.RequestParamUtils;
import com.daxiong.fun.manager.UploadManager2;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.okhttp.callback.StringCallback;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.edittext.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;

import static com.daxiong.fun.R.id.et_shool;

/**
 * 修改密码activity
 *
 * @author: sky
 */
public class EditNameAndShool extends BaseActivity {

    @Bind(R.id.back_layout)
    RelativeLayout backLayout;

    @Bind(R.id.back_iv)
    ImageView backIv;

    @Bind(R.id.back_tv)
    TextView backTv;


    @Bind(R.id.next_setp_layout)
    RelativeLayout nextSetpLayout;
    @Bind(R.id.next_step_btn)
    TextView nextStepBtn;
    @Bind(R.id.next_step_btn2)
    TextView nextStepBtn2;
    @Bind(R.id.next_step_img)
    ImageView nextStepImg;

    @Bind(et_shool)
    ClearEditText etShool;


    private String tag;
    private String text;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.editnameandshool);
        ButterKnife.bind(this);
        getExtraData();
        initView();
        initListener();
    }

    private void getExtraData() {
        Intent intent = getIntent();
        tag = intent.getStringExtra("tag");
        text = intent.getStringExtra("text");
    }

    @Override
    public void initView() {
        super.initView();


        nextStepBtn.setVisibility(View.VISIBLE);
        nextStepBtn.setText(getResources().getString(R.string.save_text));
        nextStepBtn.setTextColor(Color.parseColor("#80f74344"));


        if ("昵称".equals(tag)) {
            setWelearnTitle("昵称");
            etShool.setHint("请直接输入您的昵称");
            InputFilter[] filters = {new InputFilter.LengthFilter(12)};
            etShool.setFilters(filters);
        } else {
            setWelearnTitle("学校");
        }
        if (!TextUtils.isEmpty(text)) {
            etShool.setText(text);
        }

    }

    @Override
    public void initListener() {
        super.initListener();
        backLayout.setOnClickListener(this);
        nextSetpLayout.setOnClickListener(this);

        etShool.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
             if(s.toString().length()>0){
                 nextStepBtn.setTextColor(getResources().getColor(R.color.colorf74344));
             }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.next_setp_layout:
                doSubmitData();
                break;
        }
    }


    public void doSubmitData() {
        String text = etShool.getText().toString().trim();
        showDialog("请稍候");
        JSONObject subParams = new JSONObject();
        try {
            Map<String, List<File>> files = null;
            if ("昵称".equals(tag)) {
                subParams.put("name", text);
            } else  {
                subParams.put("schools", text);

            }
            UploadManager2.upload(AppConfig.GO_URL + "parents/upuserinfos", RequestParamUtils.getMapParam(subParams), null,
                    new StringCallback() {
                        @Override
                        public void onBefore(Request request) {
                            super.onBefore(request);
                            // ToastUtils.show("onBefore");
                        }


                        @Override
                        public void onAfter() {
                            super.onAfter();
                            // ToastUtils.show("onAfter");

                        }

                        @Override
                        public void onResponse(String response) {
                            closeDialog();
                            int code = JsonUtil.getInt(response, "Code", -1);
                            String msg = JsonUtil.getString(response, "Msg", "");
                            final String responseStr = JsonUtil.getString(response, "Data", "");
                            if (code == 0) {
                                UserInfoModel uInfo = new Gson().fromJson(responseStr, UserInfoModel.class);
                                DBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);
                                String str = etShool.getText().toString().trim();
                                Intent intent = new Intent();
                                intent.putExtra("resultString", str);
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                ToastUtils.show(msg);
                            }

                        }

                        @Override
                        public void onError(Call call, Exception e) {
                            closeDialog();
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

                        }
                    }, true, 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
