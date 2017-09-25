package com.daily.news.subscription.detail;

/**
 * Created by lixinke on 2017/9/25.
 */

public class RxException extends Exception {
    public int errCode;
    public RxException(String message, int errCode) {
        super(message);
        this.errCode = errCode;
    }
}
