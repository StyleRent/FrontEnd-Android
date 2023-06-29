package info.jeonju.stylerent.auth.ChattingModels;

public class MessageInit {
    private Integer receiverId;
    private Integer productId;

    public MessageInit(Integer receiverId, Integer productId) {
        this.receiverId = receiverId;
        this.productId = productId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}
