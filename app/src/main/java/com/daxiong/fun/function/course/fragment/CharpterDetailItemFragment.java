package com.daxiong.fun.function.course.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.daxiong.fun.R;
import com.daxiong.fun.function.MyOrientationEventListener;
import com.daxiong.fun.function.MyOrientationEventListener.OnOrientationChangedListener;
import com.daxiong.fun.function.course.model.CoursePageModel;
import com.daxiong.fun.function.course.model.CoursePoint;
import com.daxiong.fun.function.course.view.AddPointCommonView;
import com.daxiong.fun.function.question.OneQuestionMoreAnswersDetailItemFragment.OnTipsShowListener;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.DragImageView.OnScaleListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CharpterDetailItemFragment extends Fragment implements OnOrientationChangedListener, HttpListener, OnScaleListener {
	private AddPointCommonView mAddPointView;
	private CoursePageModel mPageModel;
//	private RelativeLayout divParentLayout;
	private String pointListJson = "";
	private MyOrientationEventListener moraientation;
//	private ArrayList<CoursePoint> pointList;
	private Handler mHandler = new Handler () {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				String imgurl = mPageModel.getImgurl();
				if (imgurl != null && null != mAddPointView) {
					mAddPointView.setImgPath(imgurl, false);
					loadData();
				}
				break;
			}
		}
	};
	private boolean isShowTips = true;
	private OnTipsShowListener mOnTipsShowListener;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.charpter_detail_pager_item, null);
		Bundle arguments = getArguments();
		mPageModel = (CoursePageModel) arguments.getSerializable(CoursePageModel.TAG);

		mAddPointView = (AddPointCommonView) view.findViewById(R.id.add_point_common_charpter_detail);
//		divParentLayout = (RelativeLayout) view.findViewById(R.id.div_parent);
		if (mPageModel != null) {
			mHandler.sendEmptyMessageDelayed(1, 10);
		}
		mAddPointView.setOnScaleListener(this);
		moraientation = new MyOrientationEventListener(getActivity(), this);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		moraientation.enable();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		moraientation.disable();
	}

	public String getPointList() {
		return pointListJson;
	}

	//	public void showPageData(ArrayList<HomeWorkCheckPointModel> checkpointlist) {
//	pageModel.setCheckpointlist(checkpointlist);
//	mAddPointView.showRightOrWrongPoint(checkpointlist);
//}
	
//	public void refreshData
	
	public void loadData(){
		int pageid = mPageModel.getPageid();
		if (pageid  != 0) {
			JSONObject data = new JSONObject();
			try {
				data.put("pageid", pageid);
				data.put("type", 1);
				data.put("studentid", MySharePerfenceUtil.getInstance().getUserId());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			OkHttpHelper.post(getActivity(), "course", "pagedetail", data , this);
		}
	}

	public static CharpterDetailItemFragment newInstance(CoursePageModel pageModel) {
		CharpterDetailItemFragment fragment = new CharpterDetailItemFragment();
		Bundle data = new Bundle();
		data.putSerializable(CoursePageModel.TAG, pageModel);
		fragment.setArguments(data);
		return fragment;
	}

	@Override
	public void onOrientationChanged(int orientation) {
		if (null != mAddPointView) {
			mAddPointView.setOrientation(orientation);
		}
	}

	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (code == 0) {
			if (!TextUtils.isEmpty(dataJson)) {
				ArrayList<CoursePoint> pointList = null;
				try {
					pointList = new Gson().fromJson(dataJson, new TypeToken<ArrayList<CoursePoint>>() {
					}.getType());
				} catch (Exception e) {
				}
				if (null != pointList) {
					pointListJson = dataJson;
					if (mAddPointView != null) {
						mAddPointView.addPoints(pointList);
					}
				}
			}
		} else {
			ToastUtils.show(errMsg);
		}
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {
		ToastUtils.show("请求失败，失败码：" + HttpCode);
	}
	public void setOnTipsShowListener(OnTipsShowListener listener) {
		mOnTipsShowListener = listener;
	}
	@Override
	public void onScale(boolean isScale) {
		if (isShowTips != !isScale) {
			isShowTips = !isScale;
			if (null != mOnTipsShowListener) {
				mOnTipsShowListener.onTipsShow(!isScale);
			}
		}
	}

	public void showTips(boolean isShow) {
		isShowTips = isShow;
		if (mAddPointView != null) {
			mAddPointView.showPoints(isShow);
		}
	}

}
