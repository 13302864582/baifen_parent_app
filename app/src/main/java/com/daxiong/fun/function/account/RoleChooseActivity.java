package com.daxiong.fun.function.account;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.dialog.CustomCommonTipDialog;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * 此类的描述：角色选择
 * 
 * @author: Sky @最后修改人： Sky
 * @最后修改日期:2016年6月14日 下午8:48:59
 */
public class RoleChooseActivity extends BaseActivity {

	private CheckBox rb_jiazhang;
	private TextView tv_jiazhang_title;
	private CheckBox rb_student;
	private TextView tv_student_title;
	private RelativeLayout layout_back;
	
	
	private RelativeLayout rl_jiazhang;
	private RelativeLayout rl_student;
	private LinearLayout ll_jiazhang;
	private LinearLayout ll_student;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.role_choose_activity);		
		initView();
		initListener();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void initView() {
		super.initView();
		setWelearnTitle("选择角色");
		layout_back = (RelativeLayout) this.findViewById(R.id.back_layout);
		rl_jiazhang=(RelativeLayout) this.findViewById(R.id.rl_jiazhang);
		rl_student=(RelativeLayout) this.findViewById(R.id.rl_student);
		
		ll_jiazhang=(LinearLayout) this.findViewById(R.id.ll_jiazhang);
		ll_student=(LinearLayout) this.findViewById(R.id.ll_student);
		rb_jiazhang = (CheckBox) this.findViewById(R.id.cb_jiazhang);
		tv_jiazhang_title = (TextView) this.findViewById(R.id.tv_jiazhang_title);
		rb_student = (CheckBox) this.findViewById(R.id.cb_student);
		tv_student_title = (TextView) this.findViewById(R.id.tv_student_title);
	}

	@Override
	public void initListener() {
		super.initListener();
		layout_back.setOnClickListener(this);
		rl_jiazhang.setOnClickListener(this);
		rl_student.setOnClickListener(this);
		
//		rb_jiazhang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				if (isChecked) {
//					rb_jiazhang.setChecked(true);
//					rb_student.setChecked(false);
//					rb_jiazhang.setBackgroundResource(R.drawable.ic_role_checked);
//					tv_jiazhang_title.setTextColor(Color.parseColor("#000000"));
//					rb_student.setBackgroundResource(R.drawable.ic_role_normal);
//					tv_student_title.setTextColor(Color.parseColor("#8d8d8d"));
//
//					final CustomCommonTipDialog dialog = new CustomCommonTipDialog(RoleChooseActivity.this, "",
//							getString(R.string.choose_role_dialog_parent_txt), "重新选择", "确定选择");
//					dialog.show();
//					dialog.setMyDialogClickListenerLister(new CustomCommonTipDialog.ImyDialogClickListenerLister() {
//						@Override
//						public void doConfirm() {
//							dialog.dismiss();
//							JSONObject jsonObject = new JSONObject();
//							try {
//								// 角色值 1学生 3家长
//								jsonObject.put("role", 3);
//							} catch (JSONException e) {
//								e.printStackTrace();
//							}
//							showDialog("数据加载中...");
//							OkHttpHelper.post(RoleChooseActivity.this, "parents", "setrole", jsonObject,
//									new HttpListener() {
//										@Override
//										public void onSuccess(int code, String dataJson, String errMsg) {
//											closeDialog();
//											Intent intentx=new Intent("com.action.finish_myself");
//											sendBroadcast(intentx);
//											IntentManager.goToMainView(RoleChooseActivity.this);
//										}
//
//										@Override
//										public void onFail(int HttpCode, String errMsg) {
//											closeDialog();
//											if (!TextUtils.isEmpty(errMsg)) {
//												ToastUtils.show(errMsg);
//											} else {
//												ToastUtils.show("服务器异常");
//											}
//
//										}
//									});
//						}
//
//						@Override
//						public void doCancel() {
//							dialog.dismiss();
//
//						}
//					});
//				}
//
//			}
//		});
//
//		rb_student.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				if (isChecked) {
//					rb_jiazhang.setChecked(false);
//					rb_student.setChecked(true);
//					rb_student.setBackgroundResource(R.drawable.ic_role_checked);
//					tv_student_title.setTextColor(Color.parseColor("#000000"));
//					rb_jiazhang.setBackgroundResource(R.drawable.ic_role_normal);
//					tv_jiazhang_title.setTextColor(Color.parseColor("#8d8d8d"));
//
//					final CustomCommonTipDialog dialog = new CustomCommonTipDialog(RoleChooseActivity.this, "",
//							getString(R.string.choose_role_dialog_student_txt), "重新选择", "确定选择");
//					dialog.show();
//					dialog.setMyDialogClickListenerLister(new CustomCommonTipDialog.ImyDialogClickListenerLister() {
//						@Override
//						public void doConfirm() {
//							dialog.dismiss();
//							JSONObject jsonObject = new JSONObject();
//							try {
//								// 角色值 1学生 3家长
//								jsonObject.put("role", 1);
//							} catch (JSONException e) {
//								e.printStackTrace();
//							}
//							showDialog("数据加载中...");
//							OkHttpHelper.post(RoleChooseActivity.this, "parents", "setrole", jsonObject,
//									new HttpListener() {
//
//										@Override
//										public void onSuccess(int code, String dataJson, String errMsg) {
//											closeDialog();
//											Intent intentx=new Intent("com.action.finish_myself");
//											sendBroadcast(intentx);
//											IntentManager.goToMainView(RoleChooseActivity.this);
//											
//										}
//
//										@Override
//										public void onFail(int HttpCode, String errMsg) {
//											closeDialog();
//											if (!TextUtils.isEmpty(errMsg)) {
//												ToastUtils.show(errMsg);
//											} else {
//												ToastUtils.show("服务器异常");
//											}
//
//										}
//									});
//
//						}
//
//						@Override
//						public void doCancel() {
//							dialog.dismiss();
//
//						}
//					});
//
//				}
//
//			}
//		});
	}

	@Override
	public void onClick(View v) {   
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout:
			finish();
			break;
		case R.id.rl_jiazhang:

			rb_jiazhang.setChecked(true);
			rb_student.setChecked(false);
			rb_jiazhang.setBackgroundResource(R.drawable.ic_role_checked);
			tv_jiazhang_title.setTextColor(Color.parseColor("#000000"));
			rb_student.setBackgroundResource(R.drawable.ic_role_normal);
			tv_student_title.setTextColor(Color.parseColor("#8d8d8d"));

			final CustomCommonTipDialog dialog = new CustomCommonTipDialog(RoleChooseActivity.this, "",
					getString(R.string.choose_role_dialog_parent_txt), "重新选择", "确定选择");
			dialog.show();
			dialog.setMyDialogClickListenerLister(new CustomCommonTipDialog.ImyDialogClickListenerLister() {
				@Override
				public void doConfirm() {
					dialog.dismiss();
					JSONObject jsonObject = new JSONObject();
					try {
						// 角色值 1学生 3家长
						jsonObject.put("role", 3);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					showDialog("数据加载中...");
					OkHttpHelper.post(RoleChooseActivity.this, "parents", "setrole", jsonObject,
							new HttpListener() {
								@Override
								public void onSuccess(int code, String dataJson, String errMsg) {
									closeDialog();
									Intent intentx=new Intent("com.action.finish_myself");
									sendBroadcast(intentx);
									IntentManager.goToMainView(RoleChooseActivity.this);
								}

								@Override
								public void onFail(int HttpCode, String errMsg) {
									closeDialog();
									if (!TextUtils.isEmpty(errMsg)) {
										ToastUtils.show(errMsg);
									} else {
										ToastUtils.show("服务器异常");
									}

								}
							});
				}

				@Override
				public void doCancel() {
					dialog.dismiss();

				}
			});
		
			break;
		case R.id.rl_student:

			rb_jiazhang.setChecked(false);
			rb_student.setChecked(true);
			rb_student.setBackgroundResource(R.drawable.ic_role_checked);
			tv_student_title.setTextColor(Color.parseColor("#000000"));
			rb_jiazhang.setBackgroundResource(R.drawable.ic_role_normal);
			tv_jiazhang_title.setTextColor(Color.parseColor("#8d8d8d"));

			final CustomCommonTipDialog dialogx = new CustomCommonTipDialog(RoleChooseActivity.this, "",
					getString(R.string.choose_role_dialog_student_txt), "重新选择", "确定选择");
			dialogx.show();
			dialogx.setMyDialogClickListenerLister(new CustomCommonTipDialog.ImyDialogClickListenerLister() {
				@Override
				public void doConfirm() {
					dialogx.dismiss();
					JSONObject jsonObject = new JSONObject();
					try {
						// 角色值 1学生 3家长
						jsonObject.put("role", 1);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					showDialog("数据加载中...");
					OkHttpHelper.post(RoleChooseActivity.this, "parents", "setrole", jsonObject,
							new HttpListener() {

								@Override
								public void onSuccess(int code, String dataJson, String errMsg) {
									closeDialog();
									Intent intentx=new Intent("com.action.finish_myself");
									sendBroadcast(intentx);
									IntentManager.goToMainView(RoleChooseActivity.this);
									
								}

								@Override
								public void onFail(int HttpCode, String errMsg) {
									closeDialog();
									if (!TextUtils.isEmpty(errMsg)) {
										ToastUtils.show(errMsg);
									} else {
										ToastUtils.show("服务器异常");
									}

								}
							});

				}

				@Override
				public void doCancel() {
					dialogx.dismiss();

				}
			});
		
		break;

		}
	}



	@Override
	protected void onDestroy() {
		super.onDestroy();		
		
	}

}
