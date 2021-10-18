package Model;

import java.util.ArrayList;

public class Tour{
    private ArrayList<Segment> orderedSegmentList;
    private ArrayList<Intersection> orderedIntersectionList;

    public Tour() {
        orderedSegmentList = new ArrayList<Segment>();
        orderedIntersectionList = new ArrayList<Intersection>();
    }

    public ArrayList<Segment> getOrderedSegmentList() {
        return orderedSegmentList;
    }

    public ArrayList<Intersection> getOrderedIntersectionList() {
        return orderedIntersectionList;
    }

    public void setOrderedSegmentList(ArrayList<Segment> orderedSegmentList) {
        this.orderedSegmentList = orderedSegmentList;
    }

    public void setOrderedIntersectionList(ArrayList<Intersection> orderedIntersectionList) {
        this.orderedIntersectionList = orderedIntersectionList;
    }
}
