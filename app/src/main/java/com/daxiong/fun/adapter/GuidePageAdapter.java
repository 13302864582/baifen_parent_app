package com.daxiong.fun.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.config.AppConfig;

import java.util.List;

public class GuidePageAdapter extends PagerAdapter implements OnClickListener {

	public static final int GUIDE_TYPE_LOGIN = 0x1;
	public static final int GUIDE_TYPE_ASK = 0x2;
	public static final int GUIDE_TYPE_PUBLISH_HOMEWORK = 0x3;
	private Context context;
	private List<View> views;
	private OnViewClickListener mOnViewClickListener;
	private int guideType;

	public GuidePageAdapter(Context context, List<View> views, int guideType, OnViewClickListener mOnViewClickListener) {
		this.views = views;
		this.guideType = guideType;
		this.mOnViewClickListener = mOnViewClickListener;
		this.context = context;
	}

	@Override
	public int getCount() {
		if (views != null) {
			return views.size();
		}
		return 0;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(views.get(arg1));
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(views.get(arg1), 0);
		if (arg1 == views.size() - 1) {
			switch (guideType) {
			case GUIDE_TYPE_LOGIN:
				LinearLayout loginBtnsLayout = (LinearLayout) arg0.findViewById(R.id.btns_layout);
				loginBtnsLayout.setVisibility(View.VISIBLE);
				TextView infoTV = (TextView) arg0.findViewById(R.id.login_info_tv);
				infoTV.setVisibility(View.GONE);
				String info = MyApplication.getContext().getResources().getString(R.string.login_guide_info);
				SpannableStringBuilder builder = new SpannableStringBuilder(info);
				ForegroundColorSpan redSpan = new ForegroundColorSpan(MyApplication.getContext().getResources()
						.getColor(R.color.welearn_blue));
				builder.setSpan(redSpan, 12, info.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				infoTV.setText(builder);

				infoTV.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						try {
							Uri uri = Uri.parse(AppConfig.FUDAOTUAN_URL );
							Intent it = new Intent(Intent.ACTION_VIEW, uri);
							context.startActivity(it);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

				Button phoneLogin = (Button) arg0.findViewById(R.id.phone_loginorreg_bt);
				Button qqLogin = (Button) arg0.findViewById(R.id.login_bt);
				phoneLogin.setOnClickListener(this);
				qqLogin.setOnClickListener(this);
				break;
			case GUIDE_TYPE_ASK:
				LinearLayout startToDoAskLayout = (LinearLayout) arg0.findViewById(R.id.start_todo_layout);
				startToDoAskLayout.setVisibility(View.VISIBLE);
				Button todoAskBtn = (Button) arg0.findViewById(R.id.start_todo_btn);
				todoAskBtn.setBackgroundResource(R.drawable.bg_start_ask_btn_selector);
				todoAskBtn.setOnClickListener(this);
				break;
			case GUIDE_TYPE_PUBLISH_HOMEWORK:
				LinearLayout startToDoHomeworkLayout = (LinearLayout) arg0.findViewById(R.id.start_todo_layout);
				startToDoHomeworkLayout.setVisibility(View.VISIBLE);
				Button todoHomeworkBtn = (Button) arg0.findViewById(R.id.start_todo_btn);
				todoHomeworkBtn.setBackgroundResource(R.drawable.bg_start_publish_homework_btn_selector);
				todoHomeworkBtn.setOnClickListener(this);
				break;
			}
		}
		return views.get(arg1);
	}

	@Override
	public void onClick(View v) {
		if (null != mOnViewClickListener) {
			mOnViewClickListener.onSubViewClick(v);
		}
	}

	public interface OnViewClickListener {
		void onSubViewClick(View v);
	}

}
