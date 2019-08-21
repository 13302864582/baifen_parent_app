package com.daxiong.fun.function.homework.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.NetworkImageView.OnImageLoadListener;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.base.ImageLoader;
import com.daxiong.fun.constant.EventConstant;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.function.CameraFrameWithDel;
import com.daxiong.fun.function.homework.model.HomeWorkCheckPointModel;
import com.daxiong.fun.function.homework.model.HomeWorkSinglePoint;
import com.daxiong.fun.function.study.StuHomeWorkCheckDetailActivity;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.ExplainPoint;
import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MediaUtil;
import com.daxiong.fun.util.MediaUtil.ResetImageSourceCallback;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.AnswertextPopupWindow;
import com.daxiong.fun.view.DragImageView;
import com.daxiong.fun.view.DragImageView.OnScaleListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AddPointCommonView extends FrameLayout {

    public static final String TAG = AddPointCommonView.class.getSimpleName();
    public List<ExplainPoint> points2 = new ArrayList<ExplainPoint>();

    interface NextBtnClickLinsener {
        void onNextBtnClick();
    }

    private static final int EXPLAIN_POINT_LIST = 123;
    // AnimationSet set ;
    private static final int CHECK_POINT_LIST = 321;
    ArrayList<HomeWorkSinglePoint> pointlist;
    public CameraFrameWithDel frameDelView;
    int measuredWidth;
    int measuredHeight;
    private BaseActivity mActivity;
    private DragImageView mPicIv;
    private RelativeLayout mPicContainer;
    // private int mContainerWidth;
    // private int mContainerHeight;
    // private List<ExplainPoint> mPointList;
    // private ExplainPoint mPoint;
    // private List<VoiceOrTextPoint> mPointViewList;
    // private List<RightWrongPointView> mRightWrongViewList;
    private boolean isAllowAddPoint;
    public boolean isFankui = false;
    public boolean isFankui2 = false;
    private HashMap<String, FrameLayout> pointMap;
    private boolean picIsLoaded;
    private boolean isMeasure;
    // private int heightPixels;
    // private int widthPixels;

    private boolean isScaled;

    private boolean canRotateable = true;
    private float oldOri;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EXPLAIN_POINT_LIST:
                    @SuppressWarnings("unchecked")
                    ArrayList<HomeWorkSinglePoint> expointlist = (ArrayList<HomeWorkSinglePoint>) msg.obj;
                    showCheckPoint(expointlist);
                    break;
                case CHECK_POINT_LIST:
                    @SuppressWarnings("unchecked")
                    int stuid = msg.arg1;
                    ArrayList<HomeWorkCheckPointModel> checkpointlist = (ArrayList<HomeWorkCheckPointModel>) msg.obj;
                    showRightOrWrongPoint(checkpointlist, stuid);
                    break;
            }
        }

        ;
    };

    private int frameWidthOrHeight;

    private int widthPixels;

    public AddPointCommonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mActivity = (BaseActivity) context;
        setUpViews();
    }

    public AddPointCommonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mActivity = (BaseActivity) context;
        setUpViews();
    }

    public AddPointCommonView(Context context) {
        super(context);
        this.mActivity = (BaseActivity) context;
        setUpViews();
    }

    @SuppressLint({"InflateParams", "ClickableViewAccessibility"})
    private void setUpViews() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.add_point_common_view, null);
        frameWidthOrHeight = DensityUtil.dip2px(mActivity, 40);
        DisplayMetrics outMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        widthPixels = outMetrics.widthPixels;
        // int heightPixels = (int) (((float) outMetrics.heightPixels) / 1.3f);
        // mContainerWidth = outMetrics.widthPixels;// widthPixels;
        // mContainerHeight = (int) (((float) outMetrics.heightPixels) /
        // 1.3f);// heightPixels;
        pointMap = new HashMap<String, FrameLayout>();
        mPicContainer = (RelativeLayout) view.findViewById(R.id.pic_container_add_point);

        mPicIv = (DragImageView) view.findViewById(R.id.pic_iv_add_point);
        mPicIv.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (isAllowAddPoint) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            if (!isScaled) {
                                if (mActivity != null) {
                                    removeFrameDelView();
                                    addCameraFrame(event);
                                }
                            } else {
                                removeFrameDelView();
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (isScaled) {
                                removeFrameDelView();
                            }
                            break;
                        case MotionEvent.ACTION_UP:

                            break;
                        case MotionEvent.ACTION_CANCEL:
                            break;
                    }
                }
                return false;
            }
        });

        mPicIv.setOnScaleListener(new OnScaleListener() {
            @Override
            public void onScale(boolean isScale) {
                int show = isScale ? 2 : 3;
                //showPoints(!isScale);
                showPoints(show);
                isScaled = isScale;
            }
        });

        addView(view);
    }

    /**
     * 作业单页
     *
     * @param homeWorkPageModel
     */
    public void setPagePic(String imgurl, boolean isAllowAddPoint) {
        this.isAllowAddPoint = isAllowAddPoint;
        // mPicIv.getViewTreeObserver().addOnGlobalLayoutListener(new
        // OnGlobalLayoutListener() {
        // @Override
        // public void onGlobalLayout() {
        // int measuredWidth = mPicIv.getMeasuredWidth();
        // int measuredHeight = mPicIv.getMeasuredHeight();
        // if (measuredWidth != 0 && measuredHeight != 0) {
        // // mContainerWidth = measuredWidth;
        // // mContainerHeight = measuredHeight;
        // picIsLoaded = true;
        // mPicIv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        // // mPicIv.setScreenSize(measuredWidth, measuredHeight);
        // }
        // }
        // });

        mPicContainer.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                measuredWidth = mPicContainer.getMeasuredWidth();
                measuredHeight = mPicContainer.getMeasuredHeight();
                if (measuredWidth != 0 && measuredHeight != 0) {
                    mPicIv.setScreenSize(measuredWidth, measuredHeight);
                    isMeasure = true;
                    mPicContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    LogUtils.d(TAG, "yh setPagePic() w = " + measuredWidth + ", h = " + measuredHeight);
                }
            }
        });

        ImageLoader.getInstance().loadImage(imgurl, mPicIv, R.drawable.loading, new OnImageLoadListener() {

            @Override
            public void onSuccess(ImageContainer response) {
                picIsLoaded = true;
            }

            @Override
            public void onFailed(VolleyError error) {
            }
        });

    }

    /**
     * 单个检查点
     *
     * @param pointModel
     */
    public void setCheckPointImg(final HomeWorkCheckPointModel pointModel, boolean isAllowAddPoint) {
        this.isAllowAddPoint = isAllowAddPoint;
        String imgpath = "";
        imgpath = pointModel.getImgpath();
        mPicContainer.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                measuredWidth = mPicContainer.getMeasuredWidth();
                measuredHeight = mPicContainer.getMeasuredHeight();
                if (measuredWidth != 0 && measuredHeight != 0) {
                    mPicIv.setScreenSize(measuredWidth, measuredHeight);
                    LogUtils.d(TAG, "yh setCheckPointImg() w = " + measuredWidth + ", h = " + measuredHeight);
                    int isright = pointModel.getIsright();
                    if (isright == GlobalContant.RIGHT_HOMEWORK) {
                        setRightWrongPoint(pointModel, false, 0);
                    }
                    isMeasure = true;
                    mPicContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
        ImageLoader.getInstance().loadImage(imgpath, mPicIv, R.drawable.loading, new OnImageLoadListener() {

            @Override
            public void onSuccess(ImageContainer response) {
                picIsLoaded = true;
            }

            @Override
            public void onFailed(VolleyError error) {
            }
        });

    }

    /**
     * 追问专用
     *
     * @param checkPointModel
     */
    public void setCheckPointImgAndShowExPoint(final HomeWorkCheckPointModel checkPointModel) {
        this.isAllowAddPoint = true;
        String imgpath = "";


        imgpath = checkPointModel.getImgpath();

        mPicContainer.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                measuredWidth = mPicContainer.getMeasuredWidth();
                measuredHeight = mPicContainer.getMeasuredHeight();
                if (measuredWidth != 0 && measuredHeight != 0) {
                    mPicIv.setScreenSize(measuredWidth, measuredHeight);
                    LogUtils.d(TAG,
                            "yh setCheckPointImgAndShowExPoint() w = " + measuredWidth + ", h = " + measuredHeight);
                    pointlist = checkPointModel.getExplianlist();
                    if (pointlist != null) {
                        showCheckPoint(pointlist);
                    }
                    isMeasure = true;
                    mPicContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

        ImageLoader.getInstance().loadImage(imgpath, mPicIv, R.drawable.loading, new OnImageLoadListener() {

            @Override
            public void onSuccess(ImageContainer response) {
                picIsLoaded = true;
            }

            @Override
            public void onFailed(VolleyError error) {

            }
        });
    }

    protected void setRightWrongPoint(final HomeWorkCheckPointModel checkPointModel, boolean isShowNext, final int stuid) {
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
                android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);

        int isright = checkPointModel.getIsright();
        if (isright == GlobalContant.RIGHT_HOMEWORK) {
            isShowNext = false;
        }
        String coordinateStr = checkPointModel.getCoordinate();

        if (pointMap.containsKey(coordinateStr)) {
            // pointMap.get(coordinate)
            return;
        }
        String[] coordinate = coordinateStr.split(",");
        float pointXPer = Float.parseFloat(coordinate[0]);
        float pointYPer = Float.parseFloat(coordinate[1]);

        int mContainerWidth = mPicIv.getLayoutParams().width;
        int mContainerHeight = mPicIv.getLayoutParams().height;

        int currentX = (int) (mContainerWidth * pointXPer);
        int currentY = (int) (mContainerHeight * pointYPer);

//		 relativeParams.leftMargin = currentX < 0 ? 0 : currentX;
//		 relativeParams.topMargin = currentY < 0 ? 0 : currentY;

        relativeParams.leftMargin = currentX + mPicIv.getLeft();
        relativeParams.topMargin = currentY + mPicIv.getTop();

        if (relativeParams.leftMargin < 0) {
            relativeParams.leftMargin = 0;
        }
        if (relativeParams.topMargin < 0) {
            relativeParams.topMargin = 0;
        }
        if (relativeParams.leftMargin > (widthPixels - frameWidthOrHeight)) {
            relativeParams.leftMargin = widthPixels - frameWidthOrHeight;
        }

        RightWrongPointView iconSer = new RightWrongPointView(mActivity, isright,
                checkPointModel.getShowcomplainttype());
        iconSer.setTag(isright);
        iconSer.setLayoutParams(relativeParams);
        mPicContainer.addView(iconSer);
        pointMap.put(coordinateStr, iconSer);

        if (isShowNext) {

            iconSer.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View nextBtn) {
                    if (!isFankui) {
                        Bundle data = new Bundle();

                        if (mActivity instanceof StuHomeWorkCheckDetailActivity) {
                            if (((StuHomeWorkCheckDetailActivity) mActivity).checkingFlag) {
                                checkPointModel.setAllowAppendAsk(true);
                            }
                        }
                        data.putSerializable(HomeWorkCheckPointModel.TAG, checkPointModel);
                        data.putBoolean("isFankui2", isFankui2);
                        data.putInt("stuid", stuid);
                        IntentManager.goToStuSingleCheckActivity(mActivity, data, false);
                    } else {
                        ToastUtils.show("请到错题讲解详细页面 “反馈”");
                    }
                }
            });

        } else {
            if (checkPointModel.getShowcomplainttype() != 1) {
                iconSer.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (isFankui) {
                            if (((RightWrongPointView) v).getType() == 1) {
                                MyApplication.checklist.remove((Integer) checkPointModel.getId());
                                ((RightWrongPointView) v).setType(checkPointModel.getShowcomplainttype());
                            } else {
                                ((RightWrongPointView) v).setType(1);
                                MyApplication.checklist.add(checkPointModel.getId());
                            }
                        }

                    }
                });
            }
        }
    }

    public void showCheckPoint(final ArrayList<HomeWorkSinglePoint> expointlist) {
        if (expointlist == null) {
            return;
        }
        if (picIsLoaded && isMeasure) {
            for (int i = 0; i < expointlist.size(); i++) {
                HomeWorkSinglePoint pointModel = expointlist.get(i);
                addVoiceOrTextPoint(pointModel);
            }
        } else {
            Message msg = Message.obtain();
            msg.what = EXPLAIN_POINT_LIST;
            msg.obj = expointlist;
            mHandler.sendMessageDelayed(msg, 1000);
        }
    }

    public void removeExPoint(View v, String coordinate) {
        mPicContainer.removeView(v);
        if (pointMap.containsKey(coordinate)) {
            pointMap.remove(coordinate);
        }
    }

    public VoiceOrTextPoint addVoiceOrTextPoint(final HomeWorkSinglePoint pointModel) {
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
                android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
        int explaintype = pointModel.getExplaintype();
        int roleid = pointModel.getRoleid();
        String coordinate = pointModel.getCoordinate();

        int exseqid = pointModel.getExseqid();

        String[] coordinates = coordinate.split(",");
        float pointXPer = Float.parseFloat(coordinates[0]);
        float pointYPer = Float.parseFloat(coordinates[1]);

        int mContainerWidth = mPicIv.getLayoutParams().width;
        int mContainerHeight = mPicIv.getLayoutParams().height;

        int currentX = (int) (mContainerWidth * pointXPer);
        int currentY = (int) (mContainerHeight * pointYPer);

        // relativeParams.leftMargin = currentX < 0 ? 0 : currentX;
        // relativeParams.topMargin = currentY < 0 ? 0 : currentY;
        ExplainPoint point = new ExplainPoint();
        point.setX(currentX);
        point.setY(currentY);
        if (measuredWidth > 0 && measuredHeight > 0) {
            int i = measuredWidth - mContainerWidth;
            int i1 = measuredHeight - mContainerHeight;
            i = i < 0 ? 0 : i;
            i1 = i1 < 0 ? 0 : i1;
            relativeParams.leftMargin = currentX + i / 2;
            relativeParams.topMargin = currentY + i1 / 2;
        } else {

            relativeParams.leftMargin = currentX + mPicIv.getLeft();
            relativeParams.topMargin = currentY + mPicIv.getTop();
        }
        LogUtils.d(TAG, "yh addVoiceOrTextPoint() left = " + mPicIv.getLeft() + ", top = " + mPicIv.getTop() + ", Right = " + mPicIv.getRight() + ", Bottom = " + mPicIv.getBottom() + ", x = " + mPicIv.getX() + ", y = " + mPicIv.getY());

     
        if (relativeParams.leftMargin < 0) {
            relativeParams.leftMargin = 0;
        }
        if (relativeParams.topMargin < 0) {
            relativeParams.topMargin = 0;
        }
        if (relativeParams.leftMargin > (widthPixels - frameWidthOrHeight)) {
            relativeParams.leftMargin = widthPixels - frameWidthOrHeight;
        }

        VoiceOrTextPoint pointViewContainer = new VoiceOrTextPoint(mActivity, roleid, pointModel.getSubtype());

        pointViewContainer.setLayoutParams(relativeParams);
        if (pointMap.containsKey(coordinate)) {
            return pointViewContainer;
        }

        mPicContainer.addView(pointViewContainer);
        pointMap.put(coordinate, pointViewContainer);
        if (exseqid == 0) {
            pointViewContainer.getIcSerView().setVisibility(View.GONE);
        } else {
            pointViewContainer.getIcSerView().setText(exseqid + "");
        }
        // mPointViewList.add(pointViewContainer);

        ImageView pointViewIc = pointViewContainer.getIcView();

        if (explaintype == GlobalContant.ANSWER_AUDIO) {// 声音
            if (roleid == GlobalContant.ROLE_ID_STUDENT | roleid == GlobalContant.ROLE_ID_PARENTS) {// 学生红色
                pointViewIc.setImageResource(R.drawable.me_v3);
            } else {// 老师绿色
                pointViewIc.setImageResource(R.drawable.v3);
            }
            pointViewContainer.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    MobclickAgent.onEvent(mActivity, EventConstant.CUSTOM_EVENT_PLAY_AUDIO);
                    VoiceOrTextPoint pointViewContainer = (VoiceOrTextPoint) view;
                    int roleid = pointModel.getRoleid();
                    if (TextUtils.isEmpty(pointModel.getSndpath())) {
                        ToastUtils.show(R.string.text_audio_is_playing_please_waiting);
                        return;
                    }
                    final ImageView pointViewIc = pointViewContainer.getIcView();

                    if (roleid == GlobalContant.ROLE_ID_STUDENT | roleid == GlobalContant.ROLE_ID_PARENTS) {// 学生红色
                        pointViewIc.setImageResource(R.anim.play_voice_anim_stu);
                        pointViewIc.setTag(R.drawable.me_v3);
                    } else {// 老师绿色
                        pointViewIc.setTag(R.drawable.v3);
                        pointViewIc.setImageResource(R.anim.play_voice_anim_tec);
                    }

                    AnimationDrawable mAnimationDrawable = (AnimationDrawable) pointViewIc.getDrawable();
                    MyApplication.animationDrawables.add(mAnimationDrawable);
                    MyApplication.anmimationPlayViews.add(pointViewIc);
                    /*
					 * WeLearnMediaUtil.getInstance(false) .stopPlay();
					 */
                    MediaUtil.getInstance(false).playVoice(false, pointModel.getSndpath(), mAnimationDrawable,
                            new ResetImageSourceCallback() {

                                @Override
                                public void reset() {
                                    int roleid = pointModel.getRoleid();
                                    if (roleid == GlobalContant.ROLE_ID_STUDENT | roleid == GlobalContant.ROLE_ID_PARENTS) {// 学生红色
                                        pointViewIc.setImageResource(R.drawable.me_v3);
                                    } else {// 老师绿色
                                        pointViewIc.setImageResource(R.drawable.v3);
                                    }
                                }

                                @Override
                                public void playAnimation() {
                                }

                                @Override
                                public void beforePlay() {
                                    MediaUtil.getInstance(false).resetAnimationPlayAtHomeWork(pointViewIc);
                                }
                            }, null);
                }
            });
        } else if (explaintype == GlobalContant.ANSWER_TEXT) {
            if (roleid == GlobalContant.ROLE_ID_STUDENT | roleid == GlobalContant.ROLE_ID_PARENTS) {// 学生红色
                pointViewIc.setImageResource(R.drawable.text_ic_stu_selector);
            } else {// 老师绿色
                pointViewIc.setImageResource(R.drawable.text_ic_tec_selector);
            }
            pointViewContainer.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    // Bundle bundle = new Bundle();
                    // bundle.putString(PayAnswerTextAnswerActivity.ANSWER_TEXT,
                    // pointModel.getText());
                    // IntentManager.goToAnswertextView(mActivity, bundle);
                    GlobalVariable.answertextPopupWindow = new AnswertextPopupWindow(mActivity, pointModel.getText());
                }
            });
        }
        return pointViewContainer;
    }

    public void showRightOrWrongPoint(ArrayList<HomeWorkCheckPointModel> checkpointlist, int stuid) {
        if (checkpointlist == null) {
            return;
        }
        if (picIsLoaded && isMeasure) {
            for (int i = 0; i < checkpointlist.size(); i++) {
                HomeWorkCheckPointModel pointModel = checkpointlist.get(i);
                setRightWrongPoint(pointModel, true, stuid);
            }
        } else {
            Message msg = Message.obtain();
            msg.what = CHECK_POINT_LIST;
            msg.obj = checkpointlist;
            msg.arg1 = stuid;
            mHandler.sendMessageDelayed(msg, 1000);
        }
    }

    /**
     * 去除打点框
     */
    public void removeFrameDelView() {
        if (frameDelView != null && mPicContainer != null) {
            mPicContainer.removeView(frameDelView);
            frameDelView = null;
        }
        if (mActivity != null) {
            mActivity.hideAddPointBottomContainer();
        }
    }

    private boolean addCameraFrame(MotionEvent event) {
        if ((!picIsLoaded) || (!isMeasure)) {
            return false;
        }
        float x = event.getX();// 触摸相对于image的x坐标
        float y = event.getY();// 触摸相对于image的y坐标

        int mContainerWidth = mPicIv.getLayoutParams().width;
        int mContainerHeight = mPicIv.getLayoutParams().height;

        int currentX = (int) x - DensityUtil.dip2px(mActivity, 14);// 方框左上角
        int currentY = (int) y - DensityUtil.dip2px(mActivity, 15);// 方框左上角
        if (currentX > (mContainerWidth - frameWidthOrHeight)) {
            currentX = mContainerWidth - frameWidthOrHeight;
        }
        if (currentY > (mContainerHeight - frameWidthOrHeight)) {
            currentY = mContainerHeight - frameWidthOrHeight;
        }
        if (currentX < 0) {
            currentX = 0;
        }
        if (currentY < 0) {
            currentY = 0;
        }
        int left = mPicIv.getLeft();
        int top = mPicIv.getTop();
        int currentX2 = currentX + left;
        int currentY2 = currentY + top;
        if (isOverlay(currentX, currentY)) {
            return false;
        } else {

            // mPoint = new ExplainPoint();
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            frameDelView = new CameraFrameWithDel(mActivity);

            params.leftMargin = currentX2;
            params.topMargin = currentY2;
            frameDelView.setLayoutParams(params);
            frameDelView.invalidate();

            mPicContainer.addView(frameDelView);
            float coordinateX = (float) currentX / (float) mContainerWidth;
            float coordinateY = (float) (currentY - DensityUtil.dip2px(mActivity, 15)) / (float) mContainerHeight;
            String coordinate = coordinateX + "," + coordinateY;
            int sum = pointlist.size() + mActivity.singlePointList.size() + 1;
            mActivity.showAddPointBottomContainer(coordinate, sum);
            // return coordinate;
            return true;
        }
    }

    public void stopVoice() {
        MediaUtil.getInstance(false).stopPlay();
    }

    public void showPoints(int isShow) {
        //int show = isShow ? View.VISIBLE : View.GONE;
        Set<String> keySet = pointMap.keySet();

        for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext(); ) {
            String key = (String) iterator.next();
            FrameLayout frameLayout = pointMap.get(key);
            if (isShow == 1) {
                if ((Integer) frameLayout.getTag() == 1) {//对的
                    frameLayout.setVisibility(View.GONE);
                } else if ((Integer) frameLayout.getTag() == 0) {
                    frameLayout.setVisibility(View.VISIBLE);
                }
            } else if (isShow == 2) {
                frameLayout.setVisibility(View.GONE);
            } else if (isShow == 3) {
                frameLayout.setVisibility(View.VISIBLE);
            }


        }
    }

    public boolean isCanRotateable() {
        return canRotateable;
    }

    public void setCanRotateable(boolean canRotateable) {
        this.canRotateable = canRotateable;
    }

    public void setOrientation(int ori) {
        if (canRotateable) {

            final int de = ori;

            ori = Math.abs(360 - ori);

            if (oldOri == 360 && ori == 90) {
                oldOri = 0;
            }

            if (oldOri == 90 && ori == 360) {
                ori = 0;
            }

            if (oldOri == 0 && ori == 270) {
                oldOri = 360;
            }

            if (oldOri == ori || (oldOri == 360 && ori == 0) || (oldOri == 0 && ori == 360)) {
                oldOri = ori;
                return;
            }

            LogUtils.d(TAG, "yh old=" + oldOri + ", new=" + ori);
            // RotateAnimation animation1 = new RotateAnimation(oldOri, ori,
            // Animation.RELATIVE_TO_SELF, 0.5f,
            // Animation.RELATIVE_TO_SELF, 0.5f);
            // animation1.setDuration(270);
            // animation1.setFillAfter(true);

            pointMap.keySet();
            for (Iterator<String> iterator = pointMap.keySet().iterator(); iterator.hasNext(); ) {
                final FrameLayout fl = pointMap.get((String) iterator.next());
                // ObjectAnimator.ofFloat(fl, "rotation", oldOri,
                // ori).setDuration(270).start();

                AnimatorSet set = new AnimatorSet();
                ObjectAnimator anim3 = ObjectAnimator.ofFloat(fl, "rotation", oldOri, ori);
                anim3.setDuration(270);

                set.play(anim3);
                set.start();
            }
            oldOri = ori;
        }
    }

    /**
     * 判断打点是否重叠
     */
    @SuppressWarnings("unused")
    private boolean isOverlay(float currentX, float currentY) {
        int mX1 = DensityUtil.dip2px(mActivity, 20);
        int mX2 = DensityUtil.dip2px(mActivity, 37);
        int mX4 = DensityUtil.dip2px(mActivity, 28);

        for (ExplainPoint explainPoint : points2) {
            float x = explainPoint.getX();
            float y = explainPoint.getY();
            float absX = Math.abs(currentX - x);
            float absY = Math.abs(currentY - y);
            boolean flag1 = false;
            boolean flag2 = false;
            if (currentX < x) {
                if (mX1 > absX) {
                    flag1 = true;
                }
            } else {
                if (mX2 > absX) {
                    flag1 = true;
                }
            }
            if (currentY < y) {
                if (mX1 > absY) {
                    flag2 = true;
                }
            } else {
                if (mX2 > absY) {
                    flag2 = true;
                }
            }
            if (flag1 && flag2) {
                return true;
            }
        }
        // for (ExplainPoint explainPoint : points) {
        // float x2 = explainPoint.getX();
        // float y2 = explainPoint.getY();
        // if (mX4 > Math.abs(currentX - x2) && mX4 > Math.abs(currentY - y2)) {
        // return true;
        // }
        // }

        return false;
    }
}
