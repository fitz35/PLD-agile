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

        Intersection start = new Intersection(1, 1, 1);
        Intersection one = new Intersection(2, 2, 2);
        Intersection two = new Intersection(3, 3, 3);
        Intersection three = new Intersection(4, 4, 4);

        Segment s1 = new Segment(start, two, "seg", 8);
        Segment s2 = new Segment(two, three, "seg", 3);
        Segment s3 = new Segment(three, one, "seg", 1);
        Segment s4 = new Segment(one, start, "seg", 5);

        HashMap<Intersection, Segment> pi = new HashMap<>();
        pi.put(two, s1);
        pi.put(three, s2);
        pi.put(one, s3);
        pi.put(start, s4);

        nodesToVisit = new ArrayList<>();
        /*nodesToVisit.add(start);
        nodesToVisit.add(one);
        nodesToVisit.add(two);
        nodesToVisit.add(three);*/
    }

    @Test
    void construct1 ()
    // Test with well formed list
    {
        deliveryGraph = new DeliveryGraph(nodesToVisit);
        assertEquals(deliveryGraph.getNbVertices(), 4);
    }

    @Test
    void construct2 ()
    // Test with nempty Intersection list
    {
        nodesToVisit = new ArrayList<>();
        deliveryGraph = new DeliveryGraph(nodesToVisit);
        assertEquals(deliveryGraph.getNbVertices(), 0);
    }

   /* @Test
    void construct3 ()
    // Test with null Intersection list
    {
        nodesToVisit = null;
        deliveryGraph = new DeliveryGraph(nodesToVisit);
        assertEquals(deliveryGraph.getNbVertices(), 0);
    }*/

    @Test
    void addVerticeTest() {
    }

    @Test
    void solveTSP() {
    }

    @Test
    void getNbVertices() {
    }

    @Test
    void getCost() {
    }

    @Test
    void isArc() {
    }
}