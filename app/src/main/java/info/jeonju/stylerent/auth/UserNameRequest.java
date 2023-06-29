package info.jeonju.stylerent.auth;

public class UserNameRequest {
    private String username;

    public UserNameRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
