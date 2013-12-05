package com.app.ipinle.model;

import com.app.ipinle.base.BaseModel;

public class SubmitFeedback extends BaseModel{

	public final static String FEEDBACK = "feedback";
	public final static String ORDER_ID = "order_id";
	public final static String CAR_ID = "car_id";
	
	private String feedback;
	private String order_id;
	private String car_id;
	
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

	public String getCar_id() {
		return car_id;
	}

	public void setCar_id(String car_id) {
		this.car_id = car_id;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
}
