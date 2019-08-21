package com.daxiong.fun.util;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.constant.MessageConstant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MediaUtil {

    public static final String TAG = MediaUtil.class.getSimpleName();

    private MediaRecorderUtils mMediaRecorderUtils;
    private boolean isPlaying;
    public static int RECORD_NO = 0;
    private static int RECORD_ING = 1;
    private static int RECODE_ED = 2;
    public static int RECODE_STATE = 0;
    private static int MIX_TIME = 1;
    private static boolean mIsMsgChat;
    private boolean mIsCancel;

    private static class WeLearnMediaUtilHolder {
        private static final MediaUtil INSANCE = new MediaUtil();
    }

    public static MediaUtil getInstance(boolean isMsgChat) {
        mIsMsgChat = isMsgChat;
        return WeLearnMediaUtilHolder.INSANCE;
    }

    public void setIsCancel(boolean isCancel) {
        this.mIsCancel = isCancel;
    }

    public enum MaxRecordTime {
        MAX_OF_QA, MAX_OF_HOMEWORK, MAX_OF_COURSE
    }

    /**
     * 播放过程中的回调
     *
     * @author 汪春
     */
    public interface RecordCallback {
        void onAfterRecord(float voiceTime);//录制后处理
    }

    public void record(String fileName, RecordCallback callback, Activity context) {
        if (RECODE_STATE != RECORD_ING) {
            WeLearnFileUtil.deleteVoiceFile();
            RECODE_STATE = RECORD_ING;
            if (mIsMsgChat) {
                UiUtil.getInstance().showChatVoiceDialog(context);
            } else {
                UiUtil.getInstance().showVoiceDialog(context);
            }
            mMediaRecorderUtils = new MediaRecorderUtils(fileName, context);
            try {
                mMediaRecorderUtils.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ThreadPoolUtil.execute(new ImgThread(callback));
        }
    }

//	/**
//	 * listener不为空的话, 可以点击dialog停止录音
//	 * @param fileName
//	 * @param callback
//	 * @param context
//	 * @param listener
//	 */
//	public void record(String fileName, RecordCallback callback, Activity context , OnClickListener listener , MaxRecordTime recordTime) {
//		if (RECODE_STATE != RECORD_ING) {
//			WeLearnFileUtil.deleteVoiceFile();
//			RECODE_STATE = RECORD_ING;
//			if (listener != null) {
//				WeLearnUiUtil.getInstance().showCourseVoiceDialog(context , listener);
//			}else if (mIsMsgChat) {
//				WeLearnUiUtil.getInstance().showChatVoiceDialog(context);
//			}
//			mr = new AudioRecorder(fileName, context);
//			try {
//				mr.start();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			ThreadPoolUtil.execute(new ImgThread(callback ,recordTime));
//		}
//	}

    public void record(String fileName, final RecordCallback callback, Activity context, final MaxRecordTime recordTime) {
        if (RECODE_STATE != RECORD_ING) {
            WeLearnFileUtil.deleteVoiceFile();
            RECODE_STATE = RECORD_ING;
            mMediaRecorderUtils = new MediaRecorderUtils(fileName, context);
            try {
                mMediaRecorderUtils.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ThreadPoolUtil.execute(new Runnable() {
                private int MAX_TIME;

                @Override
                public void run() {
                    switch (recordTime) {
                        case MAX_OF_QA:
                            MAX_TIME = 2;
                            break;
                        case MAX_OF_HOMEWORK:
                            MAX_TIME = 2;
                            break;
                        case MAX_OF_COURSE:
//					    MAX_TIME = 500;
                            MAX_TIME = 2;
                            break;

                        default:
                            break;
                    }
                    long maxTimeLong = MAX_TIME * 60 * 1000;
                    try {
                        Thread.sleep(maxTimeLong);
                    } catch (Exception e) {
                    }
                    if (RECODE_STATE == RECORD_ING) {
                        voiceValue = mMediaRecorderUtils.getAmplitude();

                        RECODE_STATE = RECODE_ED;
                        try {
                            if (mMediaRecorderUtils != null) {
                                mMediaRecorderUtils.stop(voiceValue);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (callback != null) {
                            callback.onAfterRecord(MAX_TIME);
                        }
                    }
                }
            });
        }
    }


    public void stopRecord(final double voiceValue, final RecordCallback callback) {
        int delayTime = 1000;
        if (mIsCancel) {
            delayTime = 0;
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                UiUtil.getInstance().closeDialog();
                if (RECODE_STATE == RECORD_ING) {
                    RECODE_STATE = RECODE_ED;
                    try {
                        mMediaRecorderUtils.stop(voiceValue);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    callback.onAfterRecord(recodeTime);
//					if (recodeTime < MIX_TIME) {
//						//WeLearnUiUtil.getInstance().showWarnDialogWhenRecordVoice();
//						RECODE_STATE = RECORD_NO;
//					} else {
//						callback.onAfterRecord(recodeTime);
//					}
                }
            }
        }, delayTime);
    }

    private float recodeTime;
    private final int MAX_TIME = 120;
    private double voiceValue;

    private class ImgThread implements Runnable {

        private RecordCallback callback;

        public ImgThread(RecordCallback callback) {
            this.callback = callback;
        }

        @Override
        public void run() {
            recodeTime = 0.0f;
            while (RECODE_STATE == RECORD_ING) {
                if (recodeTime >= MAX_TIME && MAX_TIME != 0) {
                    imgHandle.sendEmptyMessage(0);
                } else {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    recodeTime += 0.2;
                    if (RECODE_STATE == RECORD_ING) {
                        voiceValue = mMediaRecorderUtils.getAmplitude();
                        imgHandle.sendEmptyMessage(1);
                    }
                }
            }
        }

        Handler imgHandle = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                switch (msg.what) {
                    case 0:
                        if (RECODE_STATE == RECORD_ING) {
                            RECODE_STATE = RECODE_ED;
                            UiUtil.getInstance().closeDialog();
                            try {
                                if (mMediaRecorderUtils != null) {
                                    mMediaRecorderUtils.stop(voiceValue);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (recodeTime < MIX_TIME) {
                                UiUtil.getInstance().showWarnDialogWhenRecordVoice();
                                RECODE_STATE = RECORD_NO;
                            } else {
                                if (callback != null) {
                                    callback.onAfterRecord(recodeTime);
                                }
                            }
                        }
                        break;
                    case 1:
                        if (mIsMsgChat) {
                            if (!mIsCancel) {
                                UiUtil.getInstance().setMsgDialogImage(voiceValue);
                            }
                        } else {
                            UiUtil.getInstance().setDialogImage(voiceValue);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public void stopVoice(AnimationDrawable animationDrawable) {
        if (isPlaying) {
            isPlaying = false;
        }
        if (animationDrawable != null && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
        boolean isPlaying2 = false;
        try {
            if (currentMediaPlayer != null) {
                isPlaying2 = currentMediaPlayer.isPlaying();
            }
        } catch (IllegalStateException e) {
            currentMediaPlayer = null;
            currentMediaPlayer = new MediaPlayer();
        }

        if (isPlaying2) {
            currentMediaPlayer.stop();
            currentMediaPlayer.release();
            currentMediaPlayer = null;
        }
    }

    public void pauseVoice(AnimationDrawable animationDrawable) {

        if (currentMediaPlayer != null) {
            currentMediaPlayer.pause();
        }
        if (animationDrawable != null && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
    }

    private boolean isPause;
    private MediaPlayer currentMediaPlayer;

    private class ProgressBarRunnable implements Runnable {

        private ProgressBar bar;

        public ProgressBarRunnable(ProgressBar bar) {
            this.bar = bar;
        }

        @Override
        public void run() {
            synchronized (this) {
//				bar.setProgress(0);
                if (currentMediaPlayer != null) {
                    int currentPos = 0;
                    int total = currentMediaPlayer.getDuration();
                    while (currentMediaPlayer != null && currentPos < total && !isPause) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (currentMediaPlayer != null) {
                            try {
                                currentPos = currentMediaPlayer.getCurrentPosition();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
//							bar.setProgress(currentPos);
                        }
                    }
                }
            }
        }
    }

    private String currentPath = "";

    /**
     * 播放声音
     *
     * @param audioPath
     * @param animationDrawable
     * @param callback
     */
    public void playVoice(final boolean isLocal, final String audioPath,
                          final AnimationDrawable animationDrawable, final ResetImageSourceCallback callback, final ProgressBar bar) {
        if (BtnUtils.isFastClick()) {
            return;
        }
        if (!audioPath.equals(currentPath)) {
            stopPlay();
            isPause = false;
        }

        currentPath = audioPath;

        final ProgressBarRunnable pbr = new ProgressBarRunnable(bar);
        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MessageConstant.END_OF_PLAY:
                        if (null != currentMediaPlayer) {


                            currentMediaPlayer.setOnErrorListener(new OnErrorListener() {
                                @Override
                                public boolean onError(MediaPlayer mp, int what, int extra) {
                                    try {
                                        Log.e(TAG, "播放录音出错了");
                                        currentMediaPlayer.release();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    return false;
                                }
                            });
                            currentMediaPlayer.setOnCompletionListener(new OnCompletionListener() {

                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    stopVoice(animationDrawable);
                                    if (callback != null) {
                                        callback.reset();
                                    }
                                    if (bar != null) {
                                        bar.setVisibility(View.GONE);
                                    }
                                }
                            });
                            try {

                                currentMediaPlayer.setVolume(1.0f, 1.0f);
                                currentMediaPlayer.start();
                                isPlaying = true;
                                if (callback != null) {
                                    callback.playAnimation();
                                }
                                if (bar != null) {
                                    bar.setProgress(0);
                                    bar.setMax(currentMediaPlayer.getDuration());
                                    ThreadPoolUtil.execute(pbr);
                                }

                            } catch (IllegalStateException e) {
                                Log.e(TAG, "播放录音出错了IllegalStateException");
                            }
                        }
                        break;
                }
            }
        };
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            animationDrawable.start();
        }
        if (isPause && currentMediaPlayer != null) {//如果点击的时候已经暂停了，则恢复播放

            try {

                currentMediaPlayer.start();
                isPlaying = true;
                isPause = false;
                ThreadPoolUtil.execute(pbr);

            } catch (IllegalStateException e) {

            }


        } else {
            if (!isPlaying) {
                isPlaying = true;
                if (callback != null) {
                    callback.beforePlay();
                }

                currentMediaPlayer = new MediaPlayer();
                currentMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                ThreadPoolUtil.execute(new Runnable() {

                    @Override
                    public void run() {
                        FileInputStream input = null;
                        try {
                            if (isLocal) {
                                File file = new File(audioPath);
                                if (file.exists()) {
                                    input = new FileInputStream(file);
                                    currentMediaPlayer.setDataSource(input.getFD());
                                }
                            } else {
                                currentMediaPlayer.setDataSource(MyApplication.getContext(),
                                        Uri.parse(audioPath));
                            }
                            isPlaying = true;
                            currentMediaPlayer.prepare();
                            handler.sendEmptyMessage(MessageConstant.END_OF_PLAY);
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (input != null) {
                                try {
                                    input.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
            } else {// 暂停播放
                pauseVoice(animationDrawable);
                isPlaying = false;
                isPause = true;
                if (callback != null) {
                    callback.reset();
                }
            }
        }
    }

    public interface ResetImageSourceCallback {
        void beforePlay();

        void reset();

        void playAnimation();
    }

//	private MediaPlayer stopPlayer;
//	
//	public void playLocalAudio(Context context, MediaPlayer player) {
//		stopPlayer = player;
//		if (!stopPlayer.isPlaying()) {
//			try {
//				stopPlayer.prepare();
//			} catch (IllegalStateException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			stopPlayer.start();
//		}
//	}
//	
//	public void stopLocalAudio() {
//		if (stopPlayer != null) {
//			stopPlayer.stop();
//			stopPlayer.release();
//			stopPlayer = null;
//		}
//	}

    /**
     * 停止其他播放
     */
    public void stopPlay() {
        for (AnimationDrawable animation : MyApplication.animationDrawables) {
            stopVoice(animation);
        }
    }

    /**
     * 重置播放动画
     *
     * @param iconVoiceView
     */
    public void resetAnimationPlay(ImageView iconVoiceView) {
        for (ImageView currentView : MyApplication.anmimationPlayViews) {
            if (currentView != iconVoiceView) {
                currentView.setImageResource(R.drawable.ic_play2);
            }
        }
    }

    /**
     * 重置播放动画
     *
     * @param iconVoiceView
     */
    public void resetAnimationPlayAtHomeWork(ImageView iconVoiceView) {
        for (ImageView currentView : MyApplication.anmimationPlayViews) {
            if (currentView != iconVoiceView) {
                int resID = 0;
                Object tag = currentView.getTag();
                if (tag != null && (tag instanceof Integer)) {
                    resID = (Integer) tag;
                }
                if (resID == 0) {
                    currentView.setImageResource(R.drawable.ic_play2);
                } else {
                    currentView.setImageResource(resID);
                }
            }
        }
    }


    /***********************************************/
    /**
     * 播放本地声音
     *
     * @param audioPath
     * @param animationDrawable
     * @param callback
     */
    public void playAssetsVoice(final String audioPath,
                                final AnimationDrawable animationDrawable, final ResetImageSourceCallback callback, final ProgressBar bar) {

        final ProgressBarRunnable pbr = new ProgressBarRunnable(bar);


        if (BtnUtils.isFastClick()) {
            return;
        }

        if (animationDrawable != null && !animationDrawable.isRunning()) {
            animationDrawable.start();
        }
        if (isPause && currentMediaPlayer != null) {//如果点击的时候已经暂停了，则恢复播放
            try {
                currentMediaPlayer.start();
                isPlaying = true;
                isPause = false;
                ThreadPoolUtil.execute(pbr);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }


        } else {
            if (!isPlaying) {
                isPlaying = true;
                if (callback != null) {
                    callback.beforePlay();
                }
                currentMediaPlayer = new MediaPlayer();
                currentMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                AssetManager assetManager = MyApplication.getContext().getAssets();
                try {
                    AssetFileDescriptor afd = assetManager.openFd(audioPath);
                    currentMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
                            afd.getLength());
                    isPlaying = true;
                    currentMediaPlayer.setVolume(0.5f, 0.5f);
                    currentMediaPlayer.prepare();
                    currentMediaPlayer.start();
                    if (callback != null) {
                        callback.playAnimation();
                    }
                    if (bar != null) {
                        bar.setProgress(0);
                        bar.setMax(currentMediaPlayer.getDuration());
                        ThreadPoolUtil.execute(pbr);
                    }


                    currentMediaPlayer.setOnErrorListener(new OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mp, int what, int extra) {
                            try {
                                Log.e(TAG, "播放录音出错了");
                                currentMediaPlayer.release();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                    });
                    currentMediaPlayer.setOnCompletionListener(new OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            stopVoice(animationDrawable);
                            if (callback != null) {
                                callback.reset();
                            }
                            if (bar != null) {
                                bar.setVisibility(View.GONE);
                            }
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {// 暂停播放
                pauseVoice(animationDrawable);
                isPlaying = false;
                isPause = true;
                if (callback != null) {
                    callback.reset();
                }
            }
        }
    }

}
