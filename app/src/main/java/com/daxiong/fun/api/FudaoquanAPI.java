
package com.daxiong.fun.api;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.base.BaseFragment;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.http.volley.VolleyRequestClientAPI;

import java.util.HashMap;
import java.util.Map;

/**
 * 辅导券api
 * 
 * @author: sky
 */
public class FudaoquanAPI extends VolleyRequestClientAPI {

    /**
     * 获取未过期的辅导券
     * 
     * @author: sky
     * @param queue
     * @param type 1难题券 2作业券
     * @param listener
     * @param requestCode void
     */
    public void getFudaoquanList(RequestQueue queue, int type, final BaseActivity listener,
            final int requestCode) {
        // http://www.welearn.com:8080/api/coupon/list
        Map<String, Object> subParams = new HashMap<String, Object>();
        if (type == 1) {
            subParams.put("type", type);
        } else if (type == 2) {
            subParams.put("type", type);
        }
        String dataStr = JSON.toJSONString(subParams);
        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "coupon/list", dataStr,
                listener, requestCode);

    }
    
    /**
     * 获取未过期的辅导券
     * @param queue
     * @param type
     * @param listener
     * @param requestCode
     */
    public void getFudaoquanList(RequestQueue queue, int type, final BaseFragment listener,
            final int requestCode) {
        // http://www.welearn.com:8080/api/coupon/list
        Map<String, Object> subParams = new HashMap<String, Object>();
        if (type == 1) {
            subParams.put("type", type);
        } else if (type == 2) {
            subParams.put("type", type);
        }
        String dataStr = JSON.toJSONString(subParams);
        requestHttpFragment(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "coupon/list", dataStr,
                listener, requestCode);

    }

    /**
     * 获取已经过期的辅导券 包含问题和作业
     * 
     * @author: sky
     * @param queue
     * @param type
     * @param listener
     * @param requestCode void
     */
    public void getExpireFudaoquan(RequestQueue queue, int type, final BaseActivity listener,
            final int requestCode) {
        Map<String, Object> subParams = new HashMap<String, Object>();
        if (type == 0) {
            subParams.put("type", type);
        } else if (type == 1) {
            subParams.put("type", type);
        }
        String dataStr = JSON.toJSONString(subParams);
        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "coupon/expiredlist",
                dataStr, listener, requestCode);

    }

    /**
     * 用辅导券发布问题
     * 
     * @author: sky
     * @param queue
     * @param subjectid
     * @param bounty
     * @param description
     * @param couponid
     * @param listener
     * @param requestCode void
     */
    public void doFudaoquanWithPublishQuestion(RequestQueue queue, String subjectid, int bounty,
            String description, int couponid, final BaseActivity listener, final int requestCode) {
        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("subjectid", subjectid);
        subParams.put("bounty", bounty);
        subParams.put("description", description);
        subParams.put("couponid", couponid);
        String dataStr = JSON.toJSONString(subParams);
        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "question/publish", dataStr,
                listener, requestCode);

    }

    /**
     * 用辅导券发布作业
     * 
     * @author: sky
     * @param queue
     * @param tasktype 任务类型，1:问答，2:作业
     * @param paytype 付费方式，1:悬赏，2:包月，3:自建
     * @param memo 文本描述
     * @param subjectid 科目id
     * @param bounty 赏金
     * @param couponid 用户抽到的辅导券，值为0或者此key不传表示不使用，值大于0则表示使用辅导券
     * @param listener
     * @param requestCode void
     */
    public void doFudaoquanWithPublishHomework(RequestQueue queue, int tasktype, int paytype,
            String memo, int subjectid, int bounty, int couponid, final BaseActivity listener,
            final int requestCode) {
        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("tasktype", tasktype);
        subParams.put("paytype", paytype);
        subParams.put("memo", memo);
        subParams.put("subjectid", subjectid);
        subParams.put("bounty", bounty);
        subParams.put("couponid", couponid);
        String dataStr = JSON.toJSONString(subParams);
        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "homework/publish", dataStr,
                listener, requestCode);

    }
    /**
     * 获取辅导券的信息(版本大于等于3010使用)
     *  type   | int      | 取1表示获取难题券，取2表示获取作业券(必选)
   
     */
    public void getCouponinfos(RequestQueue queue, int type, 
    		final BaseActivity listener, final int requestCode) {
    	
    	Map<String, Object> subParams = new HashMap<String, Object>();
    	subParams.put("type", type);
    	String dataStr = JSON.toJSONString(subParams);
    	requestHttpActivity(queue, HTTP_METHOD_POST,
    			AppConfig.GO_URL + "parents/couponinfos", dataStr, listener, requestCode);
    	
    }
    /**
     * 放弃发布任务
     *  type   | int      | 取1表示难题，取2表示作业(必选)
   
     */
    public void giveuppublish(RequestQueue queue, int type, 
    		final BaseActivity listener, final int requestCode) {
    	
    	Map<String, Object> subParams = new HashMap<String, Object>();
    	subParams.put("type", type);
    	String dataStr = JSON.toJSONString(subParams);
    	requestHttpActivity(queue, HTTP_METHOD_POST,
    			AppConfig.GO_URL + "parents/giveuppublish", dataStr, listener, requestCode);
    	
    }

}
