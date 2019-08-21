package com.daxiong.fun.function.account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.function.homework.CropImageActivity;
import com.daxiong.fun.function.homework.SelectPicPopupWindow;
import com.daxiong.fun.http.RequestParamUtils;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.manager.UploadManager2;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.okhttp.callback.StringCallback;
import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Sky on 2016/6/29 0029.
 * 用户头像activity
 */
public class UserAvatarActivity extends BaseActivity {

    private static final String TAG = "UserAvatarActivity";


    @Bind(R.id.back_layout)
    RelativeLayout backLayout;
    @Bind(R.id.next_step_btn)
    TextView nextStepBtn;
    @Bind(R.id.next_step_btn2)
    TextView nextStepBtn2;
    @Bind(R.id.next_step_img)
    ImageView nextStepImg;
    @Bind(R.id.next_setp_layout)
    RelativeLayout nextSetpLayout;
    @Bind(R.id.iv_avatar)
    ImageView ivAvatar;

    private static final int REQUEST_CODE_GET_HEAD_IMAGE_FROM_SYS = 101;
    private String mPath = "";
    private String avatar,tag;
    float scaleWidth;
    float scaleHeight;
    Bitmap newBitmap;
    Intent getIntent;


    public static final int REQUEST_CODE_CROP = 1012;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.user_avatar_activity);
        ButterKnife.bind(this);
        initView();
        initListener();

    }


    @Override
    public void initView() {
        super.initView();
        backLayout.setVisibility(View.VISIBLE);
        nextSetpLayout.setVisibility(View.VISIBLE);
        nextStepImg.setVisibility(View.VISIBLE);
        nextStepImg.setImageResource(R.drawable.selector_user_more_btn);

        setWelearnTitle("头像");
        getIntent = getIntent();
        avatar = getIntent.getStringExtra("avatar");
        tag = getIntent.getStringExtra("tag");



   /*     Glide.with(this).load(avatar)
                .placeholder(R.drawable.yuantouxiang).diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(DensityUtil.getScreenWidth(),500)
                .into(ivAvatar);*/

        Glide.with(this).load(avatar).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                loadBitmap(resource);
            }
        }); //方法中设置asBitmap可以设置回调类型


    }


    public void loadBitmap(Bitmap bm) {
        int screenWidth = DensityUtil.getScreenWidth();
        int screenHeight = DensityUtil.getScreenHeight();
        int width = bm.getWidth();
        int height = bm.getHeight();
        int newWidth = screenWidth;
        int newHeight = screenWidth * height / width;
        scaleWidth = ((float) newWidth) / width;
        scaleHeight = ((float) newHeight) / height;
        //放大变量
        double scale = 1.1;
        Matrix matrix = new Matrix();
        matrix.postScale((float) (scaleWidth * scale), (float) (scaleHeight * scale));

        newBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        Log.i(TAG, "width图片缩放后宽度：" + newBitmap.getWidth());
        Log.i(TAG, "height图片缩放后高度：" + newBitmap.getHeight());
        ivAvatar.setImageBitmap(newBitmap);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!"change".equals(tag)){
            Bitmap extraBitmap = null;
            if (getIntent != null) {
                byte[] bis = getIntent.getByteArrayExtra("bitmap");
                extraBitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
                if (extraBitmap == null) {
                    Log.e(TAG, "bitmap is NULL !");
                } else {
                    ivAvatar.setImageBitmap(extraBitmap);
                }
            }
        }



    }

    @Override
    public void initListener() {
        super.initListener();
        backLayout.setOnClickListener(this);
        nextSetpLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.next_setp_layout:
                Intent intent = new Intent(this, SelectPicPopupWindow.class);
                startActivityForResult(intent, REQUEST_CODE_GET_HEAD_IMAGE_FROM_SYS);
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

                   /* String saveImagePath = WeLearnFileUtil.getQuestionFileFolder().getAbsolutePath() + File.separator
                            + "publish_" + System.currentTimeMillis() + ".jpg";

                    Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(path)));
                    sendBroadcast(localIntent);

                    localIntent.setClass(this, ClipPictureActivity.class);
                    localIntent.putExtra(PayAnswerImageGridActivity.IMAGE_PATH, path);
                    localIntent.putExtra(CropImageActivity.IMAGE_SAVE_PATH_TAG, saveImagePath);
                    localIntent.putExtra("isFromPhotoList", isFromPhotoList);
                    startActivityForResult(localIntent, PublishHomeWorkActivity.REQUEST_CODE_GET_IMAGE_FROM_CROP);*/


                    path = CropImageActivity.autoFixOrientation(path, isFromPhotoList, this,
                            null);

                    IntentManager.goToCropPhotoActivity(this, path);

                    break;
                case REQUEST_CODE_CROP:// 头像返回
                    if (data != null) {
                         path = data.getStringExtra("path");
                         Bitmap mBitmap= BitmapFactory.decodeFile(path);
                        loadBitmap(mBitmap);
                        doSubmitData(path);
                    }


                    break;

            }
        }

    }


    public void doSubmitData(String path) {
        showDialog("请稍候");
        JSONObject subParams = new JSONObject();
        try {
            Map<String, List<File>> files = null;
            if (!TextUtils.isEmpty(path)) {
                files = new HashMap<String, List<File>>();
                List<File> fList = new ArrayList<File>();
                fList.add(new File(path));
                files.put("picfile", fList);
                subParams.put("avatar", "picfile");
            }
            UploadManager2.upload(AppConfig.GO_URL + "parents/upuserinfos", RequestParamUtils.getMapParam(subParams), files,
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
                                Intent intent = new Intent();
                                intent.putExtra("avatar", uInfo.getAvatar_100());
                                setResult(RESULT_OK, intent);
                                finish();
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
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (newBitmap != null) {
            newBitmap.recycle();
            newBitmap = null;
        }
    }
}
