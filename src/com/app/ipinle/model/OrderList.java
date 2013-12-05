package com.app.ipinle.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.app.ipinle.base.BaseModel;

public class OrderList extends BaseModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7613177416820742706L;
	private List<String> orderList;
	//private List<String> orderList = new ArrayList();

	public List<String> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<String> orderList) {
		this.orderList = orderList;
	}
	
	static private OrderList order_list = null;
	static public OrderList getInstance(){
		if(OrderList.order_list == null)
			OrderList.order_list = new OrderList();
		return OrderList.order_list;
	}
	
	public OrderList(){ }

}
