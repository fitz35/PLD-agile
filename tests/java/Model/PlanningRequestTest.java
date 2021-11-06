package Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PlanningRequestTest {
    private PlanningRequest pr;
    private HashMap<String, Address> intAddMap;
    private HashMap<String, Request> reqMap;

    @BeforeEach
    void setUp(){
        Address add1 = new Address(0, 0, 0, 0, 0);
        Address add2 = new Address(1, 1, 1, 1, 1);
        Address add3 = new Address(2, 2, 2, 2, 2);
        Address add4 = new Address(3, 3, 3, 3, 1);
        Address add5 = new Address(4, 4, 4, 4, 2);

        Request req1 = new Request(add2, add3);
        Request req2 = new Request(add4, add5);
        reqMap = new HashMap<>();
        reqMap.put("req1", req1);
        reqMap.put("req2", req2);

        intAddMap = new HashMap<>();
        intAddMap.put("add1", add1);
        intAddMap.put("add2", add2);
        intAddMap.put("add3", add3);
        intAddMap.put("add4", add4);
        intAddMap.put("add5", add5);

        ArrayList<Request> rql = new ArrayList<>();
        rql.add(req1);
        rql.add(req2);
        pr = new PlanningRequest(rql, new Date(), add1);
    }

    @Test
    void getRequestByAddressTest(){
        assert(pr.getRequestByAddress(intAddMap.get("add2")).equals(pr.getRequestByAddress(intAddMap.get("add3"))));
        assert(pr.getRequestByAddress(intAddMap.get("add4")).equals(pr.getRequestByAddress(intAddMap.get("add5"))));
        assert(!pr.getRequestByAddress(intAddMap.get("add2")).equals(pr.getRequestByAddress(intAddMap.get("add4"))));
        assert(!pr.getRequestByAddress(intAddMap.get("add4")).equals(pr.getRequestByAddress(intAddMap.get("add2"))));
    }
}
