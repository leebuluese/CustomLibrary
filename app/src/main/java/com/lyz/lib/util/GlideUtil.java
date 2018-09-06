package com.lyz.lib.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.lyz.lib.R;
import com.lyz.lib.glide.GlideApp;


/**
 * Created by lyz on 2018/7/11.
 *
 */

public class GlideUtil {

    private static RequestOptions requestOptions = new RequestOptions()
            .placeholder(R.drawable.ic_launcher_background) //加载中图片
            .error(R.drawable.ic_launcher_background) //加载失败图片
            .fallback(R.drawable.ic_launcher_background) //url为空图片
//            .centerCrop() // 填充方式
//            .override(600,600) //尺寸
//            .transform(new CircleCrop()) //圆角
            .priority(Priority.HIGH) //优先级
            .diskCacheStrategy(DiskCacheStrategy.NONE); //缓存策略，后面详细介绍

    public static void loadImg(@NonNull Context context, ImageView view, String url){
//        GlideApp.with(context).load(url).error(R.drawable.ic_launcher_background).placeholder(R.drawable.ic_launcher_background).into(view);
        Glide.with(context).load(url).apply(requestOptions).into(view);
    }

    public static void loadAllImg(@NonNull Context context, ImageView view, String url){
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.ic_launcher_background).placeholder(R.drawable.ic_launcher_background).into(view);
    }
}
