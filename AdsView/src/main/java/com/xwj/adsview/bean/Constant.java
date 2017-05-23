package com.xwj.adsview.bean;

import android.os.Environment;

/**
 * Created by Xu Wenjian on 2017/5/10.
 */

public interface Constant {
    String BASE_URL = "https://icontinua.com";

    /** sd卡存储路径 */
    String DATA_PATH = Environment.getExternalStorageDirectory() + "/adsplayer/";

    /**
     * 刷新数据的时间间隔
     * 毫秒
     */
    long interval = 60 * 5 * 1000;
}
