package com.xwj.adsview.fragment;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xwj.adsview.R;
import com.xwj.adsview.view.AdsView;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.List;

/**
 * Created by Xu Wenjian on 2017/5/8.
 */

public class BannerFragment extends AdsFragment {

    public static final String KEY_IMAGE_URLS = "KEY_IMAGE_URLS";
    public static final String KEY_HEIGHT = "KEY_HEIGHT";

    private FrameLayout frameLayout;
    private Banner banner;
    private List<String> bannerImageUrls;
    private int height;

    public BannerFragment() {
        super(R.layout.fragment_banner);
    }

    @Override
    protected View onCreate(View view) {
        frameLayout = (FrameLayout) view;
        banner = (Banner) findViewById(R.id.banner);
        height = getArguments().getInt(KEY_HEIGHT);
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        //banner.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Glide.with(getContext()).load((String) path).into(imageView);
            }
        });
        bannerImageUrls = getArguments().getStringArrayList(KEY_IMAGE_URLS);

        AdsView.countDownLatch.countDown();

        if (bannerImageUrls != null && !bannerImageUrls.isEmpty()) {
            banner.setImages(bannerImageUrls);
            banner.start();
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
}
