package info.jeonju.stylerent.userdata;

public class Rank {
    public Integer id;
    public Integer userid;
    public Integer recieverid;
    public String error;

    public Rank(Integer id, Integer userid, Integer recieverid, String error) {
        this.id = id;
        this.userid = userid;
        this.recieverid = recieverid;
        this.error = error;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserid() {
        return userid;
    }

    public Integer getRecieverid() {
        return recieverid;
    }

    public String getError() {
        return error;
    }


}
