package com.daxiong.fun.model;


/**
 * 问题model
 * @author parsonswang
 *
 */
public class QuestionModel extends BaseModel {

	private QuestionModelGson mQuestionModelGson;
	
	public static final int QUESTION_MODEL_OBSERVER = 1000;
	
	public QuestionModelGson getQuestionModelGson() {
		return mQuestionModelGson;
	}


	public void setQuestionModelGson(QuestionModelGson questionModelGson) {
		this.mQuestionModelGson = questionModelGson;
		notifyChanged(QUESTION_MODEL_OBSERVER);
	}

}
