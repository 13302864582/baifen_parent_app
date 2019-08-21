package com.daxiong.fun.function.cram;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.api.StudyAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.base.ImageLoader;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.function.cram.fragment.EduFragment;
import com.daxiong.fun.function.cram.holder.CramSchoolDetailsModel;
import com.daxiong.fun.function.cram.view.PromoteDialog;
import com.daxiong.fun.function.homework.PublishHomeWorkActivity;
import com.daxiong.fun.function.question.PayAnswerAskActivity;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.HomeworkManager;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.manager.QuestionManager;
import com.daxiong.fun.model.MyOrgModel;
import com.daxiong.fun.model.OrgModel;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.NetworkUtils;
import com.daxiong.fun.util.StirngUtil;
import com.daxiong.fun.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 补习班主页详情介绍
 */
public class CramSchoolDetailsActivity extends BaseActivity implements OnClickListener, HttpListener, OnTouchListener {

	private static final String TAG = CramSchoolDetailsActivity.class.getSimpleName();
	/** 我的补习班 (会员)*/
	public static final int TYPE_CRAMSHCOOL = 2;
	/** 机构 (关注) */
	public static final int TYPE_EDU = 1;
	/** (未关注) */
	public static final int TYPE_NORMAL = 0;
    private static final int REQUEST_MY_ORGS_HOMEWORK = 231;

	private int orgid;
	/** 身份类型, 是会员 还是关注 */
	public int type = -1;

	private TextView step_btn;
	private ImageView step_img;
	private NetworkImageView detail_icon;
	private TextView detail_title;
	private TextView detail_details;
	private TextView detail_switch;
	private TextView detail_address;
	private TextView detail_phonenumber;

	public int avatarSize;
	private boolean switch_state;

	/** 是否关注 */
	private boolean isFollow = true;
	private PopupWindow mPopupWindow;
	private String orgname;
	
	
	private StudyAPI studyApi;
	private HomeworkManager homeworkManager;
	private QuestionManager questionManager;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initObject();
		initView();
		setWelearnTitle("");

		Intent intent = getIntent();
		if (intent != null) {
			// TODO 页面间传递type值, 不可取. type = intent.getIntExtra("type", -1);
			orgid = intent.getIntExtra("orgid", -1);
		}
		
		if(type == -1){ //自己去获取和机构关系
			JSONObject json = new JSONObject();
			try {
				json.put("orgid", orgid);
			} catch (JSONException e) {
				LogUtils.e(TAG, "Json： ", e);
			}
			OkHttpHelper.post(this, "org","getrelation", json, new HttpListener() {
				
				@Override
				public void onSuccess(int code, String dataJson, String errMsg) {
					if (!TextUtils.isEmpty(dataJson)) {
						try {
							JSONObject jsonObject = new JSONObject(dataJson);
							type = jsonObject.getInt("relationtype"); // 0未关注 1关注 2成员
							requestData();
						} catch (JSONException e) {
							LogUtils.e(TAG, "Json： ", e);
						}
					}
				
					
				}
				
				@Override
				public void onFail(int HttpCode,String errMsg) {					
					
				}
			});
		}else{
			requestData();
		}
	}
	
	public void initObject(){
	    studyApi=new StudyAPI();
	    homeworkManager=HomeworkManager.getInstance();
	    questionManager=QuestionManager.getInstance();
	}

	public void initView() {
		avatarSize = MyApplication.getContext().getResources().getDimensionPixelSize(R.dimen.cramschool_detail_icon);
		setContentView(R.layout.activity_cramschool_details);
		findViewById(R.id.back_layout).setOnClickListener(this);
		findViewById(R.id.back_layout).setOnClickListener(this);
		findViewById(R.id.next_setp_layout).setOnClickListener(this);
		step_btn = (TextView) findViewById(R.id.next_step_btn);
		step_btn.setBackgroundResource(R.drawable.publish_btn_selector);
		step_btn.setText(getResources().getString(R.string.unfollow)); // 已关注

		step_img = (ImageView) findViewById(R.id.next_step_img);
		step_img.setImageResource(R.drawable.class_nav_more);

		detail_icon = (NetworkImageView) findViewById(R.id.cramschool_detail_icon);
		detail_title = (TextView) findViewById(R.id.cramschool_detail_title);
		detail_details = (TextView) findViewById(R.id.cramschool_detail_details);
		detail_switch = (TextView) findViewById(R.id.cramschool_detail_switch);
		detail_address = (TextView) findViewById(R.id.cramschool_detail_address);
		detail_phonenumber = (TextView) findViewById(R.id.cramschool_detail_phonenumber);

		findViewById(R.id.cramschool_detail_fengcai).setOnClickListener(this);
		findViewById(R.id.cramschool_detail_jingpin).setOnClickListener(this);
		detail_switch.setOnClickListener(this);
		findViewById(R.id.cramschooldetail_sv).setOnTouchListener(this);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void refreshView(CramSchoolDetailsModel data) {
		orgname = data.getOrgname();
		setWelearnTitle(orgname);
		ImageLoader.getInstance().loadImage(data.getLogo(), detail_icon, R.drawable.ic_default_avatar, avatarSize,
				avatarSize / 10);
		detail_title.setText(data.getOrgname());
		detail_details.setText(data.getInfo());
		detail_details.post(new Runnable() {
			
			@Override
			public void run() {
				int lineCount = detail_details.getLineCount();
				if(lineCount >= 2){
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							detail_switch.setVisibility(View.VISIBLE);
						}
					});
				}
			}
		});
		
		detail_address.setText(StirngUtil.format(R.string.cramschool_detail_address, data.getAddress()));
		detail_phonenumber.setText(StirngUtil.format(R.string.cramschool_detail_phonenumber, data.getTel()));

		if (type == TYPE_CRAMSHCOOL) {
			step_btn.setVisibility(View.GONE);
			step_img.setVisibility(View.VISIBLE);
		} else if (type == TYPE_EDU) {
			step_btn.setVisibility(View.VISIBLE);
			step_img.setVisibility(View.GONE);
			step_btn.setText(getResources().getString(R.string.unfollow));
			isFollow = true;
		}else{
			step_btn.setVisibility(View.VISIBLE);
			step_img.setVisibility(View.GONE);
			step_btn.setText(getResources().getString(R.string.follow_edu));
			isFollow = false;
		}
	}

	@Override
	public void onClick(View v) {
		boolean isBeforeShowing = false; //之前是不是显示的
		if(mPopupWindow != null && mPopupWindow.isShowing()){
			mPopupWindow.dismiss();
			isBeforeShowing = true;
		}
		switch (v.getId()) {
		case R.id.back_layout: // 返回
			finish();
			break;
		case R.id.next_setp_layout:
			if (type == TYPE_CRAMSHCOOL) {
				// TODO PopupWindow 弹窗
				initPopuptWindow();
				if(!isBeforeShowing){
					mPopupWindow.showAsDropDown(v);
				}
			} else{
				changeFollow();
			}
			break;
		case R.id.cramschool_detail_switch: // 全文、收起
			if (!switch_state) {
				// 展开
				detail_switch.setText(getResources().getString(R.string.cramschool_switch_on));
				detail_details.setMaxLines(Integer.MAX_VALUE);
			} else {
				detail_switch.setText(getResources().getString(R.string.cramschool_switch_off));
				detail_details.setMaxLines(2);
			}
			switch_state = !switch_state;
			break;
		case R.id.cramschool_detail_fengcai: // TODO (已完成)名师风采
			Bundle data = new Bundle();
			data.putInt("orgid", orgid);
			IntentManager.gotoFamousTeacherListActivity(this, data);
			break;
		case R.id.cramschool_detail_jingpin: // TODO (已完成)精品讲义
			if(type == TYPE_EDU && !isFollow){
				PromoteDialog dialog = new PromoteDialog(this);
				dialog.show();
			}else{
				Bundle bundle = new Bundle();
				bundle.putInt("orgid", orgid);
				bundle.putString("orgname", orgname);
				IntentManager.goToGoodsNotesActivity(this, bundle);
			}
			break;
		case R.id.menu_answer_questions: // TODO (已完成)难题答疑
//			IntentManager.goToPayAnswerAskVipActivity(this, orgname, orgid, null);
		    requestMyOrgsQuestion();
			break;
		case R.id.menu_work_examine: // TODO (已完成)作业检查
//			IntentManager.goToStuPublishHomeWorkVipActivity(this, orgname, orgid, null);
		    requestMyOrgsHomework();
			break;
		}
	}

	/** 初始化PopuptWindow */
	private void initPopuptWindow() {
		if (null == mPopupWindow) {
			View view = View.inflate(getApplication(), R.layout.popup_cramschool_menu, null);
			view.findViewById(R.id.menu_answer_questions).setOnClickListener(this);
			view.findViewById(R.id.menu_work_examine).setOnClickListener(this);
			view.measure(0, 0);
			mPopupWindow = new PopupWindow(view, view.getMeasuredWidth(), view.getMeasuredHeight());
		}
	}

	/** 更改关注状态 */
	private void changeFollow() {
		JSONObject json = new JSONObject();
		try {
			json.put("orgid", orgid);
		} catch (JSONException e1) {
			LogUtils.e(TAG, "Json： ", e1);
		}
		OkHttpHelper.post(this, "org","doattention",  json, new HttpListener() {
			
			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				isFollow = !isFollow;
				if (isFollow) { // 已关注
					showToast(getResources().getString(R.string.follow_edu_hint));
					step_btn.setText(getResources().getString(R.string.unfollow));
				} else {
					showToast(getResources().getString(R.string.unfollow_hint));
					step_btn.setText(getResources().getString(R.string.follow_edu));
				}
				EduFragment.isNeedRefresh = true;
			
				
			}
			
			@Override
			public void onFail(int HttpCode,String errMsg) {
				
				
			}
		});
	}

	private void requestData() {
		/*
		 * "orgid":获取的辅导机构ID
		 */
		JSONObject json = new JSONObject();
		try {
			json.put("orgid", orgid);
		} catch (JSONException e1) {
			LogUtils.e(TAG, "Json： ", e1);
		}
		OkHttpHelper.post(this, "org","getorginfo", json, this);
	}

	
	
	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (!TextUtils.isEmpty(dataJson)) {
			CramSchoolDetailsModel data = null;
			try {
				data = new Gson().fromJson(dataJson, new TypeToken<CramSchoolDetailsModel>() {
				}.getType());
			} catch (Exception e) {
				LogUtils.e(TAG, "数据请求失败！", e);
			}
			if (data != null) {
			//	initView();
				refreshView(data);
			}
		}
	
		
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {
		
		
	}

	private void showToast(String str) {
		RelativeLayout layout = new RelativeLayout(getApplication());
		layout.setBackgroundResource(R.drawable.concerned_bg);

		TextView textView = new TextView(getApplication());
		textView.setText(str);
		textView.setTextColor(Color.WHITE);
		textView.setTextSize(19);
//		int left = DensityUtil.dip2px(getApplication(), 49);
//		int top = DensityUtil.dip2px(getApplication(), 37);
//		int right = DensityUtil.dip2px(getApplication(), 49);
//		int bottom = DensityUtil.dip2px(getApplication(), 37);
//		textView.setPadding(left, top, right, bottom);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		layout.addView(textView, params);

		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(mPopupWindow != null && mPopupWindow.isShowing()){
			mPopupWindow.dismiss();
		}
		return false;
	}
	
	
    private void requestMyOrgsQuestion() {
        if (NetworkUtils.getInstance().isInternetConnected(this)) {
         // 请求我的机构
            studyApi.queryMyOrgs(requestQueue, 1, 1, 1000, this, RequestConstant.REQUEST_MY_ORGS);
        }else {
            ToastUtils.show("网络连接不可用，请检查网络");            
        }
        
    }
    
    private  void requestMyOrgsHomework(){
        studyApi.queryMyOrgs(requestQueue, 1, 1, 1000, this, REQUEST_MY_ORGS_HOMEWORK);
    }
    
    
    
    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        int flag = ((Integer)param[0]).intValue();
        switch (flag) {           
            case RequestConstant.REQUEST_MY_ORGS:// 查询我的机构
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        try {
                            String dataJson = JsonUtil.getString(datas, "Data", "");
                            if (!TextUtils.isEmpty(dataJson)) {
                                MyOrgModel myOrgModel = homeworkManager.parseJsonForMyOrg(dataJson);
                                ArrayList<OrgModel> listModels = myOrgModel.getOrgList();
                                boolean isWaibao = homeworkManager.compareValueIsWaibao(listModels);
                                // 如果是外包
                                if (isWaibao) {

                                    if (myOrgModel.getSpecialuser() != null
                                            && myOrgModel.getSpecialuser().size() > 0) {
                                        int type = myOrgModel.getSpecialuser().get(0).getType();// 表示是否是特殊学生
                                        if (type == 1) {// 是特殊学生

                                            if (listModels != null && listModels.size() > 0) {
                                                MySharePerfenceUtil.getInstance().setOrgVip();
                                                questionManager.goToOutsouringQuestionActivity(
                                                        this, "", 0, listModels, type);
                                            } else {
                                                MySharePerfenceUtil.getInstance().setNotOrgVip();
                                            }

                                        }

                                        if (type == 0) {// 不是特殊学生
                                            homeworkManager.isVipOrg(this, dataJson);
                                            questionManager.goToOutsouringQuestionActivity(
                                                    this, "", 0, listModels, type);
                                        }
                                    } else {// 外包不是特殊学生
                                        homeworkManager.isVipOrg(this, dataJson);
                                        questionManager.goToOutsouringQuestionActivity(
                                                this, "", 0, listModels, 0);
                                    }

                                } else {// 如果不是外包

                                    if (listModels != null && listModels.size() > 0) {// vip
                                        homeworkManager.isVipOrg(this, dataJson);
                                        questionManager.getInstance()
                                                .goToStuPublishQuestionVipActivity(this,
                                                        "", 0, listModels);

                                    } else {// not vip
                                        homeworkManager.isVipOrg(this, dataJson);
                                        questionManager.goNotPublishQuestionVipActivity(
                                                this, PayAnswerAskActivity.class, null,
                                                false);
                                    }

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
                
            case REQUEST_MY_ORGS_HOMEWORK:// 查询我的机构_作业
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        try {
                            String dataJson = JsonUtil.getString(datas, "Data", "");
                            if (!TextUtils.isEmpty(dataJson)) {
                                MyOrgModel myOrgModel = homeworkManager.parseJsonForMyOrg(dataJson);
                                ArrayList<OrgModel> listModels=myOrgModel.getOrgList();
                                boolean isWaibao=homeworkManager.compareValueIsWaibao(listModels);
                                //如果是外包
                                if (isWaibao) {
                                                                
                                    
                                    
                                    if (myOrgModel.getSpecialuser() != null&& myOrgModel.getSpecialuser().size() > 0) {                                        
                                        int type = myOrgModel.getSpecialuser().get(0).getType();// 表示是否是特殊学生
                                        if (type == 1) {// 是特殊学生                                            
                                            
                                            if (listModels != null && listModels.size() > 0) {
                                                MySharePerfenceUtil.getInstance().setOrgVip();
                                                homeworkManager.goToOutsouringHomeWorkActivity(
                                                        this, "", 0, listModels,type); 
                                            } else {
                                                MySharePerfenceUtil.getInstance().setNotOrgVip();
                                            }

                                        } 
                                        
                                        if(type==0) {// 不是特殊学生
                                            homeworkManager.isVipOrg(this, dataJson);
                                            homeworkManager.goToOutsouringHomeWorkActivity(
                                                    this, "", 0, listModels,type); 
                                        }
                                    }else {//外包非特殊学生
                                        homeworkManager.isVipOrg(this, dataJson);
                                        homeworkManager.goToOutsouringHomeWorkActivity(
                                                this, "", 0, listModels,0); 
                                    }                                     
                                    
                                }else {//如果不是外包
                                    
                                    if (listModels!=null&&listModels.size()>0) {//vip
                                        homeworkManager.isVipOrg(this, dataJson);
                                        IntentManager.goToStuPublishHomeWorkVipActivity(this, "", 0, listModels);
                                        
                                    }else {//not vip
                                        homeworkManager.isVipOrg(this, dataJson);
                                        homeworkManager.goNotHomeworkVipActivity(this,
                                                PublishHomeWorkActivity.class, null, false);
                                    }
                                    
                                   
                                }   
                                
                                
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                       ToastUtils.show(msg); 
                       
                    }

                }
                break;

        }

    }


    
}
