package info.jeonju.stylerent.auth.ChattingModels;

public class RentRequest {
    private Integer renterId;
    private Integer productId;


    public RentRequest(Integer renterId, Integer productId) {
        this.renterId = renterId;
        this.productId = productId;
    }

    public Integer getRenterId() {
        return renterId;
    }

    public void setRenterId(Integer renterId) {
        this.renterId = renterId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}
