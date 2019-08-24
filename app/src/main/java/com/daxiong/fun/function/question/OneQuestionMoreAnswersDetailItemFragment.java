
package com.daxiong.fun.function.question;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.NetworkImageView.OnImageLoadListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.api.AnswerAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.base.ImageLoader;
import com.daxiong.fun.constant.AnswerStatusConstant;
import com.daxiong.fun.constant.EventConstant;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.dialog.CustomInputDialog;
import com.daxiong.fun.dialog.CustomTipDialog;
import com.daxiong.fun.dialog.WelearnDialog;
import com.daxiong.fun.function.CameraChoiceIconWithSer;
import com.daxiong.fun.function.RefuseAnswerPopWindow;
import com.daxiong.fun.function.answer.JiuCuoActivity;
import com.daxiong.fun.function.answer.MaxLengthWatcher;
import com.daxiong.fun.function.homework.view.AdoptHomeWorkCheckDialog;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.AnswerDetail;
import com.daxiong.fun.model.AnswerSound;
import com.daxiong.fun.model.JiuCuoModel;
import com.daxiong.fun.model.QuestionDetailGson;
import com.daxiong.fun.receiver.HeadsetPlugReceiver;
import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MediaUtil;
import com.daxiong.fun.util.MediaUtil.ResetImageSourceCallback;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.AnswertextPopupWindow;

import com.daxiong.fun.view.CropCircleTransformation;
import com.daxiong.fun.view.CustomFragment;
import com.daxiong.fun.view.DragImageView;
import com.daxiong.fun.view.DragImageView.OnScaleListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 此类的描述：对问题的详细解答Fragment
 * 
 * @author: sky @最后修改人： sky
 * @最后修改日期:2015年8月3日 下午4:42:56
 */
public class OneQuestionMoreAnswersDetailItemFragment extends CustomFragment implements OnClickListener {
	public static final String TAG = OneQuestionMoreAnswersDetailItemFragment.class.getSimpleName();

	private int currentPageIndex;

	private String jsonStr;

	private boolean isQpad;

	private TextView answerDesc;

	private ImageView detailUserAvatar;

	private TextView detailUserName;

	private TextView detailUserColleage;

	// private TextView detailUpBtn;
	private DragImageView detailImage;

	private ProgressBar mBar;

	private RelativeLayout mDetailContainer;

	private QuestionDetailGson mQuestionDetailGson;

	private AnswerDetail mAnswerDetail;

	private DragImageView mQuestionImg;

	private TextView mAnswerBtn1;

	private TextView mAnswerBtn2;

	private TextView mAnswerBtn3;

	private HeadsetPlugReceiver headsetPlugReceiver;

	private Bitmap mBitmap;

	private RelativeLayout imageParentLayout;

	private int window_width, window_height;// 控件宽度

	private ImageView rotate_btn;// 旋转图片的按钮

	private WelearnDialog mWelearnDialogBuilder;

	private boolean isShowTips = true;

	private int avatarSize;

	private TextView tv_jiucuo, tv_jubao;

	private AnswerAPI answerApi;

	private int goTag = -1;

	private int status = -1;// 纠错状态

	private JiuCuoModel jiucuoModel;// 纠错实体

	private CustomInputDialog dialog;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_answer_detail_view, null);
		initView(view, savedInstanceState);
		initListener();
		// add headset receiver
		registerHeadsetPlugReceiver();
		return view;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub

	}

	public void initView(View view, Bundle savedInstanceState) {
		/*
		 * mActionBar.setDisplayShowHomeEnabled(false);
		 * mActionBar.setLogo(R.drawable.bg_actionbar_back_up_selector);
		 * mActionBar.setIcon(R.drawable.bg_actionbar_back_up_selector);
		 */
		answerApi = new AnswerAPI();
		imageParentLayout = (RelativeLayout) view.findViewById(R.id.image_parent_layout);
		// 测量控件的高度
		imageParentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (window_height == 0 || window_width == 0) {
					window_height = imageParentLayout.getHeight();
					window_width = imageParentLayout.getWidth();

					mQuestionImg.setScreenSize(window_width, window_height);
					detailImage.setScreenSize(window_width, window_height);
				}
			}
		});

		answerDesc = (TextView) view.findViewById(R.id.answer_source);
		tv_jubao = (TextView) view.findViewById(R.id.tv_jubao);
		detailUserAvatar = (ImageView) view.findViewById(R.id.detail_user_avatar);
		avatarSize = getResources().getDimensionPixelSize(R.dimen.one_question_more_answer_detail_item_avatar_size);
		detailUserName = (TextView) view.findViewById(R.id.detail_user_name);
		detailUserColleage = (TextView) view.findViewById(R.id.detail_user_colleage);
		// detailUpBtn = (TextView) view.findViewById(R.id.detail_up_btn);
		// detailUpBtn.setOnClickListener(this);
		detailImage = (DragImageView) view.findViewById(R.id.detail_image);
		// detailImage.setEnableDrag(false);
		detailImage.setOnScaleListener(new OnScaleListener() {
			@Override
			public void onScale(boolean isScale) {
				if (isShowTips != !isScale) {
					isShowTips = !isScale;
					showTips(!isScale);
					if (null != mOnTipsShowListener) {
						mOnTipsShowListener.onTipsShow(!isScale);
					}
				}
			}
		});

		mBar = (ProgressBar) view.findViewById(R.id.img_loading);
		mQuestionImg = (DragImageView) view.findViewById(R.id.detail_question_image);

		mDetailContainer = (RelativeLayout) view.findViewById(R.id.anser_detail_container);
		mAnswerBtn1 = (TextView) view.findViewById(R.id.answer_btn1);
		mAnswerBtn2 = (TextView) view.findViewById(R.id.answer_btn2);
		mAnswerBtn3 = (TextView) view.findViewById(R.id.answer_btn3);
		tv_jiucuo = (TextView) view.findViewById(R.id.tv_jiucuo);

		rotate_btn = (ImageView) view.findViewById(R.id.rotate_btn_answerdetail);

		if (savedInstanceState != null) {
			currentPageIndex = savedInstanceState.getInt("position", -1);
			jsonStr = savedInstanceState.getString("dataStr");
			isQpad = savedInstanceState.getBoolean("isqpad");
		} else {
			currentPageIndex = getArguments().getInt("position");
			jsonStr = getArguments().getString("dataStr");
			isQpad = getArguments().getBoolean("isqpad");
		}
		showData();
	}

	public void initListener() {
		detailUserName.setOnClickListener(this);
		detailUserColleage.setOnClickListener(this);
		detailUserAvatar.setOnClickListener(this);
		mAnswerBtn1.setOnClickListener(this);
		mAnswerBtn2.setOnClickListener(this);
		mAnswerBtn3.setOnClickListener(this);
		rotate_btn.setOnClickListener(this);
		tv_jiucuo.setOnClickListener(this);

	}

	private void registerHeadsetPlugReceiver() {
		headsetPlugReceiver = new HeadsetPlugReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.intent.action.HEADSET_PLUG");
		this.getActivity().getApplication().registerReceiver(headsetPlugReceiver, intentFilter);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private List<CameraChoiceIconWithSer> answerIcsList = new ArrayList<CameraChoiceIconWithSer>();

	@SuppressLint("NewApi")
	public void showTips(boolean isShow) {

		if (null == mDetailContainer) {
			return;
		}

		int show = isShow ? View.VISIBLE : View.GONE;

		/**
		 * RelativeLayout.LayoutParams lp =
		 * (LayoutParams)mDetailContainer.getLayoutParams(); if (isShow) {
		 * lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT; lp.height =
		 * RelativeLayout.LayoutParams.MATCH_PARENT; } else { lp.width =
		 * RelativeLayout.LayoutParams.MATCH_PARENT; lp.height =
		 * RelativeLayout.LayoutParams.MATCH_PARENT; }
		 * mDetailContainer.setLayoutParams(lp);
		 **/

		View v = null;
		for (int i = 0; i < mDetailContainer.getChildCount(); i++) {
			v = mDetailContainer.getChildAt(i);
			if (null != v) {
				if (!(v instanceof DragImageView)) {
					v.setVisibility(show);
				}
			}
		}
		if (isShow) {
			try {
				detailImage.callOnClick();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		isShowTips = isShow;
	}

	private QuestionDetailGson getQuestionDetailGson() {
		JSONObject questionObj = JsonUtil.getJSONObject(jsonStr, "question", null);
		// Log.i(TAG, questionObj.toString());
		// add by milo 2014.09.15 crash bug fixed
		if (questionObj != null) {
			return new Gson().fromJson(questionObj.toString(), QuestionDetailGson.class);
		}
		return new QuestionDetailGson();
	}

	private AnswerDetail getAnswerDetail() {
		JSONArray answerArray = JsonUtil.getJSONArray(jsonStr, "answer", null);
		// Log.i (TAG, answerArray.toString());
		// add by milo 2014.09.15 crash bug fixed
		if (answerArray != null) {
			Gson gson = new Gson();
			List<AnswerDetail> answerList = gson.fromJson(answerArray.toString(),
					new TypeToken<ArrayList<AnswerDetail>>() {
					}.getType());
			return answerList.get(currentPageIndex - 1);
		}
		return null;
	}

	private void hideAppendAdoptAbourBtn() {
		mAnswerBtn1.setVisibility(View.GONE);
		mAnswerBtn3.setVisibility(View.GONE);
		mAnswerBtn2.setVisibility(View.GONE);
		if (!isShowQuestion()) {
			tv_jiucuo.setVisibility(View.GONE);
		}
	}

	private void showData() {
		if (isShowQuestion()) {
			tv_jiucuo.setVisibility(View.GONE);
			rotate_btn.setVisibility(View.VISIBLE);
			mQuestionImg.setVisibility(View.VISIBLE);
			mQuestionDetailGson = getQuestionDetailGson();
			answerDesc.setVisibility(View.VISIBLE);
			String answerDescStr = mQuestionDetailGson.getSource();
			if (TextUtils.isEmpty(answerDescStr)) {
				answerDescStr = "";
			}
			answerDesc.setText(answerDescStr);
			// ImageLoader.getInstance().loadImageWithDefaultAvatar(mQuestionDetailGson.getAvatar(),
			// detailUserAvatar,
			// avatarSize, avatarSize / 10);

			Glide.with(OneQuestionMoreAnswersDetailItemFragment.this).load(mQuestionDetailGson.getAvatar())
					.diskCacheStrategy(DiskCacheStrategy.ALL)
					.bitmapTransform(new CropCircleTransformation(getActivity())).placeholder(R.drawable.default_icon_circle_avatar)
					.into(detailUserAvatar);

			ImageLoader.getInstance().loadImage(mQuestionDetailGson.getQ_imgurl(), mQuestionImg, R.drawable.loading,
					R.drawable.retry, new OnImageLoadListener() {
						@Override
						public void onSuccess(ImageContainer arg0) {
							if (arg0 != null) {
								mBitmap = arg0.getBitmap();
							}
							mBar.setVisibility(View.GONE);
						}

						@Override
						public void onFailed(VolleyError arg0) {

						}
					});

			detailUserName.setText(mQuestionDetailGson.getStudname());
			detailUserColleage.setText(mQuestionDetailGson.getGrade());
		} else {
			mDetailContainer.setVisibility(View.VISIBLE);
			rotate_btn.setVisibility(View.GONE);
			mQuestionImg.setVisibility(View.GONE);
			JSONArray answerArray = JsonUtil.getJSONArray(jsonStr, "answer", null);
			LogUtils.i(TAG, answerArray.toString());
			Gson gson = new Gson();
			List<AnswerDetail> answerList = gson.fromJson(answerArray.toString(),
					new TypeToken<ArrayList<AnswerDetail>>() {
					}.getType());
			answerDesc.setVisibility(View.GONE);
			mAnswerDetail = answerList.get(currentPageIndex - 1);
			detailUserColleage.setText(mAnswerDetail.getSchools());
			// ImageLoader.getInstance().loadImageWithDefaultAvatar(mAnswerDetail.getT_avatar(),
			// detailUserAvatar, avatarSize, avatarSize / 10);

			Glide.with(OneQuestionMoreAnswersDetailItemFragment.this).load(mAnswerDetail.getT_avatar())
					.diskCacheStrategy(DiskCacheStrategy.ALL)
					.bitmapTransform(new CropCircleTransformation(getActivity())).placeholder(R.drawable.teacher_img)
					.into(detailUserAvatar);

			ImageLoader.getInstance().loadImage(mAnswerDetail.getA_imgurl(), detailImage, R.drawable.loading,
					R.drawable.retry, new OnImageLoadListener() {
						@Override
						public void onSuccess(ImageContainer arg0) {
							mBar.setVisibility(View.GONE);
							Bitmap bm = arg0.getBitmap();
							if (null != bm) {
								// 显示解答的题目和具体答案(语音+文字)
								detailImage.setImageBitmap(bm);
								showAnswer(arg0.getBitmap());
							}
						}

						@Override
						public void onFailed(VolleyError arg0) {

						}
					});

			detailUserName.setText(mAnswerDetail.getGrabuser() + "   " + mAnswerDetail.getGrabuserid());

		}

		if (isQpad) {
			// detailUpBtn.setVisibility(View.GONE);// 我的q板隐藏点赞
			int currentUserid = MySharePerfenceUtil.getInstance().getUserId();
			if (isShowQuestion()) {// 隐藏追问 采纳 拒绝
				hideAppendAdoptAbourBtn();
			} else {// 如果是学生
				if (MySharePerfenceUtil.getInstance().getUserRoleId() == GlobalContant.ROLE_ID_STUDENT
						| MySharePerfenceUtil.getInstance().getUserRoleId() == GlobalContant.ROLE_ID_PARENTS) {
					// 如果状态是已回答或者追问中,则显示追问 采纳 拒绝
					int a_state = mAnswerDetail.getA_state();
					if (a_state == AnswerStatusConstant.STATUS_ANSWER_ANSWERED
							|| a_state == AnswerStatusConstant.STATUS_ANSWER_APPEND) {
						mAnswerBtn1.setText(getString(R.string.text_append_ask));
						mAnswerBtn1.setTag(AnswerStatusConstant.STATUS_ANSWER_APPEND);

						mAnswerBtn2.setText(getString(R.string.text_adopt));
						mAnswerBtn2.setTag(AnswerStatusConstant.STATUS_ANSWER_ADOPTED);

						mAnswerBtn3.setText(getString(R.string.text_abour));
						mAnswerBtn3.setTag(AnswerStatusConstant.STATUS_ANSWER_ABOURED);
						tv_jiucuo.setVisibility(View.GONE);
						// 增加了已拒绝和仲裁中也可以采纳答案
					} else if (a_state == AnswerStatusConstant.STATUS_ANSWER_ABOURED
							|| a_state == AnswerStatusConstant.STATUS_ANSWER_ARBITRATION) {
						mAnswerBtn2.setText(getString(R.string.text_adopt));
						mAnswerBtn2.setTag(AnswerStatusConstant.STATUS_ANSWER_ADOPTED);
						mAnswerBtn1.setVisibility(View.INVISIBLE);
						mAnswerBtn1.setClickable(false);
						mAnswerBtn3.setVisibility(View.INVISIBLE);
						mAnswerBtn3.setClickable(false);
						tv_jiucuo.setVisibility(View.GONE);

					} else {
						hideAppendAdoptAbourBtn();
						if (!isShowQuestion()) {
							tv_jiucuo.setVisibility(View.GONE);
						}
					}
				} else if (MySharePerfenceUtil.getInstance().getUserRoleId() == GlobalContant.ROLE_ID_COLLEAGE) {
					// 老师 已拒绝 申请仲裁 放弃仲裁
					if (mAnswerDetail.getA_state() == AnswerStatusConstant.STATUS_ANSWER_ABOURED) {
						mAnswerBtn3.setVisibility(View.INVISIBLE);
						mAnswerBtn3.setClickable(false);

						// 申请仲裁
						mAnswerBtn1.setText(getString(R.string.text_arbitration_apply));
						mAnswerBtn1.setTag(AnswerStatusConstant.STATUS_ANSWER_ARBITRATION);

						// 放弃仲裁
						mAnswerBtn2.setText(getString(R.string.text_arbitration_abour));
						mAnswerBtn2.setTag(AnswerStatusConstant.STATUS_ANSWER_ABOUR_ARBITRATION);
					} else if (mAnswerDetail.getA_state() == AnswerStatusConstant.STATUS_ANSWER_ANSWERED
							|| mAnswerDetail.getA_state() == AnswerStatusConstant.STATUS_ANSWER_APPEND) {
						// 已回答 追问中 添加回答 就没有申请仲裁 放弃仲裁
						mAnswerBtn1.setVisibility(View.VISIBLE);
						mAnswerBtn2.setVisibility(View.INVISIBLE);
						mAnswerBtn2.setClickable(false);
						mAnswerBtn3.setVisibility(View.INVISIBLE);
						mAnswerBtn3.setClickable(false);

						// 回答
						mAnswerBtn1.setText(getString(R.string.text_reply));
						mAnswerBtn1.setTag(AnswerStatusConstant.STATUS_ANSWER_ANSWERED);
					} else if (mAnswerDetail.getA_state() == AnswerStatusConstant.STATUS_ANSWER_ARBITRATION) {// 仲裁中
						// 仲裁中可以放弃仲裁
						mAnswerBtn1.setVisibility(View.INVISIBLE);
						mAnswerBtn1.setClickable(false);
						mAnswerBtn3.setVisibility(View.INVISIBLE);
						mAnswerBtn3.setClickable(false);

						// 放弃仲裁
						mAnswerBtn2.setText(getString(R.string.text_arbitration_abour));
						mAnswerBtn2.setTag(AnswerStatusConstant.STATUS_ANSWER_ABOUR_ARBITRATION);
					} else {
						hideAppendAdoptAbourBtn();
						if (!isShowQuestion()) {
							tv_jiucuo.setVisibility(View.VISIBLE);
						}
					}
				}
				if (currentUserid != getQuestionDetailGson().getStudid()
						&& currentUserid != getAnswerDetail().getGrabuserid()) {

					hideAppendAdoptAbourBtn();
				}
			}
		} else {
			hideAppendAdoptAbourBtn();
			if (!isShowQuestion()) {
				tv_jiucuo.setVisibility(View.GONE);
			}
		}

		if (tv_jiucuo.getVisibility() == View.VISIBLE && !isShowQuestion()) {
			initData();
		}
	}

	/**
	 * 图片旋转
	 */
	private void rotate() {

		if (mBitmap != null) {
			Matrix matrix = new Matrix();
			matrix.postRotate(90);

			Bitmap resizedBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix,
					true);
			/*
			 * if (mBitmap != null && !mBitmap.isRecycled()) {
			 * mBitmap.recycle(); mBitmap = null; }
			 */
			mBitmap = resizedBitmap;
			mQuestionImg.setImageBitmap(resizedBitmap);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.answer_btn1:
			int tag = Integer.parseInt(v.getTag().toString());
			if (tag == AnswerStatusConstant.STATUS_ANSWER_APPEND) {// 追问
				MobclickAgent.onEvent(mActivity, "ContinueQ");
				MobclickAgent.onEvent(mActivity, "Question_Append");

				Intent intent = new Intent(mActivity, PayAnswerAppendAskActivity.class);
				intent.putExtra(PayAnswerAppendAskFragment.JSON_DATA, jsonStr);
				intent.putExtra(PayAnswerAppendAskFragment.ANSWER_INDEX, currentPageIndex);
				mActivity.startActivityForResult(intent, GlobalContant.APPEND_ASK_REQUEST_CODE);
			}
			break;
		case R.id.answer_btn2:
			int tag2 = Integer.parseInt(v.getTag().toString());
			if (tag2 == AnswerStatusConstant.STATUS_ANSWER_ADOPTED) {// 采纳
				MobclickAgent.onEvent(mActivity, "Question_Adopt");
				final CustomTipDialog tipDialog = new CustomTipDialog(getActivity(), "温馨提示", "采纳之后不能再追问，请先确定完全懂了之后再采纳喔",
						"取消", "采纳", true);
				final Button positiveBtn = tipDialog.getPositiveButton();
				final Button negativeBtn = tipDialog.getNegativeButton();
				tipDialog.setOnPositiveListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						negativeBtn.setTextColor(Color.parseColor("#57be6a"));
						tipDialog.dismiss();
						clickAdopt();
					}
				});
				tipDialog.setOnNegativeListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						negativeBtn.setTextColor(Color.parseColor("#57be6a"));
						tipDialog.dismiss();
					}
				});
				tipDialog.show();
			}
			break;
		case R.id.answer_btn3:
			int tag3 = Integer.parseInt(v.getTag().toString());
			if (tag3 == AnswerStatusConstant.STATUS_ANSWER_ABOURED) {// 拒绝
				MobclickAgent.onEvent(mActivity, "Question_Refuse");
				new RefuseAnswerPopWindow(mActivity, v, this);
			}
			break;

		case R.id.rotate_btn_answerdetail:
			rotate();
			break;
		case R.id.detail_user_avatar:
		case R.id.detail_user_name:
		case R.id.detail_user_colleage:
			if (isShowQuestion()) {
				IntentManager.gotoPersonalPage(mActivity, mQuestionDetailGson.getStudid(),
						mQuestionDetailGson.getRoleid());
			} else {
				IntentManager.gotoPersonalPage(mActivity, mAnswerDetail.getGrabuserid(), mAnswerDetail.getRoleid());
			}
			break;
		case R.id.tv_jiucuo:// 纠错

			if (status == 0) {
				dialog = new CustomInputDialog(getActivity());
				final EditText editText = (EditText) dialog.getEditText();// 方法在CustomDialog中实现
				editText.addTextChangedListener(new MaxLengthWatcher(300, editText));
				final Button positiveBtn = dialog.getPositiveButton();
				final Button negativeBtn = dialog.getNegativeButton();
				dialog.setOnPositiveListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
							positiveBtn.setTextColor(Color.parseColor("#57be6a"));
							showDialog("正在处理...");
							// 处理纠错操作
							answerApi.execCorrect(requestQueue, goTag, mAnswerDetail.getAnswer_id(),
									editText.getText().toString().trim(), OneQuestionMoreAnswersDetailItemFragment.this,
									RequestConstant.EXEC_JIUCUO);
						} else {
							ToastUtils.show("请输入纠错的理由");

						}

					}
				});
				dialog.setOnNegativeListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						negativeBtn.setTextColor(Color.parseColor("#57be6a"));
						dialog.dismiss();
					}
				});
				dialog.show();

			} else {
				// 跳转到纠错页面
				Intent intent = new Intent(getActivity(), JiuCuoActivity.class);
				intent.putExtra("jiucuomodel", jiucuoModel);
				startActivity(intent);

			}

			break;
		}
	}

	private void clickAdopt() {
		MobclickAgent.onEvent(mActivity, "Accept");

		AdoptHomeWorkCheckDialog dialog = new AdoptHomeWorkCheckDialog(mActivity,
				new AdoptHomeWorkCheckDialog.AdoptSubmitBtnClick() {

					@Override
					public void ensure(int degree, String comment, int checkbox) {
						JSONObject data = new JSONObject();
						int answer_id = mAnswerDetail.getAnswer_id();
						try {
							data.put("answer_id", answer_id);
							data.put("star", degree);
							data.put("comment", comment);
							data.put("hopeteacheranswernext", checkbox);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						BaseActivity activity = null;
						if (mActivity instanceof BaseActivity) {
							activity = (BaseActivity) mActivity;
						}
						OkHttpHelper.post(mActivity, "parents", "questionadopt", data, new HttpListener() {

							@Override
							public void onSuccess(int code, String dataJson, String errMsg) {
								setResultAndFinish();

							}

							@Override
							public void onFail(int HttpCode,String errMsg) {

							}
						});
					}

				});

		dialog.show();
	}

	private void setResultAndFinish() {
		mActivity.setResult(Activity.RESULT_OK);
		mActivity.finish();
	}

	public void refuseAnswer(String reason) {
		JSONObject data = new JSONObject();
		int answer_id = mAnswerDetail.getAnswer_id();
		try {
			data.put("answer_id", answer_id);
			data.put("reason", reason);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		BaseActivity activity = null;
		if (mActivity instanceof BaseActivity) {
			activity = (BaseActivity) mActivity;
		}
		OkHttpHelper.post(mActivity, "parents", "questionrefuse",  data, new HttpListener() {			
			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				setResultAndFinish();
				
			}			
			@Override
			public void onFail(int HttpCode,String errMsg) {
				
				
			}
		});
	}

	private AnimationDrawable mAnimationDrawable;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void showAnswer(final Bitmap loadedImage) {
		detailImage.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				final int currentWidth = detailImage.getWidth();
				final int currentHeight = detailImage.getHeight();

				final int srcWidth = mAnswerDetail.getWidth();
				final int srcHeight = mAnswerDetail.getHeight();

				// Bitmap bitmap =
				// ((BitmapDrawable)detailImage.getDrawable()).getBitmap();
				//
				// int newWidth = WeLearnScreenUtils.getScreenWidth();
				// float scaleFactor =
				// (float)newWidth/(float)currentWidth;
				// int newHeight = (int)(currentHeight * scaleFactor);
				// bitmap = Bitmap.createScaledBitmap(bitmap, newWidth,
				// newHeight, true);
				// detailImage.setImageBitmap(bitmap);

				showAnswerInconInPic(currentWidth, currentHeight, srcWidth, srcHeight);
				// showAnswerInconInPic(currentWidth, currentHeight,
				// window_width, window_height - 60);
				detailImage.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}

			private void showAnswerInconInPic(int currentWidth, int currentHeight, int srcWidth, int srcHeight) {
				List<AnswerSound> soundList = mAnswerDetail.getAnswer_snd();
				int count = 1;
				/*
				 * final boolean [] isSound = new boolean[soundList.size()]; for
				 * (int i = 0; i < isSound.length; i++) { isSound[i] = false; }
				 * for(int i = 0;i<soundList.size();i++){ final AnswerSound as =
				 * soundList.get(i);
				 */
				int measuredHeight = mDetailContainer.getMeasuredHeight();
				int measuredWidth = mDetailContainer.getMeasuredWidth();
				int meWidth = (measuredWidth - currentWidth) / 2;
				int meHeight = (measuredHeight - currentHeight) / 2;
				if (soundList != null && soundList.size() > 0) {
					// tv_jubao.setVisibility(View.VISIBLE);
					tv_jubao.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							ToastUtils.show("错题举报");

						}
					});
				}
				for (final AnswerSound as : soundList) {
					int type = as.getType();
					RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
							android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
							android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
					String[] coordinate = as.getCoordinate().split(",");
					float srcX = Float.parseFloat(coordinate[0]);
					float srcY = Float.parseFloat(coordinate[1]);

					float widthRadio = srcX / (float) srcWidth;
					float heightRadio = srcY / (float) srcHeight;

					float currentX = widthRadio * currentWidth;
					float currentY = heightRadio * currentHeight;
					currentX += meWidth;
					currentY += meHeight;
					relativeParams.leftMargin = (int) (currentX < 0 ? 0 : currentX);
					relativeParams.topMargin = (int) (currentY < 0 ? 0 : currentY);
					int frameWidthOrHeight = DensityUtil.dip2px(mActivity, 40);
					if (relativeParams.leftMargin > currentWidth + meWidth) {
						relativeParams.leftMargin = currentWidth + meWidth - frameWidthOrHeight;
					}
					if (relativeParams.topMargin > currentHeight + meHeight) {
						relativeParams.topMargin = currentHeight + meHeight - frameWidthOrHeight;
					}

					CameraChoiceIconWithSer iconSer = new CameraChoiceIconWithSer(mActivity, as.getRole(),
							as.getSubtype());
					iconSer.setLayoutParams(relativeParams);
					mDetailContainer.addView(iconSer);
					iconSer.getIcSerView().setText(count++ + "");
					answerIcsList.add(iconSer);

					final ImageView mGrabItemIcView = iconSer.getIcView();
					if (type == GlobalContant.ANSWER_AUDIO) {// 声音
						mGrabItemIcView.setImageResource(R.drawable.ic_play2);
						iconSer.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View view) {
								MobclickAgent.onEvent(mActivity, "Question_Explain");
								MobclickAgent.onEvent(mActivity, EventConstant.CUSTOM_EVENT_PLAY_AUDIO);
								/*
								 * StatService .onEvent( mActivity,
								 * EventConstant .CUSTOM_EVENT_PLAY_AUDIO, "");
								 */
								if (TextUtils.isEmpty(as.getQ_sndurl())) {
									ToastUtils.show(R.string.text_audio_is_playing_please_waiting);
									return;
								}
								mGrabItemIcView.setImageResource(R.drawable.play_animation);
								mAnimationDrawable = (AnimationDrawable) mGrabItemIcView.getDrawable();
								MyApplication.animationDrawables.add(mAnimationDrawable);
								MyApplication.anmimationPlayViews.add(mGrabItemIcView);
								/*
								 * WeLearnMediaUtil.getInstance(false)
								 * .stopPlay();
								 */
								MediaUtil.getInstance(false).playVoice(false, as.getQ_sndurl(), mAnimationDrawable,
										new ResetImageSourceCallback() {

											@Override
											public void reset() {
												mGrabItemIcView.setImageResource(R.drawable.ic_play2);
												// isSound[i] =
												// false;
											}

											@Override
											public void playAnimation() {
											}

											@Override
											public void beforePlay() {
												MediaUtil.getInstance(false).resetAnimationPlay(mGrabItemIcView);
											}
										}, null);
							}
						});
					} else if (type == GlobalContant.ANSWER_TEXT) {
						mGrabItemIcView.setImageResource(R.drawable.ic_text_choic_t);
						iconSer.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View view) {
								// Bundle bundle = new Bundle();
								// bundle.putString(PayAnswerTextAnswerActivity.ANSWER_TEXT,
								// as.getTextcontent());
								// IntentManager.goToAnswertextView(mActivity,
								// bundle);
								MobclickAgent.onEvent(mActivity, "Question_Explain");
								GlobalVariable.answertextPopupWindow = new AnswertextPopupWindow(getActivity(),
										as.getTextcontent());

							}
						});
					}
				}
			}
		});
	}

	private boolean isShowQuestion() {
		return currentPageIndex == 0;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt("position", currentPageIndex);
		outState.putString("dataStr", jsonStr);
		outState.putBoolean("isqpad", isQpad);
	}

	public void initData() {
		if (mAnswerDetail != null) {
			showDialog("加载数据中...");
			// 得到纠错的状态
			answerApi.getCorrectStatus(requestQueue, mAnswerDetail.getAnswer_id(), this,
					RequestConstant.GET_JIUCUO_STATUS);
		}
	}

	@Override
	protected void goBack() {
		if (GlobalVariable.answertextPopupWindow != null && GlobalVariable.answertextPopupWindow.isShowing()) {
			GlobalVariable.answertextPopupWindow.dismiss();
			return;
		}
		mActivity.finish();
	}

	public static OneQuestionMoreAnswersDetailItemFragment newInstance(int position, String jsonStr, boolean isQpad) {
		OneQuestionMoreAnswersDetailItemFragment fragment = new OneQuestionMoreAnswersDetailItemFragment();
		Bundle data = new Bundle();
		data.putInt("position", position);
		data.putString("dataStr", jsonStr);
		data.putBoolean("isqpad", isQpad);
		fragment.setArguments(data);
		return fragment;
	}

	@Override
	public void onPause() {
		super.onPause();
		MediaUtil.getInstance(false).stopPlay();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		MyApplication.animationDrawables.clear();
		MyApplication.anmimationPlayViews.clear();
		MediaUtil.getInstance(false).stopPlay();
		MediaUtil.getInstance(false).stopVoice(mAnimationDrawable);

		// remove headset receiver
		try {
			this.getActivity().getApplication().unregisterReceiver(headsetPlugReceiver);
		} catch (IllegalArgumentException e) {
			if (e.getMessage().contains("Receiver not registered")) {
				// Ignore this exception. This is exactly what is desired
			} else {
				// unexpected, re-throw
				throw e;
			}
		}
	}

	private OnTipsShowListener mOnTipsShowListener;

	public void setOnTipsShowListener(OnTipsShowListener listener) {
		mOnTipsShowListener = listener;
	}

	public interface OnTipsShowListener {
		// 是否显示右边的隐藏标注
		void onTipsShow(boolean isShow);
	}

	public interface SureBtnClick {
		public void ensure();
	}

	public interface AdoptSubmitBtnClick {
		public void ensure(int effect, int attitude);
	}

	@Override
	public void resultBack(Object... param) {
		super.resultBack(param);
		int flag = ((Integer) param[0]).intValue();
		switch (flag) {
		case RequestConstant.GET_JIUCUO_STATUS:// 得到纠错状态
			if (param.length > 0 && param[1] != null && param[1] instanceof String) {
				String datas = param[1].toString();
				int code = JsonUtil.getInt(datas, "Code", -1);
				String msg = JsonUtil.getString(datas, "Msg", "");
				if (code == 0) {
					try {
						String dataJson = JsonUtil.getString(datas, "Data", "");
						closeDialog();
						if (!TextUtils.isEmpty(dataJson)) {
							JSONObject json = new JSONObject(dataJson);
							status = json.optInt("status");
							if (status == 0) {// 可以纠错
								tv_jiucuo.setVisibility(View.VISIBLE);
								tv_jiucuo.setText("纠错");
							} else {// status=1 表示已经纠错
								tv_jiucuo.setVisibility(View.VISIBLE);
								tv_jiucuo.setText("已纠错");
								jiucuoModel = new Gson().fromJson(dataJson, JiuCuoModel.class);

							}
						} else {

						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
			break;

		case RequestConstant.EXEC_JIUCUO:// 执行纠错
			if (param.length > 0 && param[1] != null && param[1] instanceof String) {
				String datas = param[1].toString();
				int code = JsonUtil.getInt(datas, "Code", -1);
				String msg = JsonUtil.getString(datas, "Msg", "");
				if (code == 0) {
					try {
						closeDialog();
						dialog.dismiss();
						String dataJson = JsonUtil.getString(datas, "Data", "");
						ToastUtils.showCustomToast(getActivity(), "谢谢!提交成功!\n请等待客服姐姐审核");

					} catch (Exception e) {
						e.printStackTrace();
						closeDialog();
						dialog.dismiss();
					}
				} else {
					ToastUtils.show(msg);

				}

			}
			break;

		}

	}

	public void setGoTag(int goTag) {
		this.goTag = goTag;

	}

}
