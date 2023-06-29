package info.jeonju.stylerent.auth;

public class LoginResponse {

    private String token;
    private String error;

    public LoginResponse(String token, String error) {
        this.token = token;
        this.error = error;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
