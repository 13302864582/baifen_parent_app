package com.daxiong.fun.model;

import java.util.List;

public class PayAnswerGoldGson {
	private List<SubjectId> subjects;

	public List<SubjectId> getSubjects() {
		return subjects;
	}
	public void setSubjects(List<SubjectId> subjects) {
		this.subjects = subjects;
	}
	@Override
	public String toString() {
		return "PayAnswerGoldGson [subjects=" + subjects + "]";
	}

}
