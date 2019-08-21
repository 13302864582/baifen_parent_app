package com.daxiong.fun.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/****************
 * 文件读取与保存
 * 
 * @author xiaobao
 * 
 ****************/
public class FileOperationUtils {
	/**
	 * 根据路径读取文件
	 * 
	 * @param readPath
	 *            读取文件的路径
	 * @return
	 * @throws Exception
	 */
	public static String readFile(String readPath) throws Exception {
		return readFile(new File(readPath));
	}

	/**
	 * 读取文件
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String readFile(File file) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(file));
		StringBuffer sbf = new StringBuffer("");
		String line = null;
		while ((line = br.readLine()) != null) {
			sbf.append(line).append("\r\n");// 按行读取，追加换行\r\n
		}
		br.close();
		return sbf.toString();
	}

	/**
	 * 写入文件
	 * 
	 * @param str
	 *            要保存的内容
	 * @param savePath
	 *            保存的文件路径
	 * @throws Exception
	 *             找不到路径
	 */
	public static void writeFile(String str, String savePath) throws Exception {
		BufferedWriter bw = new BufferedWriter(new FileWriter(savePath));
		bw.write(str);
		bw.close();
	}

}