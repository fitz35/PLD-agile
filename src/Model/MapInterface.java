package Model;

import Model.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;

public abstract class MapInterface extends Observable {

    public abstract void loadMap(String fileName) throws ParserConfigurationException, ParseException, SAXException, IOException;
    public abstract void loadRequest(String fileName) throws ParserConfigurationException, SAXException, IOException, ParseException;
    public abstract Intersection getIntersectionById(long id);
    public abstract ArrayList<Intersection>  getIntersectionList();
    public abstract ArrayList<Segment>  getSegmentList();
    public abstract Tour getTour();
    public abstract void resetMap();
    public abstract void resetPlanningRequest();
    public abstract HashMap<Intersection, LinkedList<Segment>> getGraphe();
    public abstract PlanningRequest getPlanningRequest();
    public abstract void computeTour(int timeout);


    public abstract Intersection getIntersectionNorth();
    public abstract Intersection getIntersectionSouth();
    public abstract Intersection getIntersectionEast();
    public abstract Intersection getIntersectionWest();

    public abstract boolean isMapLoaded();
    public abstract boolean isPlanningLoaded();
    public abstract boolean isFirstTourComputed();

    public abstract void resetTour();
}
