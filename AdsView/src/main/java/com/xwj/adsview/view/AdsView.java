package com.xwj.adsview.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.xwj.adsview.R;
import com.xwj.adsview.bean.AdsInfo;
import com.xwj.adsview.bean.Constant;
import com.xwj.adsview.bean.ResponseData;
import com.xwj.adsview.exception.ParseResponseException;
import com.xwj.adsview.fragment.BannerFragment;
import com.xwj.adsview.fragment.VideoFragment;
import com.xwj.adsview.utils.HttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Xu Wenjian on 2017/5/9.
 */

public class AdsView extends LinearLayout {

    private FragmentManager fragmentManager;
    private View progressBarView;
    private View errorView;
    private TextView errorText;
    private Timer timer;
    private Context context;

    public static CountDownLatch countDownLatch;

    private static final int INIT = 0;
    private static final int REFRESH = 1;
    private static final int ERROR_DATA = 2;
    private static final int ERROR_SERVER = 3;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INIT:
                    showProgressBar();
                    break;
                case REFRESH:
                    refreshView(context);
                    break;
                case ERROR_DATA:
                    removeAllViews();
                    errorText.setText("数据解析错误！");
                    addView(errorView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    break;
                case ERROR_SERVER:
                    removeAllViews();
                    errorText.setText("服务器出错！");
                    addView(errorView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    break;
            }

        }
    };

    public AdsView(Context context) {
        super(context);
        init(context);
    }

    public AdsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AdsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        setOrientation(VERTICAL);

        errorView = LayoutInflater.from(context).inflate(R.layout.error, null);
        errorText = (TextView) errorView.findViewById(R.id.error_text);

        //默认显示环形进度条
        progressBarView = LayoutInflater.from(context).inflate(R.layout.welcome_progress_bar, null);
        showProgressBar();

        schedule();
    }

    private void schedule() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                HttpUtils.getSync(context, Constant.BASE_URL + "/api/ads/apkads", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        ResponseData responseData = null;
                        try {
                            responseData = ResponseData.parse(responseBody);
                            AdsInfo refreshAdsInfo = JSON.parseObject(responseData.getData().toString(), AdsInfo.class);
                            //如果adsInfo有更新，则更新UI
                            if (!AdsInfo.compareAndSet(refreshAdsInfo)) {
                                timer.cancel();
                                countDownLatch = new CountDownLatch(refreshAdsInfo.getPartNum());
                                handler.sendEmptyMessage(REFRESH);

                                countDownLatch.await();
                                schedule();
                            }
                        } catch (ParseResponseException e) {
                            handler.sendEmptyMessage(ERROR_DATA);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        handler.sendEmptyMessage(ERROR_SERVER);
                    }
                });
            }
        }, 0, Constant.interval);
    }

    /**
     * 显示进度条
     */
    private void showProgressBar() {
        removeAllViews();
        addView(progressBarView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 更新界面
     */
    public void refreshView(Context context) {
        if (fragmentManager == null) return;

        int parts = AdsInfo.getAdsInfo().getPartNum();
        int height = getFragmentHeight(context, parts);
        List<AdsInfo.Content> contentList = AdsInfo.getAdsInfo().getContent();
        if (contentList != null && !contentList.isEmpty()) {
            //清除所有view
            removeAllViews();

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            for (AdsInfo.Content content : contentList) {
                if (content.getPicUrls() != null && !content.getPicUrls().isEmpty()) {
                    System.out.println("picUrls:" + content.getPicUrls());
                    BannerFragment bannerFragment = new BannerFragment();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList(BannerFragment.KEY_IMAGE_URLS, (ArrayList<String>) content.getPicUrls());
                    bundle.putInt(BannerFragment.KEY_HEIGHT, height);
                    bannerFragment.setArguments(bundle);

                    transaction.add(getId(), bannerFragment);
                }
                else if (content.getVideoUrls() != null && !content.getVideoUrls().isEmpty()) {
                    VideoFragment videoFragment = new VideoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList(VideoFragment.KEY_VIDEO_URLS, (ArrayList<String>) content.getVideoUrls());
                    bundle.putInt(VideoFragment.KEY_HEIGHT, height);
                    videoFragment.setArguments(bundle);

                    transaction.add(getId(), videoFragment);
                }
            }
            transaction.commit();

        }
    }

    /**
     * 根据parts的个数按比例分屏
     * @param context
     * @param parts
     * @return 每屏的高度
     */
    private int getFragmentHeight(Context context, int parts) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        int height = metric.heightPixels;
        return height / parts;
    }

    /**
     * 设置FragmentManager
     * @param fragmentManager
     */
    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (timer != null) {
            timer.cancel();
        }
        super.onDetachedFromWindow();
    }
}
