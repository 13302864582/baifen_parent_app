
package com.daxiong.fun.api;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.function.homework.model.StuPublishHomeWorkModel;
import com.daxiong.fun.http.volley.VolleyRequestClientAPI;

import java.util.HashMap;
import java.util.Map;

public class HomeWorkAPI extends VolleyRequestClientAPI {

    /**
     * 此方法描述的是：得到作业大厅的列表
     * 
     * @author: sky geHomeworkHall
     * @param queue
     * @param packtype
     * @param pageindex
     * @param pagecount
     * @param userid
     * @param listener
     * @param requestCode void
     */
    public void geHomeworkHall(RequestQueue queue, int packtype, int pageindex, int pagecount,
            int userid, final BaseActivity listener, final int requestCode) {
        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("packtype", packtype);
        subParams.put("pageindex", pageindex);
        subParams.put("pagecount", pagecount);
        if (packtype == 4) {
            subParams.put("userid", userid);
        }
        if (packtype == 6) {
            subParams.put("userid", userid);
        }
        String dataStr = JSON.toJSONString(subParams);
        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "homework/getall", dataStr,
                listener, requestCode);

    }

    public void getHomeworkList(RequestQueue queue, int pageindex, int pagecount,
            final BaseActivity listener, final int requestCode) {
        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("page", pageindex);
        subParams.put("count", pagecount);

        String dataStr = JSON.toJSONString(subParams);
        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/myhomeworks",
                dataStr, listener, requestCode);

    }

    /**
     * 此方法描述的是：外包普通发布作业
     * 
     * @author: sky
     * @param queue
     * @param pm
     * @param listener
     * @param requestCode void
     */
    public void waibaoPutongPublishHomework(RequestQueue queue, StuPublishHomeWorkModel pm,
            final BaseActivity listener, final int requestCode) {
        String dataStr = "";
        if (pm != null) {
            dataStr = JSON.toJSONString(pm);
        }
        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "homework/publish", dataStr,
                listener, requestCode);

    }

    /**
     * 此方法描述的是：外包普通发布作业上传完成第三步
     * 
     * @author: sky
     * @param queue
     * @param taskid
     * @param listener
     * @param requestCode void
     */
    public void waibaoPutongPublishHomeworkUploadFinish(RequestQueue queue, int taskid,
            final BaseActivity listener, final int requestCode) {
        String dataStr = "";
        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("taskid", taskid);
        dataStr = JSON.toJSONString(subParams);
        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "homework/uploadfinish",
                dataStr, listener, requestCode);

    }

    /**
     * 此方法描述的是：外包发作业(还没放上去)
     * 
     * @author: sky
     * @param queue
     * @param pm
     * @param listener
     * @param requestCode void
     */
    public void waibaoTeshuPublishHomework(RequestQueue queue, StuPublishHomeWorkModel pm,
            final BaseActivity listener, final int requestCode) {
        String dataStr = "";
        if (pm != null) {
            dataStr = JSON.toJSONString(pm);
        }
        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "org/hwpublish", dataStr,
                listener, requestCode);

    }

    /**
     * 家长发布作业
     * 
     * @author: sky
     * @param queue
     * @param subjectid
     * @param memo
     * @param listener
     * @param requestCode void
     */
    public void publishHwStepOne(RequestQueue queue, StuPublishHomeWorkModel model,
            final BaseActivity listener, final int requestCode) {
        // http://www.welearn.com:8080/api/parents/homeworkpublish
        String dataStr = "";
        if (model != null) {
            dataStr = JSON.toJSONString(model);
        }
        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/homeworkpublish",
                dataStr, listener, requestCode);

    }

   
    /**
     * 家长发布作业获取
     * 
     * @author: sky
     * @param queue
     * @param subjectid
     * @param memo
     * @param listener
     * @param requestCode void
     */
    public void publishHwFinish(RequestQueue queue, int taskid, String memo,
            final BaseActivity listener, final int requestCode) {
        // http://www.welearn.com:8080/api/parents/homeworkuploadfinish
        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("taskid", taskid);
        String dataStr = JSON.toJSONString(subParams);
        requestHttpActivity(queue, HTTP_METHOD_POST,
                AppConfig.GO_URL + "parents/homeworkuploadfinish", dataStr, listener, requestCode);

    }

    /****************************************** 华丽的分割线 ************************************************************************************************/
    /**
     * 查看作业分析(点评)
     * @author: sky
     * @param queue
     * @param taskid
     * @param listener
     * @param requestCode void
     */
    public void viewremark(RequestQueue queue, int taskid, final BaseActivity listener,
            final int requestCode) {
        // http://www.welearn.com:8080/api/parents/viewremark
        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("taskid", taskid);
        String dataStr = JSON.toJSONString(subParams);
        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/viewremark",
                dataStr, listener, requestCode);

    }
}
