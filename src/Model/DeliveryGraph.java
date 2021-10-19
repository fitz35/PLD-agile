package Model;

import java.util.HashMap;
import java.util.LinkedList;

public class DeliveryGraph {
    private HashMap<Intersection, HashMap<Intersection, Segment>> adjacencyList;
    private HashMap<Pair<Intersection, Intersection> , LinkedList<Segment>> verticeCompositionList;

    public DeliveryGraph() {
        this.adjacencyList = new HashMap<Intersection, HashMap<Intersection, Segment>>();
        this.verticeCompositionList = new HashMap<Pair<Intersection, Intersection> , LinkedList<Pair<Intersection, Segment>>>();
    }

    public void addVertice(Intersection startIntersect, HashMap<Intersection, Segment> pi, LinkedList<Intersection> intersectionList){
        
    }
}
