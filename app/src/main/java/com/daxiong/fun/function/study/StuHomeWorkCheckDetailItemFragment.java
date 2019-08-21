package com.daxiong.fun.function.study;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.daxiong.fun.R;
import com.daxiong.fun.function.MyOrientationEventListener;
import com.daxiong.fun.function.MyOrientationEventListener.OnOrientationChangedListener;
import com.daxiong.fun.function.homework.model.HomeWorkCheckPointModel;
import com.daxiong.fun.function.homework.model.StuPublishHomeWorkPageModel;
import com.daxiong.fun.function.homework.view.AddPointCommonView;
import com.daxiong.fun.view.DragImageView;

import java.util.ArrayList;

public class StuHomeWorkCheckDetailItemFragment extends Fragment implements OnOrientationChangedListener {
	public AddPointCommonView getmAddPointView() {
		return mAddPointView;
	}

	private AddPointCommonView mAddPointView;
	private StuPublishHomeWorkPageModel pageModel;
	private RelativeLayout divParentLayout;
	private DragImageView mPicIv;

	private boolean falg=true;
	private static  StuHomeWorkCheckDetailActivity Activity;
	
	private MyOrientationEventListener moraientation;
	private View view;

	private Handler mHandler = new Handler () {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				String path = msg.obj.toString();
				if (path != null && null != mAddPointView) {
					mAddPointView.setPagePic(path, false);
				}
				break;
			}
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 view = inflater.inflate(R.layout.homework_check_detail_pager_item, null);
		Bundle arguments = getArguments();
		pageModel = (StuPublishHomeWorkPageModel) arguments.getSerializable(StuPublishHomeWorkPageModel.TAG);

		mAddPointView = (AddPointCommonView) view.findViewById(R.id.add_point_common_tec_detail);
		divParentLayout = (RelativeLayout) view.findViewById(R.id.div_parent);
		mPicIv = (DragImageView) view.findViewById(R.id.pic_iv_add_point);

		if (pageModel != null) {
			String imgpath = pageModel.getImgpath();
			mHandler.sendMessageDelayed(mHandler.obtainMessage(1, imgpath), 10);
//			if (imgpath != null) {
//				mAddPointView.setPagePic(imgpath, false);
//			}
		}
		
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
	
	public void showPageData( ArrayList<HomeWorkCheckPointModel> checkpointlist,final int i,int type,boolean isShow,int stuid) {
		pageModel.setCheckpointlist(checkpointlist);
		mAddPointView.showRightOrWrongPoint(checkpointlist,stuid);
		if(i==0) {
			Activity.mAddPointView = mAddPointView;
		}
		if(type==1){
			mAddPointView.isFankui2=true;
		}else{
			mAddPointView.isFankui2=false;
		}
		if(checkpointlist!=null&&checkpointlist.size()>0&&type!=2&&isShow){
			boolean falg2=false;
			
			for (int j = 0; j < checkpointlist.size(); j++) {
				HomeWorkCheckPointModel pointModel = checkpointlist.get(j);
				if(pointModel.getShowcomplainttype()==0){
					falg2=true;
				}
			}
			falg3=falg2;


			//Activity.setmTextView(tv_jubao);

		}
	}

	public static StuHomeWorkCheckDetailItemFragment newInstance(StuPublishHomeWorkPageModel pageModel, StuHomeWorkCheckDetailActivity stuHomeWorkCheckDetailActivity) {
		Activity=stuHomeWorkCheckDetailActivity;
		StuHomeWorkCheckDetailItemFragment fragment = new StuHomeWorkCheckDetailItemFragment();
		Bundle data = new Bundle();
		data.putSerializable(StuPublishHomeWorkPageModel.TAG, pageModel);
		fragment.setArguments(data);
		return fragment;
	}

	@Override
	public void onOrientationChanged(int orientation) {
		if (null != mAddPointView) {
			mAddPointView.setOrientation(orientation);
		}
	}
	
	
	public void showPoint(int flag){
	    if (null==mAddPointView) {
	        mAddPointView = (AddPointCommonView) view.findViewById(R.id.add_point_common_tec_detail);
	        mAddPointView.showPoints(flag);
        }else {
            mAddPointView.showPoints(flag);
        }
    }
	
	private boolean falg3=false;
	


}
