package info.jeonju.stylerent.auth.ProductModels;

public class ProductInfoResponse {
    private Integer productId;
    private String message;
    private String error;

    public ProductInfoResponse(Integer productId, String message, String error) {
        this.productId = productId;
        this.message = message;
        this.error = error;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }
}
