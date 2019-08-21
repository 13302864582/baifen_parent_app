package com.daxiong.fun.function.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.daxiong.fun.R;
import com.daxiong.fun.api.UserAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.manager.UploadManager;
import com.daxiong.fun.manager.UploadManager.OnUploadListener;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.dialog.SelectSexDialog;
import com.daxiong.fun.dialog.SelectSexDialog.OnSexUpdatedListener;
import com.daxiong.fun.function.homework.CropImageActivity;
import com.daxiong.fun.function.homework.PublishHomeWorkActivity;
import com.daxiong.fun.function.homework.SelectPicPopupWindow;
import com.daxiong.fun.function.question.PayAnswerImageGridActivity;
import com.daxiong.fun.http.RequestParamUtils;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.UploadResult;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.GradeUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.util.WeLearnFileUtil;
import com.daxiong.fun.view.CropCircleTransformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户自己的资料页面
 * @author:  sky
 */
public class MyInfoActivity extends BaseActivity  implements OnClickListener, OnUploadListener{
    private static final String TAG = StuModifiedInfoActivity.class.getSimpleName();

    private static final String UPLOAD_URL = AppConfig.GO_URL + "user/update";

    private static final int REQUEST_CODE_GET_HEAD_IMAGE_FROM_SYS = 101;

    private static final int REQUEST_CODE_BIND = 102;

    private static final int REQUEST_CODE_CITY = 103;

    public static final int REQUEST_CODE_GRADE = 104;
    public static final int REQUEST_CODE_NAME = 105;
    public static final int REQUEST_CODE_SHOOL= 106;

    private View changeAvatarBtn;

    private TextView sexTv;

    private View change_school_btn_modified;

    private View change_password_btn_modified,change_name_btn_modified;

    private View changeGradeBtn;

    private View changeSexBtn;

    private View changeTelBtn;

    private View change_adress_btn_modified;

    private TextView gradeTv;

    private TextView telTv;

    private TextView adress_tv_modified;

    private UserInfoModel uInfo;

    private TextView nickEt, school_tv_modified;

    private View saveBtn;

    private int avatarSize;

    private ImageView avatarIv;

    private TextView bindingTv;

    private String mPath;

    private UserAPI userApi;

    @Override
    @SuppressLint("InlinedApi")
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.myinfo_activity);
        initView();
        initListener();
        initData();

    }

    @Override
    public void initView() {
        super.initView();
        setWelearnTitle(R.string.my_info_text);
        findViewById(R.id.back_layout).setOnClickListener(this);
        avatarIv = (ImageView)findViewById(R.id.avatar_iv_modified);
        nickEt = (TextView)findViewById(R.id.name_tv_modified);
        school_tv_modified = (TextView)findViewById(R.id.school_tv_modified);
        change_school_btn_modified = findViewById(R.id.change_school_btn_modified);
        change_password_btn_modified = findViewById(R.id.change_password_btn_modified);
        change_adress_btn_modified = findViewById(R.id.change_adress_btn_modified);
        changeAvatarBtn = findViewById(R.id.change_avatar_btn_modified);
        changeGradeBtn = findViewById(R.id.change_grade_btn_modified);
        changeSexBtn = findViewById(R.id.change_sex_btn_modified);
        changeTelBtn = findViewById(R.id.change_tel_btn_modified);
        saveBtn = findViewById(R.id.save_btn_modified);
        change_name_btn_modified = findViewById(R.id.change_name_btn_modified);
        adress_tv_modified = (TextView)findViewById(R.id.adress_tv_modified);
        sexTv = (TextView)findViewById(R.id.sex_tv_modified);
        gradeTv = (TextView)findViewById(R.id.grade_tv_modified);
        telTv = (TextView)findViewById(R.id.tel_tv_modified);

        userApi = new UserAPI();
        avatarSize = getResources().getDimensionPixelSize(R.dimen.info_persion_icon_size);
    }

    @Override
    public void initListener() {
        super.initListener();
//        change_school_btn_modified.setOnClickListener(this);
//        change_adress_btn_modified.setOnClickListener(this);
//        change_password_btn_modified.setOnClickListener(this);
//        changeAvatarBtn.setOnClickListener(this);
//        changeGradeBtn.setOnClickListener(this);
//        changeSexBtn.setOnClickListener(this);
//        changeTelBtn.setOnClickListener(this);
//        saveBtn.setOnClickListener(this);
//        change_name_btn_modified.setOnClickListener(this);
    }

    private void initData() {
        uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
        if (null == uInfo) {
            LogUtils.e(TAG, "User info gson is null !");
            return;
        }
        String avatar_100 = uInfo.getAvatar_100();
        if (avatar_100 == null) {
            avatar_100 = "";
        }

        Glide.with(this).load(avatar_100).bitmapTransform(new CropCircleTransformation(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.default_icon_circle_avatar).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(avatarIv);
        String name = uInfo.getName();
        nickEt.setText(name);

        int sex = uInfo.getSex();
        // // 性别
        int sexResId = R.string.sextype_unknown;
        switch (sex) {
            case GlobalContant.SEX_TYPE_MAN:
                sexResId = R.string.sextype_man;
                break;
            case GlobalContant.SEX_TYPE_WOMEN:
                sexResId = R.string.sextype_women;
                break;
        }
        String sexStr = getString(sexResId);
        sexTv.setText(sexStr);

        int gradeid = uInfo.getGradeid();
        String grade = GradeUtil.getGradeString(gradeid);
        gradeTv.setText(grade);
        adress_tv_modified.setText(uInfo.getProvince() + " " + uInfo.getCity());
        String tel = uInfo.getTel();
        school_tv_modified.setText(uInfo.getSchools());
        telTv.setText(tel);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.change_name_btn_modified:
                Intent intent3 = new Intent(this, EditNameAndShool.class);
                intent3.putExtra("tag", "昵称");
                intent3.putExtra("text", uInfo.getName());
                startActivityForResult(intent3, REQUEST_CODE_NAME);
                break;
            case R.id.change_school_btn_modified:
                Intent intent4 = new Intent(this, EditNameAndShool.class);
                intent4.putExtra("text", uInfo.getSchools());
                startActivityForResult(intent4, REQUEST_CODE_SHOOL);
                break;
            case R.id.change_adress_btn_modified:
                Intent intent2 = new Intent(this, SelectCityActivity.class);
                startActivityForResult(intent2, REQUEST_CODE_CITY);
                break;
            case R.id.change_password_btn_modified:// 修改密码
                Intent intent = new Intent(this, ModifyPassActivity.class);
                intent.putExtra("phone", uInfo.getTel());
                startActivity(intent);
                break;
            case R.id.change_avatar_btn_modified:
                startActivityForResult(new Intent(this, SelectPicPopupWindow.class),
                        REQUEST_CODE_GET_HEAD_IMAGE_FROM_SYS);
                break;
            case R.id.change_grade_btn_modified:
                // IntentManager.goToGradeChoiceFromCenter(this);
                  Bundle data = new Bundle();
                  data.putBoolean("isFromCenter", true);
                  data.putInt("mgradeid", uInfo.getGradeid());
                IntentManager.goToGradeChooseFromCenter(this,data);

                break;
            case R.id.change_sex_btn_modified:
                showSexDialog();
                break;
            case R.id.change_tel_btn_modified:// 更换手机号
                uMengEvent("openbindingphone");
                Intent i = new Intent(MyInfoActivity.this, ChangePhoneActivity.class);
                i.putExtra(PhoneRegisterActivity.DO_TAG, PhoneRegisterActivity.DO_BIND);
                startActivityForResult(i, REQUEST_CODE_BIND);
                break;
            case R.id.save_btn_modified:
                execUpdateUserInfo();

                break;

            default:
                break;
        }
    }

    private void execUpdateUserInfo() {
        String name = nickEt.getText().toString().trim();
        String schools = school_tv_modified.getText().toString().trim();
//        if (TextUtils.isEmpty(name)) {
//            ToastUtils.show("昵称不能为空!");
//            return;
//        }
//        if (TextUtils.isEmpty(uInfo.getSchools()) && TextUtils.isEmpty(schools)) {
//            ToastUtils.show("学校不能为空!");
//            return;
//        }
        showDialog("请稍候");

        // WeLearnApi.updateUserInfoFromServer(this, mPath, name,
        // uInfo.getSex(),
        // uInfo.getGradeid(),uInfo.getProvince(),uInfo.getCity(),schools,
        // this);
        JSONObject subParams = new JSONObject();
        try {
            Map<String, List<File>> files = null;
            if (!TextUtils.isEmpty(mPath)) {
                files = new HashMap<String, List<File>>();
                List<File> fList = new ArrayList<File>();
                fList.add(new File(mPath));
                files.put("picfile", fList);
                subParams.put("avatar", "picfile");
            }
            // subParams.put("background", background);
            subParams.put("name", name);
            subParams.put("gradeid", uInfo.getGradeid());
            subParams.put("schools", schools);
            subParams.put("sex", uInfo.getSex());
            subParams.put("province", uInfo.getProvince());
            subParams.put("city", uInfo.getCity());
            // userApi.updateUserInfo(requestQueue, subParams, this,
            // RequestConstant.UPDATE_USERINFO);

            UploadManager.upload(AppConfig.GO_URL + "parents/upuserinfos",
                    RequestParamUtils.getParam(subParams), files, this, true, 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUploadSuccess(UploadResult result, int index) {
        String data = result.getData();
        UserInfoModel uInfo = new Gson().fromJson(data, UserInfoModel.class);
        DBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);
        closeDialog();
        ToastUtils.show("修改成功!");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onUploadError(String msg, int index) {
        closeDialog();
        if (!TextUtils.isEmpty(msg)) {
            ToastUtils.show(msg);
        }
    }

    @Override
    public void onUploadFail(UploadResult result, int index) {
        closeDialog();
        if (result != null) {
            String msg = result.getMsg();
            if (!TextUtils.isEmpty(msg)) {
                ToastUtils.show(msg);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_GET_HEAD_IMAGE_FROM_SYS:// 换头像
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
                case PublishHomeWorkActivity.REQUEST_CODE_GET_IMAGE_FROM_CROP:// 头像返回
                    mPath = data.getStringExtra(CropImageActivity.IMAGE_SAVE_PATH_TAG);

                    Glide.with(this).load(mPath).diskCacheStrategy(DiskCacheStrategy.ALL).bitmapTransform(new CropCircleTransformation(this))
                            .placeholder(R.drawable.default_icon_circle_avatar)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(avatarIv);
                    break;
                case REQUEST_CODE_BIND:
                    if (data != null) {
                        String tel = data.getStringExtra("tel");
                        if (!TextUtils.isEmpty(tel)) {
                            // bindingTv.setText(getString(R.string.change_binding_text));
                            telTv.setText(tel);
                        }
                    }
                    break;
                case REQUEST_CODE_GRADE:
                    if (data != null) {
                        int gradeid = data.getIntExtra("gradeid", 0);
                        uInfo.setGradeid(gradeid);
                        String grade = GradeUtil.getGradeString(gradeid);
                        gradeTv.setText(grade);
                    }
                    break;
                case REQUEST_CODE_CITY:
                    if (data != null) {
                        String Province = data.getStringExtra("Province");
                        String city = data.getStringExtra("city");
                        if (!TextUtils.isEmpty(Province)) {
                            uInfo.setProvince(Province);
                            if (!TextUtils.isEmpty(city)) {
                                uInfo.setCity(city);
                                adress_tv_modified
                                        .setText(uInfo.getProvince() + " " + uInfo.getCity());
                            }
                        }

                    }
                    break;
                case REQUEST_CODE_NAME:
                     if (data != null) {
                         String resultString = data.getStringExtra("resultString");
                        
                         if (!TextUtils.isEmpty(resultString)) {
                             uInfo.setName(resultString);
                             nickEt.setText(resultString);
                         }

                     }
                    break;
                case REQUEST_CODE_SHOOL:
                     if (data != null) {
                         String resultString = data.getStringExtra("resultString");
                        
                         if (!TextUtils.isEmpty(resultString)) {
                             uInfo.setSchools(resultString);
                             school_tv_modified.setText(resultString);
                         }

                     }
                    break;
                default:
                    break;
            }
        }

    }

    private void showSexDialog() {
        SelectSexDialog ss = new SelectSexDialog(this, uInfo.getSex(), new OnSexUpdatedListener() {

            @Override
            public void onSexUpdated(int selectSex) {
                uInfo.setSex(selectSex);

                int sex = uInfo.getSex();
                // // 性别
                int sexResId = R.string.sextype_unknown;
                switch (sex) {
                    case GlobalContant.SEX_TYPE_MAN:
                        sexResId = R.string.sextype_man;
                        break;
                    case GlobalContant.SEX_TYPE_WOMEN:
                        sexResId = R.string.sextype_women;
                        break;
                }
                String sexStr = getString(sexResId);
                sexTv.setText(sexStr);
            }
        });
        ss.setCanceledOnTouchOutside(true);
        ss.show();
    }



}
