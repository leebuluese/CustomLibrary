package com.lyz.lib.photo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by lyz on 2018/5/22.
 *
 */
public class BrowsePhotoPagerAdapter extends FragmentStatePagerAdapter {


    private final String KEY_PATH = "path";
    private BrowsePhotoFragment[] fragments;
    private List<String> urls;

    public BrowsePhotoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(List<String> list) {
        this.fragments = new BrowsePhotoFragment[list.size()];
        this.urls = list;
    }

    @Override
    public int getItemPosition(Object object) {
        return FragmentStatePagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Fragment getItem(int position) {

        BrowsePhotoFragment fragment = fragments[position];
        if (fragment == null) {
            fragment = BrowsePhotoFragment.newInstance();
            Bundle bundle = new Bundle();
            String url = urls.get(position);
            // 加载原图地址
            if (url.contains("http")) {
                url = url.replace("middle", "original");
                url = url.replace("small", "original");
            }
            bundle.putString(KEY_PATH, url);
            fragment.setArguments(bundle);
            fragments[position] = fragment;
        }
        return fragment;
    }
}
