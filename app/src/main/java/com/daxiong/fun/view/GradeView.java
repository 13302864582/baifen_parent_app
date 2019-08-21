package com.daxiong.fun.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.daxiong.fun.R;

public class GradeView extends RelativeLayout {

	private OnGradeChildClickListener mOnGradeChildClickListener;

	private Context context;
	private int index;
	private TextView titleTV;
	private TableLayout groupLayout;
	private LinearLayout.LayoutParams lp;
	private TableRow tr;

	public GradeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		setUpViews();
	}

	public GradeView(Context context) {
		super(context);
		this.context = context;
		setUpViews();
	}

	@SuppressLint("InflateParams")
	private void setUpViews() {
		View v = LayoutInflater.from(getContext()).inflate(R.layout.view_grade_item, null);
		titleTV = (TextView) v.findViewById(R.id.grade_title_tv);
		groupLayout = (TableLayout) v.findViewById(R.id.grade_group_layout);
		addView(v);

		lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(100, 0, 100, 0);
		lp.gravity = Gravity.CENTER;

		tr = new TableRow(context);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setGradeTitle(String title) {
		if (null != title) {
			titleTV.setText(title);
		}
	}

	public void setOnGradeChildClickListener(OnGradeChildClickListener mOnGradeChildClickListener) {
		this.mOnGradeChildClickListener = mOnGradeChildClickListener;
	}

	public void addItem(int id, String name, int childIndex) {
		boolean isNewRow = (childIndex) % 3 == 0;
		if (isNewRow) {
			tr = new TableRow(context);
			tr.setPadding(0, 10, 0, 10);
		}

		TextView tv = new TextView(context);
		tv.setId(id);
		tv.setText(name);
		tv.setPadding(30, 15, 30, 15);
		tv.setTextSize(16);
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (null != mOnGradeChildClickListener) {
					mOnGradeChildClickListener.onChildClicked(index, view.getId());
				}
				resetViewsState();
				TextView selectedTV = (TextView) view;
				selectedTV.setTextColor(getResources().getColor(android.R.color.white));
				selectedTV.setBackgroundColor(getResources().getColor(R.color.grade_text_selected));
			}
		});

		tr.addView(tv);

		if (isNewRow) {
			groupLayout.addView(tr);
		}
	}

	public void resetViewsState() {
		for (int i = 0; i < groupLayout.getChildCount(); i++) {
			TableRow tr = (TableRow) groupLayout.getChildAt(i);
			for (int j = 0; j < tr.getChildCount(); j++) {
				TextView tv = (TextView) tr.getChildAt(j);
				tv.setTextColor(getResources().getColor(R.color.grade_normal));
				tv.setBackgroundColor(getResources().getColor(android.R.color.white));
			}
		}
	}

	public interface OnGradeChildClickListener {
		void onChildClicked(int index, int id);
	}
}
