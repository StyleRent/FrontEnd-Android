package info.jeonju.stylerent.auth.Rank;

public class ReviewListModel {

    private String username;
    private String productImage;
    private Integer rank;
    private String reviewString;


    public ReviewListModel(String username, String productImage, Integer rank, String reviewString) {
        this.username = username;
        this.productImage = productImage;
        this.rank = rank;
        this.reviewString = reviewString;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getReviewString() {
        return reviewString;
    }

    public void setReviewString(String reviewText) {
        this.reviewString = reviewText;
    }
}
