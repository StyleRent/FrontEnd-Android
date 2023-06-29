package info.jeonju.stylerent.userdata;

import java.util.List;

public class UserProfileResponse {

    private Integer userid;
    private String username;
    private String email;
    private String phonenumber;
    private Integer averageRank;
    private List<Rank> receivedRank;
    private List<Rank> marks;
    private CoordinateResponse coordinateResponse;
    private ImageResponse imageResponse;

    private List<ProductDataResponse> products;



    public Integer getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public Integer getAverageRank() {
        return averageRank;
    }

    public List<Rank> getReceivedRank() {
        return receivedRank;
    }

    public List<Rank> getMarks() {
        return marks;
    }

    public CoordinateResponse getCoordinateResponse() {
        return coordinateResponse;
    }

    public ImageResponse getImageResponse() {
        return imageResponse;
    }



    public UserProfileResponse(Integer userid, String username, String email, String phonenumber, Integer averageRank, List<Rank> receivedRank, List<Rank> marks, CoordinateResponse coordinateResponse, ImageResponse imageResponse, List<ProductDataResponse> products) {
        this.userid = userid;
        this.username = username;
        this.email = email;
        this.phonenumber = phonenumber;
        this.averageRank = averageRank;
        this.receivedRank = receivedRank;
        this.marks = marks;
        this.coordinateResponse = coordinateResponse;
        this.imageResponse = imageResponse;
        this.products = products;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setAverageRank(Integer averageRank) {
        this.averageRank = averageRank;
    }

    public void setReceivedRank(List<Rank> receivedRank) {
        this.receivedRank = receivedRank;
    }

    public void setMarks(List<Rank> marks) {
        this.marks = marks;
    }

    public void setCoordinateResponse(CoordinateResponse coordinateResponse) {
        this.coordinateResponse = coordinateResponse;
    }

    public void setImageResponse(ImageResponse imageResponse) {
        this.imageResponse = imageResponse;
    }

    public List<ProductDataResponse> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDataResponse> products) {
        this.products = products;
    }
}

