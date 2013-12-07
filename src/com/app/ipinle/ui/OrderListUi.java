package com.app.ipinle.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ipinle.base.BaseMessage;
import com.app.ipinle.base.BaseUi;
import com.app.ipinle.base.C;
import com.app.ipinle.model.OrderDetail;
import com.app.ipinle.model.OrderList;
import com.app.ipinle.util.AppUser;
import com.sse.eping.R;

public class OrderListUi extends BaseUi {

	private ArrayList<String> orderIdList = null;
	String order_id = "";
	ListView list;
	ArrayList<OrderDetail> orderList;
	ArrayAdapter<String> arrayAddapter;
	String temp_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_order_list);
		/*
		 * TextView order_nub = (TextView)findViewById(R.id.order_nub); TextView
		 * order_time = (TextView)findViewById(R.id.order_time); TextView
		 * order_type = (TextView)findViewById(R.id.order_type); TextView
		 * order_start = (TextView)findViewById(R.id.order_start); TextView
		 * order_terminal = (TextView)findViewById(R.id.order_terminal);
		 */
		list = (ListView) findViewById(R.id.ilistview);

		String type = getIntent().getStringExtra("type");
		doTaskAskHistory();

	}
	
	public void setPageContent(final ArrayList<String> orderList){;
		// String[] arr = {"sdf","asdf","asdf"};
		arrayAddapter = (ArrayAdapter<String>) new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, orderList);

		list.setAdapter(arrayAddapter);

		OnItemClickListener listener;
		listener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i("click", "position:"+position+"id:"+id);
				showDetail(position);
			}
		};
		list.setOnItemClickListener(listener);
	}
	
	public void showDetail(int position){
		final Intent show_detail = new Intent(this,OrderDetailUi.class);
		show_detail.putExtra("order_id", this.orderList.get(position).getOrderId());
		startActivity(show_detail);
	}

	@SuppressWarnings("unchecked")
	public void doTaskAskHistory() {
		// 查找数据库，要求找出所有历史订单，返回orderlist类，里面应该有orderid的一个list，内容为所有order的ID
		// 与上一个函数的C.TASK.和C.API.后内容不同

		HashMap<String, String> urlParams = new HashMap<String, String>();
		try {
			this.showLoadBar();
			this.doTaskAsync(C.task.ask_history_order, C.api.ask_history_order,urlParams);
			// this.hideLoadBar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
   * 
   */
	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		// super.onTaskComplete(taskId, message);
		// ///////////////////////////////////////////////////////////////////////////
		Log.i("orderListUI", "task complete.");
		if (taskId == C.task.ask_history_order) {
			try {
				orderList = (ArrayList<OrderDetail>) message.getResultList("OrderDetail");
				if (orderList != null && orderList.size()>0) {
					Toast.makeText(OrderListUi.this, "查找历史成功",
							Toast.LENGTH_SHORT).show();
					
					// get order id list
					orderIdList = new ArrayList<String>();
					for(int i=0;i<orderList.size();i++){
						orderIdList.add(orderList.get(i).getOrderId()+" "+orderList.get(i).getOrderTime()+" "+orderList.get(i).getOrderType());
					}
					// show the content
					this.setPageContent(orderIdList);
					this.hideLoadBar();
				} else {
					Toast.makeText(OrderListUi.this, "查找历史失败，请稍候重试",
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
				this.toast("查找失败，请稍候重试" + e.getMessage());
			}

		}
		this.hideLoadBar();
	}

}
