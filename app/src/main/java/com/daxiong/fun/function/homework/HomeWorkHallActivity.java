
package com.daxiong.fun.function.homework;

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

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.R;
import com.daxiong.fun.api.HomeWorkAPI;
import com.daxiong.fun.api.StudyAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.function.homework.adapter.HomeWrokHallAdapter;
import com.daxiong.fun.function.homework.model.HomeWorkModel;
import com.daxiong.fun.manager.HomeworkManager;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.MyOrgModel;
import com.daxiong.fun.model.OrgModel;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.NetworkUtils;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.XListView;
import com.daxiong.fun.view.XListView.IXListViewListener;
import com.daxiong.fun.view.XListViewFooter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 此类的描述：作业大厅/我的作业
 * @author: Sky @最后修改人： Sky
 * @最后修改日期:2015-7-28 下午6:00:33
 */
public class HomeWorkHallActivity extends BaseActivity
        implements OnScrollListener, IXListViewListener, OnClickListener {

    public static final String TARGETNAME = "targetname";

    public static final String TARGETID = "targetid";

    public static final String PACKTYPE = "packtype";

    private static final int REQUEST_CODE = 1002;

    private static final String TAG = HomeWorkHallActivity.class.getSimpleName();

   // private TextView nextStepTV;

    private RelativeLayout nextStepLayout;

    private XListView mListView;

    private int pageIndex = 1;

    private static final int pageCount = 10;

    private boolean isRefresh = true;

    private HomeWrokHallAdapter mHomeWrokHallAdapter;

    private ArrayList<HomeWorkModel> mHomeWorkList;

    private boolean hasMore = true;

    public static boolean reFlesh;

    private int packtype;

    private int targetid;

    private String titleName;

    private HomeWorkAPI homeworkApi;

    private StudyAPI studyApi;

    private HomeworkManager homeworkManager;//homework管理类

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            onLoadFinish();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_answer_list);        
        initView();
        getExtraData();
        initListener();
        initData(packtype);
        

    }

    @Override
    public void initView() {
        super.initView();
        mListView = (XListView)findViewById(R.id.answer_list);
        findViewById(R.id.top_bar_container).setVisibility(View.GONE);

        findViewById(R.id.back_layout).setOnClickListener(this);
        nextStepLayout = (RelativeLayout)findViewById(R.id.next_setp_layout);
      //  nextStepTV = (TextView)findViewById(R.id.next_step_btn);
       // nextStepTV.setBackgroundResource(R.drawable.publish_btn_selector);

        mHomeWrokHallAdapter = new HomeWrokHallAdapter(this);
        mListView.setAdapter(mHomeWrokHallAdapter);
        mHomeWorkList = new ArrayList<HomeWorkModel>();
        homeworkApi = new HomeWorkAPI();
        homeworkManager = HomeworkManager.getInstance();
    }

    private void getExtraData() {
        Intent intent = getIntent();
        if (intent != null) {
            packtype = intent.getIntExtra(HomeWorkHallActivity.PACKTYPE, 0);
            if (packtype == 4) {
                targetid = intent.getIntExtra(HomeWorkHallActivity.TARGETID, 0);
                titleName = intent.getStringExtra(HomeWorkHallActivity.TARGETNAME);
            }

        }
        if (packtype != 3) {
           // nextStepTV.setVisibility(View.VISIBLE);
           // nextStepTV.setText(R.string.fabu_homewrok_btn_text);           
            nextStepLayout.setOnClickListener(this);            
        }
        switch (packtype) {
            case 0:
                setWelearnTitle(R.string.homewrok_hall_title_text);
              //  nextStepTV.setText(R.string.fabu_homewrok_btn_text);
                break;
            case 1:
                setWelearnTitle(R.string.homewrok_hall_title_text);
                uMengEvent("homework_hall");
                break;
            case 2:// 从我的作业检查跳转过来
                uMengEvent("homework_mycheck");
                setWelearnTitle(R.string.my_homework_title_text);                
               // nextStepTV.setText("作业分析");
                break;
            case 4:
                uMengEvent("homework_other");
                setWelearnTitle(titleName);
                break;
            case 6://从老师搜索中查询到
                uMengEvent("homewrok_other");
                String title=intent.getStringExtra("title");
                targetid=intent.getIntExtra("targetid", 0);
                setWelearnTitle(title);
                break;

            default:
                break;
        }

    }

    @Override
    public void initListener() {
        super.initListener();
        mListView.setPullLoadEnable(true);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this);
    }

    /**
     * @param packtype # 0, 1, 2, 3 0代表广场， 1代表发完作业之后的跳转广场， 2表示我的作业集， 3表示我的回答集 ,
     *            4别人的作业集
     */
    private void initData(int packtype) {
        // JSONObject data = new JSONObject();
        // try {
        // data.put("packtype", packtype);
        // data.put("pageindex", pageIndex);
        // data.put("pagecount", pageCount);
        // if (packtype == 4) {
        // data.put("userid", targetid);
        // }
        // } catch (JSONException e) {
        // e.printStackTrace();
        // }
        // handler.sendEmptyMessageDelayed(0, 7000);
        // HttpHelper.post(this, "getall", data, new HttpListener() {
        //
        // @Override
        // public void onFail(int code) {
        // showNetWorkExceptionToast();
        // }
        //
        // @Override
        // public void onSuccess(int code, String dataJson, String errMsg) {
        // if (ResponseCmdDef.CODE_RETURN_OK == code) {
        //
        // if (null == dataJson) {
        // hasMore = false;
        // } else {
        // ArrayList<HomeWorkModel> loadHomeWorkList = null;
        // try {
        // loadHomeWorkList = new Gson().fromJson(dataJson,
        // new TypeToken<ArrayList<HomeWorkModel>>() {
        // }.getType());
        // } catch (JsonSyntaxException e) {
        // e.printStackTrace();
        // }
        // if (isRefresh) {
        // mHomeWorkList.clear();
        // }
        // if (loadHomeWorkList != null && !loadHomeWorkList.isEmpty()) {
        // mHomeWorkList.addAll(loadHomeWorkList);
        // pageIndex++;
        // }
        // if (mHomeWorkList.size() == 0) {
        // ToastUtils.show(R.string.text_no_question);
        // } else if (mHomeWorkList.size() < 10) {
        // ToastUtils.show(getString(R.string.text_question_just_have,
        // mHomeWorkList.size()));
        // } else {
        // }
        // mHomeWrokHallAdapter.setData(mHomeWorkList,
        // HomeWorkHallActivity.this.packtype);
        // if (isRefresh) {
        // mListView.setSelection(0);
        // }
        // }
        // onLoadFinish();
        // } else {
        // ToastUtils.show(errMsg);
        // }
        //
        // }
        // });

        handler.sendEmptyMessageDelayed(0, 7000);
        if (NetworkUtils.getInstance().isInternetConnected(this)) {
            showDialog("正在加载数据...");
            homeworkApi.geHomeworkHall(requestQueue, packtype, pageIndex, pageCount, targetid, this,
                    RequestConstant.GET_HOME_WORK_TALL_CODE);
        }else {
            ToastUtils.show("请检查你的网络");
        }
     
    }

    public void getTall() {

    }

    // public void setPageIndex(int pageIndex) {
    // this.pageIndex = pageIndex;
    // }
    //
    // public void setIsRefresh(boolean isRefresh) {
    // this.isRefresh = isRefresh;
    // }

    @Override
    public void onResume() {
        super.onResume();
        if (reFlesh && packtype == 0) {
            published();

        }
        MobclickAgent.onEventBegin(this, TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onEventEnd(this, TAG);
    }

    public void onLoadFinish() {
        // isRefresh = true;
        mListView.stopRefresh();
        mListView.stopLoadMore();
        DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(new Date());
        mListView.setRefreshTime(time);
    }

    @Override
    public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
        mHomeWrokHallAdapter.setScrolling(true);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            mHomeWrokHallAdapter.setScrolling(false);
        }
    }

    // public void scrollToRefresh() {
    // if (mListView != null) {
    // mListView.showHeaderRefreshing();
    // mListView.setSelection(0);
    // isRefresh = true;
    // }
    // }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        hasMore = true;
        isRefresh = true;
        initData(packtype);
        mListView.getFooterView().setState(XListViewFooter.STATE_OTHER, "");
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        if (hasMore) {
            initData(packtype);
        } else {
            mListView.getFooterView().setState(XListViewFooter.STATE_NOMORE, "");
            onLoadFinish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mHomeWrokHallAdapter) {
            mHomeWrokHallAdapter.setData(null, 0);
        }
        if (mHomeWorkList != null) {
            mHomeWorkList.clear();
        }
        mListView.setXListViewListener(null);
        pageIndex = 1;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:// 返回
                finish();
                break;
            case R.id.next_setp_layout:// 发布作业/作业分析
                switch (packtype) {
                    case 0:
                    case 1:
                        uMengEvent("homework_publishfromhall");
                        break;
                    case 2://跳转到作业分析
                        uMengEvent("homework_publishfrommy");
                        Intent intent=new Intent(this,HomeworkAnalysisActivity.class);
                        HomeWorkModel item=null;
                        for (int i = 0; i < mHomeWorkList.size(); i++) {
                            HomeWorkModel entity=mHomeWorkList.get(i);
                            if (entity.getState()==2||entity.getState()==4) {
                                item=entity;
                                break;
                            }
                        }
                        if (item!=null) {
                            intent.putExtra("homeworkmodel", item);
                            startActivity(intent);
                        }else {
                            ToastUtils.show("只有已采纳或者已解答才能显示作业分析,再加载一页试试");                            
                        }  
                        return;                       

                    default:
                        break;
                }
                boolean isShow = MySharePerfenceUtil.getInstance().isShowHomeworkGuide();
                if (isShow) {
                    IntentManager.goToStuPublishHomeWorkGuideActivity(this);
                } else {
                    // IntentManager.goToStuPublishHomeWorkActivity(this);
                    // 请求接口判断进入的页面
                    requestMyOrgs();

                }
                break;
            case R.id.avatar_iv_hall_item_common:
                Integer userid = (Integer)v.getTag();
                IntentManager.gotoPersonalPage(this, userid, GlobalContant.ROLE_ID_STUDENT);
                break;
            case R.id.tec_avatar_iv_hall_item:
                Integer grabuserid = (Integer)v.getTag();
                IntentManager.gotoPersonalPage(this, grabuserid, GlobalContant.ROLE_ID_COLLEAGE);
                break;
            default:
                break;
        }
    }

    private void requestMyOrgs() {
        studyApi = new StudyAPI();
        // 请求我的机构
        // if (WeLearnSpUtil.getInstance().isOrgVip()) {
        // studyApi.queryMyOrgs(requestQueue, 1, 1, 1000, this,
        // RequestConstant.REQUEST_MY_ORGS);
        // }else {
        // IntentManager.openActivityForResult(HomeWorkHallActivity.this,
        // StuPublishHomeWorkActivity.class, null, false, 1002);
        // }

        studyApi.queryMyOrgs(requestQueue, 1, 1, 1000, this, RequestConstant.REQUEST_MY_ORGS);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // if (resultCode == RESULT_OK) {
        // if (requestCode == REQUEST_CODE) {
        // published();
        // }
        // }
    }

    private void published() {
        reFlesh = false;
        pageIndex = 1;
        hasMore = true;
        isRefresh = true;
        initData(1);
        mListView.getFooterView().setState(XListViewFooter.STATE_OTHER, "");
    }

    public void showCollectingDialog() {
        showDialog("请稍候");
    }

    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        int flag = ((Integer)param[0]).intValue();
        switch (flag) {
            case RequestConstant.GET_HOME_WORK_TALL_CODE:// 得到homework tall
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();                    
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        try {
                            String dataJson = JsonUtil.getString(datas, "Data", "");
                            closeDialog();
                            ArrayList<OrgModel> orgList = null;
                            if (!TextUtils.isEmpty(dataJson)) {

                                if (null == dataJson) {
                                    hasMore = false;
                                } else {
                                    ArrayList<HomeWorkModel> loadHomeWorkList = null;
                                    try {
                                        loadHomeWorkList = new Gson().fromJson(dataJson,
                                                new TypeToken<ArrayList<HomeWorkModel>>() {
                                                }.getType());
                                    } catch (JsonSyntaxException e) {
                                        e.printStackTrace();
                                    }
                                    if (isRefresh) {
                                        mHomeWorkList.clear();
                                    }
                                    if (loadHomeWorkList != null && !loadHomeWorkList.isEmpty()) {
                                        if (loadHomeWorkList.size()<10) {
                                            mListView.setPullLoadEnable(false);
                                        }
                                        mHomeWorkList.addAll(loadHomeWorkList);
                                        pageIndex++;
                                    }
                                    if (mHomeWorkList.size() == 0) {
                                        ToastUtils.show(R.string.text_no_question);                                        
                                    } else if (mHomeWorkList.size() < 10) {
                                        ToastUtils.show(getString(R.string.text_question_just_have,
                                                mHomeWorkList.size()));                                        
                                       
                                    } else {
                                    }
                                    mHomeWrokHallAdapter.setData(mHomeWorkList,
                                            HomeWorkHallActivity.this.packtype);
                                    
                                    if (isRefresh) {
                                        mListView.setSelection(0);
                                    }
                                }
                                onLoadFinish();
                            }
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
                                ArrayList<OrgModel> listModels=myOrgModel.getOrgList();
                                boolean isWaibao=homeworkManager.compareValueIsWaibao(listModels);
                                //如果是外包
                                if (isWaibao) { 
                                    
                                    if (myOrgModel.getSpecialuser() != null&& myOrgModel.getSpecialuser().size() > 0) {                                        
                                        int type = myOrgModel.getSpecialuser().get(0).getType();// 表示是否是特殊学生
                                        if (type == 1) {// 是特殊学生                                            
                                            
                                            if (listModels != null && listModels.size() > 0) {
                                                MySharePerfenceUtil.getInstance().setOrgVip();
                                                homeworkManager.goToOutsouringHomeWorkActivity(
                                                        HomeWorkHallActivity.this, "", 0, listModels,type); 
                                            } else {
                                                MySharePerfenceUtil.getInstance().setNotOrgVip();
                                            }

                                        } 
                                        
                                        if(type==0) {// 不是特殊学生
                                            homeworkManager.isVipOrg(this, dataJson);
                                            homeworkManager.goToOutsouringHomeWorkActivity(
                                                    HomeWorkHallActivity.this, "", 0, listModels,type); 
                                        }
                                    }else {//外包非特殊学生
                                        homeworkManager.isVipOrg(this, dataJson);
                                        homeworkManager.goToOutsouringHomeWorkActivity(
                                                HomeWorkHallActivity.this, "", 0, listModels,0); 
                                    }                                     
                                    
                                }else {//如果不是外包
                                    
                                    if (listModels!=null&&listModels.size()>0) {//vip
                                        homeworkManager.isVipOrg(this, dataJson);
                                        IntentManager.goToStuPublishHomeWorkVipActivity(this, "", 0, listModels);
                                        
                                    }else {//not vip
                                        homeworkManager.isVipOrg(this, dataJson);
                                        homeworkManager.goNotHomeworkVipActivity(this,
                                                PublishHomeWorkActivity.class, null, false);
                                    }
                                    
                                   
                                }   
                                
                                
                            }

                        } catch (Exception e) {
                            e.printStackTrace();                            
                        }
                    }else {
                       ToastUtils.show(msg); 
                       
                    }

                }
                break;

        }

    }

}
