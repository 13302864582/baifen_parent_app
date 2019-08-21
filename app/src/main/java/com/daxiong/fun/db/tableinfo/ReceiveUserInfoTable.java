package com.daxiong.fun.db.tableinfo;

import android.provider.BaseColumns;


public  class ReceiveUserInfoTable implements BaseColumns {
    public static final String TABLE_NAME = "t_recv_user";
    public static final String USERID = "userid";// "userid integer"
    public static final String ROLEID = "roleid";// "roleid integer"
    public static final String AVATAR_100 = "avatar_100";// "avatar_100 text"
    public static final String NAME = "name";// "name text"
    
    
    public static String getCreateReceiveUserInfoTableSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME).append(" (");
        sb.append(_ID).append(" INTEGER PRIMARY KEY,");
        sb.append(USERID).append(" INTEGER,");
        sb.append(ROLEID).append(" INTEGER,");
        sb.append(AVATAR_100).append(" TEXT,");
        sb.append(NAME).append(" TEXT");
        sb.append(");");
        return sb.toString();
    }
}