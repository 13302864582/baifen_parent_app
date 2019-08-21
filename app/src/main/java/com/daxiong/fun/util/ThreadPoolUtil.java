
package com.daxiong.fun.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池工具类
 * 
 * @author: sky
 */
public class ThreadPoolUtil {

    private static ExecutorService pool;

    static {
        pool = Executors.newCachedThreadPool();
    }

    public static void execute(Runnable command) {
        pool.execute(command);
    }

    /**
     *线程池不再接受新的任务
     * @author:  sky void
     */
    public static void showdown() {
        pool.shutdown();
    }

    /**
     * 立即停止线程池
     * @author:  sky void
     */
    public static void showdownNow() {
        pool.shutdownNow();
    }

}
