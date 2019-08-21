
package com.daxiong.fun.db.tableinfo;

import android.provider.BaseColumns;

public class GoldTable implements BaseColumns {
    public static final String TABLE_NAME = "p_gold";

    public static final String MAXGOLD = "maxGold";// "maxGold float"

    public static final String MINGOLD = "minGold";// "minGold float"

    public static final String BASEGOLD = "baseGold";// "baseGold float"
    // "subject integer"科目ID 1英语 2数学 3物理 4化学 5生物

    public static final String SUBJECT = "subject";

    // "gradeid integer"年级ID 初123 高456
    public static final String GRADEID = "gradeid";

    public static final String GRADE = "grade";// "grade text"

    public static String getCreateGoldTableSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME).append(" (");
        sb.append(_ID).append(" INTEGER PRIMARY KEY,");
        sb.append(MAXGOLD).append(" FLOAT,");
        sb.append(MINGOLD).append(" FLOAT,");
        sb.append(BASEGOLD).append(" FLOAT,");
        sb.append(SUBJECT).append(" INTEGER,");
        sb.append(GRADEID).append(" INTEGER,");
        sb.append(GRADE).append(" TEXT");
        sb.append(");");
        return sb.toString();
    }
}
