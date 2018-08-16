package com.lyz.lib.util;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ListView;
import android.widget.ScrollView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.provider.Settings.System.DATE_FORMAT;

/**
 * Created by Tim on 2018/6/15.
 * 图片工具类
 */
public class PhotoUtil {

    public static final String CACHE_FILE_NAME = "/kate/photo/";

    public static Bitmap getBitFromPath(String path) {
        return BitmapFactory.decodeFile(path);
    }

    public static Bitmap getBitmapFromPath2(Context context, String filePath) {
        File file = new File(filePath);
        Uri uri = Uri.fromFile(file);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap getBitmapFromPath3(File file) {
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    public static File getFileFromBitmap(String filePath, Bitmap bitmap) {
        String timeStamp = System.currentTimeMillis() + "";
        File file = new File(filePath + timeStamp + ".jpg");//将要保存图片的路径
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * uri 转 File
     */
    public File uriTurnFile(Uri uri) {

        return new File(uri.getPath());
    }

    /**
     * File 转 Uri
     */
    public Uri fileTurnUri(File file) {
        return  Uri.fromFile(file);
    }

    public Uri FileTurnUri(String filePath){
        File f = new File(filePath);
        return Uri.fromFile(f);
    }


    public static Uri getMediaUriFromPath(Context context, String path) {
        Uri mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(mediaUri, null,
                MediaStore.Images.Media.DISPLAY_NAME + "= ?",
                new String[] {path.substring(path.lastIndexOf("/") + 1)},
                null);

        Uri uri = null;
        if (cursor != null && cursor.moveToFirst()) {
            uri = ContentUris.withAppendedId(mediaUri,
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
        }
        if (cursor != null) {
            cursor.close();
        }
        return uri;
    }

    /**
     * 4.4之前
     * uri 转 path
     */
    public String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * android 4.4 及以后获取 uri 真实路径
     */
    public String getRealFilePath(Context context, Uri uri) {
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 4.4以前
     * @param context
     * @param uri
     */
    public static String handleImageBeforeKitKat(Context context, Uri uri) {
        return getImagePath(context, uri, null);
    }

    /**
     * 4.4以后
     * @param context
     * @param uri
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String handleImageOnKitKat(Context context, Uri uri) {
        String imagePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            //如果是Document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);

            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(context, contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，使用普通方式处理
            imagePath = getImagePath(context, uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri,直接获取图片路径
            imagePath = uri.getPath();
        }
        return imagePath;
    }


    private static String getImagePath(Context context, Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 优化算法
     * 1.图片不需要铺满，只需要以统一合适的宽度。然后让ImageView自己去铺满，不然长图合成长图会崩溃，这里以第一张图为例
     * 2.只缩放不相等宽度的图片。已经缩放过的不需要再次缩放
     * @param bit1
     * @param bit2
     * @return
     */
    public static Bitmap newBitmap(Bitmap bit1, Bitmap bit2) {
        Bitmap newBit = null;
        int width = bit1.getWidth();
        if (bit2.getWidth() != width) {
            int h2 = bit2.getHeight() * width / bit2.getWidth();
            newBit = Bitmap.createBitmap(width, bit1.getHeight() + h2, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBit);
            Bitmap newSizeBitmap2 = getNewSizeBitmap(bit2, width, h2);
            canvas.drawBitmap(bit1, 0, 0, null);
            canvas.drawBitmap(newSizeBitmap2, 0, bit1.getHeight(), null);
        } else {
            newBit = Bitmap.createBitmap(width, bit1.getHeight() + bit2.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBit);
            canvas.drawBitmap(bit1, 0, 0, null);
            canvas.drawBitmap(bit2, 0, bit1.getHeight(), null);
        }
        return newBit;
    }


    /**
     *
     * @param bitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    private static Bitmap getNewSizeBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        float scaleWidth = ((float) newWidth) / bitmap.getWidth();
        float scaleHeight = ((float) newHeight) / bitmap.getHeight();
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
                true);
    }

    /**
     * 添加logo水印
     * @param src
     * @param logo
     * @return
     */
    public static Bitmap createWaterMaskImage(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }
        //原图宽高
        int w = src.getWidth();
        int h = src.getHeight();
        //logo宽高
        int ww = logo.getWidth();
        int wh = logo.getHeight();
        //创建一个和原图宽高一样的bitmap
        Bitmap newBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
        //创建
        Canvas canvas = new Canvas(newBitmap);
        //绘制原始图片
        canvas.drawBitmap(src, 0, 0, null);
        //新建矩阵
        Matrix matrix = new Matrix();
        //对矩阵作缩放处理
        matrix.postScale(0.1f, 0.1f);
        //对矩阵作位置偏移，移动到底部中间的位置
        matrix.postTranslate(0.5f * w - 0.05f * ww, h - 0.1f * wh - 3);
        //将logo绘制到画布上并做矩阵变换
        canvas.drawBitmap(logo, matrix, null);
        // 保存状态
        canvas.save(Canvas.ALL_SAVE_FLAG);// 保存
        // 恢复状态
        canvas.restore();
        return newBitmap;
    }

    /**
     * 添加文字水印
     * @param context
     * @param logo          要添加的文字
     * @param canvas
     * @param width
     * @param height
     */
    public static void drawTextToBitmap(Context context, String logo, Canvas canvas, int width, int height) {
        //新建画笔，默认style为实心
        Paint paint = new Paint();
        //设置颜色，颜色可用Color.parseColor("#6b99b9")代替
        paint.setColor(Color.parseColor("#6b99b9"));
        //设置透明度
        paint.setAlpha(80);
        //抗锯齿
        paint.setAntiAlias(true);
        //画笔粗细大小
        paint.setTextSize((float) DensityUtil.dip2px(context, 30));
        //保存当前画布状态
        canvas.save();
        //画布旋转-30度
        canvas.rotate(-30);
        //获取要添加文字的宽度
        float textWidth = paint.measureText(logo);
        int index = 0;
        //行循环，从高度为0开始，向下每隔80dp开始绘制文字
        for (int positionY = -DensityUtil.dip2px(context, 30); positionY <= height; positionY += DensityUtil.dip2px(context, 80)) {
            //设置每行文字开始绘制的位置,0.58是根据角度算出tan30°,后面的(index++ % 2) * textWidth是为了展示效果交错绘制
            float fromX = -0.58f * height + (index++ % 2) * textWidth;
            //列循环，从每行的开始位置开始，向右每隔2倍宽度的距离开始绘制（文字间距1倍宽度）
            for (float positionX = fromX; positionX < width; positionX += textWidth * 2) {
                //绘制文字
                canvas.drawText(logo, positionX, positionY, paint);
            }
        }
        //恢复画布状态
        canvas.restore();
    }

    /**
     * 截取scrollview的屏幕
     **/
    public static Bitmap getScrollViewBitmap(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
        }
        // 创建对应大小的bitmap

        bitmap = Bitmap.createBitmap(DensityUtil.getScreenWidth(scrollView.getContext()), h,
                Bitmap.Config.ARGB_4444);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.parseColor("#f2f7fa"));
        scrollView.draw(canvas);
        return bitmap;
    }

    /**
     * 截图listview
     **/
    public static Bitmap getListViewBitmap(ListView listView, String picpath) {
        int h = 0;
        Bitmap bitmap;
        // 获取listView实际高度
        for (int i = 0; i < listView.getChildCount(); i++) {
            h += listView.getChildAt(i).getHeight();
        }
        listView.getHeight();
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(listView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        listView.draw(canvas);
        // 测试输出
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(picpath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (null != out) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            Log.e("PhotoUtil", "getListViewBitmap e = " + e);
        }
        return bitmap;
    }
}
