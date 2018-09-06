package com.lyz.lib.bitmap.asciibitmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.lyz.lib.R;
import com.lyz.lib.util.FileUtil;
import com.lyz.lib.util.LubanUtil;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by lyz on 2018/9/6.
 * 字符图片
 */


public class AsciiBitmapActivity extends AppCompatActivity{

    private Bitmap bitmap; //  = BitmapFactory.decodeResource(getResources(), R.mipmap.ddd);
    private ProgressBar pbProgress;
    private ImageView ivImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ascii_bitmap);
        pbProgress = findViewById(R.id.pb_progress);
        ivImage = findViewById(R.id.iv_image);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ddd);
    }

    public void doPick(View view) {
        AsciiUtil.choosePhoto(this, 1, PictureConfig.CHOOSE_REQUEST);
    }

    public void doSave(View view) {
        FileUtil.savaBitmap2SDcard(AsciiBitmapActivity.this, bitmap, System.currentTimeMillis() + "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                String path = "";
                if (selectList != null && selectList.size() > 0) {
                    LocalMedia localMedia = selectList.get(0);
                    if (localMedia.isCompressed()) {
                        path = localMedia.getCompressPath();
                    } else if (localMedia.isCut()) {
                        path = localMedia.getCutPath();
                    } else {
                        path = localMedia.getPath();
                    }
                }
                String filepath = AsciiUtil.amendRotatePhoto(path, AsciiBitmapActivity.this);
//                imageView.setImageBitmap(BitmapFactory.decodeFile(filepath));
                List<String> list = new ArrayList<>();
                list.add(filepath);
                compressPhoto(list);
            }

        }
    }

    private void compressPhoto(List<String> list) {
        LubanUtil.loadLocalImg(this, list, new LubanUtil.LubanImgListener() {
            @Override
            public void error(Throwable e) {
                Toast.makeText(AsciiBitmapActivity.this, "Compress Error!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void success(List<Bitmap> list) {
                refreshView(list);
            }

        });
    }

    void refreshView(final List<Bitmap> list) {
        pbProgress.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                bitmap = AsciiUtil.createAsciiPic(list.get(0), AsciiBitmapActivity.this);
                ivImage.post(new Runnable() {
                    @Override
                    public void run() {
                        ivImage.setImageBitmap(bitmap);
                        pbProgress.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }).start();

    }

}
