package info.jeonju.stylerent.userdata;

import info.jeonju.stylerent.auth.ProductModels.ProductImage;

import java.util.List;

public class ProductDataResponse {
    private Integer productId;
    private String productName;
    private String productInfo;
    private String productPrice;

    private Boolean liked;
    private Boolean rentStatus;
    private Double rankAverage;
    private Integer numsReview;
    private List<ProductImage> imagePath;
    private String productImage;
    private Integer renterId;

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

    public List<ProductImage> getImagePath() {
        return imagePath;
    }

    public void setImagePath(List<ProductImage> imagePath) {
        this.imagePath = imagePath;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public Boolean getRentStatus() {
        return rentStatus;
    }

    public void setRentStatus(Boolean rentStatus) {
        this.rentStatus = rentStatus;
    }

    public Double getRankAverage() {
        return rankAverage;
    }

    public void setRankAverage(Double rankAverage) {
        this.rankAverage = rankAverage;
    }

    public ProductDataResponse(Integer productId, String productName, String productInfo, String productPrice, Boolean liked, Boolean rentStatus, Double rankAverage, Integer numsReview ,List<ProductImage> imagePath, String productImage, Integer renterId) {
        this.productId = productId;
        this.productName = productName;
        this.productInfo = productInfo;
        this.productPrice = productPrice;
        this.rentStatus = rentStatus;
        this.rankAverage = rankAverage;
        this.numsReview = numsReview;
        this.liked = liked;
        this.imagePath = imagePath;
        this.productImage = productImage;
        this.renterId = renterId;
    }

    public Integer getNumsReview() {
        return numsReview;
    }

    public void setNumsReview(Integer numsReview) {
        this.numsReview = numsReview;
    }

    public Integer getRenterId() {
        return renterId;
    }

    public void setRenterId(Integer renterId) {
        this.renterId = renterId;
    }
}
