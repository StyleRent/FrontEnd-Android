package info.jeonju.stylerent.auth.ChattingModels;

public class SenderHistory {
    private Integer chatId;
    private Integer userId;
    private String message;

    public SenderHistory(Integer chatId, Integer userId, String message) {
        this.chatId = chatId;
        this.userId = userId;
        this.message = message;
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
