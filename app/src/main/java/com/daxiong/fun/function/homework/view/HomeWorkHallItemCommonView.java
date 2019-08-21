package com.daxiong.fun.function.homework.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.daxiong.fun.R;
import com.daxiong.fun.base.ImageLoader;
import com.daxiong.fun.function.homework.HomeWorkHallActivity;
import com.daxiong.fun.function.homework.adapter.TecHomeWrokCheckCommonGridViewAdapter;
import com.daxiong.fun.function.homework.model.HomeWorkModel;
import com.daxiong.fun.function.homework.model.StuPublishHomeWorkPageModel;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 
 * 此类的描述： 作业检查的作业详情 的视图自定义控件(包含了作业的对于错，学情分析...)
 * @author:  sky
 * @最后修改人： sky
 * @最后修改日期:2015年8月3日 下午2:52:17
 */
public class HomeWorkHallItemCommonView extends FrameLayout implements OnItemClickListener {

	private HomeWorkHallActivity mActivity;
	private NetworkImageView mAvatarIv;
	private TextView mNickTv;
	private TextView mAskTime;
	private TextView mGradeSubjectTv;
	private View mVipAskerTag;
	private GridView mPicGv;
//	private TextView mAnswerQTv;
	// private TextView mSowNumTv;
	private TextView mCollectCountTv;
	private ImageView mCollectIconIv;
	private TecHomeWrokCheckCommonGridViewAdapter gridViewAdapter;
//	private TextView mAnswerTimeTv;
	private HomeWorkModel mHomeWorkModel;
//	private ArrayList<NetworkImageView> mPicList = new ArrayList<NetworkImageView>();
	private int avatarSize;
	private TextView mStateTv;
	private String[] States = {"抢答中","答题中","已解答","追问中","已采纳","已拒绝","仲裁中","仲裁完成","被举报","已删除"};
	private int packtype;
	private int avatarSize_tec;
	private TextView mTecSchoolTv;
	private TextView mNickTv_tec;
	private NetworkImageView mAvatarIv_tec;
	private View mTecInfoContainer;
	private TextView mAnswerTime;
    private LinearLayout layout_collection;//收藏layout
	
	
	public HomeWorkHallItemCommonView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mActivity = (HomeWorkHallActivity) context;
		initView();
	}

	public HomeWorkHallItemCommonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mActivity = (HomeWorkHallActivity) context;
		initView();
	}

	public HomeWorkHallItemCommonView(Context context) {
		super(context);
		this.mActivity = (HomeWorkHallActivity) context;
		initView();
	}

	@SuppressLint("InflateParams")
	private void initView() {
		avatarSize = getResources().getDimensionPixelSize(R.dimen.avatar_size_homework_check_common);
		avatarSize_tec = getResources().getDimensionPixelSize(R.dimen.avatar_size_homeworkhall_item_common);

		View view = LayoutInflater.from(getContext()).inflate(R.layout.homework_hall_item_commonview, null);
		// mPicContainer = (RelativeLayout)
		// view.findViewById(R.id.pic_container_add_point);
		// mPicContainer.setOnTouchListener(this);
		mAvatarIv = (NetworkImageView) view.findViewById(R.id.avatar_iv_hall_item_common);
		mNickTv = (TextView) view.findViewById(R.id.nick_tv_hall_item_common);
		mAskTime = (TextView) view.findViewById(R.id.ask_time_tv_hall_item_common);
		mAnswerTime = (TextView) view.findViewById(R.id.answer_time_tv_hall_item_common);
		
		mAvatarIv_tec = (NetworkImageView) view.findViewById(R.id.tec_avatar_iv_hall_item);
		mNickTv_tec  = (TextView) view.findViewById(R.id.tec_nick_tv_hall_item);
		mTecSchoolTv  = (TextView) view.findViewById(R.id.tec_school_tv_hall_item);
		
		mGradeSubjectTv = (TextView) view.findViewById(R.id.grade_subject_tv_hall_item_common);
		mVipAskerTag = (View) view.findViewById(R.id.vip_asker_iv_hall_item_common);
		mPicGv = (GridView) view.findViewById(R.id.img_list_container_gridview_item);
		gridViewAdapter = new TecHomeWrokCheckCommonGridViewAdapter(null, mActivity);
		mPicGv.setAdapter(gridViewAdapter);
		mPicGv.setOnItemClickListener(this);

		mTecInfoContainer = view.findViewById(R.id.tec_info_container_hall_item);
//		mAnswerQTv = (TextView) view.findViewById(R.id.answer_qulity_tv_hall_item_common);
//		mAnswerTimeTv = (TextView) view.findViewById(R.id.answer_time_tv_hall_item_common);
		// mSowNumTv = (TextView)
		// view.findViewById(R.id.sow_num_tv_hall_item_common);
		mCollectIconIv = (ImageView) view.findViewById(R.id.collection_icon_iv_hall_item);
		mCollectCountTv = (TextView) view.findViewById(R.id.collection_count_tv_hall_item);
		mStateTv = (TextView) view.findViewById(R.id.state_homework_tv_hallitem);
		layout_collection = (LinearLayout)view.findViewById(R.id.collection_container_ll_hall_item);
		layout_collection.setVisibility(View.VISIBLE);
		layout_collection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mActivity.showCollectingDialog();
				JSONObject data = new JSONObject();
				try {
					data.put("taskid", mHomeWorkModel.getTaskid());
					data.put("tasktype", 2);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				OkHttpHelper.post(mActivity, "common","collect", data , new HttpListener() {
					
					@Override
					public void onSuccess(int code, String dataJson, String errMsg) {
						mActivity.closeDialogHelp();
						if (code == 0) {
							int praise = mHomeWorkModel.getPraise();//0是空心  1是实心
							int praisecnt = mHomeWorkModel.getPraisecnt();
							if (praise == 0) {
								mActivity.uMengEvent("homework_collect");
								praisecnt ++;
								mCollectIconIv.setImageResource(R.drawable.collect_ic_shixin);
								mHomeWorkModel.setPraise(1);
							}else {
								mActivity.uMengEvent("homework_removecollect");
								praisecnt --;
								mCollectIconIv.setImageResource(R.drawable.collect_ic_kongxin);
								mHomeWorkModel.setPraise(0);
							}
							mCollectCountTv.setText("" + praisecnt);
							mHomeWorkModel.setPraisecnt(praisecnt);
						}else {
							ToastUtils.show(errMsg);
						}
					}
					
					@Override
					public void onFail(int HttpCode,String errMsg) {
						mActivity.closeDialogHelp();
					}
				});
			}
		});

		addView(view);
	}

	/**
	 * 作业详情单页
	 * 
	 * @param homeWorkModel
	 * @param packtype 
	 */
	@SuppressLint("SimpleDateFormat")
	public void showData(HomeWorkModel homeWorkModel, int packtype) {
		if (homeWorkModel == null) {
			return;
		}
		this.mHomeWorkModel = homeWorkModel;
		this.packtype = packtype;
		String teachername = homeWorkModel.getTeachername();
		String teacheravatar = homeWorkModel.getTeacheravatar();
		int grabuserid = mHomeWorkModel.getGrabuserid();
		String teacherschool = homeWorkModel.getTeacherschool();
		boolean flag = true;
		if (/*TextUtils.isEmpty(teacherschool) || */TextUtils.isEmpty(teacheravatar) || TextUtils.isEmpty(teachername) || grabuserid == 0) {
			flag = false;
		}
			if ("抢答中".equals(States[mHomeWorkModel.getState()])) {
			flag = false;
		}
		if (flag) {
			mAvatarIv_tec.setVisibility(View.VISIBLE);
			mTecInfoContainer.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().loadImageWithDefaultAvatar(teacheravatar, mAvatarIv_tec, avatarSize_tec,
					avatarSize_tec / 10);
			mAvatarIv_tec.setTag(grabuserid);
			mAvatarIv_tec.setOnClickListener(mActivity);
			mNickTv_tec.setText(teachername);
			mTecSchoolTv.setText(teacherschool);
//			if (!TextUtils.isEmpty(teacherschool) ) {
//			}else {
//				
//			}
		} else {
			mAvatarIv_tec.setVisibility(View.GONE);
			mTecInfoContainer.setVisibility(View.GONE);
		}
		
		ImageLoader.getInstance().loadImageWithDefaultAvatar(homeWorkModel.getAvatar(), mAvatarIv, avatarSize,
				avatarSize / 10);
		mAvatarIv.setTag(mHomeWorkModel.getStudid());
		mAvatarIv.setOnClickListener(mActivity);
	
		mNickTv.setText(homeWorkModel.getStudname());

		SimpleDateFormat format = new SimpleDateFormat("M月d日 HH:mm:ss");
		String askTime = format.format(new Date(homeWorkModel.getDatatime()));

		
		mAskTime.setText(askTime);
		mGradeSubjectTv.setText(homeWorkModel.getGrade() + "  " + homeWorkModel.getSubject());
		if (homeWorkModel.getSupervip() > 0) {
			mVipAskerTag.setVisibility(View.VISIBLE);
		} else {
			mVipAskerTag.setVisibility(View.INVISIBLE);
		}
		mStateTv.setText(States[mHomeWorkModel.getState()]);
		ArrayList<StuPublishHomeWorkPageModel> pagelist = homeWorkModel.getPagelist();
		gridViewAdapter.setList(pagelist);
//		int satisfaction = homeWorkModel.getSatisfaction();
//		if (satisfaction != 0) {
//			mAnswerQTv.setVisibility(View.VISIBLE);
//			mAnswerQTv.setText(mActivity.getString(R.string.answer_quality_text, satisfaction + "")); // 详细程度几星
//		} else {
//			mAnswerQTv.setVisibility(View.GONE);
//		}
		long answertime = homeWorkModel.getAnswertime();
		if (answertime != 0) {
			long dTime = answertime - homeWorkModel.getGrabtime(); // homeWorkModel.getDatatime();
			int answerTime = (int) (dTime / 60000);
			if (answerTime < 3) {
				answerTime = 3;
			}
			mAnswerTime.setVisibility(View.VISIBLE);
			mAnswerTime.setText(mActivity.getString(R.string.answer_time_text, answerTime + ""));
		} else {
			mAnswerTime.setVisibility(View.GONE);
		}
		
		if (packtype==2) {
		    layout_collection.setVisibility(View.GONE);
        }else {
            layout_collection.setVisibility(View.VISIBLE);
        }

		
		int praise = homeWorkModel.getPraise();// 0是空心未收藏  1是实心已收藏
		int praisecnt = homeWorkModel.getPraisecnt();
		mCollectCountTv.setText("" + praisecnt);
		if (praise == 0) {
			mCollectIconIv.setImageResource(R.drawable.collect_ic_kongxin);
		} else {
			mCollectIconIv.setImageResource(R.drawable.collect_ic_shixin);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Bundle data = new Bundle();
		data.putInt("position", position);
		data.putInt("taskid", mHomeWorkModel.getTaskid());
		switch (packtype) {
		case 0://作业广场
		case 1://作业广场(学生刚发布了一个作业)
		case 4://他人作业
			IntentManager.goToStuHomeWorkDetailActivity(mActivity, data, false);
			mActivity.uMengEvent("homework_detailfromhall");
			break;
		case 2://我的作业(学生)
			mActivity.uMengEvent("homework_detailfrommy");
			IntentManager.goToStuHomeWorkDetailActivity(mActivity, data, false);
			break;
		case 6://从查找好友中查询到老师
		    IntentManager.goToStuHomeWorkDetailActivity(mActivity, data, false);
            mActivity.uMengEvent("homework_detailfromhall");
		    break;

		default:
			break;
		}
	}
}
