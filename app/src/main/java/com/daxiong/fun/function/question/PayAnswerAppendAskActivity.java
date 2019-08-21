package com.daxiong.fun.function.question;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;

import java.lang.reflect.Method;

/**
 * 此类的描述： 追问
 * @author:  sky
 */
public class PayAnswerAppendAskActivity extends BaseActivity {

	private PayAnswerAppendAskFragment mFragment;
	FrameLayout frameContainer;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		

		setContentView(R.layout.activity_main);
		frameContainer=(FrameLayout) findViewById(R.id.frameContainer);
		// getDpi() ;
		FragmentManager fm = getSupportFragmentManager();
		Fragment f /*= fm.findFragmentById(R.id.frameContainer);
		if (f == null) {
			f*/ = createFragment();
			fm.beginTransaction().add(R.id.frameContainer, f).commit();
//		}
	}
	private int getDpi() {
        int dpi = 0;

        Display display = getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        DisplayMetrics dm = new DisplayMetrics();

        @SuppressWarnings("rawtypes")

        Class c;

        try {

            c = Class.forName("android.view.Display");

            @SuppressWarnings("unchecked")

            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);

            method.invoke(display, dm);

            dpi = dm.heightPixels;
            if ((dpi - height) > 10) {
            	frameContainer.setLayoutParams(
                        new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;

    }
	@Override
	public void onBackPressed() {
		if (null != mFragment) {
			mFragment.goToPrevious(true);
		}
	}

	private PayAnswerAppendAskFragment createFragment() {
		if (null == mFragment) {
			mFragment = new PayAnswerAppendAskFragment();
		}
		return mFragment;
	}

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        
    }
}
