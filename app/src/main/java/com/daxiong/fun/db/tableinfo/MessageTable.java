
package com.daxiong.fun.db.tableinfo;

import android.provider.BaseColumns;

public class MessageTable implements BaseColumns {
    public static final String TABLE_NAME = "t_messagelist";

    /** 如果是接收消息,则是发送人id,如果为发送消息,则是接收人id */
    public static final String USERID = "userid";// "userid integer"

    public static final String CONTENTTYPE = "contenttype";// "contenttype
                                                           // integer"

    public static final String FROMROLEID = "fromroleid";// "fromroleid integer"

    public static final String QUESTIONID = "questionid";// "questionid integer"

    public static final String ISSENDFAIL = "issendfail";// "issendfail integer"

    public static final String ISREADED = "isreaded";// "isreaded integer"
                                                     // //0代表未读 1代表已读

    public static final String MSGCONTENT = "msgcontent";// "msgcontent text"

    public static final String JUMPURL = "jumpurl";// "jumpurl text"

    public static final String AUDIOTIME = "audiotime";// "audiotime integer,"

    public static final String TARGET_USER_ID = "target_user_id";// "target_user_id
                                                                 // integer"

    public static final String TARGET_ROLE_ID = "target_role_id";// "target_role_id
                                                                 // integer"

    public static final String ACTION = "action";// "action integer"

    public static final String CURRENTUSERID = "currentuserid";// "currentuserid
                                                               // integer"

    public static final String PATH = "path";// "path text"

    public static final String MSGTIME = "msgtime";// "msgtime text"

    public static final String TYPE = "type";// "type integer"

    public static final String TASKID = "taskid";// "type integer"

    public static final String CHECKPOINTID = "checkpointid";// "type integer"

    public static final String ISRIGHT = "isright";// "type integer"

    public static final String COORDINATE = "coordinate";// "type text"

    public static final String IMGPATH = "imgpath";// "type text"

    public static final String NOTICETYPE = "noticetype";// "type text"

    // public static final String COURSEID = "courseid";// "type integer"
    public static final String PAGEID = "pageid";// "type integer"

    public static String getCreateMessageTableSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME).append(" (");
        sb.append(_ID).append(" INTEGER PRIMARY KEY autoincrement,");
        sb.append(USERID).append(" INTEGER,");
        sb.append(CONTENTTYPE).append(" INTEGER,");
        sb.append(FROMROLEID).append(" INTEGER,");
        sb.append(QUESTIONID).append(" INTEGER,");
        sb.append(ISSENDFAIL).append(" INTEGER,");
        sb.append(ISREADED).append(" INTEGER,");
        sb.append(MSGCONTENT).append(" TEXT,");
        sb.append(JUMPURL).append(" TEXT,");
        sb.append(AUDIOTIME).append(" INTEGER,");
        sb.append(TARGET_USER_ID).append(" INTEGER,");
        sb.append(TARGET_ROLE_ID).append(" INTEGER,");
        sb.append(ACTION).append(" INTEGER,");
        sb.append(CURRENTUSERID).append(" INTEGER,");
        sb.append(PATH).append(" TEXT,");
        sb.append(MSGTIME).append(" TEXT,");
        sb.append(TYPE).append(" INTEGER,");

        sb.append(TASKID).append(" INTEGER,");
        sb.append(CHECKPOINTID).append(" INTEGER,");
        sb.append(ISRIGHT).append(" INTEGER,");
        sb.append(COORDINATE).append(" TEXT,");

        sb.append(NOTICETYPE).append(" TEXT,");

        sb.append(PAGEID).append(" INTEGER,");

        sb.append(IMGPATH).append(" TEXT");
        sb.append(");");
        return sb.toString();
    }
}
