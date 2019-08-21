package com.daxiong.fun.db.tableinfo;

import android.provider.BaseColumns;

public  class QuestionRuleInfoTable implements BaseColumns {
    public static final String TABLE_NAME = "t_question_rule";
    public static final String GRADE_ID = "grade_id";
    public static final String GRADE_NAME = "grade_name";
    public static final String TIME = "time";
    public static final String MONEY = "money";
    
    
    public static String getCreateQuestionRuleTableSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME).append(" (");
        sb.append(_ID).append(" INTEGER PRIMARY KEY,");
        sb.append(GRADE_ID).append(" INTEGER,");
        sb.append(GRADE_NAME).append(" TEXT,");
        sb.append(TIME).append(" INTEGER,");
        sb.append(MONEY).append(" FLOAT");
        sb.append(");");
        return sb.toString();
    }
}
