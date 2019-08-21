package com.daxiong.fun.function.communicate;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.model.ChatInfo;
import com.daxiong.fun.util.DateUtil;
import com.daxiong.fun.util.MediaUtil;
import com.daxiong.fun.util.MediaUtil.ResetImageSourceCallback;

import java.util.List;

public abstract class AbstractMsgChatItemView extends FrameLayout {
	
	public AbstractMsgChatItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews(context);
	}

	public AbstractMsgChatItemView(Context context) {
		super(context);
		setupViews(context);
	}
	
	public abstract void setupViews(Context context);
	
	/**
	 * 重置播放动画
	 * @param iconVoiceView
	 */
	protected void resetAnimationPlay(ImageView iconVoiceView) {
		for (ImageView currentView: MyApplication.anmimationPlayViews) {
			if (currentView != iconVoiceView) {
				int temp = -1;
				if (null != currentView.getTag()) {
					temp = Integer.parseInt(currentView.getTag().toString());
				}
				if (temp == 1) {
					currentView.setImageResource(R.drawable.ic_voice_msg_recv_play3);
				} else {
					currentView.setImageResource(R.drawable.ic_voice_msg_play3);
				}
			}
		}
	}
	
	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		if (visibility == View.GONE) {
			stopPlay();
			MyApplication.animationDrawables.clear();
			MyApplication.anmimationPlayViews.clear();
		}
	}
	
	protected long getPreMsgTimestamp(List<ChatInfo> mChatInfoList, int postion) {
		if (postion >= 0) {
			ChatInfo info = mChatInfoList.get(postion);
			if (info.getType() == GlobalContant.MSG_TYPE_RECV) {
				return DateUtil.convertTimestampToLong((float) (info.getTimestamp() * 1000));
			} else {
				return info.getLocalTimestamp();
			}
		} else {
			return 0L;
		}
	}
	
	/**
	 * 停止其他播放
	 */
	protected void stopPlay() {
		for (AnimationDrawable animation : MyApplication.animationDrawables) {
			MediaUtil.getInstance(false).stopVoice(animation);
		}
	}
	
	protected void play(final ImageView icPlay, String audiopath, final boolean isRecv) {
		if (isRecv) {
			icPlay.setTag(1);
			icPlay.setImageResource(R.drawable.recv_msg_voice_play_animation);
		} else {
			icPlay.setTag(0);
			icPlay.setImageResource(R.drawable.msg_voice_play_animation);
		}
		
		AnimationDrawable animationDrawable = (AnimationDrawable) icPlay.getDrawable();
		MyApplication.animationDrawables.add(animationDrawable);
		MyApplication.anmimationPlayViews.add(icPlay);
		stopPlay();
		MediaUtil.getInstance(false).playVoice(true, audiopath, animationDrawable, new ResetImageSourceCallback() {
			
			@Override
			public void reset() {
				if (isRecv) {
					icPlay.setImageResource(R.drawable.ic_voice_msg_recv_play3);
				} else {
					icPlay.setImageResource(R.drawable.ic_voice_msg_play3);
				}
				
			}
			
			@Override
			public void playAnimation() {}
			
			@Override
			public void beforePlay() {
				resetAnimationPlay(icPlay);
			}
		}, null);
	}
}
