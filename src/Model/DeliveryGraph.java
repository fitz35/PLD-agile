package Model;

import java.util.HashMap;
import java.util.LinkedList;

public class DeliveryGraph {
    private HashMap<Intersection, HashMap<Intersection, Segment>> adjacencyList;
    private HashMap<Intersection [] , HashMap<Intersection, Segment>> verticeCompositionList;

    public DeliveryGraph() {
        this.adjacencyList = new HashMap<Intersection, HashMap<Intersection, Segment>>();
        this.verticeCompositionList = new HashMap<Intersection [] , HashMap<Intersection, Segment>>();
    }
}
