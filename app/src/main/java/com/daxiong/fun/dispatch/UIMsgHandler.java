
package com.daxiong.fun.dispatch;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.daxiong.fun.util.LogUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * UI更新通知handler.该handler向各注册UI下发IM消息
 */
public class UIMsgHandler extends Handler {

    private ConcurrentHashMap<String, ImMsgDispatch> msgDispatchers;

    private static final String TAG = UIMsgHandler.class.getSimpleName();

    public UIMsgHandler() {
        msgDispatchers = new ConcurrentHashMap<String, ImMsgDispatch>();
    }

    public void registerObserver(ImMsgDispatch dispatcher, String key) {
        if (!isRegisterObserver(dispatcher, key)) {
            LogUtils.i(TAG, "---registerObserver---");
            msgDispatchers.put(key, dispatcher);
        }
    }

    public void unRegisterObserver(ImMsgDispatch dispatcher, String key) {
        if (isRegisterObserver(dispatcher, key)) {
            LogUtils.i(TAG, "---unRegisterObserver---");
            msgDispatchers.remove(key);
        }
    }

    public boolean isRegisterObserver(ImMsgDispatch dispatcher, String key) {
        if (dispatcher != null && !TextUtils.isEmpty(key)) {
            if (msgDispatchers.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg == null) {
            return;
        }
        /**
         * 根据优先级，向下分发消息 分发顺序：高优先>低优先级 PRIORITY_LEVEL_EXCLUSIVE优先级的CMD，只转发
         */
        int cmd = msg.what;

        // 如果返回的网络消息指定了消息的处理，则就不进行广播式分发，直接找到该key的处理
        // key==0，表示未指定消息处理者
        int dispatcherkey = msg.getData().getInt("dispatcherkey");
        if (dispatcherkey != 0) {
            if (msgDispatchers.containsKey(dispatcherkey)) {
                msgDispatchers.get(dispatcherkey).handleImMsg(msg);
            }
            return;
        }
        ImMsgDispatch dispatcher = null;
        boolean ret = false;
        for (int i = 1; i <= 4; i++) {
            Object[] values = msgDispatchers.values().toArray();
            for (Object key : values) {
                dispatcher = (ImMsgDispatch)key;
                switch (i) {
                    case ImMsgDispatch.PRIORITY_LEVEL_EXCLUSIVE:
                        if (dispatcher.getCmdPriority(cmd) == i) {
                            dispatcher.handleImMsg(msg);

                            // 找到后，直接分发
                            ret = true;
                            continue;
                        }
                        break;
                    case ImMsgDispatch.PRIORITY_LEVEL_ONE:
                    case ImMsgDispatch.PRIORITY_LEVEL_TWO:
                    case ImMsgDispatch.PRIORITY_LEVEL_NORMAL:
                        if (dispatcher.getCmdPriority(cmd) == i) {
                            Bundle retData = dispatcher.handleImMsg(msg);
                            if (retData != null) {
                                if (msg.getData() == null) {
                                    msg.setData(retData);
                                } else {
                                    msg.getData().putAll(retData);
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
            if (ret == true) {
                break;
            }
        }

    }

    /**
     * 重置msgHandler
     */
    public void reset() {
        msgDispatchers.clear();
        msgDispatchers = new ConcurrentHashMap<String, ImMsgDispatch>();
    }
}
