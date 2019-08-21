
package com.daxiong.fun.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import com.daxiong.fun.MyApplication;

public class HeadsetPlugReceiver extends BroadcastReceiver {

    // private static final String TAG =
    // HeadsetPlugReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (intent.hasExtra("state")) {
            if (intent.getIntExtra("state", 0) == 0) {
                // Toast.makeText(context, "headset not connected",
                // Toast.LENGTH_LONG).show();
                setSpeakerphoneOn(true);
            } else if (intent.getIntExtra("state", 0) == 1) {
                // Toast.makeText(context, "headset connected",
                // Toast.LENGTH_LONG).show();
                setSpeakerphoneOn(false);
            }
        }
    }

    // system setting API
    private void setSpeakerphoneOn(boolean on) {
        if (on) {
            MyApplication.audioManager.setSpeakerphoneOn(true);
            MyApplication.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    MyApplication.audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                    AudioManager.USE_DEFAULT_STREAM_TYPE);// FLAG_PLAY_SOUND);
            MyApplication.audioManager.setMode(AudioManager.STREAM_MUSIC);
        } else {
            MyApplication.audioManager.setSpeakerphoneOn(false);
            MyApplication.audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                    MyApplication.audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                    AudioManager.USE_DEFAULT_STREAM_TYPE);
            // WApplication.audioManager.adjustStreamVolume
            // (AudioManager.STREAM_VOICE_CALL, AudioManager.ADJUST_RAISE,
            // AudioManager.FLAG_SHOW_UI);
            MyApplication.audioManager.setMode(AudioManager.MODE_IN_CALL);
        }
    }

}
