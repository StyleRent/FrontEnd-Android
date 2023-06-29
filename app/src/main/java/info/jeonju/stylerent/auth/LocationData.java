package info.jeonju.stylerent.auth;

public class LocationData {
    public LocationData(String namelocation, String longitude, String latitude) {
        this.namelocation = namelocation;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private String namelocation;
    private String latitude;
    private String longitude;

    public LocationData(double latitude, double longitude) {
    }

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


}
