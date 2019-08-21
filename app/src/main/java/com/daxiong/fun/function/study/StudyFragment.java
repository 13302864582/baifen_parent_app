
package com.daxiong.fun.function.study;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.R;
import com.daxiong.fun.api.StudyAPI;
import com.daxiong.fun.base.BaseFragment;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.EventConstant;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.function.course.MastersCourseActivity;
import com.daxiong.fun.function.homework.HomeWorkHallActivity;
import com.daxiong.fun.function.homework.adapter.FuncAdapter;
import com.daxiong.fun.function.question.PayAnswerAskActivity;
import com.daxiong.fun.function.question.PayAnswerAskGuideActivity;
import com.daxiong.fun.function.question.adapter.QAHallListAdapter;
import com.daxiong.fun.function.question.model.AnswerListItemModel;
import com.daxiong.fun.function.study.yeartopic.YearQuestionActivity;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.HomeworkManager;
import com.daxiong.fun.manager.QuestionManager;
import com.daxiong.fun.model.FuncModel;
import com.daxiong.fun.model.MyOrgModel;
import com.daxiong.fun.model.OrgModel;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.DebugActvity;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.NetworkUtils;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.MyGridView;
import com.daxiong.fun.view.XListView;
import com.daxiong.fun.view.XListView.IXListViewListener;
import com.daxiong.fun.view.XListViewFooter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 此类的描述： 学习Fragment
 * 
 * @author: sky @最后修改人： sky
 * @最后修改日期:2015-7-31 上午10:00:20
 */
public class StudyFragment extends BaseFragment implements OnClickListener, OnItemClickListener,
        IXListViewListener, OnScrollListener, HttpListener {
    private static final int PAGE_COUNT = 10;

    public static final String TAG = StudyFragment.class.getSimpleName();

    // 该列表后期做成数据，后台可以配置
    private List<FuncModel> mFuncModels = new ArrayList<FuncModel>();

    private MyGridView mFuncGridView;

    private FuncAdapter mFuncAdapter;

    private XListView mXListView;

    private QAHallListAdapter mAdapter;

    private int pageIndex = 1;

    private boolean isRefresh = true;

    private ArrayList<AnswerListItemModel> currentList;

    private boolean hasMore = true;

    private Button debug_bt;

    private UserInfoModel uInfo;

    private StudyAPI studyApi;

    private HomeworkManager homeworkManager;

    private QuestionManager questionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gas_student_station, container, false);
        initView(view);
        initListener();
        initBlock();
        initData();
        return view;
    }

    public void initView(View view) {
        studyApi = new StudyAPI();
        homeworkManager = HomeworkManager.getInstance();
        questionManager = questionManager.getInstance();
        uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
        if (null == uInfo) {
//            ToastUtils.show(R.string.text_unlogin);
//            getActivity().finish();
        }

        debug_bt = (Button)view.findViewById(R.id.ip_change_debug_bt);
        if (AppConfig.IS_DEBUG) {
            debug_bt.setVisibility(View.VISIBLE);
            debug_bt.setOnClickListener(this);
        } else {
        }

        mFuncGridView = (MyGridView)view.findViewById(R.id.func_gridView);
        mFuncAdapter = new FuncAdapter(getActivity(), mFuncModels);
        mFuncGridView.setAdapter(mFuncAdapter);

        mXListView = (XListView)view.findViewById(R.id.x_list);
        mAdapter = new QAHallListAdapter(getActivity());
        mXListView.setAdapter(mAdapter);
        mAdapter.setGoTag(1);//1表示从悬赏问答跳转到问题解答详情页，为了纠错这个功能多个入口
        if (currentList == null) {
            currentList = new ArrayList<AnswerListItemModel>();
        }

    }

    public void initListener() {
        mFuncGridView.setOnItemClickListener(this);
        mXListView.setPullLoadEnable(true);
        mXListView.setPullRefreshEnable(true);
        mXListView.setXListViewListener(this);
    }

    /**
     * 此方法描述的是：初始化模块功能
     * 
     * @author: sky
     * @最后修改日期:2015年8月5日 下午6:51:12 initBlock void
     */
    private void initBlock() {

        // GoldNotLessActivity

        // 拍照提问
        String className = PayAnswerAskActivity.class.getCanonicalName();
        mFuncModels.add(new FuncModel(R.drawable.index_btn_photo_selector,
                getString(R.string.student_home_func_photo), className));

        // 作业检查
        className = HomeWorkHallActivity.class.getCanonicalName();
        mFuncModels.add(new FuncModel(R.drawable.index_btn_check_selector,
                getString(R.string.student_home_func_work_check), className));

        // 历年考题
        className = YearQuestionActivity.class.getCanonicalName();
        mFuncModels.add(new FuncModel(R.drawable.index_btn_questions_selector,
                getString(R.string.student_home_func_questions), className));

        // 微课辅导
        className = MastersCourseActivity.class.getCanonicalName();
        mFuncModels.add(new FuncModel(R.drawable.index_class_bg_selector,
                getString(R.string.student_home_func_class), className));
    }

    public void initData() {
        // JSONObject data = new JSONObject();
        // try {
        // data.put("packtype", 0);// 0代表广场， 1代表发完悬赏问答之后跳转到广场， 2表示我的问集， 3表示我的答集
        // data.put("pageindex", pageIndex);
        // data.put("pagecount", PAGE_COUNT);
        // // data.put("gradegroup", uInfo.getGradeid());
        // } catch (JSONException e) {
        // e.printStackTrace();
        // }
        // HttpHelper.excutePost(getActivity(), "question", "getall", this,
        // data, null);
        if (NetworkUtils.getInstance().isInternetConnected(getActivity())) {
            showDialog(getActivity().getString(R.string.please_wait));
            // 0代表广场， 1代表发完悬赏问答之后跳转到广场， 2表示我的问集， 3表示我的答集
            studyApi.geHall(requestQueue, 0, pageIndex, PAGE_COUNT, this,
                    RequestConstant.GET_TALL_CODE);
        } else {
            ToastUtils.show("网络不可用,请检查网络连接");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onEventBegin(getActivity(), EventConstant.CUSTOM_EVENT_GASSTATION);
        // WeLearnApi.getSignLog();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onEventEnd(getActivity(), EventConstant.CUSTOM_EVENT_GASSTATION);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ip_change_debug_bt:
                getActivity().startActivity(new Intent(getActivity(), DebugActvity.class));
                break;
        }
    }

   
    
    @Override
	public void onSuccess(int code, String dataJson, String errMsg) {
        // if (TextUtils.isEmpty(dataJson)) {
        // hasMore = false;
        // } else {
        // Gson gson = new Gson();
        // List<AnswerListItemModel> answerList = null;
        // try {
        // answerList = gson.fromJson(dataJson, new
        // TypeToken<List<AnswerListItemModel>>() {
        // }.getType());
        // pageIndex++;
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        //
        // if (isRefresh) {
        // currentList.clear();
        // }
        // if (answerList != null && !answerList.isEmpty()) {
        // currentList.addAll(answerList);
        // }
        // if (currentList.size() == 0) {
        // ToastUtils.show(R.string.text_no_question);
        // } else if (currentList.size() < 10) {
        // ToastUtils.show(getString(R.string.text_question_just_have,
        // currentList.size()));
        // }
        // mAdapter.setData(currentList);
        // }
        // onLoadFinish();

    
		
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {
		
		
	}

    /**
     * 顶部的四个功能模块的点击事件(拍照提问/作业检查/历年考题/微课辅导)
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        try {
            FuncModel fm = mFuncModels.get(arg2);
            String cName = fm.getClassName();
            if (PayAnswerAskActivity.class.getCanonicalName().equals(cName)) { // 拍照提问
                boolean isShowGuide = MySharePerfenceUtil.getInstance().isShowAskGuide();
                if (isShowGuide) {
                    cName = PayAnswerAskGuideActivity.class.getCanonicalName();
                    Class<?> cls = Class.forName(cName);
                    Intent i = new Intent(getActivity(), cls);
                    startActivity(i);
                } else {
                    requestMyOrgs();
                }
            } else {// 点击其他的跳转
                Class<?> cls = Class.forName(cName);
                Intent i = new Intent(getActivity(), cls);
                startActivity(i);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        hasMore = true;
        isRefresh = true;
        initData();
        mXListView.getFooterView().setState(XListViewFooter.STATE_OTHER, "");
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        if (hasMore) {
            initData();
        } else {
            mXListView.getFooterView().setState(XListViewFooter.STATE_NOMORE, "");
            onLoadFinish();
        }
    }

    @SuppressLint("SimpleDateFormat")
    public void onLoadFinish() {
        mXListView.stopRefresh();
        mXListView.stopLoadMore();
        DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(new Date());
        mXListView.setRefreshTime(time);
    }

    @Override
    public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
        mAdapter.setScrolling(true);
    }

    @Override
    public void onScrollStateChanged(AbsListView arg0, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            mAdapter.setScrolling(false);
        }
    }

    private void requestMyOrgs() {
        if (NetworkUtils.getInstance().isInternetConnected(getActivity())) {
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
            case RequestConstant.GET_TALL_CODE:// 查询学习Fragment
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        try {
                            String dataJson = JsonUtil.getString(datas, "Data", "");
                            closeDialog();
                            if (TextUtils.isEmpty(dataJson)) {
                                hasMore = false;
                            } else {
                                Gson gson = new Gson();
                                List<AnswerListItemModel> answerList = null;
                                try {
                                    answerList = gson.fromJson(dataJson,
                                            new TypeToken<List<AnswerListItemModel>>() {
                                            }.getType());
                                    pageIndex++;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (isRefresh) {
                                    currentList.clear();
                                }
                                if (answerList != null && !answerList.isEmpty()) {
                                    if (answerList.size()<10) {
                                        mXListView.setPullLoadEnable(false);
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

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
                break;

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
                                                        getActivity(), "", 0, listModels, type);
                                            } else {
                                                MySharePerfenceUtil.getInstance().setNotOrgVip();
                                            }

                                        }

                                        if (type == 0) {// 不是特殊学生
                                            homeworkManager.isVipOrg(getActivity(), dataJson);
                                            questionManager.goToOutsouringQuestionActivity(
                                                    getActivity(), "", 0, listModels, type);
                                        }
                                    } else {// 外包不是特殊学生
                                        homeworkManager.isVipOrg(getActivity(), dataJson);
                                        questionManager.goToOutsouringQuestionActivity(
                                                getActivity(), "", 0, listModels, 0);
                                    }

                                } else {// 如果不是外包

                                    if (listModels != null && listModels.size() > 0) {// vip
                                        homeworkManager.isVipOrg(getActivity(), dataJson);
                                        questionManager.getInstance()
                                                .goToStuPublishQuestionVipActivity(getActivity(),
                                                        "", 0, listModels);

                                    } else {// not vip
                                        homeworkManager.isVipOrg(getActivity(), dataJson);
                                        questionManager.goNotPublishQuestionVipActivity(
                                                getActivity(), PayAnswerAskActivity.class, null,
                                                false);
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
