
package com.daxiong.fun.function.myfudaoquan;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.daxiong.fun.R;
import com.daxiong.fun.api.FudaoquanAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.function.myfudaoquan.adapter.FudaoquanCommonAdapter;
import com.daxiong.fun.model.FudaoquanModel;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.ToastUtils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * 过期的的辅导券
 * 
 * @author: sky
 */
public class ExpireFudaoquanActivity extends BaseActivity {
	private RelativeLayout back_layout;

	private ListView lv;
	private Button btn_go_getfudaoquan;

	private String tag = "";

	private FudaoquanAPI fudaoquanApi;

	private List<FudaoquanModel> fudaoquanList = null;

	private FudaoquanCommonAdapter mAdapter;
	private LinearLayout empty_view;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.expire_fudaoquan_activity);
		getExtraData();
		initView();
		initListener();
		initData();

	}

	public void getExtraData() {
		Intent i = getIntent();
		tag = i.getStringExtra("tag");

	}

	@Override
	public void initView() {
		super.initView();
		back_layout = (RelativeLayout) findViewById(R.id.back_layout);
		empty_view = (LinearLayout) findViewById(R.id.empty_view);
		btn_go_getfudaoquan = (Button) findViewById(R.id.btn_go_getfudaoquan);		
		this.lv = (ListView) findViewById(R.id.lv);
		if ("choice_tag_question".equals(tag)) {// 选择难题答疑辅导券
			setWelearnTitle(R.string.choice_fudaoquan);
		} else if ("choice_tag_homework".equals(tag)) {// 选择作业检查辅导券
			setWelearnTitle(R.string.choice_fudaoquan);
		} else {// 过期辅导券
			setWelearnTitle(R.string.expire_fudaoquan);
		}
		fudaoquanList = new ArrayList<FudaoquanModel>();
		fudaoquanApi = new FudaoquanAPI();
		mAdapter = new FudaoquanCommonAdapter(this, fudaoquanList);
		if ("choice_tag_question".equals(tag)) {
			mAdapter.setFalg(false);
		} else if ("choice_tag_homework".equals(tag)) {
			mAdapter.setFalg(false);
		} else {
			mAdapter.setFalg(true);
		}
		lv.setAdapter(mAdapter);

	}

	@Override
	public void initListener() {
		super.initListener();
		back_layout.setOnClickListener(this);
		btn_go_getfudaoquan.setOnClickListener(this);
		lv.setOnItemClickListener(new MyOnItemClickListener());
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout:// 返回
			finish();
			break;
		case R.id.btn_go_getfudaoquan:
//		    UserInfoModel userinfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
//		    String codeUrl="";            
//            String  baseUrl=AppConfig.FUDAOTUAN_URL+"/task.html";
//            if (baseUrl.contains("fudaotuan.com")) {//如果是大熊作业的网址
//                if (baseUrl.contains("?")) {
//                      codeUrl=baseUrl+"&userid={0}&phoneos={1}&appversion={2}";
//                      codeUrl=codeUrl.replace("{0}", userinfo.getUserid()+"").replace("{1}", "android").replace("{2}", PackageManagerUtils.getVersionCode()+"");               
//                     
//                }else {
//                  codeUrl=baseUrl+"?userid={0}&phoneos={1}&appversion={2}";
//                  codeUrl=codeUrl.replace("{0}", userinfo.getUserid()+"").replace("{1}", "android").replace("{2}", PackageManagerUtils.getVersionCode()+"");  
//                }
//                Intent intent = new Intent(this, WebViewActivity.class);
//                intent.putExtra("title", "大熊作业");
//                intent.putExtra("url", codeUrl);
//                startActivity(intent);
//                
//            }else {
//                Toast.makeText(this, "网址不是火焰作业的网址", 4).show();
//            }       
			callPhone();
			break;

		default:
			break;
		}
	}

	public void initData() {
		if ("choice_tag_question".equals(tag)) {// 选择难题答疑辅导券
			fudaoquanApi.getFudaoquanList(requestQueue, 1, this, RequestConstant.GET_QUESTION_QUAN_CODE);

		} else if ("choice_tag_homework".equals(tag)) {// 选择作业检查辅导券
			fudaoquanApi.getFudaoquanList(requestQueue, 2, this, RequestConstant.GET_HOMEWORK_QUAN_CODE);
		} else {// 过期辅导券
			showDialog("加载中...");
			fudaoquanApi.getExpireFudaoquan(requestQueue, -1, this, RequestConstant.GET_EXPRIE_FUDAOQUAN_LIST_CODE);
		}

	}

	@Override
	public void resultBack(Object... param) {
		super.resultBack(param);
		int flag = ((Integer) param[0]).intValue();
		switch (flag) {
		case RequestConstant.GET_EXPRIE_FUDAOQUAN_LIST_CODE:// 获取过期辅导券列表
			if (param.length > 0 && param[1] != null && param[1] instanceof String) {
				String datas = param[1].toString();
				int code = JsonUtil.getInt(datas, "Code", -1);
				String msg = JsonUtil.getString(datas, "Msg", "");
				if (code == 0) {
					try {
						String dataJson = JsonUtil.getString(datas, "Data", "");
						closeDialog();
						if (!TextUtils.isEmpty(dataJson)) {
							List<FudaoquanModel> subList = new Gson().fromJson(dataJson,
									new TypeToken<List<FudaoquanModel>>() {
									}.getType());

							fudaoquanList.addAll(subList);
							mAdapter.notifyDataSetChanged();
							if (fudaoquanList.size() > 0) {
								empty_view.setVisibility(View.GONE);
							} else {
								empty_view.setVisibility(View.VISIBLE);
								lv.setEmptyView(empty_view);

							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					ToastUtils.show(msg);
				}

			}
			break;

		case RequestConstant.GET_QUESTION_QUAN_CODE:// 获取难题辅导券
			if (param.length > 0 && param[1] != null && param[1] instanceof String) {
				String datas = param[1].toString();
				int code = JsonUtil.getInt(datas, "Code", -1);
				String msg = JsonUtil.getString(datas, "Msg", "");
				if (code == 0) {
					try {
						String dataJson = JsonUtil.getString(datas, "Data", "");
						closeDialog();
						if (!TextUtils.isEmpty(dataJson)) {
							List<FudaoquanModel> subList = new Gson().fromJson(dataJson,
									new TypeToken<List<FudaoquanModel>>() {
									}.getType());

							fudaoquanList.addAll(subList);
							mAdapter.notifyDataSetChanged();
							if (fudaoquanList.size() > 0) {
								empty_view.setVisibility(View.GONE);
							} else {
								empty_view.setVisibility(View.VISIBLE);
								lv.setEmptyView(empty_view);

							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					ToastUtils.show(msg);
				}

			}
			break;

		case RequestConstant.GET_HOMEWORK_QUAN_CODE:// 获取作业辅导券
			if (param.length > 0 && param[1] != null && param[1] instanceof String) {
				String datas = param[1].toString();
				int code = JsonUtil.getInt(datas, "Code", -1);
				String msg = JsonUtil.getString(datas, "Msg", "");
				if (code == 0) {
					try {
						String dataJson = JsonUtil.getString(datas, "Data", "");
						closeDialog();
						if (!TextUtils.isEmpty(dataJson)) {
							List<FudaoquanModel> subList = new Gson().fromJson(dataJson,
									new TypeToken<List<FudaoquanModel>>() {
									}.getType());

							fudaoquanList.addAll(subList);
							mAdapter.notifyDataSetChanged();
							if (fudaoquanList.size() > 0) {
								empty_view.setVisibility(View.GONE);
							} else {
								empty_view.setVisibility(View.VISIBLE);
								lv.setEmptyView(empty_view);

							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					ToastUtils.show(msg);
				}

			}
			break;

		}

	}

	class MyOnItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if ("choice_tag_homework".equals(tag) || "choice_tag_question".equals(tag)) {
				FudaoquanModel model = fudaoquanList.get(position);
				if (model != null) {
					Intent i = new Intent();
					i.putExtra("fudaoquanmodel", model);
					setResult(RESULT_OK, i);
					finish();
				}
			}

		}

	}
	
	/**
	 * 直接打电话
	 */
	private void callPhone() {
		// 用intent启动拨打电话
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "400-6755-222"));
		startActivity(intent);
		this.finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (fudaoquanApi != null) {
			fudaoquanApi = null;
		}
	}

}
