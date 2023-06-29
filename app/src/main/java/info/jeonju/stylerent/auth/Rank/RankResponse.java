package info.jeonju.stylerent.auth.Rank;

public class RankResponse {
    private Integer id;
    private Integer rank;
    private Integer userId;
    private Integer receiverId;
    private String error;

    public RankResponse(Integer id, Integer rank, Integer userId, Integer receiverId, String error) {
        this.id = id;
        this.rank = rank;
        this.userId = userId;
        this.receiverId = receiverId;
        this.error = error;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
