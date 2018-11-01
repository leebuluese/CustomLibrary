package com.lyz.lib.bitmap.asciibitmap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.os.Environment;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lyz on 2018/9/6.
 * 图片转ASCII码表的图片表现形式
 */

public class AsciiUtil {

    private static final String TAG = "AsciiUtil";

    public static void choosePhoto(Activity context, int number, int requestCode) {
        PictureSelector.create(context)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                .theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(number)// 最大图片选择数量 int
//                .minSelectNum()// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
//                .previewImage()// 是否可预览图片 true or false
//                .previewVideo()// 是否可预览视频 true or false
//                .enablePreviewAudio() // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
//                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(false)// 是否裁剪 true or false
                .compress(false)// 是否压缩 true or false
//                .glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                .hideBottomControls()// 是否显示uCrop工具栏，默认不显示 true or false
//                .isGif()// 是否显示gif图片 true or false
                .compressSavePath(context.getFilesDir().getAbsolutePath())//压缩图片保存地址
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(true)// 是否圆形裁剪 true or false
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
//                .selectionMedia()// 是否传入已选图片 List<LocalMedia> list
//                .previewEggs()// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .cropCompressQuality(50)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(50)// 小于100kb的图片不压缩
//                .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
//                .rotateEnabled() // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
//                .videoQuality()// 视频录制质量 0 or 1 int
//                .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
//                .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
//                .recordVideoSecond()//视频秒数录制 默认60s int
                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .forResult(requestCode);//结果回调onActivityResult code
    }


    public interface CallBackListener {
        void onProgress(int progress);
    }

    public static Bitmap createAsciiPic(Bitmap image, Context context, CallBackListener listener) {
        // 字符串由复杂到简单
        final String base = "BXOHLTIiv;:,.";
        SpannableStringBuilder text = new SpannableStringBuilder();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(dm);
        }
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        int width0 = image.getWidth();
        int height0 = image.getHeight();
        int width1, height1;
        int scale = 7;
        if (width0 <= width / scale) {
            width1 = width0;
            height1 = height0;
        } else {
            width1 = width / scale;
            height1 = width1 * height0 / width0;
        }
        //读取图片
        image = Bitmap.createScaledBitmap(image, width1, height1, true);
        float imageHeight = image.getHeight();
        int imageWidth = image.getWidth();
        //输出到指定文件中
        for (int y = 0; y < imageHeight; y += 2) {
            for (int x = 0; x < imageWidth; x++) {
                final int pixel = image.getPixel(x, y);
                final int r = (pixel & 0xff0000) >> 16, g = (pixel & 0xff00) >> 8, b = pixel & 0xff;
                final float gray = 0.299f * r + 0.578f * g + 0.114f * b;
//                final float gray = 0.2126f * r + 0.7152f * g + 0.0722f * b;
                final int index = Math.round(gray * (base.length() + 1) / 255);
                String s = index >= base.length() ? " " : String.valueOf(base.charAt(index));
//                text.append(s);
                text.append(getSpannable(s, pixel));

//                Log.d(TAG, "x = " + x + " ; string = " + s);
            }
//            Log.d(TAG, "y = " + y + " ; width = " + image.getWidth() + " ; height = " + image.getHeight());
            int progress = (int) ((y/imageHeight) * 100);
            Log.d(TAG, "完成进度 ：" + progress + "%");
            if (null != listener) {
                listener.onProgress(progress);
            }
            text.append("\n");
        }
        return textAsBitmap(text, context);
    }

    public static SpannableString getSpannable(String s, int color) {
        SpannableString spannableString = new SpannableString(s);
        //设置颜色
        spannableString.setSpan(new ForegroundColorSpan(color), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置字体大小，true表示前面的字体大小20单位为dip
//        spannableString.setSpan(new AbsoluteSizeSpan(20, true), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置链接
//        spannableString.setSpan(new URLSpan("www.baidu.com"), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置字体，BOLD为粗体
//        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static Bitmap textAsBitmap(SpannableStringBuilder text, Context context) {

        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);
        textPaint.setTypeface(Typeface.MONOSPACE);
        textPaint.setTextSize(12);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(dm);
        }
        int width = dm.widthPixels;

        StaticLayout layout = new StaticLayout(text, textPaint, width,

                Layout.Alignment.ALIGN_CENTER, 1f, 0.0f, true);

        Bitmap bitmap = Bitmap.createBitmap(layout.getWidth() + 20,

                layout.getHeight() + 20, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.translate(10, 10);
        canvas.drawColor(Color.WHITE);
        layout.draw(canvas);

        Log.d(TAG, String.format("1:%d %d", layout.getWidth(), layout.getHeight()));

        return bitmap;

    }

    public static Bitmap scale(String src, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(BitmapFactory.decodeFile(src), newWidth, newHeight, true);
    }











    /**
     * 存放拍摄图片的文件夹
     */
    private static final String FILES_NAME = "/MyPhoto";
    /**
     * 获取的时间格式
     */
    public static final String TIME_STYLE = "yyyyMMddHHmmss";
    /**
     * 图片种类
     */
    public static final String IMAGE_TYPE = ".png";


    /**
     * 获取手机可存储路径
     *
     * @param context 上下文
     * @return 手机可存储路径
     */
    private static String getPhoneRootPath(Context context) {
        // 是否有SD卡
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                || !Environment.isExternalStorageRemovable()) {
            // 获取SD卡根目录
            if (null != context.getExternalCacheDir()) {
                return context.getExternalCacheDir().getPath();
            } else {
                return null;
            }
        } else {
            // 获取apk包下的缓存路径
            return context.getCacheDir().getPath();
        }
    }


    /**
     * 使用当前系统时间作为上传图片的名称
     *
     * @return 存储的根路径+图片名称
     */
    public static String getPhotoFileName(Context context) {
        File file = new File(getPhoneRootPath(context) + FILES_NAME);
        // 判断文件是否已经存在，不存在则创建
        if (!file.exists()) {
            file.mkdirs();
        }
        // 设置图片文件名称
        SimpleDateFormat format = new SimpleDateFormat(TIME_STYLE, Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        String time = format.format(date);
        String photoName = "/" + time + IMAGE_TYPE;
        return file + photoName;
    }

    /**
     * 保存Bitmap图片在SD卡中
     * 如果没有SD卡则存在手机中
     *
     * @param mbitmap 需要保存的Bitmap图片
     * @return 保存成功时返回图片的路径，失败时返回null
     */
    public static String savePhotoToSD(Bitmap mbitmap, Context context) {
        FileOutputStream outStream = null;
        String fileName = getPhotoFileName(context);
        try {
            outStream = new FileOutputStream(fileName);
            // 把数据写入文件，100表示不压缩
            mbitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (outStream != null) {
                    // 记得要关闭流！
                    outStream.close();
                }
                if (mbitmap != null) {
                    mbitmap.recycle();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 把原图按1/10的比例压缩
     *
     * @param path 原图的路径
     * @return 压缩后的图片
     */
    public static Bitmap getCompressPhoto(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 10; // 图片的大小设置为原来的十分之一
        Bitmap bmp = BitmapFactory.decodeFile(path, options);
        options = null;
        return bmp;
    }

    /**
     * 处理旋转后的图片
     *
     * @param originpath 原图路径
     * @param context    上下文
     * @return 返回修复完毕后的图片路径
     */
    public static String amendRotatePhoto(String originpath, Context context) {

        // 取得图片旋转角度
        int angle = readPictureDegree(originpath);

        // 把原图压缩后得到Bitmap对象
        if (angle != 0) {
            Bitmap bmp = getCompressPhoto(originpath);
            Bitmap bitmap = rotaingImageView(angle, bmp);
            return savePhotoToSD(bitmap, context);
        } else {
            return originpath;
        }

    }

    /**
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param angle  被旋转角度
     * @param bitmap 图片对象
     * @return 旋转后的图片
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }
}
