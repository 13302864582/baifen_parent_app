
package com.daxiong.fun.util;

import android.os.Handler;
import android.os.Message;

/**
 * myAsyncTask工具类
 * 
 * @author: sky
 */
public abstract class MyAsyncTask {
    public static final int MYASYNC = 215;

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MYASYNC) {
                postTask();
            }
        };
    };

    public abstract void preTask();

    public abstract void doInBack();

    public abstract void postTask();

    public void excute() {
        preTask();
        ThreadPoolUtil.execute(new Runnable() {

            @Override
            public void run() {
                doInBack();
                handler.sendEmptyMessage(MYASYNC);

            }
        });

    }
}
