package Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import Model.XML.MapInterface;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.lang.Long;
import java.lang.Double;
import java.util.Observable;

public class Map extends MapInterface {
    private ArrayList<Segment> segmentList;
    private ArrayList<Intersection> intersectionList;
    private PlanningRequest planningRequest;
    private HashMap<Intersection,HashMap<Intersection,Segment>> graphe;
    private Tour tour;
    private Intersection[] extremIntersection;
    private boolean mapLoaded = false;
    private boolean planningLoaded = false;

    /**
     * Constructor
     */
    public Map() {
        resetMap();
        resetPlanning();
        resetGraphe();
    }

    /**
     * This method create the city graph
     */
    @Override
    public void createGraph() {
        resetGraphe();
        // For each intersection
        for (Intersection inter : intersectionList) {
            HashMap<Intersection, Segment> destinations = new HashMap<>();
            Long intersectionID = inter.getId();
            System.out.println("Intersection id :"+intersectionID);
            // For each segment
            for (Segment segment : segmentList) {
                Long segmentOriginId = segment.getOrigin().getId();
                Intersection segmentDest = segment.getDestination();
                // If the segment's origin id corresponds to the intersection id
                if (segmentOriginId.equals(intersectionID)) {
                    // Add the segment to the list of destination of this intersection
                    destinations.put(segmentDest, segment);
                    System.out.println("Segment originId :"+segmentOriginId+"; destId :"+segmentDest.getId());
                }
            }
            // Add the intersection and its list of segment
            graphe.put(inter, destinations);
        }
    }

    /**
     * This method open and parse a xml map file
     *
     * @param fileName
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    @Override
    public void loadMap(String fileName) throws ParserConfigurationException, SAXException, IOException {
        resetMap();
        //Test extension of XML file name
        String[] words = fileName.split("\\.");
        if(!words[(words.length)-1].equals("XML") && !words[(words.length)-1].equals("xml")){
            this.setChanged();
            this.notifyObservers("Filename extension is not correct");
        }else{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            try {
                // parse XML file
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new File(fileName));
                doc.getDocumentElement().normalize();

                // get all nodes <intersection>
                NodeList nodeListIntersection = doc.getElementsByTagName("intersection");

                for (int temp = 0; temp < nodeListIntersection.getLength(); temp++) {
                    Node node = nodeListIntersection.item(temp);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        // get intersection's attribute
                        long id =  Long.parseLong(element.getAttribute("id"));
                        double latitude = Double.parseDouble(element.getAttribute("latitude"));
                        double longitude = Double.parseDouble(element.getAttribute("longitude"));
                        System.out.println("unique ?"+checkUniqueIntersection(id,latitude,longitude));
                        System.out.println("Intersection: "+id+"; latitude:"+latitude+"; longitude:"+longitude);
                        // if the intersection doesn't exist in the list
                        if(checkUniqueIntersection(id,latitude,longitude)){
                            // create and add the intersection
                            intersectionList.add(new Intersection(id,latitude,longitude));
                        }else{
                            System.out.println("already in the list");
                        }
                    }
                }

                // get all nodes <Segment>
                NodeList nodeListSegment = doc.getElementsByTagName("segment");

                for (int temp = 0; temp < nodeListSegment.getLength(); temp++) {
                    Node node = nodeListSegment.item(temp);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        // get intersection's attribute
                        long destinationId =  Long.parseLong(element.getAttribute("destination"));
                        long originId =  Long.parseLong(element.getAttribute("origin"));
                        double length = Double.parseDouble(element.getAttribute("length"));
                        String name = element.getAttribute("name");

                        Intersection origin = getIntersectionById(originId);
                        Intersection destination = getIntersectionById(destinationId);
                        // check that the origin and the destination of the segment are existing intersections
                        if(origin != null && destination != null){
                            segmentList.add(new Segment(origin,destination,name,length));
                        }else{
                            System.out.println("segment creation is impossible");
                        }
                        System.out.println("segment: origin:"+originId+"; destination:"+destinationId+"; length:"+length+"; name:"+name);
                    }
                }
                extremIntersection = getExtremIntersection();
                mapLoaded = true;
            } catch (ParserConfigurationException |SAXException err){
                this.setChanged();
                this.notifyObservers("Parsing XML file failed");
                throw err;
            }catch( IOException err) {
                this.setChanged();
                this.notifyObservers("Opening XML file failed");
                throw err;
            }
            this.setChanged();
        }
    }



    /**
     * This method open and parse a xml planning file
     *
     * @param fileName
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ParseException
     */
    @Override
    public void loadRequest(String fileName) throws ParserConfigurationException, SAXException, IOException, ParseException {
        resetPlanning();

        //Test extension of XML file name
        String[] words = fileName.split("\\.");
        if(!mapLoaded){
            this.setChanged();
            this.notifyObservers("No map loaded");
        }else if(!words[(words.length)-1].equals("XML") && !words[(words.length)-1].equals("xml")){
            this.setChanged();
            this.notifyObservers("Filename extension is not correct");
        }else {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            try {
                // parse XML file
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new File(fileName));
                doc.getDocumentElement().normalize();

                // get all nodes <intersection>
                NodeList nodeListRequest = doc.getElementsByTagName("request");

                for (int temp = 0; temp < nodeListRequest.getLength(); temp++) {
                    Node node = nodeListRequest.item(temp);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        // get request's attribute
                        long pickupAddressId = Long.parseLong(element.getAttribute("pickupAddress"));
                        Intersection pickupAddress = getIntersectionById(pickupAddressId);
                        long deliveryAddressId = Long.parseLong(element.getAttribute("deliveryAddress"));
                        Intersection deliveryAddress = getIntersectionById(deliveryAddressId);
                        int pickupDuration = Integer.parseInt(element.getAttribute("pickupDuration"));
                        int deliveryDuration = Integer.parseInt(element.getAttribute("deliveryDuration"));

                        System.out.println("Existing address ?");
                        System.out.println("Request: pickupAddress:" + pickupAddressId + "; deliveryAddress:" + deliveryAddressId + "; pickupDuration: " + pickupDuration + " deliveryDuration: " + deliveryDuration + ";");

                        planningRequest.addRequest(new Request(pickupAddress, pickupDuration, deliveryAddress, deliveryDuration));
                    }
                }

                // get the depot
                NodeList nodeListDepot = doc.getElementsByTagName("depot");
                for (int temp = 0; temp < nodeListDepot.getLength(); temp++) {
                    Node node = nodeListDepot.item(temp);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        // get request's attribute
                        long addressId = Long.parseLong(element.getAttribute("address"));
                        String departTime = element.getAttribute("departureTime");

                        planningRequest.setStartingPoint(getIntersectionById(addressId));
                        planningRequest.setDepartureTime(new SimpleDateFormat("HH:mm:ss").parse(departTime));

                        System.out.println("Depot: Starting point: " + addressId + " ; departureTime: " + departTime + ";");
                    }
                }
                planningLoaded = true;
            } catch (ParserConfigurationException | SAXException err) {
                this.setChanged();
                this.notifyObservers("Parsing XML file failed");
                throw err;
            } catch (ParseException err) {
                this.setChanged();
                this.notifyObservers("Bad departureTime format");
                throw err;
            } catch (IOException err) {
                this.setChanged();
                this.notifyObservers("Opening XML file failed");
                throw err;
            }
            this.setChanged();
        }
    }

    /**
     * Reset the map : create new segmentList and intersectionList
     */
    @Override
    public void resetMap()
    {
        segmentList=new ArrayList<>();
        intersectionList=new ArrayList<>();
    }

    /**
     * Reset the graphe : create new graphe
     */
    @Override
    public void resetGraphe()
    {
        graphe=new HashMap<>();
    }

    /**
     * Reset the planning : create new planning
     */
    @Override
    public void resetPlanning()
    {
        planningRequest=new PlanningRequest();
    }

    /**
     * This method find the extrem intersections (north, south, east, west)
     *
     * @return extremum|null
     */
    private Intersection[] getExtremIntersection(){
        if(!mapLoaded){
            return null;
        }
        Intersection northernmost = intersectionList.get(0);
        Intersection southernmost = intersectionList.get(0);
        Intersection easternmost = intersectionList.get(0);
        Intersection westernmost = intersectionList.get(0);

        for(int i=0 ; i<intersectionList.size() ; i++){
            Intersection testedIntersection = intersectionList.get(i);
            if(northernmost.getLatitude() < testedIntersection.getLatitude()) { northernmost = testedIntersection; } //north
            if(southernmost.getLatitude() > testedIntersection.getLatitude()) { southernmost = testedIntersection; } //south
            if(easternmost.getLongitude() < testedIntersection.getLongitude()) { easternmost = testedIntersection; } //east
            if(westernmost.getLongitude() > testedIntersection.getLongitude()) { westernmost = testedIntersection; } //west
        }
        Intersection[] extremum = {northernmost , southernmost , easternmost , westernmost};
        return extremum;
    }

    /**
     * This method return the northern intersection
     *
     * @return intersection|null
     */
    @Override
    public Intersection getIntersectionNorth(){
        if(!mapLoaded){
            return null;
        }
        return extremIntersection[0];
    };

    /**
     * This method return the southern intersection
     *
     * @return intersection|null
     */
    @Override
    public Intersection getIntersectionSouth(){
        if(!mapLoaded){
            return null;
        }
        return extremIntersection[1];
    };

    /**
     * This method return the eastern intersection
     *
     * @return intersection|null
     */
    @Override
    public Intersection getIntersectionEast(){
        if(!mapLoaded){
            return null;
        }
        return extremIntersection[2];
    };

    /**
     * This method return the western intersection
     *
     * @return intersection|null
     */
    @Override
    public Intersection getIntersectionWest(){
        if(!mapLoaded){
            return null;
        }
        return extremIntersection[3];
    };

    /**
     * Check if the data provided correspond to an existing intersection in the list
     *
     * @param intersectionId
     * @param latitude
     * @param longitude
     * @return res
     */
    @Override
    public boolean checkUniqueIntersection(long intersectionId,double latitude,double longitude)
    {
        boolean res=true;
        Intersection intersectionToTest= new Intersection(intersectionId,latitude,longitude);
        for(Intersection intersection:intersectionList)
        {
            if(intersectionToTest.equals(intersection))
            {
                res=false;
            }
        }
        return res;
    }

    /**
     * Return the intersection corresponding to the id
     *
     * @param intersectionId
     * @return intersection|null
     */
    public Intersection getIntersectionById(long intersectionId)
    {
        for(Intersection intersection : intersectionList)
        {
            if(intersection.getId() == intersectionId )
            {
                return intersection;
            }
        }
        return null;
    }

    /**
     * @return graphe
     */
    @Override
    public HashMap<Intersection, HashMap<Intersection, Segment>> getGraphe() {
        return graphe;
    }

    /**
     * @return intersectionList
     */
    @Override
    public ArrayList<Intersection> getIntersectionList() {
        return intersectionList;
    }

    /**
     * @return segmentList
     */
    @Override
    public ArrayList<Segment> getSegmentList() {
        return segmentList;
    }

    /**
     * @return tour
     */
    @Override
    public Tour getTour(){return this.tour;}

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, ParseException {
        Map map=new Map();
        map.loadMap("./data/fichiersXML2020/smallMap.xml");
        // PlanningRequest planning = new PlanningRequest();
        //map.loadRequest("./data/fichiersXML2020/requestsMedium5.xml");
        // System.out.println("passé");
        map.createGraph();

        System.out.println(map.getExtremIntersection()[0].getId() +"  "+ map.getExtremIntersection()[1].getId()+"  "+ map.getExtremIntersection()[2].getId()+"  "+ map.getExtremIntersection()[3].getId());
        System.out.println("passé");
    }

}
