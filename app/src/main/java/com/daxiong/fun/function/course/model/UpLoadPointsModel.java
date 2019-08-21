package com.daxiong.fun.function.course.model;

import java.io.Serializable;
import java.util.ArrayList;

public class UpLoadPointsModel  implements Serializable{
	/** 页ID */
	private int pageid;
	
	/** 学号 */
	private int studentid;
	
	/**课程打点 or 追问打点           1 课程 0 追问*/
	private int belongcourse;
	
	private ArrayList<CoursePoint> point;
	
	public int getPageid() {
		return pageid;
	}
	public void setPageid(int pageid) {
		this.pageid = pageid;
	}
	public int getStudentid() {
		return studentid;
	}
	public void setStudentid(int studentid) {
		this.studentid = studentid;
	}
	public int getBelongcourse() {
		return belongcourse;
	}
	public void setBelongcourse(int belongcourse) {
		this.belongcourse = belongcourse;
	}

	public ArrayList<CoursePoint> getPoint() {
		return point;
	}
	public void setPoint(ArrayList<CoursePoint> point) {
		this.point = point;
	}
	@Override
	public String toString() {
		return "UpLoadPointsModel [pageid=" + pageid + ", studentid=" + studentid + ", belongcourse=" + belongcourse
				+ ", point=" + point + "]";
	}

	
}
