
package com.daxiong.fun.function.account;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.function.account.model.CityModel;
import com.daxiong.fun.function.account.model.DistrictModel;
import com.daxiong.fun.function.account.model.ProvinceModel;
import com.daxiong.fun.util.BtnUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SelectCityActivity extends BaseActivity {
	private ListView msg_list;

	/**
	 * 所有省
	 */
	protected String[] mProvinceDatas;
	/**
	 * 一省所有市
	 */
	protected String[] citys;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

	/**
	 * key - 区 values - 邮编
	 */
	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	protected String mCurrentDistrictName = "";

	/**
	 * 当前区的邮政编码
	 */
	protected String mCurrentZipCode = "";

	/**
	 * 解析省市区的XML数据
	 */
	private boolean falg = false;
	private ImageView back_iv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_selectcity);
		msg_list = (ListView) findViewById(R.id.msg_list);
		back_iv = (ImageView) findViewById(R.id.back_iv);
		setWelearnTitle("选择地区");
		back_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(BtnUtils.isFastClick()){
					return;
				}
				
				if (!falg) {
					Intent intent = new Intent();
					intent.putExtra("Province", "");
					intent.putExtra("city", "");
					setResult(RESULT_OK, intent);

					finish();
				} else {
					msg_list.setAdapter(new CityListAdapter(SelectCityActivity.this, mProvinceDatas));
					mCurrentProviceName="";
					falg = false;
				}

			}
		});
		initProvinceDatas();
		msg_list.setAdapter(new CityListAdapter(this, mProvinceDatas));
		msg_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (!falg) {
					mCurrentProviceName=mProvinceDatas[position];
					citys = mCitisDatasMap.get(mCurrentProviceName);
					msg_list.setAdapter(new CityListAdapter(SelectCityActivity.this, citys));
					falg = true;
				} else {
					mCurrentCityName=citys[position];
					Intent intent = new Intent();
					intent.putExtra("Province", mCurrentProviceName);
					intent.putExtra("city", mCurrentCityName);
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(BtnUtils.isFastClick()){
				return false;
			}
			if (!falg) {
				Intent intent = new Intent();

				intent.putExtra("Province", "");
				intent.putExtra("city", "");
				setResult(RESULT_OK, intent);

				finish();
			} else {
				msg_list.setAdapter(new CityListAdapter(SelectCityActivity.this, mProvinceDatas));
				mCurrentProviceName="";
				falg = false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void initProvinceDatas() {
		List<ProvinceModel> provinceList = null;
		AssetManager asset = getAssets();
		try {
			InputStream input = asset.open("province_data.xml");
			// 创建一个解析xml的工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 解析xml
			SAXParser parser = spf.newSAXParser();
			XmlParserHandler handler = new XmlParserHandler();
			parser.parse(input, handler);
			input.close();
			// 获取解析出来的数据
			provinceList = handler.getDataList();
			// */ 初始化默认选中的省、市、区
			if (provinceList != null && !provinceList.isEmpty()) {
				mCurrentProviceName = provinceList.get(0).getName();
				List<CityModel> cityList = provinceList.get(0).getCityList();
				if (cityList != null && !cityList.isEmpty()) {
					mCurrentCityName = cityList.get(0).getName();
					List<DistrictModel> districtList = cityList.get(0).getDistrictList();
					mCurrentDistrictName = districtList.get(0).getName();
					mCurrentZipCode = districtList.get(0).getZipcode();
				}
			}
			// */
			mProvinceDatas = new String[provinceList.size()];
			for (int i = 0; i < provinceList.size(); i++) {
				// 遍历所有省的数据
				mProvinceDatas[i] = provinceList.get(i).getName();
				List<CityModel> cityList = provinceList.get(i).getCityList();
				String[] cityNames = new String[cityList.size()];
				for (int j = 0; j < cityList.size(); j++) {
					// 遍历省下面的所有市的数据
					cityNames[j] = cityList.get(j).getName();
					List<DistrictModel> districtList = cityList.get(j).getDistrictList();
					String[] distrinctNameArray = new String[districtList.size()];
					DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
					for (int k = 0; k < districtList.size(); k++) {
						// 遍历市下面所有区/县的数据
						DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(),
								districtList.get(k).getZipcode());
						// 区/县对于的邮编，保存到mZipcodeDatasMap
						mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
						distrinctArray[k] = districtModel;
						distrinctNameArray[k] = districtModel.getName();
					}
					// 市-区/县的数据，保存到mDistrictDatasMap
					mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
				}
				// 省-市的数据，保存到mCitisDatasMap
				mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

		}
	}
}
