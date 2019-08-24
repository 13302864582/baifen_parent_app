package com.daxiong.fun.function.question;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.api.HomeWorkAPI;
import com.daxiong.fun.api.QuestionListApi;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.callback.INetWorkListener;
import com.daxiong.fun.constant.MessageConstant;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.dispatch.QuListController;
import com.daxiong.fun.function.homepage.adapter.HomeAdapter;
import com.daxiong.fun.function.homepage.model.HomeListModel;
import com.daxiong.fun.function.homework.PublishHwActivity;
import com.daxiong.fun.util.DateUtil;
import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.SharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.XListView;
import com.daxiong.fun.view.XListView.IXListViewListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyQuestionListActivity extends BaseActivity
        implements IXListViewListener, INetWorkListener, OnCheckedChangeListener {
    private AuToRunTask runTask;
    private XListView xListView;
    private RadioGroup radioGroup;
    private HomeAdapter homeWorkListAdapter;
    private RadioButton radio_question;
    private HomeWorkAPI homeworkApi;
    private RadioButton radio_homework;
    private TextView tv2;
    private LinearLayout ll_kongbai;
    private QuestionListApi questionListApi;
    private QuListController quListController;

    private int LoadMore = 1;

    private int LoadMore3 = 1;
    private int LoadMore4 = 1;

    private int type = 0;
    boolean flag;
    boolean flag3;
    boolean flag4;
    private List<HomeListModel> list = new ArrayList<HomeListModel>();
    private List<HomeListModel> list3 = new ArrayList<HomeListModel>();
    private List<HomeListModel> list4 = new ArrayList<HomeListModel>();
    private List<Integer> list2 = new ArrayList<Integer>();
    private long errortime = SharePerfenceUtil.getLong("errortime", 0);

    private RelativeLayout back_layout;
    private RelativeLayout layout_next_step;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_homework_list2);
        initView();
        initListener();
        initData(false);
    }

    public void initView() {

        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getIntExtra("type", 0);

        }

        back_layout = (RelativeLayout)findViewById(R.id.back_layout);
        layout_next_step = (RelativeLayout)this.findViewById(R.id.next_setp_layout);
        layout_next_step.setVisibility(View.GONE);

        ImageView next_step_img=(ImageView)this.findViewById(R.id.next_step_img);
        next_step_img.setVisibility(View.GONE);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radio_question = (RadioButton) findViewById(R.id.radio_friend);
        radio_homework = (RadioButton) findViewById(R.id.radio_message);
        xListView = (XListView) findViewById(R.id.answer_list);
        ll_kongbai = (LinearLayout) findViewById(R.id.ll_kongbai);
        tv2 = (TextView) findViewById(R.id.tv2);

        radio_homework.setText("作业检查");
        radio_question.setText("难题答疑");


        questionListApi = new QuestionListApi();
        homeworkApi = new HomeWorkAPI();

        homeWorkListAdapter = new HomeAdapter(this, list);
        xListView.setAdapter(homeWorkListAdapter);

        if (type == 1) {
            radio_homework.setChecked(false);
            radio_question.setChecked(true);
        }
    }


    @Override
    public void initListener() {
        super.initListener();
        radioGroup.setOnCheckedChangeListener(this);
        xListView.setXListViewListener(this);
        xListView.setPullRefreshEnable(true);
        xListView.setPullLoadEnable(false);
        findViewById(R.id.back_layout).setOnClickListener(this);
        //findViewById(R.id.).setOnClickListener(this);

    }

    public void initData(boolean clearflag) {
        showDialog("加载中...");

        clearSelection();
        if (radio_homework.isChecked()) {
            tv2.setText("你暂时还没有作业检查");


//            radio_homework.setBackgroundResource(R.drawable.zuoyeline);
//            radio_question.setBackgroundColor(Color.parseColor("#FAFAFA"));
//            radio_homework.setTextColor(Color.parseColor("#F86467"));
//            radio_question.setTextColor(Color.BLACK);

            radio_homework.setTextColor(Color.parseColor("#ffffff"));
            radio_question.setTextColor(Color.parseColor("#57be6a"));
            if (clearflag) {
                list3.clear();
                list3.addAll(list);
                list.clear();
                if (list4.size() != 0) {

                    list.addAll(list4);

                    if (list.size() > 0) {
                        updateListUI();
                        ll_kongbai.setVisibility(View.GONE);
                    } else {
                        ll_kongbai.setVisibility(View.VISIBLE);

                    }
                    LoadMore = LoadMore4;
                    xListView.setPullLoadEnable(flag4);
                    closeDialog();
                } else {
                    homeworkApi.getHomeworkList(requestQueue, LoadMore, 10, this, RequestConstant.GET_HOMEWORK_LIST_CODE);
                }
            } else {
                if (LoadMore == 1) {
                    list.clear();
                }
                homeworkApi.getHomeworkList(requestQueue, LoadMore, 10, this, RequestConstant.GET_HOMEWORK_LIST_CODE);
            }


        } else {
            tv2.setText("你还没有难题答疑");


//            radio_question.setBackgroundResource(R.drawable.zuoyeline);
//            radio_homework.setBackgroundColor(Color.parseColor("#FAFAFA"));
//            radio_question.setTextColor(Color.parseColor("#F86467"));
//            radio_homework.setTextColor(Color.BLACK);

            radio_question.setTextColor(Color.parseColor("#ffffff"));
            radio_homework.setTextColor(Color.parseColor("#57be6a"));

            if (clearflag) {
                list4.clear();
                list4.addAll(list);
                list.clear();
                if (list3.size() != 0) {

                    list.addAll(list3);
                    if (list.size() > 0) {
                        updateListUI();
                        ll_kongbai.setVisibility(View.GONE);
                    } else {
                        ll_kongbai.setVisibility(View.VISIBLE);

                    }
                    LoadMore = LoadMore3;
                    xListView.setPullLoadEnable(flag3);
                    closeDialog();
                } else {
                    questionListApi.getQuestionList(requestQueue, LoadMore, 10, this, RequestConstant.GET_QUESTION_LIST_CODE);
                }
            } else {
                if (LoadMore == 1) {
                    list.clear();
                }
                questionListApi.getQuestionList(requestQueue, LoadMore, 10, this, RequestConstant.GET_QUESTION_LIST_CODE);
            }
        }
    }

    @Override
    public void onRefresh() {
        LoadMore = 1;

        initData(false);
    }

    @Override
    public void onLoadMore() {
        LoadMore++;

        initData(false);
    }

    public void onLoadFinish() {
        // isRefresh = true;
        xListView.stopRefresh();
        xListView.stopLoadMore();
        DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(new Date());
        xListView.setRefreshTime(time);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (quListController == null) {
            quListController = new QuListController(null, MyQuestionListActivity.this);
        }
        if (runTask != null) {
            runTask.start();
        }

    }

    @Override
    public void onPause() {
        if (runTask != null) {
            runTask.stop();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (quListController != null) {
            quListController.removeMsgInQueue();
            quListController = null;
        }
        runTask = null;
        super.onDestroy();
    }

    public void updateListUI() {

        list2.clear();

        for (int i = 0; i < list.size(); i++) {
            HomeListModel homeListModel = list.get(i);

            int task_type = homeListModel.getTask_type();
            if ((task_type == 2 && homeListModel.getHomework_state() == 1) | (task_type == 1 && homeListModel.getAnswer_state() == 0)) {
                list2.add(i);
            }
        }

        homeWorkListAdapter.notifyDataSetChanged();

        if (list2.size() > 0) {
            if (runTask == null) {

                runTask = new AuToRunTask();
            }
            runTask.start();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:// 返回
                finish();
                break;
            case R.id.bt:
                if (radio_homework.isChecked()) {
                    MobclickAgent.onEvent(this, "Open_Homework");
                    Intent hwintent = new Intent(this, PublishHwActivity.class);
                    startActivity(hwintent);

                } else {
                    MobclickAgent.onEvent(this, "Open_Question");
                    Intent questionIntent = new Intent(this, PublishQuestionActivity.class);
                    startActivity(questionIntent);
                }
                break;

            default:
                break;
        }
    }

    public class AuToRunTask implements Runnable {

        @Override
        public void run() {
            if (flag) {
                // 取消之前
                MyApplication.getMainThreadHandler().removeCallbacks(AuToRunTask.this);
                int lastVisiblePosition = xListView.getLastVisiblePosition();
                int firstVisiblePosition = xListView.getFirstVisiblePosition();
                for (Integer i : list2) {
                    if (i >= firstVisiblePosition && lastVisiblePosition >= i) {
                        HomeListModel homeListModel = list.get(i);

                        TextView tv_zuoyejieguo = (TextView) xListView.findViewWithTag("tv_zuoyejieguo" + i);
                        LinearLayout ll_jindu = (LinearLayout) xListView.findViewWithTag("ll_jindu" + i);

                        long pastTimeMillis = System.currentTimeMillis() - errortime - homeListModel.getGrab_time();
                        pastTimeMillis = pastTimeMillis < 1 ? 1 : pastTimeMillis;
                        if (tv_zuoyejieguo != null) {
                            tv_zuoyejieguo.setText("已耗时" + DateUtil.getMillis2minute(pastTimeMillis));
                        }
                        if (ll_jindu != null) {
                            long avg_cost_time = homeListModel.getAvg_cost_time();
                            avg_cost_time = avg_cost_time < 10 ? 10 : avg_cost_time;

                            long percent = pastTimeMillis * 100 / avg_cost_time;
                            percent = percent > 95 ? 95 : percent;
                            percent = percent < 5 ? 5 : percent;


                            int dip2px = DensityUtil.dip2px(MyQuestionListActivity.this, percent * 2);
                            ll_jindu.setLayoutParams(new RelativeLayout.LayoutParams(dip2px, LayoutParams.MATCH_PARENT));
                        }
                    }
                }

                // 延迟执行当前的任务
                MyApplication.getMainThreadHandler().postDelayed(AuToRunTask.this, 1000);// 递归调用
            }
        }

        public void start() {
            if (!flag) {
                MyApplication.getMainThreadHandler().removeCallbacks(AuToRunTask.this); // 取消之前
                flag = true;
                MyApplication.getMainThreadHandler().postDelayed(AuToRunTask.this, 1000);// 递归调用
            }
        }

        public void stop() {
            if (flag) {
                flag = false;
                MyApplication.getMainThreadHandler().removeCallbacks(AuToRunTask.this);
            }
        }

    }

    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        onLoadFinish();
        closeDialog();
        int flag = ((Integer) param[0]).intValue();

        switch (flag) {
            case RequestConstant.GET_QUESTION_LIST_CODE:
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        String dataJson = JsonUtil.getString(datas, "Data", "");
                        if (!TextUtils.isEmpty(dataJson)) {

                            List<HomeListModel> parseArray = JSON.parseArray(dataJson, HomeListModel.class);
                            if (parseArray.size() > 0) {
                                LoadMore3 = LoadMore;
                            }
                            if (parseArray.size() < 10) {
                                flag3 = false;
                                xListView.setPullLoadEnable(false);
                            } else {
                                flag3 = true;
                                xListView.setPullLoadEnable(true);

                            }
                            list.addAll(parseArray);

                            if (list.size() > 0) {
                                updateListUI();
                                ll_kongbai.setVisibility(View.GONE);
                            } else {
                                ll_kongbai.setVisibility(View.VISIBLE);

                            }

                        }
                    } else {
                        ToastUtils.show(msg);
                    }

                }

                break;
            case RequestConstant.GET_HOMEWORK_LIST_CODE:
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        String dataJson = JsonUtil.getString(datas, "Data", "");
                        if (!TextUtils.isEmpty(dataJson)) {

                            List<HomeListModel> parseArray = JSON.parseArray(dataJson, HomeListModel.class);
                            if (parseArray.size() > 0) {
                                LoadMore4 = LoadMore;
                            }
                            if (parseArray.size() < 10) {
                                flag4 = false;
                                xListView.setPullLoadEnable(false);
                            } else {
                                flag4 = true;
                                xListView.setPullLoadEnable(true);

                            }
                            list.addAll(parseArray);

                            if (list.size() > 0) {
                                updateListUI();
                                ll_kongbai.setVisibility(View.GONE);
                            } else {
                                ll_kongbai.setVisibility(View.VISIBLE);

                            }
                        }
                    } else {
                        ToastUtils.show(msg);
                    }

                }

                break;
        }

    }

    @Override
    public void onPre() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onException() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAfter(String jsonStr, int msgDef) {
        if (msgDef == MessageConstant.MSG_DEF_HOME_LIST) {
            String dataJson = JsonUtil.getString(jsonStr, "data", "");
            String dataJson2 = JsonUtil.getString(dataJson, "content", "");
            if (!"".equals(dataJson2)) {
                HomeListModel parseObject = JSON.parseObject(dataJson2, HomeListModel.class);
                for (int i = 0; i < list.size(); i++) {
                    if (parseObject.getCreate_time() == list.get(i).getCreate_time()) {
                        HomeListModel remove = list.remove(i);
                        if (parseObject.getHomework_state() == 9) {
                        } else {
                            list.add(i, parseObject);

                        }
                        break;
                    }
                }
                updateListUI();
            }

        }

    }

    @Override
    public void onDisConnect() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        LoadMore = 1;

        if (runTask != null) {
            runTask.stop();
        }

        initData(true);
    }


    private void clearSelection() {
        radio_homework.setTextColor(Color.parseColor("#57be6a"));
        radio_question.setTextColor(Color.parseColor("#57be6a"));

    }
}
