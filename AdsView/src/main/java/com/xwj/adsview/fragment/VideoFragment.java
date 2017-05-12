package com.xwj.adsview.fragment;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.xwj.adsview.R;
import com.xwj.adsview.bean.Constant;
import com.xwj.adsview.utils.HttpUtils;
import com.xwj.adsview.view.AdsView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.io.SessionOutputBuffer;

/**
 * Created by Xu Wenjian on 2017/5/8.
 */

public class VideoFragment extends AdsFragment {

    public static final String KEY_VIDEO_URLS = "KEY_VIDEO_URLS";
    public static final String KEY_HEIGHT = "KEY_HEIGHT";

    private FrameLayout frameLayout;
    private VideoView videoView;
    private NumberProgressBar progressBar;
    private TextView progressText;
    private FrameLayout progressLayout;
    private List<String> videoUrls;
    private int height;

    private View errorView;
    private TextView errorText;

    /** 当前播放的是第几个视频 */
    private int index;

    /** 本地的视频文件路径列表 */
    private List<String> localPathList = new ArrayList<>();

    public VideoFragment() {
        super(R.layout.video);
    }

    @Override
    protected View onCreate(View view) {
        frameLayout = (FrameLayout) view;

        errorView = LayoutInflater.from(getContext()).inflate(R.layout.error, null);
        errorText = (TextView) errorView.findViewById(R.id.error_text);

        videoView = (VideoView) findViewById(R.id.video_view);
        videoView.setClickable(false);
        progressLayout = (FrameLayout) findViewById(R.id.progress_layout);
        progressBar = (NumberProgressBar) findViewById(R.id.progress_bar);
        progressText = (TextView) findViewById(R.id.progress_text);

        height = getArguments().getInt(KEY_HEIGHT);
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));

        videoUrls = getArguments().getStringArrayList(KEY_VIDEO_URLS);
        if (videoUrls != null && !videoUrls.isEmpty()) {
            List<String> downloadUrlList = filterDownloadUrls(videoUrls);
            if (downloadUrlList.isEmpty()) {
                play();
            }
            else {
                refreshVideos(downloadUrlList, 0);
            }

            return view;
        }
        else {
            TextView emptyView = new TextView(getActivity());
            emptyView.setLayoutParams(new ViewGroup.LayoutParams(height, ViewGroup.LayoutParams.MATCH_PARENT));
            emptyView.setGravity(Gravity.CENTER);
            emptyView.setText("资源为空！");
            return emptyView;
        }
    }

    /**
     * 根据给定的url过滤出需要更新的url，并更新至localPathList
     * @param videoUrls
     * @return
     */
    private List<String> filterDownloadUrls(List<String> videoUrls) {
        String videoPath = Constant.DATA_PATH + "videos/";
        File videoPathFile = new File(videoPath);
        if (!videoPathFile.exists()) videoPathFile.mkdirs();
        //获取该目录下的文件名集合
        Set<String> videoFileSet = new HashSet<>(Arrays.asList(videoPathFile.list()));

        List<String> downloadUrls = new ArrayList<>();
        localPathList.clear();
        for (String url : videoUrls) {
            String filename = url.substring(url.lastIndexOf('/') + 1);
            if (!videoFileSet.contains(filename)) {
                downloadUrls.add(url);
            }
            localPathList.add(videoPath + filename);
        }
        return downloadUrls;
    }

    /**
     * 下载视频
     * @param downloadUrlList 需要下载的url列表
     * @param index 当前下载的是第几个url
     */
    private void refreshVideos(List<String> downloadUrlList, int index) {
        if (index >= downloadUrlList.size()) return;

        progressText.setText(index + "/" + downloadUrlList.size());

        File file = new File(Constant.DATA_PATH + "videos/");
        if (!file.exists()) file.mkdirs();
        HttpUtils.get(getContext(), downloadUrlList.get(index), new FileAsyncHttpResponseHandler(file) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Log.e("refreshVideos", "下载出错");
                frameLayout.removeAllViews();
                errorText.setText("视频加载出错！");
                frameLayout.addView(errorView);
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                int percent = (int) ((bytesWritten * 1.0 / totalSize) * 100);
                progressBar.setProgress(percent);
                super.onProgress(bytesWritten, totalSize);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                if (index == downloadUrlList.size() - 1) {
                    progressLayout.setVisibility(View.GONE);
                    videoView.setVisibility(View.VISIBLE);

                    AdsView.countDownLatch.countDown();

                    play();
                }
                else {
                    refreshVideos(downloadUrlList, index + 1);
                }
            }
        });
    }

    private void play() {
        progressLayout.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);
        doPlay();
    }

    /**
     * 播放视频
     */
    private void doPlay() {
        videoView.setVideoPath(localPathList.get(index++));
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                index %= videoUrls.size();
                doPlay();
            }
        });
    }
}
