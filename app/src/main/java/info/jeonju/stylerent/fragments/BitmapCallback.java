package info.jeonju.stylerent.fragments;

import android.graphics.Bitmap;

public interface BitmapCallback {
    void onBitmapLoaded(Bitmap bitmap);
    void onFailure(Throwable t);
}
