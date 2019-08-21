
package com.daxiong.fun.function.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.api.WeLearnApi;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.base.ImageLoader;
import com.daxiong.fun.manager.UploadManager;
import com.daxiong.fun.manager.UploadManager.OnUploadListener;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.function.homework.CropImageActivity;
import com.daxiong.fun.function.homework.SelectPicPopupWindow;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.http.RequestParamUtils;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.UploadResult;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.GoldToStringUtil;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 此类的描述：个人主页
 * @author: sky 
 * @最后修改人： sky
 * @最后修改日期:2015年8月6日 下午2:36:29
 */

public class PersonHomePageActivity extends BaseActivity
        implements OnClickListener, HttpListener, OnUploadListener {

    public static final String TAG = PersonHomePageActivity.class.getSimpleName();

    public static int SCALE_LOGO_IMG_SIZE = 120;

    public static final int REQUEST_CODE_BIND = 0;

    public static final int REQUEST_CODE_MODIFY = 1011;

    public static final int REQUEST_CODE_CROP = 1012;

    private UserInfoModel mUserInfo;

    // private RelativeLayout headBgLayout;
    private NetworkImageView iv_head_bg;

    private NetworkImageView iv_avatar;

    private TextView tv_nick;

    private TextView tv_grade;

    private LinearLayout moreLayout;

    private Button btn_logout;

    private int avatarSize;// , headIvWidth, headIvHeight, headIvTopMargin,
                           // headBgWidth, headBgHeight, headBgTopMargin;

    private AlphaAnimation aa = new AlphaAnimation(0f, 1.0f);

    private TextView stuIdTv;

    private TextView stuCreditTv;

    private ImageView stuSexIv;

    private static final int REQUEST_CODE_GET_HEAD_IMAGE_FROM_SYS = 0x1;

    private static final int REQUEST_CODE_GET_BG_IMAGE_FROM_SYS = 0x2;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_student_center);
        aa.setDuration(500);
        initView();
        initListener();
        updateUiInfo();
    }

    public void initView() {
        setWelearnTitle(R.string.contact_person_info);
        findViewById(R.id.back_layout).setOnClickListener(this);
        avatarSize = getResources().getDimensionPixelSize(R.dimen.info_persion_icon_size);

        // headBgLayout = (RelativeLayout) findViewById(R.id.head_bg_layout);
        iv_head_bg = (NetworkImageView)findViewById(R.id.stu_info_bg_iv);// 背景

        iv_avatar = (NetworkImageView)findViewById(R.id.stu_info_head_iv);// 头像
        // stuHeadIv.setOnClickListener(this);
        tv_nick = (TextView)findViewById(R.id.stu_info_nick_tv);// 昵称
        stuSexIv = (ImageView)findViewById(R.id.stu_info_sex_iv);// 信用/经验
        tv_grade = (TextView)findViewById(R.id.stu_info_grade_tv);// 年级
        stuIdTv = (TextView)findViewById(R.id.userid_tv_stu_cen);// 用户ID
        stuCreditTv = (TextView)findViewById(R.id.credit_tv_stu_cen);// 信用/经验

        moreLayout = (LinearLayout)findViewById(R.id.stu_info_more_layout);

        btn_logout = (Button)findViewById(R.id.tec_logout_btn);
        btn_logout.setVisibility(View.VISIBLE);

    }

    public void initListener() {
        findViewById(R.id.modifi_info_layout_stu_cen).setOnClickListener(this);// 点击修改资料
        findViewById(R.id.modifi_btn_stu_cen).setOnClickListener(this);// 点击修改资料
        iv_head_bg.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        WeLearnApi.getUserInfoFromServer(this, this);
    }

    private void updateUiInfo() {
        mUserInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
        if (null == mUserInfo) {
            LogUtils.e(TAG, "User info gson is null !");
            return;
        }
        moreLayout.removeAllViews();

        // 背景
        String groupphoto = mUserInfo.getGroupphoto();
        if (groupphoto == null) {
            groupphoto = "";
        }
        if (mBitmap != null) {
            iv_head_bg.setCustomBitmap(mBitmap);
        }
        ImageLoader.getInstance().loadImage(groupphoto, iv_head_bg, R.color.welearn_blue_heavy);

        // 头像
        String avatar_100 = mUserInfo.getAvatar_100();
        if (avatar_100 == null) {
            avatar_100 = "";
        }
        ImageLoader.getInstance().loadImageWithDefaultAvatar(avatar_100, iv_avatar, avatarSize,
                avatarSize / 10);
        // 昵称
        String name = TextUtils.isEmpty(mUserInfo.getName()) ? getString(R.string.persion_info)
                : mUserInfo.getName();
        tv_nick.setText(name);

        // 年级
        String grade = TextUtils.isEmpty(mUserInfo.getGrade()) ? "" : mUserInfo.getGrade();
        tv_grade.setText(grade);

        stuSexIv.setVisibility(View.VISIBLE);
        int sex = mUserInfo.getSex();
        switch (sex) {
            case GlobalContant.SEX_TYPE_MAN:
                stuSexIv.setImageResource(R.drawable.man_icon);
                break;
            case GlobalContant.SEX_TYPE_WOMEN:
                stuSexIv.setImageResource(R.drawable.woman_icon);
                break;

            default:
                stuSexIv.setVisibility(View.GONE);
                break;
        }

        int userid = mUserInfo.getUserid();
        stuIdTv.setText(getString(R.string.welearn_id_text, userid + ""));

        float credit = mUserInfo.getCredit();
        String creditStr = GoldToStringUtil.GoldToString(credit);
        stuCreditTv.setText(getString(R.string.credit_text, creditStr));

        String area = "";
        String province = mUserInfo.getProvince();
        if (!TextUtils.isEmpty(province)) {
            area = province + " ";
        }
        String city = mUserInfo.getCity();
        if (!TextUtils.isEmpty(city)) {
            area = area + city;
        }
        if (!TextUtils.isEmpty(area)) {
            View areaView = getItem(R.string.stu_info_item_title_area, "", area, false);
            moreLayout.addView(areaView);
        }

        // // 性别
        // int sexResId = R.string.sextype_unknown;
        // switch (uInfo.getSex()) {
        // case GlobalContant.SEX_TYPE_MAN:
        // sexResId = R.string.sextype_man;
        // break;
        // case GlobalContant.SEX_TYPE_WOMEN:
        // sexResId = R.string.sextype_women;
        // break;
        // }

        String tel = mUserInfo.getTel();
        if (TextUtils.isEmpty(tel)) {
            tel = "未绑定";
        }
        View telView = getItem(R.string.text_phone_num, null, tel, false);
        telView.setId(155);
        moreLayout.addView(telView);
        telView.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
                uMengEvent("openbindingphone");
                Intent i = new Intent(PersonHomePageActivity.this, PhoneRegisterActivity.class);
                i.putExtra(PhoneRegisterActivity.DO_TAG, PhoneRegisterActivity.DO_BIND);
                startActivityForResult(i, REQUEST_CODE_BIND);
                
            }
        });
        
        View passwordView = getItem(R.string.modify_pass, "", "", true);
        moreLayout.addView(passwordView);
        
        //最后加一条直线
        View view=new View(this);
        view.setBackgroundColor(Color.parseColor("#cccccc"));        
        LinearLayout.LayoutParams parms=new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 1);
        view.setLayoutParams(parms);        
        moreLayout.addView(view);
        //修改密码点击事件
        passwordView.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mUserInfo.getTel())&&!"未绑定".equals(mUserInfo.getTel())) {
                    Intent intent=new Intent(PersonHomePageActivity.this,ModifyPassActivity.class);
                    intent.putExtra("phone", mUserInfo.getTel());
                    startActivity(intent);
                }
                
            }
        });
        

        // uMengEvent("openbindingphone");
        // Intent i = new Intent(StudentCenterActivityNew.this,
        // PhoneRegisterActivity.class);
        // i.putExtra(PhoneRegisterActivity.DO_TAG,
        // PhoneRegisterActivity.DO_BIND);
        // startActivityForResult(i, REQUEST_CODE_BIND);
    }

    private View getItem(int titleResId, String leftStr, String rightStr, boolean showArrow) {
        View v = View.inflate(this, R.layout.view_stu_info_item, null);

        TextView titleTV = (TextView)v.findViewById(R.id.item_title_tv);
        TextView titleValueTV = (TextView)v.findViewById(R.id.item_title_value_tv);
        TextView arrowValueTV = (TextView)v.findViewById(R.id.item_arrow_value_tv);
        ImageView arrowIv = (ImageView)v.findViewById(R.id.item_arrow_iv);

        titleTV.setText(titleResId);
        if (TextUtils.isEmpty(leftStr)) {
            leftStr = "";
        }
        titleValueTV.setText(leftStr);

        if (TextUtils.isEmpty(rightStr)) {
            rightStr = "";
        }
        arrowValueTV.setText(rightStr);

        arrowIv.setVisibility(showArrow ? View.VISIBLE : View.GONE);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout://返回
                finish();
                break;
            case R.id.stu_info_bg_iv://点击头部的背景换背景
                startActivityForResult(new Intent(this, SelectPicPopupWindow.class),
                        REQUEST_CODE_GET_BG_IMAGE_FROM_SYS);
                break;
            case R.id.modifi_info_layout_stu_cen:
            case R.id.modifi_btn_stu_cen://修改用户的信息
                IntentManager.goToStuModifiedInfoActivity(this);
                break;
            // case R.id.stu_info_head_iv:
            // startActivityForResult(new Intent(this,
            // SelectPicPopupWindow.class),
            // REQUEST_CODE_GET_HEAD_IMAGE_FROM_SYS);
            // break;
            case R.id.tec_logout_btn://推出登录
                MobclickAgent.onEvent(this, "logout");
                // WeLearnApi.logout(this);
                showDialog(getString(R.string.text_logging_out));
                OkHttpHelper.post(PersonHomePageActivity.this, "user", "logout", null,
                        new HttpListener() {
                            @Override
                            public void onSuccess(int code, String dataJson, String errMsg) {

                            	 cleanUseInfo();
                            }

						

                            @Override
                            public void onFail(int HttpCode,String errMsg) {
                            	 cleanUseInfo();
                            }
                        });

                break;
        }
    }
	private void cleanUseInfo() {
		IntentManager.stopWService(PersonHomePageActivity.this);

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
         IntentManager.goToGuideActivity(PersonHomePageActivity.this);
	}
    @Override
    public void onSuccess(int code, String dataJson, String errMsg) {
        if (code == 0) {
            updateUiInfo();
        } else {
            if (!TextUtils.isEmpty(errMsg)) {
                ToastUtils.show(errMsg);
            }
        }
    }

    @Override
    public void onFail(int HttpCode,String errMsg) {
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case REQUEST_CODE_BIND:
                    WeLearnApi.getUserInfoFromServer(this, this);
                    break;
                case REQUEST_CODE_GET_HEAD_IMAGE_FROM_SYS:
                case REQUEST_CODE_GET_BG_IMAGE_FROM_SYS:
                    if (data != null) {

                        String path = data.getStringExtra("path");
                        boolean isFromPhotoList = data.getBooleanExtra("isFromPhotoList", false);
                        LogUtils.i(TAG, path);
                        if (TextUtils.isEmpty(path)) {
                            LogUtils.i(TAG, "path is null");
                            return;
                        }
                        path = CropImageActivity.autoFixOrientation(path, isFromPhotoList, this,
                                null);

                        IntentManager.goToCropPhotoActivity(this, path);

                    }

                    break;
                case REQUEST_CODE_MODIFY:
                    break;
                case REQUEST_CODE_CROP:
                    if (data != null) {
                        String path = data.getStringExtra("path");
                        mBitmap = BitmapFactory.decodeFile(path);
                        postImageToServer("background", path);
                    }
                    // if (requestCode == REQUEST_CODE_GET_HEAD_IMAGE_FROM_SYS)
                    // {
                    // // 上传头像
                    // postImageToServer("avatar", path);
                    // } else if (requestCode ==
                    // REQUEST_CODE_GET_BG_IMAGE_FROM_SYS) {
                    // // 上传背景
                    // }
                    break;
                default:
                    break;
            }

        }

    }

    public static final String UPLOAD_URL = AppConfig.GO_URL + "user/update";

    private Bitmap mBitmap;

    private void postImageToServer(String key, String filePath) {
        try {
            Map<String, List<File>> files = new HashMap<String, List<File>>();
            List<File> fList = new ArrayList<File>();
            fList.add(new File(filePath));
            files.put("picfile", fList);
            JSONObject obj = new JSONObject();
            obj.put(key, "picfile");
            UploadManager.upload(UPLOAD_URL, RequestParamUtils.getParam(obj), files, this, true, 0);
            showDialog("上传中,请稍候");
        } catch (JSONException e) {
            e.printStackTrace();
            ToastUtils.show("error");
        }
    }

    @Override
    public void onUploadSuccess(UploadResult result, int index) {
        closeDialog();
        if (null == result) {
            ToastUtils.show(R.string.upload_failed);
            return;
        }
        if (result.getCode() == 0) {
            WeLearnApi.getUserInfoFromServer(this, this);
        } else {
            if (!TextUtils.isEmpty(result.getMsg())) {
                ToastUtils.show(result.getMsg());
            } else {
                ToastUtils.show(R.string.upload_failed);
            }
        }
    }

    @Override
    public void onUploadError(String msg, int index) {
        closeDialog();
        if (!TextUtils.isEmpty(msg)) {
            // ToastUtils.show(msg);
        }
    }

    @Override
    public void onUploadFail(UploadResult result, int index) {
        closeDialog();
        if (null == result) {
            ToastUtils.show(R.string.upload_failed);
            return;
        }
        if (result.getCode() == 0) {
            WeLearnApi.getUserInfoFromServer(this, this);
        } else {
            if (!TextUtils.isEmpty(result.getMsg())) {
                ToastUtils.show(result.getMsg());
            } else {
                ToastUtils.show(R.string.upload_failed);
            }
        }
    }
    
    
    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        int flag = ((Integer)param[0]).intValue();
        switch (flag) {
            case RequestConstant.REQUEST_SPECIAL_STUDENT_CODE:// 查询特殊学生账号
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String rootStr = param[1].toString();                    
                    int code = JsonUtil.getInt(rootStr, "Code", -1);
                    String msg = JsonUtil.getString(rootStr, "Msg", "");
                    if (code == 0) {
                        try {
                            String dataStr = JsonUtil.getString(rootStr, "Data", "");
                            JSONObject dataJson = new JSONObject(dataStr);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }                

                }
                break;

        }

    }

    

}
