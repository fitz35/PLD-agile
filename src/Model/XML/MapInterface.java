package Model.XML;

import Model.Intersection;
import Model.Segment;

import java.util.ArrayList;

public interface MapInterface {

    public void loadMap(String fileName);
    public Intersection getIntersectionById(long id);
    public ArrayList<Intersection>  getIntersectionList();
    public ArrayList<Segment>  getSegmentList();

    public Intersection getIntersectionNorth();

    public Intersection getIntersectionSouth();

    public Intersection getIntersectionEast();
    public Intersection getIntersectionWest();

}
