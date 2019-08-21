
package com.daxiong.fun.api;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.base.BaseFragment;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.function.homework.model.StuPublishHomeWorkModel;
import com.daxiong.fun.http.volley.VolleyRequestClientAPI;

import java.util.HashMap;
import java.util.Map;

/**
 * 此类的描述：学习Fragment Api
 * 
 * @author: sky @最后修改人： sky
 * @最后修改日期:2015年8月7日 下午6:46:06
 */
public class StudyAPI extends VolleyRequestClientAPI {

    /**
     * 此方法描述的是：得到学习Fragment的首页数据
     * @author: sky
     * @param queue
     * @param packtype
     * @param pageindex
     * @param pagecount
     * @param listener
     * @param requestCode void
     */
    public void geHall(RequestQueue queue, int packtype, int pageindex, int pagecount,
            final BaseFragment listener, final int requestCode) {
        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("packtype", packtype);// 0代表广场， 1代表发完悬赏问答之后跳转到广场， 2表示我的问集，
                                            // 3表示我的答集
        subParams.put("pageindex", pageindex);
        subParams.put("pagecount", pagecount);
        String dataStr = JSON.toJSONString(subParams);
        requestHttpFragment(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "question/getall", dataStr,
                listener, requestCode);

    }

    /**
     * 此方法描述的是：查询我的机构 适用于Fragment
     * @author: sky
     * @param queue
     * @param type
     * @param pageindex
     * @param pagecount
     * @param listener
     * @param requestCode void
     */
    public void queryMyOrgs(RequestQueue queue, int type, int pageindex, int pagecount,
            final BaseFragment listener, final int requestCode) {
        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("type", type);
        subParams.put("pageindex", pageindex);
        subParams.put("pagecount", pagecount);
        String dataStr = JSON.toJSONString(subParams);
        requestHttpFragment(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "org/myorgs", dataStr,
                listener, requestCode);

    }

    /**
     * 此方法描述的是：重构查询我的机构 适用于Activity
     * @author: sky
     * @param queue
     * @param type
     * @param pageindex
     * @param pagecount
     * @param listener
     * @param requestCode void
     */
    public void queryMyOrgs(RequestQueue queue, int type, int pageindex, int pagecount,
            final BaseActivity listener, final int requestCode) {
        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("type", type);
        subParams.put("pageindex", pageindex);
        subParams.put("pagecount", pagecount);
        String dataStr = JSON.toJSONString(subParams);
        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "org/myorgs", dataStr,
                listener, requestCode);

    }

    /**
     * 此方法描述的是：得到特殊账号的列表
     * @author: sky
     * @param queue
     * @param orgid
     * @param listener
     * @param requestCode void
     */
    public void GetSpecialStudentList(RequestQueue queue, int orgid, final BaseActivity listener,
            final int requestCode) {
        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("orgid", orgid);
        String dataStr = JSON.toJSONString(subParams);
        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "org/specialstudentslist",
                dataStr, listener, requestCode);

    }
    
    
    
    /**
     * 此方法描述的是：得到特殊学生的权限列表
     * @author:  sky
     * @param queue
     * @param orgid
     * @param userid
     * @param listener
     * @param requestCode void
     */
    public void GetSpecialStudentPermissionList(RequestQueue queue, int orgid, final BaseActivity listener,
            final int requestCode) {
        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("orgid", orgid);
        String dataStr = JSON.toJSONString(subParams);
        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "org/waibaostudentpermission",
                dataStr, listener, requestCode);

    }
    
    
    
   /******************************************************************************************************************/

    /**
     * 此方法描述的是：代理发作业
     * @author: Sky 
     * @param queue
     * @param account
     * @param password
     * @param listener
     * @param requestCode void
     */
    public void executeAgentPublicWorkHome(RequestQueue queue, StuPublishHomeWorkModel entity,
            final BaseActivity listener, final int requestCode) {
        // String dataStr=subParams.toString();
        String dataStr = JSON.toJSONString(entity);
        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "org/hwpublish", dataStr,
                listener, requestCode);

    }

    /**
     * 此方法描述的是：执行完代理发布作业之后，上传图片之后的调用此接口
     * @author: sky 
     * @param queue
     * @param taskid
     * @param listener
     * @param requestCode void
     */
    public void executeAgentPublicWorkHomeFinish(RequestQueue queue, String taskid,
            final BaseActivity listener, final int requestCode) {
        // String dataStr=subParams.toString();
        Map<String, String> subParams = new HashMap<String, String>();
        subParams.put("taskid", taskid);
        String dataStr = JSON.toJSONString(subParams);
        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "org/hwuploadfinish",
                dataStr, listener, requestCode);

    }

}
