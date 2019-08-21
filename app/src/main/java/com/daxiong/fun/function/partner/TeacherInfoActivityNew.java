
package com.daxiong.fun.function.partner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.R;
import com.daxiong.fun.adapter.TeacherCommentAdapter;
import com.daxiong.fun.api.HomeListAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.EventConstant;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.Teacher_info;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.NetworkUtils;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.CropCircleTransformation;
import com.daxiong.fun.view.XListView;
import com.daxiong.fun.view.glide.BlurTransformation;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 学生搜索老师的结果页面
 *
 * @author: sky
 */
public class TeacherInfoActivityNew extends BaseActivity implements OnClickListener, XListView.IXListViewListener {

    public static final String TAG = TeacherInfoActivityNew.class.getSimpleName();

    private Teacher_info teacher_info;
    private Teacher_info.Teacher_infos uInfos;
    private List<Teacher_info.Comments> comments=new ArrayList<>();

    private ImageView mTec_info_bg_iv;
    private ImageView mIv_back;
    private TextView mTec_jiangjie;
    private TextView mTec_info_nick_tv;

    private TextView mTec_info_sex_tv;
    private TextView mUserid_tv_tec_cen;
    private TextView mTec_info_school_tv;
    private ImageView mTec_info_head_iv;

    private TextView[] tvs = new TextView[3];

    private TextView mTv_cainalv;
    private TextView mTv_zuoye;
    private TextView mTv_nanti;
    private ImageView[] mIv_tais = new ImageView[5];
    private ImageView[] mIv_nengs = new ImageView[5];
    private TeacherCommentAdapter adapter;
    private String sexstr = "他";
    ;

    private View headview;
    private XListView xListView;
    private Button communicate_btn;
    private int target_user_id;
    private int type2 = 0;
    private int page = 0;
    private int type = 0;
    private HomeListAPI homeListAPI;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_teacher_listinfo);


        Intent intent = getIntent();
        if (null == intent) {
            finish();
        }
        target_user_id = intent.getIntExtra("userid", 0);


        if (target_user_id == 0) {
            ToastUtils.show(R.string.userid_error);
            finish();
        }
        homeListAPI = new HomeListAPI();
        initView();

        initData();


    }

    public void initData() {
        if (NetworkUtils.getInstance().isInternetConnected(this)) {
            showDialog("数据加载中...");
            homeListAPI.getteacherinfos(requestQueue, this, type, target_user_id, page, 5, RequestConstant.GET_FANKUILIYOU_CODE);
        } else {
            ToastUtils.show("请检查您的网络");
        }

    }

    public void initView() {

        xListView = (XListView) findViewById(R.id.answer_list);
        communicate_btn = (Button) findViewById(R.id.communicate_btn);
        headview = View.inflate(this, R.layout.activity_teacher_info, null);
        xListView.addHeaderView(headview);

        mTec_info_bg_iv = (ImageView) findViewById(R.id.tec_info_bg_iv);
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mTec_jiangjie = (TextView) findViewById(R.id.tec_jiangjie);
        mTec_info_nick_tv = (TextView) findViewById(R.id.tec_info_nick_tv);

        mTec_info_sex_tv = (TextView) findViewById(R.id.tec_info_sex_tv);
        mUserid_tv_tec_cen = (TextView) findViewById(R.id.userid_tv_tec_cen);
        mTec_info_school_tv = (TextView) findViewById(R.id.tec_info_school_tv);
        mTec_info_head_iv = (ImageView) findViewById(R.id.tec_info_head_iv);

        tvs[0] = (TextView) headview.findViewById(R.id.tec_good_subject_xiao_tv);
        tvs[1] = (TextView) headview.findViewById(R.id.tec_good_subject_chu_tv);
        tvs[2] = (TextView) headview.findViewById(R.id.tec_good_subject_gao_tv);
        mTv_cainalv = (TextView) headview.findViewById(R.id.tv_cainalv);
        mTv_zuoye = (TextView) headview.findViewById(R.id.tv_zuoye);
        mTv_nanti = (TextView) headview.findViewById(R.id.tv_nanti);
        mIv_tais[0] = (ImageView) headview.findViewById(R.id.iv_tai1);
        mIv_tais[1] = (ImageView) headview.findViewById(R.id.iv_tai2);
        mIv_tais[2] = (ImageView) headview.findViewById(R.id.iv_tai3);
        mIv_tais[3] = (ImageView) headview.findViewById(R.id.iv_tai4);
        mIv_tais[4] = (ImageView) headview.findViewById(R.id.iv_tai5);
        mIv_nengs[0] = (ImageView) headview.findViewById(R.id.iv_neng1);
        mIv_nengs[1] = (ImageView) headview.findViewById(R.id.iv_neng2);
        mIv_nengs[2] = (ImageView) headview.findViewById(R.id.iv_neng3);
        mIv_nengs[3] = (ImageView) headview.findViewById(R.id.iv_neng4);
        mIv_nengs[4] = (ImageView) headview.findViewById(R.id.iv_neng5);
        communicate_btn.setOnClickListener(this);
        mIv_back.setOnClickListener(this);
        mTec_jiangjie.setOnClickListener(this);
        xListView.setXListViewListener(this);

        xListView.setPullRefreshEnable(false);
        xListView.setPullLoadEnable(true);

    }


    private void updateUiInfo() {

        if (null == uInfos) {
            LogUtils.e(TAG, "Contact info gson is null !");
            return;
        }

        Glide.with(TeacherInfoActivityNew.this).load(uInfos.getAvatar())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(TeacherInfoActivityNew.this))
                .placeholder(R.drawable.default_icon_circle_avatar).into(mTec_info_head_iv);

        Glide.with(TeacherInfoActivityNew.this)
                .load(uInfos.getAvatar())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new BlurTransformation(TeacherInfoActivityNew.this, 100))
                .placeholder(R.drawable.mohubg)
                .into(mTec_info_bg_iv);

        mTec_info_nick_tv.setText(uInfos.getName());

        if (uInfos.getSex() == 0) {
            mTec_info_sex_tv.setVisibility(View.GONE);
        } else if (uInfos.getSex() == 1) {
            mTec_info_sex_tv.setText("男");
        } else if (uInfos.getSex() == 2) {
            mTec_info_sex_tv.setText("女");
            sexstr = "她";
        }
        int age = uInfos.getAge() == 0 ? 1 : uInfos.getAge();
        mUserid_tv_tec_cen.setText(age + "年教龄");
        mTec_info_school_tv.setText(uInfos.getSchool());
        if (uInfos.getRelationtype() == 1) {
            type2 = 0;
            mTec_jiangjie.setText("不喜欢" + sexstr + "讲解");
            mTec_jiangjie.setTextColor(Color.parseColor("#80ffffff"));
        } else {
            type2 = 1;
            mTec_jiangjie.setText("喜欢" + sexstr + "讲解");
        }
        final String[] split = uInfos.getSkill_subjects().split(";");
        for (int i = 0; i < split.length; i++) {
            tvs[i].setVisibility(View.VISIBLE);
            tvs[i].setText(split[i]);
        }
        mTv_cainalv.setText(NumberFormat.getPercentInstance().format(uInfos.getAdopt_rate()));
        mTv_zuoye.setText(uInfos.getHomework_cnt() + "");
        mTv_nanti.setText(uInfos.getQuesiton_cnt() + "");
        for (int i = 0; i < (int) uInfos.getAttitude_index() && i < 5; i++) {
            mIv_tais[i].setImageResource(R.drawable.star_btn_select);

        }
        for (int i = 0; i < (int) uInfos.getResponsibility_index() && i < 5; i++) {
            mIv_nengs[i].setImageResource(R.drawable.star_btn_select);

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tec_jiangjie:
                showDialog("");
                if (type2 == 1) {
                    homeListAPI.liketeacherexplain(requestQueue, this, target_user_id, RequestConstant.GET_QUESTION_LIST_CODE);
                } else {
                    homeListAPI.desertteacherexplain(requestQueue, this, target_user_id, RequestConstant.COMMIT_FANKUILIYOU_CODE);
                }
                break;

            case R.id.communicate_btn:

                Bundle data3 = new Bundle();
                data3.putInt("userid", target_user_id);
                data3.putInt("roleid", 2);

                MobclickAgent.onEvent(this, EventConstant.CUSTOM_EVENT_CHAT);
                IntentManager.goToChatListView(this, data3, false);

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
            case RequestConstant.GET_FANKUILIYOU_CODE:
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");

                    if (code == 0) {
                        try {
                            String dataJson = JsonUtil.getString(datas, "Data", "");

                            if (!TextUtils.isEmpty(dataJson)) {

                                teacher_info = JSON.parseObject(dataJson, Teacher_info.class);
                                if (page == 0) {
                                    if(teacher_info.getComments()!=null){
                                        comments =teacher_info.getComments() ;
                                    }

                                } else {
                                    comments.addAll(teacher_info.getComments());
                                }
                                if (type == 0) {
                                    adapter = new TeacherCommentAdapter(this, comments);
                                    xListView.setAdapter(adapter);
                                    type = 1;
                                    uInfos = teacher_info.getTeacher_infos();
                                    updateUiInfo();
                                }


                                if (comments.size() < 5) {
                                    xListView.setPullLoadEnable(false);

                                } else {
                                    xListView.setPullLoadEnable(true);

                                }
                                adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        ToastUtils.show(msg);
                    }
                }

                break;
            case RequestConstant.GET_QUESTION_LIST_CODE://喜欢
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    closeDialog();
                    if (code == 0) {
                        ToastUtils.show("操作成功");
                        type2 = 0;
                        mTec_jiangjie.setText("不喜欢" + sexstr + "讲解");
                        mTec_jiangjie.setTextColor(Color.parseColor("#80ffffff"));
                    } else {
                        ToastUtils.show(msg);
                    }
                }

                break;
            case RequestConstant.COMMIT_FANKUILIYOU_CODE://不喜欢
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    closeDialog();
                    if (code == 0) {
                        ToastUtils.show("操作成功");
                        type2 = 1;
                        mTec_jiangjie.setText("喜欢" + sexstr + "讲解");
                        mTec_jiangjie.setTextColor(Color.parseColor("#ffffff"));


                    } else {
                        ToastUtils.show(msg);
                    }
                }

                break;

            case -1:
                closeDialog();
                ToastUtils.show("服务器或网络超时");
                break;

        }

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
}
