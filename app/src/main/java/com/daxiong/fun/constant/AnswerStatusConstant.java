package com.daxiong.fun.constant;

/**
 * 回答问题的状态
 * @author:  sky
 */
public class AnswerStatusConstant {

    //答题中
    public static final int STATUS_ANSWER_ANSWERING=0;
    
    /** 已回答 答题完毕*/
    public static final int STATUS_ANSWER_ANSWERED = 1;
    
    /** 已采纳 采纳*/
    public static final int STATUS_ANSWER_ADOPTED = 2;
    
    /** 已拒绝 拒绝*/
    public static final int STATUS_ANSWER_ABOURED = 3;
    
    /** 仲裁中 仲裁中*/
    public static final int STATUS_ANSWER_ARBITRATION = 4;

    //举报
    public static final int STATUS_ANSWER_REPORT=5;
    
    /** 追问中 沟通中*/
    public static final int STATUS_ANSWER_APPEND = 6;

    /** 放弃仲裁 */
    public static final int STATUS_ANSWER_ABOUR_ARBITRATION = 7;
    
    /** 仲裁完成 	仲裁完毕*/
    public static final int STATUS_ANSWER_ARBITRATIONED = 8;



}
