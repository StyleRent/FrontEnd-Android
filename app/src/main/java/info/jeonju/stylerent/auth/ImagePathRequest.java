package info.jeonju.stylerent.auth;

public class ImagePathRequest {
    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ImagePathRequest(String imagePath) {
        this.imagePath = imagePath;
    }
}
