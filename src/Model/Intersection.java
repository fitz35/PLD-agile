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
}
