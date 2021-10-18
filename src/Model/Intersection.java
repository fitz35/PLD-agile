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

    public static double calculDis(Intersection i1, Intersection i2){ //Calcul distance à vol d'oiseau

        double R = 6371e3; //Rayon de la terre
        double res = 0;

        double lat1= i1.latitude * Math.PI/180; //Conversion en rad
        double lat2= i2.latitude * Math.PI/180;
        double deltaLat = (i2.latitude-i1.latitude) * Math.PI/180;
        double deltaLong = (i2.longitude-i1.longitude) * Math.PI/180;

        double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) + Math.cos(lat1) * Math.cos(lat2) * Math.sin(deltaLong/2) * Math.sin(deltaLong/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); //Distance angulaire

        res = R * c; // in metres
        return res;
    }

}
