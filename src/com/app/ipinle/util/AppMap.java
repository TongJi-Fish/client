package com.app.ipinle.util;

import android.app.Activity;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;

public class AppMap {

	private static BMapManager mBMapManager = null;
	public static final String strKey = "sMW9h3hm9sSonRQ8Dd6cw03q";
	
	public static <BMapMnager> BMapMnager getInstance(Activity at){
		AppMap.mBMapManager = new BMapManager(at.getApplicationContext());
		AppMap.init(at);
		return (BMapMnager) AppMap.mBMapManager;
	}
	
	protected static void init(Activity at){
		if (!AppMap.mBMapManager.init(strKey, new MyGeneralListener(at))) {
		Toast.makeText(at.getApplicationContext(), "BMapManager  初始化错误!",
				Toast.LENGTH_LONG).show();
	}
	
	}

}

// 常用事件监听，用来处理通常的网络错误，授权验证错误等
class MyGeneralListener implements MKGeneralListener {
	
	private Activity at;
	
	public MyGeneralListener(Activity at){
		this.at = at;
	}

	@Override
	public void onGetNetworkState(int iError) {
		if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
			Toast.makeText(at.getApplicationContext(), "网络出错啦！",
					Toast.LENGTH_LONG).show();
		} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
			Toast.makeText(at.getApplicationContext(), "输入正确的检索条件！",
					Toast.LENGTH_LONG).show();
		}
		// ...
	}

	@Override
	public void onGetPermissionState(int iError) {
		if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
			// 授权Key错误：
			Toast.makeText(at.getApplicationContext(),
					"请在 DemoApplication.java文件输入正确的授权Key！",
					Toast.LENGTH_LONG).show();
		}
	}
}
