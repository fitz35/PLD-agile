package Model;

import java.util.LinkedList;
import java.util.Objects;

/**
 * This class is Path
 * @version
 * @author Hexanome 4124
 */
public class Path {
    /**
     * Address of path's departure
     */
    private Address departure;

    /**
     * Address of path's arrival
     */
    private Address arrival;

    /**
     * List of segments which constitue the path
     */
    private LinkedList<Segment> segmentsOfPath;


    /**
     * Constructor
     * @param departure
     * @param arrival
     * @param segmentsOfPath
     */
    public Path(Address departure, Address arrival, LinkedList<Segment> segmentsOfPath) {
        this.departure = departure;
        this.arrival = arrival;
        this.segmentsOfPath = segmentsOfPath;
    }

    public Path() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path p = (Path) o;
        return Objects.equals(departure, p.departure) && Objects.equals(arrival, p.arrival)
                /*&& Objects.equals(segmentsOfPath, p.segmentsOfPath)*/;
    }

    // -------- Getters --------
    /**
     * @return departure
     */
    public Address getDeparture() {
        return departure;
    }

    /**
     * @return arrival
     */
    public Address getArrival() {
        return arrival;
    }

    /**
     * @return segmentsOfPath
     */
    public LinkedList<Segment> getSegmentsOfPath() {
        return segmentsOfPath;
    }

    // -------- Setters --------
    /**
     * Set the departure address
     * @param departure
     */
    public void setDeparture(Address departure) {
        this.departure = departure;
    }

    /**
     * Set the arrival address
     * @param arrival
     */
    public void setArrival(Address arrival) {
        this.arrival = arrival;
    }

    /**
     * Set the list of segments
     * @param segmentsOfPath
     */
    public void setSegmentsOfPath(LinkedList<Segment> segmentsOfPath) {
        this.segmentsOfPath = segmentsOfPath;
    }

    /**
     * get the distance of the path
     * @return the distance of the path
     */
    public double getDistance() {
        double total = 0;
        for(Segment s : this.segmentsOfPath){
            total += s.getLength();
        }
        return total;
    }
}
