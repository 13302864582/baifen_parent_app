
package com.daxiong.fun.function.homework.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.base.ImageLoader;
import com.daxiong.fun.constant.EventConstant;
import com.daxiong.fun.model.HomeworkAnalysisModel;
import com.daxiong.fun.util.DateUtil;
import com.daxiong.fun.util.MediaUtil;
import com.daxiong.fun.util.MediaUtil.ResetImageSourceCallback;
import com.daxiong.fun.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeworkAnalysisAdapter extends BaseAdapter {

    private Context context;

    private List<HomeworkAnalysisModel> homeworkAnalysisList;

    private List<HomeworkAnalysisModel> temphomeworkAnalysisList = new ArrayList<HomeworkAnalysisModel>();

    private int complete_rate = 0;

    private int index = 0;

    private AnimationDrawable mAnimationDrawable;

    int avatarSize = 0;

    public void setDotData(int index, int complete_rate) {
        this.index = index;
        this.complete_rate = complete_rate;
        avatarSize = context.getResources()
                .getDimensionPixelSize(R.dimen.group_contacts_list_avatar_size);
        getDataFromList(index);
        notifyDataSetChanged();
    }
    
    public void indexClear(){
        this.index=0;
    }
    

    public void initDotData() {
        homeworkAnalysisList.clear();
        index=0;
        getDataFromList(index);
        notifyDataSetChanged();
    }
    
   

    public HomeworkAnalysisAdapter(Context context,
            List<HomeworkAnalysisModel> homeworkAnalysisList) {
        super();
        this.context = context;
        this.homeworkAnalysisList = homeworkAnalysisList;

    }

    @Override
    public int getCount() {
        return temphomeworkAnalysisList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return temphomeworkAnalysisList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public void getDataFromList(int day) {
        if (temphomeworkAnalysisList != null && temphomeworkAnalysisList.size() > 0) {
            temphomeworkAnalysisList.clear();
        }
        for (int i = 0; i < homeworkAnalysisList.size(); i++) {
            if (homeworkAnalysisList.get(i).getDay() == day) {
                temphomeworkAnalysisList.add(homeworkAnalysisList.get(i));
            }
        }
        // homeworkAnalysisList.clear();
        // homeworkAnalysisList.addAll(temphomeworkAnalysisList);

    }

    HomeworkAnalysisModel model = null;

    int homeworkid = 0;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.hw_analysis_listview_item_layout, null);
            holder.layout_parent = (LinearLayout)convertView.findViewById(R.id.layout_parent);
            holder.layout_hw = (LinearLayout)convertView.findViewById(R.id.layout_hw);
            holder.tv_homework_index = (TextView)convertView.findViewById(R.id.tv_homework_index);
            holder.tv_homework_id = (TextView)convertView.findViewById(R.id.tv_homework_id);
            holder.tv_date = (TextView)convertView.findViewById(R.id.tv_date);
            holder.tv_right_rate = (TextView)convertView.findViewById(R.id.tv_right_rate);
            holder.layout_knowledge = (LinearLayout)convertView.findViewById(R.id.layout_knowledge);
            holder.tv_knowledge_value = (TextView)convertView.findViewById(R.id.tv_knowledge_value);
            holder.iv_user_avatar = (NetworkImageView)convertView.findViewById(R.id.iv_user_avatar);
            holder.tv_comment = (TextView)convertView.findViewById(R.id.tv_comment);
            holder.layout_record = (LinearLayout)convertView.findViewById(R.id.layout_record);
            holder.iv_user_avatar_voice=(NetworkImageView)convertView.findViewById(R.id.iv_user_avatar_voice);
            holder.iv_voice = (ImageView)convertView.findViewById(R.id.iv_voice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        if (index > 1) {// 如果是点击的那一天就显示数据
            if (position < temphomeworkAnalysisList.size()) {
                model = temphomeworkAnalysisList.get(position);
                homeworkid = model.getId();
                holder.layout_parent.setVisibility(View.VISIBLE);
                holder.layout_hw.setVisibility(View.VISIBLE);
                holder.tv_homework_index.setText("作业" + (position + 1));
                holder.tv_homework_id.setText("ID:  " + model.getId());
                // 处理日期
                holder.tv_date.setText(DateUtil.getStrTimeWithMonthAndDay(model.getDatatime()));
                holder.tv_right_rate.setText("正确率：" + complete_rate + "%");

                // 处理知识点
                if (!TextUtils.isEmpty(model.getKpoint()) && model.getKpoint().contains(";")) {
                    if (model.getKpoint().split(";").length == 0) {
                        // 隐藏知识点
                        holder.layout_knowledge.setVisibility(View.GONE);
                    } else {
                        holder.layout_knowledge.setVisibility(View.VISIBLE);
                        holder.tv_knowledge_value.setText(model.getKpoint());
                    }
                } else {
                    holder.layout_knowledge.setVisibility(View.GONE);
                }

                // ImageLoader.getInstance().loadImage(model.getAvatar(),
                // holder.iv_user_avatar, R.drawable.ic_default_avatar);
               

                if (!TextUtils.isEmpty(model.getRemark_txt())) {
                    holder.tv_comment.setVisibility(View.VISIBLE);
                    holder.tv_comment.setText(model.getRemark_txt());
                    holder.iv_user_avatar.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().loadImage(model.getAvatar(), holder.iv_user_avatar,
                            R.drawable.ic_default_avatar, avatarSize, avatarSize / 10);
                } else {
                    holder.tv_comment.setVisibility(View.GONE);
                    holder.iv_user_avatar.setVisibility(View.GONE);
                }

                if (TextUtils.isEmpty(model.getRemark_snd_url())) {
                    holder.layout_record.setVisibility(View.GONE);
                } else {
                    if (!TextUtils.isEmpty(model.getRemark_snd_url())
                            && model.getRemark_snd_url().contains(".amr")) {
                        holder.layout_record.setVisibility(View.VISIBLE);
                        
                        ImageLoader.getInstance().loadImage(model.getAvatar(), holder.iv_user_avatar_voice,
                                R.drawable.ic_default_avatar, avatarSize, avatarSize / 10);
                        showVoice(holder, model);
                    } else {
                        holder.layout_record.setVisibility(View.GONE);
                    }

                }

                // int prehomeworkid = (position - 1) >= 0 ?
                // homeworkAnalysisList.get(position - 1).getId() : -1;
                //
                // if (prehomeworkid!=-1) {
                // if (prehomeworkid != homeworkid) {
                // holder.layout_parent.setVisibility(View.VISIBLE);
                // holder.layout_hw.setVisibility(View.VISIBLE);
                // } else {
                // holder.layout_hw.setVisibility(View.GONE);
                // }
                // }
            }

        } else {
            holder.layout_parent.setVisibility(View.GONE);
        }

        return convertView;
    }

    private void showVoice(final ViewHolder holder, final HomeworkAnalysisModel model) {
        // 显示语音
        // holder.layout_record.setVisibility(View.VISIBLE);
        holder.iv_voice.setImageResource(R.drawable.ic_play2);
        holder.layout_record.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                MobclickAgent.onEvent(context, EventConstant.CUSTOM_EVENT_PLAY_AUDIO);
                /*
                 * StatService .onEvent( mActivity, EventConstant
                 * .CUSTOM_EVENT_PLAY_AUDIO, "");
                 */
                if (TextUtils.isEmpty(model.getRemark_snd_url())) {
                    ToastUtils.show(R.string.text_audio_is_playing_please_waiting);
                    return;
                }
                holder.iv_voice.setImageResource(R.drawable.play_animation);
                mAnimationDrawable = (AnimationDrawable)holder.iv_voice.getDrawable();
                MyApplication.animationDrawables.add(mAnimationDrawable);
                MyApplication.anmimationPlayViews.add(holder.iv_voice);
                /*
                 * WeLearnMediaUtil.getInstance(false) .stopPlay();
                 */
                MediaUtil.getInstance(false).playVoice(false, model.getRemark_snd_url(),
                        mAnimationDrawable, new ResetImageSourceCallback() {

                    @Override
                    public void reset() {
                        holder.iv_voice.setImageResource(R.drawable.ic_play2);
                        // isSound[i] =
                        // false;
                    }

                    @Override
                    public void playAnimation() {
                    }

                    @Override
                    public void beforePlay() {
                        MediaUtil.getInstance(false).resetAnimationPlay(holder.iv_voice);
                    }
                }, null);
            }
        });

    }

    class ViewHolder {
        private LinearLayout layout_parent;

        private LinearLayout layout_hw;

        private TextView tv_homework_index;

        private TextView tv_homework_id;

        private TextView tv_date;

        private TextView tv_right_rate;

        private LinearLayout layout_knowledge;

        private TextView tv_knowledge_value;

        private NetworkImageView iv_user_avatar;

        private TextView tv_comment;

        private LinearLayout layout_record;
        
        private NetworkImageView iv_user_avatar_voice;

        private ImageView iv_voice;

    }

    class subAdapter extends BaseAdapter {

        private Context context;

        private List<HomeworkAnalysisModel> homeworkAnalysisList;

        public subAdapter(Context context, List<HomeworkAnalysisModel> homeworkAnalysisList) {
            super();
            this.context = context;
            this.homeworkAnalysisList = homeworkAnalysisList;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return homeworkAnalysisList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return homeworkAnalysisList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return convertView;
        }

    }

   

}
