
package com.daxiong.fun.function.homepage.model;

import java.io.Serializable;

public class SpecialCalendarEntity implements Serializable {

    private static final long serialVersionUID = -4968247882796366734L;

    private String dataStr = "";

    private int checkStatus;// 发题状态 1代表全对 2代表有错题 3代表未检查 

    private int errCount = 0;
    
    
    private String emptyStr="";
    
    

    public String getEmptyStr() {
        return emptyStr;
    }

    public void setEmptyStr(String emptyStr) {
        this.emptyStr = emptyStr;
    }

    public String getDataStr() {
        return dataStr;
    }

    public void setDataStr(String dataStr) {
        this.dataStr = dataStr;
    }

   
    public int getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(int checkStatus) {
        this.checkStatus = checkStatus;
    }

    public int getErrCount() {
        return errCount;
    }

    public void setErrCount(int errCount) {
        this.errCount = errCount;
    }
    
    

   
}
