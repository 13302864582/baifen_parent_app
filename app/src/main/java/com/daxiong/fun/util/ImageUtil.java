package com.daxiong.fun.util;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.function.account.PersonHomePageActivity;
import com.daxiong.fun.model.FitBitmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 此类的描述：图片保存工具类 
 * @author:  Sky
 * @最后修改人： Sky 
 * @最后修改日期:2016年5月18日 下午8:17:36
 */
public class ImageUtil {

	private static final int _90 = 75;
	private static final String TAG = ImageUtil.class.getSimpleName();
	private static final int MIN_PIC_SIZE = 200;

	/**
	 * Utility method for downsampling images.
	 * 
	 * @param path
	 *            the file path
	 * @param data
	 *            if file path is null, provide the image data directly
	 * @param inputStream
	 *            .
	 * @param target
	 *            the target dimension
	 * @param width
	 *            use width as target, otherwise use the higher value of height
	 *            or width
	 * @param round
	 *            corner radius
	 * @return the resized image
	 */
	public static FitBitmap getResizedImage(String path, byte[] data, Uri uri, int target, boolean width, int round) {
		Options options = new Options();
		int srcWidth = 0;
		int srcHeight = 0;
		int ssize = 0;
		if (target == PersonHomePageActivity.SCALE_LOGO_IMG_SIZE) {
			options.inJustDecodeBounds = true;
			decode(path, data, uri, options);
			srcWidth = 60;
			srcHeight = 60;
			ssize = sampleSize(options, srcWidth, srcHeight);
			/*
			 * options.outHeight = 120; options.outWidth = 120;
			 */
			options.inSampleSize = ssize;
		} else if (target == 600) {
			options.inJustDecodeBounds = true;
			decode(path, data, uri, options);
			srcWidth = 480;
			srcHeight = 800;
			// DisplayMetrics metrics = new DisplayMetrics();

			ssize = sampleSize(options, srcWidth, srcHeight);
			options.inSampleSize = ssize;
			// options.
		} else if (target > 0) {
			options.inJustDecodeBounds = true;
			decode(path, data, uri, options);
			srcWidth = 400;
			srcHeight = 320;
			ssize = sampleSize(options, srcWidth, srcHeight);
			options.inSampleSize = ssize;
			// options.
		}
		Bitmap bm = null;
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inPurgeable = true;
		options.inInputShareable = true;

		try {
			bm = decode(path, data, uri, options);
		} catch (OutOfMemoryError e) {
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (round > 0) {
			bm = getRoundedCornerBitmap(bm, round);
		}
		FitBitmap fitMap = null;
		if (bm != null) {
			fitMap = new FitBitmap(bm, ssize, bm.getWidth(), bm.getHeight());
		}
		return fitMap;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	private static int sampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	public static Bitmap decode(String path, byte[] data, Uri uri, BitmapFactory.Options options) {

		Bitmap result = null;

		if (path != null) {
			result = BitmapFactory.decodeFile(path, options);
		} else if (data != null) {

			result = BitmapFactory.decodeByteArray(data, 0, data.length, options);

		} else if (uri != null) {
			ContentResolver cr = MyApplication.getContext().getContentResolver();
			InputStream inputStream = null;

			try {
				inputStream = cr.openInputStream(uri);
				result = BitmapFactory.decodeStream(inputStream, null, options);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} finally {
				try {
					if (inputStream != null) {
						inputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public static Bitmap getAutoOrientation(Bitmap bm, String path, Uri uri) {
		int deg = 0;
		try {
			ExifInterface exif = new ExifInterface(path);
			if (uri == null) {
				exif = new ExifInterface(path);
			} else if (path == null) {
				exif = new ExifInterface(uri.getPath());
			}
			int rotateValue = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (rotateValue) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				deg = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				deg = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				deg = 270;
				break;
			default:
				deg = 0;
				break;
			}
		} catch (Exception ee) {
		}
		Matrix m = new Matrix();
		m.preRotate(deg);
		bm = createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
		return bm;
	}

	/**
	 * @param bm
	 * @param iv
	 * @param uri
	 * @param path
	 *            保存的路径
	 */
	public static Bitmap autoFixOrientation(Bitmap bm, ImageView iv, Uri uri, String path, boolean isRotate) {
		int deg = 0;
		try {
			ExifInterface exif = new ExifInterface(path);
			if (uri == null) {
				exif = new ExifInterface(path);
			} else if (path == null) {
				exif = new ExifInterface(uri.getPath());
			}
			int rotateValue = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (rotateValue) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				deg = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				deg = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				deg = 270;
				break;
			default:
				deg = 0;
				break;
			}
		} catch (Exception ee) {
			LogUtils.d("catch img error", "return");
			if (iv != null) {
				iv.setImageBitmap(bm);
			}

			return bm;
		}
		Matrix m = new Matrix();
		m.preRotate(deg);
		bm = createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);

		if (iv != null) {
			// if (isRotate) {
			// iv.setImageBitmap(bm);
			// } else {
			// iv.setBackgroundDrawable(new BitmapDrawable(bm));
			// }
			iv.setImageBitmap(bm);
		}
		saveFile(path, bm);
		return bm;
	}

	public static Bitmap createBitmap(Bitmap source, int x, int y, int width, int height, Matrix m, boolean filter) {
		Bitmap bitmap = null;
		try {
			bitmap = Bitmap.createBitmap(source, x, y, width, height, m, filter);
		} catch (OutOfMemoryError e) {
			while (bitmap == null) {
				System.gc();
				System.runFinalization();
				bitmap = createBitmap(source, x, y, width, height, m, filter);
			}
		}
		return bitmap;
	}

	public static void saveFile(String filePath, Bitmap bitmap) {
		LogUtils.i(TAG, bitmap.getWidth() * bitmap.getHeight() + "");
		float width = bitmap.getWidth();
		float height = bitmap.getHeight();
		int size = (int) (width * height / 1024);

		float scaleWid = width / 600.0f;
		float scaleHei = height / 900.0f;
		float scale = scaleWid < scaleHei ? scaleWid : scaleHei;
		if (scale < 1) {
			scale = 1;
		}
		// BitmapFactory.deBitmap
		bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width / scale), (int) (height / scale), false);

		int ratio = 0;
		if (size > 4000) {// 超过1000k进行压缩
			ratio = 70;
		} else if (4000 > size && size > 3000) {
			ratio = 75;
		} else if (3000 > size && size > 2000) {
			ratio = 80;
		} else if (2000 > size && size > 1000) {
			ratio = 85;
		} else if (1000 > size && size > 500) {
			ratio = 90;
		} else if (500 > size && size > 200) {
			ratio = 95;
		}else if(200 > size && size > 100){
			ratio = 100;
		}else  {
			ratio = 100;
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filePath);
			if (bitmap.compress(CompressFormat.JPEG, ratio, out)) {
				out.flush();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static BitmapFactory.Options options = new BitmapFactory.Options();

	public static Point getImageSize(String path) {
		/**
		 * 最关键在此，把options.inJustDecodeBounds = true;
		 * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
		 */
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
		/**
		 * options.outHeight为原始图片的高
		 */
		LogUtils.d(TAG, "Bitmap Height = " + options.outHeight + " Width = " + options.outWidth);
		Point p = new Point(options.outWidth, options.outHeight);
		return p;
	}

	public static void loadBitmap(String filePath, NetworkImageView imageView) {
		DynamicAsyncTask task = new DynamicAsyncTask(imageView);
		task.execute(filePath);
	}

	static class DynamicAsyncTask extends AsyncTask<String, Void, Bitmap> {
		private NetworkImageView imageView;
		private String data = "";

		public DynamicAsyncTask(NetworkImageView imageView) {
			super();
			this.imageView = imageView;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			data = params[0];
			return decodeSampledBitmapFromResource(data, imageView.getWidth(), imageView.getHeight());
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (imageView != null && bitmap != null) {

				imageView.setCustomBitmap(bitmap);

			}
			super.onPostExecute(bitmap);
		}

	}

	public static Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight) {
		FileInputStream fileInputStream = null;
		FileOutputStream out = null;
		Bitmap bitmap = null;
		final BitmapFactory.Options options = new BitmapFactory.Options();

		options.inJustDecodeBounds = true;
		options.inPurgeable = true;
		try {
			File f = new File(path);
			options.inInputShareable = true;
			BitmapFactory.decodeFile(path, options);
			options.inSampleSize = calculateInSampleSize(options, 400, 600);
			options.inJustDecodeBounds = false;
			int degree = getPicRotate(path);

			Matrix m = new Matrix();

			fileInputStream = new FileInputStream(path);
			if (f.length() > 1024 * 200) {
				bitmap = BitmapFactory.decodeStream(fileInputStream, null, options);
				int width = bitmap.getWidth();
				int height = bitmap.getHeight();
				if (degree != 0) {
					m.setRotate(degree);

					bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);
				}

				out = new FileOutputStream(f);

				bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
				out.flush();
				out.close();
			} else {
				bitmap = BitmapFactory.decodeStream(fileInputStream);
				int width = bitmap.getWidth();
				int height = bitmap.getHeight();
				if (degree != 0) {
					m.setRotate(degree);

					bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);
				}
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return bitmap;

	}

	public static int getPicRotate(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		int num = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

		}
		for (int i = 1; (num = (int) Math.pow(2, i)) < inSampleSize; i++) {

		}
		return num;
	}
	
	
	public static int calculateInSampleSizeByOption(int width,int height, int reqWidth, int reqHeight) {		
		int inSampleSize = 1;
		int num = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

		}
		for (int i = 1; (num = (int) Math.pow(2, i)) < inSampleSize; i++) {

		}
		return num;
	}

	/**
	 * 压缩图片
	 * 
	 * @param path
	 * @return
	 */
	public static String compressImage(String path) {
		FitBitmap fm = ImageUtil.getResizedImage(path, null, null, 600, false, 0);
		Bitmap bm = ImageUtil.getAutoOrientation(fm.getmBitmap(), path, null);
		path = WeLearnFileUtil.getImgMsgFile().getAbsolutePath() + File.separator + UUID.randomUUID().toString();
		ImageUtil.saveFile(path, bm);
		return path;
	}
	
	
	/*********************************************************华丽的分割线**********************************************/
    public static int calculateInSampleSize2(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        LogUtils.i(TAG, "height: " + height);
        LogUtils.i(TAG, "width: " + width);
        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

            long totalPixels = width * height / inSampleSize;

            final long totalReqPixelsCap = reqWidth * reqHeight * 2;
            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2;
                totalPixels /= 2;
            }
        }
        return inSampleSize;
    }

}
