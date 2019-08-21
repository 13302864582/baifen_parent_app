
package com.daxiong.fun.exception;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.SplashActivity;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.http.volley.VolleyRequestQueueWrapper;
import com.daxiong.fun.util.LogUtil;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ThreadPoolUtil;
import com.daxiong.fun.util.WeLearnFileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CrashHandlerException implements UncaughtExceptionHandler {
	// 系统默认的UncaughtException处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	public static final String TAG = "CatchExcep";

	private MyApplication application;

	// 程序的Context对象
	private Context mContext;

	// 用来存储设备信息和异常信息
	private Map<String, String> infos = new HashMap<String, String>();

	// 用于格式化日期,作为日志文件名的一部分
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	private static final String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

	//private static final String LOG_DIR = SD_PATH + "/.welearn/log/error/";  该路径已经移动到WelearnFileUtil.java 中

	private File logFile;

	public CrashHandlerException(MyApplication application) {
		// 获取系统默认的UncaughtException处理器
		this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		this.application = application;
		this.mContext = application.getApplicationContext();
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// System.out.println("系统崩溃了");
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Log.e(TAG, "error : ", e);
			}
			// restartApp();
		}
	}

	// 重启应用
	public void restartApp() {
		Intent intent = new Intent(application.getApplicationContext(), SplashActivity.class);
		PendingIntent restartIntent = PendingIntent.getActivity(application.getApplicationContext(), 0, intent,PendingIntent.FLAG_ONE_SHOT
				/*Intent.FLAG_ACTIVITY_NEW_TASK*/);
		// 退出程序
		AlarmManager mgr = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
		mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent); // 1秒钟后重启应用
		// application.finishProgram(); // 自定义方法，关闭当前打开的所有avtivity
		System.exit(0);
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		// 使用Toast来显示异常信息
		// new Thread() {
		// @Override
		// public void run() {
		// Looper.prepare();
		// Toast.makeText(application.getApplicationContext(),
		// "很抱歉,程序出现异常,即将退出.",
		// Toast.LENGTH_SHORT).show();
		// Looper.loop();
		// }
		// }.start();
		// 收集设备参数信息
		collectDeviceInfo(mContext);
		// 保存日志文件
		saveCrashInfo2File(ex);
		return true;
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			Log.e(TAG, "an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private String saveCrashInfo2File(Throwable ex) {

		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "<br/>");
		}

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		LogUtil.d("fzlog==="+sb.toString());
		try {

			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String fileName = "crash-" + time + "-" + timestamp + ".txt";

			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				// 得到SD卡中crash目录

				File dir = new File(WeLearnFileUtil.LOG_DIR);
				if (!dir.isDirectory() && !dir.exists()) {
					dir.mkdirs();
				}
				logFile = new File(WeLearnFileUtil.LOG_DIR + "/" + fileName);
				logFile.createNewFile();

				FileOutputStream fos = new FileOutputStream(logFile);
				fos.write(sb.toString().getBytes());
				fos.close();
			}

			ThreadPoolUtil.execute(new Runnable() {
				@Override
				public void run() {
					final String content = WeLearnFileUtil.readFile(logFile.getAbsolutePath(), "utf-8").toString();
					// StuApplication.mNetworkUtil.post(AppConfig.MAIL_URL,
					// content);

					StringRequest request = new StringRequest(Request.Method.POST, AppConfig.MAIL_URL,
							new Response.Listener<String>() {
								@Override
								public void onResponse(String response) {
									String reos = response;
									Log.e("crashHandlerException-->", response);

								}
							}, new Response.ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError error) {
									if (error != null) {
										Log.e("crashHandlerException-->", error.getMessage());
									}

								}

							}) {
						// 重写getParams方法设置参数
						@Override
						protected Map<String, String> getParams() throws AuthFailureError {
							Map<String, String> params = new HashMap<String, String>();
							int ver = MyApplication.versionCode;
							params.put("ver", ver + "");
							params.put("appname", "android_fdt_student");
							String sourcechan = MyApplication.getChannelValue();
							if (TextUtils.isEmpty(sourcechan)) {
								sourcechan = "welearn";
							}
							params.put("sourcechan", sourcechan);
							params.put("tokenid", MySharePerfenceUtil.getInstance().getWelearnTokenId());

							Map<String, Object> subParams = new HashMap<String, Object>();
							subParams.put("topic", "android_student_crash");
							subParams.put("content", content);
							String dataStr = JSON.toJSONString(subParams);
							params.put("data", dataStr);
							return params;
						}

					};

					RequestQueue requestQueue = VolleyRequestQueueWrapper.getInstance(mContext).getRequestQueue();
					requestQueue.add(request);
					requestQueue.start();
				}
			});
			return fileName;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "an error occured while writing file...", e);
		}
		return null;
	}
}
