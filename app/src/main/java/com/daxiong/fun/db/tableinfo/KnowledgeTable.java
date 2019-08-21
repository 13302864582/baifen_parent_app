package com.daxiong.fun.db.tableinfo;

import android.provider.BaseColumns;

public  class KnowledgeTable implements BaseColumns {
    public static final String TABLE_NAME = "t_knowledge";
    public static final String GROUPNAME = "groupname";// "groupname text,"
    public static final String GROUPID = "groupid";// "groupid integer,"
    public static final String SUBJECTNAME = "subjectname";// "subjectname text,"
    public static final String SUBJECTID = "subjectid";// "subjectid integer,"
    public static final String CHAPTERNAME = "chaptername";// "chaptername text,"
    public static final String CHAPTERID = "chapterid";// "chapterid integer,"
    public static final String NAME = "name";// "name text,"
    public static final String ID = "id";// "id integer,"
    
    public static String getCreateKnowLedgeTableSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME).append(" (");
        sb.append(_ID).append(" INTEGER PRIMARY KEY autoincrement,");
        
        sb.append(GROUPNAME).append(" TEXT,");
        sb.append(GROUPID).append(" INTEGER,");
        sb.append(SUBJECTNAME).append(" TEXT,");
        sb.append(SUBJECTID).append(" INTEGER,");
        sb.append(CHAPTERNAME).append(" TEXT,");
        sb.append(CHAPTERID).append(" INTEGER,");
        sb.append(NAME).append(" TEXT,");
        sb.append(ID).append(" INTEGER");
        
        sb.append(");");
        return sb.toString();
    }

}

