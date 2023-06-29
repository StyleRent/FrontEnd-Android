package info.jeonju.stylerent.auth.ChattingModels;

public class MessageRequest {
    private String chat;
    private Integer messageId;
    private Integer receiverId;
    private Integer productId;

    public MessageRequest(String chat, Integer messageId, Integer receiverId, Integer productId) {
        this.chat = chat;
        this.messageId = messageId;
        this.receiverId = receiverId;
        this.productId = productId;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public Integer getMessageId() {
        return messageId;
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

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}
