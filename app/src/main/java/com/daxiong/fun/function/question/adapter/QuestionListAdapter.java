package com.daxiong.fun.function.question.adapter;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.function.homepage.model.HomeListModel;
import com.daxiong.fun.function.question.MyQuestionListActivity;

import java.util.List;

public class QuestionListAdapter extends BaseAdapter {
	private MyQuestionListActivity context;
	private List<HomeListModel> list;
	private ViewHolder viewHolder;
	public QuestionListAdapter(MyQuestionListActivity context, List<HomeListModel> list2) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.hwqu_item_layout, parent, false);
			viewHolder.ll_haoshi = (LinearLayout) convertView.findViewById(R.id.ll_haoshi);
			viewHolder.ll_kong = (LinearLayout) convertView.findViewById(R.id.ll_kong);
			viewHolder.ll_subject = (LinearLayout) convertView.findViewById(R.id.ll_subject);
			viewHolder.ll_pingjia = (RelativeLayout) convertView.findViewById(R.id.ll_pingjia);
			viewHolder.rl_pingjia = (RelativeLayout) convertView.findViewById(R.id.rl_pingjia);
			viewHolder.ll_time = (LinearLayout) convertView.findViewById(R.id.ll_time);
			viewHolder.ll_head = (LinearLayout) convertView.findViewById(R.id.ll_head);
			viewHolder.ll_pigai = (LinearLayout) convertView.findViewById(R.id.ll_pigai);
			viewHolder.ll_jindu = (LinearLayout) convertView.findViewById(R.id.ll_jindu);
			viewHolder.ll_duicuo = (LinearLayout) convertView.findViewById(R.id.ll_duicuo);
			viewHolder.ll_huidazhuangtai= (LinearLayout) convertView.findViewById(R.id.ll_huidazhuangtai);
			viewHolder.ll_zhengquelv = (LinearLayout) convertView.findViewById(R.id.ll_zhengquelv);
			viewHolder.tv_jindu = (TextView) convertView.findViewById(R.id.tv_jindu);
			viewHolder.tv_haoshi = (TextView) convertView.findViewById(R.id.tv_haoshi);
			viewHolder.tv_shichang = (TextView) convertView.findViewById(R.id.tv_shichang);
			viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			viewHolder.tv_huidazhuangtai = (TextView) convertView.findViewById(R.id.tv_huidazhuangtai);
			viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			viewHolder.tv_zhuangtai = (TextView) convertView.findViewById(R.id.tv_zhuangtai);
			viewHolder.tv_hwqu = (TextView) convertView.findViewById(R.id.tv_hwqu);
			viewHolder.tv_meifati = (TextView) convertView.findViewById(R.id.tv_meifati);
			viewHolder.tv_pingjia = (TextView) convertView.findViewById(R.id.tv_pingjia);
			viewHolder.tv_dui = (TextView) convertView.findViewById(R.id.tv_dui);
			viewHolder.tv_cuo = (TextView) convertView.findViewById(R.id.tv_cuo);
			viewHolder.tv_tousuneirong = (TextView) convertView.findViewById(R.id.tv_tousuneirong);
			viewHolder.tv_youtousu = (TextView) convertView.findViewById(R.id.tv_youtousu);
			viewHolder.tv_zhengquelv = (TextView) convertView.findViewById(R.id.tv_zhengquelv);
			viewHolder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
			viewHolder.iv_show = (ImageView) convertView.findViewById(R.id.iv_show);
			viewHolder.iv_jindu = (ImageView) convertView.findViewById(R.id.iv_jindu);
			viewHolder.iv_jiayou = (ImageView) convertView.findViewById(R.id.iv_jiayou);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}

	private class ViewHolder {
		LinearLayout ll_time, ll_head, ll_pigai, ll_jindu, ll_duicuo, ll_zhengquelv, ll_haoshi,ll_subject,ll_huidazhuangtai,ll_kong;
		TextView tv_time, tv_huidazhuangtai, tv_name, tv_zhuangtai, tv_hwqu, tv_meifati, tv_pingjia, tv_dui,
				tv_cuo, tv_zhengquelv, tv_haoshi, tv_shichang, tv_jindu, tv_youtousu, tv_tousuneirong;
		ImageView iv_head, iv_show, iv_jindu,iv_jiayou;
		RelativeLayout rl_pingjia,ll_pingjia;
	}
	class Rotate3d extends Animation {
	    private float mCenterX = 0;
	    private float mCenterY = 0;
	        
	    public void setCenter(float centerX, float centerY) {
	        mCenterX = centerX;
	        mCenterY = centerY;
	    }

	    @Override
	    protected void applyTransformation(float interpolatedTime, Transformation t) {
	        Matrix matrix = t.getMatrix();
	        Camera camera = new Camera();
	        camera.save();
	        camera.rotateY(360 * interpolatedTime);
	        camera.getMatrix(matrix);
	        camera.restore();
	        matrix.preTranslate(-mCenterX, -mCenterY);
	        matrix.postTranslate(mCenterX, mCenterY);
	    }
	}
}
