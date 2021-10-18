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


    public void loadRequest(String fileName)
    {
        //Test extension of XML file name
        String[] words = fileName.split(".");
        assert(words[(words.length)-1].equals("XML") || words[(words.length)-1].equals("xml"));

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(fileName));
            doc.getDocumentElement().normalize();

            // get all nodes <intersection>
            NodeList nodeListRequest = doc.getElementsByTagName("request");

            for (int temp = 0; temp < nodeListRequest.getLength(); temp++) {
                Node node = nodeListRequest.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // get request's attribute
                    long pickupAddress = Long.parseLong(element.getAttribute("pickupAddress"));
                    long deliveryAddress = Long.parseLong(element.getAttribute("deliveryAddress"));
                    int pickupDuration = Integer.parseInt(element.getAttribute("pickupDuration"));
                    int deliveryDuration = Integer.parseInt(element.getAttribute("deliveryDuration"));


                    System.out.println("Existing address ?");
                    System.out.println("Request: pickupAddress:" + pickupAddress + "; deliveryAddress:" + deliveryAddress + "; pickupDuration: " + pickupDuration+" deliveryDuration: " + deliveryDuration + ";");

                    requestList.add(new Request(pickupAddress, pickupDuration, deliveryAddress, deliveryDuration));
                }
            }

            // get the depot
            NodeList nodeListDepot = doc.getElementsByTagName("depot");
            for (int temp = 0; temp < nodeListDepot.getLength(); temp++) {
                Node node = nodeListDepot.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // get request's attribute
                    long address = Long.parseLong(element.getAttribute("address"));
                    String departTime = element.getAttribute("departureTime");

                    startingPoint = address;
                    departureTime = new SimpleDateFormat("HH:mm:ss").parse(departTime);

                    System.out.println("Depot: Starting point: "+startingPoint+" ; departureTime: "+departTime+";");
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        PlanningRequest planning =new PlanningRequest();
        planning.loadRequest("./data/fichiersXML2020/requestsSmall1.xml");
    }


}
