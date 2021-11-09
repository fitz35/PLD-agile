package ihm.windowMap;

import org.junit.jupiter.api.Test;
import java.text.DecimalFormat;

import static org.junit.jupiter.api.Assertions.*;

class MapPanelTest {

    /**
     * Test the distance when the point is above the segment
     */
    @Test
    void getDistanceFromPointToSegmentTest1() {
        double resCalculated=MapPanel.getDistanceFromPointToSegment(2,7,1,2,4,5);
        double resTheorical=2.82842712474619;
        System.out.println("Distance calculated : "+resCalculated);
        System.out.println("Distance expected : "+resTheorical);
        DecimalFormat df=new DecimalFormat("0.000000000");
        assertEquals(df.format(resTheorical),df.format(resCalculated));
    }

    /**
     * Test the distance when the point is on the left of a horizontal segment
     */
    @Test
    void getDistanceFromPointToSegmentTest2() {
        double resCalculated=MapPanel.getDistanceFromPointToSegment(1,2,6,0,9,0);
        double resTheorical=5.38516480713450000;
        System.out.println("Distance calculated : "+resCalculated);
        System.out.println("Distance expected : "+resTheorical);
        DecimalFormat df=new DecimalFormat("0.000000000");
        assertEquals(df.format(resTheorical),df.format(resCalculated));
    }

    /**
     * Test the distance when the point is on the right of a horizontal segment
     */
    @Test
    void getDistanceFromPointToSegmentTest3() {
        double resCalculated=MapPanel.getDistanceFromPointToSegment(3,12,6,0,9,0);
        double resTheorical=12.36931687685300000;
        System.out.println("Distance calculated : "+resCalculated);
        System.out.println("Distance expected : "+resTheorical);
        DecimalFormat df=new DecimalFormat("0.000000000");
        assertEquals(df.format(resTheorical),df.format(resCalculated));
    }

    /**
     * Test the distance when the point is on the left of a vertical segment
     */
    @Test
    void getDistanceFromPointToSegmentTest4() {
        double resCalculated=MapPanel.getDistanceFromPointToSegment(1,4,4,2,4,0);
        double resTheorical=3.60555127546399000;
        System.out.println("Distance calculated : "+resCalculated);
        System.out.println("Distance expected : "+resTheorical);
        DecimalFormat df=new DecimalFormat("0.000000000");
        assertEquals(df.format(resTheorical),df.format(resCalculated));
    }

    /**
     * Test the distance when the point is on the right of a vertical segment
     */
    @Test
    void getDistanceFromPointToSegmentTest5() {
        double resCalculated=MapPanel.getDistanceFromPointToSegment(1,-2,4,2,4,0);
        double resTheorical=3.60555127546399000;
        System.out.println("Distance calculated : "+resCalculated);
        System.out.println("Distance expected : "+resTheorical);
        DecimalFormat df=new DecimalFormat("0.000000000");
        assertEquals(df.format(resTheorical),df.format(resCalculated));
    }
}