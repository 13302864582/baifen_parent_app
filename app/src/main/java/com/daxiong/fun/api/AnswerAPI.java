
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
 * 答题api
 * 
 * @author: sky
 */
public class AnswerAPI extends VolleyRequestClientAPI {

    /**
     * 纠错
     * 
     * @author: sky
     * @param queue
     * @param type
     * @param answerid
     * @param content
     * @param listener
     * @param requestCode void
     */
    public void execCorrect(RequestQueue queue, int type, int answerid, String content,
            final BaseFragment listener, final int requestCode) {
        // http://www.welearn.com:8080/api/user/correct
        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("type", type);// 纠错的题型 (1-悬赏问答,2-历年真题,3-作业检查)
        subParams.put("answerid", answerid);
        subParams.put("content", content);
        String dataStr = JSON.toJSONString(subParams);
        requestHttpFragment(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "user/correct", dataStr,
                listener, requestCode);

    }

    /**
     * 得到纠错的状态
     * 
     * @author: sky
     * @param queue
     * @param answerid
     * @param listener
     * @param requestCode void
     */
    public void getCorrectStatus(RequestQueue queue, int answerid, final BaseFragment listener,
            final int requestCode) {
        // http://www.welearn.com:8080/api/user/correctstatus
        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("answerid", answerid);
        String dataStr = JSON.toJSONString(subParams);
        requestHttpFragment(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "user/correctstatus",
                dataStr, listener, requestCode);

    }

    /**
     * 查询作业分析
     * @author: sky
     * @param queue
     * @param subjectid
     * @param datetime
     * @param listener
     * @param requestCode void
     */
    public void queryHomeworkAnalysis(RequestQueue queue, int subjectid, String datetime,
            final BaseActivity listener, final int requestCode) {
        // http://www.welearn.com:8080/api/homework/hwanalyze
        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("subjectid", subjectid);
        subParams.put("datetime", datetime);
        String dataStr = JSON.toJSONString(subParams);
        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "homework/hwanalyze",
                dataStr, listener, requestCode);

    }

}
