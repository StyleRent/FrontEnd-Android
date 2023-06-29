package info.jeonju.stylerent.auth.ChattingModels;

public class Message {
    private Integer chatId;
    private Integer userId;
    private String message;
    private String messageType;

    public Message(Integer chatId, Integer userId, String message, String messageType) {
        this.chatId = chatId;
        this.userId = userId;
        this.message = message;
        this.messageType = messageType;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
