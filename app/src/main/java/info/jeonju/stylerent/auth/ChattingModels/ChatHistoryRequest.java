package info.jeonju.stylerent.auth.ChattingModels;

public class ChatHistoryRequest {
    private Integer messageId;
    private Integer receiverId;
    private Integer productId;

    public ChatHistoryRequest(Integer messageId, Integer receiverId, Integer productId) {
        this.messageId = messageId;
        this.receiverId = receiverId;
        this.productId = productId;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }
}
