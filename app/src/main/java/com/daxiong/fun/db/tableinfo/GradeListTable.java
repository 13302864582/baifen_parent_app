package com.daxiong.fun.db.tableinfo;

import android.provider.BaseColumns;

public  class GradeListTable implements BaseColumns {
    public static final String TABLE_NAME = "grade_list";
    /** id int 初一、初二等 */
    public static final String ID = "id";
    /** 年级id int 初中、高中、小学等 */
    public static final String GRADE_ID = "grade_id";
    public static final String NAME = "name";// "名字 text"
    public static final String SUBJECTS = "subjects";// "科目 text"
    
    
    public static String getCreateGradeListTableSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME).append(" (");
        sb.append(_ID).append(" INTEGER PRIMARY KEY,");
        sb.append(ID).append(" INTEGER,");
        sb.append(GRADE_ID).append(" INTEGER,");
        sb.append(NAME).append(" TEXT,");
        sb.append(SUBJECTS).append(" TEXT");
        sb.append(");");
        return sb.toString();
    }
}

