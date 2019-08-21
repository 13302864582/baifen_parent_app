
package com.daxiong.fun.function.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MainActivity;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.api.HomeListAPI;
import com.daxiong.fun.api.WeLearnApi;
import com.daxiong.fun.base.BaseFragment;
import com.daxiong.fun.callback.INetWorkListener;
import com.daxiong.fun.constant.MessageConstant;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.dispatch.HomepageController;
import com.daxiong.fun.function.communicate.MessageMainListActivity;
import com.daxiong.fun.function.homepage.adapter.HomeAdapter;
import com.daxiong.fun.function.homepage.model.HomeListModel;
import com.daxiong.fun.function.homework.PublishHwActivity;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.model.ChatInfo;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.DateUtil;
import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MyAsyncTask;
import com.daxiong.fun.util.NetworkUtils;
import com.daxiong.fun.util.SharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.XListView;
import com.daxiong.fun.view.XListView.IXListViewListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 主页Fragmnet
 *
 * @author: sky
 */
public class HomeFragment extends BaseFragment implements IXListViewListener, INetWorkListener, AdapterView.OnItemClickListener {

    public static final String TAG = HomeFragment.class.getSimpleName();


    private MainActivity activity;
    boolean flag;
    private View view;
    private XListView xListView;
    private HomeAdapter homeAdapter;
    private HomeListAPI homeListAPI;
    private long errortime;
    private int headerindex = 1;
    private int index = 0;
    private int first = -1;
    private int last = -1;
    private int page = 0;
    private AuToUpdateTask mAuToUpdateTask;
    private AuToRunTask runTask;
    private HomepageController homepageController;


    private List<HomeListModel> list = new ArrayList<HomeListModel>();
    private List<HomeListModel> list3 = new ArrayList<HomeListModel>();
    private List<Integer> list2 = new ArrayList<Integer>();
    View headview;
    public Button bt_jiancha;
    public TextView tv_remark,tv_meiti;


    private RelativeLayout backLayout;
    private RelativeLayout titleLayout;
    public ImageView iv_choose;
    private RelativeLayout nextLayout;
    private TextView tv_red;
    private static boolean isDoInDB;
    private static long reflashTime;
    private List<ChatInfo> infos;
    private int currentIndex = 0;
    public static View unReadMsgPointIv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, null);
        initView(view);
        initListener();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void initView(View view) {
        // 左边的按钮
        backLayout = (RelativeLayout) view.findViewById(R.id.back_layout);
        ImageView backImageView = (ImageView) view.findViewById(R.id.back_iv);
        backImageView.setScaleType(ImageView.ScaleType.CENTER);
        // backImageView.setImageResource(R.drawable.icon_stu_home_menu_persion_btn_selector);
        backLayout.setVisibility(View.GONE);
        //右边按钮
        nextLayout = (RelativeLayout)view. findViewById(R.id.next_setp_layout);
        nextLayout.setVisibility(View.VISIBLE);
        ImageView nextImageView = (ImageView) view.findViewById(R.id.next_step_img);
        nextImageView.setVisibility(View.VISIBLE);
        nextImageView.setBackgroundResource(R.drawable.form_message_btn_normal);
        unReadMsgPointIv = (ImageView) view.findViewById(R.id.unread_msg_point_iv_main);
        tv_red = (TextView)view.findViewById(R.id.tv_red);
        //中间的部分
        titleLayout = (RelativeLayout) view.findViewById(R.id.title_layout);
        iv_choose = (ImageView) view.findViewById(R.id.iv_choose);
        iv_choose.setVisibility(View.GONE);



        xListView = (XListView) view.findViewById(R.id.answer_list);
        tv_meiti = (TextView) view.findViewById(R.id.tv_meiti);
        headview = View.inflate(getActivity(), R.layout.first_head_layout, null);

        bt_jiancha = (Button) headview.findViewById(R.id.bt_jiancha);
        tv_remark = (TextView) headview.findViewById(R.id.tv_remark);
        activity = (MainActivity) getActivity();

        homeListAPI = new HomeListAPI();
        homeAdapter = new HomeAdapter(activity, list);
        xListView.setAdapter(homeAdapter);

    }

    @Override
    public void initListener() {
        super.initListener();
        nextLayout.setOnClickListener(this);
        xListView.setXListViewListener(this);
        xListView.setOnItemClickListener(this);
        xListView.setPullRefreshEnable(true);
        xListView.setPullLoadEnable(false);
        bt_jiancha.setOnClickListener(this);


    }

    public void initData() {
        if (NetworkUtils.getInstance().isInternetConnected(getActivity())) {
            showDialog("数据加载中...");
            homeListAPI.homeListContext(requestQueue, page, 10, this, RequestConstant.GET_HOME_CONTEXT_CODE);
        } else {
            ToastUtils.show("请检查您的网络");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.e(TAG, "onResume");
        showMessageList();
        page = 0;

        initData();
        if (homepageController == null) {
            homepageController = new HomepageController(null, HomeFragment.this);
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


    public void auToUpdate() {
        int firstVisiblePosition = xListView.getFirstVisiblePosition();
        int lastVisiblePosition = xListView.getLastVisiblePosition();
        first = firstVisiblePosition / 5;
        last = lastVisiblePosition / 5;
        last = last <= page ? last : page;
        if (list2 == null) {
            first = -1;
            last = -1;
        } else if (list2 != null && list2.size() == 0) {
            first = -1;
            last = -1;

        } else if (list2 != null && list2.size() != 0) {
            boolean falgfirst = false;
            boolean falglast = false;
            int first2 = (first + 1) * 5;
            int last2 = (last + 1) * 5;
            for (Integer i : list2) {
                if (first2 > i && (first2 - 5) <= i) {
                    falgfirst = true;
                }
                if (lastVisiblePosition > i && (last2 - 5) <= i) {
                    falglast = true;
                }
            }
            first = falgfirst ? first : -1;
            last = falglast ? last : -1;
        }

        if (first == -1) {
            return;
        } else {
            index = first * 5;
            if (NetworkUtils.getInstance().isInternetConnected(getActivity())) {
                showDialog("数据加载中...");
                homeListAPI.homeListContext(requestQueue, first, 10, this,
                        RequestConstant.GET_HOME_CONTEXT_CODE);
            } else {
                ToastUtils.show("请检查您的网络");
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_setp_layout:// 右边按钮
                MobclickAgent.onEvent(getActivity(), "Open_ChatList");
                Intent intent = new Intent(getActivity(), MessageMainListActivity.class);
                startActivity(intent);
                break;

            case R.id.bt_jiancha:// 立即检查
                Intent hwintent = new Intent(activity, PublishHwActivity.class);
                startActivity(hwintent);
                break;


        }
    }

    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        onLoadFinish();

        int flag = ((Integer) param[0]).intValue();
        switch (flag) {
            case RequestConstant.GET_HOME_CONTEXT_CODE:
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    closeDialog();
                    if (code == 0) {
                        try {
                            String dataJson = JsonUtil.getString(datas, "Data", "");

                            if (!TextUtils.isEmpty(dataJson)&&dataJson.length()>2) {
                                String listJson = JsonUtil.getString(dataJson, "list", "");
                                String today_publish_task_infos = JsonUtil.getString(dataJson, "today_publish_task_infos", "");
                                int is_today_publish_task = JsonUtil.getInt(today_publish_task_infos, "is_today_publish_task", 0);
                                String remark = JsonUtil.getString(today_publish_task_infos, "remark", "");
                                tv_remark.setText(remark);


                                List<HomeListModel> parseArray = JSON.parseArray(listJson, HomeListModel.class);

                                if (parseArray != null) {


                                    if (first == -1) {
                                        if (page == 0) {
                                            this.list.clear();
                                        }
                                        if (parseArray.size() < 10) {
                                            xListView.setPullLoadEnable(false);

                                        } else {
                                            xListView.setPullLoadEnable(true);

                                        }
                                        this.list.addAll(parseArray);
                                        if (is_today_publish_task == 0&&list.size()>0) {
                                            if (headerindex == 1) {
                                                xListView.addHeaderView(headview);
                                                headerindex = 2;
                                            }
                                            if(TextUtils.isEmpty(remark)){
                                                tv_remark.setText("好的成绩，从作业开始。");
                                            }
                                            else{
                                                tv_remark.setText(remark);
                                            }

                                        } else {
                                            if (headerindex == 2) {
                                                xListView.removeHeaderView(headview);
                                                headerindex = 1;
                                            }
                                        }
                                        if (mAuToUpdateTask == null) {
                                            mAuToUpdateTask = new AuToUpdateTask();
                                        }
                                        MyApplication.getMainThreadHandler().removeCallbacks(mAuToUpdateTask);
                                        MyApplication.getMainThreadHandler().postDelayed(mAuToUpdateTask, 1000 * 60 * 10);
                                        updateListUI();
                                    } else {
                                        list3.addAll(parseArray);
                                        if (last == -1 | last == first) {
                                            first = -1;
                                            last = -1;
                                            for (int i = 0; i < list3.size(); i++) {
                                                HomeListModel remove = this.list.remove(index);
                                                if (remove != null) {
                                                    HomeListModel myListModel = list3.get(i);

                                                    this.list.add(index, myListModel);
                                                }
                                                index++;
                                            }
                                            index = 0;
                                            list3.clear();
                                            updateListUI();
                                            if (mAuToUpdateTask == null) {
                                                mAuToUpdateTask = new AuToUpdateTask();
                                            }
                                            MyApplication.getMainThreadHandler().removeCallbacks(mAuToUpdateTask);
                                            MyApplication.getMainThreadHandler().postDelayed(mAuToUpdateTask, 1000 * 60 * 10);
                                        } else {
                                            first = last;
                                            homeListAPI.homeListContext(requestQueue, last, 5, this,
                                                    RequestConstant.GET_HOME_CONTEXT_CODE);
                                        }
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

            case -1:
                closeDialog();
                ToastUtils.show("服务器或网络超时,试试重新下拉以下");
                break;

        }

    }


    public void updateListUI() {
        if(list.size()==0){
            tv_meiti.setVisibility(View.VISIBLE);
        }else{
            tv_meiti.setVisibility(View.GONE);
        }
        list2.clear();
        for (int i = 0; i < list.size(); i++) {
            HomeListModel homeListModel = list.get(i);
            int task_type = homeListModel.getTask_type();
            if ( (task_type == 2 && homeListModel.getHomework_state() == 1)|(task_type == 1 && homeListModel.getAnswer_state()==0)) {
                list2.add(i);
            }
        }
        homeAdapter.notifyDataSetChanged();
        if (list2.size() > 0) {
            if (runTask == null) {
                runTask = new AuToRunTask();
            }
            runTask.start();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int lastVisiblePosition = xListView.getLastVisiblePosition();
        int firstVisiblePosition = xListView.getFirstVisiblePosition();
        ToastUtils.show(firstVisiblePosition + "----"+lastVisiblePosition);
    }

    public class AuToUpdateTask implements Runnable {

        @Override
        public void run() {
            auToUpdate();
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
                   if(i>=firstVisiblePosition&&lastVisiblePosition>=i) {
                       HomeListModel homeListModel = list.get(i);

                       TextView tv_zuoyejieguo = (TextView) xListView.findViewWithTag("tv_zuoyejieguo" + i);
                       LinearLayout ll_jindu = (LinearLayout) xListView.findViewWithTag("ll_jindu" + i);
                       long errortime = SharePerfenceUtil.getLong("errortime", 0);
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


                           int dip2px = DensityUtil.dip2px(activity, percent * 2);
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
    public void onPre() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onException() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAfter(String jsonStr, int msgDef) {
        switch (msgDef){
            case MessageConstant.MSG_DEF_HOME_LIST:
                String dataJson = JsonUtil.getString(jsonStr, "data", "");
                String dataJson2 = JsonUtil.getString(dataJson, "content", "");
                if (!"".equals(dataJson2)) {
                    HomeListModel parseObject = JSON.parseObject(dataJson2, HomeListModel.class);
                    for (int i = 0; i < list.size(); i++) {
                        if (parseObject.getCreate_time() == list.get(i).getCreate_time()) {
                            HomeListModel remove = list.remove(i);
                            if ( parseObject.getHomework_state() == 9) {
                            } else {
                                list.add(i, parseObject);

                            }
                            break;
                        }
                    }
                    updateListUI();
                }
                break;
            case MessageConstant.MSG_DEF_MSGS:
                showMessageList();
                reflashTime = System.currentTimeMillis();// 更新消息收到的时间
                break;
        }




    }

    @Override
    public void onDisConnect() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRefresh() {

        page = 0;
        initData();
    }

    @Override
    public void onLoadMore() {

        page++;
        initData();
    }

    public void onLoadFinish() {
        // isRefresh = true;
        xListView.stopRefresh();
        xListView.stopLoadMore();
        DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(new Date());
        xListView.setRefreshTime(time);
    }


    private void showMessageList() {
        new MyAsyncTask() {
            @Override
            public void preTask() {

            }

            @Override
            public void postTask() {

                if (infos != null && infos.size() > 0) {
                    for (int i = 0; i < infos.size(); i++) {
                        final ChatInfo chat = infos.get(i);
                        boolean successed = queryAndSetUserData(chat);
                        if (!successed) {
                            currentIndex = i;
                            break;
                        }

                    }
                }
                int count = 0;
                for (ChatInfo info : infos) {
                    count += info.getUnReadCount();
                }
                String str = count + "";
                if (count > 99) {
                    str = "99+";
                }
                tv_red.setVisibility(View.VISIBLE);
             //   tv_red.setText(str);
                if (count == 0) {
                    tv_red.setText("");
                    tv_red.setVisibility(View.GONE);
                }

            }

            @Override
            public void doInBack() {
                if (!isDoInDB) {
                    isDoInDB = true;
                    MainActivity.isShowPoint = false;
                    infos = DBHelper.getInstance().getWeLearnDB().queryMessageList();
                    isDoInDB = false;
                }
            }
        }.excute();
    }



    private boolean queryAndSetUserData(ChatInfo chat) {
        UserInfoModel user = DBHelper.getInstance().getWeLearnDB().queryByUserId(chat.getFromuser(), true);

        boolean flag = true;
        if (user != null) {
            chat.setUser(user);
            // mAdapter.setData(infos);
            flag = true;
        } else {
            flag = false;

            WeLearnApi.getContactInfo(getActivity(), chat.getFromuser(), new OkHttpHelper.HttpListener() {
                @Override
                public void onSuccess(int code, String dataJson, String errMsg) {
                    if (code == 0) {
                        UserInfoModel user = JSON.parseObject(dataJson, UserInfoModel.class);

                        DBHelper.getInstance().getWeLearnDB().insertorUpdate(user.getUserid(), user.getRoleid(),
                                user.getName(), user.getAvatar_100());

                        if (currentIndex < infos.size()) {
                            LogUtils.i(TAG, user.toString());
                            infos.get(currentIndex).setUser(user);
                        }
                        setUser();

                    } else {
                        // ToastUtils.show(mActivity, errMsg +"----是我弹的");
                    }
                }

                @Override
                public void onFail(int HttpCode, String errMsg) {
                }
            });
        }
        return flag;
    }

    private void setUser() {
        currentIndex++;
        if (currentIndex < infos.size()) {
            ChatInfo chat = infos.get(currentIndex);
            boolean successed = queryAndSetUserData(chat);
            if (successed) {
                setUser();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (homepageController != null) {
            homepageController.removeMsgInQueue();
            homepageController = null;
        }
        if (runTask != null) {
            runTask.stop();
            runTask = null;
        }
        if (mAuToUpdateTask != null) {
            MyApplication.getMainThreadHandler().removeCallbacks(mAuToUpdateTask);
            mAuToUpdateTask = null;
        }
    }


}
