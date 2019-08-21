package com.daxiong.fun.http.volley;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * 
 * 此类的描述：RequestQueue重构 包装类
 * @author:  Sky
 * @最后修改人： Sky 
 * @最后修改日期:2015年7月14日 上午11:18:19
 * @version: 2.0
 */
public class VolleyRequestQueueWrapper {

	private  static RequestQueue queue = null;
	private static Context context;
	
	private  VolleyRequestQueueWrapper() {
		super();
	}
	
	
	private static class SingletonHolder{
		private static VolleyRequestQueueWrapper  instance=new VolleyRequestQueueWrapper();
	}

	
	public synchronized  static VolleyRequestQueueWrapper  getInstance(Context ctx){
		context=ctx;
		return SingletonHolder.instance;
	}
	
	/**
	 * 此方法描述的是：得到RequestQueue
	 * @author:  Sky
	 * @最后修改人： Sky 
	 * @最后修改日期:2015年7月14日 上午11:24:06
	 * getRequestQueue
	 * @return RequestQueue
	 */
	public RequestQueue  getRequestQueue(){
		if (null==queue) {
			queue = Volley.newRequestQueue(context);
		}
		return  queue;
	}
	 

}
