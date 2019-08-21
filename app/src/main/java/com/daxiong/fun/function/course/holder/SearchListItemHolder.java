package com.daxiong.fun.function.course.holder;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.function.course.model.SearchListItemModel;

public class SearchListItemHolder extends BaseHolder<SearchListItemModel>{

	private String mSerachText;
	
	private TextView tv_item1;
	private TextView tv_item2;
	private TextView tv_item3;

	@Override
	public View initView() {
		View view = View.inflate(MyApplication.getContext(), R.layout.search_lv_itme, null);
		tv_item1 = (TextView) view.findViewById(R.id.tv_item1);
		tv_item2 = (TextView) view.findViewById(R.id.tv_item2);
		tv_item3 = (TextView) view.findViewById(R.id.tv_item3);
		return view;
	}

	@Override
	public void refreshView() {
		int indexOf = -1;
		SearchListItemModel data = getData();
		
		String grade = data.getGrade();
		indexOf = grade.indexOf(mSerachText);
		if (indexOf == -1) {
			tv_item1.setText(grade);
		} else {
			tv_item1.setText(addStyle(grade, indexOf, indexOf + mSerachText.length()));
		}

		String subject = data.getSubject();
		indexOf = subject.indexOf(mSerachText);
		if (subject.indexOf(mSerachText) == -1) {
			tv_item2.setText(subject);
		} else {
			tv_item2.setText(addStyle(subject, indexOf, indexOf + mSerachText.length()));
		}

		String coursename = data.getCoursename();
		indexOf = coursename.indexOf(mSerachText);
		if (coursename.indexOf(mSerachText) == -1) {
			tv_item3.setText(coursename);
		} else {
			tv_item3.setText(addStyle(coursename, indexOf, indexOf + mSerachText.length()));
		}
	}

	public void setSerachText(String serachText) {
		if(null == serachText){
			mSerachText = "";
		}else{
			mSerachText = serachText;
		}
	}
	
	/**[a,b);*/
	private Spannable addStyle(String text, int start, int end) {
		Spannable spannable = new SpannableString(text);
		spannable.setSpan(
				new ForegroundColorSpan(MyApplication.getContext().getResources().getColor(R.color.search_text_hint)),
				start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		return spannable;
	}
}
