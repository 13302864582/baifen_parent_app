
package com.daxiong.fun.api;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.http.volley.VolleyRequestClientAPI;

import java.util.HashMap;
import java.util.Map;

/**
 * 此类的描述：历年考题API
 * 
 * @author: sky
 */
public class BeyondYeahExamAPI extends VolleyRequestClientAPI {

    
    /**
     * 此方法描述的是：根据关键字搜索
     * @author:  sky
     * @param queue
     * @param page
     * @param pagenum
     * @param pagecount
     * @param keyword
     * @param listener
     * @param requestCode void
     */
    public void keySearchResult(RequestQueue queue, int page, int pagenum,
            String keyword, final BaseActivity listener, final int requestCode) {
        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("page", page);
        subParams.put("pagenum", pagenum);
        subParams.put("keyword", keyword);
        String dataStr = JSON.toJSONString(subParams);
        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "question/librarysearch", dataStr,
                listener, requestCode);

    }
    
    
    
    public void knowledgeSearchResult(RequestQueue queue, int q_type, int pageIndex, int page_count,
            int gradeid, int subjectGroupId,int chapterGroupId,int knowPointGroupId,final BaseActivity listener, final int requestCode) {
        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("q_type", q_type);// #0代表一题多解 1代表收藏
        subParams.put("page", pageIndex);
        subParams.put("pagenum", page_count);
        subParams.put("grade", gradeid);
        subParams.put("subject", subjectGroupId);
        subParams.put("chapterid", chapterGroupId);
        subParams.put("pointid", knowPointGroupId);
        String dataStr = JSON.toJSONString(subParams);
        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "question/library", dataStr,
                listener, requestCode);

    }

}
