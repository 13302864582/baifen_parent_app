package com.daxiong.fun.db.tableinfo;

import android.provider.BaseColumns;

public  class UnivListTable implements BaseColumns {
    public static final String TABLE_NAME = "univ_list";
    public static final String NAME = "name";// "name text"
    
    
    public static String getCreateUnivListTableSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME).append(" (");
        sb.append(_ID).append(" INTEGER PRIMARY KEY,");
        sb.append(NAME).append(" TEXT");
        sb.append(");");
        return sb.toString();
    }

}
