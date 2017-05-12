package com.xwj.adsview.exception;

/**
 * Created by Xu Wenjian on 2017/5/8.
 */

public class ParseResponseException extends Exception {
    private String errorMessage;

    public ParseResponseException() {
        super();
    }

    public ParseResponseException(String detailMessage) {
        super(detailMessage);
        this.errorMessage = detailMessage;
    }

    public ParseResponseException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        this.errorMessage = detailMessage;
    }

    public ParseResponseException(Throwable throwable) {
        super(throwable);
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
