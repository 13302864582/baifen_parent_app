
package com.daxiong.fun.function.study;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.common.WebViewActivity;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.PackageManagerUtils;
import com.daxiong.fun.util.zxing.camera.CameraManager;
import com.daxiong.fun.util.zxing.decoding.CaptureActivityHandler;
import com.daxiong.fun.util.zxing.decoding.InactivityTimer;
import com.daxiong.fun.util.zxing.view.ViewfinderView;

import java.io.IOException;
import java.util.Vector;

/**
 * Initial the camera
 * 
 * @author sky.hu
 */
public class QRCodeScanActivity extends BaseActivity implements Callback, OnClickListener {

    private CaptureActivityHandler handler;

    private ViewfinderView viewfinderView;

    private boolean hasSurface;

    private Vector<BarcodeFormat> decodeFormats;

    private String characterSet;

    private InactivityTimer inactivityTimer;

    private MediaPlayer mediaPlayer;

    private boolean playBeep;

    private static final float BEEP_VOLUME = 0.10f;

    private boolean vibrate;

    private RelativeLayout mButtonBack;

    private String codeUrl = "";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code_scan_activity);
        // ViewUtil.addTopView(getApplicationContext(), this,
        // R.string.scan_card);
        initView();
        initListener();

    }

    @Override
    public void initView() {
        super.initView();
        CameraManager.init(getApplication());
        mButtonBack = (RelativeLayout)findViewById(R.id.back_layout);
        viewfinderView = (ViewfinderView)findViewById(R.id.viewfinder_view);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        userinfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();

    }

    @Override
    public void initListener() {
        super.initListener();
        mButtonBack.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView)findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager)getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_layout:
                finish();
                break;

            default:
                break;
        }
    }

    /**
     * 处理扫描结果
     * 
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        if (resultString.equals("")) {
            Toast.makeText(QRCodeScanActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
        } else {
             Toast.makeText(this, resultString, 4).show();
//            if (resultString.contains("fudaotuan.com/raffle.html")) {//如果是大熊作业的网址
//
//                if (!codeUrl.contains("?")) {
//                    resultString = resultString + "?";
//                    codeUrl = resultString + "userid={0}&phoneos={1}&appversion={2}";
//                } else {
//                    codeUrl = resultString + "&userid={0}&phoneos={1}&appversion={2}";
//                }
//                codeUrl = codeUrl.replace("{0}", userinfo.getUserid() + "")
//                        .replace("{1}", "android")
//                        .replace("{2}", PackageManagerUtils.getVersionCode() + "");
//                Intent intent = new Intent(this, WebViewActivity.class);
//                intent.putExtra("title", "辅导团");
//                intent.putExtra("url", codeUrl);
//                startActivity(intent);
//            } else {
//                Toast.makeText(this, "扫描出的信息是:" + resultString, 4).show();
//            }
            
            if (resultString.contains("fudaotuan.com")) {//如果是大熊作业的网址
                if (resultString.contains("?")) {
                      codeUrl=resultString+"&userid={0}&phoneos={1}&appversion={2}";
                      codeUrl=codeUrl.replace("{0}", userinfo.getUserid()+"").replace("{1}", "android").replace("{2}", PackageManagerUtils.getVersionCode()+"");               
                     
                }else {
                  codeUrl=resultString+"?userid={0}&phoneos={1}&appversion={2}";
                  codeUrl=codeUrl.replace("{0}", userinfo.getUserid()+"").replace("{1}", "android").replace("{2}", PackageManagerUtils.getVersionCode()+"");  
                }
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("title", "大熊作业");
                intent.putExtra("url", codeUrl);
                startActivity(intent);
                
            }else {
                Toast.makeText(this, "扫描出的信息是:"+resultString, 4).show();
            }

            // Intent resultIntent = new Intent();
            // Bundle bundle = new Bundle();
            // bundle.putString("result", resultString);
            // bundle.putParcelable("bitmap", barcode);
            // resultIntent.putExtras(bundle);
            // this.setResult(RESULT_OK, resultIntent);
        }
        // QRCodeScanActivity.this.finish();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(),
                        file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    private UserInfoModel userinfo;

}
