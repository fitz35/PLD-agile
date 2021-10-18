package Model.XML;

import Model.Intersection;
import Model.Segment;
import Model.Tour;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Observable;

public abstract class MapInterface extends Observable {

    public abstract void loadMap(String fileName) throws ParserConfigurationException, ParseException, SAXException, IOException;
    public abstract void loadRequest(String fileName) throws ParserConfigurationException, SAXException, IOException, ParseException;
    public abstract Intersection getIntersectionById(long id);
    public abstract ArrayList<Intersection>  getIntersectionList();
    public abstract ArrayList<Segment>  getSegmentList();
    public abstract Tour getTour();


    public abstract Intersection getIntersectionNorth();
    public abstract Intersection getIntersectionSouth();
    public abstract Intersection getIntersectionEast();
    public abstract Intersection getIntersectionWest();

}
