package com.zczy.canvasdemo1.OCRDemo;

import java.util.List;

/**
 * Created by mac on 16/4/12.
 */
public class OCRCardInfoBean {
    private String type;
    private List<OCRCardContentItemBean> items;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<OCRCardContentItemBean> getItems() {
        return items;
    }

    public void setItems(List<OCRCardContentItemBean> items) {
        this.items = items;
    }
}
