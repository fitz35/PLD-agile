package Model;

import tsp.Graph;
import tsp.TSP1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 *This class is DeliveryGraph
 * the graph obtained after applying the dijkstra algorithm
 * on all the intersections of the map
 * implements Graph interface from tsp package
 * @version
 * @author Hexanome 4124
 */
public class DeliveryGraph implements Graph{

    /**
     * the cost from a node to another
     */
    private double [][] cost;
    /**
     * the pickup and delivery addresses to visit
     */
    private ArrayList<Address> nodesToVisit;
    /**
     * the path composition of delivery graph
     */
    private LinkedList<Path> verticeCompositionList;
    private int nbVertices;
    private int timedOutError = 0;

    /**
     * Constructor
     * @param nodesToVisit
     */
    public DeliveryGraph(ArrayList<Address> nodesToVisit) {
        this.verticeCompositionList = new LinkedList<>();
        this.nodesToVisit = nodesToVisit;
        if(nodesToVisit!=null){
            this.nbVertices = nodesToVisit.size();
        }else{
            this.nbVertices=0;
        }

        this.cost = new double[this.nbVertices][this.nbVertices];
        for(int i = 0; i<cost.length; i++){
            cost[i][i] = -1;
        }
    }

    /**
     * add new path to the verticeCompositionList from the address of "numberStartNode"
     * using the hashmap obtained after applying Dijkstra algorithm
     * @param numberStartNode
     * @param pi precedence hashMap computed by the dijkstra algorithm
     */
    public void addVertice(int numberStartNode, HashMap<Intersection, Segment> pi){
        int numberDestinationNode = 0;
        Address startIntersection = nodesToVisit.get(numberStartNode);
        for(Address intersect : nodesToVisit){
            if(!intersect.equals(startIntersection)) {
                Segment seg = pi.get(intersect);
                //Pair<Intersection, Intersection> newVertice = new Pair<>(startIntersection, intersect);
                LinkedList<Segment> newVerticeCompositon = new LinkedList<>();
                Path newVertice = new Path(startIntersection, intersect, newVerticeCompositon);
                Double length = seg.getLength();
                newVerticeCompositon.add(seg);
                while (!seg.getOrigin().equals(startIntersection)) {
                    Intersection s = seg.getOrigin();
                    //seg = pi.get(seg.getOrigin());
                    seg = pi.get(s);
                    newVerticeCompositon.add(seg);
                    length += seg.getLength();
                }
                Collections.reverse(newVerticeCompositon);
                newVertice.setSegmentsOfPath(newVerticeCompositon);
                verticeCompositionList.add(newVertice);
                cost[numberStartNode][numberDestinationNode] = length;
            }
            numberDestinationNode++;
        }
    }

    /**
     * return the tour in the form of linked list without exceeding the timeout
     * @param timeout (in seconds)
     * @return "the path to follow"
     */
    public LinkedList<Path> solveTSP (int timeout){
        TSP1 tsp = new TSP1();
        this.timedOutError = tsp.searchSolution(timeout, this);
        //System.out.print("Solution of cost "+tsp.getSolutionCost());
        LinkedList<Path> result = new LinkedList<>();
        Path currentPath = new Path();
        LinkedList<Segment> intermediateResult = new LinkedList<>();
        for (int i=0; i<nbVertices; i++) {
            currentPath.setDeparture(nodesToVisit.get(tsp.getSolution(i)));
            currentPath.setArrival(nodesToVisit.get(tsp.getSolution((i+1)%nbVertices)));
            int j = verticeCompositionList.indexOf(currentPath);
            result.add(verticeCompositionList.get(j));
        }
        return result;
    }

    public int getTimedOutError() {
        return timedOutError;
    }

    @Override
    public int getNbVertices() {
        return nbVertices;
    }

    /**
     * get the VerticeCompositionList
     * @return
     */
    public LinkedList<Path> getVerticeCompositionList() {
        return verticeCompositionList;
    }

    /**
     * the cost from node i to node j
     * @param i
     * @param j
     * @return
     */
    @Override
    public int getCost(int i, int j) {
        if ((i<0) || (i>=nbVertices) || (j<0) || (j>=nbVertices)){
            return -1;
        }
        return (int) cost[i][j];
    }

    @Override
    public boolean isArc(int i, int j) {
        if ((i<0) || (i>=nbVertices) || (j<0) || (j>=nbVertices)){
            return false;
        }
        return i != j;
    }

    public static void main(String[] args){
        /*Intersection start = new Intersection(1, 1, 1);
        Intersection one = new Intersection(2, 2, 2);
        Intersection two = new Intersection(3, 3, 3);
        Intersection three = new Intersection(4, 4, 4);

        Segment s1 = new Segment(start, two, "lol", 8);
        Segment s2 = new Segment(two, three, "lol", 3);
        Segment s3 = new Segment(three, one, "lol", 1);
        Segment s4 = new Segment(one, start, "lol", 5);

        HashMap<Intersection, Segment> pi = new HashMap<>();
        pi.put(two, s1);
        pi.put(three, s2);
        pi.put(one, s3);
        pi.put(start, s4);

        ArrayList<Address> nodesToVisit = new ArrayList<>();
        nodesToVisit.add(new Address(start,100));
        nodesToVisit.add(new Address(one,100));
        nodesToVisit.add(new Address(two,100));
        nodesToVisit.add(new Address(three,100));

        DeliveryGraph dg = new DeliveryGraph(nodesToVisit);
        for(int i=0; i< 4; i++) {
            dg.addVertice(i, pi);
        }
        LinkedList<Path> result=dg.solveTSP(2000000);
        for(Path path:result){
            System.out.println(path.getDeparture().getId()+"   "+path.getArrival().getId());
        }*/
    }
}
