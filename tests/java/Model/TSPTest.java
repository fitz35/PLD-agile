package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tsp.Graph;

import java.util.ArrayList;

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
}
