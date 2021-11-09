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

    @Test
    void getDistanceFromPointToSegmentTest1() {
        double res=MapPanel.getDistanceFromPointToSegment(8,6,1,2,4,5);
        System.out.println("Disance calculated : "+res);
        System.out.println("Disance expected : 2.12132032 (7 digits)");
        DecimalFormat df=new DecimalFormat("0.0000000");
        assertEquals(df.format(2.12132033),df.format(res));
    }
}