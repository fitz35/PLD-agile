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
    private ArrayList<Request> requestList; //turn into Long
    private Date departureTime;
    private long startingPoint; //turn into intersection

    public PlanningRequest() {
        requestList = new ArrayList<Request>();
    }

    public PlanningRequest(ArrayList<Request> requestArrayList, Date departureTime, long startingPoint) {
        this.requestList = requestArrayList;
        this.departureTime = departureTime;
        this.startingPoint = startingPoint;
    }

    public void addRequest(Request newRequest) {
        requestList.add(newRequest);
    }

    public void setStartingPoint(long startingPoint) {
        this.startingPoint = startingPoint;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public static void main(String[] args){
        PlanningRequest planning =new PlanningRequest();
    }


}
