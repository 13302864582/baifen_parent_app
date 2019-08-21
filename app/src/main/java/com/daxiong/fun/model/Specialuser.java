
package com.daxiong.fun.model;

import java.io.Serializable;

/**
 * 此类的描述：特殊学生
 * 
 * @author: sky @最后修改人： sky
 * @最后修改日期:2015年8月8日 下午4:21:35
 */
public class Specialuser implements Serializable {

    private static final long serialVersionUID = 3747931650466257050L;

    private int orgid;// 所属机构

    private int type;// 是否是特殊账号 1是,0不是

    public int getOrgid() {
        return orgid;
    }

    public void setOrgid(int orgid) {
        this.orgid = orgid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
