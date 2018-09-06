package com.lyz.lib;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import com.lyz.lib.bitmap.BitmapActivity;
import com.lyz.lib.bitmap.asciibitmap.AsciiBitmapActivity;
import com.lyz.lib.bitmap.longbitmap.LongBitmapActivity;
import com.lyz.lib.bitmap.printbitmap.PrintBitmapActivity;
import com.lyz.lib.view.ViewActivity;

/**
 * Created by lyz on 2018/8/14.
 *
 */

public class EventPath {


    private Context context;
    private int position;
    private String name;
    private final String[] list;
    private final String[] bitmap;

    public EventPath(Context context, int position, String name) {
        this.context = context;
        this.position = position;
        this.name = name;

        Resources resources = context.getResources();

        list = resources.getStringArray(R.array.main_list);
        bitmap = resources.getStringArray(R.array.list_bitmap);
    }

    public void startEvent() {

        Intent intent = new Intent();

        if (name.equals(list[position])) {
            switch (position) {
                case 0:
                    intent.setClass(context, BitmapActivity.class);
                    break;
                case 1:

                    break;
                case 2:
                    intent.setClass(context, ViewActivity.class);
                    break;
                default:
                    break;
            }
        } else if (name.equals(bitmap[position])) {
            switch (position) {
                case 0:
                    intent.setClass(context, PrintBitmapActivity.class);
                    break;
                case 1:
                    intent.setClass(context, LongBitmapActivity.class);
                    break;
                case 2:
                    intent.setClass(context, AsciiBitmapActivity.class);
                    break;
                default:
                    break;
            }
        }
        if (null != intent.getComponent()) {
            context.startActivity(intent);
        }
    }
}
