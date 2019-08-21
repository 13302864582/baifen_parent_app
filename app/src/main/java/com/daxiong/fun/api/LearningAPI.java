
package com.daxiong.fun.api;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.daxiong.fun.base.BaseFragment;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.http.volley.VolleyRequestClientAPI;

import java.util.HashMap;
import java.util.Map;

/**
 * 学情api
 * @author Administrator
 *
 */
public class LearningAPI extends VolleyRequestClientAPI {

    /**
     * 查询作业分析
     * 
     * @author: sky
     * @param queue
     * @param subjectid
     * @param datetime
     * @param listener
     * @param requestCode void
     */
    public void queryHomeworkAnalysis(RequestQueue queue, int subjectid, String datetime,
            final BaseFragment listener, final int requestCode) {
        // http://www.welearn.com:8080/api/homework/hwanalyze
        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("subjectid", subjectid);
        subParams.put("datetime", datetime);
        String dataStr = JSON.toJSONString(subParams);
        requestHttpFragment(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "homework/hwanalyze",
                dataStr, listener, requestCode);

    }

    /**
     * 学情分析轮播图
     * 
     * @author: sky
     * @param queue
     * @param listener
     * @param requestCode void
     */
    public void getPromotionPic(RequestQueue queue, final BaseFragment listener,
            final int requestCode) {
        // http://www.welearn.com:8080/api/parents/getpromotionpic
        Map<String, Object> subParams = new HashMap<String, Object>();
        String dataStr = JSON.toJSONString(subParams);
        requestHttpFragment(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/getpromotionpic",
                dataStr, listener, requestCode);

    }

    /**
     * 得到学情报告的列表
     * @author:  sky
     * @param queue
     * @param pageindex
     * @param pagecount
     * @param listener
     * @param requestCode void
     */
    public void getLearningReportList(RequestQueue queue,int pageindex,int pagecount, final BaseFragment listener,
            final int requestCode) {
        // http://www.welearn.com:8080/api/parents/learningreportlist
        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("pageindex", pageindex);
        subParams.put("pagecount", pagecount);
        String dataStr = JSON.toJSONString(subParams);
        requestHttpFragment(queue, HTTP_METHOD_POST,
                AppConfig.GO_URL + "parents/learningreportlist", dataStr, listener, requestCode);

    }

}
