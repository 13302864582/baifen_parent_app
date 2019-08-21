
package com.daxiong.fun.function.account.model;

import java.io.Serializable;
import java.util.List;

/**
 * vip详情model
 * @author skyyhu
 *
 */
public class VipInfoDetailModel implements Serializable {

    private static final long serialVersionUID = 4973914990921203499L;


    private String title;

    private List<DetailBean> detail;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<DetailBean> getDetail() {
        return detail;
    }

    public void setDetail(List<DetailBean> detail) {
        this.detail = detail;
    }

    public static class DetailBean {
        private String title;
        private int icon_type;
        private String content;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getIcon_type() {
            return icon_type;
        }

        public void setIcon_type(int icon_type) {
            this.icon_type = icon_type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
