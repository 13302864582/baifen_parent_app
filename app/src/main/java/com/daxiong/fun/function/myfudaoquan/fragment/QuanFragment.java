
package com.daxiong.fun.function.myfudaoquan.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.daxiong.fun.R;
import com.daxiong.fun.api.HomeListAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.base.BaseFragment;
import com.daxiong.fun.common.WebViewActivity;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.dialog.CustomSuoquanDialog;
import com.daxiong.fun.function.account.setting.AboutActivity;
import com.daxiong.fun.function.account.vip.VipIndexActivity;
import com.daxiong.fun.function.myfudaoquan.adapter.ExpireFudaoquanAdapter;
import com.daxiong.fun.function.myfudaoquan.adapter.FudaoquanCommonAdapter;
import com.daxiong.fun.model.FudaoquanModel;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.XListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 成长Fragment
 *
 * @author: sky
 */
public class QuanFragment extends BaseFragment
        implements OnCheckedChangeListener, XListView.IXListViewListener, AdapterView.OnItemClickListener {

    private BaseActivity activity;
    private HomeListAPI homeListAPI;
    private RadioGroup radio_group;

    private RadioButton radio_keyongquan;

    private RadioButton radio_guoqiquan;
    private RadioButton radio_suoquan;
    private LinearLayout ll_kongbai,ll1;
    private TextView tv_shibai,tv_gengduo;
    private TextView tv_chongzhi;
    private ImageView iv_back;

    private XListView xListView;
    private FudaoquanCommonAdapter adapter;
    private ExpireFudaoquanAdapter adapter2;
    private int LoadMore = 0;
    private static int mtpye = 0;



    private int checkedId;


    private List<FudaoquanModel> list = new ArrayList<FudaoquanModel>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.growing_fragment, null);
        initView(view);
        initListener();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (BaseActivity) getActivity();
        checkedId= 0;
        if(mtpye==0|mtpye==1){
            radio_keyongquan.setChecked(true);
        }else if(mtpye==2){
            radio_guoqiquan.setChecked(true);
        }else if(mtpye==3){
            radio_suoquan.setChecked(true);
        }
        radio_group.setOnCheckedChangeListener(this);
        initData();


    }


    @Override
    public void initView(View view) {

        xListView = (XListView) view.findViewById(R.id.answer_list);
        radio_group = (RadioGroup) view.findViewById(R.id.radio_group);
        radio_keyongquan = (RadioButton) view.findViewById(R.id.radio_keyongquan);
        radio_guoqiquan = (RadioButton) view.findViewById(R.id.radio_guoqiquan);
        radio_suoquan = (RadioButton) view.findViewById(R.id.radio_suoquan);
        ll_kongbai = (LinearLayout) view.findViewById(R.id.ll_kongbai);
        ll1 = (LinearLayout) view.findViewById(R.id.ll1);
        tv_gengduo = (TextView) view.findViewById(R.id.tv_gengduo);
        tv_shibai = (TextView) view.findViewById(R.id.tv_shibai);
        tv_chongzhi = (TextView) view.findViewById(R.id.tv_chongzhi);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        if(mtpye>0){
            iv_back.setVisibility(View.VISIBLE);
        }
        homeListAPI = new HomeListAPI();


    }

    @Override
    public void initListener() {
        super.initListener();
        xListView.setXListViewListener(this);
        xListView.setOnItemClickListener(this);
        xListView.setPullRefreshEnable(true);
        xListView.setPullLoadEnable(false);

        tv_chongzhi.setOnClickListener(this);
        iv_back.setOnClickListener(this);


    }


    private void initData() {
        showDialog(getString(R.string.load_more));
        setTabSelection(radio_group.getCheckedRadioButtonId());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_chongzhi:
                UserInfoModel userInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
                Intent intentxx = new Intent(getActivity(), VipIndexActivity.class);
                intentxx.putExtra("type", userInfo.getVip_type());
                intentxx.putExtra("from_location", 3);
                startActivity(intentxx);
                break;
            case R.id.iv_back:
                activity.finish();
                break;

        }

    }
    public static QuanFragment newInstance(int tpye) {
        mtpye=tpye;
        QuanFragment fragment = new QuanFragment();

        return fragment;
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        setTabSelection(group.getCheckedRadioButtonId());
    }

    public void setTabSelection(int index) {
        if (index != checkedId) {

            radio_keyongquan.setTextColor(Color.parseColor("#f74344"));
            radio_guoqiquan.setTextColor(Color.parseColor("#f74344"));
            radio_suoquan.setTextColor(Color.parseColor("#f74344"));
        }
        tv_shibai.setVisibility(View.GONE);
        ll_kongbai.setVisibility(View.GONE);
        xListView.setVisibility(View.VISIBLE);
        tv_gengduo.setVisibility(View.GONE);
        switch (index) {
            case R.id.radio_keyongquan:// 可用
                ll1.setVisibility(View.GONE);
                if (index != checkedId) {
                    checkedId = index;
                    radio_keyongquan.setTextColor(Color.WHITE);
                    LoadMore = 0;
                    list.clear();
                    adapter = new FudaoquanCommonAdapter(activity, list);
                    adapter.setFalg(false);
                    xListView.setAdapter(adapter);
                }

                homeListAPI.newlist(requestQueue, this, LoadMore, 10, RequestConstant.GET_FUDAOQUAN_LIST_CODE);
                break;
            case R.id.radio_guoqiquan:// 过期
                ll1.setVisibility(View.GONE);
                if (index != checkedId) {
                    checkedId = index;
                    radio_guoqiquan.setTextColor(Color.WHITE);
                    LoadMore = 0;
                    list.clear();
                    adapter = new FudaoquanCommonAdapter(activity, list);
                    adapter.setFalg(true);
                    xListView.setAdapter(adapter);
                }

                homeListAPI.newexpiredlist(requestQueue, this, LoadMore, 10, RequestConstant.GET_EXPRIE_FUDAOQUAN_LIST_CODE);
                break;

            case R.id.radio_suoquan:// 索券

                ll1.setVisibility(View.VISIBLE);
                if (index != checkedId) {
                    checkedId = index;
                    radio_suoquan.setTextColor(Color.WHITE);
                    LoadMore = 1;
                   list.clear();
                    adapter2 = new ExpireFudaoquanAdapter(activity, this, homeListAPI, list);

                    xListView.setAdapter(adapter2);
                }

                homeListAPI.orgsendcouponlist(requestQueue, this, LoadMore, 10, RequestConstant.GET_HOMEWORK_QUAN_CODE);
                break;
            default:

                break;
        }

    }


    public void onLoadFinish() {

        xListView.stopRefresh();
        xListView.stopLoadMore();
        DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(new Date());
        xListView.setRefreshTime(time);
    }

    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        onLoadFinish();
        closeDialog();
        int flag = ((Integer) param[0]).intValue();

        switch (flag) {
            case RequestConstant.GET_FUDAOQUAN_LIST_CODE:  // 可用券
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        String dataJson = JsonUtil.getString(datas, "Data", "");
                        if (!TextUtils.isEmpty(dataJson)) {

                            List<FudaoquanModel> parseArray = JSON.parseArray(dataJson, FudaoquanModel.class);

                            if (parseArray.size() < 10) {
                                tv_gengduo.setVisibility(View.VISIBLE);
                                xListView.setPullLoadEnable(false);
                            } else {
                                tv_gengduo.setVisibility(View.GONE);
                                xListView.setPullLoadEnable(true);

                            }
                            list.addAll(parseArray);

                            updateListUI();

                        }
                    } else {
                        ToastUtils.show(msg);
                    }

                }

                break;
            case RequestConstant.GET_EXPRIE_FUDAOQUAN_LIST_CODE: // 过期的辅导券
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        String dataJson = JsonUtil.getString(datas, "Data", "");
                        if (!TextUtils.isEmpty(dataJson)) {

                            List<FudaoquanModel> parseArray = JSON.parseArray(dataJson, FudaoquanModel.class);

                            if (parseArray.size() < 10) {
                                tv_gengduo.setVisibility(View.VISIBLE);
                                xListView.setPullLoadEnable(false);
                            } else {
                                tv_gengduo.setVisibility(View.GONE);
                                xListView.setPullLoadEnable(true);

                            }
                            list.addAll(parseArray);


                            updateListUI();

                        }
                    } else {
                        ToastUtils.show(msg);
                    }

                }

                break;
            case RequestConstant.GET_HOMEWORK_QUAN_CODE://索券列表
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        String dataJson = JsonUtil.getString(datas, "Data", "");
                        if (!TextUtils.isEmpty(dataJson)) {
                            int showlist = JsonUtil.getInt(dataJson, "showlist", 0);
                            if (showlist == 1) {
                                String listinfos = JsonUtil.getString(dataJson, "list", "");
                                List<FudaoquanModel> parseArray = JSON.parseArray(listinfos, FudaoquanModel.class);

                                if (parseArray.size() < 10) {

                                    xListView.setPullLoadEnable(false);
                                } else {

                                    xListView.setPullLoadEnable(true);

                                }
                                list.addAll(parseArray);


                                adapter2.notifyDataSetChanged();

                            } else {
                                String infos = JsonUtil.getString(dataJson, "infos", "");
                                tv_shibai.setText(infos);
                                tv_shibai.setVisibility(View.VISIBLE);
                                xListView.setPullLoadEnable(false);
                            }
                        }
                    } else {
                        ToastUtils.show(msg);
                    }

                }

                break;
            case RequestConstant.GETFUDAOQUAN_HW_CODE://索券
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        String dataJson = JsonUtil.getString(datas, "Data", "");
                        if (!TextUtils.isEmpty(dataJson)) {
                            int success = JsonUtil.getInt(dataJson, "success", 0);
                            String infos = JsonUtil.getString(dataJson, "infos", "");
                            if (success == 1) {//success 表示索券是否成功 1成功 2不成功
//                                ToastUtils.show(infos);
                                xListView.setVisibility(View.VISIBLE);
                                final CustomSuoquanDialog suoQuanDialog=new CustomSuoquanDialog(getActivity(),infos,"知道了");
                                suoQuanDialog.show();
                                suoQuanDialog.setClicklistener(new CustomSuoquanDialog.ClickListenerInterface(){
                                    @Override
                                    public void doCancel() {
                                        suoQuanDialog.dismiss();
                                    }
                                });
                            } else {
                                xListView.setVisibility(View.GONE);
                                tv_shibai.setText(infos);
                                tv_shibai.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        ToastUtils.show(msg);
                    }

                }

                break;
        }

    }

    private void updateListUI() {
        adapter.notifyDataSetChanged();
        if (list.size() > 0) {

            ll_kongbai.setVisibility(View.GONE);
        } else {
            ll_kongbai.setVisibility(View.VISIBLE);
            tv_gengduo.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {


        super.onDestroy();
    }


    @Override
    public void onRefresh() {
        if (radio_suoquan.isChecked()) {
            LoadMore = 1;
        } else {
            LoadMore = 0;
        }
        LoadMore++;
        initData();
    }

    @Override
    public void onLoadMore() {
        LoadMore++;
        initData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        FudaoquanModel fudaoquanModel = list.get(position - 1);
        String orgName = fudaoquanModel.getOrgname();
        if (TextUtils.isEmpty(orgName)) {
            Intent intent = new Intent(getActivity(), AboutActivity.class);
            startActivity(intent);
        } else {
            String strurl = "";
            if (radio_suoquan.isChecked()) {
                strurl = fudaoquanModel.getOrginfourl();
            } else {
                strurl = fudaoquanModel.getOrgurl();
            }
            if (!TextUtils.isEmpty(strurl)) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", "机构");
                intent.putExtra("url", strurl);
                startActivity(intent);
            }

        }

    }
}
