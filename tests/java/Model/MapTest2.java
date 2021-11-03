package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MapTest2 extends Observable {
    private Map map;
    private boolean exceptionRaised;
    private Observer observer;
    private boolean updateCalled = false;


    //Correct => test les obj générés
    //vide
    //mal formé = miss depot / balise / ..?
    //file inexistant
    //wrong extention
    //reuest en dehors de la map

    @BeforeEach
    void setUp() {
        observer = new Observer(){
            @Override
            public void update(Observable o, Object arg){
                updateCalled = true;
            }
        };
        map= new Map();
        map.addObserver(observer);
        exceptionRaised=false;
    }

    @Test void loadPlanningRequestTest1(){
        //Well formed PlanningRequest
        try{
            map.loadMap("data/fichiersXML2020/largeMap.xml");
            map.loadRequest("tests/ressource/request1.xml");
        }catch(Exception e){
            System.out.println("error "+e);
            exceptionRaised=true;
        }

        Intersection pickup = new Intersection(208769039,45.76069, 4.8749375);
        Intersection delivery = new Intersection(208769039,45.749996, 4.858258);
        Request req1 = new Request(pickup,180,delivery,240);
        ArrayList<Request> requestList = new ArrayList<Request>();
        requestList.add(req1);
        try {
            Date departureTime = new SimpleDateFormat("HH:mm:ss").parse("8:0:0");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Intersection startingPoint = new Intersection(342873658,45.76038,4.8775625);
        PlanningRequest planingTest = new PlanningRequest();

        assert(planingTest.equals(map.getPlanningRequest()));


        assert(!exceptionRaised);
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