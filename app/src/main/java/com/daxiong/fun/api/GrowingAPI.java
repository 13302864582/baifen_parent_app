
package com.daxiong.fun.api;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.daxiong.fun.base.BaseFragment;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.http.volley.VolleyRequestClientAPI;

import java.util.HashMap;
import java.util.Map;

public class GrowingAPI extends VolleyRequestClientAPI {

    public void getGrowing(RequestQueue queue, final BaseFragment listener, final int requestCode) {
        // http://www.welearn.com:8080/api/parents/growentrance
        Map<String, Object> subParams = new HashMap<String, Object>();
        String dataStr = JSON.toJSONString(subParams);
        requestHttpFragment(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/growentrance",
                dataStr, listener, requestCode);

    }

}
