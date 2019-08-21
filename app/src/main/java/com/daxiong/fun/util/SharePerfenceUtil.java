
package com.daxiong.fun.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.daxiong.fun.MyApplication;

import java.util.Map;

/**
 * SharePerfenceUtil 工具类
 * 
 * @author: sky
 */
public class SharePerfenceUtil {
    private static SharedPreferences sp;

    private static Editor editor;

    private static final String SP_NAME = "welearn_sp";

    static {
        sp = MyApplication.getContext().getSharedPreferences(SP_NAME, Context.MODE_APPEND);
        editor = sp.edit();
    }

    public static void putString(String key, String value) {
        if (key == null) {
            return;
        }
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public static void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }
    public static void putLong(String key, long value) {
    	editor.putLong(key, value);
    	editor.commit();
    }
    
    public static long getLong(String key, long defValue) {
    	return sp.getLong(key, defValue);
    }

    public static void putBoolean(String key, Boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(String key, Boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    public static Map<String, ?> getAll() {
        return sp.getAll();
    }

    /**
     * shareperfence保存数据
     * @author:  sky
     * @param map void
     */
    public static void saveData(Map<String, Object> map) {
        for (Map.Entry<String, Object> item : map.entrySet()) {
            String key = item.getKey();
            Object value = item.getValue();

            if (value instanceof String) {
                editor.putString(key, String.valueOf(value));
            } else if (value instanceof Integer) {
                editor.putInt(key, (Integer)value);
            } else if (value instanceof Float) {
                editor.putFloat(key, (Float)value);
            } else if (value instanceof Long) {
                editor.putLong(key, (Long)value);
            } else if (value instanceof Boolean) {
                editor.putBoolean(key, (Boolean)value);
            }
            editor.commit();
        }
    }

    /**
     * 取出数据
     * @author:  sky
     * @param key
     * @param clazz
     * @return Object
     */
    public static Object getData(String key, Class clazz) {
        Object obj = null;
        if (clazz.getName().equals(String.class.getName())) {
            obj = sp.getString(key, "");
        } else if (clazz.getName().equals(Integer.class.getName())) {
            obj = sp.getInt(key, 0);
        } else if (clazz.getName().equals(Float.class.getName())) {
            obj = sp.getFloat(key, 0);
        } else if (clazz.getName().equals(Long.class.getName())) {
            obj = sp.getLong(key, 0);
        } else if (clazz.getName().equals(Boolean.class.getName())) {
            obj = sp.getBoolean(key, false);
        }
        return obj;

    }

 

}
