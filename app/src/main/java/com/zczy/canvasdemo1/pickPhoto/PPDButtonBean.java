package com.zczy.canvasdemo1.pickPhoto;

import android.graphics.Rect;

/**
 * Created by mac on 16/4/8.
 */
public class PPDButtonBean {
    private String name;
    private Rect contentRect;

    public PPDButtonBean(String name, Rect contentRect) {
        this.name = name;
        this.contentRect = contentRect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Rect getContentRect() {
        return contentRect;
    }

    public void setContentRect(Rect contentRect) {
        this.contentRect = contentRect;
    }
}
