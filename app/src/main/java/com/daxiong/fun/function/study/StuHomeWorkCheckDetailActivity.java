
package com.daxiong.fun.function.study;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.api.HomeWorkAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.EventConstant;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.constant.HomeworkStatusConstant;
import com.daxiong.fun.function.homework.SingleHwAnalysisModel;
import com.daxiong.fun.function.homework.adapter.StuHomeWorkDetailAdapter;
import com.daxiong.fun.function.homework.model.HomeWorkCheckPointModel;
import com.daxiong.fun.function.homework.model.HomeWorkModel;
import com.daxiong.fun.function.homework.model.StuPublishHomeWorkPageModel;
import com.daxiong.fun.function.homework.view.AddPointCommonView;
import com.daxiong.fun.function.homework.view.AdoptHomeWorkCheckDialog;
import com.daxiong.fun.function.homework.view.RefuseHomeWorkPopWindow;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.MediaUtil;
import com.daxiong.fun.util.MediaUtil.ResetImageSourceCallback;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.NetworkUtils;
import com.daxiong.fun.util.SharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.CropCircleTransformation;
import com.daxiong.fun.view.MyViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StuHomeWorkCheckDetailActivity extends BaseActivity implements OnClickListener, OnPageChangeListener {
    public AddPointCommonView mAddPointView;

    public void setmTextView(TextView mTextView) {
        this.mTextView = mTextView;
    }

    private int currentPosition;

    private ArrayList<View> dotLists;

    private RelativeLayout layout_dots_ll, homework_detail_bottom_container_stu;

    private LinearLayout dots_ll;

    private MyViewPager mViewPager;

    private HomeWorkModel mHomeWorkModel;

    private ArrayList<StuPublishHomeWorkPageModel> mHomeWorkPageModelList;

    private ImageView mAvatarStuIv, iv_tishi;

    private TextView mNickStuTv;

    private TextView mUserId;
    private TextView mTextView;

    private TextView mNumStuTv;

    private LinearLayout ll, tv_tishi;

    private int avatarSize;
    private int type;
    private boolean isShow2 = false;
    // private TextView mCollectTextTv;
    // private ImageView mCollectIconIv;
    private StuHomeWorkDetailAdapter mAdapter;

    private RefuseHomeWorkPopWindow mRefusePopWindow;

    private AdoptHomeWorkCheckDialog mAdoptDialog;

    private boolean isCurrentUser;

    public boolean checkingFlag;

    private TextView mGradeStuTv;

    private boolean refreshing;

    ArrayList<HomeWorkCheckPointModel> sWrongPointList;

    ArrayList<HomeWorkCheckPointModel> kWrongPointList;
    // public boolean isAdoptOrRefuse;

    // private HashMap<String, ArrayList<HomeWorkCheckPointModel>
    // >wrongPointMap;

    private RelativeLayout next_setp_layout;

    private TextView next_step_btn, next_step_btn2;
    int[] sta = null;//存储每页的对号和错号的显示隐藏状态

    @Override
    @SuppressLint("InlinedApi")
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.homework_check_detail);
        initView();

    }

    @Override
    public void initView() {
        super.initView();
        setWelearnTitle(R.string.homework_detail_title_text);

        ll = (LinearLayout) findViewById(R.id.ll);

        findViewById(R.id.back_layout).setOnClickListener(this);

        avatarSize = getResources().getDimensionPixelSize(R.dimen.avatar_size_homework_check_common);

        findViewById(R.id.back_layout).setOnClickListener(this);
        findViewById(R.id.title_layout).setVisibility(View.GONE);
        layout_dots_ll = (RelativeLayout) this.findViewById(R.id.layout_dots_ll);
        layout_dots_ll.setVisibility(View.VISIBLE);
        dots_ll = (LinearLayout) findViewById(R.id.dots_ll);
        mViewPager = (MyViewPager) findViewById(R.id.detail_pager_homework);
        mViewPager.setOffscreenPageLimit(8);

        iv_tishi = (ImageView) findViewById(R.id.iv_tishi);
        mAvatarStuIv = (ImageView) findViewById(R.id.stu_avatar_iv_stu_detail);
        tv_tishi = (LinearLayout) findViewById(R.id.tv_tishi);
        mNickStuTv = (TextView) findViewById(R.id.stu_nick_tv_stu_detail);
        mUserId = (TextView) this.findViewById(R.id.tv_stu_userid);
        mGradeStuTv = (TextView) findViewById(R.id.stu_grade_tv_stu_detail);
        mNumStuTv = (TextView) findViewById(R.id.stu_num_tv_stu_detail);
        next_setp_layout = (RelativeLayout) findViewById(R.id.next_setp_layout);
        next_setp_layout.setVisibility(View.VISIBLE);
        next_step_btn = (TextView) this.findViewById(R.id.next_step_btn);
        next_step_btn2 = (TextView) this.findViewById(R.id.next_step_btn2);
        next_step_btn2.setVisibility(View.VISIBLE);
        next_step_btn.setText("");
        next_step_btn.setHeight(30);
        next_step_btn.setWidth(30);
        next_step_btn.setBackgroundResource(R.drawable.biaozhu_close_eye);

        next_step_btn.setOnClickListener(this);
        next_step_btn2.setOnClickListener(this);
        iv_tishi.setOnClickListener(this);
        findViewById(R.id.refuse_quxiao).setOnClickListener(this);
        findViewById(R.id.fasong).setOnClickListener(this);

        // mCollectTextTv = (TextView)
        // findViewById(R.id.collection_count_tv_stu_detail);
        // mCollectIconIv = (ImageView)
        // findViewById(R.id.collection_icon_iv_stu_detail);

        homework_detail_bottom_container_stu = (RelativeLayout) findViewById(R.id.homework_detail_bottom_container_stu);
        homework_detail_bottom_container_stu.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        if (intent != null) {
            isShow2 = intent.getBooleanExtra("isShow", true);
            type = intent.getIntExtra("type", 0);//区别可以投诉  1是自己的题可以投诉 2是推荐的题不可以投诉
            int taskid = intent.getIntExtra("taskid", 0);
            currentPosition = intent.getIntExtra("position", 0);
            // mHomeWorkModel = (HomeWorkModel)
            // intent.getSerializableExtra(HomeWorkModel.TAG);
            refreshHomeWorkData(taskid);

        }

    }

    private void setData() {
        int userId = MySharePerfenceUtil.getInstance().getUserId();
        isCurrentUser = mHomeWorkModel.getStudid() == userId;
        mHomeWorkPageModelList = mHomeWorkModel.getPagelist();
        initDot(mHomeWorkPageModelList.size(), currentPosition);

        int state = mHomeWorkModel.getState();

        if (SharePerfenceUtil.getInt("sbtishi", 0) == 1 | state < 2 | type != 1) {
            iv_tishi.setVisibility(View.GONE);
        } else {
            iv_tishi.setVisibility(View.GONE);

        }
        switch (state) {
            case HomeworkStatusConstant.ADOPTED:// 已采纳
            case HomeworkStatusConstant.ARBITRATED:// 仲裁完成
                findViewById(R.id.ll_dangban).setVisibility(View.VISIBLE);
                if (isCurrentUser) {
                    findViewById(R.id.refuse_adopt_btn_container_stu_detail).setVisibility(View.VISIBLE);
                    findViewById(R.id.analysis_btn_stu_detail).setVisibility(View.VISIBLE);
                    findViewById(R.id.analysis_btn_stu_detail).setOnClickListener(this);
                } else {
                    // findViewById(R.id.collect_container_ll_stu_detail).setVisibility(View.VISIBLE);
                    // findViewById(R.id.collect_container_ll_stu_detail).setOnClickListener(this);
                    // int praise = mHomeWorkModel.getPraise();// 0是空心 1是实心
                    // setIsCollectHomeWork(praise == 1);
                }
                break;
            case HomeworkStatusConstant.ANSWERING:// 答题中
                if (isCurrentUser) {
                    checkingFlag = true;
                    findViewById(R.id.ll_dangban).setVisibility(View.VISIBLE);
                }
                break;
            case HomeworkStatusConstant.ANSWERED:// 已回答
                if (isCurrentUser) {
                    findViewById(R.id.analysis_btn_stu_detail).setVisibility(View.VISIBLE);
                    findViewById(R.id.analysis_btn_stu_detail).setOnClickListener(this);
                    findViewById(R.id.refuse_btn_stu_detail).setOnClickListener(this);
                    findViewById(R.id.refuse_btn_stu_detail).setVisibility(View.VISIBLE);
                    findViewById(R.id.adot_btn_stu_detail).setOnClickListener(this);
                    findViewById(R.id.adot_btn_stu_detail).setVisibility(View.VISIBLE);
                    findViewById(R.id.refuse_adopt_btn_container_stu_detail).setVisibility(View.VISIBLE);
                }
                findViewById(R.id.analysis_btn_stu_detail).setOnClickListener(this);
                break;
            case HomeworkStatusConstant.APPENDASK:// 追问中
                if (isCurrentUser) {
                    checkingFlag = true;
                    findViewById(R.id.refuse_btn_stu_detail).setOnClickListener(this);
                    findViewById(R.id.refuse_btn_stu_detail).setVisibility(View.VISIBLE);
                }
            case HomeworkStatusConstant.REFUSE:// 已拒绝
            case HomeworkStatusConstant.ARBITRATE:// 仲裁中
                if (isCurrentUser) {
                    findViewById(R.id.adot_btn_stu_detail).setOnClickListener(this);
                    findViewById(R.id.adot_btn_stu_detail).setVisibility(View.VISIBLE);
                    findViewById(R.id.analysis_btn_stu_detail).setVisibility(View.VISIBLE);
                    findViewById(R.id.analysis_btn_stu_detail).setOnClickListener(this);
                    findViewById(R.id.refuse_adopt_btn_container_stu_detail).setVisibility(View.VISIBLE);
                }

                // findViewById(R.id.analysis_btn_stu_detail).setVisibility(View.VISIBLE);
                findViewById(R.id.analysis_btn_stu_detail).setOnClickListener(this);
                break;
            case HomeworkStatusConstant.REPORT:// 已举报
            case HomeworkStatusConstant.DELETE:// 已删除
            case HomeworkStatusConstant.ASKING:// 抢题中
                break;

            default:
                break;
        }

        // ImageLoader.getInstance().loadImageWithDefaultAvatar(mHomeWorkModel.getAvatar(),
        // mAvatarStuIv, avatarSize,
        // avatarSize / 10);
        Glide.with(StuHomeWorkCheckDetailActivity.this).load(mHomeWorkModel.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).bitmapTransform(new CropCircleTransformation(this))
                .placeholder(R.drawable.default_icon_circle_avatar).into(mAvatarStuIv);
        mAvatarStuIv.setOnClickListener(this);
        String studname = mHomeWorkModel.getStudname();
        if (!TextUtils.isEmpty(studname)) {
            mNickStuTv.setText(studname);
        }

        int stuid = mHomeWorkModel.getStudid();
        if (!TextUtils.isEmpty(stuid + "")) {
            mUserId.setText("学号:" + stuid);
        }
        String grade = mHomeWorkModel.getGrade();
        if (!TextUtils.isEmpty(grade)) {
            mGradeStuTv.setText(grade);
        }
        int homeworkcnt = mHomeWorkModel.getHomeworkcnt();
        mNumStuTv.setText(getString(R.string.ask_num_text, homeworkcnt + ""));
        if (mHomeWorkModel.getState() == 1 && isShow2) {
            isShow2 = false;
        }
        mAdapter = new StuHomeWorkDetailAdapter(getSupportFragmentManager(), mHomeWorkPageModelList,
                StuHomeWorkCheckDetailActivity.this, type, isShow2, mHomeWorkModel.getStudid());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setCurrentItem(currentPosition, false);// 加上fasle表示切换时不出现平滑效果

    }

    private void refreshHomeWorkData(int taskid) {
        if (!NetworkUtils.getInstance().isInternetConnected(getApplicationContext())) {
            ToastUtils.show("网络连接失败");
            return;
        }
        refreshing = true;
        JSONObject data = new JSONObject();
        try {
            data.put("taskid", taskid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpHelper.post(this, "parents", "homeworkgetone", data, new HttpListener() {

            @Override
            public void onFail(int code, String errMsg) {
                ToastUtils.show("获取数据失败");
            }

            @Override
            public void onSuccess(int code, String dataJson, String errMsg) {
                if (code == 0) {
                    try {
                        mHomeWorkModel = JSON.parseObject(dataJson, HomeWorkModel.class);
                        mHomeWorkPageModelList = mHomeWorkModel.getPagelist();
                        SharePerfenceUtil.putInt("HomeWorkTaskid", mHomeWorkModel.getTaskid());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (mHomeWorkPageModelList != null) {
                        setData();
                        sWrongPointList = new ArrayList<HomeWorkCheckPointModel>();
                        kWrongPointList = new ArrayList<HomeWorkCheckPointModel>();
                        // wrongPointMap.put("s", sWrongPointList);
                        // wrongPointMap.put("k", kWrongPointList);

                        sta = new int[mHomeWorkPageModelList.size()];
                        int index = 0;
                        for (StuPublishHomeWorkPageModel pageModel : mHomeWorkPageModelList) {
                            ArrayList<HomeWorkCheckPointModel> checkpointlist = pageModel.getCheckpointlist();
                            boolean flag1 = false;
                            boolean flag2 = false;
                            if (checkpointlist != null) {
                                for (HomeWorkCheckPointModel checkPointModel : checkpointlist) {
                                    if (mHomeWorkModel.getState() == HomeworkStatusConstant.ANSWERING
                                            | mHomeWorkModel.getState() == HomeworkStatusConstant.ANSWERED
                                            | mHomeWorkModel.getState() == HomeworkStatusConstant.APPENDASK) {
                                        checkPointModel.setAllowAppendAsk(true);
                                    }
                                    checkPointModel.setGrabuserid(mHomeWorkModel.getGrabuserid());
                                    String teacheravatar = mHomeWorkModel.getTeacheravatar();
                                    if (teacheravatar != null) {
                                        checkPointModel.setTeacheravatar(teacheravatar);
                                    }
                                    checkPointModel.setTeacherhomeworkcnt(mHomeWorkModel.getTeacherhomeworkcnt());
                                    String teachername = mHomeWorkModel.getTeachername();
                                    if (teachername != null) {
                                        checkPointModel.setTeachername(teachername);
                                    }

                                    if (checkPointModel.getIsright() == GlobalContant.RIGHT_HOMEWORK) {
                                        flag1 = true;
                                    }

                                    if (checkPointModel.getIsright() == GlobalContant.WRONG_HOMEWORK) {
                                        flag2 = true;
                                        String wrongtype = checkPointModel.getWrongtype();
                                        if (!TextUtils.isEmpty(wrongtype)) {
                                            switch (wrongtype) {
                                                case "s":
                                                    sWrongPointList.add(checkPointModel);
                                                    break;
                                                case "k":
                                                    kWrongPointList.add(checkPointModel);
                                                    break;

                                                default:
                                                    break;
                                            }
                                        }
                                    }

                                }
                                if (checkpointlist.size() == 0) {
                                    sta[index] = 0;
                                } else if (flag1 && flag2) {//有对错
                                    sta[index] = 1;
                                } else if (flag1 && !flag2) {//全对
                                    sta[index] = 2;
                                } else if (flag2 && !flag1) {//全错
                                    sta[index] = 3;
                                }
                                index++;

                            }


                        }

                        mAdapter.setAllPageData(mHomeWorkPageModelList);
                        refreshing = false;

                        int st = sta[currentPosition % mHomeWorkPageModelList.size()];

                        if (st == 0) {
                            next_setp_layout.setVisibility(View.GONE);
                        } else if (st == 1 || st == 2 || st == 3) {
                            next_setp_layout.setVisibility(View.VISIBLE);
                            isShow = 3;
                            if (isShow == 3) {//全部显示
                                if (mAdapter != null) {
                                    mAdapter.showPoint(currentPosition, isShow);
                                    next_step_btn.setBackgroundResource(R.drawable.biaozhu_close_eye);
                                    //ToastUtils.show(isShow+"");
                                    if (st == 1) {
                                        isShow = 1;
                                    } else {
                                        isShow = 2;
                                    }
                                    return;
                                }
                            }
                        }


                    }
                } else {
                    ToastUtils.show(errMsg);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (checkingFlag && !refreshing) {
            if (mHomeWorkModel != null) {
                refreshHomeWorkData(mHomeWorkModel.getTaskid());
                mViewPager.setCurrentItem(currentPosition, false);
            }
        }
        GlobalVariable.mViewPager = mViewPager;
    }

    public void refresh() {

        if (mHomeWorkModel != null) {
            refreshHomeWorkData(mHomeWorkModel.getTaskid());
            mViewPager.setCurrentItem(currentPosition, false);
        }

    }

    // 初始化点
    private void initDot(int size, int defalutPosition) {
        if (size < 2) {
            dots_ll.setVisibility(View.GONE);
        } else {
            dots_ll.setVisibility(View.VISIBLE);
            dotLists = new ArrayList<View>();
            dots_ll.removeAllViews();
            for (int i = 0; i < size; i++) {
                // 设置点的宽高
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(this, 6),
                        DensityUtil.dip2px(this, 6));
                // 设置点的间距
                params.setMargins(7, 0, 7, 0);
                // 初始化点的对象
                View m = new View(this);
                // 把点的宽高设置到view里面
                m.setLayoutParams(params);
                if (i == defalutPosition) {
                    // 默认情况下，首先会调用第一个点。就必须展示选中的点
                    m.setBackgroundResource(R.drawable.dot_checked);
                } else {
                    // 其他的点都是默认的。
                    m.setBackgroundResource(R.drawable.dot_normal);
                }
                // 把所有的点装载进集合
                dotLists.add(m);
                // 现在的点进入到了布局里面
                dots_ll.addView(m);
            }
        }
    }

    private void selectDot(int postion) {
        for (View dot : dotLists) {
            dot.setBackgroundResource(R.drawable.dot_normal);
        }
        dotLists.get(postion).setBackgroundResource(R.drawable.dot_checked);
    }

    // private void setIsCollectHomeWork(boolean isCollected) {
    // if (isCollected) {
    // mCollectIconIv.setImageResource(R.drawable.adopt_star_icon_pre);
    // mCollectTextTv.setText(R.string.collect_homework_text_quxiao);
    // mHomeWorkModel.setPraise(1);
    // } else {
    // mCollectIconIv.setImageResource(R.drawable.adopt_star_icon);
    // mCollectTextTv.setText(R.string.collect_homework_text);
    // mHomeWorkModel.setPraise(0);
    // }
    // }


    public void fankui() {
        if (MyApplication.checklist.size() == 0) {
            return;
        }
        JSONObject jobj = new JSONObject();
        StringBuffer sb = new StringBuffer();
        try {
            for (int i = 0; i < MyApplication.checklist.size(); i++) {

                sb.append(MyApplication.checklist.get(i) + ",");

            }
            String str = sb.toString();
            String str2 = str.substring(0, str.length() - 1);
            jobj.put("complaint_type", 1);
            jobj.put("checkpointids", str2);
            jobj.put("taskid", mHomeWorkModel.getTaskid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkHttpHelper.post(StuHomeWorkCheckDetailActivity.this, "parents", "complainthomework", jobj, new HttpListener() {
            @Override
            public void onSuccess(int code, String dataJson, String errMsg) {
                if (code == 0) {
                    if (mHomeWorkModel != null) {
                        refreshHomeWorkData(mHomeWorkModel.getTaskid());
                        mViewPager.setCurrentItem(currentPosition, false);
                    }
                    MyApplication.checklist.clear();
                    if (mTextView != null) {
                        mTextView.setText("错批反馈");
                    }
                    changState(false);
                    ToastUtils.show("发送成功");
                } else {
                    ToastUtils.show(errMsg);
                }
            }

            @Override
            public void onFail(int HttpCode, String errMsg) {
                ToastUtils.show("反馈失败");
            }
        });

    }

    int isShow = 1;//0-表示对错全部显示 1-表示显示错  2-隐藏全部图标

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.refuse_quxiao:
                changState(false);
                refresh();
                break;
            case R.id.back_layout:
                finish();
                break;
            case R.id.iv_tishi:
                iv_tishi.setVisibility(View.GONE);
                SharePerfenceUtil.putInt("sbtishi", 1);
                break;
            case R.id.fasong:
                fankui();
                break;
            case R.id.next_step_btn2:
                View popuView = View.inflate(StuHomeWorkCheckDetailActivity.this, R.layout.popu_xianshi_menu, null);

                View v2 = (View) popuView.findViewById(R.id.v2);
                TextView tv1 = (TextView) popuView.findViewById(R.id.tv1);
                TextView tv2 = (TextView) popuView.findViewById(R.id.tv2);
                TextView tv3 = (TextView) popuView.findViewById(R.id.tv3);
                v2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
                final PopupWindow pw = new PopupWindow(popuView, FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT, true);
                tv1.setOnClickListener(new OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               pw.dismiss();
                                               if (mAdapter != null) {
                                                   mAdapter.showPoint(currentPosition, 3);
                                                   next_step_btn2.setText("显示批注");

                                               }
                                           }
                                       }
                );
                tv2.setOnClickListener(new OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               pw.dismiss();
                                               if (mAdapter != null) {
                                                   mAdapter.showPoint(currentPosition, 2);
                                                   next_step_btn2.setText("隐藏批注");

                                               }
                                           }
                                       }
                );
                tv3.setOnClickListener(new OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               pw.dismiss();
                                               if (mAdapter != null) {
                                                   mAdapter.showPoint(currentPosition, 1);
                                                   next_step_btn2.setText("只看错题");

                                               }
                                           }
                                       }
                );
                pw.setBackgroundDrawable(new ColorDrawable(0));

                pw.showAsDropDown(next_step_btn2, -DensityUtil.dip2px(StuHomeWorkCheckDetailActivity.this, 24), DensityUtil.dip2px(StuHomeWorkCheckDetailActivity.this, 4));


                break;
            case R.id.refuse_btn_stu_detail:
                MobclickAgent.onEvent(this, "Homewrok_Refuse");
                clickRefuse(view);
                break;
            case R.id.adot_btn_stu_detail:
                MobclickAgent.onEvent(this, "Homewrok_Adopt");

                clickAdot();
                break;
            // case R.id.collect_container_ll_stu_detail:
            // clickCollect();
            // break;
            case R.id.stu_avatar_iv_stu_detail:
                int userid = mHomeWorkModel.getStudid();
                IntentManager.gotoPersonalPage(this, userid, GlobalContant.ROLE_ID_STUDENT);
                break;
            case R.id.root_adopt_dialog:// 点击dialog的时候
                // case R.id.degree_ratingBar_adopt_dialog://点击dialog的时候
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(view.findViewById(R.id.comment_et_adopt_dialog).getWindowToken(), 0);
                break;
            case R.id.analysis_btn_stu_detail:
                uMengEvent("homework_analysisfromdetail");

                changState(true);

                break;

            default:
                break;
        }

    }

    private void clickRefuse(View view) {
        mRefusePopWindow = new RefuseHomeWorkPopWindow(this, view);
    }

    public void refuseAnswer(String reason) {
        showDialog("请稍候");
        JSONObject data = new JSONObject();
        try {
            data.put("taskid", mHomeWorkModel.getTaskid());

            data.put("comment", reason);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpHelper.post(this, "parents", "homeworkrefuseanswer", data, new HttpListener() {

            @Override
            public void onSuccess(int code, String dataJson, String errMsg) {
                closeDialog();
                if (code == 0) {

                    mHomeWorkModel.setState(HomeworkStatusConstant.REFUSE);
                    checkingFlag = false;
                    ToastUtils.show("已拒绝");
                    uMengEvent("homework_refuse");
                } else {
                    ToastUtils.show(errMsg);
                }

            }

            @Override
            public void onFail(int HttpCode, String errMsg) {

            }
        });

    }

    private void clickAdot() {
        mAdoptDialog = new AdoptHomeWorkCheckDialog(this, new AdoptHomeWorkCheckDialog.AdoptSubmitBtnClick() {

            @Override
            public void ensure(int degree, String comment, int checkbox) {
                showDialog("请稍候");
                JSONObject data = new JSONObject();
                mHomeWorkModel.setSatisfaction(degree);
                try {
                    data.put("taskid", mHomeWorkModel.getTaskid());
                    data.put("satisfaction", degree);
                    data.put("comment", comment);
                    data.put("hopeteacheranswernext", checkbox);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                OkHttpHelper.post(StuHomeWorkCheckDetailActivity.this, "parents", "homeworkadoptanswer", data, new HttpListener() {

                    @Override
                    public void onSuccess(int code, String dataJson, String errMsg) {
                        closeDialog();
                        if (code == 0) {
                            uMengEvent("homework_adopt");

                            findViewById(R.id.ll_dangban).setVisibility(View.VISIBLE);
                            mHomeWorkModel.setState(HomeworkStatusConstant.ADOPTED);
                            checkingFlag = false;
                            ToastUtils.show("成功采纳");
                            // isAdoptOrRefuse = true;
                        } else {
                            ToastUtils.show(errMsg);
                        }
                    }

                    @Override
                    public void onFail(int HttpCode, String errMsg) {
                        closeDialog();

                    }
                });

            }

        });

        mAdoptDialog.show();
    }

    // private void clickCollect() {
    // showDialog("请稍候");
    //
    // JSONObject data = new JSONObject();
    // try {
    // data.put("taskid", mHomeWorkModel.getTaskid());
    // data.put("tasktype", 2);
    // } catch (JSONException e) {
    // e.printStackTrace();
    // }
    //
    // HttpHelper.post("common", "collect", data, new HttpListener() {
    //
    // @Override
    // public void onSuccess(int code, String dataJson, String errMsg) {
    // closeDialog();
    // if (code == 0) {
    // int praise = mHomeWorkModel.getPraise();// 0是空心 1是实心
    // if (praise == 0) {
    // uMengEvent("homework_collect");
    // setIsCollectHomeWork(true);
    // } else {
    // uMengEvent("homework_removecollect");
    // setIsCollectHomeWork(false);
    // }
    // } else {
    // ToastUtils.show(errMsg);
    // }
    // }
    //
    // @Override
    // public void onFail(int HttpCode) {
    // closeDialog();
    // }
    // });
    // }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int postion) {
        currentPosition = postion;
        mAddPointView = mAdapter.getFragment(currentPosition).getmAddPointView();
        selectDot(postion);
        // refreshCurrentPicData();

        int st = sta[postion % mHomeWorkPageModelList.size()];

        if (st == 0) {
            next_setp_layout.setVisibility(View.GONE);
        } else if (st == 1 || st == 2 || st == 3) {

            next_setp_layout.setVisibility(View.VISIBLE);
            isShow = 3;
            if (isShow == 3) {//全部显示
                if (mAdapter != null) {
                    mAdapter.showPoint(currentPosition, isShow);
                    next_step_btn.setBackgroundResource(R.drawable.biaozhu_close_eye);
                    //ToastUtils.show(isShow+"");
                    if (st == 1) {
                        isShow = 1;
                    } else {
                        isShow = 2;
                    }

                    return;
                }
            }
        }

    }

    private ImageView iv_teacher_avatar;

    private LinearLayout layout_edit;

    private TextView tv_sumary;

    private FrameLayout layout_record;

    private ImageView iv_voice;

    private ImageView iv_del;

    private AnimationDrawable mAnimationDrawable;

    private HomeWorkAPI homeworkApi;

    private Dialog mAnalysisDialog = null;

    public void openHwAnalysisDialog() {

        View viewdialog = View.inflate(this, R.layout.single_analysis_activity, null);
        mAnalysisDialog = new Dialog(this, R.style.analysisdialog);
        mAnalysisDialog.setContentView(viewdialog);
        // 重点在于这句话，把背景的透明度设为完全透明，就看不到后面那个稍大一点的视图了。
        viewdialog.getBackground().setAlpha(0);
        mAnalysisDialog.setContentView(viewdialog);
        mAnalysisDialog.setCanceledOnTouchOutside(false);
        layout_edit = (LinearLayout) viewdialog.findViewById(R.id.layout_edit);
        tv_sumary = (TextView) viewdialog.findViewById(R.id.tv_sumary);
        layout_record = (FrameLayout) viewdialog.findViewById(R.id.layout_record);
        iv_voice = (ImageView) viewdialog.findViewById(R.id.iv_voice);
        iv_teacher_avatar = (ImageView) viewdialog.findViewById(R.id.iv_teacher_avatar);
        iv_del = (ImageView) viewdialog.findViewById(R.id.iv_del);
        homeworkApi = new HomeWorkAPI();
        iv_del.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // MediaUtil.getInstance(false).stopRecord(voiceValue,
                // mCallback);
                MediaUtil.getInstance(false).stopPlay();
                // MediaUtil.getInstance(false).stopLocalAudio();
                MediaUtil.getInstance(false).resetAnimationPlay(iv_voice);
                mAnalysisDialog.dismiss();
            }
        });

		/*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
		 * 对象,这样这可以以同样的方式改变这个Activity的属性.
		 */
        Window dialogWindow = mAnalysisDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);

		/*
         * lp.x与lp.y表示相对于原始位置的偏移.
		 * 当参数值包含Gravity.LEFT时,对话框出现在左边,所以lp.x就表示相对左边的偏移,负值忽略.
		 * 当参数值包含Gravity.RIGHT时,对话框出现在右边,所以lp.x就表示相对右边的偏移,负值忽略.
		 * 当参数值包含Gravity.TOP时,对话框出现在上边,所以lp.y就表示相对上边的偏移,负值忽略.
		 * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以lp.y就表示相对下边的偏移,负值忽略.
		 * 当参数值包含Gravity.CENTER_HORIZONTAL时
		 * ,对话框水平居中,所以lp.x就表示在水平居中的位置移动lp.x像素,正值向右移动,负值向左移动.
		 * 当参数值包含Gravity.CENTER_VERTICAL时
		 * ,对话框垂直居中,所以lp.y就表示在垂直居中的位置移动lp.y像素,正值向右移动,负值向左移动.
		 * gravity的默认值为Gravity.CENTER,即Gravity.CENTER_HORIZONTAL |
		 * Gravity.CENTER_VERTICAL. 本来setGravity的参数值为Gravity.LEFT |
		 * Gravity.TOP时对话框应出现在程序的左上角,但在
		 * 我手机上测试时发现距左边与上边都有一小段距离,而且垂直坐标把程序标题栏也计算在内了, Gravity.LEFT, Gravity.TOP,
		 * Gravity.BOTTOM与Gravity.RIGHT都是如此,据边界有一小段距离
		 */
        // lp.x = 100; // 新位置X坐标
        // lp.y = 100; // 新位置Y坐标
        // lp.width = 300; // 宽度
        // lp.height = 300; // 高度
        // lp.alpha = 0.7f; // 透明度

        // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
        // dialog.onWindowAttributesChanged(lp);
        // dialogWindow.setAttributes(lp);

		/*
		 * 将对话框的大小按屏幕大小的百分比设置
		 */
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.85); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);

        mAnalysisDialog.show();

        initAnalysisData();
    }

    public void initAnalysisData() {
        if (NetworkUtils.getInstance().isInternetConnected(this)) {
            if (mHomeWorkModel.getTaskid() > 0) {
                homeworkApi.viewremark(requestQueue, mHomeWorkModel.getTaskid(), this,
                        RequestConstant.SINGLE_HW_ANALYSIS);
            } else {
                ToastUtils.show("参数传递错误");
            }

        } else {
            ToastUtils.show("无网络");
        }

    }

    private void showVoice(final SingleHwAnalysisModel model) {
        // 显示语音
        layout_record.setVisibility(View.VISIBLE);
        iv_voice.setImageResource(R.drawable.ic_play2);
        layout_record.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                MobclickAgent.onEvent(StuHomeWorkCheckDetailActivity.this, EventConstant.CUSTOM_EVENT_PLAY_AUDIO);
				/*
				 * StatService .onEvent( mActivity, EventConstant
				 * .CUSTOM_EVENT_PLAY_AUDIO, "");
				 */
                if (TextUtils.isEmpty(model.getRemark_snd_url())) {
                    ToastUtils.show(R.string.text_audio_is_playing_please_waiting);
                    return;
                }
                iv_voice.setImageResource(R.drawable.play_animation);
                mAnimationDrawable = (AnimationDrawable) iv_voice.getDrawable();
                MyApplication.animationDrawables.add(mAnimationDrawable);
                MyApplication.anmimationPlayViews.add(iv_voice);
				/*
				 * WeLearnMediaUtil.getInstance(false) .stopPlay();
				 */
                MediaUtil.getInstance(false).playVoice(false, model.getRemark_snd_url(), mAnimationDrawable,
                        new ResetImageSourceCallback() {

                            @Override
                            public void reset() {
                                iv_voice.setImageResource(R.drawable.ic_play2);
                                // isSound[i] =
                                // false;
                            }

                            @Override
                            public void playAnimation() {
                            }

                            @Override
                            public void beforePlay() {
                                MediaUtil.getInstance(false).resetAnimationPlay(iv_voice);
                            }
                        }, null);
            }
        });

    }

    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        int flag = ((Integer) param[0]).intValue();
        switch (flag) {
            case RequestConstant.SINGLE_HW_ANALYSIS:// 单个作业分析
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        try {
                            closeDialog();
                            String dataStr = JsonUtil.getString(datas, "Data", "");
                            if (!TextUtils.isEmpty(dataStr)) {
                                SingleHwAnalysisModel singleHwAnalysisModel = JSON.parseObject(dataStr,
                                        SingleHwAnalysisModel.class);

                                bindData(singleHwAnalysisModel);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            closeDialog();
                        }
                    } else {
                        ToastUtils.show(msg);
                    }

                }
                break;

        }

    }

    @SuppressWarnings("unchecked")
    public void bindData(SingleHwAnalysisModel singleHwAnalysisModel) {
        if (!TextUtils.isEmpty(singleHwAnalysisModel.getAvatar())) {
            Glide.with(StuHomeWorkCheckDetailActivity.this).load(singleHwAnalysisModel.getAvatar())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CropCircleTransformation(StuHomeWorkCheckDetailActivity.this))
                    .placeholder(R.drawable.teacher_img).into(iv_teacher_avatar);
        }
        if (!TextUtils.isEmpty(singleHwAnalysisModel.getRemark_txt())) {
            layout_edit.setVisibility(View.VISIBLE);
            tv_sumary.setVisibility(View.VISIBLE);
            tv_sumary.setText(singleHwAnalysisModel.getRemark_txt());
        } else {
            layout_edit.setVisibility(View.VISIBLE);
            tv_sumary.setVisibility(View.VISIBLE);
            tv_sumary.setText("暂时没有作业分析哦");
        }

        if (TextUtils.isEmpty(singleHwAnalysisModel.getRemark_snd_url())) {
            layout_record.setVisibility(View.GONE);
        } else {
            if (!TextUtils.isEmpty(singleHwAnalysisModel.getRemark_snd_url())
                    && singleHwAnalysisModel.getRemark_snd_url().contains(".amr")) {
                layout_record.setVisibility(View.VISIBLE);
                // Glide.with(this).load(singleHwAnalysisModel.getAvatar())
                // .bitmapTransform(new CropCircleTransformation(this))
                // .placeholder(R.drawable.teacher_img).into(iv_user_avatar_voice);
                showVoice(singleHwAnalysisModel);
            } else {
                layout_record.setVisibility(View.GONE);
            }
        }
		
		

	/*	if (!TextUtils.isEmpty(singleHwAnalysisModel.getRemark_snd_url())
				&& singleHwAnalysisModel.getRemark_snd_url().contains(".amr")) {
			layout_record.setVisibility(View.VISIBLE);
			// Glide.with(this).load(singleHwAnalysisModel.getAvatar())
			// .bitmapTransform(new CropCircleTransformation(this))
			// .placeholder(R.drawable.teacher_img).into(iv_user_avatar_voice);
			showVoice(singleHwAnalysisModel);
			layout_edit.setVisibility(View.GONE);
			tv_sumary.setVisibility(View.GONE);
			tv_sumary.setText("");
		} else {
			layout_record.setVisibility(View.GONE);
			layout_edit.setVisibility(View.VISIBLE);
			tv_sumary.setVisibility(View.VISIBLE);
			tv_sumary.setText("暂时没有作业分析哦");
		}*/


    }

    public void changState(boolean falg) {

        if (falg) {
            // 反馈状态
            //homework_detail_bottom_container_stu.setVisibility(View.GONE);
            tv_tishi.setVisibility(View.VISIBLE);
            mViewPager.isMoveInPager = true;
            mViewPager.misMove = true;
            mAddPointView.isFankui = falg;
        } else {
            // 正常状态
            //homework_detail_bottom_container_stu.setVisibility(View.VISIBLE);
            tv_tishi.setVisibility(View.GONE);
            mViewPager.isMoveInPager = false;
            mViewPager.misMove = false;
            mAddPointView.isFankui = falg;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == GlobalContant.RESULT_OK) {
            refreshHomeWorkData(mHomeWorkModel.getTaskid());
            mViewPager.setCurrentItem(currentPosition, false);
        }
    }

    @Override
    public void onBackPressed() {
        if (GlobalVariable.answertextPopupWindow != null && GlobalVariable.answertextPopupWindow.isShowing()) {
            GlobalVariable.answertextPopupWindow.dismiss();

            return;
        }
        if (mAdoptDialog != null && mAdoptDialog.isShowing()) {
            mAdoptDialog.dismiss();
        } else if (mRefusePopWindow != null && mRefusePopWindow.isShowing()) {
            mRefusePopWindow.dismiss();
        } else {
            super.onBackPressed();
        }
    }
}
