
package com.daxiong.fun.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.daxiong.fun.MainActivity;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.constant.MessageConstant;
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
import com.daxiong.fun.function.homework.model.MsgData;
import com.daxiong.fun.function.question.model.CatalogModel;
import com.daxiong.fun.function.question.model.CatalogModel.Chapter;
import com.daxiong.fun.function.question.model.CatalogModel.Point;
import com.daxiong.fun.function.question.model.CatalogModel.Subject;
import com.daxiong.fun.model.ChatInfo;
import com.daxiong.fun.model.GradeModel;
import com.daxiong.fun.model.HomeWorkRuleModel;
import com.daxiong.fun.model.PayAnswerGoldGson;
import com.daxiong.fun.model.QuestionRuleModel;
import com.daxiong.fun.model.SubjectId;
import com.daxiong.fun.model.SubjectModel;
import com.daxiong.fun.model.UnivGson;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.MySharePerfenceUtil;

import java.util.ArrayList;
import java.util.List;

public class CommonDao {

	public static final String TAG = CommonDao.class.getSimpleName();

	private SQLiteDatabase welearnDataBase;

	public CommonDao(SQLiteOpenHelper helper) {
		welearnDataBase = helper.getWritableDatabase();
	}

	// ----- Message Start -----

	public static String getDeleteMessageTableSql() {
		return "DROP TABLE IF EXISTS " + MessageTable.TABLE_NAME;
	}

	public static String getAlterMessageTableColumnNOTICETYPESql() {
		return "ALTER TABLE " + MessageTable.TABLE_NAME + " ADD COLUMN " + MessageTable.NOTICETYPE + " TEXT";
	}

	/**
	 * 修改用户表结构 增加sixteacher字段
	 * @return
     */
	public static String getAlterUserinfoTableColumnSixTeacherSql() {
		return "ALTER TABLE " + UserInfoTable.TABLE_NAME + " ADD COLUMN " + UserInfoTable.SIXTEACHER + " INTEGER";
	}


	/**
	 * 修改用户表结构 增加tabswitch字段
	 * @return
	 */
	public static String getAlterUserinfoTableColumnTabSwitchSql() {
		return "ALTER TABLE " + UserInfoTable.TABLE_NAME + " ADD COLUMN " + UserInfoTable.TABBARSWITCH + " INTEGER";
	}

	/**
	 * 增加讲义id
	 * 
	 * @return
	 */
	public static String getAlterMessageTableColumnPAGEIDSql() {
		return "ALTER TABLE " + MessageTable.TABLE_NAME + " ADD COLUMN " + MessageTable.PAGEID + " INTEGER";// IF
																											// EXISTS
																											// "
																											// +
																											// MessageTable.TABLE_NAME;
	}

	public boolean insertMsg(ChatInfo info) {
		// welearnDataBase.beginTransaction();
		int type = info.getType();
		boolean isSend = type == GlobalContant.MSG_TYPE_SEND ? true : false;
		boolean isSuccess = true;
		String time = "";
		if (!isSend) {
			String sql = "SELECT " + MessageTable.USERID + " FROM " + MessageTable.TABLE_NAME + " WHERE "
					+ MessageTable.MSGTIME + " = ?";
			double timestamp = info.getTimestamp();
			time = msgTimeChangeString(timestamp);
			// Log.e("处理后的戳:", time);
			Cursor cursor = welearnDataBase.rawQuery(sql, new String[] { time });
			// execSQL(sql, new Double[]{msgtime});
			if (cursor != null && cursor.moveToFirst()) {
				isSuccess = false;
				cursor.close();
			}
		}
		if (isSuccess) {
			ContentValues values = new ContentValues();
			values.put(MessageTable.USERID, info.getFromuser());
			values.put(MessageTable.CURRENTUSERID, MySharePerfenceUtil.getInstance().getUserId());
			int contenttype = info.getContenttype();
			if (contenttype == MessageConstant.MSG_CONTENT_TYPE_TEXT
					|| contenttype == MessageConstant.MSG_CONTENT_TYPE_JUMP
					|| contenttype == MessageConstant.MSG_CONTENT_TYPE_JUMP_URL) {
				values.put(MessageTable.MSGCONTENT, info.getMsgcontent());
			}
			if (isSend) {
				values.put(MessageTable.MSGTIME, info.getLocalTimestamp());
			} else {
				values.put(MessageTable.MSGTIME, time);
			}
			values.put(MessageTable.ISSENDFAIL, 0);
			values.put(MessageTable.ISREADED, info.isReaded() ? 1 : 0);

			if (contenttype == MessageConstant.MSG_CONTENT_TYPE_JUMP) {
				MsgData data = info.getData();
				if (data != null) {
					values.put(MessageTable.ACTION, data.getAction());
					values.put(MessageTable.QUESTIONID, data.getQuestion_id());
					values.put(MessageTable.TARGET_USER_ID, data.getUserid());
					values.put(MessageTable.TARGET_ROLE_ID, data.getRoleid());
					String url = data.getUrl();
					if (url != null) {
						values.put(MessageTable.JUMPURL, url);
					}
					values.put(MessageTable.TASKID, data.getTaskid());
					values.put(MessageTable.CHECKPOINTID, data.getCheckpointid());
					values.put(MessageTable.ISRIGHT, data.getIsright());
					String coordinate = data.getCoordinate();
					if (coordinate != null) {
						values.put(MessageTable.COORDINATE, coordinate);
					}
					String imgpath = data.getImgpath();
					if (imgpath != null) {
						values.put(MessageTable.IMGPATH, imgpath);
					}

					values.put(MessageTable.PAGEID, data.getPageid());
				}
			}

			values.put(MessageTable.TYPE, info.getType());
			values.put(MessageTable.CONTENTTYPE, contenttype);
			values.put(MessageTable.FROMROLEID, info.getFromroleid());

			String noticetype = info.getNoticetype();
			if (noticetype != null) {
				values.put(MessageTable.NOTICETYPE, noticetype);
			}

			if (!TextUtils.isEmpty(info.getPath())) {
				values.put(MessageTable.PATH, info.getPath());
				values.put(MessageTable.AUDIOTIME, info.getAudiotime());
			}
			welearnDataBase.insert(MessageTable.TABLE_NAME, null, values);
		}

		// welearnDataBase.setTransactionSuccessful();
		// welearnDataBase.endTransaction();
		return isSuccess;
	}

	/**
	 * 更新发送状态
	 * 
	 * @param info
	 */
	public void update(ChatInfo info) {
		welearnDataBase.beginTransaction();
		String sql = "UPDATE " + MessageTable.TABLE_NAME + " SET " + MessageTable.ISSENDFAIL + " = ? WHERE "
				+ MessageTable.MSGTIME + " = ?";
		Integer sendFlag = info.isSendFail() ? 1 : 0;
		long timeL = info.getLocalTimestamp();
		String timeStr = "" + timeL;
		if (timeL == 0) {
			timeStr = msgTimeChangeString(info.getTimestamp());
		}
		welearnDataBase.execSQL(sql, new String[] { String.valueOf(sendFlag), timeStr });
		welearnDataBase.setTransactionSuccessful();
		welearnDataBase.endTransaction();
	}

	/**
	 * 更新是否已读
	 * 
	 * @param info
	 */
	public void updateIsReaded(ChatInfo info) {
		int _id = info.getId();
		welearnDataBase.beginTransaction();
		// 因为从数据库取出的收信,msgtime有误;而没进入数据库的消息则没有id.所以采用两者结合判断
		if (_id == 0) {
			long timeL = info.getLocalTimestamp();
			String timeStr = "" + timeL;
			if (timeL == 0) {
				timeStr = msgTimeChangeString(info.getTimestamp());
			}
			String sql = "UPDATE " + MessageTable.TABLE_NAME + " SET " + MessageTable.ISREADED + " = 1 WHERE "
					+ MessageTable.MSGTIME + " = ?";
			welearnDataBase.execSQL(sql, new String[] { timeStr });

		} else {
			String ID = _id + "";
			String sql = "UPDATE " + MessageTable.TABLE_NAME + " SET " + MessageTable.ISREADED + " = 1 WHERE "
					+ MessageTable._ID + " = ?";
			welearnDataBase.execSQL(sql, new String[] { ID });

		}
		welearnDataBase.setTransactionSuccessful();
		welearnDataBase.endTransaction();
	}

	public List<ChatInfo> queryMessageList() {
		String[] currentUserId = new String[] { String.valueOf(MySharePerfenceUtil.getInstance().getUserId()) };
		List<ChatInfo> result = new ArrayList<ChatInfo>();

		String innerSql = "SELECT * FROM " + MessageTable.TABLE_NAME + " WHERE " + MessageTable.CURRENTUSERID
				+ "=? ORDER BY " + MessageTable._ID;

		String sql = "SELECT * , count(*) - sum(" + MessageTable.ISREADED + ") as " + MessageTable.ISREADED + " FROM ("
				+ innerSql + ") a GROUP BY a." + MessageTable.USERID + " ORDER BY a." + MessageTable._ID + " DESC ";

		// String sql = "SELECT * , case " +
		// /** 都是已读的时候为1 其余时候为0 **/ //"when sum(isreaded) < count(*) then 0 " +
		// "when sum(" + MessageTable.ISREADED + ") = count(*) then 1 " +
		// "else 0 end " + "as "
		// + MessageTable.ISREADED + " FROM (" + innerSql + ") a GROUP BY a." +
		// MessageTable.USERID
		// + " ORDER BY a." + MessageTable._ID + " DESC ";

		// String sql = "SELECT * FROM (SELECT * FROM (" + innerSql +
		// ") a GROUP BY a." + MessageTable.USERID
		// + ") b ORDER BY b." + MessageTable._ID + " DESC ";
		// LogUtils.i(TAG, sql);
		Cursor cursor = welearnDataBase.rawQuery(sql, currentUserId);
		// HashMap<Integer, ChatInfo> map = new HashMap<Integer, ChatInfo>();
		if (cursor != null) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				ChatInfo info = new ChatInfo();
				String msgcontent = cursor.getString(cursor.getColumnIndex(MessageTable.MSGCONTENT));
				int contenttype = cursor.getInt(cursor.getColumnIndex(MessageTable.CONTENTTYPE));
				int userid = cursor.getInt(cursor.getColumnIndex(MessageTable.USERID));
				// map.put(userid, info);
				long msgtime = cursor.getLong(cursor.getColumnIndex(MessageTable.MSGTIME));
				String path = cursor.getString(cursor.getColumnIndex(MessageTable.PATH));
				int audiotime = cursor.getInt(cursor.getColumnIndex(MessageTable.AUDIOTIME));
				int type = cursor.getInt(cursor.getColumnIndex(MessageTable.TYPE));
				int questionid = cursor.getInt(cursor.getColumnIndex(MessageTable.QUESTIONID));
				int action = cursor.getInt(cursor.getColumnIndex(MessageTable.ACTION));
				int target_user_id = cursor.getInt(cursor.getColumnIndex(MessageTable.TARGET_USER_ID));
				int target_role_id = cursor.getInt(cursor.getColumnIndex(MessageTable.TARGET_ROLE_ID));
				int id = cursor.getInt(cursor.getColumnIndex(MessageTable._ID));
				String jumpurl = cursor.getString(cursor.getColumnIndex(MessageTable.JUMPURL));
				int fromroleid = cursor.getInt(cursor.getColumnIndex(MessageTable.FROMROLEID));

				int taskid = cursor.getInt(cursor.getColumnIndex(MessageTable.TASKID));
				int checkpointid = cursor.getInt(cursor.getColumnIndex(MessageTable.CHECKPOINTID));
				int isright = cursor.getInt(cursor.getColumnIndex(MessageTable.ISRIGHT));
				String imgpath = cursor.getString(cursor.getColumnIndex(MessageTable.IMGPATH));
				String coordinate = cursor.getString(cursor.getColumnIndex(MessageTable.COORDINATE));
				String noticetype = cursor.getString(cursor.getColumnIndex(MessageTable.NOTICETYPE));
				int issendfail = cursor.getInt(cursor.getColumnIndex(MessageTable.ISSENDFAIL));
				boolean sendFlag = issendfail == 0 ? false : true;// 1是发送失败
				info.setSendFail(sendFlag);

				int pageid = cursor.getInt(cursor.getColumnIndex(MessageTable.PAGEID));

				int isReaded = cursor.getInt(cursor.getColumnIndex(MessageTable.ISREADED));
				boolean unRead = isReaded > 0;// 在这里isReaded表示未读条数
				if (unRead) {
					MainActivity.isShowPoint = true;
				}
				info.setReaded(!unRead);
				info.setUnReadCount(isReaded);// 在这里isReaded表示未读条数

				info.setFromroleid(fromroleid);
				info.setId(id);
				info.setAudiotime(audiotime);
				info.setPath(path);
				info.setContenttype(contenttype);
				info.setFromuser(userid);
				info.setMsgcontent(msgcontent);
				info.setNoticetype(noticetype);
				if (contenttype == MessageConstant.MSG_CONTENT_TYPE_JUMP) {
					MsgData data = new MsgData();
					data.setAction(action);
					data.setQuestion_id(questionid);
					data.setUserid(target_user_id);
					data.setRoleid(target_role_id);
					data.setUrl(jumpurl);
					data.setTaskid(taskid);
					data.setCheckpointid(checkpointid);
					data.setIsright(isright);
					data.setCoordinate(coordinate);
					data.setImgpath(imgpath);
					data.setPageid(pageid);
					info.setData(data);
				}

				if (type == GlobalContant.MSG_TYPE_RECV) {
					info.setTimestamp(msgtime);
				} else {
					info.setLocalTimestamp(msgtime);
				}
				info.setType(type);
				result.add(info);
			}
			cursor.close();
		}
		return result;
	}

	public List<ChatInfo> queryAllByUserid(int userid, int pageIndex) {
		ArrayList<ChatInfo> result = new ArrayList<ChatInfo>();
		String sql = "SELECT * FROM " + MessageTable.TABLE_NAME + " WHERE " + MessageTable.USERID + "=? AND "
				+ MessageTable.CURRENTUSERID + "=? ORDER BY " + MessageTable._ID + " DESC LIMIT ?, ? ";
		// LogUtils.i(TAG, sql);
		Cursor cursor = welearnDataBase.rawQuery(sql, new String[] { userid + "",
				MySharePerfenceUtil.getInstance().getUserId() + "", pageIndex * 20 + "", 20 + "" });
		// LogUtils.i(TAG, userid + " " + pageIndex + " ");
		if (cursor != null) {
			// welearnDataBase.execSQL("UPDATE " + MessageTable.TABLE_NAME +
			// " SET " + MessageTable.ISREADED
			// + " = 1 WHERE " + MessageTable.USERID + " = " + userid + " AND "
			// + MessageTable.CONTENTTYPE +" <> " +
			// GlobalContant.MSG_CONTENT_TYPE_JUMP);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				ChatInfo info = new ChatInfo();
				String msgcontent = cursor.getString(cursor.getColumnIndex(MessageTable.MSGCONTENT));
				int contenttype = cursor.getInt(cursor.getColumnIndex(MessageTable.CONTENTTYPE));
				long msgtime = cursor.getLong(cursor.getColumnIndex(MessageTable.MSGTIME));
				int type = cursor.getInt(cursor.getColumnIndex(MessageTable.TYPE));
				String path = cursor.getString(cursor.getColumnIndex(MessageTable.PATH));
				int audiotime = cursor.getInt(cursor.getColumnIndex(MessageTable.AUDIOTIME));
				int questionid = cursor.getInt(cursor.getColumnIndex(MessageTable.QUESTIONID));
				int action = cursor.getInt(cursor.getColumnIndex(MessageTable.ACTION));
				int target_user_id = cursor.getInt(cursor.getColumnIndex(MessageTable.TARGET_USER_ID));
				int target_role_id = cursor.getInt(cursor.getColumnIndex(MessageTable.TARGET_ROLE_ID));
				int id = cursor.getInt(cursor.getColumnIndex(MessageTable._ID));
				String jumpurl = cursor.getString(cursor.getColumnIndex(MessageTable.JUMPURL));
				int fromroleid = cursor.getInt(cursor.getColumnIndex(MessageTable.FROMROLEID));
				int issendfail = cursor.getInt(cursor.getColumnIndex(MessageTable.ISSENDFAIL));

				int taskid = cursor.getInt(cursor.getColumnIndex(MessageTable.TASKID));
				int checkpointid = cursor.getInt(cursor.getColumnIndex(MessageTable.CHECKPOINTID));
				int isright = cursor.getInt(cursor.getColumnIndex(MessageTable.ISRIGHT));
				String imgpath = cursor.getString(cursor.getColumnIndex(MessageTable.IMGPATH));
				String coordinate = cursor.getString(cursor.getColumnIndex(MessageTable.COORDINATE));

				boolean sendFlag = issendfail == 0 ? false : true;
				info.setSendFail(sendFlag);

				int pageid = cursor.getInt(cursor.getColumnIndex(MessageTable.PAGEID));

				if (contenttype == MessageConstant.MSG_CONTENT_TYPE_JUMP
						|| contenttype == MessageConstant.MSG_CONTENT_TYPE_JUMP_URL
						|| contenttype == MessageConstant.MSG_CONTENT_TYPE_AUDIO) {
					int isReaded = cursor.getInt(cursor.getColumnIndex(MessageTable.ISREADED));
					String noticetype = cursor.getString(cursor.getColumnIndex(MessageTable.NOTICETYPE));// 0代表不需要红点,
																											// 1代表要有红点
					int redpoint = 1;
					if (noticetype != null && noticetype.length() == 3) {
						redpoint = Integer.parseInt(noticetype.charAt(0) + "");
					}
					boolean readedFlag = isReaded == 0 ? false : true;
					if (redpoint == 0) {
						info.setReaded(true);
						welearnDataBase.execSQL("UPDATE " + MessageTable.TABLE_NAME + " SET " + MessageTable.ISREADED
								+ " = 1 WHERE " + MessageTable._ID + " = "
								+ cursor.getInt(cursor.getColumnIndex(MessageTable._ID)));
					} else if (redpoint == 1) {
						info.setReaded(readedFlag);
					}
				} else {
					info.setReaded(true);
					welearnDataBase.execSQL("UPDATE " + MessageTable.TABLE_NAME + " SET " + MessageTable.ISREADED
							+ " = 1 WHERE " + MessageTable._ID + " = "
							+ cursor.getInt(cursor.getColumnIndex(MessageTable._ID)));
				}

				info.setFromroleid(fromroleid);
				info.setId(id);
				info.setAudiotime(audiotime);
				info.setPath(path);
				info.setContenttype(contenttype);
				info.setFromuser(userid);
				info.setMsgcontent(msgcontent);

				// info.setJumpUrl(jumpurl);
				// info.setQuestion_id(questionid);
				// info.setAction(action);
				// info.setTarget_role_id(target_role_id);
				// info.setTarget_user_id(target_user_id);

				if (contenttype == MessageConstant.MSG_CONTENT_TYPE_JUMP) {
					MsgData data = new MsgData();
					data.setAction(action);
					data.setQuestion_id(questionid);
					data.setUserid(target_user_id);
					data.setRoleid(target_role_id);
					data.setUrl(jumpurl);
					data.setTaskid(taskid);
					data.setCheckpointid(checkpointid);
					data.setIsright(isright);
					data.setCoordinate(coordinate);
					data.setImgpath(imgpath);
					data.setPageid(pageid);
					info.setData(data);
				}

				if (type == GlobalContant.MSG_TYPE_RECV) {
					info.setTimestamp(msgtime);
				} else {
					info.setLocalTimestamp(msgtime);
				}
				info.setType(type);
				result.add(info);
			}
			cursor.close();
		}
		return result;
	}

	public void deleteMsgInChatView(ChatInfo chat) {
		int _id = chat.getId();
		welearnDataBase.beginTransaction();
		// 因为从数据库取出的收信,msgtime有误;而没进入数据库的消息则没有id.所以采用两者结合判断
		if (_id == 0) {
			long timeL = chat.getLocalTimestamp();
			String timeStr = "" + timeL;
			if (timeL == 0) {
				timeStr = msgTimeChangeString(chat.getTimestamp());
			}
			String sql = "delete from " + MessageTable.TABLE_NAME + " where " + MessageTable.MSGTIME + " =?";
			welearnDataBase.execSQL(sql, new String[] { timeStr });
		} else {
			String sql = "delete from " + MessageTable.TABLE_NAME + " where " + MessageTable._ID + " =?";
			String ID = _id + "";
			welearnDataBase.execSQL(sql, new String[] { ID });
		}
		welearnDataBase.setTransactionSuccessful();
		welearnDataBase.endTransaction();
	}

	public void deleteMsg(int userid) {
		welearnDataBase.beginTransaction();
		String sql = "DELETE FROM " + MessageTable.TABLE_NAME + " WHERE " + MessageTable.USERID + "=? AND "
				+ MessageTable.CURRENTUSERID + "=?";
		welearnDataBase.execSQL(sql, new Object[] { userid, MySharePerfenceUtil.getInstance().getUserId() });
		welearnDataBase.setTransactionSuccessful();
		welearnDataBase.endTransaction();
	}

	public void updateMsg(int userid) {
		welearnDataBase.beginTransaction();
		String sql = "UPDATE " + MessageTable.TABLE_NAME + " SET " + MessageTable.CURRENTUSERID + "=? WHERE "
				+ MessageTable.CURRENTUSERID + "=?";
		welearnDataBase.execSQL(sql, new Object[] { userid, 0 });
		welearnDataBase.setTransactionSuccessful();
		welearnDataBase.endTransaction();
	}

	public String msgTimeChangeString(double msgtime) {
		String time = msgtime + "";
		// LogUtils.e("处理前的戳:", time);
		if (time.contains("e") || time.contains("E")) {
			int index = 0;
			if (time.contains("e")) {
				index = time.indexOf("e");
			} else {
				index = time.indexOf("E");
			}
			String x = time.substring(index + 1, index + 2);
			int xInt = Integer.parseInt(x) + 1;
			time = time.substring(0, index);
			char[] chars = time.toCharArray();
			time = "";
			for (int i = 0; i < chars.length; i++) {
				if (i == 11) {
					time = time + chars[1];
				}
				if (i != 1) {
					time = time + chars[i];
				}
			}
			int length = time.length();
			xInt = xInt - length;
			if (xInt > 0) {
				for (int i = 0; i < xInt; i++) {
					time = time + "0";
				}
			}

		}
		return time;
	}

	// ----- Message End -----

	// ----- Gold Start -----

	public static String getDeleteGoldTableSql() {
		return "DROP TABLE IF EXISTS " + GoldTable.TABLE_NAME;
	}

	public void insertGold(List<PayAnswerGoldGson> gsonlist) {
		try {
			welearnDataBase.beginTransaction();
			welearnDataBase.delete(GoldTable.TABLE_NAME, null, null);
			// welearnDB.execSQL("DROP TABLE IF EXISTS p_gold");
			for (int i = 0; i < gsonlist.size(); i++) {
				PayAnswerGoldGson grades = gsonlist.get(i);
				for (int j = 0; j < grades.getSubjects().size(); j++) {
					SubjectId subject = grades.getSubjects().get(j);
					// Log.e("subject:", subject.toString());
					ContentValues values = new ContentValues();
					values.put(GoldTable.GRADEID, i + 1);
					values.put(GoldTable.SUBJECT, j + 1);
					values.put(GoldTable.BASEGOLD, subject.getOriginal());
					values.put(GoldTable.MAXGOLD, subject.getMax());
					values.put(GoldTable.MINGOLD, subject.getMin());
					welearnDataBase.insert(GoldTable.TABLE_NAME, null, values);
				}
			}
			MySharePerfenceUtil.getInstance().setUpDatePayAskGoldTime();
			welearnDataBase.setTransactionSuccessful();
			welearnDataBase.endTransaction();
			// WeLearnSpUtil.getInstance().setIsDownUnivList(true);
		} catch (Exception e) {
			// WeLearnSpUtil.getInstance().setIsLogin(false);
			e.printStackTrace();
		} finally {
			GlobalVariable.doingGoldDB = false;
		}
	}

	public SubjectId queryGradeIdAndSubjectId(int gradeid, int subjectid) {

		String querySql = "SELECT * FROM " + GoldTable.TABLE_NAME + " WHERE " + GoldTable.GRADEID + " = ? AND "
				+ GoldTable.SUBJECT + " = ?";
		// LogUtils.i(TAG, innerSql);
		Cursor cursor = welearnDataBase.rawQuery(querySql, new String[] { gradeid + "", subjectid + "" });
		if (cursor != null && cursor.moveToFirst()) {
			SubjectId subject = new SubjectId();
			subject.setOriginal(cursor.getFloat(cursor.getColumnIndex(GoldTable.BASEGOLD)));
			subject.setMax(cursor.getFloat(cursor.getColumnIndex(GoldTable.MAXGOLD)));
			subject.setMin(cursor.getFloat(cursor.getColumnIndex(GoldTable.MINGOLD)));
			// Log.e("DB subject:", subject.toString());
			cursor.close();
			return subject;
		}
		return null;
	}

	// ----- Gold End -----

	// ----- ReceiveUserInfo Start -----

	public static String getDeleteReceiveUserInfoTableSql() {
		return "DROP TABLE IF EXISTS " + ReceiveUserInfoTable.TABLE_NAME;
	}

	public void insertorUpdate(int userid, int roleid, String name, String logo_url) {
		UserInfoModel ru = queryByUserId(userid, false);
		welearnDataBase.beginTransaction();
		if (ru == null) {// 如果没有，则进行查询
			// LogUtils.i(TAG, "insert recv db");
			String sql = "INSERT INTO " + ReceiveUserInfoTable.TABLE_NAME + "('" + ReceiveUserInfoTable.USERID + "', '"
					+ ReceiveUserInfoTable.ROLEID + "', '" + ReceiveUserInfoTable.NAME + "', "
					+ ReceiveUserInfoTable.AVATAR_100 + ") values (?, ?, ?, ?)";
			welearnDataBase.execSQL(sql, new Object[] { userid, roleid, name, logo_url });
		} else {// 否则进行更新
			// LogUtils.i(TAG, "update recv db");
			updateRecvUser(userid, roleid, name, logo_url);
		}
		welearnDataBase.setTransactionSuccessful();
		welearnDataBase.endTransaction();
	}

	// add by milo 2014.09.07
	private void updateRecvUser(int userid, int roleid, String name, String logo_url) {
		if (userid > 0) {
			ContentValues values = new ContentValues();
			values.put(ReceiveUserInfoTable.AVATAR_100, logo_url);
			values.put(ReceiveUserInfoTable.NAME, name);
			values.put(ReceiveUserInfoTable.ROLEID, roleid);
			try {
				welearnDataBase.update(ReceiveUserInfoTable.TABLE_NAME, values,
						ReceiveUserInfoTable.USERID + "=" + userid, null);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}
	}

	public UserInfoModel queryByUserId(int userid, boolean isClose) {
		UserInfoModel result = null;
		String sql = "SELECT * FROM " + ReceiveUserInfoTable.TABLE_NAME + " WHERE " + ReceiveUserInfoTable.USERID
				+ "=?";
		Cursor cur = welearnDataBase.rawQuery(sql, new String[] { String.valueOf(userid) });
		if (cur != null) {
			if (cur.moveToNext()) {
				result = new UserInfoModel();
				String name = cur.getString(cur.getColumnIndex(ReceiveUserInfoTable.NAME));
				String avatar_100 = cur.getString(cur.getColumnIndex(ReceiveUserInfoTable.AVATAR_100));
				int roleid = cur.getInt(cur.getColumnIndex(ReceiveUserInfoTable.ROLEID));

				result.setRoleid(roleid);
				result.setAvatar_100(avatar_100);
				result.setName(name);
				result.setUserid(userid);
				if (avatar_100 != null) {
					if (avatar_100.startsWith("/")) {
						result = null;
					}
				}
			}
			cur.close();
		}
		return result;
	}

	// ----- ReceiveUserInfo End -----

	// ----- UnivList Start -----

	public static String getDeleteUnivListTableSql() {
		return "DROP TABLE IF EXISTS " + UnivListTable.TABLE_NAME;
	}

	public void insertUniv(List<UnivGson> univList) {
		try {
			welearnDataBase.beginTransaction();
			welearnDataBase.delete(UnivListTable.TABLE_NAME, null, null);
			for (UnivGson univGson : univList) {
				ContentValues values = new ContentValues();
				values.put(UnivListTable._ID, univGson.getId());
				values.put(UnivListTable.NAME, univGson.getName());
				welearnDataBase.insert(UnivListTable.TABLE_NAME, null, values);
			}
			welearnDataBase.setTransactionSuccessful();
			welearnDataBase.endTransaction();

			MySharePerfenceUtil.getInstance().setIsDownUnivList(true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	public List<UnivGson> queryUnivListByName(String name) {
		name = "%" + name + "%";

		String innerSql = "SELECT * FROM " + UnivListTable.TABLE_NAME + " WHERE " + UnivListTable.NAME
				+ " LIKE ? ORDER BY " + UnivListTable._ID;
		// LogUtils.i(TAG, innerSql);
		Cursor cursor = welearnDataBase.rawQuery(innerSql, new String[] { name });
		if (cursor != null) {
			List<UnivGson> list = new ArrayList<UnivGson>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				UnivGson univ = new UnivGson();
				univ.setId(cursor.getInt(cursor.getColumnIndex(UnivListTable._ID)));
				univ.setName(cursor.getString(cursor.getColumnIndex(UnivListTable.NAME)));
				list.add(univ);
			}
			cursor.close();
			return list;
		}
		return null;
	}

	// ----- UnivList End -----

	// ----- Subjects Start -----

	public static String getDeleteSubjectsListTableSql() {
		return "DROP TABLE IF EXISTS " + SubjectListTable.TABLE_NAME;
	}

	/**
	 * 获取科目数量
	 * 
	 * @return
	 */
	public int getSubjectCount() {
		try {
			Cursor cur = welearnDataBase.query(SubjectListTable.TABLE_NAME, null, null, null, null, null, null);
			if (null != cur) {
				return cur.getCount();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 判断sub_list表中是否有语文
	 * 
	 * @author: sky
	 * @return int
	 */
	public int querySubjectWithYuwen() {
		try {
			// Cursor cur = welearnDataBase.execSQL("select name from sub_list
			// where name like '%语文%'");
			// Cursor cur = welearnDataBase.query(SubjectListTable.TABLE_NAME,
			// new String[] {
			// SubjectListTable.NAME
			// }, SubjectListTable.NAME + " like ?", new String[] {
			// "语文"
			// }, null, null, null);
			Cursor cur = welearnDataBase.rawQuery("select name from sub_list where name like ?",
					new String[] { "%语文%" });
			if (null != cur) {
				return cur.getCount();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	public void insertGrade(GradeModel gradeModel) {
		try {
			ContentValues values = new ContentValues();
			values.put(GradeListTable.ID, gradeModel.getId());
			values.put(GradeListTable.GRADE_ID, gradeModel.getGradeId());
			values.put(GradeListTable.NAME, gradeModel.getName());
			values.put(GradeListTable.SUBJECTS, gradeModel.getSubjects());
			welearnDataBase.insert(GradeListTable.TABLE_NAME, null, values);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	public void insertSubject(SubjectModel subModel) {
		try {
			ContentValues values = new ContentValues();
			values.put(SubjectListTable.SUB_ID, subModel.getId());
			values.put(SubjectListTable.NAME, subModel.getName());
			welearnDataBase.insert(SubjectListTable.TABLE_NAME, null, values);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	public ArrayList<SubjectModel> querySubjectByIdList(ArrayList<Integer> subIdList) {

		if (null == subIdList || subIdList.size() == 0) {
			return null;
		}

		ArrayList<SubjectModel> list = new ArrayList<SubjectModel>();
		SubjectModel result = null;

		StringBuffer selection = new StringBuffer();
		for (int i = 0; i < subIdList.size(); i++) {
			if (i > 0) {
				selection.append(" OR ");
			} else {
				selection.append(" WHERE ");
			}
			selection.append(SubjectListTable.SUB_ID + " = " + subIdList.get(i));
		}

		String sql = "SELECT * FROM " + SubjectListTable.TABLE_NAME + selection.toString();
		// sql = "SELECT * FROM " + SubjectListTable.TABLE_NAME;

		Cursor cur = welearnDataBase.rawQuery(sql, null);

		if (cur != null) {
			while (cur.moveToNext()) {
				result = new SubjectModel();

				int sid = cur.getInt(cur.getColumnIndex(SubjectListTable.SUB_ID));
				String name = cur.getString(cur.getColumnIndex(SubjectListTable.NAME));

				result.setId(sid);
				result.setName(name);
				list.add(result);
			}
			cur.close();
		}
		return list;
	}

	/**
	 * 更新科目
	 * 
	 * @param subject
	 */
	public int updateSubject(SubjectModel subject) {
		ContentValues cv = new ContentValues();
		cv.put(SubjectListTable.NAME, subject.getName());
		return welearnDataBase.update(SubjectListTable.TABLE_NAME, cv, SubjectListTable.SUB_ID, new String[] {});
	}

	// ----- Subjects End -----

	// ----- Grade Start -----

	public static String getDeleteGradeTableSql() {
		return "DROP TABLE IF EXISTS " + GradeListTable.TABLE_NAME;
	}

	public GradeModel queryGradeByGradeId(int gradeId) {
		GradeModel result = null;

		Cursor cur = welearnDataBase.query(GradeListTable.TABLE_NAME, null, GradeListTable.ID + "=?",
				new String[] { String.valueOf(gradeId) }, null, null, null);

		if (cur != null) {
			if (cur.moveToNext()) {
				result = new GradeModel();

				int sid = cur.getInt(cur.getColumnIndex(GradeListTable.ID));
				int gid = cur.getInt(cur.getColumnIndex(GradeListTable.GRADE_ID));
				String name = cur.getString(cur.getColumnIndex(GradeListTable.NAME));
				String subjects = cur.getString(cur.getColumnIndex(GradeListTable.SUBJECTS));

				result.setId(sid);
				result.setGradeId(gid);
				result.setName(name);
				result.setSubjects(subjects);
				return result;
			}
			cur.close();
		}

		return null;
	}

	/**
	 * 获取所有年级：小学、初中、高中
	 * 
	 * @param gid
	 * @return
	 */
	public ArrayList<GradeModel> queryAllGradeParent() {
		Cursor cur = welearnDataBase.query(GradeListTable.TABLE_NAME, null, GradeListTable.ID + "> 100", null, null,
				null, null);
		return toGradeList(cur);
	}

	/**
	 * 根据年级Id查询所有的子年级
	 * 
	 * @param gradeId
	 * @return
	 */
	public ArrayList<GradeModel> queryGradeByParentId(int gradeId) {
		Cursor cur = welearnDataBase.query(GradeListTable.TABLE_NAME, null, GradeListTable.GRADE_ID + "=?",
				new String[] { String.valueOf(gradeId) }, null, null, null);

		return toGradeList(cur);
	}

	private ArrayList<GradeModel> toGradeList(Cursor cur) {
		ArrayList<GradeModel> list = new ArrayList<GradeModel>();
		if (null == cur) {
			return list;
		}

		GradeModel result = null;
		while (cur.moveToNext()) {
			result = new GradeModel();

			int sid = cur.getInt(cur.getColumnIndex(GradeListTable.ID));
			int gid = cur.getInt(cur.getColumnIndex(GradeListTable.GRADE_ID));
			String name = cur.getString(cur.getColumnIndex(GradeListTable.NAME));
			String subjects = cur.getString(cur.getColumnIndex(GradeListTable.SUBJECTS));

			result.setId(sid);
			result.setGradeId(gid);
			result.setName(name);
			result.setSubjects(subjects);
			list.add(result);
		}
		cur.close();
		return list;
	}

	// ----- Grade End -----

	// ----- QuestionRule Start -----

	public void insertOrUpdateQuestionRuleInfo(QuestionRuleModel qrm) {
		if (null == qrm) {
			return;
		}

		Cursor cur = welearnDataBase.query(QuestionRuleInfoTable.TABLE_NAME, null,
				QuestionRuleInfoTable.GRADE_ID + "=?", new String[] { String.valueOf(qrm.getGradeId()) }, null, null,
				null);
		int count = 0;
		if (null != cur) {
			count = cur.getCount();
			cur.close();
		}
		ContentValues cv = new ContentValues();
		cv.put(QuestionRuleInfoTable.GRADE_NAME, qrm.getGrade());
		cv.put(QuestionRuleInfoTable.TIME, qrm.getTime());
		cv.put(QuestionRuleInfoTable.MONEY, qrm.getMoney());
		if (count > 0) {
			welearnDataBase.update(QuestionRuleInfoTable.TABLE_NAME, cv, QuestionRuleInfoTable.GRADE_ID + " = ?",
					new String[] { String.valueOf(qrm.getGradeId()) });
		} else {
			cv.put(QuestionRuleInfoTable.GRADE_ID, qrm.getGradeId());
			welearnDataBase.insert(QuestionRuleInfoTable.TABLE_NAME, null, cv);
		}
	}

	public QuestionRuleModel queryQuestionRuleInfoByGradeId(int gradeId) {
		Cursor cur = welearnDataBase.query(QuestionRuleInfoTable.TABLE_NAME, null,
				QuestionRuleInfoTable.GRADE_ID + "=?", new String[] { String.valueOf(gradeId) }, null, null, null);
		if (null == cur) {
			return null;
		} else {
			if (cur.moveToNext()) {
				QuestionRuleModel qrm = new QuestionRuleModel();
				qrm.setGradeId(cur.getInt(cur.getColumnIndex(QuestionRuleInfoTable.GRADE_ID)));
				qrm.setGrade(cur.getString(cur.getColumnIndex(QuestionRuleInfoTable.GRADE_NAME)));
				qrm.setTime(cur.getInt(cur.getColumnIndex(QuestionRuleInfoTable.TIME)));
				qrm.setMoney(cur.getFloat(cur.getColumnIndex(QuestionRuleInfoTable.MONEY)));
				return qrm;
			}
			cur.close();
			return null;
		}
	}

	// ----- QuestionRule End -----

	// ----- HomeWorkRule Start -----

	public void insertOrUpdateHomeWorkRuleInfo(HomeWorkRuleModel hwrm) {
		if (null == hwrm) {
			return;
		}

		Cursor cur = welearnDataBase.query(HomeWorkRuleInfoTable.TABLE_NAME, null,
				HomeWorkRuleInfoTable.GRADE_ID + "=?", new String[] { String.valueOf(hwrm.getGradeId()) }, null, null,
				null);
		int count = 0;
		if (null != cur) {
			count = cur.getCount();
			cur.close();
		}
		ContentValues cv = new ContentValues();
		cv.put(HomeWorkRuleInfoTable.GRADE_NAME, hwrm.getGarde());
		cv.put(HomeWorkRuleInfoTable.DEFAULT_PIC_COUNT, hwrm.getDefault_pic_count());
		cv.put(HomeWorkRuleInfoTable.MAX_PIC_COUNT, hwrm.getMax_pic_count());
		cv.put(HomeWorkRuleInfoTable.DEFAULT_PIC_MONEY, hwrm.getDefault_pic_money());
		cv.put(HomeWorkRuleInfoTable.SINGLE_PIC_MONEY, hwrm.getSingle_pic_money());
		cv.put(HomeWorkRuleInfoTable.TIME, hwrm.getTime());
		if (count > 0) {
			welearnDataBase.update(HomeWorkRuleInfoTable.TABLE_NAME, cv, HomeWorkRuleInfoTable.GRADE_ID + " = ?",
					new String[] { String.valueOf(hwrm.getGradeId()) });
		} else {
			cv.put(HomeWorkRuleInfoTable.GRADE_ID, hwrm.getGradeId());
			welearnDataBase.insert(HomeWorkRuleInfoTable.TABLE_NAME, null, cv);
		}
	}

	public HomeWorkRuleModel queryHomeworkRuleInfoByGradeId(int gradeId) {
		Cursor cur = welearnDataBase.query(HomeWorkRuleInfoTable.TABLE_NAME, null,
				HomeWorkRuleInfoTable.GRADE_ID + "=?", new String[] { String.valueOf(gradeId) }, null, null, null);
		if (null == cur) {
			return null;
		} else {
			if (cur.moveToNext()) {
				HomeWorkRuleModel hwrm = new HomeWorkRuleModel();
				hwrm.setGradeId(cur.getInt(cur.getColumnIndex(HomeWorkRuleInfoTable.GRADE_ID)));
				hwrm.setGrade(cur.getString(cur.getColumnIndex(HomeWorkRuleInfoTable.GRADE_NAME)));
				hwrm.setDefault_pic_count(cur.getInt(cur.getColumnIndex(HomeWorkRuleInfoTable.DEFAULT_PIC_COUNT)));
				hwrm.setMax_pic_count(cur.getInt(cur.getColumnIndex(HomeWorkRuleInfoTable.MAX_PIC_COUNT)));
				hwrm.setDefault_pic_money(cur.getFloat(cur.getColumnIndex(HomeWorkRuleInfoTable.DEFAULT_PIC_MONEY)));
				hwrm.setSingle_pic_money(cur.getFloat(cur.getColumnIndex(HomeWorkRuleInfoTable.SINGLE_PIC_MONEY)));
				hwrm.setTime(cur.getInt(cur.getColumnIndex(HomeWorkRuleInfoTable.TIME)));
				return hwrm;
			}
			cur.close();
			return null;
		}
	}

	// ----- HomeWorkRule End -----

	// ----- UserInfoTable Start -----

	public UserInfoModel queryCurrentUserInfo() {
		int getspUserId = MySharePerfenceUtil.getInstance().getspUserId();
		Cursor c = welearnDataBase.query(UserInfoTable.TABLE_NAME, null, UserInfoTable.USERID + " = ?",
				new String[] { String.valueOf(getspUserId) }, null, null, null);
		if (null == c) {
			return null;
		} else {
			if (c.moveToNext()) {
				UserInfoModel u = new UserInfoModel();
				u.setAdoptcnt(c.getInt(c.getColumnIndex(UserInfoTable.ADOPTCNT)));
				u.setAge(c.getInt(c.getColumnIndex(UserInfoTable.AGE)));
				u.setAllowgrab(c.getInt(c.getColumnIndex(UserInfoTable.ALLOWGRAB)));
				u.setAmountamt(c.getString(c.getColumnIndex(UserInfoTable.AMOUNTAMT)));
				u.setArbcnt(c.getInt(c.getColumnIndex(UserInfoTable.ARBCNT)));
				u.setAvatar_100(c.getString(c.getColumnIndex(UserInfoTable.AVATAR_100)));
				u.setAvatar_40(c.getString(c.getColumnIndex(UserInfoTable.AVATAR_40)));
				u.setCity(c.getString(c.getColumnIndex(UserInfoTable.CITY)));
				u.setCountamt(c.getInt(c.getColumnIndex(UserInfoTable.COUNTAMT)));
				u.setCredit(c.getFloat(c.getColumnIndex(UserInfoTable.CREDIT)));
				u.setDreamuniv(c.getString(c.getColumnIndex(UserInfoTable.DREAMUNIV)));
				u.setDreamunivid(c.getString(c.getColumnIndex(UserInfoTable.DREAMUNIVID)));
				u.setEarngold(c.getFloat(c.getColumnIndex(UserInfoTable.EARNGOLD)));
				u.setEdulevel(c.getInt(c.getColumnIndex(UserInfoTable.EDULEVEL)));
				u.setEmail(c.getString(c.getColumnIndex(UserInfoTable.EMAIL)));
				u.setExpensesamt(c.getFloat(c.getColumnIndex(UserInfoTable.EXPENSESAMT)));
				u.setFromchan(c.getString(c.getColumnIndex(UserInfoTable.FROMCHAN)));
				u.setGold(c.getFloat(c.getColumnIndex(UserInfoTable.GOLD)));
				u.setGrade(c.getString(c.getColumnIndex(UserInfoTable.GRADE)));
				u.setGradeid(c.getInt(c.getColumnIndex(UserInfoTable.GRADEID)));
				u.setGroupphoto(c.getString(c.getColumnIndex(UserInfoTable.GROUPPHOTO)));
				u.setHomeworkcnt(c.getInt(c.getColumnIndex(UserInfoTable.HOMEWORKCNT)));
				u.setInfostate(c.getInt(c.getColumnIndex(UserInfoTable.INFOSTATE)));
				u.setName(c.getString(c.getColumnIndex(UserInfoTable.NAME)));
				u.setNamepinyin(c.getString(c.getColumnIndex(UserInfoTable.NAMEPINYIN)));
				u.setProvince(c.getString(c.getColumnIndex(UserInfoTable.PROVINCE)));
				u.setQuickcnt(c.getInt(c.getColumnIndex(UserInfoTable.QUICKCNT)));
				u.setRoleid(c.getInt(c.getColumnIndex(UserInfoTable.ROLEID)));
				u.setSchools(c.getString(c.getColumnIndex(UserInfoTable.SCHOOLS)));
				u.setSchoolsid(c.getInt(c.getColumnIndex(UserInfoTable.SCHOOLSID)));
				u.setSex(c.getInt(c.getColumnIndex(UserInfoTable.SEX)));
				u.setState(c.getInt(c.getColumnIndex(UserInfoTable.STATE)));
				u.setSupervip(c.getInt(c.getColumnIndex(UserInfoTable.SUPERVIP)));
				u.setTeachlevel(c.getInt(c.getColumnIndex(UserInfoTable.TEACHLEVEL)));
				u.setTel(c.getString(c.getColumnIndex(UserInfoTable.TEL)));
				u.setThirdname(c.getString(c.getColumnIndex(UserInfoTable.THIRDNAME)));
				u.setTokenid(c.getString(c.getColumnIndex(UserInfoTable.TOKENID)));
				u.setUserid(c.getInt(c.getColumnIndex(UserInfoTable.USERID)));
				u.setWelearnid(c.getInt(c.getColumnIndex(UserInfoTable.WELEARNID)));
				u.setTeachmajor(c.getString(c.getColumnIndex(UserInfoTable.TEACHMAJOR)));
				u.setMajor(c.getString(c.getColumnIndex(UserInfoTable.MAJOR)));
				u.setNewuser(c.getInt(c.getColumnIndex(UserInfoTable.NEWUSER)));
				u.setHwrate(c.getInt(c.getColumnIndex(UserInfoTable.HWRATE)));
				u.setQsrate(c.getInt(c.getColumnIndex(UserInfoTable.QSRATE)));
				u.setFdfs_avatar(c.getString(c.getColumnIndex(UserInfoTable.FDFS_AVATAR)));
				u.setFdfs_groupphoto(c.getString(c.getColumnIndex(UserInfoTable.FDFS_GROUPPHOTO)));
				u.setRemind(c.getString(c.getColumnIndex(UserInfoTable.REMIND)));
				u.setVip_content(c.getString(c.getColumnIndex(UserInfoTable.VIP_CONTENT)));
				u.setVip_left_time(c.getInt(c.getColumnIndex(UserInfoTable.VIP_LEFT_TIME)));
				u.setVip_type(c.getInt(c.getColumnIndex(UserInfoTable.VIP_TYPE)));
				u.setTabbarswitch(c.getInt(c.getColumnIndex(UserInfoTable.TABBARSWITCH)));
				u.setSixteacher(c.getInt(c.getColumnIndex(UserInfoTable.SIXTEACHER)));
				u.setVip_is_expired(c.getInt(c.getColumnIndex(UserInfoTable.IS_EXPIRED)));
				c.close();
				return u;
			} else {
				c.close();
			}
		}
		return null;
	}

	public boolean isUserExists(int uid) {
		Cursor c = welearnDataBase.query(UserInfoTable.TABLE_NAME, null, UserInfoTable.USERID + " = ?",
				new String[] { String.valueOf(uid) }, null, null, null);
		if (null == c) {
			return false;
		} else {
			int count = c.getCount();
			c.close();
			if (count > 0) {
				UserInfoModel queryCurrentUserInfo = queryCurrentUserInfo();
				if (uid == queryCurrentUserInfo.getUserid()) {

					return true;
				}
				return false;
			}
		}
		return false;
	}

	public long insertOrUpdatetUserInfo(UserInfoModel uInfo) {
		if (null == uInfo) {
			return 0L;
		}
		MySharePerfenceUtil.getInstance().setUserId(uInfo.getUserid());
		boolean isUpdate = isUserExists(uInfo.getUserid());

		ContentValues cv = new ContentValues();
		cv.put(UserInfoTable.ADOPTCNT, uInfo.getAdoptcnt());
		cv.put(UserInfoTable.AGE, uInfo.getAge());
		cv.put(UserInfoTable.ALLOWGRAB, uInfo.getAllowgrab());
		cv.put(UserInfoTable.AMOUNTAMT, uInfo.getAmountamt());
		cv.put(UserInfoTable.ARBCNT, uInfo.getArbcnt());
		cv.put(UserInfoTable.AVATAR_100, uInfo.getAvatar_100());
		cv.put(UserInfoTable.AVATAR_40, uInfo.getAvatar_40());
		cv.put(UserInfoTable.CITY, uInfo.getCity());
		cv.put(UserInfoTable.COUNTAMT, uInfo.getCountamt());
		cv.put(UserInfoTable.CREDIT, uInfo.getCredit());
		cv.put(UserInfoTable.DREAMUNIV, uInfo.getDreamuniv());
		cv.put(UserInfoTable.DREAMUNIVID, uInfo.getDreamunivid());
		cv.put(UserInfoTable.EARNGOLD, uInfo.getEarngold());
		cv.put(UserInfoTable.EDULEVEL, uInfo.getEdulevel());
		cv.put(UserInfoTable.EMAIL, uInfo.getEmail());
		cv.put(UserInfoTable.EXPENSESAMT, uInfo.getExpensesamt());
		cv.put(UserInfoTable.FROMCHAN, uInfo.getFromchan());
		cv.put(UserInfoTable.GOLD, uInfo.getGold());
		cv.put(UserInfoTable.GRADE, uInfo.getGrade());
		cv.put(UserInfoTable.GRADEID, uInfo.getGradeid());
		cv.put(UserInfoTable.GROUPPHOTO, uInfo.getGroupphoto());
		cv.put(UserInfoTable.HOMEWORKCNT, uInfo.getHomeworkcnt());
		cv.put(UserInfoTable.INFOSTATE, uInfo.getInfostate());
		cv.put(UserInfoTable.NAME, uInfo.getName());
		cv.put(UserInfoTable.NAMEPINYIN, uInfo.getNamepinyin());
		cv.put(UserInfoTable.PROVINCE, uInfo.getProvince());
		cv.put(UserInfoTable.QUICKCNT, uInfo.getQuickcnt());
		cv.put(UserInfoTable.ROLEID, uInfo.getRoleid());
		cv.put(UserInfoTable.SCHOOLS, uInfo.getSchools());
		cv.put(UserInfoTable.SCHOOLSID, uInfo.getSchoolsid());
		cv.put(UserInfoTable.SEX, uInfo.getSex());
		cv.put(UserInfoTable.STATE, uInfo.getState());
		cv.put(UserInfoTable.SUPERVIP, uInfo.getSupervip());
		cv.put(UserInfoTable.TEACHLEVEL, uInfo.getTeachlevel());
		cv.put(UserInfoTable.TEL, uInfo.getTel());
		cv.put(UserInfoTable.THIRDNAME, uInfo.getThirdname());
		cv.put(UserInfoTable.TOKENID, uInfo.getTokenid());

		cv.put(UserInfoTable.USERID, uInfo.getUserid());

		cv.put(UserInfoTable.WELEARNID, uInfo.getWelearnid());
		cv.put(UserInfoTable.TEACHMAJOR, uInfo.getTeachmajor());
		cv.put(UserInfoTable.MAJOR, uInfo.getMajor());
		cv.put(UserInfoTable.NEWUSER, uInfo.getNewuser());
		cv.put(UserInfoTable.HWRATE, uInfo.getHwrate());
		cv.put(UserInfoTable.QSRATE, uInfo.getQsrate());
		cv.put(UserInfoTable.FDFS_AVATAR, uInfo.getFdfs_avatar());
		cv.put(UserInfoTable.FDFS_GROUPPHOTO, uInfo.getFdfs_groupphoto());
		cv.put(UserInfoTable.REMIND, uInfo.getRemind());
		
		cv.put(UserInfoTable.VIP_TYPE, uInfo.getVip_type());
		cv.put(UserInfoTable.IS_EXPIRED, uInfo.getVip_is_expired());
		cv.put(UserInfoTable.VIP_CONTENT, uInfo.getVip_content());
		cv.put(UserInfoTable.VIP_LEFT_TIME, uInfo.getVip_left_time());
		cv.put(UserInfoTable.TABBARSWITCH, uInfo.getTabbarswitch());
		cv.put(UserInfoTable.SIXTEACHER, uInfo.getSixteacher());

		if (isUpdate) {
			return updatetUserInfo(uInfo, cv);
		} else {
			return welearnDataBase.insert(UserInfoTable.TABLE_NAME, null, cv);
		}
	}

	/**
	 * 更新vip信息
	 * @param uInfo
	 * @param cv
     * @return
     */
	public void  updateSupervip(int userid,int supervip){
		String sql= "UPDATE " + UserInfoTable.TABLE_NAME + " SET " + UserInfoTable.SUPERVIP + " = ? WHERE "
				+ UserInfoTable.USERID + " = ?";
		welearnDataBase.beginTransaction();
		welearnDataBase.execSQL(sql,new String[]{String.valueOf(supervip),String.valueOf(userid)});
		welearnDataBase.setTransactionSuccessful();
		welearnDataBase.endTransaction();

	}

	public int updatetUserInfo(UserInfoModel uInfo, ContentValues cv) {
		if (null == uInfo || null == cv) {
			return -1;
		}

		return welearnDataBase.update(UserInfoTable.TABLE_NAME, cv, UserInfoTable.USERID + " = ?",
				new String[] { String.valueOf(uInfo.getUserid()) });
	}

	public int deleteCurrentUserInfo() {
		return welearnDataBase.delete(UserInfoTable.TABLE_NAME, null, null);
	}

	// ----- UserInfoTable End -----

	// ----- TableContactInfo Start -----

	public UserInfoModel queryContactInfoById(int uid) {
		Cursor c = welearnDataBase.query(ContactsInfoTable.TABLE_NAME, null, ContactsInfoTable.USERID + " = ?",
				new String[] { String.valueOf(uid) }, null, null, null);
		if (null == c) {
			return null;
		} else {
			if (c.moveToNext()) {
				UserInfoModel u = new UserInfoModel();
				u.setAdoptcnt(c.getInt(c.getColumnIndex(ContactsInfoTable.ADOPTCNT)));
				u.setAge(c.getInt(c.getColumnIndex(ContactsInfoTable.AGE)));
				u.setAllowgrab(c.getInt(c.getColumnIndex(ContactsInfoTable.ALLOWGRAB)));
				u.setAmountamt(c.getString(c.getColumnIndex(ContactsInfoTable.AMOUNTAMT)));
				u.setArbcnt(c.getInt(c.getColumnIndex(ContactsInfoTable.ARBCNT)));
				u.setAvatar_100(c.getString(c.getColumnIndex(ContactsInfoTable.AVATAR_100)));
				u.setAvatar_40(c.getString(c.getColumnIndex(ContactsInfoTable.AVATAR_40)));
				u.setCity(c.getString(c.getColumnIndex(ContactsInfoTable.CITY)));
				u.setCountamt(c.getInt(c.getColumnIndex(ContactsInfoTable.COUNTAMT)));
				u.setCredit(c.getFloat(c.getColumnIndex(ContactsInfoTable.CREDIT)));
				u.setDreamuniv(c.getString(c.getColumnIndex(ContactsInfoTable.DREAMUNIV)));
				u.setDreamunivid(c.getString(c.getColumnIndex(ContactsInfoTable.DREAMUNIVID)));
				u.setEarngold(c.getFloat(c.getColumnIndex(ContactsInfoTable.EARNGOLD)));
				u.setEdulevel(c.getInt(c.getColumnIndex(ContactsInfoTable.EDULEVEL)));
				u.setEmail(c.getString(c.getColumnIndex(ContactsInfoTable.EMAIL)));
				u.setExpensesamt(c.getFloat(c.getColumnIndex(ContactsInfoTable.EXPENSESAMT)));
				u.setFromchan(c.getString(c.getColumnIndex(ContactsInfoTable.FROMCHAN)));
				u.setGold(c.getFloat(c.getColumnIndex(ContactsInfoTable.GOLD)));
				u.setGrade(c.getString(c.getColumnIndex(ContactsInfoTable.GRADE)));
				u.setGradeid(c.getInt(c.getColumnIndex(ContactsInfoTable.GRADEID)));
				u.setGroupphoto(c.getString(c.getColumnIndex(ContactsInfoTable.GROUPPHOTO)));
				u.setHomeworkcnt(c.getInt(c.getColumnIndex(ContactsInfoTable.HOMEWORKCNT)));
				u.setInfostate(c.getInt(c.getColumnIndex(ContactsInfoTable.INFOSTATE)));
				u.setName(c.getString(c.getColumnIndex(ContactsInfoTable.NAME)));
				u.setNamepinyin(c.getString(c.getColumnIndex(ContactsInfoTable.NAMEPINYIN)));
				u.setProvince(c.getString(c.getColumnIndex(ContactsInfoTable.PROVINCE)));
				u.setQuickcnt(c.getInt(c.getColumnIndex(ContactsInfoTable.QUICKCNT)));
				u.setRoleid(c.getInt(c.getColumnIndex(ContactsInfoTable.ROLEID)));
				u.setSchools(c.getString(c.getColumnIndex(ContactsInfoTable.SCHOOLS)));
				u.setSchoolsid(c.getInt(c.getColumnIndex(ContactsInfoTable.SCHOOLSID)));
				u.setSex(c.getInt(c.getColumnIndex(ContactsInfoTable.SEX)));
				u.setState(c.getInt(c.getColumnIndex(ContactsInfoTable.STATE)));
				u.setSupervip(c.getInt(c.getColumnIndex(ContactsInfoTable.SUPERVIP)));
				u.setTeachlevel(c.getInt(c.getColumnIndex(ContactsInfoTable.TEACHLEVEL)));
				u.setTel(c.getString(c.getColumnIndex(ContactsInfoTable.TEL)));
				u.setThirdname(c.getString(c.getColumnIndex(ContactsInfoTable.THIRDNAME)));
				u.setTokenid(c.getString(c.getColumnIndex(ContactsInfoTable.TOKENID)));
				u.setUserid(c.getInt(c.getColumnIndex(ContactsInfoTable.USERID)));
				u.setWelearnid(c.getInt(c.getColumnIndex(ContactsInfoTable.WELEARNID)));
				u.setTeachmajor(c.getString(c.getColumnIndex(ContactsInfoTable.TEACHMAJOR)));
				u.setMajor(c.getString(c.getColumnIndex(ContactsInfoTable.MAJOR)));
				u.setRelation(c.getInt(c.getColumnIndex(ContactsInfoTable.RELATION)));
				u.setNewuser(c.getInt(c.getColumnIndex(ContactsInfoTable.NEWUSER)));
				u.setContact_subject(c.getString(c.getColumnIndex(ContactsInfoTable.CONTACT_SUBJECT)));
				c.close();
				return u;
			} else {
				c.close();
			}
		}
		return null;
	}

	public ArrayList<UserInfoModel> queryAllContactInfo() {
		ArrayList<UserInfoModel> list = new ArrayList<UserInfoModel>();
		Cursor c = welearnDataBase.query(ContactsInfoTable.TABLE_NAME, null, null, null, null, null, null);
		if (null == c) {
			return list;
		} else {
			while (c.moveToNext()) {
				UserInfoModel u = new UserInfoModel();
				u.setAdoptcnt(c.getInt(c.getColumnIndex(ContactsInfoTable.ADOPTCNT)));
				u.setAge(c.getInt(c.getColumnIndex(ContactsInfoTable.AGE)));
				u.setAllowgrab(c.getInt(c.getColumnIndex(ContactsInfoTable.ALLOWGRAB)));
				u.setAmountamt(c.getString(c.getColumnIndex(ContactsInfoTable.AMOUNTAMT)));
				u.setArbcnt(c.getInt(c.getColumnIndex(ContactsInfoTable.ARBCNT)));
				u.setAvatar_100(c.getString(c.getColumnIndex(ContactsInfoTable.AVATAR_100)));
				u.setAvatar_40(c.getString(c.getColumnIndex(ContactsInfoTable.AVATAR_40)));
				u.setCity(c.getString(c.getColumnIndex(ContactsInfoTable.CITY)));
				u.setCountamt(c.getInt(c.getColumnIndex(ContactsInfoTable.COUNTAMT)));
				u.setCredit(c.getFloat(c.getColumnIndex(ContactsInfoTable.CREDIT)));
				u.setDreamuniv(c.getString(c.getColumnIndex(ContactsInfoTable.DREAMUNIV)));
				u.setDreamunivid(c.getString(c.getColumnIndex(ContactsInfoTable.DREAMUNIVID)));
				u.setEarngold(c.getFloat(c.getColumnIndex(ContactsInfoTable.EARNGOLD)));
				u.setEdulevel(c.getInt(c.getColumnIndex(ContactsInfoTable.EDULEVEL)));
				u.setEmail(c.getString(c.getColumnIndex(ContactsInfoTable.EMAIL)));
				u.setExpensesamt(c.getFloat(c.getColumnIndex(ContactsInfoTable.EXPENSESAMT)));
				u.setFromchan(c.getString(c.getColumnIndex(ContactsInfoTable.FROMCHAN)));
				u.setGold(c.getFloat(c.getColumnIndex(ContactsInfoTable.GOLD)));
				u.setGrade(c.getString(c.getColumnIndex(ContactsInfoTable.GRADE)));
				u.setGradeid(c.getInt(c.getColumnIndex(ContactsInfoTable.GRADEID)));
				u.setGroupphoto(c.getString(c.getColumnIndex(ContactsInfoTable.GROUPPHOTO)));
				u.setHomeworkcnt(c.getInt(c.getColumnIndex(ContactsInfoTable.HOMEWORKCNT)));
				u.setInfostate(c.getInt(c.getColumnIndex(ContactsInfoTable.INFOSTATE)));
				u.setName(c.getString(c.getColumnIndex(ContactsInfoTable.NAME)));
				u.setNamepinyin(c.getString(c.getColumnIndex(ContactsInfoTable.NAMEPINYIN)));
				u.setProvince(c.getString(c.getColumnIndex(ContactsInfoTable.PROVINCE)));
				u.setQuickcnt(c.getInt(c.getColumnIndex(ContactsInfoTable.QUICKCNT)));
				u.setRoleid(c.getInt(c.getColumnIndex(ContactsInfoTable.ROLEID)));
				u.setSchools(c.getString(c.getColumnIndex(ContactsInfoTable.SCHOOLS)));
				u.setSchoolsid(c.getInt(c.getColumnIndex(ContactsInfoTable.SCHOOLSID)));
				u.setSex(c.getInt(c.getColumnIndex(ContactsInfoTable.SEX)));
				u.setState(c.getInt(c.getColumnIndex(ContactsInfoTable.STATE)));
				u.setSupervip(c.getInt(c.getColumnIndex(ContactsInfoTable.SUPERVIP)));
				u.setTeachlevel(c.getInt(c.getColumnIndex(ContactsInfoTable.TEACHLEVEL)));
				u.setTel(c.getString(c.getColumnIndex(ContactsInfoTable.TEL)));
				u.setThirdname(c.getString(c.getColumnIndex(ContactsInfoTable.THIRDNAME)));
				u.setTokenid(c.getString(c.getColumnIndex(ContactsInfoTable.TOKENID)));
				u.setUserid(c.getInt(c.getColumnIndex(ContactsInfoTable.USERID)));
				u.setWelearnid(c.getInt(c.getColumnIndex(ContactsInfoTable.WELEARNID)));
				u.setTeachmajor(c.getString(c.getColumnIndex(ContactsInfoTable.TEACHMAJOR)));
				u.setMajor(c.getString(c.getColumnIndex(ContactsInfoTable.MAJOR)));
				u.setRelation(c.getInt(c.getColumnIndex(ContactsInfoTable.RELATION)));
				u.setNewuser(c.getInt(c.getColumnIndex(ContactsInfoTable.NEWUSER)));
				u.setContact_subject(c.getString(c.getColumnIndex(ContactsInfoTable.CONTACT_SUBJECT)));
				list.add(u);
			}
			c.close();
		}
		return list;
	}

	public boolean isContactExists(int uid) {
		Cursor c = welearnDataBase.query(ContactsInfoTable.TABLE_NAME, null, ContactsInfoTable.USERID + " = ?",
				new String[] { String.valueOf(uid) }, null, null, null);
		if (null == c) {
			return false;
		} else {
			int count = c.getCount();
			c.close();
			if (count > 0) {
				return true;
			}
		}
		return false;
	}

	public long insertOrUpdatetContactInfo(UserInfoModel uInfo) {
		if (null == uInfo) {
			return 0L;
		}

		boolean isUpdate = isContactExists(uInfo.getUserid());

		ContentValues cv = new ContentValues();
		cv.put(ContactsInfoTable.ADOPTCNT, uInfo.getAdoptcnt());
		cv.put(ContactsInfoTable.AGE, uInfo.getAge());
		cv.put(ContactsInfoTable.ALLOWGRAB, uInfo.getAllowgrab());
		cv.put(ContactsInfoTable.AMOUNTAMT, uInfo.getAmountamt());
		cv.put(ContactsInfoTable.ARBCNT, uInfo.getArbcnt());
		cv.put(ContactsInfoTable.AVATAR_100, uInfo.getAvatar_100());
		cv.put(ContactsInfoTable.AVATAR_40, uInfo.getAvatar_40());
		cv.put(ContactsInfoTable.CITY, uInfo.getCity());
		cv.put(ContactsInfoTable.COUNTAMT, uInfo.getCountamt());
		cv.put(ContactsInfoTable.CREDIT, uInfo.getCredit());
		cv.put(ContactsInfoTable.DREAMUNIV, uInfo.getDreamuniv());
		cv.put(ContactsInfoTable.DREAMUNIVID, uInfo.getDreamunivid());
		cv.put(ContactsInfoTable.EARNGOLD, uInfo.getEarngold());
		cv.put(ContactsInfoTable.EDULEVEL, uInfo.getEdulevel());
		cv.put(ContactsInfoTable.EMAIL, uInfo.getEmail());
		cv.put(ContactsInfoTable.EXPENSESAMT, uInfo.getExpensesamt());
		cv.put(ContactsInfoTable.FROMCHAN, uInfo.getFromchan());
		cv.put(ContactsInfoTable.GOLD, uInfo.getGold());
		cv.put(ContactsInfoTable.GRADE, uInfo.getGrade());
		cv.put(ContactsInfoTable.GRADEID, uInfo.getGradeid());
		cv.put(ContactsInfoTable.GROUPPHOTO, uInfo.getGroupphoto());
		cv.put(ContactsInfoTable.HOMEWORKCNT, uInfo.getHomeworkcnt());
		cv.put(ContactsInfoTable.INFOSTATE, uInfo.getInfostate());
		cv.put(ContactsInfoTable.NAME, uInfo.getName());
		cv.put(ContactsInfoTable.NAMEPINYIN, uInfo.getNamepinyin());
		cv.put(ContactsInfoTable.PROVINCE, uInfo.getProvince());
		cv.put(ContactsInfoTable.QUICKCNT, uInfo.getQuickcnt());
		cv.put(ContactsInfoTable.ROLEID, uInfo.getRoleid());
		cv.put(ContactsInfoTable.SCHOOLS, uInfo.getSchools());
		cv.put(ContactsInfoTable.SCHOOLSID, uInfo.getSchoolsid());
		cv.put(ContactsInfoTable.SEX, uInfo.getSex());
		cv.put(ContactsInfoTable.STATE, uInfo.getState());
		cv.put(ContactsInfoTable.SUPERVIP, uInfo.getSupervip());
		cv.put(ContactsInfoTable.TEACHLEVEL, uInfo.getTeachlevel());
		cv.put(ContactsInfoTable.TEL, uInfo.getTel());
		cv.put(ContactsInfoTable.THIRDNAME, uInfo.getThirdname());
		cv.put(ContactsInfoTable.TOKENID, uInfo.getTokenid());
		if (!isUpdate) {
			cv.put(ContactsInfoTable.USERID, uInfo.getUserid());
		}
		cv.put(ContactsInfoTable.WELEARNID, uInfo.getWelearnid());
		cv.put(ContactsInfoTable.TEACHMAJOR, uInfo.getTeachmajor());
		cv.put(ContactsInfoTable.MAJOR, uInfo.getMajor());
		cv.put(ContactsInfoTable.RELATION, uInfo.getRelation());
		cv.put(ContactsInfoTable.NEWUSER, uInfo.getNewuser());
		cv.put(ContactsInfoTable.CONTACT_SUBJECT, uInfo.getContact_subject());

		if (isUpdate) {
			return updatetContactInfo(uInfo, cv);
		} else {
			return welearnDataBase.insert(ContactsInfoTable.TABLE_NAME, null, cv);
		}
	}

	public int updatetContactInfo(UserInfoModel uInfo, ContentValues cv) {
		if (null == uInfo || null == cv) {
			return -1;
		}

		return welearnDataBase.update(ContactsInfoTable.TABLE_NAME, cv, ContactsInfoTable.USERID + " = ?",
				new String[] { String.valueOf(uInfo.getUserid()) });
	}

	public int deleteContactUserInfoByUserId(int uid) {
		return welearnDataBase.delete(ContactsInfoTable.TABLE_NAME, ContactsInfoTable.USERID + " = ?",
				new String[] { String.valueOf(uid) });
	}

	public int clearContactUserInfo() {
		return welearnDataBase.delete(ContactsInfoTable.TABLE_NAME, null, null);
	}
	// ----- TableContactInfo End -----

	// ----- 知识点 Start -----

	public static String getDeleteKnowledgeTableSql() {
		return "DROP TABLE IF EXISTS " + KnowledgeTable.TABLE_NAME;
	}

	public void insertKnowledge(List<CatalogModel> catalogModels) {
		ContentValues cv = null;
		for (CatalogModel catalogModel : catalogModels) {
			int groupid = catalogModel.getGroupid();
			String groupname = catalogModel.getGroupname();
			List<Subject> subjects = catalogModel.getSubjects();
			for (Subject subject : subjects) {
				int subjectid = subject.getSubjectid();
				String subjectname = subject.getSubjectname();
				List<Chapter> chapters = subject.getChapter();
				for (Chapter chapter : chapters) {
					int chapterid = chapter.getChapterid();
					String chaptername = chapter.getChaptername();
					List<Point> points = chapter.getPoint();
					for (Point point : points) {
						int id = point.getId();
						String name = point.getName();
						cv = new ContentValues();
						cv.put(KnowledgeTable.GROUPNAME, groupname);
						cv.put(KnowledgeTable.GROUPID, groupid);
						cv.put(KnowledgeTable.SUBJECTNAME, subjectname);
						cv.put(KnowledgeTable.SUBJECTID, subjectid);
						cv.put(KnowledgeTable.CHAPTERNAME, chaptername);
						cv.put(KnowledgeTable.CHAPTERID, chapterid);
						cv.put(KnowledgeTable.NAME, name);
						cv.put(KnowledgeTable.ID, id);
						welearnDataBase.insert(KnowledgeTable.TABLE_NAME, null, cv);
					}
				}
			}
		}

		// welearnDataBase.beginTransaction();
		// try {
		// long insert = welearnDataBase.insert(TableKnowledge.TABLE_NAME, null,
		// cv);
		// welearnDataBase.setTransactionSuccessful();
		// welearnDataBase.endTransaction();
		// return insert;
		// } catch (Exception e) {
		// return 0;
		// }
	}

	public ArrayList<CatalogModel> queryAllKonwledge() {
		ArrayList<CatalogModel> catalogModels = null;
		String sql4 = "SELECT * FROM " + KnowledgeTable.TABLE_NAME + " ORDER BY " + KnowledgeTable.ID;
		String sql3 = "SELECT * FROM (" + sql4 + ") ORDER BY " + KnowledgeTable.CHAPTERID;
		String sql2 = "SELECT * FROM (" + sql3 + ") ORDER BY " + KnowledgeTable.SUBJECTID;
		String sql = "SELECT * FROM (" + sql2 + ") ORDER BY " + KnowledgeTable.GROUPID;
		int groupid = -1;
		int subjectid = -1;
		int chapterid = -1;
		// int id = -1;
		CatalogModel catalogModel = new CatalogModel();
		Subject subject = catalogModel.new Subject();
		Chapter chapter = catalogModel.new Chapter();
		Point point = catalogModel.new Point();
		ArrayList<Subject> subjects = new ArrayList<Subject>();
		ArrayList<Chapter> chapters = new ArrayList<Chapter>();
		ArrayList<Point> points = new ArrayList<Point>();
		Cursor cursor = welearnDataBase.rawQuery(sql, null);
		if (cursor != null) {
			catalogModels = new ArrayList<CatalogModel>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				int queryGroupId = cursor.getInt(cursor.getColumnIndex(KnowledgeTable.GROUPID));
				String queryGroupName = cursor.getString(cursor.getColumnIndex(KnowledgeTable.GROUPNAME));

				int querySubjectId = cursor.getInt(cursor.getColumnIndex(KnowledgeTable.SUBJECTID));
				String querySubjectName = cursor.getString(cursor.getColumnIndex(KnowledgeTable.SUBJECTNAME));

				int queryChapterId = cursor.getInt(cursor.getColumnIndex(KnowledgeTable.CHAPTERID));
				String queryChapterName = cursor.getString(cursor.getColumnIndex(KnowledgeTable.CHAPTERNAME));

				int queryId = cursor.getInt(cursor.getColumnIndex(KnowledgeTable.ID));
				String queryName = cursor.getString(cursor.getColumnIndex(KnowledgeTable.NAME));

				if (queryGroupId != groupid) {
					groupid = queryGroupId;
					subjectid = -1;
					catalogModel = new CatalogModel();
					catalogModel.setGroupid(queryGroupId);
					catalogModel.setGroupname(queryGroupName);
					catalogModels.add(catalogModel);
					subjects = new ArrayList<Subject>();
					catalogModel.setSubjects(subjects);
				}
				if (querySubjectId != subjectid) {
					subjectid = querySubjectId;
					chapterid = -1;
					subject = catalogModel.new Subject();
					subject.setSubjectid(querySubjectId);
					subject.setSubjectname(querySubjectName);
					subjects.add(subject);
					chapters = new ArrayList<Chapter>();
					subject.setChapter(chapters);
				}
				if (queryChapterId != chapterid) {
					chapterid = queryChapterId;
					chapter = catalogModel.new Chapter();
					chapter.setChapterid(queryChapterId);
					chapter.setChaptername(queryChapterName);
					chapters.add(chapter);
					points = new ArrayList<Point>();
					chapter.setPoint(points);
				}
				if (queryId != -1) {
					point = catalogModel.new Point();
					point.setId(queryId);
					point.setName(queryName);
					points.add(point);
				}

			}
			cursor.close();
		}
		return catalogModels;

	}

	public List<String> queryKnowledgeByName(String name, String groupid, String subjectid) {
		name = "%" + name + "%";
		String innerSql = "SELECT * FROM " + KnowledgeTable.TABLE_NAME + " WHERE " + KnowledgeTable.NAME
				+ " LIKE ? AND " + KnowledgeTable.GROUPID + " = ? AND " + KnowledgeTable.SUBJECTID + " = ? ORDER BY "
				+ KnowledgeTable._ID;
		// LogUtils.i(TAG, innerSql);
		Cursor cursor = welearnDataBase.rawQuery(innerSql, new String[] { name, groupid, subjectid });
		if (cursor != null) {
			List<String> knowledges = new ArrayList<String>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				knowledges.add(cursor.getString(cursor.getColumnIndex(KnowledgeTable.NAME)));
			}
			cursor.close();
			return knowledges;
		}
		return null;
	}

	public List<String> queryKnowledgeByName(String name) {
		name = "%" + name + "%";
		String innerSql = "SELECT * FROM " + KnowledgeTable.TABLE_NAME + " WHERE " + KnowledgeTable.NAME
				+ " LIKE ? ORDER BY " + KnowledgeTable._ID;
		// LogUtils.i(TAG, innerSql);
		Cursor cursor = welearnDataBase.rawQuery(innerSql, new String[] { name });
		if (cursor != null) {
			List<String> knowledges = new ArrayList<String>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				knowledges.add(cursor.getString(cursor.getColumnIndex(KnowledgeTable.NAME)));
			}
			cursor.close();
			return knowledges;
		}
		return null;
	}

	public boolean isKnowledgeExis() {
		String innerSql = "SELECT * FROM " + KnowledgeTable.TABLE_NAME;
		// LogUtils.i(TAG, innerSql);
		boolean flag = false;
		Cursor cursor = welearnDataBase.rawQuery(innerSql, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				flag = true;
			}
			cursor.close();
		}
		return flag;
	}
	// ----- 知识点 End -----

	// 删除subject表和grade
	public void delSubjectAndGradeTable() {
		try {
			welearnDataBase.execSQL("delete from sub_list");
			welearnDataBase.execSQL("delete from grade_list");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
