package Model;

import javax.swing.*;
import java.util.ArrayList;

public class Tour {
    private ArrayList<Segment> orderedSegmentList;
    private ArrayList<Intersection> orderedIntersectionList;
    private Map map;

    public Tour() {
        orderedSegmentList = new ArrayList<Segment>();
        orderedIntersectionList = new ArrayList<Intersection>();
    }
    
}
