package com.daxiong.fun.model;

import java.util.ArrayList;

public class SignInRuleModel {
	public class SignInLog {
		public int seqid;
		public int id;
		public double gotgold;
		public String datatime;
		
		public int getSeqid() {
			return seqid;
		}
		public void setSeqid(int seqid) {
			this.seqid = seqid;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public double getGotgold() {
			return gotgold;
		}
		public void setGotgold(double gotgold) {
			this.gotgold = gotgold;
		}
		public String getDatatime() {
			return datatime;
		}
		public void setDatatime(String datatime) {
			this.datatime = datatime;
		}
		@Override
		public String toString() {
			return "SignInLog [seqid=" + seqid + ", id=" + id + ", gotgold=" + gotgold + ", datatime=" + datatime + "]";
		}
		
	}

	private int today_signed;// 0是未签,1是已签
	private double randommin;
	private double randommax;
	private double startgold;
	private int type;
	private int id;
	private double unit;
	private int cycle;
	private String banner1;
	private String banner2;
	private ArrayList<SignInLog> signInLogs;



	@Override
	public String toString() {
		return "SignInRuleModel [today_signed=" + today_signed + ", randommin=" + randommin + ", randommax="
				+ randommax + ", startgold=" + startgold + ", type=" + type + ", id=" + id + ", unit=" + unit
				+ ", cycle=" + cycle + ", banner1=" + banner1 + ", banner2=" + banner2 + ", signInLogs=" + signInLogs
				+ "]";
	}

	public ArrayList<SignInLog> getSignInLogs() {
		return signInLogs;
	}

	public void setSignInLogs(ArrayList<SignInLog> signInLogs) {
		this.signInLogs = signInLogs;
	}

	public int getToday_signed() {
		return today_signed;
	}

	public void setToday_signed(int today_signed) {
		this.today_signed = today_signed;
	}


	public double getRandommin() {
		return randommin;
	}

	public void setRandommin(double randommin) {
		this.randommin = randommin;
	}

	public double getRandommax() {
		return randommax;
	}

	public void setRandommax(double randommax) {
		this.randommax = randommax;
	}

	public double getStartgold() {
		return startgold;
	}

	public void setStartgold(double startgold) {
		this.startgold = startgold;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getUnit() {
		return unit;
	}

	public void setUnit(double unit) {
		this.unit = unit;
	}

	public int getCycle() {
		return cycle;
	}

	public void setCycle(int cycle) {
		this.cycle = cycle;
	}

	public String getBanner1() {
		return banner1;
	}

	public void setBanner1(String banner1) {
		this.banner1 = banner1;
	}

	public String getBanner2() {
		return banner2;
	}

	public void setBanner2(String banner2) {
		this.banner2 = banner2;
	}


}
