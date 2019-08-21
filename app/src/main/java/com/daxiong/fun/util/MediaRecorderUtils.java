package com.daxiong.fun.util;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class MediaRecorderUtils {
	private Context context;

	private MediaRecorder recorder;
	private String path;
	
	public static final String TAG = MediaRecorder.class.getSimpleName();

	public MediaRecorderUtils(String path, Context context) {
		this.path = sanitizePath(path);
		this.context = context;
	}

	private String sanitizePath(String path) {
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		if (!path.contains(".")) {
			path += ".amr";
		}
		return WeLearnFileUtil.VOICE_PATH + path;
	}

	public boolean start() throws IOException {
		if (!WeLearnFileUtil.sdCardIsAvailable()) {
			ToastUtils.show("SD卡不存在，请插入SD卡!");
			throw new IOException("SD Card is not mounted,It is  " + Environment.getExternalStorageState() + ".");
		} 	
		if (!WeLearnFileUtil.sdCardHasEnoughSpace()) {
			ToastUtils.show("SD卡空间按不足!");
			return false;
		}
		File directory = new File(path).getParentFile();
		if (!directory.exists() && !directory.mkdirs()) { 
			throw new IOException("Path to file could not be created"); 
		}
		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
		recorder.setOutputFile(path);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOnInfoListener(null);
		recorder.setOnErrorListener(null);
		try {
			recorder.prepare();
		} catch (IllegalStateException e) {
			LogUtils.e(TAG, "prepare failed");
		}
		recorder.start();
		return true;
	}   

	public void stop(double voiceValue) throws IOException {
		voiceValue = 0.0;
		if (recorder != null) {
			recorder.stop();
			recorder.reset();
			recorder.release();
			recorder = null;
		}
	}
	
	public double getAmplitude() {		
		if (recorder != null) {			
			try {
				return recorder.getMaxAmplitude();
			} catch (IllegalStateException e) {
				return 0;
			}		
		} else	{
			return 0;
		}		
	}
}