package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MapTest extends Observable {
    private Map map;
    private boolean exceptionRaised;
    private Observer observer;
    private boolean updateCalled;

    @BeforeEach
    void setUp() {
        map= new Map();
        exceptionRaised=false;
        observer=new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                updateCalled=true;
                //System.out.println(arg);
            }
        };
        map.addObserver(observer);
    }

    /**
     * Check all intersections, segment and graphe created with loadMap
     */
    @Test
    void loadMapTest1(){
        try{
            map.loadMap("tests/ressource/map1.xml");
        }catch(Exception e){
            exceptionRaised=true;
        }

        Intersection intersectionOrigin = new Intersection(975886496,45.756874,4.8574047);
        Intersection intersectionDestination = new Intersection(26033277,45.756165,4.8574095);
        Segment segmentTest=new Segment(intersectionOrigin,intersectionDestination,"Rue Danton",78.72686);

        ArrayList<Intersection> intersectionList=new ArrayList<>();
        intersectionList.add(intersectionOrigin);
        intersectionList.add(intersectionDestination);

        ArrayList<Segment> segmentList=new ArrayList<>();
        segmentList.add(segmentTest);

        HashMap<Intersection, LinkedList<Segment>> graphe=new HashMap<>();

        for (Intersection inter : intersectionList) {
            LinkedList<Segment> interSegments = new LinkedList<>();
            Long intersectionID = inter.getId();
            for (Segment segment : segmentList) {
                Long segmentOriginId = segment.getOrigin().getId();
                if (segmentOriginId.equals(intersectionID)) {
                    interSegments.add(segment);
                    Intersection segmentDest = segment.getDestination();
                }
            }
            graphe.put(inter, interSegments);
        }

        assertEquals(graphe,map.getGraphe());
        assert(intersectionOrigin.equals(map.getIntersectionList().get(0)));
        assert(intersectionDestination.equals(map.getIntersectionList().get(1)));
        assert(segmentTest.equals((map.getSegmentList().get(0))));
        assert(!exceptionRaised);
        assert(updateCalled);
        assert(map.isMapLoaded());
    }

    /**
     * Check if the file exists
     */
    @Test
    void loadMapTest2(){
        try{
            map.loadMap("tests/ressource/map.xml");
        }catch(Exception e){
            exceptionRaised=true;
        }
        assert(exceptionRaised);
        assert(updateCalled);
        assert(!map.isMapLoaded());
    }

    /**
     * Check the result of a wrong parsing : wrong document root tag name
     */
    @Test
    void loadMapTest3(){
        try{
            map.loadMap("tests/ressource/map2.xml");
        }catch(Exception e){
            exceptionRaised=true;
        }
        assert(exceptionRaised);
        assert(updateCalled);
        assert(!map.isMapLoaded());
    }

    /**
     * Check the result of a wrong parsing : wrong tag "int"
     */
    @Test
    void loadMapTest4(){
        try{
            map.loadMap("tests/ressource/map3.xml");
        }catch(Exception e){
            exceptionRaised=true;
        }
        assert(exceptionRaised);
        assert(updateCalled);
        assert(!map.isMapLoaded());
    }

    /**
     * Check the result of a wrong parsing : wrong tag "seg"
     */
    @Test
    void loadMapTest5(){
        try{
            map.loadMap("tests/ressource/map4.xml");
        }catch(Exception e){
            exceptionRaised=true;
        }
        assert(exceptionRaised);
        assert(updateCalled);
        assert(!map.isMapLoaded());
    }

    /**
     * Check the result of a wrong extension
     */
    @Test
    void loadMapTest6(){
        try{
            map.loadMap("tests/ressource/map4.test.pdf");
        }catch(Exception e){
            exceptionRaised=true;
        }
        assert(exceptionRaised);
        assert(updateCalled);
        assert(!map.isMapLoaded());
    }

    /**
     * Creation of segment impossible because an intersection is missing
     */
    @Test
    void loadMapTest7(){
        try{
            map.loadMap("tests/ressource/map5.xml");
        }catch(Exception e){
            exceptionRaised=true;
        }
        assert(exceptionRaised);
        assert(updateCalled);
        assert(!map.isMapLoaded());
    }

    /**
     * Check the result when we have 2 intersections equals
     */
    @Test
    void loadMapTest8(){
        try{
            map.loadMap("tests/ressource/map6.xml");
        }catch(Exception e){
            exceptionRaised=true;
        }
        assertEquals(2,map.getIntersectionList().size());
        assert(!exceptionRaised);
        assert(updateCalled);
        assert(map.isMapLoaded());
    }

    /**
     * Check the result when we have an empty xml file
     */
    @Test
    void loadMapTest9(){
        try{
            map.loadMap("tests/ressource/empty.xml");
        }catch(Exception e){
            exceptionRaised=true;
        }
        assert(exceptionRaised);
        assert(updateCalled);
        assert(!map.isMapLoaded());
    }

    /**
     * Check the result when we have a correct planning request file given
     */
    @Test
    void loadPlanningRequestTest1(){
        //Well formed PlanningRequest
        try{
            map.loadMap("data/fichiersXML2020/largeMap.xml");
            assert(map.isMapLoaded());
            map.loadRequest("tests/ressource/request1.xml");
            assert(map.isPlanningLoaded());
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("error "+e);
            exceptionRaised=true;
        }
        assert(!exceptionRaised);
        PlanningRequest planingTest = new PlanningRequest();
        Address pickup = new Address(208769039,45.76069,4.8749375,180, 1 /*for pickup*/);
        Address delivery = new Address(25173820,45.749996,4.858258,240, 2 /*for delivery*/);
        Request req1 = new Request(pickup,delivery);
        ArrayList<Request> requestList = new ArrayList<Request>();
        requestList.add(req1);
        planingTest.setRequestList(requestList);
        try {
            Date departureTime = new SimpleDateFormat("HH:mm:ss").parse("08:00:00");
            planingTest.setDepartureTime(departureTime);
        } catch (ParseException e) {
            exceptionRaised=true;
        }
        assert(!exceptionRaised);
        Intersection startingPoint = new Intersection(342873658,45.76038,4.8775625);
        planingTest.setStartingPoint(startingPoint);
        assert(planingTest.equals(map.getPlanningRequest()));
        assert(updateCalled);
    }
    /**
     * Check the result when we have an empty xml file
     */
    @Test
    void loadPlanningRequestTest2(){
        try{
            map.loadMap("data/fichiersXML2020/largeMap.xml");
            assert(map.isMapLoaded());
            map.loadRequest("tests/ressource/empty.xml");
        }catch(Exception e){
            exceptionRaised=true;
        }
        assertTrue(map.getPlanningRequest().getRequestList().isEmpty());
        assertNull(map.getPlanningRequest().getStartingPoint());
        assertNull(map.getPlanningRequest().getDepartureTime());
        assert(!map.isPlanningLoaded());
        assert(exceptionRaised);
        assert(updateCalled);
    }


    /**
     * Check the result when we have a file with an extension different than xml
     */
    @Test
    void loadPlanningRequestTest3(){
        try{
            map.loadMap("data/fichiersXML2020/largeMap.xml");
            assert(map.isMapLoaded());
            map.loadRequest("tests/ressource/request.pdf");
        }catch(Exception e){
            exceptionRaised=true;
        }
        assertTrue(map.getPlanningRequest().getRequestList().isEmpty());
        assertNull(map.getPlanningRequest().getStartingPoint());
        assertNull(map.getPlanningRequest().getDepartureTime());
        assert(!map.isPlanningLoaded());
        assert(exceptionRaised);
        assert(updateCalled);
    }

    /**
     * Check the result when we have an existant file
     */
    @Test
    void loadPlanningRequestTest4(){
        try{
            map.loadMap("data/fichiersXML2020/largeMap.xml");
            assert(map.isMapLoaded());
            map.loadRequest("tests/ressource/request.xml");
        }catch(Exception e){
            exceptionRaised=true;
        }
        assertTrue(map.getPlanningRequest().getRequestList().isEmpty());
        assertNull(map.getPlanningRequest().getStartingPoint());
        assertNull(map.getPlanningRequest().getDepartureTime());
        assert(!map.isPlanningLoaded());
        assert(exceptionRaised);
        assert(updateCalled);
    }

    /**
     * Check the result when we have a root tag different than "planningRequest"
     */
    @Test
    void loadPlanningRequestTest5(){
        try{
            map.loadMap("data/fichiersXML2020/largeMap.xml");
            assert(map.isMapLoaded());
            map.loadRequest("tests/ressource/request3.xml");
        }catch(Exception e){
            exceptionRaised=true;
        }
        assertTrue(map.getPlanningRequest().getRequestList().isEmpty());
        assertNull(map.getPlanningRequest().getStartingPoint());
        assertNull(map.getPlanningRequest().getDepartureTime());
        assert(!map.isPlanningLoaded());
        assert(exceptionRaised);
        assert(updateCalled);
    }

    /**
     * Check the result when we have a tag different than "depot"
     */
    @Test
    void loadPlanningRequestTest6(){
        try{
            map.loadMap("data/fichiersXML2020/largeMap.xml");
            assert(map.isMapLoaded());
            map.loadRequest("tests/ressource/request4.xml");
        }catch(Exception e){
            exceptionRaised=true;
        }
        assertTrue(map.getPlanningRequest().getRequestList().isEmpty());
        assertNull(map.getPlanningRequest().getStartingPoint());
        assertNull(map.getPlanningRequest().getDepartureTime());
        assert(!map.isPlanningLoaded());
        assert(exceptionRaised);
        assert(updateCalled);
    }

    /**
     * Check the result when we have a tag different than "request"
     */
    @Test
    void loadPlanningRequestTest7(){
        try{
            map.loadMap("data/fichiersXML2020/largeMap.xml");
            assert(map.isMapLoaded());
            map.loadRequest("tests/ressource/request5.xml");
        }catch(Exception e){
            exceptionRaised=true;
        }
        assertTrue(map.getPlanningRequest().getRequestList().isEmpty());
        assertNull(map.getPlanningRequest().getStartingPoint());
        assertNull(map.getPlanningRequest().getDepartureTime());
        assert(!map.isPlanningLoaded());
        assert(exceptionRaised);
        assert(updateCalled);
    }

    /**
     * Check the result when we have a request with an unknown intersection
     */
    @Test
    void loadPlanningRequestTest8(){
        try{
            map.loadMap("data/fichiersXML2020/largeMap.xml");
            assert(map.isMapLoaded());
            map.loadRequest("tests/ressource/request6.xml");
        }catch(Exception e){
            exceptionRaised=true;
        }
        assertTrue(map.getPlanningRequest().getRequestList().isEmpty());
        assertNull(map.getPlanningRequest().getStartingPoint());
        assertNull(map.getPlanningRequest().getDepartureTime());
        assert(!map.isPlanningLoaded());
        assert(exceptionRaised);
        assert(updateCalled);
    }

    @Test
    void getExtremIntersectionTest(){

    }

    /**
     * Test the efficiency of dijkstra
     */
    @Test
    void dijkstraTest1(){
        try {
            map.loadMap("data/fichiersXML2020/largeMap.xml");
        } catch (Exception e) {
            e.printStackTrace();
            exceptionRaised=true;
        }
        assert(!exceptionRaised);
        assert(map.isMapLoaded());
        long start=System.currentTimeMillis();
        HashMap<Intersection,Segment> calculatedResult=map.dijkstra(map.getIntersectionList().get(0));
        long end=System.currentTimeMillis();
        assert((end-start)<300);
    }

    /**
     * Test the result given by dijkstra on a small map
     */
    @Test
    void dijkstraTest2(){
        try {
            map.loadMap("tests/ressource/dijkstraMap.xml");
        } catch (Exception e) {
            e.printStackTrace();
            exceptionRaised=true;
        }
        assert(!exceptionRaised);
        assert(map.isMapLoaded());

        HashMap<Intersection,Segment> theoricalResult=createMapDijkstra();
        HashMap<Intersection,Segment> calculatedResult=map.dijkstra(new Intersection(0,4.75,3.0));
        assertEquals(theoricalResult,calculatedResult);
        // http://graphonline.ru/fr/?graph=IOoCFszcSchRFjSB
    }

    private HashMap<Intersection,Segment> createMapDijkstra(){
        HashMap<Intersection,Segment> theoricalResult=new HashMap<>();
        Intersection inter0=new Intersection(0,4.75,3.0);
        Intersection inter1=new Intersection(1,4.7,3.6);
        Intersection inter2=new Intersection(2,4.6,4.5);
        Intersection inter3=new Intersection(3,3.75,2.3);
        Intersection inter4=new Intersection(4,3.80,3.7);

        Segment seg01=new Segment(inter0,inter1,"Rue d'Arménie",8);
        Segment seg02=new Segment(inter0,inter2,"Rue Vendôme",3);
        Segment seg04=new Segment(inter0,inter4,"Rue Bonnefoi",1);

        Segment seg10=new Segment(inter1,inter0,"Rue Ancienne",6);
        Segment seg12=new Segment(inter1,inter2,"Rue Neuve",3);
        Segment seg14=new Segment(inter1,inter4,"Rue d'Anvers",2);

        Segment seg21=new Segment(inter2,inter1,"Rue Montesquieu",5);
        Segment seg24=new Segment(inter2,inter4,"Avenue Jean Jaurès",2);

        Segment seg32=new Segment(inter3,inter2,"Rue D'or",4);
        Segment seg34=new Segment(inter3,inter4,"Rue Saint-Michel",2);

        Segment seg40=new Segment(inter4,inter0,"Rue Sébastien Gryphe",3);
        Segment seg41=new Segment(inter4,inter1,"Rue Béchevelin",6);
        Segment seg42=new Segment(inter4,inter2,"Route de Vienne",2);
        Segment seg43=new Segment(inter4,inter3,"Rue Saint-Michel",7);

        theoricalResult.put(inter0,null);
        theoricalResult.put(inter1,seg41);
        theoricalResult.put(inter2,seg02);
        theoricalResult.put(inter3,seg43);
        theoricalResult.put(inter4,seg04);
        return theoricalResult;
    }

    /**
     * Verify the result of the method checkUniqueIntersection when an intersection already exist
     */
    @Test
    void checkUniqueIntersectionTest1(){
        try{
            map.loadMap("tests/ressource/map1.xml");
            assert(map.isMapLoaded());
        }catch (Exception e){
            exceptionRaised=true;
        }
        assert(!exceptionRaised);
        boolean test=map.checkUniqueIntersection(975886496,45.756874,4.8574047);
        assert(!test);
    }

    /**
     * Verify the result of the method checkUniqueIntersection when an intersection don't exist
     */
    @Test
    void checkUniqueIntersectionTest2(){
        try{
            map.loadMap("tests/ressource/map1.xml");
            assert(map.isMapLoaded());
        }catch (Exception e){
            exceptionRaised=true;
        }
        assert(!exceptionRaised);
        boolean test=map.checkUniqueIntersection(97588649,45.75684,4.84047);
        assert(test);
    }

    /**
     * Verify the result of the method getIntersectionById when an intersection already exist
     */
    @Test
    void getIntersectionByIdTest1(){
        try{
            map.loadMap("tests/ressource/map1.xml");
            assert(map.isMapLoaded());
        }catch (Exception e){
            exceptionRaised=true;
        }
        assert(!exceptionRaised);
        Intersection theoricalResult=new Intersection(975886496,45.756874,4.8574047);
        Intersection realResult=map.getIntersectionById(975886496);
        assertEquals(theoricalResult,realResult);
    }

    /**
     * Verify the result of the method getIntersectionById when an intersection don't exist
     */
    @Test
    void getIntersectionByIdTest2(){
        try{
            map.loadMap("tests/ressource/map1.xml");
            assert(map.isMapLoaded());
        }catch (Exception e){
            exceptionRaised=true;
        }
        assert(!exceptionRaised);
        Intersection realResult=map.getIntersectionById(9);
        assertNull(realResult);
    }

    /**
     * Verify the result extrem intersections
     */
    @Test
    void getExtremIntersectionTest1(){
        try{
            map.loadMap("tests/ressource/mapExtremIntersection1.xml");
            assert(map.isMapLoaded());
        }catch (Exception e){
            exceptionRaised=true;
        }
        assert(!exceptionRaised);
        Intersection theroricalNorth=new Intersection(0,45,30);
        Intersection theroricalSouth=new Intersection(1,20,30);
        Intersection theroricalWest=new Intersection(2,30,20);
        Intersection theroricalEast=new Intersection(3,30,45);
        assertEquals(theroricalNorth,map.getIntersectionNorth());
        assertEquals(theroricalSouth,map.getIntersectionSouth());
        assertEquals(theroricalWest,map.getIntersectionWest());
        assertEquals(theroricalEast,map.getIntersectionEast());
    }

    /**
     * Verify the efficiency to compute a tour : less than 10 000ms on a largeMap.xml with requestsLarge7.xml
     */
    @Test
    void computeTourTest1(){
        try{
            map.loadMap("data/fichiersXML2020/largeMap.xml");
            assert(map.isMapLoaded());
            map.loadRequest("data/fichiersXML2020/requestsLarge7.xml");
            assert(map.isPlanningLoaded());
        }catch (Exception e){
            e.printStackTrace();
            exceptionRaised=true;
        }
        assert(!exceptionRaised);
        map.computeTour(10000);
        assert(map.getTimedOutError()==0);
    }

    /**
     * Verify the tour computed : interest vertex are taken in the ascending order (not optimized)
     */
    @Test
    void computeTourTest2(){
        try{
            map.loadMap("tests/ressource/mapTour.xml");
            assert(map.isMapLoaded());
            map.loadRequest("tests/ressource/requestTour.xml");
            assert(map.isPlanningLoaded());
        }catch (Exception e){
            e.printStackTrace();
            exceptionRaised=true;
        }
        assert(!exceptionRaised);
        LinkedList<Path> theoricalTour=createTheoricalTour();
        map.computeTour(200);
        assert(map.getTimedOutError()==0);
        LinkedList<Path> calculatedTour=map.getTour().getOrderedPathList();
        assertEquals(theoricalTour,calculatedTour);
    }

    private LinkedList<Path> createTheoricalTour(){
        Address inter0=new Address(new Intersection(0,4,1.0),0);
        Address inter1=new Address(new Intersection(1,6,2.5),180);
        Address inter2=new Address(new Intersection(2,6,4),180);
        Address inter3=new Address(new Intersection(3,4,4),240);
        Address inter4=new Address(new Intersection(4,4,2.5),240);

        Segment seg01=new Segment(inter0,inter1,"Rue d'Arménie",8);
        Segment seg02=new Segment(inter0,inter2,"Rue Vendôme",3);
        Segment seg04=new Segment(inter0,inter4,"Rue Bonnefoi",1);

        Segment seg10=new Segment(inter1,inter0,"Rue Ancienne",6);
        Segment seg12=new Segment(inter1,inter2,"Rue Neuve",3);
        Segment seg14=new Segment(inter1,inter4,"Rue d'Anvers",2);

        Segment seg21=new Segment(inter2,inter1,"Rue Montesquieu",5);
        Segment seg24=new Segment(inter2,inter4,"Avenue Jean Jaurès",2);

        Segment seg32=new Segment(inter3,inter2,"Rue D'or",4);
        Segment seg34=new Segment(inter3,inter4,"Rue Saint-Michel",2);

        Segment seg40=new Segment(inter4,inter0,"Rue Sébastien Gryphe",3);
        Segment seg41=new Segment(inter4,inter1,"Rue Béchevelin",6);
        Segment seg42=new Segment(inter4,inter2,"Route de Vienne",2);
        Segment seg43=new Segment(inter4,inter3,"Rue Saint-Michel",7);


        LinkedList<Path> theoricalTour=new LinkedList<>();
        LinkedList<Segment> intermediate=new LinkedList<>();

        intermediate.add(seg04);
        intermediate.add(seg41);
        Path path1=new Path(inter0,inter1,intermediate);
        theoricalTour.add(path1);

        intermediate=new LinkedList<>();
        intermediate.add(seg12);
        Path path2=new Path(inter1,inter2,intermediate);
        theoricalTour.add(path2);

        intermediate=new LinkedList<>();
        intermediate.add(seg24);
        intermediate.add(seg43);
        Path path3=new Path(inter2,inter3,intermediate);
        theoricalTour.add(path3);

        intermediate=new LinkedList<>();
        intermediate.add(seg34);
        Path path4=new Path(inter3,inter4,intermediate);
        theoricalTour.add(path4);

        intermediate=new LinkedList<>();
        intermediate.add(seg40);
        Path path5=new Path(inter4,inter0,intermediate);
        theoricalTour.add(path5);

        return theoricalTour;
    }
}