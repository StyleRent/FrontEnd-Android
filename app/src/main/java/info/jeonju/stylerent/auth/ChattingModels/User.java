package info.jeonju.stylerent.auth.ChattingModels;

//User represents data model for each user int the userList that we are using in CUstomAdapter
public class User {
    private String userName;
    private int userImage;
    private String lastMessage;

    public User(String userName, int userImage, String lastMessage) {
        this.userName = userName;
        this.userImage = userImage;
        this.lastMessage = lastMessage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserImage() {
        return userImage;
    }

    public void setUserImage(int userImage) {
        this.userImage = userImage;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
