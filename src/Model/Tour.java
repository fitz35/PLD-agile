package Model;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Class that represents the tour to be followed by the driver
 */
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
     * Precondition: newPath1.destination should be equal to newPath2.origin
     * @param oldPath the path to be replaced
     * @param newPath1 the first path to add
     * @param newPath2 the second path to add
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
            this.originPathMap.put(newPath1.getDeparture(),newPath1);
            this.originPathMap.put(newPath2.getDeparture(),newPath2);
            this.destinationPathMap.put(newPath1.getArrival(),newPath1);
            this.destinationPathMap.put(newPath2.getArrival(),newPath2);
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
     * Find a given path by it's origin
     * @param origin origin of the path to be found
     * @return path
     */
    public Path findPathOrigin(Address origin){
        Path path = originPathMap.get(origin);
        return path;
    }

    /**
     * Find a given path by it's destination
     * @param destination destination of the path to be found
     * @return path
     */
    public Path findPathDestination(Address destination){
        Path path = destinationPathMap.get(destination);
        return path;
    }

    /**
     * Replace 2 consecutive paths with a new path
     * @param oldPath1 first path to be removed
     * @param oldPath2 second path to be removed
     * @param newPath path to put into the list
     */
    public void replaceOldPaths(Path oldPath1, Path oldPath2, Path newPath){
        if (this.orderedPathList.contains(oldPath1) || this.orderedPathList.contains(oldPath2)){
            int index= this.orderedPathList.indexOf(oldPath1);
            this.orderedPathList.add(index, newPath);
            this.orderedPathList.remove(oldPath1);
            this.orderedPathList.remove(oldPath2);

            this.destinationPathMap.remove(oldPath1.getArrival());
            this.destinationPathMap.remove(oldPath2.getArrival());
            this.destinationPathMap.put(newPath.getArrival(), newPath);

            this.originPathMap.remove(oldPath1.getDeparture());
            this.originPathMap.remove(oldPath2.getDeparture());
            this.originPathMap.put(newPath.getDeparture(), newPath);

        }
    }

    /**
     * Clears all the attributes from the class
     */
    public void reset(){
        orderedSegmentList.clear();
        originPathMap.clear();
        destinationPathMap.clear();
        orderedPathList.clear();
    }
}
