
package com.daxiong.fun.base;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.NetworkImageView.OnImageLoadListener;
import com.daxiong.fun.R;
import com.daxiong.fun.http.VolleyRequestQueue;
import com.daxiong.fun.util.LogUtils;

/**
 * 统一的加载图片类
 * 
 * @author parsonswang
 */
public class ImageLoader {

    public static final String TAG = ImageLoader.class.getSimpleName();

    /** 最大缓存50M */
    private int MAX_CAHCHE_SIZE = 10 * 1024 * 1024;

    private LruCache<String, Bitmap> lruCache;

    private ImageCache imageCache;

    private com.android.volley.toolbox.ImageLoader imageLoader;

    private static ImageLoader mImageLoader;

    public static ImageLoader getInstance() {
        if (null == mImageLoader) {
            mImageLoader = new ImageLoader();
        }
        return mImageLoader;
    }

    private ImageLoader() {
        init();
    }

    public void init() {
        MAX_CAHCHE_SIZE = (int)(Runtime.getRuntime().maxMemory() / 1024) / 10;
        LogUtils.d(TAG, "max memory size = " + MAX_CAHCHE_SIZE);
        lruCache = new LruCache<String, Bitmap>(MAX_CAHCHE_SIZE) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };

        imageCache = new ImageCache() {
            @Override
            public void putBitmap(String key, Bitmap value) {
            	lruCache.put(key, value);
            }

            @Override
            public Bitmap getBitmap(String key) {
                return lruCache.get(key);
            }
        };

        imageLoader = new com.android.volley.toolbox.ImageLoader(VolleyRequestQueue.getQueue(),
                imageCache);
    }

    public void loadImageWithDefaultAvatar(String url, NetworkImageView imgView, int imageSize,
            int roundPixels) {
        if (url != null && !url.isEmpty() && imgView != null) {
            loadImage(url, imgView, R.drawable.ic_default_avatar, R.drawable.ic_default_avatar,
                    imageSize, roundPixels);
        }
    }

    public void loadImage(String url, NetworkImageView imageview, int defaultid, int imageSize,
            int roundPixels) {
        loadImage(url, imageview, defaultid, 0, imageSize, roundPixels);
    }

    public void loadImage(String url, NetworkImageView imageview, int defaultid, int errorId,
            int imageSize, int roundPixels) {
        if (null == imageview) {
            LogUtils.e(TAG, "loadImage() ==> ImageView is null !");
            return;
        }

        imageview.setImageSize(imageSize);
        imageview.setImageRoundPixels(roundPixels);
        imageview.setDefaultImageResId(defaultid);
        imageview.setErrorImageResId(errorId);
        imageview.setTag(url);
        imageview.setImageUrl(url, imageLoader);
    }

    public void resetUrl(NetworkImageView imageview) {
        if (null != imageview) {
            imageview.setImageUrl(null, imageLoader);
        }
    }

    public void loadImage(String url, NetworkImageView imageview, int defaultid,
            OnImageLoadListener listener) {
        if (null == imageview) {
            LogUtils.e(TAG, "loadImage() ==> ImageView is null !");
            return;
        }

        imageview.setOnLoadImageListener(listener);
        imageview.setDefaultImageResId(defaultid);
        imageview.setTag(url);
        imageview.setImageUrl(url, imageLoader);
    }

    public void loadImage(String url, NetworkImageView imageview, int defaultid, int errorId,
            OnImageLoadListener listener) {
        if (null == imageview) {
            LogUtils.e(TAG, "loadImage() ==> ImageView is null !");
            return;
        }

        imageview.setErrorImageResId(errorId);
        loadImage(url, imageview, defaultid, listener);
    }

    public void ajaxQuestionPic(String url, NetworkImageView imageview) {
        loadImage(url, imageview, R.drawable.default_loading_img);
    }

    public void ajaxAnswerPic(String url, NetworkImageView imageview) {
        loadImage(url, imageview, R.drawable.default_loading_img);
    }

    public void loadImage(String url, NetworkImageView imageview, int defaultid) {
        if (null == imageview) {
            LogUtils.e(TAG, "loadImage() ==> ImageView is null !");
            return;
        }

        imageview.setDefaultImageResId(defaultid);
        imageview.setTag(url);
        imageview.setImageUrl(url, imageLoader);
    }

    // public static void recycleBitmap(CropImageView imgView) {
    // Bitmap bm = imgView.getDrawingCache();
    // if (bm != null && !bm.isRecycled()) {
    // LogUtils.d(TAG, "---recycle---");
    // bm.recycle();
    // System.gc();
    // }
    // }

}
