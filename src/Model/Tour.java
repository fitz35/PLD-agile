package Model;

import java.util.HashMap;
import java.util.LinkedList;

public class Tour{
    /**
     * An ordered list of segment to compute the tour
     */
    @Deprecated
    private LinkedList<Segment> orderedSegmentList;

    /**
     * An ordered list of path to compute the tour
     */
    private LinkedList<Path> orderedPathList;
    /**
     * Map to find a path given it's origin
     */
    private HashMap<Intersection, Path> originPathMap;
    /**
     * Map to find a path given it's destination
     */
    private HashMap<Intersection, Path> destinationPathMap;


    /**
     * Constructor
     * @param orderedPathList
     */
    public Tour(LinkedList<Path> orderedPathList) {
        orderedSegmentList = new LinkedList<>();
        originPathMap = new HashMap<>();
        destinationPathMap = new HashMap<>();
        this.orderedPathList = orderedPathList;
        int i = 0;
        for(Path path: orderedPathList){
            originPathMap.put(path.getDeparture(), path);
            destinationPathMap.put(path.getArrival(), path);
            orderedSegmentList.addAll(path.getSegmentsOfPath());
            i++;
        }
    }

    /**
     * @return orderedSegmentList
     */
    @Deprecated
    public LinkedList<Segment> getOrderedSegmentList() {
        return orderedSegmentList;
    }

    /**
     * Replace the old path with 2 new paths
     * @param oldPath
     * @param newPath1
     * @param newPath2
     */
    public void replaceOldPath(Path oldPath, Path newPath1, Path newPath2){
        if (this.orderedPathList.contains(oldPath)){
            int index= this.orderedPathList.indexOf(oldPath);
            LinkedList<Path> newPath= new LinkedList<Path>();
            newPath.add(newPath1);
            newPath.add(newPath2);
            this.orderedPathList.addAll(index,newPath);
            this.orderedPathList.remove(oldPath);
            this.originPathMap.remove(oldPath.getDeparture());
            this.destinationPathMap.remove(oldPath.getArrival());
        }
    }


    /**
     * @return orderedPathList
     */
    public LinkedList<Path> getOrderedPathList() {
        return orderedPathList;
    }

    /**
     * Set the orderedSegmentList
     * @param orderedSegmentList
     */
    public void setOrderedSegmentList(LinkedList<Segment> orderedSegmentList) {
        this.orderedSegmentList = orderedSegmentList;
    }

    /**
     * @param origin
     * @return path
     */
    public Path findPathOrigin(Address origin){
        Path path = originPathMap.get(origin);
        return path;
    }

    /**
     * @param destination
     * @return path
     */
    public Path findPathDestination(Address destination){
        Path path = destinationPathMap.get(destination);
        return path;
    }

    /**
     * Replace 2 old paths ordered with a new path
     * @param oldPath1
     * @param oldPath2
     * @param newPath
     */
    public void replaceOldPaths(Path oldPath1, Path oldPath2, Path newPath){
        if (this.orderedPathList.contains(oldPath1) || this.orderedPathList.contains(oldPath2)){
            int index= this.orderedPathList.indexOf(oldPath1);
            this.orderedPathList.add(index, newPath);
            this.orderedPathList.remove(oldPath1);
            this.orderedPathList.remove(oldPath2);
        }
    }
}
