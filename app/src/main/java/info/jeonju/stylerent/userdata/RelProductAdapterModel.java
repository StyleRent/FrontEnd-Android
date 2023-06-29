package info.jeonju.stylerent.userdata;


import android.graphics.Bitmap;

import info.jeonju.stylerent.auth.ProductModels.ProductImage;

import java.util.List;

public class RelProductAdapterModel {
    private Integer productId;
    private String productName;
    private String productInfo;
    private String productPrice;

    private Boolean rentStatus;
    private List<ProductImage> productImages;
    private List<Bitmap> imageBitmaps;

    public RelProductAdapterModel(Integer productId, String productName, String productPrice, Boolean rentStatus) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.rentStatus = rentStatus;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }


    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public Boolean getRentStatus(){
        return rentStatus;
    }

    public void setRentStatus(Boolean rentStatus) {
        this.rentStatus = rentStatus;
    }

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }


    public List<Bitmap> getImageBitmaps() {
        return imageBitmaps;
    }

    public void setImageBitmaps(List<Bitmap> imageBitmaps) {
        this.imageBitmaps = imageBitmaps;
    }

}
