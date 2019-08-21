package com.daxiong.fun.function.account.vip;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.daxiong.fun.R;
import com.daxiong.fun.api.VIPAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.dialog.CustomVipQuanDetailDialog;
import com.daxiong.fun.function.account.adapter.VipAdapter;
import com.daxiong.fun.function.account.model.VipModel;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.NetworkUtils;
import com.daxiong.fun.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * VIP主页
 *
 * @author: sky
 */
public class VipIndexActivity extends BaseActivity implements OkHttpHelper.HttpListener {


    private RelativeLayout back_layout;


    private List<Button> views;

    private TextView tv_vip_title;

    private LinearLayout layout_dynamic_list;

    private LinearLayout layout_dynamic_status;

    private LinearLayout layout_chongzhi;

    private LinearLayout layout_other_vip;

    private TextView tv_shiyong_sum_day;

    private TextView tv_shiyong_left_day;

    private TextView tv_homework_count;
    private TextView tv_question_count;

    private TextView tv_vip_status;

    private int vipType, from_location;
    private String user_grade;

    private int tag = 0;
    private VIPAPI vipApi;

    /////////////////////////////////////
    @Bind(R.id.back_layout)
    RelativeLayout backLayout;
    @Bind(R.id.next_setp_layout)
    RelativeLayout nextSetpLayout;

    private TextView tv_vip_text;
    private RelativeLayout ll_not_vip;
    private TextView tv_homework_quan_count;
    private TextView tv_question_quan_count;
    private LinearLayout ll_is_vip;
    private TextView tv_shengyutianshu;
    private TextView tv_detail;


    @Bind(R.id.listview_vip)
    ListView mListView;
    private List<VipModel.BuyVipInfosBean> list;
    private VipAdapter mAdapter;
    int orgid=0;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.vip_index_activity);
        ButterKnife.bind(this);
        getExtraData();
        initView();
        initListener();
        initData();
    }

    public void getExtraData() {
        vipType = getIntent().getIntExtra("type", 0);
        from_location = getIntent().getIntExtra("from_location", 0);
        user_grade = getIntent().getStringExtra("user_grade");


    }

    @Override
    public void initView() {
        super.initView();
        back_layout = (RelativeLayout) this.findViewById(R.id.back_layout);
        View headView = View.inflate(this, R.layout.vip_index_listview_header_layout, null);
        tv_vip_text = (TextView) headView.findViewById(R.id.tv_vip_text);
        ll_not_vip = (RelativeLayout) headView.findViewById(R.id.ll_not_vip);
        tv_homework_quan_count = (TextView) headView.findViewById(R.id.tv_homework_quan_count);
        tv_question_quan_count = (TextView) headView.findViewById(R.id.tv_question_quan_count);
        ll_is_vip = (LinearLayout) headView.findViewById(R.id.ll_is_vip);
        tv_shengyutianshu = (TextView) headView.findViewById(R.id.tv_shengyutianshu);
        tv_detail = (TextView) headView.findViewById(R.id.tv_detail);
        tv_detail.setText(Html.fromHtml("<u color='#8d8d8d'>"+"详情"+"</u>"));
        mListView.addHeaderView(headView);
        vipApi = new VIPAPI();
        views = new ArrayList<Button>();
        setWelearnTitle("(" + user_grade + ")" + "VIP套餐");
        list = new ArrayList<VipModel.BuyVipInfosBean>();
        mAdapter = new VipAdapter(VipIndexActivity.this, list);
        mListView.setAdapter(mAdapter);


    }

    @Override
    public void initListener() {
        super.initListener();
        back_layout.setOnClickListener(this);
        tv_detail.setOnClickListener(this);
        mAdapter.setOnButtonClickListener(new VipAdapter.IClickListerner() {
            @Override
            public void doOrder(int position) {
                //ToastUtils.show("position->" + position);
                xiaDan(position);
            }

            @Override
            public void doDetail(int position) {
               // ToastUtils.show("详情-->" + position);
                if (list != null && list.size() > 0) {
                    VipModel.BuyVipInfosBean buyVipInfosBean = list.get(position);
                    // 购买类别: 1-辅导券 2-VIP
                    int buy_category = buyVipInfosBean.getBuy_category();
                    if(buy_category==1){
                        String detail_content = buyVipInfosBean.getDetail_content();
                        final CustomVipQuanDetailDialog dialogg=new CustomVipQuanDetailDialog(VipIndexActivity.this,detail_content,"知道了");
                        dialogg.show();
                        dialogg.setClicklistener(new CustomVipQuanDetailDialog.ClickListenerInterface() {
                            @Override
                            public void doCancel() {
                              dialogg.dismiss();
                            }
                        });

                    } else if(buy_category==2){
                        String detail_content = buyVipInfosBean.getDetail_content();
                        Intent intent = new Intent(VipIndexActivity.this, VipPlanDetailActivity.class);
                        intent.putExtra("buy_vip_infos", (Serializable) buyVipInfosBean);
                        intent.putExtra("detail_content", detail_content);
                        intent.putExtra("parents_pool_orgid", orgid);
                        startActivity(intent);
                    }

                }
            }
        });




    }


    public void initData() {
        if (NetworkUtils.getInstance().isInternetConnected(this)) {
            showDialog("正在加载...");
            // 请求vip信息
            OkHttpHelper.post(this, "parents", "newvippackages", null, this);
        } else {
            ToastUtils.show("网络有问题,请查看网络连接");
        }
    }


    @Override
    public void onSuccess(int code, String dataJson, String errMsg) {
        try {
            closeDialog();
            if (!TextUtils.isEmpty(dataJson)) {
                VipModel vipModel = JSON.parseObject(dataJson, VipModel.class);
                VipModel.VipStatusInfosBean vip_status_infos = vipModel.getVip_status_infos();
                List<VipModel.BuyVipInfosBean> buy_vip_infos = vipModel.getBuy_vip_infos();

                // 处理vip状态
                int type = vip_status_infos.getType();
                int question_coupon = vip_status_infos.getQuestion_coupon_count();
                int homework_coupon = vip_status_infos.getHomework_coupon_count();
                int vip_left_time = vip_status_infos.getVip_left_time();
                orgid=vip_status_infos.getOrgid();

                list.addAll(buy_vip_infos);
                mAdapter.notifyDataSetChanged();

                if (type == 0) {//不是vip
                    tv_vip_text.setText("非VIP用户");
                    ll_not_vip.setVisibility(View.VISIBLE);
                    ll_is_vip.setVisibility(View.GONE);
                } else if (type == 1) {//试用vip
                    tv_vip_text.setText("试用VIP用户");
                    ll_not_vip.setVisibility(View.VISIBLE);
                    ll_is_vip.setVisibility(View.GONE);
                } else if (type == 2) {//正式vip
                    tv_vip_text.setText("正式VIP用户");
                    ll_not_vip.setVisibility(View.GONE);
                    ll_is_vip.setVisibility(View.VISIBLE);
                } else if (type == 3) {//预约vip
                    tv_vip_text.setText("预约VIP用户");
                    ll_not_vip.setVisibility(View.VISIBLE);
                    ll_is_vip.setVisibility(View.GONE);
                } else {
                    tv_vip_text.setText("非VIP用户");
                    ll_not_vip.setVisibility(View.VISIBLE);
                    ll_is_vip.setVisibility(View.GONE);
                }

                String homeworkstr = "<p>可用作业券：<font size=16 color='#f78c0e'>" + homework_coupon
                        + "</font>张";
                tv_homework_quan_count.setText(Html.fromHtml(homeworkstr));

                String questionstr = "<p>可用难题券：<font size=16 color='#f78c0e'>" + question_coupon
                        + "</font>张";
                tv_question_quan_count.setText(Html.fromHtml(questionstr));

                String shengyutianshustr = "<p>剩余天数：<font size=16 color='#f78c0e'>" + vip_left_time
                        + "</font>天";
                tv_shengyutianshu.setText(Html.fromHtml(shengyutianshustr));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onFail(int HttpCode, String errMsg) {

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_layout:// 返回
                finish();
                break;
            case R.id.tv_detail:
                Intent intent = new Intent(this, BuyDetailActivity.class);
                startActivity(intent);
                break;
        }
    }


    /**
     * 下单操作(生成预支付订单)
     *
     * @param position
     */
    private void xiaDan(int position) {
        VipModel.BuyVipInfosBean item = list.get(position);
        int buy_category = item.getBuy_category();
        int buy_type = item.getBuy_type();
        //下单操作
        try {
            JSONObject json = new JSONObject();
            json.put("buy_category", buy_category);
            json.put("buy_type", buy_type);
            if (orgid > 0){
                json.put("orgid",orgid);
                json.put("packageid",item.getPackageid());
            }
            showDialog("请稍等...");
            OkHttpHelper.post(VipIndexActivity.this, "parents", "newgenerateprepaidorders", json, new OkHttpHelper.HttpListener() {
                @Override
                public void onSuccess(int code, String dataJson, String errMsg) {
                    closeDialog();
                    final String orderid = JsonUtil.getString(dataJson, "orderid", "");

                    Intent intent = new Intent(VipIndexActivity.this, MyOrderListActivity.class);
                    /*intent.putExtra(PayActivity.EXTRA_TAG_PAY_MODEL,
                            (Serializable) buy_vip_infos.get(tag));
                    intent.putExtra(PayActivity.EXTRA_TAG_UID,
                            MySharePerfenceUtil.getInstance().getUserId());
                    intent.putExtra("golangorderid", orderid);*/
                    startActivity(intent);

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
    public void resultBack(Object... param) {
        super.resultBack(param);
        int flag = ((Integer) param[0]).intValue();
        switch (flag) {
        }
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
