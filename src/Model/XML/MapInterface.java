package Model.XML;

import Model.Intersection;
import Model.Segment;
import Model.Tour;

import java.util.ArrayList;
import java.util.Observable;

public abstract class MapInterface extends Observable {

    public abstract void loadMap(String fileName);
    public abstract void loadRequest(String fileName);
    public abstract Intersection getIntersectionById(long id);
    public abstract ArrayList<Intersection>  getIntersectionList();
    public abstract ArrayList<Segment>  getSegmentList();
    public abstract Tour getTour();


    public abstract Intersection getIntersectionNorth();
    public abstract Intersection getIntersectionSouth();
    public abstract Intersection getIntersectionEast();
    public abstract Intersection getIntersectionWest();

}
