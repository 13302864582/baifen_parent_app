
package com.daxiong.fun.model;

import java.io.Serializable;

/**
 * 此类的描述：区分小模块实体类(拍照提问/作业检查/历年考题/微课辅导)
 * 
 * @author: sky @最后修改人： sky
 * @最后修改日期:2015-7-30 下午5:34:28
 */
public class FuncModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private int bgResId;

    private String name;

    private String className;

    public FuncModel(int bg, String name, String clsName) {
        this.bgResId = bg;
        this.name = name;
        this.className = clsName;
    }

    public FuncModel(int bg, String name) {
        this.bgResId = bg;
        this.name = name;
    }

    public FuncModel() {
    }

    public int getBgResId() {
        return bgResId;
    }

    public void setBgResId(int bgResId) {
        this.bgResId = bgResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

}
