package com.daxiong.fun.okhttp.builder;

import com.daxiong.fun.okhttp.OkHttpUtils;
import com.daxiong.fun.okhttp.request.OtherRequest;
import com.daxiong.fun.okhttp.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers).build();
    }
}
