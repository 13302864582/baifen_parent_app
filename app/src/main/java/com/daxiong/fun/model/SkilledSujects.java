package com.daxiong.fun.model;

public class SkilledSujects {

	private String subject;
	private String gradegroup;
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getGradegroup() {
		return gradegroup;
	}
	public void setGradegroup(String gradegroup) {
		this.gradegroup = gradegroup;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((gradegroup == null) ? 0 : gradegroup.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
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
		SkilledSujects other = (SkilledSujects) obj;
		if (gradegroup == null) {
			if (other.gradegroup != null)
				return false;
		} else if (!gradegroup.equals(other.gradegroup))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "SkilledSujects [subject=" + subject + ", gradegroup="
				+ gradegroup + "]";
	}
	
}
