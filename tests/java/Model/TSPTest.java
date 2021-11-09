package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tsp.CompleteGraph;
import tsp.Graph;
import tsp.TSP;
import tsp.TSP1;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TSPTest {
    private Graph completeGraph;
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
    }

    @Test
    void TSPTest(){
        int problemIndex = 0;
        for (int nbVertices = 3; nbVertices <= 13; nbVertices += 2){
            TSP tsp = new TSP1();
            System.out.println("Graphs with "+nbVertices+" vertices:");
            Graph g = new CompleteGraph(nbVertices);
            long startTime = System.currentTimeMillis();
            tsp.searchSolution(200000000, g);
            System.out.print("Solution of cost "+tsp.getSolutionCost()+" found in "
                    +(System.currentTimeMillis() - startTime)+"ms : ");
            for (int i=0; i<nbVertices; i++){
                System.out.print(tsp.getSolution(i)+" ");
                assertEquals(solutions.get(problemIndex)[i], tsp.getSolution(i));
            }
            System.out.println("0");
            problemIndex++;
        }
    }
}
