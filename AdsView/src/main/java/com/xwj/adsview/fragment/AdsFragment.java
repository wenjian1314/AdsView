package com.xwj.adsview.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Xu Wenjian on 2017/5/8.
 */

public abstract class AdsFragment extends Fragment {

    private View mView;
    private int layoutId;

    public AdsFragment(int layoutId) {
        // Required empty public constructor
        this.layoutId = layoutId;
    }

    protected abstract View onCreate(View view);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null) {
            mView = inflater.inflate(layoutId, container, false);
            mView = onCreate(mView);
        }

        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }

        return mView;
    }
    protected View findViewById(int id) {
        return mView.findViewById(id);
    }

}
