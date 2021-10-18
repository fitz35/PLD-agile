package Model;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Observable;

public class Tour extends Observable {
    private ArrayList<Segment> orderedSegmentList;
    private ArrayList<Intersection> orderedIntersectionList;
    private Map map;

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

    public Map getMap() {
        return map;
    }

    public void setOrderedSegmentList(ArrayList<Segment> orderedSegmentList) {
        this.orderedSegmentList = orderedSegmentList;
    }

    public void setOrderedIntersectionList(ArrayList<Intersection> orderedIntersectionList) {
        this.orderedIntersectionList = orderedIntersectionList;
    }
}
