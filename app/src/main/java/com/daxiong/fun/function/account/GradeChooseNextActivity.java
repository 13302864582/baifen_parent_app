package com.daxiong.fun.function.account;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.daxiong.fun.R;
import com.daxiong.fun.api.MainAPI;
import com.daxiong.fun.api.UserAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.manager.UploadManager;
import com.daxiong.fun.manager.UploadManager.OnUploadListener;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.function.account.adapter.GradeNextAdapter;
import com.daxiong.fun.function.account.model.BigGradeModel;
import com.daxiong.fun.function.account.model.BigGradeModel.SubGradeModel;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.http.RequestParamUtils;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.UploadResult;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.GradeUtil;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.daxiong.fun.R.id.back_layout;

/**
 * 家长端 选择年级界面
 *
 * @author Administrator
 */
public class GradeChooseNextActivity extends BaseActivity implements OnClickListener, HttpListener, OnUploadListener, OnItemClickListener {
    public static final String TAG = GradeChooseNextActivity.class.getSimpleName();

    @Bind(back_layout)
    RelativeLayout backLayout;
    @Bind(R.id.next_setp_layout)
    RelativeLayout nextSetpLayout;
    @Bind(R.id.next_step_btn)
    TextView next_step_btn;
    @Bind(R.id.listview)
    ListView listview;

    private MainAPI mainApi;
    private boolean isFromCenter = false;
    private int chooseGradeid;
    private int mgradeid = -1;
    private GradeNextAdapter gradeNextAdapter;
    private List<BigGradeModel.SubGradeModel> gradeModelList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade_choose_next_activity);
        ButterKnife.bind(this);
        registerFinishMySelf();
        initView();
        initListener();
        showBinded();
    }



    @Override
    public void initView() {
        super.initView();
        nextSetpLayout.setVisibility(View.VISIBLE);
        next_step_btn.setVisibility(View.VISIBLE);
        next_step_btn.setText("完成");
        next_step_btn.setTextColor(Color.parseColor("#80f74344"));

        Intent intent = getIntent();
        isFromCenter = intent.getBooleanExtra("isFromCenter", false);
        mgradeid = intent.getIntExtra("mgradeid", -1);
        gradeModelList = (List<SubGradeModel>) intent.getSerializableExtra("gradeList");
        String title = intent.getStringExtra("title");
        setWelearnTitle(title!=null?title:"选择年级");
        gradeNextAdapter = new GradeNextAdapter(this, gradeModelList);
        listview.setAdapter(gradeNextAdapter);
        mainApi = new MainAPI();

    }

    /**
     * 从个人资料页进来展示已经选择的年级
     */
    private void showBinded() {
        for (int i = 0; i < gradeModelList.size(); i++) {
            SubGradeModel model = gradeModelList.get(i);
            if (model.getGradeid() == mgradeid) {
                model.setChecked(1);
            }
        }
        gradeNextAdapter.notifyDataSetChanged();
    }


    @Override
    public void initListener() {
        super.initListener();
        backLayout.setOnClickListener(this);
        nextSetpLayout.setOnClickListener(this);
        listview.setOnItemClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.next_setp_layout://确定按钮
                if (chooseGradeid == 0) {
                    ToastUtils.show(R.string.text_toast_select_grade);
                } else {
                    MySharePerfenceUtil.getInstance().setGrades(GradeUtil.getGradeString(chooseGradeid));
                    // MySharePerfenceUtil.getInstance().setGoodSubject(new
                    // JSONArray().toString());


                    // WeLearnApi.updateUserInfoFromServer(this, updata_data,
                    // new HttpListener() {
                    // @Override
                    // public void onSuccess(int code, String dataJson, String
                    // errMsg) {
                    // dissmissDialog();
                    // if (code == 0) {
                    // WeLearnApi.getUserInfoFromServer(GradeChooseActivity.this,
                    // GradeChooseActivity.this);
                    // } else {
                    // if (!TextUtils.isEmpty(errMsg)) {
                    // ToastUtils.show(errMsg);
                    // } else {
                    // ToastUtils.show(R.string.modifyinfofailed);
                    // }
                    // }
                    // }
                    //
                    // @Override
                    // public void onFail(int HttpCode) {
                    // dissmissDialog();
                    // ToastUtils.show(R.string.modifyinfofailed);
                    // }
                    // });

                    showDialog("正在提交...");
                    try {
                        JSONObject updata_data = new JSONObject();
                        updata_data.put("gradeid", chooseGradeid);
                        UploadManager.upload(AppConfig.GO_URL + "parents/upuserinfos", RequestParamUtils.getParam(updata_data),
                                null, this, true, 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                break;

        }

    }


    @Override
    public void onUploadSuccess(UploadResult result, int index) {
        String data = result.getData();
        UserInfoModel uInfo = JSON.parseObject(data, UserInfoModel.class);
        DBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);
        if (isFromCenter) {//从个人资料页选择完成之后
            ToastUtils.show(R.string.modifyinfosuccessful);
            Intent intentxxx = new Intent();
            intentxxx.putExtra("gradeid", chooseGradeid);
            setResult(RESULT_OK, intentxxx);
            finish();
        } else {//注册选择完年级之后的逻辑
            new UserAPI().getUserinfos(requestQueue, this, RequestConstant.GET_USERINFO);
        }

    }

    @Override
    public void onUploadError(String msg, int index) {
        closeDialog();
        if (!TextUtils.isEmpty(msg)) {
            ToastUtils.show(msg);
        }
    }

    @Override
    public void onUploadFail(UploadResult result, int index) {
        closeDialog();
        if (result != null) {
            String msg = result.getMsg();
            if (!TextUtils.isEmpty(msg)) {
                ToastUtils.show(msg);
            }
        }
    }

    @Override
    public void onSuccess(int code, String dataJson, String errMsg) {
        UserInfoModel uInfo = new Gson().fromJson(dataJson, UserInfoModel.class);
        DBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);

        if (isFromCenter) {
            ToastUtils.show(R.string.modifyinfosuccessful);
            finish();
        } else {
            IntentManager.goToMainView(GradeChooseNextActivity.this);
        }
    }

    @Override
    public void onFail(int HttpCode, String errMsg) {

    }


    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        int flag = ((Integer) param[0]).intValue();
        switch (flag) {
            case RequestConstant.GET_USERINFO:// 得到用户信息
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    closeDialog();
                    if (code == 0) {
                        try {
                            String dataJson = JsonUtil.getString(datas, "Data", "");
                            closeDialog();
                            if (!TextUtils.isEmpty(dataJson)) {
                                UserInfoModel uInfo = JSON.parseObject(dataJson, UserInfoModel.class);
                                DBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);
                                //ToastUtils.show("修改成功!");
//							setResult(RESULT_OK);
//							finish();
//							IntentManager.goToMainView(GradeChooseActivity.this);
                                // 跳转到角色选择界面
                                Intent goRoleIntent = new Intent(GradeChooseNextActivity.this, RoleChooseActivity.class);
                                startActivity(goRoleIntent);


                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        ToastUtils.show(msg);
                    }

                }
                break;

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        for (int i1 = 0; i1 < gradeModelList.size(); i1++) {
            SubGradeModel model = gradeModelList.get(i1);
            model.setChecked(0);
            if (i1 == position) {
                model.setChecked(1);
                chooseGradeid = model.getGradeid();
                next_step_btn.setTextColor(Color.parseColor("#f74344"));
            }
        }
        gradeNextAdapter.notifyDataSetChanged();
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
                GradeChooseNextActivity.this.finish();
            }

        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mainApi != null) {
            mainApi = null;
        }
        unregisterReceiver(finishReceiver);
    }


}
