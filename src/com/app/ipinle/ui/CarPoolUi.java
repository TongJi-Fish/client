package com.app.ipinle.ui;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.app.ipinle.base.C;
import com.app.ipinle.model.SubmitFeedback;
import com.app.ipinle.util.AppMap;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.NearbySearchInfo;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MKMapTouchListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.sse.eping.R;

public class CarPoolUi extends BaseUi implements CloudListener {

	private BMapManager mBMapManager = null;
	private MapView mMapView = null;
	private MapController mMapController = null;

	// 页面变量
	private View view1 = null;
	private View view2 = null;
	private View view3 = null;

	private Button bn1;
	private Button bn2;
	private Button ok;
	private Button back_to_1;
	private Button submit;
	private Button back_to_2;
	private EditText startPoint;
	private EditText terminalPoint;
	public iMessage message = new iMessage();
	private LayoutInflater inflater;
	private GridView gv_sample;
	private int page_now;
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
	private boolean allow_back = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 初始化BMapManager
		this.mBMapManager = AppMap.getInstance(this);
		// setContentView(R.layout.ui_choose_type);

		LayoutInflater inflater = getLayoutInflater();
		// 功能选择页面
		view1 = inflater.inflate(R.layout.ui_choose_type, null);
		// 路线选择页面（起始点，时间）
		view2 = inflater.inflate(R.layout.ui_choose_route, null);
		// 确认页面
		view3 = inflater.inflate(R.layout.ui_makesure, null);
		// 记录当前页面号，默认为1
		page_now = 1;
		setContentView(view1);

		initMap();
		initOtherComponent();

	}

	public void initMap() {
		// setContentView(R.layout.show_map);
		// CharSequence titleLable = "路线规划";
		// setTitle(titleLable);
		// 初始化地图
		mMapView = (MapView) view2.findViewById(R.id.bmapView_carpool);
		mMapView.setBuiltInZoomControls(false);
		GeoPoint point = new GeoPoint((int) (31.22 * 1E6), (int) (121.48 * 1E6));
		mMapView.getController().setCenter(point);
		mMapView.getController().setZoom(10);
		mMapView.getController().enableClick(true);

		mMapController = mMapView.getController();
		mMapController.enableClick(true);

		// 初始化
		CloudManager.getInstance().init(CarPoolUi.this);

		// 地图点击事件处理
		MKMapTouchListener mapTouchListener = new MKMapTouchListener() {
			@Override
			public void onMapClick(GeoPoint point) {
				// 在此处理地图单击事件
				Log.i("test", "click1+" + point.getLatitudeE6() / 1E6 + ","
						+ point.getLongitudeE6() / 1E6);
				Toast.makeText(CarPoolUi.this, "point:" + point.toString(),
						Toast.LENGTH_SHORT).show();
				// new AlertDialog.Builder(CarPoolUi.this)
				// .setTitle("查找周围站点")
				// .setMessage("是否查找周围站点吗？")
				// .setPositiveButton("是", null)
				// .setNegativeButton("否", null)
				// .show();

				doSearchStationCloud(point);
			}

			@Override
			public void onMapDoubleClick(GeoPoint point) {
				// 在此处理地图双击事件
				Log.i("test", "click2");
			}

			@Override
			public void onMapLongClick(GeoPoint point) {
				// 在此处理地图长按事件
				Log.i("test", "click3" + point.toString());
			}
		};
		mMapView.regMapTouchListner(mapTouchListener);
	}

	public void doSearchStationCloud(GeoPoint point) {
		// LocalSearchInfo info = new LocalSearchInfo();
		// info.ak = "isEmj74g2npsD7Cycyh1OZyM";
		// info.geoTableId = 41985;
		// info.tags = "ESSC-CAR站点";
		// info.region = "上海";// 城市
		// // CloudManager.getInstance().localSearch(info);
		// Toast.makeText(this, "开始云检索", Toast.LENGTH_SHORT).show();
		// Log.i("test", "开始云检索");

		// 周边检索
		NearbySearchInfo info = new NearbySearchInfo();
		info.ak = "isEmj74g2npsD7Cycyh1OZyM";
		info.geoTableId = 41985;
		info.tags = "ESSC-CAR站点";
		info.location = point.getLongitudeE6() / 1E6 + ","
				+ point.getLatitudeE6() / 1E6;// "121.168929,31.294379";
		info.radius = 3000;
		CloudManager.getInstance().nearbySearch(info);
	}

	public void initOtherComponent() {
		// 初始化其它组件
		bn1 = (Button) view1.findViewById(R.id.drive_car);// 第一屏中的选择开车按钮
		bn1.setOnClickListener(onClickListener);
		bn2 = (Button) view1.findViewById(R.id.carpool);// 第一屏的选择搭车按钮
		bn2.setOnClickListener(onClickListener);

		// 第二屏
		ok = (Button) view2.findViewById(R.id.make_sure);
		ok.setOnClickListener(onClickListener);
		back_to_1 = (Button) view2.findViewById(R.id.back_to_1);
		back_to_1.setOnClickListener(onClickListener);
		startPoint = (EditText) view2.findViewById(R.id.start_point);
		terminalPoint = (EditText) view2.findViewById(R.id.terminal_point);
		showTime = (EditText) view2.findViewById(R.id.showtime);
		//startPoint.setEnabled(false);
		//terminalPoint.setEnabled(false);

		initializeViews();

		final Calendar c = Calendar.getInstance();
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		setTimeOfDay();

		// 第三屏
		text1 = (TextView) view3.findViewById(R.id.makesure_type);
		text2 = (TextView) view3.findViewById(R.id.makesure_time);
		text3 = (TextView) view3.findViewById(R.id.makesure_start);
		text4 = (TextView) view3.findViewById(R.id.makesure_terminal);
		submit = (Button) view3.findViewById(R.id.submit);
		back_to_2 = (Button) view3.findViewById(R.id.back_to_2);
		submit.setOnClickListener(onClickListener);
		back_to_2.setOnClickListener(onClickListener);

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

	OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case (R.id.drive_car): {
				// 第一屏中的“我要开车”按钮
				message.setDrive(true);
				// ++count;
				ForwardPage();// 前进一页

				// initScreen2();
				break;
			}
			case (R.id.carpool): {
				message.setDrive(false);
				// ++count;
				// initScreen2();
				ForwardPage();// 前进一页
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
				// ++count;
				// setContentView(pageName[count]);
				// initScreen3();
				if (message.isDrive) {
					text1.setText("租车");
				} else {
					text1.setText("拼车");
				}
				text2.setText(message.getTime());
				text3.setText(message.getStart_point());
				text4.setText(message.getTerminal_point());
				ForwardPage();// 前进一页
				break;
			}
			case (R.id.back_to_1): {
				// --count;
				// setContentView(pageName[count]);
				BackwardPage();// 回退一页
				break;
			}
			case (R.id.back_to_2): {
				// --count;
				// initScreen2();
				BackwardPage();// 回退一页
				break;
			}
			case (R.id.submit): {
				// 提交所有信息！！
				doTaskSubmit();
			}

			}

			// setContentView(R.layout.test);

		}
	};

	/*
	 * function: forward one page
	 * 
	 * parameter: none
	 * 
	 * author: Jack Yu
	 */
	public void ForwardPage() {
		switch (page_now) {
		case 1:
			// from one to two
			page_now++;// 当前页面指针加1
			mMapView.setVisibility(View.VISIBLE);
			mMapView.onResume();
			setContentView(view2);
			break;
		case 2:
			// from two to three
			page_now++;// 当前页面指针加1
			// mMapView.invalidate();
			// mMapView.refresh();
			mMapView.setVisibility(View.INVISIBLE);
			mMapView.onPause();
			setContentView(view3);
			break;
		case 3:
			// something wrong here!
			Toast.makeText(CarPoolUi.this, "已经是最后一页了", Toast.LENGTH_SHORT)
					.show();
			break;
		}
	}

	/*
	 * function: backward one page
	 * 
	 * parameter: none
	 * 
	 * author: Jack Yu
	 */
	public void BackwardPage() {
		switch (page_now) {
		case 1:
			// something wrong here
			Toast.makeText(CarPoolUi.this, "已经是第一页了", Toast.LENGTH_SHORT)
					.show();
			break;
		case 2:
			// from two to one
			page_now--;
			mMapView.setVisibility(View.INVISIBLE);
			mMapView.onPause();
			setContentView(view1);
			break;
		case 3:
			// from three to two
			page_now--;
			mMapView.setVisibility(View.VISIBLE);
			mMapView.onResume();
			setContentView(view2);
			break;
		}
	}

	/**
	 * function: set start station
	 * 
	 * parameters: s_station: the name of start station
	 * 
	 * @author fish
	 * 
	 */
	public void setStartStation(String s_station) {
		this.startPoint.setText(s_station);
	}

	/**
	 * function: set end station
	 * 
	 * parameters: t_sataion: the name of terminal station
	 * 
	 * @author fish
	 * 
	 */
	public void setEndStation(String t_station) {
		this.terminalPoint.setText(t_station);
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

		showTime = (EditText) view2.findViewById(R.id.showtime);
		pickTime = (Button) view2.findViewById(R.id.picktime);
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
			
			if(allow_back == false)
				return super.onKeyDown(keyCode, event);
			
			if (page_now >= 2)
				BackwardPage();// 回退一页
			else {
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					Toast.makeText(getApplicationContext(), "再按一次退出程序",
							Toast.LENGTH_SHORT).show();
					exitTime = System.currentTimeMillis();
				} else {
					finish();
					System.exit(0);
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}

    public void doTaskSubmit(){
   	 
		HashMap<String, String> urlParams = new HashMap<String,String>();
		urlParams.put("type", Boolean.toString(this.message.isDrive()));
		urlParams.put("time", this.message.getTime().toString());
		urlParams.put("start_point", this.message.getStart_point());
		urlParams.put("terminal_point", this.message.getTerminal_point());
		try{
			this.showLoadBar();
			this.lockScreen();// 锁定屏幕，等待过程中不能
			this.doTaskAsync(C.task.submit, C.api.submit, urlParams);
		}catch(Exception e){
			e.printStackTrace();
		}
		
    }
	
    /**
     * 
     */
    @Override
	public void onTaskComplete(int taskId, BaseMessage message){
		//super.onTaskComplete(taskId, message);
		/////////////////////////////////////////////////////////////////////////////
   	 SubmitFeedback feedback = null;
		switch(taskId){
		case C.task.submit:
			try{
				feedback = (SubmitFeedback)message.getResult("SubmitFeedback");
				
				if(feedback != null && feedback.getOrder_id() != null){
					Toast.makeText(CarPoolUi.this, "预订成功",Toast.LENGTH_SHORT).show();
					//跳转回第一屏！
					BackwardPage();
					BackwardPage();
				}
				else{
					Toast.makeText(CarPoolUi.this, "预订失败，请稍候重试",Toast.LENGTH_SHORT).show();
				}
			}catch(Exception e){
				e.printStackTrace();
				this.toast("预订失败，请稍候重试"+e.getMessage());
			}finally{
				unLockScreen();
			}
			
			break;
		}
		this.hideLoadBar();
	}

    /**
     * 
     */
    @Override
    public void onTaskError(){
    	super.onTaskError();
    	this.unLockScreen();
    	Log.i("test", "task error");
    }
    
    public void lockScreen(){
		if(this.submit!=null)
			this.submit.setEnabled(false);
		if(this.back_to_1!=null)////要改成backto2
			this.back_to_1.setEnabled(false);
		this.allow_back = false;

	 }
    
    public void unLockScreen(){
   	 if(this.submit!=null)
 			this.submit.setEnabled(true);
 		if(this.back_to_1!=null)////要改成backto2
 			this.back_to_1.setEnabled(true);
 		this.allow_back = true;
    }

	/**
	 * function: search stations in cloud
	 * 
	 * parameters:
	 * 
	 * author: Jack Yu
	 * 
	 */
	public void searchCloudStation() {

	}

	// 重写云检索相关方法
	@Override
	public void onGetDetailSearchResult(DetailSearchResult arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetSearchResult(CloudSearchResult result, int error) {
		// TODO Auto-generated method stub
		if (result != null && result.poiList != null
				&& result.poiList.size() > 0) {
			// Toast.makeText(this, "收到云检索结果", Toast.LENGTH_SHORT).show();
			// Log.i("test", "收到云检索结果");
			CloudOverlay poiOverlay = new CloudOverlay(CarPoolUi.this, mMapView);
			poiOverlay.setData(result.poiList);
			mMapView.getOverlays().clear();
			mMapView.getOverlays().add(poiOverlay);
			mMapView.refresh();
			mMapView.getController().animateTo(
					new GeoPoint((int) (result.poiList.get(0).latitude * 1e6),
							(int) (result.poiList.get(0).longitude * 1e6)));
		} else {
			Toast.makeText(this, "云检索结果为空", Toast.LENGTH_SHORT).show();
			Log.i("test", "云检索结果为空");
		}
	}

	// 定义装载云检索数据的类，因为是不可见的，所以放在内部
	class CloudOverlay extends ItemizedOverlay {

		List<CloudPoiInfo> mLbsPoints;
		Activity mContext;

		// CarPoolUi carpoolui;

		public CloudOverlay(CarPoolUi carpoolui, MapView mMapView) {
			super(null, mMapView);
			mContext = carpoolui;
			// this.showmap = showmap;
		}

		public void setData(List<CloudPoiInfo> lbsPoints) {
			if (lbsPoints != null) {
				mLbsPoints = lbsPoints;
			}
			for (CloudPoiInfo rec : mLbsPoints) {
				GeoPoint pt = new GeoPoint((int) (rec.latitude * 1e6),
						(int) (rec.longitude * 1e6));
				OverlayItem item = new OverlayItem(pt, rec.title,
						rec.extras.toString());
				// Toast.makeText(this.mContext,
				// rec.extras.get("station_name").toString(),
				// Toast.LENGTH_LONG).show();
				// Log.i("station_name",
				// rec.extras.get("station_name").toString());
				if (rec.extras.get("station_name").toString().endsWith("222")) {
					Drawable marker1 = this.mContext.getResources()
							.getDrawable(R.drawable.next_);
					item.setMarker(marker1);
				} else {
					Drawable marker1 = this.mContext.getResources()
							.getDrawable(R.drawable.icon_gcoding);
					item.setMarker(marker1);
				}
				addItem(item);
			}
		}

		@Override
		protected Object clone() throws CloneNotSupportedException {
			// TODO Auto-generated method stub
			return super.clone();
		}

		@Override
		protected boolean onTap(int arg0) {
			CloudPoiInfo item = mLbsPoints.get(arg0);
			
			final String stationName = item.extras.get("station_name").toString();
//			Toast.makeText(mContext, "选择了" + item.title, Toast.LENGTH_LONG)
//					.show();
//			Log.i("test", item.extras.get("station_name").toString());
//			// this.carpoolui = (ShowMap) this.mContext;
//			// this.carpoolui.showWhenTap(item.extras.toString());
//			setStartStation(item.extras.get("station_name").toString());

			// 设置提示框

			AlertDialog.Builder builder = new AlertDialog.Builder(
					CarPoolUi.this);
			builder.setTitle("将 " + stationName);

			builder.setItems(new String[] { "设为起点", "设为终点" },
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:

								setStartStation(stationName);
								break;
							case 1:
								setEndStation(stationName);
								break;
							default:
								break;
							}
							dialog.dismiss();
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();

			return super.onTap(arg0);
		}

	}
}
