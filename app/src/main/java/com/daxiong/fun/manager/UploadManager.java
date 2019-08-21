package com.daxiong.fun.manager;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.model.UploadResult;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.MySharePerfenceUtil;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * 此类的描述：上传图片 
 * @author:  sky
 * @最后修改人： sky
 * @最后修改日期:2015年8月5日 下午5:09:50
 */
public class UploadManager {

	public static final String TAG = UploadManager.class.getSimpleName();
	public static final String UNKNOW_ERROR_MSG = "Unknow Error!";

	public static void upload(String url, List<NameValuePair> params, Map<String, List<File>> files,
			OnUploadListener listener, boolean isMainThread, int index) {
		if (isMainThread) {
			
			UploadTask uploadTask = new UploadTask(url, params, files, listener, index);
			
			uploadTask.execute();
			
		} else {
			String result = null;
			try {
				result = post(url, params, files);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (null != listener) {
				if (null != result) {
					UploadResult ur = new Gson().fromJson(result, UploadResult.class);
					if (ur != null) {
						int code = ur.getCode();
						if (code == 0) {
							listener.onUploadSuccess(ur, index);
						} else {
							listener.onUploadFail(ur, index);
						}
					} else {
						listener.onUploadError(result, index);
					}
				} else {
					listener.onUploadError(UNKNOW_ERROR_MSG, index);
				}
			}
		}
	}

	public static class UploadTask extends AsyncTask<Object, Integer, String> {
		private String url;
		private List<NameValuePair> params;
		private Map<String, List<File>> files;
		private OnUploadListener listener;
		private int index;

		public UploadTask(String url, List<NameValuePair> params, Map<String, List<File>> files, OnUploadListener listener,
				int index) {
			this.url = url;
			this.params = params;
			this.files = files;
			this.listener = listener;
			this.index = index;
		}

		@Override
		protected String doInBackground(Object... arg0) {
			try {
			
			
			return post(url, params, files);	
			} catch (Exception e) {
				e.printStackTrace();
				String msg = null == e ? UNKNOW_ERROR_MSG : e.getMessage();
				if (TextUtils.isEmpty(msg)) {
					msg = UNKNOW_ERROR_MSG;
				}
				return msg;
			}
		}

		@Override
		protected void onPostExecute(String json) {
			
			if (null != listener) {
				if (null != json) {
					UploadResult ur = null;
					 try {
						ur = new UploadResult();
						int code = JsonUtil.getInt(json, "Code", -1);
						String msg = JsonUtil.getString(json, "Msg", "");
						String data = JsonUtil.getString(json, "Data", "");
						ur.setCode(code);
						ur.setData(data);
						ur.setMsg(msg);
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
					}
					
						 
						 if (ur != null) {
							 int code = ur.getCode();
							 if (code == 0) {
								 listener.onUploadSuccess(ur, index);
							 } else {
								 listener.onUploadFail(ur, index);
							 }
						 } else {
							 listener.onUploadError(json, index);
						 }
					 
				} else {
					listener.onUploadError(UNKNOW_ERROR_MSG, index);
				}
			}
		}
	}
   
	private static String post(String url, List<NameValuePair> nameValuePairs, final Map<String, List<File>> files)
			throws Exception {
		
		HttpParams httpParams = new BasicHttpParams();  
        HttpConnectionParams.setConnectionTimeout(httpParams, 3000);  
        HttpConnectionParams.setSoTimeout(httpParams,5000);  
       
        final DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
      
       
        
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader(OkHttpHelper.COOKIE_KEY, OkHttpHelper.WELEARN_SESSION_ID_KEY + "="
				+ MySharePerfenceUtil.getInstance().getWelearnTokenId());
	
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

		for (int index = 0; index < nameValuePairs.size(); index++) {
			entity.addPart(nameValuePairs.get(index).getName(), new StringBody(nameValuePairs.get(index).getValue(),
					Charset.forName(HTTP.UTF_8)));
		}

		if (files != null && files.size() > 0) {
			Set<Entry<String, List<File>>> entries = files.entrySet();
			for (Entry<String, List<File>> entry : entries) {
				String key = entry.getKey();
				List<File> fList = entry.getValue();
				if (null != fList) {
					for (File f : fList) {
						entity.addPart(key, new FileBody(f));
					}
				}
			}
		}

		httpPost.setEntity(entity);
	
		HttpResponse response = httpClient.execute(httpPost, localContext);
		
		return EntityUtils.toString(response.getEntity(), "UTF-8");
	}

	public interface OnUploadListener {
		void onUploadSuccess(UploadResult result, int index);

		void onUploadError(String msg, int index);

		void onUploadFail(UploadResult result, int index);
	}

}
