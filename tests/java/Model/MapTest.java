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
        assert(!updateCalled);
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
     * Creation of segment impossible beacause an intersection is missing
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
        assert(!updateCalled);
        assert(map.isMapLoaded());
    }

    //A FAIRE
    //Correct => test les obj générés
    //vide
    //mal formé = miss depot / balise / ..?
    //file inexistant
    //wrong extention
    //reuest en dehors de la map

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
        Intersection pickup = new Intersection(208769039,45.76069, 4.8749375);
        Intersection delivery = new Intersection(25173820,45.749996, 4.858258);
        Request req1 = new Request(pickup,180,delivery,240);
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
    }

    @Test void loadPlanningRequestTest2(){
        //Empty PlanningRequest
        try{
            map.loadMap("data/fichiersXML2020/largeMap.xml");
            map.loadRequest("tests/ressource/request2.xml");
        }catch(Exception e){
            exceptionRaised=true;
        }
        assert(exceptionRaised);
        assert(updateCalled);
    }

    @Test void loadPlanningRequestTest3(){
        //Empty PlanningRequest
        try{
            map.loadMap("data/fichiersXML2020/largeMap.xml");
            map.loadRequest("tests/ressource/request2.xml");
        }catch(Exception e){
            exceptionRaised=true;
        }
        assert(exceptionRaised);
        assert(updateCalled);
    }

    @Test
    void getExtremIntersectionTest(){

    }


}