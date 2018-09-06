package com.lyz.lib.photo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lyz.lib.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyz on 2018/5/22.
 * 浏览图片
 */
public class BrowsePhotoActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, HackyViewPager.OnTouchHackViewPagerListener, View.OnClickListener {

    public static final String KEY_LIST = "photoList";
    public static final String KEY_INDEX = "photoIndex";
    public static final String KEY_DELETE = "isDelete";

    /**
     * 图片集合
     */
    private List<String> photoList;
    /**
     * 查看角标
     */
    private int photoIndex;
    /**
     * 是否显示删除按钮
     * isDelete = true 显示删除按钮
     * 如果需要删除操作需要重写onActivityForResult方法，同时用startActivityForResult方式启动
     */
    private boolean isDelete;

    private TextView tvTitle;
    private ImageView ivBack;
    private ImageView ivDelete;
    private HackyViewPager viewPager;
    private RelativeLayout rlTitle;
    private RelativeLayout rlParent;

    private String formatPage = "%s/%s";

    private BrowsePhotoPagerAdapter mBrowsePhotoPagerAdapter;

    private Handler mHandler = new Handler();

    private final int VISIBLE_TIME = 3000;

    private Runnable toggleRunnable = new Runnable() {
        @Override
        public void run() {
            rlTitle.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getIntentData();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_photo);
        //设置为全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
        initData();
    }

    protected void getIntentData() {
        Intent intent = getIntent();
        if (null != intent) {
            photoList = (List<String>) intent.getSerializableExtra(KEY_LIST);
            photoIndex = intent.getIntExtra(KEY_INDEX, 0);
            isDelete = intent.getBooleanExtra(KEY_DELETE, false);
        }
        if (null == photoList) {
            photoList = new ArrayList<>();
        }
    }

    protected void initView() {
        tvTitle = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        ivDelete = findViewById(R.id.iv_delete);
        viewPager = findViewById(R.id.hvp_pager);
        rlTitle = findViewById(R.id.rl_top_layout);
        rlParent = findViewById(R.id.rl_photo_parent);
        viewPager.setOnTouchHackViewPagerListener(this);

        if (isDelete) {
            ivDelete.setVisibility(View.VISIBLE);
        } else {
            ivDelete.setVisibility(View.GONE);
        }

        ivBack.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        rlParent.setOnClickListener(this);

        toggleTitle();
    }

    protected void initData() {
        mBrowsePhotoPagerAdapter = new BrowsePhotoPagerAdapter(getSupportFragmentManager());
        mBrowsePhotoPagerAdapter.setData(photoList);

        viewPager.setAdapter(mBrowsePhotoPagerAdapter);
        viewPager.setCurrentItem(photoIndex);
        viewPager.addOnPageChangeListener(this);

        String formatText = String.format(formatPage, photoIndex + 1, photoList.size());
        tvTitle.setText(formatText);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // no code
    }

    @Override
    public void onPageSelected(int position) {
        this.photoIndex = position;

        String formatText = String.format(formatPage, photoIndex + 1, photoList.size());
        tvTitle.setText(formatText);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // no code
    }

    private void toggleTitle() {
        rlTitle.setVisibility(View.VISIBLE);
        mHandler.removeCallbacks(toggleRunnable);
        mHandler.postDelayed(toggleRunnable, VISIBLE_TIME);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                back();
                break;
            case R.id.iv_delete:
                photoList.remove(photoIndex);
                if (photoList.isEmpty()) {
                    back();
                } else {
                    mBrowsePhotoPagerAdapter.setData(photoList);
                    mBrowsePhotoPagerAdapter.notifyDataSetChanged();
                }
                String formatText = String.format(formatPage, photoIndex + 1, photoList.size());
                tvTitle.setText(formatText);
                break;
            case R.id.rl_photo_parent:
                toggleTitle();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        if (isDelete) {
            Intent intentOne = new Intent();
            intentOne.putExtra(KEY_LIST, (Serializable) photoList);
            setResult(0, intentOne);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onTouchPager(MotionEvent event) {
        toggleTitle();
    }

}
