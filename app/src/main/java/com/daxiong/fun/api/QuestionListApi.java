
package com.daxiong.fun.api;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.http.volley.VolleyRequestClientAPI;

import java.util.HashMap;
import java.util.Map;

/**
 * 此类的描述：请求问题列表 Api
 * 
 * 
 */
public class QuestionListApi extends VolleyRequestClientAPI {

	public void getQuestionList(RequestQueue queue, int pageindex, int pagecount, final BaseActivity listener,
			final int requestCode) {
		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("page", pageindex);
		subParams.put("count", pagecount);
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/myquestions", dataStr, listener,
				requestCode);

	}

}
