package com.app.ipinle.model;


import java.io.Serializable;

import com.app.ipinle.base.BaseModel;

public class OrderDetail extends BaseModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 360735680224438735L;
	
	private String orderId;	
	private String orderTime;
	private String orderType;
	private String orderStart;
	private String orderTerminal;
	private String user;
	private String carId;
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderStart() {
		return orderStart;
	}

	public void setOrderStart(String orderStart) {
		this.orderStart = orderStart;
	}

	public String getOrderTerminal() {
		return orderTerminal;
	}

	public void setOrderTerminal(String orderTerminal) {
		this.orderTerminal = orderTerminal;
	}

	public static OrderDetail getOrder_detail() {
		return order_detail;
	}

	public static void setOrder_detail(OrderDetail order_detail) {
		OrderDetail.order_detail = order_detail;
	}

	
	
	
	static private OrderDetail order_detail = null;
	static public OrderDetail getInstance(){
		if(OrderDetail.order_detail == null)
			OrderDetail.order_detail = new OrderDetail();
		return OrderDetail.order_detail;
	}
	
	public OrderDetail(){ }

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}