package info.jeonju.stylerent.auth.ChattingModels;

import java.util.List;

public class ChatHistoryResponse {
    private Integer messageId;
    private Boolean rentStatus;
    private List<SenderHistory> sender;
    private List<ReceiverHistory> receiver;
    private String error;

    public Boolean getRentStatus() {
        return rentStatus;
    }

    public void setRentStatus(Boolean rentStatus) {
        this.rentStatus = rentStatus;
    }

    public ChatHistoryResponse(Integer messageId, Boolean rentStatus, List<SenderHistory> sender, List<ReceiverHistory> receiver, String error) {
        this.messageId = messageId;
        this.sender = sender;
        this.rentStatus = rentStatus;
        this.receiver = receiver;
        this.error = error;
    }



    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public List<SenderHistory> getSender() {
        return sender;
    }

    public void setSender(List<SenderHistory> sender) {
        this.sender = sender;
    }

    public List<ReceiverHistory> getReceiver() {
        return receiver;
    }

    public void setReceiver(List<ReceiverHistory> receiver) {
        this.receiver = receiver;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
