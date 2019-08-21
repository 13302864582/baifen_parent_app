
package com.daxiong.fun.util;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class StorageUtils {
    private static final int ERROR = -1;

    public static int save_dir = 1;

    // 判断是否已经安装SD卡
    public static boolean isSDCardExist() {
        return android.os.Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡路径
     * 
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    // 内存剩余空间
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    // 内存总空间
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    // SD卡剩余空间
    public static long getAvailableExternalMemorySize() {
        if (isSDCardExist()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return ERROR;
        }
    }

    // SD卡总空间
    public static long getTotalExternalMemorySize() {
        if (isSDCardExist()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return ERROR;
        }
    }

    // 判断SD卡下external_sd文件夹的总大小
    public static long getTotalExternal_SDMemorySize() {
        if (isSDCardExist()) {
            File path = Environment.getExternalStorageDirectory();
            File externalSD = new File(path.getPath() + "/external_sd");
            if (externalSD.exists() && externalSD.isDirectory()) {
                StatFs stat = new StatFs(path.getPath() + "/external_sd");
                long blockSize = stat.getBlockSize();
                long totalBlocks = stat.getBlockCount();
                if (getTotalExternalMemorySize() != -1
                        && getTotalExternalMemorySize() != totalBlocks * blockSize) {
                    return totalBlocks * blockSize;
                } else {
                    return ERROR;
                }
            } else {
                return ERROR;
            }

        } else {
            return ERROR;
        }
    }

    // 判断SD卡下external_sd文件夹的可用大小
    public static long getAvailableExternal_SDMemorySize() {
        if (isSDCardExist()) {
            File path = Environment.getExternalStorageDirectory();
            File externalSD = new File(path.getPath() + "/external_sd");
            if (externalSD.exists() && externalSD.isDirectory()) {
                StatFs stat = new StatFs(path.getPath() + "/external_sd");
                long blockSize = stat.getBlockSize();
                long availableBlocks = stat.getAvailableBlocks();
                if (getAvailableExternalMemorySize() != -1
                        && getAvailableExternalMemorySize() != availableBlocks * blockSize) {
                    return availableBlocks * blockSize;
                } else {
                    return ERROR;
                }

            } else {
                return ERROR;
            }

        } else {
            return ERROR;
        }
    }

    /*
     * 存放在sdcard的根目录
     */
    public boolean saveFileToSdcardRoot(String fileName, byte[] data) {
        boolean flag = false;
        /*
         * 先判断sdcard的状态，是否存在
         */
        String state = Environment.getExternalStorageState();
        FileOutputStream outputStream = null;
        File rootFile = Environment.getExternalStorageDirectory(); // 获得sdcard的根路径
        /*
         * 表示sdcard挂载在手机上，并且可以读写
         */
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(rootFile, fileName);
            try {
                outputStream = new FileOutputStream(file);
                try {
                    outputStream.write(data, 0, data.length);
                    flag = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return flag;
    }

}
