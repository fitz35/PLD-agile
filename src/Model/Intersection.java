package Model;

public class Intersection {
    private long id;
    private double latitude;
    private double longitude;

    /**
     * Constructor
     *
     * @param id
     * @param latitude
     * @param longitude
     */
    public Intersection(long id, double latitude, double longitude)
    {
        this.id=id;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    /**
     * @return id
     */
    public long getId(){return id;}

    /**
     * @return latitude
     */
    public double getLatitude(){return latitude;}

    /**
     * @return longitude
     */
    public double getLongitude(){return longitude;}

    /**
     * This method return true if the intersection in parameter is equivalent
     *
     * @param intersectionToTest
     * @return
     */
    public boolean equals(Intersection intersectionToTest)
    {
        if( intersectionToTest.getId()==this.getId() &&
            intersectionToTest.getLatitude()==this.getLatitude() &&
            intersectionToTest.getLongitude()==this.getLongitude())
        {
            return true;
        }
        return false;
    }
}
