package com.wtm.library.bitmap.factory;

import android.graphics.Bitmap;

/**
 * Created with IntelliJ IDEA.
 */
public interface BitmapFactory {

    BitmapFactory cloneNew();

    Bitmap createBitmap(Bitmap rawBitmap);
}
