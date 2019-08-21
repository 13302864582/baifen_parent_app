
package com.daxiong.fun.function.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.daxiong.fun.R;
import com.daxiong.fun.api.UserAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.dialog.SelectSexDialog;
import com.daxiong.fun.dialog.SelectSexDialog.OnSexUpdatedListener;
import com.daxiong.fun.function.homework.CropImageActivity;
import com.daxiong.fun.function.homework.PublishHomeWorkActivity;
import com.daxiong.fun.function.question.PayAnswerImageGridActivity;
import com.daxiong.fun.http.RequestParamUtils;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.manager.UploadManager2;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.okhttp.callback.StringCallback;
import com.daxiong.fun.util.GradeUtil;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.util.WeLearnFileUtil;
import com.daxiong.fun.view.CropCircleTransformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;

/**
 * 个人基本资料
 */
public class StuModifiedInfoActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = StuModifiedInfoActivity.class.getSimpleName();

    private static final String UPLOAD_URL = AppConfig.GO_URL + "user/update";

    private static final int REQUEST_CODE_GET_HEAD_IMAGE_FROM_SYS = 101;


    public static final int REQUEST_CODE_AVATARS = 1025;

    private static final int REQUEST_CODE_BIND = 102;

    private static final int REQUEST_CODE_CITY = 103;

    public static final int REQUEST_CODE_GRADE = 104;

    public static final int REQUEST_CODE_NAME = 105;

    public static final int REQUEST_CODE_GENDER = 107;

    public static final int REQUEST_CODE_SHOOL = 106;

    private View changeAvatarBtn;

    private TextView sexTv;

    private View change_school_btn_modified;

    private View change_password_btn_modified, change_name_btn_modified;

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

    private int gradeid = 0;
    private int sexResId = 0;
    private String province = "";
    private String city = "";

    private LinearLayout ll_avatar;

    @Override
    @SuppressLint("InlinedApi")
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_modified_data);
        initView();
        initListener();
        bindData();

    }



    @Override
    public void initView() {
        super.initView();
        setWelearnTitle(R.string.user_info_text);
        findViewById(R.id.back_layout).setOnClickListener(this);
        ll_avatar = (LinearLayout) findViewById(R.id.ll_avatar);
        avatarIv = (ImageView) findViewById(R.id.avatar_iv_modified);
        nickEt = (TextView) findViewById(R.id.name_tv_modified);
        school_tv_modified = (TextView) findViewById(R.id.school_tv_modified);
        change_school_btn_modified = findViewById(R.id.change_school_btn_modified);
        change_password_btn_modified = findViewById(R.id.change_password_btn_modified);
        change_adress_btn_modified = findViewById(R.id.change_adress_btn_modified);
        changeAvatarBtn = findViewById(R.id.change_avatar_btn_modified);
        changeGradeBtn = findViewById(R.id.change_grade_btn_modified);
        changeSexBtn = findViewById(R.id.change_sex_btn_modified);
        changeTelBtn = findViewById(R.id.change_tel_btn_modified);
        saveBtn = findViewById(R.id.save_btn_modified);
        change_name_btn_modified = findViewById(R.id.change_name_btn_modified);
        adress_tv_modified = (TextView) findViewById(R.id.adress_tv_modified);
        sexTv = (TextView) findViewById(R.id.sex_tv_modified);
        gradeTv = (TextView) findViewById(R.id.grade_tv_modified);
        telTv = (TextView) findViewById(R.id.tel_tv_modified);

        userApi = new UserAPI();
        avatarSize = getResources().getDimensionPixelSize(R.dimen.info_persion_icon_size);
    }

    @Override
    public void initListener() {
        super.initListener();
        ll_avatar.setOnClickListener(this);
        change_school_btn_modified.setOnClickListener(this);
        change_adress_btn_modified.setOnClickListener(this);
        change_password_btn_modified.setOnClickListener(this);
        changeAvatarBtn.setOnClickListener(this);
        changeGradeBtn.setOnClickListener(this);
        changeSexBtn.setOnClickListener(this);
        changeTelBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        change_name_btn_modified.setOnClickListener(this);
    }

    private void bindData() {
        uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
        if (null == uInfo) {
            LogUtils.e(TAG, "User info gson is null !");
            return;
        }
        String avatar_100 = uInfo.getAvatar_100();
        if (avatar_100 == null) {
            avatar_100 = "";
        }

        Glide.with(this).load(avatar_100).diskCacheStrategy(DiskCacheStrategy.ALL).bitmapTransform(new CropCircleTransformation(this))
                .placeholder(R.drawable.default_icon_circle_avatar).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(avatarIv);
        String name = uInfo.getName();
        nickEt.setText(name);

        int sex = uInfo.getSex();
        // // 性别
        sexResId = R.string.sextype_unknown;
        switch (sex) {
            case GlobalContant.SEX_TYPE_MAN:
                sexResId = R.string.sextype_man;
                break;
            case GlobalContant.SEX_TYPE_WOMEN:
                sexResId = R.string.sextype_women;
                break;
            case GlobalContant.SEX_TYPE_UNKNOW:// 未知性别
                sexResId = R.string.sextype_unknown;
                break;
        }
        String sexStr = getString(sexResId);
        sexTv.setText(sexStr);

        gradeid = uInfo.getGradeid();
        String grade = GradeUtil.getGradeString(gradeid);
        gradeTv.setText(grade);
        province = uInfo.getProvince();
        city = uInfo.getCity();
        adress_tv_modified.setText(province + city);
        String tel = uInfo.getTel();
        school_tv_modified.setText(uInfo.getSchools());
        telTv.setText(tel);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
    /*		if (NetworkUtils.getInstance().isInternetConnected(this)) {
                boolean isSave = judgeIsSave();
				if (isSave) {
					final TextOkCancleDialog dialog = new TextOkCancleDialog(this);
					dialog.setOnPositiveListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
							execUpdateUserInfo();

						}
					});

					dialog.setOnNegativeListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
							finish();
						}
					});
					dialog.show();
				} else {
					finish();
				}
			} else {
				ToastUtils.show("没网络了");
				finish();
			}*/
                finish();
                break;
            case R.id.ll_avatar://头像
                Intent avatarIntent = new Intent(this, UserAvatarActivity.class);
                avatarIntent.putExtra("avatar", uInfo.getAvatar_100());
                avatarIntent.putExtra("tag", "change");
                startActivityForResult(avatarIntent,REQUEST_CODE_AVATARS);
                break;
            case R.id.change_avatar_btn_modified://更换头像
                //startActivityForResult(new Intent(this, SelectPicPopupWindow.class), REQUEST_CODE_GET_HEAD_IMAGE_FROM_SYS);
                break;
            case R.id.change_name_btn_modified://昵称
                Intent intent3 = new Intent(this, EditNameAndShool.class);
                intent3.putExtra("tag", "昵称");
                intent3.putExtra("text", uInfo.getName());
                startActivityForResult(intent3, REQUEST_CODE_NAME);
                break;
            case R.id.change_sex_btn_modified://性别
//                showSexDialog();
                Intent intentx = new Intent(this, ChooseGenderActivity.class);
                String genderStr = sexTv.getText().toString().trim();
                if ("男".equals(genderStr)) {
                    intentx.putExtra("genderid", 1);
                } else if ("女".equals(genderStr)) {
                    intentx.putExtra("genderid", 2);
                } else {
                    intentx.putExtra("genderid", 0);
                }
                startActivityForResult(intentx, REQUEST_CODE_GENDER);
                break;
            case R.id.change_adress_btn_modified://所在地区
                Intent intent2 = new Intent(this, SelectCityActivity.class);
                startActivityForResult(intent2, REQUEST_CODE_CITY);
                break;
            case R.id.change_school_btn_modified://学校
                Intent intent4 = new Intent(this, EditNameAndShool.class);
                intent4.putExtra("text", uInfo.getSchools());
                startActivityForResult(intent4, REQUEST_CODE_SHOOL);
                break;
            case R.id.change_grade_btn_modified://修改年级
                // IntentManager.goToGradeChoiceFromCenter(this);
                Bundle data = new Bundle();
                data.putBoolean("isFromCenter", true);
//			data.putInt("mgradeid", uInfo.getGradeid());
                data.putInt("mgradeid", gradeid);
                IntentManager.goToGradeChooseFromCenter(this, data);

                break;
            case R.id.change_tel_btn_modified:// 更换手机号
                uMengEvent("openbindingphone");
                Intent i = new Intent(StuModifiedInfoActivity.this, ChangePhoneActivity.class);
                i.putExtra("phone", telTv.getText().toString());
                i.putExtra(PhoneRegisterActivity.DO_TAG, PhoneRegisterActivity.DO_BIND);
                startActivityForResult(i, REQUEST_CODE_BIND);
                break;

            case R.id.change_password_btn_modified:// 修改密码
                Intent intent = new Intent(this, ModifyPassActivity.class);
                intent.putExtra("phone", uInfo.getTel());
                startActivity(intent);
                break;


            case R.id.save_btn_modified:
               /* if (NetworkUtils.getInstance().isInternetConnected(this)) {
                    boolean isSave = judgeIsSave();
                    if (isSave) {
                        saveUserInfo();
                    } else {
                        finish();
                    }
                } else {
                    ToastUtils.show("没网络了");
                    finish();
                }*/
                break;
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
                case PublishHomeWorkActivity.REQUEST_CODE_GET_IMAGE_FROM_CROP:// 头像返回
                    mPath = data.getStringExtra(CropImageActivity.IMAGE_SAVE_PATH_TAG);

                    Glide.with(this).load(mPath).diskCacheStrategy(DiskCacheStrategy.ALL).bitmapTransform(new CropCircleTransformation(this))
                            .placeholder(R.drawable.default_icon_circle_avatar).diskCacheStrategy(DiskCacheStrategy.ALL).into(avatarIv);
                    break;
                case REQUEST_CODE_AVATARS://头像
                    String avatarPath=data.getStringExtra("avatar");
                    Glide.with(this).load(avatarPath).diskCacheStrategy(DiskCacheStrategy.ALL).bitmapTransform(new CropCircleTransformation(this))
                            .placeholder(R.drawable.default_icon_circle_avatar).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(avatarIv);
                    break;
                case REQUEST_CODE_NAME:// 昵称
                    if (data != null) {
                        String resultString = data.getStringExtra("resultString");

                        if (!TextUtils.isEmpty(resultString)) {
                            // uInfo.setName(resultString);
                            nickEt.setText(resultString);
                        }

                    }
                    break;
                case REQUEST_CODE_GENDER://性别
                    if (data != null) {
                        int genderid = data.getIntExtra("genderid",1);
                        if (genderid==1) {
                            sexTv.setText("男");
                        } else if (genderid==2) {
                            sexTv.setText("女");
                        } else {
                            sexTv.setText("男");
                        }
                    }
                    break;
                case REQUEST_CODE_BIND:// 手机号
                    if (data != null) {
                        String tel = data.getStringExtra("tel");
                        if (!TextUtils.isEmpty(tel)) {
                            // bindingTv.setText(getString(R.string.change_binding_text));
                            telTv.setText(tel);
                        }
                    }
                    break;
                case REQUEST_CODE_GRADE:// 年级
                    if (data != null) {
                        gradeid = data.getIntExtra("gradeid", 0);
                        // uInfo.setGradeid(gradeid);
                        String grade = GradeUtil.getGradeString(gradeid);
                        gradeTv.setText(grade);
                    }
                    break;
                case REQUEST_CODE_CITY:// 地区
                    if (data != null) {
                        province = data.getStringExtra("Province");
                        city = data.getStringExtra("city");
                        if (!TextUtils.isEmpty(province)) {
                            // uInfo.setProvince(Province);
                            if (!TextUtils.isEmpty(city)) {
                                // uInfo.setCity(city);
                                adress_tv_modified.setText(province + " " + city);
                            }
                            saveUserInfo("modify_district");
                        }

                    }
                    break;

                case REQUEST_CODE_SHOOL:// 学校
                    if (data != null) {
                        String resultString = data.getStringExtra("resultString");

                        if (!TextUtils.isEmpty(resultString)) {
                            // uInfo.setSchools(resultString);
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
        // 性别
        int sexInt = 0;
        String genderStr = sexTv.getText().toString().trim();
        if ("男".equals(genderStr)) {
            sexInt = 1;
        } else if ("女".equals(genderStr)) {
            sexInt = 2;
        } else {
            sexInt = 0;
        }
        SelectSexDialog ss = new SelectSexDialog(this, sexInt, new OnSexUpdatedListener() {
            @Override
            public void onSexUpdated(int selectSex) {
//				uInfo.setSex(selectSex);
//				int sex = uInfo.getSex();
                // // 性别

                switch (selectSex) {
                    case GlobalContant.SEX_TYPE_MAN:
                        sexResId = R.string.sextype_man;
                        break;
                    case GlobalContant.SEX_TYPE_WOMEN:
                        sexResId = R.string.sextype_women;
                        break;
                    case GlobalContant.SEX_TYPE_UNKNOW:
                        sexResId = R.string.sextype_unknown;
                        break;
                }
                String sexStr = getString(sexResId);
                sexTv.setText(sexStr);
            }
        });
        ss.setCanceledOnTouchOutside(true);
        ss.show();
    }

    /**
     * 判断是否需要保存
     *
     * @return
     */
    private boolean judgeIsSave() {
        boolean isRn = false;
        String avatar_100Str = mPath;
        String nickStr = nickEt.getText().toString().trim();
        String genderStr = sexTv.getText().toString().trim();
        String districtStr = adress_tv_modified.getText().toString().trim();
        String schoolStr = school_tv_modified.getText().toString().trim();
        String gradeStr = gradeTv.getText().toString().trim();
        String telStr = telTv.getText().toString().trim();

        int sex = uInfo.getSex();
        int sexInt = 0;
        // 性别
        if ("男".equals(genderStr)) {
            sexInt = 1;
        } else if ("女".equals(genderStr)) {
            sexInt = 2;
        } else {
            sexInt = 0;
        }

        String address = uInfo.getProvince() + uInfo.getCity();

        int gradeid = uInfo.getGradeid();
        String grade = GradeUtil.getGradeString(gradeid);

        String avatar_100 = uInfo.getAvatar_100();
        if (avatar_100 == null) {
            avatar_100 = "";
        }

        if (!avatar_100.equals(avatar_100Str)) {
            isRn = true;
        } else if (!uInfo.getName().equals(nickStr)) {
            isRn = true;
        } else if (sexInt != sex) {
            isRn = true;
        } else if (!address.equals(districtStr)) {
            isRn = true;
        } else if (!uInfo.getSchools().equals(schoolStr)) {
            isRn = true;
        } else if (!grade.equals(gradeStr)) {
            isRn = true;
        } else if (!uInfo.getTel().equals(telStr)) {
            isRn = true;
        } else {
            isRn = false;
        }
        return isRn;
    }


    /**
     * 保存用户信息
     * <p>
     * tag  修改所在地区
     */
    private void saveUserInfo(String tag) {

        showDialog("请稍候");
        try {
            JSONObject subParams = new JSONObject();
            if ("modify_district".equals(tag)) {//修改所在地区
                subParams.put("province", province);
                subParams.put("city", city);
            }

            UploadManager2.upload(AppConfig.GO_URL + "parents/upuserinfos", RequestParamUtils.getMapParam(subParams), null,
                    new StringCallback() {
                        @Override
                        public void onBefore(Request request) {
                            super.onBefore(request);
                            // ToastUtils.show("onBefore");
                        }


                        @Override
                        public void onAfter() {
                            super.onAfter();
                            // ToastUtils.show("onAfter");

                        }

                        @Override
                        public void onResponse(String response) {
                            closeDialog();
                            int code = JsonUtil.getInt(response, "Code", -1);
                            String msg = JsonUtil.getString(response, "Msg", "");
                            final String responseStr = JsonUtil.getString(response, "Data", "");
                            if (code == 0) {
                                UserInfoModel uInfo = new Gson().fromJson(responseStr, UserInfoModel.class);
                                DBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);
                                sendUserInfoCastReceiver();
                            } else {
                                ToastUtils.show(msg);
                            }

                        }

                        @Override
                        public void onError(Call call, Exception e) {
                            closeDialog();
                            String errorMsg = "";
                            if (e != null && !TextUtils.isEmpty(e.getMessage())) {
                                errorMsg = e.getMessage();
                            } else {
                                errorMsg = e.getClass().getSimpleName();
                            }
                            if (AppConfig.IS_DEBUG) {
                                ToastUtils.show("onError:" + errorMsg);
                            } else {
                                ToastUtils.show("网络异常");
                            }

                        }
                    }, true, 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }


       /* String name = nickEt.getText().toString().trim();
        String schools = school_tv_modified.getText().toString().trim();
        String genderStr = sexTv.getText().toString().trim();
        showDialog("请稍候");
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
            subParams.put("gradeid", gradeid);
            subParams.put("schools", schools);
            subParams.put("sex", "男".equals(genderStr) ? 1 : 2);
            subParams.put("province", province);
            subParams.put("city", city);
            UploadManager.upload(AppConfig.GO_URL + "parents/upuserinfos", RequestParamUtils.getParam(subParams), files,
                    this, true, 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

    }





    public void sendUserInfoCastReceiver() {
        Intent intent = new Intent();
        intent.setAction("com.action.updateuser");
        sendBroadcast(intent);
    }

}
