package tsp;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class TSPTest {

    @Test
    void tspTest1(){
        int nbVertices=19;
        TSP tsp = new TSP1();
        System.out.println("Graphs with "+nbVertices+" vertices:");
        Graph g = new CompleteGraph(nbVertices);
        long startTime = System.currentTimeMillis();
        tsp.searchSolution(200000000, g);
        long endTime=System.currentTimeMillis();
        System.out.print("Solution of cost "+tsp.getSolutionCost()+" found in "+(endTime - startTime)+"ms");
        LinkedList<Integer> theoricalResult=new LinkedList<>(Arrays.asList(0,7,13,5,8,14,1,6,9,17,3,15,18,10,11,16,12,2,4,0));
        LinkedList<Integer> realResult=new LinkedList<>();
        for (int i=0; i<nbVertices; i++){
            realResult.add(tsp.getSolution(i));
        }
        realResult.add(0);
        assertEquals(theoricalResult,realResult);
        assert((endTime-startTime)<10000);
    }
}