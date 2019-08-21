package com.daxiong.fun.function.homework;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.baidu.location.BDLocation;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MainActivity;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.api.FudaoquanAPI;
import com.daxiong.fun.api.HomeWorkAPI;
import com.daxiong.fun.api.MainAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.dialog.CustomFudaoquanPublishTipDialog;
import com.daxiong.fun.dialog.CustomNoFudaoquanTipDialog;
import com.daxiong.fun.dialog.CustomPublicTipDialog;
import com.daxiong.fun.dialog.CustomPublishGiveupDialog;
import com.daxiong.fun.dialog.CustomTipDialog;
import com.daxiong.fun.dialog.WelearnDialog;
import com.daxiong.fun.function.AnswerListItemView;
import com.daxiong.fun.function.account.vip.VipIndexActivity;
import com.daxiong.fun.function.homework.adapter.PublishHwSubjectAdapter;
import com.daxiong.fun.function.homework.adapter.PublishImageGridviewAdapter;
import com.daxiong.fun.function.homework.adapter.PublishImageGridviewAdapter.OnImageDeleteClickListener;
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
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.manager.UploadManager;
import com.daxiong.fun.manager.UploadManager.OnUploadListener;
import com.daxiong.fun.manager.UserManager;
import com.daxiong.fun.model.FudaoquanModel;
import com.daxiong.fun.model.GradeModel;
import com.daxiong.fun.model.SubjectModel;
import com.daxiong.fun.model.UploadResult;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.AppUtils;
import com.daxiong.fun.util.ImageUtil;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LocationUtils;
import com.daxiong.fun.util.LocationUtils.LocationChangedListener;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MD5Util;
import com.daxiong.fun.util.NetworkUtils;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.util.WeLearnFileUtil;
import com.daxiong.fun.view.MyGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 家长发布作业
 *
 * @author: sky
 */
public class PublishHwActivity extends BaseActivity implements OnItemClickListener, OnImageDeleteClickListener,
        OnCheckedChangeListener, OnUploadListener, LocationChangedListener {

    private static final String TAG = PublishHwActivity.class.getSimpleName();
    @Bind(R.id.back_layout)
    RelativeLayout back_layout;
    @Bind(R.id.back_tv)
    TextView tv_back;
    @Bind(R.id.back_iv)
    ImageView iv_back;
    @Bind(R.id.image_gridview)
    MyGridView image_gridview;
    @Bind(R.id.subject_gridview)
    MyGridView subject_gridview;
    @Bind(R.id.et_desp)
    EditText et_desp;

    //辅导券
    @Bind(R.id.layout_fudaoquan)
    LinearLayout layout_fudaoquan;
    @Bind(R.id.view_fudaoquan)
    LinearLayout view_fudaoquan;
    @Bind(R.id.ll_fudaoquan_tip)
    LinearLayout ll_fudaoquan_tip;
    @Bind(R.id.btn_fudaoquan)
    Button btn_fudaoquan;
    @Bind(R.id.tv_fudaotuan_title)
    TextView tv_fudaotuan_title;
    @Bind(R.id.tv_fudoaquan_shijian)
    TextView tv_fudoaquan_shijian;
    @Bind(R.id.tv_fudaoquan_tip)
    TextView tv_fudaoquan_tip;

    @Bind(R.id.tv_chongzhi_tip)
    TextView tv_chongzhi_tip;

    //发布按钮
    @Bind(R.id.btn_publish_homework)
    Button btn_publish_homework;


    // constant
    public static final int REQUEST_CODE_GET_IMAGE_FROM_CROP = 0x2;

    public static final int HANDLER_GET_TASKID_CODE = 0x1;// 获取taskid

    public static final int HANDLER_UPLOAD_IMAGE_PROGRESS = 0x2;

    public static final int HANDLER_UPLOAD_IMAGE_FINISH = 0x3;

    public static final int HANDLER_HW_PUBLISH_SUCCESS = 0x4;

    public static final int HANDLER_HW_PUBLISH_FAIL = 0x5;

    private static final int REQUEST_PUBLISHER_CODE = 0x7;// 发题者

    private static final int REQUEST_EXPIRE_FUDAOQUAN_CODE = 21312;

    private int MAX_IMAGE_SIZE = 4;

    public static final String ADD_IMAGE_TAG = "add_image_tag";

    private static final String UPLOAD_URL = AppConfig.GO_URL + "parents/homeworkpicupload";


    private CustomPublicTipDialog tipDialog;
    private CustomTipDialog tipDialog2;


    private UserInfoModel mUserInfo;
    private GradeModel grade;
    private int gradeid;
    private int currentSubjectId = -1;
    private int taskId = -1;// 作业的taskid
    private int chooseFudaoquanTag = -1;
    private int fudaoquanCount = 0;


    private List<SubjectModel> subList;
    private List<FudaoquanModel> fudaoquanList;

    private static final View layout_choice_publisher = null;

    private StuPublishHomeWorkPageModel addHomeWorkPageModel;

    private ArrayList<StuPublishHomeWorkPageModel> imagePathList;

    private PublishImageGridviewAdapter ImageGridviewAdapter;

    private WelearnDialog mDialog;


    //发布作业失败时，用来记录当前作业
    private StuPublishHomeWorkModel tempPm = null;


    //记录图片列表是否被修改
    private boolean fileListModified = false;


    private ArrayList<StuPublishHomeWorkUploadModel> uploadList;
    private MainAPI mainApi;
    private HomeWorkAPI homeworkApi;
    private FudaoquanAPI fudaoquanApi;
    private FudaoquanModel fudaoquanmodel;


    //定位
    private LocationUtils mLocationUtils;
    private String mLocation;
    private double mLatitude, mLongitude;

    private PublishHwSubjectAdapter subjectAdapter;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_GET_TASKID_CODE:// 获取taskid
                    fileListModified = false;
                    if (taskId > 0) {
                        // 上传文件
                        uploadImage(0);
                    } else {
                        ToastUtils.show(R.string.student_publish_home_work_publish_server_error_taskid);
                    }
                    break;
                case HANDLER_UPLOAD_IMAGE_PROGRESS:// 图片一张一张上传
                    int index = msg.arg1;
                    // if (AppConfig.IS_DEBUG) {
                    // ToastUtils.show(getString(
                    // R.string.student_publish_home_work_publish_img_index, index +
                    // 1));
                    // }
                    break;
                case HANDLER_UPLOAD_IMAGE_FINISH:// 图片上传完成
                    // if (AppConfig.IS_DEBUG) {
                    // ToastUtils
                    // .show(R.string.student_publish_home_work_publish_img_upload_finish);
                    // }

                    uploadFinish();
                    break;
                case HANDLER_HW_PUBLISH_SUCCESS:// 作业发布成功
                    // ToastUtils.show(R.string.student_publish_home_work_publish_success);
                    // setResult(RESULT_OK);
                    uMengEvent("homework_publish");
                    HomeWorkHallActivity.reFlesh = true;

                    tempPm = null;
                    taskId = -1;
                    fileListModified = false;
                    UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
                    uInfo.setSixteacher(0);
                    uInfo.setTabbarswitch(1);
                    DBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);
                    Intent intent2 = new Intent(PublishHwActivity.this, MainActivity.class);
                    Bundle data2 = new Bundle();
                    data2.putString("layout", "layout_home");
                    intent2.putExtras(data2);
                    // closeDialog3(intent2);
                    closeDialog();
                    startActivity(intent2);
                    finish();
                    break;
                case HANDLER_HW_PUBLISH_FAIL:// 作业发布失败
                    closeDialog();
                    ToastUtils.show(R.string.student_publish_home_work_publish_failed);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.publish_hw_activity);
        ButterKnife.bind(this);
        initObject();
        initView();
        initListener();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        mLocationUtils.startBDLocation();

    }

    @Override
    public void initView() {
        super.initView();
        setWelearnTitle(R.string.student_publish_home_work_title);
        back_layout.setVisibility(View.VISIBLE);
        tv_back.setText("取消");
        iv_back.setVisibility(View.GONE);
        tv_back.setVisibility(View.VISIBLE);
        //取消GridView中Item选中时默认的背景色
        subject_gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));

        initSubject();
        initGridview();


    }

    @Override
    public void initListener() {
        super.initListener();

        back_layout.setOnClickListener(this);
        image_gridview.setOnItemClickListener(this);
        image_gridview.setOnItemClickListener(this);
        btn_fudaoquan.setOnClickListener(this);
        btn_publish_homework.setOnClickListener(this);
        tv_chongzhi_tip.setOnClickListener(this);
        subject_gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int checkedId = subList.get(position).getSubjectid();
                currentSubjectId = checkedId;
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

    public void initObject() {
        mLocationUtils = LocationUtils.getInstance(this);
        fudaoquanApi = new FudaoquanAPI();
        homeworkApi = new HomeWorkAPI();
        mainApi = new MainAPI();
        subList = new ArrayList<SubjectModel>();
        imagePathList = new ArrayList<StuPublishHomeWorkPageModel>();
        fudaoquanList = new ArrayList<FudaoquanModel>();
    }

    public void initGridview() {
        addHomeWorkPageModel = UserManager.getInstance().getAddPhoto(ADD_IMAGE_TAG);
        imagePathList.add(addHomeWorkPageModel);
        ImageGridviewAdapter = new PublishImageGridviewAdapter(this, imagePathList, this);
        image_gridview.setAdapter(ImageGridviewAdapter);
        image_gridview.setOnItemClickListener(this);
    }



    public void initSubject() {
        mUserInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
        gradeid = mUserInfo.getGradeid();
        grade = DBHelper.getInstance().getWeLearnDB().queryGradeByGradeId(gradeid);


         /*从本地数据库获取科目
          subList =
         DBHelper.getInstance().getWeLearnDB().querySubjectByIdList(grade.getSubjectIds());
         rg_subject_radiogroup.removeAllViews();
         rg_subject_radiogroup.invalidate();
         UiUtil.initSubjects(this, subList, rg_subject_radiogroup, false);
         */


        // 请求科目
        OkHttpHelper.post(this, "parents", "publishtasksubjects", null, new HttpListener() {
            @Override
            public void onSuccess(int code, String dataJson, String errMsg) {

                try {
                    if (!TextUtils.isEmpty(dataJson)) {
                        subList = JSON.parseArray(dataJson, SubjectModel.class);
                        //rg_subject_radiogroup.removeAllViews();
                        //rg_subject_radiogroup.invalidate();
                        //生成radioGroup
                        //UiUtil.initSubjects(this, subList, rg_subject_radiogroup, false);
                        //UiUtil.initSubjects2(this, subList, rg_subject_radiogroup, false);
                        subject_gridview.setNumColumns(3);
                        subjectAdapter = new PublishHwSubjectAdapter(PublishHwActivity.this, subList);
                        subject_gridview.setAdapter(subjectAdapter);
                    } else {
                        ToastUtils.show(errMsg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    closeDialog();
                    ToastUtils.show(errMsg);
                    finish();
                }

            }

            @Override
            public void onFail(int HttpCode, String errMsg) {
                ToastUtils.show(errMsg);
            }
        });


    }





    public void initData() {
        if (UserManager.getInstance().judgeIsVIP(mUserInfo)) { //是vip
            layout_fudaoquan.setVisibility(View.GONE);

            /*if (uInfo.getVip_left_time() > 0) {// 没有过期了

            } else {// 已经过期了
                if (fudaoquanApi == null) {
                    fudaoquanApi = new FudaoquanAPI();
                }
                fudaoquanApi.getFudaoquanList(requestQueue, 2, this, RequestConstant.GET_USE_FUDAOQUAN_CODE);
            }*/

        } else {
            layout_fudaoquan.setVisibility(View.GONE);
            btn_fudaoquan.setText("使用辅导券");
            if (fudaoquanApi == null) {
                fudaoquanApi = new FudaoquanAPI();
            }
            if (NetworkUtils.getInstance().isInternetConnected(this)) {
                fudaoquanApi.getFudaoquanList(requestQueue, 2, this, RequestConstant.GET_USE_FUDAOQUAN_CODE);
            } else {
                ToastUtils.show("网络连接失败,请检查网络连接");
            }

        }
        MyApplication.setFudaoquanmodel(null);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_layout:// 返回
                doBack();
                break;
            case R.id.tv_chongzhi_tip:// 充值
                goChongzhi();
                break;
            case R.id.btn_publish_homework:// 发布作业
                MobclickAgent.onEvent(this, "Publish_Homewrok");
                // showPublishHomeworkConfirmDialog();
                if (UserManager.getInstance().judgeIsVIP(mUserInfo)) {// vip
                    executePublishHomeWork();
                } else {
                    if (fudaoquanList != null && fudaoquanCount > 0) {
                        execFudaoquanPublishHomework();
                    } else {
                        noneFudaoquan();
                    }
                }

                break;

            case R.id.btn_fudaoquan:// 辅导券
                if (fudaoquanApi != null) {
                    fudaoquanApi.getCouponinfos(requestQueue, 2, this, RequestConstant.GET_QUAN_INFO_CODE);
                }

                break;


        }
    }

    // 点击顶部的取消
    private void doBack() {
        String description = et_desp.getText().toString().trim();
        if (currentSubjectId != -1 || imagePathList.size() >= 1 || !TextUtils.isEmpty(description)) {
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
//					MySharePerfenceUtil.getInstance().setGold(0);
//					MySharePerfenceUtil.getInstance().setSubject("");
//					MySharePerfenceUtil.getInstance().setDescription("");
//					if (MyApplication.getFudaoquanmodel() != null) {
//						MyApplication.setFudaoquanmodel(null);
//					}
                    if (MyApplication.getFudaoquanmodel() != null) {
                        MyApplication.setFudaoquanmodel(null);
                    }
                    PublishHwActivity.this.finish();
                }
            });

        } else {
//			MySharePerfenceUtil.getInstance().setGold(0);
//			MySharePerfenceUtil.getInstance().setSubject("");
//			MySharePerfenceUtil.getInstance().setDescription("");
//			if (MyApplication.getFudaoquanmodel() != null) {
//				MyApplication.setFudaoquanmodel(null);
//			}
            if (MyApplication.getFudaoquanmodel() != null) {
                MyApplication.setFudaoquanmodel(null);
            }
            finish();
        }


    }


    /**
     * 没有辅导券
     *
     * @return
     */
    public void noneFudaoquan() {
        final CustomNoFudaoquanTipDialog nofudaoquanDialog = new CustomNoFudaoquanTipDialog(PublishHwActivity.this, "",
                "您目前无可用作业券，不能发布作业", "您可拨打400-6755-222，”充值“", "放弃", "充值");
        nofudaoquanDialog.show();
        nofudaoquanDialog.setOnFangqiListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                nofudaoquanDialog.dismiss();
                AppUtils.clickevent("hw_gi_up", PublishHwActivity.this);

                new FudaoquanAPI().giveuppublish(requestQueue, 2, PublishHwActivity.this,
                        RequestConstant.GIVEUP_HW_CODE);
            }
        });

        nofudaoquanDialog.setOnChongzhiListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                nofudaoquanDialog.dismiss();
                AppUtils.clickevent("check_to_buy", PublishHwActivity.this);
                goChongzhi();

            }

        });



    }

    /**
     * 去充值
     */
    private void goChongzhi() {
        if (mUserInfo != null) {
            if (mUserInfo.getVip_type() == 0) {// 非vip
                Intent intent = new Intent(PublishHwActivity.this, VipIndexActivity.class);
                intent.putExtra("type", mUserInfo.getVip_type());
                intent.putExtra("from_location", 4);
                startActivity(intent);

            } else if (mUserInfo.getVip_type() == 1) {// 试用vip
                Intent intent = new Intent(PublishHwActivity.this, VipIndexActivity.class);
                intent.putExtra("type", mUserInfo.getVip_type());
                intent.putExtra("from_location", 4);
                startActivity(intent);

            } else if (mUserInfo.getVip_type() == 2) {// 正式vip
                Intent intent = new Intent(PublishHwActivity.this, VipIndexActivity.class);
                intent.putExtra("type", mUserInfo.getVip_type());
                intent.putExtra("from_location", 4);
                startActivity(intent);

            } else if (mUserInfo.getVip_type() == 3) {// 预约vip
                Intent intent = new Intent(PublishHwActivity.this, VipIndexActivity.class);
                intent.putExtra("type", mUserInfo.getVip_type());
                intent.putExtra("from_location", 4);
                startActivity(intent);
            } else {// 不是vip或者正式vip
                Intent intent = new Intent(PublishHwActivity.this, VipIndexActivity.class);
                intent.putExtra("type", mUserInfo.getVip_type());
                intent.putExtra("from_location", 4);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(PublishHwActivity.this, VipIndexActivity.class);
            intent.putExtra("from_location", 4);
            startActivity(intent);
        }
    }



    private void showPublishHomeworkConfirmDialog() {
        if (gradeid < 1) {
            ToastUtils.show(getString(R.string.student_publish_nograde_text));
            return;
        }

        if (imagePathList.size() == 1) {
            ToastUtils.show(R.string.student_publish_home_work_add_image);
            return;
        }
        if (currentSubjectId == -1) {
            ToastUtils.show(R.string.student_publish_home_work_choose_subject);
            return;
        }
        if (null == mDialog) {
            mDialog = WelearnDialog.getDialog(PublishHwActivity.this);
        }
        mDialog.withMessage(R.string.student_publish_home_work_confirm).setOkButtonClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                executePublishHomeWork();

            }
        });
        mDialog.show();
    }

    /**
     * 此方法描述的是：发布作业
     *
     * @author: sky void
     */
    private void executePublishHomeWork() {
        if (!NetworkUtils.getInstance().isInternetConnected(this)) {
            ToastUtils.show("无可用网络");
            return;
        }

        if (gradeid < 1) {
            ToastUtils.show(getString(R.string.student_publish_nograde_text));
            return;
        }

        if (imagePathList.size() == 1) {
            ToastUtils.show(R.string.student_publish_home_work_add_image);
            return;
        }
        if (currentSubjectId == -1) {
            ToastUtils.show(R.string.student_publish_home_work_choose_subject);
            return;
        }

        String description = et_desp.getText().toString();
        if (TextUtils.isEmpty(description)) {
            // description = getString(R.string.text_question_description);
            description = "";
        }

        // showDialog(getString(R.string.student_publish_home_work_uploading),
        // false);
        boolean isNew = false;

        StuPublishHomeWorkModel pm = new StuPublishHomeWorkModel();
        pm.setMemo(description);
        pm.setSubjectid(currentSubjectId);

        // if (fudaoquanList != null && fudaoquanCount > 0) {// 是否有辅导券
        // fudaoquanmodel = fudaoquanList.get(0);
        // pm.setCouponid(fudaoquanmodel.getId());
        // }
        // 判断是否是重复提交,判断图片是否有修改
        isNew = !(null != tempPm && pm.equals(tempPm) && !fileListModified);

        tempPm = pm;
        fileListModified = false;

        showDialog("上传中...");
        if (isNew || taskId == -1) {// 第一次上传 还没有获取到taskid，所以下面的请求就是获取taskid
            taskId = -1;
            getUploadList();
            // stepOnePublish(pm);
            pm.setNum(uploadList.size());
            pm.setLatitude(mLatitude);
            pm.setLongitude(mLongitude);
            pm.setLocation(mLocation);
            homeworkApi.publishHwStepOne(requestQueue, pm, PublishHwActivity.this, RequestConstant.PUBLISH_HW_CODE);
        } else {
            // mHandler.obtainMessage(HANDLER_GET_TASKID_CODE).sendToTarget();

            taskId = -1;
            getUploadList();
            // stepOnePublish(pm);
            pm.setNum(uploadList.size());
            pm.setLatitude(mLatitude);
            pm.setLongitude(mLongitude);
            pm.setLocation(mLocation);
            homeworkApi.publishHwStepOne(requestQueue, pm, PublishHwActivity.this, RequestConstant.PUBLISH_HW_CODE);
        }

    }

    /**
     * 使用辅导券发布作业
     */
    public void execFudaoquanPublishHomework() {
        if (!NetworkUtils.getInstance().isInternetConnected(PublishHwActivity.this)) {
            ToastUtils.show("无可用网络");
            return;
        }
        if (fudaoquanList != null && fudaoquanCount <= 0) {// 是否有辅导券
            return;
        }
        if (gradeid < 1) {
            ToastUtils.show(getString(R.string.student_publish_nograde_text));
            return;
        }

        if (imagePathList.size() == 1) {
            ToastUtils.show(R.string.student_publish_home_work_add_image);
            return;
        }
        if (currentSubjectId == -1) {
            ToastUtils.show(R.string.student_publish_home_work_choose_subject);
            return;
        }
        String orgName = "";
        String tip = "";
        if (fudaoquanList != null && fudaoquanCount > 0) {// 是否有辅导券
            fudaoquanmodel = fudaoquanList.get(0);
            orgName = fudaoquanmodel.getOrgname();
        }

        if (TextUtils.isEmpty(orgName)) {
            tip = "<p>您目前有<font size=21 color='#ff7200'>" + fudaoquanCount
                    + "</font>张可用作业券，本次发布作业，会消耗<font size=21 color='#ff7200'>" + 1 + "</font>张";
        } else {
            tip = "<p>您目前有<font size=21 color='#ff7200'>" + fudaoquanCount
                    + "</font>张可用作业券，本次发布作业，会消耗<font size=21 color='#ff7200'>" + 1 + "</font>张(来自" + orgName + ")";
        }

        final CustomFudaoquanPublishTipDialog succdialog = new CustomFudaoquanPublishTipDialog(PublishHwActivity.this,
                "", tip, "是否确认发布 ?", "否", "是");
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

                // showDialog(getString(R.string.student_publish_home_work_uploading),
                // false);
                boolean isNew = false;

                StuPublishHomeWorkModel pm = new StuPublishHomeWorkModel();
                String description = et_desp.getText().toString();
                if (TextUtils.isEmpty(description)) {
                    // description =
                    // getString(R.string.text_question_description);
                    description = "";
                }
                pm.setMemo(description);
                pm.setSubjectid(currentSubjectId);

                if (fudaoquanList != null && fudaoquanCount > 0) {// 是否有辅导券
                    fudaoquanmodel = fudaoquanList.get(0);
                    pm.setCouponid(fudaoquanmodel.getId());
                }
                // 判断是否是重复提交,判断图片是否有修改
                isNew = !(null != tempPm && pm.equals(tempPm) && !fileListModified);

                tempPm = pm;
                fileListModified = false;

                showDialog("上传中...");
                if (isNew || taskId == -1) {
                    taskId = -1;
                    getUploadList();
                    // stepOnePublish(pm);
                    pm.setNum(uploadList.size());
                    pm.setLatitude(mLatitude);
                    pm.setLongitude(mLongitude);
                    pm.setLocation(mLocation);
                    homeworkApi.publishHwStepOne(requestQueue, pm, PublishHwActivity.this,
                            RequestConstant.PUBLISH_HW_CODE);
                } else {
                    // mHandler.obtainMessage(HANDLER_GET_TASKID_CODE).sendToTarget();

                    taskId = -1;
                    getUploadList();
                    // stepOnePublish(pm);
                    pm.setNum(uploadList.size());
                    pm.setLatitude(mLatitude);
                    pm.setLongitude(mLongitude);
                    pm.setLocation(mLocation);
                    homeworkApi.publishHwStepOne(requestQueue, pm, PublishHwActivity.this,
                            RequestConstant.PUBLISH_HW_CODE);
                }
            }
        });

    }

    /**
     * 准备好上传到服务器的数据
     *
     * @sky
     */
    private void getUploadList() {
        if (null == uploadList) {
            uploadList = new ArrayList<StuPublishHomeWorkUploadModel>();
        }
        uploadList.clear();
        StuPublishHomeWorkPageModel hwpm = null;
        StuPublishHomeWorkUploadModel ppm;
        StuPublishHomeWorkUploadFileModel spwufm = null;
        for (int i = 0; i < imagePathList.size() && i < 4; i++) {
            hwpm = imagePathList.get(i);
            if (ADD_IMAGE_TAG.equals(hwpm.getImgpath())) {
                break;
            }
            ppm = new StuPublishHomeWorkUploadModel();
            spwufm = new StuPublishHomeWorkUploadFileModel();
            spwufm.setPicseqid(i + 1);
            spwufm.setMemo("");
            spwufm.setSize(new File(hwpm.getImgpath()).length());
            Point p = ImageUtil.getImageSize(hwpm.getImgpath());
            if (p.x <= 0 || p.y <= 0) {
                ToastUtils.show("找不到图片，请重新拍照上传图片");
                return;
            }
            spwufm.setWidth(p.x);
            spwufm.setHeight(p.y);
            ppm.setPicinfo(spwufm);
            uploadList.add(ppm);
        }
    }

    /**
     * 上传图片
     *
     * @param index
     */
    private void uploadImage(int index) {
        if (index < uploadList.size()) {
            StuPublishHomeWorkUploadModel um = uploadList.get(index);
            // um.setAction("add_homework");
            um.setActionid(taskId);
            um.setNum(uploadList.size());
            try {
                Map<String, List<File>> files = new HashMap<String, List<File>>();
                StuPublishHomeWorkPageModel hwpm = imagePathList.get(index);
                List<File> fList = new ArrayList<File>();
                fList.add(new File(hwpm.getImgpath()));
                files.put("picfile", fList);
                try {
                    String image_md5 = MD5Util.getFileMD5String(new File(hwpm.getImgpath()));
                    LogUtils.e("image_md5-->" + "  " + index, image_md5);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                UploadManager.upload(UPLOAD_URL, RequestParamUtils.getParam(new JSONObject(new Gson().toJson(um))), files,
                        PublishHwActivity.this, true, index);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onUploadSuccess(UploadResult result, int index) {
        String dataStr = result.getData();
        int code = result.getCode();
        String msg = result.getMsg();

        mHandler.obtainMessage(HANDLER_UPLOAD_IMAGE_PROGRESS, index, 0).sendToTarget();
        int size = uploadList.size();
        if (index < size - 1) {
            uploadImage(index + 1);
        }
        if (index == size - 1) {
            mHandler.obtainMessage(HANDLER_UPLOAD_IMAGE_FINISH).sendToTarget();
        }

    }

    @Override
    public void onUploadError(String msg, int index) {
        closeDialog();
        if(!TextUtils.isEmpty(msg)){
            ToastUtils.show(msg);
        }else{
          ToastUtils.show(R.string.text_commit_failed_please_retry);
        }
    }

    @Override
    public void onUploadFail(UploadResult result, int index) {
        closeDialog();
        final String msg = result.getMsg();
        if(!TextUtils.isEmpty(msg)){
            ToastUtils.show(msg);
        }else{
            ToastUtils.show(R.string.text_commit_failed_please_retry);
//          ToastUtils.show(R.string.text_commit_failed_please_retry);
        }
        

    }

    /**
     * 图片上传完成后确认操作
     */
    private void uploadFinish() {
        try {
            JSONObject json = new JSONObject();
            json.put("taskid", taskId);
            json.put("num", uploadList.size());
            OkHttpHelper.post(this, "parents", "homeworkuploadfinish", json, new HttpListener() {
                @Override
                public void onFail(int code, String errMsg) {
                    closeDialog();
                    mHandler.obtainMessage(HANDLER_HW_PUBLISH_FAIL).sendToTarget();
                }

                @Override
                public void onSuccess(int code, String dataJson, String errMsg) {
                    if (code == 0) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(HANDLER_HW_PUBLISH_SUCCESS), 1000);
                    } else {
                        mHandler.obtainMessage(HANDLER_HW_PUBLISH_FAIL).sendToTarget();
                    }
                }
            });
        } catch (Exception e) {
            closeDialog();
            e.printStackTrace();
            mHandler.obtainMessage(HANDLER_HW_PUBLISH_FAIL).sendToTarget();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        StuPublishHomeWorkPageModel model = imagePathList.get(position);
        String path = model.getImgpath();
        if (ADD_IMAGE_TAG.equals(path)) {
            IntentManager.goToSelectPicPopupWindow(this);
        } else {
            Bundle data = new Bundle();
            data.putInt(AnswerListItemView.EXTRA_POSITION, position);
            ArrayList<StuPublishHomeWorkPageModel> showList = new ArrayList<StuPublishHomeWorkPageModel>();
            for (int i = 0; i < imagePathList.size(); i++) {
                StuPublishHomeWorkPageModel tm = imagePathList.get(i);
                if (ADD_IMAGE_TAG.equals(tm.getImgpath())) {
                    break;
                }
                showList.add(tm);
            }
            data.putSerializable(TecHomeWorkDetail_OnlyReadActivity.HOMEWROKDETAILPAGERLIST, showList);
            IntentManager.goToHomeWorkDetail_OnlyReadActivity(this, data, false);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //		currentSubjectId = checkedId;
    }

    @Override
    public void onDeleteClick(final int pos) {
        if (null == mDialog) {
            mDialog = WelearnDialog.getDialog(PublishHwActivity.this);
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
                        int size = imagePathList.size();
                        if (pos >= 0 && pos < size) {
                            StuPublishHomeWorkPageModel sp = imagePathList.remove(pos);
                            if (sp != null) {
                                File file = new File(sp.getImgpath());
                                if (file != null) {
                                    file.delete();
                                }
                            } else {
                                ToastUtils.show("删除操作有异常，请返回重新进入发布界面");
                            }
                        }

                        size = imagePathList.size();
                        if (size < MAX_IMAGE_SIZE) {
                            StuPublishHomeWorkPageModel md = imagePathList.get(size - 1);
                            if (!ADD_IMAGE_TAG.equals(md.getImgpath())) {
                                imagePathList.add(addHomeWorkPageModel);
                            }
                        }

                        ImageGridviewAdapter.setData(imagePathList);
                    }
                });
        mDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {

			/*if (requestCode == RequestConstant.REQUEST_CODE_GET_IMAGE_FROM_SYS){
                String savePath = data.getStringExtra(CropImageActivity.IMAGE_SAVE_PATH_TAG);
				LogUtils.i(TAG, "path=" + savePath);
				imagePathList.add(imagePathList.size() - 1, getAddModel(savePath));
				fileListModified = true;
				if (imagePathList.size() > MAX_IMAGE_SIZE) {
					imagePathList.remove(MAX_IMAGE_SIZE);
				}
				ImageGridviewAdapter.setData(imagePathList);
	            
	            
			}*/
            if (requestCode == RequestConstant.REQUEST_CODE_GET_IMAGE_FROM_SYS) {
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
                startActivityForResult(localIntent, REQUEST_CODE_GET_IMAGE_FROM_CROP);
            } else if (requestCode == REQUEST_CODE_GET_IMAGE_FROM_CROP) {
                String savePath = data.getStringExtra(CropImageActivity.IMAGE_SAVE_PATH_TAG);

                LogUtils.i(TAG, "path=" + savePath);

                imagePathList.add(imagePathList.size() - 1, UserManager.getInstance().getAddPhoto(savePath));

                fileListModified = true;

                if (imagePathList.size() > MAX_IMAGE_SIZE) {
                    imagePathList.remove(MAX_IMAGE_SIZE);
                }

                ImageGridviewAdapter.setData(imagePathList);
            } else if (requestCode == REQUEST_EXPIRE_FUDAOQUAN_CODE) {// 取得辅导券的返回值
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        fudaoquanmodel = (FudaoquanModel) data.getSerializableExtra("fudaoquanmodel");
                        if (fudaoquanmodel != null) {
                            MyApplication.setFudaoquanmodel(fudaoquanmodel);
                            chooseFudaoquanTag = 1;
                            btn_fudaoquan.setText("更换辅导券");
                            layout_fudaoquan.setVisibility(View.GONE);
                            view_fudaoquan.setVisibility(View.GONE);
                            tv_fudoaquan_shijian.setText(fudaoquanmodel.getExpireDate());
                            if (fudaoquanmodel.getType() == 1) {
                                tv_fudaotuan_title.setText("已选择一张难题券");

                            } else {
                                tv_fudaotuan_title.setText("已选择一张作业券");

                            }

                        }

                    }
                }

            }
        }
    }

    /**
     * 网络回调
     */
    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        int flag = ((Integer) param[0]).intValue();
        switch (flag) {
            case RequestConstant.GET_USE_FUDAOQUAN_CODE:// 获取作业辅导券
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
                                    FudaoquanModel model = fudaoquanList.get(i);
                                    fudaoquanCount += model.getCount();
                                }

                                if (fudaoquanCount > 0) {
                                    layout_fudaoquan.setVisibility(View.GONE);
                                    String tip = "<p>您目前有<font size=21 color='#ff7200'>" + fudaoquanCount
                                            + "</font>张可用作业券，本次发布作业，会消耗<font size=21 color='#ff7200'>" + 1 + "</font>张";
                                    tv_fudaoquan_tip.setText(Html.fromHtml(tip));

                                } else {
                                    layout_fudaoquan.setVisibility(View.GONE);
                                    ll_fudaoquan_tip.setVisibility(View.GONE);
                                    String tip = "您暂无作业券可用，";
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
                                            Intent i = new Intent(PublishHwActivity.this, VipIndexActivity.class);
                                            startActivity(i);
                                        }
                                    }, 0, underLineText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


                                    tv_fudaoquan_tip.append(spStr);
                                    tv_fudaoquan_tip.setMovementMethod(LinkMovementMethod.getInstance());// 开始响应点击事件
//                                    tv_fudaoquan_tip.setText(tip);
                                }

                            } else {
                                ToastUtils.show("服务器返回异常");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            if (TextUtils.isEmpty(msg)) {
                                ToastUtils.show("无法获取作业辅导券");
                            } else {
                                ToastUtils.show(msg);
                            }
                        }
                    } else {
                        if (TextUtils.isEmpty(msg)) {
                                ToastUtils.show("无法获取作业辅导券");
                        } else {
                            ToastUtils.show(msg);
                        }

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
                                        fudaoquanIntent.putExtra("tag", "choice_tag_homework");
                                        startActivityForResult(fudaoquanIntent, REQUEST_EXPIRE_FUDAOQUAN_CODE);
                                    } else {
                                        String msgstr = JsonUtil.getString(dataJson, "msg", "");
                                        tipDialog = new CustomPublicTipDialog(PublishHwActivity.this, "", msgstr, "",
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
                                                MobclickAgent.onEvent(PublishHwActivity.this, "Open_Vip");
                                                goChongzhi();
                                                tipDialog.dismiss();

                                            }
                                        });
                                        tipDialog.show();
                                    }
                                }
                            } else {
                                ToastUtils.show("获取辅导券信息异常");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            if (TextUtils.isEmpty(msg)) {
                                ToastUtils.show("无法获取作业辅导券");
                            } else {
                                ToastUtils.show(msg);
                            }
                        }
                    } else {

                        if (TextUtils.isEmpty(msg)) {
                            ToastUtils.show("无法获取作业辅导券");
                        } else {
                            ToastUtils.show(msg);
                        }
                    }

                }
                break;
            case RequestConstant.PUBLISH_HW_CODE:// 发布作业获取taskid
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        String dataStr = JsonUtil.getString(datas, "Data", "");
                        if (!TextUtils.isEmpty(dataStr)) {
                            try {
                                JSONObject jobj = new JSONObject(dataStr);
                                // 获取taskid
                                taskId = jobj.getInt("taskid");
                                mHandler.obtainMessage(HANDLER_GET_TASKID_CODE).sendToTarget();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                closeDialog();
                                ToastUtils.show(R.string.student_publish_home_work_publish_failed);
                            }

                        } else {
                            closeDialog();
                            ToastUtils.show("无法获取taskid");
                        }
                    } else {
                        closeDialog();
                        if (!TextUtils.isEmpty(msg)) {
                            ToastUtils.show(msg);
                        } else {
                            ToastUtils.show(R.string.student_publish_home_work_publish_failed);
                        }

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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);


    }

    @Override
    public void onLocationChanged(BDLocation bdLocation, String province, String city) {
        //		LogUtils.d(TAG, "p=" + province + ",c=" + city);
//				ToastUtils.show("bdLocation" + bdLocation.getLatitude() + "," + bdLocation.getLongitude() + "province-->"
//						+ bdLocation.getAddrStr());

        if (bdLocation != null) {
            this.mLatitude = bdLocation.getLatitude();
            this.mLongitude = bdLocation.getLongitude();
            this.mLocation = bdLocation.getAddrStr();
            // mLocationUtils.stopListen();
        }
        mLocationUtils.stopBDLocation();

    }

    @Override
    public void onPause() {
        super.onPause();
        mLocationUtils.stopBDLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationUtils.stopBDLocation();
        ButterKnife.unbind(this);
    }

}
