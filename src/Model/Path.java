package Model;

import java.util.LinkedList;
import java.util.Objects;

public class Path {
    private Address departure;
    private Address arrival;
    private LinkedList<Segment> segmentsOfPath;

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

    public Address getDeparture() {
        return departure;
    }

    public Address getArrival() {
        return arrival;
    }

    public LinkedList<Segment> getSegmentsOfPath() {
        return segmentsOfPath;
    }

    public void setDeparture(Address departure) {
        this.departure = departure;
    }

    public void setArrival(Address arrival) {
        this.arrival = arrival;
    }

    public void setSegmentsOfPath(LinkedList<Segment> segmentsOfPath) {
        this.segmentsOfPath = segmentsOfPath;
    }
}
