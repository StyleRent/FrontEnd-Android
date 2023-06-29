package info.jeonju.stylerent.auth.ChattingModels;

public class MyChatResponse {
    private Integer messageId;
    private String receiverImage;

    private String productName;

    private String productPrice;
    private Integer senderId;
    private Integer receiverId;
    private String receiverName;

    private Boolean myChat; // if true -> my chat false -> their chat
    private Boolean rentStatus;
    private Boolean isRentedToMe;

    private String productImage;
    private String lastMessage;
    private Integer productId;



    public MyChatResponse(Integer messageId, String receiverImage, String productName, String productPrice, Integer senderId, Boolean myChat, Boolean rentStatus, Boolean isRentedToMe, Integer receiverId, String receiverName, String lastMessage, String productImage, Integer productId) {
        this.messageId = messageId;
        this.receiverImage = receiverImage;
        this.productName = productName;
        this.productPrice = productPrice;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.lastMessage = lastMessage;
        this.productImage = productImage;
        this.myChat = myChat;
        this.rentStatus = rentStatus;
        this.isRentedToMe = isRentedToMe;
        this.productId = productId;
    }

    public Boolean getRentedToMe() {
        return isRentedToMe;
    }

    public void setRentedToMe(Boolean rentedToMe) {
        isRentedToMe = rentedToMe;
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

    public Boolean getMyChat() {
        return myChat;
    }

    public void setMyChat(Boolean myChat) {
        this.myChat = myChat;
    }

    public Boolean getRentStatus() {
        return rentStatus;
    }

    public void setRentStatus(Boolean rentStatus) {
        this.rentStatus = rentStatus;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public String getReceiverImage() {
        return receiverImage;
    }

    public void setReceiverImage(String receiverImage) {
        this.receiverImage = receiverImage;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

}
