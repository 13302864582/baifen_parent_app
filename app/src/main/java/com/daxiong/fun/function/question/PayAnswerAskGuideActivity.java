
package com.daxiong.fun.function.question;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.daxiong.fun.R;
import com.daxiong.fun.adapter.GuidePageAdapter;
import com.daxiong.fun.adapter.GuidePageAdapter.OnViewClickListener;
import com.daxiong.fun.api.StudyAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.manager.HomeworkManager;
import com.daxiong.fun.manager.QuestionManager;
import com.daxiong.fun.model.MyOrgModel;
import com.daxiong.fun.model.OrgModel;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class PayAnswerAskGuideActivity extends BaseActivity implements OnViewClickListener {
    private ViewPager vp;

    private GuidePageAdapter vpAdapter;

    private List<View> views;

    // 底部小点图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;

    private StudyAPI homeworkApi;

    private HomeworkManager homeworkManager;

    private QuestionManager questionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 全屏并隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // slideClose = false;
        setContentView(R.layout.activity_guide);
        initObject();
        initView();

    }

    private void initObject() {
        homeworkManager = HomeworkManager.getInstance();
        questionManager = QuestionManager.getInstance();
        homeworkApi = new StudyAPI();

    }

    @Override
    public void initView() {
        super.initView();
        vp = (ViewPager)findViewById(R.id.guide_viewpager);
        views = new ArrayList<View>();
        LayoutInflater inflater = LayoutInflater.from(this);
        View page1 = inflater.inflate(R.layout.view_login_guide_layout, null);
        page1.setBackgroundResource(R.drawable.start_ask_guide_1);
        View page2 = inflater.inflate(R.layout.view_login_guide_layout, null);
        page2.setBackgroundResource(R.drawable.start_ask_guide_2);
        View page3 = inflater.inflate(R.layout.view_login_guide_layout, null);
        page3.setBackgroundResource(R.drawable.start_ask_guide_3);
        views.add(page1);
        views.add(page2);
        views.add(page3);

        vpAdapter = new GuidePageAdapter(this, views, GuidePageAdapter.GUIDE_TYPE_ASK, this);
        vp.setAdapter(vpAdapter);
        vp.setOffscreenPageLimit(2);

        MySharePerfenceUtil.getInstance().setShowAskGuideFalse();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSubViewClick(View v) {
        switch (v.getId()) {
            case R.id.start_todo_btn:
                // startActivity(new Intent(this, PayAnswerAskActivity.class));
                // finish();
                requestMyOrgs();
                break;
        }

    }

    private void requestMyOrgs() {
        // 请求我的机构
        homeworkApi.queryMyOrgs(requestQueue, 1, 1, 1000, this, RequestConstant.REQUEST_MY_ORGS);
    }

    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        int flag = ((Integer)param[0]).intValue();
        switch (flag) {
            case RequestConstant.REQUEST_MY_ORGS:// 查询我的机构
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        try {
                            String dataJson = JsonUtil.getString(datas, "Data", "");
                            if (!TextUtils.isEmpty(dataJson)) {
                                MyOrgModel myOrgModel = homeworkManager.parseJsonForMyOrg(dataJson);
                                ArrayList<OrgModel> listModels = myOrgModel.getOrgList();
                                boolean isWaibao = homeworkManager.compareValueIsWaibao(listModels);
                                // 如果是外包
                                if (isWaibao) {

                                    if (myOrgModel.getSpecialuser() != null
                                            && myOrgModel.getSpecialuser().size() > 0) {
                                        int type = myOrgModel.getSpecialuser().get(0).getType();// 表示是否是特殊学生
                                        if (type == 1) {// 是特殊学生

                                            if (listModels != null && listModels.size() > 0) {
                                                MySharePerfenceUtil.getInstance().setOrgVip();
                                                questionManager.goToOutsouringQuestionActivity(
                                                        PayAnswerAskGuideActivity.this, "", 0,
                                                        listModels, type);
                                            } else {
                                                MySharePerfenceUtil.getInstance().setNotOrgVip();
                                            }

                                        }

                                        if (type == 0) {// 不是特殊学生
                                            homeworkManager.isVipOrg(PayAnswerAskGuideActivity.this,
                                                    dataJson);
                                            questionManager.goToOutsouringQuestionActivity(
                                                    PayAnswerAskGuideActivity.this, "", 0,
                                                    listModels, type);
                                        }
                                    } else {// 外包非特殊学生
                                        homeworkManager.isVipOrg(PayAnswerAskGuideActivity.this,
                                                dataJson);
                                        questionManager.goToOutsouringQuestionActivity(
                                                PayAnswerAskGuideActivity.this, "", 0, listModels,
                                                0);
                                    }

                                } else {// 如果不是外包

                                    if (listModels != null && listModels.size() > 0) {// vip
                                        homeworkManager.isVipOrg(PayAnswerAskGuideActivity.this,
                                                dataJson);
                                        questionManager.getInstance()
                                                .goToStuPublishQuestionVipActivity(
                                                        PayAnswerAskGuideActivity.this, "", 0,
                                                        listModels);

                                    } else {// not vip
                                        homeworkManager.isVipOrg(PayAnswerAskGuideActivity.this,
                                                dataJson);
                                        questionManager.goNotPublishQuestionVipActivity(
                                                PayAnswerAskGuideActivity.this,
                                                PayAnswerAskActivity.class, null, false);
                                    }

                                }
                                finish();
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

}
