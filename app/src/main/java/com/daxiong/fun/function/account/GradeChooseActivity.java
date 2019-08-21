
package com.daxiong.fun.function.account;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.daxiong.fun.R;
import com.daxiong.fun.api.MainAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.function.account.adapter.GradeGridAdapter;
import com.daxiong.fun.function.account.model.BigGradeModel;
import com.daxiong.fun.function.account.model.BigGradeModel.SubGradeModel;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.NetworkUtils;
import com.daxiong.fun.util.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 家长端 选择年级界面
 *
 * @author Administrator
 */
public class GradeChooseActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
    public static final String TAG = GradeChooseActivity.class.getSimpleName();
    private static final int HANDLER_BIND_CODE = 1112;

    private RelativeLayout nextStepLayout;
    private ListView listview;
    private GradeGridAdapter MyAdapter;

    private List<String> titleList = null;
    //获取小学
    private List<SubGradeModel> primary_schools = null;
    // 获取初中
    private List<SubGradeModel> junior_hight_schools = null;
    // 获取高中
    private List<SubGradeModel> senior_high_school = null;

    private int mgradeid = -1;
    private boolean isFromCenter = false;
    private MainAPI mainApi;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade_choose_activity);
        registerFinishMySelf();
        initView();
        initListener();
        initObject();
    }

    @Override
    public void initView() {
        super.initView();
        setWelearnTitle(R.string.text_grade_choice);
        nextStepLayout = (RelativeLayout) findViewById(R.id.next_setp_layout);
        nextStepLayout.setVisibility(View.GONE);
        listview = (ListView) findViewById(R.id.listview);
        Intent intent = getIntent();
        isFromCenter = intent.getBooleanExtra("isFromCenter", false);
        mgradeid = intent.getIntExtra("mgradeid", -1);

        mainApi = new MainAPI();
        titleList = new ArrayList<String>();

        if (titleList != null) {
            if (titleList.size() > 0) {
                titleList.clear();
                titleList.add("小学");
                titleList.add("初中");
                titleList.add("高中");
            } else {
                titleList.add("小学");
                titleList.add("初中");
                titleList.add("高中");
            }
        }

        MyAdapter = new GradeGridAdapter(GradeChooseActivity.this, titleList);
        listview.setAdapter(MyAdapter);



    }

    public void initObject() {
        if (NetworkUtils.getInstance().isInternetConnected(this)) {
            showDialog("正在加载...");
            mainApi.getGradeList(requestQueue, this, RequestConstant.GET_GRADE_LIST);
        } else {
            ToastUtils.show("网络连接失败,请查看网络是否连接");
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        findViewById(R.id.back_layout).setOnClickListener(this);
        nextStepLayout.setOnClickListener(this);
        listview.setOnItemClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                finish();
                break;

        }

    }


    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        int flag = ((Integer) param[0]).intValue();
        switch (flag) {
            case RequestConstant.GET_GRADE_LIST:// 从服务器获取年级信息
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    closeDialog();
                    if (code == 0) {
                        try {
                            String dataJson = JsonUtil.getString(datas, "Data", "");
                            if (!TextUtils.isEmpty(dataJson)) {
                                BigGradeModel bigGradeModel = JSON.parseObject(dataJson, BigGradeModel.class);
                                // 获取小学
                                primary_schools = bigGradeModel.getPrimary_school();
                                // 获取初中
                                junior_hight_schools = bigGradeModel.getJunior_high_school();
                                // 获取高中
                                senior_high_school = bigGradeModel.getSenior_high_school();

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
        Intent Intentx = new Intent(GradeChooseActivity.this, GradeChooseNextActivity.class);
        if (position == 0) {
            Intentx.putExtra("gradeList", (Serializable) primary_schools);
            Intentx.putExtra("title", "小学");
        } else if (position == 1) {
            Intentx.putExtra("gradeList", (Serializable) junior_hight_schools);
            Intentx.putExtra("title", "初中");
        } else if (position == 2) {
            Intentx.putExtra("gradeList", (Serializable) senior_high_school);
            Intentx.putExtra("title", "高中");
        }
        Intentx.putExtra("isFromCenter", isFromCenter);
        Intentx.putExtra("mgradeid", mgradeid);
        startActivityForResult(Intentx, 115);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            int gradeid=data.getIntExtra("gradeid",0);
            data.putExtra("gradeid",gradeid);
            setResult(RESULT_OK,data);
            finish();
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
                GradeChooseActivity.this.finish();
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
        mHandler.removeCallbacksAndMessages(null);
    }


}
