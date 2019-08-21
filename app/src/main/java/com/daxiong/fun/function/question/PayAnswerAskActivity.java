
package com.daxiong.fun.function.question;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
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
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.manager.UploadManager;
import com.daxiong.fun.manager.UploadManager.OnUploadListener;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.constant.GradeConstant;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.dialog.WelearnDialog;
import com.daxiong.fun.function.myfudaoquan.ExpireFudaoquanActivity;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.http.RequestParamUtils;
import com.daxiong.fun.manager.IntentManager;
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
import com.daxiong.fun.view.CameraPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 此类的描述：拍照提问
 * 
 * @author: sky @最后修改人： sky
 * @最后修改日期:2015年8月5日 上午9:54:52
 */
public class PayAnswerAskActivity extends BaseActivity
        implements OnClickListener, OnCheckedChangeListener, HttpListener, OnUploadListener {

    private static final String TAG = PayAnswerAskActivity.class.getSimpleName();

    private TextView mSubjectChoiceText;

    private ImageView mTakePhotoBtn;  

    private ImageView mPriceBtnMinus;

    private ImageView mPriceBtnPlus;

    private TextView mPriceText;

    private ImageView mDelIc;

    private String mPath;

    private RadioGroup subjectRG;

    private TextView nextStepTV, goldTitleTV;

    private RelativeLayout nextStepLayout;

    private GradeModel grade;

    private ArrayList<SubjectModel> subList = new ArrayList<SubjectModel>();

    private WelearnDialog mWelearnDialogBuilder;

    private EditText mDescriptEt;

    private float minGold;

    private int chooseSubjectId = -1;

    private LinearLayout layout_fudaoquan;

    private ImageView iv_choice_radio;

    private LinearLayout view_fudaoquan;
    private LinearLayout layout_quan_container;

    private InputMethodManager imm;

    private TextView fudaoquan_tv_type;

    private TextView fudaoquan_tv_data_str;

    private TextView fudaoquan_tv_des;
    
    private LinearLayout layout_xuanshangxuedian;

    private FudaoquanModel fudaoquanmodel;
    
    private int chooseFudaoquanTag=-1;

    private static final int REQUEST_EXPIRE_FUDAOQUAN_CODE = 21312;
    
    private FudaoquanAPI fudaoquanApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestMyOrgs();
        setContentView(R.layout.fragment_pay_answer_ask);        
        initView();
        initListener();
        initObject();
    }

    @Override
    public void initView() {
        super.initView();
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        setWelearnTitle(R.string.text_ask_title);

        layout_xuanshangxuedian=(LinearLayout)this.findViewById(R.id.layout_xuanshangxuedian);
        mDescriptEt = (EditText)findViewById(R.id.descript_et_payask);
        mSubjectChoiceText = (TextView)findViewById(R.id.subject_choice_text);
        subjectRG = (RadioGroup)findViewById(R.id.ask_subjects_radiogroup);
        mTakePhotoBtn = (ImageView)findViewById(R.id.take_photo_btn);
        mPriceBtnMinus = (ImageView)findViewById(R.id.price_minus_btn);
        mPriceBtnPlus = (ImageView)findViewById(R.id.price_plus_btn);
        mPriceText = (TextView)findViewById(R.id.question_price);
        mDelIc = (ImageView)findViewById(R.id.question_img_del);
        nextStepLayout = (RelativeLayout)findViewById(R.id.next_setp_layout);
        nextStepTV = (TextView)findViewById(R.id.next_step_btn);
        nextStepTV.setBackgroundResource(R.drawable.publish_btn_selector);

        nextStepTV.setVisibility(View.VISIBLE);
        nextStepTV.setText(R.string.text_question_submit);
        nextStepLayout.setOnClickListener(this);

        goldTitleTV = (TextView)findViewById(R.id.gold_title_tv);
        findViewById(R.id.chongzhi_btn).setOnClickListener(this);
        // 辅导券
        layout_fudaoquan = (LinearLayout)findViewById(R.id.layout_fudaoquan);
        iv_choice_radio = (ImageView)findViewById(R.id.iv_choice_radio);
        view_fudaoquan = (LinearLayout)findViewById(R.id.view_fudaoquan);
        layout_quan_container = (LinearLayout)findViewById(R.id.layout_quan_container);
        layout_quan_container.setBackgroundResource(R.drawable.problem_coupon_bg);
        fudaoquan_tv_type = (TextView)findViewById(R.id.tv_type);
        fudaoquan_tv_data_str = (TextView)findViewById(R.id.tv_data_str);
        fudaoquan_tv_des = (TextView)findViewById(R.id.tv_des);

        UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
        if (null != uInfo) {
            grade = DBHelper.getInstance().getWeLearnDB().queryGradeByGradeId(uInfo.getGradeid());
        }
        initRuleInfo(true);
        if (grade.getGradeId() == GradeConstant.GRADE_PARENT_PRIMARY_ID) {

            // mSubjectChoiceText.setVisibility(View.GONE);
            // subjectRG.setVisibility(View.GONE);
            // findViewById(R.id.subject_select_tips).setVisibility(View.GONE);
            // WeLearnSpUtil.getInstance().setSubject(String.valueOf(0));

            initSubjects();
            mSubjectChoiceText
                    .setText(getString(R.string.text_subject_choice_with_grade, grade.getName()));
        } else {
            initSubjects();
            mSubjectChoiceText
                    .setText(getString(R.string.text_subject_choice_with_grade, grade.getName()));
        }
        
  
        
        
    }

    @Override
    public void initListener() {
        super.initListener();
        findViewById(R.id.back_layout).setOnClickListener(this);
        subjectRG.setOnCheckedChangeListener(this);
        mTakePhotoBtn.setOnClickListener(this);
        mPriceBtnMinus.setOnClickListener(this);
        mPriceBtnPlus.setOnClickListener(this);
        mDelIc.setOnClickListener(this);
        layout_fudaoquan.setOnClickListener(this);
    }
    
    
    public void initObject(){
        fudaoquanApi=new FudaoquanAPI();      
        //判断是否有作业辅导券
        fudaoquanApi.getFudaoquanList(requestQueue, 1, this, RequestConstant.GET_QUESTION_QUAN_CODE);
    }

    // private void requestMyOrgs() {
    // //请求我的机构
    // new OrganizationAPI().queryMyOrgs(requestQueue, 1, 1, 1000, this,
    // RequestConstant.REQUEST_MY_ORGS);
    // }

    private void initRuleInfo(boolean retry) {
        QuestionRuleModel qrm = DBHelper.getInstance().getWeLearnDB()
                .queryQuestionRuleInfoByGradeId(grade.getId());
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

    private void initSubjects() {
        subList = DBHelper.getInstance().getWeLearnDB().querySubjectByIdList(grade.getSubjectIds());
        UiUtil.initSubjects(this, subList, subjectRG, false);
    }

    private void setGold(float gold) {
        mPriceText.setText(GoldToStringUtil.GoldToString(gold));
        MySharePerfenceUtil.getInstance().setGold(gold);
    }

    private void getValGold() {
        float goldVal = MySharePerfenceUtil.getInstance().getGold();
        if (goldVal < minGold) {
            goldVal = minGold;
        }
        setGold(goldVal);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onEventBegin(this, "PayAnswerAsk");
        // 从配置文件中读取用户所选的科目
        String subject = MySharePerfenceUtil.getInstance().getSubject();
        if (grade.getGradeId() == GradeConstant.GRADE_PARENT_PRIMARY_ID) {
//            WeLearnSpUtil.getInstance().setSubject("0");
            if (!TextUtils.isEmpty(subject)) {
                setSubject(Integer.parseInt(subject));
            }
        } else {
            if (!TextUtils.isEmpty(subject)) {
                setSubject(Integer.parseInt(subject));
            } else {
                // if (null != subList && subList.size() >= 1) {
                // try {
                // View v = subjectRG.getChildAt(0);
                // if (v instanceof RadioButton) {
                // RadioButton r = (RadioButton) v;
                // r.setChecked(true);
                // }
                // } catch (Exception e) {
                // e.printStackTrace();
                // }
                // }
            }
        }

        mDescriptEt.setText(MySharePerfenceUtil.getInstance().getDescription());

        getValGold();
        Intent i = getIntent();
        if (i != null) {
            mPath = i.getStringExtra(PayAnswerQuestionPhotoViewActivity.QUESTION_IMG_PATH);
            if (!TextUtils.isEmpty(mPath)) {
                Bitmap bm = BitmapFactory.decodeFile(mPath);
                mDelIc.setVisibility(View.VISIBLE);

                mTakePhotoBtn.setImageBitmap(bm);
            }
        }

        double gold = 0;
        UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
        if (null != uInfo) {
            gold = uInfo.getGold();
        }

        String goldStr = GoldToStringUtil.GoldToString(gold);
        String goldTitleStr = getString(R.string.student_publish_home_work_pay_title, goldStr);

        SpannableStringBuilder builder = new SpannableStringBuilder(goldTitleStr);
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
        builder.setSpan(redSpan, goldTitleStr.length() - 1 - goldStr.length(),
                goldTitleStr.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        goldTitleTV.setText(builder);
        
        Log.e("xxddd-->", chooseFudaoquanTag+"");
        fudaoquanmodel=MyApplication.getFudaoquanmodel();
        if (fudaoquanmodel != null) {
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

        }
        
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
            case R.id.back_layout:
                MySharePerfenceUtil.getInstance().setGold(0);
                MySharePerfenceUtil.getInstance().setSubject("");
                MySharePerfenceUtil.getInstance().setDescription("");
                if (MyApplication.getFudaoquanmodel()!=null) {
                    MyApplication.setFudaoquanmodel(null);
                }             
                finish();
                break;
            case R.id.chongzhi_btn:
                IntentManager.goPayActivity(this);
                break;
            case R.id.take_photo_btn:
                if (WeLearnFileUtil.isFileExist(mPath)) {
                    Bundle data = new Bundle();
                    data.putString(PayAnswerQuestionDetailActivity.IMG_PATH, mPath);
                    data.putBoolean("doNotRoate", true);
                    IntentManager.goToQuestionDetailPicView(this, data);
                } else {
                    MySharePerfenceUtil.getInstance()
                            .setGold(Float.parseFloat(mPriceText.getText().toString()));
                    MySharePerfenceUtil.getInstance().setDescription(mDescriptEt.getText().toString());
                    new CameraPopupWindow(this, view, GlobalContant.PAY_ANSWER_ASK);
                }
                break;
            case R.id.price_minus_btn:
                float priceMinus = Float.parseFloat(mPriceText.getText().toString());
                if (priceMinus <= minGold) {
                    return;
                }

                priceMinus -= 0.1;

                setGold(priceMinus);
                // WeLearnSpUtil.getInstance().setRecordGold(priceMinus);
                break;
            case R.id.price_plus_btn:
                float pricePlus = Float.parseFloat(mPriceText.getText().toString());
                pricePlus += 0.1;
                setGold(pricePlus);
                break;
            case R.id.next_setp_layout:               
                try {
                    if (grade.getGradeId() == GradeConstant.GRADE_PARENT_PRIMARY_ID) {
                        if (chooseSubjectId == -1) {
                            ToastUtils.show(getString(R.string.text_choice_grade));
                            return;
                        }

                        // String subject =
                        // WeLearnSpUtil.getInstance().getSubject();
                        // if (TextUtils.isEmpty(subject)) {
                        // ToastUtils.show(getString(R.string.text_toast_select_subject));
                        // return;
                        // }
                    } else {
                        String subject = MySharePerfenceUtil.getInstance().getSubject();
                        if (TextUtils.isEmpty(subject)) {
                            ToastUtils.show(getString(R.string.text_toast_select_subject));
                            return;
                        }
                    }

                    if (!WeLearnFileUtil.isFileExist(mPath)) {
                        ToastUtils.show(getString(R.string.text_toast_upload_question_pic));
                        return;
                    }
                    String descript = mDescriptEt.getText().toString().trim();
                    if (TextUtils.isEmpty(descript)) {
                        descript = getString(R.string.text_question_description);
                    }
                    MySharePerfenceUtil.getInstance().setDescription(descript);
                    submitQuestion(mPath);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                break;
            case R.id.question_img_del:
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
                                    mTakePhotoBtn
                                            .setImageResource(R.drawable.bg_add_photo_selector);
                                }
                            }
                        });
                mWelearnDialogBuilder.show();

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
                    layout_xuanshangxuedian.setVisibility(View.VISIBLE);
                    
                }
                break;
        }
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
        if (chooseFudaoquanTag==-1) {
            UploadManager.upload(AppConfig.GO_URL + "question/publish", RequestParamUtils.getParam(data),
                    files, PayAnswerAskActivity.this, true, 0);
        }else {
            data.put("couponid", fudaoquanmodel.getId());
            UploadManager.upload(AppConfig.GO_URL + "question/publish", RequestParamUtils.getParam(data),
                    files, PayAnswerAskActivity.this, true, 0);
        }
       

    }

    @Override
    public void onUploadSuccess(UploadResult result, int index) {
        closeDialog();
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
            if (MyApplication.getFudaoquanmodel()!=null) {
                MyApplication.setFudaoquanmodel(null);
            }
            MySharePerfenceUtil.getInstance().setSubject("");
            MySharePerfenceUtil.getInstance().setGold(0);
            MySharePerfenceUtil.getInstance().setDescription("");
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
    public void onBackPressed() {
        MySharePerfenceUtil.getInstance().setSubject("");
        MySharePerfenceUtil.getInstance().setGold(0);
        MySharePerfenceUtil.getInstance().setDescription("");
        if (MyApplication.getFudaoquanmodel()!=null) {
            MyApplication.setFudaoquanmodel(null);            
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GlobalContant.CAPTURE_IMAGE_REQUEST_CODE_STUDENT) {
            if (resultCode == RESULT_OK) {
                String path = WeLearnFileUtil.getQuestionFileFolder().getAbsolutePath()
                        + File.separator + "publish.png";
                LogUtils.i(TAG, path);

                Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(new File(path)));
                sendBroadcast(localIntent);

                Bundle bundle = new Bundle();
                bundle.putString(PayAnswerImageGridActivity.IMAGE_PATH, path);
                IntentManager.goToQuestionPhotoView(this, bundle);
            }
        } else if (requestCode == REQUEST_EXPIRE_FUDAOQUAN_CODE) {// 取得辅导券的返回值
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
                        layout_xuanshangxuedian.setVisibility(View.GONE);
                    }

                }
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
        if (grade.getGradeId() == GradeConstant.GRADE_PARENT_PRIMARY_ID) {
            // WeLearnSpUtil.getInstance().setSubject("0");
            chooseSubjectId = 1;
            MySharePerfenceUtil.getInstance().setSubject(String.valueOf(checkedId));
        } else {
            chooseSubjectId = 1;
            MySharePerfenceUtil.getInstance().setSubject(String.valueOf(checkedId));
        }

    }

    @Override
    public void onSuccess(int code, String dataJson, String errMsg) {
        if (code == 0) {
//            initRuleInfo(false);
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
            case RequestConstant.REQUEST_MY_ORGS:// 查询我的机构
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        try {
                            String dataJson = JsonUtil.getString(datas, "Data", "");
                            ArrayList<OrgModel> orgList = null;
                            if (!TextUtils.isEmpty(dataJson)) {
                                try {
                                    orgList = new Gson().fromJson(dataJson,
                                            new TypeToken<ArrayList<OrgModel>>() {
                                            }.getType());
                                    if (orgList != null && orgList.size() > 0) {
                                        MySharePerfenceUtil.getInstance().setOrgVip();
                                        IntentManager.goToPayAnswerAskVipActivity(
                                                PayAnswerAskActivity.this, "", 0, orgList);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    ToastUtils.show("机构查询失败");
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
    
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    
   
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
    }

}
