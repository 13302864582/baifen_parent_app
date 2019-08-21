
package com.daxiong.fun.constant;

/**
 * 此类的描述： 请求码
 * 
 * @author: Sky @最后修改人： Sky
 * @最后修改日期:2015-7-20 下午8:55:11
 * @version: 2.0
 */
public class RequestConstant {
	
	//处理客户端用户未登录
	public static final int  USER_UNLOGIN=2;

    // eg.出错
    public static final int ERROR = 10001;
    // eg.统计
    public static final int TONGJI_CODE = 10007;

    // eg.cookie失效
    public static final int COOKIE_INVILD = 10002;

    // eg.登录
    public static final int LOGIN_CODE = 10003;

    // 得到studyFragment数据
    public static final int GET_TALL_CODE = 132;

    // 得到homework tall
    public static final int GET_HOME_WORK_TALL_CODE = 1322;
    // 得到系统当前时间
    public static final int GET_HOME_SYSTEM_TIME_CODE = 1327;

    public static final int REQUEST_MY_ORGS = 124;

    // 得到特殊学生账号
    public static final int REQUEST_SPECIAL_STUDENT_CODE = 125;
    // 聊天置顶topchats
    public static final int REQUEST_TOPCHATS_CODE = 1325;

    // 得到特殊学生的权限列表
    public static final int REQUEST_SPECIAL_STUDENT_PERMISS_LIST_CODE = 1225;

    // 执行外包发布作业
    public static final int EXECUTE_PUTONG_PUBLISH_HOME_WORK_CODE = 324;

    public static final int EXECUTE_PUTONG_PUBLISH_HOME_WORK_UPLOAD_FINISH = 3242;

    // 代理执行上传作业
    public static final int HOMEWORK_UPLOAD = 2155;

    // 代理发布作业，上传完图片后调用接口
    public static final int AGENT_UPLOAD_HOMEWORK_FINISH = 233;

    // 辅导券发布作业
    public static final int PUBLISH_HOMEWORK_WITH_FUDAOQUAN_CODE = 1111;

    /********************************** 知识点搜索 *****************************************/

    // 历年真题
    public static final int KNOWLEDGE_SEARCH_CODE = 21321;

    public static final int KEYWORD_SEARCH_CODE = 23423;

    // 可用券
    public static final int GET_FUDAOQUAN_LIST_CODE = 23234;

    // 过期的辅导券
    public static final int GET_EXPRIE_FUDAOQUAN_LIST_CODE = 1234;

    // 获取难题券
    public static final int GET_QUESTION_QUAN_CODE = 23444;
    // 获取辅导券的信息
    public static final int GET_QUAN_INFO_CODE = 23447;
    // 获取作业券
    public static final int GET_HOMEWORK_QUAN_CODE = 2123444;

    // 修改密码 发送验证码
    public static final int SEND_PHONE_VALIDATE_CODE = 234;

    // 修改密码
    public static final int MODIFY_PASS_CODE = 23432;

    // 得到纠错的状态
    public static final int GET_JIUCUO_STATUS = 12312;

    // 纠错
    public static final int EXEC_JIUCUO = 1233412;

    // 作业分析
    public static final int HW_ANALYSIC_CODE = 123412;

    // 登录操作
    public static final int REGISTER_LOGIN_CODE = 111;

    // 得到用户信息
    public static final int GET_USERINFO = 234123;

    // 微信预支付
    public static final int GET_WXPAY = 2341235;

    // 引导页图片
    public static final int GET_SPLASH_IMAGE_CODE = 11111;
    // 获取分享参数
    public static final int GET_SHARE_CODE = 13111;

    // 首页信息
    public static final int GET_HOME_CONTEXT_CODE = 11112;
    // 班主任信息
    public static final int GET_TEACHER_CONTEXT_CODE = 11152;

    // 获取日历模式信息
    public static final int GET_CALENDAR_INFO = 11112213;

    public static final int GET_CALENDARLIST_INFO = 11112223;

    // 发布作业
    public static final int PUBLISH_HW_CODE = 2223;
    //放弃发布任务
    public static final int GIVEUP_HW_CODE = 22235;
    // 索要辅导券
    public static final int GETFUDAOQUAN_HW_CODE = 222357;

    public static final int GET_USE_FUDAOQUAN_CODE = 22123;
    public static final int GET_HOMEWORK_LIST_CODE = 23123;
    
    //vip信息列表
    public static final int GET_VIP_LIST = 21113123;
    //预支付订单
    public static final int GET_VIP_PREPARE_ORDER = 21143;
    //正式VIP
    public static final int GET_MORE_VIP_LIST = 2123;
    
    
    public static final int GET_QUESTION_LIST_CODE = 23223;
    //获取反馈理由
    public static final int GET_FANKUILIYOU_CODE = 23225;
    //提交反馈理由
    public static final int COMMIT_FANKUILIYOU_CODE = 23227;
    
    //学情报告轮播图
    public  static final int GET_PROMOTION_PIC=1323;
    
    //得到学情报告列表
    public  static final int GET_LEARNING_REPORT_LIST=132;
    
    //成长
    public static final int GROWING_CODE=312;
    
    //更换手机号
    public static final int CHANGE_PHONE_CODE=123123;
    
   
    //更新用户信息
    public static final int UPDATE_USERINFO=433;
    
    
    public static final int SINGLE_HW_ANALYSIS=23423;
    
    //得到主界面的提示框
    public static final int MAIN_ACTIVITY_TIP=21322;  
    
    
    //从服务器获取年级
    public static final int GET_GRADE_LIST=213;  
    //请求科目
    public static final int REQUEST_SUBJECT=11213;  
    
    //获取订单列表
    public static final int GET_ORDER_LIST=11;  
    
    //刷新支付信息
    public static final int REFRESH_PAY_INFO=1144;
    
    //取消订单
    public static final int CANCLE_ORDER=11244;
    
    //相机拍照
    public static final int REQUEST_CODE_GET_IMAGE_FROM_SYS = 0111;
    
    
   
    
    

}
