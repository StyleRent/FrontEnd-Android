package info.jeonju.stylerent.auth.ProductModels;

import android.graphics.Bitmap;

import java.util.List;

public class MyProductAdapterModel {
    private Integer productId;
    private String productName;
    private String productInfo;
    private String productPrice;

    private Boolean rentStatus;
    private List<Bitmap> imageBitmaps;
    private String productImage;

    private Integer renterId;
    private String TOKEN;

    public Integer getRenterId() {
        return renterId;
    }

    public void setRenterId(Integer receiverId) {
        this.renterId = receiverId;
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    public MyProductAdapterModel(Integer productId, String productName, String productPrice, Boolean rentStatus, String productImage, Integer renterId, String TOKEN) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.rentStatus = rentStatus;
        this.productImage = productImage;
        this.renterId = renterId;
        this.TOKEN = TOKEN;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Boolean getRentStatus(){
        return rentStatus;
    }

    public void setRentStatus(Boolean rentStatus) {
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


    public List<Bitmap> getImageBitmaps() {
        return imageBitmaps;
    }

    public void setImageBitmaps(List<Bitmap> imageBitmaps) {
        this.imageBitmaps = imageBitmaps;
    }
}

