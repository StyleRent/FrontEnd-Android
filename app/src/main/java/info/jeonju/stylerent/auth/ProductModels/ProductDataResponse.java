package info.jeonju.stylerent.auth.ProductModels;

import java.util.List;

public class ProductDataResponse {
    private Integer productId;
    private Integer userId;
    private String productName;
    private String productInfo;
    private String productPrice;
    private Boolean liked;
    private Boolean rentStatus;
    private Double rankAverage;
    private Integer renterId;
    private List<ProductImageBases> imagePath;
    private String productImage;

    public ProductDataResponse(Integer productId, Integer userId, String productName, String productInfo, String productPrice, Boolean liked, Boolean rentStatus, Double rankAverage, Integer renterId, List<ProductImageBases> imagePath, String productImage) {
        this.productId = productId;
        this.userId = userId;
        this.productName = productName;
        this.productInfo = productInfo;
        this.productPrice = productPrice;
        this.liked = liked;
        this.rentStatus = rentStatus;
        this.rankAverage = rankAverage;
        this.renterId = renterId;
        this.imagePath = imagePath;
        this.productImage = productImage;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Integer getRenterId() {
        return renterId;
    }

    public void setRenterId(Integer renterId) {
        this.renterId = renterId;
    }

    public List<ProductImageBases> getImagePath() {
        return imagePath;
    }

    public void setImagePath(List<ProductImageBases> imagePath) {
        this.imagePath = imagePath;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}
