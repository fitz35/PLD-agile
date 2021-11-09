package Model;


import java.util.ArrayList;

/**
 * An address is either a pickup intersection, a delivery intersection or a depot intersection
 * It has a type and a waiting time AKA addressDuration
 */
public class Address extends Intersection{
    /**
     * Time spent at the address
     */
    private int addressDuration;
    /**
     * The type of the adress, it can be 0 if it is a depot, 1 if it is a pickup or 2 if it is a delivery
     */
    private int type;

    /**
     * return the type of the address
     * @return The type of the adress, it can be 0 if it is a depot, 1 if it is a pickup or 2 if it is a delivery
     */
    public int getType(){
        return this.type;
    }

    public Address(long id, double latitude, double longitude, int addressDuration, int type) {
        super(id, latitude, longitude);
        this.addressDuration = addressDuration;
        this.type = type;
    }

    public Address(Intersection intersection, int addressDuration,int type) {
        super(intersection.getId(), intersection.getLatitude(), intersection.getLongitude());
        this.addressDuration = addressDuration;
        this.type=type;
    }

    public Address(){
    }

    /**
     * compute the name of the segments from an intersection
     * @param intersection the intersection
     * @param segmentsList the list of all segments
     * @return the list of name
     */
    public static ArrayList<String> getStreetNames(Intersection intersection, ArrayList<Segment> segmentsList ) {
        ArrayList<String> streetNames = new ArrayList<>();
        for (int i = 0; i < segmentsList.size(); i++) {
            if (intersection.equals(segmentsList.get(i).getOrigin()) ||
                    intersection.equals(segmentsList.get(i).getDestination())) {
                streetNames.add(segmentsList.get(i).getName());
            }
        }
        ArrayList<String> newList = new ArrayList<>();
        for (String element : streetNames) {
            if (!newList.contains(element)) {
                newList.add(element);
            }

        }
        return newList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if ((o == null) || !(o instanceof Intersection)){
            return false;
        }
        if(o instanceof Address){
            if (!(super.equals(o)) || (this.type!=((Address) o).type)){
                return false;
            }
        }else {
            if (!super.equals(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public int getAddressDuration() {
        return addressDuration;
    }

    public void setAddressDuration(int addressDuration) {
        this.addressDuration = addressDuration;
    }
}
