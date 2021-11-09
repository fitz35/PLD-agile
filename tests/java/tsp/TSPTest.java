package tsp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class TSPTest {
    /*private Graph completeGraph;
    private ArrayList<int []> solutions;

    @BeforeEach
    void initAnswers(){
        solutions = new ArrayList<>(9);

        int [] solution3V  = {0, 1, 2, 0};
        int [] solution5V  = {0, 1, 3, 2, 4, 0};
        int [] solution7V  = {0, 1, 3, 5, 2, 4, 6, 0};
        int [] solution9V  = {0, 7, 3, 1, 4, 5, 2, 8, 6, 0};
        int [] solution11V = {0, 1, 7, 8, 9, 3, 2, 4, 5, 10, 6, 0};
        int [] solution13V = {0, 7, 9, 8, 1, 5, 11, 6, 2, 3, 4, 12, 10, 0};

        solutions.add(solution3V);
        solutions.add(solution5V);
        solutions.add(solution7V);
        solutions.add(solution9V);
        solutions.add(solution11V);
        solutions.add(solution13V);
    }*/

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