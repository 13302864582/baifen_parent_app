
package com.daxiong.fun.http.volley;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.base.BaseFragment;
import com.daxiong.fun.base.BaseFragmentActivity;
import com.daxiong.fun.base.IBaseFragment;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MySharePerfenceUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 此类的描述： volley 底层通信
 *
 * @author: Sky @最后修改人： Sky
 * @最后修改日期:2015年7月14日 上午10:36:23
 * @version: 2.0
 */
public class VolleyRequestClientAPI {

    // 提交方式
    public static final String HTTP_METHOD_POST = "POST";

    public static final String HTTP_METHOD_GET = "GET";

    public static final String APP_NAME = "android_fdt_parents_phone";
    // public static final String APP_NAME = "fdt_head_teacher_web";

    public VolleyRequestClientAPI() {
        super();
    }

    /**
     * 此方法描述的是：封装公共的参数
     *
     * @param params void
     * @author: Sky @最后修改人： Sky
     * @最后修改日期:2015-7-20 下午7:10:08 getBaseParams
     */
    public void getBaseParams(Map<String, String> params) {
        int ver = MyApplication.versionCode;
        params.put("ver", ver + "");
        params.put("appname", APP_NAME);
        String sourcechan = MyApplication.getChannelValue();
        if (TextUtils.isEmpty(sourcechan)) {
            sourcechan = "welearn";
        }
        params.put("sourcechan", sourcechan);
        params.put("tokenid", MySharePerfenceUtil.getInstance().getWelearnTokenId());

    }

    /**
     * 此方法描述的是：适用于Activity请求
     *
     * @param requestMethod
     * @param url
     * @param map
     * @param listener
     * @param requestCode   void
     * @author: Sky @最后修改人： Sky
     * @最后修改日期:2015年7月14日 上午10:26:39
     * @version: 2.0 requestHttpActivity
     */
    public void requestHttpActivity(RequestQueue queue, String requestMethod, final String url, final String dataStr,
                                    final BaseActivity listener, final int requestCode) {
        Log.e("requestHttpActivity url", url);
        StringRequest request = null;
        if (HTTP_METHOD_POST.equals(requestMethod)) {// 如果是post请求
            request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        listener.resultBack(requestCode, response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        if (error != null) {
                            // listener.resultBack(-1);
                            // Log.e("requestHttpActivity-->",
                            // error.getMessage());
                            LogUtils.e("activity VolleyClientAPI onErrorResponse", url + " " + error.getMessage());
                            listener.resultBack(-1, requestCode, error);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }) {
                // 重写getParams方法设置参数
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    getBaseParams(params);
                    params.put("data", dataStr);

                    return params;
                }

                // @Override
                // public Map<String, String> getHeaders() throws
                // AuthFailureError {
                // HashMap<String, String> headers = new HashMap<String,
                // String>();
                // headers.put("Accept", "application/string");
                // headers.put("Content-Type", "application/json;
                // charset=UTF-8");
                // return headers;
                //
                // }

            };

        } else {// 如果是get请求
            request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        listener.resultBack(requestCode, response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        if (error != null) {
                            // listener.resultBack(-1);
                            // Log.e("requestHttpActivity-->",
                            // error.getMessage());

                            LogUtils.e("activity VolleyClientAPI onErrorResponse", url + " " + error.getMessage());
                            listener.resultBack(-1, requestCode, error);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        }
        request.setTag("POST");
        request.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));
        queue.add(request);
        queue.start();
    }

    /**
     * 此方法描述的是：适用于微信预订单请求
     */
    public void requestWXHttpActivity(RequestQueue queue, String requestMethod, final String url, final int userid,
                                      final int fromuserid, final float totalfee, final String body, final int servertype,
                                      final String golangorderid, final BaseFragment listener, final int requestCode) {
        LogUtils.e("requestWXHttpActivity url-->", url);
        StringRequest request = null;
        if (HTTP_METHOD_POST.equals(requestMethod)) {// 如果是post请求
            request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        listener.resultBack(requestCode, response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        if (error != null) {
                            //TODO 曾经报错，历史为-1
                            listener.resultBack(-1);
                            Log.e("requestWXHttpActivity", error.getMessage());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }) {

                // 重写getParams方法设置参数
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    // getBaseParams(params);
                    params.put("userid", userid + "");
                    params.put("fromuserid", fromuserid + "");
                    params.put("totalfee", totalfee + "");
                    params.put("body", body);
                    params.put("servertype", servertype + "");
                    params.put("golangorderid", golangorderid);
                    return params;
                }

            };

        } else {// 如果是get请求
            request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        listener.resultBack(requestCode, response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        if (error != null) {
                            listener.resultBack(-1);
                            Log.e("requestWXHttpActivity", error.getMessage());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        }
        request.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));
        queue.add(request);
        queue.start();
    }

    /**
     * 此方法描述的是：适用于Fragment请求
     *
     * @param requestMethod
     * @param url
     * @param map
     * @param listener
     * @param requestCode   void
     * @author: Sky @最后修改人： Sky
     * @最后修改日期:2015年7月14日 上午10:27:30
     * @version: 2.0 requestHttpFragment
     */
    public void requestHttpFragment(RequestQueue queue, String requestMethod, final String url, final String dataStr,
                                    final BaseFragment listener, final int requestCode) {
        LogUtils.e("requestHttpFragment url-->", url);
        StringRequest request = null;
        if (HTTP_METHOD_POST.equals(requestMethod)) {// 如果是post请求
            request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        LogUtils.e("fragment response-->", response);
                        listener.resultBack(requestCode, response);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        if (error != null) {
                            // listener.resultBack(-1);
                            // Log.e("requestHttpFragment-->",
                            // error.getMessage());
                            LogUtils.e("fragment VolleyClientAPI onErrorResponse", url + " " + error.getMessage());
                            listener.resultBack(-1, requestCode, error);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }) {
                // 重写getParams方法设置参数
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    getBaseParams(params);
                    params.put("data", dataStr);
                    return params;
                }

            };

        } else {// 如果是get请求
            request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        listener.resultBack(requestCode, response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        if (error != null) {
                            // listener.resultBack(-1);
                            // Log.e("requestHttpFragment-->",
                            // error.getMessage());

                            LogUtils.e("fragment VolleyClientAPI onErrorResponse", url + " " + error.getMessage());
                            listener.resultBack(-1, requestCode, error);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        }
        request.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));
        queue.add(request);
        queue.start();
    }

    /**
     * 此方法描述的是：适用于FragmentActivity请求
     *
     * @param requestMethod
     * @param url
     * @param map
     * @param listener
     * @param requestCode   void
     * @author: Sky @最后修改人： Sky
     * @最后修改日期:2015年7月14日 上午10:42:30
     * @version: 2.0 requestHttpFragment
     */
    public void requestHttpFragmentActivity(RequestQueue queue, String requestMethod, final String url,
                                            final String dataStr, final BaseFragmentActivity listener, final int requestCode) {
        LogUtils.e("requestHttpFragmentActivity url-->", url);
        StringRequest request = null;
        if (HTTP_METHOD_POST.equals(requestMethod)) {// 如果是post请求
            request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        listener.resultBack(requestCode, response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        if (error != null) {
                            // listener.resultBack(-1);
                            // Log.e("requestHttpFragmentActivity-->",
                            // error.getMessage());

                            LogUtils.e("FragmentActivity VolleyClientAPI onErrorResponse",
                                    url + " " + error.getMessage());
                            listener.resultBack(-1, requestCode, error);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }) {
                // 重写getParams方法设置参数
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    getBaseParams(params);
                    params.put("data", dataStr);
                    return params;
                }

            };

        } else {// 如果是get请求
            request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        listener.resultBack(requestCode, response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        if (error != null) {
                            // listener.resultBack(-1);
                            // Log.e("requestHttpFragmentActivity-->",
                            // error.getMessage());

                            LogUtils.e("FragmentActivity VolleyClientAPI onErrorResponse",
                                    url + " " + error.getMessage());
                            listener.resultBack(-1, requestCode, error);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        }
        request.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));
        queue.add(request);
        queue.start();
    }

    /**
     * 此方法描述的是：适用于接口请求
     *
     * @param requestMethod
     * @param url
     * @param map
     * @param listener
     * @param requestCode   void
     * @author: Sky @最后修改人： Sky
     * @最后修改日期:2015年7月14日 上午10:41:34 requestHttpInterface
     */
    public void requestHttpInterface(RequestQueue queue, String requestMethod, final String url, final String dataStr,
                                     final IBaseFragment listener, final int requestCode) {
        LogUtils.e("requestHttpInterface url-->", url);
        StringRequest request = null;
        if (HTTP_METHOD_POST.equals(requestMethod)) {// 如果是post请求
            request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        listener.resultBack(requestCode, response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        if (error != null) {
                            // listener.resultBack(-1);
                            // Log.e("requestHttpInterface-->",
                            // error.getMessage());

                            LogUtils.e("Interface VolleyClientAPI onErrorResponse", url + " " + error.getMessage());
                            listener.resultBack(-1, requestCode, error);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }) {
                // 重写getParams方法设置参数
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    getBaseParams(params);
                    params.put("data", dataStr);
                    return params;
                }

            };

        } else {// 如果是get请求
            request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        listener.resultBack(requestCode, response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        if (error != null) {
                            // listener.resultBack(-1);
                            // Log.e("requestHttpInterface-->",
                            // error.getMessage());

                            LogUtils.e("Interface VolleyClientAPI onErrorResponse", url + " " + error.getMessage());
                            listener.resultBack(-1, requestCode, error);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        }
        request.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));
        queue.add(request);
        queue.start();
    }

}
