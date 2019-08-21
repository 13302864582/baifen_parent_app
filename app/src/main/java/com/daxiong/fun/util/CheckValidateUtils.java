package com.daxiong.fun.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */

public class CheckValidateUtils {


    /*
     要更加准确的匹配手机号码只匹配11位数字是不够的，比如说就没有以144开始的号码段，
     故先要整清楚现在已经开放了多少个号码段，国家号码段分配如下：
     移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     联通：130、131、132、152、155、156、185、186
     电信：133、153、180、189、（1349卫通）
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean isPhoneNo() {
        String value = "手机号";
        String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(value);
        return m.find();//boolean
    }


}
