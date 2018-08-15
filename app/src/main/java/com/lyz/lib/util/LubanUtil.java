package com.lyz.lib.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lyz.lib.luban.Luban;
import com.lyz.lib.luban.OnCompressListener;

import java.io.File;
import java.util.List;




/**
 * Created by Tim on 2018/4/24.
 * @author liyongzhen
 * 图片压缩工具类
 */
public class LubanUtil {

    private static final String TAG = LubanUtil.class.getSimpleName();
    /**
     * 压缩到缓存目录
     */
    private static final String CACHE_FILE_NAME = "/kate/";
    /**
     * 压缩回调监听
     */
    private LubanImgListener mListener;

    public interface LubanImgListener {
        /**
         * 失败
         * @param e
         */
        void error(Throwable e);

        /**
         * 成功
         * @param list
         */
        void success(List<Bitmap> list);
    }

    private LubanUtil() {
        throw new IllegalAccessError("Utility class");
    }

    /**
     * 压缩图片返回文件File
     * @param context  上下文引用
     * @param imgUrls  本地图片地址
     * @param listener 监听
     */
    public static void loadLocalImg(Context context, List<String> imgUrls, @Nullable final LubanImgListener listener) {
        Luban.with(context)
                .load(imgUrls) // 图片路径地址
                .ignoreBy(100) // 限制压缩大小 100kb左右
                .setTargetDir(getDirectoryPath(CACHE_FILE_NAME)) // 缓存目录
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // not user here
                    }

                    @Override
                    public void onSuccess(Bitmap file) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        // 压缩失败
                        if (listener != null) {
                            listener.error(e);
                        }
                    }
                    @Override
                    public void onComplete(List<Bitmap> list) {
                        // 成功返回
                        if (listener != null) {
                            listener.success(list);
                        }
                    }

                }).launch();
    }

    /**
     * 获取指定缓存路径
     * @return 创建路径
     */
    private static String getDirectoryPath(String fileName) {
        String path = Environment.getExternalStorageDirectory() + fileName;
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }


    /**
     * 判断提交图片大小
     * @param imgs
     * @return
     */
    public static boolean isBigThan5Mb(List<String> imgs) {
        // 限制5mb  这里是5120kb
        long limitSize = 5120;
        for (String img : imgs) {
            File file = new File(img);
            long fileSize = (file.length() >> 10);
            Log.e("Luban", "compressImg : file size = " + (file.length() >> 10) + " kb" + " ; file name = " + file.getName());
            if (fileSize > limitSize) {
                return true;
            }
        }
        return false;
    }




}
