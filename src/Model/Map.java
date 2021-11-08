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
import java.util.*;

public class Map extends MapInterface {
    /**
     * List of sements which constitute the map
     */
    private ArrayList<Segment> segmentList;

    /**
     * List of intersections which constitute the map
     */
    private ArrayList<Intersection> intersectionList;

    /**
     * Planning with all requests
     */
    private PlanningRequest planningRequest;

    /**
     * The city graphe
     */
    private HashMap<Intersection,LinkedList<Segment>> graphe;

    /**
     * An optimal tour
     */
    private Tour tour;

    /**
     * Define the extreme intersections of the map
     * index 0 : north
     * index 1 : south
     * index 2 : east
     * index 3 : west
     */
    private Intersection[] extremIntersection;

    /**
     * Indicate if the map is loaded
     */
    private boolean mapLoaded = false;

    /**
     * Indicate if the planning is loaded
     */
    private boolean planningLoaded = false;

    /**
     * Store the delivery graphe
     */
    private DeliveryGraph deliveryGraph;

    /**
     * Indicate if the time of computation for the TSP is elapsed
     * 0 if it's not elapsed
     * 1 if it's elapsed
     */
    private int timedOutError;

    /**
     * Constructor
     */
    public Map() {
        resetMap();
        resetPlanningRequest();
        graphe= new HashMap<>();
    }

    /**
     * Creation of the city graphe
     * @return graphe the city graphe
     */
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
                }
            }
            graphe.put(inter, interSegments);
        }
        return graphe;
    }

    /**
     * Load a map with a xml file
     * @param fileName the xml file
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    @Override
    public void loadMap(String fileName) throws ParserConfigurationException, SAXException, IOException {
        resetMap();
        //Test extension of XML file name
        String[] words = fileName.split("\\.");
        if(!(words[(words.length)-1].equals("XML")) && !(words[(words.length)-1].equals("xml"))){
            this.setChanged();
            this.notifyObservers("Filename extension is not correct");
            throw new IOException();
        }else{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            try {
                // parse XML file
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new File(fileName));
                doc.getDocumentElement().normalize();

                // Check the document root name
                Element root = doc.getDocumentElement();
                if(!root.getNodeName().equals("map")){
                    throw new NumberFormatException();
                }

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
                        if ((origin != null) && (destination != null)) {
                            segmentList.add(new Segment(origin, destination, name, length));
                        } else {
                            // System.out.println("segment creation is impossible");
                        }
                    }
                }

            } catch (ParserConfigurationException |SAXException err){
                this.setChanged();
                this.notifyObservers("Parsing XML file failed. Please choose another XML file.");
                throw err;
            }catch( IOException err) {
                this.setChanged();
                this.notifyObservers("Opening XML file failed. Please choose another XML file.");
                throw err;
            }catch (NumberFormatException err){}

            if((intersectionList.isEmpty()) || (segmentList.isEmpty())){
                resetMap();
                this.setChanged();
                this.notifyObservers("Map is empty. Check your XML file.");
                throw new IOException();
            }
            mapLoaded = true;
            extremIntersection = getExtremIntersection();
            this.createGraph();
            this.setChanged();
            this.notifyObservers();
        }
    }

    /**
     * Load a planning of requests with a xml file, a map have to be loaded before
     * @param fileName the xml file
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ParseException
     */
    @Override
    public void loadRequest(String fileName)
            throws ParserConfigurationException, SAXException, IOException, ParseException {
        resetPlanningRequest();
        //Test extension of XML file name
        String[] words = fileName.split("\\.");
        if(!mapLoaded){
            this.setChanged();
            this.notifyObservers("No map loaded, load a map and try again.");
            throw new IOException();
        }else if(!(words[(words.length)-1].equals("XML")) && !(words[(words.length)-1].equals("xml"))){
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

                // Check the document root name
                Element root = doc.getDocumentElement();
                if(!root.getNodeName().equals("planningRequest")){
                    throw new NumberFormatException();
                }

                // get all nodes <intersection>
                NodeList nodeListRequest = doc.getElementsByTagName("request");
                for (int temp = 0; temp < nodeListRequest.getLength(); temp++) {
                    Node node = nodeListRequest.item(temp);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        // get request's attribute
                        long pickupIntersectionId = Long.parseLong(element.getAttribute("pickupAddress"));
                        Intersection pickupIntersection = getIntersectionById(pickupIntersectionId);
                        long deliveryIntersectionId = Long.parseLong(element.getAttribute("deliveryAddress"));
                        Intersection deliveryIntersection = getIntersectionById(deliveryIntersectionId);
                        if((pickupIntersection==null) || (deliveryIntersection==null)){
                            throw new NumberFormatException();
                        }
                        int pickupDuration = Integer.parseInt(element.getAttribute("pickupDuration"));
                        int deliveryDuration = Integer.parseInt(element.getAttribute("deliveryDuration"));
                        Address pickupAddress = new Address(pickupIntersectionId,pickupIntersection.getLatitude(),pickupIntersection.getLongitude(),pickupDuration, 1 /*for pickup*/);
                        Address deliveryAddress = new Address(deliveryIntersectionId,deliveryIntersection.getLatitude(),deliveryIntersection.getLongitude(),deliveryDuration, 2 /*for delivery*/);

                        planningRequest.addRequest(new Request(pickupAddress, deliveryAddress));
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
            } catch (ParserConfigurationException | SAXException err) {
                this.setChanged();
                this.notifyObservers("Parsing XML file failed.  Please choose another XML file.");
                throw err;
            } catch (ParseException err) {
                this.setChanged();
                this.notifyObservers("Bad departureTime format.");
                throw err;
            } catch (IOException err) {
                this.setChanged();
                this.notifyObservers("Opening XML file failed.  Please choose another XML file.");
                throw err;
            }catch (NumberFormatException err){}
            if((planningRequest.getRequestList().isEmpty())
                    || (planningRequest.getStartingPoint()==null)
                    || (planningRequest.getDepartureTime()==null)){
                resetPlanningRequest();
                this.setChanged();
                this.notifyObservers("Planning is empty. Check your XML file.");
                throw new IOException();
            }
            planningLoaded = true;
            this.setChanged();
        }
    }

    /**
     * Check if the data provided correspond to an existant intersection in the list
     * @param intersectionId
     * @param latitude
     * @param longitude
     * @return res
     */
    public boolean checkUniqueIntersection(long intersectionId,double latitude,double longitude){
        boolean res=true;
        for(Intersection intersection:intersectionList){
            if( intersection.equals(new Intersection(intersectionId,latitude,longitude))){
                res=false;
            }
        }
        return res;
    }

    /**
     * Create new lists of segments and intersections
     */
    @Override
    public void resetMap(){
        segmentList=new ArrayList<>();
        intersectionList=new ArrayList<>();
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Create a new planning request
     */
    @Override
    public void resetPlanningRequest(){
        planningRequest=new PlanningRequest();
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Set the tour to null
     */
    @Override
    public void resetTour(){
        tour=null;
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Set timedOutError to 0
     */
    @Override
    public void resetTimedOutError(){
        this.timedOutError = 0;
    }

    /**
     * Return the intersection corresponding to the id given
     * @param intersectionId
     * @return intersection
     */
    @Override
    public Intersection getIntersectionById(long intersectionId){
        for(Intersection intersection : intersectionList){
            if(intersection.getId() == intersectionId ){
                return intersection;
            }
        }
        return null;
    }

    /**
     * Find the exrem intersections of the map
     * @return extremum
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
            //north
            if(northernmost.getLatitude() < testedIntersection.getLatitude()) {
                northernmost = testedIntersection;
            }
            //south
            if(southernmost.getLatitude() > testedIntersection.getLatitude()) {
                southernmost = testedIntersection;
            }
            //east
            if(easternmost.getLongitude() < testedIntersection.getLongitude()) {
                easternmost = testedIntersection;
            }
            //west
            if(westernmost.getLongitude() > testedIntersection.getLongitude()) {
                westernmost = testedIntersection;
            }
        }
        Intersection[] extremum = {northernmost , southernmost , easternmost , westernmost};
        return extremum;
    }

    /**
     * Give the northern intersection
     * @return extremIntersection[0]
     */
    @Override
    public Intersection getIntersectionNorth(){
        if(!mapLoaded){
            return null;
        }
        return extremIntersection[0];
    };

    /**
     * Give the southern intersection
     * @return extremIntersection[1]
     */
    @Override
    public Intersection getIntersectionSouth(){
        if(!mapLoaded){
            return null;
        }
        return extremIntersection[1];
    };

    /**
     * Give the eastern intersection
     * @return extremIntersection[2]
     */
    @Override
    public Intersection getIntersectionEast(){
        if(!mapLoaded){
            return null;
        }
        return extremIntersection[2];
    };

    /**
     * Give the western intersection
     * @return extremIntersection[3]
     */
    @Override
    public Intersection getIntersectionWest(){
        if(!mapLoaded){
            return null;
        }
        return extremIntersection[3];
    };

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
    public Tour getTour(){
        return this.tour;
    }

    /**
     * @return graphe
     */
    @Override
    public HashMap<Intersection,LinkedList<Segment>> getGraphe() {
        return graphe;
    }

    /**
     * @return timedOutError
     */
    @Override
    public int getTimedOutError() {
        return timedOutError;
    }

    /**
     * @return planningRequest
     */
    @Override
    public PlanningRequest getPlanningRequest()
    {
        return this.planningRequest;
    }

    /**
     * @return mapLoaded
     */
    @Override
    public boolean isMapLoaded() {
        return mapLoaded;
    }

    /**
     * @return planningLoaded
     */
    @Override
    public boolean isPlanningLoaded() {
        return planningLoaded;
    }

    //public boolean isFirstTourComputed() {
    //      return tour != null;
    //}

    /**
     * @return deliveryGraph
     */
    public DeliveryGraph getDeliveryGraph() {
        return deliveryGraph;
    }

    /**
     * Find all shortest path from a starting point
     * @param startIntersection
     * @return pi the precedence table, for each intersection we have the shortest segment to go to another intersection
     */
    public HashMap<Intersection,Segment> dijkstra(Intersection startIntersection){
        HashMap<Intersection,Double> d = new HashMap<>();
        HashMap<Intersection,Segment> pi = new HashMap<>();
        ArrayList<Intersection> blanc= new ArrayList<>(),noir = new ArrayList<>();
        PriorityQueue<Intersection> gris = new PriorityQueue<>(Comparator.comparingDouble(d::get));
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
            Intersection minimalSuccessor = gris.peek();
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
                    gris.add(successor);
                    blanc.remove(successor);
                }
            });
            noir.add(minimalSuccessor);
            gris.remove(minimalSuccessor);
        }
        return pi;
    }

    /**
     * Find an optimal tour
     * @param timeout the timeout (milliseconds)
     */
    @Override
    public void computeTour(int timeout){
        long startTime = System.currentTimeMillis();
        ArrayList<Address> listAddress = this.planningRequest.getListAddress();
        this.deliveryGraph = new DeliveryGraph(listAddress);
        for(int i=0; i<listAddress.size();i++){
            HashMap<Intersection,Segment> pi = dijkstra(listAddress.get(i));
            deliveryGraph.addVertice(i,pi);
        }
        LinkedList<Path> tourCalculated = deliveryGraph.solveTSP(timeout);
        this.timedOutError = deliveryGraph.getTimedOutError();
        tour = new Tour(tourCalculated);
        this.setChanged();
        this.notifyObservers();
        long totalTime = System.currentTimeMillis() - startTime;
        System.out.println("Tour computed in " + totalTime+" ms with a timeout of " + timeout + " ms");
    }

    /**
     * If the time out with computeTour, we can continue to compute an optimal tour
     * @param timeout the timeout (milliseconds)
     */
    @Override
    public void continueTour(int timeout){
        LinkedList<Path> tourCalculated = deliveryGraph.solveTSP(timeout);
        this.timedOutError = deliveryGraph.getTimedOutError();
        tour = new Tour(tourCalculated);
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Add a new request to the previously computed tour
     * @param beforeNewPickup   the address to visit before visiting the new pick up address
     * @param newPickup         the new pick up address
     * @param beforeNewDelivery the address to visit before visiting the new delivery address
     * @param newDelivery       the new delivery address
     */
    @Override
    public void addRequest(Address beforeNewPickup, Address newPickup, Address beforeNewDelivery, Address newDelivery) throws Exception {
        if(!intersectionList.contains(beforeNewPickup) || !intersectionList.contains(newPickup)
                || !intersectionList.contains(beforeNewDelivery) || !intersectionList.contains(newDelivery)){
            this.setChanged();
            this.notifyObservers("Address doesn't exist in the map.");
            throw new Exception();
        }else{
            Request newRequest = new Request(newPickup, newDelivery);
            this.planningRequest.addRequest(newRequest);
            replaceOldPathInTour(beforeNewPickup, newPickup);
            replaceOldPathInTour(beforeNewDelivery, newDelivery);
            this.setChanged();
            this.notifyObservers();
        }
    }

    /**
     * Delete a request from
     * @param pickupOrDelivery
     */
    public void deleteRequest(Address pickupOrDelivery){
        Request requestToRemove = planningRequest.getRequestByAddress(pickupOrDelivery);
        ArrayList<Address> AddressOfRequest= new ArrayList<>();
        AddressOfRequest.add(requestToRemove.getPickupAddress());
        AddressOfRequest.add(requestToRemove.getDeliveryAddress());
        this.planningRequest.removeRequest(requestToRemove);
        if(!this.planningRequest.isEmpty()) {
            for (Address a : AddressOfRequest) {
                Path pathToRemove1 = this.tour.findPathDestination(a);
                Path pathToRemove2 = this.tour.findPathOrigin(a);
                Path newPath = this.findShortestPath(pathToRemove1.getDeparture(), pathToRemove2.getArrival());
                this.tour.replaceOldPaths(pathToRemove1, pathToRemove2, newPath);
            }
        }else{
            this.tour.reset();
        }
        this.setChanged();
        notifyObservers();
    }

    /**
     * Retrieves a list of address necessary for the undo redo functionality
     * @param pickupOrDelivery The pickup or delivery used to retrieve the aforementioned list
     * @return Returns a list of address as follows, the pickup address, the delivery address, the address we visit before
     * going to the pickup address and the address we visit before going to de delivery address
     */
    public List<Address> addressForUndoDelete(Address pickupOrDelivery){
        Request req = planningRequest.getRequestByAddress(pickupOrDelivery);
        Path beforePickupPickup = tour.findPathDestination(req.getPickupAddress());
        Path beforeDeliveryDelivery = tour.findPathDestination(req.getDeliveryAddress());

        ArrayList<Address> answer = new ArrayList<>();
        answer.add(req.getPickupAddress());
        answer.add(req.getDeliveryAddress());
        answer.add(beforePickupPickup.getDeparture());
        answer.add(beforeDeliveryDelivery.getDeparture());

        return answer;
    }

    /**
     * Add a new point of interest to the tour and find the new shortest paths between the origin and the destination
     * @param toVisitBefore
     * @param destination
     */
    private void replaceOldPathInTour(Address toVisitBefore, Address destination) {
        Path oldPath = tour.findPathOrigin(toVisitBefore);
        Path newPath1 = findShortestPath(toVisitBefore, destination);
        Path newPath2 = findShortestPath(destination, oldPath.getArrival());
        tour.replaceOldPath(oldPath, newPath1, newPath2);
    }

    /**
     * Find the sortest path between 2 addresses
     * @param start
     * @param destination
     * @return newPath
     */
    private Path findShortestPath(Address start, Address destination){
        HashMap<Intersection, Segment> pi = dijkstra(start);
        Segment seg = pi.get(destination);
        LinkedList<Segment> newPathComposition = new LinkedList<>();
        Path newPath = new Path(start, destination, newPathComposition);
        newPathComposition.add(seg);
        while (!seg.getOrigin().equals(start)) {
            Intersection s = seg.getOrigin();
            seg = pi.get(s);
            newPathComposition.add(seg);
        }
        Collections.reverse(newPathComposition);
        newPath.setSegmentsOfPath(newPathComposition);
        return newPath;
    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, ParseException {
        Map map = new Map();
        // map.loadMap("./data/fichiersXML2020/largeMap.xml");
        // PlanningRequest planning = new PlanningRequest();
        // map.loadRequest("./data/fichiersXML2020/requestsMedium5.xml");
        // System.out.println("passé");

        //TEST DJIKSTRA
        /*map.loadMap("data/fichiersXML2020/largeMap.xml");
        //map.loadRequest("data/fichiersXML2020/requestsLarge7.xml");
        map.createGraph();
        Intersection inter = map.getIntersectionList().get(0);
        HashMap<Intersection,Segment> testDjikstra = new HashMap<>();
        testDjikstra = map.dijkstra(inter);
        //System.out.println("Test djikstra");
        testDjikstra.forEach((inte, segm)->{
            System.out.println(inte.getId());
        });*/

        map.loadMap("tests/ressource/mapTour.xml");
        map.loadRequest("tests/ressource/requestTour.xml");
        map.computeTour(200000000);
        Tour tour = map.getTour();
        LinkedList<Path> result=tour.getOrderedPathList();
        for(Path path:result){
            System.out.println("Path : "+ path.getDeparture().getId()+"   "+path.getArrival().getId());
            for(Segment seg: path.getSegmentsOfPath()){
                System.out.println(seg.getOrigin().getId()+"   "+seg.getDestination().getId());
            }

        }

        //Test égalité adresse / intersection
        /*HashMap<Address,Intersection> h = new HashMap<>();
        HashMap<Intersection,Address> h2 = new HashMap<>();
        Intersection inter = new Intersection(0,4.75,2.2);
        Intersection int2 = new Intersection(0,4.75,2.2);
        Address address = new Address(0,4.75,2.2,6);
        Address ad2 = new Address(0,4.75,2.2,6);
        //if (address == ad2 ){
        //if (address.equals(inter) ){
        h.put(address,inter);
        Intersection int3 = h.get(int2);
        h2.put(inter,address);
        Address ad3 = h2.get(ad2);
        h2.replace(address,ad2);
        if(inter == int2){
            System.out.println("EGALITE INTERSECTION/ADDRESS REUSSIE");
        }else{
            System.out.println("EGALITE INTERSECTION/ADDRESS ECHOUE");
        }

        //System.out.println(map.getExtremIntersection()[0].getId() +"  "+ map.getExtremIntersection()[1].getId()+"  "+ map.getExtremIntersection()[2].getId()+"  "+ map.getExtremIntersection()[3].getId());
        //System.out.println("passé");*/
    }

}
