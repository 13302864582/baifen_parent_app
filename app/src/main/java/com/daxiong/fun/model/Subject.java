package com.daxiong.fun.model;

import java.util.ArrayList;
import java.util.List;

public class Subject {
	private int gradegroup;
	private List<String> subject = new ArrayList<String>();
	public int getGradegroup() {
		return gradegroup;
	}
	public void setGradegroup(int gradegroup) {
		this.gradegroup = gradegroup;
	}
	public List<String> getSubject() {
		return subject;
	}
	public void setSubject(List<String> subject) {
		this.subject = subject;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + gradegroup;
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
		Subject other = (Subject) obj;
		if (gradegroup != other.gradegroup)
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
		return "Subject [gradegroup=" + gradegroup + ", subject=" + subject
				+ "]";
	}
}
