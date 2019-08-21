package com.daxiong.fun.function.question;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;

public class PayAnswerTextAnswerActivity extends BaseActivity {

	public static final String ANSWER_TEXT = "answer_text";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// mActionBar.hide();

		View view = View.inflate(this, R.layout.fragment_text_answer, null);
		setContentView(view);

		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TextView answerText = (TextView) view.findViewById(R.id.text_answer_text);
		answerText.setText(getIntent().getStringExtra(ANSWER_TEXT));
	}

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        
    }
}
