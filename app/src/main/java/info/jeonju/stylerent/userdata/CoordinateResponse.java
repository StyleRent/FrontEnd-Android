package info.jeonju.stylerent.userdata;

public class CoordinateResponse {
    private String namelocation;
    private String latitude;
    private String longitude;
    private String error;


    public String getNamelocation() {
        return namelocation;
    }

    public void setNamelocation(String namelocation) {
        this.namelocation = namelocation;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


    public CoordinateResponse(String namelocation, String latitude, String longitude, String error) {
        this.namelocation = namelocation;
        this.latitude = latitude;
        this.longitude = longitude;
        this.error = error;
    }

    // constructor, getters and setters
}
