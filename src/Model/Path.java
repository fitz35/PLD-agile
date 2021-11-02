package Model;

import java.util.LinkedList;

public class Path {
    private Intersection departure;
    private Intersection arrival;
    private LinkedList<Segment> segmentsOfPath;

    public Path(Intersection departure, Intersection arrival, LinkedList<Segment> segmentsOfPath) {
        this.departure = departure;
        this.arrival = arrival;
        this.segmentsOfPath = segmentsOfPath;
    }

    public Path() {
    }

    public Intersection getDeparture() {
        return departure;
    }

    public Intersection getArrival() {
        return arrival;
    }

    public LinkedList<Segment> getSegmentsOfPath() {
        return segmentsOfPath;
    }

    public void setDeparture(Intersection departure) {
        this.departure = departure;
    }

    public void setArrival(Intersection arrival) {
        this.arrival = arrival;
    }

    public void setSegmentsOfPath(LinkedList<Segment> segmentsOfPath) {
        this.segmentsOfPath = segmentsOfPath;
    }
}
