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
    /**
     * load a xml map
     * @param fileName the xml file
     * @throws ParserConfigurationException
     * @throws ParseException
     * @throws SAXException
     * @throws IOException
     */
    public abstract void loadMap(String fileName) throws ParserConfigurationException, ParseException, SAXException,
            IOException;

    /**
     * load a xml request
     * @param fileName the xml file
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ParseException
     */
    public abstract void loadRequest(String fileName) throws ParserConfigurationException, SAXException, IOException,
            ParseException;

    /**
     * get an intersection by an id
     * @param id the id
     * @return the intersection
     */
    public abstract Intersection getIntersectionById(long id);

    /**
     * all the intersection
     * @return all the intersection
     */
    public abstract ArrayList<Intersection>  getIntersectionList();

    /**
     * all the segment
     * @return all the segment
     */
    public abstract ArrayList<Segment>  getSegmentList();

    /**
     * the calculated tour
     * @return the calculated tour if it exist (can be null)
     */
    public abstract Tour getTour();

    /**
     * reset the map (put the segmentList and the intersectionList to empty,not null)
     */
    public abstract void resetMap();

    /**
     * put the planning request to null
     */
    public abstract void resetPlanningRequest();

    /**
     * get the graphe
     * @return get the graphe
     */
    public abstract HashMap<Intersection, LinkedList<Segment>> getGraphe();

    /**
     * get the planning request
     * @return the planning request (can be null)
     */
    public abstract PlanningRequest getPlanningRequest();

    /**
     * compute the tour with a timeout
     * @param timeout the timeout (seconds)
     */
    public abstract void computeTour(int timeout);

    /**
     * continue a tour (must be launch before with computeTour)
     * @param timeout the timeout
     */
    public abstract void continueTour(int timeout);

    /**
     * the intersection the most in north
     * @return the intersection
     */
    public abstract Intersection getIntersectionNorth();
    /**
     * the intersection the most in south
     * @return the intersection
     */
    public abstract Intersection getIntersectionSouth();
    /**
     * the intersection the most in east
     * @return the intersection
     */
    public abstract Intersection getIntersectionEast();
    /**
     * the intersection the most in west
     * @return the intersection
     */
    public abstract Intersection getIntersectionWest();

    /**
     * test if the map is loaded
     * @return if the map is loaded
     */
    public abstract boolean isMapLoaded();

    /**
     * test if the planning request is loaded
     * @return if the planning request is loaded
     */
    public abstract boolean isPlanningLoaded();

    /**
     * get a timeout Error (0 : no error, 1 : error)
     * @return the timeout
     */
    public abstract int getTimedOutError();

    /**
     * reset the timeout to 0
     */
    public abstract void resetTimedOutError();

    /**
     * reset a tour (null)
     */
    public abstract void resetTour();

    /**
     * Add a new request to the previously computed tour
     * @param beforeNewPickup the address to visit before visiting the new pick up address
     * @param newPickup the new pick up address
     * @param beforeNewDelivery the address to visit before visiting the new delivery address
     * @param newDelivery the new delivery address
     */
    public abstract void addRequest(Address beforeNewPickup, Address newPickup, Address beforeNewDelivery, Address newDelivery);
}
