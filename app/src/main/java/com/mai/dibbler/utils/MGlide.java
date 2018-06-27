package com.mai.dibbler.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

public class MGlide {

    public static void initImageView(ImageView imageView) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    public static void load(Context ctx, String url, ImageView iv) {
        initImageView(iv);
        Glide.with(ctx).load(url).into(iv);

    }
}
