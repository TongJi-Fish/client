package com.app.ipinle.ui;

import android.os.Bundle;
import android.view.View;

import com.app.ipinle.base.BaseUi;
import com.app.ipinle.util.AppMap;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.sse.eping.R;

public class CarPoolUi extends BaseUi {


	private BMapManager mBMapManager = null;
	private MapView mMapView = null;
	private MapController mMapController = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		setContentView(R.layout.ui_carpool);
		super.onCreate(savedInstanceState);
		
		this.mBMapManager = AppMap.getInstance(this);

		setContentView(R.layout.show_map);
		CharSequence titleLable = "路线规划";
		setTitle(titleLable);
		// 初始化地图
		mMapView  = (MapView) findViewById(R.id.bmapsView);
		mMapView.setBuiltInZoomControls(false);
		GeoPoint point = new GeoPoint((int) (31.22 * 1E6), (int) (121.48 * 1E6));
		mMapView.getController().setCenter(point);
		mMapView.getController().setZoom(10);
		mMapView.getController().enableClick(true);

		mMapController  = mMapView.getController();
		mMapController.enableClick(true);
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
