package Model;

import java.util.HashMap;
import java.util.LinkedList;

public class Tour{
    @Deprecated
    private LinkedList<Segment> orderedSegmentList;
    private LinkedList<Path> orderedPathList;
    private HashMap<Intersection, Integer> originPathMap;
    private HashMap<Intersection, Integer> destinationPathMap;


    public Path findPathOrigin(Address origin){
        Path path = orderedPathList.get(originPathMap.get(origin));
        return path;
    }

    public Path findPathDestination(Address destination){
        Path path = orderedPathList.get(destinationPathMap.get(destination));
        return path;
    }

    public Tour(LinkedList<Path> orderedPathList) {
        orderedSegmentList = new LinkedList<>();
        originPathMap = new HashMap<>();
        destinationPathMap = new HashMap<>();
        this.orderedPathList = orderedPathList;
        int i = 0;
        for(Path path: orderedPathList){
            originPathMap.put(path.getDeparture(), i);
            destinationPathMap.put(path.getArrival(), i);
            orderedSegmentList.addAll(path.getSegmentsOfPath());
            i++;
        }
    }
    @Deprecated
    public LinkedList<Segment> getOrderedSegmentList() {
        return orderedSegmentList;
    }

    public void replaceOldPath(Path oldPath, Path newPath1, Path newPath2){
        if (this.orderedPathList.contains(oldPath)){
            int index= this.orderedPathList.indexOf(oldPath);
            LinkedList<Path> newPath= new LinkedList<Path>();
            newPath.add(1,newPath1);
            newPath.add(2,newPath2);
            this.orderedPathList.addAll(index,newPath);
            this.orderedPathList.remove(oldPath);
        }
    }

    public LinkedList<Path> getOrderedPathList() {
        return orderedPathList;
    }

    public void setOrderedSegmentList(LinkedList<Segment> orderedSegmentList) {
        this.orderedSegmentList = orderedSegmentList;
    }
}
