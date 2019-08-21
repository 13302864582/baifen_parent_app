package com.daxiong.fun.function.question;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.toolbox.ImageUtils;
import com.baidu.location.BDLocation;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MainActivity;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.api.FudaoquanAPI;
import com.daxiong.fun.api.MainAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.constant.GradeConstant;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.dialog.CustomFudaoquanPublishTipDialog;
import com.daxiong.fun.dialog.CustomNoFudaoquanTipDialog;
import com.daxiong.fun.dialog.CustomPublicTipDialog;
import com.daxiong.fun.dialog.CustomPublishGiveupDialog;
import com.daxiong.fun.dialog.WelearnDialog;
import com.daxiong.fun.function.account.vip.VipIndexActivity;
import com.daxiong.fun.function.homework.CropImageActivity;
import com.daxiong.fun.function.homework.PublishHomeWorkActivity;
import com.daxiong.fun.function.homework.adapter.PublishHwSubjectAdapter;
import com.daxiong.fun.function.myfudaoquan.ExpireFudaoquanActivity;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.http.RequestParamUtils;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.manager.UploadManager;
import com.daxiong.fun.manager.UploadManager.OnUploadListener;
import com.daxiong.fun.manager.UserManager;
import com.daxiong.fun.model.FudaoquanModel;
import com.daxiong.fun.model.GradeModel;
import com.daxiong.fun.model.OrgModel;
import com.daxiong.fun.model.SubjectModel;
import com.daxiong.fun.model.UploadResult;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.AppUtils;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LocationUtils;
import com.daxiong.fun.util.LocationUtils.LocationChangedListener;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.NetworkUtils;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.util.WeLearnFileUtil;
import com.daxiong.fun.view.MyGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 家长发布问题
 *
 * @author: sky
 */

public class PublishQuestionActivity extends BaseActivity
        implements OnClickListener, OnCheckedChangeListener, HttpListener, OnUploadListener, LocationChangedListener {

    private static final String TAG = PublishQuestionActivity.class.getSimpleName();

    //头部
    @Bind(R.id.next_setp_layout)
    RelativeLayout nextStepLayout;
    @Bind(R.id.back_iv)
    ImageView iv_back;
    @Bind(R.id.back_tv)
    TextView tv_back;
    @Bind(R.id.next_step_btn)
    TextView nextStepTV;

    // 拍照
    @Bind(R.id.pay_answer_take_photo_btn)
    ImageView mTakePhotoBtn;
    // 删除小图片
    @Bind(R.id.pay_answer_question_img_del)
    ImageView iv_del_icon;
    // 描述
    @Bind(R.id.et_description)
    EditText et_description;

    //科目
    @Bind(R.id.rg_subject)
    MyGridView rg_subject;
    // 辅导券
    @Bind(R.id.layout_fudaoquan)
    LinearLayout layout_fudaoquan;
    @Bind(R.id.view_fudaoquan)
    LinearLayout view_fudaoquan;
    @Bind(R.id.tv_fudaotuan_title)
    TextView tv_fudaotuan_title;
    @Bind(R.id.tv_fudoaquan_shijian)
    TextView tv_fudoaquan_shijian;
    @Bind(R.id.tv_fudaoquan_tip)
    TextView tv_fudaoquan_tip;
    @Bind(R.id.btn_fudaoquan)
    Button btn_fudaoquan;
    //发送按钮
    @Bind(R.id.btn_publish_question)
    Button btn_publish_question;
    @Bind(R.id.tv_chongzhi_tip)
    TextView tv_chongzhi_tip;
    @Bind(R.id.ll_fudaoquan_tip)
    LinearLayout ll_fudaoquan_tip;


    private String description = "";

    private int subjectid = -1;

    private int chooseFudaoquanTag = -1;

    private String mPath;

    private int gradeid;

    private GradeModel gradeModel;

    private FudaoquanModel fudaoquanmodel = null;

    private static final int REQUEST_EXPIRE_FUDAOQUAN_CODE = 21312;
    private FudaoquanAPI fudaoquanApi;
    private WelearnDialog mWelearnDialogBuilder;
    private CustomPublicTipDialog tipDialog;
    private InputMethodManager imm;

    private List<SubjectModel> subList = new ArrayList<SubjectModel>();

    private ArrayList<OrgModel> orgList;

    private UserInfoModel mUserInfo;

    private MainAPI mainApi;

    private List<FudaoquanModel> fudaoquanList = null;

    int fudaoquanCount = 0;
    private LocationUtils mLocationUtils;
    private String mLocation;
    private double mLatitude, mLongitude;
    private PublishHwSubjectAdapter subjectAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_question_activity);
        ButterKnife.bind(this);
        initObject();
        initView();
        initListener();
        initData();

    }

    /**
     * 此方法描述的是：初始化对象
     *
     * @author: sky void
     */
    public void initObject() {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        fudaoquanApi = new FudaoquanAPI();
        mainApi = new MainAPI();
        fudaoquanList = new ArrayList<FudaoquanModel>();
        mLocationUtils = LocationUtils.getInstance(this);
    }

    @Override
    public void initView() {
        super.initView();
        setWelearnTitle(R.string.text_ask_title);
        //处理头部的文字和标题
        nextStepTV.setBackgroundResource(R.drawable.publish_btn_selector);
        nextStepTV.setVisibility(View.GONE);
        nextStepTV.setText(R.string.text_question_submit);
        iv_back.setVisibility(View.GONE);
        tv_back.setVisibility(View.VISIBLE);
        tv_back.setText("取消");

        rg_subject.setSelector(new ColorDrawable(Color.TRANSPARENT));//取消GridView中Item选中时默认的背景色
        initSubject();

    }

    public void initSubject() {
        mUserInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
        if (null != mUserInfo) {
            gradeid = mUserInfo.getGradeid();
            gradeModel = DBHelper.getInstance().getWeLearnDB().queryGradeByGradeId(mUserInfo.getGradeid());
        }
        if (gradeModel != null) {
            // subList = DBHelper.getInstance().getWeLearnDB()
            // .querySubjectByIdList(gradeModel.getSubjectIds());
            // UiUtil.initSubjects(this, subList, rg_subject, false);
            // 请求科目
            OkHttpHelper.post(this, "parents", "publishtasksubjects", null, new HttpListener() {
                @Override
                public void onSuccess(int code, String dataJson, String errMsg) {
                    try {
                        if (!TextUtils.isEmpty(dataJson)) {
                            subList = JSON.parseArray(dataJson, SubjectModel.class);
                            //UiUtil.initSubjects(this, subList, rg_subject, false);

                            rg_subject.setNumColumns(3);
                            subjectAdapter = new PublishHwSubjectAdapter(PublishQuestionActivity.this, subList);
                            rg_subject.setAdapter(subjectAdapter);

                        } else {
                            ToastUtils.show(errMsg);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        closeDialog();
                        if (TextUtils.isEmpty(errMsg)) {
                            ToastUtils.show("请求科目失败");
                        } else {
                            ToastUtils.show(errMsg);
                        }
                        finish();

                    }

                }

                @Override
                public void onFail(int HttpCode, String errMsg) {
                    ToastUtils.show(errMsg);
                }
            });



        }

    }

    @Override
    public void initListener() {
        super.initListener();
        findViewById(R.id.back_layout).setOnClickListener(this);
        nextStepLayout.setOnClickListener(this);
        mTakePhotoBtn.setOnClickListener(this);
        iv_del_icon.setOnClickListener(this);
        layout_fudaoquan.setOnClickListener(this);
        btn_publish_question.setOnClickListener(this);
        btn_fudaoquan.setOnClickListener(this);
        tv_chongzhi_tip.setOnClickListener(this);
        //rg_subject.setOnCheckedChangeListener(this);

        rg_subject.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int checkedId = subList.get(position).getSubjectid();
                if (gradeid == GradeConstant.GRADE_PARENT_PRIMARY_ID) {
                    subjectid = checkedId;
                    MySharePerfenceUtil.getInstance().setSubject(String.valueOf(checkedId));
                } else {
                    subjectid = checkedId;
                    MySharePerfenceUtil.getInstance().setSubject(String.valueOf(checkedId));
                }


                for (int k3 = 0; k3 < subList.size(); k3++) {
                    SubjectModel model = subList.get(k3);
                    model.setChecked(0);
                    if (k3 == position) {
                        model.setChecked(1);
                    }
                }
                subjectAdapter.notifyDataSetChanged();

            }

        });
    }

    public void initData() {

        MyApplication.setFudaoquanmodel(null);
        if (mUserInfo.getSupervip() == 1) {
            layout_fudaoquan.setVisibility(View.GONE);

            /*if (mUserInfo.getVip_left_time() > 0) {// 没有过期了

            } else {// 已经过期了
                fudaoquanApi.getFudaoquanList(requestQueue, 1, this, RequestConstant.GET_QUESTION_QUAN_CODE);
            }*/

        } else {
            layout_fudaoquan.setVisibility(View.GONE);
            btn_fudaoquan.setText("使用辅导券");
            // 请求辅导券
            if(NetworkUtils.getInstance().isInternetConnected(this)){
                fudaoquanApi.getFudaoquanList(requestQueue, 1, this, RequestConstant.GET_QUESTION_QUAN_CODE);
            }else{
                ToastUtils.show("网络连接失败,请检查网络");
            }

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onEventBegin(this, "PayAnswerAsk");
        // 从配置文件中读取用户所选的科目
        // String subject = WeLearnSpUtil.getInstance().getSubject();
        // if (gradeid == GlobalContant.GRADE_PARENT_PRIMARY_ID) {
        // WeLearnSpUtil.getInstance().setSubject("0");
        // } else {
        // if (!TextUtils.isEmpty(subject)) {
        // setSubject(Integer.parseInt(subject));
        // } else {
        // }
        // }

        // mDescriptEt.setText(WeLearnSpUtil.getInstance().getDescription());

        if (null != mUserInfo) {
            mUserInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
        }
        mLocationUtils.startBDLocation();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout:// 返回
                doBack();
                break;
            case R.id.tv_chongzhi_tip:// 充值
                goChongzhi();
                break;
            case R.id.pay_answer_take_photo_btn:
                if (WeLearnFileUtil.isFileExist(mPath)) {
                    Bundle data = new Bundle();
                    data.putString(PayAnswerQuestionDetailActivity.IMG_PATH, mPath);
                    data.putBoolean("doNotRoate", true);
                    IntentManager.goToQuestionDetailPicView(this, data);
                } else {
                    IntentManager.goToSelectPicPopupWindow(this);
                }
                break;

            case R.id.btn_publish_question:// 发布
                MobclickAgent.onEvent(this, "Publish_Question");
                if (UserManager.getInstance().judgeIsVIP(mUserInfo)) {//是vip
                    submitQuestion();
                } else {
                    if (fudaoquanList != null && fudaoquanCount > 0) {
                        submitFudaoquanQuestion();
                    } else {
                        noneFudaoquan();
                    }
                }
                break;
            case R.id.pay_answer_question_img_del:
                if (null == mWelearnDialogBuilder) {
                    mWelearnDialogBuilder = WelearnDialog.getDialog(this);
                }
                mWelearnDialogBuilder.withMessage(R.string.text_del_question_pic)
                        .setOkButtonClick(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {
                                    mWelearnDialogBuilder.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (WeLearnFileUtil.deleteFile(mPath)) {
                                    iv_del_icon.setVisibility(View.GONE);
                                    mTakePhotoBtn.setImageResource(R.drawable.carema_icon);
                                }
                            }
                        });
                mWelearnDialogBuilder.show();

                break;

            case R.id.btn_fudaoquan:// 辅导券

                new FudaoquanAPI().getCouponinfos(requestQueue, 1, this, RequestConstant.GET_QUAN_INFO_CODE);

                break;
        }
    }

    // 点击顶部的取消
    private void doBack() {
        description = et_description.getText().toString().trim();
        if (subjectid != -1 || WeLearnFileUtil.isFileExist(mPath) || !TextUtils.isEmpty(description)) {
            final CustomPublishGiveupDialog giveupDialog = new CustomPublishGiveupDialog(this, "取消", "退出", "退出此次编辑?");
            giveupDialog.show();
            giveupDialog.setClicklistener(new CustomPublishGiveupDialog.ClickListenerInterface() {
                @Override
                public void doLeft() {
                    giveupDialog.dismiss();
                }

                @Override
                public void doRight() {
                    giveupDialog.dismiss();
                    MySharePerfenceUtil.getInstance().setGold(0);
                    MySharePerfenceUtil.getInstance().setSubject("");
                    MySharePerfenceUtil.getInstance().setDescription("");
                    if (MyApplication.getFudaoquanmodel() != null) {
                        MyApplication.setFudaoquanmodel(null);
                    }
                    PublishQuestionActivity.this.finish();
                }
            });

        }else{
            MySharePerfenceUtil.getInstance().setGold(0);
            MySharePerfenceUtil.getInstance().setSubject("");
            MySharePerfenceUtil.getInstance().setDescription("");
            if (MyApplication.getFudaoquanmodel() != null) {
                MyApplication.setFudaoquanmodel(null);
            }
            finish();
        }


    }



    private void goChongzhi() {
        if (mUserInfo != null) {
            if (mUserInfo.getVip_type() == 0) {// 非vip
                Intent intent = new Intent(PublishQuestionActivity.this, VipIndexActivity.class);
                intent.putExtra("type", mUserInfo.getVip_type());
                intent.putExtra("from_location", 4);
                startActivity(intent);

            } else if (mUserInfo.getVip_type() == 1) {// 试用vip
                Intent intent = new Intent(PublishQuestionActivity.this, VipIndexActivity.class);
                intent.putExtra("type", mUserInfo.getVip_type());
                intent.putExtra("from_location", 4);
                startActivity(intent);

            } else if (mUserInfo.getVip_type() == 2) {// 正式vip
                Intent intent = new Intent(PublishQuestionActivity.this, VipIndexActivity.class);
                intent.putExtra("type", mUserInfo.getVip_type());
                intent.putExtra("from_location", 4);
                startActivity(intent);

            } else if (mUserInfo.getVip_type() == 3) {// 预约vip
                Intent intent = new Intent(PublishQuestionActivity.this, VipIndexActivity.class);
                intent.putExtra("type", mUserInfo.getVip_type());
                intent.putExtra("from_location", 4);
                startActivity(intent);
            } else {// 不是vip或者正式vip
                Intent intent = new Intent(PublishQuestionActivity.this, VipIndexActivity.class);
                intent.putExtra("type", mUserInfo.getVip_type());
                intent.putExtra("from_location", 4);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(PublishQuestionActivity.this, VipIndexActivity.class);
            intent.putExtra("from_location", 4);
            startActivity(intent);
        }

    }



    /**
     * 没有辅导券
     */
    public void noneFudaoquan() {

        final CustomNoFudaoquanTipDialog nofudaoquanDialog = new CustomNoFudaoquanTipDialog(
                PublishQuestionActivity.this, "", "您目前无可用难题券，不能发布难题", "您可拨打400-6755-222，”充值“", "放弃", "充值");
        nofudaoquanDialog.show();
        nofudaoquanDialog.setOnFangqiListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AppUtils.clickevent("check_to_buy", PublishQuestionActivity.this);
                nofudaoquanDialog.dismiss();
                goChongzhi();
            }
        });

        nofudaoquanDialog.setOnChongzhiListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AppUtils.clickevent("question_gi_up", PublishQuestionActivity.this);
                new FudaoquanAPI().giveuppublish(requestQueue, 1, PublishQuestionActivity.this, RequestConstant.GIVEUP_HW_CODE);
                nofudaoquanDialog.dismiss();

            }
        });



    }

    private void submitQuestion() {
        if (!NetworkUtils.getInstance().isInternetConnected(this)) {
            ToastUtils.show("无可用网络");
            return;
        }

        if (gradeid < 1) {
            ToastUtils.show(getString(R.string.student_publish_nograde_text));
            return;
        }

        if (gradeid > 6) {
        } else {
            if (subjectid == 0) {
                ToastUtils.show(getString(R.string.text_toast_select_subject));
                return;
            }
        }
        if (subjectid == -1) {
            ToastUtils.show(getString(R.string.text_toast_select_subject));
            return;
        }

        if (!WeLearnFileUtil.isFileExist(mPath)) {
            ToastUtils.show(getString(R.string.text_toast_upload_question_pic));
            return;
        }
        description = et_description.getText().toString().trim();
        if (TextUtils.isEmpty(description)) {
            //			description = getString(R.string.text_question_description);
            description = "";
        }
        // WeLearnSpUtil.getInstance().setDescription(descript);


        JSONObject data = new JSONObject();
        Map<String, List<File>> files = new HashMap<String, List<File>>();
        List<File> picFileList = new ArrayList<File>();
        picFileList.add(new File(mPath));
        files.put("picfile", picFileList);

        try {
            data.put("subjectid", subjectid);
            data.put("description", description);
            //			if (fudaoquanList != null && fudaoquanList.size() > 0) {
            //				fudaoquanmodel = fudaoquanList.get(0);
            //				data.put("couponid", fudaoquanmodel.getId());
            //			}
            data.put("longitude", mLongitude);
            data.put("latitude", mLatitude);
            data.put("location", mLocation);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // showDialog("正在上传问题",false);
        showDialog("上传中...");
        UploadManager.upload(AppConfig.GO_URL + "parents/questionpublish", RequestParamUtils.getParam(data), files,
                PublishQuestionActivity.this, true, 0);

    }

    /**
     * 使用辅导券发作业
     */
    private void submitFudaoquanQuestion() {
        if (!NetworkUtils.getInstance().isInternetConnected(PublishQuestionActivity.this)) {
            ToastUtils.show("无可用网络");
            return;
        }

        if (gradeid < 1) {
            ToastUtils.show(getString(R.string.student_publish_nograde_text));
            return;
        }

        if (gradeid > 6) {
        } else {
            if (subjectid == 0) {
                ToastUtils.show(getString(R.string.text_toast_select_subject));
                return;
            }
        }
        if (subjectid == -1) {
            ToastUtils.show(getString(R.string.text_toast_select_subject));
            return;
        }

        if (!WeLearnFileUtil.isFileExist(mPath)) {
            ToastUtils.show(getString(R.string.text_toast_upload_question_pic));
            return;
        }

        String tip = "<p>您目前有<font size=21 color='#ff7200'>" + fudaoquanCount
                + "</font>张可用难题券，本次发布难题，会消耗<font size=21 color='#ff7200'>" + 1 + "</font>张";

        final CustomFudaoquanPublishTipDialog succdialog = new CustomFudaoquanPublishTipDialog(
                PublishQuestionActivity.this, "", tip, "是否确认发布 ?", "否", "是");
        succdialog.show();

        succdialog.setOnCanclesListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                succdialog.dismiss();

            }
        });
        succdialog.setOnOKListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                succdialog.dismiss();

                description = et_description.getText().toString().trim();
                if (TextUtils.isEmpty(description)) {
                    //					description = getString(R.string.text_question_description);
                    description = "";
                }
                // WeLearnSpUtil.getInstance().setDescription(descript);


                JSONObject data = new JSONObject();
                Map<String, List<File>> files = new HashMap<String, List<File>>();
                List<File> picFileList = new ArrayList<File>();
                picFileList.add(new File(mPath));
                files.put("picfile", picFileList);

                try {
                    data.put("subjectid", subjectid);
                    data.put("description", description);
                    data.put("longitude", mLongitude);
                    data.put("latitude", mLatitude);
                    data.put("location", mLocation);
                    if (fudaoquanList != null && fudaoquanList.size() > 0) {
                        fudaoquanmodel = fudaoquanList.get(0);
                        data.put("couponid", fudaoquanmodel.getId());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // showDialog("正在上传问题",false);
                showDialog("上传中...");
                UploadManager.upload(AppConfig.GO_URL + "parents/questionpublish", RequestParamUtils.getParam(data), files,
                        PublishQuestionActivity.this, true, 0);

            }
        });

    }

    @Override
    public void onUploadSuccess(UploadResult result, int index) {

        if (result.getCode() == 0) {

            if (GlobalVariable.myQPadActivity != null) {
                GlobalVariable.myQPadActivity.finish();
            }
            if (GlobalVariable.QAHallActivity != null) {
                GlobalVariable.QAHallActivity.finish();
            }

            if (MyApplication.getFudaoquanmodel() != null) {
                MyApplication.setFudaoquanmodel(null);
            }
            MySharePerfenceUtil.getInstance().setSubject("");
            MySharePerfenceUtil.getInstance().setGold(0);
            MySharePerfenceUtil.getInstance().setDescription("");
            UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
            uInfo.setSixteacher(0);
            uInfo.setTabbarswitch(1);
            DBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);
            Intent intent2 = new Intent(PublishQuestionActivity.this, MainActivity.class);
            Bundle data2 = new Bundle();
            data2.putString("layout", "layout_home");
            intent2.putExtras(data2);
            //closeDialog3(intent2);
            closeDialog();
            startActivity(intent2);
            finish();

        } else {
            ToastUtils.show(result.getMsg());
        }
    }

    @Override
    public void onUploadError(String msg, int index) {
        closeDialog();
        if (!TextUtils.isEmpty(msg)){
            ToastUtils.show(msg);
        }else{
            ToastUtils.show("上传失败");
        }


    }

    @Override
    public void onUploadFail(UploadResult result, int index) {
        closeDialog();
        final String msg = result.getMsg();
        if (!TextUtils.isEmpty(msg)){
            ToastUtils.show(msg);
        }else{
            ToastUtils.show("上传失败");
//            ToastUtils.show("上传失败");
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onEventEnd(this, "PayAnswerAsk");
        mLocationUtils.stopBDLocation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && null != data) {
            switch (requestCode) {
//			case PublishHomeWorkActivity.REQUEST_CODE_GET_IMAGE_FROM_SYS:
                case RequestConstant.REQUEST_CODE_GET_IMAGE_FROM_SYS:
                    String path = data.getStringExtra("path");
                    LogUtils.i(TAG, path);
                    if (TextUtils.isEmpty(path)) {
                        LogUtils.i(TAG, "path is null");
                        return;
                    }

                    boolean isFromPhotoList = data.getBooleanExtra("isFromPhotoList", false);

                    String saveImagePath = WeLearnFileUtil.getQuestionFileFolder().getAbsolutePath() + File.separator
                            + "publish_" + System.currentTimeMillis() + ".jpg";

                    Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(path)));
                    sendBroadcast(localIntent);

                    localIntent.setClass(this, CropImageActivity.class);
                    localIntent.putExtra(PayAnswerImageGridActivity.IMAGE_PATH, path);
                    localIntent.putExtra(CropImageActivity.IMAGE_SAVE_PATH_TAG, saveImagePath);
                    localIntent.putExtra("isFromPhotoList", isFromPhotoList);
                    startActivityForResult(localIntent, PublishHomeWorkActivity.REQUEST_CODE_GET_IMAGE_FROM_CROP);
                    break;
                case PublishHomeWorkActivity.REQUEST_CODE_GET_IMAGE_FROM_CROP:
                    mPath = data.getStringExtra(CropImageActivity.IMAGE_SAVE_PATH_TAG);

                    LogUtils.i(TAG, "path=" + mPath);
                    Bitmap bm = BitmapFactory.decodeFile(mPath);
                    //mTakePhotoBtn.setImageBitmap(bm);

                    //圆角
                    if (null != bm) {
                        int imageSize = PublishQuestionActivity.this.getResources().getDimensionPixelSize(R.dimen.menu_persion_icon_size);
                        bm = ImageUtils.corner(bm, imageSize / 10, imageSize);
                        mTakePhotoBtn.setImageBitmap(bm);
                        mTakePhotoBtn.setVisibility(View.VISIBLE);
                        mTakePhotoBtn.invalidate();
                        iv_del_icon.setVisibility(View.VISIBLE);
                    } else {

                    }
                    break;

                case REQUEST_EXPIRE_FUDAOQUAN_CODE:
                    // 取得辅导券的返回值
                    fudaoquanmodel = (FudaoquanModel) data.getSerializableExtra("fudaoquanmodel");
                    if (fudaoquanmodel != null) {
                        MyApplication.setFudaoquanmodel(fudaoquanmodel);
                        chooseFudaoquanTag = 1;
                        btn_fudaoquan.setText("更换辅导券");
                        layout_fudaoquan.setVisibility(View.GONE);
                        view_fudaoquan.setVisibility(View.VISIBLE);
                        tv_fudoaquan_shijian.setText(fudaoquanmodel.getExpireDate());
                        if (fudaoquanmodel.getType() == 1) {
                            tv_fudaotuan_title.setText("已选择一张难题券");

                        } else {
                            tv_fudaotuan_title.setText("已选择一张作业券");

                        }

                    }
                    break;

            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //		if (gradeid == GradeConstant.GRADE_PARENT_PRIMARY_ID) {
        //			subjectid = checkedId;
        //			MySharePerfenceUtil.getInstance().setSubject(String.valueOf(checkedId));
        //		} else {
        //			subjectid = checkedId;
        //			MySharePerfenceUtil.getInstance().setSubject(String.valueOf(checkedId));
        //		}

    }

    @Override
    public void onSuccess(int code, String dataJson, String errMsg) {
        if (code == 0) {

        } else {
            ToastUtils.show(errMsg);
        }
    }

    @Override
    public void onFail(int HttpCode, String errMsg) {

    }

    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        int flag = ((Integer) param[0]).intValue();
        switch (flag) {
            case RequestConstant.GET_QUESTION_QUAN_CODE:// 获取难题券
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        try {
                            String dataJson = JsonUtil.getString(datas, "Data", "");
                            if (!TextUtils.isEmpty(dataJson)) {
                                fudaoquanList = JSON.parseArray(dataJson, FudaoquanModel.class);
                                for (int i = 0; i < fudaoquanList.size(); i++) {
                                    fudaoquanmodel = fudaoquanList.get(i);
                                    fudaoquanCount += fudaoquanmodel.getCount();
                                }
                                if (fudaoquanList != null && fudaoquanCount > 0) {
                                    layout_fudaoquan.setVisibility(View.GONE);
                                    String tip = "<p>您目前有<font size=21 color='#ff7200'>" + fudaoquanCount
                                            + "</font>张可用难题券，本次发布难题，会消耗<font size=21 color='#ff7200'>" + 1 + "</font>张";
                                    tv_fudaoquan_tip.setText(Html.fromHtml(tip));

                                } else {
                                    layout_fudaoquan.setVisibility(View.GONE);
                                    ll_fudaoquan_tip.setVisibility(View.GONE);
                                    String tip = "您暂无难题券可用，";
                                    tv_fudaoquan_tip.setText(tip);

                                    String underLineText=getResources().getString(R.string.please_chongzhi_buy);
                                    SpannableString spStr = new SpannableString(underLineText);
                                    // 设置字体前景色
                                    spStr.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.colorf74344)), 0, spStr.length(),
                                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色

                                    // 设置字体背景色// 设置背景色
//                                     spStr.setSpan(new BackgroundColorSpan(this.getResources().getColor(R.color.colorf74344)),
//                                     0, spStr.length(),
//                                     Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                    spStr.setSpan(new ClickableSpan() {
                                        @Override
                                        public void updateDrawState(TextPaint ds) {
                                            super.updateDrawState(ds);
                                            ds.setColor(Color.parseColor("#f74344"));       //设置文件颜色
                                            ds.setUnderlineText(true); // 设置下划线
                                        }

                                        @Override
                                        public void onClick(View widget) {
                                            Intent i = new Intent(PublishQuestionActivity.this, VipIndexActivity.class);
                                            startActivity(i);
                                        }
                                    }, 0, underLineText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


                                    tv_fudaoquan_tip.append(spStr);
                                    tv_fudaoquan_tip.setMovementMethod(LinkMovementMethod.getInstance());// 开始响应点击事件


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
            case RequestConstant.GET_QUAN_INFO_CODE:// 获取辅导券的信息
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        try {
                            String dataJson = JsonUtil.getString(datas, "Data", "");
                            if (!TextUtils.isEmpty(dataJson)) {

                                int type = JsonUtil.getInt(dataJson, "type", -1);
                                if (type > 0) {
                                    // 1-有正常可使用的券，2-没有可使用的券,type=2时候才会有，文字说明
                                    if (type == 1) {
                                        Intent fudaoquanIntent = new Intent(this, ExpireFudaoquanActivity.class);
                                        fudaoquanIntent.putExtra("tag", "choice_tag_question");
                                        startActivityForResult(fudaoquanIntent, REQUEST_EXPIRE_FUDAOQUAN_CODE);
                                    } else {
                                        String msgstr = JsonUtil.getString(dataJson, "msg", "");
                                        tipDialog = new CustomPublicTipDialog(PublishQuestionActivity.this, "", msgstr, "",
                                                "立即购买", true);

                                        tipDialog.setOnNegativeListener(new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                tipDialog.dismiss();

                                            }
                                        });

                                        tipDialog.setOnPositiveListener(new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                MobclickAgent.onEvent(PublishQuestionActivity.this, "Open_Vip");
                                                goChongzhi();
                                                tipDialog.dismiss();

                                            }
                                        });
                                        tipDialog.show();
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
                //ToastUtils.show("网络异常");
                break;
        }

    }

    @Override
    public void onLocationChanged(BDLocation bdLocation, String province, String city) {
        //		LogUtils.d(TAG, "p=" + province + ",c=" + city);
        //		ToastUtils.show("bdLocation" + bdLocation.getLatitude() + "," + bdLocation.getLongitude() + "province-->"
        //				+ bdLocation.getAddrStr());
        if (bdLocation != null) {
            this.mLatitude =  bdLocation.getLatitude();
            this.mLongitude =  bdLocation.getLongitude();
            this.mLocation = bdLocation.getAddrStr();
            // mLocationUtils.stopListen();
        }
        mLocationUtils.stopBDLocation();
        //		mLocationUtils.stopBDLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationUtils.stopBDLocation();
        ButterKnife.unbind(this);
    }

}
