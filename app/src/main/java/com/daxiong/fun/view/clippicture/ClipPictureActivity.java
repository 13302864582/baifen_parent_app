package com.daxiong.fun.view.clippicture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.function.account.UserAvatarActivity;
import com.daxiong.fun.function.question.PayAnswerImageGridActivity;

import java.io.ByteArrayOutputStream;

import static com.daxiong.fun.function.homework.CropImageActivity.IMAGE_SAVE_PATH_TAG;

/**
 * @图片的放缩
 * @移动、截图
 */
public class ClipPictureActivity extends BaseActivity implements View.OnTouchListener {

    ImageView srcPic;
    Button sure;
    ClipView clipview;

    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    private static final String TAG = "11";
    int mode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    private String imageSourcePath;
    private String imageSavePath;
    private boolean isFromPhotoList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clip_picture_activity);

        srcPic = (ImageView) this.findViewById(R.id.src_pic);
        srcPic.setOnTouchListener(this);

        sure = (Button) this.findViewById(R.id.sure);
        sure.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            imageSourcePath = intent.getStringExtra(PayAnswerImageGridActivity.IMAGE_PATH);
            imageSavePath = intent.getStringExtra(IMAGE_SAVE_PATH_TAG);
            isFromPhotoList = intent.getBooleanExtra("isFromPhotoList", false);
           /* if (!TextUtils.isEmpty(imageSourcePath)) {
                imageSourcePath = autoFixOrientation(imageSourcePath, isFromPhotoList, this, mPhotoImage);//outWidth
                srcPic.setImageBitmap(mBitmap);
            }*/


            Glide.with(this).load(imageSourcePath).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    srcPic.setImageBitmap(resource);
                }
            });
        }

    }

    /*这里实现了多点触摸放大缩小，和单点移动图片的功能，参考了论坛的代码*/
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                // 設置初始點位置
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG");
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOM");
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                Log.d(TAG, "mode=NONE");
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    // ...
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY()
                            - start.y);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix);
        return true; // indicate event was handled
    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        //return FloatMath.sqrt(x * x + y * y);
        return (float) Math.sqrt(x * x + y * y);

    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /*点击进入预览*/
    public void onClick(View v) {
        Bitmap fianBitmap = getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        fianBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bitmapByte = baos.toByteArray();

        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), UserAvatarActivity.class);
        intent.putExtra("bitmap", bitmapByte);

        startActivity(intent);
    }

    /*获取矩形区域内的截图*/
    private Bitmap getBitmap() {
        getBarHeight();
        Bitmap screenShoot = takeScreenShot();

        clipview = (ClipView) this.findViewById(R.id.clipview);
        int width = clipview.getWidth();
        int height = clipview.getHeight();
        Bitmap finalBitmap = Bitmap.createBitmap(screenShoot,
                (width - height / 2) / 2, height / 4 + titleBarHeight + statusBarHeight, height / 2, height / 2);
        return finalBitmap;
    }

    int statusBarHeight = 0;
    int titleBarHeight = 0;

    private void getBarHeight() {
        // 获取状态栏高度
        Rect frame = new Rect();
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;

        int contenttop = this.getWindow()
                .findViewById(Window.ID_ANDROID_CONTENT).getTop();
        // statusBarHeight是上面所求的状态栏的高度
        titleBarHeight = contenttop - statusBarHeight;

        Log.v(TAG, "statusBarHeight = " + statusBarHeight
                + ", titleBarHeight = " + titleBarHeight);
    }

    // 获取Activity的截屏
    private Bitmap takeScreenShot() {
        View view = this.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }
}
