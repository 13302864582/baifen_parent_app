
package com.daxiong.fun.function.communicate;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.adapter.ChatListAdapter;
import com.daxiong.fun.api.WeLearnApi;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.callback.INetWorkListener;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.constant.MessageConstant;
import com.daxiong.fun.constant.ResponseCmdConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.dispatch.ChatMsgController;
import com.daxiong.fun.dispatch.WelearnHandler;
import com.daxiong.fun.function.question.PayAnswerImageGridActivity;
import com.daxiong.fun.function.question.PayAnswerQuestionDetailActivity;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.ChatInfo;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.receiver.HeadsetPlugReceiver;
import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.util.ImageUtil;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MediaUtil;
import com.daxiong.fun.util.MediaUtil.RecordCallback;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.util.UiUtil;
import com.daxiong.fun.util.WeLearnFileUtil;
import com.daxiong.fun.view.AndroidBug5497Workaround;
import com.daxiong.fun.view.XListView;
import com.daxiong.fun.view.XListView.IXListViewListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ChatMsgViewActivity extends BaseActivity implements OnClickListener, OnTouchListener, 
INetWorkListener,IXListViewListener, HttpListener {

    private static final String TAG = ChatMsgViewActivity.class.getSimpleName();

    private TextView title;

    private XListView mChatList;

    private ImageButton mTypeChoiceBtn;

    private ImageView mSendMsgBtn;

    private EditText mMsgSendText;

    private ImageView mTextVoiceChoice;

    private Button mSendVoiceMsgBtn;

    private SendVoiceMsgCallback mCallback;

    private InputMethodManager mImm;

    private LinearLayout mChatPhotoContainer;

    private ImageView mChatCameraBtn;

    private ImageView mChatAlbumBtn;

    private RelativeLayout mMsgSendContainer;

    private TextView mCopyView;

    private PopupWindow copyTextPop;

    private PopupWindow savePicPop;

    private PopupWindow vodPop;

    public static final String USER_NAME = "username";

    public static final String USER_ID = "userid";

    private NotificationManager notificationManager;

    private HeadsetPlugReceiver headsetPlugReceiver;

    private ChatListAdapter mAdapter;

    private List<ChatInfo> mChatInfoList = new ArrayList<ChatInfo>();

    protected boolean isShowPoP;

    private List<ChatInfo> reChats = new ArrayList<ChatInfo>();

    private static HashMap<Double, ChatInfo> map;

    public ChatMsgController mController;

    private String titleName;

    private int userid;
    private LinearLayout ll,ll2;
    private int pageIndex = 0;

    private static boolean isFromNoti;

    private boolean mIsVoiceMsg = false;// 是否语音消息

    private boolean isShowPhotoMenu = false;// 是否显示图片菜单

    private boolean isCancel;

    private String audioName;

    private long audioTime;

    private int position;

    private Bitmap mSaveBm;

    private String mSavePath;

    private int deletePosition;

    private boolean isQueryAll = false;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unchecked")
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x01:
                    mChatInfoList = (List<ChatInfo>)msg.obj;
                    if (mChatInfoList != null && mChatInfoList.size() > 0) {
                        UserInfoModel user = DBHelper.getInstance().getWeLearnDB()
                                .queryByUserId(userid, true);
                        if (user == null) {
                            isQueryAll = true;
                            WeLearnApi.getContactInfo(ChatMsgViewActivity.this, userid,
                                    ChatMsgViewActivity.this);
                        } else {
                            title.setText(user.getName());
                            Iterator<ChatInfo> chk_it = mChatInfoList.iterator();
    						while (chk_it.hasNext()) {
    							ChatInfo chatInfo = chk_it.next();
    							if ((chatInfo.getContenttype() == MessageConstant.MSG_CONTENT_TYPE_TEXT
    									| chatInfo.getContenttype() == MessageConstant.MSG_CONTENT_TYPE_JUMP)
    									&& TextUtils.isEmpty(chatInfo.getMsgcontent())) {
    								chk_it.remove();
    							}
    						}
                            setChatInfoList(user);
                        }
                    }
                    break;
                case 555:
                    double timestamp = (Double)msg.obj;
                    if (map.containsKey(timestamp)) {
                        ChatInfo chatInfo = map.remove(timestamp);
                        if (chatInfo != null) {
                            chatInfo.setSendFail(true);
                            mAdapter.setData(mChatInfoList);
                            ToastUtils.show(R.string.text_send_timeout);
                            DBHelper.getInstance().getWeLearnDB().update(chatInfo);
                        }
                    }
                    // sendFail();
                    break;
            }
        };
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.fragment_chat_view);
        AndroidBug5497Workaround.assistActivity(this);
        initObject();
        initView();

        initListener();
        ExtraOperation();
        showChatInfoList();
        copyAndDelPopView();

    }

    public void initObject() {
        if (map == null) {
            map = new HashMap<Double, ChatInfo>();
        }
        mImm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        // add headset receiver
        registerHeadsetPlugReceiver();
        mCallback = new SendVoiceMsgCallback();
    }

    @Override
    public void initView() {
        super.initView();
        title = (TextView)this.findViewById(R.id.title);
        mChatList = (XListView)this.findViewById(R.id.chat_list);
        mTypeChoiceBtn = (ImageButton)this.findViewById(R.id.send_img_msg);
        mSendMsgBtn = (ImageView)this.findViewById(R.id.send_msg_btn);
        mMsgSendText = (EditText)this.findViewById(R.id.msg_send_content);
        mTextVoiceChoice = (ImageView)this.findViewById(R.id.chat_text_voice_choice);
        mSendVoiceMsgBtn = (Button)this.findViewById(R.id.send_voice_msg_btn);
        mChatPhotoContainer = (LinearLayout)this.findViewById(R.id.chat_photo_container);
        mChatCameraBtn = (ImageView)this.findViewById(R.id.chat_camera_btn);
        mChatAlbumBtn = (ImageView)this.findViewById(R.id.chat_album_btn);
        mMsgSendContainer = (RelativeLayout)this.findViewById(R.id.msg_send_container);
        ll = (LinearLayout) this.findViewById(R.id.ll);
        ll2 = (LinearLayout) this.findViewById(R.id.ll2);
        titleName = getIntent().getStringExtra(USER_NAME);
        // mActionBar.setTitle(name);
        title.setText(titleName);

        mAdapter = new ChatListAdapter(this);
        mChatList.setAdapter(mAdapter);

        if (!mIsVoiceMsg) {
            showSendMsgTextView();
        }
        if (isShowPhotoMenu) {
            mChatPhotoContainer.setVisibility(View.GONE);
        }

    }

    @Override
    public void initListener() {
        super.initListener();
        this.findViewById(R.id.back_layout).setOnClickListener(this);
        mChatList.setPullLoadEnable(false);
        mChatList.setPullRefreshEnable(true);
        mChatList.setXListViewListener(this);
        mChatList.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                hidePhotoContainer(true);
                disMissPop();
                return false;
            }
        });
        ll.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				int heightDiff = ll.getRootView().getHeight() - ll.getHeight();
				if (heightDiff > 100) {
					// 键盘弹出
					
					mChatPhotoContainer.setVisibility(View.GONE);
				} else {

				}
			}
		});
        mMsgSendText.addTextChangedListener(mWatcher);
        mMsgSendText.setOnClickListener(this);
        mTextVoiceChoice.setOnClickListener(this);
        mSendVoiceMsgBtn.setOnTouchListener(this);
        mSendMsgBtn.setOnClickListener(this);
        mTypeChoiceBtn.setOnClickListener(this);
        mChatCameraBtn.setOnClickListener(this);
        mChatAlbumBtn.setOnClickListener(this);
    }

    private void ExtraOperation() {
        Intent intent = getIntent();
        if (intent != null) {
            userid = intent.getIntExtra(USER_ID, 0);
            isFromNoti = intent.getBooleanExtra("isFromNoti", false);
            if (GlobalVariable.mChatMsgViewActivity != null) {
                if (isFromNoti && GlobalVariable.mChatMsgViewActivity != this) {
                    if (GlobalVariable.mChatMsgViewActivity.mController != null) {
                        GlobalVariable.mChatMsgViewActivity.mController.removeMsgInQueue();
                    }
                    WelearnHandler.getInstance().removeMessage(MessageConstant.MSG_DEF_CHAT_LIST);
                    WelearnHandler.getInstance().removeMessage(MessageConstant.MSG_DEF_MSG_RESULT);
                }
            }
            if (userid != 0) {
                MyApplication.currentUserId = userid;
            } else {
                userid = MyApplication.currentUserId;
            }
            String imagePath = intent.getStringExtra(PayAnswerImageGridActivity.IMAGE_PATH);
            if (!TextUtils.isEmpty(imagePath) && WeLearnFileUtil.isFileExist(imagePath)) {
                // Log.i(TAG, "----------------" + imagePath);
                sendImageMsg(imagePath);
            }
        }

        if (MyApplication.notifiFromUser == userid) {// 发现消息栏有通知
            MyApplication.notifiFromUser = 0;
            notificationManager.cancel(0x0a);// 点击一下隐藏通知
        }
        if (userid == GlobalContant.USER_ID_HELPER || userid == GlobalContant.USER_ID_SYSTEM) {
            mMsgSendContainer.setVisibility(View.GONE);
        } else {
            mMsgSendContainer.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 复制和删除的view
     * 
     * @author: sky void
     */
    private void copyAndDelPopView() {
        View copyTextPopView = View.inflate(this, R.layout.copy_text_chat_pop, null);
        copyTextPopView.findViewById(R.id.copy_text_btn_chat).setOnClickListener(this);
        copyTextPopView.findViewById(R.id.delete_text_btn_chat).setOnClickListener(this);
        copyTextPop = new PopupWindow(copyTextPopView, // coyp_btn.getWidth(),
                                                       // coyp_btn.getHeight());
                DensityUtil.dip2px(this, 101), DensityUtil.dip2px(this, 46));

        View vodPopView = View.inflate(this, R.layout.vod_chat_pop, null);
        vodPopView.findViewById(R.id.delete_vod_btn_chat).setOnClickListener(this);
        vodPop = new PopupWindow(vodPopView, // coyp_btn.getWidth(),
                                             // coyp_btn.getHeight());
                DensityUtil.dip2px(this, 60), DensityUtil.dip2px(this, 46));

        View savePicPopView = View.inflate(this, R.layout.save_pic_chat_pop, null);
        savePicPopView.findViewById(R.id.save_pic_btn_chat).setOnClickListener(this);
        savePicPopView.findViewById(R.id.delete_pic_btn_chat).setOnClickListener(this);
        savePicPop = new PopupWindow(savePicPopView, // coyp_btn.getWidth(),
                                                     // coyp_btn.getHeight());
                DensityUtil.dip2px(this, 144), DensityUtil.dip2px(this, 46));
    }

    @Override
    public void onResume() {
        super.onResume();
        // Log.i(TAG, "---onResume---");
        GlobalVariable.mChatMsgViewActivity = this;
        MobclickAgent.onEventBegin(this, "OpenChat");
        MyApplication.isInChatMsgView = true;
        if (mController == null) {
            mController = new ChatMsgController(null, this);
        }

        if (MyApplication.notifiFromUser == userid) {// 发现消息栏有通知
            notificationManager.cancel(0x0a);// 隐藏通知
            MyApplication.notifiFromUser = 0;
            pageIndex = 0;
            showChatInfoList();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MyApplication.isInChatMsgView = false;
        MobclickAgent.onEventEnd(this, "OpenChat");
    }

    /**
     * 复制文字
     * 
     * @param rootView
     */
    public void showPop(TextView rootView, int position) {
        disMissPop();
        mCopyView = rootView;
        deletePosition = position;
        isShowPoP = true;
        copyTextPop.showAsDropDown(rootView, DensityUtil.dip2px(this, -10),
                DensityUtil.dip2px(this, -94));
        // pop.isOutsideTouchable();
        copyTextPop.setOutsideTouchable(true);
    }

    /**
     * 长按语音
     * 
     * @param rootView
     */
    public void showVodPop(View rootView, int position) {
        disMissPop();
        deletePosition = position;
        isShowPoP = true;
        vodPop.showAsDropDown(rootView, DensityUtil.dip2px(this, -10),
                DensityUtil.dip2px(this, -94));
        vodPop.setOutsideTouchable(true);
    }

    public void showSavePicPop(View rootView, Bitmap saveBm, String path, int position) {
        disMissPop();
        deletePosition = position;
        // mSaveView = rootView;
        mSaveBm = saveBm;
        mSavePath = path;
        isShowPoP = true;
        savePicPop.showAsDropDown(rootView, DensityUtil.dip2px(this, -10),
                DensityUtil.dip2px(this, -100));
        // pop.isOutsideTouchable();
        savePicPop.setOutsideTouchable(true);
    }

    /**
     * 隐藏pop
     */
    public void disMissPop() {
        if (copyTextPop != null) {
            copyTextPop.dismiss();
        }
        if (vodPop != null) {
            vodPop.dismiss();
        }
        if (savePicPop != null) {
            savePicPop.dismiss();
        }
        isShowPoP = false;
    }

    /**
     * 发送后更新ui(显示该聊天发送消息)
     * 
     * @author qinhaowen
     * @param recodrTime
     * @param path
     * @param msgcontent
     * @param contentType
     * @return
     */
    private double refreshUI(int recodrTime, String path, String msgcontent, int contentType) {
        UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
        if (null != uInfo) {
            ChatInfo chatInfo = getChatInfo(uInfo.getAvatar_100(), uInfo.getName(), msgcontent,
                    GlobalContant.MSG_TYPE_SEND, userid, contentType);
            double timestamp = (double)chatInfo.getLocalTimestamp() / 1000;
            if (!TextUtils.isEmpty(path)) {
                if (!WeLearnFileUtil.isFileExist(path)) {
                    WeLearnFileUtil.decodeFileByBase64(msgcontent, path);
                }
                chatInfo.setPath(path);
                if (recodrTime > 0) {
                    chatInfo.setAudiotime(recodrTime);
                }
            }

            LogUtils.i(TAG, mChatInfoList.toString());

            mChatInfoList.add(chatInfo);
            mAdapter.setData(mChatInfoList);
            mChatList.setSelection(mChatList.getLastVisiblePosition());
            chatInfo.setReaded(true);

            DBHelper.getInstance().getWeLearnDB().insertMsg(chatInfo);

            Message msg = Message.obtain();
            msg.what = 555;
            msg.obj = timestamp;
            map.put(timestamp, chatInfo);

            int delay = 10000;
            if (contentType == MessageConstant.MSG_CONTENT_TYPE_IMG
                    || contentType == MessageConstant.MSG_CONTENT_TYPE_AUDIO) {
                delay = 15000;
            }
            mHandler.sendMessageDelayed(msg, delay);
            // Log.i(TAG, mChatInfoList.toString());
            return timestamp;
        } else {

            IntentManager.goToGuideActivity(this);

        }
        return 0;
    }

    private void showSendMsgTextView() {
    	mSendMsgBtn.setVisibility(View.VISIBLE);
        mTextVoiceChoice.setImageResource(R.drawable.bg_chat_voice_selector);
        mSendVoiceMsgBtn.setVisibility(View.GONE);
        mMsgSendText.setVisibility(View.VISIBLE);
    }

    private void showChatInfoList() {
        mChatInfoList = DBHelper.getInstance().getWeLearnDB().queryAllByUserid(userid, pageIndex++);
        Message msg = mHandler.obtainMessage();
        msg.obj = mChatInfoList;
        msg.what = 0x01;
        mHandler.sendMessage(msg);
    }

    // 设置聊天列表数据
    private void setChatInfoList(UserInfoModel user) {
        for (int i = 0; i < mChatInfoList.size(); i++) {
            if (mChatInfoList.get(i).getType() == GlobalContant.MSG_TYPE_RECV) {
                setUserData(mChatInfoList.get(i), user);
            }
        }
        // pageIndex = pageIndex * 20;
        Collections.reverse(mChatInfoList);
        mAdapter.setData(mChatInfoList);
        scrollChatListToBottom();
    }

    private ChatInfo setUserData(ChatInfo chat, UserInfoModel user) {
        chat.setUser(user);
        return chat;
    }

    // 设置单个聊天数据
    private void setChatData(ChatInfo chat, UserInfoModel user) {
        if (chat == null) {
            return;
        }
        chat = setUserData(chat, user);

        boolean insertMsg = DBHelper.getInstance().getWeLearnDB().insertMsg(chat);

        if (insertMsg) {
            if (AppConfig.IS_DEBUG) {
                ToastUtils.show("接收消息入库");
            }
            mChatInfoList.add(chat);
            mAdapter.setData(mChatInfoList);
        }
        scrollChatListToBottom();
    }

    private void scrollChatListToBottom() {
        mChatList.setSelection(mAdapter.getCount() - 1);
    }

    private void showSendVoiceMsgView() {
    	mSendMsgBtn.setVisibility(View.GONE);
        mTextVoiceChoice.setImageResource(R.drawable.bg_chat_text_selector);
        mSendVoiceMsgBtn.setVisibility(View.VISIBLE);
        mMsgSendText.setVisibility(View.GONE);
        mImm.hideSoftInputFromWindow(mMsgSendText.getWindowToken(), 0);
    }

    private void hidePhotoContainer(boolean isHideKeyboard) {
        mChatPhotoContainer.setVisibility(View.GONE);
        disMissPop();
        isShowPhotoMenu = false;
        if (isHideKeyboard) {
            mImm.hideSoftInputFromWindow(mMsgSendText.getWindowToken(), 0);
        }
    }

    /**
     * 发送语音聊天
     * 
     * @param recodrTime
     */
    private void sendAudioMsg(float recodrTime) {
        String path = WeLearnFileUtil.getVoiceFile().getAbsolutePath() + File.separator + audioName
                + ".amr";
        String msgcontent = WeLearnFileUtil.encodeFileByBase64(path);
        if (!TextUtils.isEmpty(msgcontent)) {
            int audtioTime = Math.round(recodrTime);
            double timestamp = refreshUI(audtioTime, path, msgcontent,
                    MessageConstant.MSG_CONTENT_TYPE_AUDIO);
            WeLearnApi.sendMsg(MessageConstant.MSG_CONTENT_TYPE_AUDIO, userid, msgcontent, audtioTime,
                    MessageConstant.MSG_DEF_MSG_RESULT, timestamp);
        }
    }

    /**
     * 发送文字聊天
     */
    private void sendTextMsg() {
        String msgcontent = mMsgSendText.getText().toString();
        if (TextUtils.isEmpty(msgcontent.trim())) {
            ToastUtils.show(getString(R.string.text_toast_empty_msgcontent));
            return;
        }
        double timestamp = refreshUI(0, null, msgcontent, MessageConstant.MSG_CONTENT_TYPE_TEXT);
        WeLearnApi.sendMsg(MessageConstant.MSG_CONTENT_TYPE_TEXT, userid, msgcontent, 0,
                MessageConstant.MSG_DEF_MSG_RESULT, timestamp);
        mMsgSendText.setText("");
    }

    /**
     * 发送图片聊天
     */
    public void sendImageMsg(String path) {
        path = ImageUtil.compressImage(path);
        String msgcontent = WeLearnFileUtil.encodeFileByBase64(path);
        // Log.i(TAG, "====" + path);

        // refreshUI(0, path, msgcontent, GlobalContant.MSG_CONTENT_TYPE_IMG);
        double timestamp = refreshUI(0, path, msgcontent, MessageConstant.MSG_CONTENT_TYPE_IMG);
        WeLearnApi.sendMsg(MessageConstant.MSG_CONTENT_TYPE_IMG, userid, msgcontent, 0,
                MessageConstant.MSG_DEF_MSG_RESULT, timestamp);
    }

    private ChatInfo getChatInfo(String avatar100, String name, String msgcontent, int type,
            int userid, int contentType) {
        ChatInfo chat = new ChatInfo();
        UserInfoModel user = new UserInfoModel();

        user.setAvatar_100(avatar100);
        user.setName(name);
        // chat.setSendFail(true);
        chat.setUser(user);
        chat.setMsgcontent(msgcontent);
        chat.setLocalTimestamp(System.currentTimeMillis());
        chat.setFromuser(userid);
        chat.setType(type);
        chat.setContenttype(contentType);
        return chat;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View v) {
        disMissPop();
        int roleId = MySharePerfenceUtil.getInstance().getUserRoleId();
        switch (v.getId()) {
            case R.id.back_layout:// 返回
                leave();
                // getActivity().finish();
                break;
            case R.id.chat_text_voice_choice:
                MobclickAgent.onEvent(this,"Send_Chat");
                mIsVoiceMsg = !mIsVoiceMsg;
                if (mIsVoiceMsg) {
                    showSendVoiceMsgView();
                } else {
                    showSendMsgTextView();
                }
                hidePhotoContainer(true);
                break;
            case R.id.send_msg_btn:
                MobclickAgent.onEvent(this,"Send_Chat");
                sendTextMsg();
                if (roleId == GlobalContant.ROLE_ID_COLLEAGE) {// 老师发送消息
                    MobclickAgent.onEvent(this, "TecherChat");
                } else {// 学生发送消息
                    MobclickAgent.onEvent(this, "StudentChat");
                }
                break;
            case R.id.msg_send_content:
                MobclickAgent.onEvent(this,"Send_Chat");
                hidePhotoContainer(false);
                break;
            case R.id.send_img_msg:
                MobclickAgent.onEvent(this,"Send_Chat");
                if (roleId == GlobalContant.ROLE_ID_COLLEAGE) {// 老师发送消息
                    MobclickAgent.onEvent(this, "TecherChat");
                } else {// 学生发送消息
                    MobclickAgent.onEvent(this, "StudentChat");
                }
                isShowPhotoMenu = !isShowPhotoMenu;
                if (isShowPhotoMenu) {
                    mChatPhotoContainer.setVisibility(View.VISIBLE);
                } else {
                    mChatPhotoContainer.setVisibility(View.GONE);
                }
                mImm.hideSoftInputFromWindow(mMsgSendText.getWindowToken(), 0);
                break;
            case R.id.chat_camera_btn:// 拍照
                IntentManager.startImageCapture(this, GlobalContant.SEND_IMG_MSG);
                break;
            case R.id.chat_album_btn:// 相册;
                Bundle data = new Bundle();
                data.putInt("tag", GlobalContant.SEND_IMG_MSG);
                data.putInt(USER_ID, userid);
                data.putString(USER_NAME, titleName);
                IntentManager.goToAlbumView(this, data);
                break;
            case R.id.copy_text_btn_chat:// 复制文字
                ClipboardManager c = (ClipboardManager)this
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                if (mCopyView != null) {
                    CharSequence text = mCopyView.getText();
                    c.setText(text);
                    if (AppConfig.IS_DEBUG) {
                        ToastUtils.show("成功复制:\"" + text + "\"到剪贴板");
                    }
                }
                break;
            case R.id.save_pic_btn_chat:
                if (!WeLearnFileUtil.sdCardIsAvailable()) {// 没有SD卡
                    ToastUtils.show(this.getString(R.string.text_toast_sdcard_not_available));
                    return;
                }
                if (!WeLearnFileUtil.sdCardHasEnoughSpace()) {// SD卡空间不足
                    ToastUtils.show(this.getString(R.string.text_toast_have_not_enough));
                    return;
                }
                String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                        + File.separator + "welearnimg" + File.separator;
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdirs();
                }
                mSavePath = mSavePath.replaceAll(File.separator + "chat" + File.separator + ".", "")
                        + ".png";
                // new Md5FileNameGenerator().generate(imageUri);
                // path = path +5+".png";
                ImageUtil.saveFile(mSavePath, mSaveBm);
                if (AppConfig.IS_DEBUG) {
                }
                ToastUtils.show(getString(R.string.text_image_saved_path, path), 1);
                break;

            case R.id.delete_text_btn_chat:
            case R.id.delete_pic_btn_chat:
            case R.id.delete_vod_btn_chat:
                if (mChatInfoList.size() > deletePosition) {
                    ChatInfo chat = mChatInfoList.remove(deletePosition);
                    mAdapter.setData(mChatInfoList);
                    DBHelper.getInstance().getWeLearnDB().deleteMsgInChatView(chat);
                }
                break;
        }
    }

    @Override
    public void onAfter(String jsonStr, int msgDef) {
        int code = JsonUtil.getInt(jsonStr, "code", -1);
        // String errMsg = JSONUtils.getString(jsonStr, "errmsg", "");
        if (msgDef == MessageConstant.MSG_DEF_CHAT_LIST) {// 接收聊天消息
            // Log.e("收到聊天消息", jsonStr);
            isQueryAll = false;

            ChatInfo chat = new Gson().fromJson(jsonStr, ChatInfo.class);
            chat.setType(GlobalContant.MSG_TYPE_RECV);
            String noticetype = chat.getNoticetype();
            boolean isReaded = false;
            if (!TextUtils.isEmpty(noticetype) && noticetype.length() == 3) {
                int isRed = Integer.parseInt(noticetype.charAt(0) + "");
                if (isRed == 0) {
                    isReaded = true;
                } else {
                    isReaded = false;
                }
            } else {
                isReaded = true;
            }
            chat.setReaded(isReaded);

            // 如果是接收的语音消息或者图片消息,则解码文件
            if (chat.getContenttype() == MessageConstant.MSG_CONTENT_TYPE_AUDIO) {
                String audioPath = WeLearnFileUtil.getVoiceFile().getAbsolutePath() + File.separator
                        + UUID.randomUUID().toString() + ".amr";
                // Log.i(TAG, "====" + audioPath);
                if (!WeLearnFileUtil.isFileExist(audioPath)) {
                    WeLearnFileUtil.decodeFileByBase64(chat.getMsgcontent(), audioPath);
                }
                chat.setPath(audioPath);
            } else if (chat.getContenttype() == MessageConstant.MSG_CONTENT_TYPE_IMG) {
                String imgPath = WeLearnFileUtil.getImgMsgFile().getAbsolutePath() + File.separator
                        + UUID.randomUUID().toString() + ".png";
                if (!WeLearnFileUtil.isFileExist(imgPath)) {
                    WeLearnFileUtil.decodeFileByBase64(chat.getMsgcontent(), imgPath);
                }
                chat.setPath(imgPath);
            } else if (chat.getContenttype() == MessageConstant.MSG_CONTENT_TYPE_JUMP) {

            } else if (chat.getContenttype() == MessageConstant.MSG_CONTENT_TYPE_TEXT) {
            }
            // 新开线程进行db存储
            int userid = chat.getFromuser();
            // 根据id查找次用户信息是否已经保存在本地了
            // 如果没有保存在本地,则调用接口去拉取,然后保存在本地

            UserInfoModel user = DBHelper.getInstance().getWeLearnDB().queryByUserId(userid, true);

            if (user == null) {
                WeLearnApi.getContactInfo(ChatMsgViewActivity.this, userid,
                        ChatMsgViewActivity.this);
                reChats.add(chat);
            } else {
                setChatData(chat, user);
            }
            if (AppConfig.IS_DEBUG) {
                ToastUtils.show("接收消息成功");
            }
        } else if (msgDef == MessageConstant.MSG_DEF_MSG_RESULT) {
            if (code == ResponseCmdConstant.CODE_RETURN_OK) {
                // Log.e("收到聊天消息回包:", jsonStr);
                double timestamp = JsonUtil.getDouble(jsonStr, "timestamp", 0);
                // Log.e("判断两个时间戳:", (sendT == revT) +"");
                if (AppConfig.IS_DEBUG) {
                    ToastUtils.show("接收到消息成功回包");
                }
                if (map.containsKey(timestamp)) {

                    ChatInfo chatInfo = map.remove(timestamp);
                    chatInfo.setSendFail(false);
                    if (AppConfig.IS_DEBUG) {
                        // chatInfo.setSendFail(true);
                        ToastUtils.show("消息入库成功");
                    }
                    mAdapter.setData(mChatInfoList);
                    DBHelper.getInstance().getWeLearnDB().update(chatInfo);
                }
            }
        }

    }

    /**
     * 发送失败后重新发送
     */
    public void resend(ChatInfo chatInfo) {
        // this.chatInfo = chatInfo;
        String msgContent = chatInfo.getMsgcontent();
        if (TextUtils.isEmpty(msgContent)) {
            if (!TextUtils.isEmpty(chatInfo.getPath())
                    && WeLearnFileUtil.isFileExist(chatInfo.getPath())) {
                msgContent = WeLearnFileUtil.encodeFileByBase64(chatInfo.getPath());
            }
        }
        if (mController == null) {
            mController = new ChatMsgController(null, this);
        }
        chatInfo.setSendFail(false);
        double timestamp = (double)chatInfo.getLocalTimestamp() / 1000;
        mAdapter.setData(mChatInfoList);
        Message msg = Message.obtain();
        msg.what = 555;
        msg.obj = timestamp;
        if (map.containsKey(timestamp)) {
            map.remove(timestamp);
        }
        map.put(timestamp, chatInfo);
        int delay = 10000;
        int contentType = chatInfo.getContenttype();
        if (contentType == MessageConstant.MSG_CONTENT_TYPE_IMG
                || contentType == MessageConstant.MSG_CONTENT_TYPE_AUDIO) {
            delay = 15000;
        }
        // Log.e("重发的时间戳:", timestamp+"");
        mHandler.sendMessageDelayed(msg, delay);
        // WeLearnApi.sendMsg(contentType, userid, chatInfo.getMsgcontent(),
        // chatInfo.getAudiotime(),
        // MessageConstant.MSG_DEF_MSG_RESULT, timestamp);
        WeLearnApi.sendMsg(contentType, userid, msgContent, chatInfo.getAudiotime(),
                MessageConstant.MSG_DEF_MSG_RESULT, timestamp);
    }

    @Override
    public void onPre() {

    }

    @Override
    public void onException() {

    }

    @Override
    public void onDisConnect() {
        Set<Double> keySet = map.keySet();

        Iterator<Double> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            Double timestamp = iterator.next();
            ChatInfo chatInfo = map.get(timestamp);
            iterator.remove();
            if (chatInfo != null) {
                chatInfo.setSendFail(true);
                DBHelper.getInstance().getWeLearnDB().update(chatInfo);
            }
        }
        mAdapter.setData(mChatInfoList);

        mHandler.removeMessages(555);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        float startEventY = 0.0f;
        float currentEventY = 0.0f;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                audioTime = System.currentTimeMillis();
                mSendVoiceMsgBtn.setBackgroundResource(R.drawable.bg_input_voice_btn_pressed);
                mSendVoiceMsgBtn.setText(getString(R.string.text_cancel_end));
                startEventY = event.getY();
                audioName = UUID.randomUUID().toString();
                MediaUtil.getInstance(true).record(audioName, mCallback, this);
                break;
            case MotionEvent.ACTION_MOVE:
                currentEventY = event.getY();
                float distance = currentEventY - startEventY;
                if (Math.abs(distance) > 200) {
                    isCancel = true;
                    mSendVoiceMsgBtn.setText(getString(R.string.text_cancel_send));
                    MediaUtil.getInstance(true).setIsCancel(isCancel);
                    UiUtil.getInstance().showCancelSendDialog();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                boolean isTooShort = System.currentTimeMillis() - audioTime < 1000;
                if (isTooShort) {
                    isCancel = true;
                    MediaUtil.getInstance(true).setIsCancel(isCancel);
                    // WeLearnUiUtil.getInstance().closeDialog();
                    UiUtil.getInstance().showWarnDialogWhenRecordVoice();
                } else {
                }
                mSendVoiceMsgBtn.setBackgroundResource(R.drawable.bg_input_voice_btn_normal);
                mSendVoiceMsgBtn.setText(getString(R.string.text_input_voice_btn));
                MediaUtil.getInstance(true).stopRecord(0, mCallback);
                break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GlobalContant.CAPTURE_IMAGE_REQUEST_CODE_SEND_IMG) {
            if (resultCode == RESULT_OK) {

                String path = WeLearnFileUtil.getImgMsgFile().getAbsolutePath() + File.separator
                        + "publish.png";

                Bundle bund = new Bundle();
                bund.putString(PayAnswerQuestionDetailActivity.IMG_PATH, path);
                IntentManager.goToPreSendPicReViewActivity(this, bund, false);
            }
        }
    }

    @Override
    public void onSuccess(int code, String dataJson, String errMsg) {
        if (code == 0) {
            UserInfoModel uInfo = new Gson().fromJson(dataJson, UserInfoModel.class);

            if (null == uInfo) {
                return;
            }

            DBHelper.getInstance().getWeLearnDB().insertorUpdate(uInfo.getUserid(),
                    uInfo.getRoleid(), uInfo.getName(), uInfo.getAvatar_100());

            title.setText(uInfo.getName());
            if (isQueryAll) {
                setChatInfoList(uInfo);
            } else {
                if (null != reChats && reChats.size() > 1) {
                    ChatInfo ci = null;
                    try {
                        ci = reChats.remove(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (null != ci) {
                        setChatData(ci, uInfo);
                    }
                }
            }
        } else {
            ToastUtils.show(errMsg);
        }
    }

    @Override
    public void onFail(int HttpCode,String errMsg) {
    }

    private class SendVoiceMsgCallback implements RecordCallback {

        @Override
        public void onAfterRecord(float recodrTime) {
            if (isCancel) {
                WeLearnFileUtil.deleteFile(audioName);
                isCancel = false;
                MediaUtil.getInstance(true).setIsCancel(isCancel);
            } else {
                sendAudioMsg(recodrTime);
                int roleId = MySharePerfenceUtil.getInstance().getUserRoleId();
                if (roleId == GlobalContant.ROLE_ID_COLLEAGE) {// 老师发送消息
                    MobclickAgent.onEvent(ChatMsgViewActivity.this, "TecherChat");
                } else {// 学生发送消息
                    MobclickAgent.onEvent(ChatMsgViewActivity.this, "StudentChat");
                }
            }
        }

    }

    private TextWatcher mWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().length() > 0) {
//                mTypeChoiceBtn.setVisibility(View.GONE);
                mSendMsgBtn.setVisibility(View.VISIBLE);
                mSendMsgBtn.setBackgroundResource(R.drawable.bg_send_msg_btn_selector);
            } else {
//                mTypeChoiceBtn.setVisibility(View.VISIBLE);
//                mSendMsgBtn.setVisibility(View.GONE);
                mSendMsgBtn.setVisibility(View.VISIBLE);
                mSendMsgBtn.setBackgroundResource(R.drawable.send_msgs_pre);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if(pageIndex>1){
                scrollChatListToBottom();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void registerHeadsetPlugReceiver() {
        headsetPlugReceiver = new HeadsetPlugReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        getApplication().registerReceiver(headsetPlugReceiver, intentFilter);
    }

    public void onLoadFinish() {
        mChatList.stopRefresh();
    }

    @Override
    public void onRefresh() {
        List<ChatInfo> currentList = DBHelper.getInstance().getWeLearnDB().queryAllByUserid(userid,
                pageIndex++);
        position = currentList.size();
        if (position > 0) {
            // pageIndex = pageIndex * 20;
            LogUtils.i(TAG, "currentList---" + currentList.size());
            Collections.reverse(currentList);

            mChatInfoList.addAll(0, currentList);
            if (mChatInfoList != null && mChatInfoList.size() > 0) {
                UserInfoModel user = DBHelper.getInstance().getWeLearnDB().queryByUserId(userid,
                        true);
                if (user == null) {
                    isQueryAll = true;
                    WeLearnApi.getContactInfo(ChatMsgViewActivity.this, userid,
                            ChatMsgViewActivity.this);
                } else {
                    for (int i = 0; i < mChatInfoList.size(); i++) {
                        if (mChatInfoList.get(i).getType() == GlobalContant.MSG_TYPE_RECV) {
                            setUserData(mChatInfoList.get(i), user);
                        }
                    }
                }
            }
            // Log.i(TAG, "mChatInfoList---" + mChatInfoList.size());
            mChatList.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);
            mAdapter.setData(mChatInfoList);
            mChatList.post(new Runnable() {

                @Override
                public void run() {
                    mChatList.setSelection(position);
                }
            });
        }
        onLoadFinish();
    }

    @Override
    public void onLoadMore() {
    }

    public void leave() {
        disMissPop();
        mIsVoiceMsg = false;
        isShowPhotoMenu = false;
        MyApplication.isInChatMsgView = false;
        if (GlobalVariable.mainActivity != null) {
            finish();
        } else {
            // WeLearnApi.reLogin();
            IntentManager.goToMainView(this);
        }
    }

    @Override
    public void onBackPressed() {
        leave();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            getApplication().unregisterReceiver(headsetPlugReceiver);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Receiver not registered")) {
                // Ignore this exception. This is exactly what is desired
            } else {
                // unexpected, re-throw
                throw e;
            }
        }
        if (isFromNoti && (GlobalVariable.mChatMsgViewActivity != this)) {
            MyApplication.isInChatMsgView = true;
        } else {
            MyApplication.isInChatMsgView = false;
            if (mController != null) {
                mController.removeMsgInQueue();
            }
            mHandler.removeMessages(555);
            WelearnHandler.getInstance().removeMessage(MessageConstant.MSG_DEF_CHAT_LIST);
            WelearnHandler.getInstance().removeMessage(MessageConstant.MSG_DEF_MSG_RESULT);
        }

    }

}
