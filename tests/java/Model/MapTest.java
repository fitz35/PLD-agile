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
        Address pickup = new Address(208769039,45.76069,4.8749375,180);
        Address delivery = new Address(25173820,45.749996,4.858258,240);
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


}