package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryGraphTest {

    private boolean exceptionRaised;
    private DeliveryGraph deliveryGraph;
    private ArrayList<Address> nodesToVisit;

    @BeforeEach
    void setUp() {
        exceptionRaised=false;

        Address inter0=new Address(0,4,1.0,0,0);
        Address inter1=new Address(1,6,2.5,180,1);
        Address inter2=new Address(2,6,4,180,1);
        Address inter3=new Address(3,4,4,240,2);
        Address inter4=new Address(4,4,2.5,240,2);

        nodesToVisit = new ArrayList<>();
        nodesToVisit.add(inter0);
        nodesToVisit.add(inter1);
        nodesToVisit.add(inter2);
        nodesToVisit.add(inter3);
        nodesToVisit.add(inter4);
    }

    /**
     * Test with well formed list
     */
    @Test
    void constructorTest1 (){
        deliveryGraph = new DeliveryGraph(nodesToVisit);
        assertEquals(deliveryGraph.getNbVertices(), 5);
    }

    /**
     * Test with an empty Intersection list
     */
    @Test
    void constructTest2 (){
        nodesToVisit = new ArrayList<>();
        deliveryGraph = new DeliveryGraph(nodesToVisit);
        assertEquals(deliveryGraph.getNbVertices(), 0);
    }

    /**
     * Test with null Intersection list
     */
   @Test
    void constructTest3 (){
        nodesToVisit = null;
        deliveryGraph = new DeliveryGraph(nodesToVisit);
        assertEquals(deliveryGraph.getNbVertices(), 0);
    }

    /**
     * Test the cost from 0 to each vertex
     */
    @Test
    void getCostTest1() {
        deliveryGraph = new DeliveryGraph(nodesToVisit);
        deliveryGraph.addVertice(0, piDeliveryGraphe());
        assertEquals(-1,deliveryGraph.getCost(0,0));
        assertEquals(7,deliveryGraph.getCost(0,1));
        assertEquals(3,deliveryGraph.getCost(0,2));
        assertEquals(8,deliveryGraph.getCost(0,3));
        assertEquals(1,deliveryGraph.getCost(0,4));
    }

    /**
     * Test the cost from 1 to another vertex, but 1 is not the starting point
     */
    @Test
    void getCostTest2() {
        deliveryGraph = new DeliveryGraph(nodesToVisit);
        deliveryGraph.addVertice(0, piDeliveryGraphe());
        assertEquals(0,deliveryGraph.getCost(1,0));
    }

    private HashMap<Intersection,Segment> piDeliveryGraphe(){
        HashMap<Intersection,Segment> theoricalResult=new HashMap<>();
        Intersection inter0=new Intersection(0,4,1);
        Intersection inter1=new Intersection(1,6,2.5);
        Intersection inter2=new Intersection(2,6,4);
        Intersection inter3=new Intersection(3,4,4);
        Intersection inter4=new Intersection(4,4,2.5);

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