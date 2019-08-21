package com.daxiong.fun.util;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeLearnFileUtil extends FileUtils {

	private static final String TAG = "WeLearnFileUtil";

	// SD卡的路径
	private static final String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

	private static final String CACHE_DIR = SD_PATH + "/welearn_par/caches/";

	private static final String APP_ROOT_FOLDER_NAME = ".welearn_par";

	private static final String ANSWER_DIR = SD_PATH + File.separator + APP_ROOT_FOLDER_NAME + File.separator + "answer"
			+ File.separator;

	private static final String IMG_MSG_DIR = SD_PATH + File.separator + APP_ROOT_FOLDER_NAME + File.separator + "chat"
			+ File.separator + ".img" + File.separator;

	private static final String CONTACT_IMG_DIR = SD_PATH + File.separator + APP_ROOT_FOLDER_NAME + File.separator
			+ "contact" + File.separator + "img" + File.separator;

	private static final String QUESTION_DIR = SD_PATH + File.separator + APP_ROOT_FOLDER_NAME + File.separator
			+ "question" + File.separator;

	public static final String VOICE_PATH = SD_PATH + File.separator + APP_ROOT_FOLDER_NAME + File.separator + "chat"
			+ File.separator + "voice" + File.separator;

	public static final String LOG_RESP_PATH = SD_PATH + File.separator + APP_ROOT_FOLDER_NAME + File.separator + "log"
			+ File.separator + "response" + File.separator;

	public static final String LOG_REQ_PATH = SD_PATH + File.separator + APP_ROOT_FOLDER_NAME + File.separator + "log"
			+ File.separator + "request" + File.separator;

	public static final String DB_PATH = SD_PATH + File.separator + APP_ROOT_FOLDER_NAME + File.separator + "db"
			+ File.separator;

	public static final String WELCOME_IMAGE_PATH = SD_PATH + File.separator + APP_ROOT_FOLDER_NAME + File.separator
			+ "welcome_image" + File.separator;

	private static final String SHOT_DIR = SD_PATH + File.separator + APP_ROOT_FOLDER_NAME + File.separator + "shot"
			+ File.separator;
	
	
	// 错误日志的路径
	public static final String LOG_DIR = SD_PATH + File.separator + APP_ROOT_FOLDER_NAME + File.separator + "log"
			+ File.separator;;

	public static final String WELCOME_IMAGE_NAME = "welcome.png";
	public static final String SHOT_IMAGE_NAME = "shot.png";

	private static final SimpleDateFormat FILENAME_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINESE);

	private static final long LOG_OUT_TIME = 1000 * 60 * 60 * 2;

	private static File ANSWER_FILE = new File(ANSWER_DIR);
	private static File QUESTION_FILE = new File(QUESTION_DIR);
	private static File VOICE_FILE = new File(VOICE_PATH);
	private static File CACHE_FILE = new File(CACHE_DIR);
	private static File IMG_MSG_FILE = new File(IMG_MSG_DIR);
	private static File CONTACT_IMG_FILE = new File(CONTACT_IMG_DIR);
	private static File WELCOME_IMAGE_FOLDER = new File(WELCOME_IMAGE_PATH);
	private static File SHOT_FILE = new File(SHOT_DIR);

	private static File DB_FILE = new File(DB_PATH);

	public static String LOG_RESP_FILE_PATH = LOG_RESP_PATH
			+ FILENAME_FORMAT.format(new Date(System.currentTimeMillis())) + ".txt";
	public static String LOG_REQ_FILE_PATH = LOG_REQ_PATH + FILENAME_FORMAT.format(new Date(System.currentTimeMillis()))
			+ ".txt";



	public static File getCacheFile() {
		String state = android.os.Environment.getExternalStorageState();
		if (!sdCardIsAvailable()) {
			LogUtils.e(TAG, "SD Card is not mounted,It is  " + state + ".");
		}
		if (!CACHE_FILE.exists() && !CACHE_FILE.mkdirs()) {
			LogUtils.e(TAG, "Path to file could not be created");
		}
		if (!CACHE_FILE.exists()) {
			CACHE_FILE.mkdirs();
		}
		return CACHE_FILE;
	}

	public static File getDBFile() {
		String state = android.os.Environment.getExternalStorageState();
		if (!sdCardIsAvailable()) {
			LogUtils.e(TAG, "SD Card is not mounted,It is  " + state + ".");
		}
		if (!DB_FILE.exists() && !DB_FILE.mkdirs()) {
			LogUtils.e(TAG, "Path to file could not be created");
		}
		if (!DB_FILE.exists()) {
			LogUtils.e(TAG, "mkdirs db");
			DB_FILE.mkdirs();
		}
		return DB_FILE;
	}

	public static File getQuestionFileFolder() {
		if (!QUESTION_FILE.exists()) {
			QUESTION_FILE.mkdirs();
		}
		return QUESTION_FILE;
	}

	public static void clearQuestionFiles() {
		File questionFolder = getQuestionFileFolder();
		if (null != questionFolder && questionFolder.isDirectory()) {
			File[] files = questionFolder.listFiles();
			if (null != files) {
				for (File f : files) {
					if (null != f && f.exists()) {
						try {
							f.delete();
						} catch (Exception e) {
						}
					}
				}
			}
		}
	}

	public static void deleteQuestionFiles() {
		File file = getQuestionFileFolder();
		if (file != null) {
			File[] list = file.listFiles();
			for (File f : list) {
				if (null != f && f.exists()) {
					try {
						f.delete();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static File getAnswerFile() {
		if (!ANSWER_FILE.exists()) {
			ANSWER_FILE.mkdirs();
		}
		return ANSWER_FILE;
	}

	public static File getImgMsgFile() {
		if (!IMG_MSG_FILE.exists()) {
			IMG_MSG_FILE.mkdirs();
		}
		return IMG_MSG_FILE;
	}

	public static File getContactImgFile() {
		if (!CONTACT_IMG_FILE.exists()) {
			CONTACT_IMG_FILE.mkdirs();
		}
		return CONTACT_IMG_FILE;
	}

	public static File getVoiceFile() {
		if (!VOICE_FILE.exists()) {
			VOICE_FILE.mkdirs();
		}
		return VOICE_FILE;
	}

	public static String getWelcomeImagePath() {
		if (!WELCOME_IMAGE_FOLDER.exists()) {
			WELCOME_IMAGE_FOLDER.mkdirs();
		}
		return WELCOME_IMAGE_FOLDER.getAbsolutePath() + File.separator + WELCOME_IMAGE_NAME;
	}

	public static String getShotImagePath() {
		if (!SHOT_FILE.exists()) {
			SHOT_FILE.mkdirs();
		}
		return SHOT_FILE.getAbsolutePath() + File.separator + SHOT_IMAGE_NAME;
	}

	public static void deleteWelcomeImage() {
		File wFile = new File(WELCOME_IMAGE_FOLDER.getAbsolutePath() + File.separator + WELCOME_IMAGE_NAME);
		if (null != wFile && wFile.exists()) {
			try {
				wFile.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void deleteOutLogs(String dirPath) {
		File dir = new File(dirPath);
		try {
			final long currTime = System.currentTimeMillis();
			File[] files = dir.listFiles(new FilenameFilter() {

				public boolean accept(File dir, String filename) {
					File f = new File(dir.getAbsolutePath() + "/" + filename);
					long time = f.lastModified();
					if (currTime - time > LOG_OUT_TIME) {
						return true;
					}
					return false;
				}
			});
			if (files == null) {
				return;
			}
			for (File f : files) {
				try {
					f.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteVoiceFile() {
		String state = android.os.Environment.getExternalStorageState();
		if (!sdCardIsAvailable()) {
			LogUtils.e(TAG, "SD Card is not mounted,It is  " + state + ".");
		}
		if (!VOICE_FILE.exists() && !VOICE_FILE.mkdirs()) {
			LogUtils.e(TAG, "Path to file could not be created");
		}
		if (VOICE_FILE.exists()) {
			VOICE_FILE.delete();
		}
	}

	public static String encodeFileByBase64(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		String base64Content = null;
		File file = new File(path);
		if (file.exists()) {
			FileInputStream input = null;
			try {
				input = new FileInputStream(file);
				byte[] buffer = new byte[(int) file.length()];
				input.read(buffer);
				base64Content = Base64.encodeToString(buffer, Base64.DEFAULT);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
					}
					input = null;
				}
			}
		}
		return base64Content;
	}

	public static void decodeFileByBase64(String content, String audioPath) {
		if (content == null || audioPath == null) {
			return;
		}
		byte[] bytes = Base64.decode(content, Base64.DEFAULT);
		File file = new File(audioPath);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(bytes);
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
				fos = null;
			}
		}
	}

	public synchronized static boolean writeLogFile(String path, List<String> content) {
		if (sdCardIsAvailable() && sdCardHasEnoughSpace()) {
			if (!isFileExist(path)) {
				File file = new File(path);
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return writeFile(path, content, true);
		}
		return false;
	}

	/**
	 * SDCard是否可用
	 * 
	 * @return
	 */
	public static boolean sdCardIsAvailable() {
		String state = Environment.getExternalStorageState();
		return state.equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * sdcard是否有足够的空间
	 * 
	 * @return
	 */
	public static boolean sdCardHasEnoughSpace() {
		if (!sdCardIsAvailable()) {
			return false;
		}
		long realSize = getRealSizeOnSdCard();
		return realSize > 0;
	}

	/**
	 * 得到SD卡的大小
	 * 
	 * @return
	 */
	private static long getRealSizeOnSdCard() {
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
		StatFs statfs = new StatFs(file.getPath());
		long blockSize = statfs.getBlockSize();
		long availableBlocks = statfs.getAvailableBlocks();
		return blockSize * availableBlocks;
	}
}
