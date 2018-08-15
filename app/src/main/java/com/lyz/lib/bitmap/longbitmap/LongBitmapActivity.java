package com.lyz.lib.bitmap.longbitmap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.lyz.lib.R;
import com.lyz.lib.util.LubanUtil;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;

public class LongBitmapActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQUEST_CODE = 26;

    private int screenWidth = 0;
    private int screenHeight = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_bitmap);

        // request permission
        ActivityCompat.requestPermissions(
                LongBitmapActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA},
                REQUEST_CODE);

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(outMetrics);
        }
        screenWidth = outMetrics.widthPixels;
        screenHeight = outMetrics.heightPixels;

        findViewById(R.id.btn_add).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                PhotoPicker.builder()
                        .setPhotoCount(9)
                        .setShowCamera(true)
                        .setShowGif(true)
                        .setPreviewEnabled(false)
                        .start(LongBitmapActivity.this, PhotoPicker.REQUEST_CODE);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (null != data) {
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                compressPhoto(photos);
            }

        }
    }

    private void compressPhoto(List<String> list) {
        LubanUtil.loadLocalImg(this, list, new LubanUtil.LubanImgListener() {
            @Override
            public void error(Throwable e) {
                Toast.makeText(LongBitmapActivity.this, "Compress Error!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void success(List<Bitmap> list) {
                makeLongPic(list);
            }
        });
    }

    private void makeLongPic(List<Bitmap> list) {
        for (Bitmap bitmap : list) {


        }
    }

    private void createBitmap(Bitmap bitOne, Bitmap bitTwo) {

    }

    private Bitmap mixBitmap(Bitmap bitmap) {
        if (bitmap.getWidth() != screenWidth) {
            int scaleHeight = (bitmap.getHeight() * screenWidth) / bitmap.getWidth();

            return null;
        } else {
            return bitmap;
        }
    }


}
