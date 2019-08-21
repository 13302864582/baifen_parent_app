package com.daxiong.fun.function.communicate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.R;
import com.daxiong.fun.common.AuthActivity;
import com.daxiong.fun.common.WebViewActivity;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.constant.MessageConstant;
import com.daxiong.fun.constant.HomeworkStatusConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.function.AnswerListItemView;
import com.daxiong.fun.function.course.MastersCourseActivity;
import com.daxiong.fun.function.homework.model.HomeWorkCheckPointModel;
import com.daxiong.fun.function.homework.model.MsgData;
import com.daxiong.fun.function.question.OneQuestionMoreAnswersDetailActivity;
import com.daxiong.fun.function.question.PayAnswerQuestionDetailActivity;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.ChatInfo;
import com.daxiong.fun.model.FitBitmap;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.DateUtil;
import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.util.ImageUtil;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.view.CropCircleTransformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * 接收消息的view
 * 
 * @author parsonswang
 * 
 */
public class ChatMsgRecvView extends AbstractMsgChatItemView implements OnClickListener {

	private TextView mSendTime;
	private ImageView mAvatar;
	private TextView mMsgContent;
	private FrameLayout mRecvMsgContainer;
	private ImageView mRecvMsgPlay;
	private TextView mRecvMsgTime;
	private String mAudioPath;
	private ChatInfo mChatInfo;
	private ImageView mChatRecvImg;
	private FrameLayout mChatRecvImgContainer;
//	private Context mActivity;
	private ChatMsgViewActivity mActivity;
	private int avatarSize;
	private ImageView mRedPointIv;
	private int fromuser;
	public ChatMsgRecvView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mActivity = (ChatMsgViewActivity) context;
	}

	public ChatMsgRecvView(Context context) {
		super(context);
		this.mActivity = (ChatMsgViewActivity) context;
	}

	@SuppressLint("InflateParams")
	public void setupViews(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.chat_msg_recv_view, null);
		mSendTime = (TextView) view.findViewById(R.id.to_user_send_time);
		mMsgContent = (TextView) view.findViewById(R.id.recv_msg_content);
		mAvatar = (ImageView) view.findViewById(R.id.to_user_avatar);
		avatarSize = getResources().getDimensionPixelSize(R.dimen.msg_list_avatar_size);
		mRecvMsgContainer = (FrameLayout) view.findViewById(R.id.voice_recv_msg_container);
		mRedPointIv = (ImageView) view.findViewById(R.id.red_point_iv_chat_rev);
		mRecvMsgPlay = (ImageView) view.findViewById(R.id.ic_voice_recv_msg_play);
		mRecvMsgTime = (TextView) view.findViewById(R.id.recv_msg_audiotime);
		mChatRecvImg = (ImageView) view.findViewById(R.id.recv_msg_img);
		mChatRecvImgContainer = (FrameLayout) view.findViewById(R.id.recv_msg_img_container);

		mAvatar.setOnClickListener(this);
		mMsgContent.setOnClickListener(this);
		mRecvMsgContainer.setOnClickListener(this);
		mChatRecvImgContainer.setOnClickListener(this);

		addView(view);
	}

	public void showData(ChatInfo info, List<ChatInfo> chatinfos, final int position) {
		mChatInfo = info;
		mRedPointIv.setVisibility(View.GONE);
		long timestamp = DateUtil.convertTimestampToLong((float) info.getTimestamp() * 1000);
		long preMsgTimestamp = getPreMsgTimestamp(chatinfos, position - 1);
		boolean isAfter = DateUtil.isAfter(timestamp, preMsgTimestamp);
		if (isAfter) {
			mSendTime.setVisibility(View.VISIBLE);
		} else {
			mSendTime.setVisibility(View.GONE);
		}
		mSendTime.setText(DateUtil.getDisplayChatTime(new Date(timestamp)));
		UserInfoModel ug = info.getUser();
		int defaultOrErrorAvatarId = R.drawable.ic_default_avatar;
		
		fromuser = info.getFromuser();
		if (ug != null) {
			switch (fromuser) {
			case GlobalContant.USER_ID_SYSTEM:
				defaultOrErrorAvatarId = R.drawable.ic_system_avatar;
				break;
			case GlobalContant.USER_ID_HELPER:
				defaultOrErrorAvatarId = R.drawable.ic_solve_helper_avatar;
				break;
			}
			
			Glide.with(mActivity).load(ug.getAvatar_100()).placeholder(R.drawable.default_icon_circle_avatar)

			.bitmapTransform(new CropCircleTransformation(mActivity)).diskCacheStrategy(DiskCacheStrategy.ALL).into(mAvatar);
		} else {
			switch (fromuser) {
			case GlobalContant.USER_ID_SYSTEM:
				defaultOrErrorAvatarId = R.drawable.ic_system_avatar;
				break;
			case GlobalContant.USER_ID_HELPER:
				defaultOrErrorAvatarId = R.drawable.ic_solve_helper_avatar;
				break;
			}
			Glide.with(mActivity).load(R.drawable.default_contact_image)
			.bitmapTransform(new CropCircleTransformation(mActivity)).diskCacheStrategy(DiskCacheStrategy.ALL).into(mAvatar);
		}
		// 如果是文字消息或者系统消息
		if (info.getContenttype() == MessageConstant.MSG_CONTENT_TYPE_TEXT
				|| info.getContenttype() == MessageConstant.MSG_CONTENT_TYPE_JUMP
				|| info.getContenttype() == MessageConstant.MSG_CONTENT_TYPE_JUMP_URL) {
			if (info.getContenttype() == MessageConstant.MSG_CONTENT_TYPE_JUMP
					|| info.getContenttype() == MessageConstant.MSG_CONTENT_TYPE_JUMP_URL) {
				//mMsgContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.bg_jump_msg, 0);
				mMsgContent.setBackgroundResource(R.drawable.chat_rev_homework_selector);
				boolean isReaded = info.isReaded();
				if (!isReaded) {
					mRedPointIv.setVisibility(View.VISIBLE);
				}
			} else {
				mMsgContent.setBackgroundResource(R.drawable.information_dialogteacher_icon);
				//mMsgContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			}

			mMsgContent.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					if (GlobalVariable.mChatMsgViewActivity != null) {
						GlobalVariable.mChatMsgViewActivity.showPop(mMsgContent, position);
					}
					return true;
				}
			});

			mMsgContent.setVisibility(View.VISIBLE);
			mRecvMsgContainer.setVisibility(View.GONE);
			mRecvMsgTime.setVisibility(View.GONE);
			mChatRecvImgContainer.setVisibility(View.GONE);

			mMsgContent.setText(info.getMsgcontent());
		} else if (info.getContenttype() == MessageConstant.MSG_CONTENT_TYPE_AUDIO) {
			mAudioPath = info.getPath();
			if (TextUtils.isEmpty(mAudioPath)) {
				return;
			}
			boolean isReaded = info.isReaded();
			if (!isReaded) {
				mRedPointIv.setVisibility(View.VISIBLE);
			}
			mMsgContent.setVisibility(View.GONE);
			mRecvMsgContainer.setVisibility(View.VISIBLE);
			mRecvMsgTime.setVisibility(View.VISIBLE);
			mChatRecvImgContainer.setVisibility(View.GONE);
			mRecvMsgContainer.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					if (GlobalVariable.mChatMsgViewActivity != null) {
						GlobalVariable.mChatMsgViewActivity.showVodPop(mRecvMsgContainer, position);
					}
					return true;
				}
			});
			int audiotime = info.getAudiotime();
			if (audiotime != 0) {
				mRecvMsgTime.setText(audiotime + "s");
				
				switch (audiotime) {
				case 1:
					mRecvMsgContainer.setMinimumWidth(DensityUtil.dip2px(mActivity, 60));
					break;
				case 2:
					mRecvMsgContainer.setMinimumWidth(DensityUtil.dip2px(mActivity, 70));
					break;
				case 3:
					mRecvMsgContainer.setMinimumWidth(DensityUtil.dip2px(mActivity, 80));
					break;
				case 4:
					mRecvMsgContainer.setMinimumWidth(DensityUtil.dip2px(mActivity, 90));
					break;
				case 5:
					mRecvMsgContainer.setMinimumWidth(DensityUtil.dip2px(mActivity, 100));
					break;
				case 6:
					mRecvMsgContainer.setMinimumWidth(DensityUtil.dip2px(mActivity, 110));
					break;
				case 7:
					mRecvMsgContainer.setMinimumWidth(DensityUtil.dip2px(mActivity, 120));
					break;
				case 8:
					mRecvMsgContainer.setMinimumWidth(DensityUtil.dip2px(mActivity, 130));
					break;
				case 9:
					mRecvMsgContainer.setMinimumWidth(DensityUtil.dip2px(mActivity, 140));
					break;
				case 10:
					mRecvMsgContainer.setMinimumWidth(DensityUtil.dip2px(mActivity, 150));
					break;
				case 11:
					mRecvMsgContainer.setMinimumWidth(DensityUtil.dip2px(mActivity, 160));
					break;
				case 12:
					mRecvMsgContainer.setMinimumWidth(DensityUtil.dip2px(mActivity, 170));
					break;
				case 13:
					mRecvMsgContainer.setMinimumWidth(DensityUtil.dip2px(mActivity, 180));
					break;
				case 14:
					mRecvMsgContainer.setMinimumWidth(DensityUtil.dip2px(mActivity, 190));
					break;
				default:
					mRecvMsgContainer.setMinimumWidth(DensityUtil.dip2px(mActivity, 200));
					break;
				}
			}
		} else if (info.getContenttype() == MessageConstant.MSG_CONTENT_TYPE_IMG) {
			final String path = info.getPath();
			mChatRecvImgContainer.setVisibility(View.VISIBLE);
			mMsgContent.setVisibility(View.GONE);
			mRecvMsgContainer.setVisibility(View.GONE);

			// mChatRecvImg.post(new Runnable() {
			// @Override
			// public void run() {
			if (!TextUtils.isEmpty(path)) {
				mMsgContent.setVisibility(View.GONE);
				mRecvMsgTime.setVisibility(View.GONE);
				mChatRecvImg.setVisibility(View.VISIBLE);
				FitBitmap fm = ImageUtil.getResizedImage(path, null, null, 600, false, 0);
				if (fm != null) {
					final Bitmap bitmap = fm.getmBitmap();
					mChatRecvImg.setImageBitmap(bitmap);
					mChatRecvImgContainer.setOnLongClickListener(new OnLongClickListener() {

						@Override
						public boolean onLongClick(View arg0) {
							if (GlobalVariable.mChatMsgViewActivity != null) {
								GlobalVariable.mChatMsgViewActivity.showSavePicPop(mChatRecvImgContainer, bitmap, path,
										position);
							}
							return true;
						}
					});
				}
			}
			// }
			// });
		}
	}

	@Override
	public void onClick(View v) {
		if (GlobalVariable.mChatMsgViewActivity != null) {
			GlobalVariable.mChatMsgViewActivity.disMissPop();
		}
		int id = v.getId();
		switch (id) {
		case R.id.voice_recv_msg_container:
			mRedPointIv.setVisibility(View.GONE);
			mChatInfo.setReaded(true);
			DBHelper.getInstance().getWeLearnDB().updateIsReaded(mChatInfo);
			if (!TextUtils.isEmpty(mAudioPath)) {
				play(mRecvMsgPlay, mAudioPath, true);
			}
			break;
		case R.id.recv_msg_content:
			mRedPointIv.setVisibility(View.GONE);
			mChatInfo.setReaded(true);
			DBHelper.getInstance().getWeLearnDB().updateIsReaded(mChatInfo);
			if (mChatInfo.getContenttype() == MessageConstant.MSG_CONTENT_TYPE_JUMP) {
				MsgData data = mChatInfo.getData();
				if (data == null) {
					return;
				}
				int action = data.getAction();
				Bundle bundleData = new Bundle();
				switch (action) {
				case 1:
					int questionId = data.getQuestion_id();
					bundleData.putLong(AnswerListItemView.EXTRA_QUESTION_ID, questionId);
					bundleData.putInt(AnswerListItemView.EXTRA_POSITION, 0);
					bundleData.putBoolean(AnswerListItemView.EXTRA_ISQPAD, true);
					IntentManager.goToAnswerDetail( mActivity,OneQuestionMoreAnswersDetailActivity.class, bundleData);
					break;
				case 2:
					
					int target_user_id = data.getUserid();
					int target_role_id = data.getRoleid();
					int userId = MySharePerfenceUtil.getInstance().getUserId();
					if (target_role_id == 0 || target_user_id == 0) {
						break;
					}
					// Bundle data = new Bundle();
					bundleData.putInt("userid", target_user_id);
					bundleData.putInt("roleid", target_role_id);
					if (target_user_id == userId) {// 进入主页
//						IntentManager.goToStudentCenterView( mActivity, bundleData);
					} else {// 进入他人个人资料
						if (target_role_id == GlobalContant.ROLE_ID_COLLEAGE) {// 老师个人资料
							IntentManager.goToTeacherInfoView( mActivity, bundleData);
						} else {// 学生个人资料
							IntentManager.goToStudentInfoView( mActivity, bundleData);
						}
					}
					break;
				case 3://系统消息
					Intent intent = new Intent(mActivity, WebViewActivity.class);
					intent.putExtra("title", "大熊作业");
					intent.putExtra("url", data.getUrl());
					mActivity.startActivity(intent);
					
					break;
				case 4:

					break;
				case 5:
					int taskid = data.getTaskid();
					clickIntoHomeWork(taskid);
					break;
				case 6://有人追问了,点击进入单题
					clickIntoSingleHomeWork(data);
					break;
				case 7://老师回答了我的追问,点击进入追问界面
//					bundleData.putInt("choose", MastersCourseActivity.CHOOSE_MY_COURSE);
//					IntentManager.goToMastersCourseActivity(mActivity, bundleData, false, -1);
					
					clickIntoSingleStudentCourese(data.getPageid());
					
					break;
				case 8://课程有更新了,点击进入我的课程
					bundleData.putInt("choose", MastersCourseActivity.CHOOSE_MY_COURSE);
					IntentManager.goToMastersCourseActivity(mActivity, bundleData, false, -1);
					break;

				default:
					break;
				}

			} else if (mChatInfo.getContenttype() == MessageConstant.MSG_CONTENT_TYPE_JUMP_URL) {
				MsgData msgdata = mChatInfo.getData();
				Bundle data = new Bundle();
				data.putString(AuthActivity.AUTH_URL, msgdata.getUrl());
				IntentManager.gotoAuthView( mActivity, data);
			}
			break;
		case R.id.recv_msg_img_container:
			Bundle data = new Bundle();
			data.putString(PayAnswerQuestionDetailActivity.IMG_PATH, mChatInfo.getPath());
			data.putBoolean("doNotRoate", true);
			IntentManager.goToQuestionDetailPicView( mActivity, data);
			break;
		case R.id.to_user_avatar:
			if (mChatInfo.getFromuser() == GlobalContant.USER_ID_SYSTEM
					|| mChatInfo.getFromuser() == GlobalContant.USER_ID_HELPER) {
				return;
			}
			
			int userid = mChatInfo.getFromuser();
			int roleid = mChatInfo.getFromroleid();
			Bundle bundle = new Bundle();
			bundle.putInt("userid", userid);
			bundle.putInt("roleid", roleid);
			bundle.putBoolean("isFromChat", true);
			if (roleid == GlobalContant.ROLE_ID_STUDENT||roleid==GlobalContant.ROLE_ID_PARENTS) {
				MobclickAgent.onEvent( mActivity, "studentInfoView");
				IntentManager.goToStudentInfoView( mActivity, bundle);
			} else if (roleid == GlobalContant.ROLE_ID_COLLEAGE|roleid == GlobalContant.ROLE_ID_CLASSTEACHER) {
				MobclickAgent.onEvent( mActivity, "teacherInfoView");
				IntentManager.goToTeacherInfoView( mActivity, bundle);
			}
			break;
		}
	}
	
	
	private void clickIntoSingleStudentCourese(final int pageid) {
		if (pageid != 0) {
			JSONObject data = new JSONObject();
			try {
				data.put("pageid", pageid);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			OkHttpHelper.post(mActivity, "course","appendpagedetail",  data, new HttpListener() {
				
				@Override
				public void onSuccess(int code, String dataJson, String errMsg) {
					if (!TextUtils.isEmpty(dataJson)) {
						int teacherid = JsonUtil.getInt(dataJson, "teacherid", 0);
						String teachername = JsonUtil.getString(dataJson, "teachername", "");
						String teacheravatar = JsonUtil.getString(dataJson, "teacheravatar", "");
						String imgurl = JsonUtil.getString(dataJson, "imgurl", "");
						String charptername = JsonUtil.getString(dataJson, "charptername", "");
						String point = JsonUtil.getString(dataJson, "point", "");
						IntentManager.goToSingleStudentQAActivity(mActivity, pageid, imgurl, charptername, point, teacherid, teacheravatar, teachername);
					}
				
					
				}
				
				@Override
				public void onFail(int HttpCode,String errMsg) {
					
					
				}
			});
		}
	}

	private void clickIntoHomeWork(int taskid) {
		if ((fromuser == GlobalContant.USER_ID_HELPER )||(fromuser == GlobalContant.USER_ID_SYSTEM )) {
			mActivity.uMengEvent("homework_detailfromsysmsg");
		}else {
			mActivity.uMengEvent("homework_detailfromchat");
		}
		Bundle data = new Bundle();
		data.putInt("taskid", taskid);
		data.putInt("type", 1);
		IntentManager.goToStuHomeWorkDetailActivity(mActivity, data, false);
	}

	private  void clickIntoSingleHomeWork(final MsgData msgdata){
		mActivity.showDialog("请稍候");
		JSONObject data = new JSONObject();
		try {
			data.put("taskid", msgdata.getTaskid());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpHelper.post(mActivity, "parents","homeworkgetone", data, new HttpListener() {
			
			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				mActivity.closeDialogHelp();
				if (code == 0) {
					int checkpointid = msgdata.getCheckpointid();
					HomeWorkCheckPointModel checkPointModel = new HomeWorkCheckPointModel();

					
					checkPointModel.setId(checkpointid);
					int isright = msgdata.getIsright();
					checkPointModel.setIsright(isright);
					String coordinate = msgdata.getCoordinate();
					if (coordinate != null) {
						checkPointModel.setCoordinate(coordinate);
					}
					String imgpath = msgdata.getImgpath();
					checkPointModel.setImgpath(imgpath);
					
					int state = JsonUtil.getInt(dataJson, "state", 0);
					if (state == HomeworkStatusConstant.ANSWERED ||state == HomeworkStatusConstant.APPENDASK  ||state == HomeworkStatusConstant.ANSWERING ) {
						checkPointModel.setAllowAppendAsk(true);
					}
					if (isright == GlobalContant.RIGHT_HOMEWORK) {
						checkPointModel.setAllowAppendAsk(false);
					}
					
					String teacheravatar = JsonUtil.getString(dataJson, "teacheravatar", "");
					String teachername = JsonUtil.getString(dataJson, "teachername", "");
					int teacherhomeworkcnt = JsonUtil.getInt(dataJson, "teacherhomeworkcnt", 0);
					int grabuserid = JsonUtil.getInt(dataJson, "grabuserid", 0);
					checkPointModel.setGrabuserid(grabuserid);
					checkPointModel.setTeacheravatar(teacheravatar);
					checkPointModel.setTeachername(teachername);
					checkPointModel.setTeacherhomeworkcnt(teacherhomeworkcnt);
					Bundle data = new Bundle();
					data.putBoolean("fromMsg", true);
					int taskid = msgdata.getTaskid();
					data.putInt("taskid", taskid);
					data.putSerializable(HomeWorkCheckPointModel.TAG, checkPointModel);
					IntentManager.goToStuSingleCheckActivity(mActivity, data, false);
				}
			}
			
			@Override
			public void onFail(int HttpCode,String errMsg) {
				mActivity.closeDialogHelp();
			}
		});
		
		
		
	}
}
