package Model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PlanningRequest {
    private ArrayList<Request> requestList;
    private Date departureTime;
    private Intersection startingPoint;

    public PlanningRequest() {
        requestList = new ArrayList<Request>();
    }

    public PlanningRequest(ArrayList<Request> requestArrayList, Date departureTime, Intersection startingPoint) {
        this.requestList = requestArrayList;
        this.departureTime = departureTime;
        this.startingPoint = startingPoint;
    }

    public void addRequest(Request newRequest) {
        requestList.add(newRequest);
    }

    public void setStartingPoint(Intersection startingPoint) {
        this.startingPoint = startingPoint;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }



    public ArrayList<Intersection> getIntersection() {
        //return all Intersection from the request and add the starting point address at the first place
        ArrayList<Intersection> intersection = new ArrayList<>(1 + requestList.size());
        intersection.add(startingPoint);

        for(Request req : requestList){
            intersection.add(req.getPickupAddress());
            intersection.add(req.getDeliveryAddress());
        }
        return intersection;
    }

    public int size(){
        return requestList.size();
    }

    public ArrayList<Request> getRequestList() {
        return requestList;
    }
    public Intersection getStartingPoint()
    {
        return startingPoint;
    }

    public ArrayList<Request> getRequestList() {
        return requestList;
    }

    public static void main(String[] args){
        PlanningRequest planning =new PlanningRequest();
    }


}
