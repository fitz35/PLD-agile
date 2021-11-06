package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class AddressTest {

    private boolean exceptionRaised;
    private HashMap<String, Intersection> intAddMap;

    @BeforeEach
    void setUp(){
        Address add1 = new Address(1, 1, 1, 0, 0 /*for depot*/);
        Address add2 = new Address(2, 2, 2, 0, 1 /*for pickup*/);
        Address add3 = new Address(2, 2, 2, 0, 2 /*for delivery*/);

        Intersection int1 = new Intersection(2, 2, 2);

        intAddMap = new HashMap<>();

        intAddMap.put("add1", add1);
        intAddMap.put("add2", add2);
        intAddMap.put("add3", add3);
        intAddMap.put("int1", int1);
    }

    @Test
    void equalsTest(){
        assert(!(intAddMap.get("add1").equals(intAddMap.get("add2"))));
        assert(!(intAddMap.get("add2").equals(intAddMap.get("add3"))));
        assert((intAddMap.get("add2").equals(intAddMap.get("int1"))));
        assert((intAddMap.get("add3").equals(intAddMap.get("int1"))));
    }

    @Test
    void hashTest(){
        assert(intAddMap.get("add2").hashCode() == intAddMap.get("add3").hashCode());
        assert(intAddMap.get("add1").hashCode() != intAddMap.get("add2").hashCode());
        assert(intAddMap.get("add2").hashCode() == intAddMap.get("int1").hashCode());
    }


}
