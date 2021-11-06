package Model;

import java.util.Objects;

public class Intersection {
    private long id;
    private double latitude;
    private double longitude;

    public Intersection(long id, double latitude, double longitude){
        this.id=id;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public Intersection() {
    }

    public long getId(){
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if ((o == null) || !(o instanceof Intersection) ){
            return false;
        }
        Intersection that = (Intersection) o;
        return (id == that.id) && (Double.compare(that.latitude, latitude) == 0)
                    && (Double.compare(that.longitude, longitude) == 0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,latitude,longitude);
    }

    public static double calculDis(Intersection i1, Intersection i2){ //Calcul distance à vol d'oiseau
        double R = 6371e3; //Rayon de la terre
        double res = 0;

        double lat1= i1.latitude * Math.PI/180; //Conversion en rad
        double lat2= i2.latitude * Math.PI/180;
        double deltaLat = Math.abs((i2.latitude-i1.latitude) * Math.PI/180);
        double deltaLong = Math.abs((i2.longitude-i1.longitude) * Math.PI/180);

        double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) + Math.cos(lat1) * Math.cos(lat2) * Math.sin(deltaLong/2) * Math.sin(deltaLong/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); //Distance angulaire

        res = R * c; // in metres
        return res;
    }
}
