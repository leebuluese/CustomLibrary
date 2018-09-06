package com.lyz.lib.photo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lyz.lib.photoview.PhotoView;

import com.lyz.lib.util.GlideUtil;


/**
 * Created by lyz on 2018/5/22.
 *
 */
public class BrowsePhotoFragment extends Fragment {

    private final String KEY_PATH = "path";

    public static BrowsePhotoFragment newInstance() {
        Bundle args = new Bundle();
        BrowsePhotoFragment fragment = new BrowsePhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Activity context = (Activity) getActivity();
        PhotoView view = new PhotoView(context);
        view.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Bundle arguments = getArguments();
        if (null != arguments) {
            String urlPath = arguments.getString(KEY_PATH, "");
            if (context != null) {
                GlideUtil.loadImg(context, view, urlPath);
            }
        }
        return view;
    }

}