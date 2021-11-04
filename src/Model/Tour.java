package Model;

import java.util.ArrayList;
import java.util.LinkedList;

public class Tour{
    @Deprecated
    private LinkedList<Segment> orderedSegmentList;
    private LinkedList<Path> orderedPathList;


    public Path findPath(Address origin)
    {
        Path myPath = null;
        for(Path path : orderedPathList)
        {
            if(path.getDeparture().equals(origin))
            {
                return path;
            }
        }
        return myPath;
    }

    public Tour(LinkedList<Path> orderedPathList) {
        orderedSegmentList = new LinkedList<Segment>();
        this.orderedPathList = orderedPathList;
        for(Path path: orderedPathList)
        {
            orderedSegmentList.addAll(path.getSegmentsOfPath());
        }
    }

    public LinkedList<Segment> getOrderedSegmentList() {
        return orderedSegmentList;
    }

    public LinkedList<Path> getOrderedPathList() {return orderedPathList; }

    public void setOrderedSegmentList(LinkedList<Segment> orderedSegmentList) {
        this.orderedSegmentList = orderedSegmentList;
    }

}
