package Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class TourTest {

    private boolean exceptionRaised;
    private HashMap<String, Address> intAddMap;
    private LinkedList<Path> llpath;
    private Tour tour;

    @BeforeEach
    void setUp(){
        Address add1 = new Address(0, 0, 0, 0, 0);
        Address add2 = new Address(1, 1, 1, 1, 1);
        Address add3 = new Address(2, 2, 2, 2, 2);
        Address add4 = new Address(3, 3, 3, 3, 1);
        Address add5 = new Address(4, 4, 4, 4, 2);

        Segment seg1 = new Segment(add1, add2, "", 1);
        Segment seg2 = new Segment(add2, add3, "", 2);
        Segment seg3 = new Segment(add3, add4, "", 3);
        Segment seg4 = new Segment(add4, add5, "", 4);
        Segment seg5 = new Segment(add5, add1, "", 4);

        LinkedList<Segment> ll = new LinkedList<>();
        ll.add(seg1);
        Path pth1 = new Path(add1, add2, ll);

        ll = new LinkedList<>();
        ll.add(seg2);
        Path pth2 = new Path(add2, add3, ll);

        ll = new LinkedList<>();
        ll.add(seg3);
        Path pth3 = new Path(add3, add4, ll);

        ll = new LinkedList<>();
        ll.add(seg4);
        Path pth4 = new Path(add4, add5, ll);

        ll = new LinkedList<>();
        ll.add(seg5);
        Path pth5 = new Path(add5, add1, ll);

        intAddMap = new HashMap<>();
        intAddMap.put("add1", add1);
        intAddMap.put("add2", add2);
        intAddMap.put("add3", add3);
        intAddMap.put("add4", add4);
        intAddMap.put("add5", add5);

        llpath = new LinkedList<>();
        llpath.add(pth1);
        llpath.add(pth2);
        llpath.add(pth3);
        llpath.add(pth4);
        llpath.add(pth5);

        tour = new Tour(llpath);

    }

    @Test
    void getPath(){
        assert(tour.findPathDestination(intAddMap.get("add2")).equals(tour.findPathOrigin(intAddMap.get("add1"))));
        assert(tour.findPathOrigin(intAddMap.get("add1")).equals(tour.findPathDestination(intAddMap.get("add2"))));
        assert(!tour.findPathDestination(intAddMap.get("add2")).equals(tour.findPathOrigin(intAddMap.get("add3"))));
    }
}
