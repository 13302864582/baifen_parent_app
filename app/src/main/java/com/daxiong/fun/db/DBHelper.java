package com.daxiong.fun.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.daxiong.fun.MyApplication;
import com.daxiong.fun.db.dao.CommonDao;
import com.daxiong.fun.db.tableinfo.ContactsInfoTable;
import com.daxiong.fun.db.tableinfo.GoldTable;
import com.daxiong.fun.db.tableinfo.GradeListTable;
import com.daxiong.fun.db.tableinfo.HomeWorkRuleInfoTable;
import com.daxiong.fun.db.tableinfo.KnowledgeTable;
import com.daxiong.fun.db.tableinfo.MessageTable;
import com.daxiong.fun.db.tableinfo.QuestionRuleInfoTable;
import com.daxiong.fun.db.tableinfo.ReceiveUserInfoTable;
import com.daxiong.fun.db.tableinfo.SubjectListTable;
import com.daxiong.fun.db.tableinfo.UnivListTable;
import com.daxiong.fun.db.tableinfo.UserInfoTable;
import com.daxiong.fun.util.LogUtils;

public class DBHelper extends SQLiteOpenHelper {

	public static final String TAG = DBHelper.class.getSimpleName();

	public static final String DATABASE_NAME = "db_welearn.db";

	private static DBHelper DBHelper;
	private CommonDao weLearnDB;
	
	private int currentVersion=28;

	private DBHelper(Context context) {
		super(context, DATABASE_NAME, null, 28);
		// 数据库做升级，版本号+1，需要按照以下格式注明修改点
		// version:16整合数据库 yh
		// version:17 增加联系人信息表qhw
		// version:18 增加年级表、科目表yh
		// vresion:19 ???
		// version:20 增加任务规则表（问答规则、作业规则）yh
		// version:21 新增用户信息表，删除原来的用户信息表，完全使用go服务器返回数据yh
		// version:22 联系人增加小学yh
		// version:23 增加聊天表字段 
		// version:24用户信息表增加“专业”字段
		// version:25统一联系人表
		// version:26新增知识点表
		// version:27聊天表增加pageid字段
	}

	public static DBHelper getInstance() {
		if (null == DBHelper) {
			DBHelper = new DBHelper(MyApplication.getContext());
		}
		return DBHelper;
	}

	public CommonDao getWeLearnDB() {
		if (null == weLearnDB) {
			weLearnDB = new CommonDao(getInstance());
		}
		return weLearnDB;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		LogUtils.e(TAG, "onCreate");
		db.beginTransaction();

		db.execSQL(MessageTable.getCreateMessageTableSql());
//		db.execSQL(WeLearnDB.getCreateContactListNewTableSql());
		db.execSQL(GoldTable.getCreateGoldTableSql());
		db.execSQL(ReceiveUserInfoTable.getCreateReceiveUserInfoTableSql());
		db.execSQL(UnivListTable.getCreateUnivListTableSql());
//		db.execSQL(WeLearnDB.getCreateUserInfoTableSql());
//		db.execSQL(WeLearnDB.getCreateContactInfoTableSql());
		db.execSQL(GradeListTable.getCreateGradeListTableSql());
		db.execSQL(SubjectListTable.getCreateSubjectsListTableSql());
		db.execSQL(QuestionRuleInfoTable.getCreateQuestionRuleTableSql());
		db.execSQL(HomeWorkRuleInfoTable.getCreateHomeWorkRuleTableSql());
		db.execSQL(UserInfoTable.getCreateUserInfoTableSql1());
		db.execSQL(ContactsInfoTable.getCreateContactsInfoTableSql());

		db.execSQL(KnowledgeTable.getCreateKnowLedgeTableSql());//26
		
		db.setTransactionSuccessful();
		db.endTransaction();

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		LogUtils.e(TAG, "onUpgrade");
		// 数据表未做修改不需要清除数据
//		 db.beginTransaction();
//		 db.execSQL(WeLearnDB.getDeleteMessageTableSql());
//		 db.execSQL(WeLearnDB.getDeleteContactListNewTableSql());
//		 db.execSQL(WeLearnDB.getDeleteGoldTableSql());
//		 db.execSQL(WeLearnDB.getDeleteReceiveUserInfoTableSql());
//		 db.execSQL(WeLearnDB.getDeleteUnivListTableSql());
//		 db.execSQL(WeLearnDB.getDeleteUserInfoTableSql());
//		 db.setTransactionSuccessful();
//		 db.endTransaction();
		
//		if (oldVersion < 17) {
//			db.execSQL(WeLearnDB.getCreateContactInfoTableSql());
//		}
		if (oldVersion < 18) {
			db.execSQL(GradeListTable.getCreateGradeListTableSql());
			db.execSQL(SubjectListTable.getCreateSubjectsListTableSql());
		}
		if (oldVersion < 19) {
			db.execSQL("ALTER TABLE " + MessageTable.TABLE_NAME +" ADD COLUMN " + MessageTable.TASKID + " INTEGER");
			db.execSQL("ALTER TABLE " + MessageTable.TABLE_NAME +" ADD COLUMN " + MessageTable.CHECKPOINTID + " INTEGER");
			db.execSQL("ALTER TABLE " + MessageTable.TABLE_NAME +" ADD COLUMN " + MessageTable.ISRIGHT + " INTEGER");
			db.execSQL("ALTER TABLE " + MessageTable.TABLE_NAME +" ADD COLUMN " + MessageTable.COORDINATE + " TEXT");
			db.execSQL("ALTER TABLE " + MessageTable.TABLE_NAME +" ADD COLUMN " + MessageTable.IMGPATH + " TEXT");
		}
		if (oldVersion < 20) {
			db.execSQL(QuestionRuleInfoTable.getCreateQuestionRuleTableSql());
			db.execSQL(HomeWorkRuleInfoTable.getCreateHomeWorkRuleTableSql());
		}
		if (oldVersion < 21) {
			db.execSQL(UserInfoTable.getCreateUserInfoTableSql1());
			db.execSQL("DROP TABLE IF EXISTS t_user");//删除旧的用户信息表
		}
//		if (oldVersion < 22) {
//			db.execSQL("ALTER TABLE " + TableContactInfo.TABLE_NAME +" ADD COLUMN " + TableContactInfo.XIAO + " TEXT");
//		}

		//增加noticetype字段
		if (oldVersion < 23) {
			db.execSQL(CommonDao.getAlterMessageTableColumnNOTICETYPESql());
		}
		//用户信息表增加“专业”字段
		if (oldVersion < 24) {
			db.execSQL("ALTER TABLE " + ContactsInfoTable.TABLE_NAME + " ADD COLUMN " + ContactsInfoTable.MAJOR + " TEXT");
		}
		//统一联系人表
		if (oldVersion < 25) {
			db.execSQL(ContactsInfoTable.getCreateContactsInfoTableSql());
		}
		
		if (oldVersion < 26) {
			db.execSQL(KnowledgeTable.getCreateKnowLedgeTableSql());
		}
		if (oldVersion < 27) {
			db.execSQL(CommonDao.getAlterMessageTableColumnPAGEIDSql());
		}

		//向用户表中添加思想teacher字段和tabswitch字段
		if(oldVersion<28){
			db.execSQL(CommonDao.getAlterUserinfoTableColumnSixTeacherSql());
			db.execSQL(CommonDao.getAlterUserinfoTableColumnTabSwitchSql());
		}


	}

}
