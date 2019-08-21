package com.daxiong.fun.model;

import java.util.LinkedHashSet;

public class AnswerListModel extends BaseModel {

	private LinkedHashSet<AnswerListItemGsonModel> mAnswerListItemGsons;

	public static final int ANSWER_LIST_MODEL_OBSERVER = 1000;

	public LinkedHashSet<AnswerListItemGsonModel> getAnswerListItemGsons() {
		return mAnswerListItemGsons;
	}

	public void setAnswerListItemGsons(
			LinkedHashSet<AnswerListItemGsonModel> mAnswerListItemGsons) {
		this.mAnswerListItemGsons = mAnswerListItemGsons;
		notifyChanged(ANSWER_LIST_MODEL_OBSERVER);
	}
}
