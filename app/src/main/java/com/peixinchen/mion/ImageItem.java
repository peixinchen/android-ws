package com.peixinchen.mion;

import android.graphics.Bitmap;

/**
 * Created by peixi on 2017/11/17.
 */

class ImageItem {
    public boolean isChecked;
    public Bitmap image;

    public ImageItem(boolean isChecked, Bitmap bitmap) {
        this.isChecked = isChecked;
        this.image = bitmap;
    }
}
