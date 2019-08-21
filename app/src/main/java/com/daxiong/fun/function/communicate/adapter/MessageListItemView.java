
package com.daxiong.fun.function.communicate.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daxiong.fun.R;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.MessageConstant;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.ChatInfo;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.DateUtil;
import com.daxiong.fun.view.CropCircleTransformation;

import java.util.Date;

public class MessageListItemView extends FrameLayout {

    private ImageView mAvatar;

    private TextView mTime;

    private TextView mName;

    private TextView mMsgContent;

    private TextView unread_tv;

    private Context mContext;

    private View mAvatar_container;

    private int avatarSize;
    // private static final String TAG =
    // MessageListItemView.class.getSimpleName();

    public MessageListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    public MessageListItemView(Context context) {
        super(context);
        setupView(context);
    }

    @SuppressLint("InflateParams")
    private void setupView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.message_list_item, null);
        mAvatar_container = view.findViewById(R.id.send_msg_user_avatar_frame);
        mAvatar = (ImageView)view.findViewById(R.id.send_msg_user_avatar);
        avatarSize = getResources()
                .getDimensionPixelSize(R.dimen.communicate_contacts_list_avatar_size);
        unread_tv = (TextView)view.findViewById(R.id.send_msg_user_avatar_unread);
        mTime = (TextView)view.findViewById(R.id.send_msg_time);
        mName = (TextView)view.findViewById(R.id.send_msg_user_name);
        mMsgContent = (TextView)view.findViewById(R.id.send_msg_content);

        addView(view);
    }

    public void showData(ChatInfo info) {
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

            Glide.with(mContext).load(ug.getAvatar_100()).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CropCircleTransformation(mContext.getApplicationContext()))
                    .placeholder(R.drawable.default_icon_circle_avatar).into(mAvatar);

            mName.setText(ug.getName());
        } else {
            // ImageLoader.getInstance().loadImage(null, mAvatar,
            // R.drawable.ic_default_avatar,
            // R.drawable.ic_default_avatar, avatarSize, avatarSize / 10);

            Glide.with(mContext).load("")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CropCircleTransformation(mContext.getApplicationContext()))
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
        mAvatar_container.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putInt("userid", fromUserId);
                data.putInt("roleid", finalRoleid);
                if (finalRoleid == GlobalContant.ROLE_ID_COLLEAGE) {
                    IntentManager.goToTeacherInfoView((Activity)mContext, data);
                } else if (finalRoleid == GlobalContant.ROLE_ID_STUDENT) {
                    IntentManager.goToStudentInfoView((Activity)mContext, data);
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
