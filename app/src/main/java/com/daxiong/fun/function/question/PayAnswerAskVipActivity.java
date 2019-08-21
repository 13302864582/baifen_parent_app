
package com.daxiong.fun.function.question;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.R;
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
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.http.RequestParamUtils;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.GradeModel;
import com.daxiong.fun.model.OrgModel;
import com.daxiong.fun.model.SubjectModel;
import com.daxiong.fun.model.UploadResult;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.util.UiUtil;
import com.daxiong.fun.util.WeLearnFileUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 此类的描述：机构的拍照提问/疑难答疑
 * 
 * @author: sky @最后修改人： sky
 * @最后修改日期:2015年8月5日 上午11:03:59
 */
public class PayAnswerAskVipActivity extends BaseActivity
        implements OnClickListener, OnCheckedChangeListener, HttpListener, OnUploadListener {

    private static final String TAG = PayAnswerAskVipActivity.class.getSimpleName();

    private TextView mSubjectChoiceText;

    private ImageView mTakePhotoBtn;

    // private ImageView mPriceBtnMinus;
    // private ImageView mPriceBtnPlus;
    // private TextView mPriceText;
    private ImageView mDelIc;

    private RadioGroup subjectRG;

    private InputMethodManager imm;

    // private int gradeid;
    private TextView nextStepTV/* , goldTitleTV */;

    private RelativeLayout nextStepLayout;

    // private GradeModel grade;
    private ArrayList<SubjectModel> subList = new ArrayList<SubjectModel>();

    private WelearnDialog mWelearnDialogBuilder;

    private EditText mDescriptEt;

    private int subjectid=-1;

    private int orgid;

    private String description;

    private String mPath;

    private int gradeid;

    private View choiceFudaoBtn;

    private TextView fudaoTv;

    private ArrayList<OrgModel> orgList;

    private String orgname;

    private StudyAPI orgApi;

    private RelativeLayout layout_choice_publisher;// 选择发题者layout

    private TextView tv_publisher;// 显示发题者

    private ImageView iv_publiser_arrow;

    private List<UserInfoModel> publiserList;// 发题者集合
    
    private static final int REQUEST_PUBLISHER_CODE = 0x7;//发题者
    
    private int choice_userId=0;//选择发题者的id
    private String choice_username="";

    private boolean isSpecialStudent=false;

    private UserInfoModel mUserInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_answer_ask_vip);
        initView();
        initListener();
    }

    @Override
    public void initView() {
        super.initView();
        orgApi = new StudyAPI();
        setWelearnTitle(R.string.text_ask_title);
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        mDescriptEt = (EditText)findViewById(R.id.pay_answer_descript_et_payask);
        mSubjectChoiceText = (TextView)findViewById(R.id.pay_answer_choice_text);
        subjectRG = (RadioGroup)findViewById(R.id.pay_answer_subjects_radiogroup);
        mTakePhotoBtn = (ImageView)findViewById(R.id.pay_answer_take_photo_btn);
        mDelIc = (ImageView)findViewById(R.id.pay_answer_question_img_del);

        nextStepLayout = (RelativeLayout)findViewById(R.id.next_setp_layout);
        nextStepTV = (TextView)findViewById(R.id.next_step_btn);
        nextStepTV.setBackgroundResource(R.drawable.publish_btn_selector);
        nextStepTV.setVisibility(View.VISIBLE);
        nextStepTV.setText(R.string.text_question_submit);
        choiceFudaoBtn = findViewById(R.id.choice_fudao_btn_ask);
        fudaoTv = (TextView)findViewById(R.id.fudao_tv_ask);

        layout_choice_publisher = (RelativeLayout)this.findViewById(R.id.layout_choice_publisher);
        tv_publisher = (TextView)this.findViewById(R.id.tv_publisher);
        iv_publiser_arrow = (ImageView)this.findViewById(R.id.iv_publiser_arrow);

        publiserList = new ArrayList<UserInfoModel>();

        ExtraData();
        if (orgList != null && orgList.size()== 1) {
            // 调用接口是否是机构的特殊账号,决定了是否显示发题者layout
            orgApi.GetSpecialStudentList(requestQueue, orgList.get(0).getOrgid(), this,
                    RequestConstant.REQUEST_SPECIAL_STUDENT_CODE);
        }

        mUserInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
        GradeModel grade = null;
        if (null != mUserInfo) {
            gradeid = mUserInfo.getGradeid();
            grade = DBHelper.getInstance().getWeLearnDB().queryGradeByGradeId(gradeid);
        }

        if (grade != null) {
            if (gradeid == GradeConstant.GRADE_PARENT_PRIMARY_ID) {
                mSubjectChoiceText.setVisibility(View.GONE);
                subjectRG.setVisibility(View.GONE);
                findViewById(R.id.subject_select_tips).setVisibility(View.GONE);
                // WeLearnSpUtil.getInstance().setSubject(String.valueOf(0));
            } else {
                subList = DBHelper.getInstance().getWeLearnDB()
                        .querySubjectByIdList(grade.getSubjectIds());
                UiUtil.initSubjects(this, subList, subjectRG, false);
                mSubjectChoiceText.setText(
                        getString(R.string.text_subject_choice_with_grade, grade.getName()));
            }
        }
    }

    /**
     * 此方法描述的是：传递过来的数据
     * 
     * @author: sky
     * @最后修改日期:2015年8月5日 上午11:09:31 ExtraData void
     */
    private void ExtraData() {
        Intent extraIntent = getIntent();
        if (extraIntent != null) {
            orgid = extraIntent.getIntExtra("orgid", 0);
            orgname = extraIntent.getStringExtra("orgname");
            Serializable serializableExtra = extraIntent.getSerializableExtra(OrgModel.TAG);
            if (serializableExtra instanceof ArrayList<?>) {
                orgList = (ArrayList<OrgModel>)extraIntent.getSerializableExtra(OrgModel.TAG);
            }
        }

        if (orgList != null && orgList.size() == 1) {
            OrgModel choiceListModel = orgList.get(0);
            orgid = choiceListModel.getOrgid();
            orgname = choiceListModel.getOrgname();
        }

        if (orgid == 0) {
            choiceFudaoBtn.setOnClickListener(this);
        } else {
            fudaoTv.setText(orgname);
            findViewById(R.id.fudao_iv_ask).setVisibility(View.GONE);
            fudaoTv.setBackgroundColor(getResources().getColor(R.color.unclickable));
        }

    }

    @Override
    public void initListener() {
        super.initListener();
        findViewById(R.id.back_layout).setOnClickListener(this);
        nextStepLayout.setOnClickListener(this);
        subjectRG.setOnCheckedChangeListener(this);
        mTakePhotoBtn.setOnClickListener(this);
        mDelIc.setOnClickListener(this);
        layout_choice_publisher.setOnClickListener(this);
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

    }

    private void setSubject(int sid) {
        for (int i = 0; i < subjectRG.getChildCount(); i++) {
            View v = subjectRG.getChildAt(i);
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
            case R.id.back_layout://返回
                finish();
                break;
            case R.id.choice_fudao_btn_ask://选择辅导机构
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

            case R.id.next_setp_layout://发布
                if (gradeid > 6) {
                } else {
                    if (subjectid ==-1) {
                        ToastUtils.show(getString(R.string.text_toast_select_subject));
                        return;
                    }
                }
                if (subjectid ==-1) {
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
                description = mDescriptEt.getText().toString().trim();
                if (TextUtils.isEmpty(description)) {
                    description = getString(R.string.text_question_description);
                }
                // WeLearnSpUtil.getInstance().setDescription(descript);
                submitQuestion();
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
                                    mDelIc.setVisibility(View.GONE);
                                    mTakePhotoBtn.setImageResource(R.drawable.carema_icon);
                                }
                            }
                        });
                mWelearnDialogBuilder.show();

                break;
            case R.id.layout_choice_publisher://选择发题者
                if (orgid != 0 && !TextUtils.isEmpty(orgname)) {
                    Intent intent = new Intent(this, ChoicePublisherActivity.class);
                    if (publiserList!=null&&publiserList.size()>0) {
                        intent.putExtra("publiserList", (Serializable)publiserList);   
                    }                   
                    startActivityForResult(intent, REQUEST_PUBLISHER_CODE);
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
        showDialog("正在发布，请稍候");
        if (isSpecialStudent) {//是特殊学生
            try {
                data.put("userid", choice_userId);
                UploadManager.upload(AppConfig.GO_URL + "org/qpublish", RequestParamUtils.getParam(data), files,
                        PayAnswerAskVipActivity.this, true, 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
           
        }else {
            UploadManager.upload(AppConfig.GO_URL + "org/qpublish", RequestParamUtils.getParam(data), files,
                    PayAnswerAskVipActivity.this, true, 0);    
        }
        

    }

    @Override
    public void onUploadSuccess(UploadResult result, int index) {
        closeDialog();
        if (result.getCode() == 0) {

            if (GlobalVariable.myQPadActivity != null) {
                GlobalVariable.myQPadActivity.finish();
            }
            if (GlobalVariable.QAHallActivity != null) {
                GlobalVariable.QAHallActivity.finish();
            }
            ToastUtils.show("发布成功!");
            IntentManager.goToMyQpadActivity(this, true);
        } else {
            ToastUtils.show(result.getMsg());
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
                    mDelIc.setVisibility(View.VISIBLE);

                    mTakePhotoBtn.setImageBitmap(bm);
                    break;
                case 1002:
                    orgid = data.getIntExtra("orgid", 0);
                    orgname = data.getStringExtra("orgname");
                    fudaoTv.setText(orgname);

                    break;
                case REQUEST_PUBLISHER_CODE://选择发题者
                    choice_userId = data.getIntExtra("userid", 0);
                    choice_username = data.getStringExtra("username");
                    tv_publisher.setText(choice_username); 
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
        }

    }

    @Override
    public void onSuccess(int code, String dataJson, String errMsg) {
        if (code == 0) {
            // initRuleInfo(false);
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
            case RequestConstant.REQUEST_SPECIAL_STUDENT_CODE:// 查询特殊学生账号
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        try {
                            String dataStrs = JsonUtil.getString(datas, "Data", "");
                            JSONObject dataJson = new JSONObject(dataStrs);
                            // 最终是这样的，count代表它管理学生个个数，如果是０，则代表他不能替代别人发题，如果是大于０，代表他管理学生的个数，students是学生列表
                            int count = dataJson.optInt("count");
                            if (count > 1) {
                                isSpecialStudent = true;
                                choiceFudaoBtn.setVisibility(View.GONE);
                                layout_choice_publisher.setVisibility(View.VISIBLE);
                                JSONArray array = dataJson.optJSONArray("students");
                                UserInfoModel us = null;
                                for (int i = 0; i < array.length(); i++) {
                                    us = new UserInfoModel();
                                    JSONObject eachObj = array.getJSONObject(i);
                                    us.setUserid(eachObj.optInt("userid"));
                                    us.setGrade(eachObj.optString("grade"));
                                    us.setName(eachObj.optString("name"));
                                    publiserList.add(us);
                                }

                                OrgModel choiceListModel = orgList.get(0);
                                orgid = choiceListModel.getOrgid();
                                orgname = choiceListModel.getOrgname();
                            } else {
                                isSpecialStudent=false;
                                if (orgList != null && orgList.size() >= 1) {
                                    choiceFudaoBtn.setVisibility(View.VISIBLE);
                                } else {
                                    choiceFudaoBtn.setVisibility(View.GONE);
                                }
                                layout_choice_publisher.setVisibility(View.GONE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
                break;

        }

    }

}
