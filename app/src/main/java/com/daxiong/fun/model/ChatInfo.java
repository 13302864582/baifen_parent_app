package com.daxiong.fun.model;

import com.daxiong.fun.function.homework.model.MsgData;

import java.io.Serializable;

public class ChatInfo implements Serializable {

	private static final long serialVersionUID = 2530437496531842678L;
	private boolean isReaded;
	private int id;
	private int contenttype;
	private String msgcontent;
	private double timestamp;
	private long localTimestamp;
	private int audiotime;
	private String path;
	private int fromuser;
	private int fromroleid;
	private int type;// 1:发送 2:接收
	private int unReadCount;// 未读消息条数
	private UserInfoModel user;
	private boolean isSendFail;// 是否发送成功
	private String noticetype;//"1111" #四位数，从左至右：是否显示红点，是否震动，是否语音，是否取系统。红点的获取是独立的，是否取系统的优先级最高
	private MsgData data;


	public String getNoticetype() {
		return noticetype;
	}

	public void setNoticetype(String noticetype) {
		this.noticetype = noticetype;
	}

	public int getUnReadCount() {
		return unReadCount;
	}

	public void setUnReadCount(int unReadCount) {
		this.unReadCount = unReadCount;
	}

	public MsgData getData() {
		return data;
	}

	public void setData(MsgData data) {
		this.data = data;
	}

	public int getFromroleid() {
		return fromroleid;
	}

	public boolean isReaded() {
		return isReaded;
	}

	public void setReaded(boolean isReaded) {
		this.isReaded = isReaded;
	}

	public void setFromroleid(int fromroleid) {
		this.fromroleid = fromroleid;
	}


	public long getLocalTimestamp() {
		return localTimestamp;
	}

	public void setLocalTimestamp(long localTimestamp) {
		this.localTimestamp = localTimestamp;
	}

	public boolean isSendFail() {
		return isSendFail;
	}

	public void setSendFail(boolean isSendFail) {
		this.isSendFail = isSendFail;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAudiotime() {
		return audiotime;
	}

	public void setAudiotime(int audiotime) {
		this.audiotime = audiotime;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public UserInfoModel getUser() {
		return user;
	}

	public void setUser(UserInfoModel user) {
		this.user = user;
	}

	public int getContenttype() {
		return contenttype;
	}

	public void setContenttype(int contenttype) {
		this.contenttype = contenttype;
	}

	public String getMsgcontent() {
		return msgcontent;
	}

	public void setMsgcontent(String msgcontent) {
		this.msgcontent = msgcontent;
	}

	public double getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(double timestamp) {
		this.timestamp = timestamp;
	}

	public int getFromuser() {
		return fromuser;
	}

	public void setFromuser(int fromuser) {
		this.fromuser = fromuser;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
//	public String getJumpUrl() {
//		return jumpUrl;
//	}
//
//	public void setJumpUrl(String jumpUrl) {
//		this.jumpUrl = jumpUrl;
//	}
//
//	public int getQuestion_id() {
//		return question_id;
//	}
//
//	public void setQuestion_id(int question_id) {
//		this.question_id = question_id;
//	}
//
//	public int getTarget_user_id() {
//		return target_user_id;
//	}
//
//	public void setTarget_user_id(int target_user_id) {
//		this.target_user_id = target_user_id;
//	}
//
//	public int getAction() {
//		return action;
//	}
//
//	public void setAction(int action) {
//		this.action = action;
//	}
//
//	public int getTarget_role_id() {
//		return target_role_id;
//	}
//
//	public void setTarget_role_id(int target_role_id) {
//		this.target_role_id = target_role_id;
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + audiotime;
		result = prime * result + contenttype;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + fromroleid;
		result = prime * result + fromuser;
		result = prime * result + id;
		result = prime * result + (isReaded ? 1231 : 1237);
		result = prime * result + (isSendFail ? 1231 : 1237);
		result = prime * result + (int) (localTimestamp ^ (localTimestamp >>> 32));
		result = prime * result + ((msgcontent == null) ? 0 : msgcontent.hashCode());
		result = prime * result + ((noticetype == null) ? 0 : noticetype.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		long temp;
		temp = Double.doubleToLongBits(timestamp);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + type;
		result = prime * result + unReadCount;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChatInfo other = (ChatInfo) obj;
		if (audiotime != other.audiotime)
			return false;
		if (contenttype != other.contenttype)
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (fromroleid != other.fromroleid)
			return false;
		if (fromuser != other.fromuser)
			return false;
		if (id != other.id)
			return false;
		if (isReaded != other.isReaded)
			return false;
		if (isSendFail != other.isSendFail)
			return false;
		if (localTimestamp != other.localTimestamp)
			return false;
		if (msgcontent == null) {
			if (other.msgcontent != null)
				return false;
		} else if (!msgcontent.equals(other.msgcontent))
			return false;
		if (noticetype == null) {
			if (other.noticetype != null)
				return false;
		} else if (!noticetype.equals(other.noticetype))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (Double.doubleToLongBits(timestamp) != Double.doubleToLongBits(other.timestamp))
			return false;
		if (type != other.type)
			return false;
		if (unReadCount != other.unReadCount)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ChatInfo [isReaded=" + isReaded + ", id=" + id + ", contenttype=" + contenttype + ", msgcontent="
				+ msgcontent + ", timestamp=" + timestamp + ", localTimestamp=" + localTimestamp + ", audiotime="
				+ audiotime + ", path=" + path + ", fromuser=" + fromuser + ", fromroleid=" + fromroleid + ", type="
				+ type + ", unReadCount=" + unReadCount + ", user=" + user + ", isSendFail=" + isSendFail
				+ ", noticetype=" + noticetype + ", data=" + data + "]";
	}

}
