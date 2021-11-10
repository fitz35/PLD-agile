package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
public class IntersectionTest {
    Intersection inter1;
    Intersection inter2;
    Intersection inter3;
    Intersection inter4;
    @BeforeEach
    void setUp(){
        inter1 = new Intersection(1, 1, 1);
        inter2 = new Intersection(2, 45, 45);
        inter3 = new Intersection(3, 30, 30);
        inter4 = new Intersection(4, -6, -6);
    }

    @Test
    void computeEllipticDistance(){
        int answer = 6516742; //Answer in meters (roof)
        int result = (int) Intersection.calculDis(inter1, inter2) + 1;//Approximation of the result
        assert(answer == result);
        answer = 4447706;
        result = (int) Intersection.calculDis(inter1, inter3) + 1;
        assert(answer == result);
        answer = 1099906;
        result = (int) Intersection.calculDis(inter1, inter4) + 1;
        assert(answer == result);
    }
}
