package com.daxiong.fun.manager;

import com.daxiong.fun.okhttp.OkHttpUtils;
import com.daxiong.fun.okhttp.builder.PostFormBuilder;
import com.daxiong.fun.okhttp.callback.StringCallback;
import com.daxiong.fun.util.ToastUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 上传图片工具manager Okhttp
 */
public class UploadManager2 {

	public static final String TAG = UploadManager2.class.getSimpleName();

	public static void upload(String url, Map<String, String> params, Map<String, List<File>> files,
							  StringCallback listener, boolean isMainThread, int index) {

		try {
			PostFormBuilder builder = OkHttpUtils.post();

			if (files != null && files.size() > 0) {
				Set<Map.Entry<String, List<File>>> entries = files.entrySet();
				for (Map.Entry<String, List<File>> entry : entries) {
					String key = entry.getKey();
					List<File> fList = entry.getValue();
					if (null != fList) {
						for (File f : fList) {
							if (!f.exists()) {
								ToastUtils.show("文件不存在，请修改文件路径");
								return;
							}
							builder.addFile(key, key, f);
						}
					}
				}
			}

			builder.url(url).params(params)//
					.build()//
					.connTimeOut(15000).writeTimeOut(30000).readTimeOut(30000).execute(listener);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


}
