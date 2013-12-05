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

	String order_id = "";
	ListView list;
	OrderList orderList;
	ArrayAdapter<String> arrayAddapter;
	String temp_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_order_list);
		/*
		TextView order_nub = (TextView)findViewById(R.id.order_nub);
		TextView order_time = (TextView)findViewById(R.id.order_time);
		TextView order_type = (TextView)findViewById(R.id.order_type);
		TextView order_start = (TextView)findViewById(R.id.order_start);
		TextView order_terminal = (TextView)findViewById(R.id.order_terminal);*/
		list = (ListView)findViewById(R.id.ilistview);
		
		
		String type = getIntent().getStringExtra("type");
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		orderList = (OrderList)bundle.getSerializable("order_list_key");
		//String[] arr = {"sdf","asdf","asdf"};
		arrayAddapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_1, orderList.getOrderList());
		
		list.setAdapter(arrayAddapter);
		OnItemClickListener listener;
		listener = new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				order_id = orderList.getOrderList().get(position);
				//下一句测试后删!!!!!!!!!!!!
				temp_id = order_id;
				doTaskAskDetail();
				
			}
		};
		list.setOnItemClickListener(listener);	
		
	}
	
	public void doTaskAskDetail(){
		//查找数据库，根据提供的order_id查找该订单的ID，时间，类型，起始地点和目的地，存入OrderDetail这个实体类中
	   	 
 		HashMap<String, String> urlParams = new HashMap<String,String>();
 		
 		urlParams.put("sid", AppUser.getUser().getSid());
 		urlParams.put("id", AppUser.getUser().getId());
 		urlParams.put("order_id", order_id);

 		try{
 			//this.showLoadBar();
 			list.setOnItemClickListener(null);
 			
 			/////////////////////////////////////////////////////////////////////
 			//测试代码
 			final Intent show_detail = new Intent(this,OrderDetailUi.class);
 			Bundle bundle = new Bundle();
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setOrderId(temp_id);
			orderDetail.setOrderStart("asdf2");
			orderDetail.setOrderTerminal("asdf3");
			orderDetail.setOrderTime("asdf4");
			orderDetail.setOrderType("asdf5");
			
			bundle.putSerializable("order_detail_key", orderDetail);
			show_detail.putExtra("type", "order_detail");  
			show_detail.putExtras(bundle);
			startActivity(show_detail);
			return;
 			
 			
 			///////////////////////////////////////////////////////////////////////
			//下一行应被恢复，为正常代码
 			//this.doTaskAsync(C.task.ask_order_detail, C.api.ask_order_detail, urlParams);
 		}catch(Exception e){
 			e.printStackTrace();
 		}
 		
     }
	
	@Override
	public void onTaskComplete(int taskId, BaseMessage message){
		final Intent show_detail = new Intent(this,OrderListUi.class);
		Bundle bundle = new Bundle();
		OrderDetail orderDetail = null;
		if(taskId == C.task.ask_order_detail){
			try{
				orderDetail = (OrderDetail)message.getResult("OrderDetail");
				if(orderDetail.getOrderId()==null){
					Toast.makeText(this, "订单不存在", Toast.LENGTH_LONG).show();
					return;
				}
				bundle.putSerializable("order_detail_key", orderDetail);
				show_detail.putExtra("type", "order_detail");  
				show_detail.putExtras(bundle);
					
				startActivity(show_detail);
			}catch(Exception e){
 				e.printStackTrace();
				this.toast(e.getMessage());
 			}finally{
 				//this.hideLoadBar();
 				OnItemClickListener listener;
 				listener = new OnItemClickListener(){
 					@Override
 					public void onItemClick(AdapterView<?> parent, View view, int position, long id){
 						order_id = orderList.getOrderList().get(position);
 						
 					}
 				};
 				list.setOnItemClickListener(listener);
 			}
			
			
		}
	}
	
	
}
