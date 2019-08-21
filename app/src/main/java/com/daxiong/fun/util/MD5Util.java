package com.daxiong.fun.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	/**
	 * 榛樿鐨勫瘑鐮佸瓧绗︿覆缁勫悎锛岀敤鏉ュ皢瀛楄妭杞崲锟�? 16 杩涘埗琛ㄧず鐨勫瓧锟�?,apache鏍￠獙涓嬭浇鐨勬枃浠剁殑姝ｇ‘鎬х敤鐨勫氨鏄粯璁ょ殑杩欎釜缁勫悎
	 */
	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	protected static MessageDigest messagedigest = null;
	static {
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsaex) {
			System.err.println(MD5Util.class.getName()
					+ "鍒濆鍖栧け璐ワ紝MessageDigest涓嶆敮鎸丮D5Util");
			nsaex.printStackTrace();
		}
	}
	
	/**
	 * 鐢熸垚瀛楃涓茬殑md5鏍￠獙锟�?
	 * 
	 * @param s
	 * @return
	 */
	public static String getMD5String(String s) {
		if (null == s) {
			return s;
		}
		return getMD5String(s.getBytes());
	}
	
	/**
	 * 鍒ゆ柇瀛楃涓茬殑md5鏍￠獙鐮佹槸鍚︿笌锟�?涓凡鐭ョ殑md5鐮佺浉鍖归厤
	 * 
	 * @param password 瑕佹牎楠岀殑瀛楃锟�?
	 * @param md5PwdStr 宸茬煡鐨刴d5鏍￠獙锟�?
	 * @return
	 */
	public static boolean checkPassword(String password, String md5PwdStr) {
		String s = getMD5String(password);
		return s.equals(md5PwdStr);
	}
	
	/**
	 * 鐢熸垚鏂囦欢鐨刴d5鏍￠獙锟�?
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getFileMD5String(File file) throws IOException {		
		InputStream fis;
	    fis = new FileInputStream(file);
	    byte[] buffer = new byte[1024];
	    int numRead = 0;
	    while ((numRead = fis.read(buffer)) > 0) {
	    	messagedigest.update(buffer, 0, numRead);
	    }
	    fis.close();
		return bufferToHex(messagedigest.digest());
	}

	/**
	 * JDK1.4涓笉鏀寔浠appedByteBuffer绫诲瀷涓哄弬鏁皍pdate鏂规硶锛屽苟涓旂綉涓婃湁璁ㄨ瑕佹厧鐢∕appedByteBuffer锟�?
	 * 鍘熷洜鏄綋浣跨敤 FileChannel.map 鏂规硶鏃讹紝MappedByteBuffer 宸茬粡鍦ㄧ郴缁熷唴鍗犵敤浜嗕竴涓彞鏌勶紝
	 * 鑰屼娇锟�? FileChannel.close 鏂规硶鏄棤娉曢噴鏀捐繖涓彞鏌勭殑锛屼笖FileChannel鏈夋病鏈夋彁渚涚被锟�? unmap 鐨勬柟娉曪紝
	 * 鍥犳浼氬嚭鐜版棤娉曞垹闄ゆ枃浠剁殑鎯呭喌锟�?
	 * 
	 * 涓嶆帹鑽愪娇锟�?
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getFileMD5String_old(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		FileChannel ch = in.getChannel();
		MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,
				file.length());
		messagedigest.update(byteBuffer);
		String result = bufferToHex(messagedigest.digest());
		in.close();
		return result;
	}

	public static String getMD5String(byte[] bytes) {
		messagedigest.update(bytes);
		return bufferToHex(messagedigest.digest());
	}

	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];// 鍙栧瓧鑺備腑锟�? 4 浣嶇殑鏁板瓧杞崲, >>> 涓猴拷?锟借緫鍙崇Щ锛屽皢绗﹀彿浣嶄竴璧峰彸锟�?,姝ゅ鏈彂鐜颁袱绉嶇鍙锋湁浣曚笉锟�? 
		char c1 = hexDigits[bt & 0xf];// 鍙栧瓧鑺備腑锟�? 4 浣嶇殑鏁板瓧杞崲 
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}
}
