package com.daxiong.fun;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends Activity implements OnClickListener/*, OnUploadListener*/ {

	private String uploadFile = "/sdcard/bg.jpg";
	private String actionUrl = "http://192.168.1.100:7001/api/homework/upload";
	private TextView mText1;
	private TextView mText2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);

		mText1 = (TextView) findViewById(R.id.textView1);
		mText1.setText("文件路径：\n" + uploadFile);
		mText2 = (TextView) findViewById(R.id.textView2);
		mText2.setText("上传网址：\n" + actionUrl);

		findViewById(R.id.dialog_ok_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Map<String, File> files = new HashMap<String, File>();
				files.put("file1", new File(uploadFile));
//				UploadManagerxxx.upload(actionUrl, getParam(), files, UploadActivity.this, true, 0);
			}
		});

	}

	final static int BUFFER_SIZE = 4096;

	/**
	 * 将InputStream转换成String
	 * 
	 * @param in
	 *            InputStream
	 * @return String
	 * @throws Exception
	 * 
	 */
	public static String InputStreamTOString(InputStream in) throws Exception {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);

		data = null;
		return new String(outStream.toByteArray(), "UTF-8");
	}

	@Override
	public void onClick(View arg0) {

	}

/*	public static List<NameValuePair> getParam() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String appname = "android";

		params.add(new BasicNameValuePair("ver", "" + "1810"));
		params.add(new BasicNameValuePair("appname", appname));
		params.add(new BasicNameValuePair("sourcechan", "welearn"));
		params.add(new BasicNameValuePair(
				"data",
				"{\"action\": \"add_homework\",\"actionid\": 59, \"picinfo\":{\"seqid\": 1, \"memo\": \"图片说明\", \"width\": 200, \"height\": 200, \"uploadkey\": \"file1\" }}"));

		return params;
	}

	@Override
	public void onUploadSuccess(UploadResult result, int index) {
		ToastUtils.show("onSuccess");		
	}

	@Override
	public void onUploadError(String msg, int index) {
		ToastUtils.show("onError:" + msg);		
	}

	@Override
	public void onUploadFail(UploadResult result, int index) {
		ToastUtils.show("onFail");		
	}*/

}
