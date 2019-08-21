
package com.daxiong.fun.dispatch;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.api.WeLearnApi;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.MessageConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.function.communicate.ChatMsgViewActivity;
import com.daxiong.fun.model.ChatInfo;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MessageUtils;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.util.WeLearnFileUtil;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

/**
 * 处理服务器的响应
 *
 * @author parsonswang
 */
public class WelearnHandler {

    private static final String TAG = WelearnHandler.class.getSimpleName();

    private UIMsgHandler mUIMsgHandler;

    private WelearnHandler() {
        this.mUIMsgHandler = new UIMsgHandler();
    }

    private static class WelearnHandlerHolder {
        private static final WelearnHandler INSANCE = new WelearnHandler();
    }

    public static WelearnHandler getInstance() {
        return WelearnHandlerHolder.INSANCE;
    }

    public UIMsgHandler getHandler() {
        return mUIMsgHandler;
    }

    @SuppressWarnings("deprecation")
    private void showNotification(ChatInfo chat, Boolean flag2) {
        String notifiName = "大熊作业";
        String notifiContent = "有您的新消息";
        UserInfoModel user = DBHelper.getInstance().getWeLearnDB().queryByUserId(chat.getFromuser(),
                true);
        if (user != null) {
            String name = user.getName();
            if (!TextUtils.isEmpty(name)) {
                notifiName = name;
            }
        }

        // 1.弹出通知
        final Context context = MyApplication.getContext();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent();
        if (flag2) {
            Bundle data = new Bundle();
            data.putInt(ChatMsgViewActivity.USER_ID, chat.getFromuser());
            data.putBoolean("isFromNoti", true);
            // Log.i(TAG, "showNotification===" + chat.getFromuser());
            intent.putExtras(data);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setClass(context, ChatMsgViewActivity.class);
        }
        int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent pIntent = PendingIntent.getActivity(context, iUniqueId, intent, 0);

        //Notification notification = new Notification();
        Notification.Builder notificationBuilder = new Notification.Builder(context.getApplicationContext());
        // 设置通知来到的时间
        notificationBuilder.setWhen(System.currentTimeMillis());

        if (flag2) {
//            notification.icon = R.drawable.ic_launcher;
            //设置图标
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
            if (chat != null && chat.getMsgcontent() != null) {
                if (chat.getContenttype() == 1) {
//                    notification.tickerText = chat.getMsgcontent();
                    notificationBuilder.setTicker(chat.getMsgcontent());
                    notifiContent = chat.getMsgcontent();
                } else if (chat.getContenttype() == 2) {
//                    notification.tickerText = "收到一段语音哦,听不听？";
                    notificationBuilder.setTicker("收到一段语音哦,听不听？");
                    notifiContent = "[语音]";
                } else if (chat.getContenttype() == 3) {
//                    notification.tickerText = "发现一张图正向您扑来……";
                    notificationBuilder.setTicker("发现一张图正向您扑来……");
                    notifiContent = "[图片]";
                } else {
//                    notification.tickerText = chat.getMsgcontent();
                    notificationBuilder.setTicker(chat.getMsgcontent());
                    notifiContent = chat.getMsgcontent();
                }
            } else {
//                notification.tickerText = "";
                notificationBuilder.setTicker("");
            }
        } else {

        }
        boolean flag = true;
        boolean dayNotDis = MySharePerfenceUtil.getInstance().getDayNotDis();
        boolean nightNotDis = MySharePerfenceUtil.getInstance().getNightNotDis();
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if (dayNotDis) {
            int week = c.get(Calendar.DAY_OF_WEEK);
            int minute = c.get(Calendar.MINUTE);
            if (week > 1 && week < 7) {
                if ((hour >= 8 && hour < 12) || (hour >= 14 && hour < 17)) {
                    flag = false;
                }
                if (hour == 17) {
                    if (minute <= 30) {
                        flag = false;
                    }
                }
            }
        }

        if (nightNotDis) {
            if ((hour >= 0 && hour < 6) || (hour == 23)) {
                flag = false;
            }
        }

        if (flag) {
            boolean msgNotifyVibrate = MySharePerfenceUtil.getInstance().getMsgNotifyVibrate();
            boolean msgNotifyFlag = MySharePerfenceUtil.getInstance().getMsgNotifyFlag();
            int vibrate = 1;
            int sound = 1;
            String noticetype = chat.getNoticetype();
            if (!TextUtils.isEmpty(noticetype) && noticetype.length() == 3) {
                vibrate = Integer.parseInt(noticetype.charAt(1) + "");
                sound = Integer.parseInt(noticetype.charAt(2) + "");
            }
            if (msgNotifyVibrate && msgNotifyFlag) {
                if (vibrate == 1 && sound == 1) {
//                   notification.defaults = Notification.DEFAULT_ALL;
                    notificationBuilder.setDefaults(Notification.DEFAULT_ALL);

                } else if (vibrate == 1) {
//                notification.defaults = Notification.DEFAULT_VIBRATE;
                    notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
                } else if (sound == 1) {
//                	 notification.defaults = Notification.DEFAULT_SOUND;
                    notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
                }
            } else if (msgNotifyVibrate && vibrate == 1) {
//                notification.defaults = Notification.DEFAULT_VIBRATE;
                notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);

            } else if (msgNotifyFlag && sound == 1) {
//            	notification.defaults = Notification.DEFAULT_SOUND;
                notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
            }
        }
//       notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationBuilder.setAutoCancel(true);
//        notification.setLatestEventInfo(context, notifiName, notifiContent, pIntent);

        notificationBuilder.setContentTitle(notifiName);
        notificationBuilder.setContentText(notifiContent);
        notificationBuilder.setContentIntent(pIntent);
        Notification notification = notificationBuilder.build();

        if (flag2) {
            notificationManager.notify(0x0a, notification);
            MyApplication.notifiFromUser = chat.getFromuser();
        } else {
            notificationManager.notify(0x0b, notification);
            // notificationManager.cancel(0x0b);
        }
    }

    public void onCmdHandle(String respText) {
        int code = JsonUtil.getInt(respText, "code", -1);
        if (code == 5) {// 若返回字段中，code=5，说明是错误的
            return;
        }
        int type = JsonUtil.getInt(respText, "type", 0);
        switch (type) {
            case 1:// 功能
                dispatchMessage(respText);
                break;
            case 2:// 聊天、通知
                splitMessage(respText);
                break;
            case 3:// 系统
                int subtype3 = JsonUtil.getInt(respText, "subtype", 1);
                // 1分配sessionid 2 升级 3刷新数据
                // String sessionid = JSONUtils.getString(respText,
                // "sessionid", "");
                // WeLearnSpUtil.getInstance().setSession(sessionid);
                // WeLearnApi.reLogin();
                if (subtype3 == 1) {
                    MessageUtils.sendSessionToServer();
                }
                break;
            case 4:// 第三方登录
                break;
            case 5:// 拍题搜索
                break;
            case 6:// 支付
                break;
            case 7:// 系统主动向客户端广播类消息
                break;

        }
    }

    /**
     * type=2时候的消息分发
     *
     * @param respText voi
     * @author: sky
     */
    private void splitMessage(String respText) {
        int subtype = JsonUtil.getInt(respText, "subtype", 0);
        if (subtype != 5) {
            WeLearnApi.talkMsgReceivedVerity(respText);
        }
        switch (subtype) {
            case 1:// 聊天消息，包括即时聊天
            case 2:// 问题类的通知消息，如问题状态变更、问题类的通知
                int fromUserid = JsonUtil.getInt(respText, "fromuser", 0);
                final ChatInfo chat = new Gson().fromJson(respText, ChatInfo.class);
                if (chat.getContenttype() == 0) {
                    return;
                } else if (chat.getContenttype() == 4) {

                    Message msg = mUIMsgHandler.obtainMessage();
                    if (msg == null) {
                        msg = new Message();
                    }
                    if (chat.getData().getAction() == 10) {
                        // 作业问题列表更新
                        msg.what = MessageConstant.MSG_DEF_HOME_LIST;
                        msg.obj = respText;
                        mUIMsgHandler.sendMessage(msg);
                        showNotification(chat, false);
                        return;
                    } else if (chat.getData().getAction() == 9) {
                        // 班主任简评更新
                        msg.what = MessageConstant.MSG_DEF_HOME_TEC;
                        msg.obj = respText;
                        mUIMsgHandler.sendMessage(msg);
                        showNotification(chat, false);
                        return;
                    }

                }
                if (MyApplication.isInChatMsgView && fromUserid == MyApplication.currentUserId) {// 在聊天窗口中并且发送人id是和当前聊天id相同并且不是群聊
                    Message msg = mUIMsgHandler.obtainMessage();
                    if (msg == null) {
                        msg = new Message();
                    }
                    msg.what = MessageConstant.MSG_DEF_CHAT_LIST;
                    msg.obj = respText;
                    mUIMsgHandler.sendMessage(msg);
                } else {
                    // 3.发送消息通知更新ui final ChatInfo
                    chat.setType(GlobalContant.MSG_TYPE_RECV);
                    setMsgPath(chat);

                    boolean insertMsg = DBHelper.getInstance().getWeLearnDB().insertMsg(chat);

                    if (insertMsg) {
                        Message msg = mUIMsgHandler.obtainMessage();
                        if (msg == null) {
                            msg = new Message();
                        }

                        msg.what = MessageConstant.MSG_DEF_MSGS;

                        msg.obj = respText;
                        mUIMsgHandler.sendMessage(msg);
                        int userId = MySharePerfenceUtil.getInstance().getUserId();
                        if (userId != 0) {
                            showNotification(chat, true);
                        }

                    }

                }
                break;
            case 3:// 个人财务、信用变更的通知
                break;
            case 4:// 消息集合体 如离线消息，发送的是一个消息集合
                // Log.e("离线消息:", respText);
                Map<String, List<String>> resultMap = JsonUtil
                        .convetJsonObjToMap(JsonUtil.getJSONObject(respText, "data", null));
                ChatInfo chat4 = null;
                // boolean insertMsg4 = false;
                if (resultMap != null) {
                    Set<String> sets = resultMap.keySet();
                    for (String key : sets) {
                        List<String> jsons = resultMap.get(key);
                        for (String jsonStr : jsons) {
                            fromUserid = JsonUtil.getInt(jsonStr, "fromuser", 0);
                            int contenttype4 = JsonUtil.getInt(jsonStr, "contenttype", 0);
                            String string = JsonUtil.getString(jsonStr, "msgcontent", "");
                            if (contenttype4 == 0) {
                                return;
                            }
                            if (contenttype4 == 4 && "".equals(string)) {
                                return;
                            }
                            if (MyApplication.isInChatMsgView
                                    && fromUserid == MyApplication.currentUserId) {// 在聊天窗口中并且发送人id是和当前聊天id相同并且不是群聊
                                Message msg = mUIMsgHandler.obtainMessage();
                                if (msg == null) {
                                    msg = new Message();
                                }
                                msg.what = MessageConstant.MSG_DEF_CHAT_LIST;
                                msg.obj = jsonStr;
                                mUIMsgHandler.sendMessage(msg);
                            } else {
                                chat4 = new Gson().fromJson(jsonStr, ChatInfo.class);
                                chat4.setType(GlobalContant.MSG_TYPE_RECV);
                                setMsgPath(chat4);

                                /* insertMsg4 = */
                                DBHelper.getInstance().getWeLearnDB()
                                        .insertMsg(chat4);
                                // 更改数据调用方式 modified by yh 2015-01-07
                                // End
                            }

                        }
                    }
                    Message msg = mUIMsgHandler.obtainMessage();
                    if (msg == null) {
                        msg = new Message();
                    }
                    msg.what = MessageConstant.MSG_DEF_MSGS;
                    msg.obj = respText;
                    mUIMsgHandler.sendMessage(msg);
                    int userId = MySharePerfenceUtil.getInstance().getUserId();
                    if (chat4 != null && userId != 0) {
                        showNotification(chat4, true);
                    }
                }
                break;
            case 5:// 服务端聊天消息接收确认
                dispatchMessage(respText);
                break;
            case 6:// 客户端聊天消息接收确认
                break;
            case 7:// 金币刷新以及接收确认
                String dataStr = JsonUtil.getString(respText, "data", "");
                int action = JsonUtil.getInt(dataStr, "action", -1);

                switch (action) {
                    case 0:// 无操作
                        float gold = (float) JsonUtil.getDouble(dataStr, "gold", 0);
                        float credit = (float) JsonUtil.getDouble(dataStr, "credit", 0);
                        UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB()
                                .queryCurrentUserInfo();
                        if (null != uInfo) {
                            uInfo.setCredit(credit);
                            uInfo.setGold(gold);
                            DBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);
                        }
                        break;
                    case 1:// 打开问题
                        MySharePerfenceUtil.getInstance().setOrgVip();
                        break;
                    case 2:// 打开userid主页
                        break;
                    case 3:// 打开URL
                        break;
                    case 4:// 发起邀请，字段内容待定
                        break;

                    case 5:// 作业
                        break;
                    case 6:// 单题
                        break;
                }

                break;
            case 8:// 绑定session（客户端连接成功后，第一次要发送的subtype值）
                break;
            case 9:// 提示在另一端登录了账户（web得知移动端）
                break;
            case 10:// 提示被踢了 (有两个移动端登录的情况下，前一个移动端被下线)
                break;
            case 11:// 提示服务器已与客户端断开连接
                break;
        }
    }

    private void setMsgPath(ChatInfo chat) {
        if (chat.getContenttype() == MessageConstant.MSG_CONTENT_TYPE_AUDIO) {
            String audioPath = WeLearnFileUtil.getVoiceFile().getAbsolutePath() + File.separator
                    + UUID.randomUUID().toString() + ".amr";
            LogUtils.i(TAG, "====" + audioPath);
            if (!WeLearnFileUtil.isFileExist(audioPath)) {
                WeLearnFileUtil.decodeFileByBase64(chat.getMsgcontent(), audioPath);
            }
            chat.setPath(audioPath);
        } else if (chat.getContenttype() == MessageConstant.MSG_CONTENT_TYPE_IMG) {
            String imgPath = WeLearnFileUtil.getImgMsgFile().getAbsolutePath() + File.separator
                    + UUID.randomUUID().toString() + ".png";
            if (!WeLearnFileUtil.isFileExist(imgPath)) {
                WeLearnFileUtil.decodeFileByBase64(chat.getMsgcontent(), imgPath);
            }
            chat.setPath(imgPath);
        }
    }

    private void dispatchMessage(String respText) {
        double timestamp = JsonUtil.getDouble(respText, "timestamp", 0L);
        // Log.e("", "");
        if (0 != timestamp) {
            Message msg = mUIMsgHandler.obtainMessage();
            if (msg == null) {
                msg = new Message();
            }
            msg.what = MyApplication.time2CmdMap.get(timestamp);
            msg.obj = respText;
            mUIMsgHandler.sendMessage(msg);
        } else {
            if (AppConfig.IS_DEBUG) {
                ToastUtils.show("时间戳为0");
            }
            Message msg = mUIMsgHandler.obtainMessage();
            if (msg == null) {
                msg = new Message();
            }
            msg.what = MessageConstant.MSG_DEF_SVR_ERROR;
            msg.obj = respText;
            mUIMsgHandler.sendMessage(msg);
        }
    }

    public void registDispatcher(ImMsgDispatch dispatcher, String key) {
        if (mUIMsgHandler == null) {
            return;
        }
        if (!mUIMsgHandler.isRegisterObserver(dispatcher, key)) {
            mUIMsgHandler.registerObserver(dispatcher, key);
        }
    }

    public void unRegistDispatcher(ImMsgDispatch dispatcher, String key) {
        if (mUIMsgHandler == null) {
            return;
        }
        if (mUIMsgHandler.isRegisterObserver(dispatcher, key)) {
            mUIMsgHandler.unRegisterObserver(dispatcher, key);
        }
    }

    public void removeMessage(int msgDef) {
        if (mUIMsgHandler == null) {
            return;
        }
        // Log.i(TAG, "---remove msg---");
        mUIMsgHandler.removeMessages(msgDef);
    }
}
