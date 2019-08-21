package com.daxiong.fun.function.learninganalysis;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daxiong.fun.MainActivity;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseFragment;
import com.daxiong.fun.common.WebViewActivity;
import com.daxiong.fun.function.learninganalysis.adapter.NoneXueQingAdapter;
import com.daxiong.fun.function.learninganalysis.adapter.SpaceItemDecoration;
import com.daxiong.fun.function.learninganalysis.model.NoneXueqingModel;
import com.daxiong.fun.function.learninganalysis.model.XueqingBigModel;
import com.daxiong.fun.function.learninganalysis.pickerview.TimePickerDialog;
import com.daxiong.fun.function.learninganalysis.pickerview.data.Type;
import com.daxiong.fun.function.learninganalysis.pickerview.listener.OnDateSetListener;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.SpecialCalendarUtils;
import com.daxiong.fun.view.CropCircleTransformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 学情分析Fragment
 *
 * @author: sky
 */
public class AnalysisFragment extends BaseFragment implements OnDateSetListener, NoneXueQingAdapter.MyItemClickListener {

    private static final String TAG = "AnalysisFragment";


    @Bind(R.id.back_layout)
    RelativeLayout backLayout;

    @Bind(R.id.title_layout)
    RelativeLayout rlTitleLayout;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.iv_choose)
    ImageView ivChoose;

    @Bind(R.id.next_setp_layout)
    RelativeLayout nextSetpLayout;

    @Bind(R.id.ll_head)
    LinearLayout llHeader;
    @Bind(R.id.ll_heaview)
    LinearLayout llHeaview;
    @Bind(R.id.view_header_split_xian)
    View view_header_split_xian;
    @Bind(R.id.iv_header_arrow)
    ImageView iv_header_arrow;
    @Bind(R.id.iv_banzhuren_avatar)
    ImageView ivBanzhurenAvatar;
    @Bind(R.id.rl_headview_left)
    RelativeLayout rlHeadviewLeft;
    @Bind(R.id.tv_biaoxian)
    TextView tvBiaoxian;
    @Bind(R.id.tv_biaoxian_des)
    TextView tvBiaoxianDes;
    @Bind(R.id.rl_headview_right)
    LinearLayout rlHeadviewRight;

    @Bind(R.id.ll_cuoti)
    RelativeLayout llCuoti;
    @Bind(R.id.tv_cuoti_count_value)
    TextView tvCuotiCountValue;
    @Bind(R.id.iv_cuotifenbu_arrow)
    ImageView ivCuotifenbuArrow;

    @Bind(R.id.ll_zhengquelv)
    RelativeLayout llZhengquelv;
    @Bind(R.id.tv_zhengquelv_value)
    TextView tvZhengquelvValue;
    @Bind(R.id.iv_zhengquelv_arrow)
    ImageView ivZhengquelvArrow;

    @Bind(R.id.ll_xueqingbaogao)
    RelativeLayout llXueqingbaogao;
    @Bind(R.id.tv_xueqingbaogao_value)
    TextView tvXueqingbaogaoValue;
    @Bind(R.id.iv_xueqingbaogao_arrow)
    ImageView ivXueqingbaogaoArrow;


    @Bind(R.id.layout_container)
    FrameLayout layoutContainer;


    @Bind(R.id.layout_none_xueqing)
    RelativeLayout LayoutNoneXueqing;
    @Bind(R.id.recycleview)
    RecyclerView recycleView;
    @Bind(R.id.tv_remind)
    TextView tvRemind;


    private View view;

    private SubWrongAnalysisFragment subWrongAnalysisFragment;

    private SubRatedetailFragment subRatedetailFragment;

    private SubLearningReportFragment subReportFragment;

    private ScoresPredictionSubFragment scoresPredictionFragment;

    private FragmentManager fm;

    private MainActivity activity;

    private XueqingBigModel xueqingBigModel;

    private long regtime;//注册时间

    private String dateNowStr;


    private boolean isShow = true;

    TimePickerDialog mDialogYearMonth;

    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

    private String chooseDate = "";

    private  List<NoneXueqingModel.ReportsBean> noneList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getChildFragmentManager();
        if (savedInstanceState != null) {
            subWrongAnalysisFragment = (SubWrongAnalysisFragment) fm.findFragmentByTag("subWrongAnalysisFragment");
            subRatedetailFragment = (SubRatedetailFragment) fm
                    .findFragmentByTag("subRatedetailFragment");
            subReportFragment = (SubLearningReportFragment) fm
                    .findFragmentByTag("subReportFragment");

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtils.e(TAG, "onCreateView");
        view = inflater.inflate(R.layout.learning_analysis_fragment, null);
        ButterKnife.bind(this, view);
        registerChooseDateReceiver();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.e(TAG, "onActivityCreated");
        initView(view);
        initListener();
        onClick(llCuoti);


    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (ivChoose != null && mDialogYearMonth != null) {
                ivChoose.setBackgroundResource(R.drawable.selector_choose_year_and_month_down);
                mDialogYearMonth.dismiss();
                isShow = false;
            }
            if(subWrongAnalysisFragment!=null){
                subWrongAnalysisFragment.onHiddenChanged(true);
            }
        }

        if(!hidden){
            initData(dateNowStr);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void initView(View view) {
        nextSetpLayout.setVisibility(View.GONE);
        activity = (MainActivity) getActivity();
        ivChoose.setVisibility(View.VISIBLE);
        subWrongAnalysisFragment = new SubWrongAnalysisFragment();
        subRatedetailFragment = new SubRatedetailFragment();
        subReportFragment = new SubLearningReportFragment();
        FragmentTransaction transaction = fm.beginTransaction();
        if (!subWrongAnalysisFragment.isAdded()) {
            transaction.add(R.id.layout_container, subWrongAnalysisFragment, "subWrongAnalysisFragment");
        }

        if (!subRatedetailFragment.isAdded()) {
            transaction.add(R.id.layout_container, subRatedetailFragment, "subRatedetailFragment");
        }

        if (!subReportFragment.isAdded()) {
            transaction.add(R.id.layout_container, subReportFragment, "subReportFragment");
        }
        transaction.commit();


        //获取当前系统日期
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        dateNowStr = sdf.format(d);
        title.setText((d.getMonth() + 1) + "月份学情");
        chooseDate = sf.format(d);


    }

    @Override
    public void initListener() {
        super.initListener();
        llCuoti.setOnClickListener(this);
        llZhengquelv.setOnClickListener(this);
        llXueqingbaogao.setOnClickListener(this);
        rlTitleLayout.setOnClickListener(this);

    }

    public void initData(String dateNowStr) {
        try {
            showDialog("加载中...");
            JSONObject json = new JSONObject();
            json.put("date", Integer.parseInt(dateNowStr));
//            json.put("date", 201606);
            OkHttpHelper.post(getActivity(), "parents", "learningreportpage", json, new OkHttpHelper.HttpListener() {
                @Override
                public void onSuccess(int code, String dataJson, String errMsg) {
                    closeDialog();
                    int isnew1 = JsonUtil.getInt(dataJson, "isnew", 0);
                    if (isnew1 == 0) {
                        //不是新用户
                        llHeader.setVisibility(View.VISIBLE);
                        layoutContainer.setVisibility(View.VISIBLE);
                        LayoutNoneXueqing.setVisibility(View.GONE);

                        rlTitleLayout.setClickable(true);
                        ivChoose.setVisibility(View.VISIBLE);

                        xueqingBigModel = JSON.parseObject(dataJson, XueqingBigModel.class);
                        bindData(xueqingBigModel);

                    } else if (isnew1 == 1) {
                        //新用户  没有学情
                        llHeader.setVisibility(View.GONE);
                        layoutContainer.setVisibility(View.GONE);
                        LayoutNoneXueqing.setVisibility(View.VISIBLE);
                        rlTitleLayout.setClickable(false);
                        ivChoose.setVisibility(View.GONE);
                        title.setText("学情");
                        NoneXueqingModel noneXueqingModel = JSON.parseObject(dataJson, NoneXueqingModel.class);
                         noneList = noneXueqingModel.getReports();
                        tvRemind.setText(noneXueqingModel.getRemind());
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
                        recycleView.addItemDecoration(new SpaceItemDecoration(10));
                        recycleView.setLayoutManager(gridLayoutManager);
                        recycleView.setHasFixedSize(true);
                        NoneXueQingAdapter mAdapter = new NoneXueQingAdapter(getActivity(), noneList);
                        recycleView.setAdapter(mAdapter);
                        mAdapter.setOnItemClickListener(AnalysisFragment.this);


                    }


                }

                @Override
                public void onFail(int HttpCode, String errMsg) {
                    closeDialog();

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_cuoti://错题
                setTabSelection(llCuoti.getId());
                break;
            case R.id.ll_zhengquelv://正确率
                setTabSelection(llZhengquelv.getId());
                break;
            case R.id.ll_xueqingbaogao://学情报告
                setTabSelection(llXueqingbaogao.getId());
                break;
            case R.id.title_layout://中间的选择年份和月份

                if (isShow) {
                    ivChoose.setBackgroundResource(R.drawable.selector_choose_year_and_month_up);
                    Rect frame = new Rect();
                    activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                    int statusBarHeight = frame.top;
                    mDialogYearMonth.showAtLocation(activity.getWindow().getDecorView(), Gravity.TOP, 0,
                            DensityUtil.dip2px(activity, 48) + statusBarHeight);
                } else {
                    ivChoose.setBackgroundResource(R.drawable.selector_choose_year_and_month_down);
                    mDialogYearMonth.dismiss();
                }
                isShow = !isShow;
                break;
        }


    }

    private void setTabSelection(int index) {
        // 每次先清除上次的选中状态
        clearSelection();
        FragmentTransaction ft = fm.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(ft);
        switch (index) {
            case R.id.ll_cuoti://错题分布
                tvCuotiCountValue.setTextAppearance(getActivity(), R.style.TextViewStyleBold);
                tvZhengquelvValue.setTextAppearance(getActivity(), R.style.TextViewStyleNormal);
                tvXueqingbaogaoValue.setTextAppearance(getActivity(), R.style.TextViewStyleNormal);


                ivCuotifenbuArrow.setVisibility(View.VISIBLE);
                ivZhengquelvArrow.setVisibility(View.GONE);
                ivXueqingbaogaoArrow.setVisibility(View.GONE);


                subWrongAnalysisFragment = (SubWrongAnalysisFragment) fm.findFragmentByTag("subWrongAnalysisFragment");


                if (subWrongAnalysisFragment == null) {
                    subWrongAnalysisFragment = new SubWrongAnalysisFragment();
                }

                if (subWrongAnalysisFragment.isAdded()) {
                    ft.show(subWrongAnalysisFragment);
                } else {
                    ft.add(R.id.layout_container, subWrongAnalysisFragment, "subWrongAnalysisFragment");
                }

                break;

            case R.id.ll_zhengquelv:// 正确率
                tvCuotiCountValue.setTextAppearance(getActivity(), R.style.TextViewStyleNormal);
                tvZhengquelvValue.setTextAppearance(getActivity(), R.style.TextViewStyleBold);
                tvXueqingbaogaoValue.setTextAppearance(getActivity(), R.style.TextViewStyleNormal);

                ivCuotifenbuArrow.setVisibility(View.GONE);
                ivZhengquelvArrow.setVisibility(View.VISIBLE);
                ivXueqingbaogaoArrow.setVisibility(View.GONE);

                subRatedetailFragment = (SubRatedetailFragment) fm.findFragmentByTag("subRatedetailFragment");


                if (subRatedetailFragment == null) {
                    subRatedetailFragment = new SubRatedetailFragment();
                }

                if (subRatedetailFragment.isAdded()) {
                    ft.show(subRatedetailFragment);
                } else {

                    ft.add(R.id.layout_container, subRatedetailFragment, "subRatedetailFragment");
                }
                //subRatedetailFragment.setData(activity,xueqingBigModel.getRatedetail());
                if(null != xueqingBigModel)
                subRatedetailFragment.setRatedetails(xueqingBigModel.getRatedetail());
                break;
            case R.id.ll_xueqingbaogao:// 学情报告
                tvCuotiCountValue.setTextAppearance(getActivity(), R.style.TextViewStyleNormal);
                tvZhengquelvValue.setTextAppearance(getActivity(), R.style.TextViewStyleNormal);
                tvXueqingbaogaoValue.setTextAppearance(getActivity(), R.style.TextViewStyleBold);

                ivCuotifenbuArrow.setVisibility(View.GONE);
                ivZhengquelvArrow.setVisibility(View.GONE);
                ivXueqingbaogaoArrow.setVisibility(View.VISIBLE);


                subReportFragment = (SubLearningReportFragment) fm.findFragmentByTag("subReportFragment");
                if (subReportFragment == null) {
                    subReportFragment = new SubLearningReportFragment();
                }
                if (subReportFragment.isAdded()) {
                    ft.show(subReportFragment);
                } else {
                    ft.add(R.id.layout_container, subReportFragment,
                            "subReportFragment");
                }
                // subReportFragment.setData(activity, xueqingBigModel.getReportdetail());
                if(null!=xueqingBigModel)
                subReportFragment.setList(xueqingBigModel.getReportdetail());
                break;

        }
        ft.commit();
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        tvCuotiCountValue.setTextAppearance(getActivity(), R.style.TextViewStyleNormal);
        tvZhengquelvValue.setTextAppearance(getActivity(), R.style.TextViewStyleNormal);
        tvXueqingbaogaoValue.setTextAppearance(getActivity(), R.style.TextViewStyleNormal);

        ivCuotifenbuArrow.setVisibility(View.GONE);
        ivZhengquelvArrow.setVisibility(View.GONE);
        ivXueqingbaogaoArrow.setVisibility(View.GONE);
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (subWrongAnalysisFragment != null) {
            transaction.hide(subWrongAnalysisFragment);
        }
        if (subRatedetailFragment != null) {
            transaction.hide(subRatedetailFragment);
        }
        if (subReportFragment != null) {
            transaction.hide(subReportFragment);
        }
    }

    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        int flag = ((Integer) param[0]).intValue();
        switch (flag) {
        }

    }


    private void bindData(XueqingBigModel xueqingBigModel) {
        int isnew = xueqingBigModel.getIsnew();
        //注册时间
        regtime = xueqingBigModel.getRegtime();
        String name = xueqingBigModel.getName();
        int color = xueqingBigModel.getColor();
        String avatar = xueqingBigModel.getAvatar();
        String level = xueqingBigModel.getLevel();
        String comment = xueqingBigModel.getComment();
        int wrongcnt = xueqingBigModel.getWrongcnt();
        double rate = xueqingBigModel.getRate();
        int reportcnt = xueqingBigModel.getReportcnt();

        LogUtils.e(TAG, "regtime-->" + regtime);

       if( !subRatedetailFragment.isHidden()){
           subRatedetailFragment.setRatedetails(xueqingBigModel.getRatedetail());
       }
        if( !subReportFragment.isHidden()){
            subReportFragment.setList(xueqingBigModel.getReportdetail());
       }
        mDialogYearMonth = new TimePickerDialog.Builder()
                .setType(Type.YEAR_MONTH)
                .setThemeColor(getResources().getColor(R.color.login_background_color))
                .setCyclic(false)
                .setMinMillseconds(regtime * 1000)
                .setMaxMillseconds(System.currentTimeMillis())
                .setCallBack(this)
                .build();

        switch (color) {
            case 1://绿
                rlHeadviewLeft.setBackgroundColor(getResources().getColor(R.color.color22c00d));
                rlHeadviewRight.setBackgroundColor(getResources().getColor(R.color.color1baf07));
                llHeaview.setBackgroundColor(getResources().getColor(R.color.color22c00d));
                view_header_split_xian.setBackgroundColor(getResources().getColor(R.color.color22c00d));
                iv_header_arrow.setImageResource(R.drawable.xueqing_lv_arrow);

                break;
            case 2://蓝
                rlHeadviewLeft.setBackgroundColor(getResources().getColor(R.color.color5677fc));
                rlHeadviewRight.setBackgroundColor(getResources().getColor(R.color.color4869f1));
                llHeaview.setBackgroundColor(getResources().getColor(R.color.color5677fc));
                view_header_split_xian.setBackgroundColor(getResources().getColor(R.color.color5677fc));
                iv_header_arrow.setImageResource(R.drawable.xueqing_lan_arrow);
                break;
            case 3://橙
                rlHeadviewLeft.setBackgroundColor(getResources().getColor(R.color.colorf6aa06));
                rlHeadviewRight.setBackgroundColor(getResources().getColor(R.color.colorf09e10));
                llHeaview.setBackgroundColor(getResources().getColor(R.color.colorf6aa06));
                view_header_split_xian.setBackgroundColor(getResources().getColor(R.color.colorf6aa06));
                iv_header_arrow.setImageResource(R.drawable.xueqing_hua_arrow);

                break;
            case 4://红
                rlHeadviewLeft.setBackgroundColor(getResources().getColor(R.color.colorf74344));
                rlHeadviewRight.setBackgroundColor(getResources().getColor(R.color.colorea3839));
                llHeaview.setBackgroundColor(getResources().getColor(R.color.colorf74344));
                view_header_split_xian.setBackgroundColor(getResources().getColor(R.color.colorf74344));
                iv_header_arrow.setImageResource(R.drawable.xueqing_hong_arrow);
                break;
        }

        Glide.with(this).load(avatar).diskCacheStrategy(DiskCacheStrategy.ALL).bitmapTransform(new CropCircleTransformation(getActivity()))
                .into(ivBanzhurenAvatar);
        tvBiaoxian.setText(level);
        tvBiaoxianDes.setText(comment);
        tvCuotiCountValue.setText(wrongcnt + "");
        tvZhengquelvValue.setText((int) (rate * 100) + "%");
        tvXueqingbaogaoValue.setText(reportcnt + "");
        String[] arr = chooseDate.split("-");
        subWrongAnalysisFragment.setData(regtime, arr[0], arr[1], arr[2], xueqingBigModel.getList());


    }


    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        Date d = new Date(millseconds);
        chooseDate = sf.format(d);
        String[] arr = chooseDate.split("-");
        String finalYearAndMonth = arr[0] + arr[1];
        title.setText((d.getMonth() + 1) + "月份学情");
        initData(finalYearAndMonth);


    }


    @Override
    public void onPause() {
        super.onPause();


    }

    @Override
    public void onStop() {
        super.onStop();

    }


    public void registerChooseDateReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.action.choosedate");
        getActivity().registerReceiver(chooseDateReceiver, filter);
    }

    private BroadcastReceiver chooseDateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("com.action.choosedate".equals(action)) {
                String tag = intent.getStringExtra("tag");
                if ("last".equals(tag)) {
                    String date = intent.getStringExtra("date");
                    String[] arr = date.split("-");
                    int year = Integer.parseInt(arr[0]);
                    int lastMonth = new SpecialCalendarUtils().getLastMonth(arr[1]);
                    title.setText(lastMonth + "月份学情");
                    String lastMonthStr = "";
                    if (lastMonth < 10) {
                        lastMonthStr = "0" + lastMonth;
                    } else {
                        lastMonthStr = lastMonth + "";
                    }

                    chooseDate = year + "-" + lastMonth + "-01";
                    initData(year + lastMonthStr);

                } else if ("next".equals(tag)) {

                    String date = intent.getStringExtra("date");
                    String[] arr = date.split("-");
                    int year = Integer.parseInt(arr[0]);
                    int nextMonth = new SpecialCalendarUtils().getNextMonth(arr[1]);
                    title.setText(nextMonth + "月份学情");
                    String lastMonthStr = "";
                    if (nextMonth < 10) {
                        lastMonthStr = "0" + nextMonth;
                    } else {
                        lastMonthStr = nextMonth + "";
                    }

                    chooseDate = year + "-" + nextMonth + "-01";
                    initData(year + lastMonthStr);

                }


            }


        }
    };


    @Override
    public void onItemClick(View view, int postion) {
        NoneXueqingModel.ReportsBean reportsBean = noneList.get(postion);
        if (reportsBean!=null){
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("title", "学情");
            intent.putExtra("url", reportsBean.getUrl());
            startActivity(intent);
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(chooseDateReceiver);
        ButterKnife.unbind(this);
    }



}
