package Model;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Map extends MapInterface {
    private ArrayList<Segment> segmentList;
    private ArrayList<Intersection> intersectionList;
    private PlanningRequest planningRequest;
    private HashMap<Intersection,LinkedList<Segment>> graphe;
    private Tour tour;
    private Intersection[] extremIntersection;
    private boolean mapLoaded = false;
    private boolean planningLoaded = false;
    private DeliveryGraph deliveryGraph;

    public Tour getTour(){return this.tour;}

    public Map() {
        resetMap();
        resetPlanningRequest();
        graphe= new HashMap<>();
    }


    public HashMap<Intersection,LinkedList<Segment>> createGraph() {
        for (Intersection inter : intersectionList) {
            //HashMap<Intersection, Segment> destinations = new HashMap<>();
            LinkedList<Segment> interSegments = new LinkedList<>();
            Long intersectionID = inter.getId();
            //System.out.println("Intersection id :"+intersectionID);
            for (Segment segment : segmentList) {
                Long segmentOriginId = segment.getOrigin().getId();
                if (segmentOriginId.equals(intersectionID)) {
                    interSegments.add(segment);
                    Intersection segmentDest = segment.getDestination();
                    //System.out.println("Segment originId :"+segmentOriginId+"; destId :"+segmentDest.getId());
                }
            }
            graphe.put(inter, interSegments);
        }
        //test
        return graphe;
    }

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
                        // if the intersection doesn't exist in the list
                        if(checkUniqueIntersection(id,latitude,longitude)){
                            intersectionList.add(new Intersection(id,latitude,longitude));
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
                        long destinationId = Long.parseLong(element.getAttribute("destination"));
                        long originId = Long.parseLong(element.getAttribute("origin"));
                        double length = Double.parseDouble(element.getAttribute("length"));
                        String name = element.getAttribute("name");

                        Intersection origin = getIntersectionById(originId);
                        Intersection destination = getIntersectionById(destinationId);
                        if (origin != null && destination != null) {
                            segmentList.add(new Segment(origin, destination, name, length));
                        } else {
                            // System.out.println("segment creation is impossible");
                        }
                    }
                }

            } catch (ParserConfigurationException |SAXException err){
                this.setChanged();
                this.notifyObservers("Parsing XML file failed.");
                throw err;
            }catch( IOException err) {
                this.setChanged();
                this.notifyObservers("Opening XML file failed.");
                throw err;
            }
            if(intersectionList.isEmpty() || segmentList.isEmpty())
            {
                this.setChanged();
                this.notifyObservers("Map is empty. Check your XML file.");
                throw new IOException();
            }
            mapLoaded = true;
            extremIntersection = getExtremIntersection();
            this.setChanged();
        }
    }

    // Check if the data provided correspond to an existant intersection in the list
    public boolean checkUniqueIntersection(long intersectionId,double latitude,double longitude)
    {
        boolean res=true;
        for(Intersection intersection:intersectionList)
        {
            if( intersection.getId() == intersectionId &&
                intersection.getLatitude()== latitude &&
                intersection.getLongitude()==longitude)
            {
                res=false;
            }
        }
        return res;
    }

    // Return the intersection corresponding to the id
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

    public HashMap<Intersection,LinkedList<Segment>> getGraphe() {
        return graphe;
    }

    @Override
    public void resetMap()
    {
        segmentList=new ArrayList<>();
        intersectionList=new ArrayList<>();
    }

    @Override
    public void resetPlanningRequest()
    {
        planningRequest=null;
        this.setChanged();
        this.notifyObservers();
    }

    @Override
    public void loadRequest(String fileName) throws ParserConfigurationException, SAXException, IOException, ParseException {
        resetPlanningRequest();
        planningRequest=new PlanningRequest();

        //Test extension of XML file name
        String[] words = fileName.split("\\.");
        if(!mapLoaded){
            this.setChanged();
            this.notifyObservers("No map loaded, load a map and try again.");
            throw new IOException();
        }else if(!words[(words.length)-1].equals("XML") && !words[(words.length)-1].equals("xml")){
            this.setChanged();
            this.notifyObservers("Filename extension is not correct.");
            throw new IOException();
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

                        if(deliveryAddress!=null && pickupAddress !=null)
                        {
                            planningRequest.addRequest(new Request(pickupAddress, pickupDuration, deliveryAddress, deliveryDuration));
                        }
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
                    }
                }
                planningLoaded = true;
            } catch (ParserConfigurationException | SAXException err) {
                this.setChanged();
                this.notifyObservers("Parsing XML file failed.");
                throw err;
            } catch (ParseException err) {
                this.setChanged();
                this.notifyObservers("Bad departureTime format.");
                throw err;
            } catch (IOException err) {
                this.setChanged();
                this.notifyObservers("Opening XML file failed.");
                throw err;
            }

            if(planningRequest.getRequestList().isEmpty())
            {
                this.setChanged();
                this.notifyObservers("Planning is empty. Check your XML file.");
                throw new IOException();
            }
            this.createGraph();
            this.setChanged();
        }
    }

    private Intersection[] getExtremIntersection(){
        //System.out.println(mapLoaded);
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

    @Override
    public Intersection getIntersectionNorth(){
        if(!mapLoaded){
            return null;
        }
        return extremIntersection[0];
    };

    @Override
    public Intersection getIntersectionSouth(){
        if(!mapLoaded){
            return null;
        }
        return extremIntersection[1];
    };

    @Override
    public Intersection getIntersectionEast(){
        if(!mapLoaded){
            return null;
        }
        return extremIntersection[2];
    };

    @Override
    public Intersection getIntersectionWest(){
        if(!mapLoaded){
            return null;
        }
        return extremIntersection[3];
    };

    @Override
    public ArrayList<Intersection> getIntersectionList() {
        return intersectionList;
    }

    @Override
    public ArrayList<Segment> getSegmentList() {
        return segmentList;
    }

    public HashMap<Intersection,Segment> dijkstra(Intersection startIntersection){
        HashMap<Intersection,Double> d = new HashMap<>();
        HashMap<Intersection,Segment> pi = new HashMap<>();
        ArrayList<Intersection> blanc= new ArrayList<>(),gris= new ArrayList<>(),noir = new ArrayList<>();
        graphe.forEach((i, dest) -> {
            if(i.equals(startIntersection)){
                d.put(startIntersection,0.0);
            }else {
                d.put(i, Double.MAX_VALUE);
            }
            pi.put(i,null);
            blanc.add(i);
        });
        gris.add(startIntersection);
        while (!gris.isEmpty()){
            Intersection minimalSuccessor = gris.get(0);
            LinkedList<Segment> destinations = graphe.get(minimalSuccessor);
            destinations.forEach((segment)->{
                Intersection successor = segment.getDestination();
                if((blanc.contains(successor)) || (gris.contains(successor))){
                    if(d.get(successor) > d.get(minimalSuccessor) + segment.getLength()){
                        d.put(successor, d.get(minimalSuccessor) + segment.getLength());
                        pi.replace(successor, segment);
                    }
                }
                if(blanc.contains(successor)){
                    int a=0;
                    for(int i=0; i<gris.size(); i++){
                        a=i;
                        if( d.get(gris.get(i)) > d.get(successor) ){
                            break;
                        }
                    }
                    gris.add(a,successor);
                    blanc.remove(successor);
                }
            });
            noir.add(minimalSuccessor);
            gris.remove(minimalSuccessor);
        }
        return pi;
    }

    public void computeTour(int timeout){
        ArrayList<Intersection> listIntersections = this.planningRequest.getIntersection();
        this.deliveryGraph = new DeliveryGraph(listIntersections);
        for(int i=0; i<listIntersections.size();i++){
            HashMap<Intersection,Segment> pi = dijkstra(listIntersections.get(i));
            deliveryGraph.addVertice(i,pi);
        }
        LinkedList<Segment> tourCalculated = deliveryGraph.solveTSP(timeout);
        tour = new Tour(tourCalculated);
        this.setChanged();
        this.notifyObservers();
    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, ParseException {
        Map map=new Map();
        map.loadMap("./data/fichiersXML2020/smallMap.xml");
        // PlanningRequest planning = new PlanningRequest();
        //map.loadRequest("./data/fichiersXML2020/requestsMedium5.xml");
        // System.out.println("passé");

        //TEST DJIKSTRA
        map.loadMap("src/Model/XML/mapTest.xml");
        map.loadRequest("src/Model/XML/planingTest.xml");
        HashMap<Intersection,LinkedList<Segment>> graphe = new HashMap<>();
        graphe = map.createGraph();
        Intersection inter = new Intersection(0,4.75,2.2);
        HashMap<Intersection,Segment> testDjikstra = new HashMap<>();
        testDjikstra = map.dijkstra(inter);
        //System.out.println("Test djikstra");
        testDjikstra.forEach((inte, segm)->{
            //System.out.println(inte.getId());
        });

        map.computeTour(200000000);

        //System.out.println(map.getExtremIntersection()[0].getId() +"  "+ map.getExtremIntersection()[1].getId()+"  "+ map.getExtremIntersection()[2].getId()+"  "+ map.getExtremIntersection()[3].getId());
        //System.out.println("passé");
    }

    public PlanningRequest getPlanningRequest()
    {
        return this.planningRequest;
    }

}
