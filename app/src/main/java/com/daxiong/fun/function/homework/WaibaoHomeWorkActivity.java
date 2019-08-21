
package com.daxiong.fun.function.homework;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.api.FudaoquanAPI;
import com.daxiong.fun.api.HomeWorkAPI;
import com.daxiong.fun.api.StudyAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.manager.UploadManager;
import com.daxiong.fun.manager.UploadManager.OnUploadListener;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.GradeConstant;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.constant.ResponseCmdConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.dialog.WelearnDialog;
import com.daxiong.fun.function.AnswerListItemView;
import com.daxiong.fun.function.homework.adapter.StuPublishHomeWorkAdapter;
import com.daxiong.fun.function.homework.adapter.StuPublishHomeWorkAdapter.OnImageDeleteClickListener;
import com.daxiong.fun.function.homework.model.StuPublishHomeWorkModel;
import com.daxiong.fun.function.homework.model.StuPublishHomeWorkPageModel;
import com.daxiong.fun.function.homework.model.StuPublishHomeWorkUploadFileModel;
import com.daxiong.fun.function.homework.model.StuPublishHomeWorkUploadModel;
import com.daxiong.fun.function.homework.teacher.TecHomeWorkDetail_OnlyReadActivity;
import com.daxiong.fun.function.myfudaoquan.ExpireFudaoquanActivity;
import com.daxiong.fun.function.question.PayAnswerImageGridActivity;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.http.RequestParamUtils;
import com.daxiong.fun.manager.HomeworkManager;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.FudaoquanModel;
import com.daxiong.fun.model.GradeModel;
import com.daxiong.fun.model.HomeWorkRuleModel;
import com.daxiong.fun.model.OrgModel;
import com.daxiong.fun.model.SubjectModel;
import com.daxiong.fun.model.UploadResult;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.util.GoldToStringUtil;
import com.daxiong.fun.util.ImageUtil;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.util.UiUtil;
import com.daxiong.fun.util.WeLearnFileUtil;
import com.daxiong.fun.view.MyGridView;
import com.daxiong.fun.view.MyRadioGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 此类的描述：外包发布作业
 * 
 * @author: sky
 * @最后修改日期:2015年8月4日 上午9:28:48
 */
public class WaibaoHomeWorkActivity extends BaseActivity
        implements OnClickListener, OnItemClickListener, OnImageDeleteClickListener,
        OnCheckedChangeListener, OnUploadListener, HttpListener {

    private static final String TAG = WaibaoHomeWorkActivity.class.getSimpleName();

    private StuPublishHomeWorkAdapter mStuPublishHomeWorkAdapter;

    private StuPublishHomeWorkPageModel addHomeWorkPageModel;

    private ArrayList<StuPublishHomeWorkUploadModel> uploadList;

    private TextView nextStepTV;

    private RelativeLayout nextStepLayout;

    private MyGridView mMyGridView;

    private EditText et_des;// 描述

    private TextView tv_publish_title;

    private TextView subjectsTitleTV;

    private MyRadioGroup subjectRadioGroup;

    private View choiceFudaoBtn;

    private TextView fudaoTv;

    private LinearLayout layout_not_vip_permission_view;

    private TextView gold_title_tv;

    private Button chongzhi_btn;

    private ImageButton home_work_gold_minus_ibtn;

    private TextView home_work_gold_tv;

    private ImageButton home_work_gold_plus_ibtn;

    private GradeModel grade;

    private ArrayList<SubjectModel> subList = new ArrayList<SubjectModel>();

    private ArrayList<StuPublishHomeWorkPageModel> hwImagePathList = new ArrayList<StuPublishHomeWorkPageModel>();

    private int gradeid;

    private int currentSubjectId = -1;
    private int subjectTag=-1;

    /** 发布作业失败时，用来记录当前作业 */
    private StuPublishHomeWorkModel tempPm = null;

    private int homeWorkId = -1;

    /** 记录图片列表是否被修改 */
    private boolean fileListModified = false;

    private WelearnDialog mDialog;

    // constant
    private int BASE_PAGE_SIZE;

    private int MAX_IMAGE_SIZE = 4;

    private float SINGLE_PAGE_GOLD;

    private float MIN_GOLD;

    // request code
    private static final String UPLOAD_URL = AppConfig.GO_URL + "org/hwpicupload";

    public static final int REQUEST_CODE_GET_IMAGE_FROM_SYS = 0x1;

    public static final int REQUEST_CODE_GET_IMAGE_FROM_CROP = 0x2;

    public static final String ADD_IMAGE_TAG = "add_image_tag";

    public static final int REQUEST_PUBLISHER_CODE = 0x7;// 发题者

    // handler code
    public static final int HANDLER_MSG_PUBLISH_OK = 0x1;

    public static final int HANDLER_MSG_UPLOAD_PROGRESS = 0x2;

    public static final int HANDLER_MSG_UPLOAD_FINISH = 0x3;

    public static final int HANDLER_MSG_COMMIT_FINISH = 0x4;

    public static final int HANDLER_MSG_COMMIT_FAILED = 0x5;
    
    

    private ArrayList<OrgModel> OrgList;

    private int orgid;// 机构id

    private String orgname;// 机构名称

    private int isSpecialStudenType = 0;// 是否是特殊学生帐号

    private RelativeLayout layout_choice_publisher;// 选择发题者layout

    private TextView tv_publisher;// 显示发题者

    private ImageView iv_publiser_arrow;

    private List<UserInfoModel> publiserList;// 发题者集合

    private int choice_userId = 0;// 选择发题者的id

    private String username = "";

    private StudyAPI studyApi;
    private FudaoquanAPI fudaoquanApi;

    private HomeworkManager homeworkManager;

    private UserInfoModel uInfo;

    private LinearLayout layout_vip_view;

    private String specialstudentslistData;
    
    private int homeworkmiss;
    
    private HomeWorkAPI homeworkApi;
    
    
    private LinearLayout layout_fudaoquan;

    private ImageView iv_choice_radio;

    private LinearLayout view_fudaoquan;
    
    private TextView fudaoquan_tv_type;

    private TextView fudaoquan_tv_data_str;

    private TextView fudaoquan_tv_des;
    
    private LinearLayout layout_xuanshangxuedian;

    private FudaoquanModel fudaoquanmodel;
    
    private static final int REQUEST_EXPIRE_FUDAOQUAN_CODE = 21312;
    private int chooseFudaoquanTag=-1;//是否需要辅导券

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_MSG_PUBLISH_OK:
                    fileListModified = false;
                    if (homeWorkId > 0) {
                        // 上传文件
                        uploadImage(0);
                    } else {
                        ToastUtils.show(R.string.student_publish_home_work_publish_server_error);
                    }
                    break;
                case HANDLER_MSG_UPLOAD_PROGRESS:
                    int index = msg.arg1;
                    if (AppConfig.IS_DEBUG) {
                        ToastUtils.show(getString(
                                R.string.student_publish_home_work_publish_img_index, index + 1));
                    }
                    break;
                case HANDLER_MSG_UPLOAD_FINISH:
                    if (AppConfig.IS_DEBUG) {
                        ToastUtils
                                .show(R.string.student_publish_home_work_publish_img_upload_finish);
                    }
                    if (isSpecialStudenType==1) {//特殊学生
                        if (homeworkmiss==1) {
                            uploadFinish();
                        }else {
                            homeworkApi.waibaoPutongPublishHomeworkUploadFinish(requestQueue, homeWorkId, WaibaoHomeWorkActivity.this, RequestConstant.EXECUTE_PUTONG_PUBLISH_HOME_WORK_UPLOAD_FINISH);
                        }
                        
                    }else {//非特殊学生
                        if (homeworkmiss==1) {
                            uploadFinish();
//                            homeworkApi.waibaoPutongPublishHomeworkUploadFinish(requestQueue, homeWorkId, WaibaoHomeWorkActivity.this, RequestConstant.EXECUTE_PUTONG_PUBLISH_HOME_WORK_UPLOAD_FINISH);
                        }else {
                            homeworkApi.waibaoPutongPublishHomeworkUploadFinish(requestQueue, homeWorkId, WaibaoHomeWorkActivity.this, RequestConstant.EXECUTE_PUTONG_PUBLISH_HOME_WORK_UPLOAD_FINISH);
                        }
                    }
                    
                   
                    break;
                case HANDLER_MSG_COMMIT_FINISH:
                    ToastUtils.show(R.string.student_publish_home_work_publish_success);
                    // setResult(RESULT_OK);
                    HomeWorkHallActivity.reFlesh = true;
                    closeDialog();
                    uMengEvent("homework_publish");

                    tempPm = null;
                    homeWorkId = -1;
                    fileListModified = false;

                    finish();
                    break;
                case HANDLER_MSG_COMMIT_FAILED:
                    closeDialog();
                    ToastUtils.show(R.string.student_publish_home_work_publish_failed);
                    break;
            }
        }
    };





    @Override
    @SuppressLint("InlinedApi")
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.outsouring_home_work_activity);
        initView();
        getExtraData();
        initObject();
        initListener();        
        initData();
        WeLearnFileUtil.clearQuestionFiles();
    }

    /**
     * 此方法描述的是：得到传递过来的数据
     * 
     * @author: sky @最后修改人： sky
     * @最后修改日期:2015年8月4日 上午10:55:49 getExtraData void
     */
    private void getExtraData() {
        double gold = 0;
        Intent intent = getIntent();
        if (intent != null) {
            orgid = intent.getIntExtra("orgid", 0);
            orgname = intent.getStringExtra("orgname");
            isSpecialStudenType = intent.getIntExtra("type", 0);
            Serializable serializableExtra = intent.getSerializableExtra(OrgModel.TAG);
            if (serializableExtra instanceof ArrayList<?>) {
                OrgList = (ArrayList<OrgModel>)intent.getSerializableExtra(OrgModel.TAG);
            }
        }

        uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
        if (null != uInfo) {
            gradeid = uInfo.getGradeid();
            if (gradeid < 1) {
                ToastUtils.show(getString(R.string.student_publish_nograde_text));
            }
            grade = DBHelper.getInstance().getWeLearnDB().queryGradeByGradeId(gradeid);
            gold = uInfo.getGold();
        }
        
        
        String goldStr = GoldToStringUtil.GoldToString(gold);
        String goldTitleStr = getString(R.string.student_publish_home_work_pay_title, goldStr);

        SpannableStringBuilder builder = new SpannableStringBuilder(goldTitleStr);
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
        builder.setSpan(redSpan, goldTitleStr.length() - 1 - goldStr.length(), goldTitleStr.length() - 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        gold_title_tv.setText(builder);
    }

    public void initView() {
        super.initView();

        setWelearnTitle(R.string.student_publish_home_work_title);
        findViewById(R.id.back_layout).setOnClickListener(this);

        nextStepLayout = (RelativeLayout)findViewById(R.id.next_setp_layout);
        nextStepTV = (TextView)findViewById(R.id.next_step_btn);
        nextStepTV.setBackgroundResource(R.drawable.publish_btn_selector);
        nextStepTV.setWidth(DensityUtil.dip2px(this, 60));
        // nextStepTV.setTextScaleX(1.8f);
        nextStepTV.setVisibility(View.VISIBLE);
        nextStepTV.setText(R.string.student_publish_home_work_publish);
        nextStepLayout.setOnClickListener(this);

        mMyGridView = (MyGridView)findViewById(R.id.home_work_gridview);

        et_des = (EditText)findViewById(R.id.pay_answer_descript_et_homework);
        choiceFudaoBtn = findViewById(R.id.choice_fudao_btn_homework);
        fudaoTv = (TextView)findViewById(R.id.fudao_tv_homework);

        tv_publish_title = (TextView)findViewById(R.id.tip_title);
        subjectsTitleTV = (TextView)findViewById(R.id.home_work_subject_choose_title);
        subjectRadioGroup = (MyRadioGroup)findViewById(R.id.home_work_subjects_radiogroup);

        layout_choice_publisher = (RelativeLayout)this.findViewById(R.id.layout_choice_publisher);
        tv_publisher = (TextView)this.findViewById(R.id.tv_publisher);
        iv_publiser_arrow = (ImageView)this.findViewById(R.id.iv_publiser_arrow);

        // not vip view
        layout_not_vip_permission_view = (LinearLayout)findViewById(
                R.id.layout_not_vip_view);
        gold_title_tv = (TextView)findViewById(R.id.gold_title_tv);
        chongzhi_btn = (Button)findViewById(R.id.chongzhi_btn);
        home_work_gold_minus_ibtn = (ImageButton)findViewById(R.id.home_work_gold_minus_ibtn);
        home_work_gold_tv = (TextView)findViewById(R.id.home_work_gold_tv);
        home_work_gold_plus_ibtn = (ImageButton)findViewById(R.id.home_work_gold_plus_ibtn);

        layout_vip_view = (LinearLayout)findViewById(R.id.layout_vip_view);
        
        
     // 辅导券
        layout_fudaoquan = (LinearLayout)findViewById(R.id.layout_fudaoquan);
        iv_choice_radio = (ImageView)findViewById(R.id.iv_choice_radio);
        view_fudaoquan = (LinearLayout)findViewById(R.id.view_fudaoquan);
        fudaoquan_tv_type = (TextView)findViewById(R.id.tv_type);
        fudaoquan_tv_data_str = (TextView)findViewById(R.id.tv_data_str);
        fudaoquan_tv_des = (TextView)findViewById(R.id.tv_des);
        layout_fudaoquan.setOnClickListener(this);
        
        
        layout_not_vip_permission_view.setVisibility(View.GONE);
        layout_vip_view.setVisibility(View.VISIBLE);

        publiserList = new ArrayList<UserInfoModel>();
    }

    // 初始化监听
    public void initListener() {
        layout_choice_publisher.setOnClickListener(this);
        subjectRadioGroup.setOnCheckedChangeListener(this);
        chongzhi_btn.setOnClickListener(this);
        home_work_gold_minus_ibtn.setOnClickListener(this);
        home_work_gold_plus_ibtn.setOnClickListener(this);
        home_work_gold_minus_ibtn.setOnClickListener(this);
        home_work_gold_plus_ibtn.setOnClickListener(this);
       
    }

    private void initObject() {
        homeworkApi=new HomeWorkAPI();
        studyApi = new StudyAPI();
        fudaoquanApi = new FudaoquanAPI();
        homeworkManager = HomeworkManager.getInstance();

        if (OrgList != null && OrgList.size() == 1) {
            OrgModel choiceListModel = OrgList.get(0);
            orgid = choiceListModel.getOrgid();
            orgname = choiceListModel.getOrgname();
        }

        // 调用接口是否是机构的特殊账号,决定了是否显示发题者layout
        if (OrgList != null && OrgList.size() > 0 && isSpecialStudenType == 1) {// 外包特殊帐号
            studyApi.GetSpecialStudentList(requestQueue, OrgList.get(0).getOrgid(), this,
                    RequestConstant.REQUEST_SPECIAL_STUDENT_CODE);
        } else {// 外包非特殊学生帐号
            studyApi.GetSpecialStudentPermissionList(requestQueue, orgid, this,
                    RequestConstant.REQUEST_SPECIAL_STUDENT_PERMISS_LIST_CODE);
        }

        if (orgid == 0) {
            choiceFudaoBtn.setOnClickListener(this);
        } else {
            fudaoTv.setText(orgname);
            findViewById(R.id.fudao_iv_homework).setVisibility(View.GONE);
//            fudaoTv.setBackgroundColor(getResources().getColor(R.color.unclickable));
        }

        if (gradeid == GradeConstant.GRADE_PARENT_PRIMARY_ID) {
            subjectsTitleTV.setVisibility(View.GONE);
            subjectRadioGroup.setVisibility(View.GONE);
            findViewById(R.id.subject_select_tips).setVisibility(View.GONE);
            MySharePerfenceUtil.getInstance().setSubject(String.valueOf(0));
        } else {
            subjectsTitleTV
                    .setText(getString(R.string.text_subject_choice_with_grade, grade.getName()));
            initSubjects();
        }

    }

    // 初始化科目
    private void initSubjects() {
        subList = DBHelper.getInstance().getWeLearnDB().querySubjectByIdList(grade.getSubjectIds());
        subjectRadioGroup.removeAllViews();
        subjectRadioGroup.invalidate();
        UiUtil.initSubjects(this, subList, subjectRadioGroup, false);
    }

    private void initData() {
        // UserInfoModel uInfo =
        // WLDBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();

        addHomeWorkPageModel = getAddModel(ADD_IMAGE_TAG);
        hwImagePathList.add(addHomeWorkPageModel);
        mStuPublishHomeWorkAdapter = new StuPublishHomeWorkAdapter(this, hwImagePathList, this);
        mMyGridView.setAdapter(mStuPublishHomeWorkAdapter);
        mMyGridView.setOnItemClickListener(this);
        initRuleInfo(true);
        tv_publish_title.setText(
                getString(R.string.student_publish_home_work_add_img_title, MAX_IMAGE_SIZE));
      
    }

    private void initRuleInfo(boolean retry) {
        boolean isNoData = false;
        HomeWorkRuleModel hwrm = DBHelper.getInstance().getWeLearnDB()
                .queryHomeworkRuleInfoByGradeId(grade.getId());
        if (null != hwrm) {
            BASE_PAGE_SIZE = hwrm.getDefault_pic_count();
            MIN_GOLD = hwrm.getDefault_pic_money();
            MAX_IMAGE_SIZE = hwrm.getMax_pic_count();
            SINGLE_PAGE_GOLD = hwrm.getSingle_pic_money();
        }

        if (BASE_PAGE_SIZE <= 0 || MIN_GOLD <= 0 || MAX_IMAGE_SIZE <= 0 || SINGLE_PAGE_GOLD <= 0) {
            isNoData = true;
        }

        if (isNoData) {
            if (retry) {
//                WeLearnApi.getRuleInfo(this, this);
            } else {
                ToastUtils.show(R.string.server_error);
                finish();
            }
        }

        tv_publish_title.setText(
                getString(R.string.student_publish_home_work_add_img_title, MAX_IMAGE_SIZE));
        setGlodByImageListSize(MIN_GOLD);
    }

    private StuPublishHomeWorkPageModel getAddModel(String path) {
        StuPublishHomeWorkPageModel newModel = new StuPublishHomeWorkPageModel();
        newModel.setImgpath(path);
        return newModel;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:// 返回
                if (MyApplication.getFudaoquanmodel()!=null) {
                    MyApplication.setFudaoquanmodel(null);
                }   
                finish();
                break;
            case R.id.choice_fudao_btn_homework:// 选择辅导机构
                IntentManager.goToChoiceFudaoActivity(this, OrgList);
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

            case R.id.home_work_gold_minus_ibtn:// 减少学币
                String tmPrice = home_work_gold_tv.getText().toString();
                float priceMinus = Float.parseFloat(tmPrice);
                float minGold = setGlodByImageListSize(priceMinus);
                if (priceMinus <= minGold) {
                    return;
                }
                priceMinus -= 0.5;
                home_work_gold_tv.setText(GoldToStringUtil.GoldToString(priceMinus));
                break;
            case R.id.home_work_gold_plus_ibtn:// 增加学币
                float pricePlus = Float.parseFloat(home_work_gold_tv.getText().toString());
                pricePlus += 0.5;
                home_work_gold_tv.setText(GoldToStringUtil.GoldToString(pricePlus));
                break;
            case R.id.next_setp_layout:// 发布作业
                if (gradeid < 1) {
                    ToastUtils.show(getString(R.string.student_publish_nograde_text));
                    return;
                }

                if (hwImagePathList.size() == 1) {
                    ToastUtils.show(R.string.student_publish_home_work_add_image);
                    return;
                }

//                if (gradeid > 6) {
//                    currentSubjectId = 0;
//                } else {
//                    if (currentSubjectId == -1) {
//                        ToastUtils.show(R.string.student_publish_home_work_choose_subject);
//                        return;
//                    }
//                }
                if (currentSubjectId == -1) {
                    ToastUtils.show(R.string.student_publish_home_work_choose_subject);
                    return;
                }
                
                if (subjectTag == -1) {
                    ToastUtils.show(R.string.student_publish_home_work_choose_subject);
                    return;
                }
                
                if (layout_choice_publisher.isShown()) {
                    if ("".equals(tv_publisher.getText().toString().trim())) {
                        ToastUtils.show("请选择特殊学生发题");
                        return;
                    }                    
                }
                showPublishHomeworkConfirmDialog();
                break;
            case R.id.layout_fudaoquan:// 辅导券                
                if (MyApplication.getFudaoquanmodel()==null) {
                    Intent fudaoquanIntent = new Intent(this, ExpireFudaoquanActivity.class);
                    fudaoquanIntent.putExtra("tag", "choice_tag_homework");
                    startActivityForResult(fudaoquanIntent, REQUEST_EXPIRE_FUDAOQUAN_CODE);
                }else {
                    view_fudaoquan.setVisibility(View.GONE);
                    iv_choice_radio.setBackgroundResource(R.drawable.choice_no_icon);
                    MyApplication.setFudaoquanmodel(null);
                    layout_not_vip_permission_view.setVisibility(View.VISIBLE);                 
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
        StuPublishHomeWorkPageModel model = hwImagePathList.get(pos);
        String path = model.getImgpath();
        if (ADD_IMAGE_TAG.equals(path)) {
            // startActivityForResult(new Intent(this,
            // SelectPicPopupWindow.class), REQUEST_CODE_GET_IMAGE_FROM_SYS);
            IntentManager.goToSelectPicPopupWindow(this);
        } else {
            Bundle data = new Bundle();
            data.putInt(AnswerListItemView.EXTRA_POSITION, pos);
            ArrayList<StuPublishHomeWorkPageModel> showList = new ArrayList<StuPublishHomeWorkPageModel>();
            for (int i = 0; i < hwImagePathList.size(); i++) {
                StuPublishHomeWorkPageModel tm = hwImagePathList.get(i);
                if (ADD_IMAGE_TAG.equals(tm.getImgpath())) {
                    break;
                }
                showList.add(tm);
            }
            data.putSerializable(TecHomeWorkDetail_OnlyReadActivity.HOMEWROKDETAILPAGERLIST,
                    showList);
            IntentManager.goToHomeWorkDetail_OnlyReadActivity(this, data, false);
        }
    }

    @Override
    public void onDeleteClick(final int pos) {
        if (null == mDialog) {
            mDialog = WelearnDialog.getDialog(WaibaoHomeWorkActivity.this);
        }
        mDialog.withMessage(R.string.student_publish_delete_home_work_confirm)
                .setOkButtonClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            mDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        int size = hwImagePathList.size();
                        if (pos >= 0 && pos < size) {
                            hwImagePathList.remove(pos);
                        }

                        size = hwImagePathList.size();
                        if (size < MAX_IMAGE_SIZE) {
                            StuPublishHomeWorkPageModel md = hwImagePathList.get(size - 1);
                            if (!ADD_IMAGE_TAG.equals(md.getImgpath())) {
                                hwImagePathList.add(addHomeWorkPageModel);
                            }
                        }

                        mStuPublishHomeWorkAdapter.setData(hwImagePathList);
                    }
                });
        mDialog.show();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && null != data) {
            if (requestCode == REQUEST_CODE_GET_IMAGE_FROM_SYS) {
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
                startActivityForResult(localIntent, REQUEST_CODE_GET_IMAGE_FROM_CROP);
            } else if (requestCode == REQUEST_CODE_GET_IMAGE_FROM_CROP) {
                String savePath = data.getStringExtra(CropImageActivity.IMAGE_SAVE_PATH_TAG);

                LogUtils.i(TAG, "path=" + savePath);

                hwImagePathList.add(hwImagePathList.size() - 1, getAddModel(savePath));

                fileListModified = true;

                if (hwImagePathList.size() > MAX_IMAGE_SIZE) {
                    hwImagePathList.remove(MAX_IMAGE_SIZE);
                }
                setGlodByImageListSize(Float.parseFloat(home_work_gold_tv.getText().toString()));
                mStuPublishHomeWorkAdapter.setData(hwImagePathList);
            } else if (requestCode == 1002) {// 选择机构
                orgid = data.getIntExtra("orgid", 0);
                orgname = data.getStringExtra("orgname");
                fudaoTv.setText(orgname);
            } else if (requestCode == REQUEST_PUBLISHER_CODE) {// 选择发题者
                choice_userId = data.getIntExtra("userid", 0);
                username = data.getStringExtra("username");
                tv_publisher.setText(username);

                if (isSpecialStudenType == 1) {// 外包特殊学生帐号
                    boolean isRn = homeworkManager.getUseridInHomeworklist(choice_userId,
                            specialstudentslistData);
                    homeworkmiss=1;
                    // 可以免费发作业                   
                    if (isRn) {
                        choiceFudaoBtn.setVisibility(View.GONE);
                        layout_not_vip_permission_view.setVisibility(View.GONE);
                        layout_vip_view.setVisibility(View.VISIBLE);
                    }                       


                } else {// 外包非特殊学生帐号
                        // 选择学生获取userid，然后返回这个学生权限
                    ToastUtils.show("你还没有作业发布的权限，请找管理员");

                }

            }   else if (requestCode == REQUEST_EXPIRE_FUDAOQUAN_CODE) {// 取得辅导券的返回值
                if (resultCode == RESULT_OK) {
                    if (data != null) {
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
                            layout_not_vip_permission_view.setVisibility(View.GONE);
                        }

                    }
                }

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WeLearnFileUtil.deleteQuestionFiles();
        if (null != subjectRadioGroup) {
            subjectRadioGroup.removeAllViews();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (gradeid == GradeConstant.GRADE_PARENT_PRIMARY_ID) {
            currentSubjectId = checkedId;
            subjectTag=checkedId;
        } else {
            currentSubjectId = checkedId;
            subjectTag=checkedId;
        }
    }

    /**
     * 此方法描述的是：发布作业
     * 
     * @author: sky void
     */
    private void executePublishHomeWork() {
        showDialog(getString(R.string.student_publish_home_work_uploading));
        boolean isNew = false;

        StuPublishHomeWorkModel pm = new StuPublishHomeWorkModel();
        String description = et_des.getText().toString();
        if (TextUtils.isEmpty(description)) {
            description = getString(R.string.text_question_description);
        }
        pm.setMemo(description);
        pm.setSubjectid(currentSubjectId);
        pm.setOrgid(orgid);
        // 判断是否是重复提交,判断图片是否有修改
        if (null != tempPm && pm.equals(tempPm) && !fileListModified) {
            isNew = false;
        } else {
            isNew = true;
        }

        tempPm = pm;
        fileListModified = false;

        if (isNew || homeWorkId == -1) {
            homeWorkId = -1;
            getUploadList();
            if (isSpecialStudenType == 1) {// 外包特殊学生
                if (homeworkmiss == 1) {// 是否包作业（发布作业不用钱）                   
                    pm.setStuid(choice_userId);                
                    stepOnePublish(pm);
                } else {// 不包作业(发布作业需要钱)
                    pm.setTasktype(2);
                    pm.setPaytype(1);
                    pm.setBounty(Float.parseFloat(home_work_gold_tv.getText().toString()));
                    if (chooseFudaoquanTag==1) {//需要辅导券
                        pm.setCouponid(fudaoquanmodel.getId());
                        //调用使用辅导券发布作业的接口
                        fudaoquanApi.doFudaoquanWithPublishHomework(requestQueue, 2, 1, description, currentSubjectId, 0, fudaoquanmodel.getId(), this, RequestConstant.PUBLISH_HOMEWORK_WITH_FUDAOQUAN_CODE);
                    }else {//不需要辅导券
                        //普通发布作业接口                    
                        homeworkApi.waibaoPutongPublishHomework(requestQueue, pm, this, RequestConstant.EXECUTE_PUTONG_PUBLISH_HOME_WORK_CODE); 
                    }
                   
                }
                
                
            } else {// 外包不是特殊学生

                if (homeworkmiss == 1) {// 是否包作业（发布作业不用钱）     
//                    pm.setStuid(choice_userId);      
//                    stepOnePublish(pm);
                    
                    pm.setTasktype(2);
                    pm.setPaytype(1);
                    pm.setBounty(Float.parseFloat(home_work_gold_tv.getText().toString()));
                    //普通发布作业接口
                    stepOnePublish(pm);
//                    homeworkApi.waibaoPutongPublishHomework(requestQueue, pm, this, RequestConstant.EXECUTE_PUTONG_PUBLISH_HOME_WORK_CODE);
                } else {// 不包作业(发布作业需要钱)
                    pm.setTasktype(2);
                    pm.setPaytype(1);
                    pm.setBounty(Float.parseFloat(home_work_gold_tv.getText().toString()));
                    if (chooseFudaoquanTag==1) {
                        pm.setCouponid(fudaoquanmodel.getId());
                    }
                    //普通发布作业接口
                    homeworkApi.waibaoPutongPublishHomework(requestQueue, pm, this, RequestConstant.EXECUTE_PUTONG_PUBLISH_HOME_WORK_CODE);
                }

               
            }

        } else {
            mHandler.obtainMessage(HANDLER_MSG_PUBLISH_OK).sendToTarget();
        }
    }

    private void stepOnePublish(StuPublishHomeWorkModel pm) {

        try {            
            OkHttpHelper.post(this, "org", "hwpublish", new JSONObject(new Gson().toJson(pm)),
                    new HttpListener() {

                        @Override
                        public void onFail(int code,String errMsg) {
                            ToastUtils.show(R.string.student_publish_home_work_publish_failed);
                            closeDialog();
                            LogUtils.d(TAG, "onFail code = " + code);
                        }

                        @Override
                        public void onSuccess(int code, String dataJson, String errMsg) {
                            if (code == ResponseCmdConstant.CODE_RETURN_OK) {
                                try {
                                    JSONObject jobj = new JSONObject(dataJson);
                                    homeWorkId = jobj.getInt("taskid");
                                    mHandler.obtainMessage(HANDLER_MSG_PUBLISH_OK).sendToTarget();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    ToastUtils.show(
                                            R.string.student_publish_home_work_publish_failed);
                                    String msg = null == e ? "Error" : e.getMessage();
                                    LogUtils.d(TAG, msg);
                                }
                            } else {
                                closeDialog();
                                if (!TextUtils.isEmpty(errMsg)) {
                                    ToastUtils.show(errMsg);
                                    LogUtils.d(TAG, errMsg);
                                } else {
                                    ToastUtils.show(
                                            R.string.student_publish_home_work_publish_failed);
                                }
                            }

                        }
                    });
        } catch (JSONException e) {
            closeDialog();
            String error = e.getMessage() == null ? "error" : e.getMessage();
            ToastUtils.show(error);
            e.printStackTrace();
        }
    }
    
    
    

    private void getUploadList() {
        if (null == uploadList) {
            uploadList = new ArrayList<StuPublishHomeWorkUploadModel>();
        }
        uploadList.clear();
        StuPublishHomeWorkPageModel hwpm = null;
        StuPublishHomeWorkUploadModel ppm;
        StuPublishHomeWorkUploadFileModel spwufm;
        for (int i = 0; i < hwImagePathList.size(); i++) {
            hwpm = hwImagePathList.get(i);
            if (ADD_IMAGE_TAG.equals(hwpm.getImgpath())) {
                break;
            }
            ppm = new StuPublishHomeWorkUploadModel();
            spwufm = new StuPublishHomeWorkUploadFileModel();
            spwufm.setPicseqid(i + 1);
            spwufm.setMemo("");
            Point p = ImageUtil.getImageSize(hwpm.getImgpath());
            spwufm.setWidth(p.x);
            spwufm.setHeight(p.y);
            ppm.setPicinfo(spwufm);
            uploadList.add(ppm);
        }
    }

    private void uploadImage(int index) {
        if (index < uploadList.size()) {
            StuPublishHomeWorkUploadModel um = uploadList.get(index);
            um.setAction("add_homework");
            um.setActionid(homeWorkId);
            try {
                Map<String, List<File>> files = new HashMap<String, List<File>>();
                StuPublishHomeWorkPageModel hwpm = hwImagePathList.get(index);
                List<File> fList = new ArrayList<File>();
                fList.add(new File(hwpm.getImgpath()));
                files.put("picfile", fList);
                UploadManager.upload(UPLOAD_URL,
                        RequestParamUtils.getParam(new JSONObject(new Gson().toJson(um))), files,
                        WaibaoHomeWorkActivity.this, true, index);
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtils.show("error");
            }
        }
    }

    @Override
    public void onUploadSuccess(UploadResult result, int index) {
        mHandler.obtainMessage(HANDLER_MSG_UPLOAD_PROGRESS, index, 0).sendToTarget();
        int size = uploadList.size();
        if (index < size - 1) {
            uploadImage(index + 1);
        }
        if (index == size - 1) {
            mHandler.obtainMessage(HANDLER_MSG_UPLOAD_FINISH).sendToTarget();
        }
    }

    @Override
    public void onUploadError(String msg, int index) {
        closeDialog();
        ToastUtils.show(R.string.text_commit_failed_please_retry);
    }

    @Override
    public void onUploadFail(UploadResult result, int index) {
        closeDialog();
        ToastUtils.show(R.string.text_commit_failed_please_retry);
    }

    private void uploadFinish() {
        try {
            JSONObject json = new JSONObject();
            json.put("taskid", homeWorkId);
            json.put("userid", choice_userId);
            // new JSONObject("{\"taskid\": " + homeWorkId + "}")
            OkHttpHelper.post(this, "org", "hwuploadfinish", json, new HttpListener() {
                @Override
                public void onFail(int code,String errMsg) {
                    closeDialog();
                    mHandler.obtainMessage(HANDLER_MSG_COMMIT_FAILED).sendToTarget();
                }

                @Override
                public void onSuccess(int code, String dataJson, String errMsg) {
                    if (code == 0) {
                        mHandler.sendMessageDelayed(
                                mHandler.obtainMessage(HANDLER_MSG_COMMIT_FINISH), 1000);
                    } else {
                        mHandler.obtainMessage(HANDLER_MSG_COMMIT_FAILED).sendToTarget();
                    }
                }
            });
        } catch (Exception e) {
            closeDialog();
            e.printStackTrace();
            mHandler.obtainMessage(HANDLER_MSG_COMMIT_FAILED).sendToTarget();
        }
    }

    private void showPublishHomeworkConfirmDialog() {
        if (null == mDialog) {
            mDialog = WelearnDialog.getDialog(WaibaoHomeWorkActivity.this);
        }
        mDialog.withMessage(R.string.student_publish_home_work_confirm)
                .setOkButtonClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                        
                        executePublishHomeWork();

                    }
                });
        mDialog.show();
    }

    @Override
    public void onSuccess(int code, String dataJson, String errMsg) {
        if (code == 0) {
        } else {
            ToastUtils.show(errMsg);
        }
    }

    @Override
    public void onFail(int HttpCode,String errMsg) {
        closeDialog();
    }

    private float setGlodByImageListSize(float currentGold) {
        float result = currentGold;
        int size = hwImagePathList.size();
        float returnMin = MIN_GOLD;

        if (ADD_IMAGE_TAG.equals(hwImagePathList.get(size - 1).getImgpath())) {
            size = size - 1;
        }

        if (size <= BASE_PAGE_SIZE) {
            returnMin = MIN_GOLD;
            if (currentGold < MIN_GOLD) {
                result = MIN_GOLD;
            }
        } else {
            float min = MIN_GOLD + (size - BASE_PAGE_SIZE) * SINGLE_PAGE_GOLD;
            returnMin = min;
            if (currentGold < min) {
                result = min;
            }
        }
        home_work_gold_tv.setText(GoldToStringUtil.GoldToString(result));
        return returnMin;
    }

    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        int flag = ((Integer)param[0]).intValue();
        switch (flag) {
            case RequestConstant.REQUEST_SPECIAL_STUDENT_CODE:// 得到特殊学生列表
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        try {
                            String dataJson = JsonUtil.getString(datas, "Data", "");
                            specialstudentslistData = dataJson;
                            if (!TextUtils.isEmpty(dataJson)) {
                                List<UserInfoModel> list = homeworkManager
                                        .parseSpecialStudent(dataJson);
                                publiserList.addAll(list);
                                if (publiserList != null && publiserList.size() > 0) {
                                    choiceFudaoBtn.setVisibility(View.GONE);
                                    layout_choice_publisher.setVisibility(View.VISIBLE);
                                    layout_not_vip_permission_view.setVisibility(View.GONE);
                                    layout_vip_view.setVisibility(View.VISIBLE);
                                } else {                       
                                    layout_not_vip_permission_view.setVisibility(View.GONE);
                                    layout_vip_view.setVisibility(View.VISIBLE);
                                    choiceFudaoBtn.setVisibility(View.VISIBLE);
                                    layout_choice_publisher.setVisibility(View.GONE);                                   
                                    
                                    AlertDialog.Builder dialog=new AlertDialog.Builder(WaibaoHomeWorkActivity.this);
                                    dialog.setTitle("提示");
                                    dialog.setMessage("没有学生你可以为他发题，暂不能发题");
                                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {                                        
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
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
                                int quespermiss = permissObj.optInt("question");
                                homeworkmiss = permissObj.optInt("homework");
                                if (homeworkmiss == 1) {// 作业免费
                                    changeViewInWaibao(true);
                                    layout_fudaoquan.setVisibility(View.GONE);
                                } else {// 作业不免费
                                    changeViewInWaibao(false);
                                  //判断是否有作业辅导券
                                    fudaoquanApi.getFudaoquanList(requestQueue, 2, this, RequestConstant.GET_HOMEWORK_QUAN_CODE);
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
                break;
            case RequestConstant.EXECUTE_PUTONG_PUBLISH_HOME_WORK_CODE:// 执行外包普通发布作业 第一步
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        String dataStr = JsonUtil.getString(datas, "Data", "");
                        if (!TextUtils.isEmpty(dataStr)) {
                            try {
                                JSONObject dataJson = new JSONObject(dataStr);
                                try {                                    
                                    homeWorkId = dataJson.getInt("taskid");
                                    mHandler.obtainMessage(HANDLER_MSG_PUBLISH_OK).sendToTarget();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    ToastUtils.show(R.string.student_publish_home_work_publish_failed);                                    
                                }
                            
                            } catch (JSONException e) {                               
                                e.printStackTrace();
                            }
                           

                        }
                    }else {
                        closeDialog();
                        if (!TextUtils.isEmpty(msg)) {
                            ToastUtils.show(msg);
                        } else {
                            ToastUtils.show(R.string.student_publish_home_work_publish_failed);
                        }
                    
                    }

                }
                break;
            case RequestConstant.EXECUTE_PUTONG_PUBLISH_HOME_WORK_UPLOAD_FINISH://第三步
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    try {
                        if (code == 0) {
                            mHandler.sendMessageDelayed(mHandler.obtainMessage(HANDLER_MSG_COMMIT_FINISH), 1000);
                        }else {
                            mHandler.obtainMessage(HANDLER_MSG_COMMIT_FAILED).sendToTarget();
                        }
                    } catch (Exception e) {
                        closeDialog();
                        e.printStackTrace();
                        mHandler.obtainMessage(HANDLER_MSG_COMMIT_FAILED).sendToTarget();
                    }

                }
                break;
            case RequestConstant.GET_HOMEWORK_QUAN_CODE:// 获取作业辅导券
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
            case RequestConstant.PUBLISH_HOMEWORK_WITH_FUDAOQUAN_CODE:// 执行使用辅导券发布作业 第一步
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        String dataStr = JsonUtil.getString(datas, "Data", "");
                        if (!TextUtils.isEmpty(dataStr)) {
                            try {
                                JSONObject dataJson = new JSONObject(dataStr);
                                try {                                    
                                    homeWorkId = dataJson.getInt("taskid");
                                    mHandler.obtainMessage(HANDLER_MSG_PUBLISH_OK).sendToTarget();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    ToastUtils.show(R.string.student_publish_home_work_publish_failed);                                    
                                }
                            
                            } catch (JSONException e) {                               
                                e.printStackTrace();
                            }
                           

                        }
                    }else {
                        closeDialog();
                        if (!TextUtils.isEmpty(msg)) {
                            ToastUtils.show(msg);
                        } else {
                            ToastUtils.show(R.string.student_publish_home_work_publish_failed);
                        }
                    
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
            if (isSpecialStudenType==1) {
                choiceFudaoBtn.setVisibility(View.GONE);
            }else {
                choiceFudaoBtn.setVisibility(View.VISIBLE);                
            }
            choiceFudaoBtn.setVisibility(View.VISIBLE); 
            layout_not_vip_permission_view.setVisibility(View.GONE);
            layout_vip_view.setVisibility(View.VISIBLE);
            homeworkmiss=1;
        } else {
            // 发作业不免费
            if (isSpecialStudenType==1) {
                choiceFudaoBtn.setVisibility(View.GONE);
            }else {
                choiceFudaoBtn.setVisibility(View.VISIBLE);                
            }
            choiceFudaoBtn.setVisibility(View.GONE); 
            layout_not_vip_permission_view.setVisibility(View.VISIBLE);
            layout_vip_view.setVisibility(View.GONE);
            homeworkmiss=0;
        }
    }

}
