
package com.daxiong.fun.function;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.android.volley.toolbox.NetworkImageView;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.base.ImageLoader;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.function.question.OneQuestionMoreAnswersDetailActivity;
import com.daxiong.fun.function.question.model.AnswerListItemModel;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.view.AbstractAnswerListItemView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 此类的描述： 作业大厅显示的内容视图
 * 
 * @author: sky @最后修改人： sky
 * @最后修改日期:2015年8月3日 下午4:01:17
 */
public class QAHallListItemView extends AbstractAnswerListItemView {

    private long question_id;

    private Activity mActivity;

    private static final String TAG = QAHallListItemView.class.getSimpleName();

    public static final String EXTRA_POSITION = "position";

    public static final String EXTRA_QUESTION_ID = "question_id";

    public static final String EXTRA_ISQPAD = "iaqpad";

    public static final String EXTRA_TAG = "go_tag";

    private int stuId;

    private AnswerListItemModel mItemEntity;

    private int goTag = -1;

    public void setGoTag(int goTag) {
        this.goTag = goTag;
    }

    public QAHallListItemView(Activity context) {
        super(context);
        this.mActivity = context;
    }

    /**
     * 此方法描述的是： * 显示作业大厅listview里面的数据
     * 
     * @最后修改日期:2015年8月3日 下午3:53:12 showData
     * @param item
     * @param isScroll void
     */
    public void showData(AnswerListItemModel item, boolean isScroll) {
        mItemEntity = item;
        collectionContainer.setVisibility(View.VISIBLE);
        praisecnt = item.getPraisecnt();
        collectionCountTv.setText(praisecnt + "");// 收藏总数
        int praise = item.getPraise();
        if (praise == 0) {// 0是未收藏,1是已经收藏
            isCollected = false;
        } else {
            isCollected = true;
        }
        if (isCollected) {// 已经收藏是实心
            collectionIconIv.setImageResource(R.drawable.ic_up_and_shoucang_pressed);
        } else {// 未收藏是空心
            collectionIconIv.setImageResource(R.drawable.ic_up_and_shoucang);
        }

        question_id = mItemEntity.getQid();
        stuId = item.getStudid();
        ImageLoader.getInstance().loadImage(item.getAvatar(), mAnswerAvatar,
                R.drawable.ic_default_avatar, avatarSize, avatarSize / 10);
        mAnswerNick.setText(item.getStudname());
        // 显示年级和科目
        mColleage.setText(item.getGrade() + " " + item.getSubject());

        mStatus.setVisibility(View.GONE);
        long answertime = item.getAnswertime();
        if (answertime != 0) {
            long dTime = answertime - item.getGrabtime(); // homeWorkModel.getDatatime();
            int answerTime = (int)(dTime / 60000);
            if (answerTime < 3) {
                answerTime = 3;
            }
            mAnswerViewCnt.setVisibility(View.VISIBLE);
            mAnswerViewCnt.setText(mActivity.getString(R.string.answer_time_text, answerTime + ""));
        } else {
            mAnswerViewCnt.setVisibility(View.GONE);
        }

        mGradeSubject.setVisibility(View.INVISIBLE);

        // String grabTimeStr = item.getGrabtime();
        // String grabTime = DateUtil.getDisplayTime(DateUtil.parseString2Date(
        // grabTimeStr, "yyyy-MM-dd HH:mm:ss"));
        SimpleDateFormat format = new SimpleDateFormat("M月d日 HH:mm:ss");
        String askTime = format.format(new Date(item.getDatatime()));
        mPublishTime.setText(askTime);

        disPlayQuestion(item.getImgpath(), mQuestionPic);

        mAnswerPicContainer.setVisibility(View.GONE);
    }

    private void disPlayQuestion(String url, NetworkImageView imageview) {
        ImageLoader.getInstance().ajaxQuestionPic(url, imageview);
        imageview.setTag(0);
    }

    @Override
    protected void onClickCallback(View v) {
        switch (v.getId()) {
            case R.id.answerer_avatar:// 点击头像
                if (stuId != 0) {
                    IntentManager.gotoPersonalPage(mActivity, stuId, GlobalContant.ROLE_ID_STUDENT);
                }

                break;
            case R.id.collection_container_linearlayout_answerlistitem:// 收藏
                JSONObject json = new JSONObject();
                try {
                    json.put("taskid", question_id);
                    json.put("tasktype", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                BaseActivity activity = null;
                if (mActivity instanceof BaseActivity) {
                    activity = (BaseActivity)mActivity;
                } else {
                    activity = null;
                }
                OkHttpHelper.post(mActivity, "common", "collect",  json, new HttpListener() {
					
					@Override
					public void onSuccess(int code, String dataJson, String errMsg) {
                        isCollected = !isCollected;
                        if (isCollected) {// 已经收藏是实心
                            collectionIconIv
                                    .setImageResource(R.drawable.ic_up_and_shoucang_pressed);
                            praisecnt += 1;
                            mItemEntity.setPraise(1);// 0是未收藏,1是已经收藏
                        } else {// 未收藏是空心
                            collectionIconIv.setImageResource(R.drawable.ic_up_and_shoucang);
                            praisecnt -= 1;
                            mItemEntity.setPraise(0);// 0是未收藏,1是已经收藏
                        }
                        mItemEntity.setPraisecnt(praisecnt);
                        collectionCountTv.setText("" + praisecnt);
						
					}
					
					@Override
					public void onFail(int HttpCode,String errMsg) {
						
						
					}
				});

                break;

            case R.id.question_pic:// 点击item的图片
                MobclickAgent.onEvent(mActivity, "QaHallDetail");
                Bundle data = new Bundle();
                data.putLong(EXTRA_QUESTION_ID, question_id);
                data.putBoolean(EXTRA_ISQPAD, false);
                data.putInt(AnswerListItemView.EXTRA_POSITION, 0);
                data.putInt(EXTRA_TAG, goTag);
                IntentManager.goToAnswerDetail(mActivity,
                        OneQuestionMoreAnswersDetailActivity.class, data);
                break;
        }
    }
}
