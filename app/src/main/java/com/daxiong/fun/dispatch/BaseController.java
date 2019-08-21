
package com.daxiong.fun.dispatch;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.daxiong.fun.callback.INetWorkListener;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.MessageConstant;
import com.daxiong.fun.model.BaseModel;

/**
 * 用作控制器
 * 
 * @author parsonswang
 */
public class BaseController {

    private HandlerThread mHandlerThread;

    private Handler mHandler;

    protected BaseModel mModel;

    protected INetWorkListener mListner;

    private String mMsgQueueName;
    
    public void setModel(BaseModel model) {
        this.mModel = model;
    }

    public Handler getHandler() {
        return mHandler;
    }

    private BaseController() {
        mHandlerThread = new HandlerThread(GlobalContant.HANDLER_THREAD_NAME);
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                BaseController.this.handleEventMessage(msg);
            }
        };
    }

    public BaseController(BaseModel model) {
        this();
        this.mModel = model;
        WelearnHandler.getInstance().registDispatcher(dispatcher, mMsgQueueName);
    }

    public BaseController(BaseModel model, INetWorkListener listner, String msgQueueName) {
        this();
        if (model != null) {
            this.mModel = model;
        }
        this.mListner = listner;
        this.mMsgQueueName = msgQueueName;
        WelearnHandler.getInstance().registDispatcher(dispatcher, mMsgQueueName);
    }

    private ImMsgDispatch dispatcher = new ImMsgDispatch() {

        @Override
        public Bundle handleImMsg(Message msg) {
            handleResponseMessage(msg);
            return null;
        }
    };

    protected void handleResponseMessage(Message msg) {
        if ((MessageConstant.MSG_DEF_CONN_TIMEOUT == msg.what
                || MessageConstant.MSG_DEF_SVR_ERROR == msg.what) && mListner != null) {
            mListner.onDisConnect();
            if (MessageConstant.MSG_DEF_CONN_TIMEOUT == msg.what) {
            }
        } else {
            if (msg.obj instanceof String) {
                String jsonStr = (String)msg.obj;
                if (!TextUtils.isEmpty(jsonStr) && mListner != null) {
                    mListner.onAfter(jsonStr, msg.what);
                }
            }
        }
    }

    public void removeMsgInQueue() {
        if (mHandlerThread != null) {
            Looper looper = mHandlerThread.getLooper();
            if (looper != null) {
                looper.quit();
            }
        }
        if (mMsgQueueName == null || dispatcher == null) {
            return;
        }
        WelearnHandler.getInstance().unRegistDispatcher(dispatcher, mMsgQueueName);
    }

    protected void handleEventMessage(Message msg) {
    }


}
