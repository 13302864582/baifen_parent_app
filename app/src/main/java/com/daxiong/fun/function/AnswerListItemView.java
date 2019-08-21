
package com.daxiong.fun.function;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.android.volley.toolbox.NetworkImageView;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.base.ImageLoader;
import com.daxiong.fun.function.question.OneQuestionMoreAnswersDetailActivity;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.AnswerListItemGsonModel;
import com.daxiong.fun.model.AnswerModel;
import com.daxiong.fun.util.DateUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.view.AbstractAnswerListItemView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 展示list列表数据界面 公共的
 * 
 * @author: sky
 */
public class AnswerListItemView extends AbstractAnswerListItemView {

    private static final String TAG = AnswerListItemView.class.getSimpleName();

    private Context mContext;

    private AnswerModel mLastAnswererModel;

    private long question_id;

    public static final String EXTRA_POSITION = "position";

    public static final String EXTRA_QUESTION_ID = "question_id";

    public static final String EXTRA_ISQPAD = "iaqpad";
    
    public static final String EXTRA_TAG="go_tag";
    
    
    private int  goTag=-1;
    
    public void setGoTag(int goTag){
        this.goTag=goTag;
    }

    /*
     * public AnswerListItemView(Context context, AttributeSet attrs) {
     * super(context, attrs); this.mContext = context; }
     */
    public AnswerListItemView(Context context) {
        super(context);
        this.mContext = context;
    }

    /**
     * 展示界面数据
     * 
     * @author: sky
     * @param item
     * @param isScroll void
     */
    public void showData(AnswerListItemGsonModel item, boolean isScroll) {
        mAnswerListItemGson = item;
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

        question_id = item.getQuestionid();
        List<AnswerModel> answers = item.getAnswerlist();
        //问题的答案个数
        int size = answers == null ? 0 : answers.size();
        if (answers.size() > 0) {
            mLastAnswererModel = answers.get(answers.size() - 1);
            ImageLoader.getInstance().loadImage(mLastAnswererModel.getAvatar(), mAnswerAvatar,
                    R.drawable.ic_default_avatar, avatarSize, avatarSize / 10);
            mColleage.setText(mLastAnswererModel.getSchools());
            mStatus.setText(String.valueOf(status.get(mLastAnswererModel.getA_state())));
            mAnswerNick.setText(mLastAnswererModel.getTeach_name());
        } else {
            mStatus.setVisibility(View.GONE);
        }
        mStatus.setVisibility(View.GONE);
        mAnswerViewCnt.setText(mContext.getString(R.string.text_read_count, item.getViewcnt()));
        if (item.getDuration() > 0 && item.getViewcnt() == 0) {
            mAnswerViewCnt.setText(
                    mContext.getString(R.string.text_minutes_to_answer, item.getDuration()));
        }
        // 显示年级+科目+问题的id
        mGradeSubject.setText(mContext.getString(R.string.text_question_id_format, item.getGrade(),
                item.getSubject(), question_id));
        //发布问题的时间
        String dateStr = item.getDatatime();
        String grabTimeStr = item.getGrabtime();
        if (!TextUtils.isEmpty(dateStr)) {
            String publishTime = DateUtil
                    .getDisplayTime(DateUtil.parseString2Date(dateStr, "yyyy-MM-dd HH:mm:ss"));
            mPublishTime.setText(publishTime);
        } else {
            String grabTime = DateUtil
                    .getDisplayTime(DateUtil.parseString2Date(grabTimeStr, "yyyy-MM-dd HH:mm:ss"));
            mPublishTime.setText(grabTime);
        }       

        //显示图片
        if (null != item) {
            LogUtils.i(TAG, item.getQ_pic());
        }
        disPlayQuestion(item.getQ_pic(), mQuestionPic);
        switch (size) {
            case 0://没有答案
                mAnswerPicContainer.setVisibility(View.GONE);
                break;
            case 1:// 只有一张答案
                mAnswerPicContainer.setVisibility(View.VISIBLE);
                mAnswerPic1Container.setVisibility(View.VISIBLE);
                mAnswerPic2Container.setVisibility(View.GONE);
                mAnswerPic3Container.setVisibility(View.GONE);
                disPlayAnswer(answers.get(0).getA_pic(), mAnswerPic1, 1);
                break;
            case 2:// 只有2张答案
                mAnswerPicContainer.setVisibility(View.VISIBLE);
                mAnswerPic1Container.setVisibility(View.VISIBLE);
                mAnswerPic2Container.setVisibility(View.VISIBLE);
                mAnswerPic3Container.setVisibility(View.GONE);
                disPlayAnswer(answers.get(0).getA_pic(), mAnswerPic1, 1);
                disPlayAnswer(answers.get(1).getA_pic(), mAnswerPic2, 2);
                break;
            case 3://只有3张答案
                mAnswerPicContainer.setVisibility(View.VISIBLE);
                mAnswerPic1Container.setVisibility(View.VISIBLE);
                mAnswerPic2Container.setVisibility(View.VISIBLE);
                mAnswerPic3Container.setVisibility(View.VISIBLE);
                disPlayAnswer(answers.get(0).getA_pic(), mAnswerPic1, 1);
                disPlayAnswer(answers.get(1).getA_pic(), mAnswerPic2, 2);
                disPlayAnswer(answers.get(2).getA_pic(), mAnswerPic3, 3);
                break;
        }
    }

 

    @Override
    protected void onClickCallback(View v) {
        switch (v.getId()) {
            case R.id.answerer_avatar://点击头像
                IntentManager.gotoPersonalPage((Activity)mContext, mLastAnswererModel.getTeach_id(),
                        mLastAnswererModel.getRoleid());
                break;
            case R.id.collection_container_linearlayout_answerlistitem://点击收藏

                JSONObject json = new JSONObject();
                try {
                    json.put("taskid", question_id);
                    json.put("tasktype", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                BaseActivity activity = null;
                if (mContext instanceof BaseActivity) {
                    activity = (BaseActivity)mContext;
                } else {
                    activity = null;
                }
                OkHttpHelper.post(mContext, "common", "collect",json, new HttpListener() {
					
					@Override
					public void onSuccess(int code, String dataJson, String errMsg) {
                        isCollected = !isCollected;
                        if (isCollected) {// 已经收藏是实心
                            collectionIconIv
                                    .setImageResource(R.drawable.ic_up_and_shoucang_pressed);
                            praisecnt += 1;
                            mAnswerListItemGson.setPraise(1);// 0是未收藏,1是已经收藏
                        } else {// 未收藏是空心
                            collectionIconIv.setImageResource(R.drawable.ic_up_and_shoucang);
                            praisecnt -= 1;
                            mAnswerListItemGson.setPraise(0);// 0是未收藏,1是已经收藏
                        }
                        mAnswerListItemGson.setPraisecnt(praisecnt);
                        collectionCountTv.setText("" + praisecnt);

                    
						
					}
					
					@Override
					public void onFail(int HttpCode,String errMsg) {
						
						
					}
				});

                break;
            default:
                if (null != v.getTag() && v.getTag() instanceof Integer) {
                    int postion = (Integer)v.getTag();
                    Bundle data = new Bundle();
                    data.putInt(EXTRA_POSITION, postion);
                    data.putLong(EXTRA_QUESTION_ID, question_id);
                    data.putBoolean(EXTRA_ISQPAD, false);
                    data.putInt(EXTRA_TAG, goTag);
                    MobclickAgent.onEvent((Activity)mContext, "OneQuestionMoreAnswersDetail");
                    IntentManager.goToAnswerDetail((Activity)mContext, OneQuestionMoreAnswersDetailActivity.class,data);
                }

                break;
        }
    }
    
    
    private void disPlayAnswer(String url, NetworkImageView imageview, int position) {
        ImageLoader.getInstance().ajaxAnswerPic(url, imageview);
        imageview.setTag(position);
    }

    private void disPlayQuestion(String url, NetworkImageView imageview) {
        ImageLoader.getInstance().ajaxQuestionPic(url, imageview);
        imageview.setTag(0);
    }
}
