package info.jeonju.stylerent.userdata;

import java.util.List;

public class OtherUserDataResponse {
    private Integer userId;
    private String username;
    private String profileImage;
    private Double rankAverage;
    private List<ProductDataResponse> products;


    public OtherUserDataResponse(Integer userId, String username, String profileImage,Double rankAverage,List<ProductDataResponse> products) {
        this.userId = userId;
        this.username = username;
        this.profileImage = profileImage;
        this.rankAverage = rankAverage;
        this.products = products;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getprofileImage() {
        return profileImage;
    }

    public void setprofileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Double getRankAverage(){
        return rankAverage;
    }

    public void setRankAverage(Double rankAverage){
        this.rankAverage = rankAverage;
    }

    public List<ProductDataResponse> getProducts(){
        return products;
    }

    public void setProducts(List<ProductDataResponse> products){
        this.products = products;
    }

}