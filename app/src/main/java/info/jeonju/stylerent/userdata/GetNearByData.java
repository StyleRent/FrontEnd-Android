package info.jeonju.stylerent.userdata;

import java.util.List;

public class GetNearByData {
    private String userName;
    private Integer userId;
    private String  profileImage;
    private String longtitude;
    private String latitude;
    private String distance;
    private List<ProductDataResponse> products;



    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }


    public void setUsername(String username) {
        this.userName = username;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }


    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public List<ProductDataResponse> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDataResponse> products) {
        this.products = products;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public GetNearByData(String userName, Integer userId, String profileImage, String longtitude, String latitude, String distance, List<ProductDataResponse> products) {
        this.userName = userName;
        this.userId = userId;
        this.profileImage = profileImage;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.distance = distance;
        this.products = products;
    }
}
