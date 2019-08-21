
package com.daxiong.fun.constant;

/**
 * 消息的常量
 * 
 * @author: sky
 */
public class MessageConstant {

    public static final int END_OF_PLAY = 0x03;// 结束播放

    public static final int MSG_DEF_SVR_ERROR = 0x09;

    public static final int MSG_DEF_CONN_TIMEOUT = 0x13;

    public static final int MSG_DEF_MSGS = 0x18;// 离线消息

    public static final int MSG_DEF_CHAT_LIST = 0x19;// 聊天页面

    public static final int MSG_DEF_MSG_RESULT = 0x21;// 用户各人信息
    public static final int MSG_DEF_HOME_LIST = 0x23;// 首页列表
    public static final int MSG_DEF_CALENDAR_LIST = 0x25;// 日历列表
    public static final int MSG_DEF_HW_LIST = 0x27;
    public static final int MSG_DEF_QS_LIST = 0x29;
    public static final int MSG_DEF_HOME_TEC = 0x31;
    public static final int MSG_DEF_CHAT_TEC = 0x33;

    public static final int MSG_DEF_OBTAIN_UPDATE_SUCCESS = 0x44;

    /** 纯文字 */
    public static final int MSG_CONTENT_TYPE_TEXT = 1;

    /** 声音 */
    public static final int MSG_CONTENT_TYPE_AUDIO = 2;

    /** 图片 */
    public static final int MSG_CONTENT_TYPE_IMG = 3;

    /** 文字+链接 */
    public static final int MSG_CONTENT_TYPE_JUMP = 4;

    /** 富文本 */
    public static final int MSG_CONTENT_TYPE_JUMP_URL = 5;

}
