package info.jeonju.stylerent.activities;

import android.net.Uri;

import java.io.File;

public class ProductImageView {
    Uri uri;
    File imagePath;

    public ProductImageView(Uri uri, File imagePath) {
        this.uri = uri;
        this.imagePath = imagePath;
    }

    public Uri getUri() {
        return uri;
    }

    public File getImagePath() {
        return imagePath;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public void setImagePath(File imagePath) {
        this.imagePath = imagePath;
    }
}
