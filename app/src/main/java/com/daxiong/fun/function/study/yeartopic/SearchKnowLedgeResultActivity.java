
package com.daxiong.fun.function.study.yeartopic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.R;
import com.daxiong.fun.api.BeyondYeahExamAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.function.study.yeartopic.adapter.AnswerListAdapter;
import com.daxiong.fun.model.AnswerListItemGsonModel;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.XListView;
import com.daxiong.fun.view.XListView.IXListViewListener;
import com.daxiong.fun.view.XListViewFooter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 此类的描述： 知识点搜索结果页面
 * 
 * @author: sky
 */
public class SearchKnowLedgeResultActivity extends BaseActivity
        implements OnClickListener, IXListViewListener, OnScrollListener {

 

    private ArrayList<AnswerListItemGsonModel> currentList;

    private XListView search_result_list;

    private AnswerListAdapter mAdapter;
    

    
    private boolean isRefresh = true;

    private boolean isQuery = false;

    private boolean hasMore = true;

    private TextView nextStepTV;

    private RelativeLayout nextStepLayout;

    private int q_type;

    public static final String KEY_IS_QPAD = "is_qpad";
    private int pageIndex = 1;
    private static final int PAGE_COUNT = 10;

    
    //extra
    private BeyondYeahExamAPI yeahExamApi;

    private int type;

    private String keyword;

    private int gradeid;

    private int subjectGroupId;

    private int chapterGroupId;

    private int knowPointGroupId;

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            onLoadFinish();
        }
    };

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.search_knowledge_result_activity);
        getExtraData();
        initView();
        initListener();
    }

    public void getExtraData() {
        Intent getExtraIntent = getIntent();
        type = getExtraIntent.getIntExtra("type", 0);

        if (type == 1) {// 关键字搜索
            keyword = getExtraIntent.getStringExtra("keyword");
        } else if (type == 2) { // 知识点搜索
            gradeid = getExtraIntent.getIntExtra("gradeid", 0);
            subjectGroupId = getExtraIntent.getIntExtra("subjectGroupId", 0);
            chapterGroupId = getExtraIntent.getIntExtra("chapterGroupId", 0);
            knowPointGroupId = getExtraIntent.getIntExtra("knowPointGroupId", 0);
        }
    }

    @Override
    public void initView() {
        super.initView();
        yeahExamApi = new BeyondYeahExamAPI();   
        TextView titleTV = (TextView)findViewById(R.id.title);
        titleTV.setText(R.string.text_excellent_selction);
        nextStepLayout = (RelativeLayout)findViewById(R.id.next_setp_layout);
        nextStepTV = (TextView)findViewById(R.id.next_step_btn);
        nextStepTV.setVisibility(View.GONE);
        nextStepTV.setText(R.string.text_knowledge_point);
        nextStepTV.setBackgroundResource(R.drawable.publish_btn_selector);
        search_result_list = (XListView)findViewById(R.id.search_result_list);
        mAdapter = new AnswerListAdapter(this);
        search_result_list.setAdapter(mAdapter);
        if (currentList == null) {
            currentList = new ArrayList<AnswerListItemGsonModel>();
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        findViewById(R.id.back_layout).setOnClickListener(this);
        nextStepLayout.setOnClickListener(this);
        search_result_list.setAdapter(mAdapter);
        search_result_list.setPullLoadEnable(true);
        search_result_list.setPullRefreshEnable(true);
        search_result_list.setXListViewListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onEventBegin(this, "OneQuestionMoreAnswers");
        if (type == 1) {// 关键字搜索
            keySearch();
        } else {// 知识点搜索
            knowledgeSearch();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout:// 返回
                finish();
                break;
        }
    }
    

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onEventEnd(this, "OneQuestionMoreAnswers");
    }

    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        int flag = ((Integer)param[0]).intValue();
        switch (flag) {
            case RequestConstant.KEYWORD_SEARCH_CODE:// 关键字搜索
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        try {
                            String dataJson = JsonUtil.getString(datas, "Data", "");
                            if (!TextUtils.isEmpty(dataJson)) {
                                if (TextUtils.isEmpty(dataJson)) {
                                    hasMore = false;
                                } else {
                                    Gson gson = new Gson();
                                    ArrayList<AnswerListItemGsonModel> answerList = null;
                                    try {
                                        answerList = gson.fromJson(dataJson,
                                                new TypeToken<ArrayList<AnswerListItemGsonModel>>() {
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
                                        }
                                        currentList.addAll(answerList);
                                    }
                                    if (currentList.size() == 0) {
                                        ToastUtils.show(R.string.text_no_question);
                                    } else if (currentList.size() < 10) {
                                        ToastUtils.show(getString(R.string.text_question_just_have,
                                                currentList.size()));
                                    }
                                    mAdapter.setData(currentList);
                                }
                                onLoadFinish();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
                break;
            case RequestConstant.KNOWLEDGE_SEARCH_CODE:// 知识点搜索
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        try {
                            String dataJson = JsonUtil.getString(datas, "Data", "");
                            if (!TextUtils.isEmpty(dataJson)) {
                                if (TextUtils.isEmpty(dataJson)) {
                                    hasMore = false;
                                } else {
                                    Gson gson = new Gson();
                                    ArrayList<AnswerListItemGsonModel> answerList = null;
                                    try {
                                        answerList = gson.fromJson(dataJson,
                                                new TypeToken<ArrayList<AnswerListItemGsonModel>>() {
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
                                        }
                                        currentList.addAll(answerList);
                                    }
                                    if (currentList.size() == 0) {
                                        ToastUtils.show(R.string.text_no_question);
                                    } else if (currentList.size() < 10) {
                                        ToastUtils.show(getString(R.string.text_question_just_have,
                                                currentList.size()));
                                    }
                                    mAdapter.setData(currentList);
                                }
                                onLoadFinish();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
                break;

        }

    }

    

    // 根据输入的关键字搜索
    public void keySearch() {
        yeahExamApi.keySearchResult(requestQueue, pageIndex, PAGE_COUNT, keyword, this,
                RequestConstant.KEYWORD_SEARCH_CODE);
    }

    // 根据知识点搜索
    public void knowledgeSearch() {
        q_type=0;
        yeahExamApi.knowledgeSearchResult(requestQueue, q_type, pageIndex,PAGE_COUNT, gradeid,
                subjectGroupId, chapterGroupId, knowPointGroupId, this,
                RequestConstant.KNOWLEDGE_SEARCH_CODE);
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
    
    
    @Override
    public void onRefresh() {
        pageIndex = 1;
        hasMore = true;
        isRefresh = true;
        if (type==1) {
            keySearch();
        }else {
            knowledgeSearch();
        }
        
        search_result_list.getFooterView().setState(XListViewFooter.STATE_OTHER, "");
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        if (hasMore) {
            if (type==1) {
                keySearch();
            }else {
                knowledgeSearch();
            }
        } else {
            search_result_list.getFooterView().setState(XListViewFooter.STATE_NOMORE, "");
            onLoadFinish();
        }
    }

    public void onLoadFinish() {
        search_result_list.stopRefresh();
        search_result_list.stopLoadMore();
        DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(new Date());
        search_result_list.setRefreshTime(time);
    }

   
}
