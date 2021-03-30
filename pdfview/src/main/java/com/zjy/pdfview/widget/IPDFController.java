package com.zjy.pdfview.widget;

/**
 * Date: 2021/3/19
 * Author: Yang
 * Describe: PDF控制栏接口
 */
public interface IPDFController {

    void addOperateListener(OperateListener listener);

    void setPageIndexText(String text);

    interface OperateListener {

        void clickPrevious();

        void clickNext();
    }

}
