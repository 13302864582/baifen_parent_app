package com.daxiong.fun.model;

public class AnswerSound {

	public int getSubtype() {
		return subtype;
	}
	public void setSubtype(int subtype) {
		this.subtype = subtype;
	}
	private String datatime;
	private String textcontent;
	private String q_sndurl;
	private int role;
	private int snd_id;
	private String coordinate;
	private int type;
	private int subtype;
	public String getDatatime() {
		return datatime;
	}
	public void setDatatime(String datatime) {
		this.datatime = datatime;
	}
	public String getTextcontent() {
		return textcontent;
	}
	public void setTextcontent(String textcontent) {
		this.textcontent = textcontent;
	}
	public String getQ_sndurl() {
		return q_sndurl;
	}
	public void setQ_sndurl(String q_sndurl) {
		this.q_sndurl = q_sndurl;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public int getSnd_id() {
		return snd_id;
	}
	public void setSnd_id(int snd_id) {
		this.snd_id = snd_id;
	}
	public String getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "AnswerSound [datatime=" + datatime + ", textcontent="
				+ textcontent + ", q_sndurl=" + q_sndurl + ", role=" + role
				+ ", snd_id=" + snd_id + ", coordinate=" + coordinate
				+ ", type=" + type + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((coordinate == null) ? 0 : coordinate.hashCode());
		result = prime * result
				+ ((datatime == null) ? 0 : datatime.hashCode());
		result = prime * result
				+ ((q_sndurl == null) ? 0 : q_sndurl.hashCode());
		result = prime * result + role;
		result = prime * result + snd_id;
		result = prime * result
				+ ((textcontent == null) ? 0 : textcontent.hashCode());
		result = prime * result + type;
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
		AnswerSound other = (AnswerSound) obj;
		if (coordinate == null) {
			if (other.coordinate != null)
				return false;
		} else if (!coordinate.equals(other.coordinate))
			return false;
		if (datatime == null) {
			if (other.datatime != null)
				return false;
		} else if (!datatime.equals(other.datatime))
			return false;
		if (q_sndurl == null) {
			if (other.q_sndurl != null)
				return false;
		} else if (!q_sndurl.equals(other.q_sndurl))
			return false;
		if (role != other.role)
			return false;
		if (snd_id != other.snd_id)
			return false;
		if (textcontent == null) {
			if (other.textcontent != null)
				return false;
		} else if (!textcontent.equals(other.textcontent))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	
}
