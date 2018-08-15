package com.lyz.lib.bitmap.longbitmap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.lyz.lib.R;
import com.lyz.lib.util.LubanUtil;
import com.lyz.lib.util.PhotoUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;

public class LongBitmapActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LongBitmapActivity.class.getSimpleName();
    private final int REQUEST_CODE = 26;

    private int screenWidth = 0;
    private int screenHeight = 0;
    private RecyclerView rvPhotos;
    private LongBitmapAdapter bitmapAdapter;
    private List<Bitmap> newBitList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
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
        Log.e(TAG, "onCreate   screenWidth = " + screenWidth + " ; screenHeight = " + screenHeight);

        findViewById(R.id.btn_add).setOnClickListener(this);

        rvPhotos = findViewById(R.id.rv_photos);
        rvPhotos.setLayoutManager(new LinearLayoutManager(this));


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

    private void showBitmap(List<Bitmap> bitmap) {

        if (null == bitmapAdapter) {
            bitmapAdapter = new LongBitmapAdapter(this);
            bitmapAdapter.setData(bitmap);
            rvPhotos.setAdapter(bitmapAdapter);
        } else {
            bitmapAdapter.setData(bitmap);
            bitmapAdapter.notifyDataSetChanged();
        }
    }

    private void makeLongPic(List<Bitmap> list) {
        Bitmap longBit = null;
        newBitList = new ArrayList<>();
        for (Bitmap bitmap : list) {
            longBit = createBitmap(bitmap, longBit);
        }
        File file = PhotoUtil.getFileFromBitmap(LubanUtil.getDirectoryPath(PhotoUtil.CACHE_FILE_NAME), longBit);
        long size = file.length() >> 10;
        Log.e(TAG, "makeLongPic   size = " + size);
        showBitmap(newBitList);
    }

    private Bitmap createBitmap(Bitmap bitOne, Bitmap bitTwo) {
        Bitmap resultBit = null;
        if (null == bitTwo) {
            resultBit = mixBitmap(bitOne);
            newBitList.add(mixBitmap(bitOne));
        } else {
            Bitmap bitNew = mixBitmap(bitOne);
            newBitList.add(bitNew);
            if (bitNew != null) {
                int heightOne = bitNew.getHeight();
                int widthOne = bitNew.getWidth();
                int heightTwo = bitTwo.getHeight();
                int widthTwo = bitTwo.getWidth();
                int totalHeight = heightOne + heightTwo;
                Log.e(TAG, "createBitmap  heightOne = " + heightOne +
                        " ; heightTwo = " + heightTwo +
                        " ; totalHeight = " + totalHeight +
                        " ; widthOne = " + widthOne +
                        " ; widthTwo = " + widthTwo);
                try {
                    Bitmap bitmap = Bitmap.createBitmap(screenWidth, totalHeight, Bitmap.Config.ARGB_4444);
                    Canvas canvas = new Canvas(bitmap);
                    canvas.drawBitmap(bitTwo, 0, 0, null);
                    canvas.drawBitmap(bitNew, 0, heightTwo, null);
                    if (!bitTwo.isRecycled()) {
                        bitTwo.recycle();
                    }
//                    System.gc();
                    resultBit = bitmap;
                } catch (Exception e) {
                    Log.e(TAG, "drawBitmap e = " + e);
                }
            }
        }
        if (resultBit == null) {
            Log.e(TAG, "createBitmap   resultBit = null");
        }
        return resultBit;
    }

    private Bitmap mixBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width != screenWidth) {
            int scaleHeight = (height * screenWidth) / width;
            float widthScale = ((float) screenWidth) / width;
            float heightScale = ((float) scaleHeight) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(widthScale, heightScale);
            Log.e(TAG, "mixBitmap  width = " + width +
                    " ; height = " + height +
                    " ; scaleHeight = " + scaleHeight +
                    " ; widthScale = " + widthScale +
                    " ; heightScale = " + heightScale);
            try {
                return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            } catch (Exception e) {
                Log.e(TAG, "mixBitmap e = " + e);
            }
            Log.e(TAG, "mixBitmap   createBitmap = null");
            return null;
        } else {
            return bitmap;
        }
    }


}
