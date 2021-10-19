package tests;

import Model.Intersection;
import Model.Map;
import ihm.windowMap.MapPanel;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MapInterfaceTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {

    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void loadMap() {
    }

    @org.junit.jupiter.api.Test
    void getIntersectionById() {
    }

    @org.junit.jupiter.api.Test
    void getIntersectionList() {
    }

    @org.junit.jupiter.api.Test
    void getSegmentList() {
    }

    @org.junit.jupiter.api.Test
    void calculDis() {
        Intersection i1 = new Intersection(1, 50.0663889, 5.714722222222222);
        Intersection i2 = new Intersection(2, 58.6438889, 3.0700000000000003);

        assertEquals(968853.5467049106,Intersection.calculDis(i1,i2));
    }

    @org.junit.jupiter.api.Test
    void latLonToOffsets() {
    }
}