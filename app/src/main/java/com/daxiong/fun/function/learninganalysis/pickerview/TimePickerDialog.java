package com.daxiong.fun.function.learninganalysis.pickerview;

import java.util.Calendar;

import com.daxiong.fun.R;
import com.daxiong.fun.function.learninganalysis.pickerview.config.PickerConfig;
import com.daxiong.fun.function.learninganalysis.pickerview.data.Type;
import com.daxiong.fun.function.learninganalysis.pickerview.data.WheelCalendar;
import com.daxiong.fun.function.learninganalysis.pickerview.listener.OnDateSetListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by jzxiang on 16/4/19.
 */
public class TimePickerDialog extends PopupWindow implements View.OnClickListener {
	PickerConfig mPickerConfig;
	private TimeWheel mTimeWheel;
	private long mCurrentMillSeconds;
	
	private TimePickerDialog(){}

	private TimePickerDialog(Activity activity, PickerConfig pickerConfig) {
		
		initialize(pickerConfig);
//		mPickerConfig = pickerConfig;
		initView(activity);
	
	}

	private static TimePickerDialog newIntance(PickerConfig pickerConfig) {	
		TimePickerDialog timePickerDialog = new TimePickerDialog();
		timePickerDialog.initialize(pickerConfig);

		return timePickerDialog;
	}

	/*
	 * @Override public void onCreate(@Nullable Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState); Activity activity = getActivity();
	 * activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.
	 * SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	 * 
	 * }
	 */

	/*
	 * @Override public void onResume() { super.onResume(); int height =
	 * getResources().getDimensionPixelSize(R.dimen.picker_height);
	 * 
	 * Window window = getDialog().getWindow();
	 * window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height);//Here!
	 * window.setGravity(Gravity.TOP);
	 * 
	 * }
	 */

	private void initialize(PickerConfig pickerConfig) {
		mPickerConfig = pickerConfig;
	}

	/*
	 * @NonNull
	 * 
	 * @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
	 * Dialog dialog = new Dialog(getActivity(), R.style.Dialog_NoTitle);
	 * dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	 * dialog.setCancelable(true); dialog.setCanceledOnTouchOutside(true);
	 * dialog.setContentView(initView()); return dialog; }
	 */

	 public View initView(Activity activity) {
//		LayoutInflater inflater = LayoutInflater.from(activity);
		  LayoutInflater inflater = (LayoutInflater) activity
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.timepicker_layout, null);
		int h = activity.getWindowManager().getDefaultDisplay().getHeight();
		int w = activity.getWindowManager().getDefaultDisplay().getWidth();

		this.setContentView(view);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(w);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(false);
		this.setOutsideTouchable(false);
		// 刷新状态
		this.update();
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0000000000);
		// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
		this.setBackgroundDrawable(dw);
		// mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);

		TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
		cancel.setOnClickListener(this);
		TextView sure = (TextView) view.findViewById(R.id.tv_sure);
		sure.setOnClickListener(this);
		TextView title = (TextView) view.findViewById(R.id.tv_title);
		View toolbar = view.findViewById(R.id.toolbar);

		title.setText(mPickerConfig.mTitleString);
		cancel.setText(mPickerConfig.mCancelString);
		sure.setText(mPickerConfig.mSureString);
		toolbar.setBackgroundColor(mPickerConfig.mThemeColor);

		mTimeWheel = new TimeWheel(view, mPickerConfig);
		return view;

	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.tv_cancel) {
			dismiss();
		} else if (i == R.id.tv_sure) {
			sureClicked();
		}
	}

	/*
	 * @desc This method returns the current milliseconds. If current
	 * milliseconds is not set, this will return the system milliseconds.
	 * 
	 * @param none
	 * 
	 * @return long - the current milliseconds.
	 */
	public long getCurrentMillSeconds() {
		if (mCurrentMillSeconds == 0)
			return System.currentTimeMillis();

		return mCurrentMillSeconds;
	}

	/*
	 * @desc This method is called when onClick method is invoked by sure
	 * button. A Calendar instance is created and initialized.
	 * 
	 * @param none
	 * 
	 * @return none
	 */
	void sureClicked() {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();

		calendar.set(Calendar.YEAR, mTimeWheel.getCurrentYear());
		calendar.set(Calendar.MONTH, mTimeWheel.getCurrentMonth() - 1);
		calendar.set(Calendar.DAY_OF_MONTH, mTimeWheel.getCurrentDay());
		calendar.set(Calendar.HOUR_OF_DAY, mTimeWheel.getCurrentHour());
		calendar.set(Calendar.MINUTE, mTimeWheel.getCurrentMinute());

		mCurrentMillSeconds = calendar.getTimeInMillis();
		if (mPickerConfig.mCallBack != null) {
			mPickerConfig.mCallBack.onDateSet(this, mCurrentMillSeconds);
		}
		dismiss();
	}

	public static class Builder {
		PickerConfig mPickerConfig;
		Activity activity;

		public Builder() {		
			mPickerConfig = new PickerConfig();
		}

		public Builder setType(Type type) {
			mPickerConfig.mType = type;
			return this;
		}

		public Builder setThemeColor(int color) {
			mPickerConfig.mThemeColor = color;
			return this;
		}

		public Builder setCancelStringId(String left) {
			mPickerConfig.mCancelString = left;
			return this;
		}

		public Builder setSureStringId(String right) {
			mPickerConfig.mSureString = right;
			return this;
		}

		public Builder setTitleStringId(String title) {
			mPickerConfig.mTitleString = title;
			return this;
		}

		public Builder setToolBarTextColor(int color) {
			mPickerConfig.mToolBarTVColor = color;
			return this;
		}

		public Builder setWheelItemTextNormalColor(int color) {
			mPickerConfig.mWheelTVNormalColor = color;
			return this;
		}

		public Builder setWheelItemTextSelectorColor(int color) {
			mPickerConfig.mWheelTVSelectorColor = color;
			return this;
		}

		public Builder setWheelItemTextSize(int size) {
			mPickerConfig.mWheelTVSize = size;
			return this;
		}

		public Builder setCyclic(boolean cyclic) {
			mPickerConfig.cyclic = cyclic;
			return this;
		}

		public Builder setMinMillseconds(long millseconds) {
			mPickerConfig.mMinCalendar = new WheelCalendar(millseconds);
			return this;
		}

		public Builder setMaxMillseconds(long millseconds) {
			mPickerConfig.mMaxCalendar = new WheelCalendar(millseconds);
			return this;
		}

		public Builder setCurrentMillseconds(long millseconds) {
			mPickerConfig.mCurrentCalendar = new WheelCalendar(millseconds);
			return this;
		}

		public Builder setYearText(String year) {
			mPickerConfig.mYear = year;
			return this;
		}

		public Builder setMonthText(String month) {
			mPickerConfig.mMonth = month;
			return this;
		}

		public Builder setDayText(String day) {
			mPickerConfig.mDay = day;
			return this;
		}

		public Builder setHourText(String hour) {
			mPickerConfig.mHour = hour;
			return this;
		}

		public Builder setMinuteText(String minute) {
			mPickerConfig.mMinute = minute;
			return this;
		}

		public Builder setCallBack(OnDateSetListener listener) {
			mPickerConfig.mCallBack = listener;
			this.activity =((com.daxiong.fun.base.BaseFragment)listener).getActivity();
			return this;
		}

		public TimePickerDialog build() {
			return new TimePickerDialog(activity,mPickerConfig);
		}

	}

}
