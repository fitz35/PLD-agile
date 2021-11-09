package ihm.windowMap;

import Model.Intersection;
import Model.Map;
import ihm.windowMap.MapPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

import static org.junit.jupiter.api.Assertions.*;

class MapPanelTest extends Observable {
    private Map map;
    private boolean exceptionRaised;
    private Observer observer;
    private boolean updateCalled;

    @BeforeEach
    void setUp() {
        map= new Map();
        exceptionRaised=false;
        observer=new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                updateCalled=true;
            }
        };
        map.addObserver(observer);
        /*try {
            map.loadMap("tests/ressource/mapTour.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    /**
     * Test the distance when the point is above the segment
     */
    @Test
    void getDistanceFromPointToSegmentTest1() {
        double resCalculated=MapPanel.getDistanceFromPointToSegment(8,6,1,2,4,5);
        double resTheorical=2.121320344;
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