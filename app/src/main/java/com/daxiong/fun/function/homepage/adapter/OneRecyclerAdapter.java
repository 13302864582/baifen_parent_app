package com.daxiong.fun.function.homepage.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daxiong.fun.MainActivity;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.Firstuse;
import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.view.CropCircleTransformation;
import com.daxiong.fun.view.HoloCircularProgressBar;
import com.daxiong.fun.view.Rotate3dAnimation;

import java.util.List;
import java.util.Random;

import static com.daxiong.fun.R.id.rl1;
import static com.daxiong.fun.R.id.tv_pigai;

public class OneRecyclerAdapter extends RecyclerView.Adapter<OneRecyclerAdapter.ViewHolder> {
    private MainActivity context;
    private List<Firstuse.Teacher_infos> list;
    private int mtime1, mtime2, mtime3;

    public OneRecyclerAdapter(MainActivity context, List<Firstuse.Teacher_infos> list2, int mtime1, int mtime2, int mtime3) {
        super();
        this.context = context;
        this.list = list2;
        this.mtime1 = mtime1;
        this.mtime2 = mtime2;
        this.mtime3 = mtime3;
    }
    @Override
    public OneRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_home,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(OneRecyclerAdapter.ViewHolder viewHolder, int position) {
        final Firstuse.Teacher_infos teacher_infos = list.get(position);
        ObjectAnimator mProgressBarAnimator = null;
        final int index = 0;
        final int index2 = index;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context,150));
        if(position==0|position==3){
            lp.setMargins(0, DensityUtil.dip2px(context,6), DensityUtil.dip2px(context,3), 0);
        }else if(position==1|position==4){
            lp.setMargins(DensityUtil.dip2px(context,3), DensityUtil.dip2px(context,6), DensityUtil.dip2px(context,3), 0);
        }else if(position==2|position==5){
            lp.setMargins(DensityUtil.dip2px(context,3), DensityUtil.dip2px(context,6),0, 0);
        }

        viewHolder.rl1.setLayoutParams(lp);
        viewHolder.rl1.setPersistentDrawingCache(RelativeLayout.PERSISTENT_ANIMATION_CACHE);
        viewHolder.name.setText(teacher_infos.getName());
        viewHolder.studname.setText("“"+teacher_infos.getTids().get(index).getStudname()+"”");
        viewHolder.chakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle data = new Bundle();
                data.putBoolean("isShow", false);
                data.putInt("type", 2);
                data.putInt("position", 0);
                data.putInt("taskid", teacher_infos.getTids().get(index2).getTaskid());
                context.uMengEvent("homework_detailfrommy");

                IntentManager.goToStuHomeWorkDetailActivity(context, data, false);
            }
        });

        Glide.with(context).load(teacher_infos.getAvatar()).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new CropCircleTransformation(context)).placeholder(R.drawable.default_icon_circle_avatar)
                .into(viewHolder.avatar);
        if (teacher_infos.getType() == 0) {
            viewHolder.rl2.setVisibility(View.GONE);
            viewHolder.rl3.setVisibility(View.VISIBLE);
            float vFloat1 = new Random().nextFloat();
            float vFloat5 = new Random().nextFloat();
            vFloat5 = vFloat5 <0.4f ? vFloat5:0.4f;
            viewHolder.mHoloCircularProgressBar.setVisibility(View.VISIBLE);
            viewHolder.tv_pigai.setVisibility(View.VISIBLE);

            animate(viewHolder.mHoloCircularProgressBar, null, vFloat1, 0, mProgressBarAnimator);
            viewHolder.mHoloCircularProgressBar.setProgress(vFloat1);
            StartMyAnimation startMyAnimation = new StartMyAnimation(viewHolder, index, mProgressBarAnimator,position, 1.0f - vFloat1+vFloat5);
            MyApplication.runnablelists.add(startMyAnimation);
            MyApplication.getMainThreadHandler().postDelayed(startMyAnimation,500);

        } else if (teacher_infos.getType() == 1) {
            viewHolder.rl2.setVisibility(View.GONE);
            viewHolder.rl3.setVisibility(View.VISIBLE);
            viewHolder.avatar.setClickable(true);
            viewHolder.avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundleData = new Bundle();
                    bundleData.putInt("userid", teacher_infos.getUid());
                    bundleData.putInt("roleid", 2);

                    IntentManager.goToTeacherInfoView(context, bundleData);
                }
            });
            float vFloat2 = new Random().nextFloat();
            vFloat2 = vFloat2 <0.6f ? vFloat2+1:vFloat2;
            StartVisible startVisible = new StartVisible(viewHolder, index, mProgressBarAnimator,position);
            MyApplication.runnablelists.add(startVisible);
            MyApplication.getMainThreadHandler().postDelayed(startVisible, 1000 * (int) (mtime1 * vFloat2));

        } else if (teacher_infos.getType() == 2) {
            viewHolder.rl3.setVisibility(View.GONE);
            viewHolder.rl2.setVisibility(View.VISIBLE);
            float vFloat3 = new Random().nextFloat();
            vFloat3 = vFloat3 <0.6f ? vFloat3+1:vFloat3;
            ApplyRotation applyRotation = new ApplyRotation(viewHolder, index, mProgressBarAnimator, position);
            MyApplication.runnablelists.add(applyRotation);
            MyApplication.getMainThreadHandler().postDelayed(applyRotation, 1000 * (int) (mtime3 * vFloat3));
        }

    }

    @Override
    public int getItemCount() {
        return 6;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl1, rl2, rl3;
        HoloCircularProgressBar mHoloCircularProgressBar;
        ImageView avatar;
        TextView name, studname, chakan,tv_pigai;
        public ViewHolder(View view){
            super(view);
           mHoloCircularProgressBar = (HoloCircularProgressBar) view.findViewById(
                    R.id.holoCircularProgressBar);
           rl1 = (RelativeLayout) view.findViewById(
                    R.id.rl1);
           rl2 = (RelativeLayout) view.findViewById(
                    R.id.rl2);
           rl3 = (RelativeLayout) view.findViewById(
                    R.id.rl3);
           avatar = (ImageView) view.findViewById(
                    R.id.avatar);
           name = (TextView) view.findViewById(
                    R.id.name);
            studname = (TextView) view.findViewById(
                    R.id.studname);
            chakan = (TextView) view.findViewById(
                    R.id.chakan);
            tv_pigai = (TextView) view.findViewById(
                    R.id.tv_pigai);

        }
    }
    class ClassHolder {

        public int index;
        public int position;

        public ObjectAnimator mProgressBarAnimator;

        public OneRecyclerAdapter.ViewHolder viewHolder;
        public ClassHolder(OneRecyclerAdapter.ViewHolder viewHolder,int index, ObjectAnimator mProgressBarAnimator, int position) {

            this.viewHolder = viewHolder;


            this.position = position;
            this.index = index;
            this.mProgressBarAnimator = mProgressBarAnimator;
        }


    }


    private final class ApplyRotation extends ClassHolder implements Runnable {


        public ApplyRotation(ViewHolder viewHolder, int index, ObjectAnimator mProgressBarAnimator, int position) {
            super(viewHolder, index, mProgressBarAnimator, position);
        }

        public void run() {
            MyApplication.runnablelists.remove(this);
            MyApplication.getMainThreadHandler().removeCallbacks(this);
            final float centerX = viewHolder.rl1.getWidth() / 2.0f;
            final float centerY = viewHolder.rl1.getHeight() / 2.0f;


            final Rotate3dAnimation rotation =
                    new Rotate3dAnimation(0, 90, centerX, centerY, 310.0f, true);
            rotation.setDuration(800);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new AccelerateInterpolator());
            rotation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (viewHolder.rl2.getVisibility() == View.VISIBLE) {
                        viewHolder.rl2.setVisibility(View.GONE);

                        viewHolder.rl3.setVisibility(View.VISIBLE);
                        position=position<6?position+6:position-6;
                        viewHolder.name.setText(list.get(position).getName());
                        Glide.with(context).load(list.get(position).getAvatar()).asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .transform(new CropCircleTransformation(context)).placeholder(R.drawable.default_icon_circle_avatar)
                                .into(viewHolder.avatar);
                        viewHolder.avatar.setClickable(true);
                        viewHolder.avatar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundleData = new Bundle();
                                bundleData.putInt("userid", list.get(position).getUid());
                                bundleData.putInt("roleid", 2);

                                IntentManager.goToTeacherInfoView(context, bundleData);
                            }
                        });
                    } else {
                        viewHolder.rl2.setVisibility(View.VISIBLE);
                        viewHolder.rl3.setVisibility(View.GONE);
                        index=(index+1)%20;
                        viewHolder.studname.setText("“"+list.get(position).getTids().get(index).getStudname()+"”");
                        final int index2 = index;
                        viewHolder. chakan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Bundle data = new Bundle();
                                data.putBoolean("isShow", false);
                                data.putInt("type", 2);
                                data.putInt("position", 0);
                                data.putInt("taskid", list.get(position).getTids().get(index2).getTaskid());
                                context.uMengEvent("homework_detailfrommy");

                                IntentManager.goToStuHomeWorkDetailActivity(context, data, false);
                            }
                        });
                    }
                    SwapViews swapViews = new SwapViews(viewHolder, index, mProgressBarAnimator, position);
                    MyApplication.runnablelists.add(swapViews);
                    MyApplication.getMainThreadHandler().post(swapViews);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            viewHolder.rl1.startAnimation(rotation);


        }
    }

    private final class SwapViews extends ClassHolder implements Runnable {


        public SwapViews(ViewHolder viewHolder, int index, ObjectAnimator mProgressBarAnimator,  int position) {
            super(viewHolder, index, mProgressBarAnimator,  position);
        }

        public void run() {
            MyApplication.runnablelists.remove(this);
            MyApplication.getMainThreadHandler().removeCallbacks(this);
            final float centerX = viewHolder.rl1.getWidth() / 2.0f;
            final float centerY = viewHolder.rl1.getHeight() / 2.0f;
            Rotate3dAnimation rotation;


            rotation = new Rotate3dAnimation(90, 0, centerX, centerY, 310.0f, false);

            rotation.setDuration(800);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());

            rotation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    float vFloat3 = new Random().nextFloat();
                    vFloat3 = vFloat3 <0.3f ? (vFloat3+1):(vFloat3 <0.5f ?vFloat3+0.2f:vFloat3);
                    if (viewHolder.rl3.getVisibility() == View.VISIBLE) {

                        if (mProgressBarAnimator != null) {
                            mProgressBarAnimator.cancel();
                        }
                        viewHolder. mHoloCircularProgressBar.setVisibility(View.GONE);
                        viewHolder. tv_pigai.setVisibility(View.GONE);

                        StartVisible startVisible = new StartVisible(viewHolder, index, mProgressBarAnimator,position);
                        MyApplication.runnablelists.add(startVisible);
                        MyApplication.getMainThreadHandler().postDelayed(startVisible, 1000 *(int)( mtime1 * vFloat3));

                    } else {

                        ApplyRotation applyRotation = new ApplyRotation(viewHolder, index, mProgressBarAnimator, position);
                        MyApplication.runnablelists.add(applyRotation);
                        MyApplication.getMainThreadHandler().postDelayed(applyRotation, 1000 * (int) (mtime3 * vFloat3));
                    }

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            viewHolder.rl1.startAnimation(rotation);


        }
    }

    private final class StartVisible extends ClassHolder implements Runnable {


        public StartVisible(ViewHolder viewHolder, int index, ObjectAnimator mProgressBarAnimator,  int position) {
            super(viewHolder, index, mProgressBarAnimator,  position);
        }

        public void run() {
            MyApplication.runnablelists.remove(this);
            MyApplication.getMainThreadHandler().removeCallbacks(this);
            viewHolder.mHoloCircularProgressBar.setVisibility(View.VISIBLE);
            viewHolder.avatar.setClickable(false);

            viewHolder.tv_pigai.setVisibility(View.VISIBLE);
            animate(viewHolder.mHoloCircularProgressBar, null, 0f, 0, mProgressBarAnimator);
            viewHolder. mHoloCircularProgressBar.setProgress(0f);
            float vFloat4 = new Random().nextFloat();
            vFloat4 =vFloat4<0.8f?vFloat4+0.8f:vFloat4;
            StartMyAnimation startMyAnimation = new StartMyAnimation(viewHolder, index, mProgressBarAnimator, position, vFloat4);
            MyApplication.runnablelists.add(startMyAnimation);
            MyApplication.getMainThreadHandler().post(startMyAnimation);
        }
    }

    private final class StartMyAnimation extends ClassHolder implements Runnable {

        private float vFloat = 1f;

        public StartMyAnimation(ViewHolder viewHolder, int index, ObjectAnimator mProgressBarAnimator,  int position,float vFloat) {
            super(viewHolder, index, mProgressBarAnimator,position);
            this.vFloat = vFloat;
        }





        public void run() {
            MyApplication.runnablelists.remove(this);
            MyApplication.getMainThreadHandler().removeCallbacks(this);
            if (mProgressBarAnimator != null) {
                mProgressBarAnimator.cancel();
            }
            viewHolder. mHoloCircularProgressBar.setVisibility(View.VISIBLE);
            viewHolder.tv_pigai.setVisibility(View.VISIBLE);
            animate(viewHolder.mHoloCircularProgressBar, new AnimatorListenerAdapter() {


                @Override
                public void onAnimationEnd(Animator animation) {
                    viewHolder. mHoloCircularProgressBar.setProgress(1f);
                    viewHolder.mHoloCircularProgressBar.setVisibility(View.GONE);
                    viewHolder. tv_pigai.setVisibility(View.GONE);
                    ApplyRotation applyRotation = new ApplyRotation(viewHolder, index, mProgressBarAnimator, position);
                    MyApplication.runnablelists.add(applyRotation);
                    MyApplication.getMainThreadHandler().post(applyRotation);
                }


            }, 1f, 1000 * (int) (mtime2 * vFloat), mProgressBarAnimator);

        }
    }

    private void animate(final HoloCircularProgressBar progressBar, final AnimatorListenerAdapter listener,
                         final float progress, final int duration, ObjectAnimator mProgressBarAnimator) {

        mProgressBarAnimator = ObjectAnimator.ofFloat(progressBar, "progress", progress);
        mProgressBarAnimator.setDuration(duration);


        if (listener != null) {
            mProgressBarAnimator.addListener(listener);
        }
        mProgressBarAnimator.reverse();
        mProgressBarAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                progressBar.setProgress((Float) animation.getAnimatedValue());
            }
        });

        mProgressBarAnimator.start();
    }

}
