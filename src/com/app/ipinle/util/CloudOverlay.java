package com.app.ipinle.util;

import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.app.ipinle.ui.CarPoolUi;
import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.sse.eping.R;

class CloudOverlay extends ItemizedOverlay {

	List<CloudPoiInfo> mLbsPoints;
	Activity mContext;
	CarPoolUi carpoolui;

	public CloudOverlay(CarPoolUi carpoolui, Activity context, MapView mMapView) {
		super(null, mMapView);
		mContext = context;
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
			//Toast.makeText(this.mContext, rec.extras.get("station_name").toString(), Toast.LENGTH_LONG).show();
			//Log.i("station_name", rec.extras.get("station_name").toString());
			if (rec.extras.get("station_name").toString().endsWith("222")) {
				Drawable marker1 = this.mContext.getResources().getDrawable(
						R.drawable.next_);
				item.setMarker(marker1);
			} else {
				Drawable marker1 = this.mContext.getResources().getDrawable(
						R.drawable.ic_launcher);
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
		// Toast.makeText(mContext, "��ѡ����"+item.title,Toast.LENGTH_LONG).show();
		//this.carpoolui = (ShowMap) this.mContext;
		//this.carpoolui.showWhenTap(item.extras.toString());
		return super.onTap(arg0);
	}

}
