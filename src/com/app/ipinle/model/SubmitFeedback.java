package com.app.ipinle.model;

import com.app.ipinle.base.BaseModel;

public class SubmitFeedback extends BaseModel{

	public final static String FEEDBACK = "feedback";
	
	
	private String feedback;
	
	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	static private SubmitFeedback sub_feedback = null;
	
	static public SubmitFeedback getInstance(){
		if(SubmitFeedback.sub_feedback == null)
			SubmitFeedback.sub_feedback = new SubmitFeedback();
		return SubmitFeedback.sub_feedback;
	}
	
	public SubmitFeedback(){ }
}
