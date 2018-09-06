package com.lyz.lib.bitmap.printbitmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.lyz.lib.R;
import com.lyz.lib.bitmap.asciibitmap.AsciiUtil;
import com.lyz.lib.photo.BrowsePhotoActivity;
import com.lyz.lib.util.DensityUtil;
import com.lyz.lib.util.FileUtil;
import com.lyz.lib.util.LubanUtil;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@SuppressLint("Registered")
public class PrintBitmapActivity extends AppCompatActivity {

    private EditText etText;
    private Button btnPicker;
    private ImageView ivLogo;
    private final int REQUEST_CODE = 26;
    private Button btnLook;
    private List<String> photos = new ArrayList<>();
//    private DragView mDragView;

    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_bitmap);

        // request permission
        ActivityCompat.requestPermissions(
                PrintBitmapActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA},
                REQUEST_CODE);

        initView();

        initData();

//        initDrag();
    }

//    private void initDrag() {
//        mDragView = (DragView)findViewById(R.id.dragview);
//        MyTextView tv = new MyTextView(PrintBitmapActivity.this);
//        tv.setText("向日葵美术");
//        tv.setGravity(Gravity.CENTER);
//        tv.setTextColor(0xffffffff);
//        tv.setBackgroundColor(0xffff2341);
//        mDragView.addDragView(tv, 0,400,380,760, false,false);
//    }

    private void initView() {
        etText = findViewById(R.id.et_text);
        btnPicker = findViewById(R.id.btn_picker);
        btnLook = findViewById(R.id.btn_look);
        ivLogo = findViewById(R.id.iv_logo);
    }

    private void initData() {
        btnPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etText.getText())) {
                    Toast.makeText(PrintBitmapActivity.this, "请输入水印文字", Toast.LENGTH_SHORT).show();
                    return;
                }
//                PhotoPicker.builder()
//                        .setPhotoCount(9)
//                        .setShowCamera(true)
//                        .setShowGif(true)
//                        .setPreviewEnabled(false)
//                        .start(PrintBitmapActivity.this, PhotoPicker.REQUEST_CODE);

                AsciiUtil.choosePhoto(PrintBitmapActivity.this, 9, PictureConfig.CHOOSE_REQUEST);
            }
        });

        btnLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photos.isEmpty()) {
                    Toast.makeText(PrintBitmapActivity.this, "数据为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(PrintBitmapActivity.this, BrowsePhotoActivity.class);
                intent.putExtra(BrowsePhotoActivity.KEY_LIST, (Serializable) photos);
                intent.putExtra(BrowsePhotoActivity.KEY_DELETE, false);
                intent.putExtra(BrowsePhotoActivity.KEY_INDEX, 0);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
//            if (null != data) {
//                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
//                compressPhoto(photos);
//            }
//
//        }


        if (resultCode == RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {

            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            List<String> paths = new ArrayList<>();
            if (selectList != null && selectList.size() > 0) {
                for (int i = 0; i < selectList.size(); i++) {
                    String path = "";
                    LocalMedia localMedia = selectList.get(0);
                    if (localMedia.isCompressed()) {
                        path = localMedia.getCompressPath();
                    } else if (localMedia.isCut()) {
                        path = localMedia.getCutPath();
                    } else {
                        path = localMedia.getPath();
                    }
                    paths.add(path);
                }
            }
            compressPhoto(paths);
        }
    }

    private void compressPhoto(List<String> photos) {

        LubanUtil.loadLocalImg(this, photos, new LubanUtil.LubanImgListener() {
            @Override
            public void error(Throwable e) {
                Toast.makeText(PrintBitmapActivity.this, "Compress Error!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void success(List<Bitmap> list) {
                addPrintText(list);
            }
        });
    }

    private void addPrintText(List<Bitmap> list) {
        photos.clear();
        List<Bitmap> photos2 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Bitmap bitmap = list.get(i);

            Date currentTime = new Date();
            SimpleDateFormat dateInstance = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
            dateInstance.applyPattern("yyyy-MM-dd HH:mm");
            String dateString = dateInstance.format(currentTime);
            String replace = dateString.replace("-", "/");


            Bitmap bt = drawTextToRightBottom(
                    this, bitmap, DensityUtil.getScreenWidth(this), etText.getText().toString(), 20f,
                    0xffffffff, 10, 10);
            photos2.add(bt);
            long time = System.currentTimeMillis();
            String name = "tim_" + time;
            String photo = FileUtil.savaBitmap2SDcard(this, bt, name);

//            String photo = addWatermarkBitmap(bitmap, etText.getText().toString());
            photos.add(photo);
        }
        ivLogo.setImageBitmap(photos2.get(0));
//        GlideUtil.loadImg(this, ivLogo, photos.get(0));
    }

    private Bitmap drawTextToRightBottom(Context context, Bitmap bitmap, int screenWidth, String text,
                                         float size, int color, float paddingRight, float paddingBottom) {
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                size, context.getResources().getDisplayMetrics());
        Log.e("print", "sp2px = " + size);

        float scaleSize = (size * width) / screenWidth;
        Log.e("print", "width = " + width + " ; screenWidth = " + screenWidth + " ; scale = " + scaleSize);

        Rect bounds = new Rect();

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(scaleSize);// dp2px(context, scaleSize)
        // 同时填充和描边
//        paint.setStyle(Paint.Style.FILL_AND_STROKE);
//        paint.setStrokeWidth(3);
//        paint.setShadowLayer(1f, 3f, 3f, Color.WHITE);
//        paint.setTextSize(dp2px(context, bitmap.getWidth()/20));
        paint.setDither(true); // 获取跟清晰的图像采样
        paint.setFilterBitmap(true);// 过滤一些
        paint.setAntiAlias(true);  //抗锯齿
        paint.getTextBounds(text, 0, text.length(), bounds);


        Paint stockPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        stockPaint.setColor(Color.BLACK);
        stockPaint.setTextSize(scaleSize);
        // 同时填充和描边
        stockPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        stockPaint.setStrokeWidth(2);
//        paint.setShadowLayer(1f, 3f, 3f, Color.WHITE);
//        paint.setTextSize(dp2px(context, bitmap.getWidth()/20));
        stockPaint.setDither(true); // 获取跟清晰的图像采样
        stockPaint.setFilterBitmap(true);// 过滤一些
        stockPaint.setAntiAlias(true);  //抗锯齿
        stockPaint.getTextBounds(text, 0, text.length(), bounds);


        float paddingLeft = width - bounds.width() - dp2px(context, paddingRight);
        float paddingTop = height - dp2px(context, paddingBottom);

        Bitmap.Config bitmapConfig = bitmap.getConfig();

        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawText(text, paddingLeft, paddingTop, stockPaint);
        canvas.drawText(text, paddingLeft, paddingTop, paint);
        return bitmap;
    }

    public int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
