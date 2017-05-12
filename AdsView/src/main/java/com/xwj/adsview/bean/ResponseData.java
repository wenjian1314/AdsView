package com.xwj.adsview.bean;

import com.alibaba.fastjson.JSONObject;
import com.xwj.adsview.exception.ParseResponseException;

/**
 * Created by Xu Wenjian on 2017/5/8.
 */

public class ResponseData {
    private int status;
    private long timestamp;
    private Object data;
    private int errno;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public static ResponseData parse(String text) throws ParseResponseException {
        try {
            return JSONObject.parseObject(text, ResponseData.class);
        } catch (Exception e) {
            throw new ParseResponseException("数据格式错误", e);
        }
    }

    public static ResponseData parse(byte[] byteArray) throws ParseResponseException {
        try {
            return parse(new String(byteArray));
        } catch (NullPointerException e) {
            throw new ParseResponseException("数据为空", e);
        }
    }
}

