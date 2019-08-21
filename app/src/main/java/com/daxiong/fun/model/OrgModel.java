
package com.daxiong.fun.model;

import java.io.Serializable;

/**
 * 机构实体类 此类的描述：机构实体
 * 
 * @author: sky @最后修改人： sky
 * @最后修改日期:2015-7-31 下午3:08:27
 */
public class OrgModel implements Serializable {

    private static final long serialVersionUID = -7785172378346890901L;

    public static final String TAG = OrgModel.class.getSimpleName();

    private String orgname;// 机构名称

    private int orgid;// 机构id

    // sky add
    private String logo;//机构图像
    
    private int orgtype;//机构类型 (1为vip,2为外包) 

    private int relationtype;//关系(# 1关注 2成员) 

    private String sidlist;//科目列表,格式类似于:("1,2,6")
    
    
    
    
    
    
    
    

    public int getOrgtype() {
        return orgtype;
    }

    public void setOrgtype(int orgtype) {
        this.orgtype = orgtype;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getRelationtype() {
        return relationtype;
    }

    public void setRelationtype(int relationtype) {
        this.relationtype = relationtype;
    }

    public String getSidlist() {
        return sidlist;
    }

    public void setSidlist(String sidlist) {
        this.sidlist = sidlist;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public int getOrgid() {
        return orgid;
    }

    public void setOrgid(int orgid) {
        this.orgid = orgid;
    }

    @Override
    public String toString() {
        return "ChoiceListModel [orgname=" + orgname + ", orgid=" + orgid + "]";
    }

}
