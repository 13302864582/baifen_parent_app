package com.daxiong.fun.db.tableinfo;

import android.provider.BaseColumns;

public  class SubjectListTable implements BaseColumns {
    public static final String TABLE_NAME = "sub_list";
    public static final String SUB_ID = "sub_id";// "科目id int"
    public static final String NAME = "name";// "name text"
    
    
    public static String getCreateSubjectsListTableSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME).append(" (");
        sb.append(_ID).append(" INTEGER PRIMARY KEY,");
        sb.append(SUB_ID).append(" INTEGER,");
        sb.append(NAME).append(" TEXT");
        sb.append(");");
        return sb.toString();
    }

}
