package com.daxiong.fun.function.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.function.account.view.ClipImageLayout;
import com.daxiong.fun.util.ImageUtil;

import java.io.File;

/**
 * 
 * 此类的描述： 裁剪图片
 * @author: Sky
 * @最后修改人： Sky
 * @最后修改日期:2015-7-21 下午5:47:34
 * @version: 2.0
 */
public class CropPhotoActivity extends BaseActivity implements ClipImageLayout.IButtonClick {

	private String image_path = "";// 图片路径

	private ClipImageLayout mClipImageLayout;

	@Override
	@SuppressLint("InlinedApi")
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.crop_photo_activity);
		initView();
		getExtra();
		initListener();
	}

	public void initView() {
		mClipImageLayout = (ClipImageLayout) this.findViewById(R.id.id_clipImageLayout);
	}

	public void initListener() {
		mClipImageLayout.setButtonClick(this);
	}

	private void getExtra() {
		Intent intent = getIntent();
		if (intent != null) {
			image_path = intent.getStringExtra("path");
		}
		File file = new File(image_path);
		if (file.exists()) {
			mClipImageLayout.setImagePath(image_path);
		}
	}

	@Override
	public void customClick(Button button) {
		switch (button.getId()) {
		case 0:// 确定
//			Toast.makeText(this, "确定", 4).show();
			execClicpPhoto();
			break;
		case 1:// 取消
//			Toast.makeText(this, "取消", 4).show();
			this.finish();
			break;

		}

	}

	/**
	 * 此方法描述的是：裁剪图片
	 * @author: Sky
	 * @最后修改人： Sky
	 * @最后修改日期:2015-7-22 上午9:44:50 execClicpPhoto void
	 */
	private void execClicpPhoto() {
		Bitmap bitmap = mClipImageLayout.clip();
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//		byte[] datas = baos.toByteArray();
		ImageUtil.saveFile(image_path, bitmap);
		
		Intent intent = new Intent();
		intent.putExtra("path", image_path);
		setResult(RESULT_OK, intent);
		finish();
	}

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        
    }

}
