package com.daxiong.fun.common.camera;

import android.app.Activity;
import android.content.Intent;

import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.function.homework.CropImageActivity;
import com.daxiong.fun.function.question.PayAnswerImageGridActivity;

import java.util.Stack;

public class CameraManager {

    private static CameraManager mInstance;
    private Stack<Activity> cameras = new Stack<>();

    public static CameraManager getInstance() {
        if (mInstance == null) {
            synchronized (CameraManager.class) {
                if (mInstance == null)
                    mInstance = new CameraManager();
            }
        }
        return mInstance;
    }

 

/*    public void processPhotoItem(Activity activity, Photo photo) {
        Uri uri = photo.getImageUri().startsWith("file:") ? Uri.parse(photo
                .getImageUri()) : Uri.parse("file://" + photo.getImageUri());

        Intent intent = new Intent(activity, CropPhotoActivity.class);        
        intent.setData(uri);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE_GET_IMAGE_FROM_SYS);
        
    }*/
    
    public void processPhotoItem(Activity activity, String photopath) {           
        //跳转原来的
        Intent localIntent = new Intent();        
        localIntent.setClass(activity, CropImageActivity.class);
		localIntent.putExtra(PayAnswerImageGridActivity.IMAGE_PATH, photopath);
		localIntent.putExtra(CropImageActivity.IMAGE_SAVE_PATH_TAG, photopath);
		localIntent.putExtra("isFromPhotoList", false);
		activity.startActivityForResult(localIntent, RequestConstant.REQUEST_CODE_GET_IMAGE_FROM_SYS);
        
    }

    public void addActivity(Activity act) {
        cameras.add(act);
    }

    public void removeActivity(Activity act) {
        cameras.remove(act);
    }

}
