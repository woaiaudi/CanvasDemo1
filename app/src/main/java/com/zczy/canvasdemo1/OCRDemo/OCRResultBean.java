package com.zczy.canvasdemo1.OCRDemo;

import java.util.List;

/**
 * Created by mac on 16/4/12.
 */
public class OCRResultBean {

    private OCRMessageBean message;
    private List<OCRCardInfoBean> cardsinfo;

    public OCRMessageBean getMessage() {
        return message;
    }

    public void setMessage(OCRMessageBean message) {
        this.message = message;
    }

    public List<OCRCardInfoBean> getCardsinfo() {
        return cardsinfo;
    }

    public void setCardsinfo(List<OCRCardInfoBean> cardsinfo) {
        this.cardsinfo = cardsinfo;
    }
}
