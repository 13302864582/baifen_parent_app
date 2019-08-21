package com.daxiong.fun.function.course.holder;

import android.view.View;
import android.widget.TextView;

import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.function.course.model.CharpterModel;

public class CourseDetailsListItemHolder extends BaseHolder<CharpterModel> {
	public final static int STATE_NONE = 0;
	public final static int STATE_FREE_TRIAL = 1;
	public final static int STATE_LEARNED = 2;

	private TextView tv_learned;
	private TextView tv_freetrial;
	private TextView tv_period;
	private TextView tv_period_content;
	private TextView tv_kpoint;

	@Override
	public View initView() {
		View view = View.inflate(MyApplication.getContext(), R.layout.details_item4_lv_item, null);
		tv_period = (TextView) view.findViewById(R.id.details_item_item_tv_period);
		tv_period_content = (TextView) view.findViewById(R.id.details_item_item_tv_period_content);
		tv_kpoint = (TextView) view.findViewById(R.id.details_item_item_tv_kpoint);

		tv_freetrial = (TextView) view.findViewById(R.id.details_item_item_tv_freetrial);
		tv_learned = (TextView) view.findViewById(R.id.details_item_item_tv_learned);
		return view;
	}

	@Override
	public void refreshView() {
		CharpterModel data = getData();
		tv_period.setText((getPosition() + 1)+".");
		tv_period_content.setText(data.getCharptername());
		tv_kpoint.setText(data.getKpoint());
		
		//TODO (已完成)课程目录， 课时状态
		if(getPosition() < 2 && !isParamBool()){
			setLearnState(STATE_FREE_TRIAL);
		}
	}

	private void setLearnState(int state) {
		switch (state) {
		case STATE_FREE_TRIAL:
			tv_freetrial.setVisibility(View.VISIBLE);
			tv_learned.setVisibility(View.GONE);
			break;
		case STATE_LEARNED:
			tv_learned.setVisibility(View.VISIBLE);
			tv_freetrial.setVisibility(View.GONE);
			break;
		case STATE_NONE:
			tv_learned.setVisibility(View.GONE);
			tv_freetrial.setVisibility(View.GONE);
			break;
		default:
			tv_learned.setVisibility(View.GONE);
			tv_freetrial.setVisibility(View.GONE);
			break;
		}
	}
}
