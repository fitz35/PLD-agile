package Model.XML;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import Model.*;
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
    private HashMap<Intersection,LinkedList<Segment>> graphe;
    private Tour tour;
    private Intersection[] extremIntersection;
    private boolean mapLoaded = false;
    private boolean planningLoaded = false;


    public Tour getTour(){return this.tour;}

    public Map() {
        segmentList = new ArrayList<Segment>();
        intersectionList = new ArrayList<Intersection>();
        planningRequest = new PlanningRequest();
        graphe= new HashMap<>();
    }


    public HashMap<Intersection,LinkedList<Segment>> createGraph() {
        for (Intersection inter : intersectionList) {
            //HashMap<Intersection, Segment> destinations = new HashMap<>();
            LinkedList<Segment> interSegments = new LinkedList<>();
            Long intersectionID = inter.getId();
            System.out.println("Intersection id :"+intersectionID);
            for (Segment segment : segmentList) {
                Long segmentOriginId = segment.getOrigin().getId();
                if (segmentOriginId.equals(intersectionID)) {
                    interSegments.add(segment);
                    Intersection segmentDest = segment.getDestination();
                    System.out.println("Segment originId :"+segmentOriginId+"; destId :"+segmentDest.getId());
                }
            }
            graphe.put(inter, interSegments);
        }
        //test
        return graphe;
    }

    @Override
    public void loadMap(String fileName) throws ParserConfigurationException, SAXException, IOException {
        intersectionList=new ArrayList<>();
        segmentList=new ArrayList<>();
        //Test extension of XML file name
        String[] words = fileName.split("\\.");
        if(!words[(words.length)-1].equals("XML") && !words[(words.length)-1].equals("xml")){
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

                        if(checkUniqueIntersection(id,latitude,longitude)){
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
                this.notifyObservers("Parsing XML file failed");
                throw err;
            }catch( IOException err) {
                this.notifyObservers("Opening XML file failed");
                throw err;
            }
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

    public HashMap<Intersection, LinkedList<Segment>> getGraphe() {
        return graphe;
    }

    @Override
    public void loadRequest(String fileName) throws ParserConfigurationException, SAXException, IOException, ParseException {
        planningRequest=new PlanningRequest();

        //Test extension of XML file name
        String[] words = fileName.split("\\.");
        if(!mapLoaded){
            this.notifyObservers("No map loaded");
        }else if(!words[(words.length)-1].equals("XML") && !words[(words.length)-1].equals("xml")){
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
                this.notifyObservers("Parsing XML file failed");
                throw err;
            } catch (ParseException err) {
                this.notifyObservers("Bad departureTime format");
                throw err;
            } catch (IOException err) {
                this.notifyObservers("Opening XML file failed");
                throw err;
            }
        }
    }

    public Intersection[] getExtremIntersection(){
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
    public Intersection getIntersectionNorth(){return extremIntersection[0];};

    @Override
    public Intersection getIntersectionSouth(){return extremIntersection[1];};

    @Override
    public Intersection getIntersectionEast(){return extremIntersection[2];};

    @Override
    public Intersection getIntersectionWest(){return extremIntersection[3];};

    @Override
    public ArrayList<Intersection> getIntersectionList() {
        return intersectionList;
    }

    @Override
    public ArrayList<Segment> getSegmentList() {
        return segmentList;
    }

    public HashMap<Intersection,Segment> djikstra(Intersection startIntersection){
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
                noir.add(minimalSuccessor);
                gris.remove(minimalSuccessor);
            });
        }
        return pi;
    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, ParseException {
        Map map=new Map();
        //map.loadMap("./data/fichiersXML2020/smallMap.xml");
        // PlanningRequest planning = new PlanningRequest();
        //map.loadRequest("./data/fichiersXML2020/requestsMedium5.xml");
        // System.out.println("passé");
        map.loadMap("src/Model/XML/mapTest.xml");

        map.loadRequest("src/Model/XML/planingTest.xml");

        HashMap<Intersection,LinkedList<Segment>> graphe = new HashMap<>();
        graphe = map.createGraph();
        //System.out.println(graphe.get(0).get(0).getOrigin().getId());
        Intersection inter = new Intersection(0,4.75,2.2);
        //LinkedList<Segment> seg = graphe.get(inter);
        //System.out.println(seg.get(0).getOrigin());
        HashMap<Intersection,Segment> testDjikstra = new HashMap<>();
        testDjikstra = map.djikstra(inter);
        System.out.println("Test djikstra");
        testDjikstra.forEach((inte, segm)->{
            System.out.println(inte.getId());
        });

        //System.out.println(map.getExtremIntersection()[0].getId() +"  "+ map.getExtremIntersection()[1].getId()+"  "+ map.getExtremIntersection()[2].getId()+"  "+ map.getExtremIntersection()[3].getId());
        System.out.println("passé");
    }

}