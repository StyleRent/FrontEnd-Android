package info.jeonju.stylerent.auth.ProductModels;

public class ProductImage {
    private Integer imageId;
    private String path;
    private String message;
    private String error;

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ProductImage(Integer imageId, String path, String message, String error) {
        this.imageId = imageId;
        this.path = path;
        this.message = message;
        this.error = error;
    }
}
