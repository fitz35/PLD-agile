package Model;

import java.util.ArrayList;

public interface MapInterface {

    public void loadMap(String fileName);
    public Intersection getIntersectionById(long id);
    public ArrayList<Intersection>  getIntersectionList();
    public ArrayList<Segment>  getSegmentList();

}
