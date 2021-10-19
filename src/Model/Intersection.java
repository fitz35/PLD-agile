package Model;

public class Intersection {
    private long id;
    private double latitude;
    private double longitude;

    public Intersection(long id, double latitude, double longitude)
    {
        this.id=id;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public long getId()
    {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean equals(Intersection intersectionToTest)
    {
        if( intersectionToTest.getId()==this.getId() &&
                intersectionToTest.getLatitude()==this.getLatitude() &&
                intersectionToTest.getLongitude()==this.getLongitude()){
            return true;
        }
        return false;
    }
}
