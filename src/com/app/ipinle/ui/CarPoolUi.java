package com.app.ipinle.ui;

import java.util.Calendar;
import java.util.HashMap;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;

import android.os.Bundle;
import android.view.View;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.ipinle.base.BaseMessage;
import com.app.ipinle.base.BaseUi;
import com.app.ipinle.util.AppMap;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.sse.eping.R;

import com.app.ipinle.base.C;
import com.app.ipinle.model.User;
import com.app.ipinle.util.AppUser;
import com.sse.eping.R;

public class CarPoolUi extends BaseUi {

	private BMapManager mBMapManager = null;
	private MapView mMapView = null;
	private MapController mMapController = null;

	private Button bn1;
	private Button bn2;
	private Button ok;
	private Button back;
	private Button submit;
	private Button back_to_1;
	private EditText startPoint;
	private EditText terminalPoint;
	public iMessage message = new iMessage();
	private LayoutInflater inflater;
	private GridView gv_sample;
	private int count;
	private int[] pageName;

	private int mHour;
	private int mMinute;
	private EditText showTime = null;
	private Button pickTime = null;
	private static final int SHOW_TIMEPICK = 0;
	private static final int TIME_DIALOG_ID = 1;

	private TextView text1;
	private TextView text2;
	private TextView text3;
	private TextView text4;

	private long exitTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.ui_choose_type);
		super.onCreate(savedInstanceState);
		pageName = new int[3];
		pageName[0] = R.layout.ui_choose_type;
		pageName[1] = R.layout.ui_choose_route;
		pageName[2] = R.layout.ui_makesure;

		count = 0;
		bn1 = (Button) findViewById(R.id.drive_car);
		bn1.setOnClickListener(onClickListener);
		bn2 = (Button) findViewById(R.id.carpool);
		bn2.setOnClickListener(onClickListener);

		this.mBMapManager = AppMap.getInstance(this);

		setContentView(R.layout.show_map);
		CharSequence titleLable = "路线规划";
		setTitle(titleLable);
		// 初始化地图
		mMapView = (MapView) findViewById(R.id.bmapsView);
		mMapView.setBuiltInZoomControls(false);
		GeoPoint point = new GeoPoint((int) (31.22 * 1E6), (int) (121.48 * 1E6));
		mMapView.getController().setCenter(point);
		mMapView.getController().setZoom(10);
		mMapView.getController().enableClick(true);

		mMapController = mMapView.getController();
		mMapController.enableClick(true);

	}

	OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case (R.id.drive_car): {
				message.setDrive(true);
				++count;

				initScreen2();
				break;
			}
			case (R.id.carpool): {
				message.setDrive(false);
				++count;
				initScreen2();

				break;
			}

			case (R.id.make_sure): {

				if (startPoint.getText().length() <= 0
						|| terminalPoint.getText().length() <= 0) {
					Toast.makeText(CarPoolUi.this, "请输入起点及终点",
							Toast.LENGTH_SHORT).show();
					break;
				}

				final Calendar c = Calendar.getInstance();
				mHour = c.get(Calendar.HOUR_OF_DAY);
				mMinute = c.get(Calendar.MINUTE);
				String presentTime = Integer.toString(mHour) + ":"
						+ Integer.toString(mMinute);
				String orderTime = showTime.getText().toString();
				if (presentTime.length() > orderTime.length()) {
					Toast.makeText(CarPoolUi.this, "订车时间必须晚于当前时间",
							Toast.LENGTH_SHORT).show();
					break;
				} else {
					if (presentTime.compareTo(orderTime) > 0) {
						Toast.makeText(CarPoolUi.this, "订车时间必须晚于当前时间",
								Toast.LENGTH_SHORT).show();
						break;
					}
				}

				message.setTime(showTime.getText().toString());

				message.setStart_point(startPoint.getText().toString());
				message.setTerminal_point(terminalPoint.getText().toString());
				++count;
				setContentView(pageName[count]);

				initScreen3();
				break;
			}
			case (R.id.back_to_0): {
				--count;
				setContentView(pageName[count]);
				bn1 = (Button) findViewById(R.id.drive_car);
				bn1.setOnClickListener(onClickListener);
				bn2 = (Button) findViewById(R.id.carpool);
				bn2.setOnClickListener(onClickListener);
				break;
			}
			case (R.id.back_to_1): {
				--count;
				initScreen2();
				break;
			}
			case (R.id.submit): {
				// 提交所有信息！！
			}

			}

			// setContentView(R.layout.test);

		}
	};

	public void initScreen2() {

		setContentView(pageName[count]);
		ok = (Button) findViewById(R.id.make_sure);
		ok.setOnClickListener(onClickListener);
		back = (Button) findViewById(R.id.back_to_0);
		back.setOnClickListener(onClickListener);
		startPoint = (EditText) findViewById(R.id.start_point);
		terminalPoint = (EditText) findViewById(R.id.terminal_point);
		showTime = (EditText) findViewById(R.id.showtime);

		initializeViews();

		final Calendar c = Calendar.getInstance();
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		setTimeOfDay();
	}

	public void initScreen3() {
		setContentView(pageName[count]);
		text1 = (TextView) findViewById(R.id.makesure_type);
		if (message.isDrive) {
			text1.setText("租车");
		} else {
			text1.setText("拼车");
		}
		text2 = (TextView) findViewById(R.id.makesure_time);
		text2.setText(message.getTime());
		text3 = (TextView) findViewById(R.id.makesure_start);
		text3.setText(message.getStart_point());
		text4 = (TextView) findViewById(R.id.makesure_terminal);
		text4.setText(message.getTerminal_point());
		submit = (Button) findViewById(R.id.submit);
		back_to_1 = (Button) findViewById(R.id.back_to_1);
		submit.setOnClickListener(onClickListener);
		back_to_1.setOnClickListener(onClickListener);
	}

	class iMessage {
		private boolean isDrive;
		private String start_point;
		private String terminal_point;
		private String time;
		private int number;

		public boolean isDrive() {
			return isDrive;
		}

		public void setDrive(boolean isDrive) {
			this.isDrive = isDrive;
		}

		public String getStart_point() {
			return start_point;
		}

		public void setStart_point(String start_point) {
			this.start_point = start_point;
		}

		public String getTerminal_point() {
			return terminal_point;
		}

		public void setTerminal_point(String terminal_point) {
			this.terminal_point = terminal_point;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String terminal_point) {
			this.time = terminal_point;
		}

		public int getNumber() {
			return number;
		}

		public void setNumber(int number) {
			this.number = number;
		}
	}

	private void initializeViews() {

		showTime = (EditText) findViewById(R.id.showtime);
		pickTime = (Button) findViewById(R.id.picktime);
		pickTime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Message msg = new Message();
				if (pickTime.equals((Button) v)) {
					msg.what = CarPoolUi.SHOW_TIMEPICK;
				}
				CarPoolUi.this.dateandtimeHandler.sendMessage(msg);
			}
		});
	}

	/**
	 * 设置时间
	 */
	private void setTimeOfDay() {
		final Calendar c = Calendar.getInstance();
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		updateTimeDisplay();
	}

	/**
	 * 更新时间显示
	 */
	private void updateTimeDisplay() {
		showTime.setText(new StringBuilder().append(mHour).append(":")
				.append((mMinute < 10) ? "0" + mMinute : mMinute));
	}

	/**
	 * 时间控件事件
	 */
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;

			updateTimeDisplay();
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {

		return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
				true);

	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {

		((TimePickerDialog) dialog).updateTime(mHour, mMinute);
	}

	Handler dateandtimeHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case CarPoolUi.SHOW_TIMEPICK:
				showDialog(TIME_DIALOG_ID);
				break;
			}
		}

	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			switch (count) {

			case (0): {
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					Toast.makeText(getApplicationContext(), "再按一次退出程序",
							Toast.LENGTH_SHORT).show();
					exitTime = System.currentTimeMillis();
				} else {
					finish();
					System.exit(0);
				}
				break;
			}
			case (1): {
				--count;
				setContentView(pageName[count]);
				bn1 = (Button) findViewById(R.id.drive_car);
				bn1.setOnClickListener(onClickListener);
				bn2 = (Button) findViewById(R.id.carpool);
				bn2.setOnClickListener(onClickListener);
				break;
			}
			case (2): {
				--count;
				initScreen2();
				break;
			}
			}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void doTaskSubmit() {

		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("typw", Boolean.toString(this.message.isDrive()));
		urlParams.put("time", this.message.getTime().toString());
		urlParams.put("start_poing", this.message.getStart_point());
		urlParams.put("terminal_poing", this.message.getTerminal_point());
		try {
			this.showLoadBar();

			this.doTaskAsync(C.task.login, C.api.login, urlParams);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		super.onTaskComplete(taskId, message);
		// ///////////////////////////////////////////////////////////////////////////
		this.hideLoadBar();
	}

	@Override
	protected void onPause() {
		/**
		 * MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		 */
		mMapView.setVisibility(View.INVISIBLE);
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		/**
		 * MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		 */
		mMapView.setVisibility(View.VISIBLE);
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		/**
		 * MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		 */
		mMapView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}
}
