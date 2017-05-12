package com.xwj.adsplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xwj.adsview.view.AdsView;


public class MainActivity extends AppCompatActivity {

    private AdsView adsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adsView = (AdsView) findViewById(R.id.ads_view);
        adsView.setFragmentManager(getSupportFragmentManager());
    }
}
