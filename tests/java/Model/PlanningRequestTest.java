package Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlanningRequestTest {
    private PlanningRequest pr;
    private PlanningRequest pr0;
    private PlanningRequest pr1;
    private HashMap<String, Address> intAddMap;
    private HashMap<String, Request> reqMap;

    @BeforeEach
    void setUp(){
        Address add1 = new Address(0, 0, 0, 0, 0);
        Address add2 = new Address(1, 1, 1, 1, 1);
        Address add3 = new Address(2, 2, 2, 2, 2);
        Address add4 = new Address(3, 3, 3, 3, 1);
        Address add5 = new Address(4, 4, 4, 4, 2);
        Address add6 = new Address(5, 5, 5, 5, 1);
        Address add7 = new Address(6, 6, 6, 6, 2);

        Request req1 = new Request(add2, add3);
        Request req2 = new Request(add4, add5);
        Request req3 = new Request(add6, add7);
        reqMap = new HashMap<>();
        reqMap.put("req1", req1);
        reqMap.put("req2", req2);
        reqMap.put("req3", req3);

        intAddMap = new HashMap<>();
        intAddMap.put("add1", add1);
        intAddMap.put("add2", add2);
        intAddMap.put("add3", add3);
        intAddMap.put("add4", add4);
        intAddMap.put("add5", add5);
        intAddMap.put("add6", add6);
        intAddMap.put("add7", add7);

        ArrayList<Request> rql = new ArrayList<>();
        rql.add(req1);
        rql.add(req2);
        pr = new PlanningRequest(rql, new Date(), add1);
        rql = new ArrayList<>();
        rql.add(req1);
        rql.add(req2);
        pr0 = new PlanningRequest(rql, new Date(), add1);
        ArrayList<Request> rql1 = new ArrayList<>();
        rql1.add(req1);
        rql1.add(req2);
        rql1.add(req3);
        pr1 = new PlanningRequest(rql1, new Date(), add1);
    }

    @Test
    void getRequestByAddressTest(){
        assert(pr.getRequestByAddress(intAddMap.get("add2")).equals(pr.getRequestByAddress(intAddMap.get("add3"))));
        assert(pr.getRequestByAddress(intAddMap.get("add4")).equals(pr.getRequestByAddress(intAddMap.get("add5"))));
        assert(!pr.getRequestByAddress(intAddMap.get("add2")).equals(pr.getRequestByAddress(intAddMap.get("add4"))));
        assert(!pr.getRequestByAddress(intAddMap.get("add4")).equals(pr.getRequestByAddress(intAddMap.get("add2"))));
    }

    @Test
    void testAddThenRemoveRequest(){
        pr.addRequest(reqMap.get("req3"));
        assertEquals(pr.getRequestList(), pr1.getRequestList());
        pr.removeRequest(reqMap.get("req3"));
        assertEquals(pr.getRequestList(), pr0.getRequestList());
    }
}
