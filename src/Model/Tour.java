package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Tour{
    @Deprecated
    private LinkedList<Segment> orderedSegmentList;
    private LinkedList<Path> orderedPathList;
    private HashMap<Intersection, Integer> addressPathHashMap;


    public Path findPath(Address origin)
    {
        Path path = orderedPathList.get(addressPathHashMap.get(origin));
        return path;
    }

    public Tour(LinkedList<Path> orderedPathList) {
        orderedSegmentList = new LinkedList<>();
        addressPathHashMap = new HashMap<>();
        this.orderedPathList = orderedPathList;
        int i = 0;
        for(Path path: orderedPathList)
        {
            addressPathHashMap.put(path.getDeparture(), i);
            orderedSegmentList.addAll(path.getSegmentsOfPath());
            i++;

        }
    }
    @Deprecated
    public LinkedList<Segment> getOrderedSegmentList() {
        return orderedSegmentList;
    }

    public LinkedList<Path> getOrderedPathList() {return orderedPathList; }

    public void setOrderedSegmentList(LinkedList<Segment> orderedSegmentList) {
        this.orderedSegmentList = orderedSegmentList;
    }

}
