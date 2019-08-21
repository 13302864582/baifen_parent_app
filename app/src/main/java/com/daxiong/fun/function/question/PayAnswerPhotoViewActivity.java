package com.daxiong.fun.function.question;

import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.WindowManager;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.manager.IntentManager;

public class PayAnswerPhotoViewActivity extends BaseActivity {

	private PayAnswerPhotoViewFragment mFragment;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}

		setContentView(R.layout.activity_main);
		FragmentManager fm = getSupportFragmentManager();
		Fragment f = fm.findFragmentById(R.id.frameContainer);
		if (f == null) {
			f = createFragment();
			fm.beginTransaction().add(R.id.frameContainer, f).commit();
		}
	}

	protected Fragment createFragment() {
		mFragment = new PayAnswerPhotoViewFragment();
		return mFragment;
	}

	@Override
	public void onBackPressed() {
		if (null != mFragment) {
			mFragment.goToPrevious(true);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GlobalContant.CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK && null != mFragment) {
//				mFragment.recycleBitmap();
//				mFragment.showPhoto();
				Bundle bData = new Bundle();
				bData.putBoolean("isFromPhotoList", false);
				bData.putString(PayAnswerImageGridActivity.IMAGE_PATH, mFragment.path);
				IntentManager.goToPhotoView(this, bData );
			}
		}
	}

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        
    }
}
