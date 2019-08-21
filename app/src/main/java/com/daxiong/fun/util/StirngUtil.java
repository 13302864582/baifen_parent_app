package com.daxiong.fun.util;

import com.daxiong.fun.MyApplication;

import java.text.MessageFormat;

public class StirngUtil {

	public static String getRadomChar() {
		char c = (char) (int) (Math.random() * 26 + 97);
		return String.valueOf(c);
	}
	
	public static String format(String format, Object... args){
		return MessageFormat.format(format, args);
	}
	
	public static String format(int res, Object... args){
		String format = MyApplication.getContext().getResources().getString(res);
		return MessageFormat.format(format, args);
	}
    public static String ToSBC(String input) { 
    	  char[] c = input.toCharArray();  
          for (int i = 0; i < c.length; i++) {  
              if (c[i] == 12288) {  
                  c[i] = (char) 32;  
                  continue;  
              }  
              if (c[i] > 65280 && c[i] < 65375)  
                  c[i] = (char) (c[i] - 65248);  
          }  
          return new String(c);  
    } 

}
