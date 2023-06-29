package info.jeonju.stylerent.auth.FavoriteModels;

import android.graphics.Bitmap;

public class FavModel {
    private String productImage;
    private Integer productId;
    private String productName;
    private String productPrice;
    private Bitmap imageBitmap;

    public FavModel(String productImage, Integer productId, String productName, String productPrice, Bitmap imageBitmap) {
        this.productImage = productImage;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.imageBitmap = imageBitmap;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
}
