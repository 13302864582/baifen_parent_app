
package com.daxiong.fun.function.question;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.api.FudaoquanAPI;
import com.daxiong.fun.api.StudyAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.manager.UploadManager;
import com.daxiong.fun.manager.UploadManager.OnUploadListener;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.constant.GradeConstant;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.dialog.WelearnDialog;
import com.daxiong.fun.function.homework.ChoicePublisherActivity;
import com.daxiong.fun.function.homework.CropImageActivity;
import com.daxiong.fun.function.homework.PublishHomeWorkActivity;
import com.daxiong.fun.function.myfudaoquan.ExpireFudaoquanActivity;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.http.RequestParamUtils;
import com.daxiong.fun.manager.HomeworkManager;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.manager.QuestionManager;
import com.daxiong.fun.model.FudaoquanModel;
import com.daxiong.fun.model.GradeModel;
import com.daxiong.fun.model.OrgModel;
import com.daxiong.fun.model.QuestionRuleModel;
import com.daxiong.fun.model.SubjectModel;
import com.daxiong.fun.model.UploadResult;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.GoldToStringUtil;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.util.UiUtil;
import com.daxiong.fun.util.WeLearnFileUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 此类的描述：外包的拍照提问/疑难答疑
 * 
 * @author: sky
 * @最后修改日期:2015年8月5日 上午11:03:59
 */
public class WaibaoPayAnswerAskActivity extends BaseActivity
        implements OnClickListener, OnCheckedChangeListener, HttpListener, OnUploadListener {

    private static final String TAG = WaibaoPayAnswerAskActivity.class.getSimpleName();

    // private ImageView mPriceBtnMinus;
    // private ImageView mPriceBtnPlus;
    // private TextView mPriceText;

    /////////////////////
    // private int gradeid;
    private TextView nextStepTV/* , goldTitleTV */;

    private RelativeLayout nextStepLayout;

    private ImageView iv_del_icon;// 删除小图片

    private LinearLayout layout_quan_container;
    private LinearLayout layout_not_vip_view;

    private LinearLayout layout_vip_view;

    private EditText et_description;// 描述

    private Button btn_chongzhi;// 充值

    private RelativeLayout layout_choice_publisher;// 选择发题者layout

    private TextView tv_publisher;// 显示发题者

    private RelativeLayout layout_choice_fudao_org;

    private TextView tv_show_org_name;// 选择辅导机构显示

    private ImageView btn_jian;

    private ImageView btn_jia;

    private RadioGroup rg_subject;

    private TextView tv_subject_choice;// 科目选择

    private ImageView mTakePhotoBtn;// 拍照

    private ImageView iv_publiser_arrow;// 选择发布者的箭头

    private TextView tv_question_title; // 悬赏价格

    // object
    private QuestionManager questionManager;

    private HomeworkManager homeworkManager;

    private StudyAPI studyApi;

    private WelearnDialog mWelearnDialogBuilder;

    private InputMethodManager imm;

    // value
    private int orgid;

    private String orgname;

    private int isSpecialStudenType = 0;// 是否是特殊学生帐号

    private int choice_userId = -1;// 选择发题者的id

    private String choice_username = "";

    private UserInfoModel mUserInfo;

    private float minGold;

    private int quespermiss =-1;// 问题权限

    private String specialstudentslistData;// 特殊学生数据

    private ArrayList<OrgModel> orgList;

    private String description = "";

    private int subjectid=-1;

    private String mPath;

    private int gradeid;

    private TextView tv_price_value;// 显示金币的值

    private static final int REQUEST_PUBLISHER_CODE = 0x7;// 发题者

    private List<UserInfoModel> publiserList;// 发题者集合

    private GradeModel gradeModel;

    private ArrayList<SubjectModel> subList = new ArrayList<SubjectModel>();
    
    private LinearLayout layout_fudaoquan;
    private ImageView iv_choice_radio;
    private LinearLayout view_fudaoquan;
    private TextView fudaoquan_tv_type;

    private TextView fudaoquan_tv_data_str;

    private TextView fudaoquan_tv_des;
    
  

    private FudaoquanModel fudaoquanmodel;
    
    private int chooseFudaoquanTag=-1;

    private static final int REQUEST_EXPIRE_FUDAOQUAN_CODE = 21312;

    private FudaoquanAPI fudaoquanApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.outsourcing_pay_answer_ask_activity);
        ExtraData();
        initObject();
        initView();
        initData();
        initListener();
    }

    /**
     * 此方法描述的是：传递过来的数据
     * 
     * @author: sky
     */
    @SuppressWarnings("unchecked")
    private void ExtraData() {
        Intent extraIntent = getIntent();
        if (extraIntent != null) {
            orgid = extraIntent.getIntExtra("orgid", 0);
            orgname = extraIntent.getStringExtra("orgname");
            isSpecialStudenType = extraIntent.getIntExtra("type", 0);
            Serializable serializableExtra = extraIntent.getSerializableExtra(OrgModel.TAG);
            if (serializableExtra instanceof ArrayList<?>) {
                orgList = (ArrayList<OrgModel>)extraIntent.getSerializableExtra(OrgModel.TAG);
            }
        }

    }

    /**
     * 此方法描述的是：初始化对象
     * 
     * @author: sky void
     */
    public void initObject() {
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        studyApi = new StudyAPI();
        homeworkManager = HomeworkManager.getInstance();
        questionManager = QuestionManager.getInstance();
        publiserList = new ArrayList<UserInfoModel>();
        fudaoquanApi = new FudaoquanAPI();
    }

    @Override
    public void initView() {
        super.initView();
        setWelearnTitle(R.string.text_ask_title);
        et_description = (EditText)findViewById(R.id.et_description);
        tv_subject_choice = (TextView)findViewById(R.id.tv_subject_choice);
        rg_subject = (RadioGroup)findViewById(R.id.rg_subject);
        mTakePhotoBtn = (ImageView)findViewById(R.id.pay_answer_take_photo_btn);
        iv_del_icon = (ImageView)findViewById(R.id.pay_answer_question_img_del);

        nextStepLayout = (RelativeLayout)findViewById(R.id.next_setp_layout);
        nextStepTV = (TextView)findViewById(R.id.next_step_btn);
        nextStepTV.setBackgroundResource(R.drawable.publish_btn_selector);
        nextStepTV.setVisibility(View.VISIBLE);
        nextStepTV.setText(R.string.text_question_submit);
        layout_choice_fudao_org = (RelativeLayout)findViewById(R.id.choice_fudao_btn_ask);
        tv_show_org_name = (TextView)findViewById(R.id.fudao_tv_ask);

        layout_choice_publisher = (RelativeLayout)this.findViewById(R.id.layout_choice_publisher);
        tv_publisher = (TextView)this.findViewById(R.id.tv_publisher);
        iv_publiser_arrow = (ImageView)this.findViewById(R.id.iv_publiser_arrow);

        layout_not_vip_view = (LinearLayout)findViewById(R.id.layout_not_vip_view);
        tv_question_title = (TextView)findViewById(R.id.gold_title_tv);
        btn_chongzhi = (Button)findViewById(R.id.chongzhi_btn);
        btn_jian = (ImageView)findViewById(R.id.price_minus_btn);
        tv_price_value = (TextView)findViewById(R.id.question_price);
        btn_jia = (ImageView)findViewById(R.id.price_plus_btn);

        layout_vip_view = (LinearLayout)findViewById(R.id.layout_vip_view);
        
        // 辅导券
        layout_fudaoquan = (LinearLayout)findViewById(R.id.layout_fudaoquan);
        iv_choice_radio = (ImageView)findViewById(R.id.iv_choice_radio);
        view_fudaoquan = (LinearLayout)findViewById(R.id.view_fudaoquan);
        layout_quan_container= (LinearLayout)findViewById(R.id.layout_quan_container);
        layout_quan_container.setBackgroundResource(R.drawable.problem_coupon_bg);
        fudaoquan_tv_type = (TextView)findViewById(R.id.tv_type);
        fudaoquan_tv_data_str = (TextView)findViewById(R.id.tv_data_str);
        fudaoquan_tv_des = (TextView)findViewById(R.id.tv_des);

        layout_not_vip_view.setVisibility(View.GONE);
        layout_vip_view.setVisibility(View.VISIBLE);

        if (orgList != null && orgList.size() == 1) {
            OrgModel choiceListModel = orgList.get(0);
            orgid = choiceListModel.getOrgid();
            orgname = choiceListModel.getOrgname();
        }

        if (orgid == 0) {
            layout_choice_fudao_org.setOnClickListener(this);
        } else {
            tv_show_org_name.setText(orgname);
            findViewById(R.id.fudao_iv_ask).setVisibility(View.GONE);
            // tv_show_org_name.setBackgroundColor(getResources().getColor(R.color.unclickable));
        }

    }

    public void initData() {
        mUserInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
        if (null != mUserInfo) {
            gradeid = mUserInfo.getGradeid();
            gradeModel = DBHelper.getInstance().getWeLearnDB()
                    .queryGradeByGradeId(mUserInfo.getGradeid());
        }
        initRuleInfo(true);
        if (gradeModel != null) {
            if (gradeid == GradeConstant.GRADE_PARENT_PRIMARY_ID) {
                tv_subject_choice.setVisibility(View.GONE);
                rg_subject.setVisibility(View.GONE);
                findViewById(R.id.tv_subject_tips).setVisibility(View.GONE);
                MySharePerfenceUtil.getInstance().setSubject(String.valueOf(0));
            } else {
                initSubject();
                tv_subject_choice.setText(
                        getString(R.string.text_subject_choice_with_grade, gradeModel.getName()));
            }
        }

        // 调用接口是否是机构的特殊账号,决定了是否显示发题者layout
        if (orgList != null && orgList.size() > 0 && isSpecialStudenType == 1) {// 外包特殊帐号
            studyApi.GetSpecialStudentList(requestQueue, orgList.get(0).getOrgid(), this,
                    RequestConstant.REQUEST_SPECIAL_STUDENT_CODE);
        } else {// 外包非特殊学生帐号
            studyApi.GetSpecialStudentPermissionList(requestQueue, orgid, this,
                    RequestConstant.REQUEST_SPECIAL_STUDENT_PERMISS_LIST_CODE);
        }

    }

    public void initSubject() {
        subList = DBHelper.getInstance().getWeLearnDB()
                .querySubjectByIdList(gradeModel.getSubjectIds());
        UiUtil.initSubjects(this, subList, rg_subject, false);
    }

    private void initRuleInfo(boolean retry) {
        QuestionRuleModel qrm = DBHelper.getInstance().getWeLearnDB()
                .queryQuestionRuleInfoByGradeId(gradeModel.getId());
        if (null != qrm) {
            minGold = qrm.getMoney();
        }

        if (minGold <= 0) {
            if (retry) {
//                WeLearnApi.getRuleInfo(this, this);
            } else {
                ToastUtils.show(R.string.server_error);
                finish();
            }
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        findViewById(R.id.back_layout).setOnClickListener(this);
        nextStepLayout.setOnClickListener(this);
        rg_subject.setOnCheckedChangeListener(this);
        mTakePhotoBtn.setOnClickListener(this);
        iv_del_icon.setOnClickListener(this);
        layout_choice_publisher.setOnClickListener(this);
        btn_chongzhi.setOnClickListener(this);
        btn_jian.setOnClickListener(this);
        btn_jia.setOnClickListener(this);
        layout_fudaoquan.setOnClickListener(this);
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

        getValGold();
        double gold = 0;       
        if (null != mUserInfo) {            
            gold = mUserInfo.getGold();
        }else {
            mUserInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
            gold = mUserInfo.getGold();
        }

        String goldStr = GoldToStringUtil.GoldToString(gold);
        String goldTitleStr = getString(R.string.student_publish_home_work_pay_title, goldStr);

        SpannableStringBuilder builder = new SpannableStringBuilder(goldTitleStr);
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
        builder.setSpan(redSpan, goldTitleStr.length() - 1 - goldStr.length(),
                goldTitleStr.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_question_title.setText(builder);

    }

    private void getValGold() {
        float goldVal = MySharePerfenceUtil.getInstance().getGold();
        if (goldVal < minGold) {
            goldVal = minGold;
        }
        setGold(goldVal);
    }

    private void setSubject(int sid) {
        for (int i = 0; i < rg_subject.getChildCount(); i++) {
            View v = rg_subject.getChildAt(i);
            if (v instanceof RadioButton) {
                if (v.getId() == sid) {
                    ((RadioButton)v).setChecked(true);
                } else {
                    ((RadioButton)v).setChecked(false);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout:// 返回
                MySharePerfenceUtil.getInstance().setGold(0);
                MySharePerfenceUtil.getInstance().setSubject("");
                MySharePerfenceUtil.getInstance().setDescription("");
                if (MyApplication.getFudaoquanmodel()!=null) {
                    MyApplication.setFudaoquanmodel(null);
                }   
                finish();
                break;
            case R.id.choice_fudao_btn_ask:// 选择辅导机构
                IntentManager.goToChoiceFudaoActivity(this, orgList);
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

            case R.id.next_setp_layout:// 发布
                if (gradeid > 6) {
                } else {
                    if (subjectid == 0) {
                        ToastUtils.show(getString(R.string.text_toast_select_subject));
                        return;
                    }
                }
                if (subjectid==-1) {
                    ToastUtils.show(getString(R.string.text_toast_select_subject));
                    return;
                }
                
                if (orgid == 0) {
                    ToastUtils.show(getString(R.string.select_fudao_toast_text));
                    return;
                }
                if (!WeLearnFileUtil.isFileExist(mPath)) {
                    ToastUtils.show(getString(R.string.text_toast_upload_question_pic));
                    return;
                }
                description = et_description.getText().toString().trim();
                if (TextUtils.isEmpty(description)) {
                    description = getString(R.string.text_question_description);
                }
                // WeLearnSpUtil.getInstance().setDescription(descript);
                if (layout_choice_publisher.isShown()) {
                    if ("".equals(tv_publisher.getText().toString().trim())) {
                        ToastUtils.show("请选择特殊学生发题");
                        return;
                    }                    
                }
                if (isSpecialStudenType == 1) {// 特殊学生
                    if (quespermiss == 1) {// 有提问权限
                        submitQuestion();
                    } else {// 无提问权限
                        try {
                            submitQuestion(mPath);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } else {// 不是特殊学生
                    if (quespermiss == 1) {// 有提问权限
                        submitQuestion();
                        // submitQuestion(mPath);
                    } else {// 无提问权限
                        try {
                            submitQuestion(mPath);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
                // submitQuestion();
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
            case R.id.layout_choice_publisher:// 选择发题者
                if (orgid != 0 && !TextUtils.isEmpty(orgname)) {
                    Intent intent = new Intent(this, ChoicePublisherActivity.class);
                    if (publiserList != null && publiserList.size() > 0) {
                        intent.putExtra("publiserList", (Serializable)publiserList);
                    }
                    startActivityForResult(intent, REQUEST_PUBLISHER_CODE);
                }

                break;
            case R.id.chongzhi_btn:// 充值
                IntentManager.goPayActivity(this);
                break;

            case R.id.price_minus_btn:
                float priceMinus = Float.parseFloat(tv_price_value.getText().toString());
                if (priceMinus <= minGold) {
                    return;
                }

                priceMinus -= 0.1;

                setGold(priceMinus);
                // WeLearnSpUtil.getInstance().setRecordGold(priceMinus);
                break;
            case R.id.price_plus_btn:
                float pricePlus = Float.parseFloat(tv_price_value.getText().toString());
                pricePlus += 0.1;
                setGold(pricePlus);
                break;
                
            case R.id.layout_fudaoquan:// 辅导券                
                if (MyApplication.getFudaoquanmodel()==null) {
                    Intent fudaoquanIntent = new Intent(this, ExpireFudaoquanActivity.class);
                    fudaoquanIntent.putExtra("tag", "choice_tag_question");
                    startActivityForResult(fudaoquanIntent, REQUEST_EXPIRE_FUDAOQUAN_CODE);
                }else {
                    view_fudaoquan.setVisibility(View.GONE);
                    iv_choice_radio.setBackgroundResource(R.drawable.choice_no_icon);
                    MyApplication.setFudaoquanmodel(null);
                    layout_not_vip_view.setVisibility(View.VISIBLE);
                    
                }
                break;
        }
    }

    private void submitQuestion() {
        JSONObject data = new JSONObject();
        Map<String, List<File>> files = new HashMap<String, List<File>>();
        List<File> picFileList = new ArrayList<File>();
        picFileList.add(new File(mPath));
        files.put("picfile", picFileList);
        if (gradeid < 1) {
            ToastUtils.show(getString(R.string.student_publish_nograde_text));
            return;
        }
        try {
            data.put("orgid", orgid);
            data.put("subjectid", subjectid);
            data.put("description", description);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        if (choice_userId !=1) {// 是特殊学生
            try {
                data.put("stuid", choice_userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        showDialog("正在发布，请稍候");
        UploadManager.upload(AppConfig.GO_URL + "org/qpublish", RequestParamUtils.getParam(data),
                files, WaibaoPayAnswerAskActivity.this, true, 0);

    }

    private void submitQuestion(String path) throws JSONException {
        JSONObject data = new JSONObject();
        Map<String, List<File>> files = new HashMap<String, List<File>>();
        List<File> picFileList = new ArrayList<File>();
        picFileList.add(new File(path));
        files.put("picfile", picFileList);
        UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
        int gradeid = uInfo.getGradeid();
        if (gradeid < 1) {
            ToastUtils.show(getString(R.string.student_publish_nograde_text));
            return;
        }
        try {
            if (null != uInfo) {
                data.put("subjectid", Integer.parseInt(MySharePerfenceUtil.getInstance().getSubject()));
                float gold = MySharePerfenceUtil.getInstance().getGold();
                String goldToString = GoldToStringUtil.GoldToString(gold);
                data.put("bounty", Double.parseDouble(goldToString));
                String desc = MySharePerfenceUtil.getInstance().getDescription();
                if (TextUtils.isEmpty(desc)) {
                    desc = getString(R.string.text_question_description);
                }
                data.put("description", desc);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }        
        showDialog("正在发布，请稍候");
        if (chooseFudaoquanTag==1) {
            data.put("couponid", fudaoquanmodel.getId());
            data.put("bounty",0);
            UploadManager.upload(AppConfig.GO_URL + "question/publish", RequestParamUtils.getParam(data),
                    files, WaibaoPayAnswerAskActivity.this, true, 0);
        }else {
            UploadManager.upload(AppConfig.GO_URL + "question/publish", RequestParamUtils.getParam(data),
                    files, WaibaoPayAnswerAskActivity.this, true, 0);
        }
       

    }

    private void setGold(float gold) {
        tv_price_value.setText(GoldToStringUtil.GoldToString(gold));
        MySharePerfenceUtil.getInstance().setGold(gold);
    }

    @Override
    public void onUploadSuccess(UploadResult result, int index) {
        closeDialog();
        switch (quespermiss) {
            case 1:

                if (result.getCode() == 0) {

                    if (GlobalVariable.myQPadActivity != null) {
                        GlobalVariable.myQPadActivity.finish();
                    }
                    if (GlobalVariable.QAHallActivity != null) {
                        GlobalVariable.QAHallActivity.finish();
                    }
                    ToastUtils.show("发布成功!");
                    if (MyApplication.getFudaoquanmodel()!=null) {
                        MyApplication.setFudaoquanmodel(null);
                    }
                    IntentManager.goToMyQpadActivity(this, true);
                } else {
                    ToastUtils.show(result.getMsg());
                }
            
                break;

            case 0://不包问题的时候回调
                if (result.getCode() == 0) {
                    float minusGold = MySharePerfenceUtil.getInstance().getGold();
                    UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
                    if (null != uInfo) {
                        float gold = uInfo.getGold();
                        gold -= minusGold;
                        uInfo.setGold(gold);
                        DBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);
                    }

                    if (GlobalVariable.myQPadActivity != null) {
                        GlobalVariable.myQPadActivity.finish();
                    }
                    if (GlobalVariable.QAHallActivity != null) {
                        GlobalVariable.QAHallActivity.finish();
                    }
                    ToastUtils.show("发布成功!");
                    MySharePerfenceUtil.getInstance().setSubject("");
                    MySharePerfenceUtil.getInstance().setGold(0);
                    MySharePerfenceUtil.getInstance().setDescription("");
                    IntentManager.goToMyQpadActivity(this, true);
                } else {
                    ToastUtils.show(result.getMsg());
                }
            
                break;
        }
        
        

    }

    @Override
    public void onUploadError(String msg, int index) {
        closeDialog();
        ToastUtils.show(msg);
    }

    @Override
    public void onUploadFail(UploadResult result, int index) {
        closeDialog();
        String msg = result.getMsg();
        if (msg != null) {
            ToastUtils.show(msg);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onEventEnd(this, "PayAnswerAsk");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && null != data) {
            switch (requestCode) {
                case PublishHomeWorkActivity.REQUEST_CODE_GET_IMAGE_FROM_SYS:
                    String path = data.getStringExtra("path");
                    LogUtils.i(TAG, path);
                    if (TextUtils.isEmpty(path)) {
                        LogUtils.i(TAG, "path is null");
                        return;
                    }

                    boolean isFromPhotoList = data.getBooleanExtra("isFromPhotoList", false);

                    String saveImagePath = WeLearnFileUtil.getQuestionFileFolder().getAbsolutePath()
                            + File.separator + "publish_" + System.currentTimeMillis() + ".jpg";

                    Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.fromFile(new File(path)));
                    sendBroadcast(localIntent);

                    localIntent.setClass(this, CropImageActivity.class);
                    localIntent.putExtra(PayAnswerImageGridActivity.IMAGE_PATH, path);
                    localIntent.putExtra(CropImageActivity.IMAGE_SAVE_PATH_TAG, saveImagePath);
                    localIntent.putExtra("isFromPhotoList", isFromPhotoList);
                    startActivityForResult(localIntent,
                            PublishHomeWorkActivity.REQUEST_CODE_GET_IMAGE_FROM_CROP);
                    break;
                case PublishHomeWorkActivity.REQUEST_CODE_GET_IMAGE_FROM_CROP:
                    mPath = data.getStringExtra(CropImageActivity.IMAGE_SAVE_PATH_TAG);

                    LogUtils.i(TAG, "path=" + mPath);
                    Bitmap bm = BitmapFactory.decodeFile(mPath);
                    iv_del_icon.setVisibility(View.VISIBLE);

                    mTakePhotoBtn.setImageBitmap(bm);
                    break;
                case 1002:
                    orgid = data.getIntExtra("orgid", 0);
                    orgname = data.getStringExtra("orgname");
                    tv_show_org_name.setText(orgname);

                    break;
                case REQUEST_PUBLISHER_CODE:// 选择发题者
                    choice_userId = data.getIntExtra("userid", 0);
                    choice_username = data.getStringExtra("username");
                    tv_publisher.setText(choice_username);

                    if (isSpecialStudenType == 1) {// 特殊学生帐号
                        boolean isRn = questionManager.getUseridInQuestionlist(choice_userId,
                                specialstudentslistData);
                        // 可以免费发作业
                        quespermiss=1;
                        if (isRn) {
                            layout_choice_fudao_org.setVisibility(View.GONE);
                            layout_not_vip_view.setVisibility(View.GONE);
                            layout_vip_view.setVisibility(View.VISIBLE);
                        }                       

                    } else {// 不是特殊学生帐号
                            // 选择学生获取userid，然后返回这个学生权限
                        studyApi.GetSpecialStudentPermissionList(requestQueue, orgid, this,
                                RequestConstant.REQUEST_SPECIAL_STUDENT_PERMISS_LIST_CODE);

                    }
                    break;
                case REQUEST_EXPIRE_FUDAOQUAN_CODE:                   
                    // 取得辅导券的返回值
                    fudaoquanmodel = (FudaoquanModel)data.getSerializableExtra("fudaoquanmodel");
                    if (fudaoquanmodel != null) {
                        MyApplication.setFudaoquanmodel(fudaoquanmodel);
                        chooseFudaoquanTag=1;
                        iv_choice_radio.setBackgroundResource(R.drawable.choice_icon);
                        view_fudaoquan.setVisibility(View.VISIBLE);
                        fudaoquan_tv_data_str.setText(fudaoquanmodel.getExpireDate());
                        if (fudaoquanmodel.getType() == 1) {
                            fudaoquan_tv_type.setText("难题券");
                            fudaoquan_tv_des.setText("限发布难题使用");
                        } else {
                            fudaoquan_tv_type.setText("作业券");
                            fudaoquan_tv_des.setText("限发布作业使用");
                        }
                        
                        //不显示悬赏学点                        
                        layout_not_vip_view.setVisibility(View.GONE);
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
            int[] leftTop = {
                    0, 0
            };
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top
                    && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (gradeid == GradeConstant.GRADE_PARENT_PRIMARY_ID) {
            subjectid = 0;
        } else {
            subjectid = checkedId;
            MySharePerfenceUtil.getInstance().setSubject(String.valueOf(checkedId));
        }

    }

    @Override
    public void onSuccess(int code, String dataJson, String errMsg) {
        if (code == 0) {
            initRuleInfo(false);
        } else {
            ToastUtils.show(errMsg);
        }
    }

    @Override
    public void onFail(int HttpCode,String errMsg) {

    }

    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        int flag = ((Integer)param[0]).intValue();
        switch (flag) {
            case RequestConstant.REQUEST_SPECIAL_STUDENT_CODE:// 查询特殊学生账号列表
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        try {
                            String dataJson = JsonUtil.getString(datas, "Data", "");
                            specialstudentslistData = dataJson;
                            if (!TextUtils.isEmpty(dataJson)) {
                                List<UserInfoModel> list = questionManager
                                        .parseSpecialStudent(dataJson);
                                publiserList.addAll(list);
                                if (publiserList != null && publiserList.size() > 0) {
                                    layout_choice_fudao_org.setVisibility(View.GONE);
                                    layout_choice_publisher.setVisibility(View.VISIBLE);

                                    layout_not_vip_view.setVisibility(View.GONE);
                                    layout_vip_view.setVisibility(View.VISIBLE);
                                } else {
                                    layout_choice_fudao_org.setVisibility(View.VISIBLE);
                                    layout_choice_publisher.setVisibility(View.GONE);
                                    layout_not_vip_view.setVisibility(View.GONE);
                                    layout_vip_view.setVisibility(View.VISIBLE);
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(
                                            WaibaoPayAnswerAskActivity.this);
                                    dialog.setTitle("提示");
                                    dialog.setMessage("没有学生你可以为他发题，暂不能发题");
                                    dialog.setPositiveButton("确定",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                        int which) {
                                                    dialog.dismiss();
                                                    finish();
                                                }
                                            });
                                    dialog.show();

                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
                break;
            case RequestConstant.REQUEST_SPECIAL_STUDENT_PERMISS_LIST_CODE:// 外包非特殊学生得到权限列表
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        try {
                            String dataStr = JsonUtil.getString(datas, "Data", "");
                            if (!TextUtils.isEmpty(dataStr)) {
                                JSONObject dataJson = new JSONObject(dataStr);
                                JSONObject permissObj = dataJson.getJSONObject("permissions");
                                quespermiss = permissObj.optInt("question");
                                int homeworkmiss = permissObj.optInt("homework");
                                if (quespermiss == 1) {// 提问免费
                                    changeViewInWaibao(true);
                                    layout_fudaoquan.setVisibility(View.GONE);
                                } else {// 提问不免费
                                    changeViewInWaibao(false);
                                    fudaoquanApi.getFudaoquanList(requestQueue, 1, this, RequestConstant.GET_QUESTION_QUAN_CODE);
                                    
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
                break;
            case RequestConstant.GET_QUESTION_QUAN_CODE:// 获取难题券
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        try {
                            String dataJson = JsonUtil.getString(datas, "Data", "");                            
                            if (!TextUtils.isEmpty(dataJson)) {
                                List<FudaoquanModel> subList = new Gson().fromJson(dataJson,
                                        new TypeToken<List<FudaoquanModel>>() {
                                        }.getType());
                                
                                if (subList!=null&&subList.size()>0) {
                                    layout_fudaoquan.setVisibility(View.VISIBLE);
                                }else {
                                    layout_fudaoquan.setVisibility(View.GONE);
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

    /**
     * 此方法描述的是：根据选择的学生发作业的权限改变界面
     * 
     * @author: sky void
     */
    public void changeViewInWaibao(boolean isFree) {
        if (isFree) {
            // 发作业免费
            quespermiss = 1;
            if (isSpecialStudenType == 1) {
                layout_choice_fudao_org.setVisibility(View.GONE);
            } else {
                layout_choice_fudao_org.setVisibility(View.VISIBLE);
            }
            layout_choice_fudao_org.setVisibility(View.VISIBLE);
            layout_not_vip_view.setVisibility(View.GONE);
            layout_vip_view.setVisibility(View.VISIBLE);

        } else {
            // 发作业不免费
            quespermiss = 0;
            if (isSpecialStudenType == 1) {
                layout_choice_fudao_org.setVisibility(View.GONE);
            } else {
                layout_choice_fudao_org.setVisibility(View.VISIBLE);
            }
            layout_choice_fudao_org.setVisibility(View.GONE);
            layout_not_vip_view.setVisibility(View.VISIBLE);
            layout_vip_view.setVisibility(View.GONE);

        }
    }

}
