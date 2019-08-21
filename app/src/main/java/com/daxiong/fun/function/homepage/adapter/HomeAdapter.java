package com.daxiong.fun.function.homepage.adapter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.function.homepage.model.HomeListModel;
import com.daxiong.fun.function.question.OneQuestionMoreAnswersDetailActivity;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.util.DateUtil;
import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.util.SharePerfenceUtil;
import com.daxiong.fun.view.CropCircleTransformation;
import com.daxiong.fun.view.RoundedCornersTransformation;

import java.util.List;

public class HomeAdapter extends BaseAdapter {


    private BaseActivity context;
    private List<HomeListModel> list;
    private ViewHolder viewHolder;


    public HomeAdapter(BaseActivity context, List<HomeListModel> list2) {
        super();
        this.context = context;
        this.list = list2;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_hw_qu_layout, parent, false);

            viewHolder.ll_jindu = (LinearLayout) convertView.findViewById(R.id.ll_jindu);
            viewHolder.ll_duicuo = (LinearLayout) convertView.findViewById(R.id.ll_duicuo);
            viewHolder.ll_jindu2 = (LinearLayout) convertView.findViewById(R.id.ll_jindu2);
            viewHolder.ll_biaoqian = (LinearLayout) convertView.findViewById(R.id.ll_biaoqian);
            viewHolder.rl_jindu = (RelativeLayout) convertView.findViewById(R.id.rl_jindu);
            viewHolder.rl = (RelativeLayout) convertView.findViewById(R.id.rl);

            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tv_huidazhuangtai = (TextView) convertView.findViewById(R.id.tv_huidazhuangtai);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);

            viewHolder.tv_dui = (TextView) convertView.findViewById(R.id.tv_dui);
            viewHolder.tv_cuo = (TextView) convertView.findViewById(R.id.tv_cuo);
            viewHolder.tv_huidadongzuo = (TextView) convertView.findViewById(R.id.tv_huidadongzuo);
            viewHolder.tv_zuoyezhuangtai = (TextView) convertView.findViewById(R.id.tv_zuoyezhuangtai);
            viewHolder.tv_zuoyejieguo = (TextView) convertView.findViewById(R.id.tv_zuoyejieguo);
            viewHolder.tv_biaoqians[0] = (TextView) convertView.findViewById(R.id.tv_biaoqian1);
            viewHolder.tv_biaoqians[1] = (TextView) convertView.findViewById(R.id.tv_biaoqian2);
            viewHolder.tv_biaoqians[2] = (TextView) convertView.findViewById(R.id.tv_biaoqian3);

            viewHolder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
            viewHolder.iv_show = (ImageView) convertView.findViewById(R.id.iv_show);
            viewHolder.iv_show2 = (ImageView) convertView.findViewById(R.id.iv_show2);
            viewHolder.iv_kemu = (ImageView) convertView.findViewById(R.id.iv_kemu);
            viewHolder.tv_percent = (TextView) convertView.findViewById(R.id.tv_percent);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final HomeListModel homeListModel = list.get(position);
        final int task_type = homeListModel.getTask_type();
        final int question_state = homeListModel.getQuestion_state();
        final int homework_state = homeListModel.getHomework_state();
        final int answer_state = homeListModel.getAnswer_state();
        final long huida_time = homeListModel.getAnswer_time() - homeListModel.getGrab_time();
        final long errortime = SharePerfenceUtil.getLong("errortime", 0);
        long pastTimeMillis = 0;
        boolean falg = true;
        viewHolder.ll_jindu.setTag("ll_jindu" + position);
        viewHolder.tv_zuoyejieguo.setTag("tv_zuoyejieguo" + position);
        viewHolder.tv_biaoqians[0].setVisibility(View.GONE);
        viewHolder.tv_biaoqians[1].setVisibility(View.GONE);
        viewHolder.tv_biaoqians[2].setVisibility(View.GONE);
        //科目
        switch (homeListModel.getSubject_id()) {
            case 0:
                viewHolder.iv_kemu.setVisibility(View.GONE);
                break;
            case 1:
                viewHolder.iv_kemu.setVisibility(View.VISIBLE);
                viewHolder.iv_kemu.setImageResource(R.drawable.form_course_english_icon);
                break;
            case 2:
                viewHolder.iv_kemu.setVisibility(View.VISIBLE);
                viewHolder.iv_kemu.setImageResource(R.drawable.form_course_math_icon);
                break;
            case 3:
                viewHolder.iv_kemu.setVisibility(View.VISIBLE);
                viewHolder.iv_kemu.setImageResource(R.drawable.form_course_physics_icon);
                break;
            case 4:
                viewHolder.iv_kemu.setVisibility(View.VISIBLE);
                viewHolder.iv_kemu.setImageResource(R.drawable.form_course_chemistry_icon);
                break;
            case 5:
                viewHolder.iv_kemu.setVisibility(View.VISIBLE);
                viewHolder.iv_kemu.setImageResource(R.drawable.form_course_biology_icon);
                break;
            case 6:
                viewHolder.iv_kemu.setVisibility(View.VISIBLE);
                viewHolder.iv_kemu.setImageResource(R.drawable.form_course_chinese_icon);
                break;

        }
        if (task_type == 2 && homework_state > 1 && homework_state < 8) {
            if (homeListModel.getPercent() > 0) {
                viewHolder.tv_percent.setVisibility(View.VISIBLE);
                viewHolder.tv_percent.setText(homeListModel.getPercent() + "%");
            }
        } else {
            viewHolder.tv_percent.setVisibility(View.GONE);
        }


        //缩略图
        String iv_showpic = "";
        if (task_type == 2) {
            iv_showpic = homeListModel.getHomewrok_thumbnail();
        } else {
            iv_showpic = homeListModel.getQuestion_thumbnail();
        }


        Glide.with(context).load(iv_showpic)


            .placeholder(R.drawable.yuanjiao)
                .bitmapTransform(new RoundedCornersTransformation(context, 8, 0))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.iv_show);
//        viewHolder.iv_show2.setVisibility(View.GONE);
//        Glide.with(context).load(R.drawable.anzhuobg)
//
//
//                .bitmapTransform(new RoundedCornersTransformation(context, 5, 0))
//
//                .into(viewHolder.iv_show2);

        //头像
        Glide.with(context).load(homeListModel.getTeacher_pic()).asBitmap().centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new CropCircleTransformation(context)).placeholder(R.drawable.default_icon_circle_avatar)
                .into(viewHolder.iv_head);
        //教师名
        if (TextUtils.isEmpty(homeListModel.getTeacher_name())) {
            viewHolder.tv_name.setText("大熊老师");
        } else {
            viewHolder.tv_name.setText(homeListModel.getTeacher_name());
        }
        //发题时间

        long create_time = homeListModel.getCreate_time();
        long pastTime = System.currentTimeMillis() - errortime - create_time;
        if (pastTime < 1000 * 60 * 2) {//小于两分钟
            viewHolder.tv_time.setText("刚刚");
        } else if (pastTime < 1000 * 60 * 60) {//小于一小时
            viewHolder.tv_time.setText(pastTime / (1000 * 60) + "分钟前");
        } else {
            viewHolder.tv_time.setText(DateUtil.getMonthweek2(create_time));
        }
        //对错数和标签
        if ((task_type == 2 && homework_state > 1 && homework_state < 8) | (task_type == 1 && answer_state > 0 && answer_state < 9)) {
            if (homeListModel.getTags() != null && homeListModel.getTags().size() > 0) {
                viewHolder.ll_biaoqian.setVisibility(View.VISIBLE);
                int sum = 0;
                for (int i = 0; i < homeListModel.getTags().size() && i < 3;
                     i++) {
                    String content = homeListModel.getTags().get(i).getContent();
                    sum = sum + content.length();


                    if ((sum > 13 && i == 2) | TextUtils.isEmpty(content)) {
                        viewHolder.tv_biaoqians[i].setVisibility(View.GONE);
                    } else {
                        viewHolder.tv_biaoqians[i].setVisibility(View.VISIBLE);
                    }
                    viewHolder.tv_biaoqians[i].setText(content);
                }
            } else {
                viewHolder.ll_biaoqian.setVisibility(View.GONE);
            }


        } else {
            viewHolder.ll_biaoqian.setVisibility(View.GONE);

        }
        //对错数
        if (task_type == 2) {
            if (homework_state > 1 && homework_state < 8) {
                viewHolder.ll_duicuo.setVisibility(View.VISIBLE);
                viewHolder.tv_dui.setText(" " + homeListModel.getRight_count());
                viewHolder.tv_cuo.setText(" " + homeListModel.getWrong_count());
            } else {

                viewHolder.ll_duicuo.setVisibility(View.GONE);
            }
        } else {
            viewHolder.ll_duicuo.setVisibility(View.GONE);
        }
        //进度条
        if ((task_type == 2 && homework_state == 1) | (task_type == 1 && answer_state == 0)) {
            viewHolder.rl_jindu.setVisibility(View.VISIBLE);

            pastTimeMillis = System.currentTimeMillis() - errortime - homeListModel.getGrab_time();
            pastTimeMillis = pastTimeMillis < 1 ? 1 : pastTimeMillis;

            long avg_cost_time = homeListModel.getAvg_cost_time();
            avg_cost_time = avg_cost_time < 10 ? 10 : avg_cost_time;
            long percent = pastTimeMillis * 100 / avg_cost_time;
            percent = percent > 95 ? 95 : percent;
            percent = percent < 5 ? 5 : percent;


            int dip2px = DensityUtil.dip2px(context, percent * 2);
            viewHolder.ll_jindu.setLayoutParams(new RelativeLayout.LayoutParams(dip2px, RelativeLayout.LayoutParams.MATCH_PARENT));
        } else {
            viewHolder.rl_jindu.setVisibility(View.GONE);
        }
        //状态文字
        if ((task_type == 2 && homework_state == 0) | (task_type == 1 && question_state == 0)) {
            falg = false;
            viewHolder.tv_huidadongzuo.setVisibility(View.GONE);
            viewHolder.tv_zuoyezhuangtai.setVisibility(View.VISIBLE);
            viewHolder.tv_zuoyezhuangtai.setText("正在准备");

            viewHolder.tv_huidazhuangtai.setVisibility(View.VISIBLE);
            if (task_type == 2) {
                viewHolder.tv_huidazhuangtai.setText("为您批改作业");
            } else {
                viewHolder.tv_huidazhuangtai.setText("为您解答难题");
            }
            viewHolder.tv_zuoyejieguo.setText("请稍后...");
        } else if ((task_type == 2 && homework_state == 1) | (task_type == 1 && answer_state == 0)) {
            viewHolder.tv_huidadongzuo.setVisibility(View.GONE);
            viewHolder.tv_zuoyezhuangtai.setVisibility(View.VISIBLE);

            if (task_type == 2) {
                viewHolder.tv_zuoyezhuangtai.setText("批改中");
            } else {
                viewHolder.tv_zuoyezhuangtai.setText("回答中");
            }
            viewHolder.tv_huidazhuangtai.setVisibility(View.VISIBLE);

            viewHolder.tv_huidazhuangtai.setText("，点击查看进度");

            viewHolder.tv_zuoyejieguo.setText("已耗时" + DateUtil.getMillis2minute(pastTimeMillis));
        } else if ((task_type == 2 && homework_state == 2) | (task_type == 1 && answer_state == 1)) {
            viewHolder.tv_huidadongzuo.setVisibility(View.VISIBLE);
            if (task_type == 2) {
                viewHolder.tv_huidadongzuo.setText("您的作业已经");
            } else {
                viewHolder.tv_huidadongzuo.setText("您的难题已经");
            }
            viewHolder.tv_zuoyezhuangtai.setVisibility(View.VISIBLE);

            if (task_type == 2) {
                viewHolder.tv_zuoyezhuangtai.setText("批改完成");
            } else {
                viewHolder.tv_zuoyezhuangtai.setText("回答完成");
            }
            viewHolder.tv_huidazhuangtai.setVisibility(View.GONE);


            viewHolder.tv_zuoyejieguo.setText("总耗时" + DateUtil.getMillis2minute(huida_time));
        } else if ((task_type == 2 && homework_state == 3) | (task_type == 1 && answer_state == 6)) {
            viewHolder.tv_huidadongzuo.setVisibility(View.VISIBLE);
            if (task_type == 2) {
                viewHolder.tv_huidadongzuo.setText("您的作业正在");
            } else {
                viewHolder.tv_huidadongzuo.setText("您的难题正在");
            }
            viewHolder.tv_zuoyezhuangtai.setVisibility(View.VISIBLE);


            viewHolder.tv_zuoyezhuangtai.setText("追问");

            viewHolder.tv_huidazhuangtai.setVisibility(View.GONE);


            viewHolder.tv_zuoyejieguo.setText("总耗时" + DateUtil.getMillis2minute(huida_time));
        } else if ((task_type == 2 && homework_state == 4) | (task_type == 1 && answer_state == 2)) {
            viewHolder.tv_huidadongzuo.setVisibility(View.VISIBLE);
            if (task_type == 2) {
                viewHolder.tv_huidadongzuo.setText("老师批改的作业已经被您");
            } else {
                viewHolder.tv_huidadongzuo.setText("老师回答的问题已经被您");
            }
            viewHolder.tv_zuoyezhuangtai.setVisibility(View.VISIBLE);


            viewHolder.tv_zuoyezhuangtai.setText("采纳");

            viewHolder.tv_huidazhuangtai.setVisibility(View.GONE);


            viewHolder.tv_zuoyejieguo.setText("总耗时" + DateUtil.getMillis2minute(huida_time));
        } else if ((task_type == 2 && (homework_state == 5 | homework_state == 6)) | (task_type == 1 && (answer_state == 3 | answer_state == 4))) {
            viewHolder.tv_huidadongzuo.setVisibility(View.VISIBLE);
            if (task_type == 2) {
                viewHolder.tv_huidadongzuo.setText("您的作业正在");
            } else {
                viewHolder.tv_huidadongzuo.setText("您的难题正在");
            }
            viewHolder.tv_zuoyezhuangtai.setVisibility(View.VISIBLE);


            viewHolder.tv_zuoyezhuangtai.setText("仲裁");

            viewHolder.tv_huidazhuangtai.setVisibility(View.GONE);


            viewHolder.tv_zuoyejieguo.setText("总耗时" + DateUtil.getMillis2minute(huida_time));
        } else if ((task_type == 2 && homework_state == 8) | (task_type == 1 && (answer_state == 5 | question_state == 9))) {
            falg = false;
            viewHolder.tv_huidadongzuo.setVisibility(View.VISIBLE);
            if (task_type == 2) {
                viewHolder.tv_huidadongzuo.setText("老师无法批改，");
            } else {
                viewHolder.tv_huidadongzuo.setText("老师无法作答，");
            }

            viewHolder.tv_zuoyezhuangtai.setVisibility(View.VISIBLE);
            viewHolder.tv_zuoyezhuangtai.setText("请重拍");

            viewHolder.tv_huidazhuangtai.setVisibility(View.GONE);


            //viewHolder.tv_zuoyejieguo.setText("可能图片不符合规则");
            viewHolder.tv_zuoyejieguo.setText(homeListModel.getReport_reason());

            viewHolder.tv_percent.setVisibility(View.GONE);
        }
        //点击
        viewHolder.rl.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 if (task_type == 2) {
                                                     Bundle data = new Bundle();
                                                     data.putBoolean("isShow", true);
                                                     data.putInt("type", 1);
                                                     data.putInt("position", 0);
                                                     data.putInt("taskid", homeListModel.getTask_id());
                                                     context.uMengEvent("homework_detailfrommy");

                                                     IntentManager.goToStuHomeWorkDetailActivity(context, data, false);
                                                 } else {
                                                     Bundle data = new Bundle();
                                                     data.putInt("position", 0);
                                                     data.putLong("question_id", homeListModel.getQuestion_id());
                                                     data.putBoolean("iaqpad", true);
                                                     // 1表示从问答跳转到解答详情页面 为了表示从学情分析跳转的还是从我的问答跳转的
                                                     data.putInt("go_tag", 1);
                                                     MobclickAgent.onEvent(context, "MyQaDetail");

                                                     IntentManager.goToAnswerDetail(context, OneQuestionMoreAnswersDetailActivity.class, data, 1002);
                                                 }

                                             }
                                         }
        );
        final boolean falg2 = falg;
        viewHolder.iv_head.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      if (falg2) {
                                                          IntentManager.gotoPersonalPage(context, homeListModel.getTeacher_id(), GlobalContant.ROLE_ID_COLLEAGE);
                                                      }
                                                  }
                                              }
        );
        return convertView;
    }

    private class ViewHolder {
        LinearLayout ll_duicuo, ll_jindu, ll_jindu2, ll_biaoqian;
        TextView tv_dui, tv_cuo, tv_name, tv_time, tv_huidadongzuo, tv_huidazhuangtai, tv_zuoyezhuangtai, tv_zuoyejieguo, tv_percent;
        TextView[] tv_biaoqians = new TextView[3];
        ImageView iv_head, iv_show, iv_kemu, iv_show2;
        RelativeLayout rl_jindu, rl;

    }


}
