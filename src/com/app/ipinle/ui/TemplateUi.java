package com.app.ipinle.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.Toast;

import com.app.ipinle.base.BaseMessage;
import com.app.ipinle.base.BaseUi;
import com.app.ipinle.base.C;
import com.app.ipinle.model.OrderList;
import com.app.ipinle.util.AppUser;
import com.sse.eping.R;

public class TemplateUi extends BaseUi {

	public static final String TAB_BUS_ROUTE = "busroute";
	public static final String TAB_CARPOOL = "carpool";
	public static final String TAB_NAVI = "navigation";
	private RadioGroup group;
	private TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.ui_template);

		initTab();
	}

	public void initTab() {
		tabHost = (TabHost) findViewById(R.id.myTabHost);
		if (tabHost != null) {
			// 如果没有继承TabActivity时，通过该种方法加载启动tabHost
			tabHost.setup(this.getLocalActivityManager());

			tabHost.addTab(tabHost.newTabSpec(TAB_BUS_ROUTE)
					.setIndicator(TAB_BUS_ROUTE).setIndicator("VIEW")
					.setContent(new Intent(this, ShowBusRouteUi.class)));
			tabHost.addTab(tabHost.newTabSpec(TAB_CARPOOL)
					.setIndicator(TAB_CARPOOL)
					.setContent(new Intent(this, CarPoolUi.class)));
			tabHost.addTab(tabHost.newTabSpec(TAB_NAVI).setIndicator(TAB_NAVI)
					.setContent(new Intent(this, NavigationUi.class)));
			group = (RadioGroup) findViewById(R.id.main_radio);

			if (group != null) {
				group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.radio_button0:
							tabHost.setCurrentTabByTag(TAB_BUS_ROUTE);
							//forward(ShowBusRouteUi.class);
							break;
						case R.id.radio_button1:
							tabHost.setCurrentTabByTag(TAB_CARPOOL);
							// forward(CarPoolUi.class);
							break;
						case R.id.radio_button2:
							tabHost.setCurrentTabByTag(TAB_NAVI);
							// forward(NavigationUi.class);
							break;

						default:
							break;
						}
					}
				});
			} else {
				Toast.makeText(this, "group is null", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this, "tabHost is null", Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		/*menu.add(0,PRESENT_ORDER,0,"当前业务信息");
		menu.add(0,HISTORY_ORDER,0,"历史业务信息");
		return super.onCreateOptionsMenu(menu);*/
		final MenuItem present_order = menu.add("当前业务信息");
		final MenuItem history_order = menu.add("历史业务信息");
		final Intent show_detail = new Intent(this,LoginUi.class);
		final Intent show_list = new Intent(this,OrderListUi.class);
		
		present_order.setOnMenuItemClickListener(new OnMenuItemClickListener(){
			@Override
			public boolean onMenuItemClick(MenuItem item){
				Log.i("test", "choose menu item present_order.");
				//这里会有一些测试代码，正常后删去///////////////////////////////////////////////////
//				Bundle bundle = new Bundle();
//				OrderList orderList = new OrderList();
//				List<String> l = new ArrayList();
//				l.add("a");
//				l.add("ab");
//				l.add("ac");
//				l.add("ad");
//				l.add("aeeeeee");
//				l.add("afffff");
//				l.add("aggggggggg");
//				l.add("aggggggggg");
//				l.add("aggggggggg");
//				l.add("aggggggggg");
//				l.add("aggggggggg");
//				l.add("aggggggggg");
//				orderList.setOrderList(l);
//				bundle.putSerializable("order_list_key", orderList);
//				show_list.putExtra("type", "order_list");  
//				show_list.putExtras(bundle);
					
				startActivity(show_list);
				//forward(OrderListUi.class);
				//doTaskAskHistory();
				return true;
				
				
				//测试代码结束//////////////////////////////////////////////////////////////////////
				//一下为正常代码，应解除注释
				/*
				doTaskAskPresent();
				return false;*/
			}
		});
		history_order.setOnMenuItemClickListener(new OnMenuItemClickListener(){
			@Override
			public boolean onMenuItemClick(MenuItem item){
				
				doTaskAskHistory();
				return false;
			}
		});
		
		return super.onCreateOptionsMenu(menu);
		
	}
	
	public void doTaskAskPresent(){
		//查找数据库，要求找出所有当前订单，返回orderlist类，里面应该有orderid的一个list，内容为所有order的ID
   	 
 		HashMap<String, String> urlParams = new HashMap<String,String>();
 		
 		//urlParams.put("sid", AppUser.getUser().getSid());
 		urlParams.put("id", AppUser.getUser().getId());

 		try{
 			this.showLoadBar();
 			//this.lockScreen();// 锁定屏幕，等待过程中不能
 			this.doTaskAsync(C.task.ask_present_order, C.api.ask_present_order, urlParams);
 			//this.hideLoadBar();
 		}catch(Exception e){
 			e.printStackTrace();
 		}
 		
     }
	
	public void doTaskAskHistory(){
		//查找数据库，要求找出所有历史订单，返回orderlist类，里面应该有orderid的一个list，内容为所有order的ID
		//与上一个函数的C.TASK.和C.API.后内容不同
	   	 
 		HashMap<String, String> urlParams = new HashMap<String,String>();
 		
 		//urlParams.put("sid", AppUser.getUser().getSid());
 		urlParams.put("id", AppUser.getUser().getId());

 		try{
 			this.showLoadBar();
 			
 			this.doTaskAsync(C.task.ask_history_order, C.api.ask_history_order, urlParams);
 			//this.hideLoadBar();
 		}catch(Exception e){
 			e.printStackTrace();
 		}
 		
     }
	
	@Override
	public void onTaskComplete(int taskId, BaseMessage message){
		final Intent show_list = new Intent(this,OrderListUi.class);
		Bundle bundle = new Bundle();
		OrderList orderList = null;
		if(taskId == C.task.ask_history_order||taskId == C.task.ask_present_order){
			try{
				orderList = (OrderList)message.getResult("OrderList");
				if(orderList.getOrderList().size()==0){
					hideLoadBar();
					Toast.makeText(this, "查询订单内容为空", Toast.LENGTH_LONG).show();
					return;
				}else{
				bundle.putSerializable("order_list_key", orderList);
				show_list.putExtra("type", "order_list");  
				show_list.putExtras(bundle);
				
				hideLoadBar();	
				startActivity(show_list);
				}
			}catch(Exception e){
 				e.printStackTrace();
				this.toast(e.getMessage());
 			}finally{
 				this.hideLoadBar();
 			}
			
			
		}
	}
	
	

}
