package info.jeonju.stylerent.auth.ChattingModels;

public class MessageInitResponse {
    private Integer messageId;
    private String message;
    private Integer productId;
    private String productName;
    private String productPrice;
    private Integer userId;
    private Integer receiverId;
    private Boolean rentStatus;
    private Boolean myChat;
    private Boolean isRentedToMe;
    private String productImage;
    private String error;

    public MessageInitResponse(Integer messageId, String message, Integer productId, String productName, String productPrice, Integer userId, Integer receiverId, Boolean rentStatus, Boolean myChat, Boolean isRentedToMe, String productImage, String error) {
        this.messageId = messageId;
        this.message = message;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.userId = userId;
        this.receiverId = receiverId;
        this.rentStatus = rentStatus;
        this.myChat = myChat;
        this.isRentedToMe = isRentedToMe;
        this.productImage = productImage;
        this.error = error;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public Boolean getRentStatus() {
        return rentStatus;
    }

    public void setRentStatus(Boolean rentStatus) {
        this.rentStatus = rentStatus;
    }

    public Boolean getMyChat() {
        return myChat;
    }

    public void setMyChat(Boolean myChat) {
        this.myChat = myChat;
    }

    public Boolean getRentedToMe() {
        return isRentedToMe;
    }

    public void setRentedToMe(Boolean rentedToMe) {
        isRentedToMe = rentedToMe;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
