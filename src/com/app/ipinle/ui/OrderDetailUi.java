package com.app.ipinle.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.app.ipinle.base.BaseUi;
import com.app.ipinle.model.OrderDetail;
import com.app.ipinle.model.OrderList;
import com.sse.eping.R;

public class OrderDetailUi extends BaseUi {
	
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
		id = (TextView)findViewById(R.id.order_nub);
		time = (TextView)findViewById(R.id.order_time);
		start = (TextView)findViewById(R.id.order_start);
		terminal = (TextView)findViewById(R.id.order_terminal);
		type = (TextView)findViewById(R.id.order_type);
		ok = (Button)findViewById(R.id.detail_ok);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		orderDetail = (OrderDetail)bundle.getSerializable("order_detail_key");

		id.setText(orderDetail.getOrderId());
		time.setText(orderDetail.getOrderTime());
		start.setText(orderDetail.getOrderStart());
		terminal.setText(orderDetail.getOrderTerminal());
		type.setText(orderDetail.getOrderType());
		
		back = new Intent(this,TemplateUi.class);
		ok.setOnClickListener(onClickListener);
		
	}
	
	OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.detail_ok){
				
				startActivity(back);
			}
		}
	};

}
