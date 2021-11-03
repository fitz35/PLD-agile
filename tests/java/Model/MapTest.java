package Model;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {
    private Map map;
    private boolean exceptionRaised;
    @BeforeEach
    void setUp() {
        map= new Map();
        exceptionRaised=false;
    }
    @Test
    void loadMap() {
        System.out.println("test");
    }



    @Test
    void loadMapTest1(){
        System.out.println("loadMapTest1 : Correct map");
        try{
            map.loadMap("tests/ressource/map1.xml");
        }catch(Exception e){
            exceptionRaised=true;
        }
        Intersection intersectionTest1 = new Intersection(26033277,45.756165,4.8574095);
        Intersection intersectionTest2 = new Intersection(975886496,45.756874,4.8574047);
        Segment segmentTest=new Segment(intersectionTest2,intersectionTest1,"rue Danton",78.72686);
        assert(!exceptionRaised);
    }
}