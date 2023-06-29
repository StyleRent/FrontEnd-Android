package info.jeonju.stylerent.userdata;

public class ItemProductImageResponse {
    private String path;
    private String message;
    private String error;

    public ItemProductImageResponse(String path, String message, String error) {
        this.path = path;
        this.message = message;
        this.error = error;
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
}
