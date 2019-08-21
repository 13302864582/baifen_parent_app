
package com.daxiong.fun.api;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.base.BaseFragment;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.http.volley.VolleyRequestClientAPI;

import java.util.HashMap;
import java.util.Map;

public class HomeListAPI extends VolleyRequestClientAPI {

    /**
     * 索券
     */
    public void orgsendcoupon(RequestQueue queue,
                              final BaseFragment listener, int orgid, final int requestCode) {
        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("orgid", orgid);

        String dataStr = JSON.toJSONString(subParams);

        requestHttpFragment(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/orgsendcoupon",
                dataStr, listener, requestCode);

    }

    /**
     * 索券列表
     */
    public void orgsendcouponlist(RequestQueue queue,
                                  final BaseFragment listener, int page, int count, final int requestCode) {
        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("page", page);
        subParams.put("count", count);
        String dataStr = JSON.toJSONString(subParams);

        requestHttpFragment(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/orgsendcouponlist",
                dataStr, listener, requestCode);

    }

    /**
     * 可用券列表
     */
    public void newlist(RequestQueue queue,
                        final BaseFragment listener, int page, int count, final int requestCode) {
        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("page", page);
        subParams.put("count", count);
        String dataStr = JSON.toJSONString(subParams);

        requestHttpFragment(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "coupon/newlist",
                dataStr, listener, requestCode);

    }

    /**
     * 过期券列表
     */
    public void newexpiredlist(RequestQueue queue,
                               final BaseFragment listener, int page, int count, final int requestCode) {
        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("page", page);
        subParams.put("count", count);
        String dataStr = JSON.toJSONString(subParams);
        requestHttpFragment(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "coupon/newexpiredlist",
                dataStr, listener, requestCode);

    }

    /**
     * 聊天置顶
     */
    public void topchats(RequestQueue queue,
                         final BaseFragment listener, final int requestCode) {
        Map<String, Object> subParams = new HashMap<String, Object>();

        String dataStr = JSON.toJSONString(subParams);
        requestHttpFragment(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/topchats",
                dataStr, listener, requestCode);

    }

    /**
     * 首页信息
     */
    public void homeListContext(RequestQueue queue, int page, int count,
                                final BaseFragment listener, final int requestCode) {
        Map<String, Object> subParams = new HashMap<String, Object>();

        subParams.put("page", page);
        subParams.put("count", count);
        String dataStr = JSON.toJSONString(subParams);
        requestHttpFragment(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/newhomepageinfos",
                dataStr, listener, requestCode);

    }

    /**
     * 班主任信息
     */
    public void teacherContext(RequestQueue queue,
                               final BaseFragment listener, final int requestCode) {
        Map<String, Object> subParams = new HashMap<String, Object>();

        String dataStr = JSON.toJSONString(subParams);
        requestHttpFragment(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/headteacherinfos",
                dataStr, listener, requestCode);

    }

    /**
     * 日历信息
     */
    public void calendarInfo(RequestQueue queue, int year, int month, final BaseFragment listener,
                             final int requestCode) {
        // http://www.welearn.com:8080/api/parents/calendarinfo
        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("year", year);
        subParams.put("month", month);
        String dataStr = JSON.toJSONString(subParams);
        requestHttpFragment(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/calendarinfo",
                dataStr, listener, requestCode);

    }

    /**
     * 日历列表
     *
     * @param queue
     * @param querytime
     * @param count
     * @param listener
     * @param requestCode void
     * @author: sky
     */
    public void getSomeDayCalendarList(RequestQueue queue, long querytime, int count, final BaseFragment listener,
                                       final int requestCode) {
        // http://www.welearn.com:8080/api/parents/somedayquestioninfo
        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("querytime", querytime);
        subParams.put("count", count);
        String dataStr = JSON.toJSONString(subParams);
        requestHttpFragment(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/somedayquestioninfo",
                dataStr, listener, requestCode);

    }

    /**
     * 系统当前时间
     */
    public void getSystemTime(RequestQueue queue, final BaseActivity listener,
                              final int requestCode) {
        Map<String, Object> subParams = new HashMap<String, Object>();

        String dataStr = JSON.toJSONString(subParams);
        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "common/getservertime", dataStr, listener, requestCode);

    }

    /**
     * 讲解反馈理由
     */
    public void getExplainfeedbackreasons(RequestQueue queue, final BaseActivity listener,
                                          final int requestCode) {
        Map<String, Object> subParams = new HashMap<String, Object>();

        String dataStr = JSON.toJSONString(subParams);
        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/explainfeedbackreasons", dataStr, listener, requestCode);

    }

    /**
     * 获取老师信息
     */
    public void getteacherinfos(RequestQueue queue, final BaseActivity listener, int type, int teacherid, int comment_page, int comment_count, final int requestCode) {
        Map<String, Object> subParams = new HashMap<String, Object>();

        subParams.put("type", type);
        subParams.put("teacherid", teacherid);
        subParams.put("comment_page", comment_page);
        subParams.put("comment_count", comment_count);
        String dataStr = JSON.toJSONString(subParams);


        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/teacherinfos", dataStr, listener, requestCode);

    }

    /**
     * 喜欢老师讲解
     */
    public void liketeacherexplain(RequestQueue queue, final BaseActivity listener, int teacherid, final int requestCode) {
        Map<String, Object> subParams = new HashMap<String, Object>();


        subParams.put("teacherid", teacherid);

        String dataStr = JSON.toJSONString(subParams);


        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/liketeacherexplain", dataStr, listener, requestCode);

    }
  /**
     * 不喜欢老师讲解
     */
    public void desertteacherexplain(RequestQueue queue, final BaseActivity listener, int teacherid, final int requestCode) {
        Map<String, Object> subParams = new HashMap<String, Object>();


        subParams.put("teacherid", teacherid);

        String dataStr = JSON.toJSONString(subParams);


        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/desertteacherexplain", dataStr, listener, requestCode);

    }

    /**
     * 提交讲解反馈理由
     */
    public void explainfeedbackreasonscommit(RequestQueue queue, String reasons, int checkpointid, final BaseActivity listener,
                                             final int requestCode) {

        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("reasons", reasons);
        subParams.put("checkpointid", checkpointid);
        String dataStr = JSON.toJSONString(subParams);
        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/explainfeedbackreasonscommit",
                dataStr, listener, requestCode);

    }

}
