package com.daxiong.fun.db.tableinfo;

import android.provider.BaseColumns;

public  class HomeWorkRuleInfoTable implements BaseColumns {
    public static final String TABLE_NAME = "t_homework_rule";
    public static final String GRADE_ID = "grade_id";
    public static final String GRADE_NAME = "grade_name";
    public static final String DEFAULT_PIC_COUNT = "default_pic_count";
    public static final String MAX_PIC_COUNT = "max_pic_count";
    public static final String DEFAULT_PIC_MONEY = "default_pic_money";
    public static final String SINGLE_PIC_MONEY = "single_pic_money";
    public static final String TIME = "time";
    
    public static String getCreateHomeWorkRuleTableSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME).append(" (");
        sb.append(_ID).append(" INTEGER PRIMARY KEY,");
        sb.append(GRADE_ID).append(" INTEGER,");
        sb.append(GRADE_NAME).append(" TEXT,");
        sb.append(DEFAULT_PIC_COUNT).append(" INTEGER,");
        sb.append(MAX_PIC_COUNT).append(" INTEGER,");
        sb.append(DEFAULT_PIC_MONEY).append(" FLOAT,");
        sb.append(SINGLE_PIC_MONEY).append(" FLOAT,");
        sb.append(TIME).append(" INTEGER");
        sb.append(");");
        return sb.toString();
    }
}


