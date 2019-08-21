
package com.daxiong.fun.api;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.http.volley.VolleyRequestClientAPI;
import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.util.MD5Util;

import java.util.HashMap;
import java.util.Map;

/**
 * 此类的描述： 用户API 
 * @author: Sky
 * @最后修改人： Sky
 * @最后修改日期:2015年7月14日 上午10:09:26
 * @version: 2.0
 */
public class ExampleAPI extends VolleyRequestClientAPI {

    /**
     * executeLogin 此方法描述的是：执行登录操作
     * 
     * @author: Sky
     * @最后修改人： Sky
     * @最后修改日期:2015年7月14日 上午10:07:03
     * @version: 2.0 *
     * @param accountNumber 用户名
     * @param type 登录类型
     * @param password 密码
     * @param listener activity
     * @param requestCode 请求码
     */
    public void executeLogin(RequestQueue queue, String account, String password, final BaseActivity listener, final int requestCode) {
        Map<String, String> subParams = new HashMap<String, String>();
        subParams.put("count", account);
        subParams.put("password", MD5Util.getMD5String(password));
        subParams.put("os", 1 + "");
        subParams.put("phonemodel", DensityUtil.getPhoneModel());
        subParams.put("source", "APP");
        // String dataStr=subParams.toString();
        String dataStr = JSON.toJSONString(subParams);

        // UserInfo us = new UserInfo();
        // us.setCount("180640");
        // us.setPassword(MD5Util.getMD5String("987456321"));
        // String dataStr = "";
        // try {
        // dataStr = new JSONObject(new Gson().toJson(us)).toString();
        // } catch (JSONException e) {
        // e.printStackTrace();
        // }
        // dataStr=dataStr.toString();
        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "/api" + "/user/tellogin", dataStr, listener, requestCode);
    }

}
