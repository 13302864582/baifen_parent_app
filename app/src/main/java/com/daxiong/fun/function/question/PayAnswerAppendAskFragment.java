
package com.daxiong.fun.function.question;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.NetworkImageView.OnImageLoadListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.base.ImageLoader;
import com.daxiong.fun.manager.UploadManager;
import com.daxiong.fun.manager.UploadManager.OnUploadListener;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.function.CameraChoiceIconWithSer;
import com.daxiong.fun.http.RequestParamUtils;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.AnswerDetail;
import com.daxiong.fun.model.AnswerSound;
import com.daxiong.fun.model.ExplainPoint;
import com.daxiong.fun.model.UploadResult;
import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MediaUtil;
import com.daxiong.fun.util.MediaUtil.ResetImageSourceCallback;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.AnswertextPopupWindow;
import com.daxiong.fun.view.CropCircleTransformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class PayAnswerAppendAskFragment extends PayAnswerPhotoViewFragment
        implements OnClickListener, OnUploadListener {

    public static final String JSON_DATA = "json_data";

    public static final String ANSWER_INDEX = "answer_index";

    public static final String APPEND_ANSWER = "append_answer";

    private AnswertextPopupWindow answertextPopupWindow;

    private static final String TAG = PayAnswerAppendAskFragment.class.getSimpleName();

    private ImageView mUserAvatar;

    private TextView mUsername;

    private TextView mSchoolName;
    private LinearLayout ll_append_user_avatar;

    private AnswerDetail mAnswerDetail;

    private AnimationDrawable mAnimationDrawable;

    // private AppendAnswerController mAppendAnswerController;
    private List<CameraChoiceIconWithSer> answerIcsList = new ArrayList<CameraChoiceIconWithSer>();

    private int avatarSize;

    private AnswerDetail getQuestionDetailGson() {
        String jsonStr = mActivity.getIntent().getStringExtra(JSON_DATA);
        int index = mActivity.getIntent().getIntExtra(ANSWER_INDEX, 1);
        JSONArray answerArray = JsonUtil.getJSONArray(jsonStr, "answer", null);
        // Log.i (TAG, answerArray.toString());
        Gson gson = new Gson();
        List<AnswerDetail> answerList = gson.fromJson(answerArray.toString(),
                new TypeToken<ArrayList<AnswerDetail>>() {
                }.getType());
        return answerList.get(index - 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mUserAvatar = (ImageView)view.findViewById(R.id.append_user_avatar);
        ll_append_user_avatar = (LinearLayout)view.findViewById(R.id.ll_append_user_avatar);
        mUserAvatar = (ImageView)view.findViewById(R.id.append_user_avatar);
        avatarSize = getResources().getDimensionPixelSize(R.dimen.append_user_avatar_size);
        mUsername = (TextView)view.findViewById(R.id.append_user_name);
       
        mSchoolName = (TextView)view.findViewById(R.id.append_user_colleage);
        TextView back = (TextView)view.findViewById(R.id.photo_view_nav_btn_back);
        back.setText(R.string.text_nav_cancel);
        mUserAvatar.setVisibility(View.GONE);
        ll_append_user_avatar.setVisibility(View.GONE);
        tips_tec_single.setVisibility(View.VISIBLE);
        mUserAvatar.setOnClickListener(this);
        mUsername.setOnClickListener(this);
        mSchoolName.setOnClickListener(this);
        showData();
        return view;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.append_user_avatar:
            case R.id.append_user_name:
            case R.id.append_user_colleage:
                IntentManager.gotoPersonalPage(mActivity, mAnswerDetail.getGrabuserid(),
                        mAnswerDetail.getRoleid());
                break;
        }
    }

    @Override
    protected void showBottom() {
       mRotateContainer.setVisibility(View.GONE);
        mUserinfoContainer.setVisibility(View.VISIBLE);
    }

    @Override
    protected void hideBottom() {
        mRotateContainer.setVisibility(View.GONE);
        mUserinfoContainer.setVisibility(View.GONE);
    }

    private void showData() {

        mAnswerDetail = getQuestionDetailGson();

        showAnswerPic(mAnswerDetail);

        showUserInfo(mAnswerDetail);
    }

    private void showAnswerPic(AnswerDetail model) {
        // ImageLoader.getInstance().displayImage(model.getA_imgurl(),
        // mPhotoImage, new SimpleImageLoadingListener() {
        //
        // @Override
        // public void onLoadingStarted(String imageUri, View view) {
        // }
        //
        // @Override
        // public void onLoadingComplete(String imageUri, View view, Bitmap
        // loadedImage) {
        //
        // }
        //
        // }, null);

        ImageLoader.getInstance().loadImage(model.getA_imgurl(), mPhotoImage, 0,
                new OnImageLoadListener() {

                    @Override
                    public void onSuccess(ImageContainer response) {
                        if (response != null) {
                            Bitmap bitmap = response.getBitmap();
                            if (bitmap != null) {
                                showAnswer(bitmap);
                            }
                        }

                    }

                    @Override
                    public void onFailed(VolleyError error) {

                    }
                });
    }

    private void showUserInfo(AnswerDetail model) {
//        ImageLoader.getInstance().loadImageWithDefaultAvatar(model.getT_avatar(), mUserAvatar,
//                avatarSize, avatarSize / 10);

        Glide.with(PayAnswerAppendAskFragment.this).load(model.getT_avatar())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(getActivity()))
                .placeholder(R.drawable.teacher_img).into(mUserAvatar);

        mUsername.setText(model.getGrabuser());
    }

    private int currentWidth;

    private int currentHeight;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showAnswer(Bitmap loadedImage) {
        mPhotoImage.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                currentWidth = mPhotoImage.getWidth();
                currentHeight = mPhotoImage.getHeight();

                final int srcWidth = mAnswerDetail.getWidth();
                final int srcHeight = mAnswerDetail.getHeight();

                showAnswerInconInPic(currentWidth, currentHeight, srcWidth, srcHeight);
                mPhotoImage.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }

            private void showAnswerInconInPic(int currentWidth, int currentHeight, int srcWidth,
                    int srcHeight) {
                List<AnswerSound> soundList = mAnswerDetail.getAnswer_snd();
                int count = 1;
                int measuredHeight = mAnswerPicContainer.getMeasuredHeight();
                int measuredWidth =mAnswerPicContainer.getMeasuredWidth();
                int meWidth=   (measuredWidth-currentWidth)/2;
                int meHeight =   (measuredHeight-currentHeight)/2;
                for (final AnswerSound as : soundList) {
                    int type = as.getType();
                    RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                            android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
                            android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
                    String[] coordinate = as.getCoordinate().split(",");
                    float srcX = Float.parseFloat(coordinate[0]);
                    float srcY = Float.parseFloat(coordinate[1]);
                    float widthRadio = srcX / (float)srcWidth;
                    float heightRadio = srcY / (float)srcHeight;

                    float currentX = widthRadio * currentWidth;
                    float currentY = heightRadio * currentHeight;
                    currentX+=meWidth;
                    currentY+=meHeight;
                    relativeParams.leftMargin = (int)(currentX < 0 ? 0 : currentX);
                    relativeParams.topMargin = (int)(currentY < 0 ? 0 : currentY);
                    int frameWidthOrHeight = DensityUtil.dip2px(mActivity, 40);
                    if (relativeParams.leftMargin > currentWidth+meWidth) {
                        relativeParams.leftMargin = currentWidth+meWidth - frameWidthOrHeight;
                    }
                    if (relativeParams.topMargin > currentHeight+meHeight) {
                        relativeParams.topMargin = currentHeight+meHeight - frameWidthOrHeight;
                    }
                    ExplainPoint point = new ExplainPoint();
                    point.setX(relativeParams.leftMargin);
                    point.setY(relativeParams.topMargin);
                    points2.add(point);
                    CameraChoiceIconWithSer iconSer = new CameraChoiceIconWithSer(mActivity,
                            as.getRole(), as.getSubtype());
                    iconSer.setLayoutParams(relativeParams);
                    mAnswerPicContainer.addView(iconSer);
                    iconSer.getIcSerView().setText(count++ + "");
                    answerIcsList.add(iconSer);

                    final ImageView mGrabItemIcView = iconSer.getIcView();
                    if (type == GlobalContant.ANSWER_AUDIO) {// 声音
                        mGrabItemIcView.setImageResource(R.drawable.ic_play2);
                        iconSer.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                if (TextUtils.isEmpty(as.getQ_sndurl())) {
                                    return;
                                }
                                mGrabItemIcView.setImageResource(R.drawable.play_animation);
                                mAnimationDrawable = (AnimationDrawable)mGrabItemIcView
                                        .getDrawable();
                                MyApplication.animationDrawables.add(mAnimationDrawable);
                                MyApplication.anmimationPlayViews.add(mGrabItemIcView);
                                MediaUtil.getInstance(false).stopPlay();
                                MediaUtil.getInstance(false).playVoice(false, as.getQ_sndurl(),
                                        mAnimationDrawable, new ResetImageSourceCallback() {

                                    @Override
                                    public void reset() {
                                        mGrabItemIcView.setImageResource(R.drawable.ic_play2);
                                    }

                                    @Override
                                    public void playAnimation() {
                                    }

                                    @Override
                                    public void beforePlay() {
                                        MediaUtil.getInstance(false)
                                                .resetAnimationPlay(mGrabItemIcView);
                                    }
                                }, null);
                            }
                        });
                    } else if (type == GlobalContant.ANSWER_TEXT) {
                        mGrabItemIcView.setImageResource(R.drawable.ic_text_choic_t);
                        iconSer.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                // Bundle bundle = new Bundle();
                                // bundle.putString(PayAnswerTextAnswerActivity.ANSWER_TEXT,
                                // as.getTextcontent());
                                // IntentManager.goToAnswertextView(mActivity,
                                // bundle);
                                answertextPopupWindow = new AnswertextPopupWindow(mActivity,
                                        as.getTextcontent());
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void sureBtnClick() {
        final LinkedHashSet<ExplainPoint> points = MyApplication.coordinateAnswerIconSet;
        if (points.size() == 0) {
            ToastUtils.show(getString(R.string.text_toast_append_ask));
            return;
        }
        int orignWidth = mAnswerDetail.getWidth();
        int orignHeight = mAnswerDetail.getHeight();
        int mX1 = DensityUtil.dip2px(mActivity, 8);
        for (ExplainPoint p : points) {
            float x = p.getX() - mX1;
            float y = p.getY() - mX1;
            // 原始坐标=原有尺寸/现有尺寸*现有坐标
            float srcX = ((float)orignWidth / (float)currentWidth) * x;
            float srcY = ((float)orignHeight / (float)currentHeight) * y;
            p.setX(srcX);
            p.setY(srcY);
        }

        // WeLearnApi.appendAsk(mAnswerDetail.getAnswer_id(),
        // PayAnswerAppendAskFragment.this);
        showDialog(getResources().getString(R.string.text_toast_append_answer_ing));
        JSONObject submitJson = new JSONObject();

        Map<String, List<File>> files = new HashMap<String, List<File>>();

        List<File> sndFileList = new ArrayList<File>();
        JSONArray pointlist = new JSONArray();
        Iterator<ExplainPoint> iter = MyApplication.coordinateAnswerIconSet.iterator();
        try {
            while (iter.hasNext()) {
                ExplainPoint p = iter.next();
                JSONObject pointObj = new JSONObject();
                String audioPath = p.getAudioPath();
                int type = TextUtils.isEmpty(audioPath) ? 1 : 2;
                pointObj.put("type", type);
                pointObj.put("textcontent", p.getText());
                pointObj.put("coordinate", p.getX() + "," + p.getY());
                pointlist.put(pointObj);
                if (type == 2) {
                    sndFileList.add(new File(audioPath));
                }
            }
            files.put("sndfile", sndFileList);

            submitJson.put("answer_id", mAnswerDetail.getAnswer_id());
            submitJson.put("pointlist", pointlist);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        UploadManager.upload(AppConfig.GO_URL + "parents/questionappend",
                RequestParamUtils.getParam(submitJson), files, this, true, 0);

        LogUtils.i(TAG, points.toString());
    }

    @Override
    protected void goBack() {
        if (answertextPopupWindow != null && answertextPopupWindow.isShowing()) {
            answertextPopupWindow.dismiss();
            return;
        }
        mActivity.finish();
    }

    @Override
    public void onUploadSuccess(UploadResult result, int index) {
        closeDialog();
        if (result.getCode() == 0) {
//        	 final CustomTipDialogWithNoButton customTipDialogWithNoButton = new CustomTipDialogWithNoButton(mActivity);
// 			
//			 customTipDialogWithNoButton.show();
//			 MyApplication.getMainThreadHandler().postDelayed(new Runnable() {
//					
//					@Override
//					public void run() {
//						customTipDialogWithNoButton.dismiss();
//						   mActivity.setResult(Activity.RESULT_OK);
//				            mActivity.finish();
//						
//					}
//				},1000);
			 mActivity.setResult(Activity.RESULT_OK);
	            mActivity.finish();
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
    public void onDestroyView() {
        super.onDestroyView();
        MyApplication.coordinateAnswerIconSet.clear();
        MyApplication.animationDrawables.clear();
        MyApplication.anmimationPlayViews.clear();
    }
}
