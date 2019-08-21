package com.daxiong.fun.function.learninganalysis.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daxiong.fun.R;
import com.daxiong.fun.function.learninganalysis.model.NoneXueqingModel;
import com.daxiong.fun.view.CropCircleTransformation;

import java.util.List;


/**
 * 没有学情的时候就显示这个adapter
 */

public class NoneXueQingAdapter extends RecyclerView.Adapter  {

    private static final String TAG = "NoneXueQingAdapter";

    private Context context;
    private List<NoneXueqingModel.ReportsBean> list;

    MyItemClickListener mItemClickListener;


    public NoneXueQingAdapter(Context context, List<NoneXueqingModel.ReportsBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.none_xueqing_recycleview_item, null);
        MyViewHolder myViewHolder = new MyViewHolder(view, mItemClickListener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        NoneXueqingModel.ReportsBean reportsBean = list.get(position);
        MyViewHolder myViewHolder= (MyViewHolder) viewHolder;


        Glide.with(context).load(reportsBean.getAvatar()).placeholder(R.drawable.default_icon_circle_avatar).bitmapTransform(new CropCircleTransformation(context))
                .into(myViewHolder.ivAvatar);

        myViewHolder.tvName.setText(reportsBean.getName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView ivAvatar;
        public TextView tvName;

        private MyItemClickListener mListener;


        public MyViewHolder(View itemView,MyItemClickListener listener) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            this.mListener=listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mItemClickListener != null){
                mItemClickListener.onItemClick(v,getPosition());
            }

        }
    }


    /**
     * 设置Item点击监听
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }



    public interface MyItemClickListener {
        void onItemClick(View view, int postion);
    }


}
