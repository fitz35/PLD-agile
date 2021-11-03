package Model;

import java.util.ArrayList;
import java.util.LinkedList;

public class Tour{
    @Deprecated
    private LinkedList<Segment> orderedSegmentList;
    //liste de path
    //methode pour retrouver un path a partir de 2 adresses
    // public getPath(Adress origin, Adress destination)

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
