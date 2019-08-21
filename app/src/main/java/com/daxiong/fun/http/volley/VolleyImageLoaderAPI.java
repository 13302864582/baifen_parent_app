
package com.daxiong.fun.http.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;

/**
 * 此类的描述： volley 加载图片底层
 * 
 * @author: Sky @最后修改人： Sky
 * @最后修改日期:2015年7月14日 上午10:44:05
 */
public class VolleyImageLoaderAPI {
    private static VolleyImageLoaderAPI volleyImageLoaderAPI;

    /** 最大缓存50M */
    private int MAX_CAHCHE_SIZE = 10 * 1024 * 1024;

    private LruCache<String, Bitmap> lruCache;

    private ImageCache imageCache;

    private com.android.volley.toolbox.ImageLoader mImageLoader;

    public synchronized static VolleyImageLoaderAPI getInstance() {
        if (volleyImageLoaderAPI == null) {
            volleyImageLoaderAPI = new VolleyImageLoaderAPI();
        }
        return volleyImageLoaderAPI;
    }

    private VolleyImageLoaderAPI() {
        initImageLoder();
    }

    /**
     * 此方法描述的是：初始化图片缓存
     * @author:  sky
     * @最后修改人： sky
     * @最后修改日期:2015-7-30 上午11:34:40
     * initImageLoder void
     */
    private void initImageLoder() {
        MAX_CAHCHE_SIZE = (int) (Runtime.getRuntime().maxMemory() / 1024) / 10;
        lruCache = new LruCache<String, Bitmap>(MAX_CAHCHE_SIZE){
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
        mImageLoader = new ImageLoader(VolleyRequestQueueWrapper.getInstance(
                MyApplication.getContext()).getRequestQueue(), imageCache);
    }

    /**
     * 此方法描述的是：调用此方法可以直接加载图片
     * 
     * @author: sky
     * @最后修改日期:2015年7月13日 下午6:17:16
     * @version: 2.0 loadImage
     * @param ctx
     * @param iv
     */
    public void loadImageForImageRequest(Context ctx, final ImageView iv) {
        RequestQueue queue = Volley.newRequestQueue(ctx);
        String url = "https://www.baidu.com/img/bdlogo.png";
        // 第三第四个参数分别用于指定允许图片最大的宽度和高度，如果指定的网络图片的宽度或高度大于这里的最大值，则会对图片进行压缩，指定成0的话就表示不管图片有多大，都不会进行压缩。
        // 第五个参数就是ImageView里中的属性ScaleType
        // 第六个参数用于指定图片的颜色属性

        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                iv.setImageBitmap(response);
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, 120, 120);
        request.setShouldCache(true);// 设置缓存 缓存路径看我以前的帖子
        queue.add(request);
    }

    public void loadImageImageLoader(Context ctx, String url, ImageView imageView) {
        // imageView是一个ImageView实例
        // ImageLoader.getImageListener的第二个参数是默认的图片resource id
        // 第三个参数是请求失败时候的资源id，可以指定为0
        ImageListener listener = ImageLoader.getImageListener(imageView,
                android.R.drawable.ic_menu_rotate, android.R.drawable.ic_delete);
        mImageLoader.get(url, listener, 0, 0);
    }

    public void loadNetWorkImage(Context ctx, String imageUrl, NetworkImageView view) {
        // 1. 创建一个RequestQueue对象。
        // 2. 创建一个ImageLoader对象。
        // 3. 在布局文件中添加一个NetworkImageView控件。
        // 4. 在代码中获取该控件的实例。
        // 5. 设置要加载的图片地址。
        view.setDefaultImageResId(R.mipmap.ic_launcher);
        view.setErrorImageResId(R.mipmap.ic_launcher);
        view.setImageUrl(imageUrl, mImageLoader);
    }
}
