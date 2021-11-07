package Model;

import java.util.Objects;

public class Intersection {
    /**
     * id of an intersection
     */
    private long id;

    /**
     * latitude of an intersection
     */
    private double latitude;

    /**
     * longitude of an intersection
     */
    private double longitude;

    /**
     * Constructor
     * @param id
     * @param latitude
     * @param longitude
     */
    public Intersection(long id, double latitude, double longitude){
        this.id=id;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public Intersection() {
    }

    //----------- Getters -----------
    /**
     * @return id
     */
    public long getId(){
        return id;
    }

    /**
     * @return latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @return longitude
     */
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


    /**
     * Calculate the distance bewteen 2 intersections
     * @param i1 Intersection 1
     * @param i2 Intersection 2
     * @return res the distance between i1 and i2
     */
    public static double calculDis(Intersection i1, Intersection i2){
        double R = 6371e3; // earth radius
        double res = 0;

        double lat1= i1.latitude * Math.PI/180; //radian conversion
        double lat2= i2.latitude * Math.PI/180;
        double deltaLat = Math.abs((i2.latitude-i1.latitude) * Math.PI/180);
        double deltaLong = Math.abs((i2.longitude-i1.longitude) * Math.PI/180);

        double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) + Math.cos(lat1) * Math.cos(lat2)
                    * Math.sin(deltaLong/2) * Math.sin(deltaLong/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); // Angular distance

        res = R * c; // in meters
        return res;
    }
}
