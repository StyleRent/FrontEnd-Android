package info.jeonju.stylerent.auth.ProductModels;

public class ProductImageResponse {
    private Integer productId;
    private String image_path;

    public ProductImageResponse(Integer productId, String image_path) {
        this.productId = productId;
        this.image_path = image_path;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
