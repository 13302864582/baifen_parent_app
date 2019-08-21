package com.daxiong.fun.constant;

/**
 * 作业状态
 *
 * @author: sky
 */
public class HomeworkStatusConstant {

    /**
     * 抢答中 	可抢题状态
     */
    public static final int ASKING = 0;
    /**
     * 答题中 回答问题中
     */
    public static final int ANSWERING = 1;
    /**
     * 已提交答案 回答完毕
     */
    public static final int ANSWERED = 2;
    /**
     * 追问中 沟通中(用于追问)
     */
    public static final int APPENDASK = 3;
    /**
     * 已采纳 	采纳
     */
    public static final int ADOPTED = 4;
    /**
     * 已拒绝 拒绝
     */
    public static final int REFUSE = 5;
    /**
     * 仲裁中 	仲裁中
     */
    public static final int ARBITRATE = 6;
    /**
     * 仲裁结束 	仲裁完毕
     */
    public static final int ARBITRATED = 7;
    /**
     * 被举报 	举报
     */
    public static final int REPORT = 8;
    /**
     * 已删除 	删除
     */
    public static final int DELETE = 9;

    /**
     * 未知状态
     */
    public static final int UNCONFIRM=10;
}
