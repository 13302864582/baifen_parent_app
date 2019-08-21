package com.daxiong.fun.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class MyVipScrollview  extends ScrollView{

	public MyVipScrollview(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
                MeasureSpec.AT_MOST);  
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
