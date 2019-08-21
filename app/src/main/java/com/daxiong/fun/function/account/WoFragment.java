package com.daxiong.fun.function.account;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MainActivity;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseFragment;
import com.daxiong.fun.common.WebViewActivity;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.function.account.model.WoInfoModel;
import com.daxiong.fun.function.account.vip.MyOrderListActivity;
import com.daxiong.fun.function.account.vip.VipIndexActivity;
import com.daxiong.fun.function.myfudaoquan.MyFudaoquanActivity;
import com.daxiong.fun.function.question.MyQuestionListActivity;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.manager.UpdateManagerForDialog;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.AppUtils;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.PackageManagerUtils;
import com.daxiong.fun.util.SharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.CropCircleTransformation;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.alibaba.fastjson.JSON.parseObject;
import static com.daxiong.fun.R.id.iv_banzhuren_avatar;
import static com.daxiong.fun.R.id.tv_fudaotuan_count;
import static com.daxiong.fun.R.id.tv_grade;
import static com.daxiong.fun.R.id.tv_xuehao;

/**
 * 个人主页
 */
public class WoFragment extends BaseFragment implements OnClickListener {

    public static final String TAG = WoFragment.class.getSimpleName();

    @Bind(R.id.next_setp_layout)
    RelativeLayout nextLayout;
    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.user_avatar)
    ImageView userAvatar;
    @Bind(R.id.user_avatar_vip)
    ImageView userAvatarVip;
    @Bind(R.id.tv_user_nick)
    TextView tvUserNick;
    @Bind(tv_xuehao)
    TextView tvXuehao;
    @Bind(tv_grade)
    TextView tvGrade;
    @Bind(R.id.rl_userifo)
    RelativeLayout rlUserifo;
    @Bind(iv_banzhuren_avatar)
    ImageView ivBanzhurenAvatar;
    @Bind(R.id.tv_banzhuren_name)
    TextView tvBanzhurenName;
    @Bind(R.id.tv_banzhuren_des)
    TextView tvBanzhurenDes;
    @Bind(R.id.rl_banzhureninfo)
    RelativeLayout rlBanzhureninfo;
    @Bind(R.id.ll_chongzhi)
    LinearLayout llChongzhi;
    @Bind(R.id.tv_daizhifu_order_count)
    TextView tvDaizhifuOrderCount;
    @Bind(R.id.ll_daizhifudingdan)
    LinearLayout llDaizhifudingdan;
    @Bind(R.id.ll_suoquan)
    LinearLayout llSuoquan;
    @Bind(R.id.tv_fudaoquan_text)
    TextView tvFudaoquanText;
    @Bind(tv_fudaotuan_count)
    TextView tvFudaotuanCount;
    @Bind(R.id.rl_myfudaoquan)
    RelativeLayout rlMyfudaoquan;
    @Bind(R.id.tv_homework_count)
    TextView tvHomeworkCount;
    @Bind(R.id.rl_myhomework)
    RelativeLayout rlMyhomework;
    @Bind(R.id.tv_question_count)
    TextView tvQuestionCount;
    @Bind(R.id.rl_question)
    RelativeLayout rlQuestion;
    @Bind(R.id.rl_event)
    RelativeLayout rlEvent;
    @Bind(R.id.rl_setting)
    RelativeLayout rlSetting;


    private UserInfoModel userInfo;
    private int latestVersion;
    private UpdateManagerForDialog mUpdateManager;
    // private ImageView qrIV;
    public static boolean isDoInDb = false;
    private boolean orgVip;
    private boolean isLasted;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUpdateManager = new UpdateManagerForDialog(getActivity());
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wo, null);
        ButterKnife.bind(this, view);
        initView(view);
        initListener(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            requestWoInfo();

        }
    }

    /**
     * 请求我的一些信息
     */
    private void requestWoInfo() {
        OkHttpHelper.post(getActivity(), "parents", "myselfpageinfos", null, new HttpListener() {

            @Override
            public void onSuccess(int code, String dataJson, String errMsg) {
                WoInfoModel woInfoModel = JSON.parseObject(dataJson, WoInfoModel.class);
                updateInfo(woInfoModel);

            }

            @Override
            public void onFail(int HttpCode, String errMsg) {

            }
        });
    }

    /**
     * 此方法描述的是：初始化视图
     *
     * @param view void
     * @author: sky @最后修改人： sky
     * @最后修改日期:2015-7-31 下午6:00:31 initView
     */
    public void initView(View view) {
        registerUserinfoReceiver();
        nextLayout.setVisibility(View.GONE);
        title.setText(getActivity().getResources().getString(R.string.main_tab_wo));
        /*View updateTips = view.findViewById(R.id.tips_update_iv_setting);
        int latestVersion = MySharePerfenceUtil.getInstance().getLatestVersion();
        if (latestVersion > MyApplication.versionCode) {
            isLasted = false;
            updateTips.setVisibility(View.VISIBLE);
        } else {
            updateTips.setVisibility(View.INVISIBLE);
            isLasted = true;
        }*/
    }

    private void initListener(View view) {
        rlUserifo.setOnClickListener(this);
        rlBanzhureninfo.setOnClickListener(this);
        llChongzhi.setOnClickListener(this);
        llDaizhifudingdan.setOnClickListener(this);
        llSuoquan.setOnClickListener(this);
        rlMyfudaoquan.setOnClickListener(this);
        rlMyhomework.setOnClickListener(this);
        rlQuestion.setOnClickListener(this);
        rlEvent.setOnClickListener(this);
        rlSetting.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.e(TAG, "onResume");
        // updateLeftUI();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_userifo:// 个人主页
                MobclickAgent.onEvent(getActivity(), "Open_MyHome");
                IntentManager.goToStuModifiedInfoActivity(getActivity());
                break;
            case R.id.rl_banzhureninfo://班主任
                // 获取班主任id
                int banzhurenid = SharePerfenceUtil.getInt("teather_userid", -1);
                if (banzhurenid == -1) {
                    ToastUtils.show("没有获取到班主任id");
                } else {
                    Intent i = new Intent(getActivity(), BanzhurenActivity.class);
                    i.putExtra("userid", banzhurenid);
                    getActivity().startActivity(i);
                }
                break;

            case R.id.ll_chongzhi:// 充值
                MobclickAgent.onEvent(getActivity(), "Open_Recharge");
                MobclickAgent.onEvent(getActivity(), "learnpointrecharge");

                AppUtils.clickevent("to_buy", (MainActivity) getActivity());
                MobclickAgent.onEvent(getActivity(), "Open_Vip");
                Intent intentxx = new Intent(getActivity(), VipIndexActivity.class);
                intentxx.putExtra("type", userInfo.getVip_type());
                intentxx.putExtra("from_location", 3);
                intentxx.putExtra("user_grade", userInfo.getGrade());
                startActivity(intentxx);


                break;
            case R.id.ll_daizhifudingdan:// 待支付订单
                MobclickAgent.onEvent(getActivity(), "Open_Recharge");
                AppUtils.clickevent("to_buy", (MainActivity) getActivity());
                MobclickAgent.onEvent(getActivity(), "Open_Vip");
                //goVip();
                Intent orderIntent = new Intent(getActivity(), MyOrderListActivity.class);
                startActivity(orderIntent);
                break;
            case R.id.ll_suoquan://索券
                if (getActivity() != null) {
                    Intent intent = new Intent(getActivity(), MyFudaoquanActivity.class);
                    intent.putExtra("type", 3);
                    getActivity().startActivity(intent);
                }
                break;
            case R.id.rl_myfudaoquan:// 我的辅导券
                MobclickAgent.onEvent(getActivity(), "Open_Voucher");
                AppUtils.clickevent("m_ticket", (MainActivity) getActivity());
                if (getActivity() != null) {
                    Intent intent = new Intent(getActivity(), MyFudaoquanActivity.class);

                    getActivity().startActivity(intent);
                }
                break;
            case R.id.rl_myhomework:// 我的作业检查
                MobclickAgent.onEvent(getActivity(), "Open_MyHomework");
                Intent i = new Intent(getActivity(), MyQuestionListActivity.class);

                getActivity().startActivity(i);

                break;

            case R.id.rl_question:// 我的难题
                MobclickAgent.onEvent(getActivity(), "Open_MyQuestion");
                if (getActivity() != null) {
                    Intent intent = new Intent(getActivity(), MyQuestionListActivity.class);
                    intent.putExtra("type", 1);
                    getActivity().startActivity(intent);
                }
                break;

            case R.id.rl_event:// 活动
                MobclickAgent.onEvent(getActivity(), "Open_Activity");
                AppUtils.clickevent("m_activity", (MainActivity) getActivity());
                String codeUrl = "";
                String baseUrl = AppConfig.FUDAOTUAN_URL + "/task.html";
                if (baseUrl.contains("fudaotuan.com")) {// 如果是大熊作业的网址
                    if (baseUrl.contains("?")) {
                        codeUrl = baseUrl + "&userid={0}&phoneos={1}&appversion={2}&now=" + new Date().getTime();
                        codeUrl = codeUrl.replace("{0}", userInfo.getUserid() + "").replace("{1}", "android").replace("{2}",
                                PackageManagerUtils.getVersionCode() + "");

                    } else {
                        codeUrl = baseUrl + "?userid={0}&phoneos={1}&appversion={2}&now=" + new Date().getTime();
                        codeUrl = codeUrl.replace("{0}", userInfo.getUserid() + "").replace("{1}", "android").replace("{2}",
                                PackageManagerUtils.getVersionCode() + "");
                    }
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("title", "活动");
                    intent.putExtra("url", codeUrl);
                    startActivity(intent);

                } else {
                    ToastUtils.show("网址不是大熊作业的网址");
                }

                break;

            case R.id.rl_setting:// 进入设置
                MobclickAgent.onEvent(getActivity(), "Open_SetUp");
                IntentManager.goToSystemSetting(getActivity());
                //startActivity(new Intent(getActivity(), TtsDemoActivity.class));
                break;
            case R.id.btn_logout:// 退出登录
                // MobclickAgent.onEvent(getActivity(), "logout");
                //
                // showDialog(getString(R.string.text_logging_out));
                // HttpHelper.post(getActivity(), "user", "logout", null, new
                // HttpListener() {
                // @Override
                // public void onSuccess(int code, String dataJson, String
                // errMsg) {
                // cleanUseInfo();
                // }
                //
                // @Override
                // public void onFail(int HttpCode) {
                // cleanUseInfo();
                // }
                // });

                break;
            case R.id.menu_about_us:
                IntentManager.goToAbout(getActivity());
                break;
        }
    }


    private void cleanUseInfo() {
        IntentManager.stopWService(getActivity());

        DBHelper.getInstance().getWeLearnDB().deleteCurrentUserInfo();
        MyApplication.mQQAuth.logout(MyApplication.getContext());
        MySharePerfenceUtil.getInstance().setWelearnTokenId("");
        MySharePerfenceUtil.getInstance().setTokenId("");
        MySharePerfenceUtil.getInstance().setIsChoicGream(false);
        MySharePerfenceUtil.getInstance().setGoLoginType(-1);
        // WeLearnSpUtil.getInstance().setGradeId(0);
        // WeLearnSpUtil.getInstance().setOpenId("");
        MySharePerfenceUtil.getInstance().setPhoneNum("");
        MySharePerfenceUtil.getInstance().setAccessToken("");
        if (GlobalVariable.mainActivity != null) {
            GlobalVariable.mainActivity.finish();
        }
        closeDialog();
        IntentManager.goToPhoneLoginActivity(getActivity(), null, true);
    }


    /**
     * 此方法描述的是：更新Ui
     */
    public void updateInfo(WoInfoModel woInfoModel) {

        //获取个人的一些信息
        WoInfoModel.MyInfosBean myInfoModel = woInfoModel.getMy_infos();
        String userAvatarStr = myInfoModel.getAvatar_100();
        String gradeStr = myInfoModel.getGrade();
        String nameStr = myInfoModel.getName();
        int supervip = myInfoModel.getSupervip();
        int vip_type = myInfoModel.getVip_type();
        //获取班主任的一些信息
        WoInfoModel.HeadteacherInfosBean headTeacherInfoModel = woInfoModel.getHeadteacher_infos();
        SharePerfenceUtil.putInt("teather_userid",headTeacherInfoModel.getUserid());
        String headTeacherAvatarStr = headTeacherInfoModel.getAvatar_100();
        String headTeacherName = headTeacherInfoModel.getName();
        String remindStr = headTeacherInfoModel.getRemind();
        //获取券的张数
        int unpay_order_count = woInfoModel.getUnpay_order_count();
        int homework_count = woInfoModel.getHomework_count();
        int question_count = woInfoModel.getQuestion_count();
        int coupon_count = woInfoModel.getCoupon_count();
        String expired_coupon_infos = woInfoModel.getExpired_coupon_infos();
        userInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();

        if (null == userInfo) {
            //ToastUtils.show(R.string.text_unlogin);
            // 处理重新登录
            /*WeLearnApi.relogin(getActivity(), new HttpListener() {
                @Override
                public void onSuccess(int code, String dataJson, String errMsg) {
                    switch (code) {
                        case 0:
                            UserInfoModel uInfo = null;
                            try {
                                uInfo = new Gson().fromJson(dataJson, UserInfoModel.class);
                                if (uInfo != null) {
                                    DBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);
                                }
                                // 打开websocket连接
                                IntentManager.startWService(MyApplication.getContext());
                                //postWithUrl(context, url, data, listener);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;

                        default:
                            // 跳转登录页面
                            MyApplication.mNetworkUtil.disConnect();
                            if (null != getActivity()) {
                                IntentManager.goToPhoneLoginActivity(getActivity(), null, true);
                            }
                            break;
                    }

                }

                @Override
                public void onFail(int HttpCode, String errMsg) {

                }
            });*/

            ToastUtils.show("登录失效，请重新登录");
            if (null != getActivity()) {
                IntentManager.goToPhoneLoginActivity(getActivity(), null, true);
            }

        }

        Glide.with(getActivity()).load(userAvatarStr).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
                .transform(new CropCircleTransformation(getActivity())).placeholder(R.drawable.default_icon_circle_avatar)
                .into(userAvatar);

        tvUserNick.setText(nameStr == null ? "" : userInfo.getName());

        tvXuehao.setText("学号:" + userInfo.getUserid() + "");
        tvGrade.setText(gradeStr);

        Glide.with(getActivity()).load(headTeacherAvatarStr).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
                .transform(new CropCircleTransformation(getActivity())).placeholder(R.drawable.default_icon_circle_avatar)
                .into(ivBanzhurenAvatar);
        if (supervip == 1) {
            userAvatarVip.setVisibility(View.VISIBLE);
        } else {
            userAvatarVip.setVisibility(View.GONE);
        }
        //数据库中的supervip
        DBHelper.getInstance().getWeLearnDB().updateSupervip( userInfo.getUserid(),supervip);

        tvBanzhurenName.setText(headTeacherName);
        tvBanzhurenDes.setText(remindStr);
        tvDaizhifuOrderCount.setText(unpay_order_count + " 笔");
        tvFudaotuanCount.setText(coupon_count + "");
        tvHomeworkCount.setText(homework_count + "");
        tvQuestionCount.setText(question_count + "");
        tvFudaoquanText.setText(expired_coupon_infos);


    }


    /**
     * 跳转到vip
     */
    private void goVip() {
        //			if (userInfo != null) {
        //				Intent intent = new Intent(getActivity(), NoneVipPayMoneyActivity.class);
        //				intent.putExtra("type", userInfo.getVip_type());
        //				startActivity(intent);
        //			}

        if (userInfo != null) {
            if (userInfo.getVip_type() == 0) {// 非vip
                Intent intent = new Intent(getActivity(), VipIndexActivity.class);
                intent.putExtra("type", userInfo.getVip_type());
                intent.putExtra("from_location", 2);
                startActivity(intent);

            } else if (userInfo.getVip_type() == 1) {// 试用vip
                Intent intent = new Intent(getActivity(), VipIndexActivity.class);
                intent.putExtra("type", userInfo.getVip_type());
                intent.putExtra("from_location", 2);
                startActivity(intent);

            } else if (userInfo.getVip_type() == 2) {// 正式vip
                Intent intent = new Intent(getActivity(), VipIndexActivity.class);
                intent.putExtra("type", userInfo.getVip_type());
                intent.putExtra("from_location", 2);
                startActivity(intent);

            } else if (userInfo.getVip_type() == 3) {// 预约vip
                Intent intent = new Intent(getActivity(), VipIndexActivity.class);
                intent.putExtra("type", userInfo.getVip_type());
                intent.putExtra("from_location", 2);
                startActivity(intent);
            } else {// 不是vip或者正式vip
                Intent intent = new Intent(getActivity(), VipIndexActivity.class);
                intent.putExtra("type", userInfo.getVip_type());
                intent.putExtra("from_location", 2);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(getActivity(), VipIndexActivity.class);
            //				intent.putExtra("type", userInfo.getVip_type());
            intent.putExtra("from_location", 2);
            startActivity(intent);
        }
    }

    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        int flag = ((Integer) param[0]).intValue();
        switch (flag) {
            case RequestConstant.GET_USERINFO:// 得到用户信息
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    closeDialog();
                    if (code == 0) {
                        try {
                            String dataJson = JsonUtil.getString(datas, "Data", "");
                            closeDialog();
                            if (!TextUtils.isEmpty(dataJson)) {
                                UserInfoModel uInfo = parseObject(dataJson, UserInfoModel.class);
                                DBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);
//                                updateLeftUI();
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

    public void registerUserinfoReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.action.updateuser");
        getActivity().registerReceiver(receiver, filter);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("com.action.updateuser".equals(action)) {
                //new UserAPI().getUserinfos(requestQueue, WoFragment.this, RequestConstant.GET_USERINFO);
                requestWoInfo();
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
        }
    }
}
