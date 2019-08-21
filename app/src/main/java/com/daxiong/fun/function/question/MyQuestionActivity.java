
package com.daxiong.fun.function.question;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.R;
import com.daxiong.fun.api.StudyAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.function.question.adapter.MyQuestionAdapter;
import com.daxiong.fun.function.question.model.AnswerListItemModel;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.HomeworkManager;
import com.daxiong.fun.manager.QuestionManager;
import com.daxiong.fun.model.MyOrgModel;
import com.daxiong.fun.model.OrgModel;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.NetworkUtils;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.XListView;
import com.daxiong.fun.view.XListView.IXListViewListener;
import com.daxiong.fun.view.XListViewFooter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 此类的描述：我的提问 * 
 * @author: sky
 */
public class MyQuestionActivity extends BaseActivity
        implements OnClickListener, OnScrollListener, IXListViewListener, HttpListener {

    public static final String KEY_IS_QPAD = "is_qpad";

    private static final int PAGE_COUNT = 10;

    private static final int TYPE_ASK = 2;

    private static final int TYPE_SAME_GRADE = 4;

    private static final int TYPE_OTHER_ASK = 5;

    private static final int TYPE_OTHER_ANSWER = 6;

    private ArrayList<AnswerListItemModel> currentList;

    private XListView mAnswerList;

    private MyQuestionAdapter mAdapter;

    private LinearLayout mTopBarContainer;

    private TextView mTopbarLeftTab;

    private TextView mTopbarRightTab;

    private int pageIndex = 1;

    private boolean isRefresh = true;

    // add by milo 2014.09.01
    private int target_role_id;

    private int targetuserid;

    private boolean hasMore = true;

    private TextView nextStepTV;

    private RelativeLayout nextStepLayout;

    private int packtype = TYPE_ASK;

    private StudyAPI studyApi;

    private HomeworkManager homeworkManager;

    private QuestionManager questionManager;

    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_answer_list);
        initObject();
        initView();
        initListener();
        refreshUI();
        loadData();

    };

    public void initView() {
        int roleId = MySharePerfenceUtil.getInstance().getUserRoleId();
        if (roleId == GlobalContant.ROLE_ID_STUDENT) {
            setWelearnTitle(R.string.text_my_qpad_stu);

            nextStepLayout = (RelativeLayout)findViewById(R.id.next_setp_layout);
            nextStepTV = (TextView)findViewById(R.id.next_step_btn);
            nextStepTV.setVisibility(View.VISIBLE);
            nextStepTV.setText(R.string.text_ask_title);
            nextStepTV.setBackgroundResource(R.drawable.publish_btn_selector);
            nextStepLayout.setOnClickListener(this);
        }

        findViewById(R.id.back_layout).setOnClickListener(this);

        Intent intent = getIntent();
        int roleid = intent.getIntExtra("roleid", 0);
        int userid = intent.getIntExtra("userid", 0);
        if (roleid != 0 && userid != 0) {

            this.target_role_id = roleid;
            switch (roleid) {
                case GlobalContant.ROLE_ID_STUDENT:// 学生 type为别人的问集
                    packtype = TYPE_OTHER_ASK;

                    break;
                case GlobalContant.ROLE_ID_COLLEAGE:
                    packtype = TYPE_OTHER_ANSWER;
                    break;

                default:
                    break;
            }

            this.targetuserid = userid;
            // mActionBar.setTitle(intent.getStringExtra("title"));
            setWelearnTitle(intent.getStringExtra("title"));
        } else {
            this.target_role_id = MySharePerfenceUtil.getInstance().getUserRoleId();
            this.targetuserid = MySharePerfenceUtil.getInstance().getUserId();
        }

        mAdapter = new MyQuestionAdapter(this);

        mAnswerList = (XListView)findViewById(R.id.answer_list);
        mTopBarContainer = (LinearLayout)findViewById(R.id.top_bar_container);
        mTopbarLeftTab = (TextView)findViewById(R.id.tab_text_collection);
        mTopbarRightTab = (TextView)findViewById(R.id.tab_text_questions);
        mAdapter.setGoTag(1);//1表示从问答跳转到解答详情页面 为了表示从学情分析跳转的还是从我的问答跳转的
        mAnswerList.setAdapter(mAdapter);
       
        if (currentList == null) {
            currentList = new ArrayList<AnswerListItemModel>();
        }
    }
    public void initObject() {
        studyApi = new StudyAPI();
        homeworkManager = HomeworkManager.getInstance();
        questionManager = QuestionManager.getInstance();
    }
    
    public void initListener(){
        mAnswerList.setPullLoadEnable(true);
        mAnswerList.setPullRefreshEnable(true);
        mAnswerList.setXListViewListener(this);
        mTopbarLeftTab.setOnClickListener(this);
        mTopbarRightTab.setOnClickListener(this);
    }

   

    private void loadData() {
        JSONObject data = new JSONObject();
        try {
            data.put("packtype", packtype);// 0代表广场， 1代表发完悬赏问答之后跳转到广场， 2表示我的问集，
                                           // 3表示我的答集 , 4代表同级问题
            data.put("pageindex", pageIndex);
            data.put("pagecount", PAGE_COUNT);
            data.put("targetuserid", targetuserid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpHelper.post(this, "question", "getall",  data, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onEventBegin(this, "MyQPad");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onEventEnd(this, "MyQPad");
    }

    private void refreshUI() {
        if (packtype == TYPE_ASK) {
            mTopBarContainer.setVisibility(View.VISIBLE);
        } else {
            mTopBarContainer.setVisibility(View.GONE);
        }
        mTopbarLeftTab.setText(getString(R.string.text_my_question));
        mTopbarLeftTab.setTag(GlobalContant.QUESTION_OF_MINE);

        mTopbarRightTab.setText(getString(R.string.text_same_grade_question));
        mTopbarRightTab.setTag(GlobalContant.GRADE_SAME_QUESTION);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.next_setp_layout:
                GlobalVariable.myQPadActivity = this;
                // IntentManager.goToPayAnswerAskView(this, false);
                // 调用myrogs接口判断跳转
                requestMyOrgs();
                break;
            case R.id.tab_text_collection:
                mTopbarLeftTab.setTextColor(getResources().getColor(R.color.tabbar_normal));
                mTopbarRightTab.setTextColor(getResources().getColor(R.color.tabbar_pressed));

                int tagLeft = Integer.parseInt(view.getTag().toString());
                if (tagLeft == GlobalContant.QUESTION_OF_MINE) {// 我的问题
                    if (packtype == TYPE_SAME_GRADE) {
                        packtype = TYPE_ASK;
                        scrollToRefresh();
                        onRefresh();
                    }
                }
                break;
            case R.id.tab_text_questions:
                MobclickAgent.onEvent(this, "sameGradeQ");
                mTopbarLeftTab.setTextColor(getResources().getColor(R.color.tabbar_pressed));
                mTopbarRightTab.setTextColor(getResources().getColor(R.color.tabbar_normal));
                int tagRight = Integer.parseInt(view.getTag().toString());
                if (tagRight == GlobalContant.GRADE_SAME_QUESTION) {// 同级问题
                    if (packtype == TYPE_ASK) {
                        packtype = TYPE_SAME_GRADE;
                        scrollToRefresh();
                        onRefresh();
                    }
                }
                break;
        }
    }

    
    
    
    
    @Override
	public void onSuccess(int code, String dataJson, String errMsg) {
        if (TextUtils.isEmpty(dataJson)) {
            hasMore = false;
        } else {
            Gson gson = new Gson();
            ArrayList<AnswerListItemModel> answerList = null;
            try {
                answerList = gson.fromJson(dataJson,
                        new TypeToken<ArrayList<AnswerListItemModel>>() {
                        }.getType());
            } catch (Exception e) {
            }

            pageIndex++;
            if (isRefresh) {
                currentList.clear();
            }
            if (answerList != null && !answerList.isEmpty()) {
                if (answerList.size() < PAGE_COUNT) {
                    hasMore = false;
                    mAnswerList.setPullLoadEnable(false);
                    
                }
                currentList.addAll(answerList);
            }
            if (currentList.size() == 0) {
                ToastUtils.show(R.string.text_no_question);
            } else if (currentList.size() < 10) {
                ToastUtils.show(getString(R.string.text_question_just_have, currentList.size()));
            }
            mAdapter.setData(currentList);
        }
        onLoadFinish();
    
		
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {
		// TODO Auto-generated method stub
		
	}

    public void onLoadFinish() {
        mAnswerList.stopRefresh();
        mAnswerList.stopLoadMore();
        DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(new Date());
        mAnswerList.setRefreshTime(time);
    }

    @Override
    public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
        mAdapter.setScrolling(true);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            mAdapter.setScrolling(false);
        }
    }

    public void scrollToRefresh() {
        if (mAnswerList != null) {
            mAnswerList.showHeaderRefreshing();
            mAnswerList.setSelection(0);
            isRefresh = true;
        }
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        hasMore = true;
        isRefresh = true;
        loadData();
        mAnswerList.getFooterView().setState(XListViewFooter.STATE_OTHER, "");
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        if (hasMore) {
            loadData();
        } else {
            mAnswerList.getFooterView().setState(XListViewFooter.STATE_NOMORE, "");
            onLoadFinish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1002) {
                onRefresh();
            }
        }
    }

    private void requestMyOrgs() {
        if (NetworkUtils.getInstance().isInternetConnected(this)) {
            // 请求我的机构
            studyApi.queryMyOrgs(requestQueue, 1, 1, 1000, this, RequestConstant.REQUEST_MY_ORGS);
        } else {
            ToastUtils.show("网络连接不可用，请检查网络");
        }

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
                                                questionManager.goToOutsouringQuestionActivity(this,
                                                        "", 0, listModels, type);
                                            } else {
                                                MySharePerfenceUtil.getInstance().setNotOrgVip();
                                            }

                                        }

                                        if (type == 0) {// 不是特殊学生
                                            homeworkManager.isVipOrg(this, dataJson);
                                            questionManager.goToOutsouringQuestionActivity(this, "",
                                                    0, listModels, type);
                                        }
                                    } else {// 外包不是特殊学生
                                        homeworkManager.isVipOrg(this, dataJson);
                                        questionManager.goToOutsouringQuestionActivity(this, "", 0,
                                                listModels, 0);
                                    }

                                } else {// 如果不是外包

                                    if (listModels != null && listModels.size() > 0) {// vip
                                        homeworkManager.isVipOrg(this, dataJson);
                                        questionManager.getInstance()
                                                .goToStuPublishQuestionVipActivity(this, "", 0,
                                                        listModels);

                                    } else {// not vip
                                        homeworkManager.isVipOrg(this, dataJson);
                                        questionManager.goNotPublishQuestionVipActivity(this,
                                                PayAnswerAskActivity.class, null, false);
                                    }

                                }

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
