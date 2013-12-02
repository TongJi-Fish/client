package com.app.ipinle.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.app.ipinle.base.BaseUi;
import com.sse.eping.MainActivity;
import com.sse.eping.R;

public class ShowBusRouteUi extends BaseUi {

	@Override
	protected void onCreate(Bundle savedInstanceState){
		setContentView(R.layout.ui_show_bus_route);
		super.onCreate(savedInstanceState);
		Button btn = (Button) findViewById(R.id.testbtn);
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent .setClass(ShowBusRouteUi.this, MainActivity.class);
				startActivity(intent);
			}
			
		});
	}
}
