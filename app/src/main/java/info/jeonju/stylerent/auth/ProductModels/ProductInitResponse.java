package info.jeonju.stylerent.auth.ProductModels;

public class ProductInitResponse {
    private Integer currentProductId;
    private String message;
    private String error;

    public ProductInitResponse(Integer currentProductId, String message, String error) {
        this.currentProductId = currentProductId;
        this.message = message;
        this.error = error;
    }

    public Integer getCurrentProductId() {
        return currentProductId;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public void setCurrentProductId(Integer currentProductId) {
        this.currentProductId = currentProductId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setError(String error) {
        this.error = error;
    }
}
