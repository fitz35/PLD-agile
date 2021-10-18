package Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class Map {
    private ArrayList<Segment> segmentList;
    private ArrayList<Intersection> intersectionList;
    private PlanningRequest planningRequest;

    public Map() {
        segmentList = new ArrayList<Segment>();
        intersectionList = new ArrayList<Intersection>();
        planningRequest = new PlanningRequest();
    }

    public void loadMap(String fileName)
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
            NodeList nodeListIntersection = doc.getElementsByTagName("intersection");

            for (int temp = 0; temp < nodeListIntersection.getLength(); temp++) {
                Node node = nodeListIntersection.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // get intersection's attribute
                    long id =  Long.parseLong(element.getAttribute("id"));
                    double latitude = Double.parseDouble(element.getAttribute("latitude"));
                    double longitude = Double.parseDouble(element.getAttribute("longitude"));
                    System.out.println("unique ?"+checkUniqueIntersection(id,latitude,longitude));
                    System.out.println("Intersection: "+id+"; latitude:"+latitude+"; longitude:"+longitude);

                    if(checkUniqueIntersection(id,latitude,longitude)){
                        intersectionList.add(new Intersection(id,latitude,longitude));
                    }else{
                        System.out.println("already in the list");
                    }
                }
            }

            // get all nodes <Segment>
            NodeList nodeListSegment = doc.getElementsByTagName("segment");

            for (int temp = 0; temp < nodeListSegment.getLength(); temp++) {
                Node node = nodeListSegment.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // get intersection's attribute
                    long destinationId =  Long.parseLong(element.getAttribute("destination"));
                    long originId =  Long.parseLong(element.getAttribute("origin"));
                    double length = Double.parseDouble(element.getAttribute("length"));
                    String name = element.getAttribute("name");

                    Intersection origin = getIntersectionById(originId);
                    Intersection destination = getIntersectionById(destinationId);
                     if(origin != null && destination != null){
                         segmentList.add(new Segment(origin,destination,name,length));
                     }else{
                         System.out.println("segment creation is impossible");
                     }
                    System.out.println("segment: origin:"+originId+"; destination:"+destinationId+"; length:"+length+"; name:"+name);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    // Check if the data provided correspond to an existant intersection in the list
    public boolean checkUniqueIntersection(long intersectionId,double latitude,double longitude)
    {
        boolean res=true;
        for(Intersection intersection:intersectionList)
        {
            if( intersection.getId() == intersectionId &&
                intersection.getLatitude()== latitude &&
                intersection.getLongitude()==longitude)
            {
                res=false;
            }
        }
        return res;
    }

    // Return the intersection corresponding to the id
    public Intersection getIntersectionById(long intersectionId)
    {
        for(Intersection intersection : intersectionList)
        {
            if(intersection.getId() == intersectionId )
            {
                return intersection;
            }
        }
        return null;
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
                    long pickupAddressId = Long.parseLong(element.getAttribute("pickupAddress"));
                    Intersection pickupAddress = getIntersectionById(pickupAddressId);
                    long deliveryAddressId = Long.parseLong(element.getAttribute("deliveryAddress"));
                    Intersection deliveryAddress = getIntersectionById(deliveryAddressId);
                    int pickupDuration = Integer.parseInt(element.getAttribute("pickupDuration"));
                    int deliveryDuration = Integer.parseInt(element.getAttribute("deliveryDuration"));

                    System.out.println("Existing address ?");
                    System.out.println("Request: pickupAddress:" + pickupAddressId + "; deliveryAddress:" + deliveryAddressId + "; pickupDuration: " + pickupDuration+" deliveryDuration: " + deliveryDuration + ";");

                    planningRequest.addRequest(new Request(pickupAddress, pickupDuration, deliveryAddress, deliveryDuration));
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

                    planningRequest.setStartingPoint(address);
                    planningRequest.setDepartureTime(new SimpleDateFormat("HH:mm:ss").parse(departTime));

                    System.out.println("Depot: Starting point: "+address+" ; departureTime: "+departTime+";");
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        Map map=new Map();
        map.loadMap("./data/fichiersXML2020/smallMap.xml");
        PlanningRequest planning = new PlanningRequest();
        map.loadMap("./data/fichiersXML2020/requestsMedium5.xml");
        System.out.println("passé");
    }

}
