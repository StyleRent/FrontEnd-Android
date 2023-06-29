package info.jeonju.stylerent.auth;

public class RegisterResponse {
    private String token;
    private String error;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setError(String error){
        this.error = error;
    }

    public String getError(){
        return error;
    }

    public RegisterResponse(String token, String error) {
        this.token = token;
        this.error = error;
    }
}

