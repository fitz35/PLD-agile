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

    public Intersection[] getIntersectionId() {
        //return all Intersection from the request and add the starting point address at the first place
        Intersection[] intersectionId = new Intersection[1 + requestList.size()];
        intersectionId[0] = startingPoint;

        for(int i=1 ; i<1 + requestList.size() ; i++){
            intersectionId[i] = requestList.get(i-1).getPickupAddress();
            i++;
            intersectionId[i] = requestList.get(i-1).getDeliveryAddress();
        }
        return intersectionId;
    }

    public ArrayList<Request> getRequestList() {
        return requestList;
    }
    public Intersection getStartingPoint()
    {
        return startingPoint;
    }

    public static void main(String[] args){
        PlanningRequest planning =new PlanningRequest();
    }


}
