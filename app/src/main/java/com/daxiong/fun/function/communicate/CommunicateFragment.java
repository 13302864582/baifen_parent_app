
package com.daxiong.fun.function.communicate;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.nikhilpanju.recyclerviewenhanced.OnActivityTouchListener;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MainActivity;
import com.daxiong.fun.R;
import com.daxiong.fun.api.HomeListAPI;
import com.daxiong.fun.api.WeLearnApi;
import com.daxiong.fun.base.BaseFragment;
import com.daxiong.fun.callback.INetWorkListener;
import com.daxiong.fun.constant.EventConstant;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.MessageConstant;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.dialog.WelearnDialog;
import com.daxiong.fun.dispatch.CommunicateController;
import com.daxiong.fun.dispatch.WelearnHandler;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.ChatInfo;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.DateUtil;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MyAsyncTask;
import com.daxiong.fun.util.SharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.CropCircleTransformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 沟通Fragment
 * 
 * @author Sky
 */
public class CommunicateFragment extends BaseFragment
		implements INetWorkListener ,RecyclerTouchListener.RecyclerTouchListenerHelper {
	private static final String TAG = CommunicateFragment.class.getSimpleName();

	private Activity mActivity;

	private CommunicateController mCommunicateController;



	private RecyclerTouchListener onTouchListener;

	private OnActivityTouchListener touchListener;

	private List<ChatInfo> infos=new ArrayList<ChatInfo>();
	private Map<Integer, ChatInfo> map = new HashMap<Integer, ChatInfo>();
	private List<Integer> list;

	private int currentIndex;

	private static boolean isDoInDB;

	private static long reflashTime;

	private WelearnDialog mDialog;
	private TextView tv_meiyou;
	RecyclerView mRecyclerView;
	MainAdapter mAdapter;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_communicate, container, false);

		mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
		tv_meiyou = (TextView) view.findViewById(R.id.tv_meiyou);
		mAdapter = new MainAdapter(mActivity, infos);
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

		onTouchListener = new RecyclerTouchListener(mActivity, mRecyclerView);
		onTouchListener

				.setClickable(new RecyclerTouchListener.OnRowClickListener() {
					@Override
					public void onRowClicked(int position) {
						Bundle data = new Bundle();
						int userid = infos.get(position).getFromuser();
						data.putInt("userid", userid);
						IntentManager.goToChatListView(mActivity, data, false);
					}

					@Override
					public void onIndependentViewClicked(int independentViewID, int position) {

					}
				})

				.setSwipeOptionViews( R.id.change)
				.setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
					@Override
					public void onSwipeOptionClicked(int viewID, int position) {
						final int userid = infos.get(position).getFromuser();
						DBHelper.getInstance().getWeLearnDB().deleteMsg(userid);
						showMessageList();
					}
				});

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		mRecyclerView.addOnItemTouchListener(onTouchListener);
		MobclickAgent.onEventBegin(mActivity, EventConstant.CUSTOM_EVENT_COMMUNACATE);
		if (mCommunicateController == null) {
			mCommunicateController = new CommunicateController(null, CommunicateFragment.this);
		}
		if (list != null) {
			showMessageList();
		}else{
			if(!DateUtil.getBirthString2().equals(SharePerfenceUtil.getString("topchats",""))) {
				new HomeListAPI().topchats(requestQueue, this, RequestConstant.REQUEST_TOPCHATS_CODE);
			}else{
			String  topchatsstr=	SharePerfenceUtil.getString("topchats2","");
				if(TextUtils.isEmpty(topchatsstr)){
					list = JSON.parseArray(topchatsstr, Integer.class);
				}
				showMessageList();
			}
		}

	}


	@Override
	public void onPause() {
		super.onPause();
		mRecyclerView.removeOnItemTouchListener(onTouchListener);
		MobclickAgent.onEventEnd(mActivity, EventConstant.CUSTOM_EVENT_COMMUNACATE);
	}

	private void showMessageList() {
		new MyAsyncTask() {

			@Override
			public void preTask() {

			}

			@Override
			public void postTask() {

				if (infos != null && infos.size() > 0) {
					for (int i = 0; i < infos.size(); i++) {
						final ChatInfo chat = infos.get(i);
						boolean successed = queryAndSetUserData(chat);
						if (!successed) {
							currentIndex = i;
							break;
						}

					}
				}
				topchats();
				if(infos.size()==0){
					tv_meiyou.setVisibility(View.VISIBLE);
				}else{
					tv_meiyou.setVisibility(View.GONE);
				}
				mAdapter = new MainAdapter(mActivity, infos);
				mRecyclerView.setAdapter(mAdapter);

			}

			

			@Override
			public void doInBack() {
				if (!isDoInDB) {
					isDoInDB = true;
					MainActivity.isShowPoint = false;
					infos = DBHelper.getInstance().getWeLearnDB().queryMessageList();
					isDoInDB = false;
				}
			}
		}.excute();
	}
	private void topchats() {
		if (list != null) {
			
			 Iterator<ChatInfo> iter =  infos.iterator();
		        while(iter.hasNext()){
		        	ChatInfo chatInfo= iter.next();
		        	if (chatInfo!=null) {
		        		UserInfoModel user=chatInfo.getUser();
		        		if (user!=null) {
		        			Integer userid=user.getUserid();
			        		if(list.contains(userid)){
			        			map.put(userid, chatInfo);
			        			iter.remove();
			        		}
						}
		        		
					}
		        }
			for(int i=0;i<list.size();i++){
				ChatInfo chatInfo2=map.get(list.get(i));
				if(chatInfo2!=null){
					infos.add(0, chatInfo2);
				}
			}
			map.clear();
		}
	}
	private boolean queryAndSetUserData(ChatInfo chat) {
		UserInfoModel user = DBHelper.getInstance().getWeLearnDB().queryByUserId(chat.getFromuser(), true);

		boolean flag = true;
		if (user != null) {
			chat.setUser(user);
			// mAdapter.setData(infos);
			flag = true;
		} else {
			flag = false;

			WeLearnApi.getContactInfo(getActivity(), chat.getFromuser(), new HttpListener() {
				@Override
				public void onSuccess(int code, String dataJson, String errMsg) {
					if (code == 0) {
						UserInfoModel user = new Gson().fromJson(dataJson, UserInfoModel.class);

						DBHelper.getInstance().getWeLearnDB().insertorUpdate(user.getUserid(), user.getRoleid(),
								user.getName(), user.getAvatar_100());

						if (currentIndex < infos.size()) {
							LogUtils.i(TAG, user.toString());
							infos.get(currentIndex).setUser(user);
						}
						setUser();
						topchats();
						if(infos.size()==0){
							tv_meiyou.setVisibility(View.VISIBLE);
						}else{
							tv_meiyou.setVisibility(View.GONE);
						}
						mAdapter = new MainAdapter(mActivity, infos);
						mRecyclerView.setAdapter(mAdapter);
					} else {
						// ToastUtils.show(mActivity, errMsg +"----是我弹的");
					}
				}

				@Override
				public void onFail(int HttpCode,String errMsg) {
				}
			});
		}
		return flag;
	}

	private void setUser() {
		currentIndex++;
		if (currentIndex < infos.size()) {
			ChatInfo chat = infos.get(currentIndex);
			boolean successed = queryAndSetUserData(chat);
			if (successed) {
				setUser();
			}
		}
	}

	@Override
	public void onPre() {
	}

	@Override
	public void onException() {
	}

	@Override
	public void onAfter(String jsonStr, int msgDef) {
		// int code = JSONUtils.getInt(jsonStr, "code", -1);
		// String errMsg = JSONUtils.getString(jsonStr, "errmsg", "");
		switch (msgDef) {
		case MessageConstant.MSG_DEF_MSGS:
			// if (System.currentTimeMillis() - reflashTime >= 1000) {
			showMessageList();
			reflashTime = System.currentTimeMillis();// 更新消息收到的时间
			// }
			break;
		}
	}

	@Override
	public void onDisConnect() {

	}


	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mCommunicateController != null) {
			mCommunicateController.removeMsgInQueue();
		}
		WelearnHandler.getInstance().removeMessage(MessageConstant.MSG_DEF_MSGS);
	}

	@Override
	public void resultBack(Object... param) {
		super.resultBack(param);

		int flag = ((Integer) param[0]).intValue();
		switch (flag) {
		case RequestConstant.REQUEST_TOPCHATS_CODE:
			if (param.length > 0 && param[1] != null && param[1] instanceof String) {
				String datas = param[1].toString();
				int code = JsonUtil.getInt(datas, "Code", -1);
				String msg = JsonUtil.getString(datas, "Msg", "");
				closeDialog();
				if (code == 0) {
					String dataJson = JsonUtil.getString(datas, "Data", "");

					if (!TextUtils.isEmpty(dataJson)) {
						SharePerfenceUtil.putString("topchats",DateUtil.getBirthString2());
						SharePerfenceUtil.putString("topchats2",dataJson);
						list = JSON.parseArray(dataJson, Integer.class);
						if (list != null) {
							Collections.reverse(list);
						}
					}
				} else {
					ToastUtils.show(msg);
				}

			}

			break;
		}
		if (System.currentTimeMillis() - reflashTime >= 1000) {
			showMessageList();
			reflashTime = System.currentTimeMillis();
		}
	}

	@Override
	public void setOnActivityTouchListener(OnActivityTouchListener listener) {
		this.touchListener = listener;
	}


	private class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
		LayoutInflater inflater;
		List<ChatInfo> modelList;

		public MainAdapter(Context context, List<ChatInfo> list) {
			inflater = LayoutInflater.from(context);
			modelList = new ArrayList<>(list);

		}

		@Override
		public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view = inflater.inflate(R.layout.recycler_row, parent, false);
			return new MainViewHolder(view);
		}

		@Override
		public void onBindViewHolder(MainViewHolder holder, int position) {
			holder.bindData(modelList.get(position));
		}

		@Override
		public int getItemCount() {
			return modelList.size();
		}

		class MainViewHolder extends RecyclerView.ViewHolder {

			private ImageView mAvatar;

			private TextView mTime;

			private TextView mName;

			private TextView mMsgContent;

			private TextView unread_tv;



			private View mAvatar_container;

			public MainViewHolder(View view) {
				super(view);
				mAvatar_container = view.findViewById(R.id.send_msg_user_avatar_frame);
				mAvatar = (ImageView)view.findViewById(R.id.send_msg_user_avatar);

				unread_tv = (TextView)view.findViewById(R.id.send_msg_user_avatar_unread);
				mTime = (TextView)view.findViewById(R.id.send_msg_time);
				mName = (TextView)view.findViewById(R.id.send_msg_user_name);
				mMsgContent = (TextView)view.findViewById(R.id.send_msg_content);

			}

			public void bindData(ChatInfo info) {
				final int fromUserId = info.getFromuser();
				UserInfoModel ug = info.getUser();
				int roleid = 0;
				if (info.isReaded()) {
					unread_tv.setVisibility(View.GONE);
				} else {
					unread_tv.setVisibility(View.VISIBLE);
					int unReadCount = info.getUnReadCount();
					String str = "" + unReadCount;
					if (unReadCount > 99) {
						str = "99+";
					}
					unread_tv.setText(str);
				}
				if (ug != null) {
					int defaultOrErrorAvatarId = R.drawable.ic_default_avatar;
					switch (fromUserId) {
						case GlobalContant.USER_ID_SYSTEM:
							defaultOrErrorAvatarId = R.drawable.information_default_company_icon;
							break;
						case GlobalContant.USER_ID_HELPER:
							defaultOrErrorAvatarId = R.drawable.information_answer_assistant_icon;
							break;
					}
					// ImageLoader.getInstance().loadImage(ug.getAvatar_100(), mAvatar,
					// defaultOrErrorAvatarId,
					// defaultOrErrorAvatarId, avatarSize, avatarSize / 10);

					Glide.with(mActivity).load(ug.getAvatar_100())
							.diskCacheStrategy(DiskCacheStrategy.ALL)
							.bitmapTransform(new CropCircleTransformation(mActivity.getApplicationContext()))
							.placeholder(R.drawable.default_icon_circle_avatar).into(mAvatar);

					mName.setText(ug.getName());
				} else {
					// ImageLoader.getInstance().loadImage(null, mAvatar,
					// R.drawable.ic_default_avatar,
					// R.drawable.ic_default_avatar, avatarSize, avatarSize / 10);

					Glide.with(mActivity).load("")
							.diskCacheStrategy(DiskCacheStrategy.ALL)
							.bitmapTransform(new CropCircleTransformation(mActivity.getApplicationContext()))
							.placeholder(R.drawable.teacher_img).into(mAvatar);
					mName.setText("");
				}

				switch (fromUserId) {
					case GlobalContant.USER_ID_SYSTEM:
					case GlobalContant.USER_ID_HELPER:
						roleid = 0;
						mAvatar_container.setEnabled(false);
						break;
					default:
						if (ug != null) {
							roleid = ug.getRoleid();
							mAvatar_container.setEnabled(true);
						}
						break;
				}
				final int finalRoleid = roleid;
				mAvatar_container.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Bundle data = new Bundle();
						data.putInt("userid", fromUserId);
						data.putInt("roleid", finalRoleid);
						if (finalRoleid == GlobalContant.ROLE_ID_COLLEAGE) {
							IntentManager.goToTeacherInfoView(mActivity, data);
						} else if (finalRoleid == GlobalContant.ROLE_ID_STUDENT) {
							IntentManager.goToStudentInfoView(mActivity, data);
						}

					}
				});

				switch (info.getContenttype()) {
					case MessageConstant.MSG_CONTENT_TYPE_TEXT:
					case MessageConstant.MSG_CONTENT_TYPE_JUMP:
					case MessageConstant.MSG_CONTENT_TYPE_JUMP_URL:
						mMsgContent.setText(info.getMsgcontent());
						break;
					case MessageConstant.MSG_CONTENT_TYPE_AUDIO:
						mMsgContent.setText(getResources().getString(R.string.text_default_audio));
						break;
					case MessageConstant.MSG_CONTENT_TYPE_IMG:
						mMsgContent.setText(getResources().getString(R.string.text_default_image));
						break;
				}
				if (info.getType() == GlobalContant.MSG_TYPE_RECV) {
					mTime.setText(DateUtil.getDisplayChatTimeWithOutSeconds(
							new Date(DateUtil.convertTimestampToLong((float)info.getTimestamp() * 1000))));
				} else {
					mTime.setText(
							DateUtil.getDisplayChatTimeWithOutSeconds(new Date(info.getLocalTimestamp())));
				}
			}
		}
	}
}
