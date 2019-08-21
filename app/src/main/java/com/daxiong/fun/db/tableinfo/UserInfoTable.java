package com.daxiong.fun.db.tableinfo;

import android.provider.BaseColumns;

public  class UserInfoTable implements BaseColumns {
    public static final String TABLE_NAME = "t_user_info";
    public static final String ADOPTCNT = "adoptcnt";// 0,采纳次数
    public static final String AGE = "age";// 0
    public static final String ALLOWGRAB = "allowgrab";// : 1,
    public static final String AMOUNTAMT = "amountamt";// : 0,消费累计金额
    public static final String ARBCNT = "arbcnt"; // : 0,仲裁次数
    public static final String AVATAR_100 = "avatar_100";
    public static final String AVATAR_40 = "avatar_40";
    public static final String CITY = "city";
    public static final String COUNTAMT = "countamt";// : 0,发任务次数
    public static final String CREDIT = "credit";// : 5,
    public static final String DREAMUNIV = "dreamuniv";// "",
    public static final String DREAMUNIVID = "dreamunivid";
    public static final String EARNGOLD = "earngold";// 0,老师收入金币
    public static final String EDULEVEL = "edulevel";// 0,老师教育水平
    public static final String EMAIL = "email";
    public static final String EXPENSESAMT = "expensesamt";// 消费累计金额,
    public static final String FROMCHAN = "fromchan";
    public static final String GOLD = "gold";
    public static final String GRADE = "grade";
    public static final String GRADEID = "gradeid";
    public static final String GROUPPHOTO = "groupphoto";// 背景图像
    public static final String HOMEWORKCNT = "homeworkcnt";// 0,发作业次数
    public static final String INFOSTATE = "infostate";// 0,标识用户信息是否完整
    public static final String NAME = "name";
    public static final String NAMEPINYIN = "namepinyin";
    public static final String PROVINCE = "province";
    public static final String QUICKCNT = "quickcnt";// 0,抢题次数
    public static final String ROLEID = "roleid";// 1,
    public static final String SCHOOLS = "schools";
    public static final String SCHOOLSID = "schoolsid";// 0
    public static final String SEX = "sex";
    public static final String STATE = "state";// 1,是否封号
    public static final String SUPERVIP = "supervip";
    public static final String TEACHLEVEL = "teachlevel";
    public static final String TEL = "tel";
    public static final String THIRDNAME = "thirdname";
    public static final String TOKENID = "tokenid";
    public static final String USERID = "userid";
    public static final String WELEARNID = "welearnid";// 0 所属客服id
    public static final String TEACHMAJOR = "teachmajor";// 老师擅长科目
    public static final String MAJOR = "major";// 专业
    public static final String NEWUSER = "newuser";// :0
    public static final String HWRATE = "hwrate";// 
    public static final String QSRATE = "qsrate";// 
    public static final String FDFS_AVATAR = "fdfs_avatar";
    public static final String FDFS_GROUPPHOTO = "fdfs_groupphoto";
    public static final String REMIND = "remind";
    public static final String VIP_TYPE = "vip_type";
    public static final String IS_EXPIRED = "vip_is_expired";
    public static final String VIP_CONTENT = "vip_content";
    public static final String VIP_LEFT_TIME = "vip_left_time";
    public static final String TABBARSWITCH= "tabbarswitch";
    public static final String SIXTEACHER = "sixteacher";
  
    
    public static final String getCreateUserInfoTableSql1() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME).append(" (");
        sb.append(_ID).append(" INTEGER PRIMARY KEY,");
        sb.append(ADOPTCNT).append(" INTEGER,");
        sb.append(AGE).append(" INTEGER,");
        sb.append(ALLOWGRAB).append(" INTEGER,");
        sb.append(AMOUNTAMT).append(" FLOAT,");
        sb.append(ARBCNT).append(" INTEGER,");
        sb.append(AVATAR_40).append(" TEXT,");
        sb.append(AVATAR_100).append(" TEXT,");
        sb.append(CITY).append(" TEXT,");
        sb.append(COUNTAMT).append(" INTEGER,");
        sb.append(CREDIT).append(" FLOAT,");
        sb.append(DREAMUNIV).append(" TEXT,");
        sb.append(DREAMUNIVID).append(" TEXT,");
        sb.append(EARNGOLD).append(" FLOAT,");
        sb.append(EDULEVEL).append(" INTEGER,");
        sb.append(EMAIL).append(" TEXT,");
        sb.append(EXPENSESAMT).append(" FLOAT,");
        sb.append(FROMCHAN).append(" TEXT,");
        sb.append(GOLD).append(" FLOAT,");
        sb.append(GRADE).append(" TEXT,");
        sb.append(GRADEID).append(" INTEGER,");
        sb.append(GROUPPHOTO).append(" TEXT,");
        sb.append(HOMEWORKCNT).append(" INTEGER,");
        sb.append(INFOSTATE).append(" INTEGER,");
        sb.append(NAME).append(" TEXT,");
        sb.append(NAMEPINYIN).append(" TEXT,");
        sb.append(PROVINCE).append(" TEXT,");
        sb.append(QUICKCNT).append(" INTEGER,");
        sb.append(ROLEID).append(" INTEGER,");
        sb.append(SCHOOLS).append(" TEXT,");
        sb.append(SCHOOLSID).append(" INTEGER,");
        sb.append(SEX).append(" INTEGER,");
        sb.append(STATE).append(" INTEGER,");
        sb.append(SUPERVIP).append(" INTEGER,");
        sb.append(TEACHLEVEL).append(" INTEGER,");
        sb.append(TEL).append(" TEXT,");
        sb.append(THIRDNAME).append(" TEXT,");
        sb.append(TOKENID).append(" TEXT,");
        sb.append(USERID).append(" INTEGER,");
        sb.append(WELEARNID).append(" INTEGER,");
        sb.append(TEACHMAJOR).append(" TEXT,");
        sb.append(MAJOR).append(" TEXT,");
        sb.append(NEWUSER).append(" INTEGER,");
        sb.append(HWRATE).append(" INTEGER,");
        sb.append(QSRATE).append(" INTEGER,");
        sb.append(FDFS_AVATAR).append(" TEXT,");
        sb.append(FDFS_GROUPPHOTO).append(" TEXT,");
        sb.append(VIP_TYPE).append(" INTEGER,");
        sb.append(IS_EXPIRED).append(" INTEGER,");
        sb.append(VIP_LEFT_TIME).append(" INTEGER,");
        sb.append(VIP_CONTENT).append(" TEXT,");
        sb.append(REMIND).append(" TEXT,");
        sb.append(TABBARSWITCH).append(" INTEGER,");
        sb.append(SIXTEACHER).append(" INTEGER");
        sb.append(");");
        return sb.toString();
    }
}
