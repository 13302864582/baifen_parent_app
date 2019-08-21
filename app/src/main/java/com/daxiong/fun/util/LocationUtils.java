
package com.daxiong.fun.util;

import android.annotation.SuppressLint;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.okhttp.OkHttpUtils;
import com.daxiong.fun.okhttp.callback.StringCallback;

import okhttp3.Call;

public class LocationUtils {

	private static LocationUtils mLocationUtils;

	private LocationManager locationManager;

	private static LocationChangedListener locListener;

	private Criteria criteria;

	private static final int MSG_REQUEST_LOCATION = 0x1;

	private int currCount = 0;

	public static final int GET_LOCATION_INTERVAL_TIME = 5000;

	public static final int GET_LOCATION_COUNT = 3;

	public static String URL = "http://api.map.baidu.com/geocoder/v2/?" + "ak=bhxSDTX4vE7DoZxCBMsYamaK"
			+ "&mcode=AC:16:DC:65:B6:B8:65:92:F0:74:5E:DF:42:0D:73:C9:5E:61:69:41;com.welearn.welearn" + "&output=json"
			+ "&coordtype=wgs84ll" + "&location=";

	private static BDLocationUtils bdLocationUtils;

	public LocationClient mLocationClient;

	@SuppressLint("HandlerLeak")
	private static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			/*
			 * case MSG_REQUEST_LOCATION: if (currCount < GET_LOCATION_COUNT) {
			 * currCount++; requestLocataion();
			 * mHandler.sendEmptyMessageDelayed(MSG_REQUEST_LOCATION,
			 * GET_LOCATION_INTERVAL_TIME); } else { if
			 * (hasMessages(MSG_REQUEST_LOCATION)) {
			 * removeMessages(MSG_REQUEST_LOCATION); } } break;
			 */
			case BDLocationUtils.LOCATION_DATA_CODE:// 百度地图执行结果
				BDLocation location = (BDLocation) msg.obj;
				if (location != null) {
					netRequestLocation(location);
				}
				break;
			}
		}
	};

	public static LocationUtils getInstance(LocationChangedListener listener) {
		if (null == mLocationUtils) {
			mLocationUtils = new LocationUtils();
		}
		locListener = listener;
		bdLocationUtils = new BDLocationUtils(MyApplication.getContext(), mHandler);
		return mLocationUtils;
	}

	private LocationUtils() {

	}

	public void startBDLocation() {
		bdLocationUtils.startBDLocation();
	}

	public void stopBDLocation() {
		bdLocationUtils.stopBDLocation();
	}

	public static void netRequestLocation(BDLocation location) {
		if (null == location) {
			return;
		}

		try {
			// 从百度定位中取得省
			String province = location.getProvince();
			// 从百度定位中取得市
			String city = location.getCity();
			if (null != locListener) {
				locListener.onLocationChanged(location, province, city);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private LocationListener mLocationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		}

		@Override
		public void onProviderEnabled(String arg0) {
		}

		@Override
		public void onProviderDisabled(String arg0) {
		}

		@Override
		public void onLocationChanged(final Location location) {
			new Thread() {
				public void run() {
					updateToNewLocation(location);
				};
			}.start();
		}
	};

	private void updateToNewLocation(Location location) {
		if (null == location) {
			return;
		}
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		try {
			// httpResult = OkHttpHelper.get(URL + +latitude + "," + longitude);
			OkHttpUtils.post().url(URL + +latitude + "," + longitude).build().connTimeOut(5000).writeTimeOut(8000)
					.readTimeOut(8000).execute(new StringCallback() {
						@Override
						public void onResponse(String response) {
							if (!TextUtils.isEmpty(response)) {
								int status = JsonUtil.getInt(response, "status", -1);
								if (status != 0) {
									return;
								}
								String result = JsonUtil.getString(response, "result", "");
								String address = JsonUtil.getString(result, "addressComponent", "");
								String province = JsonUtil.getString(address, "province", "");
								String city = JsonUtil.getString(address, "city", "");
								if (null != locListener) {
									locListener.onLocationChanged(new BDLocation(), province, city);
								}
							}

						}

						@Override
						public void onError(Call call, Exception e) {

						}
					});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void requestLocataion() {
		String provider = LocationManager.GPS_PROVIDER;
		provider = locationManager.getBestProvider(criteria, true);
		if (provider != null) {
			locationManager.requestLocationUpdates(provider, 0, 0, mLocationListener);
		}

	}

	// public void startListen(LocationChangedListener listener) {
	// currCount = 0;
	// locListener = listener;
	// if (!mHandler.hasMessages(MSG_REQUEST_LOCATION)) {
	// mHandler.sendEmptyMessage(MSG_REQUEST_LOCATION);
	// }
	// }
	//
	// public void stopListen() {
	// currCount = 0;
	// mHandler.removeMessages(MSG_REQUEST_LOCATION);
	// if (null != locationManager) {
	// locationManager.removeUpdates(mLocationListener);
	// }
	// }

	private void getCriteria() {
		criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
	}

	// 定义接口便于回调
	public interface LocationChangedListener {
		public void onLocationChanged(BDLocation location, String province, String city);
	}
}
