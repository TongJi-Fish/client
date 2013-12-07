package com.app.ipinle.ui;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ipinle.base.BaseMessage;
import com.app.ipinle.base.BaseUi;
import com.app.ipinle.base.C;
import com.app.ipinle.model.OrderDetail;
import com.app.ipinle.model.OrderList;
import com.app.ipinle.model.SubmitFeedback;
import com.sse.eping.R;

public class OrderDetailUi extends BaseUi {

	private String order_id = null;

	TextView id;
	TextView time;
	TextView start;
	TextView terminal;
	TextView type;
	OrderDetail orderDetail;
	private Button ok;
	Intent back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_order_detail);
		id = (TextView) findViewById(R.id.order_nub);
		time = (TextView) findViewById(R.id.order_time);
		start = (TextView) findViewById(R.id.order_start);
		terminal = (TextView) findViewById(R.id.order_terminal);
		type = (TextView) findViewById(R.id.order_type);
		ok = (Button) findViewById(R.id.detail_ok);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		this.order_id = (String) bundle.get("order_id");

		// id.setText(orderDetail.getOrderId());
		// time.setText(orderDetail.getOrderTime());
		// start.setText(orderDetail.getOrderStart());
		// terminal.setText(orderDetail.getOrderTerminal());
		// type.setText(orderDetail.getOrderType());

		back = new Intent(this, TemplateUi.class);
		ok.setOnClickListener(onClickListener);

		this.doTaskAskPresent();

	}

	// 请求订单详细内容
	public void doTaskAskPresent() {
		// 查找数据库，要求找出所有当前订单，返回orderlist类，里面应该有orderid的一个list，内容为所有order的ID

		HashMap<String, String> urlParams = new HashMap<String, String>();
		if (!this.order_id.equalsIgnoreCase("myorder")) {
			Log.i("orderdetailUi", "get by id:"+this.order_id);
			urlParams.put("order_id", this.order_id);
			try {
				this.showLoadBar();
				// this.lockScreen();// 锁定屏幕，等待过程中不能
				this.doTaskAsync(C.task.ask_id_order,
						C.api.ask_id_order, urlParams);
				// this.hideLoadBar();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			Log.i("orderdetailUi", "get now order");
			try {
				this.showLoadBar();
				// this.lockScreen();// 锁定屏幕，等待过程中不能
				this.doTaskAsync(C.task.ask_now_order, C.api.ask_now_order,
						urlParams);
				// this.hideLoadBar();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * function: show the content get from server.
	 * 
	 * parameters: id,time,start,terminal,type
	 * 
	 * author: jack yu
	 */
	public void setPageContent(String o_id, String o_time, String o_start,
			String o_terminal, String o_type) {
		id.setText(o_id);
		time.setText(o_time);
		start.setText(o_start);
		terminal.setText(o_terminal);
		type.setText(o_type);
	}

	/**
     * 
     */
	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		// super.onTaskComplete(taskId, message);
		// ///////////////////////////////////////////////////////////////////////////
		Log.i("ordertailUI", "task complete.");
		OrderDetail orderDetail = null;
		if (taskId == C.task.ask_now_order || taskId == C.task.ask_id_order) {
			try {
				orderDetail = (OrderDetail) message.getResult("OrderDetail");

				if (orderDetail != null && orderDetail.getOrderId() != null) {
					Toast.makeText(OrderDetailUi.this, "查找成功",
							Toast.LENGTH_SHORT).show();
					// show the content
					this.setPageContent(orderDetail.getOrderId(),
							orderDetail.getOrderTime(),
							orderDetail.getOrderStart(),
							orderDetail.getOrderTerminal(),
							orderDetail.getOrderType());
				} else {
					Toast.makeText(OrderDetailUi.this, "查找订单失败，请稍候重试",
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
				this.toast("查找失败，请稍候重试" + e.getMessage());
			}

		}
		this.hideLoadBar();
	}

	OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.detail_ok) {
				back();
			}
		}
	};

	public void back() {
		this.finish();
	}

}
