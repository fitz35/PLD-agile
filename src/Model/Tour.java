package Model;

import java.util.ArrayList;
import java.util.LinkedList;

public class Tour{
    private LinkedList<Segment> orderedSegmentList;
    //private ArrayList<Intersection> orderedIntersectionList;

    public Tour(LinkedList<Segment> orderedSegmentList) {
        this.orderedSegmentList = orderedSegmentList;
        //orderedIntersectionList = new ArrayList<Intersection>();
    }

    public LinkedList<Segment> getOrderedSegmentList() {
        return orderedSegmentList;
    }


    public void setOrderedSegmentList(LinkedList<Segment> orderedSegmentList) {
        this.orderedSegmentList = orderedSegmentList;
    }

}
