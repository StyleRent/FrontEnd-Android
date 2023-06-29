package info.jeonju.stylerent.auth.Rank;

public class RankRequest {
    private Integer rank;
    private Integer receiverId;
    private Integer productId;

    private String evaluationText;

    public RankRequest(Integer rank, Integer receiverId, Integer productId, String evaluationText) {
        this.rank = rank;
        this.receiverId = receiverId;
        this.productId = productId;
        this.evaluationText = evaluationText;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
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

    public String getEvaluationText() {
        return evaluationText;
    }

    public void setEvaluationText(String evaluationText) {
        this.evaluationText = evaluationText;
    }
}
