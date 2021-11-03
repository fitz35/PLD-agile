package Model;

import tsp.Graph;
import tsp.TSP1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class DeliveryGraph implements Graph{
    private double [][] cost;
    private ArrayList<Intersection> nodesToVisit;
    private HashMap<Pair<Intersection, Intersection> , LinkedList<Segment>> verticeCompositionList;
    private int nbVertices;
    private int timedOutError = 0;

    public DeliveryGraph(ArrayList<Intersection> nodesToVisit) {
        this.verticeCompositionList = new HashMap<>();
        this.nodesToVisit = nodesToVisit;
        this.nbVertices = nodesToVisit.size();
        this.cost = new double[this.nbVertices][this.nbVertices];
    }

    public void addVertice(int numberStartNode, HashMap<Intersection, Segment> pi){
        int numberDestinationNode = 0;
        Intersection startIntersection = nodesToVisit.get(numberStartNode);
        for(Intersection intersect : nodesToVisit){
            if(intersect != startIntersection) {
                Segment seg = pi.get(intersect);
                Pair<Intersection, Intersection> newVertice = new Pair<>(startIntersection, intersect);
                LinkedList<Segment> newVerticeCompositon = new LinkedList<>();
                Double length = seg.getLength();
                newVerticeCompositon.add(seg);
                while (seg.getOrigin() != startIntersection && seg != null) {
                    seg = pi.get(seg.getOrigin());
                    newVerticeCompositon.add(seg);
                    length += seg.getLength();
                }
                Collections.reverse(newVerticeCompositon);
                verticeCompositionList.put(newVertice, newVerticeCompositon);
                cost[numberStartNode][numberDestinationNode] = length;
                numberDestinationNode++;
            }
        }
    }

    public LinkedList<Segment> solveTSP (int timeout){
        TSP1 tsp = new TSP1();
        this.timedOutError = tsp.searchSolution(timeout, this);
        //System.out.print("Solution of cost "+tsp.getSolutionCost());
        LinkedList<Segment> result = new LinkedList<>();
        LinkedList<Segment> intermediateResult;
        Pair<Intersection, Intersection> currentVertice;
        for (int i=0; i<nbVertices; i++) {
            currentVertice = new Pair<Intersection, Intersection>
                    (nodesToVisit.get(tsp.getSolution(i)), nodesToVisit.get(tsp.getSolution((i+1)%nbVertices)));
            intermediateResult = verticeCompositionList.get(currentVertice);
            for(Segment currentSegment : intermediateResult){
                result.add(currentSegment);
            }
        }
        return result;
    }

    public int getTimedOutError() {
        return timedOutError;
    }

    @Override
    public int getNbVertices() {
        return this.nbVertices;
    }

    @Override
    public int getCost(int i, int j) {
        if (i<0 || i>=nbVertices || j<0 || j>=nbVertices)
            return -1;
        return (int) cost[i][j];
    }

    @Override
    public boolean isArc(int i, int j) {
        if (i<0 || i>=nbVertices || j<0 || j>=nbVertices)
            return false;
        return i != j;
    }

    public static void main(String[] args){
        Intersection start = new Intersection(1, 1, 1);
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

        ArrayList<Intersection> nodesToVisit = new ArrayList<>();
        nodesToVisit.add(start);
        nodesToVisit.add(one);
        nodesToVisit.add(two);
        nodesToVisit.add(three);

        DeliveryGraph dg = new DeliveryGraph(nodesToVisit);
        for(int i=0; i< 4; i++) {
            dg.addVertice(i, pi);
        }
        dg.solveTSP(2000000);

    }
}
